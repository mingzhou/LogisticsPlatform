package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import com.logistics.R;
import android.os.Bundle;
import android.view.Menu;

@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity {

	public static final String TAG = MapActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	

}
