package org.smartregister.chw.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cozej4 on 7/16/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class ChildDBConstantsTest {

    @Test
    public void childAgeLimitFilter() {
        String filter = " ((( julianday('now') - julianday(ec_child.dob))/365.25) <5)   and (( ifnull(ec_child.entry_point,'') <> 'PNC' ) or (ifnull(ec_child.entry_point,'') = 'PNC' and ( date(ec_child.dob, '+28 days') <= date() and ((SELECT is_closed FROM ec_family_member WHERE base_entity_id = ec_child.mother_entity_id ) = 0)))  or (ifnull(ec_child.entry_point,'') = 'PNC'  and (SELECT is_closed FROM ec_family_member WHERE base_entity_id = ec_child.mother_entity_id ) = 1))  and ((( julianday('now') - julianday(ec_child.dob))/365.25) < 5) ";
        Assert.assertEquals(filter, ChildDBConstants.childAgeLimitFilter());
    }

    @Test
    public void childDueFilter() {
        String filter = "(( IFNULL(STRFTIME('%Y%m%d%H%M%S', datetime((last_home_visit)/1000,'unixepoch')),0) < STRFTIME('%Y%m%d%H%M%S', datetime('now','start of month')) AND IFNULL(STRFTIME('%Y%m%d%H%M%S', datetime((visit_not_done)/1000,'unixepoch')),0) < STRFTIME('%Y%m%d%H%M%S', datetime('now','start of month'))  ))";
        Assert.assertEquals(filter, ChildDBConstants.childDueFilter());
    }

    @Test
    public void childMainFilter() {
        String dueMainFilter = "SELECT object_id FROM ec_child_search WHERE object_id IN  ( SELECT object_id FROM ec_child_search WHERE  object_id = '1234'  AND phrase MATCH 'john*'  UNION  SELECT ec_child_search.object_id FROM ec_child_search JOIN ec_family_search on ec_child_search.object_relational_id = ec_family_search.object_id JOIN ec_family_member_search on ec_family_member_search.object_id = ec_family_search.primary_caregiver WHERE  object_id = '1234' AND ec_family_member_search.phrase MATCH 'john*' )   ORDER BY last_interacted_with LIMIT 0,10";
        Assert.assertEquals(dueMainFilter, ChildDBConstants.childMainFilter("object_id = '1234'", "object_id = '1234'", "john", "last_interacted_with", 10, 0));
    }
}