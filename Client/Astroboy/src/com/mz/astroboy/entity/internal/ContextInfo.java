package com.mz.astroboy.entity.internal;

import android.content.Context;

import com.google.inject.Inject;

/**
 * 另一个具体的实例，但是这个实例没有继承接口
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class ContextInfo {
	@Inject
	private Context context;
	
	public String getPackageName() {
		return context.getApplicationInfo().packageName;
	}
}
