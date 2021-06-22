package org.smartregister.chw.core.custom_views;

import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CoreFamilyMemberFloatingMenuTest {

    @Mock
    private View view;

    @Mock
    private CoreFamilyMemberFloatingMenu familyMemberFloatingMenu;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnClick() {
        Mockito.doNothing().when(familyMemberFloatingMenu).onClick(view);
        familyMemberFloatingMenu.onClick(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(familyMemberFloatingMenu, Mockito.times(1)).onClick(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }

    @Test
    public void testGetCallLayout() {
        Mockito.when(familyMemberFloatingMenu.getCallLayout())
                .thenReturn(view);
        Assert.assertEquals(view, familyMemberFloatingMenu.getCallLayout());
    }

    @Test
    public void testRedraw() {
        Mockito.doNothing().when(familyMemberFloatingMenu).reDraw(true);
        familyMemberFloatingMenu.reDraw(true);
        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        Mockito.verify(familyMemberFloatingMenu, Mockito.times(1)).reDraw(captor.capture());
        Assert.assertEquals(captor.getValue(), true);
    }
}