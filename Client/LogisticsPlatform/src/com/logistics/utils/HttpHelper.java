package com.logistics.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
	private static final String SERVER_URI = "http://219.223.168.66/file/";
	//private static final String FILE_TXT = "1.txt";
	//private static final String FILE_TXT = "1.txt";
	private HttpClient client;
	private HttpGet get;
	
	public HttpClient getClient() {
		return client;
	}
	
	public HttpGet getGet() {
		return get;
	}
	
	public void setGet(String s) {
		this.get = new HttpGet( SERVER_URI+s);
	}

	public HttpHelper(String s){
		this.client = new DefaultHttpClient();
		setGet(s);
	}
	
	public void shutdown(){
		get.abort();
		client.getConnectionManager().shutdown();
	}
	
	public String getMessage() throws ClientProtocolException, IOException{
		HttpGet get = this.getGet();
		HttpResponse response = this.getClient().execute(get);
		String reply = EntityUtils.toString(response.getEntity());
		return reply;
	}
	
}
