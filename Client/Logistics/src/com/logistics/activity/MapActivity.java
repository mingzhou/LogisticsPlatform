package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.logistics.service.NotifyCenter;
import com.logistics.service.PushAndPull;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

@SuppressLint("WorldReadableFiles")
@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity  implements IXListViewListener{

	public static final String TAG = MapActivity.class.getSimpleName();
	public static final int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1; 
	public static final int MODE_APPEND = 32768;
	
	@Inject
	//private AsyncHttpHelper httpHelper;
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	
	@InjectView(R.id.xListView)
	private XListView mListView;
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;
	
	private JSONArray jArray = new JSONArray();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		try {
			initComponent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
		
	private void initComponent() throws IOException, JSONException {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(MapActivity.this, NotifyCenter.class);
//		startService(intent);
		
		Intent intent2 = new Intent(MapActivity.this, PushAndPull.class);
		startService(intent2);
		
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();
		
        
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
		mListView.setAdapter(mAdapter);

		mListView.setXListViewListener(this);
		mHandler = new Handler();
		
		loadFile();
		for (int i =0; i<jArray.length();i++){
        	//r[i] =
        	items.add(jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"));
        	}
		mAdapter.notifyDataSetChanged();
	}
	

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateFormat.getTimeFormat(this).format(new Date(System.currentTimeMillis())));
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//start = ++refreshCnt;
				items.clear();
				
				Log.d(TAG,"refresh begin");
				try {
					getHttpResponse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mAdapter.notifyDataSetChanged();
//				mAdapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_expandable_list_item_1, items);
//				mListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2500);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				
				try {
					getHttpResponse2();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2500);
	}
	
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		loadFile();
		String jOS = jArray.getJSONObject(0).toString();
		rp.put("data", jOS);
		
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				 try {
					 	downFile(response);
					 	//Log.d(TAG+"nihao","header num"+Integer.toString(headers.length));
					 	for(int i=0;i<headers.length;i++){
					 	Log.d(TAG+"nihao",headers[i].toString());
					 	}
					 	//newnum = Integer.parseInt(headers[3].getValue());
					 	loadFile();
				        Log.d(TAG+"nihao","read done");
				        Log.d(TAG+"nihao",jArray.toString());
				        for (int i =0; i<jArray.length();i++){
				        	//r[i] =
				        	items.add(jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"));
				        	}
				   	} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
										
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(MapActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
						
		};
		httpHelper.post("http://219.223.190.211/latest",rp, jrh);
	}
	
	public void getHttpResponse2() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		//loadFile(jArray);
		String jOS = jArray.getJSONObject(jArray.length()-1).toString();
		rp.put("data", jOS);
				
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8"){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				//super.onFailure(statusCode, headers, responseBody, e);
				Toast.makeText(MapActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(JSONArray response) {
				// TODO Auto-generated method stub
				try {
					//updateFile(response);
				 	//loadFile(jArray);
					for(int i =0;i<response.length();i++){
					jArray.put(jArray.length(), response.getJSONObject(i));
					}
			        Log.d(TAG,"update read done");
			       
			        Log.d(TAG,"response "+Integer.toString(response.length()) );
			        Log.d(TAG,"jArray "+Integer.toString(jArray.length()) );
			        for (int i =0; i<jArray.length();i++){
			        	//r[i] =
			        	items.add(jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"));
			        	}
			   	} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		//httpHelper.
		httpHelper.post("http://219.223.190.211/previous",rp,jrh);
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=MapActivity.this.openFileInput("tmp.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        clearArray();
        JSONArray tmp = new JSONArray(stream.toString());
       // Log.d(TAG,tmp.toString());
        for(int i = 0; i<tmp.length();i++){
        	jArray.put(i, tmp.getJSONObject(i));
        }
        Log.d(TAG,"jArray in loadfile()"+Integer.toString(jArray.length()));
        stream.close();
        inStream.close();
        
	}
	
	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=MapActivity.this.openFileOutput("tmp.txt",MODE_WORLD_READABLE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
	}
	
	public void updateFile(JSONArray response) throws IOException{
		FileOutputStream outStream=MapActivity.this.openFileOutput("tmp.txt",MODE_APPEND);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"update done");
	}
	
	public void clearArray() throws JSONException{
		jArray = new JSONArray();
		Log.d(TAG+"nihao",jArray.toString());
	}

}
