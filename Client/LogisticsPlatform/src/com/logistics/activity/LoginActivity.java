package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.logistics.R;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {

	@InjectView(R.id.welcome_login)
	private Button loginButton;
	
	@InjectView(R.id.welcome_register)
	private Button registerButton;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, LoginDetailActivity.class));
				finish();
				onDestroy();
			}
		});
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				finish();
				onDestroy();
			}
		});
	}
}
