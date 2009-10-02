package org.openid4java.httpclient.spi.gae;

import java.io.IOException;

import org.openid4java.httpclient.Header;

import com.google.appengine.api.urlfetch.URLFetchService;

public interface GAEHttpMethod extends org.openid4java.httpclient.HttpMethod {
	public int execute(URLFetchService service) throws IOException;
	public void setResponseHeaders(Header[] headers);
	public void setMaxRedirects(int maxRedirects);
}
