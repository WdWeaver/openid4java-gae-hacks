package org.openid4java.httpclient;

import java.io.IOException;

@SuppressWarnings("serial")
public class HttpException extends IOException {

	public HttpException() {
		super();
	}
	
	public HttpException(String reason) {
		super(reason);
	}

	public HttpException(Throwable cause) {
		super(cause);
	}
}
