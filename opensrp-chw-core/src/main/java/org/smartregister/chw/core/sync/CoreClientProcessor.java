package org.smartregister.chw.core.sync;

import android.content.ContentValues;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.util.DBConstants;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.dao.ChildDao;
import org.smartregister.chw.core.dao.ChwNotificationDao;
import org.smartregister.chw.core.dao.EventDao;
import org.smartregister.chw.core.domain.MonthlyTally;
import org.smartregister.chw.core.domain.StockUsage;
import org.smartregister.chw.core.model.CommunityResponderModel;
import org.smartregister.chw.core.repository.CommunityResponderRepository;
import org.smartregister.chw.core.repository.MonthlyTalliesRepository;
import org.smartregister.chw.core.repository.StockUsageReportRepository;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.CoreReferralUtils;
import org.smartregister.chw.core.utils.ReportUtils;
import org.smartregister.chw.core.utils.StockUsageReportUtils;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.fp.util.FamilyPlanningConstants;
import org.smartregister.chw.fp.util.FpUtil;
import org.smartregister.chw.malaria.util.Constants;
import org.smartregister.chw.malaria.util.MalariaUtil;
import org.smartregister.clientandeventmodel.DateUtil;
import org.smartregister.commonregistry.AllCommonsRepository;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Client;
import org.smartregister.domain.Event;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.jsonmapping.ClientClassification;
import org.smartregister.domain.jsonmapping.Column;
import org.smartregister.domain.jsonmapping.Table;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.sync.ClientProcessorForJava;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class CoreClientProcessor extends ClientProcessorForJava {

    private ClientClassification classification;
    private Table vaccineTable;
    private Table serviceTable;

    protected CoreClientProcessor(Context context) {
        super(context);
    }

    public static ClientProcessorForJava getInstance(Context context) {
        if (instance == null) {
            instance = new CoreClientProcessor(context);
        }
        return instance;
    }

    public static void addVaccine(VaccineRepository vaccineRepository, Vaccine vaccine) {
        try {
            if (vaccineRepository == null || vaccine == null) {
                return;
            }

            // if its an updated vaccine, delete the previous object
            vaccineRepository.deleteVaccine(vaccine.getBaseEntityId(), vaccine.getName());

            // Add the vaccine
            vaccineRepository.add(vaccine);

            String name = vaccine.getName();
            if (StringUtils.isBlank(name)) {
                return;
            }

            // Update vaccines in the same group where either can be given
            // For example measles 1 / mr 1
            name = VaccineRepository.removeHyphen(name);
            String ftsVaccineName = null;

            if (VaccineRepo.Vaccine.measles1.display().equalsIgnoreCase(name)) {
                ftsVaccineName = VaccineRepo.Vaccine.mr1.display();
            } else if (VaccineRepo.Vaccine.mr1.display().equalsIgnoreCase(name)) {
                ftsVaccineName = VaccineRepo.Vaccine.measles1.display();
            } else if (VaccineRepo.Vaccine.measles2.display().equalsIgnoreCase(name)) {
                ftsVaccineName = VaccineRepo.Vaccine.mr2.display();
            } else if (VaccineRepo.Vaccine.mr2.display().equalsIgnoreCase(name)) {
                ftsVaccineName = VaccineRepo.Vaccine.measles2.display();
            }

            if (ftsVaccineName != null) {
                ftsVaccineName = VaccineRepository.addHyphen(ftsVaccineName.toLowerCase());
                Vaccine ftsVaccine = new Vaccine();
                ftsVaccine.setBaseEntityId(vaccine.getBaseEntityId());
                ftsVaccine.setName(ftsVaccineName);
                vaccineRepository.updateFtsSearch(ftsVaccine);
            }

        } catch (Exception e) {
            Timber.e(e);
        }

    }

    @Override
    public synchronized void processClient(List<EventClient> eventClients) throws Exception {

        ClientClassification clientClassification = getClassification();
        Table vaccineTable = getVaccineTable();
        Table serviceTable = getServiceTable();

        if (!eventClients.isEmpty()) {
            for (EventClient eventClient : eventClients) {
                Event event = eventClient.getEvent();
                if (event == null) {
                    return;
                }

                String eventType = event.getEventType();
                if (eventType == null) {
                    continue;
                }

                processEvents(clientClassification, vaccineTable, serviceTable, eventClient, event, eventType);
            }

        }
    }

    private ClientClassification getClassification() {
        if (classification == null) {
            classification = assetJsonToJava("ec_client_classification.json", ClientClassification.class);
        }
        return classification;
    }

    private Table getVaccineTable() {
        if (vaccineTable == null) {
            vaccineTable = assetJsonToJava("ec_client_vaccine.json", Table.class);
        }
        return vaccineTable;
    }

    private Table getServiceTable() {
        if (serviceTable == null) {
            serviceTable = assetJsonToJava("ec_client_service.json", Table.class);
        }
        return serviceTable;
    }

    protected void processEvents(ClientClassification clientClassification, Table vaccineTable, Table serviceTable, EventClient eventClient, Event event, String eventType) throws Exception {
        switch (eventType) {
            case VaccineIntentService.EVENT_TYPE:
            case VaccineIntentService.EVENT_TYPE_OUT_OF_CATCHMENT:
                if (vaccineTable == null) {
                    return;
                }
                processVaccine(eventClient, vaccineTable, eventType.equals(VaccineIntentService.EVENT_TYPE_OUT_OF_CATCHMENT));
                break;
            case RecurringIntentService.EVENT_TYPE:
                if (serviceTable == null) {
                    return;
                }
                processService(eventClient, serviceTable);
                break;
            case CoreConstants.EventType.CHILD_HOME_VISIT:
                processVisitEvent(Utils.processOldEvents(eventClient), CoreConstants.EventType.CHILD_HOME_VISIT);
                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                break;
            case CoreConstants.EventType.CHILD_VISIT_NOT_DONE:
            case CoreConstants.EventType.WASH_CHECK:
            case CoreConstants.EventType.FAMILY_KIT:
            case CoreConstants.EventType.ROUTINE_HOUSEHOLD_VISIT:
                processVisitEvent(eventClient);
                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                break;
            case CoreConstants.EventType.MINIMUM_DIETARY_DIVERSITY:
            case CoreConstants.EventType.MUAC:
            case CoreConstants.EventType.LLITN:
            case CoreConstants.EventType.ECD:
            case CoreConstants.EventType.DEWORMING:
            case CoreConstants.EventType.VITAMIN_A:
            case CoreConstants.EventType.EXCLUSIVE_BREASTFEEDING:
            case CoreConstants.EventType.MNP:
            case CoreConstants.EventType.IPTP_SP:
            case CoreConstants.EventType.TT:
            case CoreConstants.EventType.VACCINE_CARD_RECEIVED:
            case CoreConstants.EventType.DANGER_SIGNS_BABY:
            case CoreConstants.EventType.PNC_HEALTH_FACILITY_VISIT:
            case CoreConstants.EventType.KANGAROO_CARE:
            case CoreConstants.EventType.UMBILICAL_CORD_CARE:
            case CoreConstants.EventType.IMMUNIZATION_VISIT:
            case CoreConstants.EventType.OBSERVATIONS_AND_ILLNESS:
            case CoreConstants.EventType.SICK_CHILD:
                processVisitEvent(eventClient, CoreConstants.EventType.CHILD_HOME_VISIT);
                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                break;
            case CoreConstants.EventType.ANC_HOME_VISIT:
            case org.smartregister.chw.anc.util.Constants.EVENT_TYPE.ANC_HOME_VISIT_NOT_DONE:
            case org.smartregister.chw.anc.util.Constants.EVENT_TYPE.ANC_HOME_VISIT_NOT_DONE_UNDO:
            case CoreConstants.EventType.PNC_HOME_VISIT:
            case CoreConstants.EventType.PNC_HOME_VISIT_NOT_DONE:
            case FamilyPlanningConstants.EventType.FP_FOLLOW_UP_VISIT:
            case FamilyPlanningConstants.EventType.FAMILY_PLANNING_REGISTRATION:
            case org.smartregister.chw.tb.util.Constants.EventType.FOLLOW_UP_VISIT:
            case org.smartregister.chw.hiv.util.Constants.EventType.FOLLOW_UP_VISIT:
                if (eventClient.getEvent() == null) {
                    return;
                }
                processVisitEvent(eventClient);
                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                break;
            case CoreConstants.EventType.REMOVE_FAMILY:
                if (eventClient.getClient() == null) {
                    return;
                }
                processRemoveFamily(eventClient.getClient().getBaseEntityId(), event.getEventDate().toDate());
                break;
            case CoreConstants.EventType.REMOVE_MEMBER:
                if (eventClient.getClient() == null) {
                    return;
                }
                processRemoveMember(eventClient.getClient().getBaseEntityId(), event.getEventDate().toDate());
                break;
            case FamilyPlanningConstants.EventType.FAMILY_PLANNING_CHANGE_METHOD:
                clientProcessByObs(eventClient, clientClassification, event, "reason_stop_fp_chw", "decided_to_change_method");
                break;
            case Constants.EVENT_TYPE.MALARIA_FOLLOW_UP_VISIT:
                clientProcessByObs(eventClient, clientClassification, event, "fever_still", "Yes");
                if (eventClient.getClient() == null) {
                    return;
                }

                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                List<Obs> observations = event.getObs();
                for (Obs obs : observations) {
                    if (obs.getFormSubmissionField().equals("reason_stop_fp_chw") && !obs.getHumanReadableValues().get(0).equals("decided_to_change_method")) {
                        processVisitEvent(eventClient);
                        FpUtil.processChangeFpMethod(eventClient.getClient().getBaseEntityId());
                        break;
                    }
                }
                break;
            case CoreConstants.EventType.STOCK_USAGE_REPORT:
                clientProcessStockEvent(event);
                break;
            case CoreConstants.EventType.REMOVE_COMMUNITY_RESPONDER:
                CommunityResponderRepository repo = CoreChwApplication.getInstance().communityResponderRepository();
                repo.purgeCommunityResponder(event.getBaseEntityId());
                break;
            case CoreConstants.EventType.COMMUNITY_RESPONDER_REGISTRATION:
                clientProcessCommunityResponderEvent(event);
                break;
            case CoreConstants.EventType.CHW_IN_APP_REPORT_EVENT:
                clientProcessInAppReportingEvent(event);
                break;
            case CoreConstants.EventType.HF_IN_APP_REPORT_EVENT:
                clientProcessHfInAppReportingEvent(event);
                break;
            case CoreConstants.EventType.REMOVE_CHILD:
                if (eventClient.getClient() == null) {
                    return;
                }
                processRemoveChild(eventClient.getClient().getBaseEntityId(), event.getEventDate().toDate());
                break;
            case CoreConstants.EventType.CHILD_VACCINE_CARD_RECEIVED:
                if (eventClient.getClient() == null) {
                    return;
                }
                processVisitEvent(eventClient);
                processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                break;
            case CoreConstants.EventType.CHILD_REFERRAL:
            case CoreConstants.EventType.ANC_REFERRAL:
            case CoreConstants.EventType.PNC_REFERRAL:
            case CoreConstants.EventType.CLOSE_REFERRAL:
            case CoreConstants.EventType.FAMILY_PLANNING_REFERRAL:
            case CoreConstants.EventType.MALARIA_REFERRAL:
                if (eventClient.getClient() != null) {
                    processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                    org.smartregister.util.Utils.startAsyncTask(new MalariaUtil.CloseMalariaMemberFromRegister(event.getBaseEntityId()), null);
                }
                break;
            case CoreConstants.EventType.REFERRAL_DISMISSAL:
                if (eventClient.getClient() != null) {
                    processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                    CoreReferralUtils.completeClosedReferralTasks();
                }
                break;
            case org.smartregister.chw.anc.util.Constants.EVENT_TYPE.DELETE_EVENT:
                processDeleteEvent(eventClient.getEvent());
                break;
            case CoreConstants.EventType.ANC_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.PNC_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.MALARIA_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.SICK_CHILD_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.FAMILY_PLANNING_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.HIV_NOTIFICATION_DISMISSAL:
            case CoreConstants.EventType.TB_NOTIFICATION_DISMISSAL:
                processNotificationDismissalEvent(eventClient.getEvent());
                break;
            default:
                if (eventClient.getClient() != null) {
                    if (eventType.equals(CoreConstants.EventType.UPDATE_FAMILY_RELATIONS) && event.getEntityType().equalsIgnoreCase(CoreConstants.TABLE_NAME.FAMILY_MEMBER)) {
                        event.setEventType(CoreConstants.EventType.UPDATE_FAMILY_MEMBER_RELATIONS);
                    }
                    processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
                }
                break;
        }
    }

    public void processDeleteEvent(Event event) {
        try {
            // delete from vaccine table
            EventDao.deleteVaccineByFormSubmissionId(event.getFormSubmissionId());
            // delete from visit table
            EventDao.deleteVisitByFormSubmissionId(event.getFormSubmissionId());
            // delete from recurring service table
            EventDao.deleteServiceByFormSubmissionId(event.getFormSubmissionId());

            Timber.d("Ending processDeleteEvent: %s", event.getEventId());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public boolean saveReportDateSent() {
        return true;
    }

    public boolean processHfReportEvents() {
        return false;
    }

    private void clientProcessInAppReportingEvent(Event event) {
        List<Obs> reportObs = event.getObs();
        MonthlyTally report = ReportUtils.getMonthlyTallyFromObs(reportObs);
        if (report != null) {
            MonthlyTalliesRepository monthlyTalliesRepository = CoreChwApplication.getInstance().monthlyTalliesRepository();
            String formSubmissionId = event.getFormSubmissionId();
            report.setSubmissionId(formSubmissionId);
            report.setProviderId(event.getProviderId());
            if (!saveReportDateSent() && event.getEventType().equals(CoreConstants.EventType.CHW_IN_APP_REPORT_EVENT))
                report.setDateSent(null);
            monthlyTalliesRepository.save(report);
        }
    }

    private void clientProcessHfInAppReportingEvent(Event event) {
        if (processHfReportEvents())
            clientProcessInAppReportingEvent(event);
    }

    private void clientProcessStockEvent(Event event) {
        List<Obs> stockObs = event.getObs();
        StockUsage usage = ReportUtils.getStockUsageFromObs(stockObs);
        if (usage != null) {
            StockUsageReportRepository repo = CoreChwApplication.getInstance().getStockUsageRepository();
            String formSubmissionId = event.getFormSubmissionId();
            usage.setId(formSubmissionId);
            repo.addOrUpdateStockUsage(usage);
        }
    }

    private void processNotificationDismissalEvent(Event event) {
        List<Obs> notificationObs = event.getObs();
        String notificationId = null;
        String dateMarkedAsDone = null;
        if (notificationObs.size() > 0) {
            for (Obs obs : notificationObs) {
                if (CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.NOTIFICATION_ID.equals(obs.getFormSubmissionField())) {
                    notificationId = (String) obs.getValue();
                } else if (CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.DATE_NOTIFICATION_MARKED_AS_DONE.equals(obs.getFormSubmissionField())) {
                    dateMarkedAsDone = (String) obs.getValue();
                }
            }
            if (StringUtils.isBlank(dateMarkedAsDone)) {
                dateMarkedAsDone = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(event.getDateCreated().toDate());
            }
            ChwNotificationDao.markNotificationAsDone(getContext(), notificationId, event.getEntityType(), dateMarkedAsDone);
        }
    }

    private void clientProcessByObs(EventClient eventClient, ClientClassification clientClassification, Event event, String formSubmissionField, String humanReadableValues) {
        if (eventClient.getClient() == null) {
            return;
        }
        try {
            processEvent(eventClient.getEvent(), eventClient.getClient(), clientClassification);
            List<Obs> observations = event.getObs();
            for (Obs obs : observations) {
                if (obs.getFormSubmissionField().equals(formSubmissionField) && !obs.getHumanReadableValues().get(0).equals(humanReadableValues)) {
                    if (event.getEventType().equals(FamilyPlanningConstants.EventType.FAMILY_PLANNING_CHANGE_METHOD)) {
                        FpUtil.processChangeFpMethod(eventClient.getClient().getBaseEntityId());
                    } else if (event.getEventType().equals(Constants.EVENT_TYPE.MALARIA_FOLLOW_UP_VISIT)) {
                        org.smartregister.util.Utils.startAsyncTask(new MalariaUtil.CloseMalariaMemberFromRegister(event.getBaseEntityId()), null);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Timber.d(e);
        }
    }

    // possible to delegate
    private Boolean processVaccine(EventClient vaccine, Table vaccineTable, boolean outOfCatchment) {

        try {
            if (vaccine == null || vaccine.getEvent() == null) {
                return false;
            }

            if (vaccineTable == null) {
                return false;
            }

            Timber.d("Starting processVaccine table: %s", vaccineTable.name);

            ContentValues contentValues = processCaseModel(vaccine, vaccineTable);

            // updateFamilyRelations the values to db
            if (contentValues != null && contentValues.size() > 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(contentValues.getAsString(VaccineRepository.DATE));

                VaccineRepository vaccineRepository = CoreChwApplication.getInstance().vaccineRepository();
                Vaccine vaccineObj = new Vaccine();
                vaccineObj.setBaseEntityId(contentValues.getAsString(VaccineRepository.BASE_ENTITY_ID));
                vaccineObj.setName(contentValues.getAsString(VaccineRepository.NAME));
                if (contentValues.containsKey(VaccineRepository.CALCULATION)) {
                    vaccineObj.setCalculation(parseInt(contentValues.getAsString(VaccineRepository.CALCULATION)));
                }
                vaccineObj.setDate(date);
                vaccineObj.setAnmId(contentValues.getAsString(VaccineRepository.ANMID));
                vaccineObj.setLocationId(contentValues.getAsString(VaccineRepository.LOCATION_ID));
                vaccineObj.setSyncStatus(VaccineRepository.TYPE_Synced);
                vaccineObj.setFormSubmissionId(vaccine.getEvent().getFormSubmissionId());
                vaccineObj.setEventId(vaccine.getEvent().getEventId());
                vaccineObj.setOutOfCatchment(outOfCatchment ? 1 : 0);
                vaccineObj.setProgramClientId(getVaccineProgramClient(vaccine));

                String createdAtString = contentValues.getAsString(VaccineRepository.CREATED_AT);
                Date createdAt = getDate(createdAtString);
                vaccineObj.setCreatedAt(createdAt);

                addVaccine(vaccineRepository, vaccineObj);

                Timber.d("Ending processVaccine table: %s", vaccineTable.name);
            }
            return true;

        } catch (Exception e) {

            Timber.e(e, "Process Vaccine Error");
            return null;
        }
    }

    private String getVaccineProgramClient(EventClient eventClient) {
        Map<String, String> details = eventClient.getEvent().getDetails();
        return details != null ? details.get(IMConstants.VaccineEvent.PROGRAM_CLIENT_ID) : null;
    }

    // possible to delegate
    private Boolean processService(EventClient service, Table serviceTable) {

        try {

            if (service == null || service.getEvent() == null) {
                return false;
            }

            if (serviceTable == null) {
                return false;
            }

            Timber.d("Starting processService table: %s", serviceTable.name);

            ContentValues contentValues = processCaseModel(service, serviceTable);

            // updateFamilyRelations the values to db
            if (contentValues != null && contentValues.size() > 0) {
                String name = contentValues.getAsString(RecurringServiceTypeRepository.NAME);

                if (StringUtils.isNotBlank(name)) {
                    name = name.replaceAll("_", " ").replace("dose", "").trim();
                }


                String eventDateStr = contentValues.getAsString(RecurringServiceRecordRepository.DATE);
                Date date = getDate(eventDateStr);
                String value = null;

                if (StringUtils.containsIgnoreCase(name, "Exclusive breastfeeding")) {
                    value = contentValues.getAsString(RecurringServiceRecordRepository.VALUE);
                }

                RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
                List<ServiceType> serviceTypeList = recurringServiceTypeRepository.searchByName(name);
                if (serviceTypeList == null || serviceTypeList.isEmpty()) {
                    return false;
                }

                if (date == null) {
                    return false;
                }

                RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
                ServiceRecord serviceObj = new ServiceRecord();
                serviceObj.setBaseEntityId(contentValues.getAsString(RecurringServiceRecordRepository.BASE_ENTITY_ID));
                serviceObj.setName(name);
                serviceObj.setDate(date);
                serviceObj.setAnmId(contentValues.getAsString(RecurringServiceRecordRepository.ANMID));
                serviceObj.setLocationId(contentValues.getAsString(RecurringServiceRecordRepository.LOCATION_ID));
                serviceObj.setSyncStatus(RecurringServiceRecordRepository.TYPE_Synced);
                serviceObj.setFormSubmissionId(service.getEvent().getFormSubmissionId());
                serviceObj.setEventId(service.getEvent().getEventId()); //FIXME hard coded id
                serviceObj.setValue(value);
                serviceObj.setRecurringServiceId(serviceTypeList.get(0).getId());

                String createdAtString = contentValues.getAsString(RecurringServiceRecordRepository.CREATED_AT);
                Date createdAt = getDate(createdAtString);
                serviceObj.setCreatedAt(createdAt);

                recurringServiceRecordRepository.add(serviceObj);

                Timber.d("Ending processService table: %s", serviceTable.name);
            }
            return true;

        } catch (Exception e) {
            Timber.e(e, "Process Service Error");
            return null;
        }
    }

    private void processVisitEvent(List<EventClient> eventClients, String parentEventName) {
        for (EventClient eventClient : eventClients) {
            processVisitEvent(eventClient, parentEventName); // save locally
        }
    }

    // possible to delegate
    private void processVisitEvent(EventClient eventClient) {
        try {
            NCUtils.processHomeVisit(eventClient); // save locally
        } catch (Exception e) {
            String formID = (eventClient != null && eventClient.getEvent() != null) ? eventClient.getEvent().getFormSubmissionId() : "no form id";
            Timber.e("Form id " + formID + ". " + e.toString());
        }
    }

    private void processVisitEvent(EventClient eventClient, String parentEventName) {
        try {
            NCUtils.processSubHomeVisit(eventClient, parentEventName); // save locally
        } catch (Exception e) {
            String formID = (eventClient != null && eventClient.getEvent() != null) ? eventClient.getEvent().getFormSubmissionId() : "no form id";
            Timber.e("Form id " + formID + ". " + e.toString());
        }
    }

    /**
     * Update the family members
     *
     * @param familyID
     */
    private void processRemoveFamily(String familyID, Date eventDate) {

        Date myEventDate = eventDate;
        if (myEventDate == null) {
            myEventDate = new Date();
        }

        if (familyID == null) {
            return;
        }

        AllCommonsRepository commonsRepository = CoreChwApplication.getInstance().getAllCommonsRepository(CoreConstants.TABLE_NAME.FAMILY);
        if (commonsRepository != null) {

            ContentValues values = new ContentValues();
            values.put(DBConstants.KEY.DATE_REMOVED, new SimpleDateFormat("yyyy-MM-dd").format(myEventDate));
            values.put("is_closed", 1);

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CoreConstants.TABLE_NAME.FAMILY, values,
                    DBConstants.KEY.BASE_ENTITY_ID + " = ?  ", new String[]{familyID});

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CoreConstants.TABLE_NAME.CHILD, values,
                    DBConstants.KEY.RELATIONAL_ID + " = ?  ", new String[]{familyID});

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CoreConstants.TABLE_NAME.FAMILY_MEMBER, values,
                    DBConstants.KEY.RELATIONAL_ID + " = ?  ", new String[]{familyID});

            // clean fts table
            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CommonFtsObject.searchTableName(CoreConstants.TABLE_NAME.FAMILY), values,
                    CommonFtsObject.idColumn + " = ?  ", new String[]{familyID});

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CommonFtsObject.searchTableName(CoreConstants.TABLE_NAME.CHILD), values,
                    String.format(" %s in (select base_entity_id from %s where relational_id = ? )  ", CommonFtsObject.idColumn, CoreConstants.TABLE_NAME.CHILD), new String[]{familyID});

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CommonFtsObject.searchTableName(CoreConstants.TABLE_NAME.FAMILY_MEMBER), values,
                    String.format(" %s in (select base_entity_id from %s where relational_id = ? )  ", CommonFtsObject.idColumn, CoreConstants.TABLE_NAME.FAMILY_MEMBER), new String[]{familyID});

            List<String> familyMembers = ChildDao.getFamilyMembers(familyID);
            for (String baseEntityId : familyMembers) {
                CoreChwApplication.getInstance().getContext().alertService().deleteOfflineAlerts(baseEntityId);
            }
        }
    }

    private void processRemoveMember(String baseEntityId, Date eventDate) {

        Date myEventDate = eventDate;
        if (myEventDate == null) {
            myEventDate = new Date();
        }

        if (baseEntityId == null) {
            return;
        }

        AllCommonsRepository commonsRepository = CoreChwApplication.getInstance().getAllCommonsRepository(CoreConstants.TABLE_NAME.FAMILY_MEMBER);
        if (commonsRepository != null) {

            ContentValues values = new ContentValues();
            values.put(DBConstants.KEY.DATE_REMOVED, new SimpleDateFormat("yyyy-MM-dd").format(myEventDate));
            values.put("is_closed", 1);

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CoreConstants.TABLE_NAME.FAMILY_MEMBER, values,
                    DBConstants.KEY.BASE_ENTITY_ID + " = ?  ", new String[]{baseEntityId});

            // clean fts table
            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CommonFtsObject.searchTableName(CoreConstants.TABLE_NAME.FAMILY_MEMBER), values,
                    " object_id  = ?  ", new String[]{baseEntityId});

            // Utils.context().commonrepository(CoreConstants.TABLE_NAME.FAMILY_MEMBER).populateSearchValues(baseEntityId, DBConstants.KEY.DATE_REMOVED, new SimpleDateFormat("yyyy-MM-dd").format(eventDate), null);
            CoreChwApplication.getInstance().getContext().alertService().deleteOfflineAlerts(baseEntityId);
        }
    }

    private void processRemoveChild(String baseEntityId, Date eventDate) {

        Date myEventDate = eventDate;
        if (myEventDate == null) {
            myEventDate = new Date();
        }

        if (baseEntityId == null) {
            return;
        }

        AllCommonsRepository commonsRepository = CoreChwApplication.getInstance().getAllCommonsRepository(CoreConstants.TABLE_NAME.CHILD);
        if (commonsRepository != null) {

            ContentValues values = new ContentValues();
            values.put(DBConstants.KEY.DATE_REMOVED, new SimpleDateFormat("yyyy-MM-dd").format(myEventDate));
            values.put("is_closed", 1);

            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CoreConstants.TABLE_NAME.CHILD, values,
                    DBConstants.KEY.BASE_ENTITY_ID + " = ?  ", new String[]{baseEntityId});

            // clean fts table
            CoreChwApplication.getInstance().getRepository().getWritableDatabase().update(CommonFtsObject.searchTableName(CoreConstants.TABLE_NAME.CHILD), values,
                    CommonFtsObject.idColumn + "  = ?  ", new String[]{baseEntityId});

            // Utils.context().commonrepository(CoreConstants.TABLE_NAME.CHILD).populateSearchValues(baseEntityId, DBConstants.KEY.DATE_REMOVED, new SimpleDateFormat("yyyy-MM-dd").format(eventDate), null);
            CoreChwApplication.getInstance().getContext().alertService().deleteOfflineAlerts(baseEntityId);
        }
    }

    private ContentValues processCaseModel(EventClient eventClient, Table table) {
        try {
            List<Column> columns = table.columns;
            ContentValues contentValues = new ContentValues();

            for (Column column : columns) {
                processCaseModel(eventClient.getEvent(), eventClient.getClient(), column, contentValues);
            }

            return contentValues;
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    private Integer parseInt(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            Timber.e(e);
        }
        return null;
    }

    private Date getDate(String eventDateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(eventDateStr)) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
                date = dateFormat.parse(eventDateStr);
            } catch (ParseException e) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    date = dateFormat.parse(eventDateStr);
                } catch (ParseException pe) {
                    try {
                        date = DateUtil.parseDate(eventDateStr);
                    } catch (ParseException pee) {
                        Timber.e(pee, pee.toString());
                    }
                }
            }
        }
        return date;
    }

    @Override
    public void updateClientDetailsTable(Event event, Client client) {
        Timber.d("Started updateClientDetailsTable");
        event.addDetails("detailsUpdated", Boolean.TRUE.toString());
        Timber.d("Finished updateClientDetailsTable");
    }

    private void processVisitEvent(List<EventClient> eventClients) {
        for (EventClient eventClient : eventClients) {
            processVisitEvent(eventClient); // save locally
        }
    }

    private Float parseFloat(String string) {
        try {
            return Float.valueOf(string);
        } catch (NumberFormatException e) {
            Timber.e(e);
        }
        return null;
    }

    private CommunityResponderModel getCommunityResponderFromObs(Event event) {
        List<Obs> responderObs = event.getObs();
        CommunityResponderModel communityResponderModel = new CommunityResponderModel();
        for (Obs obs : responderObs) {
            if (obs.getFormSubmissionField().equals(CoreConstants.JsonAssets.RESPONDER_NAME)) {
                String value = StockUsageReportUtils.getObsValue(obs);
                if (StringUtils.isNotBlank(value)) {
                    communityResponderModel.setResponderName(value);
                    continue;
                } else
                    return null;
            } else if (obs.getFormSubmissionField().equals(CoreConstants.JsonAssets.RESPONDER_PHONE_NUMBER)) {
                String value = StockUsageReportUtils.getObsValue(obs);
                if (StringUtils.isNotBlank(value)) {
                    communityResponderModel.setResponderPhoneNumber(value);
                    continue;
                } else
                    return null;
            } else if (obs.getFormSubmissionField().equals(CoreConstants.JsonAssets.RESPONDER_ID)) {
                String value = StockUsageReportUtils.getObsValue(obs);
                if (StringUtils.isNotBlank(value)) {
                    communityResponderModel.setId(value);
                } else {
                    communityResponderModel.setId(event.getBaseEntityId());
                    continue;
                }
            } else if (obs.getFormSubmissionField().equals(CoreConstants.JsonAssets.RESPONDER_GPS)) {
                String value = StockUsageReportUtils.getObsValue(obs);
                if (StringUtils.isNotBlank(value)) {
                    communityResponderModel.setResponderLocation(value);
                    continue;
                } else
                    return null;
            }
        }
        return communityResponderModel;
    }

    private void clientProcessCommunityResponderEvent(Event event) {
        CommunityResponderModel communityResponderModel = getCommunityResponderFromObs(event);
        if (communityResponderModel != null) {
            CommunityResponderRepository repo = CoreChwApplication.getInstance().communityResponderRepository();
            if (StringUtils.isBlank(communityResponderModel.getId()))
                communityResponderModel.setId(event.getBaseEntityId());
            repo.addOrUpdate(communityResponderModel);
        }
    }
}