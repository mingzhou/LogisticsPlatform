package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
		initComponent();
	}


	private void initComponent() {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   String from= extras.getString("from");
		   String to= extras.getString("to");
		   String site= extras.getString("site");
		   //String url= extras.getString("url");
		   String deadline= extras.getString("deadline");
		   String title = extras.getString("title");
		   Log.d("nihao-time","deadline");
		   goods_detail_title.setText(title);
		   goods_detail_info.setText("出发地："+from+"\n"
				   +"到达地："+to+"\n"
				   +"截止时间:"+deadline+"\n");
		   source_detail_info.setText("来源："+site+"\n");
				  // +"URL:"+url);
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
