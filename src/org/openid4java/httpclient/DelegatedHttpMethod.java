package org.openid4java.httpclient;

import java.io.IOException;
import java.io.InputStream;

public abstract class DelegatedHttpMethod implements HttpMethod {

	HttpMethod delegate;
	
	public HttpMethod getDelegateHttpMethod() {
		return delegate;
	}
	
	public Header[] getResponseHeaders() {
		return delegate.getResponseHeaders();
	}

	public StatusLine getStatusLine() {
		return delegate.getStatusLine();
	}

	public URI getURI() throws HttpException {
		return delegate.getURI();
	}

	public void releaseConnection() {
		delegate.releaseConnection();
	}

	public void setFollowRedirects(boolean follow) {
		delegate.setFollowRedirects(follow);
	}

	public InputStream getResponseBodyAsStream() throws IOException {
		return delegate.getResponseBodyAsStream();
	}

	public String getResponseBodyAsString() throws IOException {
		return delegate.getResponseBodyAsString();
	}

	public void setRequestEntity(StringRequestEntity entity) {
		delegate.setRequestEntity(entity);
	}

	public void setRequestHeader(String name, String value) {
		delegate.setRequestHeader(name, value);
	}

	
}
