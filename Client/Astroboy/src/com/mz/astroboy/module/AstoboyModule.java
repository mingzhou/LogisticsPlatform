package com.mz.astroboy.module;

import com.google.inject.AbstractModule;
import com.mz.astroboy.entity.IAstroboy;
import com.mz.astroboy.entity.internal.Astroboy;
import com.mz.astroboy.service.IAstroboyRemoteControl;
import com.mz.astroboy.service.internal.AstroboyRemoteControl;
import com.mz.astroboy.utils.DatabaseHelper;
import com.mz.astroboy.utils.HttpHelper;

/**
 * 模块化安装各个组件
 * 实现了接口和服务的分离
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class AstoboyModule extends AbstractModule {
	
	@Override
	protected void configure() {
		//	指定要使用的interface是由哪个具体的service实现的
		bind(IAstroboy.class).to(Astroboy.class);
		bind(IAstroboyRemoteControl.class).to(AstroboyRemoteControl.class);
		
		//	如果直接使用具体的service，当然就不需要再绑定了，比如这里的com.mz.astroboy.entity.internal.ContextInfo
		
		requestStaticInjection(DatabaseHelper.class);
		requestStaticInjection(HttpHelper.class);
	}

}
