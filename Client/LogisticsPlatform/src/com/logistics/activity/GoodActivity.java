package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.logistics.R;

@ContentView(R.layout.activity_good)
public class GoodActivity extends RoboActivity {
	@InjectView(R.id.goods_departure)
	private Spinner goods_Departure;
	
	@InjectView(R.id.goods_destination)
    private Spinner goods_Destination;
	
	@InjectView(R.id.goods_type)
    private Spinner goods_Type;
	
	@InjectView(R.id.goods_weight)
    private EditText goods_weight;
	
	@InjectView(R.id.edit_btn)
    private Button edit_btn;
	
	@InjectView(R.id.goods_searchButton)
	private Button goods_searchButton;
    
    private ArrayAdapter<String> depAdapter;
    private ArrayAdapter<String> desAdapter;
    private ArrayAdapter<String> typeAdapter;
    
    private String[] depStrings = { "江", "浙", "沪" };
    private String[] desStrings = { "粤", "豫", "湘", "闽" };
    private String[] typeStrings = { "食品", "建材" };
	
	public static final String TAG = GoodActivity.class.getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        initComponent();
}

	private void initComponent() {
		// TODO Auto-generated method stub
		// departure
        // 添加下拉列表数据
        depAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, depStrings);
        // 设置下拉列表风格
        depAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goods_Departure.setAdapter(depAdapter);
         

        // destination
        desAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, desStrings);
        desAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goods_Destination.setAdapter(desAdapter);
        

        // type
        typeAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, typeStrings);
        typeAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goods_Type.setAdapter(typeAdapter);
        
        //weight
        
        
        //return 
        edit_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(GoodActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
			}});
        
        goods_searchButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(GoodActivity.this,GoodResultActivity.class);
				Bundle b =query();

				intent.putExtras(b);
				
				startActivity(intent);
			}});
	}
	
	private Bundle query(){
		Bundle bundle = new Bundle();
		bundle.putString("key_dep", goods_Departure.getSelectedItem().toString());
		bundle.putString("key_des", goods_Destination.getSelectedItem().toString());
		bundle.putString("key_typ", goods_Type.getSelectedItem().toString());
		return bundle;
	}
}
