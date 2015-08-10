package com.logistics.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.logistics.R;
import com.logistics.service.NotifyCenter;
import com.logistics.service.PushAndPull;

import me.maxwin.view.BadgeView;

/**
 * 主界面，可以添加不同的选项卡TabHost
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@SuppressWarnings("deprecation")
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

	@InjectView(R.id.main_tabhost)
	private TabHost tabHost;
	
	private LocalActivityManager mlam;

	public final static int MODE_WORLD_READABLE = 1;
	private SharedPreferences sharedPreferences;  
	
	private MsgReceiver msgReceiver;  
	//private Intent mIntent;  
	
	BadgeView badge7;
	ImageView imageView;
	
	BadgeView badge6;
	ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
	
	@SuppressLint({ "WorldReadableFiles", "Wakelock" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = getResources();

		
		mlam = new LocalActivityManager(this, true);
		mlam.dispatchCreate(savedInstanceState);
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);
		int i = sharedPreferences.getInt("refresh", 0);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
		
		msgReceiver = new MsgReceiver();  
        IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction("com.example.communication.RECEIVER");  
        registerReceiver(msgReceiver, intentFilter);  
        
        Intent startRSS = new Intent(MainActivity.this, NotifyCenter.class);
        startService(startRSS);

        WakeLock wakeLock=((PowerManager)getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
        if(wakeLock!=null)
        {
            wakeLock.acquire();//这句执行后，手机将不会休眠，直到执行wakeLock.release();方法
        }

        if(beIn){
		switch (i){
		default:
			Intent startIntent0 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent0.putExtra("refresh",1);
			startService(startIntent0);  
			break;  
		case 1:
			Intent startIntent1 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent1.putExtra("refresh",5);
			startService(startIntent1);
			break;  
		case 2:
			Intent startIntent2 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent2.putExtra("refresh",30);
			startService(startIntent2); 
			break;  
		case 3:
			Intent startIntent3 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent3.putExtra("refresh",60);
			startService(startIntent3); 
			break;
		case 4:
			Intent startIntent4 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent4.putExtra("refresh",120);
			startService(startIntent4); 
			break;
		case 5:
			Intent startIntent5 = new Intent(MainActivity.this, PushAndPull.class); 
			startIntent5.putExtra("refresh",360);
			startService(startIntent5); 
			break;
		case 6:
			Intent startIntent6 = new Intent(MainActivity.this, PushAndPull.class); 
			stopService(startIntent6); 	
			  if(wakeLock!=null)
		        {
		            wakeLock.release();
		        }
			break;  
		}}



		tabHost.setup(mlam);
		
		TabSpec spec = null;
		
		spec = createTabSpec(tabHost, MapActivity.TAG, res, R.string.map_title, R.drawable.ic_source_selected, MapActivity.class);
		tabHost.addTab(spec);
		
//		spec = createTabSpec(tabHost, TruckActivity.TAG, res, R.string.title_activity_truck, R.drawable.ic_tab_stopwatch, TruckActivity.class);
//		tabHost.addTab(spec);
        if(beIn == false){
            spec = createTabSpec(tabHost, RSSActivity.TAG, res, R.string.rss_title, R.drawable.ic_rss_unselected, LoginDetailActivity.class);
            tabHost.addTab(spec);

            spec = createTabSpec(tabHost, GoodActivity.TAG, res, R.string.goods_title, R.drawable.ic_search_unselected, LoginDetailActivity.class);
            tabHost.addTab(spec);

            spec = createTabSpec(tabHost, ProfileActivity.TAG, res, R.string.profile_title, R.drawable.ic_mine_unselected, LoginDetailActivity.class);
            tabHost.addTab(spec);
        }
        else{
        spec = createTabSpec(tabHost, RSSActivity.TAG, res, R.string.rss_title, R.drawable.ic_rss_unselected, RSSActivity.class);
        tabHost.addTab(spec);

		spec = createTabSpec(tabHost, GoodActivity.TAG, res, R.string.goods_title, R.drawable.ic_search_unselected, GoodActivity.class);
		tabHost.addTab(spec);
		
		spec = createTabSpec(tabHost, ProfileActivity.TAG, res, R.string.profile_title, R.drawable.ic_mine_unselected, ProfileActivity.class);
		tabHost.addTab(spec);
        }



        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {

                if(MapActivity.TAG.equals(tabId)) {
                    //destroy earth
                    View mView = tabHost.getTabWidget().getChildAt(0);
                    imageView = (ImageView)mView.findViewById(R.id.tab_icon);
                    imageView.setImageResource(R.drawable.ic_source_selected);

                    View mView1 = tabHost.getTabWidget().getChildAt(1);
                    imageView1 = (ImageView)mView1.findViewById(R.id.tab_icon);
                    imageView1.setImageResource(R.drawable.ic_rss_unselected);

                    View mView2 = tabHost.getTabWidget().getChildAt(2);
                    imageView2 = (ImageView)mView2.findViewById(R.id.tab_icon);
                    imageView2.setImageResource(R.drawable.ic_search_unselected);

                    View mView3 = tabHost.getTabWidget().getChildAt(3);
                    imageView3 = (ImageView)mView3.findViewById(R.id.tab_icon);
                    imageView3.setImageResource(R.drawable.ic_mine_unselected);

                   // badge7.hide();
                }
                if(RSSActivity.TAG.equals(tabId)) {
                    //destroy mars
                    View mView = tabHost.getTabWidget().getChildAt(0);
                    imageView = (ImageView)mView.findViewById(R.id.tab_icon);
                    imageView.setImageResource(R.drawable.ic_source_unselected);

                    View mView1 = tabHost.getTabWidget().getChildAt(1);
                    imageView1 = (ImageView)mView1.findViewById(R.id.tab_icon);
                    imageView1.setImageResource(R.drawable.ic_rss_selected);

                    View mView2 = tabHost.getTabWidget().getChildAt(2);
                    imageView2 = (ImageView)mView2.findViewById(R.id.tab_icon);
                    imageView2.setImageResource(R.drawable.ic_search_unselected);

                    View mView3 = tabHost.getTabWidget().getChildAt(3);
                    imageView3 = (ImageView)mView3.findViewById(R.id.tab_icon);
                    imageView3.setImageResource(R.drawable.ic_mine_unselected);
                }
                if(GoodActivity.TAG.equals(tabId)){
                    View mView = tabHost.getTabWidget().getChildAt(0);
                    imageView = (ImageView)mView.findViewById(R.id.tab_icon);
                    imageView.setImageResource(R.drawable.ic_source_unselected);

                    View mView1 = tabHost.getTabWidget().getChildAt(1);
                    imageView1 = (ImageView)mView1.findViewById(R.id.tab_icon);
                    imageView1.setImageResource(R.drawable.ic_rss_unselected);

                    View mView2 = tabHost.getTabWidget().getChildAt(2);
                    imageView2 = (ImageView)mView2.findViewById(R.id.tab_icon);
                    imageView2.setImageResource(R.drawable.ic_search_selected);

                    View mView3 = tabHost.getTabWidget().getChildAt(3);
                    imageView3 = (ImageView)mView3.findViewById(R.id.tab_icon);
                    imageView3.setImageResource(R.drawable.ic_mine_unselected);
                }
                if(ProfileActivity.TAG.equals(tabId)){
                    View mView = tabHost.getTabWidget().getChildAt(0);
                    imageView = (ImageView)mView.findViewById(R.id.tab_icon);
                    imageView.setImageResource(R.drawable.ic_source_unselected);

                    View mView1 = tabHost.getTabWidget().getChildAt(1);
                    imageView1 = (ImageView)mView1.findViewById(R.id.tab_icon);
                    imageView1.setImageResource(R.drawable.ic_rss_unselected);

                    View mView2 = tabHost.getTabWidget().getChildAt(2);
                    imageView2 = (ImageView)mView2.findViewById(R.id.tab_icon);
                    imageView2.setImageResource(R.drawable.ic_search_unselected);

                    View mView3 = tabHost.getTabWidget().getChildAt(3);
                    imageView3 = (ImageView)mView3.findViewById(R.id.tab_icon);
                    imageView3.setImageResource(R.drawable.ic_mine_selected);
                }

            }});
        tabHost.setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabHost.setCurrentTab(1);
                tabHost.setCurrentTab(0);
            }
        });

		View mView = tabHost.getTabWidget().getChildAt(0); 
		imageView = (ImageView)mView.findViewById(R.id.tab_icon);
		badge7 = new BadgeView(MainActivity.this, imageView);
		
		View mView1 = tabHost.getTabWidget().getChildAt(1);
		imageView1 = (ImageView)mView1.findViewById(R.id.tab_icon);
		badge6 = new BadgeView(MainActivity.this, imageView1);
		
	}


	
	@SuppressLint("InflateParams")
	private TabSpec createTabSpec(TabHost tabHost, String tag,
        Resources res, int labelId, int iconId, Class<?> cls) {
		TabSpec spec = tabHost.newTabSpec(tag);
		String label = res.getString(labelId);
		Drawable icon = res.getDrawable(iconId);

		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab, null);
		((ImageView) linearLayout.findViewById(R.id.tab_icon)).setImageDrawable(icon);
		((TextView) linearLayout.findViewById(R.id.tab_label)).setText(label);
		spec.setIndicator(linearLayout);
		spec.setContent(new Intent().setClass(this, cls));
				
		return spec;

	}



	@Override
	protected void onResume() {
		super.onResume();
		mlam.dispatchResume();
//        if(tabHost.getCurrentTab()==0){
//            badge7.hide();
//        }
        int i = sharedPreferences.getInt("refresh", 0);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
        if(beIn){
            switch (i){
                default:
                    Intent startIntent0 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent0.putExtra("refresh",1);
                    startService(startIntent0);
                    break;
                case 1:
                    Intent startIntent1 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent1.putExtra("refresh",5);
                    startService(startIntent1);
                    break;
                case 2:
                    Intent startIntent2 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent2.putExtra("refresh",30);
                    startService(startIntent2);
                    break;
                case 3:
                    Intent startIntent3 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent3.putExtra("refresh",60);
                    startService(startIntent3);
                    break;
                case 4:
                    Intent startIntent4 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent4.putExtra("refresh",120);
                    startService(startIntent4);
                    break;
                case 5:
                    Intent startIntent5 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent5.putExtra("refresh",360);
                    startService(startIntent5);
                    break;
                case 6:
                    Intent startIntent6 = new Intent(MainActivity.this, PushAndPull.class);
                    stopService(startIntent6);
                    break;
            }}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mlam.dispatchPause(isFinishing());

//        if(tabHost.getCurrentTab()==0){
//            badge7.hide();
//        }
        int i = sharedPreferences.getInt("refresh", 0);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
        if(beIn){
            switch (i){
                default:
                    Intent startIntent0 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent0.putExtra("refresh",1);
                    startService(startIntent0);
                    break;
                case 1:
                    Intent startIntent1 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent1.putExtra("refresh",5);
                    startService(startIntent1);
                    break;
                case 2:
                    Intent startIntent2 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent2.putExtra("refresh",30);
                    startService(startIntent2);
                    break;
                case 3:
                    Intent startIntent3 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent3.putExtra("refresh",60);
                    startService(startIntent3);
                    break;
                case 4:
                    Intent startIntent4 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent4.putExtra("refresh",120);
                    startService(startIntent4);
                    break;
                case 5:
                    Intent startIntent5 = new Intent(MainActivity.this, PushAndPull.class);
                    startIntent5.putExtra("refresh",360);
                    startService(startIntent5);
                    break;
                case 6:
                    Intent startIntent6 = new Intent(MainActivity.this, PushAndPull.class);
                    stopService(startIntent6);
                    break;
            }}
	}
	
	/** 
     * 广播接收器 
     * @author len from csdn
     * 
     */  
    public class MsgReceiver extends BroadcastReceiver{  
    		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("foregroudn","broadcast came");
			int i = intent.getIntExtra("data", 0);
			boolean back = intent.getBooleanExtra("background", true);
			Log.d("foregroudn","we get num is "+i);			
			Log.d("foregroudn","we get num is "+back);	
			
			
			if(i>100){
			badge7.setText("100+");
			badge7.show();
			if(!back){
				//Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				//vibrator.vibrate(1000);
				}}
			else if(i>0){
				badge7.setText(Integer.toString(i));
				badge7.show();
				if(!back){
				//Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				//vibrator.vibrate(1000);
				}
			}else{
				badge7.hide();
			}
			
			
			//badge7.toggle();
			 
			
		}  
		
		
          
    }  

	
	
}
