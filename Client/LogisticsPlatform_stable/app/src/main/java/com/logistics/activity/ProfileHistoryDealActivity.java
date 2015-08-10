package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@ContentView(R.layout.activity_profile_history_deal)
public class ProfileHistoryDealActivity extends RoboActivity {

//	@InjectView(R.id.history_deal)
//	private TextView history_deal;
//	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.list_historydeal)
	private ListView listview;
	
	private ArrayAdapter<String[]> mAdapter;
	private ArrayList<String[]> items = new ArrayList<String[]>();
	private JSONArray mFav = new JSONArray();	
	//private JSONArray jFav = new JSONArray();
	
	/** 准备第一个模板，从字符串中提取出日期数字  */  
    private static String pat1 = "yyyy-MM-dd HH:mm:ss" ;    
    /** 准备第二个模板，将提取后的日期数字变为指定的格式*/    
    private static String pat2 = "yyyy年MM月dd日 HH:mm:ss" ;  
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;           
    @SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ; 
	
	
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
					intent.putExtra("position",mFav.length()-position-1);
					intent.putExtra("data",mFav.getJSONObject(mFav.length()-position-1).toString() );
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
		downFile();
		loadFile();
		
		mAdapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, items){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View view = super.getView(position, convertView, parent);
				String[] entry = items.get(position);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
			    text1.setText(entry[0]);
                text1.setTextColor(Color.parseColor("#44566d"));
			    text2.setText(entry[1]);
                text2.setTextColor(Color.parseColor("#818ea7"));
				return view;
			}
		};
		listview.setAdapter(mAdapter);
		try {
			//mAdapter.clear();
			for (int i =mFav.length()-1; i>=0;i--){
				long dt = mFav.getJSONObject(i).getJSONObject("datetime").getLong("$date");
				Date datetime = new Date(dt);
				sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
				String crawlTime = sdf1.format(datetime);
				String time = "更新时间:"+crawlTime;
				
				mAdapter.add(new String[]{mFav.getJSONObject(i).getString("from")+" -> " + mFav.getJSONObject(i).getString("to"),
						time});
	        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
	        }
			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	private String getTime(String crawlTime) {
//		// TODO Auto-generated method stub
//		//yyyy-MM-dd HH:mm:ss
//				long nt = System.currentTimeMillis();
//				Date nowtime = new Date(nt);
//				sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//				String curTime = sdf1.format(nowtime);
//				int yearC = Integer.valueOf(curTime.substring(0, 4));
//				int monthC = Integer.valueOf(curTime.substring(5, 7));
//				int dayC = Integer.valueOf(curTime.substring(8, 10));
//				int hourC = Integer.valueOf(curTime.substring(11, 13));
//				int minC = Integer.valueOf(curTime.substring(14, 16));
//				Log.d("time=cur",curTime);
//				Log.d("time",crawlTime);
//						
//				int yearT = Integer.valueOf(crawlTime.substring(0, 4));
//				int monthT = Integer.valueOf(crawlTime.substring(5, 7));
//				int dayT = Integer.valueOf(crawlTime.substring(8, 10));
//				int hourT = Integer.valueOf(crawlTime.substring(11, 13));
//				int minT = Integer.valueOf(crawlTime.substring(14, 16));
//				//int secT = Integer.valueOf(crawlTime.substring(8, 10));
//				
//				if(yearT != yearC || monthT!= monthC){
//					return crawlTime;
//				}else if(dayT != dayC){
//					if(dayC - dayT > 1){return crawlTime;}
//					else return "昨天";
//				}else if(hourC!=hourT){
//					if(hourC - hourT ==1 && minC< minT){
//					return  60+minC-minT+"分钟之前";
//				}
//					else return hourC-hourT-1+"小时之前";
//				}else if (minC-minT>1){
//					return minC-minT-1+"分钟之前";
//				}else return "刚刚";
//	}

	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=ProfileHistoryDealActivity.this.openFileInput("favorite.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        
        mFav = new JSONArray(stream.toString());
                
        stream.close();
        inStream.close();
	}
	
        
	public void downFile() throws IOException{
		FileOutputStream outStream=ProfileHistoryDealActivity.this.openFileOutput("favorite.txt",MODE_APPEND);
		outStream.write(new JSONArray().toString().getBytes());
		outStream.close();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			loadFile();
			mAdapter.clear();
			for (int i =mFav.length()-1; i>=0;i--){
				long dt = mFav.getJSONObject(i).getJSONObject("datetime").getLong("$date");
				Date datetime = new Date(dt);
				sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
				String crawlTime = sdf1.format(datetime);
                String time = "更新时间:"+crawlTime;
				
				mAdapter.add(new String[]{mFav.getJSONObject(i).getString("from")+" -> " + mFav.getJSONObject(i).getString("to"),
                        time});
	        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
	        }
			mAdapter.notifyDataSetChanged();			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
