package org.smartregister.chw.core.rule;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.smartregister.chw.core.utils.CoreConstants;

import java.util.Date;

public class TbFollowupRule implements ICommonRule {
    public static final String RULE_KEY = "tbFollowupRule";
    private String visitID;
    private DateTime tbDate;
    private DateTime dueDate;
    private DateTime overDueDate;
    private DateTime lastVisitDate;
    private DateTime expiryDate;
    private int daysDifference;

    public TbFollowupRule(Date tbDate, Date lastVisitDate) {
        this.tbDate = tbDate != null ? new DateTime(tbDate.getTime()) : null;
        this.lastVisitDate = lastVisitDate == null ? null : new DateTime(lastVisitDate);
    }

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public int getDaysDifference() {
        return daysDifference;
    }

    public boolean isValid(int dueDay, int overdueDate, int expiry) {
        if (lastVisitDate != null) {
            this.dueDate = lastVisitDate.plusDays(dueDay);
            this.overDueDate = lastVisitDate.plusDays(overdueDate);
            this.expiryDate = lastVisitDate.plusDays(expiry);
        } else {
            this.dueDate = tbDate.plusDays(dueDay);
            this.overDueDate = tbDate.plusDays(overdueDate);
            this.expiryDate = tbDate.plusDays(expiry);
        }

        daysDifference = Days.daysBetween(new DateTime(), new DateTime(dueDate)).getDays();
        return true;
    }

    public Date getDueDate() {
        return dueDate != null ? dueDate.toDate() : null;
    }

    public Date getOverDueDate() {
        return overDueDate != null ? overDueDate.toDate() : null;
    }

    public Date getExpiryDate() {
        return expiryDate != null ? expiryDate.toDate() : null;
    }

    @Override
    public String getRuleKey() {
        return "tbFollowupRule";
    }

    @Override
    public String getButtonStatus() {
        DateTime currentDate = new DateTime(new LocalDate().toDate());
        DateTime lastVisit = lastVisitDate;

        if (currentDate.isBefore(expiryDate)) {
            if ((currentDate.isAfter(overDueDate) || currentDate.isEqual(overDueDate)))
                return CoreConstants.VISIT_STATE.OVERDUE;
            if ((currentDate.isAfter(dueDate) || currentDate.isEqual(dueDate)) && currentDate.isBefore(overDueDate))
                return CoreConstants.VISIT_STATE.DUE;
            if (lastVisit != null && currentDate.isEqual(lastVisit))
                return CoreConstants.VISIT_STATE.VISIT_DONE;
            return CoreConstants.VISIT_STATE.NOT_DUE_YET;

        }
        return CoreConstants.VISIT_STATE.EXPIRED;
    }
}