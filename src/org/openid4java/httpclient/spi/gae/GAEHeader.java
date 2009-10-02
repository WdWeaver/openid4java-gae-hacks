package org.openid4java.httpclient.spi.gae;

import org.openid4java.httpclient.Header;

public class GAEHeader implements Header {

	String name;
	String value;

	GAEHeader() {
		name = null;
		value = null;
	}
	
	GAEHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
