package org.smartregister.chw.core.rule;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.utils.CoreConstants;

import java.util.Date;

public class HivFollowupRuleTest {

    private Date lastVisitDate = new DateTime().minusDays(7).toDate();
    private Date hivRegistrationDate = new DateTime().minusDays(30).toDate();
    private int dueDay = 1;
    private int overdueDay = 3;
    private int expiry = 5;

    private HivFollowupRule hivFollowupRule = new HivFollowupRule(hivRegistrationDate, lastVisitDate);
    private HivFollowupRule hivFollowupRule1 = new HivFollowupRule(hivRegistrationDate, null);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        valid();
    }

    @Test
    public void testGetAndSetVisitID() {
        String visitId = "12345";
        hivFollowupRule.setVisitID(visitId);
        Assert.assertEquals("12345", hivFollowupRule.getVisitID());
    }

    @Test
    public void testGetDueDate() {
        Assert.assertEquals(hivFollowupRule.getDueDate(), new DateTime(lastVisitDate).plusDays(1).toDate());
    }

    @Test
    public void testGetOverDueDate() {
        Assert.assertEquals(hivFollowupRule.getOverDueDate(), new DateTime(lastVisitDate).plusDays(3).toDate());

    }

    @Test
    public void testGetExpiryDate() {
        Assert.assertEquals(hivFollowupRule.getExpiryDate(), new DateTime(lastVisitDate).plusDays(5).toDate());
    }

    @Test
    public void testGetRuleKey() {
        Assert.assertEquals("hivFollowupRule", hivFollowupRule.getRuleKey());
    }

    private void valid() {
        hivFollowupRule.isValid(dueDay, overdueDay, expiry);
    }

    @Test
    public void testGetButtonStatusWhenLastVisitIsNotNull() {
        Assert.assertEquals(CoreConstants.VISIT_STATE.EXPIRED, hivFollowupRule.getButtonStatus());

        dueDay = 6;
        overdueDay = 14;
        expiry = 28;
        valid();
        Assert.assertEquals(CoreConstants.VISIT_STATE.DUE, hivFollowupRule.getButtonStatus());

        dueDay = 2;
        overdueDay = 6;
        expiry = 14;
        valid();
        Assert.assertEquals(CoreConstants.VISIT_STATE.OVERDUE, hivFollowupRule.getButtonStatus());
    }

    @Test
    public void testGetButtonStatusWhenLastVisitIsNull() {
        dueDay =29;
        overdueDay = 32;
        expiry = 34;
        hivFollowupRule1.isValid(dueDay, overdueDay, expiry);
        Assert.assertEquals(CoreConstants.VISIT_STATE.DUE, hivFollowupRule1.getButtonStatus());

        dueDay = 26;
        overdueDay = 29;
        expiry = 33;
        hivFollowupRule1.isValid(dueDay, overdueDay, expiry);
        Assert.assertEquals(CoreConstants.VISIT_STATE.OVERDUE, hivFollowupRule1.getButtonStatus());
    }
}