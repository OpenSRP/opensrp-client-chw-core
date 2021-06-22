package org.smartregister.chw.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.activity.BaseChwNotificationDetailsActivity;
import org.smartregister.chw.core.activity.BaseReferralTaskViewActivity;
import org.smartregister.chw.core.activity.CoreAllClientsRegisterActivity;
import org.smartregister.chw.core.activity.CoreFamilyProfileActivity;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.contract.FamilyCallDialogContract;
import org.smartregister.chw.core.custom_views.CoreAncFloatingMenu;
import org.smartregister.chw.core.custom_views.CoreFamilyMemberFloatingMenu;
import org.smartregister.chw.core.custom_views.CoreFamilyPlanningFloatingMenu;
import org.smartregister.chw.core.custom_views.CoreHivFloatingMenu;
import org.smartregister.chw.core.custom_views.CoreMalariaFloatingMenu;
import org.smartregister.chw.core.custom_views.CoreTbFloatingMenu;
import org.smartregister.chw.core.domain.Hia2Indicator;
import org.smartregister.chw.core.domain.MonthlyTally;
import org.smartregister.chw.core.fragment.CopyToClipboardDialog;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.Event;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.util.DBConstants;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PermissionUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.smartregister.chw.core.dao.VisitDao.getMUACValue;
import static org.smartregister.opd.utils.OpdJsonFormUtils.locationId;

public abstract class Utils extends org.smartregister.family.util.Utils {
    public static final SimpleDateFormat dd_MMM_yyyy = new SimpleDateFormat("dd MMM yyyy");
    public static final SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-mm-dd");
    private static List<String> assets;

    public static int getAnCWomanImageResourceIdentifier() {
        return R.drawable.anc_woman;
    }

    public static int getPnCWomanImageResourceIdentifier() {
        return R.drawable.pnc_woman;
    }

    public static int getMemberImageResourceIdentifier() {
        return R.mipmap.ic_member;
    }

    public static String getImmunizationHeaderLanguageSpecific(Context context, String value) {
        if (TextUtils.isEmpty(value)) return "";
        if (value.equalsIgnoreCase("at birth")) {
            return context.getString(R.string.at_birth);
        } else if (value.contains("weeks")) {
            return context.getString(R.string.week_full);
        } else if (value.contains("months")) {
            return context.getString(R.string.month_full);
        }
        return value;
    }

    public static String getYesNoAsLanguageSpecific(Context context, String value) {
        if (TextUtils.isEmpty(value)) return "";
        if (value.equalsIgnoreCase("yes")) {
            return context.getString(R.string.yes);
        } else if (value.equalsIgnoreCase("no")) {
            return context.getString(R.string.no);
        }
        return value;
    }

    public static String getGenderLanguageSpecific(Context context, String value) {
        if (TextUtils.isEmpty(value)) return "";
        if (value.equalsIgnoreCase("male")) {
            return context.getString(org.smartregister.family.R.string.male);
        } else if (value.equalsIgnoreCase("female")) {
            return context.getString(org.smartregister.family.R.string.female);
        }
        return value;
    }


    public static String firstCharacterUppercase(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String convertToDateFormateString(String timeAsDDMMYYYY, SimpleDateFormat dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());//12-08-2018
        try {
            Date date = sdf.parse(timeAsDDMMYYYY);
            return dateFormat.format(date);
        } catch (ParseException e) {
            Timber.e(e);
        }
        return "";
    }

    public static boolean launchDialer(final Activity activity, final FamilyCallDialogContract.View callView, final String phoneNumber) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // set a pending call execution request
            if (callView != null) {
                callView.setPendingCallRequest(() -> Utils.launchDialer(activity, callView, phoneNumber));
            }

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE);

            return false;
        } else {

            if (((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                    == null) {

                Timber.i("No dial application so we launch copy to clipboard...");

                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(activity.getText(R.string.copied_phone_number), phoneNumber);
                clipboard.setPrimaryClip(clip);

                CopyToClipboardDialog copyToClipboardDialog = new CopyToClipboardDialog(activity, R.style.copy_clipboard_dialog);
                copyToClipboardDialog.setContent(phoneNumber);
                copyToClipboardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                copyToClipboardDialog.show();
                // no phone
                Toast.makeText(activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                activity.startActivity(intent);
            }
            return true;
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }

    public static int getOverDueProfileImageResourceIDentifier() {
        return R.color.visit_status_over_due;
    }

    public static int getDueProfileImageResourceIDentifier() {
        return R.color.due_profile_blue;
    }

    public static String actualDaysBetweenDateAndNow(Context context, String date) {
        Integer days = daysBetweenDateAndNow(date);
        if (days != null) {
            if (days <= 1) {
                return days + getStringSpacePrefix(context, R.string.day);
            } else {
                return days + getStringSpacePrefix(context, R.string.days);
            }
        }
        return "";
    }

    public static Integer daysBetweenDateAndNow(String date) {
        DateTime duration;
        if (StringUtils.isNotBlank(date)) {
            try {
                duration = new DateTime(new Date(Long.valueOf(date)));
                Days days = Days.daysBetween(duration.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay());
                return days.getDays();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return null;
    }

    private static String getStringSpacePrefix(Context context, int resId) {
        return " " + context.getString(resId);
    }

    @Nullable
    public static String getDayOfMonthWithSuffix(int day, Context context) {
        Preconditions.checkArgument(day >= 1 && day <= 31, "illegal day of month: " + day);
        switch (day) {
            case 1:
                return context.getString(R.string.abv_first);
            case 2:
                return context.getString(R.string.abv_second);
            case 3:
                return context.getString(R.string.abv_third);
            case 4:
                return context.getString(R.string.abv_fourth);
            case 5:
                return context.getString(R.string.abv_fifth);
            case 6:
                return context.getString(R.string.abv_sixth);
            case 7:
                return context.getString(R.string.abv_seventh);
            case 8:
                return context.getString(R.string.abv_eigth);
            case 9:
                return context.getString(R.string.abv_nineth);
            case 10:
                return context.getString(R.string.abv_tenth);
            case 11:
                return context.getString(R.string.abv_eleventh);
            case 12:
                return context.getString(R.string.abv_twelfth);
            default:
                return null;
        }
    }

    /**
     * use  translated equivalent  {@link #getDayOfMonthWithSuffix(int, Context)}
     *
     * @param n
     * @return
     */
    @Deprecated
    public static String getDayOfMonthSuffix(String n) {
        return getDayOfMonthSuffix(Integer.parseInt(n));
    }

    /**
     * use  translated equivalent  {@link #getDayOfMonthWithSuffix(int, Context)}
     *
     * @param n
     * @return
     */
    @Deprecated
    public static String getDayOfMonthSuffix(final int n) {
        Preconditions.checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return context().getStringResource(R.string.st);
            case 2:
                return context().getStringResource(R.string.nd);
            case 3:
                return context().getStringResource(R.string.th);
            default:
                return context().getStringResource(R.string.th);
        }
    }

    @NonNull
    public static CommonPersonObjectClient clientForEdit(@NonNull String baseEntityId) {
        CommonRepository commonRepository = Utils.context().commonrepository(Utils.metadata().familyMemberRegister.tableName);
        CommonPersonObject personObject = commonRepository.findByBaseEntityId(baseEntityId);
        CommonPersonObjectClient client = new CommonPersonObjectClient(personObject.getCaseId(), personObject.getDetails(), "");
        client.setColumnmaps(personObject.getColumnmaps());
        return client;
    }

    @NonNull
    public static Intent formActivityIntent(@NonNull Activity activity, @NonNull String jsonForm) {
        Intent intent = new Intent(activity, org.smartregister.family.util.Utils.metadata().familyMemberFormActivity);
        intent.putExtra(org.smartregister.family.util.Constants.JSON_FORM_EXTRA.JSON, jsonForm);

        Form form = new Form();
        form.setActionBarBackground(R.color.family_actionbar);
        form.setWizard(false);
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, form);

        return intent;
    }

    /**
     * @param obs
     * @return
     */
    public static Map<String, List<Obs>> groupObsByFieldObservations(List<Obs> obs) {
        Map<String, List<Obs>> map = new HashMap<>();
        for (Obs o : obs) {
            List<Obs> cur_vals = map.get(o.getFormSubmissionField());
            if (cur_vals == null) {
                cur_vals = new ArrayList<>();
            }
            cur_vals.add(o);

            map.put(o.getFormSubmissionField(), cur_vals);
        }
        return map;
    }

    /**
     * Check is the file exists
     *
     * @param form_name
     * @return
     */
    public static String getLocalForm(String form_name) {
        Locale current = CoreChwApplication.getCurrentLocale();

        String formIdentity = MessageFormat.format("{0}_{1}", form_name, current.getLanguage());
        // validate variant exists
        try {
            if (assets == null) {
                assets = new ArrayList<>();
            }

            if (assets.size() == 0) {
                String[] local_assets = CoreChwApplication.getInstance().getApplicationContext().getAssets().list("json.form");
                if (local_assets != null && local_assets.length > 0) {
                    for (String s : local_assets) {
                        assets.add(s.substring(0, s.length() - 5));
                    }
                }
            }

            if (assets.contains(formIdentity)) {
                return formIdentity;
            }
        } catch (Exception e) {
            // return default
            return form_name;
        }
        return form_name;
    }

    @Deprecated
    /**
     * To be removed in a future build
     */
    public static String getLocalForm(String form_name, Locale locale, AssetManager assetManager) {
        return getFileName(form_name, locale, assetManager);
    }

    public static String getFileName(String form_name, Locale current, AssetManager assetManager) {
        String formIdentity = MessageFormat.format("{0}_{1}", form_name, current.getLanguage());
        try {
            if (assets == null) {
                assets = new ArrayList<>();
            }


            if (assets.size() == 0) {
                String[] local_assets = assetManager.list("json.form");
                if (local_assets != null && local_assets.length > 0) {
                    for (String s : local_assets) {
                        assets.add(s.substring(0, s.length() - 5));
                    }
                }
            }

            if (assets.contains(formIdentity)) {
                return formIdentity;
            }
        } catch (Exception e) {
            Timber.v(e);
        }
        return form_name;
    }

    public static String getFamilyMembersSql(String familyID) {

        return DBConstants.KEY.RELATIONAL_ID + " , " +
                DBConstants.KEY.BASE_ENTITY_ID + " , " +
                DBConstants.KEY.FIRST_NAME + " , " +
                DBConstants.KEY.MIDDLE_NAME + " , " +
                DBConstants.KEY.LAST_NAME + " , " +
                DBConstants.KEY.PHONE_NUMBER + " , " +
                DBConstants.KEY.OTHER_PHONE_NUMBER + " , " +
                DBConstants.KEY.DOB + " , " +
                DBConstants.KEY.DOD + " , " +
                DBConstants.KEY.GENDER;
    }

    public static String formatReferralDuration(DateTime referralTime, Context context) {
        DateTime now = new DateTime();
        int days = Days.daysBetween(referralTime, now).getDays();
        if (days == 0) {
            return context.getString(R.string.hours_ago, Hours.hoursBetween(referralTime, now).getHours());
        } else if (days == 1) {
            return context.getString(R.string.yesterday);
        } else {
            return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(referralTime.toDate());
        }
    }

    public static String getSyncFilterValue() {
        String providerId = org.smartregister.Context.getInstance().allSharedPreferences().fetchRegisteredANM();
        String userLocationId = org.smartregister.Context.getInstance().allSharedPreferences().fetchUserLocalityId(providerId);
        List<String> locationIds = LocationHelper.getInstance().locationsFromHierarchy(true, null);
        if (!isEmptyCollection(locationIds)) {
            int index = locationIds.indexOf(userLocationId);
            List<String> subLocationIds = locationIds.subList(index, locationIds.size());
            return StringUtils.join(subLocationIds, ",");
        }
        return "";
    }

    public static void redrawWithOption(LinearLayout menu, boolean has_phone) {
        TextView callTextView = menu.findViewById(R.id.CallTextView);
        TextView callTextViewHint = menu.findViewById(R.id.CallTextViewHint);
        setCallLayoutListener(has_phone, menu);
        if (has_phone) {
            callTextViewHint.setVisibility(GONE);
            callTextView.setTypeface(null, Typeface.NORMAL);
            callTextView.setTextColor(menu.getResources().getColor(android.R.color.black));
            ((FloatingActionButton) menu.findViewById(R.id.callFab)).getDrawable().setAlpha(255);

        } else {
            callTextViewHint.setVisibility(VISIBLE);
            callTextView.setTypeface(null, Typeface.ITALIC);
            callTextView.setTextColor(menu.getResources().getColor(R.color.grey));
            ((FloatingActionButton) menu.findViewById(R.id.callFab)).getDrawable().setAlpha(122);
        }

    }

    private static void setCallLayoutListener(boolean has_phone, LinearLayout menu) {
        CoreFamilyMemberFloatingMenu memberFloatingMenu;
        CoreAncFloatingMenu ancFloatingMenu;
        CoreFamilyPlanningFloatingMenu fpFloatingMenu;
        CoreTbFloatingMenu tbFloatingMenu;
        CoreHivFloatingMenu hivFloatingMenu;
        if (has_phone && menu instanceof CoreFamilyMemberFloatingMenu) {
            memberFloatingMenu = (CoreFamilyMemberFloatingMenu) menu;
            memberFloatingMenu.getCallLayout().setOnClickListener(memberFloatingMenu);
        } else if (!has_phone && menu instanceof CoreFamilyMemberFloatingMenu) {
            memberFloatingMenu = (CoreFamilyMemberFloatingMenu) menu;
            memberFloatingMenu.getCallLayout().setOnClickListener(null);
        } else if (has_phone && menu instanceof CoreAncFloatingMenu) {
            ancFloatingMenu = (CoreAncFloatingMenu) menu;
            ancFloatingMenu.getCallLayout().setOnClickListener(ancFloatingMenu);
        } else if (!has_phone && menu instanceof CoreAncFloatingMenu) {
            ancFloatingMenu = (CoreAncFloatingMenu) menu;
            ancFloatingMenu.getCallLayout().setOnClickListener(null);
        } else if (!has_phone && menu instanceof CoreMalariaFloatingMenu) {
            CoreMalariaFloatingMenu malariaFloatingMenu = (CoreMalariaFloatingMenu) menu;
            malariaFloatingMenu.getCallLayout().setOnClickListener(null);
        } else if (has_phone && menu instanceof CoreFamilyPlanningFloatingMenu) {
            fpFloatingMenu = (CoreFamilyPlanningFloatingMenu) menu;
            fpFloatingMenu.getCallLayout().setOnClickListener(fpFloatingMenu);
        } else if (!has_phone && menu instanceof CoreFamilyPlanningFloatingMenu) {
            fpFloatingMenu = (CoreFamilyPlanningFloatingMenu) menu;
            fpFloatingMenu.getCallLayout().setOnClickListener(null);
        } else if (has_phone && menu instanceof CoreTbFloatingMenu) {
            tbFloatingMenu = (CoreTbFloatingMenu) menu;
            tbFloatingMenu.getCallLayout().setOnClickListener(tbFloatingMenu);
        } else if (!has_phone && menu instanceof CoreTbFloatingMenu) {
            tbFloatingMenu = (CoreTbFloatingMenu) menu;
            tbFloatingMenu.getCallLayout().setOnClickListener(null);
        } else if (has_phone && menu instanceof CoreHivFloatingMenu) {
            hivFloatingMenu = (CoreHivFloatingMenu) menu;
            hivFloatingMenu.getCallLayout().setOnClickListener(hivFloatingMenu);
        } else if (!has_phone && menu instanceof CoreHivFloatingMenu) {
            hivFloatingMenu = (CoreHivFloatingMenu) menu;
            hivFloatingMenu.getCallLayout().setOnClickListener(null);
        }
    }


    public static boolean isMemberOfReproductiveAge(CommonPersonObjectClient commonPersonObject, int fromAge, int toAge) {
        if (commonPersonObject == null) {
            return false;
        }

        // check for age
        String dobString = org.smartregister.util.Utils.getValue(commonPersonObject.getColumnmaps(), "dob", false);
        if (!TextUtils.isEmpty(dobString)) {
            Period period = new Period(new DateTime(dobString), new DateTime());
            int age = period.getYears();
            return age >= fromAge && age <= toAge;
        }

        return false;
    }

    /**
     * This is a compatibility class to process the old child home visit events to
     * the new visits structure
     *
     * @param eventClient
     * @return
     */
    public static List<EventClient> processOldEvents(EventClient eventClient) {

        // remove all nested events and add them to this object
        List<EventClient> events = new ArrayList<>();

        if (eventClient.getEvent() == null) {
            return new ArrayList<>();
        }

        Event event = eventClient.getEvent();
        List<org.smartregister.domain.Obs> observations = new ArrayList<>();
        for (org.smartregister.domain.Obs obs : event.getObs()) {
            switch (obs.getFieldCode()) {
                case "illness_information":
                    try {
                        JSONObject jsonObject = new JSONObject(obs.getValues().get(0).toString());
                        if (jsonObject.has("obsIllness")) {
                            Event obsEvent = convert(jsonObject.getString("obsIllness"), Event.class);
                            events.add(new EventClient(obsEvent, eventClient.getClient()));
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    break;
                case "birth_certificate":
                    try {
                        String val = obs.getValues().get(0).toString();
                        if (val.equalsIgnoreCase("NOT_GIVEN") || val.equalsIgnoreCase("GIVEN")) {
                            observations.add(obs);
                            continue;
                        }

                        JSONObject jsonObject = new JSONObject(val);
                        if (jsonObject.has("birtCert")) {
                            Event obsEvent = convert(jsonObject.getString("birtCert"), Event.class);
                            events.add(new EventClient(obsEvent, eventClient.getClient()));
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    break;
                case "service":
                    try {
                        JSONArray jsonArray = new JSONArray(obs.getValues());
                        int length = jsonArray.length();
                        int x = 0;
                        while (x < length) {
                            String value_raw = jsonArray.getString(x);
                            String values = value_raw.substring(1, value_raw.length() - 1);
                            String[] services = values.split(",");
                            for (String service_str : services) {
                                String[] service = service_str.split(":");
                                if (service.length == 2) {
                                    String key = service[0].substring(1, service[0].length() - 1);
                                    String val = service[1].length() > 1 ? service[1].substring(1, service[1].length() - 1) : service[1];

                                    org.smartregister.domain.Obs obs1 = new org.smartregister.domain.Obs();
                                    obs1.setFieldType("formsubmissionField");
                                    obs1.setFieldDataType("text");
                                    obs1.setFieldCode(key);
                                    obs1.setFormSubmissionField(key);
                                    obs1.setParentCode("");
                                    obs1.setValues(new ArrayList<>(Arrays.asList(val)));
                                    obs1.setHumanReadableValues(new ArrayList<>(Arrays.asList(val)));
                                    observations.add(obs1);
                                }
                            }
                            x++;
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    break;
                default:
                    observations.add(obs);
                    break;
            }

        }

        // exclude these events
        // fieldCode : singleVaccine , service ,  vaccineNotGiven , groupVaccine , serviceNotGiven


        // convert the json in these events
        // fieldCode : birth_certificate

        event.setObs(null);
        event.setObs(observations);

        events.add(new EventClient(event, eventClient.getClient()));

        return events;
    }

    private static <T> T convert(String jsonString, Class<T> t) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return JsonFormUtils.gson.fromJson(jsonString, t);
        } catch (Exception e) {
            Timber.e(e);
            Timber.e(jsonString);
            return null;
        }
    }

    public static String getStringResourceByName(String name, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(name, "string", packageName);
        if (resId == 0) {
            return name;
        } else {
            return context.getString(resId);
        }
    }

    //TODO
    public static MemberObject malariaToAncMember(org.smartregister.chw.malaria.domain.MemberObject memberObject) {

        try {

            JSONObject malariaJson = new JSONObject(org.smartregister.family.util.JsonFormUtils.gson.toJson(memberObject));

            MemberObject memberObject1 = convert(malariaJson.toString(), MemberObject.class);

            return memberObject1;

        } catch (JSONException e) {
            Timber.e(e);
        }

        return null;
    }

    public static MemberObject referralToAncMember(org.smartregister.chw.referral.domain.MemberObject memberObject) {
        try {
            JSONObject referralJson = new JSONObject(org.smartregister.family.util.JsonFormUtils.gson.toJson(memberObject));
            return convert(referralJson.toString(), MemberObject.class);
        } catch (JSONException var3) {
            Timber.e(var3);
            return null;
        }
    }

    public static String getFamilyDueFilter() {
        return "and ec_family.base_entity_id in ( select ec_family_member.relational_id from schedule_service inner join ec_family_member on ec_family_member.base_entity_id = schedule_service.base_entity_id " +
                " where strftime('%Y-%m-%d') BETWEEN schedule_service.due_date and schedule_service.expiry_date and ifnull(schedule_service.not_done_date,'') = '' and ifnull(schedule_service.completion_date,'') = ''  ) ";
    }

    public static Locale getLocale(Context context) {
        if (context == null) {
            return Locale.getDefault();
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static String retrieveValue(List<MonthlyTally> monthlyTallies, Hia2Indicator hia2Indicator) {
        String defaultValue = "0";
        if (hia2Indicator == null || monthlyTallies == null) {
            return defaultValue;
        }

        for (MonthlyTally monthlyTally : monthlyTallies) {
            if (monthlyTally.getIndicator() != null && monthlyTally.getIndicator().getIndicatorCode()
                    .equalsIgnoreCase(hia2Indicator.getIndicatorCode())) {
                return monthlyTally.getValue();
            }
        }

        return defaultValue;
    }

    public static void updateToolbarTitle(Activity activity, int toolbarTextViewId, String familyName) {
        int titleResource = -1;
        if (activity.getIntent().getExtras() != null)
            titleResource = activity.getIntent().getExtras().getInt(CoreConstants.INTENT_KEY.TOOLBAR_TITLE, -1);
        if (titleResource != -1) {
            TextView toolbarTitleTextView = activity.findViewById(toolbarTextViewId);
            if (titleResource == R.string.return_to_family_name) {
                toolbarTitleTextView.setText(activity.getString(R.string.return_to_family_name, familyName));
            } else {
                toolbarTitleTextView.setText(titleResource);
            }
        }
    }

    public static void passToolbarTitle(Activity activity, Intent intent) {
        if (activity instanceof CoreAllClientsRegisterActivity)
            intent.putExtra(CoreConstants.INTENT_KEY.TOOLBAR_TITLE, R.string.return_to_all_client);
        else if (activity instanceof BaseChwNotificationDetailsActivity)
            intent.putExtra(CoreConstants.INTENT_KEY.TOOLBAR_TITLE, R.string.return_to_notification_details);
        else if (activity instanceof CoreFamilyProfileActivity)
            intent.putExtra(CoreConstants.INTENT_KEY.TOOLBAR_TITLE, R.string.return_to_family_name);
        else if (activity instanceof BaseReferralTaskViewActivity)
            intent.putExtra(CoreConstants.INTENT_KEY.TOOLBAR_TITLE, R.string.return_to_task_details);
    }

    @NotNull
    public static CommonPersonObjectClient getCommonPersonObjectClient(@NonNull String baseEntityId) {
        CommonRepository commonRepository = org.smartregister.family.util.Utils.context().commonrepository(org.smartregister.family.util.Utils.metadata().familyMemberRegister.tableName);

        final CommonPersonObject commonPersonObject = commonRepository.findByBaseEntityId(baseEntityId);
        final CommonPersonObjectClient client =
                new CommonPersonObjectClient(commonPersonObject.getCaseId(), commonPersonObject.getDetails(), "");
        client.setColumnmaps(commonPersonObject.getColumnmaps());
        return client;
    }

    public static String getDateDifferenceInDays(Date endDate, Date startDate) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return String.valueOf(TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS));
    }

    public static Pair<String, String> fetchMUACValues(String childBaseEntityId) {
        String muacValue = getMUACValue(childBaseEntityId);
        String muacCode = null;
        String muacDiaplay = null;
        if (!muacValue.isEmpty()) {
            try {
                muacCode = muacValue.substring(4);
                muacDiaplay = muacCode.substring(0, 1).toUpperCase() + muacCode.substring(1);
            } catch (IndexOutOfBoundsException e) {
                Timber.e(e);
            }
        }
        return Pair.create(muacCode, muacDiaplay);
    }

    public static String getRandomGeneratedId() {
        return UUID.randomUUID().toString();
    }

    public static FormTag getFormTag(AllSharedPreferences allSharedPreferences) {
        FormTag formTag = new FormTag();
        String providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.locationId = locationId(allSharedPreferences);
        formTag.childLocationId = allSharedPreferences.fetchCurrentLocality();
        formTag.teamId = allSharedPreferences.fetchDefaultTeamId(providerId);
        formTag.team = allSharedPreferences.fetchDefaultTeam(providerId);
        formTag.databaseVersion = FamilyLibrary.getInstance().getDatabaseVersion();
        formTag.appVersion = FamilyLibrary.getInstance().getApplicationVersion();
        return formTag;
    }

    // date utils from core
    public static String getDuration(String date) {
        DateTime duration;
        if (StringUtils.isNotBlank(date)) {
            try {
                duration = new DateTime(date);
                return getDuration(duration);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return "";
    }


    public static String getDuration(String date1, String date2) {
        try {
            DateTime dateTime1 = toDateTime(date1);
            DateTime dateTime2 = toDateTime(date2);

            if (dateTime1 == null || dateTime2 == null) {
                return "";
            }

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateTime1.toDate());
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(dateTime2.toDate());
            calendar2.set(Calendar.HOUR_OF_DAY, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.MILLISECOND, 0);

            long timeDiff = Math.abs(calendar1.getTimeInMillis() - calendar2.getTimeInMillis());

            return getDuration(timeDiff, dateTime1, dateTime2, getLocale());

        } catch (Exception e) {
            Timber.e(e);
        }
        return "";
    }

    public static String getDuration(DateTime dateTime) {
        if (dateTime != null) {
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(dateTime.toDate());
            dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            dateCalendar.set(Calendar.MINUTE, 0);
            dateCalendar.set(Calendar.SECOND, 0);
            dateCalendar.set(Calendar.MILLISECOND, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long timeDiff = Math.abs(dateCalendar.getTimeInMillis() - today.getTimeInMillis());

            DateTime todayDateTime = new DateTime();
            return getDuration(timeDiff, dateTime, todayDateTime, getLocale());
        }
        return null;
    }

    private static Locale getLocale() {
        Locale locale = CoreLibrary.getInstance().context().applicationContext().getResources().getConfiguration().locale;
        locale = locale != null && locale.toString().startsWith("ar") ? Locale.ENGLISH : locale;

        return locale;
    }

    public static String getDuration(long timeDiff, DateTime dateTime, DateTime todayDateTime, Locale locale) {

        Context context = CoreLibrary.getInstance().context().applicationContext();
        String duration;
        if (timeDiff >= 0
                && timeDiff <= TimeUnit.MILLISECONDS.convert(13, TimeUnit.DAYS)) {
            // Represent in days
            long days = Math.abs(Days.daysBetween(dateTime.toLocalDate(), todayDateTime.toLocalDate()).getDays());
            duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_days), days);
        } else if (timeDiff > TimeUnit.MILLISECONDS.convert(13, TimeUnit.DAYS)
                && timeDiff <= TimeUnit.MILLISECONDS.convert(97, TimeUnit.DAYS)) {
            // Represent in weeks and days
            int weeks = (int) Math.floor((float) timeDiff /
                    TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS));
            int days = (int) Math.floor((float) (timeDiff -
                    TimeUnit.MILLISECONDS.convert(weeks * 7, TimeUnit.DAYS)) /
                    TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

            if (days >= 7) {
                days = 0;
                weeks++;
            }

            if (days > 0) {
                duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_weeks_days), weeks, days);
            } else {
                duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_weeks), weeks);
            }

        } else if (timeDiff > TimeUnit.MILLISECONDS.convert(97, TimeUnit.DAYS)
                && timeDiff <= TimeUnit.MILLISECONDS.convert(363, TimeUnit.DAYS)) {
            // Represent in months and weeks
            int months = (int) Math.floor((float) timeDiff /
                    TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS));
            int weeks = (int) Math.floor((float) (timeDiff - TimeUnit.MILLISECONDS.convert(
                    months * 30, TimeUnit.DAYS)) /
                    TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS));

            if (weeks >= 4) {
                weeks = 0;
                months++;
            }

            if (months < 12) {
                if (weeks > 0) {
                    duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_months_weeks), months, weeks);
                } else {
                    duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_months), months);
                }
            } else {
                duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_years), 1);
            }
        } else {
            // Represent in years and months
            int years = Math.abs(Years.yearsBetween(dateTime.toLocalDate(), todayDateTime.toLocalDate()).getYears());
            int totalMonths = Math.abs(Months.monthsBetween(dateTime.toLocalDate(), todayDateTime.toLocalDate()).getMonths());
            int months = Math.abs(totalMonths - (12 * years));

            if (months > 0) {
                duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_years_months), years, months);
            } else {
                duration = String.format(locale, context.getResources().getString(org.smartregister.R.string.x_years), years);
            }
        }

        return duration;

    }
}
