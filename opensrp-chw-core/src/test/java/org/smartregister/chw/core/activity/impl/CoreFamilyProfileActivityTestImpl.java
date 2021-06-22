package org.smartregister.chw.core.activity.impl;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import org.mockito.Mockito;
import org.smartregister.chw.anc.activity.BaseAncMemberProfileActivity;
import org.smartregister.chw.core.activity.CoreAboveFiveChildProfileActivity;
import org.smartregister.chw.core.activity.CoreChildProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileMenuActivity;
import org.smartregister.chw.core.activity.CoreFamilyRemoveMemberActivity;
import org.smartregister.chw.core.presenter.CoreFamilyProfilePresenter;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonPersonObject;

import java.util.HashMap;

public class CoreFamilyProfileActivityTestImpl extends CoreFamilyProfileActivity {

    @Override
    protected void initializePresenter() {
        super.initializePresenter();
        presenter = Mockito.mock(CoreFamilyProfilePresenter.class);
    }

    @Override
    protected void refreshPresenter() {
        // do nothing
    }

    @Override
    protected void refreshList(Fragment item) {
        // do nothing
    }

    @Override
    protected Class<? extends CoreFamilyRemoveMemberActivity> getFamilyRemoveMemberClass() {
        return CoreFamilyRemoveMemberActivity.class;
    }

    @Override
    protected Class<? extends CoreFamilyProfileMenuActivity> getFamilyProfileMenuClass() {
        return CoreFamilyProfileMenuActivity.class;
    }

    @Override
    protected Class<?> getFamilyOtherMemberProfileActivityClass() {
        return CoreAboveFiveChildProfileActivity.class;
    }

    @Override
    protected Class<? extends CoreAboveFiveChildProfileActivity> getAboveFiveChildProfileActivityClass() {
        return CoreAboveFiveChildProfileActivity.class;
    }

    @Override
    protected Class<? extends CoreChildProfileActivity> getChildProfileActivityClass() {
        return CoreAboveFiveChildProfileActivity.class;
    }

    @Override
    protected Class<? extends BaseAncMemberProfileActivity> getAncMemberProfileActivityClass() {
        return BaseAncMemberProfileActivity.class;
    }

    @Override
    protected Class<? extends BasePncMemberProfileActivity> getPncMemberProfileActivityClass() {
        return BasePncMemberProfileActivity.class;
    }

    @Override
    protected void goToFpProfile(String baseEntityId, Activity activity) {
        // do nothing
    }

    @Override
    protected void goToHivProfile(String baseEntityId, Activity activity) {
        // do nothing
    }

    @Override
    protected void goToTbProfile(String baseEntityId, Activity activity) {
        // do nothing
    }

    @Override
    protected boolean isAncMember(String baseEntityId) {
        return false;
    }

    @Override
    protected HashMap<String, String> getAncFamilyHeadNameAndPhone(String baseEntityId) {
        return null;
    }

    @Override
    protected CommonPersonObject getAncCommonPersonObject(String baseEntityId) {
        return Mockito.mock(CommonPersonObject.class);
    }

    @Override
    protected CommonPersonObject getPncCommonPersonObject(String baseEntityId) {
        return Mockito.mock(CommonPersonObject.class);
    }

    @Override
    protected boolean isPncMember(String baseEntityId) {
        return false;
    }

    @Override
    public void setEventDate(String eventDate) {
        // do nothing
    }
}
