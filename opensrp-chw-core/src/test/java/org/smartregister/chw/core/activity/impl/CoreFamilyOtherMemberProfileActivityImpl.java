package org.smartregister.chw.core.activity.impl;

import android.content.Context;

import org.mockito.Mockito;
import org.smartregister.chw.core.activity.CoreFamilyOtherMemberProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.custom_views.CoreFamilyMemberFloatingMenu;
import org.smartregister.chw.core.presenter.CoreFamilyOtherMemberActivityPresenter;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.fragment.BaseFamilyOtherMemberProfileFragment;
import org.smartregister.view.contract.BaseProfileContract;

public class CoreFamilyOtherMemberProfileActivityImpl extends CoreFamilyOtherMemberProfileActivity {
    @Override
    public void startAncRegister() {
        // do nothing
    }

    @Override
    public void startFpRegister() {
        // do nothing
    }

    @Override
    public void startFpChangeMethod() {
        // do nothing
    }

    @Override
    public void startMalariaRegister() {
        // do nothing
    }

    @Override
    protected void startHivRegister() {
        // do nothing
    }

    @Override
    protected void startTbRegister() {
        // do nothing
    }

    @Override
    public void startMalariaFollowUpVisit() {
        // do nothing
    }

    @Override
    public void startHfMalariaFollowupForm() {
        // do nothing
    }

    @Override
    public void setIndependentClient(boolean isIndependent) {
        // do nothing
    }

    @Override
    public void removeIndividualProfile() {
        // do nothing
    }

    @Override
    public void startEditMemberJsonForm(Integer title_resource, CommonPersonObjectClient client) {
        // do nothing
    }

    @Override
    protected BaseProfileContract.Presenter getFamilyOtherMemberActivityPresenter(String familyBaseEntityId, String baseEntityId, String familyHead, String primaryCaregiver, String villageTown, String familyName) {
        return Mockito.mock(CoreFamilyOtherMemberActivityPresenter.class);
    }

    @Override
    protected CoreFamilyMemberFloatingMenu getFamilyMemberFloatingMenu() {
        return Mockito.mock(CoreFamilyMemberFloatingMenu.class);
    }

    @Override
    protected Context getFamilyOtherMemberProfileActivity() {
        return null;
    }

    @Override
    protected Class<? extends CoreFamilyProfileActivity> getFamilyProfileActivity() {
        return null;
    }

    @Override
    protected BaseFamilyOtherMemberProfileFragment getFamilyOtherMemberProfileFragment() {
        return null;
    }
}
