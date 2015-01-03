package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import com.logistics.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class WebDisplayActivity extends RoboActivity {
	@InjectView(R.id.webview)
	private WebView mWebView;
	
	@InjectView(R.id.return_btn)
	private Button mReturn;
	
	private String urlString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_display);
			
		initComponent();
	}
	
	private Boolean getExtras() {
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			urlString = extras.getString("data");
			Log.d("nihao",urlString);
			return true;
		}else{
			return false;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initComponent() {
		// TODO Auto-generated method stub
		mReturn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
                onDestroy();
                
			}});
	
		if(getExtras()){
			mWebView = (WebView) findViewById(R.id.webview);
			//mWebView.setInitialScale(100);
			mWebView.getSettings().setJavaScriptEnabled(true);
			//mWebView.getSettings().setBuiltInZoomControls(true); 
			mWebView.getSettings().setUseWideViewPort(true);
			mWebView.getSettings().setLoadWithOverviewMode(true); 
			mWebView.loadUrl(urlString);
		}else{
			Log.d("nihao","erro");
		}
	}
}
