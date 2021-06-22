package org.smartregister.chw.core.rule;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.core.utils.CoreConstants;

import java.util.Date;

public class TbFollowupRuleTest {

    private Date lastVisitDate = new DateTime().minusDays(7).toDate();
    private Date tbRegistrationDate = new DateTime().minusDays(30).toDate();
    private int dueDay = 1;
    private int overdueDay = 3;
    private int expiry = 5;

    private TbFollowupRule tbFollowupRule = new TbFollowupRule(tbRegistrationDate, lastVisitDate);
    private TbFollowupRule tbFollowupRule1 = new TbFollowupRule(tbRegistrationDate, null);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        valid();
    }

    @Test
    public void testGetAndSetVisitID() {
        String visitId = "12345";
        tbFollowupRule.setVisitID(visitId);
        Assert.assertEquals("12345", tbFollowupRule.getVisitID());
    }

    @Test
    public void testGetDueDate() {
        Assert.assertEquals(tbFollowupRule.getDueDate(), new DateTime(lastVisitDate).plusDays(1).toDate());
    }

    @Test
    public void testGetOverDueDate() {
        Assert.assertEquals(tbFollowupRule.getOverDueDate(), new DateTime(lastVisitDate).plusDays(3).toDate());

    }

    @Test
    public void testGetExpiryDate() {
        Assert.assertEquals(tbFollowupRule.getExpiryDate(), new DateTime(lastVisitDate).plusDays(5).toDate());
    }

    @Test
    public void testGetRuleKey() {
        Assert.assertEquals("tbFollowupRule", tbFollowupRule.getRuleKey());
    }

    private void valid() {
        tbFollowupRule.isValid(dueDay, overdueDay, expiry);
    }

    @Test
    public void testGetButtonStatusWhenLastVisitIsNotNull() {
        Assert.assertEquals(CoreConstants.VISIT_STATE.EXPIRED, tbFollowupRule.getButtonStatus());

        dueDay = 6;
        overdueDay = 14;
        expiry = 28;
        valid();
        Assert.assertEquals(CoreConstants.VISIT_STATE.DUE, tbFollowupRule.getButtonStatus());

        dueDay = 2;
        overdueDay = 6;
        expiry = 14;
        valid();
        Assert.assertEquals(CoreConstants.VISIT_STATE.OVERDUE, tbFollowupRule.getButtonStatus());
    }

    @Test
    public void testGetButtonStatusWhenLastVisitIsNull() {
        dueDay =29;
        overdueDay = 32;
        expiry = 34;
        tbFollowupRule1.isValid(dueDay, overdueDay, expiry);
        Assert.assertEquals(CoreConstants.VISIT_STATE.DUE, tbFollowupRule1.getButtonStatus());

        dueDay = 26;
        overdueDay = 29;
        expiry = 33;
        tbFollowupRule1.isValid(dueDay, overdueDay, expiry);
        Assert.assertEquals(CoreConstants.VISIT_STATE.OVERDUE, tbFollowupRule1.getButtonStatus());
    }
}