package org.smartregister.chw.core.presenter;

import androidx.annotation.Nullable;

import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.chw.core.activity.CoreTbProfileActivity;
import org.smartregister.chw.core.contract.CoreTbProfileContract;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.tb.contract.BaseTbProfileContract;
import org.smartregister.chw.tb.domain.TbMemberObject;
import org.smartregister.chw.tb.presenter.BaseTbProfilePresenter;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.repository.AllSharedPreferences;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class CoreTbProfilePresenter extends BaseTbProfilePresenter implements CoreTbProfileContract.Presenter, FamilyProfileContract.InteractorCallBack {
    private BaseTbProfileContract.Interactor interactor;
    private WeakReference<BaseTbProfileContract.View> view;
    private TbMemberObject tbMemberObject;

    public CoreTbProfilePresenter(BaseTbProfileContract.View view, BaseTbProfileContract.Interactor interactor, TbMemberObject tbMemberObject) {
        super(view, interactor, tbMemberObject);
        this.interactor = interactor;
        this.view = new WeakReference<>(view);
        this.tbMemberObject = tbMemberObject;
    }

    @Override
    public void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString) throws Exception {
        ((CoreTbProfileContract.Interactor) interactor).createReferralEvent(allSharedPreferences, jsonString, tbMemberObject.getBaseEntityId());
    }

    @Override
    public void startTbReferral() {
        try {
            getView().startFormActivity((new FormUtils()).getFormJsonFromRepositoryOrAssets(((CoreTbProfileActivity) getView()), CoreConstants.JSON_FORM.getTbReferralForm()), tbMemberObject);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    @Nullable
    public CoreTbProfileContract.View getView() {
        if (view != null) {
            return (CoreTbProfileContract.View) view.get();
        } else {
            return null;
        }
    }

    @Override
    public void startFormForEdit(CommonPersonObjectClient client) {
        // TODO
    }

    @Override
    public void refreshProfileTopSection(CommonPersonObjectClient client) {
        // TODO
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        // TODO
    }

    @Override
    public void onNoUniqueId() {
        // TODO
    }

    @Override
    public void onRegistrationSaved(boolean editMode, boolean isSaved, FamilyEventClient familyEventClient) {
        if (isSaved) {
            refreshProfileData();
            Timber.d("On member profile registration saved");
        }
    }
}
