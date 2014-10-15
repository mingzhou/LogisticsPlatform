package com.logistics.activity;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.http.Header;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity  implements IXListViewListener{

	public static final String TAG = MapActivity.class.getSimpleName();
	
	@Inject
	private AsyncHttpHelper httpHelper;
	
	@InjectView(R.id.xListView)
	private XListView mListView;
	
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		//geneItems();

		
		mListView.setPullLoadEnable(false);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
		//mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		//onRefresh();
	}
	
	

	private void geneItems() {
		getHttpResponse();
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
				start = 0;
				geneItems();
				// mAdapter.notifyDataSetChanged();
				mAdapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_expandable_list_item_1, items);
				mListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				start = 2;
				geneItems();
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	private void getHttpResponse(){
		RequestParams rp = new RequestParams();
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
					
					try {
						
						for (int i =start; i<start+4;i++){
							//r[i] = 
							items.add(response.getJSONObject(i).getString("from")+" -> " + response.getJSONObject(i).getString("to"));
						}
						
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
		httpHelper.get("227:8000/", rp,jrh);
	}
	

}
