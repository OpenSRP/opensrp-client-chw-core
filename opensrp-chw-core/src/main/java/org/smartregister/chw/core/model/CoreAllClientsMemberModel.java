package org.smartregister.chw.core.model;

import org.smartregister.chw.core.contract.CoreAllClientsMemberContract;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.domain.FamilyEventClient;
import org.smartregister.family.util.JsonFormUtils;

public class CoreAllClientsMemberModel implements CoreAllClientsMemberContract.Model {
    @Override
    public FamilyEventClient processJsonForm(String jsonString, String familyBaseEntityId) {
        return JsonFormUtils.processFamilyUpdateForm(FamilyLibrary.getInstance().context().allSharedPreferences(), jsonString, familyBaseEntityId);
    }
}
