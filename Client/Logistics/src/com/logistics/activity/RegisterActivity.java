package com.logistics.activity;

import java.io.IOException;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends RoboActivity {
	public final static int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1;
	
	private final String BASE_URL = "http://219.223.190.211";
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
		
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.password)
	private EditText password;
	
	@InjectView(R.id.usr_name)
	private EditText usr_name;
	
//	@InjectView(R.id.role_id)
//	private Spinner role_id;
	
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
		//role_id.setAdapter(roleAdapter);
		
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();  
              
               
		// TODO Auto-generated method stub
		register.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
								
				try {
					getHttpResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
	
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		final String mPhone = phone.getText().toString();
		final String mPassword = password.getText().toString();	
		final String mUsrname = usr_name.getText().toString();
//		final String mRoleid = role_id.getSelectedItem().toString();
//		final int i_p = role_id.getSelectedItemPosition();
//		
		boolean cancel = false;
		View focusView = null;
		if(TextUtils.isEmpty(mUsrname)){
			usr_name.setError(Html.fromHtml("<font color=#E10979>用户名不能为空</font>"));
			focusView = usr_name;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mPassword)|| !isPasswordValid(mPassword) ) {
			
			password.setError(Html.fromHtml("<font color=#E10979>密码太短，应大于4位</font>"));
			focusView = password;
			cancel = true;
		}
		if(TextUtils.isEmpty(mPhone)){
			phone.setError(Html.fromHtml("<font color=#E10979>手机号不能为空</font>"));
			focusView = phone;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
		JSONObject tmp = new JSONObject();
		tmp.put("phone", mPhone);
		tmp.put("password", mPassword);
		tmp.put("user", mUsrname);
		//tmp.put("role", mRoleid);
		Log.d("nihao",tmp.toString());
		rp.put("data", tmp.toString());
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String response) {
				//Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_LONG).show();
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
				intent.setClass(RegisterActivity.this,LoginDetailActivity.class);
				editor.putString("phone", mPhone);
				editor.putString("password", mPassword);
				editor.putString("usr_name", mUsrname);
//				editor.putString("role_id", mRoleid);
//				editor.putInt("role_id_p", i_p);
				editor.commit();
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				startActivity(intent);
				finish();
				onDestroy();
				}else {
					Toast.makeText(RegisterActivity.this, "账号或用户名已注册", Toast.LENGTH_LONG).show();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(RegisterActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
						
		};
		httpHelper.post(BASE_URL+"/signup",rp, jrh);}
	}

	public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);  
        return mer.find();  
    }
	
	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

}
