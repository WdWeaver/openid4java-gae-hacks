package org.openid4java.httpclient.spi.commons;

import org.openid4java.httpclient.Header;

class CommonsHeader implements Header {
	org.apache.commons.httpclient.Header originalHeader;
	CommonsHeader(org.apache.commons.httpclient.Header header) {
		originalHeader = header;
	}

	public String getName() {
		return originalHeader.getName();
	}
	public String getValue() {
		return originalHeader.getValue();
	}
	public void setName(String name) {
		originalHeader.setName(name);
	}
	public void setValue(String value) {
		originalHeader.setValue(value);
	}
	
}
