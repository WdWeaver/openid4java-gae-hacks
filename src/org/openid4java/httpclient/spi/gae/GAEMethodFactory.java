package org.openid4java.httpclient.spi.gae;

import org.openid4java.httpclient.HttpMethod;
import org.openid4java.httpclient.MethodFactory;

public class GAEMethodFactory implements MethodFactory {

	public HttpMethod createMethod(String url, MethodType type) {
		switch (type) {
		case HEAD:
			return new GAEHeadMethod(url);
		case GET:
			return new GAEGetMethod(url);
		case POST:
			return new GAEPostMethod(url);
		}
		return null;
	}

}
