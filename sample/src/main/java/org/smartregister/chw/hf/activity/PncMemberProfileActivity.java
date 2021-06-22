package org.smartregister.chw.hf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.activity.CorePncMemberProfileActivity;
import org.smartregister.chw.core.activity.CorePncRegisterActivity;
import org.smartregister.chw.core.interactor.CorePncMemberProfileInteractor;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.hf.R;
import org.smartregister.chw.hf.adapter.ReferralCardViewAdapter;
import org.smartregister.chw.hf.contract.PncMemberProfileContract;
import org.smartregister.chw.hf.interactor.PncMemberProfileInteractor;
import org.smartregister.chw.hf.model.FamilyProfileModel;
import org.smartregister.chw.hf.presenter.PncMemberProfilePresenter;
import org.smartregister.chw.malaria.dao.MalariaDao;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.Task;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.interactor.FamilyProfileInteractor;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;

import java.util.Date;
import java.util.Set;

import timber.log.Timber;

public class PncMemberProfileActivity extends CorePncMemberProfileActivity implements PncMemberProfileContract.View {

    public RelativeLayout referralRow;
    public RecyclerView referralRecyclerView;
    private CommonPersonObjectClient commonPersonObjectClient;
    private PncMemberProfilePresenter pncMemberProfilePresenter;

    public static void startMe(Activity activity, String baseEntityID, CommonPersonObjectClient commonPersonObjectClient) {
        Intent intent = new Intent(activity, PncMemberProfileActivity.class);
        intent.putExtra(Constants.ANC_MEMBER_OBJECTS.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(CoreConstants.INTENT_KEY.CLIENT, commonPersonObjectClient);
        activity.startActivity(intent);
    }

    @Override
    public void openMedicalHistory() {
        PncMedicalHistoryActivity.startMe(this, memberObject);
    }

    @Override
    protected void startMalariaRegister() {
        MalariaRegisterActivity.startMalariaRegistrationActivity(this, memberObject.getBaseEntityId());
    }

    @Override
    protected void startFpRegister() {
        //TODO implement start FP register for HF
    }

    @Override
    protected void startHivRegister() {
        //TODO implement start FP register for HF
    }

    @Override
    protected void startTbRegister() {
        //TODO implement start FP register for HF
    }

    @Override
    protected void startFpChangeMethod() {
        //TODO implement start FP change method for HF
    }

    public void setReferralTasks(Set<Task> taskList) {
        if (referralRecyclerView != null && taskList.size() > 0) {
            RecyclerView.Adapter mAdapter = new ReferralCardViewAdapter(taskList, this, memberObject, getFamilyHeadName(),
                    getFamilyHeadPhoneNumber(), getCommonPersonObjectClient(), CoreConstants.REGISTERED_ACTIVITIES.PNC_REGISTER_ACTIVITY);
            referralRecyclerView.setAdapter(mAdapter);
            referralRow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreation() {
        super.onCreation();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setCommonPersonObjectClient((CommonPersonObjectClient) getIntent().getSerializableExtra(CoreConstants.INTENT_KEY.CLIENT));
        }
    }

    @Override
    public void registerPresenter() {
        presenter = new PncMemberProfilePresenter(this, new PncMemberProfileInteractor(), memberObject);
    }

    @Override
    public void initializeFloatingMenu() {
        super.initializeFloatingMenu();
        if (baseAncFloatingMenu != null) {
            FloatingActionButton floatingActionButton = baseAncFloatingMenu.findViewById(R.id.anc_fab);
            if (floatingActionButton != null)
                floatingActionButton.setImageResource(R.drawable.floating_call);
        }
    }

    @Override
    public void setUpComingServicesStatus(String service, AlertStatus status, Date date) {
        view_most_due_overdue_row.setVisibility(View.GONE);
        rlUpcomingServices.setVisibility(View.GONE);
    }

    public CommonPersonObjectClient getCommonPersonObjectClient() {
        return commonPersonObjectClient;
    }

    public void setCommonPersonObjectClient(CommonPersonObjectClient commonPersonObjectClient) {
        this.commonPersonObjectClient = commonPersonObjectClient;
    }

    private void initializeReferralsRecyclerView() {
        referralRecyclerView = findViewById(R.id.referral_card_recycler_view);
        referralRow = findViewById(R.id.referal_row);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        referralRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReferralsRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((PncMemberProfileContract.Presenter) presenter()).fetchReferralTasks();
        if (referralRecyclerView != null && referralRecyclerView.getAdapter() != null) {
            referralRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_pnc_registration).setVisible(false);
        menu.findItem(R.id.action__pnc_remove_member).setVisible(false);
        menu.findItem(R.id.action__pnc_danger_sign_outcome).setVisible(true);
        if (MalariaDao.isRegisteredForMalaria(baseEntityID)) {
            menu.findItem(R.id.action_malaria_followup_visit).setVisible(true);
            menu.findItem(R.id.action_malaria_diagnosis).setVisible(false);
        } else {
            menu.findItem(R.id.action_malaria_diagnosis).setVisible(true);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JsonFormUtils.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            String jsonString = data.getStringExtra(org.smartregister.family.util.Constants.JSON_FORM_EXTRA.JSON);
            try {
                JSONObject form = new JSONObject(jsonString);
                if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Utils.metadata().familyMemberRegister.updateEventType)) {

                    FamilyEventClient familyEventClient =
                            new FamilyProfileModel(memberObject.getFamilyName()).processUpdateMemberRegistration(jsonString, memberObject.getBaseEntityId());
                    new FamilyProfileInteractor().saveRegistration(familyEventClient, jsonString, true, (FamilyProfileContract.InteractorCallBack) pncMemberProfilePresenter());
                }
            } catch (JSONException jsonException) {
                Timber.e(jsonException);
            }
        }
    }

    @Override
    public void setupViews() {
        super.setupViews();
    }

    @Override
    public void setFamilyStatus(AlertStatus status) {
        view_family_row.setVisibility(View.GONE);
        rlFamilyServicesDue.setVisibility(View.GONE);
    }

    @Override
    protected Class<? extends CoreFamilyProfileActivity> getFamilyProfileActivityClass() {
        return FamilyProfileActivity.class;
    }

    @Override
    protected CorePncMemberProfileInteractor getPncMemberProfileInteractor() {
        return new PncMemberProfileInteractor();
    }

    @Override
    protected void removePncMember() {
        //TODO implement functionality to remove PNC member
    }

    @Override
    protected Class<? extends CorePncRegisterActivity> getPncRegisterActivityClass() {
        return PncRegisterActivity.class;
    }

    @Override
    protected void startMalariaFollowUpVisit() {
//        TODO implement functionality to malaria home visit
    }

    @Override
    protected void startHfMalariaFollowupForm() {
        //        TODO implement functionality to malaria HF follow up
    }

    @Override
    protected void getRemoveBabyMenuItem(MenuItem item) {

    }

    public PncMemberProfileContract.Presenter pncMemberProfilePresenter() {
        if (pncMemberProfilePresenter == null) {
            pncMemberProfilePresenter = new PncMemberProfilePresenter(this, new PncMemberProfileInteractor(), memberObject);
        }
        return pncMemberProfilePresenter;
    }
}
