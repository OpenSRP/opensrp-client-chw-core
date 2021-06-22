package org.smartregister.chw.core.interactor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.hiv.domain.HivMemberObject;

/**
 * Created by cozej4 on 7/14/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class CoreHivProfileInteractorTest extends BaseUnitTest {

    private CoreHivProfileInteractor interactor;

    @Before
    public void setUp() {
        interactor = new CoreHivProfileInteractor(null);
    }

    @Test
    public void testToMember() {

        HivMemberObject hivMemberObject = new HivMemberObject(null);
        hivMemberObject.setBaseEntityId("2332");
        hivMemberObject.setFirstName("John");
        hivMemberObject.setLastName("Doe");
        hivMemberObject.setMiddleName("Paul");

        MemberObject memberObject = interactor.toMember(hivMemberObject);
        Assert.assertEquals("2332", memberObject.getBaseEntityId());
        Assert.assertEquals("John", memberObject.getFirstName());
        Assert.assertEquals("Doe", memberObject.getLastName());
        Assert.assertEquals("Paul", memberObject.getMiddleName());

    }
}