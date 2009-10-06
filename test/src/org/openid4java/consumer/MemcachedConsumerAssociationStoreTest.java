/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.consumer;

import org.openid4java.association.Association;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Marius Scurtescu
 */
public class MemcachedConsumerAssociationStoreTest extends ConsumerAssociationStoreTest
{
    public MemcachedConsumerAssociationStoreTest(String name)
    {
        super(name);
    }

    protected ConsumerAssociationStore createStore()
    {
        return new MemcachedConsumerAssociationStore();
    }

    public void testCleanup() throws InterruptedException
    {
        super.testCleanup();
        MemcachedConsumerAssociationStore memcachedAssociationStore = (MemcachedConsumerAssociationStore) _associationStore;
        assertNotNull(memcachedAssociationStore.load("http://example.org","d"));
        assertNull(memcachedAssociationStore.load("http://example.com","a"));
        assertNull(memcachedAssociationStore.load("http://example.com","b"));
        assertNull(memcachedAssociationStore.load("http://example.com","c"));
        assertNull(memcachedAssociationStore.load("http://example.net","a"));
        assertNull(memcachedAssociationStore.load("http://example.net","b"));
        assertNull(memcachedAssociationStore.load("http://example.net","c"));
        Thread.sleep(1000);
        assertNull(memcachedAssociationStore.load("http://example.org","d"));
        
        //assertEquals(1, memcachedAssociationStore.size());
    }

    public static Test suite()
    {
        return new TestSuite(MemcachedConsumerAssociationStoreTest.class);
    }
}
