package org.openid4java.consumer;

import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public final class MemcachedNonceVerifier extends AbstractNonceVerifier {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private MemcacheService service;
	private MemcachedLockUtil lock;
	public MemcachedNonceVerifier(int maxAge) {
        super(maxAge);
		service = MemcacheServiceFactory.getMemcacheService();
		service.setNamespace(this.getClass().getName());
		lock = new MemcachedLockUtil(service,service.getNamespace());
    }

	@Override
	protected synchronized int seen(Date now, String opUrl, String nonce) {
		
		try {
			if(!lock.aquireLock(10)) {
				return INVALID_TIMESTAMP;
			}
			
			logger.finest(String.format("#seen now=%s,opUrl=%s,nonce=%s",now,opUrl,nonce));
	
			String n = (String)service.get(opUrl+"|"+nonce);
	
	        if (n!=null){
	        	
	        	logger.finest(String.format("got cached nonce"));
	        
	        	return SEEN;
	        
	        } else {
	        	
	        	logger.finest(String.format("did not cached nonce"));
	
		        Expiration exp = Expiration.byDeltaSeconds(getMaxAge());
		        
		        logger.finest(String.format("save nonce: expiration=%s",exp.toString()));
		
		        service.put(opUrl+"|"+nonce,nonce,exp);
		        
		        return OK;
	        }
		} finally {
			lock.releaseLock();
		}
	}

    public synchronized int size() {
    	throw new UnsupportedOperationException();
    }
	
}
