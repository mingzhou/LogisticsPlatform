package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * 主界面
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@ContentView(R.layout.activity_main)	//	绑定了activity_main.xml
public class MainActivity extends RoboActivity {
	@InjectView(R.id.helloTextView)
	TextView helloTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		helloTextView.setText("物流平台");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
