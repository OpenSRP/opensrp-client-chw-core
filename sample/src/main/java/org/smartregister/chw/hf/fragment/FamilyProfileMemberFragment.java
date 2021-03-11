package org.smartregister.chw.hf.fragment;

import android.os.Bundle;

import org.smartregister.chw.core.fragment.CoreFamilyProfileMemberFragment;
import org.smartregister.chw.core.provider.CoreMemberRegisterProvider;
import org.smartregister.chw.hf.model.FamilyProfileMemberModel;
import org.smartregister.chw.hf.presenter.FamilyProfileMemberPresenter;
import org.smartregister.chw.hf.provider.HfMemberRegisterProvider;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.fragment.BaseFamilyProfileMemberFragment;
import org.smartregister.family.util.Constants;
import org.smartregister.view.contract.IView;

import java.util.Set;

public class FamilyProfileMemberFragment extends CoreFamilyProfileMemberFragment {

    public static BaseFamilyProfileMemberFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileMemberFragment fragment = new FamilyProfileMemberFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeAdapter(Set<IView> visibleColumns, String familyHead, String primaryCaregiver) {
        CoreMemberRegisterProvider chwMemberRegisterProvider = new HfMemberRegisterProvider(this.getActivity(), this.commonRepository(), visibleColumns, this.registerActionHandler, this.paginationViewHandler, familyHead, primaryCaregiver);
        this.clientAdapter = new RecyclerViewPaginatedAdapter(null, chwMemberRegisterProvider, this.context().commonrepository(this.tablename));
        this.clientAdapter.setCurrentlimit(20);
        this.clientsView.setAdapter(this.clientAdapter);
    }

    @Override
    protected void initializePresenter() {
        String familyBaseEntityId = getArguments().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        String familyHead = getArguments().getString(Constants.INTENT_KEY.FAMILY_HEAD);
        String primaryCareGiver = getArguments().getString(Constants.INTENT_KEY.PRIMARY_CAREGIVER);
        presenter = new FamilyProfileMemberPresenter(this, new FamilyProfileMemberModel(), null, familyBaseEntityId, familyHead, primaryCareGiver);
    }
}
