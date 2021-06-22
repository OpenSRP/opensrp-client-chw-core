package org.smartregister.chw.core.utils;

public interface QueryConstant {

    @Deprecated
    String SUCCESSFUL_REFERRAL_QUERY_COUNT =
            "/* COUNT NOTIFICATION REFERRALS MARKED AS DONE AT THE FACILITY */\n" +
                    "SELECT COUNT(*) AS c\n" +
                    "FROM task\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
                    "         inner join ec_close_referral on ec_close_referral.referral_task = task._id\n" +
                    "         inner join event on ec_close_referral.id = event.formSubmissionId\n" +
                    "\n" +
                    "WHERE ec_family_member.is_closed = '0'\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND task.business_status = 'Complete'\n" +
                    "  AND task.status = 'COMPLETED'\n" +
                    "  AND task.code = 'Referral'\n";

    @Deprecated
    String SUCCESSFUL_REFERRAL_QUERY =
            "/* NOTIFICATION FROM REFERRALS MARKED AS DONE AT THE FACILITY */\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       task._id                       AS referral_task_id,\n" +
                    "       event.dateCreated              AS notification_date,\n" +
                    "       'Successful referral'          AS notification_type\n" +
                    "\n" +
                    "FROM task\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
                    "         inner join ec_close_referral on ec_close_referral.referral_task = task._id\n" +
                    "         inner join event on ec_close_referral.id = event.formSubmissionId\n" +
                    "\n" +
                    "WHERE ec_family_member.is_closed = '0'\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND task.business_status = 'Complete'\n" +
                    "  AND (task.status = 'READY' OR task.status = 'IN_PROGRESS')\n" +
                    "  AND task.code = 'Referral'\n" +
                    "  AND ec_family_member.base_entity_id IN (%s)\n" +
                    "ORDER BY event.dateCreated DESC";

    String NOT_YET_DONE_REFERRAL_COUNT_QUERY =
            "/* COUNT NOTIFICATION FROM NOT YET DONE REFERRALS FROM THE FACILITY */\n" +
            "SELECT COUNT(*) AS c\n" +
            "FROM task\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
            "         inner join ec_not_yet_done_referral on ec_not_yet_done_referral.referral_task = task._id\n" +
            "         inner join event on ec_not_yet_done_referral.id = event.formSubmissionId\n" +
            "\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND task.status = 'CANCELLED'\n" +
            "  AND task.code = 'Referral'\n";

    String NOT_YET_DONE_REFERRAL_MAIN_SELECT =
            "/* NOTIFICATION FROM NOT YET DONE REFERRALS FROM THE FACILITY */\n" +
            "SELECT ec_family_member.first_name    AS first_name,\n" +
            "       ec_family_member.middle_name   AS middle_name,\n" +
            "       ec_family_member.last_name     AS last_name,\n" +
            "       ec_family_member.dob           AS dob,\n" +
            "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id      AS base_entity_id,\n" +
            "       ec_family_member.relational_id AS relationalid,\n" +
            "       task._id                       AS referral_task_id,\n" +
            "       event.dateCreated              AS notification_date,\n" +
            "       'Referral not completed yet'             AS notification_type\n" +
            "\n" +
            "FROM task\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = task.for\n" +
            "         inner join ec_not_yet_done_referral on ec_not_yet_done_referral.referral_task = task._id\n" +
            "         inner join event on ec_not_yet_done_referral.id = event.formSubmissionId\n" +
            "\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND task.status = 'CANCELLED'\n" +
            "  AND task.code = 'Referral'\n" +
            "  AND ec_family_member.base_entity_id IN (%s)\n" +
            "ORDER BY event.dateCreated DESC";

    String ALL_CLIENTS_SELECT_QUERY = "/* ANC REGISTER */\n" +
            "SELECT ec_family_member.first_name          AS first_name,\n" +
            "       ec_family_member.middle_name         AS middle_name,\n" +
            "       ec_family_member.last_name           AS last_name,\n" +
            "       ec_family_member.gender              AS gender,\n" +
            "       ec_family_member.dob                 AS dob,\n" +
            "       ec_family_member.base_entity_id      AS base_entity_id,\n" +
            "       ec_family_member.id                  as _id,\n" +
            "       'ANC'                                AS register_type,\n" +
            "       ec_family_member.relational_id       as relationalid,\n" +
            "       ec_family.village_town               as home_address,\n" +
            "       ec_anc_register.last_interacted_with AS last_interacted_with,\n" +
            "       NULL                                 AS mother_first_name,\n" +
            "       NULL                                 AS mother_last_name,\n" +
            "       NULL                                 AS mother_middle_name\n" +
            "FROM ec_anc_register\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = ec_anc_register.base_entity_id\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  and ec_anc_register.is_closed is 0\n" +
            "  and ec_anc_register.base_entity_id IN (%s)\n" +
            "\n" +
            "UNION ALL\n" +
            "\n" +
            "/* PNC REGISTER */\n" +
            "\n" +
            "SELECT ec_family_member.first_name               AS first_name,\n" +
            "       ec_family_member.middle_name              AS middle_name,\n" +
            "       ec_family_member.last_name                AS last_name,\n" +
            "       ec_family_member.gender                   AS gender,\n" +
            "       ec_family_member.dob                      AS dob,\n" +
            "       ec_family_member.base_entity_id           AS base_entity_id,\n" +
            "       ec_family_member.id                       as _id,\n" +
            "       'PNC'                                     AS register_type,\n" +
            "       ec_family_member.relational_id            as relationalid,\n" +
            "       ec_family.village_town                    as home_address,\n" +
            "       ec_pregnancy_outcome.last_interacted_with AS last_interacted_with,\n" +
            "       NULL                                      AS mother_first_name,\n" +
            "       NULL                                      AS mother_last_name,\n" +
            "       NULL                                      AS mother_middle_name\n" +
            "FROM ec_pregnancy_outcome\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = ec_pregnancy_outcome.base_entity_id\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  and ec_pregnancy_outcome.is_closed is 0\n" +
            "  AND ec_pregnancy_outcome.base_entity_id NOT IN\n" +
            "      (SELECT base_entity_id FROM ec_anc_register WHERE ec_anc_register.is_closed IS 0)\n" +
            "  AND ec_pregnancy_outcome.base_entity_id IN (%s)\n" +
            "\n" +
            "UNION ALL\n" +
            "/* CHILD REGISTER */\n" +
            "\n" +
            "SELECT ec_family_member.first_name     AS first_name,\n" +
            "       ec_family_member.middle_name    AS middle_name,\n" +
            "       ec_family_member.last_name      AS last_name,\n" +
            "       ec_family_member.gender         AS gender,\n" +
            "       ec_family_member.dob            AS dob,\n" +
            "       ec_family_member.base_entity_id AS base_entity_id,\n" +
            "       ec_family_member.id             as _id,\n" +
            "       'Child'                         AS register_type,\n" +
            "       ec_family_member.relational_id  as relationalid,\n" +
            "       ec_family.village_town          as home_address,\n" +
            "       ec_child.last_interacted_with   AS last_interacted_with,\n" +
            "       ec_child.mother_first_name      AS mother_first_name,\n" +
            "       ec_child.mother_middle_name     AS mother_middle_name,\n" +
            "       ec_child.mother_last_name       AS mother_last_name\n" +
            "FROM (SELECT ec_child.*,\n" +
            "             mother.first_name  AS mother_first_name,\n" +
            "             mother.last_name   AS mother_last_name,\n" +
            "             mother.middle_name AS mother_middle_name\n" +
            "      FROM ec_child\n" +
            "               inner join ec_family on ec_family.base_entity_id = ec_child.relational_id\n" +
            "               INNER JOIN ec_family_member AS mother ON ec_family.primary_caregiver = mother.base_entity_id\n" +
            "     ) ec_child\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = ec_child.base_entity_id\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND cast(strftime('%Y-%m-%d %H:%M:%S', 'now') - strftime('%Y-%m-%d %H:%M:%S', ec_child.dob) as int) > 0\n" +
            "  AND ec_child.base_entity_id IN (%s)\n" +
            "\n" +
            "UNION ALL\n" +
            "/* TB REGISTER */\n" +
            "\n" +
            "SELECT ec_family_member.first_name               AS first_name,\n" +
            "       ec_family_member.middle_name              AS middle_name,\n" +
            "       ec_family_member.last_name                AS last_name,\n" +
            "       ec_family_member.gender                   AS gender,\n" +
            "       ec_family_member.dob                      AS dob,\n" +
            "       ec_family_member.base_entity_id           AS base_entity_id,\n" +
            "       ec_family_member.id                       as _id,\n" +
            "       'TB'                                      AS register_type,\n" +
            "       ec_family_member.relational_id            as relationalid,\n" +
            "       ec_family.village_town                    as home_address,\n" +
            "       ec_tb_register.last_interacted_with       AS last_interacted_with,\n" +
            "       NULL                                      AS mother_first_name,\n" +
            "       NULL                                      AS mother_last_name,\n" +
            "       NULL                                      AS mother_middle_name\n" +
            "FROM ec_tb_register\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = ec_tb_register.base_entity_id\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  and ec_tb_register.is_closed is 0\n" +
            "  and ec_tb_register.tb_case_closure_date is null\n" +
            "  AND ec_tb_register.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            ")\n" +
            "\n" +
            "UNION ALL\n" +
            "/* HIV REGISTER */\n" +
            "\n" +
            "SELECT ec_family_member.first_name               AS first_name,\n" +
            "       ec_family_member.middle_name              AS middle_name,\n" +
            "       ec_family_member.last_name                AS last_name,\n" +
            "       ec_family_member.gender                   AS gender,\n" +
            "       ec_family_member.dob                      AS dob,\n" +
            "       ec_family_member.base_entity_id           AS base_entity_id,\n" +
            "       ec_family_member.id                       as _id,\n" +
            "       'HIV'                                     AS register_type,\n" +
            "       ec_family_member.relational_id            as relationalid,\n" +
            "       ec_family.village_town                    as home_address,\n" +
            "       ec_hiv_register.last_interacted_with      AS last_interacted_with,\n" +
            "       NULL                                      AS mother_first_name,\n" +
            "       NULL                                      AS mother_last_name,\n" +
            "       NULL                                      AS mother_middle_name\n" +
            "FROM ec_hiv_register\n" +
            "         inner join ec_family_member on ec_family_member.base_entity_id = ec_hiv_register.base_entity_id\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  and ec_hiv_register.is_closed is 0\n" +
            "  AND ec_hiv_register.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            "    UNION ALL\n" +
            "    SELECT ec_tb_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_tb_register\n" +
            "    WHERE ec_tb_register.tb_case_closure_date is null\n" +
            ")\n" +
            "\n" +
            "UNION ALL\n" +
            "/*OTHER FAMILY MEMBERS*/\n" +
            "SELECT ec_family_member.first_name,\n" +
            "       ec_family_member.middle_name,\n" +
            "       ec_family_member.last_name,\n" +
            "       ec_family_member.gender,\n" +
            "       ec_family_member.dob,\n" +
            "       ec_family_member.base_entity_id,\n" +
            "       ec_family_member.id                   as _id,\n" +
            "       NULL                                  AS register_type,\n" +
            "       ec_family_member.relational_id        as relationalid,\n" +
            "       ec_family.village_town                as home_address,\n" +
            "       NULL                                  AS mother_first_name,\n" +
            "       NULL                                  AS mother_last_name,\n" +
            "       NULL                                  AS mother_middle_name,\n" +
            "       ec_family_member.last_interacted_with AS last_interacted_with\n" +
            "FROM ec_family_member\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  AND (ec_family.entity_type = 'ec_family' OR ec_family.entity_type is null)\n" +
            "  AND ec_family_member.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            "    UNION ALL\n" +
            "    SELECT ec_malaria_confirmation.base_entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_confirmation\n" +
            "    UNION ALL\n" +
            "    SELECT ec_family_planning.base_entity_id AS base_entity_id\n" +
            "    FROM ec_family_planning\n" +
            "    UNION ALL\n" +
            "    SELECT ec_tb_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_tb_register\n" +
            "    WHERE ec_tb_register.tb_case_closure_date is null\n" +
            "    UNION ALL\n" +
            "    SELECT ec_hiv_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_hiv_register\n" +
            ")\n" +
            "UNION ALL\n" +
            "/*INDEPENDENT MEMBERS*/\n" +
            "SELECT ec_family_member.first_name,\n" +
            "       ec_family_member.middle_name,\n" +
            "       ec_family_member.last_name,\n" +
            "       ec_family_member.gender,\n" +
            "       ec_family_member.dob,\n" +
            "       ec_family_member.base_entity_id,\n" +
            "       ec_family_member.id                   as _id,\n" +
            "       'Independent'                         AS register_type,\n" +
            "       ec_family_member.relational_id        as relationalid,\n" +
            "       ec_family.village_town                as home_address,\n" +
            "       NULL                                  AS mother_first_name,\n" +
            "       NULL                                  AS mother_last_name,\n" +
            "       NULL                                  AS mother_middle_name,\n" +
            "       ec_family_member.last_interacted_with AS last_interacted_with\n" +
            "FROM ec_family_member\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  AND ec_family.entity_type = 'ec_independent_client'\n" +
            "  AND ec_family_member.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            "    UNION ALL\n" +
            "    SELECT ec_malaria_confirmation.base_entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_confirmation\n" +
            "    UNION ALL\n" +
            "    SELECT ec_family_planning.base_entity_id AS base_entity_id\n" +
            "    FROM ec_family_planning\n" +
            "    UNION ALL\n" +
            "    SELECT ec_tb_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_tb_register\n" +
            "    WHERE ec_tb_register.tb_case_closure_date is null\n" +
            "    UNION ALL\n" +
            "    SELECT ec_hiv_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_hiv_register\n" +
            ")" +
            "UNION ALL" +
            "/*ONLY MALARIA PATIENTS*/\n" +
            "SELECT ec_family_member.first_name,\n" +
            "       ec_family_member.middle_name,\n" +
            "       ec_family_member.last_name,\n" +
            "       ec_family_member.gender,\n" +
            "       ec_family_member.dob,\n" +
            "       ec_family_member.base_entity_id,\n" +
            "       ec_family_member.id                          as _id,\n" +
            "       'Malaria'                                    AS register_type,\n" +
            "       ec_family_member.relational_id               as relationalid,\n" +
            "       ec_family.village_town                       as home_address,\n" +
            "       NULL                                         AS mother_first_name,\n" +
            "       NULL                                         AS mother_last_name,\n" +
            "       NULL                                         AS mother_middle_name,\n" +
            "       ec_malaria_confirmation.last_interacted_with AS last_interacted_with\n" +
            "FROM ec_family_member\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "         inner join ec_malaria_confirmation\n" +
            "                    on ec_family_member.base_entity_id = ec_malaria_confirmation.base_entity_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            "    UNION ALL\n" +
            "    SELECT ec_family_planning.base_entity_id AS base_entity_id\n" +
            "    FROM ec_family_planning\n" +
            "    UNION ALL\n" +
            "    SELECT ec_tb_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_tb_register\n" +
            "    WHERE ec_tb_register.tb_case_closure_date is null\n" +
            "    UNION ALL\n" +
            "    SELECT ec_hiv_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_hiv_register\n" +
            ")\n" +
            "UNION ALL\n" +
            "\n" +
            "/*ONLY FAMILY PLANNING PATIENTS*/\n" +
            "SELECT ec_family_member.first_name,\n" +
            "       ec_family_member.middle_name,\n" +
            "       ec_family_member.last_name,\n" +
            "       ec_family_member.gender,\n" +
            "       ec_family_member.dob,\n" +
            "       ec_family_member.base_entity_id,\n" +
            "       ec_family_member.id                          as _id,\n" +
            "       'Family Planning'                             AS register_type,\n" +
            "       ec_family_member.relational_id               as relationalid,\n" +
            "       ec_family.village_town                       as home_address,\n" +
            "       NULL                                         AS mother_first_name,\n" +
            "       NULL                                         AS mother_last_name,\n" +
            "       NULL                                         AS mother_middle_name,\n" +
            "       ec_family_planning.last_interacted_with AS last_interacted_with\n" +
            "FROM ec_family_member\n" +
            "         inner join ec_family on ec_family.base_entity_id = ec_family_member.relational_id\n" +
            "         inner join ec_family_planning\n" +
            "                    on ec_family_member.base_entity_id = ec_family_planning.base_entity_id\n" +
            "where ec_family_member.date_removed is null\n" +
            "  AND ec_family_planning.is_closed is 0\n" +
            "  AND ec_family_member.base_entity_id IN (%s)\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_anc_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_anc_register\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pregnancy_outcome.base_entity_id AS base_entity_id\n" +
            "    FROM ec_pregnancy_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_child.base_entity_id AS base_entity_id\n" +
            "    FROM ec_child\n" +
            "    UNION ALL\n" +
            "    SELECT ec_malaria_confirmation.base_entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_confirmation\n" +
            "    UNION ALL\n" +
            "    SELECT ec_tb_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_tb_register\n" +
            "    WHERE ec_tb_register.tb_case_closure_date is null\n" +
            "    UNION ALL\n" +
            "    SELECT ec_hiv_register.base_entity_id AS base_entity_id\n" +
            "    FROM ec_hiv_register\n" +
            ")\n" +
            "ORDER BY last_interacted_with DESC;";


    String SICK_CHILD_FOLLOW_UP_COUNT_QUERY = "SELECT COUNT(*) AS c\n" +
            "FROM ec_sick_child_followup\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_sick_child_followup.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_sick_child_followup.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_sick_child_followup.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            ")\n";

    String ANC_DANGER_SIGNS_OUTCOME_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_anc_danger_signs_outcome\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_anc_danger_signs_outcome.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_anc_danger_signs_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_anc_danger_signs_outcome.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            ")\n";

    String PNC_DANGER_SIGNS_OUTCOME_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_pnc_danger_signs_outcome\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_pnc_danger_signs_outcome.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_pnc_danger_signs_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_pnc_danger_signs_outcome.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            "    UNION ALL\n" +
            "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_anc_danger_signs_outcome\n" +
            ")\n";

    String FAMILY_PLANNING_UPDATE_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_family_planning_update\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_family_planning_update.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_family_planning_update.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_family_planning_update.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            "    UNION ALL\n" +
            "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_pnc_danger_signs_outcome\n" +
            ")\n";

    String MALARIA_HF_FOLLOW_UP_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_malaria_followup_hf\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_malaria_followup_hf.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_malaria_followup_hf.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_malaria_followup_hf.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_pnc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_anc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
            "    FROM ec_sick_child_followup\n" +
            ")\n";

    String HIV_OUTCOME_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_hiv_outcome\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_hiv_outcome.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_hiv_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_hiv_outcome.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_pnc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_anc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
            "    FROM ec_sick_child_followup\n" +
            "    UNION ALL\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            ")\n";

    String TB_OUTCOME_COUNT_QUERY = "SELECT COUNT(*)\n" +
            "FROM ec_tb_outcome\n" +
            "inner join ec_family_member on ec_family_member.base_entity_id = ec_tb_outcome.entity_id\n" +
            "WHERE ec_family_member.is_closed = '0'\n" +
            " AND (ec_tb_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_tb_outcome.date_marked_as_done) <= 3))\n" +
            "  AND ec_family_member.date_removed is null\n" +
            "  AND ec_family_member.base_entity_id NOT IN (\n" +
            "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_pnc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_anc_danger_signs_outcome\n" +
            "    UNION ALL\n" +
            "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
            "    FROM ec_sick_child_followup\n" +
            "    UNION ALL\n" +
            "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
            "    FROM ec_malaria_followup_hf\n" +
            "    UNION ALL\n" +
            "    SELECT ec_hiv_outcome.entity_id AS base_entity_id\n" +
            "    FROM ec_hiv_outcome\n" +
            ")\n";

    String ANC_DANGER_SIGNS_OUTCOME_MAIN_SELECT =
            "/*ANC DANGER SIGNS OUTCOME*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_anc_danger_signs_outcome.id AS n_id,\n" +
                    "       ec_anc_danger_signs_outcome.visit_date AS notification_date,\n" +
                    "       'ANC Danger Signs'          AS notification_type\n" +
                    "FROM ec_anc_danger_signs_outcome\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_anc_danger_signs_outcome.entity_id\n" +
                    "WHERE  ec_anc_danger_signs_outcome.entity_id IN (%s)\n" +
                    " AND (ec_anc_danger_signs_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_anc_danger_signs_outcome.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_family_member.is_closed = '0'\n" +
                    "  AND ec_anc_danger_signs_outcome.entity_id NOT IN (\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    ")\n";

    String SICK_CHILD_FOLLOW_UP_MAIN_SELECT =
            "/*SICK CHILD FOLLOW-UPS*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_sick_child_followup.id AS n_id,\n" +
                    "       ec_sick_child_followup.visit_date AS notification_date,\n" +
                    "       'Sick Child'          AS notification_type\n" +
                    "FROM ec_sick_child_followup\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_sick_child_followup.entity_id\n" +
                    "WHERE ec_sick_child_followup.entity_id IN (%s)\n" +
                    " AND (ec_sick_child_followup.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_sick_child_followup.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_family_member.is_closed = '0'\n" +
                    "  AND ec_family_member.base_entity_id NOT IN (\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    ")\n";

    String PNC_DANGER_SIGNS_OUTCOME_MAIN_SELECT =
            "/*PNC DANGER SIGNS OUTCOME*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_pnc_danger_signs_outcome.id AS n_id,\n" +
                    "       ec_pnc_danger_signs_outcome.visit_date AS notification_date,\n" +
                    "       'PNC Danger Signs'          AS notification_type\n" +
                    "FROM ec_pnc_danger_signs_outcome\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_pnc_danger_signs_outcome.entity_id\n" +
                    "WHERE ec_pnc_danger_signs_outcome.entity_id IN (%s)\n" +
                    " AND (ec_pnc_danger_signs_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_pnc_danger_signs_outcome.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_family_member.is_closed = '0'\n" +
                    "  AND ec_pnc_danger_signs_outcome.entity_id NOT IN (\n" +
                    "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_anc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    ")\n";

    String FAMILY_PLANNING_UPDATE_MAIN_SELECT =
            "/*FAMILY PLANNING UPDATE*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_family_planning_update.id AS n_id,\n" +
                    "       ec_family_planning_update.fp_reg_date AS notification_date,\n" +
                    "       'Family Planning'          AS notification_type\n" +
                    "FROM ec_family_planning_update\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_family_planning_update.entity_id\n" +
                    "WHERE ec_family_planning_update.entity_id IN (%s)\n" +
                    " AND (ec_family_planning_update.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_family_planning_update.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_family_member.is_closed = '0'\n" +
                    "  AND ec_family_planning_update.entity_id NOT IN (\n" +
                    "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_pnc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    ")\n";

    String MALARIA_FOLLOW_UP_MAIN_SELECT =
            "/*MALARIA HF FOLLOW-UP*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_malaria_followup_hf.id AS n_id,\n" +
                    "       ec_malaria_followup_hf.visit_date AS notification_date,\n" +
                    "       'Malaria Follow-up'          AS notification_type\n" +
                    "FROM ec_malaria_followup_hf\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_malaria_followup_hf.entity_id\n" +
                    "WHERE  ec_malaria_followup_hf.entity_id IN (%s)\n" +
                    " AND (ec_malaria_followup_hf.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_malaria_followup_hf.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_family_member.is_closed = '0'\n" +
                    "  AND ec_malaria_followup_hf.entity_id NOT IN (\n" +
                    "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_anc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_pnc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
                    "    FROM ec_sick_child_followup\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_family_planning_update.entity_id AS base_entity_id\n" +
                    "    FROM ec_family_planning_update\n" +
                    ")\n";

    String HIV_OUTCOME_MAIN_SELECT =
            "/*HIV OUTCOME*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_hiv_outcome.id AS n_id,\n" +
                    "       ec_hiv_outcome.visit_date AS notification_date,\n" +
                    "       'HIV Problem Outcome'          AS notification_type\n" +
                    "FROM ec_hiv_outcome\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_hiv_outcome.entity_id\n" +
                    "WHERE ec_family_member.is_closed = '0'\n" +
                    " AND (ec_hiv_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_hiv_outcome.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_hiv_outcome.entity_id IN (%s)\n" +
                    "  AND ec_hiv_outcome.entity_id NOT IN (\n" +
                    "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_anc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_pnc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
                    "    FROM ec_sick_child_followup\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_family_planning_update.entity_id AS base_entity_id\n" +
                    "    FROM ec_family_planning_update\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    ")\n";

    String TB_OUTCOME_MAIN_SELECT =
            "/*TB OUTCOME*/\n" +
                    "SELECT ec_family_member.first_name    AS first_name,\n" +
                    "       ec_family_member.middle_name   AS middle_name,\n" +
                    "       ec_family_member.last_name     AS last_name,\n" +
                    "       ec_family_member.dob           AS dob,\n" +
                    "       ec_family_member.id            AS _id,\n" +
                    "       ec_family_member.base_entity_id,\n" +
                    "       ec_family_member.relational_id AS relationalid,\n" +
                    "       ec_tb_outcome.id AS n_id,\n" +
                    "       ec_tb_outcome.visit_date AS notification_date,\n" +
                    "       'TB Problem Outcome'          AS notification_type\n" +
                    "FROM ec_tb_outcome\n" +
                    "         inner join ec_family_member on ec_family_member.base_entity_id = ec_tb_outcome.entity_id\n" +
                    "WHERE ec_family_member.is_closed = '0'\n" +
                    " AND (ec_tb_outcome.date_marked_as_done IS NULL OR (julianday('now', 'localtime') - julianday(ec_tb_outcome.date_marked_as_done) <= 3))\n" +
                    "  AND ec_family_member.date_removed is null\n" +
                    "  AND ec_tb_outcome.entity_id IN (%s)\n" +
                    "  AND ec_tb_outcome.entity_id NOT IN (\n" +
                    "    SELECT ec_anc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_anc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_pnc_danger_signs_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_pnc_danger_signs_outcome\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_sick_child_followup.entity_id AS base_entity_id\n" +
                    "    FROM ec_sick_child_followup\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_family_planning_update.entity_id AS base_entity_id\n" +
                    "    FROM ec_family_planning_update\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_malaria_followup_hf.entity_id AS base_entity_id\n" +
                    "    FROM ec_malaria_followup_hf\n" +
                    "    UNION ALL\n" +
                    "    SELECT ec_hiv_outcome.entity_id AS base_entity_id\n" +
                    "    FROM ec_hiv_outcome\n" +
                    ")\n";
}
