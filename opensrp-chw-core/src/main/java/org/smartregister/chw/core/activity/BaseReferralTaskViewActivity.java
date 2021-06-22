package org.smartregister.chw.core.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.dao.AncDao;
import org.smartregister.chw.core.dao.PNCDao;
import org.smartregister.chw.core.model.ChildModel;
import org.smartregister.chw.core.utils.ChildDBConstants;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Task;
import org.smartregister.family.util.DBConstants;
import org.smartregister.view.activity.SecuredActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.util.List;

import timber.log.Timber;

import static org.smartregister.chw.core.utils.Utils.updateToolbarTitle;
import static org.smartregister.chw.malaria.util.DBConstants.KEY.FIRST_NAME;
import static org.smartregister.chw.malaria.util.DBConstants.KEY.LAST_NAME;
import static org.smartregister.chw.malaria.util.DBConstants.KEY.MIDDLE_NAME;
import static org.smartregister.chw.malaria.util.DBConstants.KEY.PHONE_NUMBER;
import static org.smartregister.family.util.DBConstants.KEY.OTHER_PHONE_NUMBER;

public abstract class BaseReferralTaskViewActivity extends SecuredActivity {

    protected String name;
    protected String baseEntityId;
    protected Task task;
    protected CustomFontTextView womanGa;
    protected LinearLayout womanGaLayout;
    protected MemberObject memberObject;
    protected String familyHeadName;
    protected String familyHeadPhoneNumber;
    protected CustomFontTextView clientName;
    protected CustomFontTextView careGiverName;
    protected CustomFontTextView childName;
    protected CustomFontTextView careGiverPhone;
    protected CustomFontTextView clientReferralProblem;
    protected CustomFontTextView referralDate;
    protected CustomFontTextView chwDetailsNames;
    protected LinearLayout careGiverLayout;
    protected LinearLayout childNameLayout;
    protected AppBarLayout appBarLayout;
    protected String startingActivity;
    protected static CommonPersonObjectClient personObjectClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public static void setPersonObjectClient(CommonPersonObjectClient personObjectClient) {
        BaseReferralTaskViewActivity.personObjectClient = personObjectClient;
    }

    public CommonPersonObjectClient getPersonObjectClient() {
        return personObjectClient;
    }

    protected void extraClientTask() {
        setTask((Task) getIntent().getSerializableExtra(CoreConstants.INTENT_KEY.USERS_TASKS));

        if (getTask() == null) {
            Timber.e("ReferralTaskViewActivity --> The task object is null");
            finish();
        }
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public MemberObject getMemberObject() {
        return memberObject;
    }

    protected void addGaDisplay() {
        if (CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS.equals(getTask().getFocus())) {
            womanGaLayout.setVisibility(View.VISIBLE);
            String gaWeeks = getMemberObject().getGestationAge() + " " + getString(R.string.weeks);
            womanGa.setText(gaWeeks);
        }
    }

    protected void extraDetails() {
        if (CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS.equalsIgnoreCase(task.getFocus()) ||
                CoreConstants.TASKS_FOCUS.PNC_DANGER_SIGNS.equalsIgnoreCase(task.getFocus())) {
            setMemberObject(AncDao.getMember(getPersonObjectClient().getCaseId()));
        }
        setClientName();
        baseEntityId = getPersonObjectClient().getCaseId();
        setFamilyHeadName((String) getIntent().getSerializableExtra(CoreConstants.INTENT_KEY.FAMILY_HEAD_NAME));
        setFamilyHeadPhoneNumber((String) getIntent().getSerializableExtra(CoreConstants.INTENT_KEY.FAMILY_HEAD_PHONE_NUMBER));
    }

    public void setMemberObject(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

    public void setFamilyHeadName(String familyHeadName) {
        this.familyHeadName = familyHeadName;
    }

    public void setFamilyHeadPhoneNumber(String familyHeadPhoneNumber) {
        this.familyHeadPhoneNumber = familyHeadPhoneNumber;
    }

    public String getFamilyHeadPhoneNumber() {
        return familyHeadPhoneNumber;
    }

    protected void getReferralDetails() {
        if (getPersonObjectClient() != null && getTask() != null) {
            updateProblemDisplay();
            String clientAge = (Utils.getTranslatedDate(Utils.getDuration(Utils.getValue(getPersonObjectClient().getColumnmaps(), DBConstants.KEY.DOB, false)), getBaseContext()));
            clientName.setText(getString(R.string.client_name_age_suffix, name, clientAge));
            referralDate.setText(org.smartregister.chw.core.utils.Utils.dd_MMM_yyyy.format(getTask().getExecutionStartDate().toDate()));

            //For PNC get children belonging to the woman
            String childrenForPncWoman = getChildrenForPncWoman(getPersonObjectClient().entityId());
            if (getTask().getFocus().equalsIgnoreCase(CoreConstants.TASKS_FOCUS.PNC_DANGER_SIGNS) &&
                    StringUtils.isNoneEmpty(childrenForPncWoman)) {
                childName.setText(childrenForPncWoman);
                childNameLayout.setVisibility(View.VISIBLE);
            }

            //Hide Care giver for clients other than CHILD
            careGiverLayout.setVisibility(View.GONE);
            if (getTask().getFocus().equalsIgnoreCase(CoreConstants.TASKS_FOCUS.SICK_CHILD)) {
                careGiverLayout.setVisibility(View.VISIBLE);
                String parentFirstName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_FIRST_NAME, true);
                String parentLastName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_LAST_NAME, true);
                String parentMiddleName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MIDDLE_NAME, true);
                String parentName = getString(R.string.care_giver_prefix, org.smartregister.util.Utils.getName(parentFirstName, parentMiddleName + " " + parentLastName));
                careGiverName.setText(parentName);
            }

            String familyMemberContacts = getFamilyMemberContacts();
            careGiverPhone.setText(familyMemberContacts.isEmpty() ? getString(R.string.phone_not_provided) : familyMemberContacts);

            chwDetailsNames.setText(getTask().getRequester());

            addGaDisplay();
        }
    }

    protected void setClientName() {
        String firstName = Utils.getValue(getPersonObjectClient().getColumnmaps(), FIRST_NAME, true);
        String lastName = Utils.getValue(getPersonObjectClient().getColumnmaps(), LAST_NAME, true);
        String middleName = Utils.getValue(getPersonObjectClient().getColumnmaps(), MIDDLE_NAME, true);
        name = Utils.getName(firstName, middleName + " " + lastName);
    }

    private String getChildrenForPncWoman(String baseEntityId) {
        List<ChildModel> childModels = PNCDao.childrenForPncWoman(baseEntityId);
        StringBuilder stringBuilder = new StringBuilder();
        for (ChildModel childModel : childModels) {
            String childAge = (Utils.getTranslatedDate(Utils.getDuration(childModel.getDateOfBirth()), getBaseContext()));
            stringBuilder.append(childModel.getChildFullName())
                    .append(", ")
                    .append(childAge)
                    .append("; ");
        }

        String children = stringBuilder.toString().replaceAll("; $", "").trim();

        return childModels.size() == 1 ? getString(R.string.child_prefix, children) :
                getString(R.string.children_prefix, children);
    }

    protected void updateProblemDisplay() {
        if (CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS.equals(getTask().getFocus())) {
            clientReferralProblem.setText(getString(R.string.anc_danger_sign_prefix, getTask().getDescription()));
        } else {
            clientReferralProblem.setText(getTask().getDescription());
        }
    }

    protected String getFamilyMemberContacts() {

        String familyPhoneNumber = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER, true);
        String familyPhoneNumberOther = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER_OTHER, true);
        String clientPhoneNumber = Utils.getValue(getPersonObjectClient().getColumnmaps(), PHONE_NUMBER, true);
        String clientOtherPhoneNumber = Utils.getValue(getPersonObjectClient().getColumnmaps(), OTHER_PHONE_NUMBER, true);

        if (StringUtils.isNoneEmpty(clientPhoneNumber) && StringUtils.isNoneEmpty(clientOtherPhoneNumber))
            return clientPhoneNumber + ", " + clientOtherPhoneNumber;
        else if (StringUtils.isNoneEmpty(clientPhoneNumber)) return clientPhoneNumber;
        else if (StringUtils.isEmpty(clientPhoneNumber) && StringUtils.isNoneEmpty(clientOtherPhoneNumber))
            return clientOtherPhoneNumber;
        else if (StringUtils.isNoneEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther))
            return familyPhoneNumber + ", " + familyPhoneNumberOther;
        else if (StringUtils.isNoneEmpty(familyPhoneNumber)) return familyPhoneNumber;
        else if (StringUtils.isEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther))
            return familyPhoneNumberOther;
        else if (StringUtils.isNoneEmpty(getFamilyHeadPhoneNumber()))
            return getFamilyHeadPhoneNumber();
        return "";
    }

    protected void inflateToolbar() {
        Toolbar toolbar = findViewById(R.id.back_referrals_toolbar);
        CustomFontTextView toolBarTextView = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setElevation(0);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        if (getStartingActivity().equals(CoreConstants.REGISTERED_ACTIVITIES.REFERRALS_REGISTER_ACTIVITY)) {
            toolBarTextView.setText(R.string.back_to_referrals);
        } else {
            if (TextUtils.isEmpty(name)) {
                toolBarTextView.setText(R.string.back_to_referrals);
            } else {
                toolBarTextView.setText(getString(R.string.return_to, name));
            }
        }
        toolBarTextView.setOnClickListener(v -> finish());
        appBarLayout = findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        updateToolbarTitle(this, R.id.toolbar_title, "");
    }

    public String getStartingActivity() {
        return startingActivity;
    }
}
