package com.logistics.activity;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_good_result)
public class GoodResultActivity extends RoboActivity {

	@InjectView(R.id.result_show)
	private TextView test;
	
	//private final String DUMMY = null;
		
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.list_goodresult)
	private ListView listview;
	private JSONArray jArray = new JSONArray();
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_result);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		initComponent();
//		Bundle b = getBundle();
//		getHttpResponse(b);
		
		
//		listview.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}});
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(GoodResultActivity.this,GoodResultDetailActivity.class);
				
				try {
					intent.putExtra("from", jArray.getJSONObject(position).getString("from"));
					intent.putExtra("to", jArray.getJSONObject(position).getString("to"));
					intent.putExtra("site", jArray.getJSONObject(position).getString("site"));
					//intent.putExtra("url", jArray.getJSONObject(position-1).getString("url"));
					long deadline = jArray.getJSONObject(position).getJSONObject("deadline").getLong("$date");
					intent.putExtra("deadline", DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(deadline)));
					intent.putExtra("title","信息详情");
					//Log.d(TAG+"data",jArray.getJSONObject(position).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
				onPause();
			}
			

		});
		
		
	}
	
	
	
	private void initComponent(){
		//return 
		return_btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				onDestroy();
			}});
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
		listview.setAdapter(mAdapter);
		Intent intent =getIntent(); 
		String jS = intent.getStringExtra("query");
		Log.d("nihao-js",jS);
		try {
			jArray = new JSONArray(jS);
			for (int i =0; i<jArray.length();i++){
				//long datetime = jArray.getJSONObject(i).getLong("$date");
	        mAdapter.add(jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"));
	        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
	        }
			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	
}
