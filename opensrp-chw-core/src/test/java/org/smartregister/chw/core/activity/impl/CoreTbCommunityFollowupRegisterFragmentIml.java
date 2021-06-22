package org.smartregister.chw.core.implementation;

import org.mockito.Mockito;
import org.smartregister.chw.core.fragment.CoreTbCommunityFollowupRegisterFragment;
import org.smartregister.chw.tb.presenter.BaseTbRegisterFragmentPresenter;

import timber.log.Timber;

/**
 * Created by cozej4 on 7/16/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class CoreTbCommunityFollowupRegisterFragmentIml extends CoreTbCommunityFollowupRegisterFragment {
    @Override
    protected void initializePresenter() {
        presenter = Mockito.mock(BaseTbRegisterFragmentPresenter.class);
    }

    @Override
    public void onResume() {

        try {
            super.onResume();
        } catch (Exception e) {
            Timber.e(e);
        }

    }
}
