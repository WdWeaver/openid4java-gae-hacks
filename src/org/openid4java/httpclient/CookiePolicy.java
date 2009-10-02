package org.openid4java.httpclient;

public enum CookiePolicy {
	IGNORE_COOKIES() {
		public String toString() {
			return "ignoreCookies";
		}
	}
}
