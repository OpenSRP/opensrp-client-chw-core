package org.smartregister.chw.core.dao;

import android.content.Context;
import android.util.Pair;

import org.smartregister.chw.core.domain.NotificationRecord;
import org.smartregister.chw.core.utils.ChwNotificationUtil;
import org.smartregister.dao.AbstractDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class ChwNotificationDao extends AbstractDao {

    /**
     * This method is used to get details of referral notification with the provided task id
     *
     * @param referralId unique identifier for the task
     * @return a notification record with details for the referral
     */
    @Deprecated
    public static NotificationRecord getSuccessfulReferral(String referralId) {
        String sql = String.format(
                "/* Get details for successful referral */\n" +
                        "SELECT ec_family_member.first_name || ' ' || CASE ec_family_member.last_name\n" +
                        "                                                 WHEN NULL THEN ec_family_member.middle_name\n" +
                        "                                                 ELSE ec_family_member.last_name END full_name,\n" +
                        "       ec_family_member.dob          AS                                              dob,\n" +
                        "       ec_family.village_town        AS                                              village,\n" +
                        "       event.dateCreated             AS                                              notification_date,\n" +
                        "       ec_family_member.phone_number AS                                              phone_number,\n" +
                        "       ec_family_member.base_entity_id AS                                            base_entity_id\n" +
                        "\n" +
                        "FROM task\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
                        "         inner join ec_close_referral on ec_close_referral.referral_task = task._id\n" +
                        "         inner join event on ec_close_referral.id = event.formSubmissionId\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND task.code = 'Referral'\n" +
                        "  AND task._id = '%s' COLLATE NOCASE\n", referralId);

        return AbstractDao.readSingleValue(sql, mapColumnValuesToModel());

    }

    public static NotificationRecord getSickChildFollowUpRecord(String notificationId) {
        String sql = String.format(
                "/* Get details for a sick child follow-up */\n" +
                        "SELECT ec_family_member.first_name || ' ' || ifnull(ec_family_member.last_name, ec_family_member.middle_name) as full_name,\n" +
                        "cg.first_name || ' ' || ifnull(cg.last_name, cg.middle_name) as cg_full_name,\n" +
                        "       ec_family.village_town        AS      village,\n" +
                        "       ec_sick_child_followup.diagnosis,\n" +
                        "       ec_sick_child_followup.results,\n" +
                        "       ec_sick_child_followup.visit_date\n" +
                        "\n" +
                        "FROM ec_sick_child_followup\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = ec_sick_child_followup.entity_id\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "         inner join (select fm.base_entity_id, fm.first_name, fm.middle_name, fm.last_name from ec_family_member fm) cg on ec_family.primary_caregiver = cg.base_entity_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND ec_sick_child_followup.id = '%s'\n", notificationId);

        return AbstractDao.readSingleValue(sql, mapColumnValuesToModel());
    }

    public static NotificationRecord getAncPncDangerSignsOutcomeRecord(String notificationId, String table) {
        String sql = String.format(
                "/* Get details for ANC or PNC Danger Signs Outcome */\n" +
                        "SELECT ec_family_member.first_name || ' ' || ifnull(ec_family_member.last_name, ec_family_member.middle_name) as full_name,\n" +
                        "ec_family.village_town        AS      village,\n" +
                        table + ".danger_signs_present,\n" +
                        table + ".action_taken,\n" +
                        table + ".visit_date\n" +
                        "FROM " + table + "\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = " + table + ".entity_id\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND " + table + ".id = '%s'\n", notificationId);

        return AbstractDao.readSingleValue(sql, mapColumnValuesToModel());
    }

    public static NotificationRecord getHivTBOutcomeRecord(String notificationId, String table) {
        String sql = String.format(
                "/* Get details for HIV or TB Danger Signs Outcome */\n" +
                        "SELECT ec_family_member.first_name || ' ' || ifnull(ec_family_member.last_name, ec_family_member.middle_name) as full_name,\n" +
                        "ec_family.village_town        AS      village,\n" +
                        table + ".problem,\n" +
                        table + ".action_taken,\n" +
                        table + ".test_results,\n" +
                        table + ".visit_date\n" +
                        "FROM " + table + "\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = " + table + ".entity_id\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND " + table + ".id = '%s'\n", notificationId);

        return AbstractDao.readSingleValue(sql, mapHivTbColumnValuesToModel());
    }


    public static NotificationRecord getMalariaFollowUpRecord(String notificationId) {
        String sql = String.format(
                "/* Get details for a sick child follow-up */\n" +
                        "SELECT ec_family_member.first_name || ' ' || ifnull(ec_family_member.last_name, ec_family_member.middle_name) as full_name,\n" +
                        "       ec_family.village_town        AS      village,\n" +
                        "       ec_malaria_followup_hf.outcomes AS action_taken,\n" +
                        "       ec_malaria_followup_hf.test_results AS results,\n" +
                        "       ec_malaria_followup_hf.visit_date\n" +
                        "\n" +
                        "FROM ec_malaria_followup_hf\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = ec_malaria_followup_hf.entity_id\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND ec_malaria_followup_hf.id = '%s'\n", notificationId);

        return AbstractDao.readSingleValue(sql, mapColumnValuesToModel());
    }

    public static NotificationRecord getFamilyPlanningRecord(String notificationId) {
        String sql = String.format(
                "/* Get details for family planning registration or method change */\n" +
                        "SELECT ec_family_member.first_name || ' ' || ifnull(ec_family_member.last_name, ec_family_member.middle_name) as full_name,\n" +
                        "       ec_family.village_town        AS      village,\n" +
                        "       ec_family_planning_update.fp_method_accepted AS method,\n" +
                        "       ec_family_planning_update.fp_reg_date AS visit_date\n" +
                        "\n" +
                        "FROM ec_family_planning_update\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = ec_family_planning_update.entity_id\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND ec_family_planning_update.id = '%s'\n", notificationId);

        return AbstractDao.readSingleValue(sql, mapColumnValuesToModel());
    }

    private static DataMap<NotificationRecord> mapColumnValuesToModel() {
        return row -> {
            NotificationRecord record = new NotificationRecord(getCursorValue(row, "base_entity_id"));
            record.setClientName(getCursorValue(row, "full_name"));
            record.setVillage(getCursorValue(row, "village"));
            record.setVisitDate(formatVisitDate(getCursorValue(row, "visit_date")));

            String careGiverName = getCursorValue(row, "cg_full_name");
            String diagnosis = getCursorValue(row, "diagnosis");
            String results = getCursorValue(row, "results");
            String dangerSignsPresent = getCursorValue(row, "danger_signs_present");
            String actionTaken = getCursorValue(row, "action_taken");
            String method = getCursorValue(row, "method");

            if (careGiverName != null) {
                record.setCareGiverName(careGiverName);
            }
            if (diagnosis != null) {
                record.setDiagnosis(diagnosis);
            }
            if (results != null) {
                record.setResults(ChwNotificationUtil.getStringFromJSONArrayString(results));
            }
            if (dangerSignsPresent != null) {
                record.setDangerSigns(ChwNotificationUtil.getStringFromJSONArrayString(dangerSignsPresent));
            }
            if (actionTaken != null) {
                record.setActionTaken(ChwNotificationUtil.getStringFromJSONArrayString(actionTaken));
            }
            if (method != null) {
                record.setMethod(method);
            }
            return record;
        };
    }

    private static DataMap<NotificationRecord> mapHivTbColumnValuesToModel() {
        return row -> {
            NotificationRecord record = new NotificationRecord(getCursorValue(row, "base_entity_id"));
            record.setVillage(getCursorValue(row, "village"));
            record.setClientName(getCursorValue(row, "full_name"));
            String careGiverName = getCursorValue(row, "cg_full_name");
            record.setVisitDate(formatVisitDate(getCursorValue(row, "visit_date","")));
            String diagnosis = getCursorValue(row, "diagnosis");
            String results = getCursorValue(row, "test_results");
            String dangerSigns = getCursorValue(row, "problem");
            String actionTaken = getCursorValue(row, "action_taken");

            if (careGiverName != null) {
                record.setCareGiverName(careGiverName);
            }
            if (diagnosis != null) {
                record.setDiagnosis(diagnosis);
            }
            if (results != null) {
                record.setResults(ChwNotificationUtil.getStringFromJSONArrayString(results));
            }
            if (dangerSigns != null) {
                record.setDangerSigns(ChwNotificationUtil.getStringFromJSONArrayString(dangerSigns));
            }
            if (actionTaken != null) {
                record.setActionTaken(ChwNotificationUtil.getStringFromJSONArrayString(actionTaken));
            }
            return record;
        };
    }

    /**
     * This method is used to check whether a Notification has been marked as done or not
     *
     * @param context        the Android context for String value retrieval
     * @param notificationId the unique identifier for the notification record
     * @return true if the Notification is closed and false if otherwise
     */
    public static boolean isMarkedAsDone(Context context, String notificationId, String notificationType) {
        String table = ChwNotificationUtil.getNotificationDetailsTable(context, notificationType);
        String sql = String.format("select count(*) count from %s where id = '%s' and is_closed = 1", table, notificationId);
        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");
        List<Integer> res = readData(sql, dataMap);

        if (res == null || res.size() != 1)
            return false;

        return res.get(0) > 0;
    }

    public static String getSyncLocationId(String baseEntityId) {
        String sql = String.format("SELECT sync_location_id FROM ec_family_member WHERE base_entity_id = '%s'", baseEntityId);
        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "sync_location_id");
        List<String> res = readData(sql, dataMap);

        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    /**
     * This method is used to obtain the syncLocationId of the family head
     * by using the passed familyBaseEntity Id
     * @param familyBaseEntityId family base entity id
     * @return family head sync location id
     */
    public static String getFamilyHeadSyncLocationId(String familyBaseEntityId) {
        String sql = String.format(
                " SELECT sync_location_id FROM ec_family f " +
                        " INNER JOIN ec_family_member fm ON f.family_head = fm.base_entity_id " +
                        " WHERE f.base_entity_id = '%s'", familyBaseEntityId);
        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "sync_location_id");
        List<String> res = readData(sql, dataMap);

        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    public static void markNotificationAsDone(Context context, String notificationId, String notificationTable, String dateMarkedAsDone) {
        String sql = String.format("UPDATE %s SET is_closed = '1', date_marked_as_done = '%s' WHERE id = '%s'", notificationTable, dateMarkedAsDone, notificationId);
        updateDB(sql);
    }

    private static String formatVisitDate(String visitDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//12-08-2018 14:05:10
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(visitDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            Timber.e(e);
        }
        return "";
    }

    /**
     * This method is used to get details of referral notification with the provided task id
     *
     * @param referralId unique identifier for the task
     * @return a notification record with details for the referral
     */
    public static NotificationRecord getNotYetDoneReferral(String referralId) {
        String sql = String.format(
                "/* Get details for not yet done referral */\n" +
                        "SELECT ec_family_member.first_name || ' ' || CASE ec_family_member.last_name\n" +
                        "                                                 WHEN NULL THEN ec_family_member.middle_name\n" +
                        "                                                 ELSE ec_family_member.last_name END full_name,\n" +
                        "       ec_family_member.dob          AS                                              dob,\n" +
                        "       ec_family.village_town        AS                                              village,\n" +
                        "       event.dateCreated             AS                                              notification_date,\n" +
                        "       ec_family_member.phone_number AS                                              phone_number,\n" +
                        "       ec_family_member.base_entity_id AS                                            base_entity_id\n" +
                        "\n" +
                        "FROM task\n" +
                        "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
                        "         inner join ec_not_yet_done_referral on ec_not_yet_done_referral.referral_task = task._id\n" +
                        "         inner join event on ec_not_yet_done_referral.id = event.formSubmissionId\n" +
                        "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
                        "\n" +
                        "WHERE ec_family_member.is_closed = '0'\n" +
                        "  AND ec_family_member.date_removed is null\n" +
                        "  AND task.code = 'Referral'\n" +
                        "  AND task._id = '%s' COLLATE NOCASE\n", referralId);

        return AbstractDao.readSingleValue(sql, mapReferralNotificationColumns());

    }

    private static DataMap<NotificationRecord> mapReferralNotificationColumns() {
        return row -> {
            NotificationRecord record = new NotificationRecord(getCursorValue(row, "base_entity_id"));
            record.setClientName(getCursorValue(row, "full_name"));
            record.setClientDateOfBirth(getCursorValue(row, "dob"));
            record.setVillage(getCursorValue(row, "village"));
            record.setNotificationDate(getCursorValue(row, "notification_date"));
            record.setPhone(getCursorValue(row, "phone_number"));
            return record;
        };
    }

    /**
     * This method is used to retrieve all the notifications belonging to the client with the
     * specified id
     *
     * @param baseEntityId unique identifier for the client
     * @return a list of pair of the notification id and type
     */
    public static List<Pair<String, String>> getClientNotifications(String baseEntityId) {
        String query =
                "SELECT id as notification_id, 'Sick Child' as notification_type\n" +
                        "FROM ec_sick_child_followup\n" +
                        "WHERE base_entity_id = '%s' COLLATE NOCASE\n" +
                        "UNION ALL\n" +
                        "SELECT id as notification_id, 'PNC Danger Signs' as notification_type\n" +
                        "FROM ec_pnc_danger_signs_outcome\n" +
                        "WHERE base_entity_id = '%s'  COLLATE NOCASE\n" +
                        "UNION ALL\n" +
                        "SELECT id as notification_id, 'ANC Danger Signs' as notification_type\n" +
                        "FROM ec_anc_danger_signs_outcome\n" +
                        "WHERE base_entity_id = '%s'  COLLATE NOCASE\n" +
                        "UNION ALL\n" +
                        "SELECT id as notification_id, 'Malaria Follow-up' as notification_type\n" +
                        "FROM ec_malaria_followup_hf\n" +
                        "WHERE base_entity_id = '%s'  COLLATE NOCASE\n" +
                        "UNION ALL\n" +
                        "SELECT id as notification_id, 'Family Planning' as notification_type\n" +
                        "FROM ec_family_planning_update\n" +
                        "WHERE base_entity_id = '%s'  COLLATE NOCASE\n" +
                        "UNION ALL\n" +
                        "SELECT id as notification_id, 'Referral not completed yet' as notification_type\n" +
                        "FROM ec_not_yet_done_referral\n" +
                        "WHERE entity_id = '%s'  COLLATE NOCASE;";

        List<Pair<String, String>> values = AbstractDao.readData(query.replace("%s", baseEntityId),
                getNotificationPair());
        if (values == null || values.size() == 0)
            return new ArrayList<>();
        return values;
    }

    private static DataMap<Pair<String, String>> getNotificationPair() {
        return cursor -> Pair.create(
                getCursorValue(cursor, "notification_id"),
                getCursorValue(cursor, "notification_type"));
    }
}
