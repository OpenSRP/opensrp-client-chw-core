package org.smartregister.chw.core.utils;

import android.content.res.AssetManager;

import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
import org.smartregister.chw.malaria.util.Constants;

import java.util.Locale;

public class CoreConstants {

    public static final String ENTITY_ID = "entity_id";
    public static final String REFERRAL_PLAN_ID = "5270285b-5a3b-4647-b772-c0b3c52e2b71";
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CURRENT_LOCATION_ID = "CURRENT_LOCATION_ID";
    public static final String FORMSUBMISSION_FIELD = "formsubmissionField";
    public static final String TEXT = "text";
    public static final String DATE = "date";
    public static String EC_CLIENT_FIELDS = "ec_client_fields.json";
    public static String IGNORE = "ignore";

    public enum VisitType {DUE, OVERDUE, LESS_TWENTY_FOUR, VISIT_THIS_MONTH, NOT_VISIT_THIS_MONTH, DONE, EXPIRY}

    public enum ServiceType {DUE, OVERDUE, UPCOMING}

    public enum FamilyServiceType {DUE, OVERDUE, NOTHING}


    interface Properties {
        String TASK_IDENTIFIER = "taskIdentifier";
        String TASK_BUSINESS_STATUS = "taskBusinessStatus";
        String TASK_STATUS = "taskStatus";
        String TASK_CODE = "taskCode";
        String LOCATION_UUID = "locationUUID";
        String LOCATION_VERSION = "locationVersion";
        String LOCATION_TYPE = "locationType";
        String LOCATION_PARENT = "locationParent";
        String LOCATION_ID = "location_id";
        String FEATURE_SELECT_TASK_BUSINESS_STATUS = "featureSelectTaskBusinessStatus";
        String BASE_ENTITY_ID = "baseEntityId";
        String STRUCTURE_NAME = "structure_name";
        String APP_VERSION_NAME = "appVersionName";
        String FORM_VERSION = "form_version";
        String TASK_CODE_LIST = "task_code_list";
        String FAMILY_MEMBER_NAMES = "family_member_names";
        String PLAN_IDENTIFIER = "planIdentifier";
        String STRUCTURE_ID = "structure_id";
        String TASK_AGGREGATE_STATUS = "taskAggregateStatus";
    }

    public interface DB_CONSTANTS {
        String ID = "_id";
        String FOR = "for";
        String FOCUS = "focus";
        String REQUESTER = "requester";
        String OWNER = "owner";
        String START = "start";
        String ENTRY_POINT = "entry_point";
        String STATUS = "status";
        String LAST_MODIFIED = "last_modified";
        String NOTIFICATION_ID = "n_id";
        String NOTIFICATION_TYPE = "notification_type";
        String NOTIFICATION_DATE = "notification_date";
        String REFERRAL_TASK_ID = "referral_task_id";
        String BASE_ENTITY_ID = "base_entity_id";
        String THINK_MD_ID = "thinkmd_id";
        String PLAN_ID = "plan_id";
        String BUSINESS_STATUS = "business_status";
        String DETAILS = "details";
        String PRIMARY_CAREGIVER_NAME = "primary_caregiver_name";
    }

    public interface SERVICE_GROUPS {
        String CHILD = "child";
        String WOMAN = "woman";
        String PNC = "pnc";
        String ANC = "anc";
    }

    public interface SCHEDULE_TYPES {
        String CHILD_VISIT = "CHILD_VISIT";
        String ANC_VISIT = "ANC_VISIT";
        String PNC_VISIT = "PNC_VISIT";
        String WASH_CHECK = "WASH_CHECK";
        String FAMILY_KIT = "FAMILY_KIT";
        String MALARIA_VISIT = "MALARIA_VISIT";
        String FP_VISIT = "FP_VISIT";
        String TB_VISIT = "TB_VISIT";
        String HIV_VISIT = "HIV_VISIT";
        String ROUTINE_HOUSEHOLD_VISIT = "ROUTINE_HOUSEHOLD_VISIT";
    }

    public interface SCHEDULE_GROUPS {
        String HOME_VISIT = "HOME_VISIT";
        String FAMILY = "FAMILY";
    }

    public static class CONFIGURATION {
        public static final String LOGIN = "login";
        public static final String FAMILY_REGISTER = "family_register";
        public static final String FAMILY_MEMBER_REGISTER = "family_member_register";
        public static final String ANC_REGISTER = "anc_register";
        public static final String MALARIA_REGISTER = "anc_malaria_confirmation";
    }

    public static final class EventType {
        public static final String BIRTH_CERTIFICATION = "Birth Certification";
        public static final String OBS_ILLNESS = "Observations Illness";
        public static final String COUNSELING = "Counseling";
        public static final String FAMILY_REGISTRATION = "Family Registration";
        public static final String FAMILY_MEMBER_REGISTRATION = "Family Member Registration";
        public static final String ECD = "Early childhood development";
        public static final String CHILD_REGISTRATION = "Child Registration";
        public static final String UPDATE_CHILD_REGISTRATION = "Update Child Registration";
        public static final String CHILD_HOME_VISIT = "Child Home Visit";
        public static final String CHILD_VISIT_NOT_DONE = "Visit not done";
        public static final String UNDO_CHILD_VISIT_NOT_DONE = "Undo child visit not done";
        public static final String CHILD_REFERRAL = "Sick Child Referral";
        public static final String ANC_REFERRAL = "ANC Referral";
        public static final String PNC_REFERRAL = "PNC Referral";
        public static final String MALARIA_REFERRAL = "Malaria Referral";
        public static final String FAMILY_PLANNING_REFERRAL = "Family Planning Referral";
        public static final String CHILD_VACCINE_CARD_RECEIVED = "Child vaccine card received";
        public static final String VACCINE_CARD_RECEIVED = "Vaccine Card Received";
        public static final String MINIMUM_DIETARY_DIVERSITY = "Minimum dietary diversity";
        public static final String MUAC = "Mid-upper arm circumference (MUAC)";
        public static final String LLITN = "Sleeping under a LLITN";
        public static final String VITAMIN_A = "Vitamin A";
        public static final String DEWORMING = "De-worming";
        public static final String TT = "TT Vaccination";
        public static final String IPTP_SP = "IPTp-SP Service";
        public static final String MNP = "MNP";
        public static final String DANGER_SIGNS_BABY = "Danger signs - Baby";
        public static final String PNC_HEALTH_FACILITY_VISIT = "PNC health facility visit";
        public static final String EXCLUSIVE_BREASTFEEDING = "Exclusive breast feeding";
        public static final String KANGAROO_CARE = "Kangaroo Care";
        public static final String UMBILICAL_CORD_CARE = "Umbilical cord care";
        public static final String IMMUNIZATION_VISIT = "Immunization Visit";
        public static final String OBSERVATIONS_AND_ILLNESS = "Observations Illness";
        public static final String UPDATE_FAMILY_RELATIONS = "Update Family Relations";
        public static final String UPDATE_FAMILY_MEMBER_RELATIONS = "Update Family Member Relations";

        public static final String UPDATE_FAMILY_REGISTRATION = "Update Family Registration";
        public static final String UPDATE_FAMILY_MEMBER_REGISTRATION = "Update Family Member Registration";

        public static final String REMOVE_MEMBER = "Remove Family Member";
        public static final String REMOVE_CHILD = "Remove Child Under 5";
        public static final String REMOVE_FAMILY = "Remove Family";

        public static final String ANC_REGISTRATION = "ANC Registration";
        public static final String ANC_HOME_VISIT = "ANC Home Visit";
        public static final String PNC_HOME_VISIT = "PNC Home Visit";
        public static final String ANC_HOME_VISIT_NOT_DONE = "ANC Home Visit Not Done";
        public static final String ANC_HOME_VISIT_NOT_DONE_UNDO = "ANC Home Visit Not Done Undo";
        public static final String UPDATE_ANC_REGISTRATION = "Update ANC Registration";
        public static final String CLOSE_REFERRAL = "Close Referral";
        public static final String EXPIRED_REFERRAL = "Expired Referral";
        public static final String NOT_YET_DONE_REFERRAL = "Not Yet Done Referral";
        public static final String PREGNANCY_OUTCOME = "Pregnancy Outcome";
        public static final String PNC_REGISTRATION = "PNC Registration";
        public static final String PNC_HOME_VISIT_NOT_DONE = "PNC Home Visit Not Done";
        public static final String PNC_HOME_VISIT_NOT_DONE_UNDO = "PNC Home Visit Not Done Undo";
        public static final String WASH_CHECK = "WASH check";
        public static final String FAMILY_KIT = "Family Kit";
        public static final String NUTRITION_STATUS_BABY = "Nutrition Status - Baby";
        public static final String REFERRAL_DISMISSAL = "Referral Dismissal";
        public static final String ANC_NOTIFICATION_DISMISSAL = "ANC Notification Dismissal";
        public static final String PNC_NOTIFICATION_DISMISSAL = "PNC Notification Dismissal";
        public static final String MALARIA_NOTIFICATION_DISMISSAL = "Malaria Notification Dismissal";
        public static final String SICK_CHILD_NOTIFICATION_DISMISSAL = "Sick Child Notification Dismissal";
        public static final String FAMILY_PLANNING_NOTIFICATION_DISMISSAL = "Family Planning Notification Dismissal";
        public static final String HIV_NOTIFICATION_DISMISSAL = "HIV Notification Dismissal";
        public static final String TB_NOTIFICATION_DISMISSAL = "TB Notification Dismissal";

        public static final String ROUTINE_HOUSEHOLD_VISIT = "Routine Household Visit";
        public static final String SICK_CHILD = "Sick Child";
        public static final String STOCK_USAGE_REPORT = "Stock Usage Report";
        public static final String CHW_IN_APP_REPORT_EVENT = "CHW Monthly tallies Report";
        public static final String HF_IN_APP_REPORT_EVENT = "HF Monthly tallies Report";
        public static final String SICK_CHILD_FOLLOW_UP = "Sick Child Follow-up";
        public static final String COMMUNITY_RESPONDER_REGISTRATION = "Community Responder Registration";
        public static final String ANC_DANGER_SIGNS_OUTCOME = "ANC Danger Signs Outcome";
        public static final String PNC_DANGER_SIGNS_OUTCOME = "PNC Danger Signs Outcome";
        public static final String MALARIA_FOLLOW_UP_HF = "Malaria Follow-up HF";
        public static final String FAMILY_PLANNING_UPDATE = "Family Planning Update";
        public static final String REMOVE_COMMUNITY_RESPONDER = "Remove Community Responder";
        public static final String CLOSE_HIV_FEEDBACK = "Close Hiv Feedback";
        public static final String CLOSE_TB_FEEDBACK = "Close Tb Feedback";
        public static final String REGISTER_HIV_INDEX = "Register Hiv Index";
    }

    public static final class EncounterType {
        public static final String CLOSE_REFERRAL = "Close Referral";
    }

    /**
     * Only access form constants via the getter
     */
    public static class JSON_FORM {
        public static final String BIRTH_CERTIFICATION = "birth_certification";
        public static final String OBS_ILLNESS = "observation_illness";
        public static final String FAMILY_REGISTER = "family_register";
        public static final String FAMILY_MEMBER_REGISTER = "family_member_register";
        public static final String CHILD_REGISTER = "child_enrollment";
        public static final String CHILD_SICK_FORM = "child_sick_form";
        public static final String FAMILY_DETAILS_REGISTER = "family_details_register";
        public static final String FAMILY_DETAILS_REMOVE_MEMBER = "family_details_remove_member";
        public static final String FAMILY_DETAILS_REMOVE_CHILD = "family_details_remove_child";
        public static final String FAMILY_DETAILS_REMOVE_FAMILY = "family_details_remove_family";
        public static final String HOME_VISIT_COUNSELLING = "routine_home_visit";
        public static final String COMMUNITY_RESPONDER_REGISTRATION_FORM = "community_responder_registration";
        public static final String IN_APP_REPORT_FORM = "in_app_monthly_tallies_report";
        private static final String ANC_REGISTRATION = "anc_member_registration";
        private static final String PREGNANCY_OUTCOME = "anc_pregnancy_outcome";
        private static final String MALARIA_CONFIRMATION = "malaria_confirmation";
        private static final String MALARIA_FOLLOW_UP_VISIT_FORM = "malaria_follow_up_visit";
        private static final String WASH_CHECK = "wash_check";
        private static final String FAMILY_KIT = "family_kit";
        private static final String CHILD_REFERRAL_FORM = "child_referral_form";
        private static final String ANC_REFERRAL_FORM = "anc_referral_form";
        private static final String STOCK_USAGE_REPORT_FORM = "stock_usage_report";
        private static final String PNC_REFERRAL_FORM = "pnc_referral_form";
        private static final String CHILD_UNIFIED_REFERRAL_FORM = "referrals/child_referral_form";
        private static final String ANC_UNIFIED_REFERRAL_FORM = "referrals/anc_referral_form";
        private static final String PNC_UNIFIED_REFERRAL_FORM = "referrals/pnc_referral_form";
        private static final String HIV_REFERRAL_FORM = "referrals/hiv_referral_form";
        private static final String TB_REFERRAL_FORM = "referrals/tb_referral_form";
        private static final String GBV_REFERRAL_FORM = "referrals/gbv_referral_form";
        private static final String FEMALE_FAMILY_PLANNING_UNIFIED_REFERRAL_FORM = "referrals/female_fp_referral_form";
        private static final String MALE_FAMILY_PLANNING_UNIFIED_REFERRAL_FORM = "referrals/male_fp_referral_form";
        private static final String CHILD_GBV_REFERRAL_FORM = "referrals/child_gbv_referral_form";
        private static final String REFERRAL_FOLLOWUP_FORM = "referrals/referral_followup_neat_form";
        private static final String ROUTINE_HOUSEHOLD_VISIT = "routine_household_visit";
        private static final String FEMALE_FAMILY_PLANNING_REFERRAL_FORM = "female_fp_referral_form";
        private static final String MALE_FAMILY_PLANNING_REFERRAL_FORM = "male_fp_referral_form";
        private static final String MALARIA_REFERRAL_FORM = "referrals/malaria_referral_form";
        private static final String ANC_DANGER_SIGNS_OUTCOME_FORM = "anc_danger_signs_outcome";
        private static final String FEMALE_FAMILY_PLANNING_REGISTRATION_FORM = "female_family_planning_registration";
        private static final String MALE_FAMILY_PLANNING_REGISTRATION_FORM = "male_family_planning_registration";
        private static final String FEMALE_FAMILY_PLANNING_CHANGE_METHOD_FORM = "female_family_planning_change_method";
        private static final String MALE_FAMILY_PLANNING_CHANGE_METHOD_FORM = "male_family_planning_change_method";
        private static final String PNC_DANGER_SIGNS_OUTCOME_FORM = "pnc_danger_signs_outcome";
        private static final String MALARIA_FOLLOW_UP_HF_FORM = "malaria_follow_up_hf";
        private static final String HIV_REGISTRATION = "hiv_registration";
        private static final String HIV_FOLLOWUP_VISIT = "hiv_followup_visit";
        private static final String HIV_OUTCOME = "hiv_outcome";
        private static final String HIV_COMMUNITY_FOLLOWUP_REFERRAL = "hiv_community_followup_referral";
        private static final String TB_OUTCOME = "tb_outcome";
        private static final String TB_COMMUNITY_FOLLOWUP_REFERRAL = "tb_community_followup_referral";
        private static final String TB_REGISTRATION = "tb_registration";
        private static final String TB_FOLLOWUP_VISIT = "tb_followup_visit";
        private static final String TB_CASE_CLOSURE = "tb_case_closure";
        private static final String HIV_COMMUNITY_FOLLOW_FEEDBACK = "hiv_community_followup_feedback";
        private static final String TB_COMMUNITY_FOLLOW_FEEDBACK = "tb_community_followup_feedback";
        private static final String ALL_CLIENT_REGISTRATION_FORM = "all_clients_registration_form";
        private static final String ALL_CLIENT_UPDATE_REGISTRATION_INFO_FORM = "all_clients_update_registration_info_form";

        private static final String HIV_INDEX_CLIENTS_REGISTRATION_FORM = "hiv_index_clients_registration_form";




        public static AssetManager assetManager;
        public static Locale locale;

        /**
         * NOTE: This method must be called first before using any of the forms. Preferable onCreate()
         * method of Application
         *
         * @param locale       locale used to determine language
         * @param assetManager asset manager used to access Json Forms
         */
        public static void setLocaleAndAssetManager(Locale locale, AssetManager assetManager) {
            JSON_FORM.assetManager = assetManager;
            JSON_FORM.locale = locale;
        }

        public static String getChildReferralForm() {
            return CHILD_REFERRAL_FORM;
        }

        public static String getAncReferralForm() {
            return ANC_REFERRAL_FORM;
        }

        public static String getStockUsageForm() {
            return STOCK_USAGE_REPORT_FORM;
        }

        public static String getPncReferralForm() {
            return PNC_REFERRAL_FORM;
        }

        public static String getChildUnifiedReferralForm() {
            return CHILD_UNIFIED_REFERRAL_FORM;
        }

        public static String getAncUnifiedReferralForm() {
            return ANC_UNIFIED_REFERRAL_FORM;
        }

        public static String getPncUnifiedReferralForm() {
            return PNC_UNIFIED_REFERRAL_FORM;
        }

        public static String getFamilyPlanningReferralForm(String gender) {
            return gender.equalsIgnoreCase(Gender.MALE.toString()) ? MALE_FAMILY_PLANNING_REFERRAL_FORM : FEMALE_FAMILY_PLANNING_REFERRAL_FORM;
        }

        public static String getFamilyPlanningUnifiedReferralForm(String gender) {
            return gender.equalsIgnoreCase(Gender.MALE.toString()) ? MALE_FAMILY_PLANNING_UNIFIED_REFERRAL_FORM : FEMALE_FAMILY_PLANNING_UNIFIED_REFERRAL_FORM;
        }

        public static String getMalariaReferralForm() {
            return MALARIA_REFERRAL_FORM;
        }

        public static String getBirthCertification() {
            return Utils.getLocalForm(BIRTH_CERTIFICATION, locale, assetManager);
        }

        public static String getObsIllness() {
            return Utils.getLocalForm(OBS_ILLNESS, locale, assetManager);
        }

        public static String getFamilyRegister() {
            return Utils.getLocalForm(FAMILY_REGISTER, locale, assetManager);
        }

        public static String getFamilyMemberRegister() {
            return Utils.getLocalForm(FAMILY_MEMBER_REGISTER, locale, assetManager);
        }

        public static String getChildSickForm() {
            return Utils.getLocalForm(CHILD_SICK_FORM, locale, assetManager);
        }

        public static String getChildRegister() {
            return Utils.getLocalForm(CHILD_REGISTER, locale, assetManager);
        }

        public static String getFamilyDetailsRegister() {
            return Utils.getLocalForm(FAMILY_DETAILS_REGISTER, locale, assetManager);
        }

        public static String getFamilyDetailsRemoveMember() {
            return Utils.getLocalForm(FAMILY_DETAILS_REMOVE_MEMBER, locale, assetManager);
        }

        public static String getFamilyDetailsRemoveChild() {
            return Utils.getLocalForm(FAMILY_DETAILS_REMOVE_CHILD, locale, assetManager);
        }

        public static String getFamilyDetailsRemoveFamily() {
            return Utils.getLocalForm(FAMILY_DETAILS_REMOVE_FAMILY, locale, assetManager);
        }

        public static String getHomeVisitCounselling() {
            return Utils.getLocalForm(HOME_VISIT_COUNSELLING, locale, assetManager);
        }

        public static String getAncRegistration() {
            return Utils.getLocalForm(ANC_REGISTRATION, locale, assetManager);
        }

        public static String getPregnancyOutcome() {
            return Utils.getLocalForm(PREGNANCY_OUTCOME, locale, assetManager);
        }

        public static String getMalariaConfirmation() {
            return Utils.getLocalForm(MALARIA_CONFIRMATION, locale, assetManager);
        }

        public static String getFpRegistrationForm(String gender) {
            String formName = gender.equalsIgnoreCase(Gender.MALE.toString()) ? MALE_FAMILY_PLANNING_REGISTRATION_FORM : FEMALE_FAMILY_PLANNING_REGISTRATION_FORM;
            return Utils.getLocalForm(formName, locale, assetManager);
        }

        public static String getFpChangeMethodForm(String gender) {
            String formName = gender.equalsIgnoreCase(Gender.MALE.toString()) ? MALE_FAMILY_PLANNING_CHANGE_METHOD_FORM : FEMALE_FAMILY_PLANNING_CHANGE_METHOD_FORM;
            return Utils.getLocalForm(formName, locale, assetManager);
        }

        public static boolean isMultiPartForm(JSONObject jsonForm) {
            String encounterType = jsonForm.optString(CoreJsonFormUtils.ENCOUNTER_TYPE);
            return !encounterType.equals(Constants.EVENT_TYPE.MALARIA_FOLLOW_UP_VISIT);
        }

        public static String getMalariaFollowUpVisitForm() {
            return Utils.getLocalForm(MALARIA_FOLLOW_UP_VISIT_FORM, locale, assetManager);
        }

        public static String getWashCheck() {
            return Utils.getLocalForm(WASH_CHECK, locale, assetManager);
        }

        public static String getFamilyKit() {
            return Utils.getLocalForm(FAMILY_KIT, locale, assetManager);
        }

        public static String getRoutineHouseholdVisit() {
            return Utils.getLocalForm(ROUTINE_HOUSEHOLD_VISIT, locale, assetManager);
        }

        public static String getHivReferralForm() {
            return HIV_REFERRAL_FORM;
        }

        public static String getTbReferralForm() {
            return TB_REFERRAL_FORM;
        }

        public static String getGbvReferralForm() {
            return GBV_REFERRAL_FORM;
        }

        public static String getChildGbvReferralForm() {
            return CHILD_GBV_REFERRAL_FORM;
        }

        public static String getReferralFollowupForm() {
            return Utils.getLocalForm(REFERRAL_FOLLOWUP_FORM, locale, assetManager);
        }

        public static String getAncDangerSignsOutcomeForm() {
            return Utils.getLocalForm(ANC_DANGER_SIGNS_OUTCOME_FORM, locale, assetManager);
        }

        public static String getPncDangerSignsOutcomeForm() {
            return PNC_DANGER_SIGNS_OUTCOME_FORM;
        }

        public static String getMalariaFollowUpHfForm() {
            return Utils.getLocalForm(MALARIA_FOLLOW_UP_HF_FORM, locale, assetManager);
        }

        public static String getHivRegistration() {
            return HIV_REGISTRATION;
        }

        public static String getTbRegistration() {
            return TB_REGISTRATION;
        }

        public static String getHivFollowupVisit() {
            return HIV_FOLLOWUP_VISIT;
        }

        public static String getHivOutcome() {
            return HIV_OUTCOME;
        }

        public static String getHivCommunityFollowupReferral() {
            return HIV_COMMUNITY_FOLLOWUP_REFERRAL;
        }

        public static String getTbOutcome() {
            return TB_OUTCOME;
        }

        public static String getTbCommunityFollowupReferral() {
            return TB_COMMUNITY_FOLLOWUP_REFERRAL;
        }

        public static String getTbFollowupVisit() {
            return TB_FOLLOWUP_VISIT;
        }

        public static String getTbCaseClosure() {
            return TB_CASE_CLOSURE;
        }

        public static String getHivCommunityFollowFeedback() {
            return HIV_COMMUNITY_FOLLOW_FEEDBACK;
        }

        public static String getAllClientRegistrationForm() {
            return ALL_CLIENT_REGISTRATION_FORM;
        }

        public static String getAllClientUpdateRegistrationInfoForm() {
            return ALL_CLIENT_UPDATE_REGISTRATION_INFO_FORM;
        }

        public static String getHivIndexClientsRegistrationForm() {
            return HIV_INDEX_CLIENTS_REGISTRATION_FORM;
        }

        public static String getTbCommunityFollowFeedback() {
            return TB_COMMUNITY_FOLLOW_FEEDBACK;
        }

        public static class CHILD_HOME_VISIT {
            private static final String VACCINE_CARD = "child_hv_vaccine_card_received";
            private static final String VITAMIN_A = "child_hv_vitamin_a";
            private static final String DEWORMING = "child_hv_deworming";
            private static final String MUAC = "child_hv_muac";
            private static final String DIETARY = "child_hv_dietary_diversity";
            private static final String MNP = "child_hv_mnp";
            private static final String MALARIA_PREVENTION = "child_hv_malaria_prevention";
            private static final String SLEEPING_UNDER_LLITN = "child_hv_sleeping_under_llitn";
            private static final String NUTRITION_STATUS = "child_hv_nutrition_status";

            public static String getVaccineCard() {
                return Utils.getLocalForm(VACCINE_CARD, locale, assetManager);
            }

            public static String getVitaminA() {
                return Utils.getLocalForm(VITAMIN_A, locale, assetManager);
            }

            public static String getDEWORMING() {
                return Utils.getLocalForm(DEWORMING, locale, assetManager);
            }

            public static String getMUAC() {
                return Utils.getLocalForm(MUAC, locale, assetManager);
            }

            public static String getDIETARY() {
                return Utils.getLocalForm(DIETARY, locale, assetManager);
            }

            public static String getMNP() {
                return Utils.getLocalForm(MNP, locale, assetManager);
            }

            public static String getMalariaPrevention() {
                return Utils.getLocalForm(MALARIA_PREVENTION, locale, assetManager);
            }

            public static String getSleepingUnderLlitn() {
                return Utils.getLocalForm(SLEEPING_UNDER_LLITN, locale, assetManager);
            }

            public static String getNutritionStatus() {
                return Utils.getLocalForm(NUTRITION_STATUS, locale, assetManager);
            }
        }

        public static class ANC_HOME_VISIT {
            private static final String DANGER_SIGNS = "anc_hv_danger_signs";
            private static final String ANC_COUNSELING = "anc_hv_counseling";
            private static final String SLEEPING_UNDER_LLITN = "anc_hv_sleeping_under_llitn";
            private static final String ANC_CARD_RECEIVED = "anc_hv_anc_card_received";
            private static final String TT_IMMUNIZATION = "anc_hv_tt_immunization";
            private static final String IPTP_SP = "anc_hv_anc_iptp_sp";

            private static final String HEALTH_FACILITY_VISIT = "anc_hv_health_facility_visit";
            private static final String FAMILY_PLANNING = "anc_hv_family_planning";
            private static final String NUTRITION_STATUS = "anc_hv_nutrition_status";
            private static final String COUNSELLING = "anc_hv_counselling";
            private static final String MALARIA = "anc_hv_malaria";
            private static final String OBSERVATION_AND_ILLNESS = "anc_hv_observations";
            private static final String REMARKS_AND_COMMENTS = "anc_hv_remarks_and_comments";
            private static final String EARLY_CHILDHOOD_DEVELOPMENT = "early_childhood_development";
            private static final String PREGNANCY_RISK = "anc_hv_pregnancy_risk_form";

            public static String getDangerSigns() {
                return Utils.getLocalForm(DANGER_SIGNS, locale, assetManager);
            }

            public static String getAncCounseling() {
                return Utils.getLocalForm(ANC_COUNSELING, locale, assetManager);
            }

            public static String getSleepingUnderLlitn() {
                return Utils.getLocalForm(SLEEPING_UNDER_LLITN, locale, assetManager);
            }

            public static String getAncCardReceived() {
                return Utils.getLocalForm(ANC_CARD_RECEIVED, locale, assetManager);
            }

            public static String getTtImmunization() {
                return Utils.getLocalForm(TT_IMMUNIZATION, locale, assetManager);
            }

            public static String getIptpSp() {
                return Utils.getLocalForm(IPTP_SP, locale, assetManager);
            }

            public static String getHealthFacilityVisit() {
                return Utils.getLocalForm(HEALTH_FACILITY_VISIT, locale, assetManager);
            }

            public static String getFamilyPlanning() {
                return Utils.getLocalForm(FAMILY_PLANNING, locale, assetManager);
            }

            public static String getNutritionStatus() {
                return Utils.getLocalForm(NUTRITION_STATUS, locale, assetManager);
            }

            public static String getCOUNSELLING() {
                return Utils.getLocalForm(COUNSELLING, locale, assetManager);
            }

            public static String getMALARIA() {
                return Utils.getLocalForm(MALARIA, locale, assetManager);
            }

            public static String getObservationAndIllness() {
                return Utils.getLocalForm(OBSERVATION_AND_ILLNESS, locale, assetManager);
            }

            public static String getRemarksAndComments() {
                return Utils.getLocalForm(REMARKS_AND_COMMENTS, locale, assetManager);
            }

            public static String getEarlyChildhoodDevelopment() {
                return Utils.getLocalForm(EARLY_CHILDHOOD_DEVELOPMENT, locale, assetManager);
            }

            public static String getPregnancyRisk() {
                return Utils.getLocalForm(PREGNANCY_RISK, locale, assetManager);
            }

        }

        public static class PNC_HOME_VISIT {
            private static final String DANGER_SIGNS = "pnc_hv_danger_signs";
            private static final String DANGER_SIGNS_MOTHER = "pnc_danger_signs_mother";
            private static final String DANGER_SIGNS_BABY = "pnc_danger_signs_baby";
            private static final String HEALTH_FACILITY_VISIT = "pnc_health_facility_visit";
            private static final String HEALTH_FACILITY_VISIT_TWO = "pnc_health_facility_visit_two";
            private static final String COUNSELLING = "pnc_counselling";
            private static final String UMBILICAL_CORD = "pnc_umbilical_cord";
            private static final String NUTRITION_STATUS_MOTHER = "pnc_nutrition_status_mother";
            private static final String NUTRITION_STATUS_INFANT = "pnc_nutrition_status_infant";
            private static final String MALARIA_PREVENTION = "pnc_malaria_prevention";
            private static final String FAMILY_PLANNING = "pnc_family_planning";
            private static final String KANGAROO_CARE = "pnc_kangaroo_care";
            private static final String VACCINE_CARD = "pnc_vaccine_card";
            private static final String EXCLUSIVE_BREAST_FEEDING = "pnc_exclusive_breastfeeding";
            private static final String OBSERVATION_AND_ILLNESS_MOTHER = "pnc_hv_observations_mother";
            private static final String OBSERVATION_AND_ILLNESS_INFANT = "pnc_hv_observations_infant";

            public static String getDangerSigns() {
                return Utils.getLocalForm(DANGER_SIGNS, locale, assetManager);
            }

            public static String getDangerSignsMother() {
                return Utils.getLocalForm(DANGER_SIGNS_MOTHER, locale, assetManager);
            }

            public static String getDangerSignsBaby() {
                return Utils.getLocalForm(DANGER_SIGNS_BABY, locale, assetManager);
            }

            public static String getHealthFacilityVisit() {
                return Utils.getLocalForm(HEALTH_FACILITY_VISIT, locale, assetManager);
            }

            public static String getHealthFacilityVisitTwo() {
                return Utils.getLocalForm(HEALTH_FACILITY_VISIT_TWO, locale, assetManager);
            }

            public static String getCOUNSELLING() {
                return Utils.getLocalForm(COUNSELLING, locale, assetManager);
            }

            public static String getUmbilicalCord() {
                return Utils.getLocalForm(UMBILICAL_CORD, locale, assetManager);
            }

            public static String getNutritionStatusMother() {
                return Utils.getLocalForm(NUTRITION_STATUS_MOTHER, locale, assetManager);
            }

            public static String getNutritionStatusInfant() {
                return Utils.getLocalForm(NUTRITION_STATUS_INFANT, locale, assetManager);
            }

            public static String getMalariaPrevention() {
                return Utils.getLocalForm(MALARIA_PREVENTION, locale, assetManager);
            }

            public static String getFamilyPlanning() {
                return Utils.getLocalForm(FAMILY_PLANNING, locale, assetManager);
            }

            public static String getKangarooCare() {
                return Utils.getLocalForm(KANGAROO_CARE, locale, assetManager);
            }

            public static String getVaccineCard() {
                return Utils.getLocalForm(VACCINE_CARD, locale, assetManager);
            }

            public static String getExclusiveBreastFeeding() {
                return Utils.getLocalForm(EXCLUSIVE_BREAST_FEEDING, locale, assetManager);
            }

            public static String getObservationAndIllnessMother() {
                return Utils.getLocalForm(OBSERVATION_AND_ILLNESS_MOTHER, locale, assetManager);
            }

            public static String getObservationAndIllnessInfant() {
                return Utils.getLocalForm(OBSERVATION_AND_ILLNESS_INFANT, locale, assetManager);
            }
        }

        public static class FamilyPlanningFollowUpVisitUtils {
            private static final String FAMILY_PLANNING_FOLLOWUP_COUNSEL = "fp_followup_counsel";
            private static final String FAMILY_PLANNING_FOLLOWUP_RESUPPLY = "fp_followup_resupply";
            private static final String FAMILY_PLANNING_FOLLOWUP_SIDE_EFFECTS = "fp_followup_side_effects";

            public static String getFamilyPlanningFollowupCounsel() {
                return Utils.getLocalForm(FAMILY_PLANNING_FOLLOWUP_COUNSEL, locale, assetManager);
            }

            public static String getFamilyPlanningFollowupResupply() {
                return Utils.getLocalForm(FAMILY_PLANNING_FOLLOWUP_RESUPPLY, locale, assetManager);
            }

            public static String getFamilyPlanningFollowupSideEffects() {
                return Utils.getLocalForm(FAMILY_PLANNING_FOLLOWUP_SIDE_EFFECTS, locale, assetManager);
            }
        }
    }

    public static class RELATIONSHIP {
        public static final String FAMILY = "family";
        public static final String FAMILY_HEAD = "family_head";
        public static final String PRIMARY_CAREGIVER = "primary_caregiver";
        public static final String EVER_SCHOOL = "ever_school";
    }

    public static class TABLE_NAME {
        public static final String FAMILY = "ec_family";
        public static final String FAMILY_MEMBER = "ec_family_member";
        public static final String CHILD = "ec_child";
        public static final String CHILD_ACTIVITY = "ec_child_activity";
        public static final String ANC_MEMBER = "ec_anc_register";
        public static final String PNC_MEMBER = "ec_pregnancy_outcome";
        public static final String ANC_MEMBER_LOG = "ec_anc_log";
        public static final String FP_MEMBER = "ec_family_planning";
        public static final String MALARIA_CONFIRMATION = "ec_malaria_confirmation";
        public static final String ANC_PREGNANCY_OUTCOME = "ec_pregnancy_outcome";
        public static final String TASK = "task";
        public static final String WASH_CHECK_LOG = "ec_wash_check_log";
        public static final String FAMILY_KIT_LOG = "ec_family_kit_log";
        public static final String CHILD_REFERRAL = "ec_child_referral";
        public static final String ANC_REFERRAL = "ec_anc_referral";
        public static final String PNC_REFERRAL = "ec_pnc_referral";
        public static final String FP_REFERRAL = "ec_fp_referral";
        public static final String CLOSE_REFERRAL = "ec_close_referral";
        public static final String HIV_INDEX = "ec_hiv_index";
        public static final String NOT_YET_DONE_REFERRAL = "ec_not_yet_done_referral";
        public static final String SCHEDULE_SERVICE = "schedule_service";
        public static final String MALARIA_REFERRAL = "ec_malaria_referral";
        public static final String STOCK_USAGE_REPORT = "stock_usage_report";
        public static final String MONTHLY_TALLIES_REPORT = "monthly_tallies";
        public static final String SICK_CHILD_FOLLOW_UP = "ec_sick_child_followup";
        public static final String ANC_DANGER_SIGNS_OUTCOME = "ec_anc_danger_signs_outcome";
        public static final String REFERRAL_DISMISSAL = "ec_referral_dismissal";
        public static final String PNC_DANGER_SIGNS_OUTCOME = "ec_pnc_danger_signs_outcome";
        public static final String MALARIA_FOLLOW_UP_HF = "ec_malaria_followup_hf";
        public static final String COMMUNITY_RESPONDERS = "community_responders";
        public static final String REFERRAL = "ec_referral";
        public static final String FAMILY_PLANNING_UPDATE = "ec_family_planning_update";
        public static final String NOTIFICATION_UPDATE = "notification_update";
        public static final String INDEPENDENT_CLIENT = "ec_independent_client";
        public static final String HIV_MEMBER = "ec_hiv_register";
        public static final String TB_MEMBER = "ec_tb_register";
    }

    public static final class INTENT_KEY {
        public static final String SERVICE_DUE = "service_due";
        public static final String CHILD_BASE_ID = "child_base_id";
        public static final String CHILD_NAME = "child_name";
        public static final String CHILD_DATE_OF_BIRTH = "child_dob";
        public static final String CHILD_LAST_VISIT_DAYS = "child_visit_days";
        public static final String CHILD_VACCINE_LIST = "child_vaccine_list";
        public static final String GROWTH_TITLE = "growth_title";
        public static final String GROWTH_QUESTION = "growth_ques";
        public static final String GROWTH_IMMUNIZATION_TYPE = "growth_type";
        public static final String CHILD_COMMON_PERSON = "child_common_peron";
        public static final String USERS_TASKS = "tasks";
        public static final String CLASS = "class";
        public static final String VIEW_REGISTER_CLASS = "view_register_class";
        public static final String STARTING_ACTIVITY = "starting_activity";
        public static final String CLIENT = "client";
        public static final String MEMBER_OBJECT = "member_object";
        public static final String FAMILY_HEAD_NAME = "family_head_name";
        public static final String FAMILY_HEAD_PHONE_NUMBER = "family_head_phone_number";
        public static final String TOOLBAR_TITLE = "toolbar_title";
        public static final String CONTENT_TO_DISPLAY = "content_to_display";
    }

    public static final class IMMUNIZATION_CONSTANT {
        public static final String DATE = "date";
        public static final String VACCINE = "vaccine";
    }

    public static final class DrawerMenu {
        public static final String ALL_CLIENTS = "All Clients";
        public static final String ALL_FAMILIES = "All Families";
        public static final String ANC_CLIENTS = "ANC Clients";
        public static final String CHILD_CLIENTS = "Child Clients";
        public static final String CHILD = "Child";
        public static final String HIV_CLIENTS = "HIV Clients";
        public static final String HIV_INDEX_CLIENTS = "HIV Index Clients";
        public static final String TB_CLIENTS = "TB Clients";
        public static final String ANC = "ANC";
        public static final String LD = "L&D";
        public static final String PNC = "PNC";
        public static final String FAMILY_PLANNING = "Family Planning";
        public static final String MALARIA = "Malaria";
        public static final String REFERRALS = "Referrals";
        public static final String STOCK_USAGE_REPORT = "StockUsageItemModel usage report";
        public static final String UPDATES = "Updates";
    }

    public static final class RULE_FILE {
        public static final String HOME_VISIT = "home-visit-rules.yml";
        public static final String ANC_HOME_VISIT = "anc-home-visit-rules.yml";
        public static final String PNC_HOME_VISIT = "pnc-home-visit-rules.yml";
        public static final String IMMUNIZATION_EXPIRED = "immunization-expire-rules.yml";
        public static final String CONTACT_RULES = "contact-rules.yml";
        public static final String PNC_HEALTH_FACILITY_VISIT = "pnc-health-facility-schedule-rule.yml";
        public static final String MALARIA_FOLLOW_UP_VISIT = "malaria-followup-rules.yml";
        public static final String FP_COC_POP_REFILL = "fp-coc-pop-refill-rule.yml";
        public static final String FP_CONDOM_REFILL = "fp-condom-refill-rule.yml";
        public static final String FP_INJECTION_DUE = "fp-injection-due-rule.yml";
        public static final String FP_FEMALE_STERILIZATION = "fp-female-sterilization-rule.yml";
        public static final String FP_IUCD = "fp-iucd-rule.yml";
        public static final String TB_FOLLOW_UP_VISIT = "tb-followup-rules.yml";
        public static final String HIV_FOLLOW_UP_VISIT = "hiv-followup-rules.yml";
    }

    public static class PROFILE_CHANGE_ACTION {
        public static final String ACTION_TYPE = "change_action_type";
        public static final String PRIMARY_CARE_GIVER = "change_primary_cg";
        public static final String HEAD_OF_FAMILY = "change_head";
    }

    public static class JsonAssets {
        public static final String DETAILS = "details";
        public static final String FAM_NAME = "fam_name";
        public static final String SURNAME = "surname";
        public static final String PREGNANT_1_YR = "preg_1yr";
        public static final String SEX = "sex";
        public static final String PRIMARY_CARE_GIVER = "primary_caregiver";
        public static final String IS_PRIMARY_CARE_GIVER = "is_primary_caregiver";
        public static final String AGE = "age";
        public static final String ID_AVAIL = "id_avail";
        public static final String NATIONAL_ID = "national_id";
        public static final String VOTER_ID = "voter_id";
        public static final String DRIVER_LICENSE = "driver_license";
        public static final String PASSPORT = "passport";
        public static final String INSURANCE_PROVIDER = "insurance_provider";
        public static final String INSURANCE_PROVIDER_OTHER = "insurance_provider_other";
        public static final String INSURANCE_PROVIDER_NUMBER = "insurance_provider_number";
        public static final String DISABILITIES = "disabilities";
        public static final String DISABILITY_TYPE = "type_of_disability";
        public static final String SERVICE_PROVIDER = "service_provider";
        public static final String LEADER = "leader";
        public static final String OTHER_LEADER = "leader_other";
        public static final String BIRTH_CERT_AVAILABLE = "birth_cert_available";
        public static final String BIRTH_REGIST_NUMBER = "birth_regist_number";
        public static final String RHC_CARD = "rhc_card";
        public static final String NUTRITION_STATUS = "nutrition_status";
        public static final String GESTATION_AGE = "gestation_age";
        public static final String IS_PROBLEM = "is_problem";
        public static final String REFERRAL_CODE = "Referral";
        public static final String STOCK_NAME = "stock_name";
        public static final String STOCK_YEAR = "stock_year";
        public static final String STOCK_MONTH = "stock_month";
        public static final String STOCK_USAGE = "stock_usage";
        public static final String STOCK_PROVIDER = "stock_provider";
        public static final String RESPONDER_ID = "id";
        public static final String RESPONDER_NAME = "responder_name";
        public static final String RESPONDER_PHONE_NUMBER = "responder_phone_number";
        public static final String RESPONDER_GPS = "responder_gps";
        public static final String IN_APP_REPORT_INDICATOR_CODE = "indicator_code";
        public static final String IN_APP_REPORT_MONTH = "month";
        public static final String IN_APP_REPORT_EDITED = "edited";
        public static final String IN_APP_REPORT_DATE_SENT = "date_sent";
        public static final String IN_APP_REPORT_CREATED_AT = "created_at";
        public static final String IN_APP_REPORT_VALUE = "value";

        public static class FAMILY_MEMBER {
            public static final String HIGHEST_EDUCATION_LEVEL = "highest_edu_level";
            public static final String EVER_SCHOOL = "ever_school";
            public static final String SCHOOL_LEVEL = "school_level";
            public static final String PHONE_NUMBER = "phone_number";
            public static final String OTHER_PHONE_NUMBER = "other_phone_number";
        }

    }

    public static class ProfileActivityResults {
        public static final int CHANGE_COMPLETED = 9090;
    }

    public static class FORM_CONSTANTS {

        public static class REMOVE_MEMBER_FORM {
            public static final String REASON = "remove_reason";
            public static final String DATE_DIED = "date_died";
            public static final String DATE_MOVED = "date_moved";
        }

        public static class CHANGE_CARE_GIVER {
            public static class PHONE_NUMBER {
                public static final String CODE = "159635AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            }

            public static class OTHER_PHONE_NUMBER {
                public static final String CODE = "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
                public static final String PARENT_CODE = "159635AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            }

            public static class HIGHEST_EDU_LEVEL {
                public static final String CODE = "1712AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            }

            public static class EverSchool {
                public static final String CODE = "everSchool";
            }

            public static class SchoolLevel {
                public static final String CODE = "schoolLevel";
            }
        }

        public static class ILLNESS_ACTION_TAKEN_LEVEL {
            public static final String CODE = "164378AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }

        public static class VACCINE_CARD {
            public static final String CODE = "164147AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }

        public static class MINIMUM_DIETARY {
            public static final String CODE = "";
        }

        public static class MUAC {
            public static final String CODE = "160908AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }

        public static class LLITN {
            public static final String CODE = "1802AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        }

        public static class FORM_SUBMISSION_FIELD {
            public static final String TASK_MINIMUM_DIETARY = "diet_diversity";
            public static final String TASK_MUAC = "muac";
            public static final String TASK_LLITN = "llitn";
            public static final String HOME_VISIT_ID = "home_visit_id";
            public static final String HOME_VISIT_DATE_LONG = "home_visit_date";
            public static final String LAST_HOME_VISIT = "last_home_visit";
            public static final String HOME_VISIT_SINGLE_VACCINE = "singleVaccine";
            public static final String HOME_VISIT_GROUP_VACCINE = "groupVaccine";
            public static final String HOME_VISIT_VACCINE_NOT_GIVEN = "vaccineNotGiven";
            public static final String HOME_VISIT_SERVICE = "service";
            public static final String HOME_VISIT_SERVICE_NOT_GIVEN = "serviceNotGiven";
            public static final String HOME_VISIT_BIRTH_CERT = "birth_certificate";
            public static final String HOME_VISIT_ILLNESS = "illness_information";
            public static final String WASH_CHECK_DETAILS = "details_info";
            public static final String WASH_CHECK_LAST_VISIT = "last_visit";
            public static final String FAMILY_KIT_DETAILS = "family_kit_details_info";
            public static final String FAMILY_KIT_LAST_VISIT = "family_kit_last_visit";
            public static final String FAMILY_ID = "family_id";
            public static final String REFERRAL_TASK = "referral_task";
            public static final String REFERRAL_TASK_PREVIOUS_STATUS = "referral_task_previous_status";
            public static final String REFERRAL_TASK_PREVIOUS_BUSINESS_STATUS = "referral_task_previous_business_status";
            public static final String NOTIFICATION_ID = "notification_id";
            public static final String DATE_NOTIFICATION_MARKED_AS_DONE = "date_marked_as_done";
            public static final String INDEX_CLIENT_BASE_ENTITY_ID = "index_client_base_entity_id";
        }
    }

    public static class GLOBAL {
        public static final String NAME = "name";
        public static final String MESSAGE = "message";
    }

    public static class MenuType {
        public static final String ChangeHead = "ChangeHead";
        public static final String ChangePrimaryCare = "ChangePrimaryCare";
    }

    public static class IDENTIFIER {
        public static final String UNIQUE_IDENTIFIER_KEY = "opensrp_id";
    }

    public static final class RQ_CODE {
        public static final int STORAGE_PERMISIONS = 1;
    }

    public static final class PEER_TO_PEER {
        public static final String LOCATION_ID = "location-id";
    }

    public static final class KujakuConstants {
        public static final String LAT_LNG = "latLng";
        public static final String LAND_MARK = "landMark";
        public static final String NAME = "name";
        public static final String ANC_WOMAN_PHONE = "ancWomanPhone";
        public static final String ANC_WOMAN_FAMILY_HEAD = "ancWomanFamilyHead";
        public static final String ANC_WOMAN_FAMILY_HEAD_PHONE = "ancWomanFamilyHeadPhone";
        public static final String FAMILY_NAME = "familyName";
    }

    public static final class ACTIVITY_PAYLOAD {
        public static final String ACTION = "action";
    }

    public static final class ACTION {
        public static final String START_REGISTRATION = "start_registration";
    }

    public static final class VISIT_STATE {
        public static final String EXPIRED = "EXPIRED";
        public static final String DUE = "DUE";
        public static final String OVERDUE = "OVERDUE";
        public static final String VISIT_NOT_DONE = "VISIT_NOT_DONE";
        public static final String VISIT_DONE = "VISIT_DONE";
        public static final String NOT_VISIT_THIS_MONTH = "NOT_VISIT_THIS_MONTH";
        public static final String NOT_DUE_YET = "NOT_DUE_YET";
    }

    public static final class DATE_FORMATS {
        public static final String NATIVE_FORMS = "dd-MM-yyyy";
        public static final String HOME_VISIT_DISPLAY = "dd MMM yyyy";
        public static final String FORMATED_DB_DATE = "yyyy-MM-dd";
        public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm'Z'";
    }

    public static final class REGISTERED_ACTIVITIES {
        public static final String CHILD_REGISTER_ACTIVITY = "CHILD_REGISTER_ACTIVITY";
        public static final String FAMILY_REGISTER_ACTIVITY = "FAMILY_REGISTER_ACTIVITY";
        public static final String ANC_REGISTER_ACTIVITY = "ANC_REGISTER_ACTIVITY";
        public static final String PNC_REGISTER_ACTIVITY = "PNC_REGISTER_ACTIVITY";
        public static final String REFERRALS_REGISTER_ACTIVITY = "REFERRALS_REGISTER_ACTIVITY";
        public static final String MALARIA_REGISTER_ACTIVITY = "MALARIA_REGISTER_ACTIVITY";
        public static final String FP_REGISTER_ACTIVITY = "FP_REGISTER_ACTIVITY";
        public static final String ALL_CLIENTS_REGISTERED_ACTIVITY = "ALL_CLIENTS";
        public static final String UPDATES_REGISTER_ACTIVITY = "UPDATES_REGISTER_ACTIVITY";
        public static final String HIV_REGISTER_ACTIVITY = "HIV_REGISTER_ACTIVITY";
        public static final String HIV_INDEX_REGISTER_ACTIVITY = "HIV_INDEX_REGISTER_ACTIVITY";
        public static final String TB_REGISTER_ACTIVITY = "TB_REGISTER_ACTIVITY";
    }

    public static final class BUSINESS_STATUS {
        public static final String REFERRED = "Referred";
        public static final String IN_PROGRESS = "In-Progress";
        public static final String COMPLETE = "Complete";
        public static final String EXPIRED = "Expired";
    }

    public static final class TASKS_FOCUS {
        public static final String SICK_CHILD = "Sick Child";
        public static final String ANC_DANGER_SIGNS = "ANC Danger Signs";
        public static final String PNC_DANGER_SIGNS = "PNC Danger Signs";
        public static final String FP_SIDE_EFFECTS = "FP Initiation";
        public static final String SUSPECTED_MALARIA = "Suspected Malaria";
        public static final String SUSPECTED_HIV = "Suspected HIV";
        public static final String SUSPECTED_TB = "Suspected TB";
        public static final String SUSPECTED_GBV = "Suspected GBV";
        public static final String SUSPECTED_CHILD_GBV = "Suspected Child GBV";
    }

    public static final class REGISTER_TYPE {
        public static final String CHILD = "Child";
        public static final String ANC = "ANC";
        public static final String PNC = "PNC";
        public static final String MALARIA = "Malaria";
        public static final String FAMILY_PLANNING = "Family Planning";
        public static final String TB = "TB";
        public static final String HIV = "HIV";
        public static final String OTHER = "Other";
        public static final String INDEPENDENT = "Independent";
    }

    public static final class KeyIndicatorsUtil {
        public static final String HIA_2_INDICATOR = "hia2_indicator";
        public static final String STEPNAME = "stepName";
        public static final String LOOK_UP = "look_up";
        public static final String ENTITY_ID = "entity_id";
    }

    public static class HfStockUsageUtil {
        public static final String STOCK_NAME = "stockName";
        public static final String PROVIDER_NAME = "providerName";
        public static final String STOCK_USAGE_TABLE_NAME = "stock_usage_report";
        public static final String PROVIDER_TYPE = "stock_usage_providers";

    }

    public static class HfInAppUtil {
        public static final String IN_APP_TABLE_NAME = "monthly_tallies";
        public static final String PROVIDER_TYPE = "in_app_providers";
    }

    public static class AncHealthFacilityVisitUtil {
        public static final String TETANUS_TOXOID = "Tetanus toxoid (TT)";
    }


    public static class ThinkMdConstants {
        public static String HTML_ASSESSMENT = "html_assessment";
        public static String CARE_PLAN_DATE = "care_plan_date";
        public static String CHILD_PROFILE_ACTIVITY = "org.smartregister.chw.activity.ChildProfileActivity";
    }
}
