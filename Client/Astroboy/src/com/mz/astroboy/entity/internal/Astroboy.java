package com.mz.astroboy.entity.internal;

import java.util.Random;

import android.app.Application;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mz.astroboy.entity.IAstroboy;

/**
 * 一个具体的实例，继承了一个接口
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@Singleton	//	整个程序只有一个Astroboy实例，此后任何使用@Inject Astroboy astroboy的astroboy都会指向同一个实例。
public class Astroboy implements IAstroboy {
	
	@Inject
	private Application application;
	
	@Inject
	private Vibrator vibrator;
	
	@Inject
	private Random random;
	
	@Override
	public void brushTeeth() {
		vibrator.vibrate(new long[]{0, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50, 200, 50,  }, -1);
	}
	
	@Override
	public String punch() {
		final String expletives[] = new String[]{"POW!", "BANG!", "KERPOW!", "OOF!"};
		return expletives[random.nextInt(expletives.length)];
	}
	
	@Override
	public void say(String something) {
		Toast.makeText(application, "Astroboy says, \"" + something + "\"", Toast.LENGTH_LONG).show();
	}
}
