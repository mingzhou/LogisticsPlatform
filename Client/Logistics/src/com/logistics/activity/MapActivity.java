package com.logistics.activity;

import java.util.ArrayList;
import java.util.Date;

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
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

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
	private static int refreshCnt = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		geneItems1();

		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	}
	
	private void geneItems1() {
		//getHttpResponse();
		for (int i = 0; i != 6; ++i) {
			items.add("refresh cnt " + (++start));
		}
	}

	private void geneItems() {
		getHttpResponse();
//		for (int i = 0; i != 20; ++i) {
//			items.add("refresh cnt " + (++start));
//		}
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
				start = ++refreshCnt;
				items.clear();
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
			public void onSuccess(JSONObject response) {
					Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
					
					//items.add(response.getString("k1"));
					//items.add(response.getString("k2"));
					try {
						items.add(response.getString("from"));
						items.add(response.getString("site"));
						items.add(response.getString("to"));
//						long t = response.getJSONObject("datetime").getLong("$date");
//						items.add(new Date(t).toString());
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//test.setText("车牌"+chepai+"\n"+"电话"+dianhual;
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(MapActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
		};
		httpHelper.get("221:8000/", rp,jrh);
	}
	

}
