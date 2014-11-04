package com.logistics.activity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_result_detail);
		try {
			initComponent();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void initComponent() throws JSONException {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   String title = extras.getString("title");
		   final JSONObject jO = new JSONObject(extras.getString("data"));
		   goods_detail_title.setText(title);
		   long deadline = jO.getJSONObject("deadline").getLong("$date");
		   String deadlineTime = DateFormat.getDateFormat(GoodResultDetailActivity.this).format(new Date(deadline));;
		   goods_detail_info.setText("出发地："+jO.getString("from")+"\n"
				   +"到达地："+jO.getString("to")+"\n"
				   +"截止时间:"+deadlineTime+"\n");
		   source_detail_info.setText("来源："+jO.getString("site")+"\n");
		   source_detail_info.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					try {
						Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(jO.getString("url")));
						it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");  
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
		
		submit_order.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//                intent.setClass(GoodResultDetailActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
				onDestroy();
			}});
		
		
	}
		

}
