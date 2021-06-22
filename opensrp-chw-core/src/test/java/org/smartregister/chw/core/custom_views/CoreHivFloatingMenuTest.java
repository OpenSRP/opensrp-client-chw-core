package org.smartregister.chw.core.custom_views;

import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.core.listener.OnClickFloatingMenu;

public class CoreHivFloatingMenuTest extends BaseUnitTest {

    @Mock
    private View view;

    @Mock
    private CoreHivFloatingMenu coreHivFloatingMenu;

    @Mock
    private OnClickFloatingMenu onClickFloatingMenu;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitUi() {
        Mockito.doNothing().when(coreHivFloatingMenu).setFloatMenuClickListener(onClickFloatingMenu);
        coreHivFloatingMenu.setFloatMenuClickListener(onClickFloatingMenu);

        ArgumentCaptor<OnClickFloatingMenu> captor = ArgumentCaptor.forClass(OnClickFloatingMenu.class);
        Mockito.verify(coreHivFloatingMenu, Mockito.times(1)).setFloatMenuClickListener(captor.capture());
        Assert.assertEquals(captor.getValue(), onClickFloatingMenu);
    }

    @Test
    public void testOnClick() {
        Mockito.doNothing().when(coreHivFloatingMenu).onClick(view);
        coreHivFloatingMenu.onClick(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(coreHivFloatingMenu, Mockito.times(1)).onClick(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }

    @Test
    public void testGetCallLayout() {
        Mockito.when(coreHivFloatingMenu.getCallLayout())
                .thenReturn(view);
        Assert.assertEquals(view, coreHivFloatingMenu.getCallLayout());
    }

    @Test
    public void testRedraw() {
        Mockito.doNothing().when(coreHivFloatingMenu).redraw(true);
        coreHivFloatingMenu.redraw(true);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        Mockito.verify(coreHivFloatingMenu, Mockito.times(1)).redraw(captor.capture());
        Assert.assertEquals(captor.getValue(), true);
    }
}
