package org.smartregister.chw.core.provider;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.rule.MalariaFollowUpRule;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.MalariaVisitUtil;
import org.smartregister.chw.malaria.provider.MalariaRegisterProvider;
import org.smartregister.chw.malaria.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import timber.log.Timber;

public class ChwMalariaRegisterProvider extends MalariaRegisterProvider {

    private Context context;

    public ChwMalariaRegisterProvider(Context context, View.OnClickListener paginationClickListener,
                                      View.OnClickListener onClickListener, Set visibleColumns) {
        super(context, paginationClickListener, onClickListener, visibleColumns);
        this.context = context;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, RegisterViewHolder viewHolder) {
        super.getView(cursor, client, viewHolder);

        viewHolder.dueButton.setVisibility(View.GONE);
        viewHolder.dueButton.setOnClickListener(null);
        CommonPersonObjectClient pc = (CommonPersonObjectClient) client;
        Utils.startAsyncTask(new UpdateAsyncTask(viewHolder, pc), null);
    }

    private void updateDueColumn(Button dueButton, String followStatus) {
        dueButton.setVisibility(View.VISIBLE);
        dueButton.setOnClickListener(onClickListener);
        if (CoreConstants.VISIT_STATE.OVERDUE.equalsIgnoreCase(followStatus)) {
            dueButton.setTextColor(context.getResources().getColor(R.color.white));
            dueButton.setBackgroundResource(R.drawable.overdue_red_btn_selector);
        }
        if (CoreConstants.VISIT_STATE.DUE.equalsIgnoreCase(followStatus)) {
            dueButton.setTextColor(context.getResources().getColor(R.color.alert_in_progress_blue));
            dueButton.setBackgroundResource(R.drawable.blue_btn_selector);
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        private final RegisterViewHolder viewHolder;
        private final CommonPersonObjectClient pc;
        private MalariaFollowUpRule malariaFollowUpRule;

        private UpdateAsyncTask(RegisterViewHolder viewHolder, CommonPersonObjectClient pc) {
            this.viewHolder = viewHolder;
            this.pc = pc;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Date date = new SimpleDateFormat(CoreConstants.DATE_FORMATS.NATIVE_FORMS, Locale.getDefault()).parse(Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.MALARIA_TEST_DATE, false));
                malariaFollowUpRule = MalariaVisitUtil.getMalariaStatus(date);
            } catch (ParseException e) {
                Timber.e(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (malariaFollowUpRule != null && StringUtils.isNotBlank(malariaFollowUpRule.getButtonStatus()) &&
                    !CoreConstants.VISIT_STATE.EXPIRED.equalsIgnoreCase(malariaFollowUpRule.getButtonStatus())) {
                updateDueColumn(viewHolder.dueButton, malariaFollowUpRule.getButtonStatus());
            }
        }
    }
}
