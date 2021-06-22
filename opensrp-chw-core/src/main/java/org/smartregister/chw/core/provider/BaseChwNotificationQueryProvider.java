package org.smartregister.chw.core.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.smartregister.chw.core.utils.CoreReferralUtils;

import static org.smartregister.chw.core.utils.QueryConstant.ANC_DANGER_SIGNS_OUTCOME_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.ANC_DANGER_SIGNS_OUTCOME_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.FAMILY_PLANNING_UPDATE_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.FAMILY_PLANNING_UPDATE_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.HIV_OUTCOME_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.HIV_OUTCOME_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.MALARIA_FOLLOW_UP_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.MALARIA_HF_FOLLOW_UP_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.NOT_YET_DONE_REFERRAL_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.NOT_YET_DONE_REFERRAL_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.PNC_DANGER_SIGNS_OUTCOME_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.PNC_DANGER_SIGNS_OUTCOME_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.SICK_CHILD_FOLLOW_UP_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.SICK_CHILD_FOLLOW_UP_MAIN_SELECT;
import static org.smartregister.chw.core.utils.QueryConstant.TB_OUTCOME_COUNT_QUERY;
import static org.smartregister.chw.core.utils.QueryConstant.TB_OUTCOME_MAIN_SELECT;

public class BaseChwNotificationQueryProvider {
    /**
     * Return query to be used to select object_ids from the search table so that these objects_ids
     * are later used to retrieve the actual rows from the normal(non-FTS) table
     *
     * @param filters This is the search phrase entered in the search box
     * @return query string for getting object ids
     */
    @NonNull
    public String getObjectIdsQuery(@Nullable String filters) {
        return CoreReferralUtils.getFamilyMemberFtsSearchQuery(filters);
    }

    /**
     * Return query(s) to be used to perform the total count of register clients eg. If OPD combines records
     * in multiple tables then you can provide multiple queries with the result having a single row+column
     * and the counts will be summed up. Kindly try to use the search tables wherever possible.
     *
     * @return query string used for counting items
     */
    @NonNull
    public String[] countExecuteQueries() {
        return new String[]{
                SICK_CHILD_FOLLOW_UP_COUNT_QUERY,
                ANC_DANGER_SIGNS_OUTCOME_COUNT_QUERY,
                PNC_DANGER_SIGNS_OUTCOME_COUNT_QUERY,
                FAMILY_PLANNING_UPDATE_COUNT_QUERY,
                MALARIA_HF_FOLLOW_UP_COUNT_QUERY,
                HIV_OUTCOME_COUNT_QUERY,
                TB_OUTCOME_COUNT_QUERY,
                NOT_YET_DONE_REFERRAL_COUNT_QUERY
        };
    }

    /**
     * Return query to be used to retrieve the client details. This query should have a "WHERE base_entity_id IN (%s)" clause where
     * the comma-separated  base-entity-ids for the clients will be inserted into the query and later
     * executed
     *
     * @return query string used for displaying notifications
     */
    @NonNull
    public String mainSelectWhereIDsIn() {
        return String.format("%s UNION ALL %s UNION ALL %s UNION ALL %s UNION ALL %s UNION ALL %s UNION ALL %s UNION ALL %s",
                SICK_CHILD_FOLLOW_UP_MAIN_SELECT, ANC_DANGER_SIGNS_OUTCOME_MAIN_SELECT,
                PNC_DANGER_SIGNS_OUTCOME_MAIN_SELECT, FAMILY_PLANNING_UPDATE_MAIN_SELECT,
                MALARIA_FOLLOW_UP_MAIN_SELECT, HIV_OUTCOME_MAIN_SELECT, TB_OUTCOME_MAIN_SELECT, NOT_YET_DONE_REFERRAL_MAIN_SELECT);
    }
}
