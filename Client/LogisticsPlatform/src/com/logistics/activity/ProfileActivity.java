package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.logistics.R;
import com.logistics.service.NotifyCenter;
import com.logistics.service.PushAndPull;

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
	
//	@InjectView(R.id.role_id)
//	private TextView role_id;
		
//	@InjectView(R.id.cur_deal)
//	private Button cur_deal;
//	
//	@InjectView(R.id.his_deal)
//	private Button his_deal;
	
	@InjectView(R.id.change_password)
	private Button cha_pa;
	
	@InjectView(R.id.favorite)
	private Button favorite;
	
	@InjectView(R.id.submit)
	private Button submit;
	
	@InjectView(R.id.refreshtime)
	private Spinner refreshtime;
	private ArrayAdapter<String> refreshAdapter;
	private String[] refreshStrings = { "1分钟", "5分钟", "30分钟", "1小时","2小时","6小时","不推送" };
	
	@InjectView(R.id.logout)
	private Button logout;
	
	public final static int MODE_WORLD_READABLE = 1;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor;  
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponent();
	}


	@SuppressLint("WorldReadableFiles")
	private void initComponent() {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();
        String p = sharedPreferences.getString("phone", null);
        String u = sharedPreferences.getString("usr_name", null);
        int i = sharedPreferences.getInt("refresh", 0);
		usr_name.setText(u);
		phone.setText(p);
	//	role_id.setText(i);
		
		cha_pa.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,ProfileChangePasswordActivity.class);
                startActivity(intent);
                onPause();
			}});
		
		favorite.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,ProfileHistoryDealActivity.class);
                startActivity(intent);
                onPause();
                //finish();
                //onDestroy();
			}});
		
		refreshAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, refreshStrings);
		refreshAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		refreshtime.setAdapter(refreshAdapter);
		refreshtime.setSelection(i);
		
		submit.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int r_p = refreshtime.getSelectedItemPosition();
				editor.putInt("refresh", r_p);
				editor.commit();
				Intent startIntent = new Intent(ProfileActivity.this, PushAndPull.class); 
				stopService(startIntent);
				switch (refreshtime.getSelectedItemPosition()){
				default:
					Intent startIntent0 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent0.putExtra("refresh",1);
					startService(startIntent0); 
									 
					break;  
				case 1:
					Intent startIntent1 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent1.putExtra("refresh",5);
					startService(startIntent1); 
					break;  
				case 2:
					Intent startIntent2 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent2.putExtra("refresh",30);
					startService(startIntent2); 
					break;  
				case 3:
					Intent startIntent3 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent3.putExtra("refresh",60);
					startService(startIntent3); 
					break;
				case 4:
					Intent startIntent4 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent4.putExtra("refresh",120);
					startService(startIntent4); 
					break;
				case 5:
					Intent startIntent5 = new Intent(ProfileActivity.this, PushAndPull.class); 
					startIntent5.putExtra("refresh",360);
					startService(startIntent5); 
					break;
				case 6:
					Intent startIntent6 = new Intent(ProfileActivity.this, PushAndPull.class); 
					stopService(startIntent6); 					
					break;  
				} 
			}});
		
		
		
		
		logout.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                Intent stopIntent = new Intent(ProfileActivity.this, PushAndPull.class);  
                stopService(stopIntent); 
                Intent stopIntent1 = new Intent(ProfileActivity.this, NotifyCenter.class);  
                stopService(stopIntent1); 
                finish();
                onDestroy();
			}});
				
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
        PackageManager pm = getPackageManager();    
        ResolveInfo homeInfo =   
            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);   
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            ActivityInfo ai = homeInfo.activityInfo;    
            Intent startIntent = new Intent(Intent.ACTION_MAIN);    
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);    
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));    
            startActivitySafely(startIntent);    
            return true;    
        } else    
            return super.onKeyDown(keyCode, event);    
    }  
    private void startActivitySafely(Intent intent) {    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        try {    
            startActivity(intent);    
        } catch (ActivityNotFoundException e) {    
            Toast.makeText(this, "null",    
                    Toast.LENGTH_SHORT).show();    
        } catch (SecurityException e) {    
            Toast.makeText(this, "null",    
                    Toast.LENGTH_SHORT).show();     
        }    
    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		final int r_p = refreshtime.getSelectedItemPosition();
		editor.putInt("refresh", r_p);
		editor.commit();
		
		super.onDestroy();
		
		
	}  
	
	
}
