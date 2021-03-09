package org.smartregister.chw.core.fragment;

import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.core.contract.FamilyCallDialogContract;
import org.smartregister.chw.core.presenter.FamilyCallDialogPresenter;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class FamilyCallDialogFragmentTest extends BaseUnitTest {
    @Mock
    private CommonRepository commonRepository;
    @Mock
    private Context context;

    @Mock
    private FamilyCallDialogFragment fragment;

    @Mock
    private FamilyCallDialogContract.Model model;

    @Mock
    private TextView textView;

    @Mock
    private LinearLayout linearLayout;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();

        fragment = new FamilyCallDialogFragment();

        CoreLibrary.init(context);
        when(context.commonrepository(anyString())).thenReturn(commonRepository);
        Context.bindtypes = new ArrayList<>();
        SyncStatusBroadcastReceiver.init(activity);
        activity.getFragmentManager().beginTransaction().add(fragment, "Presenter").commit();

        ReflectionHelpers.setField(fragment, "tvFamilyHeadTitle", textView);
        ReflectionHelpers.setField(fragment, "tvFamilyHeadName", textView);
        ReflectionHelpers.setField(fragment, "tvFamilyHeadPhone", textView);
        ReflectionHelpers.setField(fragment, "tvCareGiverTitle", textView);
        ReflectionHelpers.setField(fragment, "tvCareGiverName", textView);
        ReflectionHelpers.setField(fragment, "tvCareGiverPhone", textView);
        ReflectionHelpers.setField(fragment, "dialogTitle", textView);
        ReflectionHelpers.setField(fragment, "llFamilyHead", linearLayout);
        ReflectionHelpers.setField(fragment, "llCareGiver", linearLayout);
        ReflectionHelpers.setField(fragment, "familyBaseEntityId", "12345");

    }

    @Test
    public void testInitializePresenter() {
        fragment.initializePresenter();
        FamilyCallDialogPresenter presenter = new FamilyCallDialogPresenter(fragment, "12345");
        Assert.assertTrue(presenter instanceof FamilyCallDialogPresenter);
        assertNotNull(presenter);
    }

    @Test
    public void testSetPendingCallRequest() {
        FamilyCallDialogContract.Dialer dialer = null;
        fragment.setPendingCallRequest(dialer);
        assertNull(fragment.getPendingCallRequest());
    }

    @Test
    public void testSetFamilyBaseEntityId() {
        fragment.setFamilyBaseEntityId("12345");
        Assert.assertEquals("12345", fragment.familyBaseEntityId);
    }


    @Test
    public void whenRefreshHeadOfFamilyViewAnswered() {
        when(model.getName()).thenReturn("Family head Name");
        when(model.getRole()).thenReturn("Family Head");
        fragment.refreshHeadOfFamilyView(model);
        assertNotNull(model);
        assertNull(model.getPhoneNumber());
        Assert.assertEquals("Family head Name", model.getName());
        Assert.assertEquals("Family Head", model.getRole());
    }


    @Test
    public void whenRefreshHeadOfFamilyViewAnsweredWhenPhoneNumberIsNotBlank() {
        when(model.getPhoneNumber()).thenReturn("0727272828");
        when(model.getName()).thenReturn("Family head Name");
        when(model.getRole()).thenReturn("Family Head");
        fragment.refreshHeadOfFamilyView(model);
        assertNotNull(model);
        Assert.assertEquals("0727272828", model.getPhoneNumber());
        Assert.assertEquals("Family head Name", model.getName());
        Assert.assertEquals("Family Head", model.getRole());
    }


    @Test
    public void whenRefreshCareGiverViewAnswered() {
        when(model.getName()).thenReturn("Care giver Name");
        when(model.getRole()).thenReturn("Care Giver");
        fragment.refreshCareGiverView(model);
        assertNotNull(model);
        assertNull(model.getPhoneNumber());
        Assert.assertEquals("Care giver Name", model.getName());
        Assert.assertEquals("Care Giver", model.getRole());
    }

    @Test
    public void whenRefreshCareGiverViewAnsweredWhenModelIsIndependent() {
        when(model.getName()).thenReturn("Care giver Name");
        when(model.getRole()).thenReturn("Care Giver");
        when(model.isIndependent()).thenReturn(true);
        Pair<String, String> pair = new Pair<>(null, null);
        when(model.getOtherContact()).thenReturn(pair);

        fragment.refreshCareGiverView(model);

        assertNotNull(model);
        assertNull(model.getPhoneNumber());
        Assert.assertEquals("Care giver Name", model.getName());
        Assert.assertEquals("Care Giver", model.getRole());
        Assert.assertTrue(model.isIndependent());

    }

    @Test
    public void whenRefreshCareGiverViewAnsweredWhenModelIsNotIndependent() {
        when(model.getPhoneNumber()).thenReturn("0727272828");
        when(model.getName()).thenReturn("Care giver Name");
        when(model.getRole()).thenReturn("Care Giver");
        when(model.isIndependent()).thenReturn(false);
        Pair<String, String> pair = new Pair<>(null, null);
        when(model.getOtherContact()).thenReturn(pair);

        fragment.refreshCareGiverView(model);

        assertNotNull(model);
        Assert.assertEquals("0727272828", model.getPhoneNumber());
        Assert.assertEquals("Care giver Name", model.getName());
        Assert.assertEquals("Care Giver", model.getRole());
        Assert.assertFalse(model.isIndependent());

    }


}