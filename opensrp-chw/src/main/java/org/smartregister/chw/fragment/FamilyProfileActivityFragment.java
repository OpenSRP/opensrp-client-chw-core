package org.smartregister.chw.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.smartregister.chw.R;
import org.smartregister.chw.adapter.WashCheckAdapter;
import org.smartregister.chw.model.FamilyProfileActivityModel;
import org.smartregister.chw.util.TestConstant;
import org.smartregister.chw.util.WashCheck;
import org.smartregister.chw.presenter.FamilyProfileActivityPresenter;
import org.smartregister.chw.provider.FamilyActivityRegisterProvider;
import org.smartregister.configurableviews.model.View;
import org.smartregister.family.adapter.FamilyRecyclerViewCustomAdapter;
import org.smartregister.family.fragment.BaseFamilyProfileActivityFragment;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import timber.log.Timber;

public class FamilyProfileActivityFragment extends BaseFamilyProfileActivityFragment {
    private static final String TAG = FamilyProfileActivityFragment.class.getCanonicalName();
    private String familyName;
    private RecyclerView washCheckRecyclerView;
    private WashCheckAdapter washCheckAdapter;

    public static BaseFamilyProfileActivityFragment newInstance(Bundle bundle) {
        Bundle args = bundle;
        BaseFamilyProfileActivityFragment fragment = new FamilyProfileActivityFragment();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupViews(android.view.View view) {
        super.setupViews(view);
        washCheckRecyclerView = view.findViewById(R.id.recycler_view_wash_check);
        updateWashCheck();
    }

    @Override
    public void initializeAdapter(Set<View> visibleColumns) {
        FamilyActivityRegisterProvider familyActivityRegisterProvider = new FamilyActivityRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new FamilyRecyclerViewCustomAdapter(null, familyActivityRegisterProvider, context().commonrepository(this.tablename), Utils.metadata().familyActivityRegister.showPagination);
        clientAdapter.setCurrentlimit(Utils.metadata().familyActivityRegister.currentLimit);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    protected void initializePresenter() {
        String familyBaseEntityId = getArguments().getString(Constants.INTENT_KEY.FAMILY_BASE_ENTITY_ID);
        familyName = getArguments().getString(Constants.INTENT_KEY.FAMILY_NAME);
        presenter = new FamilyProfileActivityPresenter(this, new FamilyProfileActivityModel(), null, familyBaseEntityId);
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {
        //TODO
        Timber.d("setAdvancedSearchFormData");
    }
    public void updateWashCheckBar(ArrayList<WashCheck> washCheckList){
        if(washCheckList.size()>0){
            washCheckRecyclerView.setVisibility(android.view.View.VISIBLE);
            if(washCheckAdapter == null){
                washCheckAdapter =new WashCheckAdapter(getActivity(), familyName, new WashCheckAdapter.OnClickAdapter() {
                    @Override
                    public void onClick(int position, WashCheck washCheck) {
                        WashCheckDialogFragment dialogFragment = WashCheckDialogFragment.getInstance(washCheck.getDetailsJson());
                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        dialogFragment.show(ft, WashCheckDialogFragment.DIALOG_TAG);
                    }
                });
                washCheckAdapter.setData(washCheckList);
                washCheckRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                washCheckRecyclerView.setAdapter(washCheckAdapter);

            }else{
                washCheckAdapter.setData(washCheckList);
                washCheckAdapter.notifyDataSetChanged();
            }
        }


    }
    public void updateWashCheck(){
        if(TestConstant.IS_WASH_CHECK_VISIBLE){
            ((FamilyProfileActivityPresenter)presenter).fetchLastWashCheck();
        }
    }

}
