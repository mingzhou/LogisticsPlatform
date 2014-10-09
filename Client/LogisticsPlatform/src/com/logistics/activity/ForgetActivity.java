package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgetActivity extends RoboActivity {
	
	@InjectView(R.id.phone)
	private EditText phone;
	//
	
	@InjectView(R.id.set_password)
	private EditText set_password;
	
	@InjectView(R.id.submit)
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		initComponent();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ForgetActivity.this,LoginDetailActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
	}

	

}
