package com.mz.astroboy.service;

/**
 * 控制器，就是把对实例的操作进行了更高层次的抽象
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public interface IAstroboyRemoteControl {
	public void brushTeeth();
	
	public void say(String something);
	
	public void selfDestruct();
}
