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

@ContentView(R.layout.activity_profile_truck_deal_detail)
public class ProfileTruckDealDetailActivity extends RoboActivity {

    @InjectView(R.id.return_btn)
    private Button return_btn;

    @InjectView(R.id.notify_source)
    private Button notify_source;

    @InjectView(R.id.goods_detail_title)
    private TextView goods_detail_title;

    @InjectView(R.id.goods_detail_info)
    private TextView goods_detail_info;

    @InjectView(R.id.source_detail_info)
    private TextView source_detail_info;

    private String title = null;
    private JSONObject jO = new JSONObject();
    //private JSONObject mMap = new JSONObject();
    private JSONArray mFav= new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_current_deal_detail);
        try {
            initComponent();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Boolean getExtras() throws JSONException{
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            title = extras.getString("title");
            jO = new JSONObject(extras.getString("data"));
            Log.d("truck",jO.toString());
            return true;
        }else{
            return false;
        }
    }

    private void initComponent() throws JSONException, IOException {
        // TODO Auto-generated method stub

        if (getExtras()) {
            Log.d("nihao-time","deadline");
            goods_detail_title.setText(title);
            long deadline = jO.getJSONObject("deadline").getLong("$date");
            String deadlineTime = DateFormat.getDateFormat(ProfileTruckDealDetailActivity.this).format(new Date(deadline));;
            goods_detail_info.setText("出发地："+jO.getString("from")+"\n"
                    +"到达地："+jO.getString("to")+"\n"
                    +"截止时间:"+deadlineTime+"\n"
                    +"车牌号:"+jO.getString("number")+"\n"
                    +"联系电话:"+jO.getString("telephone")+"\n"
                    +"手机:"+jO.getString("mobile")+"\n"
                    +"载重:"+jO.getString("quality")+"\n"
                    +"联系单位或人:"+jO.getString("contact")+"\n"
                    +"货车类型:"+jO.getString("vehicle")+"\n"
                    +"货车长度:"+jO.getString("length")+"\n"
                    +"预计费用:"+jO.getString("fee")+"\n"
                    +"备注:"+jO.getString("others")+"\n");
            SpannableString siteString = new SpannableString("来源："+jO.getString("site"));
            siteString.setSpan(new UnderlineSpan(), 3, siteString.length(), 0);
            siteString.setSpan(new StyleSpan(Typeface.ITALIC), 3, siteString.length(), 0);
            source_detail_info.setText(siteString);

            source_detail_info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {
//					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(jO.getString("url")));
//					it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");  
                        Intent it = new Intent();
                        it.setClass(ProfileTruckDealDetailActivity.this,WebDisplayActivity.class);
                        it.putExtra("data", jO.getString("url"));
                        startActivity(it);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            });
        }

        return_btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                onDestroy();

            }});

        downFile();
        loadFile();
        Log.d("fav",Integer.toString(mFav.length()));
        if(has(mFav,jO)){
            notify_source.setEnabled(false);
            notify_source.setText("已收藏");
            Log.d("fav-has",mFav.toString());
        }
        else{
            notify_source.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        mFav.put(jO);
                        notify_source.setEnabled(false);
                        notify_source.setText("已收藏");
                        downFile(mFav);
                        Log.d("fav",Integer.toString(mFav.length()));
                        Log.d("fav",mFav.toString());

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }});}

    }


    private boolean has(JSONArray jAr, JSONObject jOb) throws JSONException {
        // TODO Auto-generated method stub
        for(int i=0 ;i<jAr.length();i++){
            if(jOb.getJSONObject("_id").getString("$oid").
                    equals(jAr.getJSONObject(i).getJSONObject("_id").getString("$oid"))){
                return true;
            }
        }
        return false;
    }

    public void downFile(JSONArray response) throws IOException{
        FileOutputStream outStream=ProfileTruckDealDetailActivity.this.openFileOutput("favorite.txt",MODE_PRIVATE);
        outStream.write(response.toString().getBytes());
        outStream.close();
        Log.d("fav","write done");
    }

    public void downFile() throws IOException{
        FileOutputStream outStream=ProfileTruckDealDetailActivity.this.openFileOutput("favorite.txt",MODE_APPEND);
        outStream.write(new JSONArray().toString().getBytes());
        Log.d("fav","create done");
        outStream.close();
    }

    public void loadFile() throws IOException, JSONException{
        FileInputStream inStream=ProfileTruckDealDetailActivity.this.openFileInput("favorite.txt");
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int length=-1;
        Log.d("fav","read done0");
        while((length=inStream.read(buffer))!=-1)   {
            stream.write(buffer,0,length);
        }
        stream.close();
        inStream.close();
        //Log.d("fav",stream.toString()); 

        mFav = new JSONArray(stream.toString());

        Log.d("fav","read done");


    }

}
