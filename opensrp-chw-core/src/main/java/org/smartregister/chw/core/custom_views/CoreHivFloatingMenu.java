package org.smartregister.chw.core.custom_views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.core.R;
import org.smartregister.chw.core.listener.OnClickFloatingMenu;
import org.smartregister.chw.hiv.custom_views.BaseHivFloatingMenu;
import org.smartregister.chw.hiv.domain.HivMemberObject;
import org.smartregister.chw.hiv.fragment.BaseHivClientCallDialogFragment;
import org.smartregister.chw.hiv.util.HivUtil;

import static org.smartregister.chw.core.utils.Utils.redrawWithOption;

public abstract class CoreHivFloatingMenu extends BaseHivFloatingMenu {
    public FloatingActionButton fab;
    protected View referLayout;
    protected View registerHivIndexClientsLayout;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation rotateForward;
    private Animation rotateBack;
    private View callLayout;
    private RelativeLayout activityMain;
    private boolean isFabMenuOpen = false;
    private LinearLayout menuBar;
    private OnClickFloatingMenu onClickFloatingMenu;

    public CoreHivFloatingMenu(Context context, HivMemberObject hivMemberObject) {
        super(context, hivMemberObject);
    }

    public void setFloatMenuClickListener(OnClickFloatingMenu onClickFloatingMenu) {
        this.onClickFloatingMenu = onClickFloatingMenu;
    }

    @Override
    public void initUi() {
        inflate(getContext(), R.layout.view_hiv_call_client_floating_menu, this);

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBack = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_back);

        activityMain = findViewById(R.id.activity_main);
        menuBar = findViewById(R.id.menu_bar);

        fab = findViewById(R.id.hiv_fab);
        fab.setOnClickListener(this);

        callLayout = findViewById(R.id.call_layout);
        callLayout.setOnClickListener(this);
        callLayout.setClickable(false);

        referLayout = findViewById(R.id.refer_to_facility_layout);
        referLayout.setOnClickListener(this);
        referLayout.setClickable(false);

        registerHivIndexClientsLayout = findViewById(R.id.register_index_clients_layout);
        registerHivIndexClientsLayout.setOnClickListener(this);
        registerHivIndexClientsLayout.setClickable(false);

        menuBar.setVisibility(GONE);

    }

    @Override
    public void onClick(View view) {
        onClickFloatingMenu.onClickMenu(view.getId());
    }

    public void animateFAB() {
        if (menuBar.getVisibility() == GONE) {
            menuBar.setVisibility(VISIBLE);
        }

        if (isFabMenuOpen) {
            activityMain.setBackgroundResource(R.color.transparent);
            fab.startAnimation(rotateBack);
            fab.setImageResource(R.drawable.ic_edit_white);

            callLayout.startAnimation(fabClose);
            callLayout.setClickable(false);

            referLayout.startAnimation(fabClose);
            referLayout.setClickable(false);

            registerHivIndexClientsLayout.startAnimation(fabClose);
            registerHivIndexClientsLayout.setClickable(false);

            isFabMenuOpen = false;
        } else {
            activityMain.setBackgroundResource(R.color.grey_tranparent_50);
            fab.startAnimation(rotateForward);
            fab.setImageResource(R.drawable.ic_input_add);

            callLayout.startAnimation(fabOpen);
            callLayout.setClickable(true);

            referLayout.startAnimation(fabOpen);
            referLayout.setClickable(true);

            registerHivIndexClientsLayout.startAnimation(fabOpen);
            registerHivIndexClientsLayout.setClickable(true);

            isFabMenuOpen = true;
        }
    }

    public void launchCallWidget() {
        BaseHivClientCallDialogFragment.Companion.launchDialog((Activity) this.getContext(), HivUtil.getFullName(this.getHivMemberObject()),
                this.getHivMemberObject().getPhoneNumber(), this.getHivMemberObject().getPrimaryCareGiver(), this.getHivMemberObject().getPrimaryCareGiverPhoneNumber());
    }

    public void redraw(boolean hasPhoneNumber) {
        redrawWithOption(this, hasPhoneNumber);
    }

    public View getCallLayout() {
        return callLayout;
    }
}
