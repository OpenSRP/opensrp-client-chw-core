package org.smartregister.chw.core.utils;

import android.content.Context;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.dao.ChwNotificationDao;
import org.smartregister.chw.core.listener.OnRetrieveNotifications;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ChwNotificationUtil {

    public static String getNotificationDetailsTable(Context context, String notificationType) {
        if (context.getString(R.string.notification_type_sick_child_follow_up).equals(notificationType)) {
            return "ec_sick_child_followup";
        } else if (context.getString(R.string.notification_type_pnc_danger_signs).equals(notificationType)) {
            return "ec_pnc_danger_signs_outcome";
        } else if (context.getString(R.string.notification_type_anc_danger_signs).equals(notificationType)) {
            return "ec_anc_danger_signs_outcome";
        } else if (context.getString(R.string.notification_type_malaria_follow_up).equals(notificationType)) {
            return "ec_malaria_followup_hf";
        } else if (context.getString(R.string.notification_type_family_planning).equals(notificationType)) {
            return "ec_family_planning_update";
        } else if (context.getString(R.string.hiv_problem_outcome).equals(notificationType)) {
            return "ec_hiv_outcome";
        } else if (context.getString(R.string.tb_problem_outcome).equals(notificationType)) {
            return "ec_tb_outcome";
        }
        return null;
    }

    public static String getNotificationDismissalEventType(Context context, String notificationType) {
        Map<String, String> notificationEventMap = new HashMap<>();
        notificationEventMap.put(context.getString(R.string.notification_type_sick_child_follow_up), CoreConstants.EventType.SICK_CHILD_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_pnc_danger_signs), CoreConstants.EventType.PNC_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_anc_danger_signs), CoreConstants.EventType.ANC_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_malaria_follow_up), CoreConstants.EventType.MALARIA_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_family_planning), CoreConstants.EventType.FAMILY_PLANNING_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_hiv_problem_outcome), CoreConstants.EventType.HIV_NOTIFICATION_DISMISSAL);
        notificationEventMap.put(context.getString(R.string.notification_type_tb_problem_outcome), CoreConstants.EventType.TB_NOTIFICATION_DISMISSAL);

        return notificationEventMap.get(notificationType);
    }

    public static Event createNotificationDismissalEvent(Context context, String baseEntityId, String notificationId, String notificationType, String dateMarkedAsDone) {
        Event dismissalEvent = null;
        try {
            dismissalEvent = (Event) new Event()
                    .withBaseEntityId(baseEntityId)
                    .withEventDate(new Date())
                    .withEventType( ChwNotificationUtil.getNotificationDismissalEventType(context, notificationType))
                    .withFormSubmissionId(JsonFormUtils.generateRandomUUIDString())
                    .withEntityType(getNotificationDetailsTable(context, notificationType))
                    .withDateCreated(new Date());
        } catch (Exception ex) {
            Timber.e(ex);
        }

        if (dismissalEvent != null) {
            dismissalEvent.addObs(new Obs("concept", "text", CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.NOTIFICATION_ID, "",
                    CoreJsonFormUtils.toList(notificationId), new ArrayList<>(), null, CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.NOTIFICATION_ID));
            dismissalEvent.addObs(new Obs("concept", "text", CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.DATE_NOTIFICATION_MARKED_AS_DONE, "",
                    CoreJsonFormUtils.toList(dateMarkedAsDone), new ArrayList<>(), null, CoreConstants.FORM_CONSTANTS.FORM_SUBMISSION_FIELD.DATE_NOTIFICATION_MARKED_AS_DONE));
        }
        return dismissalEvent;
    }

    public static String getStringFromJSONArrayString(String jsonArrayString) {
        try {
            Object json = new JSONTokener(jsonArrayString).nextValue();
            if (json instanceof JSONArray) {
                return new JSONArray(jsonArrayString).join(",").replaceAll("\"", "").trim();
            }
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return jsonArrayString;
    }

    /**
     * This method is used to retrieve notifications for a particular client
     *
     * @param hasReferrals            this action should only be performed for flavours with referrals
     * @param baseEntityId            the clients unique identifier
     * @param onRetrieveNotifications callback method invoked after retrieving the notifications
     */
    public static void retrieveNotifications(boolean hasReferrals, String baseEntityId,
                                             OnRetrieveNotifications onRetrieveNotifications) {
        if (hasReferrals)
            Single.fromCallable(() ->
                    ChwNotificationDao.getClientNotifications(baseEntityId))
                    .subscribe(new SingleObserver<List<Pair<String, String>>>() {
                        private Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            this.disposable = disposable;
                        }

                        @Override
                        public void onSuccess(List<Pair<String, String>> notifications) {
                            if (notifications.size() > 0) {
                                onRetrieveNotifications.onReceivedNotifications(notifications);
                                disposable.dispose();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Timber.e(throwable, "Error retrieving notifications for %s", baseEntityId);
                        }
                    });
    }
}
