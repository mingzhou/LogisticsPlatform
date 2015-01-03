package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.maxwin.view.BadgeView;

import org.apache.http.Header;
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

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.logistics.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
	
	private BadgeAdapter cityListAdapter;
	//private ArrayAdapter<String> mAdapter;
	private ArrayList<String> city = new ArrayList<String>();
	//private static ArrayList<String> update_size =  new ArrayList<String>();
	private static JSONObject jUS = new JSONObject();	
	
	private boolean isLongClick = false;
	
	private SharedPreferences sharedPreferences;  
	private SharedPreferences.Editor editor; 
	
	private Intent intent = new Intent();
	
	private JSONObject jObj = new JSONObject();//city.txt
		
	private RSSReceiver rssReceiver;
	 
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        try {
			initComponent();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
}



	@SuppressLint({ "WorldReadableFiles", "NewApi" })
	private void initComponent() throws IOException, JSONException {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);  
        editor = sharedPreferences.edit();  
        
        downFile();
        loadCityFile();
        
        rssReceiver = new RSSReceiver();  
        IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction("com.example.communication.RSSRECEIVER");  
        registerReceiver(rssReceiver, intentFilter); 
        
        Set<String> set = sharedPreferences.getStringSet("cities", null);
        if(set != null)
        {
        	city = new ArrayList<String>(set);
        	int len = city.size();
			jUS = new JSONObject();
			if(len!=0){
			for(int i =0;i<len;i++){
			JSONObject jTmp = new JSONObject();
			jTmp.put("to", city.get(i));
			jTmp.put("from", city.get(i));
			jTmp.put("id", jObj.getString(city.get(i)));
			String jOS = jTmp.toString();
			Log.d("notify",jOS);
			HttpPost httpRequest =new HttpPost(BASE_URL+"/citytop");
			List<BasicNameValuePair> params=new ArrayList<BasicNameValuePair>();	
			params.add(new BasicNameValuePair("data",jOS));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			Header header = httpResponse.getLastHeader("new");
			Log.d(TAG+"nihao",header.toString());
			//update_size.add(header.getValue());
			jUS.put(city.get(i), header.getValue());}}}
        }        
        
        ListView gridview = (ListView) findViewById(R.id.list_rsscities);
        cityListAdapter = new BadgeAdapter(city,this);
        gridview.setAdapter(cityListAdapter);
		
		mAddBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newcity = mCities.getText().toString();
				if(!newcity.endsWith(" ")&&!newcity.isEmpty()&&!city.contains(newcity))
				{					
					city.add(newcity);
					cityListAdapter.city = city;
					cityListAdapter.notifyDataSetChanged();
					
					try {
						jObj.put(newcity, "");
						jUS.put(newcity, "0");
						downCityFile(jObj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Set<String> set = new HashSet<String>();
					set.addAll(city);
					editor.putStringSet("cities", set);
					editor.commit();
				}
				
			}});   
		
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!isLongClick){
				try {
					
					getHttpResponse(position);
									
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
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
							jObj.remove(city.get(position));
							jUS.remove(city.get(position));
							try {
								downCityFile(jObj);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							city.remove(position);
							//update_size.remove(position);
							Set<String> set = new HashSet<String>();
							set.addAll(city);
							editor.putStringSet("cities", set);
							editor.commit();
							cityListAdapter.notifyDataSetChanged();
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
		set.addAll(city);
		editor.putStringSet("cities", set);
		editor.commit();
		
		super.onDestroy();
	}
    
    @SuppressLint("NewApi")
	public void getHttpResponse(int position) throws IOException, JSONException{
		RequestParams rp = new RequestParams();
		
//		Set<String> set = sharedPreferences.getStringSet("cities", null);
//        if(set != null)
//        {city = new ArrayList<String>(set);}  
        
		String mTo = city.get(position);
		final String mFrom = city.get(position);
		String mItem = "";
		if(!jObj.isNull(mFrom))
		{
			mItem = jObj.getString(mFrom);
		}
			
		JSONObject tmp = new JSONObject();
		tmp.put("to", mTo);
		tmp.put("from", mFrom);
		tmp.put("id", mItem);
		Log.d("nihao-RSS",tmp.toString());
		rp.put("data", tmp.toString());
		
		JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
					//Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
				
				try {
					jObj.put(mFrom, response.getJSONObject(0).getJSONObject("_id").getString("$oid"));
					jUS.put(mFrom, "0");
					Log.d("nihao-RSS",jObj.toString());
					downCityFile(jObj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				intent.setClass(RSSActivity.this,GoodResultActivity.class);
				intent.putExtra("query", response.toString());
				//Log.d("nihao-RSS",response.toString());
				startActivity(intent);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Toast.makeText(RSSActivity.this, statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
				intent.setClass(RSSActivity.this,GoodResultActivity.class);
				intent.putExtra("query", "");
				//Log.d("nihao",response.toString());
				startActivity(intent);
			}
						
		};
		httpHelper.post(BASE_URL+"/citytop",rp, jrh);
	}
	
    public void loadCityFile() throws IOException, JSONException{
		FileInputStream inStream=RSSActivity.this.openFileInput("city.txt");
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
		FileOutputStream outStream=RSSActivity.this.openFileOutput("city.txt",MODE_APPEND);
		outStream.write(new JSONObject().toString().getBytes());
		outStream.close();
	}
    
    public void downCityFile(JSONObject response) throws IOException{
		FileOutputStream outStream=RSSActivity.this.openFileOutput("city.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d(TAG,"write done");
	}
    
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cityListAdapter.notifyDataSetChanged();
	}



	public class RSSReceiver extends BroadcastReceiver{  
		
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			Log.d("foregroudn","broadcast came");
			
			//update_size = intent.getStringArrayListExtra("data");
			Bundle extras = intent.getExtras();
			if(extras != null){
			try {
				jUS = new JSONObject(extras.getString("data"));
				Log.d(TAG+"nihao",jUS.toString());
				
				cityListAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};}
			
//			if(update_size.size()>0){
//								
//				Set<String> set = sharedPreferences.getStringSet("cities", null);
//		        if(set != null){
//		        	city = new ArrayList<String>(set);
//		        }
//		       				
//				cityListAdapter.notifyDataSetChanged();
//			 }
		}  
          
    }
	
		
	private static class BadgeAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private ArrayList<String> city;
                
        private static final int droidGreen = Color.parseColor("#A4C639");
        
        public BadgeAdapter(ArrayList<String> arraylist0,Context context) {
            super();
        	mInflater = LayoutInflater.from(context);
            mContext = context;
            this.city = arraylist0;
            //this.number = arraylist1;            
        }

        public int getCount() {
            return city.size();
        }

        public Object getItem(int position) {
        	if (position < getCount()) {
        		return city.get(position);
        		//number.get(position);
        	}
        	return null;
        }
        
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);
                holder.badge = new BadgeView(mContext, holder.text);
                holder.badge.setBadgeBackgroundColor(droidGreen);
                holder.badge.setTextColor(Color.BLACK);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(city.get(position));
            
            try {
            	String city_num = jUS.get(city.get(position)).toString();
            	holder.badge.setText(city_num);
            	holder.badge.show();
            	if(Integer.parseInt(city_num)==0){
                	holder.badge.hide();
                }            	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            
            return convertView;
        }

        static class ViewHolder {
            TextView text;
            BadgeView badge;
        }
    }
	
	
}
