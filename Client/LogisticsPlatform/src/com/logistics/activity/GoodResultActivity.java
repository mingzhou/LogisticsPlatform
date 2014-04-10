package com.logistics.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_good_result)
public class GoodResultActivity extends RoboActivity {

	@InjectView(R.id.result_show)
	private TextView test;
	
	//private final String DUMMY = null;
	
	@Inject
	private AsyncHttpHelper httpHelper;
	
	@InjectView(R.id.return_btn)
	private Button return_btn;
	
	@InjectView(android.R.id.list)
	private ListView listview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_result);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		initComponent();
		Bundle b = getBundle();
		getHttpResponse(b);
		
		
		
		
	}
	
	
	
	private void initComponent(){
		//return 
		return_btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(GoodResultActivity.this,GoodActivity.class);
                startActivity(intent);
                finish();
			}});
		
	}
	
	public void setstring(String a, String b){
		a = b;
	}
	
	private void getHttpResponse(Bundle b){
		
		RequestParams rp = new RequestParams();
		rp.put("dep", b.getString("key_dep"));
		rp.put("des", b.getString("key_des"));
		rp.put("typ", b.getString("key_typ"));
		
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(JSONObject response) {
					Toast.makeText(GoodResultActivity.this, response.toString(), Toast.LENGTH_LONG).show();
					
					try {
						String[] items = {response.getString("车牌"),response.getString("电话")};
						
						listview.setAdapter(new ArrayAdapter<String>(	GoodResultActivity.this ,android.R.layout.simple_list_item_1,
								items));
						listview.setOnItemClickListener(null);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//test.setText("车牌"+chepai+"\n"+"电话"+dianhual;
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(GoodResultActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				test.setText(statusCode + "\t" + responseBody);
			}
		};
		httpHelper.get("/json/", rp,jrh);
		
	}

	private Bundle getBundle(){
		Bundle bundle = this.getIntent().getExtras();
		return bundle;
	}
}
