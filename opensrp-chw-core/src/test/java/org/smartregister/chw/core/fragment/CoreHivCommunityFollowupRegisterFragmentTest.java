package org.smartregister.chw.core.fragment;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.core.implementation.CoreHivCommunityFollowupRegisterFragmentIml;

import java.util.Set;

public class CoreHivCommunityFollowupRegisterFragmentTest extends BaseUnitTest {

    @Mock
    private CoreHivCommunityFollowupRegisterFragmentIml hivCommunityFollowupRegisterFragment;

    @Mock
    private View view;

    @Mock
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        AppCompatActivity activity = Robolectric
                .buildActivity(AppCompatActivity.class).create().start()
                .resume().get();

        hivCommunityFollowupRegisterFragment = new CoreHivCommunityFollowupRegisterFragmentIml();

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = activity.getSupportFragmentManager()
                .findFragmentByTag("HivCommunityFollowupRegisterFragment");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.add(hivCommunityFollowupRegisterFragment, "HivCommunityFollowupRegisterFragment");
        fragmentTransaction.commitAllowingStateLoss();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitializeAdapter() {
        hivCommunityFollowupRegisterFragment.initializeAdapter(visibleColumns);

        ArgumentCaptor<Set> captor = ArgumentCaptor.forClass(Set.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).initializeAdapter(captor.capture());
        Assert.assertEquals(captor.getValue(), visibleColumns);
    }

    @Test
    public void testToggleFilterSelection() {
        Mockito.doNothing().when(hivCommunityFollowupRegisterFragment).toggleFilterSelection(view);
        hivCommunityFollowupRegisterFragment.toggleFilterSelection(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).toggleFilterSelection(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }

    @Test
    public void testOnViewClicked() {
        Mockito.doNothing().when(hivCommunityFollowupRegisterFragment).onViewClicked(view);
        hivCommunityFollowupRegisterFragment.onViewClicked(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).onViewClicked(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }

    @Test
    public void testFilterDue() {
        String filterString = "filterString";
        String joinTableString = "joinTableString";
        String mainConditionString = "mainConditionString";
        Mockito.doNothing().when(hivCommunityFollowupRegisterFragment).filterDue(filterString, joinTableString, mainConditionString);
        hivCommunityFollowupRegisterFragment.filterDue(filterString, joinTableString, mainConditionString);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captor2 = ArgumentCaptor.forClass(String.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).filterDue(captor.capture(), captor1.capture(), captor2.capture());
        Assert.assertEquals(captor.getValue(), filterString);
        Assert.assertEquals(captor1.getValue(), joinTableString);
        Assert.assertEquals(captor2.getValue(), mainConditionString);
    }

    @Test
    public void testNormalFilter() {
        Mockito.doNothing().when(hivCommunityFollowupRegisterFragment).normalFilter(view);
        hivCommunityFollowupRegisterFragment.normalFilter(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).normalFilter(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }

    @Test
    public void testDueFilters() {
        Mockito.doNothing().when(hivCommunityFollowupRegisterFragment).dueFilter(view);
        hivCommunityFollowupRegisterFragment.dueFilter(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(hivCommunityFollowupRegisterFragment, Mockito.times(1)).dueFilter(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }


}
