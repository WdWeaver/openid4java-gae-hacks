package org.openid4java.consumer;

import java.util.Date;
import java.util.logging.Logger;

import org.openid4java.association.Association;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public final class MemcachedConsumerAssociationStore implements
		ConsumerAssociationStore {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private MemcacheService service;
	private MemcachedLockUtil lock;
	
	public MemcachedConsumerAssociationStore() {
		
		service = MemcacheServiceFactory.getMemcacheService();
	
		service.setNamespace(this.getClass().getName());

		lock = new MemcachedLockUtil(service,service.getNamespace());
	}
	
	public synchronized Association load(String opUrl, String handle) {
	
		logger.finest(String.format("#load opUrl=%s,handle=%s",opUrl,handle));
		
		Association association = (Association)service.get(opUrl+"|"+handle); 
		
		if(association!=null) {
			
			logger.finest(String.format("loaded association cache expiration=%s",association.getExpiry())); 
		
		}
		
		return association;
		
	}

	public synchronized Association load(String opUrl) {
		
		logger.finest(String.format("#load opUrl=%s",opUrl));
		
		Association association = (Association)service.get(opUrl); 

		if(association!=null) {
			
			logger.finest(String.format("loaded association cache expiration=%s",association.getExpiry())); 
		
		}

		return association;
		
	}

	public synchronized void remove(String opUrl, String handle) {
		
		try {
			if(!lock.aquireLock(10)) return;		
			logger.finest(String.format("#remove opUrl=%s,handle=%s",opUrl,handle));
			service.delete(opUrl+"|"+handle);
		} finally {
			lock.releaseLock();
		}
	
	}

	public synchronized void save(String opUrl, Association association) {

		try {

			if(!lock.aquireLock(10)) return;			
			
			logger.finest("#save opUrl="+opUrl+",handle="+association.getHandle());
			
			String handle = association.getHandle();
			Date expdate = association.getExpiry();
			Expiration exp = Expiration.onDate(expdate);
	
			logger.finest(String.format("save association opUrl=%s,handle=%s,expiration=%s",opUrl,association.getHandle(),association.getExpiry()));
			
			service.put(opUrl+"|"+handle, association, exp);
			
			Association latest = (Association) service.get(opUrl);
			
			if(latest==null||expdate.after(latest.getExpiry())) {
				logger.finest(String.format("save latest association handle=%s,expiration=%s",association.getHandle(),association.getExpiry()));
				service.put(opUrl, association, exp);
			}
		} finally {
			lock.releaseLock();
		}
		
	}

	
	public synchronized int size() {
	    throw new UnsupportedOperationException();
    }
	
}
