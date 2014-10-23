package com.logistics.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.logistics.R;
import com.logistics.activity.MainActivity;
import com.logistics.activity.MapActivity;
import com.logistics.utils.AsyncHttpHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


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
	
	public class MsgBinder extends Binder{  
        /** 
         * 获取当前Service的实例 
         * @return 
         */  
        public NotifyCenter getService(){  
            return NotifyCenter.this;  
        }  
    }  
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate() executed");
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cancelNotification();
		Log.d(TAG, "onDestroy() executed");
		isStop = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE); 
        packageName = this.getPackageName();
        
        notifyCheck();
        
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	public void notifyCheck(){
		new Thread() { 
            public void run() { 
            	
                try { 
                    while (!isStop) { 
                    	Thread.sleep(5000); 
                        if (isAppOnForeground()) { 
                            Log.v(TAG, "前台运行");
                        } else { 
                            Log.v(TAG, "后台运行");
                            showNotification();
                        } 
                    } 
                } catch (Exception e) { 
                    e.printStackTrace(); 
                    Log.v(TAG+"nihao", "问题在这里");
                } 
            } 
        }.start(); 
              
	}
	
	
	protected void showNotification() {
		// TODO Auto-generated method stub
		
//		//Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		
//		 // 创建一个NotificationManager的引用
//        NotificationManager notificationManager = (
//        		NotificationManager)getSystemService(
//        				android.content.Context.NOTIFICATION_SERVICE);
//        
//        // 定义Notification的各种属性
//        Notification notification = new Notification(
//        		R.drawable.icon,"物流平台", 
//        		System.currentTimeMillis());
//        		
//        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        // 点击后自动清除Notification
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//        notification.defaults = Notification.DEFAULT_LIGHTS;
//        notification.ledARGB = Color.BLUE;
//        notification.ledOnMS = 5000;
//        //notification.sound = soundUri;
//        ++notification.number;
//        
//        CharSequence contentTitle = "物流平台"; // 通知栏标题
//        CharSequence contentText = "推送信息显示，请查看……"; // 通知栏内容        
//        Intent notificationIntent = new Intent(this,MainActivity.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(
//        		this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//		
//        notificationIntent.setAction(Intent.ACTION_MAIN);
//        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        
//        notification.setLatestEventInfo(
//        		this, contentTitle, contentText, contentIntent);
//        
//        // 把Notification传递给NotificationManager
//        notificationManager.notify(0, notification);
		
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(R.drawable.icon)
			    .setContentTitle("物流平台")
			    .setContentText("推送信息显示，请查看……")
			    ;
		
		Intent notificationIntent = new Intent(this,MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
        		this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(contentIntent);
		int mNotificationId = 001;
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
        
    }
	
	// 取消通知
	public void cancelNotification(){
		NotificationManager notificationManager = (
		NotificationManager) getSystemService(
				android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
	

	protected boolean isAppOnForeground() {
		// TODO Auto-generated method stub
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
        if (appProcesses == null) return false; 
        
        for (RunningAppProcessInfo appProcess : appProcesses) { 
            // The name of the process that this object is associated with. 
            if (appProcess.processName.equals(packageName) 
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) { 
                return true; 
            } 
        } 
		return false;
	}

}
