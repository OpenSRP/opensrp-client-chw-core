package org.smartregister.chw.hf.activity;

import android.content.Context;
import android.view.Menu;

import org.json.JSONObject;
import org.smartregister.chw.core.activity.CoreFamilyOtherMemberProfileActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.custom_views.CoreFamilyMemberFloatingMenu;
import org.smartregister.chw.core.fragment.FamilyCallDialogFragment;
import org.smartregister.chw.core.utils.BAJsonFormUtils;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.hf.HealthFacilityApplication;
import org.smartregister.chw.hf.R;
import org.smartregister.chw.hf.custom_view.FamilyMemberFloatingMenu;
import org.smartregister.chw.hf.fragment.FamilyOtherMemberProfileFragment;
import org.smartregister.chw.hf.presenter.FamilyOtherMemberActivityPresenter;
import org.smartregister.chw.malaria.dao.MalariaDao;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.fragment.BaseFamilyOtherMemberProfileFragment;
import org.smartregister.family.model.BaseFamilyOtherMemberProfileActivityModel;
import org.smartregister.family.util.DBConstants;
import org.smartregister.view.contract.BaseProfileContract;

import timber.log.Timber;

public class FamilyOtherMemberProfileActivity extends CoreFamilyOtherMemberProfileActivity {
    private FamilyMemberFloatingMenu familyFloatingMenu;
    private BAJsonFormUtils baJsonFormUtils;

    @Override
    protected void onCreation() {
        super.onCreation();
        baJsonFormUtils = new BAJsonFormUtils(HealthFacilityApplication.getInstance());
        setIndependentClient(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setupMenuOptions(menu);
        return true;
    }

    @Override
    protected void startAncRegister() {
        //TODO implement start anc register for HF
    }

    @Override
    protected void startFpRegister() {
        //TODO implement start anc register for HF
    }

    @Override
    protected void startMalariaRegister() {
        //TODO implement start malaria register for HF
    }

    @Override
    protected void startHivRegister() {
        //TODO implement start hiv register for HF
    }

    @Override
    protected void startTbRegister() {
        //TODO implement start tb register for HF
    }

    @Override
    protected void startFpChangeMethod() {
        //TODO implement start fp change register for HF
    }

    @Override
    protected void startMalariaFollowUpVisit() {
        //TODO implement start malaria  follow-up visit for HF
    }

    @Override
    protected void startHfMalariaFollowupForm() {
        //TODO implement start malaria  follow-up visit for HF
    }


    @Override
    protected void setIndependentClient(boolean isIndependent) {
        this.isIndependent = isIndependent;
    }

    @Override
    protected void removeIndividualProfile() {
        Timber.d("Remove member action is not required in HF");
    }

    @Override
    protected void startEditMemberJsonForm(Integer title_resource, CommonPersonObjectClient client) {
        JSONObject form;
        if (title_resource != null) {
            form = baJsonFormUtils.getAutoJsonEditMemberFormString(getResources().getString(title_resource), CoreConstants.JSON_FORM.getFamilyMemberRegister(),
                    this, client, Utils.metadata().familyMemberRegister.updateEventType, familyName, commonPersonObject.getCaseId().equalsIgnoreCase(primaryCaregiver));
        } else {
            form = baJsonFormUtils.getAutoJsonEditMemberFormString(null, CoreConstants.JSON_FORM.getFamilyMemberRegister(),
                    this, client, Utils.metadata().familyMemberRegister.updateEventType, familyName, commonPersonObject.getCaseId().equalsIgnoreCase(primaryCaregiver));
        }
        try {
            startFormActivity(form);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    protected BaseProfileContract.Presenter getFamilyOtherMemberActivityPresenter(
            String familyBaseEntityId, String baseEntityId, String familyHead, String primaryCaregiver, String villageTown, String familyName) {
        return new FamilyOtherMemberActivityPresenter(this, new BaseFamilyOtherMemberProfileActivityModel(),
                null, familyBaseEntityId, baseEntityId, familyHead, primaryCaregiver, villageTown, familyName);
    }

    @Override
    protected CoreFamilyMemberFloatingMenu getFamilyMemberFloatingMenu() {
        if (familyFloatingMenu == null) {
            prepareFab();
        }
        return familyFloatingMenu;
    }

    @Override
    protected Context getFamilyOtherMemberProfileActivity() {
        return FamilyOtherMemberProfileActivity.this;
    }

    @Override
    protected Class<? extends CoreFamilyProfileActivity> getFamilyProfileActivity() {
        return FamilyProfileActivity.class;
    }

    @Override
    public void updateHasPhone(boolean hasPhone) {
        super.updateHasPhone(hasPhone);
        if (!hasPhone) {
            familyFloatingMenu.hideFab();
        }

    }

    @Override
    protected void initializePresenter() {
        super.initializePresenter();
        onClickFloatingMenu = viewId -> {
            if (viewId == R.id.call_layout) {
                FamilyCallDialogFragment.launchDialog(this, familyBaseEntityId);
            }
        };
    }

    @Override
    protected BaseFamilyOtherMemberProfileFragment getFamilyOtherMemberProfileFragment() {
        return FamilyOtherMemberProfileFragment.newInstance(getIntent().getExtras());
    }

    private void prepareFab() {
        familyFloatingMenu = new FamilyMemberFloatingMenu(this);
        familyFloatingMenu.fab.setOnClickListener(v -> FamilyCallDialogFragment.launchDialog(this, familyBaseEntityId));
    }

    private void setupMenuOptions(Menu menu) {
        menu.findItem(R.id.action_malaria_registration).setVisible(false);
        menu.findItem(R.id.action_malaria_followup_visit).setVisible(false);
        menu.findItem(R.id.action_sick_child_follow_up).setVisible(false);
        menu.findItem(R.id.action_anc_registration).setVisible(false);
        menu.findItem(R.id.action_remove_member).setVisible(false);

        if (MalariaDao.isRegisteredForMalaria(baseEntityId)) {
            menu.findItem(R.id.action_malaria_followup_visit).setTitle(R.string.hf_malaria_follow_up);
            menu.findItem(R.id.action_malaria_followup_visit).setVisible(true);
            menu.findItem(R.id.action_malaria_diagnosis).setVisible(false);
        } else {
            menu.findItem(R.id.action_malaria_diagnosis).setVisible(true);
        }

        if ("Female".equalsIgnoreCase(Utils.getValue(commonPersonObject.getColumnmaps(), DBConstants.KEY.GENDER, false)) && Utils.isMemberOfReproductiveAge(commonPersonObject, 10, 49)) {
            menu.findItem(R.id.action_pregnancy_confirmation).setVisible(true);
            menu.findItem(R.id.action_fp_initiation).setVisible(true);
        }
    }
}
