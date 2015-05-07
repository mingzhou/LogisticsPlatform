package com.logistics.activity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetActivity extends RoboActivity {
	public final static int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1;
	private final String BASE_URL = "http://219.223.189.234";
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	
	@InjectView(R.id.phone)
	private EditText phone;
	
	@InjectView(R.id.set_password)
	private EditText set_password;

    @InjectView(R.id.psw_confirm)
    private EditText psw_confirm;
	
	@InjectView(R.id.usr_name)
	private EditText usr_name;
	
	@InjectView(R.id.answer)
	private EditText answer;
	
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
								
				// TODO Auto-generated method stub
				try {
					getHttpResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(ForgetActivity.this, "错误", Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(ForgetActivity.this, "错误", Toast.LENGTH_LONG).show();
				}
			}});
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}
	
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		final String mPhone = phone.getText().toString();
		final String mPassword = set_password.getText().toString();	
		final String mUsrname = usr_name.getText().toString();
		final String mAnswer = answer.getText().toString();
		final String iAnswer =  sharedPreferences.getString("answer", null);
		boolean cancel = false;
		View focusView = null;
		if(TextUtils.isEmpty(mPassword)|| !isPasswordValid(mPassword)){
			set_password.setError(Html.fromHtml("<font color=#E10979>密码太短，应大于4位</font>"));
			focusView = set_password;
			cancel = true;
		}
		
		if(!mAnswer.equals(iAnswer)){
			answer.setError(Html.fromHtml("<font color=#E10979>密保问题回答不正确</font>"));
			focusView = answer;
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
		
		Log.d("nihao",tmp.toString());
		rp.put("data", tmp.toString());
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String response) {
				//Toast.makeText(ForgetActivity.this, response.toString(), Toast.LENGTH_LONG).show();
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
				intent.setClass(ForgetActivity.this,LoginDetailActivity.class);
				editor.putString("phone", mPhone);
				editor.putString("password", mPassword);
				editor.putString("usr_name", mUsrname);
				
				editor.commit();
				Toast.makeText(ForgetActivity.this, "修改成功", Toast.LENGTH_LONG).show();
				startActivity(intent);
				finish();
				onDestroy();
				}else {
					//Toast.makeText(ForgetActivity.this, "账号或用户名错误", Toast.LENGTH_LONG).show();
					usr_name.setError(Html.fromHtml("<font color=#E10979>手机号或者用户名错误</font>"));
					phone.setError(Html.fromHtml("<font color=#E10979>手机号或者用户名错误</font>"));
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(ForgetActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
						
		};
		httpHelper.post(BASE_URL+"/reset",rp, jrh);}
	}

	public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);  
        return mer.find();  
    }  
	

}
