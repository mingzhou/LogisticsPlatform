package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@ContentView(R.layout.activity_profile_history_deal_detail)
public class ProfileHistoryDealDetailActivity extends RoboActivity {
	
	@InjectView(R.id.goods_detail_title)
	private TextView goods_detail_title;
	
	@InjectView(R.id.goods_detail_info)
	private TextView goods_detail_info;
	
	@InjectView(R.id.source_detail_info)
	private TextView source_detail_info;
		
	@InjectView(R.id.return_btn)
	private TextView return_btn;
	
	@InjectView(R.id.quit_favorite)
	private Button quit_favorite;
	
	private String title = null;
	private int position = 0;
	private JSONObject jO = new JSONObject();
	private JSONArray mFav = new JSONArray();	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_history_deal_detail);
		try {
			initComponent();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean getExtras() throws JSONException{
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			title = extras.getString("title");
			position = extras.getInt("position");
			jO = new JSONObject(extras.getString("data"));
			return true;
		}else{
			return false;
		}
	}


	private void initComponent() throws JSONException {
		// TODO Auto-generated method stub
		return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
                onDestroy();
			}});
		
		if(getExtras()){
			goods_detail_title.setText(title);
			long deadline = jO.getJSONObject("deadline").getLong("$date");
			   String deadlineTime = DateFormat.getDateFormat(ProfileHistoryDealDetailActivity.this).format(new Date(deadline));;
			   goods_detail_info.setText("出发地："+jO.getString("from")+"\n"
					   +"到达地："+jO.getString("to")+"\n"
					   +"截止时间:"+deadlineTime+"\n"
					   +"货物名称:"+jO.getString("title")+"\n"
					   +"货物类型:"+jO.getString("type")+"\n"
					   +"货物体积:"+jO.getString("volume")+"\n"
					   +"货物质量:"+jO.getString("quality")+"\n"
					   +"包装方式:"+jO.getString("packing")+"\n"
					   +"货车类型:"+jO.getString("vehicle")+"\n"
					   +"货车长度:"+jO.getString("length")+"\n"
					   +"预计费用:"+jO.getString("fee")+"\n"
					   +"备注:"+jO.getString("others")+"\n");
			   SpannableString siteString = new SpannableString("来源："+jO.getString("site"));
			   siteString.setSpan(new UnderlineSpan(), 3, siteString.length(), 0);
			   siteString.setSpan(new StyleSpan(Typeface.ITALIC), 3, siteString.length(), 0);
			   source_detail_info.setText(siteString);
			   
			   source_detail_info.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						try {
							Intent it = new Intent();
							it.setClass(ProfileHistoryDealDetailActivity.this,WebDisplayActivity.class);
							it.putExtra("data", jO.getString("url"));
							startActivity(it);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 				         
					}
					   
				   });
			}
		
		quit_favorite.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					loadFile();
					JSONArray tmp = new JSONArray();  
					if(getExtras()){
						int len = mFav.length();
						for(int i=0;i<len;i++){
							if (i != position) 
					        {
								tmp.put(mFav.get(i));
					        }
						}
						
					};
					downFile(tmp);
					quit_favorite.setEnabled(false);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
                onDestroy();
			}});
		
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=ProfileHistoryDealDetailActivity.this.openFileInput("favorite.txt");
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
	
	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=ProfileHistoryDealDetailActivity.this.openFileOutput("favorite.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d("fav","write done");
	}
	

}
