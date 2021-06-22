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

import org.json.JSONObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.util.Constants;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.CoreTbProfileContract;
import org.smartregister.chw.core.contract.FamilyProfileExtendedContract;
import org.smartregister.chw.core.interactor.CoreTbProfileInteractor;
import org.smartregister.chw.core.presenter.CoreTbProfilePresenter;
import org.smartregister.chw.core.rule.TbFollowupRule;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.CoreJsonFormUtils;
import org.smartregister.chw.core.utils.HomeVisitUtil;
import org.smartregister.chw.tb.activity.BaseTbProfileActivity;
import org.smartregister.chw.tb.dao.TbDao;
import org.smartregister.chw.tb.domain.TbMemberObject;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
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

public abstract class CoreTbProfileActivity extends BaseTbProfileActivity implements FamilyProfileExtendedContract.PresenterCallBack, CoreTbProfileContract.View {
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
        updateToolbarTitle(this, R.id.toolbar_title, getTbMemberObject().getFamilyName());
    }

    @Override
    public void setupViews() {
        super.setupViews();
        new UpdateFollowUpVisitButtonTask(getTbMemberObject()).execute();
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        setTbProfilePresenter(new CoreTbProfilePresenter(this, new CoreTbProfileInteractor(this), getTbMemberObject()));
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
        } else if (itemId == R.id.action_close_tb_case) {
            startTbCaseClosure();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_profile_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_HOME_VISIT) {
            refreshViewOnHomeVisitResult();
        }
    }

    private void refreshViewOnHomeVisitResult() {
        Observable<Visit> observable = Observable.create(visitObservableEmitter -> {
            Visit lastVisit = TbDao.getLatestVisit(getTbMemberObject().getBaseEntityId(), org.smartregister.chw.tb.util.Constants.EventType.FOLLOW_UP_VISIT);
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
                        onMemberDetailsReloaded(getTbMemberObject());
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

    public void onMemberDetailsReloaded(TbMemberObject tbMemberObject) {
        super.onMemberDetailsReloaded(tbMemberObject);
    }

    protected abstract void removeMember();

    protected abstract void startTbCaseClosure();

    public void startFormForEdit(Integer titleResource, String formName) {

        JSONObject form = null;
        CommonPersonObjectClient client = org.smartregister.chw.core.utils.Utils.clientForEdit(getTbMemberObject().getBaseEntityId());

        if (formName.equals(CoreConstants.JSON_FORM.getFamilyMemberRegister())) {
            form = CoreJsonFormUtils.getAutoPopulatedJsonEditMemberFormString(
                    (titleResource != null) ? getResources().getString(titleResource) : null,
                    CoreConstants.JSON_FORM.getFamilyMemberRegister(),
                    this, client,
                    Utils.metadata().familyMemberRegister.updateEventType, getTbMemberObject().getLastName(), false);
        } else if (formName.equals(CoreConstants.JSON_FORM.getAncRegistration())) {
            form = CoreJsonFormUtils.getAutoJsonEditAncFormString(
                    getTbMemberObject().getBaseEntityId(), this, formName, org.smartregister.chw.tb.util.Constants.EventType.REGISTRATION, getResources().getString(titleResource));
        }

        try {
            assert form != null;
            startFormActivity(form, getTbMemberObject());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void startFormActivity(JSONObject formJson, TbMemberObject tbMemberObject) {
        Intent intent = org.smartregister.chw.core.utils.Utils.formActivityIntent(this, formJson.toString());
        intent.putExtra(org.smartregister.chw.tb.util.Constants.TbMemberObject.MEMBER_OBJECT, tbMemberObject);
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
        private TbMemberObject tbMemberObject;
        private TbFollowupRule tbFollowupRule;
        private Visit lastVisit;

        public UpdateFollowUpVisitButtonTask(TbMemberObject tbMemberObject) {
            this.tbMemberObject = tbMemberObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            lastVisit = TbDao.getLatestVisit(tbMemberObject.getBaseEntityId(), org.smartregister.chw.tb.util.Constants.EventType.FOLLOW_UP_VISIT);
            Date lastVisitDate = lastVisit != null ? lastVisit.getDate() : null;
            tbFollowupRule = HomeVisitUtil.getTbVisitStatus(lastVisitDate, tbMemberObject.getTbRegistrationDate());
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (tbFollowupRule != null && (tbFollowupRule.getButtonStatus().equalsIgnoreCase(CoreConstants.VISIT_STATE.OVERDUE) ||
                    tbFollowupRule.getButtonStatus().equalsIgnoreCase(CoreConstants.VISIT_STATE.DUE))
            ) {
                updateFollowUpVisitButton(tbFollowupRule.getButtonStatus());
            }

            if (tbFollowupRule != null && tbFollowupRule.getDaysDifference() > 7)
                hideFollowUpVisitButton();

            updateFollowUpVisitStatusRow(lastVisit);

            Date lastVisitDate = lastVisit != null ? lastVisit.getDate() : null;
            updateLastVisitRow(lastVisitDate);
        }
    }
}
