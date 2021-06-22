package org.smartregister.chw.core.implementation;

import org.mockito.Mockito;
import org.smartregister.chw.core.fragment.CoreHivCommunityFollowupRegisterFragment;
import org.smartregister.chw.hiv.presenter.BaseHivRegisterFragmentPresenter;

import timber.log.Timber;

/**
 * Created by cozej4 on 7/16/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class CoreHivCommunityFollowupRegisterFragmentIml extends CoreHivCommunityFollowupRegisterFragment {
    @Override
    protected void initializePresenter() {
        presenter = Mockito.mock(BaseHivRegisterFragmentPresenter.class);
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
