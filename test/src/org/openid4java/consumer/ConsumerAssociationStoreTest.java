/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.consumer;

import java.io.File;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.openid4java.TestEnvironment;
import org.openid4java.association.Association;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * @author Marius Scurtescu
 */
public abstract class ConsumerAssociationStoreTest extends TestCase
{
    ConsumerAssociationStore _associationStore;

    public ConsumerAssociationStoreTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());  
		ApiProxy.setDelegate(new ApiProxyLocalImpl(new File("./"+this.getClass().getName())) {}); 
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
    	
        _associationStore = createStore();
    }

    protected abstract ConsumerAssociationStore createStore();

    public void tearDown() throws Exception
    {
        _associationStore = null;

        ApiProxy.setDelegate(null);  
		ApiProxy.setEnvironmentForCurrentThread(null);  
        
    }

    public void testSaveLoadRemove()
    {
        _associationStore.save("http://example.com", Association.generateHmacSha1("a", 60));
        _associationStore.save("http://example.com", Association.generateHmacSha256("b", 60));
        _associationStore.save("http://example.com", Association.generateHmacSha1("c", 60));

        assertNotNull(_associationStore.load("http://example.com", "a"));
        assertNotNull(_associationStore.load("http://example.com", "b"));
        assertNotNull(_associationStore.load("http://example.com", "c"));

        assertNotNull(_associationStore.load("http://example.com"));

        _associationStore.remove("http://example.com", "b");

        assertNull(_associationStore.load("http://example.com", "b"));
    }

    public void testCleanup() throws InterruptedException
    {
        _associationStore.save("http://example.com", Association.generateHmacSha1("a", 1));
        _associationStore.save("http://example.com", Association.generateHmacSha256("b", 1));
        _associationStore.save("http://example.com", Association.generateHmacSha1("c", 1));

        _associationStore.save("http://example.net", Association.generateHmacSha1("a", 1));
        _associationStore.save("http://example.net", Association.generateHmacSha256("b", 1));
        _associationStore.save("http://example.net", Association.generateHmacSha1("c", 1));

        Thread.sleep(2000);

        _associationStore.save("http://example.org", Association.generateHmacSha1("d", 1));
    }

    public static Test suite()
    {
        return new TestSuite(InMemoryConsumerAssociationStoreTest.class);
    }
}
