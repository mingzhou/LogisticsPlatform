package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.logistics.R;

/**
 * 主界面，可以添加不同的选项卡TabHost
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@SuppressWarnings("deprecation")
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

	@InjectView(R.id.main_tabhost)
	private TabHost tabHost;
	
	private LocalActivityManager mlam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = getResources();

		mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);

		tabHost.setup(mlam);
		
		TabSpec spec = null;
		
		spec = createTabSpec(tabHost, GoodActivity.TAG, res, R.string.goods_title, R.drawable.ic_tab_worldclock, GoodActivity.class);
		tabHost.addTab(spec);
		
		spec = createTabSpec(tabHost, ProfileActivity.TAG, res, R.string.profile_title, R.drawable.ic_tab_alarm, ProfileActivity.class);
		tabHost.addTab(spec);
		
		spec = createTabSpec(tabHost, MapActivity.TAG, res, R.string.map_title, R.drawable.ic_tab_more, MapActivity.class);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
	
	private TabSpec createTabSpec(TabHost tabHost, String tag,
            Resources res, int labelId, int iconId, Class<?> cls) {
		TabSpec spec = tabHost.newTabSpec(tag);
		String label = res.getString(labelId);
		Drawable icon = res.getDrawable(iconId);

		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab, null);
		((ImageView) linearLayout.findViewById(R.id.tab_icon)).setImageDrawable(icon);
		((TextView) linearLayout.findViewById(R.id.tab_label)).setText(label);
		spec.setIndicator(linearLayout);
		spec.setContent(new Intent().setClass(this, cls));

		return spec;

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mlam.dispatchResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mlam.dispatchPause(isFinishing());
	}
}
