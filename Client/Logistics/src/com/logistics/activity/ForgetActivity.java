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

public class ForgetActivity extends RoboActivity {
	public final static int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1;
	
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.set_password)
	private EditText set_password;
	
	@InjectView(R.id.submit)
	private Button submit;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		initComponent();
	}

	private void initComponent() {
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();  
		
		// TODO Auto-generated method stub
		submit.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				String p = phone.getText().toString();
				String m = set_password.getText().toString();
				
				editor.putString("phone", p);
				editor.putString("password", m);
				editor.commit();
				
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ForgetActivity.this,LoginDetailActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
	}

	

}
