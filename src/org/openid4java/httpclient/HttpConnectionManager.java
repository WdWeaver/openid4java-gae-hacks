package org.openid4java.httpclient;

/**
 * This is pseudo connection manager just parameter holder.   
 * @author wataru
 *
 */
public class HttpConnectionManager {
	HttpClientParams httpClientParams = new HttpClientParams();
	public HttpClientParams getParams() {
		return httpClientParams;
	}
}
