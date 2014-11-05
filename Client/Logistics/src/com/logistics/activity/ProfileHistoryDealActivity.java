package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private JSONObject mFav = new JSONObject();	
	private JSONArray jFav = new JSONArray();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_history_deal);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			initComponent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ProfileHistoryDealActivity.this,ProfileHistoryDealDetailActivity.class);
				
				
				try {
					intent.putExtra("title","信息详情");
					intent.putExtra("data",jFav.getJSONObject(position).toString() );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.d(TAG+"data",jArray.getJSONObject(position).toString());
				startActivity(intent);
				onPause();
			}
			

		});
		
	}

	private void initComponent() throws IOException, JSONException {
		// TODO Auto-generated method stub
		return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                finish();
                onDestroy();
			}});
		
		loadFile();
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
		listview.setAdapter(mAdapter);
		
		
		Iterator<?> it = mFav.keys();
		while(it.hasNext()){
			JSONObject jO = new JSONObject(mFav.get(it.next().toString()).toString());
			jFav.put(jO);
			//mAdapter.add(jO.getString("from")+" -> " +jO.getString("to"));
		}
		
		for(int i =0; i<jFav.length();i++){
			mAdapter.add(jFav.getJSONObject(i).getString("from")+" -> " + jFav.getJSONObject(i).getString("to"));
		}
		mAdapter.notifyDataSetChanged();
		
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=ProfileHistoryDealActivity.this.openFileInput("favorite.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        
        mFav = new JSONObject(stream.toString());
                
        stream.close();
        inStream.close();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}
        

}
