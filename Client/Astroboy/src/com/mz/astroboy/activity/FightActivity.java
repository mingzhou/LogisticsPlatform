package com.mz.astroboy.activity;

import java.util.Random;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.inject.Inject;
import com.mz.astroboy.R;
import com.mz.astroboy.entity.IAstroboy;

/**
 * 战斗界面
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@ContentView(R.layout.activity_fight)
public class FightActivity extends RoboActivity {

	@InjectView(R.id.expletive)
	private TextView expletiveText;
	
	@InjectResource(R.anim.animation)	//	绑定了一个资源文件，这里是animation.xml
	private Animation expletiveAnimation;
	
	@Inject
	private Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibrator.vibrate(new long[]{0, 200, 50, 200}, -1);
		
		expletiveText.setAnimation(expletiveAnimation);;
		expletiveAnimation.start();
		
		for (int i = 0; i < 5; i++) {
			new AsyncPunch(this) {
				@Override
				protected void onSuccess(String expletive) throws Exception {
					expletiveText.setText(expletive);
				};
			}.execute();
		}
	}
	
	/**
	 * 后台执行操作，多线程
	 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
	 *
	 */
	public static class AsyncPunch extends RoboAsyncTask<String> {
		@Inject
		private IAstroboy astroboy;
		
		@Inject
		private Random random;
		
		public AsyncPunch(Context context) {
			super(context);
		}
		
		@Override
		public String call() throws InterruptedException {
			Thread.sleep(random.nextInt(5 * 1000));
			return astroboy.punch();
		}
	}
}
