package org.smartregister.chw.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.contract.CoreAllClientsMemberContract;
import org.smartregister.chw.core.listener.OnClickFloatingMenu;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.CoreJsonFormUtils;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.family.util.Constants.JSON_FORM_EXTRA;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.opd.activity.BaseOpdFormActivity;
import org.smartregister.opd.utils.OpdConstants;
import org.smartregister.view.customcontrols.CustomFontTextView;

import timber.log.Timber;

import static org.smartregister.chw.core.utils.CoreReferralUtils.getCommonRepository;

public abstract class CoreAllClientsMemberProfileActivity extends CoreFamilyOtherMemberProfileActivity implements OnClickFloatingMenu, CoreAllClientsMemberContract.View {
    private RelativeLayout layoutFamilyHasRow;
    private CustomFontTextView familyHeadTextView;
    private CustomFontTextView careGiverTextView;

    @Override
    protected void onCreation() {
        setIndependentClient(true);
        setContentView(R.layout.activity_all_clients_member_profile);

        Toolbar toolbar = findViewById(org.smartregister.family.R.id.family_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        appBarLayout = findViewById(org.smartregister.family.R.id.toolbar_appbarlayout);

        imageRenderHelper = new ImageRenderHelper(this);

        initializePresenter();

        setupViews();
    }


    @Override
    public void setFamilyServiceStatus(String status) {
        layoutFamilyHasRow.setVisibility(View.GONE);
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.return_to_all_client));
        layoutFamilyHasRow = findViewById(R.id.family_has_row);
        familyHeadTextView = findViewById(R.id.family_head);
        careGiverTextView = findViewById(R.id.primary_caregiver);
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        layoutFamilyHasRow.setVisibility(View.GONE);
    }

    @Override
    public void toggleFamilyHead(boolean show) {
        familyHeadTextView.setVisibility(View.GONE);
    }

    @Override
    public void togglePrimaryCaregiver(boolean show) {
        careGiverTextView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        if (itemId == R.id.action_location_info) {
            JSONObject preFilledForm = CoreJsonFormUtils.getAutoPopulatedJsonEditFormString(
                    CoreConstants.JSON_FORM.getFamilyDetailsRegister(), this,
                    getFamilyRegistrationDetails(), Utils.metadata().familyRegister.updateEventType);
            if (preFilledForm != null) startFormActivity(preFilledForm);
            return true;
        }
        return true;
    }


    @NotNull
    protected CommonPersonObjectClient getFamilyRegistrationDetails() {
        //Update common person client object with all details from family register table
        final CommonPersonObject personObject = getCommonRepository(Utils.metadata().familyRegister.tableName)
                .findByBaseEntityId(familyBaseEntityId);
        CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient(personObject.getCaseId(),
                personObject.getDetails(), "");
        commonPersonObjectClient.setColumnmaps(personObject.getColumnmaps());
        commonPersonObjectClient.setDetails(personObject.getDetails());
        return commonPersonObjectClient;
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, BaseOpdFormActivity.class);
        intent.putExtra(OpdConstants.JSON_FORM_EXTRA.JSON, jsonForm.toString());
        Form form = new Form();
        form.setName(getString(R.string.update_client_registration));
        form.setActionBarBackground(R.color.family_actionbar);
        form.setNavigationBackground(R.color.family_navigation);
        form.setHomeAsUpIndicator(R.mipmap.ic_cross_white);
        form.setPreviousLabel(getResources().getString(R.string.back));
        form.setWizard(false);
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);
        startActivityForResult(intent, JsonFormUtils.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        try {
            String jsonString = data.getStringExtra(JSON_FORM_EXTRA.JSON);
            JSONObject form = new JSONObject(jsonString);
            if (form.getString(JsonFormUtils.ENCOUNTER_TYPE).equals(Utils.metadata().familyRegister.updateEventType)) {
                getAllClientsMemberPresenter().updateLocationInfo(jsonString, familyBaseEntityId);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    protected void setIndependentClient(boolean isIndependentClient) {
        super.isIndependent = isIndependentClient;
    }

}
