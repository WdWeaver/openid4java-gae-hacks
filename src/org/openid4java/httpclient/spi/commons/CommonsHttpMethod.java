package org.openid4java.httpclient.spi.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.openid4java.httpclient.Header;
import org.openid4java.httpclient.HttpException;
import org.openid4java.httpclient.StatusLine;
import org.openid4java.httpclient.StringRequestEntity;
import org.openid4java.httpclient.URI;
import org.openid4java.httpclient.URIException;

abstract class CommonsHttpMethod implements org.openid4java.httpclient.HttpMethod {

	HttpMethodBase method;
	CommonsHttpMethod(HttpMethodBase method) {
		this.method = method;
	}
	
	public InputStream getResponseBodyAsStream() throws IOException {
		return method.getResponseBodyAsStream();
	}

	public String getResponseBodyAsString() throws IOException {
		return method.getResponseBodyAsString();
	}

	public Header[] getResponseHeaders() {
		List<Header> headers = new ArrayList<Header>();
		for(org.apache.commons.httpclient.Header src: method.getResponseHeaders()) {
			headers.add(new CommonsHeader(src));
		}
		return headers.toArray(new Header[0]);
	}

	public StatusLine getStatusLine() {
		return new StatusLine(method.getStatusLine().toString());
	}

	public URI getURI() throws HttpException {
		try {
			return new URI(method.getURI().toString(), false);
		} catch (org.apache.commons.httpclient.URIException e) {
			throw new URIException(e.getReasonCode());
		} catch (NullPointerException e) {
			throw e;
		}
	}

	public void releaseConnection() {
		method.releaseConnection();
	}

	public void setFollowRedirects(boolean follow) {
		method.setFollowRedirects(follow);
	}

	public abstract void setRequestEntity(StringRequestEntity entity);

	public void setRequestHeader(String name, String value) {
		method.setRequestHeader(name, value);
	}

	public int execute(HttpClient client) throws HttpException, IOException  {
		try {
			return client.executeMethod(method);
		} catch (org.apache.commons.httpclient.HttpException e) {
			throw new HttpException(e);
		} catch (IOException e) {
			throw e;
		}
	}
}
