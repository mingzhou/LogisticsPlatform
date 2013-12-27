package com.logistics.server;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

/**
 * 处理客户端请求的线程
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class ClientHandleThread extends Thread {

	private HttpService httpService;
    private HttpServerConnection connection;

    public ClientHandleThread(HttpService httpService, HttpServerConnection connection) {
    	super();
    	this.httpService = httpService;
        this.connection = connection;
	}

    @Override
    public void run() {
    	HttpContext context = new BasicHttpContext(null);
		try {
			while (!Thread.interrupted() && connection.isOpen()) {
				httpService.handleRequest(connection, context);
			}
		} catch (ConnectionClosedException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		}
    }
}
