package org.openid4java.httpclient;

public interface Header {

	public String getName();

	public String getValue();

	public void setName(String name);

	public void setValue(String value);

/*
	String name;
	String value;

	public Header() {
		name = null;
		value = null;
	}
	
	public Header(String name, String value) {
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
*/
}
