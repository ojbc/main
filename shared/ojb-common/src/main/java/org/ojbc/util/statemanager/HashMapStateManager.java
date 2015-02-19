package org.ojbc.util.statemanager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.TimeUtils;

public class HashMapStateManager {

	private HashMap<String, Long> stateManager = new HashMap<String, Long>();

	private static final Log log = LogFactory.getLog(HashMapStateManager.class);
	
	/**
	 * Insert the key provided along with current time value in milliseconds
	 * @param key
	 */
	public void insert(String key)
	{
		Calendar currentDate = Calendar.getInstance();
		
		stateManager.put(key, currentDate.getTimeInMillis());
	}
	
	public boolean check(String key)
	{
		return stateManager.containsKey(key);
	}
	
	public void evict()
	{

		Calendar currentDate = Calendar.getInstance();
		long currentTimeinMillis = currentDate.getTimeInMillis();
		
		for (Entry<String, Long> entry : stateManager.entrySet()) {
		    
		    String key = entry.getKey();
		    long persistedTimeinMillis = entry.getValue();
		    
		    long difference = currentTimeinMillis - persistedTimeinMillis;
		    
		    if (difference > TimeUtils.ONE_DAY)
		    {
		    	stateManager.remove(key);
		    	log.debug("Evicting key from hashmap: " + key);
		    }	
		    
		}


	}
}
