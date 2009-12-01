package org.openid4java.httpclient.spi.gae;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openid4java.httpclient.Header;
import org.openid4java.httpclient.HttpException;
import org.openid4java.httpclient.HttpStatus;
import org.openid4java.httpclient.StatusLine;
import org.openid4java.httpclient.StringRequestEntity;
import org.openid4java.httpclient.URI;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;

class GAEHeadMethod implements GAEHttpMethod {
	
	Logger logger = Logger.getLogger(GAEHeadMethod.class.getName());
	boolean followRedirects;
	String url;
	HTTPMethod capsuledMethod;
	StatusLine statusLine;
	List<HTTPHeader> responseHeaders;
	int executeDepth = 0;
	GAEHeadMethod(String url) {
		logger.finest("HeadMethod#<init>");
		this.url = url;
		capsuledMethod = HTTPMethod.HEAD;
	}

	public void setFollowRedirects(boolean b) {
		this.followRedirects = b;
	}

	public void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}
	
	public StatusLine getStatusLine() {
		return statusLine;
	}

	public URI getURI() throws HttpException {
		return new URI(url,true);
	}


	public Header[] getResponseHeaders() {
		List<Header> headers = new ArrayList<Header>();
		for(HTTPHeader header:responseHeaders) {
			//test if value has multiple header;
			String s = header.getValue();
			if ( s!=null && s.split(",").length > 1 ) {
				String[] values = s.split(",");
				for (String value: values) {
					headers.add(new GAEHeader(header.getName(),value.trim()));
				}
			} else {
				headers.add(new GAEHeader(header.getName(),header.getValue()));
			}
			
		}
		return headers.toArray(new Header[0]);
	}

	public void releaseConnection() {
		logger.finest("HeadMethod#releaseConnection");
	}

	public int execute(URLFetchService service) throws IOException {

		executeDepth++;
		FetchOptions options = FetchOptions.Builder.disallowTruncate();
		
		options = options.doNotFollowRedirects();
		
		//recursive point
		HTTPMethod gmethod = capsuledMethod;
		logger.info("Execute "+this.getClass().getSimpleName()+" url="+url.toString());
		
		URL encoded = new URL(new URI(url,true).toString());
		
		HTTPRequest request = new HTTPRequest(encoded,gmethod,options); 
		HTTPResponse response = service.fetch(request);

		int code = response.getResponseCode();
		//build pseudo status line 
		statusLine = new StatusLine("HTTP/1.1 " + 
				Integer.toString(code) + " " + HttpStatus.getStatusText(code));
		
		
		responseHeaders = response.getHeaders();
		for(HTTPHeader h:responseHeaders) {
			logger.info(String.format("%s = %s", h.getName(), h.getValue()));
		}
		
		if (executeDepth < maxRedirects && (code == 301 || code == 302)) {
			for(HTTPHeader header: responseHeaders) {
				if("Location".equalsIgnoreCase(header.getName())) {
					url = header.getValue();
					code = this.execute(service);
					break;
				}
			}
		}
		executeDepth--;
		return code; 
	}

	public InputStream getResponseBodyAsStream() {
		throw new UnsupportedOperationException();
	}

	public String getResponseBodyAsString() {
		throw new UnsupportedOperationException();	
	}

	public void setRequestEntity(StringRequestEntity entity) {
		throw new UnsupportedOperationException();	
	}

	public void setRequestHeader(String name, String value) {
		throw new UnsupportedOperationException();	
	}

	public void setResponseHeaders(Header[] headers) {
		throw new UnsupportedOperationException();
	}

	int maxRedirects = 10;
	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

}
