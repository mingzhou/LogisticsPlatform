package com.mz.astroboy.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.inject.Inject;
import com.mz.astroboy.R;
import com.mz.astroboy.entity.internal.ContextInfo;
import com.mz.astroboy.service.IAstroboyRemoteControl;

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
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sayText.setText(contextInfo.getPackageName());
		
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
				//	Activity之间通过Intent机制实现跳转
				startActivity(new Intent(MainActivity.this, FightActivity.class));
			}
		});
	}
	
}
