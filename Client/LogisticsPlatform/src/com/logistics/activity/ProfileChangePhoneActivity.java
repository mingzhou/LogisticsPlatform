package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@ContentView(R.layout.activity_profile_change_phone)
public class ProfileChangePhoneActivity extends RoboActivity {
	
	@InjectView(R.id.old_phone)
	private EditText old_phone;
	
	@InjectView(R.id.new_phone)
	private EditText new_phone;
	
	@InjectView(R.id.confirm)
	private EditText confirm;
	
	@InjectView(R.id.submit)
	private Button submit;
	
	@InjectView(R.id.return_btn)
	private Button return_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_change_phone);
		initComponent();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		//return 
        return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileChangePhoneActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
			}});
        
        submit.setOnClickListener(new Button.OnClickListener(){
        	//communication need
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileChangePhoneActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
			}});
	}

	
	
}
