package com.logistics.server;

/**
 * 启动服务器
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class Main {
	
	public static void main(String[] args) {
		new Thread(new WebServer(80, 100)).start();
	}
}
