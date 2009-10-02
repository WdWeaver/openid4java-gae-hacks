/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.samples;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.TestEnvironment;
import org.openid4java.consumer.SampleConsumer;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class LoginServlet extends HttpServletSupport
{
    private static final long serialVersionUID = 1L;
    private SampleConsumer consumer_;

    public LoginServlet(SampleConsumer consumer)
    {
        consumer_ = consumer;
    }

    protected void onService(HttpServletRequest req, HttpServletResponse resp) throws Exception
    {
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());  
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File("./"+this.getClass().getName())) {}); 
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
    	
        if (req.getParameter("openid_identifier") != null)
        {
            logger_.info("openind_identifier set => try to consume");
            consumer_.authRequest(req.getParameter("openid_identifier"), req, resp);
        }
        else
        {
            logger_.info("display form");
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<html><body><form><input type='text' name='openid_identifier'/><input type='submit'/></form></body></html>");
        }

        ApiProxy.setDelegate(null);  
		ApiProxy.setEnvironmentForCurrentThread(null);  
        
    }

}
