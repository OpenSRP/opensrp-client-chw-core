package org.smartregister.chw.core.model;


import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.Arrays;

public class PncRegisterFragmentModelTest {
    private  PncRegisterFragmentModel pncRegisterFragmentModel;
    @Before
    public void setUp() {
        pncRegisterFragmentModel = Mockito.spy(PncRegisterFragmentModel.class);
    }
    @Test
    public void testMainSelect() {

        pncRegisterFragmentModel = new PncRegisterFragmentModel();
        String mainSelect = pncRegisterFragmentModel.mainSelect("ec_pregnancy_outcome", "where ec_family_member.last_name = Doe");
        Assert.assertNotNull(mainSelect);
        Assert.assertEquals(mainSelect, "Select ec_pregnancy_outcome.id as _id , " +
                "ec_anc_register.phone_number , ec_family_member.middle_name , " +
                "ec_pregnancy_outcome.base_entity_id , " +
                "ec_family_member.first_name , ec_family.primary_caregiver , " +
                "ec_family_member.last_name , ec_family.village_town , ec_family_member.dob , " +
                "ec_family.family_head , ec_family.first_name as family_name , " +
                "ec_family_member.unique_id , ec_family_member.relational_id as relationalid , " +
                "ec_family_member.relational_id , ec_pregnancy_outcome.delivery_date FROM " +
                "ec_pregnancy_outcome INNER JOIN ec_family_member ON  " +
                "ec_pregnancy_outcome.base_entity_id = ec_family_member.base_entity_id " +
                "AND ec_pregnancy_outcome.is_closed IS 0 AND ec_family_member.is_closed IS 0 AND " +
                "ec_pregnancy_outcome.delivery_date IS NOT NULL COLLATE NOCASE  INNER JOIN ec_family " +
                "ON  ec_family_member.relational_id = ec_family.base_entity_id COLLATE NOCASE  " +
                "INNER JOIN ec_anc_register ON  " +
                "ec_pregnancy_outcome.base_entity_id = ec_anc_register.base_entity_id AND " +
                "ec_pregnancy_outcome.is_closed IS 0 AND ec_pregnancy_outcome.delivery_date " +
                "IS NOT NULL COLLATE NOCASE  WHERE where ec_family_member.last_name = Doe ");
    }
    @Test
    public void testMainColumns() {
        String[] mainColumns = pncRegisterFragmentModel.mainColumns("ec_pregnancy_outcome");
        Assert.assertEquals(14, mainColumns.length);
        Assert.assertEquals(mainColumns[0], "ec_anc_register.phone_number");
        Assert.assertEquals(mainColumns[2], "ec_pregnancy_outcome.base_entity_id");
    }
}