package org.openid4java.httpclient.spi.commons;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.openid4java.httpclient.CookiePolicy;
import org.openid4java.httpclient.DelegatedHttpMethod;
import org.openid4java.httpclient.HttpException;
import org.openid4java.httpclient.HttpMethod;
import org.openid4java.httpclient.spi.gae.GAEHttpMethod;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class CommonsHttpClient extends org.openid4java.httpclient.HttpClient {
	
	HttpClient client;
	HttpConnectionManager connManager;
	public CommonsHttpClient(int maxRedirects,
            Boolean allowCircularRedirects,
            int connTimeout, int socketTimeout,
            CookiePolicy cookiePolicy) {
     
		
        if (HttpClientFactory.isMultiThreadedHttpClient())
            connManager = new MultiThreadedHttpConnectionManager();
        else
            connManager = new SimpleHttpConnectionManager();

        client = new HttpClient(connManager);

        getParams().setParameter(
                "http.protocol.max-redirects", new Integer(maxRedirects));
        getParams().setParameter(
                "http.protocol.allow-circular-redirects", allowCircularRedirects);
        getParams().setSoTimeout(socketTimeout);
        getHttpConnectionManager().getParams().setConnectionTimeout(connTimeout);
        getParams().setParameter("http.protocol.cookie-policy",
                cookiePolicy.toString());

        ProxyProperties proxyProperties = HttpClientFactory.getProxyProperties();
        
        if (proxyProperties != null)
        {
            HostConfiguration hostConf = client.getHostConfiguration();

            hostConf.setProxy(proxyProperties.getProxyHostName(), proxyProperties.getProxyPort());

            //now set headers for auth
            AuthScope authScope = new AuthScope(AuthScope.ANY_HOST,
                    AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
            client.getState().setProxyCredentials(authScope,
                    new UsernamePasswordCredentials(
                            proxyProperties.getUserName(),
                            proxyProperties.getPassword()));
        }

	}

	
	@Override
	public int executeMethod(HttpMethod method)
			throws IOException, HttpException {
		CommonsHttpMethod m = CommonsHttpMethod.class.cast(DelegatedHttpMethod.class.cast(method).getDelegateHttpMethod());
		setClientParameters();
		return m.execute(client);
		
	}
	
	private void setClientParameters() {
		org.openid4java.httpclient.HttpClientParams source = getParams();
		HttpClientParams params = client.getParams(); 
		Map<String,Object> map = source.getMap();
		for(String key:map.keySet()) {
			params.setParameter(key,map.get(key));
		}
		params.setSoTimeout(source.getSoTimeout());
		client.getHttpConnectionManager().
		 getParams().setConnectionTimeout(getHttpConnectionManager().getParams().getConnectionTimeout());
	}
}
