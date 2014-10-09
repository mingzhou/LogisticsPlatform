package com.logistics.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends RoboActivity {

	public static final String TAG = SettingsActivity.class.getSimpleName();
	
	@InjectView(R.id.result_show)
	private TextView test;
	
	//private final String DUMMY = null;
	
	@Inject
	private AsyncHttpHelper httpHelper;
	
	@InjectView(R.id.list_goodresult)
	private ListView listview;
	
	private ArrayList<String> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		initComponent();
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				
			}});
	}
	
	private void initComponent(){
		
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
