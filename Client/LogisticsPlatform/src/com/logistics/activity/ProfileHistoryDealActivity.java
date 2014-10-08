package com.logistics.activity;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@ContentView(R.layout.activity_profile_history_deal)
public class ProfileHistoryDealActivity extends RoboActivity {

	@InjectView(R.id.history_deal)
	private TextView history_deal;
	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.list_historydeal)
	private ListView listview;
	
	private ArrayList<String> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_history_deal);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initComponent();
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ProfileHistoryDealActivity.this,ProfileHistoryDealDetailActivity.class);
				startActivity(intent);
				onPause();
				
			}});
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                finish();
                onDestroy();
			}});
		list = getData();
		
		listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,list));
	}

	private ArrayList<String> getData() {
		// TODO Auto-generated method stub
		ArrayList<String> data = new ArrayList<String>();
		data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");
        data.add("测试数据5");
        data.add("测试数据6");
        data.add("测试数据7");
        data.add("测试数据8");
              
         
        return data;
	}

	

}
