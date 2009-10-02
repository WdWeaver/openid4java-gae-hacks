package org.openid4java.httpclient.spi.gae;

import java.util.Date;
import java.util.logging.Logger;

import org.openid4java.association.Association;
import org.openid4java.consumer.ConsumerAssociationStore;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcachedConsumerAssociationStore implements
		ConsumerAssociationStore {

	Logger logger = Logger.getLogger(this.getClass().getName());
	
	MemcacheService service;
	
	public MemcachedConsumerAssociationStore() {
		
		service = MemcacheServiceFactory.getMemcacheService();
	
		service.setNamespace(this.getClass().getName());
	
	}
	
	public Association load(String opUrl, String handle) {
	
		logger.finest(String.format("#load opUrl=%s,handle=%s",opUrl,handle));
		
		Association association = (Association)service.get(opUrl+"|"+handle); 
		
		if(association!=null) {
			
			logger.finest(String.format("loaded association cache expiration=%s",association.getExpiry())); 
		
		}
		
		return association;
		
	}

	public Association load(String opUrl) {
		
		logger.finest(String.format("#load opUrl=%s",opUrl));
		
		Association association = (Association)service.get(opUrl); 

		if(association!=null) {
			
			logger.finest(String.format("loaded association cache expiration=%s",association.getExpiry())); 
		
		}

		return association;
		
	}

	public void remove(String opUrl, String handle) {
		
		logger.finest(String.format("#remove opUrl=%s,handle=%s",opUrl,handle));

		service.delete(opUrl+"|"+handle);
	
	}

	public void save(String opUrl, Association association) {

		logger.finest("#save opUrl="+opUrl+",handle="+association.getHandle());
		
		String handle = association.getHandle();
		
		Date expdate = association.getExpiry();
		
		Expiration exp = Expiration.onDate(expdate);
		
		logger.finest(String.format("save association opUrl=%s,handle=%s,expiration=%s",opUrl,association.getHandle(),association.getExpiry()));

		service.put(opUrl+"|"+handle, association, exp);

		Association latest = (Association) service.get(opUrl);
		
		Date latestexp = null;
		
		if(latest!=null) {
			latestexp = latest.getExpiry();
		}
		
		if(latest==null||expdate.after(latestexp)) {
			logger.finest(String.format("save latest association handle=%s,expiration=%s",association.getHandle(),association.getExpiry()));
			service.put(opUrl, association, exp);
		}
			
	}

}
