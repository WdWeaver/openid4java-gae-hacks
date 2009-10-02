package org.openid4java.httpclient;

public class StatusLine {
	String statusLine;
	public StatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	
	public String toString() {
		return this.statusLine;
	}
}
