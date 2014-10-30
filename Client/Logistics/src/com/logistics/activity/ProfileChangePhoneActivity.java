package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_change_phone);
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
//				if(old_phone.getText().toString().equals(sharedPreferences.getString("phone", null))
//						&& (confirm.getText().toString().equals(new_phone.getText().toString()) ) ){
//					String m = confirm.getText().toString();
//					editor.putString("phone", m);
//					editor.commit();
//					finish();
//					onDestroy();
//				}else{
//					Toast.makeText(ProfileChangePhoneActivity.this, "sth goes wrong", Toast.LENGTH_LONG).show();
//				
//				}
				attemptModify();
			}});
	}

	public void attemptModify() {
		String mOldP = old_phone.getText().toString();
		String mNewP = new_phone.getText().toString();
		String mConF = confirm.getText().toString();
		String mPinSP = sharedPreferences.getString("phone", null);
		boolean cancel = false;
		View focusView = null;
		if(!mOldP.equals(mPinSP)){
			old_phone.setError(Html.fromHtml("<font color=#E10979>输入手机号错误</font>"));
			focusView = old_phone;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mNewP) ) {
			
			new_phone.setError(Html.fromHtml("<font color=#E10979>手机号不能为空</font>"));
			focusView = new_phone;
			cancel = true;
		}
		if(!mConF.equals(mNewP)){
			confirm.setError(Html.fromHtml("<font color=#E10979>两次手机号不一致</font>"));
			focusView = confirm;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			
			finish();
			onDestroy();
		}
	}
	
	
	
}
