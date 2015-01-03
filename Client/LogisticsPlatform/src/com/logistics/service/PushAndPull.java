package com.logistics.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.logistics.R;
import com.logistics.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


@SuppressLint("SimpleDateFormat")
public class PushAndPull extends Service {
	
	public static final String TAG = "PushAndPull";
	private final String BASE_URL = "http://219.223.190.211";
	
	private ActivityManager activityManager; 
    private String packageName;
    private boolean isStop = false;
    
        
    private UpdateHandler updateHandler;
    //private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);    
    private JSONArray jArray = new JSONArray();
    private JSONArray jArray2 =  new JSONArray();
    private JSONObject jO = new JSONObject();
    
    private String latestitem;

    private int update_size =0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

//	@SuppressLint("Wakelock")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate() executed");
//		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//		        "MyWakelockTag");
//		wakeLock.acquire();
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		int refreshtime = intent.getIntExtra("refresh", 1);
		Log.d(TAG, "refreshtime "+refreshtime);
		updateHandler = new UpdateHandler();		
		updateThread m1 = new updateThread(refreshtime);
		new Thread(m1).start();
        
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
			if(msg.what!=0&& update_size > 0 &&msg.arg1==0 ){//
				if(!isAppOnForeground()){
				NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				Intent intent = new Intent(PushAndPull.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);		
				PendingIntent contentIntent = PendingIntent.getActivity(
						PushAndPull.this, 
						R.drawable.icon, 
						intent, 
						PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(PushAndPull.this)
					    .setSmallIcon(R.drawable.icon)
					    .setContentTitle("物流平台")
					    .setContentText("您新收到"+update_size+"条物流信息")
					    .setAutoCancel(true)
					    .setWhen(System.currentTimeMillis())
					    .setSound(soundUri)
					    .setLights(0xff00ff00, 1000, 2000)
					    ;
				mBuilder.setContentIntent(contentIntent);
				
//				final Notification notification = mBuilder.build();
//				startForeground(1, notification);
				nm.notify(1,mBuilder.build());
				Intent bintent = new Intent("com.example.communication.RECEIVER"); 
				bintent.putExtra("data",update_size);
				Log.d(TAG+"nihao",Integer.toString(update_size));
				sendBroadcast(bintent); 
				 jO = new JSONObject();
				try {
					jO.put("num", update_size);
					jO.put("item", latestitem);
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date curDate = new Date(System.currentTimeMillis());
					String curDateS = sdf1.format(curDate);
					jO.put("time", curDateS);
					Log.d(TAG+"nihao","jo"+jO.toString());
					loadFile1();
					if(jArray2.length()==0){
						jArray2.put(0,jO);
						Log.d(TAG+"nihao","j2"+jArray2.toString());
						downFile1(jArray2);
					}
					else if(update_size!= jArray2.getJSONObject(jArray2.length()-1).getInt("num")){
					jArray2.put(jArray2.length(),jO);
					Log.d(TAG+"nihao","j2"+jArray2.toString());
					downFile1(jArray2);}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
				else{
					//Log.d(TAG+"nihao","test");
					Intent intent = new Intent("com.example.communication.RECEIVER"); 
					intent.putExtra("data",update_size);
					sendBroadcast(intent); 
				}
			}else if(msg.what!=0&& update_size > 0 &&msg.arg1!=0 ){
				Intent intent = new Intent("com.example.communication.RECEIVER"); 
				intent.putExtra("data",update_size);
				sendBroadcast(intent);
			}
			
			super.handleMessage(msg);
		}
		        
	}
	
	class updateThread implements Runnable {
		
		private int n;
				
		public updateThread(int n) {
			super();
			this.n = n;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int tmp = n;
			while(!isStop){
				
					try {
						Thread.sleep(60000);
						loadFile();
						//loadFile1();
						String jOS = jArray.getJSONObject(0).toString();
						HttpPost httpRequest =new HttpPost(BASE_URL+"/latest");
						List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();	
						params.add(new BasicNameValuePair("data",jOS));
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
						if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						Header header = httpResponse.getLastHeader("new");
						HttpEntity entity = httpResponse.getEntity();
						String result = retrieveInputStream(entity);  
						JSONArray jResult = new JSONArray(result);
						latestitem = jResult.getJSONObject(0).toString();
						//Log.d(TAG+"nihao",latestitem);
						Log.d(TAG+"nihao",header.toString());
						update_size = Integer.parseInt(header.getValue());
						tmp--;
												
						Message msg1 = new Message();
						msg1.what = 1;
						msg1.arg1 = tmp;					
						updateHandler.sendMessage(msg1);
						if(tmp==0){
							tmp = n;
						}
						}
//						String strResult = EntityUtils.toString(httpResponse.getEntity());
//						String temp = URLDecoder.decode(strResult, "UTF_8");
						//Log.v(TAG, "++++++++PushContent : "+ 	temp + "+++++++++");
						
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
							
			}		
		}	
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=PushAndPull.this.openFileInput("tmp.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        clearArray();
        JSONArray tmp = new JSONArray(stream.toString());
       // Log.d(TAG,tmp.toString());
        for(int i = 0; i<tmp.length();i++){
        	jArray.put(i, tmp.getJSONObject(i));
        }
        Log.d(TAG,"jArray in loadfile()"+Integer.toString(jArray.length()));
        stream.close();
        inStream.close();
        
	}
	
	public void loadFile1() throws IOException, JSONException{
		FileInputStream inStream=PushAndPull.this.openFileInput("push.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        clearArray();
        JSONArray tmp = new JSONArray(stream.toString());
       // Log.d(TAG,tmp.toString());
        for(int i = 0; i<tmp.length();i++){
        	jArray2.put(i, tmp.getJSONObject(i));
        }
        Log.d(TAG,"jArray2 in loadfile()"+Integer.toString(jArray2.length()));
        stream.close();
        inStream.close();
        
	}
	
	protected static String retrieveInputStream(HttpEntity httpEntity) {  
                  
       int length = (int) httpEntity.getContentLength();         
        //the number of bytes of the content, or a negative number if unknown. If the content length is known but exceeds Long.MAX_VALUE, a negative number is returned.  
        //length==-1，下面这句报错，println needs a message  
        if (length < 0) length = 10000;  
        StringBuffer stringBuffer = new StringBuffer(length);  
        try {  
            InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), HTTP.UTF_8);  
            char buffer[] = new char[length];  
            int count;  
            while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {  
                stringBuffer.append(buffer, 0, count);  
            }  
        } catch (UnsupportedEncodingException e) {  
            Log.e(TAG, e.getMessage());  
        } catch (IllegalStateException e) {  
            Log.e(TAG, e.getMessage());  
        } catch (IOException e) {  
            Log.e(TAG, e.getMessage());  
        }  
        return stringBuffer.toString();  
    }  


	public void clearArray() throws JSONException{
		jArray = new JSONArray();
		//Log.d(TAG+"nihao",jArray.toString());
	}
	
	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=PushAndPull.this.openFileOutput("tmp.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
	}
	public void downFile1(JSONArray response) throws IOException{
		FileOutputStream outStream=PushAndPull.this.openFileOutput("push.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
	}
	
	public void downFile() throws IOException{
		FileOutputStream outStream=PushAndPull.this.openFileOutput("push.txt",MODE_APPEND);
		outStream.write(new JSONArray().toString().getBytes());
		outStream.close();
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
