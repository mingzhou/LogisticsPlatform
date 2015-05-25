package com.logistics.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.logistics.R;


public class GoodFragmentActivity extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return  inflater.inflate(R.layout.activity_good, container, false);

    }

//    private final String BASE_URL = "http://219.223.189.234";
//    private AsyncHttpClient httpHelper = new AsyncHttpClient(80);
//
//    private EditText goods_Departure;
//    private EditText goods_Destination;
//    private View mProgressView;
//    private Button goods_searchButton;
//
//    private GridView gridview;
//
//    private ArrayAdapter<String> depAdapter;
//    private String[] depStrings = { "广东", "上海", "北京" ,"安徽","河南","山东","浙江","四川","辽宁","山西","陕西","江苏","浙江","广西"};
//
//    private Intent intent = new Intent();
//    private Handler mHandler;
//
//    public static final String TAG = GoodFragmentActivity.class.getSimpleName();
//
//    private Boolean isIt = true;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        return  inflater.inflate(R.layout.activity_good, container, false);
//
//    }
//
//
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initComponent();
//    }
//
//    private void initComponent() {
//        goods_searchButton = (Button)goods_searchButton.findViewById(R.id.goods_searchButton);
//        goods_searchButton.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                showProgress(true);
//                try {
//                    getHttpResponse();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                   e.printStackTrace();
//                }}});
//        gridview = (GridView) gridview.findViewById(R.id.gridview);
//        depAdapter = new ArrayAdapter<String> (getActivity(),R.layout.gridview_item, depStrings);
//        gridview.setAdapter(depAdapter);
//        gridview.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // TODO Auto-generated method stub
//                Log.d("cursor-goods_Departure",isIt.toString());
//                Log.d("cursor-goods_Destination",isIt.toString());
//
//                if(isIt){
//                    goods_Departure.setText(depStrings[position]);
//
//                }else{
//                    goods_Destination.setText(depStrings[position]);
//                }
//            }
//        });
//
//        goods_Departure = (EditText) goods_Departure.findViewById(R.id.goods_departure);
//        goods_Departure.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                isIt = true;
//            }});
//
//        goods_Destination = (EditText) goods_Destination.findViewById(R.id.goods_destination);
//        goods_Destination.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                isIt = false;
//            }});
//    }
//
//    public void getHttpResponse() throws IOException, JSONException{
//        RequestParams rp = new RequestParams();
//        String mTo = goods_Destination.getText().toString();
//        String mFrom = goods_Departure.getText().toString();
//        JSONObject tmp = new JSONObject();
//        tmp.put("to", mTo);
//        tmp.put("from", mFrom);
//        Log.d("nihao",tmp.toString());
//        rp.put("data", tmp.toString());
//        JsonHttpResponseHandler jrh = new JsonHttpResponseHandler("UTF-8") {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers,
//                                  JSONArray response) {
//                //Toast.makeText(MapActivity.this, response.toString(), Toast.LENGTH_LONG).show();
//
//                intent.setClass(getActivity(),GoodResultActivity.class);
//                intent.putExtra("query", response.toString());
//                //Log.d("nihao",response.toString());
//                showProgress(false);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers,
//                                  String responseBody, Throwable e) {
//                Toast.makeText(getActivity(), statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
//                showProgress(false);
//            }
//
//        };
//        httpHelper.post(BASE_URL+"/query",rp, jrh);
//    }
//    public void showProgress(final boolean show){
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }



}