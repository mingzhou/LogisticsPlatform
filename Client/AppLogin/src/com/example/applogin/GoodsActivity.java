package com.example.applogin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class GoodsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		initComponent();
	}

	private Spinner goods_Departure;
	private Spinner goods_Destination;
	private Spinner goods_Type;
	private EditText goods_weight;
	private ImageButton goods_return;
		
	private ArrayAdapter<String> depAdapter;
	private ArrayAdapter<String> desAdapter;
	private ArrayAdapter<String> typeAdapter;
	//private double weightDouble;
	
	private String[] depStrings = { "江", "浙", "沪" };
	private String[] desStrings = { "粤", "豫", "湘", "闽" };
	private String[] typeStrings = { "食品", "建材" };
		
	private void initComponent() {
		// TODO Auto-generated method stub
		// departure
		goods_Departure = (Spinner) findViewById(R.id.goods_departure);
		// 添加下拉列表数据
		depAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, depStrings);
		// 设置下拉列表风格
		depAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goods_Departure.setAdapter(depAdapter);
		 

		// destination
		goods_Destination = (Spinner) findViewById(R.id.goods_destination);
		desAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, desStrings);
		desAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goods_Destination.setAdapter(desAdapter);
		

		// type
		goods_Type = (Spinner) findViewById(R.id.goods_type);
		typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeStrings);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		goods_Type.setAdapter(typeAdapter);
		
		//weight
		goods_weight = (EditText) findViewById(R.id.goods_weight);
		//weightDouble = Double.parseDouble(goods_weight.getText().toString());
		
		goods_return = (ImageButton) findViewById(R.id.goods_return);
		goods_return.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(GoodsActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goods, menu);
		return true;
	}

}
