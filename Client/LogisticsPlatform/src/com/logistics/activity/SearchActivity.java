package com.logistics.activity;

import com.logistics.utils.HttpHelper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
*测试客户端和服务器的收发
*/

@ContentView(R.layout.activity_search)	//	绑定了activity_search.xml
public class SearchActivity extends RoboActivity {
	@InjectView(R.id.query)
	private EditText query;
	
	@InjectView(R.id.send)
	private Button send;
	
	@InjectView(R.id.result)
	private TextView result;
	
//	private HttpHelper hh = new HttpHelper("1.txt"); 
//	private String reply =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//query.setOnEditorActionListener(new );
		
		send.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {
				new AsyncTest().execute();
			}});
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	private class AsyncTest extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uri) {
			// TODO Auto-generated method stub
			if(query.getText().toString().equals("1.txt")){
			HttpHelper hh = new HttpHelper("1.txt");
			return hh.getMessage();}
			else return "wrong";
			
			
		}

		@Override
		protected void onPostExecute(String reply) {
			// TODO Auto-generated method stub
			result.setText(reply);
		}


	}
	
}



