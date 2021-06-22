package org.smartregister.chw.core.presenter;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.chw.core.activity.CoreAllClientsMemberProfileActivity;
import org.smartregister.chw.core.contract.CoreAllClientsMemberContract;
import org.smartregister.chw.core.interactor.CoreAllClientsMemberInteractor;
import org.smartregister.chw.core.model.CoreAllClientsMemberModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;

import java.lang.ref.WeakReference;

public class CoreAllClientsMemberPresenter implements CoreAllClientsMemberContract.Presenter, FamilyProfileContract.InteractorCallBack, FamilyOtherMemberContract.InteractorCallBack {

    private CoreAllClientsMemberContract.Interactor interactor;

    private WeakReference<CoreAllClientsMemberContract.View> view;
    private String baseEntityId;

    public CoreAllClientsMemberPresenter(CoreAllClientsMemberProfileActivity allClientsMemberProfileActivity, String baseEntityId) {
        this.baseEntityId = baseEntityId;
        interactor = new CoreAllClientsMemberInteractor();
        view = new WeakReference<>(allClientsMemberProfileActivity);
    }

    @Override
    public void updateLocationInfo(String jsonString, String familyBaseEntityId) {
        interactor.updateLocationInfo(jsonString, new CoreAllClientsMemberModel().processJsonForm(jsonString, familyBaseEntityId), this);
    }

    @Override
    public CoreAllClientsMemberContract.View getView() {
        return view.get();
    }

    @Override
    public void startFormForEdit(CommonPersonObjectClient client) {
        //Overridden: Not Needed
    }

    @Override
    public void refreshProfileTopSection(CommonPersonObjectClient client) {
        //implement
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
        //Overridden: Not Needed
    }

    @Override
    public void onNoUniqueId() {
        //Overridden: Not Needed
    }

    @Override
    public void onRegistrationSaved(boolean editMode, boolean isSaved, FamilyEventClient familyEventClient) {
        CoreAllClientsMemberProfileActivity view = (CoreAllClientsMemberProfileActivity) getView();
        if (editMode) {
            view.hideProgressDialog();

            refreshProfileView();

            view.refreshList();
        }
    }

    @Override
    public void refreshProfileView() {
        interactor.updateProfileInfo(baseEntityId, this);
    }
}
