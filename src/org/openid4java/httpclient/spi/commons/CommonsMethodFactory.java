package org.openid4java.httpclient.spi.commons;

import org.openid4java.httpclient.HttpMethod;
import org.openid4java.httpclient.MethodFactory;

public class CommonsMethodFactory implements MethodFactory {

	public HttpMethod createMethod(String url, MethodType type) {
		switch (type) {
		case HEAD:
			return new CommonsHeadMethod(url);
		case GET:
			return new CommonsGetMethod(url);
		case POST:
			return new CommonsPostMethod(url);
		}
		return null;
	}

}
