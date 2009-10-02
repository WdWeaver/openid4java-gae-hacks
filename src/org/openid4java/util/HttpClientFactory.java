/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

/*
 * Created on Mar 5, 2007
 */
package org.openid4java.util;

import org.openid4java.httpclient.CookiePolicy;
import org.openid4java.httpclient.HttpClient;
import org.openid4java.httpclient.MethodFactory;

import org.openid4java.httpclient.spi.commons.CommonsHttpClient;
import org.openid4java.httpclient.spi.commons.CommonsMethodFactory;
import org.openid4java.httpclient.spi.gae.GAEHttpClient;
import org.openid4java.httpclient.spi.gae.GAEMethodFactory;

/*
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
*/

/**
 * This class handles all HTTPClient connections for the
 * org.openid4java packages.
 *
 * @author Kevin
 */
public class HttpClientFactory
{
    private HttpClientFactory() {}

    /**
     * proxy properties for HTTPClient calls
     */
    private static ProxyProperties proxyProperties = null;

    private static boolean multiThreadedHttpClient = true;

    public static ProxyProperties getProxyProperties()
    {
        return proxyProperties;
    }

    public static void setProxyProperties(ProxyProperties proxyProperties)
    {
        HttpClientFactory.proxyProperties = proxyProperties;
    }

    public static boolean isMultiThreadedHttpClient() {
        return multiThreadedHttpClient;
    }

    /**
     * Configures the type of HttpClient's constructed by the factory.
     *
     * @param multiThreadedHttpClient if true, MultiThreadedHttpConnectionManager's are constructed;
     *                                if false - SimpleHttpConnectionManager's.
     *
     */
    public static void setMultiThreadedHttpClient(boolean multiThreadedHttpClient) {
        HttpClientFactory.multiThreadedHttpClient = multiThreadedHttpClient;
    }


    public enum HttpClientType {
    	GAE,
    	COMMONS
    }
    
    //set default Client Type
    static HttpClientType httpClientType = HttpClientType.GAE;
    
    public static void setHttpClientType(HttpClientType type) {
    	httpClientType = type;
    }
    
    public static HttpClientType getHttpClientType() {
    	return httpClientType;
    }

    public static HttpClient getInstance(int maxRedirects,
                                         Boolean allowCircularRedirects,
                                         int connTimeout, int socketTimeout,
                                         CookiePolicy cookiePolicy) {
    	HttpClient client = null;
    	switch(httpClientType) {
    	case GAE:
    		client = new GAEHttpClient(maxRedirects);
    		break;
    	case COMMONS:
    		client = new CommonsHttpClient(maxRedirects,allowCircularRedirects,connTimeout,socketTimeout,cookiePolicy);
    	}
    	
    	return client;
    }

    public static MethodFactory getMethodFactory() {
    	MethodFactory factory = null;
    	switch(httpClientType) {
    	case GAE:
    		factory = new GAEMethodFactory();
    		break;
    	case COMMONS:
    		factory = new CommonsMethodFactory();
    	}
    	return factory;
    }
    
}

