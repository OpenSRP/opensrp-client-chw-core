package org.smartregister.chw.core.interactor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.core.BaseUnitTest;
import org.smartregister.chw.tb.domain.TbMemberObject;

/**
 * Created by cozej4 on 7/14/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class CoreTbProfileInteractorTest extends BaseUnitTest {

    private CoreTbProfileInteractor interactor;

    @Before
    public void setUp() {
        interactor = new CoreTbProfileInteractor(null);
    }

    @Test
    public void testToMember() {

        TbMemberObject tbMemberObject = new TbMemberObject(null);
        tbMemberObject.setBaseEntityId("2332");
        tbMemberObject.setFirstName("John");
        tbMemberObject.setLastName("Doe");
        tbMemberObject.setMiddleName("Paul");

        MemberObject memberObject = interactor.toMember(tbMemberObject);
        Assert.assertEquals("2332", memberObject.getBaseEntityId());
        Assert.assertEquals("John", memberObject.getFirstName());
        Assert.assertEquals("Doe", memberObject.getLastName());
        Assert.assertEquals("Paul", memberObject.getMiddleName());

    }
}