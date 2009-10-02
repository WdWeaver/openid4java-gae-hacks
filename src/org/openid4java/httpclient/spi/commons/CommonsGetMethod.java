package org.openid4java.httpclient.spi.commons;

import org.apache.commons.httpclient.methods.GetMethod;
import org.openid4java.httpclient.StringRequestEntity;


class CommonsGetMethod extends CommonsHttpMethod {
	CommonsGetMethod(String url) {
		super(new GetMethod(url));
	}

	@Override
	public void setRequestEntity(StringRequestEntity entity) {
		throw new UnsupportedOperationException();
	}
}
