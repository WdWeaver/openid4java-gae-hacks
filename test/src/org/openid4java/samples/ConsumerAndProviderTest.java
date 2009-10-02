/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.samples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.openid4java.TestEnvironment;
import org.openid4java.consumer.SampleConsumer;
import org.openid4java.message.ParameterList;
import org.openid4java.server.SampleServer;
import org.openid4java.server.ServerException;

import net.sourceforge.jwebunit.junit.WebTester;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.appengine.tools.development.DevAppServer;
import com.google.appengine.tools.development.DevAppServerFactory;
import com.google.apphosting.api.ApiProxy;

//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlForm;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.meterware.httpunit.HttpInternalErrorException;
//import com.meterware.httpunit.WebConversation;
//import com.meterware.httpunit.WebForm;
//import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;

public class ConsumerAndProviderTest extends TestCase
{

    private Server _server;
    private String _baseUrl;

    public ConsumerAndProviderTest(final String testName) throws Exception
    {
        super(testName);
        int servletPort = Integer.parseInt(System.getProperty("SERVLET_PORT", "8989"));
        _server = new Server(servletPort);

        Context context = new Context(_server, "/", Context.SESSIONS);
        _baseUrl = "http://localhost:" + servletPort; // +
        // context.getContextPath();

        
        SampleConsumer consumer = new SampleConsumer(_baseUrl + "/loginCallback");
        _server.addHandler(new Handler() {
        	Server _handler_server = null;
			public void destroy() {

			}

			public Server getServer() {
				return _handler_server;
			}

			public void handle(String arg0, HttpServletRequest arg1,
					HttpServletResponse arg2, int arg3) throws IOException,
					ServletException {
			}

			public void setServer(Server arg0) {
				_handler_server = arg0;
			}

			public boolean isFailed() {
				return false;
			}

			public boolean isRunning() {
				return false;
			}

			public boolean isStarted() {
				return false;
			}

			public boolean isStarting() {
				return false;
			}

			public boolean isStopped() {
				return false;
			}

			public boolean isStopping() {
				return false;
			}

			public void start() throws Exception {
				ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());  
				ApiProxy.setDelegate(new ApiProxyLocalImpl(new File("./"+this.getClass().getName())) {}); 
				ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
			}

			public void stop() throws Exception {
				ApiProxy.setDelegate(null);  
				ApiProxy.setEnvironmentForCurrentThread(null);  
			}
        	
        });
        context.addServlet(new ServletHolder(new LoginServlet(consumer)), "/login");
        context.addServlet(new ServletHolder(new LoginCallbackServlet(consumer)), "/loginCallback");
        context.addServlet(new ServletHolder(new UserInfoServlet()), "/user");

        SampleServer server = new SampleServer(_baseUrl + "/provider")
        {
            protected List userInteraction(ParameterList request) throws ServerException
            {
                List back = new ArrayList();
                back.add("userSelectedClaimedId"); // userSelectedClaimedId
                back.add(Boolean.TRUE); // authenticatedAndApproved
                back.add("user@example.com"); // email
                return back;
            }
        };
        context.addServlet(new ServletHolder(new ProviderServlet(server)), "/provider");
    }

    public void setUp() throws Exception
    {
        _server.start();
    }

    protected void tearDown() throws Exception
    {
        _server.stop();
        _server.join();

    }

    public void testCycleWithXrdsUser() throws Exception
    {
        HttpServletSupport.lastException = null;
        HttpServletSupport.count_ = 0;
        WebTester wc = new WebTester();
        try
        {
            wc.setScriptingEnabled(false);
            wc.beginAt(_baseUrl + "/login");
            wc.setTextField("openid_identifier", _baseUrl + "/user");
            wc.submit();
            wc.clickLink("login");
            wc.assertTextPresent("success");
            wc.assertTextPresent("emailFromFetch:user@example.com");
            wc.assertTextPresent("emailFromSReg:user@example.com");
        }
        catch (Exception exc)
        {
            System.err.println("last page before exception :" + wc.getPageSource());
            if (HttpServletSupport.lastException != null)
            {
                throw HttpServletSupport.lastException;
            }
            else
            {
                throw exc;
            }
        }
    }

    public void testCycleWithHtmlUser() throws Exception
    {
        HttpServletSupport.lastException = null;
        HttpServletSupport.count_ = 0;
        WebTester wc = new WebTester();
        try
        {
            wc.setScriptingEnabled(false);
            wc.beginAt(_baseUrl + "/login");
            wc.setTextField("openid_identifier", _baseUrl + "/user?format=html");
            wc.submit();
            wc.clickLink("login");
            wc.assertTextPresent("success");
            wc.assertTextPresent("emailFromFetch:user@example.com");
            wc.assertTextPresent("emailFromSReg:user@example.com");
        }
        catch (Exception exc)
        {
            System.err.println("last page before exception :" + wc.getPageSource());
            if (HttpServletSupport.lastException != null)
            {
                throw HttpServletSupport.lastException;
            }
            else
            {
                throw exc;
            }
        }
    }
}
