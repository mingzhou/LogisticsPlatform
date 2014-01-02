package com.logistics.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import com.logistics.utils.HttpHelper;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
//import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
	
	//private HttpHelper hh = new HttpHelper("1.txt"); 
	//private String reply =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		send.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {
				
					
					doNetworkCompuationThread( );
					
				
				
			}});
		
		
	}
	
	
	public void  doNetworkCompuationThread( ){
		//String reply = null;
		new Thread(new Runnable(){
			public void run() {
			
				try {
					final String reply = new HttpHelper("1.txt").getMessage();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				result.post(new  Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						result.setText(reply);
					}});

					
			//	result.setText(reply);
			
			
		}}).start();
		
			
	}
	   
	//
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
