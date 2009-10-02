package org.openid4java.httpclient.spi.commons;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

class CommonsPostMethod extends CommonsHttpMethod {

	CommonsPostMethod(String url) {
		super(new PostMethod(url));
	}

	@Override
	public void setRequestEntity(org.openid4java.httpclient.StringRequestEntity originalEntity) {
		StringRequestEntity entity = null;
		try {
			entity = new StringRequestEntity(originalEntity.getContent(),
					 originalEntity.getContentType(),
					 originalEntity.getCharset());
			
		} catch (UnsupportedEncodingException e) {
			//check it at instancing originalEntity
			// we are ignore this exception
			e.printStackTrace();
		}
		PostMethod.class.cast(method).setRequestEntity(entity);
		
	}
}
