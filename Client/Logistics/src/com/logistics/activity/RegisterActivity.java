package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends RoboActivity {
	
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.password)
	private EditText password;
	
	@InjectView(R.id.usr_name)
	private EditText usr_name;
	
	@InjectView(R.id.role_id)
	private Spinner role_id;
	
	@InjectView(R.id.register)
	private Button register;
	
	private ArrayAdapter<String> roleAdapter;
	private String[] roleStrings = { "driver", "source"};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
        initComponent();

	}


	private void initComponent() {
		// TODO Auto-generated method stub
		register.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this,LoginDetailActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
		
		roleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, roleStrings);
		roleAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		role_id.setAdapter(roleAdapter);
	}

	

}
