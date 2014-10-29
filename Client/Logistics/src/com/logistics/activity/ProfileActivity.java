package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.logistics.R;

/**
 * 个人中心
 * @author Li.ZHuo (xiaojue1990 @ foxmail.com)
 *
 */
@ContentView(R.layout.activity_profile)
public class ProfileActivity extends RoboActivity {

	public static final String TAG = ProfileActivity.class.getSimpleName();
	
	@InjectView(R.id.usr_name)
	private TextView usr_name;
	
	@InjectView(R.id.phone)
	private TextView phone;
	
	@InjectView(R.id.role_id)
	private TextView role_id;
	
//	@InjectView(R.id.cur_deal)
//	private Button cur_deal;
//	
//	@InjectView(R.id.his_deal)
//	private Button his_deal;
	
	@InjectView(R.id.change_password)
	private Button cha_pa;
	
	@InjectView(R.id.change_phone)
	private Button cha_ph;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;  
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponent();
}


	private void initComponent() {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();
        String p = sharedPreferences.getString("phone", null);
        String u = sharedPreferences.getString("usr_name", null);
        String i = sharedPreferences.getString("role_id", null);
		usr_name.setText(u);
		phone.setText(p);
		role_id.setText(i);
		
//		cur_deal.setOnClickListener(new Button.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//                intent.setClass(ProfileActivity.this,ProfileCurrentDealActivity.class);
//                startActivity(intent);
//                onPause();
//			}});
//		
//		his_deal.setOnClickListener(new Button.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//                intent.setClass(ProfileActivity.this,ProfileHistoryDealActivity.class);
//                startActivity(intent);
//                onPause();
//			}});
		
		cha_pa.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,ProfileChangePasswordActivity.class);
                startActivity(intent);
                onPause();
			}});
		
		cha_ph.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,ProfileChangePhoneActivity.class);
                startActivity(intent);
                onPause();
                //finish();
                //onDestroy();
			}});
		
		
		
	}
	
}
