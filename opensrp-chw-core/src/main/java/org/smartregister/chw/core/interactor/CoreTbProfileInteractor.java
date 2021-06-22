package org.smartregister.chw.core.interactor;

import android.content.Context;

import org.joda.time.LocalDate;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.model.BaseUpcomingService;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.chw.core.contract.CoreTbProfileContract;
import org.smartregister.chw.core.dao.AlertDao;
import org.smartregister.chw.core.dao.AncDao;
import org.smartregister.chw.core.dao.PNCDao;
import org.smartregister.chw.core.dao.VisitDao;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.hiv.dao.HivDao;
import org.smartregister.chw.tb.contract.BaseTbProfileContract;
import org.smartregister.chw.tb.dao.TbDao;
import org.smartregister.chw.tb.domain.TbMemberObject;
import org.smartregister.chw.tb.interactor.BaseTbProfileInteractor;
import org.smartregister.dao.AbstractDao;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.repository.AllSharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class CoreTbProfileInteractor extends BaseTbProfileInteractor implements CoreTbProfileContract.Interactor {
    private Context context;

    public CoreTbProfileInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void updateProfileTbStatusInfo(TbMemberObject memberObject, BaseTbProfileContract.InteractorCallback callback) {
        Runnable runnable = new Runnable() {

            Date lastVisitDate = getLastVisitDate(memberObject);
            AlertStatus familyAlert = AlertDao.getFamilyAlertStatus(memberObject.getFamilyBaseEntityId());
            Alert upcomingService = getAlerts(context, memberObject.getBaseEntityId());

            @Override
            public void run() {
                getAppExecutors().mainThread().execute(() -> {
                    callback.refreshFamilyStatus(familyAlert);
                    callback.refreshLastVisit(lastVisitDate);
                    if (upcomingService == null) {
                        callback.refreshUpComingServicesStatus("", AlertStatus.complete, new Date());
                    } else {
                        callback.refreshUpComingServicesStatus(upcomingService.scheduleName(), upcomingService.status(), new LocalDate(upcomingService.startDate()).toDate());
                    }
                });
            }
        };
        getAppExecutors().diskIO().execute(runnable);
    }

    private String getMemberVisitType(String baseEntityId) {
        String type = null;

        if (TbDao.isRegisteredForTb(baseEntityId)) {
            type = CoreConstants.TABLE_NAME.TB_MEMBER;
        } else if (HivDao.isRegisteredForHiv(baseEntityId)) {
            type = CoreConstants.TABLE_NAME.HIV_MEMBER;
        } else if (AncDao.isANCMember(baseEntityId)) {
            type = CoreConstants.TABLE_NAME.ANC_MEMBER;
        } else if (PNCDao.isPNCMember(baseEntityId)) {
            type = CoreConstants.TABLE_NAME.PNC_MEMBER;
        }
        return type;
    }

    public Date getLastVisitDate(TbMemberObject memberObject) {
        Date lastVisitDate = null;
        List<Visit> visits = new ArrayList<>();
        String memberType = getMemberVisitType(memberObject.getBaseEntityId());

        if (memberType != null) {
            switch (memberType) {
                case CoreConstants.TABLE_NAME.HIV_MEMBER:
                    visits = HivDao.getHivVisitsMedicalHistory(memberObject.getBaseEntityId());
                    break;
                case CoreConstants.TABLE_NAME.TB_MEMBER:
                    visits = TbDao.getTbVisitsMedicalHistory(memberObject.getBaseEntityId());
                    break;
                case CoreConstants.TABLE_NAME.ANC_MEMBER:
                    visits = getAncVisitsMedicalHistory(memberObject.getBaseEntityId());
                    break;
                case CoreConstants.TABLE_NAME.PNC_MEMBER:
                    visits = VisitDao.getPNCVisitsMedicalHistory(memberObject.getBaseEntityId());
                    break;
                default:
                    break;
            }

            if (visits.size() > 0) {
                lastVisitDate = visits.get(0).getDate();
            }
        }
        return lastVisitDate;
    }

    protected Alert getAlerts(Context context, String baseEntityID) {
        try {
            List<BaseUpcomingService> baseUpcomingServices = new ArrayList<>(new CoreTbUpcomingServicesInteractor().getMemberServices(context, toMember(TbDao.getMember(baseEntityID))));
            if (baseUpcomingServices.size() > 0) {
                Comparator<BaseUpcomingService> comparator = (o1, o2) -> o1.getServiceDate().compareTo(o2.getServiceDate());
                Collections.sort(baseUpcomingServices, comparator);

                BaseUpcomingService baseUpcomingService = baseUpcomingServices.get(0);
                Date serviceDate = baseUpcomingService.getServiceDate();
                String serviceName = baseUpcomingService.getServiceName();
                AlertStatus upcomingServiceAlertStatus = serviceDate != null && serviceDate.before(new Date()) ? AlertStatus.urgent : AlertStatus.normal;
                String formattedServiceDate = serviceDate != null ? AbstractDao.getDobDateFormat().format(serviceDate) : null;
                return new Alert(baseEntityID, serviceName, serviceName, upcomingServiceAlertStatus, formattedServiceDate, "", true);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    private List<Visit> getAncVisitsMedicalHistory(String baseEntityId) {
        List<Visit> visits = VisitUtils.getVisits(baseEntityId);
        List<Visit> allVisits = new ArrayList<>(visits);

        for (Visit visit : visits) {
            List<Visit> childVisits = VisitUtils.getChildVisits(visit.getVisitId());
            allVisits.addAll(childVisits);
        }
        return allVisits;
    }

    public MemberObject toMember(TbMemberObject memberObject) {
        MemberObject res = new MemberObject();
        res.setBaseEntityId(memberObject.getBaseEntityId());
        res.setFirstName(memberObject.getFirstName());
        res.setLastName(memberObject.getLastName());
        res.setMiddleName(memberObject.getMiddleName());
        res.setDob(memberObject.getAge());
        return res;
    }

    @Override
    public void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString, String entityID) throws Exception {
        //TODO implement this
    }
}