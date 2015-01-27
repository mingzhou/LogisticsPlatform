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

@ContentView(R.layout.activity_good_result_detail)
public class GoodResultDetailActivity extends RoboActivity {

	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(R.id.submit_order)
	private Button submit_order;
	
	@InjectView(R.id.goods_detail_title)
	private TextView goods_detail_title;
	
	@InjectView(R.id.goods_detail_info)
	private TextView goods_detail_info;
	
	@InjectView(R.id.source_detail_info)
	private TextView source_detail_info;

	private String title = null;
	private JSONObject jO = new JSONObject();
	private JSONArray mFav = new JSONArray();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_result_detail);
		try {
			initComponent();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean getExtras() throws JSONException{
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			title = extras.getString("title");
			jO = new JSONObject(extras.getString("data"));
			return true;
		}else{
			return false;
		}
	}

	private void initComponent() throws JSONException, IOException {
		// TODO Auto-generated method stub
		
		  if (getExtras()) {
		   goods_detail_title.setText(title);
		   long deadline = jO.getJSONObject("deadline").getLong("$date");
		   String deadlineTime = DateFormat.getDateFormat(GoodResultDetailActivity.this).format(new Date(deadline));;
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
						it.setClass(GoodResultDetailActivity.this,WebDisplayActivity.class);
						it.putExtra("data", jO.getString("url"));
						startActivity(it);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
			         
				}
				   
			   });
		  }      
		
		return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
                onDestroy();
			}});
		
		downFile();
		loadFile();
		Log.d("fav",Integer.toString(mFav.length()));
		if(has(mFav,jO)){
			submit_order.setEnabled(false);
			submit_order.setText("已收藏");
			Log.d("fav-has",mFav.toString());
		}
		else{
		submit_order.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mFav.put(jO);
					submit_order.setEnabled(false);
					submit_order.setText("已收藏");
					downFile(mFav);
					 Log.d("fav",Integer.toString(mFav.length()));
					 Log.d("fav",mFav.toString());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});}
	}
		
	private boolean has(JSONArray jAr, JSONObject jOb) throws JSONException {
		// TODO Auto-generated method stub
		for(int i=0 ;i<jAr.length();i++){
			if(jOb.getJSONObject("_id").getString("$oid").
					equals(jAr.getJSONObject(i).getJSONObject("_id").getString("$oid"))){
				return true;
				}
		}
		return false;
	}

	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=GoodResultDetailActivity.this.openFileOutput("favorite.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d("fav","write done");
	}
	
	public void downFile() throws IOException{
		FileOutputStream outStream=GoodResultDetailActivity.this.openFileOutput("favorite.txt",MODE_APPEND);
		outStream.write(new JSONArray().toString().getBytes());
		outStream.close();
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=GoodResultDetailActivity.this.openFileInput("favorite.txt");
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
	
}
