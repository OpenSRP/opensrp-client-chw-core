package org.smartregister.chw.core.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.contract.CoreApplication;
import org.smartregister.chw.core.contract.NavigationContract;
import org.smartregister.chw.core.model.NavigationModel;
import org.smartregister.chw.core.utils.CoreConstants;

import java.util.HashMap;

public class NavigationPresenterTest {
    @Mock
    private CoreApplication coreApplication;

    @Mock
    private NavigationContract.View view;

    @Mock
    private NavigationModel.Flavor flavor;

    private NavigationPresenter navigationPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        navigationPresenter = new NavigationPresenter(coreApplication, view, flavor);
    }

    @Test
    public void testGetNavigationView() {
        NavigationContract.View testView = navigationPresenter.getNavigationView();
        Assert.assertEquals(view, testView);
    }

    @Test
    public void testUpdateTableMap() {
        Assert.assertEquals(navigationPresenter.getTableMap().size(), 12);

        HashMap<String, String> mp = new HashMap<>();
        mp.put(CoreConstants.DrawerMenu.REFERRALS, CoreConstants.TABLE_NAME.TASK);

        navigationPresenter.updateTableMap(mp);
        Assert.assertEquals(navigationPresenter.getTableMap().get(CoreConstants.DrawerMenu.REFERRALS), CoreConstants.TABLE_NAME.TASK);
    }
}
