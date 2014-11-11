package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.http.Header;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("WorldReadableFiles")
@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity  implements IXListViewListener{

	public static final String TAG = MapActivity.class.getSimpleName();
	public static final int MODE_PRIVATE = 0;
	public final static int MODE_WORLD_READABLE = 1; 
	public static final int MODE_APPEND = 32768;
	private final String BASE_URL = "http://219.223.190.211";
	
	public boolean firsttime = false;
	
	/** 准备第一个模板，从字符串中提取出日期数字  */  
    private static String pat1 = "yyyy-MM-dd HH:mm:ss" ;    
    /** 准备第二个模板，将提取后的日期数字变为指定的格式*/    
    private static String pat2 = "yyyy年MM月dd日 HH:mm:ss" ;  
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;           
    @SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ; 
	
	@Inject
	//private AsyncHttpHelper httpHelper;
	private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
	
	@InjectView(R.id.xListView)
	private XListView mListView;
	
	@InjectView(R.id.refresh)
	private Button mRefresh;
	
	private ArrayAdapter<String[]> mAdapter;
	private ArrayList<String[]> items = new ArrayList<String[]>();
	private Handler mHandler;
	
	private JSONArray jArray = new JSONArray();
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		try {
			initComponent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
		
	private void initComponent() throws IOException, JSONException {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(MapActivity.this, NotifyCenter.class);
//		startService(intent);
		
//		Intent intent2 = new Intent(MapActivity.this, PushAndPull.class);
//		startService(intent2);
		
		
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, items){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View view = super.getView(position, convertView, parent);
				String[] entry = items.get(position);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
			    text1.setText(entry[0]);
			    text2.setText(entry[1]);
				return view;
			}
		};
		mListView.setAdapter(mAdapter);
		mListView.setRefreshTime(DateFormat.getTimeFormat(this).format(new Date(System.currentTimeMillis())));
		mListView.setXListViewListener(this);
		mHandler = new Handler();
				
		downFile();
		loadFile();
		Log.d(TAG,"initial");
		if(jArray.length()==0){
			firsttime = true;
		}
		else if(jArray.length()>0){
			try {
				mAdapter.clear();
				for (int i =0; i<jArray.length();i++){
					long dt = jArray.getJSONObject(i).getJSONObject("datetime").getLong("$date");
					Date datetime = new Date(dt);
					sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
					String crawlTime = sdf1.format(datetime);
					String time = getTime(crawlTime);
					mAdapter.add(new String[]{jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"),
		        		time});
		        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
		        }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		
		
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MapActivity.this,ProfileCurrentDealDetailActivity.class);
				
				try {
//					intent.putExtra("from", jArray.getJSONObject(position-1).getString("from"));
//					intent.putExtra("to", jArray.getJSONObject(position-1).getString("to"));
//					intent.putExtra("site", jArray.getJSONObject(position-1).getString("site"));
//					//intent.putExtra("url", jArray.getJSONObject(position-1).getString("url"));
//					long deadline = jArray.getJSONObject(position-1).getJSONObject("deadline").getLong("$date");
//					intent.putExtra("deadline", DateFormat.getDateFormat(MapActivity.this).format(new Date(deadline)));
					intent.putExtra("title","信息详情");
					intent.putExtra("data", jArray.getJSONObject(position-1).toString());
					//Log.d(TAG+"data",jArray.getJSONObject(position).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
				onPause();
			}			
		});
		
		mRefresh.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mAdapter.clear();
				onRefresh();
				
				//items.clear();
				
			}});        
		
	}
	

	private void onLoad() {
		mListView.stopRefresh();
		//mListView.stopLoadMore();
		mListView.setRefreshTime(DateFormat.getTimeFormat(this).format(new Date(System.currentTimeMillis())));
	}
	
	private void onLoad2() {
		mListView.stopLoadMore();
		
		//mListView.stopRefresh();
		//mListView.setRefreshTime(SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
		//mListView.setRefreshTime(DateFormat.getTimeFormat(this).format(new Date(System.currentTimeMillis())));
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//start = ++refreshCnt;
				//mAdapter.clear();
				if(firsttime){
					getHttpResponse1();
					mAdapter.notifyDataSetChanged();
					firsttime = false;
				}else {
				Log.d(TAG,"refresh begin");
				try {
					getHttpResponse();
					mAdapter.notifyDataSetChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				
//				mAdapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_expandable_list_item_1, items);
//				mListView.setAdapter(mAdapter);
				onLoad();
				mListView.setSelection(0);
			}
		}, 2000);
		
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//items.clear();
				if(firsttime){
					getHttpResponse1();
					mAdapter.notifyDataSetChanged();
					firsttime = false;
				}else {
				try {
					getHttpResponse2();
					mAdapter.notifyDataSetChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				//mAdapter.notifyDataSetChanged();
				
				onLoad2();
			}
		}, 2000);
	}
	
	public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);  
        return mer.find();  
    }  
	//for refreshing not the first time
	public void getHttpResponse() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		loadFile();
		Log.d(TAG+"nihao","now jArray"+Integer.toString(jArray.length()));
		
		String jOS = jArray.getJSONObject(0).toString();
		rp.put("data", jOS);
		
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				 try {
					 	downFile(response);
					 	//Log.d(TAG+"nihao","header num"+Integer.toString(headers.length));
					 	int updata_num =0;
					 	for(int i=0;i<headers.length;i++){
						 	Log.d(TAG+"nihao",headers[i].toString());
						 	if(isInteger(headers[i].getValue().toString())){
						 		updata_num = Integer.parseInt(headers[i].getValue());
						 		Log.d("nihao",headers[i].getValue());
						 	}
						 	}
					 	//updata_num = statusCode;
					 	Toast toast = Toast.makeText(MapActivity.this, "更新 "+updata_num, Toast.LENGTH_SHORT);
					 	toast.setGravity(0x00000030, 0, 55);
					 	
					 	toast.show();

					 	loadFile();
					 	mAdapter.clear();
				        Log.d(TAG+"nihao","read done");
				        //Log.d(TAG+"nihao",jArray.toString());
				        for (int i =0; i<jArray.length();i++){
							long dt = jArray.getJSONObject(i).getJSONObject("datetime").getLong("$date");
							Date datetime = new Date(dt);
							sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
							String crawlTime = sdf1.format(datetime);
							String time = getTime(crawlTime);
							mAdapter.add(new String[]{jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"),
				        		time});
				        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
				        }
				        				       
				   	} catch (FileNotFoundException e) {
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
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(MapActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				
			}
						
		};
		httpHelper.post(BASE_URL+"/latest",rp, jrh);
	}
	//for loading for the first time
	public void getHttpResponse1(){
		RequestParams rp = new RequestParams();
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8"){
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				 try {
					 	downFile(response);
					 	//Log.d(TAG+"nihao","header num"+Integer.toString(headers.length));
					 	loadFile();
					 	mAdapter.clear();
					 	for (int i =0; i<jArray.length();i++){
							long dt = jArray.getJSONObject(i).getJSONObject("datetime").getLong("$date");
							Date datetime = new Date(dt);
							sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
							String crawlTime = sdf1.format(datetime);
							String time = getTime(crawlTime);
							mAdapter.add(new String[]{jArray.getJSONObject(i).getString("from")+" -> " + jArray.getJSONObject(i).getString("to"),
				        		time});
				        		//+DateFormat.getDateFormat(GoodResultActivity.this).format(new Date(jArray.getJSONObject(i).getLong("$date"))));
				        }
					 	
				   	} catch (FileNotFoundException e) {
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
		};
		httpHelper.get(BASE_URL+"/latest",rp, jrh);
	}
	//for loading more
	public void getHttpResponse2() throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		//loadFile();
		Log.d("nihao",Integer.toString(jArray.length()));
		String jOS = jArray.getJSONObject(jArray.length()-1).toString();
		rp.put("data", jOS);
				
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8"){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				//super.onFailure(statusCode, headers, responseBody, e);
				Toast.makeText(MapActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(JSONArray response) {
				// TODO Auto-generated method stub
				try {
					//updateFile(response);
				 	//loadFile(jArray);
					int len = jArray.length();
					Log.d("nihao-len",Integer.toString(len));
					for(int i =0;i<response.length();i++){
					jArray.put(jArray.length(), response.getJSONObject(i));					
					long dt = response.getJSONObject(i).getJSONObject("datetime").getLong("$date");
					Date datetime = new Date(dt);
					sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
					String crawlTime = sdf1.format(datetime);
					String time = getTime(crawlTime);
					mAdapter.add(new String[]{response.getJSONObject(i).getString("from")+" -> " + response.getJSONObject(i).getString("to"),
			        		time});
					}
					
			        Log.d(TAG,"update read done");
			       				        
			   	} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		//httpHelper.
		httpHelper.post(BASE_URL+"/previous",rp,jrh);
	}

	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=MapActivity.this.openFileInput("tmp.txt");
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
	
	public void downFile() throws IOException{
		FileOutputStream outStream=MapActivity.this.openFileOutput("tmp.txt",MODE_APPEND);
		outStream.write(new JSONArray().toString().getBytes());
		outStream.close();
	}
	
	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=MapActivity.this.openFileOutput("tmp.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
	}
		
	public void clearArray() throws JSONException{
		jArray = new JSONArray();
		Log.d(TAG+"nihao",jArray.toString());
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
    
    private String getTime(String crawlTime) {
		// TODO Auto-generated method stub
		//yyyy-MM-dd HH:mm:ss
		long nt = System.currentTimeMillis();
		Date nowtime = new Date(nt);
		sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String curTime = sdf1.format(nowtime);
		int yearC = Integer.valueOf(curTime.substring(0, 4));
		int monthC = Integer.valueOf(curTime.substring(5, 7));
		int dayC = Integer.valueOf(curTime.substring(8, 10));
		int hourC = Integer.valueOf(curTime.substring(11, 13));
		int minC = Integer.valueOf(curTime.substring(14, 16));
//		Log.d("time=cur",curTime);
//		Log.d("time",crawlTime);
//				
		int yearT = Integer.valueOf(crawlTime.substring(0, 4));
		int monthT = Integer.valueOf(crawlTime.substring(5, 7));
		int dayT = Integer.valueOf(crawlTime.substring(8, 10));
		int hourT = Integer.valueOf(crawlTime.substring(11, 13));
		int minT = Integer.valueOf(crawlTime.substring(14, 16));
		//int secT = Integer.valueOf(crawlTime.substring(8, 10));
		
		if(yearT != yearC || monthT!= monthC){
			return crawlTime;
		}else if(dayT != dayC){
			if(dayC - dayT > 1){return crawlTime;}
			else return "昨天";
		}else if(hourC!=hourT){
			if(hourC - hourT ==1 && minC< minT){
			return  60+minC-minT+"分钟之前";
		}
			else return hourC-hourT-1+"小时之前";
		}else if (minC-minT>1){
			return minC-minT-1+"分钟之前";
		}else return "刚刚";
		
	}

}
