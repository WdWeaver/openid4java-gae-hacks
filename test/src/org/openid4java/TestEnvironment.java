package org.openid4java;

import java.util.HashMap;
import java.util.Map;

import com.google.apphosting.api.ApiProxy.Environment;

public class TestEnvironment implements Environment {

	public String getAppId() {
		return "Unit Tests";
	}

	public String getVersionId() {
		return "1.0";
	}
	
	public Map<String, Object> getAttributes() {
		return new HashMap();
	}

	public String getRequestNamespace() {
		return "";
	}

	public String getAuthDomain() {
		throw new UnsupportedOperationException();
	}

	public String getEmail() {
		throw new UnsupportedOperationException();
	}


	public boolean isAdmin() {
		throw new UnsupportedOperationException();
	}

	public boolean isLoggedIn() {
		throw new UnsupportedOperationException();
	}

}
