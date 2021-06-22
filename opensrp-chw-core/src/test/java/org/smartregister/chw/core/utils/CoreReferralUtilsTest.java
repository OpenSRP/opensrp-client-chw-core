package org.smartregister.chw.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cozej4 on 2020-03-23.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class CoreReferralUtilsTest {

    @Test
    public void mainSelectTest() {
        Assert.assertEquals(CoreReferralUtils.mainSelect("test_tbl", "family_tbl", ""), "Select test_tbl.id as _id , test_tbl.relational_id as relationalid , test_tbl.last_interacted_with , test_tbl.base_entity_id , test_tbl.first_name , test_tbl.middle_name , test_tbl.last_name , family_tbl.village_town as family_home_address , family_tbl.primary_caregiver , family_tbl.family_head , test_tbl.unique_id , test_tbl.gender , test_tbl.dob , test_tbl.dob_unknown FROM test_tbl LEFT JOIN family_tbl ON  test_tbl.relational_id = family_tbl.id COLLATE NOCASE  WHERE test_tbl.base_entity_id = '' ");
    }

    @Test
    public void mainColumnsTest() {
        String tableName = "test_tbl";
        String familyTable = "family_tbl";
        String[] expectedColumns = {tableName + ".relational_id as relationalid", tableName + ".last_interacted_with", tableName + ".base_entity_id", tableName + ".first_name", tableName + ".middle_name", tableName + ".last_name", familyTable + ".village_town as family_home_address", familyTable + ".primary_caregiver", familyTable + ".family_head", tableName + ".unique_id", tableName + ".gender", tableName + ".dob", tableName + ".dob_unknown"};
        Assert.assertArrayEquals(expectedColumns, CoreReferralUtils.mainColumns(tableName, familyTable));
    }

    @Test
    public void pncFamilyMemberProfileDetailsSelect() {
        String tableName = "tbl_ec_pnc";
        String baseEntityId = "baseEntityId";
        String expectedQuery = "Select tbl_ec_pnc.id as _id , tbl_ec_pnc.relationalid , tbl_ec_pnc.village_town , tbl_ec_pnc.primary_caregiver , tbl_ec_pnc.family_head FROM tbl_ec_pnc LEFT JOIN ec_family_member ON  tbl_ec_pnc.base_entity_id = ec_family_member.relational_id WHERE ec_family_member.base_entity_id = 'baseEntityId' ";
        Assert.assertEquals(expectedQuery,CoreReferralUtils.pncFamilyMemberProfileDetailsSelect(tableName, baseEntityId));
    }

    @Test
    public void mainAncDetailsSelect() {
        String tableName = "tbl_ec_anc";
        String baseEntityId = "baseEntityId";
        String expectedQuery = "Select tbl_ec_anc.id as _id , tbl_ec_anc.relational_id as relationalid , tbl_ec_anc.last_menstrual_period , ec_anc_log.date_created , ec_family.village_town , tbl_ec_anc.confirmed_visits , tbl_ec_anc.last_home_visit FROM tbl_ec_anc LEFT JOIN ec_anc_log ON  tbl_ec_anc.base_entity_id = ec_anc_log.id COLLATE NOCASE  LEFT JOIN ec_family ON  tbl_ec_anc.relational_id = ec_family.base_entity_id WHERE tbl_ec_anc.base_entity_id = 'baseEntityId' ";
        Assert.assertEquals(expectedQuery,CoreReferralUtils.mainAncDetailsSelect(tableName, baseEntityId));
    }

    @Test
    public void mainCareGiverSelect() {
        String tableName = "tbl_ec_family_member";
        String mainCondition = "12345";
        String expectedQuery = "Select tbl_ec_family_member.id as _id , tbl_ec_family_member.relational_id as relationalid , tbl_ec_family_member.first_name as family_first_name , tbl_ec_family_member.middle_name as family_last_name , tbl_ec_family_member.last_name as family_middle_name , tbl_ec_family_member.phone_number as family_member_phone_number , tbl_ec_family_member.other_phone_number as family_member_phone_number_other FROM tbl_ec_family_member WHERE tbl_ec_family_member.base_entity_id = '12345' ";
        Assert.assertEquals(expectedQuery,CoreReferralUtils.mainCareGiverSelect(tableName, mainCondition));
    }

    @Test
    public void getReferralProblems() {
        String jsonForm = "{\"count\":\"1\",\"encounter_type\":\"PNC Referral\",\"entity_id\":\"\",\"relational_id\":\"\",\"step1\":{\"title\":\"PNC referral form\",\"fields\":[{\"key\":\"problem_hf_pnc\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"check_box\",\"label\":\"Client condition / problem\",\"label_text_style\":\"bold\",\"options\":[{\"key\":\"Vaginal_bleeding\",\"text\":\"Vaginal bleeding\",\"value\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"147232AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Discoloured_or_watery_liquid_vaginal_discharge\",\"text\":\"Discoloured or watery, liquid vaginal discharge with a bad smell\",\"value\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"123396AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Severe_abdominal_pain\",\"text\":\"Severe abdominal pain\",\"value\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"165271AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Severe_anaemia\",\"text\":\"Severe anaemia\",\"value\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"162044AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Convulsions\",\"text\":\"Convulsions\",\"value\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"113054AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"A_severe_headache_dizziness\",\"text\":\"A severe headache / dizziness\",\"value\":\"false\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"139081AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Swelling_of_the_face_and_or_hands\",\"text\":\"Swelling of the face and/or hands\",\"value\":\"false\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"460AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Vomiting\",\"text\":\"Vomiting\",\"value\":\"false\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"122983AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Cord_prolapse\",\"text\":\"Cord prolapse\",\"value\":\"false\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"113617AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"},{\"key\":\"Other_symptom\",\"text\":\"Other symptom\",\"value\":\"false\",\"ignore\":\"true\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"openmrs_entity_parent\":\"\"}],\"v_required\":{\"value\":true,\"err\":\"Please specify client condition/problem\"}},{\"key\":\"problem_hf_pnc_other\",\"type\":\"edit_text\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"openmrs_entity_parent\":\"\",\"edit_type\":\"name\",\"hint\":\"Other symptom\",\"relevance\":{\"rules-engine\":{\"ex-rules\":{\"rules-file\":\"pnc_referral_relevance.yml\"}}}}]}}";
        String problemsString = "Vaginal bleeding, Discoloured or watery, liquid vaginal discharge with a bad smell, Severe abdominal pain, Severe anaemia, Convulsions";
        Assert.assertEquals(problemsString,CoreReferralUtils.getReferralProblems(jsonForm));
    }
}