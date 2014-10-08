package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginDetailActivity extends RoboActivity {
	
	
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.password)
	private EditText password;
	
	@InjectView(R.id.login)
	private Button login;
	
	@InjectView(R.id.forget)
	private Button forget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_detail);
		initComponent();
		
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		login.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginDetailActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
		
		forget.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginDetailActivity.this,ForgetActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
	}


}
