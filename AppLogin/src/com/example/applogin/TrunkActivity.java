package com.example.applogin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class TrunkActivity extends Activity {
	
	private Spinner trunk_Departure;
	private Spinner trunk_Destination;
	private Spinner trunk_Type;
	private EditText trunk_weight;
	private ImageButton trunk_return;
		
	private ArrayAdapter<String> depAdapter;
	private ArrayAdapter<String> desAdapter;
	private ArrayAdapter<String> typeAdapter;
	//private double weightDouble;
	
	private String[] depStrings = { "江", "浙", "沪" };
	private String[] desStrings = { "粤", "豫", "湘", "闽" };
	private String[] typeStrings = { "平板车", "厢车" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trunk);
		initComponent();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// departure
		trunk_Departure = (Spinner) findViewById(R.id.trunk_departure);
		// 添加下拉列表数据
		depAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, depStrings);
		// 设置下拉列表风格
		depAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		trunk_Departure.setAdapter(depAdapter);
				 

		// destination
		trunk_Destination = (Spinner) findViewById(R.id.trunk_destination);
		desAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, desStrings);
		desAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		trunk_Destination.setAdapter(desAdapter);
				

		// type
		trunk_Type = (Spinner) findViewById(R.id.trunk_type);
		typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, typeStrings);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		trunk_Type.setAdapter(typeAdapter);
				
		//weight
		trunk_weight = (EditText) findViewById(R.id.trunk_weight);
		//weightDouble = Double.parseDouble(trunk_weight.getText().toString());
		
		trunk_return = (ImageButton) findViewById(R.id.trunk_return);
		trunk_return.setOnClickListener(new ImageButton.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TrunkActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trunk, menu);
		return true;
	}

}
