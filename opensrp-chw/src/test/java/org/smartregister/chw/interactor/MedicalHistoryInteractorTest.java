package org.smartregister.chw.interactor;

import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.BaseUnitTest;
import org.smartregister.chw.contract.MedicalHistoryContract;
import org.smartregister.chw.fragment.GrowthNutritionInputFragment;
import org.smartregister.chw.util.ChildDBConstants;
import org.smartregister.chw.util.ServiceContent;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.immunization.domain.ServiceRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.smartregister.chw.util.ChildDBConstants.KEY.BIRTH_CERT;
import static org.smartregister.chw.util.ChildDBConstants.KEY.BIRTH_CERT_NOTIFIICATION;
import static org.smartregister.chw.util.ChildDBConstants.KEY.ILLNESS_ACTION;
import static org.smartregister.chw.util.ChildDBConstants.KEY.ILLNESS_DATE;
import static org.smartregister.chw.util.ChildDBConstants.KEY.ILLNESS_DESCRIPTION;

public class MedicalHistoryInteractorTest extends BaseUnitTest {
    private static final String TAG = MedicalHistoryInteractorTest.class.getCanonicalName();

    private MedicalHistoryInteractor interactor;
    @Mock
    private MedicalHistoryContract.InteractorCallBack callBack;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interactor = Mockito.spy(MedicalHistoryInteractor.class);
    }

    @After
    public void tearDown() throws Exception {
        // TODO
        Log.d(TAG, "tearDown implementation");
    }

    @Test
    public void fetchFullyImmunizationData() {
        // TODO
        Log.d(TAG, "fetchFullyImmunizationData implementation");
    }

    @Test
    public void fetchBirthAndIllnessData_true_birthdata() {

        AppExecutors appExecutors = Mockito.spy(AppExecutors.class);
        Whitebox.setInternalState(interactor, "appExecutors", appExecutors);
        String caseId = "cd6f66c8-3587-4c4d-b26d-f8753ba9dfa4";
        String name = "";
        Map<String, String> mapBirth = new HashMap<>();
        mapBirth.put(BIRTH_CERT, "yes");
        mapBirth.put(BIRTH_CERT_NOTIFIICATION, "no");
        CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient(caseId, mapBirth, name);
        commonPersonObjectClient.setColumnmaps(mapBirth);
        interactor.fetchBirthAndIllnessData(commonPersonObjectClient, callBack);
        verify(callBack, never()).updateBirthCertification(Mockito.any(ArrayList.class));

    }

    @Test
    public void fetchBirthAndIllnessData_true_illnessdata() {

        AppExecutors appExecutors = Mockito.spy(AppExecutors.class);
        Whitebox.setInternalState(interactor, "appExecutors", appExecutors);
        String caseId = "cd6f66c8-3587-4c4d-b26d-f8753ba9dfa4";
        String name = "";
        Map<String, String> mapIllness = new HashMap<>();
        mapIllness.put(ILLNESS_DATE, "04-02-2019");
        mapIllness.put(ILLNESS_DESCRIPTION, "description");
        mapIllness.put(ILLNESS_ACTION, "managed");
        CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient(caseId, mapIllness, name);
        commonPersonObjectClient.setColumnmaps(mapIllness);
        android.content.Context ctx = Mockito.mock(android.content.Context.class);
        PowerMockito.when(interactor.getContext()).thenReturn(ctx);
        interactor.fetchBirthAndIllnessData(commonPersonObjectClient, callBack);
        verify(callBack, never()).updateBirthCertification(Mockito.any(ArrayList.class));

    }

    @Test
    public void addContentInitialBreastfeeding() throws Exception{
        ServiceContent serviceContent = new ServiceContent();
        ServiceRecord initialServiceRecord = new ServiceRecord();
        initialServiceRecord.setType(GrowthNutritionInputFragment.GROWTH_TYPE.EXCLUSIVE.getValue());
        initialServiceRecord.setName(ChildDBConstants.KEY.CHILD_BF_HR);
        initialServiceRecord.setValue("yes");
        Whitebox.invokeMethod(interactor,"addContent",serviceContent,initialServiceRecord);
        Assert.assertEquals("Early initiation breastfeeding: Yes",serviceContent.getServiceName());
    }

}