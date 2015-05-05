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
import roboguice.inject.InjectView;

import com.logistics.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProfilePushlistActivity extends RoboActivity {
	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.list_historydeal)
	private ListView listview;
	
	private JSONArray jArray = new JSONArray();
	
	private ArrayAdapter<String[]> mAdapter;
	private ArrayList<String[]> items = new ArrayList<String[]>();
	
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
		setContentView(R.layout.activity_profile_pushlist);
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
			    text2.setText(entry[1]);
				return view;
			}
		};
		listview.setAdapter(mAdapter);
		try {
			
			for (int i =jArray.length()-1; i>=0;i--){
				String dt = jArray.getJSONObject(i).getString("time");
				
				//String time = getTime(crawlTime);
				
				mAdapter.add(new String[]{ "更新"+Integer.toString(jArray.getJSONObject(i).getInt("num"))+"货运信息" ,
						dt});
	        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
	        }
			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=ProfilePushlistActivity.this.openFileInput("push.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        
        jArray = new JSONArray(stream.toString());
                
        stream.close();
        inStream.close();
	}
	
        
	public void downFile() throws IOException{
		FileOutputStream outStream=ProfilePushlistActivity.this.openFileOutput("push.txt",MODE_APPEND);
		outStream.write(new JSONArray().toString().getBytes());
		outStream.close();
	}


}
