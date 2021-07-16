package org.smartregister.chw.core.interactor;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.core.contract.FamilyCallDialogContract;
import org.smartregister.chw.core.model.FamilyCallDialogModel;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.family.util.DBConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FamilyCallDialogInteractorTest extends BaseUnitTest {

    private FamilyCallDialogInteractor familyCallDialogInteractor;

    private final String familyBaseEntityId = "71dafd81-6c0d-4f78-97b6-3b596878a640";
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final Map<String, String> columnMaps = new HashMap<>();

    @Before
    public void setUp(){
        Executor executor = Runnable::run;
        AppExecutors executors = new AppExecutors(executor, executor, executor);
        familyCallDialogInteractor = spy(new FamilyCallDialogInteractor(executors, familyBaseEntityId));
        final CommonPersonObject commonPersonObject = new CommonPersonObject("caseId", "relationalid", new HashMap<>(), commonRepository.TABLE_NAME);
        columnMaps.put(DBConstants.KEY.FAMILY_HEAD, "");
        columnMaps.put(DBConstants.KEY.PHONE_NUMBER, "phone number");
        columnMaps.put(DBConstants.KEY.FIRST_NAME, "first name");
        columnMaps.put(DBConstants.KEY.LAST_NAME, "last name");
        columnMaps.put(DBConstants.KEY.MIDDLE_NAME, "middle name");
        commonPersonObject.setColumnmaps(columnMaps);
        when(commonRepository.findByBaseEntityId(anyString())).thenReturn(commonPersonObject);
        doReturn("FamilyRegisterTableName").when(familyCallDialogInteractor).familyRegisterTableName();
        doReturn("FamilyRegisterTableName").when(familyCallDialogInteractor).familyMemberTableName();
        doReturn(commonRepository).when(familyCallDialogInteractor).getCommonRepository(anyString());
    }

    @Test
    public void testGetHeadOfFamilyUpdatesHeadOfFamilyWhenCaregiver(){
        columnMaps.put(DBConstants.KEY.PRIMARY_CAREGIVER, "d04aebf7-93d6-4aeb-9565-0c0e8be8260b");
        FamilyCallDialogContract.Presenter presenter = mock(FamilyCallDialogContract.Presenter.class);
        Context context = mock(Context.class);

        familyCallDialogInteractor.getHeadOfFamily(presenter, context);
        verify(presenter).updateHeadOfFamily(any(FamilyCallDialogModel.class));
    }

    @Test
    public void testGetHeadOfFamilyUpdatesCareGiverWhenCareGiverAndIsIndependent(){
        columnMaps.put(DBConstants.KEY.PRIMARY_CAREGIVER, "d04aebf7-93d6-4aeb-9565-0c0e8be8260b");
        columnMaps.put(DBConstants.KEY.ENTITY_TYPE, CoreConstants.TABLE_NAME.INDEPENDENT_CLIENT);
        FamilyCallDialogContract.Presenter presenter = mock(FamilyCallDialogContract.Presenter.class);
        Context context = mock(Context.class);

        familyCallDialogInteractor.getHeadOfFamily(presenter, context);
        verify(presenter).updateCareGiver(any(FamilyCallDialogModel.class));
    }

}
