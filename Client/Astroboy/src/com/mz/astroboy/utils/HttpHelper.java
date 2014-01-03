package com.mz.astroboy.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Http客户端
 * @author zhuangmz
 *
 */

/**
 * 同步线程处理HTTP请求
 * @author zhuangmz
 *
 */
@ContextSingleton
public class HttpHelper {

//	private final String USER_AGENT = "logistics client";
	
	public HttpHelper() {
		// empty
	}
	
	@Inject
	private static Provider<Context> contextProvider;
	
	public String sendHttpGet(String uri, String charset) {
		try {
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(uri));
//			HttpClient client = AndroidHttpClient.newInstance(USER_AGENT);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpGet);
			if (response == null) {
				Toast.makeText(contextProvider.get(), "NULL" , Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(contextProvider.get(), "NOT NULL" , Toast.LENGTH_LONG).show();
			}
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, charset);
				Toast.makeText(contextProvider.get(), "Response : " + result, Toast.LENGTH_LONG).show();
//				return EntityUtils.toString(entity, charset);
				return result;
			} else {
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(contextProvider.get(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
