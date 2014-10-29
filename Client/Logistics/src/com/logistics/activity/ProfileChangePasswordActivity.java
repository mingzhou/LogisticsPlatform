package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@ContentView(R.layout.activity_profile_change_password)
public class ProfileChangePasswordActivity extends RoboActivity {

	@InjectView(R.id.old_password)
	private EditText old_password;
	
	@InjectView(R.id.new_password)
	private EditText new_password;
	
	@InjectView(R.id.confirm)
	private EditText confirm;
	
	@InjectView(R.id.submit)
	private Button submit;
	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_change_password);
		initComponent();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();
		//return 
        return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                finish();
                onDestroy();
			}});
        
        submit.setOnClickListener(new Button.OnClickListener(){
        	//communication need
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(old_password.getText().toString().equals(sharedPreferences.getString("password", null))
						&& (confirm.getText().toString().equals(new_password.getText().toString()) ) ){
					String m = confirm.getText().toString();
					editor.putString("password", m);
					editor.commit();
					finish();
					onDestroy();
				}else{
					Toast.makeText(ProfileChangePasswordActivity.this, "sth goes wrong", Toast.LENGTH_LONG).show();
				
				}
				
			}});
          
	}

	

}
