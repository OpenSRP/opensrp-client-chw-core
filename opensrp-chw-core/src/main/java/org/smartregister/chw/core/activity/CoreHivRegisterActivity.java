package org.smartregister.chw.core.activity;

import android.app.Activity;
import android.content.Intent;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.custom_views.NavigationMenu;
import org.smartregister.chw.core.job.HomeVisitServiceJob;
import org.smartregister.chw.core.job.VaccineRecurringServiceJob;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.hiv.activity.BaseHivRegisterActivity;
import org.smartregister.chw.hiv.activity.BaseHivRegistrationFormsActivity;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.job.ImageUploadServiceJob;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.job.SyncTaskServiceJob;

public class CoreHivRegisterActivity extends BaseHivRegisterActivity {
    private String baseEntityID;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        baseEntityID = getIntent().getStringExtra(org.smartregister.chw.hiv.util.Constants.ActivityPayload.BASE_ENTITY_ID);
        super.onCreate(savedInstanceState);
        NavigationMenu.getInstance(this, null, null);
    }


    @Override
    public void startFormActivity(@Nullable String formName, @Nullable String entityId, @Nullable String metaData) {
        Intent intent = new Intent(this, BaseHivRegistrationFormsActivity.class);
        intent.putExtra(org.smartregister.chw.hiv.util.Constants.ActivityPayload.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(org.smartregister.chw.hiv.util.Constants.ActivityPayload.JSON_FORM, metaData);
        intent.putExtra(org.smartregister.chw.hiv.util.Constants.ActivityPayload.USE_DEFAULT_NEAT_FORM_LAYOUT, false);

        this.startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
    }

    private void startRegisterActivity() {
        HomeVisitServiceJob.scheduleJobImmediately(HomeVisitServiceJob.TAG);
        VaccineRecurringServiceJob.scheduleJobImmediately(VaccineRecurringServiceJob.TAG);
        ImageUploadServiceJob.scheduleJobImmediately(ImageUploadServiceJob.TAG);
        org.smartregister.job.SyncServiceJob.scheduleJobImmediately(org.smartregister.job.SyncServiceJob.TAG);
        PullUniqueIdsServiceJob.scheduleJobImmediately(PullUniqueIdsServiceJob.TAG);
        HomeVisitServiceJob.scheduleJobImmediately(HomeVisitServiceJob.TAG);
        SyncTaskServiceJob.scheduleJobImmediately(SyncTaskServiceJob.TAG);
        Intent intent = new Intent(this, getClass());
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        this.finish();
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        NavigationMenu menu = NavigationMenu.getInstance(this, null, null);
        if (menu != null) {
            menu.getNavigationAdapter().setSelectedView(CoreConstants.DrawerMenu.HIV_CLIENTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == org.smartregister.chw.malaria.util.Constants.REQUEST_CODE_GET_JSON) {
            startRegisterActivity();
        } else {
            finish();
        }

    }
}
 