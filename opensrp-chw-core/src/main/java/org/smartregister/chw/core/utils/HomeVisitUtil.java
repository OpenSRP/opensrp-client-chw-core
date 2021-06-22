package org.smartregister.chw.core.utils;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.jeasy.rules.api.Rules;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.contract.RegisterAlert;
import org.smartregister.chw.core.rule.AncVisitAlertRule;
import org.smartregister.chw.core.rule.FpAlertRule;
import org.smartregister.chw.core.rule.HivFollowupRule;
import org.smartregister.chw.core.rule.PncVisitAlertRule;
import org.smartregister.chw.core.rule.TbFollowupRule;

import java.util.Date;

public class HomeVisitUtil {
    public static VisitSummary getAncVisitStatus(Context context, Rules rules, String visitDate, String visitNotDate, LocalDate dateCreated) {
        AncVisitAlertRule ancVisitAlertRule = new AncVisitAlertRule(context, DateTimeFormat.forPattern("dd-MM-yyyy").print(dateCreated), visitDate, visitNotDate, dateCreated);
        CoreChwApplication.getInstance().getRulesEngineHelper().getButtonAlertStatus(ancVisitAlertRule, rules);
        Date date = null;
        if (StringUtils.isNotBlank(visitDate)) {
            date = DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(visitDate).toDate();
        }
        return getAncVisitStatus(ancVisitAlertRule, date);
    }

    public static VisitSummary getAncVisitStatus(RegisterAlert registerAlert, Date visitDate) {
        VisitSummary visitSummary = new VisitSummary();
        visitSummary.setVisitStatus(registerAlert.getButtonStatus());
        visitSummary.setNoOfMonthDue(registerAlert.getNumberOfMonthsDue());
        visitSummary.setNoOfDaysDue(registerAlert.getNumberOfDaysDue());
        visitSummary.setLastVisitDays(registerAlert.getNumberOfDaysDue());
        visitSummary.setLastVisitMonthName(registerAlert.getVisitMonthName());
        if (visitDate != null) {
            visitSummary.setLastVisitTime(visitDate.getTime());
        }
        return visitSummary;
    }

    public static PncVisitAlertRule getPncVisitStatus(Rules rules, Date lastVisitDate, Date deliveryDate) {
        PncVisitAlertRule pncVisitAlertRule = new PncVisitAlertRule(lastVisitDate, deliveryDate);
        CoreChwApplication.getInstance().getRulesEngineHelper().getButtonAlertStatus(pncVisitAlertRule, rules);
        return pncVisitAlertRule;
    }

    public static FpAlertRule getFpVisitStatus(Rules rules, Date lastVisitDate, Date fpDate, Integer pillCycles, String fpMethod) {
        FpAlertRule fpAlertRule = new FpAlertRule(fpDate, lastVisitDate, pillCycles, fpMethod);
        CoreChwApplication.getInstance().getRulesEngineHelper().getButtonAlertStatus(fpAlertRule, rules);
        return fpAlertRule;
    }

    public static TbFollowupRule getTbVisitStatus( Date lastVisitDate, Date tbDate) {
        TbFollowupRule tbFollowupRule = new TbFollowupRule(tbDate, lastVisitDate);
        CoreChwApplication.getInstance().getRulesEngineHelper().getTbRule(tbFollowupRule, CoreConstants.RULE_FILE.TB_FOLLOW_UP_VISIT);
        return tbFollowupRule;
    }

    public static HivFollowupRule getHivVisitStatus(Date lastVisitDate, Date tbDate) {
        HivFollowupRule hivFollowupRule = new HivFollowupRule(tbDate, lastVisitDate);
        CoreChwApplication.getInstance().getRulesEngineHelper().getHivRule(hivFollowupRule, CoreConstants.RULE_FILE.HIV_FOLLOW_UP_VISIT);
        return hivFollowupRule;
    }
}
