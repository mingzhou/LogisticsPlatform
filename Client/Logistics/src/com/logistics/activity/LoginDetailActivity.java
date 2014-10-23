package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginDetailActivity extends RoboActivity {
	
	
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.password)
	private EditText password;
	
	@InjectView(R.id.login)
	private Button login;
	
	@InjectView(R.id.forget)
	private Button forget;
	
	public final static int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1;
	private SharedPreferences sharedPreferences;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_detail);
		initComponent();
		
	}

	private void initComponent() {
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);
		final String phone_l = sharedPreferences.getString("phone", null);
		final String pw_l = sharedPreferences.getString("password", null);
		
		// TODO Auto-generated method stub
		login.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone_i = phone.getText().toString();
				String pw_i = password.getText().toString();
				if(phone_i.compareTo(phone_l)==0 && pw_i.compareTo(pw_l)==0){				
				Intent intent = new Intent();
				intent.setClass(LoginDetailActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				onDestroy();}
				else{
					String log = "手机号或者密码错误";
					Toast.makeText(LoginDetailActivity.this, log, Toast.LENGTH_LONG).show();
				}
				
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
