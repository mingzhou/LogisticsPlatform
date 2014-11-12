package com.logistics.service;

import java.util.List;

import com.logistics.R;
import com.logistics.activity.MainActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class NotifyCenter extends Service {
	
	public static final String TAG = "NotifyCenter";
	private ActivityManager activityManager; 
    private String packageName;
    private boolean isStop = false;
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;//new MsgBinder();
	}
		
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "NotifyCenter onCreate() executed");
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy() executed");
		isStop = true;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE); 
        packageName = this.getPackageName();
        
       
        
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

}
