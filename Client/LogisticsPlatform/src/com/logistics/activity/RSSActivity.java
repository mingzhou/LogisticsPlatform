package com.logistics.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_rss)
public class RSSActivity extends RoboActivity {
	public static final String TAG = RSSActivity.class.getSimpleName();
	public final static int MODE_WORLD_READABLE = 1;
	
	private final String BASE_URL = "http://219.223.190.211";
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	
	@InjectView(R.id.cities)
	private EditText mCities;
	
	@InjectView(R.id.add_btn)
	private Button mAddBtn;
	
	@InjectView(R.id.del_btn)
	private Button del_btn;
	
	private ArrayAdapter<String> citiesAdapter;
	 //private String[] citiesStrings ={} ;
	private ArrayList<String> items = new ArrayList<String>();
	
	private boolean isLongClick = false;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor; 
	 
	 
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        initComponent();
}



	@SuppressLint({ "WorldReadableFiles", "NewApi" })
	private void initComponent() {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();  
        
        Set<String> set = sharedPreferences.getStringSet("cities", null);
        if(set != null)
        {items = new ArrayList<String>(set);}        
        
		GridView gridview = (GridView) findViewById(R.id.gridview);
		citiesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        gridview.setAdapter(citiesAdapter);
		
		mAddBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newcity = mCities.getText().toString();
				if(!newcity.endsWith(" ")&&!newcity.isEmpty())
				{
					citiesAdapter.add(newcity);
					citiesAdapter.notifyDataSetChanged();
					Set<String> set = new HashSet<String>();
					set.addAll(items);
					editor.putStringSet("cities", set);
					editor.commit();
				}
				Log.d("rss",items.toString());
			}});   
		
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!isLongClick){
				Intent intent = new Intent();
				intent.setClass(RSSActivity.this,GoodResultActivity.class);
				intent.putExtra("query", "");
				startActivity(intent);}
			}
		});
		
		gridview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if(!isLongClick){
					isLongClick = true;  
					
					del_btn.setVisibility(View.VISIBLE);
					del_btn.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							del_btn.setVisibility(View.GONE);
							isLongClick = false;  
							items.remove(position);
							Set<String> set = new HashSet<String>();
							set.addAll(items);
							editor.putStringSet("cities", set);
							editor.commit();
							citiesAdapter.notifyDataSetChanged();
						}});
					
					//isLongClick = false;
					return true;
					
				}
				return false;
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
    
    @SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	Set<String> set = new HashSet<String>();
		set.addAll(items);
		editor.putStringSet("cities", set);
		editor.commit();
		
		super.onDestroy();
		
		
	}  
	

}
