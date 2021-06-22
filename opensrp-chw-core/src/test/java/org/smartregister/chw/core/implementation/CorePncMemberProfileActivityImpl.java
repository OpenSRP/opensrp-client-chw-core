package org.smartregister.chw.core.implementation;

import android.content.Intent;
import android.view.MenuItem;

import org.mockito.Mockito;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.activity.CorePncMemberProfileActivity;
import org.smartregister.chw.core.activity.CorePncRegisterActivity;
import org.smartregister.chw.core.interactor.CorePncMemberProfileInteractor;
import org.smartregister.chw.core.presenter.CorePncMemberProfilePresenter;

public class CorePncMemberProfileActivityImpl extends CorePncMemberProfileActivity {
    @Override
    protected Class<? extends CoreFamilyProfileActivity> getFamilyProfileActivityClass() {
        return CoreFamilyProfileActivity.class;
    }

    @Override
    protected CorePncMemberProfileInteractor getPncMemberProfileInteractor() {
        return Mockito.mock(CorePncMemberProfileInteractor.class);
    }

    @Override
    public void registerPresenter() {
        this.presenter = Mockito.mock(CorePncMemberProfilePresenter.class);
    }

    @Override
    protected Intent getPNCIntent() {
        return Mockito.mock(Intent.class);
    }

    @Override
    public void removePncMember() {
        // do nothing
    }

    @Override
    protected Class<? extends CorePncRegisterActivity> getPncRegisterActivityClass() {
        return CorePncRegisterActivity.class;
    }

    @Override
    public void startMalariaRegister() {
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
    public void startMalariaFollowUpVisit() {
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

    public void startHfMalariaFollowupForm() {
        // do nothing
    }

    @Override
    public void getRemoveBabyMenuItem(MenuItem item) {
        // do nothing
    }
}
