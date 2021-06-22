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

public class CoreTbFloatingMenuTest extends BaseUnitTest {

    @Mock
    private View view;

    @Mock
    private OnClickFloatingMenu onClickFloatingMenu;


    @Mock
    private CoreTbFloatingMenu coreTbFloatingMenu;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetupViews() {
        Mockito.doNothing().when(coreTbFloatingMenu).setFloatMenuClickListener(onClickFloatingMenu);
        coreTbFloatingMenu.setFloatMenuClickListener(onClickFloatingMenu);

        ArgumentCaptor<OnClickFloatingMenu> captor = ArgumentCaptor.forClass(OnClickFloatingMenu.class);
        Mockito.verify(coreTbFloatingMenu, Mockito.times(1)).setFloatMenuClickListener(captor.capture());
        Assert.assertEquals(captor.getValue(), onClickFloatingMenu);
    }

    @Test
    public void testGetCallLayout() {
        Mockito.when(coreTbFloatingMenu.getCallLayout())
                .thenReturn(view);
        Assert.assertEquals(view, coreTbFloatingMenu.getCallLayout());
    }

    @Test
    public void testRedraw() {
        Mockito.doNothing().when(coreTbFloatingMenu).redraw(true);
        coreTbFloatingMenu.redraw(true);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        Mockito.verify(coreTbFloatingMenu, Mockito.times(1)).redraw(captor.capture());
        Assert.assertEquals(captor.getValue(), true);
    }

    @Test
    public void testOnClick() {
        Mockito.doNothing().when(coreTbFloatingMenu).onClick(view);
        coreTbFloatingMenu.onClick(view);

        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        Mockito.verify(coreTbFloatingMenu, Mockito.times(1)).onClick(captor.capture());
        Assert.assertEquals(captor.getValue(), view);
    }
}
