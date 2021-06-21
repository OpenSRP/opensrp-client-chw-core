package org.smartregister.chw.core.custom_views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.style.FadingCircle;

import org.apache.commons.lang3.tuple.Pair;
import org.smartregister.chw.core.R;
import org.smartregister.chw.core.activity.ChwP2pModeSelectActivity;
import org.smartregister.chw.core.activity.CoreCommunityRespondersRegisterActivity;
import org.smartregister.chw.core.activity.CoreStockInventoryReportActivity;
import org.smartregister.chw.core.adapter.NavigationAdapter;
import org.smartregister.chw.core.adapter.NavigationAdapterHost;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.contract.NavigationContract;
import org.smartregister.chw.core.model.NavigationModel;
import org.smartregister.chw.core.model.NavigationOption;
import org.smartregister.chw.core.presenter.NavigationPresenter;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.domain.FetchStatus;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.util.LangUtils;
import org.smartregister.util.PermissionUtils;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class NavigationMenu implements NavigationContract.View, SyncStatusBroadcastReceiver.SyncStatusListener, DrawerLayout.DrawerListener , NavigationAdapterHost {
    private static NavigationMenu instance;
    private static WeakReference<Activity> activityWeakReference;
    private static CoreChwApplication application;
    private static NavigationMenu.Flavour menuFlavor;
    private static NavigationModel.Flavor modelFlavor;
    private static Map<String, Class> registeredActivities;
    private static boolean showDeviceToDeviceSync = true;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationAdapter navigationAdapter;
    private RecyclerView recyclerView;
    private TextView tvLogout;
    private View rootView = null;
    private ImageView ivSync;
    private ProgressBar syncProgressBar;
    private NavigationContract.Presenter mPresenter;
    private View parentView;
    private Timer timer;
    private static String selectedView = CoreConstants.DrawerMenu.ALL_FAMILIES;

    public static void setupNavigationMenu(CoreChwApplication application, NavigationMenu.Flavour menuFlavor,
                                           NavigationModel.Flavor modelFlavor, Map<String, Class> registeredActivities, boolean showDeviceToDeviceSync) {
        NavigationMenu.application = application;
        NavigationMenu.menuFlavor = menuFlavor;
        NavigationMenu.modelFlavor = modelFlavor;
        NavigationMenu.registeredActivities = registeredActivities;
        NavigationMenu.showDeviceToDeviceSync = showDeviceToDeviceSync;
    }

    @Nullable
    public static NavigationMenu getInstance(Activity activity, View parentView, Toolbar myToolbar) {
        try {
            SyncStatusBroadcastReceiver.getInstance().removeSyncStatusListener(instance);
            activityWeakReference = new WeakReference<>(activity);
            int orientation = activity.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (instance == null) {
                    instance = new NavigationMenu();
                }

                SyncStatusBroadcastReceiver.getInstance().addSyncStatusListener(instance);
                instance.init(activity, parentView, myToolbar);
                return instance;
            } else {
                return null;
            }
        } catch (OutOfMemoryError e) {
            Timber.e(e);
        }

        return null;
    }

    private void init(Activity activity, View myParentView, Toolbar myToolbar) {
        // parentActivity = activity;
        try {
            setParentView(activity, parentView);
            toolbar = myToolbar;
            parentView = myParentView;
            mPresenter = new NavigationPresenter(application, this, modelFlavor);
            prepareViews(activity);
            mPresenter.updateTableMap(menuFlavor.getTableMapValues());
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void setParentView(Activity activity, View parentView) {
        if (parentView != null) {
            rootView = parentView;
        } else {
            ViewGroup current = (ViewGroup) ((ViewGroup) (activity.findViewById(android.R.id.content))).getChildAt(0);
            if (!(current instanceof DrawerLayout)) {

                if (current.getParent() != null) {
                    ((ViewGroup) current.getParent()).removeView(current); // <- fix
                }

                // swap content view
                LayoutInflater mInflater = LayoutInflater.from(activity);
                ViewGroup contentView = (ViewGroup) mInflater.inflate(R.layout.activity_base, null);
                activity.setContentView(contentView);

                rootView = activity.findViewById(R.id.nav_view);
                RelativeLayout rl = activity.findViewById(R.id.nav_content);

                if (current.getParent() != null) {
                    ((ViewGroup) current.getParent()).removeView(current); // <- fix
                }

                if (current instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    current.setLayoutParams(params);
                    rl.addView(current);
                } else {
                    rl.addView(current);
                }
            } else {
                rootView = current;
            }
        }
    }

    @Override
    public void prepareViews(Activity activity) {

        drawer = activity.findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);
        recyclerView = rootView.findViewById(R.id.rvOptions);
        tvLogout = rootView.findViewById(R.id.tvLogout);
        recyclerView = rootView.findViewById(R.id.rvOptions);
        ivSync = rootView.findViewById(R.id.ivSyncIcon);
        syncProgressBar = rootView.findViewById(R.id.pbSync);

        ImageView ivLogo = rootView.findViewById(R.id.ivLogo);
        ivLogo.setContentDescription(activity.getString(R.string.nav_logo));
        ivLogo.setImageResource(R.drawable.ic_logo);

        TextView tvLogo = rootView.findViewById(R.id.tvLogo);
        tvLogo.setText(activity.getString(R.string.nav_logo));

        if (syncProgressBar != null) {
            FadingCircle circle = new FadingCircle();
            syncProgressBar.setIndeterminateDrawable(circle);
        }

        // register top section menu items
        registerDrawer(activity);
        registerNavigation(activity);

        /// register bottom section menu items after the separator
        registerLanguageSwitcher(activity);
        registerServiceActivity(activity);
        registerStockReport(activity);
        registerCommunityResponders(activity);
        registerDeviceToDeviceSync(activity);
        registerSync(activity);
        registerLogout(activity);

        // update all actions
        mPresenter.refreshLastSync();
        mPresenter.refreshNavigationCount(activity);
    }

    @Override
    public void refreshLastSync(Date lastSync) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa, dd MMM yyyy", Locale.getDefault());
        if (rootView != null) {
            TextView tvLastSyncTime = rootView.findViewById(R.id.tvSyncTime);
            if (lastSync != null) {
                tvLastSyncTime.setVisibility(View.VISIBLE);
                tvLastSyncTime.setText(MessageFormat.format(" {0}", sdf.format(lastSync)));
            } else {
                tvLastSyncTime.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void refreshCurrentUser(String name) {
        if (tvLogout != null && activityWeakReference.get() != null) {
            tvLogout.setText(String.format("%s %s", activityWeakReference.get().getResources().getString(R.string.log_out_as), name));
        }
    }

    @Override
    public void logout(Activity activity) {
        Toast.makeText(activity.getApplicationContext(), activity.getResources().getText(R.string.action_log_out), Toast.LENGTH_SHORT).show();
        application.logoutCurrentUser();
    }

    @Override
    public void refreshCount() {
        navigationAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void registerServiceActivity(Activity activity) {
        if (menuFlavor.hasServiceReport()) {
            View rlIconServiceReport = rootView.findViewById(R.id.rlServiceReport);
            rlIconServiceReport.setVisibility(View.VISIBLE);
            rlIconServiceReport.setOnClickListener(view -> {
                activity.startActivity( menuFlavor.getHIA2ReportActivityIntent(activity));
            });
        }
    }

    private void registerStockReport(Activity activity) {
        if (menuFlavor.hasStockReport()) {
            View rlIconStockReport = rootView.findViewById(org.smartregister.chw.core.R.id.rlIconStockReport);
            rlIconStockReport.setVisibility(View.VISIBLE);
            rlIconStockReport.setOnClickListener(view -> {
                Intent intent = new Intent(activity, CoreStockInventoryReportActivity.class);
                activity.startActivity(intent);
            });
        }
    }

    private void registerCommunityResponders(Activity activity) {
        if (menuFlavor.hasCommunityResponders()) {
            View rlIconResponders = rootView.findViewById(org.smartregister.chw.core.R.id.rlIconResponders);
            rlIconResponders.setVisibility(View.VISIBLE);
            rlIconResponders.setOnClickListener(view -> {
                Intent intent = new Intent(activity, CoreCommunityRespondersRegisterActivity.class);
                activity.startActivity(intent);
            });
        }
    }

    private void registerDrawer(Activity parentActivity) {
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    parentActivity, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        }
    }

    private void registerNavigation(Activity parentActivity) {
        if (recyclerView != null) {

            List<NavigationOption> navigationOptions = mPresenter.getOptions();
            navigationAdapter = new NavigationAdapter(navigationOptions, parentActivity, registeredActivities, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(parentActivity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(navigationAdapter);
        }
    }

    private void registerLogout(final Activity parentActivity) {
        mPresenter.displayCurrentUser();
        tvLogout.setOnClickListener(v -> logout(parentActivity));
    }

    private void registerSync(final Activity parentActivity) {

        TextView tvSync = rootView.findViewById(R.id.tvSync);
        ivSync = rootView.findViewById(R.id.ivSyncIcon);
        syncProgressBar = rootView.findViewById(R.id.pbSync);

        View.OnClickListener syncClicker = v -> {
            Toast.makeText(parentActivity, parentActivity.getResources().getText(R.string.action_start_sync), Toast.LENGTH_SHORT).show();
            mPresenter.sync(parentActivity);
        };


        tvSync.setOnClickListener(syncClicker);
        ivSync.setOnClickListener(syncClicker);

        refreshSyncProgressSpinner();
    }

    private void registerLanguageSwitcher(final Activity context) {

        View rlIconLang = rootView.findViewById(R.id.rlIconLang);
        final TextView tvLang = rootView.findViewById(R.id.tvLang);

        final List<Pair<String, Locale>> locales = menuFlavor.getSupportedLanguages();

        String[] languages = new String[locales.size()];
        Locale current = context.getResources().getConfiguration().locale;
        int x = 0;
        while (x < locales.size()) {
            languages[x] = locales.get(x).getKey();
            if (current.getLanguage().equals(locales.get(x).getValue().getLanguage())) {
                tvLang.setText(locales.get(x).getKey());
            }
            x++;
        }

        rlIconLang.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.choose_language));
            builder.setItems(languages, (dialog, which) -> {
                Pair<String, Locale> lang = locales.get(which);
                tvLang.setText(lang.getLeft());
                LangUtils.saveLanguage(context.getApplication(), lang.getValue().getLanguage());
                CoreChwApplication.getInstance().persistLanguage(lang.getValue().getLanguage());

                // destroy current instance
                drawer.closeDrawers();
                instance = null;
                Intent intent = context.getIntent();
                context.finish();
                context.startActivity(intent);
                application.notifyAppContextChange();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void registerDeviceToDeviceSync(@NonNull final Activity activity) {
        if (!showDeviceToDeviceSync) {
            rootView.findViewById(R.id.rlIconDevice).setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.rlIconDevice)
                .setOnClickListener(v -> startP2PActivity(activity));
    }

    private void refreshSyncProgressSpinner() {
        if (syncProgressBar == null || ivSync == null)
            return;

        if (SyncStatusBroadcastReceiver.getInstance().isSyncing()) {
            syncProgressBar.setVisibility(View.VISIBLE);
            ivSync.setVisibility(View.INVISIBLE);
        } else {
            syncProgressBar.setVisibility(View.INVISIBLE);
            ivSync.setVisibility(View.VISIBLE);
        }
    }

    public void startP2PActivity(@NonNull Activity activity) {
        if (PermissionUtils.isPermissionGranted(activity
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                , CoreConstants.RQ_CODE.STORAGE_PERMISIONS)) {
            activity.startActivity(new Intent(activity, ChwP2pModeSelectActivity.class));
        }
    }

    public NavigationAdapter getNavigationAdapter() {
        return navigationAdapter;
    }

    public void lockDrawer(Activity activity) {
        prepareViews(activity);
        if (drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public boolean onBackPressed() {
        boolean res = false;
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            res = true;
        }
        return res;
    }

    @Override
    public void onSyncStart() {
        // set the sync icon to be a rotating menu
        refreshSyncProgressSpinner();
    }

    @Override
    public void onSyncInProgress(FetchStatus fetchStatus) {
        Timber.v("onSyncInProgress");
    }

    @Override
    public void onSyncComplete(FetchStatus fetchStatus) {
        // hide the rotating menu
        refreshSyncProgressSpinner();
        // update the time
        mPresenter.refreshLastSync();
        // refreshLastSync(new Date());
        if (activityWeakReference.get() != null && !activityWeakReference.get().isDestroyed()) {
            mPresenter.refreshNavigationCount(activityWeakReference.get());
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        Timber.v("Drawer is sliding");
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        Timber.v("Drawer is opened");
        recomputeCounts();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        Timber.v("Drawer is closed");
        if (timer != null)
            timer = null;
    }


    @Override
    public void onDrawerStateChanged(int newState) {
        Timber.v("Drawer state is changed");
    }

    private void recomputeCounts() {
        try {
            if (timer == null) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Activity activity = activityWeakReference.get();
                        if (mPresenter != null && activity != null) {
                            mPresenter.refreshNavigationCount(activity);
                        }
                    }
                }, 0, 5000);
            }
        } catch (Exception e) {
            Timber.v(e);
        }
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    public String getSelectedView() {
        return NavigationMenu.selectedView;
    }

    @Override
    public void setSelectedView(String selectedView) {
        NavigationMenu.selectedView = selectedView;
    }
  
    public static String getChildNavigationCountString(){
       return menuFlavor.childNavigationMenuCountString();
    }

    public interface Flavour {
        List<Pair<String, Locale>> getSupportedLanguages();

        HashMap<String, String> getTableMapValues();

        boolean hasServiceReport();

        boolean hasStockReport();

        boolean hasCommunityResponders();

        Intent getStockReportIntent(Activity activity);

        Intent getServiceReportIntent(Activity activity);

        String childNavigationMenuCountString();

        Intent getHIA2ReportActivityIntent(Activity activity);
    }
}
