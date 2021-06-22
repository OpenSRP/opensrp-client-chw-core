package org.smartregister.chw.core.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.activity.CoreAboveFiveChildProfileActivity;
import org.smartregister.chw.core.activity.CoreChildProfileActivity;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.utils.CoreChildUtils;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.malaria.activity.BaseMalariaProfileActivity;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;

import java.lang.ref.WeakReference;

import static org.smartregister.chw.core.utils.Utils.getDuration;
import static org.smartregister.chw.core.utils.Utils.passToolbarTitle;

public abstract class CoreChwNotificationGoToMemberProfileTask extends AsyncTask<Void, Void, Void> {
    private final CommonPersonObjectClient commonPersonObjectClient;
    private final Bundle bundle;
    private WeakReference<Activity> activity;
    private String notificationType;

    public CoreChwNotificationGoToMemberProfileTask(CommonPersonObjectClient commonPersonObjectClient, Bundle bundle, String notificationType, Activity activity) {
        this.commonPersonObjectClient = commonPersonObjectClient;
        this.bundle = bundle;
        this.notificationType = notificationType;
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (notificationType.equals(activity.get().getString(R.string.notification_type_anc_danger_signs))) {
            goToAncProfileActivity(commonPersonObjectClient, bundle);
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_pnc_danger_signs))) {
            gotToPncProfileActivity(commonPersonObjectClient, bundle);
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_family_planning))) {
            goToFpProfile(commonPersonObjectClient.entityId(), activity.get());
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_sick_child_follow_up))) {
            goToChildProfileActivity(commonPersonObjectClient, bundle);
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_malaria_follow_up))) {
            gotToMalariaProfile(commonPersonObjectClient, bundle);
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_hiv_problem_outcome))) {
            goToHivProfile(commonPersonObjectClient.entityId(), activity.get());
        } else if (notificationType.equals(activity.get().getString(R.string.notification_type_tb_problem_outcome))) {
            goToTbProfile(commonPersonObjectClient.entityId(), activity.get());
        }
        return null;
    }

    private void goToChildProfileActivity(CommonPersonObjectClient patient, Bundle bundle) {
        String dobString = getDuration(Utils.getValue(patient.getColumnmaps(), DBConstants.KEY.DOB, false));
        Integer yearOfBirth = CoreChildUtils.dobStringToYear(dobString);
        Intent intent;
        if (yearOfBirth != null && yearOfBirth >= 5) {
            intent = new Intent(activity.get(), getAboveFiveChildProfileActivityClass());
        } else {
            intent = new Intent(activity.get(), getChildProfileActivityClass());
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        passToolbarTitle(activity.get(), intent);
        intent.putExtra(Constants.INTENT_KEY.BASE_ENTITY_ID, patient.getCaseId());
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT, new MemberObject(patient));
        activity.get().startActivity(intent);
    }

    private void goToAncProfileActivity(CommonPersonObjectClient patient, Bundle bundle) {
        patient.getColumnmaps().putAll(getAncCommonPersonObject(patient.entityId()).getColumnmaps());
        activity.get().startActivity(initProfileActivityIntent(patient, bundle, getAncMemberProfileActivityClass()));
    }

    private void gotToPncProfileActivity(CommonPersonObjectClient patient, Bundle bundle) {
        patient.getColumnmaps().putAll(getPncCommonPersonObject(patient.entityId()).getColumnmaps());
        activity.get().startActivity(initProfileActivityIntent(patient, bundle, getPncMemberProfileActivityClass()));
    }

    private Intent initProfileActivityIntent(CommonPersonObjectClient patient, Bundle bundle, Class activityClass) {
        Intent intent = new Intent(activity.get(), activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        passToolbarTitle(activity.get(), intent);
        intent.putExtra(org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS.BASE_ENTITY_ID, patient.entityId());
        intent.putExtra(CoreConstants.INTENT_KEY.CLIENT, patient);
        return intent;
    }

    private void gotToMalariaProfile(CommonPersonObjectClient client, Bundle bundle) {
        Intent intent = new Intent(activity.get(), getMalariaProfileActivityClass());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        passToolbarTitle(activity.get(), intent);
        intent.putExtra(org.smartregister.chw.malaria.util.Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, client.getCaseId());
        activity.get().startActivity(intent);
    }

    private CommonPersonObject getAncCommonPersonObject(String baseEntityId) {
        return CoreChwApplication.ancRegisterRepository().getAncCommonPersonObject(baseEntityId);
    }

    private CommonPersonObject getPncCommonPersonObject(String baseEntityId) {
        return CoreChwApplication.pncRegisterRepository().getPncCommonPersonObject(baseEntityId);
    }

    protected abstract void goToFpProfile(String baseEntityId, Activity activity);

    protected abstract void goToHivProfile(String baseEntityId, Activity activity);

    protected abstract void goToTbProfile(String baseEntityId, Activity activity);

    protected abstract Class<? extends CoreAboveFiveChildProfileActivity> getAboveFiveChildProfileActivityClass();

    protected abstract Class<? extends CoreChildProfileActivity> getChildProfileActivityClass();

    protected abstract Class<? extends BaseAncMemberProfileActivity> getAncMemberProfileActivityClass();

    protected abstract Class<? extends BasePncMemberProfileActivity> getPncMemberProfileActivityClass();

    protected abstract Class<? extends BaseMalariaProfileActivity> getMalariaProfileActivityClass();
}
