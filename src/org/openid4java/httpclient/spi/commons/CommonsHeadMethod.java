package org.openid4java.httpclient.spi.commons;

import org.apache.commons.httpclient.methods.HeadMethod;
import org.openid4java.httpclient.StringRequestEntity;

class CommonsHeadMethod extends CommonsHttpMethod {

	CommonsHeadMethod(String url) {
		super(new HeadMethod(url));
	}

	@Override
	public void setRequestEntity(StringRequestEntity entity) {
		throw new UnsupportedOperationException();
	}
	
}
