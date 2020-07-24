package org.smartregister.chw.core.dao;

import android.util.Pair;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.dao.AbstractDao;
import org.smartregister.thinkmd.model.FHIRBundleModel;

import static org.smartregister.chw.core.dao.ChildDao.getChildProfileData;
import static org.smartregister.chw.core.utils.Utils.fetchMUACValues;
import static org.smartregister.chw.core.utils.Utils.getRandomGeneratedId;

public class FHIRBundleDao extends AbstractDao {

    public static FHIRBundleModel fetchFHIRDateModel(String childBaseEntityId){
        FHIRBundleModel model = new FHIRBundleModel();
        model.setRandomlyGeneratedId(getRandomGeneratedId());
        model.setEncounterId(getRandomGeneratedId());
        Triple<String, String, String> userProfile = getChildProfileData(childBaseEntityId);
        model.setGender(userProfile.getRight());
        model.setDob(userProfile.getMiddle());
        model.setAgeInDays(userProfile.getLeft());
        Pair<String, String> muacPair = fetchMUACValues(childBaseEntityId);
        model.setMUACValueCode(muacPair.first);
        model.setMUACValueDisplay(muacPair.second);
        //Todo: these values needs to be query and set into model
        model.setPractitionerId(null);
        model.setPatientId(null);
        model.setUserName(null);
        model.setLocationId(null);
        model.setUniqueIdGeneratedForThinkMD(null);

        return model;
    }

}
