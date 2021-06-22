package org.smartregister.chw.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.CorePncMemberProfileContract;
import org.smartregister.chw.core.dao.PNCDao;
import org.smartregister.chw.core.interactor.CoreChildProfileInteractor;
import org.smartregister.chw.core.interactor.CorePncMemberProfileInteractor;
import org.smartregister.chw.core.model.ChildModel;
import org.smartregister.chw.core.presenter.CorePncMemberProfilePresenter;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.CoreJsonFormUtils;
import org.smartregister.chw.core.utils.MalariaFollowUpStatusTaskUtil;
import org.smartregister.chw.malaria.dao.MalariaDao;
import org.smartregister.chw.pnc.activity.BasePncMemberProfileActivity;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.AlertStatus;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.chw.core.utils.Utils.getCommonPersonObjectClient;
import static org.smartregister.chw.core.utils.Utils.updateToolbarTitle;

public abstract class CorePncMemberProfileActivity extends BasePncMemberProfileActivity implements CorePncMemberProfileContract.View {

    protected ImageView imageViewCross;
    protected boolean hasDueServices = false;
    protected CorePncMemberProfileInteractor pncMemberProfileInteractor = getPncMemberProfileInteractor();
    protected HashMap<String, String> menuItemEditNames = new HashMap<>();
    protected HashMap<String, String> menuItemRemoveNames = new HashMap<>();
    protected RecyclerView notificationAndReferralRecyclerView;
    protected RelativeLayout notificationAndReferralLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_pnc_member_registration) {
            startActivityForResult(getPNCIntent(), JsonFormUtils.REQUEST_CODE_GET_JSON);
            return true;
        } else if (itemId == R.id.action_pnc_registration) {
            getEditMenuItem(item);
        } else if (itemId == R.id.action_malaria_registration) {
            startMalariaRegister();
            return true;
        } else if (itemId == R.id.action_malaria_followup_visit) {
            startMalariaFollowUpVisit();
            return true;
        } else if (itemId == R.id.action_fp_initiation_pnc) {
            startFpRegister();
            return true;
        } else if (itemId == R.id.action_hiv_registration) {
            startHivRegister();
            return true;
        } else if (itemId == R.id.action_tb_registration) {
            startTbRegister();
            return true;
        } else if (itemId == R.id.action_fp_change) {
            startFpChangeMethod();
            return true;
        } else if (itemId == R.id.action__pnc_remove_member) {
            removePncMember();
            return true;
        } else if (itemId == R.id.action_pnc_remove_baby) {
            getRemoveBabyMenuItem(item);
            return true;
        } else if (itemId == R.id.action__pnc_danger_sign_outcome) {
            getPncMemberProfilePresenter().startPncDangerSignsOutcomeForm();
            return true;
        }
        else if(itemId == R.id.action_malaria_diagnosis){
            startHfMalariaFollowupForm();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Intent getPNCIntent(){
        JSONObject form = CoreJsonFormUtils.getAncPncForm(R.string.edit_member_form_title, CoreConstants.JSON_FORM.getFamilyMemberRegister(), memberObject, this);
        return CoreJsonFormUtils.getAncPncStartFormIntent(form, this);
    }

    protected static CommonPersonObjectClient getClientDetailsByBaseEntityID(@NonNull String baseEntityId) {
        return getCommonPersonObjectClient(baseEntityId);
    }

    @Override
    public void startFormActivity(JSONObject formJson) {
        startActivityForResult(CoreJsonFormUtils.getJsonIntent(this, formJson,
                org.smartregister.family.util.Utils.metadata().familyMemberFormActivity), JsonFormUtils.REQUEST_CODE_GET_JSON);
    }

    protected List<CommonPersonObjectClient> getChildren(MemberObject memberObject) {
        return pncMemberProfileInteractor.pncChildrenUnder29Days(memberObject.getBaseEntityId());
    }

    public CorePncMemberProfilePresenter getPncMemberProfilePresenter() {
        return (CorePncMemberProfilePresenter) presenter;
    }

    @Override
    public void registerPresenter() {
        presenter = new CorePncMemberProfilePresenter(this, new CorePncMemberProfileInteractor(), memberObject);
    }

    @Override
    protected void onCreation() {
        super.onCreation();
        initializeNotificationReferralRecyclerView();
    }

    protected void initializeNotificationReferralRecyclerView() {
        notificationAndReferralLayout = findViewById(R.id.notification_and_referral_row);
        notificationAndReferralRecyclerView = findViewById(R.id.notification_and_referral_recycler_view);
        notificationAndReferralRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pnc_member_profile_menu, menu);
        List<ChildModel> childModels = PNCDao.childrenForPncWoman(memberObject.getBaseEntityId());
        for (int i = 0; i < childModels.size(); i++) {
            menu.add(0, R.id.action_pnc_registration, 100 + i, getString(R.string.edit_child_form_title, childModels.get(i).getFirstName()));
            menuItemEditNames.put(getString(R.string.edit_child_form_title, childModels.get(i).getFirstName()), childModels.get(i).getBaseEntityId());
            menu.add(0, R.id.action_pnc_remove_baby, 700 + i, getString(R.string.remove_child_form_title, childModels.get(i).getFirstName()));
            menuItemRemoveNames.put(getString(R.string.remove_child_form_title, childModels.get(i).getFirstName()), childModels.get(i).getBaseEntityId());
            if (MalariaDao.isRegisteredForMalaria(baseEntityID)) {
                Utils.startAsyncTask(new MalariaFollowUpStatusTaskUtil(menu, baseEntityID), null);
            } else {
                menu.findItem(R.id.action_malaria_registration).setVisible(true);
            }
        }
        return true;
    }

    private boolean getEditMenuItem(MenuItem item) {
        if (getChildren(memberObject).size() > 0) {
            for (CommonPersonObjectClient child : getChildren(memberObject)) {
                for (Map.Entry<String, String> entry : menuItemEditNames.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(item.getTitle().toString()) && entry.getValue().equalsIgnoreCase(child.entityId())) {
                        CoreChildProfileInteractor childProfileInteractor = new CoreChildProfileInteractor();
                        JSONObject childEnrollmentForm = childProfileInteractor.getAutoPopulatedJsonEditFormString(CoreConstants.JSON_FORM.getChildRegister(), entry.getKey(), this, child);
                        startFormForEdit(org.smartregister.chw.anc.util.JsonFormUtils.setRequiredFieldsToFalseForPncChild(childEnrollmentForm, memberObject.getFamilyBaseEntityId(),
                                memberObject.getBaseEntityId()));
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == CoreConstants.ProfileActivityResults.CHANGE_COMPLETED) {
            Intent intent = new Intent(this, getPncRegisterActivityClass());
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            finish();
        }
    }

    public void startFormForEdit(JSONObject form) {
        try {
            startActivityForResult(CoreJsonFormUtils.getAncPncStartFormIntent(form, this), JsonFormUtils.REQUEST_CODE_GET_JSON);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void setupViews() {
        super.setupViews();
        imageViewCross = findViewById(R.id.tick_image);
        imageViewCross.setOnClickListener(this);
        updateToolbarTitle(this, R.id.toolbar_title, memberObject.getFamilyName());
    }

    @Override
    public void openFamilyDueServices() {
        Intent intent = new Intent(this, getFamilyProfileActivityClass());

        intent.putExtra(org.smartregister.family.util.Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID, memberObject.getFamilyBaseEntityId());
        intent.putExtra(org.smartregister.family.util.Constants.INTENT_KEY.FAMILY_HEAD, memberObject.getFamilyHead());
        intent.putExtra(org.smartregister.family.util.Constants.INTENT_KEY.PRIMARY_CAREGIVER, memberObject.getPrimaryCareGiver());
        intent.putExtra(org.smartregister.family.util.Constants.INTENT_KEY.FAMILY_NAME, memberObject.getFamilyName());

        intent.putExtra(CoreConstants.INTENT_KEY.SERVICE_DUE, hasDueServices);
        startActivity(intent);
    }

    @Override
    public void updateVisitNotDone(long value) {
        //Overridden
    }

    @Override
    public void setFamilyStatus(AlertStatus status) {
        TextView tvFamilyStatus;
        tvFamilyStatus = findViewById(R.id.textview_family_has);

        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);

        if (status == AlertStatus.complete) {
            hasDueServices = false;
            tvFamilyStatus.setText(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            hasDueServices = true;
            tvFamilyStatus.setText(R.string.family_has_services_due);
        } else if (status == AlertStatus.urgent) {
            hasDueServices = true;
            tvFamilyStatus.setText(NCUtils.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    @Override
    public MemberObject getMemberObject(String baseEntityID) {
        return PNCDao.getMember(baseEntityID);
    }

    protected abstract Class<? extends CoreFamilyProfileActivity> getFamilyProfileActivityClass();

    protected abstract CorePncMemberProfileInteractor getPncMemberProfileInteractor();

    protected abstract void removePncMember();

    protected abstract Class<? extends CorePncRegisterActivity> getPncRegisterActivityClass();

    protected abstract void startMalariaRegister();

    protected abstract void startFpRegister();

    protected abstract void startFpChangeMethod();

    protected abstract void startMalariaFollowUpVisit();

    protected abstract void startHivRegister();

    protected abstract void startTbRegister();

    protected abstract void getRemoveBabyMenuItem(MenuItem item);

    protected abstract void startHfMalariaFollowupForm();
}