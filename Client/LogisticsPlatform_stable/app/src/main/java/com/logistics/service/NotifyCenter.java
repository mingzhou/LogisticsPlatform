package com.logistics.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class NotifyCenter extends Service {
	
	public static final String TAG = "NotifyCenter";
	private final String BASE_URL = "http://219.223.189.234";
	
	private ActivityManager activityManager; 
    private String packageName;
    private boolean isStop = false;
    private JSONObject jObj = new JSONObject();//city.txt
    private SharedPreferences sharedPreferences;  
	private ArrayList<String> items = new ArrayList<String>();
        
    private UpdateHandler updateHandler;
    //private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);    
    private ArrayList<String> update_size =  new ArrayList<String>();
    private JSONObject jUS = new JSONObject();
   
    //private int update_size;
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate() executed");
		try {
			downFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	  
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy() executed");
		isStop = true;
		super.onDestroy();
	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		updateHandler = new UpdateHandler();		
		updateThread m1 = new updateThread();
        new Thread(m1).start();
        sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
       // editor = sharedPreferences.edit();  
        
        
		
		activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE); 
        packageName = this.getPackageName();
        
		// TODO Auto-generated method stub
		//return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
	}

	class UpdateHandler extends Handler {
        public UpdateHandler() { }
        public UpdateHandler(Looper L) {
            super(L);
        }
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what!=0){//
					Intent intent = new Intent("com.example.communication.RSSRECEIVER"); 
					intent.putExtra("data",jUS.toString());
					Log.d(TAG+"nihao",jUS.toString());
					sendBroadcast(intent);
			}			
			super.handleMessage(msg);
		}
        
	}
	
	class updateThread implements Runnable {
		
		public updateThread() {
			super();			
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!isStop){
					try {
						loadCityFile();
						Set<String> set = sharedPreferences.getStringSet("cities", null);
				        if(set != null)
				        {items = new ArrayList<String>(set);}  
						int len = items.size();
						Log.d(TAG+"nihao",Integer.toString(len));
						Log.d(TAG+"nihao",jObj.toString());
						//update_size =  new ArrayList<String>();
						jUS = new JSONObject();
						if(len!=0){
						for(int i =0;i<len;i++){
						JSONObject jTmp = new JSONObject();
						jTmp.put("to", items.get(i));
						jTmp.put("from", items.get(i));
						jTmp.put("id", jObj.getString(items.get(i)));
						String jOS = jTmp.toString();
						Log.d("notify",jOS);

                       try{ HttpClient client = new DefaultHttpClient();
                        HttpConnectionParams.setConnectionTimeout(client.getParams(), 1500);
                        HttpConnectionParams.setSoTimeout(client.getParams(), 1500);
                        HttpPost httpRequest =new HttpPost(BASE_URL+"/citytop");
						List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();	
						params.add(new BasicNameValuePair("data",jOS));
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse httpResponse=client.execute(httpRequest);

						if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						Header header = httpResponse.getLastHeader("new");
						Log.d(TAG+"nihao",header.toString());
						//update_size.add(header.getValue());
						jUS.put(items.get(i), header.getValue());}
                        else{jUS.put(items.get(i),0);}}catch (Exception e){jUS.put(items.get(i),0);}


                       }}
						Message msg1 = new Message();
						msg1.what = 1;	
						updateHandler.sendMessage(msg1);
//						String strResult = EntityUtils.toString(httpResponse.getEntity());
//						String temp = URLDecoder.decode(strResult, "UTF_8");
						//Log.v(TAG, "++++++++PushContent : "+ 	temp + "+++++++++");

						
					}  catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
		}	
	}
	
	public void loadCityFile() throws IOException, JSONException{
		FileInputStream inStream=NotifyCenter.this.openFileInput("city.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        jObj = new JSONObject(stream.toString());
        stream.close();
        inStream.close();
    }

    public void downFile() throws IOException{
		FileOutputStream outStream=NotifyCenter.this.openFileOutput("city.txt",MODE_APPEND);
		outStream.write(new JSONObject().toString().getBytes());
		outStream.close();
	}
    
    public void downCityFile(JSONObject response) throws IOException{
		FileOutputStream outStream=NotifyCenter.this.openFileOutput("city.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
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
