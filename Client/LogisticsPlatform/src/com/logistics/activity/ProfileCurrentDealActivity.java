package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import com.logistics.R;
import android.os.Bundle;
import android.view.Menu;

@ContentView(R.layout.activity_profile_current_deal)
public class ProfileCurrentDealActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_current_deal);
	}

	

}
