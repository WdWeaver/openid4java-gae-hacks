package org.openid4java.httpclient;

public interface MethodFactory {
	public enum MethodType {
		HEAD,
		GET,
		POST,
	}
	public HttpMethod createMethod(String url, MethodType type);
}
