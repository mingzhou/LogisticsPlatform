package com.logistics.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

import com.logistics.server.handler.FileHandler;
import com.logistics.server.handler.JsonHandler;

/**
 * 服务器线程池
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class WebServer implements Runnable {
	
	public WebServer() {
		this (80, 100);
	}
	
	/**
	 * 监听端口
	 */
	private int			serverPort		= 8888;
	private HttpProcessor	processor		= null;
	private UriHttpRequestHandlerMapper registry	= null;
	private HttpConnectionFactory<DefaultBHttpServerConnection> connectionFactory = null;
	private ServerSocket	serverSocket	= null;
	private HttpService		httpService		= null;
	private boolean		isStopped		= false;
	private Thread			runningThread	= null;
	private ExecutorService		threadPool	= null;

	public WebServer (int serverPort, int maxThreadNumber) {
		try {
			System.out.println("Constructor:\t" + WebServer.class.getName());
			threadPool = Executors.newFixedThreadPool(maxThreadNumber);
			processor = HttpProcessorBuilder.create()
					.add(new ResponseDate())
					.add(new ResponseServer("Mingzhou Main /1.1"))
					.add(new ResponseContent())
					.add(new ResponseConnControl())
					.build();
			//	注册URI处理器
			registry = new UriHttpRequestHandlerMapper();
			registry.register("/file/*", new FileHandler());
			registry.register("/json/*", new JsonHandler());
			httpService = new HttpService(processor, registry);
			connectionFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
			this.serverPort = serverPort;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped) {
			try {
				//	监听连接请求
				Socket clientSocket = serverSocket.accept();
				System.err.println("与" + clientSocket.getInetAddress() + "连接成功");
				HttpServerConnection connection = connectionFactory.createConnection(clientSocket);

				//	分配线程池处理请求
				this.threadPool.execute(new ClientHandleThread(httpService, connection));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean isStopped() {
		return isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
			System.err.println("监听端口:\t" + serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Thread getRunningThread() {
		return runningThread;
	}
}
