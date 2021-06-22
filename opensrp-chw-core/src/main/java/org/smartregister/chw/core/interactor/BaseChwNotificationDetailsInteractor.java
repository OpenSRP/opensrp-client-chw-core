package org.smartregister.chw.core.interactor;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.ChwNotificationDetailsContract;
import org.smartregister.chw.core.dao.ChwNotificationDao;
import org.smartregister.chw.core.domain.NotificationItem;
import org.smartregister.chw.core.domain.NotificationRecord;
import org.smartregister.chw.core.utils.ChwNotificationUtil;
import org.smartregister.chw.hiv.util.Constants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.opd.utils.OpdUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static org.smartregister.chw.core.utils.Utils.getDuration;
import static org.smartregister.util.Utils.getAllSharedPreferences;

public class BaseChwNotificationDetailsInteractor implements ChwNotificationDetailsContract.Interactor {

    private ChwNotificationDetailsContract.Presenter presenter;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public BaseChwNotificationDetailsInteractor(ChwNotificationDetailsContract.Presenter presenter) {
        this.presenter = presenter;
        context = (Activity) presenter.getView();
    }

    @Override
    public void createNotificationDismissalEvent(String notificationId, String notificationType) {
        String dateMarkedAsDone = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Event baseEvent = ChwNotificationUtil.createNotificationDismissalEvent(context, presenter.getClientBaseEntityId(), notificationId,notificationType, dateMarkedAsDone);
        JsonFormUtils.tagEvent(getAllSharedPreferences(), baseEvent);
        try {
            NCUtils.addEvent(getAllSharedPreferences(), baseEvent);
            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            ChwNotificationDao.markNotificationAsDone(context, notificationId,  ChwNotificationUtil.getNotificationDetailsTable(context, notificationType), dateMarkedAsDone);
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());

        } catch (Exception ex) {
            Timber.e(ex);
        }

    }

    @Override
    public void fetchNotificationDetails(String notificationId, String notificationType) {
        NotificationItem notificationItem = null;
        if (notificationType.equals(context.getString(R.string.notification_type_sick_child_follow_up)))
            notificationItem = getSickChildFollowUpDetails(notificationId);
        else if (notificationType.equals(context.getString(R.string.notification_type_pnc_danger_signs)) ||
                notificationType.equals(context.getString(R.string.notification_type_anc_danger_signs)))
            notificationItem = getAncPncOutcomeDetails(notificationId, notificationType);
        else if (notificationType.contains(context.getString(R.string.notification_type_malaria_follow_up)))
            notificationItem = getMalariaFollowUpDetails(notificationId);
        else if (notificationType.contains(context.getString(R.string.notification_type_family_planning)))
            notificationItem = getDetailsForFamilyPlanning(notificationId);
        else if (notificationType.equalsIgnoreCase(context.getString(R.string.notification_type_not_yet_done_referrals)))
            notificationItem = getDetailsForNotYetDoneReferral(notificationId);
        else if (notificationType.equalsIgnoreCase(context.getString(R.string.hiv_problem_outcome)) ||
                notificationType.equalsIgnoreCase(context.getString(R.string.tb_problem_outcome))
        )
            notificationItem = getHivTbProblemOutcomeDetails(notificationId, notificationType);

        presenter.onNotificationDetailsFetched(notificationItem);
    }

    @NotNull
    private NotificationItem getSickChildFollowUpDetails(String notificationId) {
        NotificationRecord notificationRecord = ChwNotificationDao.getSickChildFollowUpRecord(notificationId);
        String title = context.getString(R.string.followup_notification_title, notificationRecord.getClientName(), notificationRecord.getVisitDate());
        List<String> details = new ArrayList<>();
        details.add(context.getString(R.string.notification_care_giver, notificationRecord.getCareGiverName()));
        details.add(context.getString(R.string.notification_diagnosis, notificationRecord.getDiagnosis()));
        details.add(context.getString(R.string.notification_action_taken, notificationRecord.getResults()));
        details.add(context.getString(R.string.notification_village, notificationRecord.getVillage()));
        return new NotificationItem(title, details);
    }

    @NotNull
    private NotificationItem getAncPncOutcomeDetails(String notificationId, String notificationType) {
        NotificationRecord notificationRecord;
        notificationRecord = ChwNotificationDao.getAncPncDangerSignsOutcomeRecord(notificationId, ChwNotificationUtil.getNotificationDetailsTable(context, notificationType));

        String title = context.getString(R.string.followup_notification_title, notificationRecord.getClientName(), notificationRecord.getVisitDate());
        List<String> details = new ArrayList<>();
        details.add(context.getString(R.string.notification_danger_sign, notificationRecord.getDangerSigns()));
        details.add(context.getString(R.string.notification_action_taken, notificationRecord.getActionTaken()));
        details.add(context.getString(R.string.notification_village, notificationRecord.getVillage()));
        return new NotificationItem(title, details);
    }


    @NotNull
    private NotificationItem getMalariaFollowUpDetails(String notificationId) {
        NotificationRecord notificationRecord = ChwNotificationDao.getMalariaFollowUpRecord(notificationId);
        String title = context.getString(R.string.followup_notification_title, notificationRecord.getClientName(), notificationRecord.getVisitDate());
        List<String> details = new ArrayList<>();
        details.add(context.getString(R.string.notification_diagnosis, notificationRecord.getResults()));
        details.add(context.getString(R.string.notification_action_taken, notificationRecord.getActionTaken()));
        details.add(context.getString(R.string.notification_village, notificationRecord.getVillage()));
        return new NotificationItem(title, details);
    }

    @NotNull
    private NotificationItem getDetailsForFamilyPlanning(String notificationId) {
        NotificationRecord notificationRecord = ChwNotificationDao.getFamilyPlanningRecord(notificationId);
        String title = context.getString(R.string.followup_notification_title, notificationRecord.getClientName(), notificationRecord.getVisitDate());
        List<String> details = new ArrayList<>();
        details.add(context.getString(R.string.notification_selected_method, notificationRecord.getMethod()));
        details.add(context.getString(R.string.notification_village, notificationRecord.getVillage()));
        return new NotificationItem(title, details);
    }

    @NotNull
    private NotificationItem getDetailsForNotYetDoneReferral(String referralTaskId) {
        NotificationRecord record = ChwNotificationDao.getNotYetDoneReferral(referralTaskId);
        List<String> details = getNotificationRecordDetails(record);
        details.add(context.getString(R.string.notification_record_not_yet_done));
        String title = context.getString(R.string.successful_referral_notification_title,
                record.getClientName(), getClientAge(record.getClientDateOfBirth()));
        return new NotificationItem(title, details).setClientBaseEntityId(record.getClientBaseEntityId());
    }

    private List<String> getNotificationRecordDetails(NotificationRecord record) {
        List<String> details = new ArrayList<>();
        if (record != null) {
            Pair<String, String> notificationDatesPair = null;
            String notificationDate = record.getNotificationDate();
            try {
                notificationDate = dateFormat.format(dateFormat.parse(record.getNotificationDate()));
                notificationDatesPair = Pair.create(notificationDate, getDismissalDate(dateFormat.format(new Date())));
            } catch (ParseException e) {
                Timber.e(e, "Error Parsing date: %s", record.getNotificationDate());
            }
            presenter.setNotificationDates(notificationDatesPair);


            details.add(context.getString(R.string.notification_phone, record.getPhone() != null ? record.getPhone() : context.getString(R.string.no_phone_provided)));
            details.add(context.getString(R.string.referral_notification_closure_date, notificationDate));
            if (record.getVillage() != null) {
                details.add(context.getString(R.string.notification_village, record.getVillage()));
            }
        }
        return details;
    }

    @NotNull
    private NotificationItem getHivTbProblemOutcomeDetails(String notificationId, String notificationType) {
        NotificationRecord notificationRecord;
        notificationRecord = ChwNotificationDao.getHivTBOutcomeRecord(notificationId, ChwNotificationUtil.getNotificationDetailsTable(context, notificationType));

        String title = context.getString(R.string.followup_notification_title, notificationRecord.getClientName(), notificationRecord.getVisitDate());
        List<String> details = new ArrayList<>();
        details.add(context.getString(R.string.notification_action_taken, notificationRecord.getActionTaken()));
        if (notificationRecord.getResults() != null && ChwNotificationUtil.getNotificationDetailsTable(context, notificationType).equals(org.smartregister.chw.hiv.util.Constants.Tables.HIV_OUTCOME)) {
            if (notificationRecord.getResults().equalsIgnoreCase(Constants.HivStatus.POSITIVE))
                details.add(context.getString(R.string.notification_diagnosis, context.getString(R.string.hiv_positive_status)));
            else if (notificationRecord.getResults().equalsIgnoreCase(Constants.HivStatus.NEGATIVE))
                details.add(context.getString(R.string.notification_diagnosis, context.getString(R.string.hiv_negative_status)));
        } else if (notificationRecord.getResults() != null && ChwNotificationUtil.getNotificationDetailsTable(context, notificationType).equals(org.smartregister.chw.tb.util.Constants.Tables.TB_OUTCOME)) {
            if (notificationRecord.getResults().equalsIgnoreCase(org.smartregister.chw.tb.util.Constants.TbStatus.POSITIVE))
                details.add(context.getString(R.string.notification_diagnosis, context.getString(R.string.tb_positive_status)));
            else if (notificationRecord.getResults().equalsIgnoreCase(org.smartregister.chw.tb.util.Constants.TbStatus.NEGATIVE))
                details.add(context.getString(R.string.notification_diagnosis, context.getString(R.string.tb_negative_status)));
        }

        details.add(context.getString(R.string.notification_village, notificationRecord.getVillage()));
        return new NotificationItem(title, details);
    }

    /**
     * This method is used to obtain the date when the referral will be dismissed from the updates
     * register
     *
     * @param eventCreationDate date the referral was created
     * @return new date returned after adding 3 to the provided date
     */
    private String getDismissalDate(String eventCreationDate) {

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(eventCreationDate));
        } catch (ParseException e) {
            Timber.e(e);
        }
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        return dateFormat.format(calendar.getTime());
    }

    private String getClientAge(String dobString) {
        String translatedYearInitial = context.getResources().getString(R.string.abbrv_years);
        return OpdUtils.getClientAge(getDuration(dobString), translatedYearInitial);
    }
}
