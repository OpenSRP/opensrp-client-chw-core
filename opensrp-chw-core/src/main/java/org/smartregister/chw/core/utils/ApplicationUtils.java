package org.smartregister.chw.core.utils;

import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.family.util.DBConstants;

public class ApplicationUtils {
    public static CommonFtsObject getCommonFtsObject(CommonFtsObject commonFtsObject) {
        CommonFtsObject ftsObject;
        if (commonFtsObject == null) {
            ftsObject = new CommonFtsObject(getFtsTables());
            for (String ftsTable : ftsObject.getTables()) {
                ftsObject.updateSearchFields(ftsTable, getFtsSearchFields(ftsTable));
                ftsObject.updateSortFields(ftsTable, getFtsSortFields(ftsTable));
            }
        } else {
            ftsObject = commonFtsObject;
        }
        return ftsObject;
    }

    private static String[] getFtsTables() {
        return new String[]{
                CoreConstants.TABLE_NAME.FAMILY,
                CoreConstants.TABLE_NAME.FAMILY_MEMBER,
                CoreConstants.TABLE_NAME.CHILD,
                CoreConstants.TABLE_NAME.ANC_MEMBER,
                CoreConstants.TABLE_NAME.PNC_MEMBER
        };
    }

    private static String[] getFtsSearchFields(String tableName) {
        switch (tableName) {
            case CoreConstants.TABLE_NAME.FAMILY:
                return new String[]{
                        DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.VILLAGE_TOWN, DBConstants.KEY.FIRST_NAME,
                        DBConstants.KEY.LAST_NAME, DBConstants.KEY.UNIQUE_ID
                };
            case CoreConstants.TABLE_NAME.FAMILY_MEMBER:
                return new String[]{
                        DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.FIRST_NAME, DBConstants.KEY.MIDDLE_NAME,
                        DBConstants.KEY.LAST_NAME, DBConstants.KEY.UNIQUE_ID, DBConstants.KEY.RELATIONAL_ID
                };
            case CoreConstants.TABLE_NAME.CHILD:
                return new String[]{
                        DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.FIRST_NAME, DBConstants.KEY.MIDDLE_NAME,
                        DBConstants.KEY.LAST_NAME, DBConstants.KEY.UNIQUE_ID, ChildDBConstants.KEY.ENTRY_POINT, DBConstants.KEY.DOB, DBConstants.KEY.DATE_REMOVED
                };
            case CoreConstants.TABLE_NAME.ANC_MEMBER:
            case CoreConstants.TABLE_NAME.PNC_MEMBER:
                return new String[]{
                        DBConstants.KEY.BASE_ENTITY_ID, DBConstants.KEY.UNIQUE_ID, DBConstants.KEY.RELATIONAL_ID
                };
            default:
                return null;
        }
    }

    private static String[] getFtsSortFields(String tableName) {
        switch (tableName) {
            case CoreConstants.TABLE_NAME.FAMILY:
                return new String[]{DBConstants.KEY.LAST_INTERACTED_WITH, DBConstants.KEY.DATE_REMOVED,
                        DBConstants.KEY.FAMILY_HEAD, DBConstants.KEY.PRIMARY_CAREGIVER, DBConstants.KEY.ENTITY_TYPE,
                        CoreConstants.DB_CONSTANTS.DETAILS};
            case CoreConstants.TABLE_NAME.FAMILY_MEMBER:
                return new String[]{DBConstants.KEY.DOB, DBConstants.KEY.DOD,
                        DBConstants.KEY.LAST_INTERACTED_WITH, DBConstants.KEY.DATE_REMOVED, DBConstants.KEY.RELATIONAL_ID};
            case CoreConstants.TABLE_NAME.CHILD:
                return new String[]{ChildDBConstants.KEY.LAST_HOME_VISIT, ChildDBConstants.KEY.VISIT_NOT_DONE, DBConstants.KEY
                        .LAST_INTERACTED_WITH, ChildDBConstants.KEY.DATE_CREATED, DBConstants.KEY.DATE_REMOVED, DBConstants.KEY.DOB, ChildDBConstants.KEY.ENTRY_POINT};
            case CoreConstants.TABLE_NAME.ANC_MEMBER:
                return new String[]{
                        DBConstants.KEY.LAST_INTERACTED_WITH, DBConstants.KEY.RELATIONAL_ID, org.smartregister.chw.anc.util.DBConstants.KEY.LAST_HOME_VISIT,
                        org.smartregister.chw.anc.util.DBConstants.KEY.VISIT_NOT_DONE};
            case CoreConstants.TABLE_NAME.PNC_MEMBER:
                return new String[]{
                        DBConstants.KEY.LAST_INTERACTED_WITH, DBConstants.KEY.RELATIONAL_ID, ChwDBConstants.DELIVERY_DATE};
            default:
                return null;
        }
    }
}
