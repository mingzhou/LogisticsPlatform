package com.logistics.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@ContentView(R.layout.activity_profile_change_password)
public class ProfileChangePasswordActivity extends RoboActivity {
	private final String BASE_URL = "http://219.223.190.211";
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	

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
//				// TODO Auto-generated method stub
//				if(old_password.getText().toString().equals(sharedPreferences.getString("password", null))
//						&& (confirm.getText().toString().equals(new_password.getText().toString()) ) ){
//					String m = confirm.getText().toString();
//					editor.putString("password", m);
//					editor.commit();
//					finish();
//					onDestroy();
//				}else{
//					Toast.makeText(ProfileChangePasswordActivity.this, "sth goes wrong", Toast.LENGTH_LONG).show();
//				
//				}		
			try {
				attemptModify();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			}});
			
	}
	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}
	
	public void attemptModify() throws JSONException {
		RequestParams rp = new RequestParams();
		String mOldP = old_password.getText().toString();
		String mNewP = new_password.getText().toString();
		final String mConF = confirm.getText().toString();
		boolean cancel = false;
		View focusView = null;
		String mPinSP = sharedPreferences.getString("password", null);
		String mPhinSP = sharedPreferences.getString("phone", null);
		String mUinSP = sharedPreferences.getString("usr_name", null);
		// Check for a valid password, if the user entered one.
		if(!mOldP.equals(mPinSP)){
			old_password.setError(Html.fromHtml("<font color=#E10979>输入密码错误</font>"));
			focusView = old_password;
			cancel = true;
		}
		if (TextUtils.isEmpty(mNewP) || !isPasswordValid(mNewP)) {
			
			new_password.setError(Html.fromHtml("<font color=#E10979>密码太短，应大于4位</font>"));
			focusView = new_password;
			cancel = true;
		}
		if(!mConF.equals(mNewP)){
			confirm.setError(Html.fromHtml("<font color=#E10979>两次密码不一致</font>"));
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
			
			JSONObject tmp = new JSONObject();
			tmp.put("phone", mPhinSP);
			tmp.put("password", mConF);
			tmp.put("user", mUinSP);
			
			Log.d("nihao",tmp.toString());
			rp.put("data", tmp.toString());
			JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String response) {
					//Toast.makeText(ProfileChangePasswordActivity.this, response.toString(), Toast.LENGTH_LONG).show();
					int state_num =4;
				 	for(int i=0;i<headers.length;i++){
					 	Log.d("nihao",headers[i].toString());
					 	if(isInteger(headers[i].getValue().toString())){
					 		state_num = Integer.parseInt(headers[i].getValue());
					 		Log.d("nihao",headers[i].getValue());
					 	}
					 	}
				 	if(state_num ==0){
					Intent intent = new Intent();
					intent.setClass(ProfileChangePasswordActivity.this,LoginDetailActivity.class);
					
					editor.putString("password", mConF);
					
					
					editor.commit();
					Toast.makeText(ProfileChangePasswordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
					startActivity(intent);
					finish();
					onDestroy();
					}else {
						Toast.makeText(ProfileChangePasswordActivity.this, "账号或用户名错误", Toast.LENGTH_LONG).show();
						
					}
					
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseBody, Throwable e) {
					Toast.makeText(ProfileChangePasswordActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
					
				}
							
			};
			httpHelper.post(BASE_URL+"/reset",rp, jrh);}
		}
		
	

	public static boolean isInteger(String input){  
    Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);  
    return mer.find();  
} 

}
