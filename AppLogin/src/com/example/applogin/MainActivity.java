package com.example.applogin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
		setListensers();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		trunkbutton = (Button) findViewById(R.id.button_trunk);
		goodsbutton = (Button) findViewById(R.id.button_goods);
	}

	private Button trunkbutton;//button_trunk
	private Button goodsbutton;//button_goods
	
	private void setListensers() {
		// TODO Auto-generated method stub
		trunkbutton.setOnClickListener(trunk);
		goodsbutton.setOnClickListener(goods);
	}

	private Button.OnClickListener trunk = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,TrunkActivity.class);
			startActivity(intent);
			
		}
		
	};
	
	private Button.OnClickListener goods = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,GoodsActivity.class);
			startActivity(intent);
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
