package com.logistics.server.handler;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

/**
 * 处理URI为"/file/*"的请求
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
public class FileHandler implements HttpRequestHandler {
	/**
	 * 服务器文件根目录
	 */
	private final String WEB_ROOT = "WEB-ROOT";

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		
		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			byte[] entityContent = EntityUtils.toByteArray(entity);
			System.out.println("Incoming entity content (bytes): " + entityContent.length);
			System.out.println(new String(entityContent));
		}
		
		String uri = request.getRequestLine().getUri();
		String target = uri.substring(6);
		File file = new File(WEB_ROOT, URLDecoder.decode(target, "UTF8"));
		if (!file.exists()) {
			response.setStatusCode(HttpStatus.SC_NOT_FOUND);
			StringEntity entity = new StringEntity("<html><body><h1>File: " + file.getPath() +
					" not found</h1></body></html>",
					ContentType.create("text/html", "UTF-8"));
			response.setEntity(entity);
			System.out.println("File " + file.getPath() + " not found");
		} else if (!file.canRead() || file.isDirectory()) {
			response.setStatusCode(HttpStatus.SC_FORBIDDEN);
			StringEntity entity = new StringEntity("<html><body><h1>Access denied</h1></body></html>",
					ContentType.create("text/html", "UTF-8"));
			response.setEntity(entity);
			System.out.println("Cannot read file " + file.getPath());
		} else {
			response.setStatusCode(HttpStatus.SC_OK);
			ContentType contentType = ContentType.APPLICATION_OCTET_STREAM;
			if (target.toLowerCase().endsWith(".txt")) {
				contentType = ContentType.TEXT_PLAIN;
			} else if (target.toLowerCase().endsWith(".json")) {
				contentType = ContentType.APPLICATION_JSON;
			}else if (target.toLowerCase().endsWith(".pdf")) {
				contentType = ContentType.create("application/pdf");
			} else if (target.toLowerCase().endsWith(".mp4")) {
				contentType = ContentType.create("video/mp4");
			}
			response.setEntity(new FileEntity(file, contentType));
			System.out.println("Serving file " + file.getPath());
		}		
	}

}
