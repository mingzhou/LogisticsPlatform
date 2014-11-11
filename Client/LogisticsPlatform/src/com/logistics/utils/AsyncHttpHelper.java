package com.logistics.utils;

import com.google.inject.Singleton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 异步线程处理HTTP请求
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@Singleton
public class AsyncHttpHelper {
	private AsyncHttpClient client;
	
	public  AsyncHttpHelper() {
		//AsyncHttpClient client = new AsyncHttpClient(80);
	}
	
	private final String BASE_URL = "http://219.223.190.211";
	
	public AsyncHttpClient getClient(){
		return client = new AsyncHttpClient(80);
	}
	
	
	public void get(String url, ResponseHandlerInterface responseHandler) {
		client.get(getAbsoluteUrl(url), responseHandler);
	}
	
	public void get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public void post(String url, ResponseHandlerInterface responseHandler) {
		client.post(getAbsoluteUrl(url), responseHandler);
	}
	
	public void post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
