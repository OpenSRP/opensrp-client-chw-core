package org.smartregister.chw.core.contract;

import org.json.JSONObject;
import org.smartregister.chw.hiv.contract.BaseHivProfileContract;
import org.smartregister.chw.hiv.domain.HivMemberObject;
import org.smartregister.repository.AllSharedPreferences;

public interface CoreHivProfileContract {

    interface View extends BaseHivProfileContract.View {
        void startFormActivity(JSONObject formJson, HivMemberObject hivMemberObject, String formName);
    }

    interface Presenter extends BaseHivProfileContract.Presenter {
        void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString) throws Exception;

        void startHivReferral();
    }

    interface Interactor extends BaseHivProfileContract.Interactor {
        void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString, String entityID) throws Exception;
    }
}
