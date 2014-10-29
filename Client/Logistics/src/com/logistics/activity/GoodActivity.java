package com.logistics.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@ContentView(R.layout.activity_good)
public class GoodActivity extends RoboActivity {
	public static final String TAG1 = GoodActivity.class.getSimpleName();
	private final String BASE_URL = "http://219.223.190.211";
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	
	@InjectView(R.id.goods_departure)
	private EditText goods_Departure;
	
	@InjectView(R.id.goods_destination)
    private EditText goods_Destination;
	
//	@InjectView(R.id.goods_type)
//    private Spinner goods_Type;
//	
//	@InjectView(R.id.goods_weight)
//    private EditText goods_weight;
	
		
	@InjectView(R.id.goods_searchButton)
	private Button goods_searchButton;
    
//    private ArrayAdapter<String> depAdapter;
//    private ArrayAdapter<String> desAdapter;
//    private ArrayAdapter<String> typeAdapter;
//    
//    private String[] depStrings = { "Jiangsu", "Zhejiang", "Shanghai" };
//    private String[] desStrings = { "Guangdong", "Henan", "Hunan", "Fujian" };
//    private String[] typeStrings = { "food", "fruit" };
	
	//private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	
	public static final String TAG = GoodActivity.class.getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        initComponent();
}

	private void initComponent() {
		// TODO Auto-generated method stub
		// departure
//        // 添加下拉列表数据
//        depAdapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, depStrings);
//        // 设置下拉列表风格
//        depAdapter
//                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        goods_Departure.setAdapter(depAdapter);
//         
//
//        // destination
//        desAdapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, desStrings);
//        desAdapter
//                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        goods_Destination.setAdapter(desAdapter);
//        
//
//        // type
//        typeAdapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item, typeStrings);
//        typeAdapter
//                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        goods_Type.setAdapter(typeAdapter);
//        
        //weight
        
		mHandler = new Handler();
                
        goods_searchButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				onSummit();
				intent.setClass(GoodActivity.this,GoodResultActivity.class);
								
				startActivity(intent);
			}});
	}
	
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		
		String des = goods_Destination.getText().toString();
		String dep = goods_Departure.getText().toString();
		rp.put("destination", des);
		rp.put("depature", dep);
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				for(int i = 0;i<response.length();i++){
				try {
					items.add(response.getJSONObject(i).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
										
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(GoodActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
						
		};
		httpHelper.post(BASE_URL+"/query",rp, jrh);
	}
	
	public void onSummit() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//start = ++refreshCnt;
				items.clear();
				try {
						getHttpResponse();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}, 500);
	}
	
		
}
