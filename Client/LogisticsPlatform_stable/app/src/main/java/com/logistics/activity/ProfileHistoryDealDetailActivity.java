package com.logistics.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.logistics.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@ContentView(R.layout.activity_profile_history_deal_detail)
public class ProfileHistoryDealDetailActivity extends RoboActivity {

    @InjectView(R.id.title_dep)
    private TextView title_dep;

    @InjectView(R.id.title_des)
    private TextView title_des;

    @InjectView(R.id.details_dep)
    private TextView details_dep;

    @InjectView(R.id.details_des)
    private TextView details_des;

    @InjectView(R.id.details_pub_t)
    private TextView details_pub_t;

    @InjectView(R.id.details_dead_t)
    private TextView details_dead_t;

    @InjectView(R.id.details_name)
    private TextView details_name;

    @InjectView(R.id.details_type)
    private TextView details_type;

    @InjectView(R.id.details_vol)
    private TextView details_vol;

    @InjectView(R.id.details_wei)
    private TextView details_wei;

    @InjectView(R.id.details_type_v)
    private TextView details_type_v;

    @InjectView(R.id.details_len_v)
    private TextView details_len_v;

    @InjectView(R.id.details_pack)
    private TextView details_pack;

    @InjectView(R.id.details_fee)
    private TextView details_fee;

    @InjectView(R.id.details_contact)
    private TextView details_contact;

    @InjectView(R.id.details_note)
    private TextView details_note;
		
	@InjectView(R.id.return_btn)
	private TextView return_btn;
	
	@InjectView(R.id.quit_favorite)
	private Button quit_favorite;

    @InjectView(R.id.submit)//其实是网站链接
    private Button submit;

	private String title = null;
	private int position = 0;
	private JSONObject jO = new JSONObject();
	private JSONArray mFav = new JSONArray();	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_history_deal_detail);
		try {
			initComponent();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Boolean getExtras() throws JSONException{
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			title = extras.getString("title");
			position = extras.getInt("position");
			jO = new JSONObject(extras.getString("data"));
			return true;
		}else{
			return false;
		}
	}


	private void initComponent() throws JSONException {
		// TODO Auto-generated method stub
		return_btn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
                onDestroy();
			}});
		
		if(getExtras()){

			long deadline = jO.getJSONObject("deadline").getLong("$date");
			   String deadlineTime = DateFormat.getDateFormat(ProfileHistoryDealDetailActivity.this).format(new Date(deadline));;
            title_dep.setText(jO.getString("from"));
            title_des.setText(jO.getString("to"));
            details_dep.setText(jO.getString("from"));
            details_des.setText(jO.getString("to"));
            details_pub_t.setText(deadlineTime);
            details_dead_t.setText(deadlineTime);
            details_name.setText(jO.getString("title"));
            details_type.setText(jO.getString("type"));
            details_vol.setText(jO.getString("volume"));
            details_wei.setText(jO.getString("quality"));
            details_type_v.setText(jO.getString("vehicle"));
            details_len_v.setText(jO.getString("length"));
            details_pack.setText(jO.getString("packing"));
            details_fee.setText(jO.getString("fee"));
            details_contact.setText("请登录");
            details_note.setText(jO.getString("others"));

            submit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						try {
							Intent it = new Intent();
							it.setClass(ProfileHistoryDealDetailActivity.this,WebDisplayActivity.class);
							it.putExtra("data", jO.getString("url"));
							startActivity(it);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 				         
					}
					   
				   });
			}
		
		quit_favorite.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					loadFile();
					JSONArray tmp = new JSONArray();  
					if(getExtras()){
						int len = mFav.length();
						for(int i=0;i<len;i++){
							if (i != position) 
					        {
								tmp.put(mFav.get(i));
					        }
						}
						
					};
					downFile(tmp);
					quit_favorite.setEnabled(false);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
                onDestroy();
			}});
		
	}
	
	public void loadFile() throws IOException, JSONException{
		FileInputStream inStream=ProfileHistoryDealDetailActivity.this.openFileInput("favorite.txt");
		ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int length=-1;

        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        
        mFav = new JSONArray(stream.toString());
                
        stream.close();
        inStream.close();
	}
	
	public void downFile(JSONArray response) throws IOException{
		FileOutputStream outStream=ProfileHistoryDealDetailActivity.this.openFileOutput("favorite.txt",MODE_PRIVATE);
		outStream.write(response.toString().getBytes());
		outStream.close();
		 Log.d("fav","write done");
	}
	

}
