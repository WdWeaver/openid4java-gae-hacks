package org.openid4java.consumer;

import com.google.appengine.api.memcache.MemcacheService;

public class MemcachedLockUtil {
	private MemcacheService service;
	private String lockname;
	MemcachedLockUtil(MemcacheService service, String lockname) {
		this.service=service;
		this.lockname="Lock_"+lockname;
	}
	
	boolean aquireLock(int retrycount) {
		boolean locked = false;
		for(int retry=retrycount; retry>0; retry--) {
			long lock = 0;
			try {
				lock = service.increment(lockname,1);
			} catch(Exception e) {
				service.put(lockname,0);
				lock = service.increment(lockname,1);
			}
			if(lock==1) {
				locked = true;
				break;
			}
			if(lock> 1) {
				service.increment(lockname,-1);
			}
			try {
				Thread.sleep(100*retry);
			} catch (InterruptedException e) {
				break;
			}
		}
		return locked;
	}
	
	void releaseLock() {
		try {
			service.increment(lockname,-1);
		} catch(Exception e) {
			
		}
	}

}
