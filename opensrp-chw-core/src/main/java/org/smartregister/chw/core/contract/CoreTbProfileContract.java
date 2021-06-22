package org.smartregister.chw.core.contract;

import org.json.JSONObject;
import org.smartregister.chw.tb.contract.BaseTbProfileContract;
import org.smartregister.chw.tb.domain.TbMemberObject;
import org.smartregister.repository.AllSharedPreferences;

public interface CoreTbProfileContract {

    interface View extends BaseTbProfileContract.View {
        void startFormActivity(JSONObject formJson, TbMemberObject tbMemberObject);
    }

    interface Presenter extends BaseTbProfileContract.Presenter {
        void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString) throws Exception;

        void startTbReferral();
    }

    interface Interactor extends BaseTbProfileContract.Interactor {
        void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString, String entityID) throws Exception;
    }
}
