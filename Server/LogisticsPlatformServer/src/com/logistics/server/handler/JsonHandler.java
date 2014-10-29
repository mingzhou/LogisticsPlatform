package com.logistics.server.handler;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 处理URI为"/json/*"的请求
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class JsonHandler implements HttpRequestHandler {

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		String s =request.getRequestLine().getUri().toLowerCase().substring(7);
		String[] ss = s.split("&");
		String dep,des,typ;
		dep =ss[0].split("=")[1];
		des =ss[1].split("=")[1];
		typ =ss[2].split("=")[1];
		
		if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		
		try {
			JSONObject object = new JSONObject();
			System.out.println(dep);
			System.out.println(des);
			System.out.println(typ);
			if( dep.equals("shanghai")){
			object.put("车牌", "豫B7998");
			object.put("电话", "123214214");}
			else{
				object.put("车牌", "no result");
				object.put("电话", "no result");
			}
			HttpEntity entity = new StringEntity(object.toString(), ContentType.APPLICATION_JSON);
			response.setEntity(entity);
			response.setStatusCode(HttpStatus.SC_OK);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

}
