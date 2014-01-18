package com.logistics.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_good_result)
public class GoodResultActivity extends RoboActivity {

	@InjectView(R.id.result_show)
	private TextView test;
	
	private final String DUMMY = null;
	
	@Inject
	private AsyncHttpHelper httpHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_result);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initComponent();
		Bundle b = getBundle();
		
		
	}
	
	private void initComponent(){
		//test.setText(DUMMY);
		
	}

	private void getHttpResponse(){
		String s = "";
		httpHelper.get("/file/test.json", new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(JSONObject response) {
				//Toast.makeText(GoodResultActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				//test.setText(response.toString());
				
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				//Toast.makeText(GoodResultActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				//test.setText(statusCode + "\t" + responseBody);
				
			}
		});
		
		return s;
	}

	private Bundle getBundle(){
		Bundle bundle = this.getIntent().getExtras();
		return bundle;
	}
}
