package com.logistics.activity;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
	//private AsyncHttpHelper httpHelper = new AsyncHttpHelper();
	@InjectView(R.id.goods_departure)
	private EditText goods_Departure;
	
	@InjectView(R.id.goods_destination)
    private EditText goods_Destination;
	
//	@InjectView(R.id.goods_type)
//    private Spinner goods_Type;
//	
//	@InjectView(R.id.goods_weight)
//    private EditText goods_weight;
	@InjectView(R.id.http_progress)
	private View mProgressView;
		
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
	//private ArrayList<String> items = new ArrayList<String>();
		
	private Intent intent = new Intent();
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
		
                
        goods_searchButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgress(true);
				//onRefresh();
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
        
	}
	
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
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
		}, 2500);
	}
	
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		String mTo = goods_Destination.getText().toString();
		String mFrom = goods_Departure.getText().toString();	
		JSONObject tmp = new JSONObject();
		tmp.put("to", mTo);
		tmp.put("from", mFrom);
		Log.d("nihao",tmp.toString());
		rp.put("data", tmp.toString());
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				
				intent.setClass(GoodActivity.this,GoodResultActivity.class);
				intent.putExtra("query", response.toString());
				//Log.d("nihao",response.toString());
				showProgress(false);
				startActivity(intent);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(GoodActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				showProgress(false);
			}
						
		};
		httpHelper.post(BASE_URL+"/query",rp, jrh);
	}
	
	public void showProgress(final boolean show){
		mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
        PackageManager pm = getPackageManager();    
        ResolveInfo homeInfo =   
            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);   
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            ActivityInfo ai = homeInfo.activityInfo;    
            Intent startIntent = new Intent(Intent.ACTION_MAIN);    
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);    
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));    
            startActivitySafely(startIntent);    
            return true;    
        } else    
            return super.onKeyDown(keyCode, event);    
    }  
    private void startActivitySafely(Intent intent) {    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        try {    
            startActivity(intent);    
        } catch (ActivityNotFoundException e) {    
            Toast.makeText(this, "null",    
                    Toast.LENGTH_SHORT).show();    
        } catch (SecurityException e) {    
            Toast.makeText(this, "null",    
                    Toast.LENGTH_SHORT).show();     
        }    
    }  
	
		
}
