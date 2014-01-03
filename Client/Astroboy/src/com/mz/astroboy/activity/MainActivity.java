package com.mz.astroboy.activity;

import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.inject.Inject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mz.astroboy.R;
import com.mz.astroboy.entity.Account;
import com.mz.astroboy.entity.internal.ContextInfo;
import com.mz.astroboy.service.IAstroboyRemoteControl;
import com.mz.astroboy.utils.AsyncHttpHelper;
import com.mz.astroboy.utils.DatabaseHelper;

/**
 * 主界面
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@ContentView(R.layout.activity_main)	//	绑定了activity_main.xml
public class MainActivity extends RoboActivity {
	@InjectView(R.id.self_destruct)		//	绑定了self_destruct
	private Button selfDestructButton;
	
	@InjectView(R.id.say_text)   
	private EditText sayText;
	
	@InjectView(R.id.brush_teeth)
	private Button brushTeethButton;
	
	@InjectView(tag="fightevil") 
	private Button fightEvilButton;
	
	@Inject								//	绑定了一个普通Java接口
	private IAstroboyRemoteControl remoteControl;
	
	@Inject
	private Vibrator vibrator;					//	绑定了一个普通的Android系统服务

	@Inject
	private ContextInfo contextInfo;
	
	@Inject
	private DatabaseHelper databaseHelper;		//	数据库
	
//	@Inject
//	private HttpHelper httpHelper;
	
	@Inject
	private AsyncHttpHelper httpHelper;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		sayText.setText(contextInfo.getPackageName());
		
		testDatabase();
		
		sayText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				remoteControl.say(textView.getText().toString());
				textView.setText(null);
				return true;
			}
		});
		
		brushTeethButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				remoteControl.brushTeeth();
				httpHelper.get("/file/1.json", new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onSuccess(JSONObject response) {
						Toast.makeText(contextInfo.getContext(), response.toString(), Toast.LENGTH_LONG).show();
						sayText.setText(response.toString());
					}
					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseBody, Throwable e) {
						Toast.makeText(contextInfo.getContext(), statusCode + "\t" + responseBody, Toast.LENGTH_LONG).show();
						sayText.setText(statusCode + "\t" + responseBody);
					}
				});
			}
		});
		
		selfDestructButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				vibrator.vibrate(2000);
				remoteControl.selfDestruct();;
			}
		});
		
		fightEvilButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				httpHelper.get("/file/1.txt", new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						Toast.makeText(contextInfo.getContext(), statusCode + "\t" + content, Toast.LENGTH_LONG).show();
					}
				});
				
				//	Activity之间通过Intent机制实现跳转
				startActivity(new Intent(MainActivity.this, FightActivity.class));
			}
		});
	}
	
	private void testDatabase() {
		//	Data Access Object (DAO) 进行增删改查操作。
		RuntimeExceptionDao<Account, Long> accountDao = databaseHelper.getRuntimeExceptionDao(Account.class);
		
		for (int i = 0; i < 3; i++) {
			Account account = new Account();
			account.setUsername("user_" + i);
			account.setPassword("pwd_" + i);
			account.setRegisterDate(new Date(5000000 * 5));
			accountDao.create(account);
		}
		
		List<Account> list = accountDao.queryForAll();
		for (int i = 0; i < list.size(); i++) {
			Account account = list.get(i);
			Toast.makeText(this, account.toString(), Toast.LENGTH_LONG).show();
			if (i != 0) {
				//	第一个元素不删除，检查是否真的存入了数据库
				accountDao.delete(account);
			}			
		}
		
	}
}
