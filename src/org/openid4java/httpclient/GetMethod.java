package org.openid4java.httpclient;

import org.openid4java.httpclient.MethodFactory.MethodType;
import org.openid4java.util.HttpClientFactory;

public final class GetMethod extends DelegatedHttpMethod {

	public GetMethod(String url) {
		delegate = HttpClientFactory.getMethodFactory().createMethod(url,MethodType.GET);
	}
	
}
