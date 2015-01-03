package com.logistics.activity;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ProfilePushlistActivity extends RoboActivity {
	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.list_historydeal)
	private ListView listview;
	
	private ArrayAdapter<String[]> mAdapter;
	private ArrayList<String[]> items = new ArrayList<String[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_pushlist);
	}


}
