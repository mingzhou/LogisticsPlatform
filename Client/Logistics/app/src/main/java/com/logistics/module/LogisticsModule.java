package com.logistics.module;

import com.google.inject.AbstractModule;
import com.logistics.utils.DatabaseHelper;

/**
 * 模块化安装各个组件
 * 实现了接口和服务的分离
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class LogisticsModule extends AbstractModule {
	
	@Override
	protected void configure() {
		//	指定要使用的interface是由哪个具体的service实现的
		
		//	如果直接使用具体的service，当然就不需要再绑定了，比如这里的com.mz.astroboy.entity.internal.ContextInfo
		requestStaticInjection(DatabaseHelper.class);
	}

}
