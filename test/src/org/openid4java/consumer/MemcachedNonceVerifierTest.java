/*
 * Copyright 2006-2008 Sxip Identity Corporation
 */

package org.openid4java.consumer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Marius Scurtescu, Johnny Bufu
 */
public class MemcachedNonceVerifierTest extends AbstractNonceVerifierTest
{
    public MemcachedNonceVerifierTest(String name)
    {
        super(name);
    }

    public NonceVerifier createVerifier(int maxAge)
    {
        return new MemcachedNonceVerifier(maxAge);
    }

    public void testNonceCleanup() throws Exception
    {
        super.testNonceCleanup();

        MemcachedNonceVerifier memcachedNonceVerifier = (MemcachedNonceVerifier ) _nonceVerifier;

    }

    public static Test suite()
    {
        return new TestSuite(MemcachedNonceVerifierTest.class);
    }


}
