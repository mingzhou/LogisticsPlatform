package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends RoboActivity {
	public final static int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1;
		
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
	
//	@InjectView(R.id.test)
//	private Button test;
	
	private ArrayAdapter<String> roleAdapter;
	private String[] roleStrings = { "司机", "货主"};
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;  
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
        initComponent();

	}


	private void initComponent() {
		roleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, roleStrings);
		roleAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		role_id.setAdapter(roleAdapter);
		
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();  
              
               
		// TODO Auto-generated method stub
		register.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String p = phone.getText().toString();
				String m = password.getText().toString();
				String u = usr_name.getText().toString();
				String i = role_id.getSelectedItem().toString();
				int i_p = role_id.getSelectedItemPosition();
				
				editor.putString("phone", p);
				editor.putString("password", m);
				editor.putString("usr_name", u);
				editor.putString("role_id", i);
				editor.putInt("role_id_p", i_p);

				editor.commit();
				
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this,LoginDetailActivity.class);
				startActivity(intent);
				finish();
				onDestroy();
			}});
		
//		test.setOnClickListener(new Button.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				String p = sharedPreferences.getString("phone", null);
//				String m = sharedPreferences.getString("password", null);
//				String u = sharedPreferences.getString("usr_name", null);
//				int i = sharedPreferences.getInt("role_id_p", 0);
//				
//				phone.setText(p);
//				password.setText(m);
//				usr_name.setText(u);
//				role_id.setSelection(i);
//			}});
				
	}

	

}
