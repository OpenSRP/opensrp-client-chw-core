package org.smartregister.chw.core.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.CoreHivProfileContract;
import org.smartregister.chw.core.contract.FamilyProfileExtendedContract;
import org.smartregister.chw.core.interactor.CoreHivProfileInteractor;
import org.smartregister.chw.core.presenter.CoreHivProfilePresenter;
import org.smartregister.chw.core.rule.HivFollowupRule;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.CoreJsonFormUtils;
import org.smartregister.chw.core.utils.HomeVisitUtil;
import org.smartregister.chw.hiv.activity.BaseHivProfileActivity;
import org.smartregister.chw.hiv.dao.HivDao;
import org.smartregister.chw.hiv.dao.HivIndexDao;
import org.smartregister.chw.hiv.domain.HivMemberObject;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.interactor.FamilyProfileInteractor;
import org.smartregister.family.model.BaseFamilyProfileModel;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.family.util.Utils;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static org.smartregister.chw.core.utils.Utils.updateToolbarTitle;

public abstract class CoreHivProfileActivity extends BaseHivProfileActivity implements FamilyProfileExtendedContract.PresenterCallBack, CoreHivProfileContract.View {
    protected RecyclerView notificationAndReferralRecyclerView;
    protected RelativeLayout notificationAndReferralLayout;

    protected static CommonPersonObjectClient getClientDetailsByBaseEntityID(@NonNull String baseEntityId) {
        CommonRepository commonRepository = Utils.context().commonrepository(Utils.metadata().familyMemberRegister.tableName);

        final CommonPersonObject commonPersonObject = commonRepository.findByBaseEntityId(baseEntityId);
        final CommonPersonObjectClient client =
                new CommonPersonObjectClient(commonPersonObject.getCaseId(), commonPersonObject.getDetails(), "");
        client.setColumnmaps(commonPersonObject.getColumnmaps());
        return client;

    }

    protected void initializeNotificationReferralRecyclerView() {
        notificationAndReferralLayout = findViewById(R.id.notification_and_referral_row);
        notificationAndReferralRecyclerView = findViewById(R.id.notification_and_referral_recycler_view);
        notificationAndReferralRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeNotificationReferralRecyclerView();
        updateToolbarTitle(this, R.id.toolbar_title, getHivMemberObject().getFamilyName());
    }

    @Override
    public void setupViews() {
        super.setupViews();
        new UpdateFollowUpVisitButtonTask(getHivMemberObject()).execute();
        new SetIndexClientsTask(getHivMemberObject()).execute();
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        setHivProfilePresenter(new CoreHivProfilePresenter(this, new CoreHivProfileInteractor(this), getHivMemberObject()));
        fetchProfileData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_registration) {
            startFormForEdit(R.string.registration_info,
                    CoreConstants.JSON_FORM.FAMILY_MEMBER_REGISTER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hiv_profile_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case JsonFormUtils.REQUEST_CODE_GET_JSON:
                if (resultCode == RESULT_OK) {
                    try {
                        String jsonString = data.getStringExtra(org.smartregister.family.util.Constants.JSON_FORM_EXTRA.JSON);
                        JSONObject form = new JSONObject(jsonString);
                        if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Utils.metadata().familyMemberRegister.updateEventType)) {
                            FamilyEventClient familyEventClient =
                                    new BaseFamilyProfileModel(getHivMemberObject().getFamilyName()).processUpdateMemberRegistration(jsonString, getHivMemberObject().getBaseEntityId());
                            new FamilyProfileInteractor().saveRegistration(familyEventClient, jsonString, true, (FamilyProfileContract.InteractorCallBack) getHivProfilePresenter());
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
                break;
            case Constants.REQUEST_CODE_HOME_VISIT:
                refreshViewOnHomeVisitResult();
                break;
            default:
                break;
        }
    }

    private void refreshViewOnHomeVisitResult() {
        Observable<Visit> observable = Observable.create(visitObservableEmitter -> {
            Visit lastVisit = HivDao.getLatestVisit(getHivMemberObject().getBaseEntityId(), org.smartregister.chw.hiv.util.Constants.EventType.FOLLOW_UP_VISIT);
            visitObservableEmitter.onNext(lastVisit);
            visitObservableEmitter.onComplete();
        });

        final Disposable[] disposable = new Disposable[1];
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Visit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(Visit visit) {
                        updateLastVisitRow(visit.getDate());
                        onMemberDetailsReloaded(getHivMemberObject());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {
                        disposable[0].dispose();
                        disposable[0] = null;
                    }
                });
    }

    public void onMemberDetailsReloaded(HivMemberObject hivMemberObject) {
        super.onMemberDetailsReloaded(hivMemberObject);
    }

    protected abstract void removeMember();

    public void startFormForEdit(Integer titleResource, String formName) {

        JSONObject form = null;
        CommonPersonObjectClient client = org.smartregister.chw.core.utils.Utils.clientForEdit(getHivMemberObject().getBaseEntityId());

        if (formName.equals(CoreConstants.JSON_FORM.getFamilyMemberRegister())) {
            form = CoreJsonFormUtils.getAutoPopulatedJsonEditMemberFormString(
                    (titleResource != null) ? getResources().getString(titleResource) : null,
                    CoreConstants.JSON_FORM.getFamilyMemberRegister(),
                    this, client,
                    Utils.metadata().familyMemberRegister.updateEventType, getHivMemberObject().getLastName(), false);
        } else if (formName.equals(CoreConstants.JSON_FORM.getAncRegistration())) {
            form = CoreJsonFormUtils.getAutoJsonEditAncFormString(
                    getHivMemberObject().getBaseEntityId(), this, formName, org.smartregister.chw.hiv.util.Constants.EventType.REGISTRATION, getResources().getString(titleResource));
        }

        try {
            assert form != null;
            startFormActivity(form, getHivMemberObject(), formName);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void startFormActivity(JSONObject formJson, HivMemberObject hivMemberObject, String formName) {
        Intent intent = org.smartregister.chw.core.utils.Utils.formActivityIntent(this, formJson.toString());
        intent.putExtra(org.smartregister.chw.hiv.util.Constants.HivMemberObject.MEMBER_OBJECT, hivMemberObject);

        Form form = new Form();
        form.setName(formName);
        form.setActionBarBackground(org.smartregister.chw.core.R.color.family_actionbar);
        form.setNavigationBackground(org.smartregister.chw.core.R.color.family_navigation);
        form.setHomeAsUpIndicator(org.smartregister.chw.core.R.mipmap.ic_cross_white);
        form.setPreviousLabel(getResources().getString(org.smartregister.chw.core.R.string.back));
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);

        startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);

    }

    private void updateFollowUpVisitButton(String buttonStatus) {
        switch (buttonStatus) {
            case CoreConstants.VISIT_STATE.DUE:
                setFollowUpButtonDue();
                break;
            case CoreConstants.VISIT_STATE.OVERDUE:
                setFollowUpButtonOverdue();
                break;
            default:
                break;
        }
    }

    public void updateFollowUpVisitStatusRow(Visit lastVisit) {
        setupFollowupVisitEditViews(VisitUtils.isVisitWithin24Hours(lastVisit));
    }

    @Override
    public Context getContext() {
        return this;
    }

    private class UpdateFollowUpVisitButtonTask extends AsyncTask<Void, Void, Void> {
        private HivMemberObject hivMemberObject;
        private HivFollowupRule hivFollowupRule;
        private Visit lastVisit;

        public UpdateFollowUpVisitButtonTask(HivMemberObject hivMemberObject) {
            this.hivMemberObject = hivMemberObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            lastVisit = HivDao.getLatestVisit(hivMemberObject.getBaseEntityId(), org.smartregister.chw.hiv.util.Constants.EventType.FOLLOW_UP_VISIT);
            Date lastVisitDate = lastVisit != null ? lastVisit.getDate() : null;
            hivFollowupRule = HomeVisitUtil.getHivVisitStatus(lastVisitDate, hivMemberObject.getHivRegistrationDate());
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (hivFollowupRule != null && (hivFollowupRule.getButtonStatus().equalsIgnoreCase(CoreConstants.VISIT_STATE.OVERDUE) ||
                    hivFollowupRule.getButtonStatus().equalsIgnoreCase(CoreConstants.VISIT_STATE.DUE))
            ) {
                updateFollowUpVisitButton(hivFollowupRule.getButtonStatus());
            }
            if (hivFollowupRule != null && hivFollowupRule.getDaysDifference() > 7)
                hideFollowUpVisitButton();

            updateFollowUpVisitStatusRow(lastVisit);
            Date lastVisitDate = lastVisit != null ? lastVisit.getDate() : null;
            updateLastVisitRow(lastVisitDate);
        }
    }

    private class SetIndexClientsTask extends AsyncTask<Void, Void, Integer> {
        private HivMemberObject hivMemberObject;

        public SetIndexClientsTask(HivMemberObject hivMemberObject) {
            this.hivMemberObject = hivMemberObject;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return HivIndexDao.getIndexContacts(hivMemberObject.getBaseEntityId()).size();
        }

        @Override
        protected void onPostExecute(Integer param) {
            setIndexClientsStatus(param > 0);
        }
    }
}
