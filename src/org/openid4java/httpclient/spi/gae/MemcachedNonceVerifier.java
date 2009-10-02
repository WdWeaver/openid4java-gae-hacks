package org.openid4java.httpclient.spi.gae;

import java.util.Date;
import java.util.logging.Logger;

import org.openid4java.consumer.AbstractNonceVerifier;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcachedNonceVerifier extends AbstractNonceVerifier {

	Logger logger = Logger.getLogger(this.getClass().getName());
	MemcacheService service;
    public MemcachedNonceVerifier(int maxAge) {
        super(maxAge);
		service = MemcacheServiceFactory.getMemcacheService();
		service.setNamespace(this.getClass().getName());
    }

	@Override
	protected int seen(Date now, String opUrl, String nonce) {
		
		logger.finest(String.format("#seen now=%s,opUrl=%s,nonce=%s",now,opUrl,nonce));
		
		String n = (String)service.get(opUrl+"|"+nonce);

        if (n!=null){
        	
        	logger.finest(String.format("got cached nonce"));
        
        	return SEEN;
        
        } else {
        	
        	logger.finest(String.format("did not cached nonce"));
        }

        Expiration exp = Expiration.byDeltaSeconds(getMaxAge());
        
        logger.finest(String.format("save nonce: expiration=%s",exp.toString()));
        
        service.put(opUrl+"|"+nonce,nonce,exp);
        
        return OK;
	}


}
