package org.openid4java.httpclient;

import java.io.IOException;
import java.io.InputStream;

public interface HttpMethod {

	public void setFollowRedirects(boolean follow);

	public StatusLine getStatusLine();

	public URI getURI() throws HttpException;

	public Header[] getResponseHeaders();

	public void releaseConnection();
	
	public void setRequestHeader(String name, String value);

	public InputStream getResponseBodyAsStream() throws IOException;
	
	public String getResponseBodyAsString() throws IOException;
	
	public void setRequestEntity(StringRequestEntity entity);

}
