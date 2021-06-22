package org.smartregister.chw.core.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.anc.activity.BaseAncUpcomingServicesActivity;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.presenter.BaseAncUpcomingServicesPresenter;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.core.interactor.CoreHivUpcomingServicesInteractor;

public class CoreHivUpcomingServicesActivity extends BaseAncUpcomingServicesActivity {
    public static void startMe(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, CoreHivUpcomingServicesActivity.class);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    public void initializePresenter() {
        presenter = new BaseAncUpcomingServicesPresenter(memberObject, new CoreHivUpcomingServicesInteractor(), this);
    }

}

