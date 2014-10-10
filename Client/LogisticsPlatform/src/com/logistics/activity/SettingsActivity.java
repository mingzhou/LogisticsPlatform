package com.logistics.activity;

import java.util.Date;
import java.util.LinkedList;

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
import com.logistics.utils.PullRefreshLayout;
import com.logistics.utils.PullRefreshLayout.OnPullListener;
import com.logistics.utils.PullRefreshLayout.OnPullStateListener;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends RoboActivity implements OnPullListener,
OnPullStateListener, OnItemClickListener{

	public static final String TAG = SettingsActivity.class.getSimpleName();
	
	
	
	@Inject
	private AsyncHttpHelper httpHelper;
			
	private PullRefreshLayout mPullLayout;
	private TextView mActionText;
	private TextView mTimeText;
	private TextView mSettingsTitle;
	private View mProgress;
	private View mActionImage;
	
	private Animation mRotateUpAnimation;
	private Animation mRotateDownAnimation;

	private ListView mListView;

	private boolean mInLoading = false;
	
	private LinkedList<String> data = new LinkedList<String>();
	private MyAdapter adapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initView();
		
		initData();
	}

	private void initView() {
		mRotateUpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_up);
		mRotateDownAnimation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_down);

		mPullLayout = (PullRefreshLayout) findViewById(R.id.pull_container);
		mPullLayout.setOnActionPullListener(this);
		mPullLayout.setOnPullStateChangeListener(this);

		mProgress = findViewById(android.R.id.progress);
		mActionImage = findViewById(android.R.id.icon);
		mActionText = (TextView) findViewById(R.id.pull_note);
		mTimeText = (TextView) findViewById(R.id.refresh_time);

		mTimeText.setText(R.string.note_not_update);
		mActionText.setText(R.string.note_pull_down);
		
//		mSettingsTitle = (TextView) findViewById(R.id.settings_title);
//		mSettingsTitle.setText(R.string.settings_title);

		mListView = (ListView) findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
	}

	private void initData() {
		for (int i = 0; i < 10; i++) {
			data.add(String.format("Item %d", i));
		}

		adapter = new MyAdapter(this, android.R.layout.simple_list_item_1,
				android.R.id.text1);
		mListView.setAdapter(adapter);
	}
	
	private void dataLoaded() {
		if (mInLoading) {
			mInLoading = false;
			mPullLayout.setEnableStopInActionView(false);
			mPullLayout.hideActionView();
			mActionImage.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);

			if (mPullLayout.isPullOut()) {
				mActionText.setText(R.string.note_pull_refresh);
				mActionImage.clearAnimation();
				mActionImage.startAnimation(mRotateUpAnimation);
			} else {
				mActionText.setText(R.string.note_pull_down);
			}

			mTimeText.setText(getString(R.string.note_update_at, DateFormat.getTimeFormat(this)
					.format(new Date(System.currentTimeMillis()))));
			
			
		}
	}
	
	@Override
	public void onPullOut() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_refresh);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateUpAnimation);
		}
	}

	@Override
	public void onPullIn() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_down);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateDownAnimation);
		}
	}

	@Override
	public void onSnapToTop() {
		if (!mInLoading) {
			mInLoading = true;
			mPullLayout.setEnableStopInActionView(true);
			mActionImage.clearAnimation();
			mActionImage.setVisibility(View.GONE);
			mProgress.setVisibility(View.VISIBLE);
			mActionText.setText(R.string.note_pull_loading);
			
			new RefreshDataTask().execute();
		}
	}
	
	private class RefreshDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "new item refreshed";
        }

        @Override
        protected void onPostExecute(String result) {
        	dataLoaded();
            data.addFirst(result);
            adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }
	
	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mLayoutInflater;
		private int layout;
		private int id;

		MyAdapter(Context context, int layout, int id) {
			mLayoutInflater = LayoutInflater.from(context);
			this.layout = layout;
			this.id = id;
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(layout, null);
			}
			
			TextView textView = (TextView)convertView.findViewById(id);
			if(textView != null) {
				if(position < data.size())
					textView.setText(data.get(position));
			}
			
			return convertView;
		}
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
