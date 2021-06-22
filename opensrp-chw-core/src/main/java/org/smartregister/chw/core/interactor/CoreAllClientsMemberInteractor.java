package org.smartregister.chw.core.interactor;

import org.smartregister.chw.core.contract.CoreAllClientsMemberContract;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.family.contract.FamilyOtherMemberContract;
import org.smartregister.family.contract.FamilyProfileContract;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.interactor.FamilyOtherMemberProfileInteractor;
import org.smartregister.family.interactor.FamilyProfileInteractor;

public class CoreAllClientsMemberInteractor extends FamilyOtherMemberProfileInteractor implements CoreAllClientsMemberContract.Interactor {

    private FamilyProfileInteractor familyProfileInteractor;

    public CoreAllClientsMemberInteractor() {
        familyProfileInteractor = new FamilyProfileInteractor();
    }

    @Override
    public void updateLocationInfo(String jsonString, FamilyEventClient familyEventClient, FamilyProfileContract.InteractorCallBack interactorCallback) {
        familyEventClient.getEvent().setEntityType(CoreConstants.TABLE_NAME.INDEPENDENT_CLIENT);
        familyProfileInteractor.saveRegistration(familyEventClient, jsonString, true, interactorCallback);
    }

    @Override
    public void updateProfileInfo(String baseEntityId, FamilyOtherMemberContract.InteractorCallBack callback) {
        refreshProfileView(baseEntityId, callback);
    }
}
