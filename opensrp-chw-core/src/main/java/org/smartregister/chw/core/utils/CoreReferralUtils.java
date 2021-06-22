package org.smartregister.chw.core.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.dao.ReferralTaskDao;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.domain.Task;
import org.smartregister.family.util.DBConstants;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

public class CoreReferralUtils {

    public static String mainSelect(String tableName, String familyTableName, String mainCondition) {
        return mainSelectRegisterWithoutGroupby(tableName, familyTableName, tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + mainCondition + "'");
    }

    private static String mainSelectRegisterWithoutGroupby(String tableName, String familyTableName, String mainCondition) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(tableName, mainColumns(tableName, familyTableName));
        queryBuilder.customJoin("LEFT JOIN " + familyTableName + " ON  " + tableName + "." + DBConstants.KEY.RELATIONAL_ID + " = " + familyTableName + ".id COLLATE NOCASE ");
        return queryBuilder.mainCondition(mainCondition);
    }

    public static String[] mainColumns(String tableName, String familyTable) {
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add(tableName + "." + DBConstants.KEY.RELATIONAL_ID + " as " + ChildDBConstants.KEY.RELATIONAL_ID);
        columnList.add(tableName + "." + DBConstants.KEY.LAST_INTERACTED_WITH);
        columnList.add(tableName + "." + DBConstants.KEY.BASE_ENTITY_ID);
        columnList.add(tableName + "." + DBConstants.KEY.FIRST_NAME);
        columnList.add(tableName + "." + DBConstants.KEY.MIDDLE_NAME);
        columnList.add(tableName + "." + DBConstants.KEY.LAST_NAME);
        columnList.add(familyTable + "." + DBConstants.KEY.VILLAGE_TOWN + " as " + ChildDBConstants.KEY.FAMILY_HOME_ADDRESS);
        columnList.add(familyTable + "." + DBConstants.KEY.PRIMARY_CAREGIVER);
        columnList.add(familyTable + "." + DBConstants.KEY.FAMILY_HEAD);
        columnList.add(tableName + "." + DBConstants.KEY.UNIQUE_ID);
        columnList.add(tableName + "." + DBConstants.KEY.GENDER);
        columnList.add(tableName + "." + DBConstants.KEY.DOB);
        columnList.add(tableName + "." + org.smartregister.family.util.Constants.JSON_FORM_KEY.DOB_UNKNOWN);
        return columnList.toArray(new String[columnList.size()]);
    }

    public static String mainCareGiverSelect(String tableName, String mainCondition) {
        return createCareGiverSelect(tableName, tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + mainCondition + "'");
    }

    private static String createCareGiverSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder smartRegisterQueryBuilder = new SmartRegisterQueryBuilder();
        smartRegisterQueryBuilder.selectInitiateMainTable(tableName, mainCareGiverColumns(tableName));
        return smartRegisterQueryBuilder.mainCondition(mainCondition);
    }

    private static String[] mainCareGiverColumns(String tableName) {
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add(tableName + "." + DBConstants.KEY.RELATIONAL_ID + " as " + ChildDBConstants.KEY.RELATIONAL_ID);
        columnList.add(tableName + "." + DBConstants.KEY.FIRST_NAME + " as " + ChildDBConstants.KEY.FAMILY_FIRST_NAME);
        columnList.add(tableName + "." + DBConstants.KEY.MIDDLE_NAME + " as " + ChildDBConstants.KEY.FAMILY_LAST_NAME);
        columnList.add(tableName + "." + DBConstants.KEY.LAST_NAME + " as " + ChildDBConstants.KEY.FAMILY_MIDDLE_NAME);
        columnList.add(tableName + "." + DBConstants.KEY.PHONE_NUMBER + " as " + ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER);
        columnList.add(tableName + "." + DBConstants.KEY.OTHER_PHONE_NUMBER + " as " + ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER_OTHER);
        return columnList.toArray(new String[columnList.size()]);
    }

    public static String mainAncDetailsSelect(String tableName, String baseEntityId) {
        return createAncDetailsSelect(tableName, tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + baseEntityId + "'");
    }

    public static String mainAncDetailsSelect(String[] tableNames, int familyTableIndex, int ancDetailsColumnsTableIndex, String baseEntityId) {
        return createAncDetailsSelect(tableNames, ancDetailsColumnsTableIndex, tableNames[ancDetailsColumnsTableIndex] + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + baseEntityId +
                "' AND " + tableNames[familyTableIndex] + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + tableNames[ancDetailsColumnsTableIndex] + "." + DBConstants.KEY.RELATIONAL_ID);
    }

    private static String createAncDetailsSelect(String tableName, String selectCondition) {
        SmartRegisterQueryBuilder smartRegisterQueryBuilder = new SmartRegisterQueryBuilder();
        smartRegisterQueryBuilder.selectInitiateMainTable(tableName, mainAncDetailsColumns(tableName));
        smartRegisterQueryBuilder.customJoin("LEFT JOIN " + CoreConstants.TABLE_NAME.ANC_MEMBER_LOG + " ON  " + tableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + CoreConstants.TABLE_NAME.ANC_MEMBER_LOG + ".id COLLATE NOCASE ");
        smartRegisterQueryBuilder.customJoin("LEFT JOIN " + CoreConstants.TABLE_NAME.FAMILY + " ON  " + tableName + "." + DBConstants.KEY.RELATIONAL_ID + " = " + CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.BASE_ENTITY_ID);
        return smartRegisterQueryBuilder.mainCondition(selectCondition);
    }

    private static String createAncDetailsSelect(String[] tableNames, int ancDetailsColumnsTableIndex, String selectCondition) {
        SmartRegisterQueryBuilder smartRegisterQueryBuilder = new SmartRegisterQueryBuilder();
        smartRegisterQueryBuilder.selectInitiateMainTable(tableNames, mainAncDetailsColumns(tableNames[ancDetailsColumnsTableIndex]));
        smartRegisterQueryBuilder.customJoin("LEFT JOIN " + CoreConstants.TABLE_NAME.ANC_MEMBER_LOG + " ON  " + tableNames[ancDetailsColumnsTableIndex] + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + CoreConstants.TABLE_NAME.ANC_MEMBER_LOG + ".id COLLATE NOCASE ");
        return smartRegisterQueryBuilder.mainCondition(selectCondition);
    }

    private static String[] mainAncDetailsColumns(String tableName) {
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add(tableName + "." + DBConstants.KEY.RELATIONAL_ID + " as " + ChildDBConstants.KEY.RELATIONAL_ID);
        columnList.add(tableName + "." + ChildDBConstants.KEY.LAST_MENSTRUAL_PERIOD);
        columnList.add(CoreConstants.TABLE_NAME.ANC_MEMBER_LOG + "." + org.smartregister.chw.anc.util.DBConstants.KEY.DATE_CREATED);
        columnList.add(CoreConstants.TABLE_NAME.FAMILY + "." + DBConstants.KEY.VILLAGE_TOWN);
        columnList.add(tableName + "." + org.smartregister.chw.anc.util.DBConstants.KEY.CONFIRMED_VISITS);
        columnList.add(tableName + "." + org.smartregister.chw.anc.util.DBConstants.KEY.LAST_HOME_VISIT);
        return columnList.toArray(new String[0]);
    }

    public static String pncFamilyMemberProfileDetailsSelect(String familyTableName, String baseEntityId) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.selectInitiateMainTable(familyTableName, pncFamilyMemberProfileDetails(familyTableName));
        queryBuilder.customJoin("LEFT JOIN " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + " ON  " + familyTableName + "." + DBConstants.KEY.BASE_ENTITY_ID + " = " + CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.RELATIONAL_ID);
        return queryBuilder.mainCondition(CoreConstants.TABLE_NAME.FAMILY_MEMBER + "." + DBConstants.KEY.BASE_ENTITY_ID + " = '" + baseEntityId + "'");
    }

    private static String[] pncFamilyMemberProfileDetails(String familyTable) {
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add(familyTable + "." + ChildDBConstants.KEY.RELATIONAL_ID);
        columnList.add(familyTable + "." + DBConstants.KEY.VILLAGE_TOWN);
        columnList.add(familyTable + "." + DBConstants.KEY.PRIMARY_CAREGIVER);
        columnList.add(familyTable + "." + DBConstants.KEY.FAMILY_HEAD);
        return columnList.toArray(new String[columnList.size()]);
    }

    public static void createReferralEvent(AllSharedPreferences allSharedPreferences, String jsonString, String referralTable, String entityId) throws Exception {
        final Event baseEvent = org.smartregister.chw.anc.util.JsonFormUtils.processJsonForm(allSharedPreferences, setEntityId(jsonString, entityId), referralTable);
        NCUtils.processEvent(baseEvent.getBaseEntityId(), new JSONObject(org.smartregister.chw.anc.util.JsonFormUtils.gson.toJson(baseEvent)));
        createReferralTask(baseEvent.getBaseEntityId(), allSharedPreferences, assignReferralFocus(referralTable), getReferralProblems(jsonString));
    }

    public static String setEntityId(String jsonString, String entityId) {
        String jsonForm = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonObject.put(CoreConstants.ENTITY_ID, entityId);

            jsonForm = jsonObject.toString();
        } catch (JSONException e) {
            Timber.e(e, "Interactor --> setEntityId");
        }

        return jsonForm;
    }

    private static void createReferralTask(String baseEntityId, AllSharedPreferences allSharedPreferences, String focus, String referralProblems) {
        Task task = new Task();
        task.setIdentifier(UUID.randomUUID().toString());
        //TODO Implement plans
      /*  Iterator<String> iterator = ChwApplication.getInstance().getPlanDefinitionRepository()
                .findAllPlanDefinitionIds().iterator();
        if (iterator.hasNext()) {
            task.setPlanIdentifier(iterator.next());
        } else {

            Timber.e("No plans exist in the server");
        }*/
        task.setPlanIdentifier(CoreConstants.REFERRAL_PLAN_ID);
        LocationHelper locationHelper = LocationHelper.getInstance();
        task.setGroupIdentifier(locationHelper.getOpenMrsLocationId(locationHelper.generateDefaultLocationHierarchy(CoreChwApplication.getInstance().getAllowedLocationLevels()).get(0)));
        task.setStatus(Task.TaskStatus.READY);
        task.setBusinessStatus(CoreConstants.BUSINESS_STATUS.REFERRED);
        task.setPriority(3);
        task.setCode(CoreConstants.JsonAssets.REFERRAL_CODE);
        task.setDescription(referralProblems);
        task.setFocus(focus);
        task.setForEntity(baseEntityId);
        DateTime now = new DateTime();
        task.setExecutionStartDate(now);
        task.setAuthoredOn(now);
        task.setLastModified(now);
        task.setOwner(allSharedPreferences.fetchRegisteredANM());
        task.setSyncStatus(BaseRepository.TYPE_Created);
        task.setRequester(allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM()));
        task.setLocation(allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM()));
        CoreChwApplication.getInstance().getTaskRepository().addOrUpdate(task);
    }

    private static String assignReferralFocus(String referralTable) {
        String focus;
        switch (referralTable) {
            case CoreConstants.TABLE_NAME.CHILD_REFERRAL:
                focus = CoreConstants.TASKS_FOCUS.SICK_CHILD;
                break;
            case CoreConstants.TABLE_NAME.ANC_REFERRAL:
                focus = CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS;
                break;
            case CoreConstants.TABLE_NAME.PNC_REFERRAL:
                focus = CoreConstants.TASKS_FOCUS.PNC_DANGER_SIGNS;
                break;
            case CoreConstants.TABLE_NAME.FP_REFERRAL:
                focus = CoreConstants.TASKS_FOCUS.FP_SIDE_EFFECTS;
                break;
            case CoreConstants.TABLE_NAME.MALARIA_REFERRAL:
                focus = CoreConstants.TASKS_FOCUS.SUSPECTED_MALARIA;
                break;
            default:
                focus = "";
                break;
        }

        return focus;
    }

    @VisibleForTesting
    static String getReferralProblems(String jsonString) {
        String referralProblems = "";
        List<String> formValues = new ArrayList<>();
        try {
            JSONObject problemJson = new JSONObject(jsonString);
            JSONArray fields = FormUtils.getMultiStepFormFields(problemJson);
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if (field.optBoolean(CoreConstants.JsonAssets.IS_PROBLEM, true)) {
                    if (field.has(JsonFormConstants.TYPE) && JsonFormConstants.CHECK_BOX.equals(field.getString(JsonFormConstants.TYPE))) {
                        if (field.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
                            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
                            String values = getCheckBoxSelectedOptions(options);
                            if (StringUtils.isNotEmpty(values)) {
                                formValues.add(values);
                            }
                        }
                    } else if (field.has(JsonFormConstants.TYPE) && JsonFormConstants.RADIO_BUTTON.equals(field.getString(JsonFormConstants.TYPE))) {
                        if (field.has(JsonFormConstants.OPTIONS_FIELD_NAME) && field.has(JsonFormConstants.VALUE)) {
                            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
                            String value = field.getString(JsonFormConstants.VALUE);
                            String values = getRadioButtonSelectedOptions(options, value);
                            if (StringUtils.isNotEmpty(values)) {
                                formValues.add(values);
                            }
                        }
                    } else {
                        String values = getOtherWidgetSelectedItems(field);
                        if (StringUtils.isNotEmpty(values)) {
                            formValues.add(values);
                        }
                    }
                }
            }

            referralProblems = StringUtils.join(formValues, ", ");
        } catch (JSONException e) {
            Timber.e(e, "CoreReferralUtils --> getReferralProblems");
        }
        return referralProblems;
    }

    private static String getCheckBoxSelectedOptions(@NotNull JSONArray options) {
        String selectedOptionValues = "";
        List<String> selectedValue = new ArrayList<>();
        try {
            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);
                boolean useItem = true;

                if (option.optBoolean(CoreConstants.IGNORE, false)) {
                    useItem = false;
                }

                if (option.has(JsonFormConstants.VALUE) && Boolean.valueOf(option.getString(JsonFormConstants.VALUE))
                        && useItem) { //Don't add values for  items with other
                    selectedValue.add(option.getString(JsonFormConstants.TEXT));
                }
            }
            selectedOptionValues = StringUtils.join(selectedValue, ", ");
        } catch (JSONException e) {
            Timber.e(e, "CoreReferralUtils --> getSelectedOptions");
        }

        return selectedOptionValues;
    }

    private static String getRadioButtonSelectedOptions(@NotNull JSONArray options, String value) {
        String selectedOptionValues = "";
        try {
            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);
                if ((option.has(JsonFormConstants.KEY) && value.equals(option.getString(JsonFormConstants.KEY))) && (option.has(JsonFormConstants.TEXT) && StringUtils.isNotEmpty(option.getString(JsonFormConstants.VALUE)))) {
                    selectedOptionValues = option.getString(JsonFormConstants.TEXT);
                }
            }
        } catch (JSONException e) {
            Timber.e(e, "CoreReferralUtils --> getSelectedOptions");
        }

        return selectedOptionValues;
    }

    private static String getOtherWidgetSelectedItems(@NotNull JSONObject jsonObject) {
        String value = "";
        try {
            if (jsonObject.has(JsonFormConstants.VALUE) && StringUtils.isNotEmpty(jsonObject.getString(JsonFormConstants.VALUE))) {
                value = jsonObject.getString(JsonFormConstants.VALUE);
            }
        } catch (JSONException e) {
            Timber.e(e, "CoreReferralUtils --> getOtherWidgetSelectedItems");
        }

        return value;
    }

    public static CommonRepository getCommonRepository(String tableName) {
        return Utils.context().commonrepository(tableName);
    }

    @NotNull
    public static String getFamilyMemberFtsSearchQuery(@Nullable String filters) {
        if (TextUtils.isEmpty(filters)) {
            return "SELECT object_id, last_interacted_with\n" +
                    "FROM (SELECT object_id, last_interacted_with FROM ec_family_member_search WHERE date_removed IS NULL)\n" +
                    "ORDER BY last_interacted_with DESC";
        } else {
            String query = "SELECT object_id\n" +
                    "FROM (SELECT object_id, last_interacted_with\n" +
                    "      FROM ec_family_member_search\n" +
                    "      WHERE date_removed IS NULL\n" +
                    "        AND phrase MATCH '%s*'\n" +
                    "     )\n" +
                    "ORDER BY last_interacted_with DESC";
            query = query.replace("%s", filters);
            return query;
        }
    }

    /**
     * This method is used by CHW to complete a referral task by marking it as done.
     */
    public static void completeClosedReferralTasks() {
        List<Task> tasksToBeCompleted = ReferralTaskDao.getToBeCompletedReferralTasks();
        if (tasksToBeCompleted != null) {
            Timber.d("Tasks to be completed size: %s ", tasksToBeCompleted.size());
            for (Task task : tasksToBeCompleted) {
                completeTask(task, true);
            }
        }
    }

    public static void completeTask(Task currentTask, boolean endTask) {
        DateTime now = new DateTime();
        if (endTask) {
            currentTask.setStatus(Task.TaskStatus.COMPLETED);
        }
        currentTask.setBusinessStatus(CoreConstants.BUSINESS_STATUS.COMPLETE);
        currentTask.setLastModified(now);
        currentTask.setSyncStatus(BaseRepository.TYPE_Unsynced);
        CoreChwApplication.getInstance().getTaskRepository().addOrUpdate(currentTask);
    }
}