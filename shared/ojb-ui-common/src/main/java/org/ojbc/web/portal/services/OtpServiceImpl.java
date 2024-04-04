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
package org.ojbc.web.portal.services;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OTPService{

	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	ConcurrentHashMap<String, UserOTPDetails> otpMap;
	
	@Override
	public boolean isUserAuthenticated(String userIdentifier) {
		
		log.info("Checking isUserAuthenticated: " + userIdentifier);

		UserOTPDetails userOTPDetails = otpMap.get(userIdentifier);
		
		if (userOTPDetails != null)
		{
			log.info("User OTP details: " + userOTPDetails);
			
			if (userOTPDetails.isUserAuthenticated())
			{
				log.info("User Is authenticated, check timestamp before proceeding.");
				
				//Double check to make sure user is still within timeframe
				LocalDateTime expirationTimestamp = userOTPDetails.getExpirationTimestamp();
				LocalDateTime now = LocalDateTime.now();
				
				if (now.isBefore(expirationTimestamp))
				{
					log.info("Timestamp is valid returning true.");
					
					return true;
				}

			}	
			else
			{
				return false;
			}	
		}
		
		log.info("Authentication and timestamp could not be validated.  Return false.");

		removeOldEntries();
		
		return false;
	}
	
	@Override
	public boolean unauthenticateUser(String userIdentifier) {
		log.info("Entering unauthenticate user: " + userIdentifier);

		boolean result = false; 
		removeOldEntries();
		if (otpMap.containsKey(userIdentifier)){
			otpMap.remove(userIdentifier);
			result = true;
		}
		else {
			log.info("User is not authenticated.  Return false.");
		}
		return result;
	}	
	
	private void removeOldEntries() {
		Iterator<Entry<String, UserOTPDetails>> it = otpMap.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<String, UserOTPDetails> pair = (Entry<String, UserOTPDetails>)it.next();

		    UserOTPDetails userOTPDetails = (UserOTPDetails) pair.getValue();
		    
			//Remove expired tokens
			LocalDateTime expirationTimestamp = userOTPDetails.getExpirationTimestamp();
			LocalDateTime now = LocalDateTime.now();
			
			if (now.isAfter(expirationTimestamp))
			{
				log.info("Removing old token for: " + userOTPDetails.getEmailAddress());
				it.remove(); // avoids a ConcurrentModificationException
			}
		}
		
	}

}
