package org.openid4java.httpclient.spi.gae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

class GAEGetMethod implements GAEHttpMethod {

	Logger logger = Logger.getLogger(GAEGetMethod.class.getName());
	String url;
	boolean followRedirects;
	StatusLine statusLine;
	List<HTTPHeader> responseHeaders;
	HTTPMethod capsuledMethod;
	Map<String, HTTPHeader> requestHeaders = new HashMap<String,HTTPHeader>();
	byte[] content;
	ByteArrayInputStream stream;

	int executeDepth = 0;
	
	GAEGetMethod(String url) {
		
		logger.finest("GetMethod#<init>");

		this.url = url;
		
		capsuledMethod = HTTPMethod.GET;
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
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
            	logger.finest("error has occured with closing stream");
            }
        }
		logger.finest("GetMethod#releaseConnection");
	}

	public int execute(URLFetchService service) throws IOException {

		executeDepth++;
		FetchOptions options = FetchOptions.Builder.disallowTruncate();
		options = options.doNotFollowRedirects();
		
		HTTPMethod gmethod = capsuledMethod;
		URL encoded = new URL(new URI(url,true).toString());
		HTTPRequest request = new HTTPRequest(encoded,gmethod,options); 
		for(HTTPHeader header:requestHeaders.values()) {
			request.addHeader(header);	
		}
		HTTPResponse response = service.fetch(request);
	
		content = response.getContent();
		
		int code = response.getResponseCode();
		//build pseudo status line 
		statusLine = new StatusLine("HTTP/1.1 " + 
				Integer.toString(code) + " " + HttpStatus.getStatusText(code));

		responseHeaders = response.getHeaders();

		if (executeDepth < maxRedirects && (code == 301 || code == 302)) {
			for(HTTPHeader header: responseHeaders) {
				if("Location".equals(header.getName())) {
					url = header.getValue();
					code = this.execute(service);
					break;
				}
			}
		}
		executeDepth--;
		
		return code;
	}

	public void setRequestHeader(String headerName, String value) {
		requestHeaders.put(headerName, new HTTPHeader(headerName,value));
	}

	public InputStream getResponseBodyAsStream() {
		stream = new ByteArrayInputStream(content); 
		return stream;
	}

	public String getResponseBodyAsString() {
		throw new UnsupportedOperationException();
	}

	public void setRequestEntity(StringRequestEntity entity) {
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
