package org.openid4java.httpclient.spi.gae;

import java.io.IOException;

import org.openid4java.httpclient.DelegatedHttpMethod;
import org.openid4java.httpclient.HttpClientParams;
import org.openid4java.httpclient.HttpException;
import org.openid4java.httpclient.HttpMethod;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GAEHttpClient extends org.openid4java.httpclient.HttpClient {

	URLFetchService service;

	//default max redirects
	int maxRedirects = 10;
	public GAEHttpClient(int maxRedirects) {
		this.maxRedirects = maxRedirects;
		service = URLFetchServiceFactory.getURLFetchService();
	}

	@Override
	public int executeMethod(HttpMethod method)
			throws IOException, HttpException {
		GAEHttpMethod m = GAEHttpMethod.class.cast(DelegatedHttpMethod.class.cast(method).getDelegateHttpMethod());
		return m.execute(service);
	}
}
