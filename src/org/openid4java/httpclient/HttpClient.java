package org.openid4java.httpclient;

import java.io.IOException;

public abstract class HttpClient {
	
	HttpClientParams httpClientParams = new HttpClientParams();
	
	private HttpConnectionManager manager = new HttpConnectionManager();
	
	public HttpConnectionManager getHttpConnectionManager() {
		return manager;
	}

	public HttpClientParams getParams() {
		return httpClientParams;
	}


	public abstract int executeMethod(HttpMethod method) throws IOException, HttpException;

}
