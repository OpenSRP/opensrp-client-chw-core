package org.smartregister.chw.core.interactor;

import android.content.Context;
import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.FamilyCallDialogContract;
import org.smartregister.chw.core.model.FamilyCallDialogModel;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;

public class FamilyCallDialogInteractor implements FamilyCallDialogContract.Interactor {

    private AppExecutors appExecutors;
    private String familyBaseEntityId;


    public FamilyCallDialogInteractor(String familyBaseEntityId) {
        this(new AppExecutors(), familyBaseEntityId);
    }

    @VisibleForTesting
    FamilyCallDialogInteractor(AppExecutors appExecutors, String familyBaseEntityId) {
        this.appExecutors = appExecutors;
        this.familyBaseEntityId = familyBaseEntityId;
    }

    @Override
    public void getHeadOfFamily(final FamilyCallDialogContract.Presenter presenter, final Context context) {

        Runnable runnable = () -> {

            final CommonPersonObject personObject = getCommonRepository(Utils.metadata().familyRegister.tableName).findByBaseEntityId(familyBaseEntityId);
            final CommonPersonObjectClient client = new CommonPersonObjectClient(personObject.getCaseId(), personObject.getDetails(), "");
            client.setColumnmaps(personObject.getColumnmaps());

            String primaryCaregiverID = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.PRIMARY_CAREGIVER, false);
            String familyHeadID = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.FAMILY_HEAD, false);

            if (primaryCaregiverID != null) {
                // load primary care giver
                final FamilyCallDialogModel headModel = prepareModel(context, familyHeadID, primaryCaregiverID, true);
                appExecutors.mainThread().execute(() -> presenter.updateHeadOfFamily((headModel == null || headModel.getPhoneNumber() == null) ? null : headModel));
            }

            if (familyHeadID != null && !familyHeadID.equals(primaryCaregiverID) && primaryCaregiverID != null) {
                final FamilyCallDialogModel careGiverModel = prepareModel(context, familyHeadID, primaryCaregiverID, false);
                appExecutors.mainThread().execute(() -> presenter.updateCareGiver((careGiverModel == null || careGiverModel.getPhoneNumber() == null) ? null : careGiverModel));

            }
        };

        if (familyBaseEntityId != null) {
            appExecutors.diskIO().execute(runnable);
        }
    }

    public CommonRepository getCommonRepository(String tableName) {
        return Utils.context().commonrepository(tableName);
    }

    private FamilyCallDialogModel prepareModel(
            Context context,
            String familyHeadID, String primaryCaregiverID,
            Boolean isHead
    ) {

        if (primaryCaregiverID.equalsIgnoreCase(familyHeadID) && !isHead) {
            return null;
        }

        String baseID = (isHead && StringUtils.isNotBlank(familyHeadID)) ? familyHeadID : primaryCaregiverID;

        final CommonPersonObject personObject = getCommonRepository(Utils.metadata().familyMemberRegister.tableName).findByBaseEntityId(baseID);
        FamilyCallDialogModel model = new FamilyCallDialogModel();

        if (personObject != null) {
            final CommonPersonObjectClient client = new CommonPersonObjectClient(personObject.getCaseId(), personObject.getDetails(), "");
            client.setColumnmaps(personObject.getColumnmaps());
            String phoneNumber = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.PHONE_NUMBER, false);
            String firstName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.FIRST_NAME, false);
            String lastName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.LAST_NAME, false);
            String middleName = Utils.getValue(client.getColumnmaps(), DBConstants.KEY.MIDDLE_NAME, false);
            model.setPhoneNumber(phoneNumber);
            model.setName(String.format("%s %s", String.format("%s %s", firstName, middleName).trim(), lastName));

            model.setRole((primaryCaregiverID.toLowerCase().equals(familyHeadID.toLowerCase()))
                    ? String.format("%s, %s", context.getString(R.string.head_of_family), context.getString(R.string.care_giver))
                    : (isHead ? context.getString(R.string.head_of_family)
                    : context.getString(R.string.care_giver)));
        }

        return model;
    }

}
