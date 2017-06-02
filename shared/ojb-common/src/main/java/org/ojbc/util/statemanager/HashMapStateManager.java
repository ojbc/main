/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
