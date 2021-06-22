package org.smartregister.chw.core.utils;

import android.content.Context;
import android.os.Build;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.application.TestApplication;
import org.smartregister.chw.core.listener.OnRetrieveNotifications;
import org.smartregister.clientandeventmodel.Event;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, sdk = Build.VERSION_CODES.P)
public class ChwNotificationUtilTest {

    @Test
    public void getNotificationsTableReturnsCorrectValue() {
        Context context = RuntimeEnvironment.application;
        String sickChildTable = "ec_sick_child_followup";
        String familyPlanningTable = "ec_family_planning_update";
        String pncDangerSignsOutcomeTable = "ec_pnc_danger_signs_outcome";
        String ancDangerSignsOutcomeTable = "ec_anc_danger_signs_outcome";
        String malariaFollowupHfTable = "ec_malaria_followup_hf";
        String hivOutcomeTable = "ec_hiv_outcome";
        String tbOutcomeTable = "ec_tb_outcome";


        Assert.assertEquals(sickChildTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.notification_type_sick_child_follow_up)));
        Assert.assertEquals(familyPlanningTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.notification_type_family_planning)));
        Assert.assertEquals(pncDangerSignsOutcomeTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.notification_type_pnc_danger_signs)));
        Assert.assertEquals(ancDangerSignsOutcomeTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.notification_type_anc_danger_signs)));
        Assert.assertEquals(malariaFollowupHfTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.notification_type_malaria_follow_up)));
        Assert.assertEquals(hivOutcomeTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.hiv_problem_outcome)));
        Assert.assertEquals(tbOutcomeTable, ChwNotificationUtil.getNotificationDetailsTable(context, context.getString(R.string.tb_problem_outcome)));
    }

    @Test
    public void canGetStringFromJSONArrayString() {
        String jsonArrayString = "[\"Value Uno\", \"Value dos\"]";
        String expectedString = "Value Uno,Value dos";
        Assert.assertEquals(expectedString, ChwNotificationUtil.getStringFromJSONArrayString(jsonArrayString));
        jsonArrayString = "Value uno";
        expectedString = "Value uno";
        Assert.assertEquals(expectedString, ChwNotificationUtil.getStringFromJSONArrayString(jsonArrayString));
    }


    @Test
    public void canGetCorrectNotificationDismissalEventType() {
        Context context = RuntimeEnvironment.application;
        Assert.assertEquals(CoreConstants.EventType.SICK_CHILD_NOTIFICATION_DISMISSAL, ChwNotificationUtil.getNotificationDismissalEventType(context, context.getString(R.string.notification_type_sick_child_follow_up)));
        Assert.assertEquals(CoreConstants.EventType.ANC_NOTIFICATION_DISMISSAL, ChwNotificationUtil.getNotificationDismissalEventType(context, context.getString(R.string.notification_type_anc_danger_signs)));
        Assert.assertEquals(CoreConstants.EventType.MALARIA_NOTIFICATION_DISMISSAL, ChwNotificationUtil.getNotificationDismissalEventType(context, context.getString(R.string.notification_type_malaria_follow_up)));
    }

    @Test
    public void shouldRetrieveNotificationsForClient() {
        OnRetrieveNotifications onRetrieveNotifications = Mockito.mock(OnRetrieveNotifications.class);
        ChwNotificationUtil.retrieveNotifications(true, "some-base-entity-id", onRetrieveNotifications);
        Mockito.verify(onRetrieveNotifications, Mockito.never()).onReceivedNotifications(Mockito.anyList());
    }

    @Test
    public void canCreateNotificationDismissalEvent() {
        String baseEntityId = "test-entity-0001";
        String notificationID = "123789";
        String notificationType = "ANC Danger Signs";
        String dateMarkedAsDone = "2020-06-23";

        Event dismissalEvent = ChwNotificationUtil.createNotificationDismissalEvent(RuntimeEnvironment.application, baseEntityId, notificationID, notificationType, dateMarkedAsDone);
        Assert.assertEquals(dismissalEvent.getObs().size(), 2);
    }
}
