package org.openid4java.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HttpClientParams {

	Map<String,Object> parameters = new HashMap<String, Object>();
	
	int socketTimeout; 
	int connectionTimeout;
	
	public void setParameter(String key, Object value) {
		parameters.put(key, value);
	}
	public void getParameter(String key) {
		parameters.get(key);
		
	}
	public Map<String,Object> getMap() {
		return parameters;
	}
	
	public void setSoTimeout(int socketTimeout) {
		
		this.socketTimeout = socketTimeout;
		
	}

	public int getSoTimeout() {
		
		return this.socketTimeout;
		
	}

	public int getConnectionTimeout() {
		
		return this.connectionTimeout;
		
	}
	public void setConnectionTimeout(int connTimeout) {
		this.connectionTimeout = connTimeout;
	}

}
