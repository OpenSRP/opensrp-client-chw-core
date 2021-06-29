package org.smartregister.chw.core.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.chw.core.model.NavigationOption;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class NavigationAdapterTest {
    private List<NavigationOption> navigationOptionList;
    @Mock
    private Activity context;
    @Mock
    private Resources mockContextResources;
    private Map<String, Class> registeredActivities;
    @Mock
    private NavigationAdapterHost host;
    private NavigationAdapter navigationAdapter;

    @Mock
    private NavigationAdapter.MyViewHolder myViewHolder;
    @Mock
    private LayoutInflater inflater;
    private int layoutId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(context.getResources()).thenReturn(mockContextResources);
        registeredActivities = new HashMap<>();
        registeredActivities.put("AnyActivity", AppCompatActivity.class);
        NavigationOption navigationOption1 = new NavigationOption(10,11, 12, "opt1", 100);
        NavigationOption navigationOption2 = new NavigationOption(20,21, 22, "opt2", 200);

        navigationOptionList = Arrays.asList(navigationOption1, navigationOption2);

        navigationAdapter = new NavigationAdapter(navigationOptionList,context,registeredActivities, host);
    }
    @Test
    public void onCreateViewHolder() {
        Assert.assertNotNull(myViewHolder);
    }

    @Test
    public void testOnBindViewHolder() {
        TextView tvName = Mockito.mock(TextView.class);
        TextView tvCount = Mockito.mock(TextView.class);
        ImageView ivIcon = Mockito.mock(ImageView.class);

        ReflectionHelpers.setField(myViewHolder, "tvName", tvName);
        ReflectionHelpers.setField(myViewHolder, "tvCount", tvCount);
        ReflectionHelpers.setField(myViewHolder, "ivIcon", ivIcon);

        // intercept contextual objects and provide mocks
        TextView tvContent = Mockito.mock(TextView.class);
        View view = Mockito.mock(View.class);
        Mockito.doReturn(tvContent).when(view).findViewById(Mockito.anyInt());
        Mockito.doReturn(view).when(inflater).inflate(Mockito.anyInt(), Mockito.any());
        Mockito.doReturn(view).when(myViewHolder).getView();
       when(host.getSelectedView()).thenReturn("opt1");
        // add state
        ReflectionHelpers.setField(navigationAdapter, "navigationOptionList", navigationOptionList);


        navigationAdapter.onBindViewHolder(myViewHolder, 0);

        Mockito.verify(tvName, Mockito.atLeast(1)).setText(Mockito.any());
        Mockito.verify(tvCount, Mockito.atLeast(1)).setText(Mockito.any());

    }

    @Test
    public void testGetItemCount() {
        navigationAdapter.getItemCount();
        Assert.assertEquals(2, navigationOptionList.size());
    }

    public static class MockActivity extends Activity {
    }

}
