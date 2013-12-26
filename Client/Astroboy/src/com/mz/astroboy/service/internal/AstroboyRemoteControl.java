package com.mz.astroboy.service.internal;

import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;
import android.app.Activity;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mz.astroboy.entity.IAstroboy;
import com.mz.astroboy.service.IAstroboyRemoteControl;

/**
 * 封装了Astroboy的控制器，在更高级别上进行抽象
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@ContextSingleton	//	顾名思义，在Context里面的唯一的，不同的Context里面则是不同的，比如MainActivity和FightActivity就是两个不同的Context。
public class AstroboyRemoteControl implements IAstroboyRemoteControl {	
	@Inject
	private IAstroboy astroboy;
	
	@Inject
	private Activity activity;
	
	@Override
	public void brushTeeth() {
		//	日志功能
		Ln.d("Sent brushTeeth command to Astroboy");
		astroboy.brushTeeth();
	}
	
	@Override
	public void say(String something) {
		Ln.d("Sent say(" + something + ") command to Astroboy");
		astroboy.say(something);
	}
	
	@Override
	public void selfDestruct() {
		Toast.makeText(activity, "Your evil remote control has exploded! Now Astroboy is FREEEEEEEEEE!", Toast.LENGTH_LONG).show();
        activity.finish();
	}
}
