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
				
		if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		
		try {
			JSONObject object = new JSONObject();
			object.put("k1", "v1");
			object.put("k2", "中文");
			HttpEntity entity = new StringEntity(object.toString(), ContentType.APPLICATION_JSON);
			response.setEntity(entity);
			response.setStatusCode(HttpStatus.SC_OK);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

}
