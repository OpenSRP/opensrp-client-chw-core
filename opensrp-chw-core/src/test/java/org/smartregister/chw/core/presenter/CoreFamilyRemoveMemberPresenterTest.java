package org.smartregister.chw.core.presenter;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.chw.core.contract.FamilyRemoveMemberContract;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.util.DBConstants;
import org.smartregister.opd.utils.OpdDbConstants;

import java.lang.ref.WeakReference;
import java.util.HashMap;


@PrepareForTest(CoreFamilyRemoveMemberPresenter.class)
public class CoreFamilyRemoveMemberPresenterTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private FamilyRemoveMemberContract.View view;

    @Mock
    private FamilyRemoveMemberContract.Interactor interactor;

    @Mock
    private FamilyRemoveMemberContract.Model model;

    private CoreFamilyRemoveMemberPresenter removeMemberPresenter;

    private CommonPersonObjectClient commonPersonClient;

    private HashMap<String, String> columnMaps;

    private String viewConfigurationIdentifier = "viewConfigurationIdentifier";

    private String familyBaseEntityId = "familyBaseEntityId";
    private String familyHead = "familyHead";
    private String primaryCaregiver = "primaryCaregiver";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        removeMemberPresenter = new CoreFamilyRemoveMemberPresenter(view, model, viewConfigurationIdentifier, familyBaseEntityId, familyHead, primaryCaregiver);
        ReflectionHelpers.setField(removeMemberPresenter, "viewReference", new WeakReference<>(view));
        ReflectionHelpers.setField(removeMemberPresenter, "interactor", interactor);
        ReflectionHelpers.setField(removeMemberPresenter, "model", model);
        ReflectionHelpers.setField(removeMemberPresenter, "familyHead", "family-head-entity-id");
        ReflectionHelpers.setField(removeMemberPresenter, "primaryCaregiver", "primary-care-giver");
        ReflectionHelpers.setField(removeMemberPresenter, "familyBaseEntityId", "family-entity-id");

        commonPersonClient = new CommonPersonObjectClient("test-case-id", new HashMap<>(), "test");
        columnMaps = new HashMap<>();

        commonPersonClient.setColumnmaps(columnMaps);
    }

    @Test
    public void removeMemberStartsRemoveMemberForm() throws Exception {
        columnMaps.put(DBConstants.KEY.BASE_ENTITY_ID, "");
        columnMaps.put(OpdDbConstants.KEY.REGISTER_TYPE, "");
        CoreFamilyRemoveMemberPresenter presenterSpy = PowerMockito.spy(removeMemberPresenter);
        PowerMockito.doNothing().when(presenterSpy, "startRemoveMemberForm", ArgumentMatchers.any(CommonPersonObjectClient.class));
        Assert.assertNotNull(commonPersonClient);
        removeMemberPresenter.removeMember(commonPersonClient);
    }


    @Test
    public void removeFamilyHeadInvokesProcessFamilyMember() {
        columnMaps.put(DBConstants.KEY.BASE_ENTITY_ID, "family-head-entity-id");
        columnMaps.put(OpdDbConstants.KEY.REGISTER_TYPE, "");

        removeMemberPresenter.removeMember(commonPersonClient);
        Mockito.verify(interactor, Mockito.times(1)).processFamilyMember(
                ArgumentMatchers.eq("family-entity-id"),
                ArgumentMatchers.any(CommonPersonObjectClient.class),
                ArgumentMatchers.any(CoreFamilyRemoveMemberPresenter.class));
    }

    @Test
    public void removeEveryoneStartsRemovalForm() {
        JSONObject form = Mockito.mock(JSONObject.class);
        Mockito.when(model.prepareFamilyRemovalForm(
                ArgumentMatchers.eq("family-entity-id"),
                ArgumentMatchers.eq("family-name"),
                ArgumentMatchers.eq("family-details"))).thenReturn(form);
        removeMemberPresenter.removeEveryone("family-name", "family-details");
        Mockito.verify(view, Mockito.times(1)).startJsonActivity(form);
    }

    @Test
    public void familyRemovedExecutesViewEveryoneRemoved() {
        removeMemberPresenter.onFamilyRemoved(false);
        Mockito.verify(view, Mockito.times(0)).onEveryoneRemoved();
        removeMemberPresenter.onFamilyRemoved(true);
        Mockito.verify(view, Mockito.times(1)).onEveryoneRemoved();
    }

    @Test
    public void memberRemovedExecutesViewOnMemberRemoved() {
        removeMemberPresenter.memberRemoved("test-removal");
        Mockito.verify(view, Mockito.times(1)).onMemberRemoved("test-removal");
    }

    @Test
    public void getMainConditionReturnsCorrectString() {
        String mainCondition = String.format(" %s = '%s' and %s is null and %s is null ",
                DBConstants.KEY.OBJECT_RELATIONAL_ID, "family-entity-id",
                DBConstants.KEY.DATE_REMOVED,
                DBConstants.KEY.DOD);
        Assert.assertEquals(mainCondition, removeMemberPresenter.getMainCondition());
    }

    @Test
    public void getDefaultSortQueryReturnsCorrectString() {
        String defaultSortQuery = String.format(" %s ASC ", DBConstants.KEY.DOB);
        Assert.assertEquals(defaultSortQuery, removeMemberPresenter.getDefaultSortQuery());
    }

    @Test
    public void testSetInteractor() {
        removeMemberPresenter.setInteractor(interactor);
        Assert.assertNotNull(interactor);
    }

}