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

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("totpServiceMemoryImpl")
public class TotpServiceMemoryImpl implements OTPService{

	private final Log log = LogFactory.getLog(this.getClass());
	
	private ConcurrentHashMap<String, UserOTPDetails> totpMap = new ConcurrentHashMap<String, UserOTPDetails>();
	
	/**
	 * If string ends with 'M', minutes.  If string ends with 'S', seconds.
	 */
	@Value("${otpValidityPeriodInMinutes:5M}")
	String otpValidityPeriod;
	
	@Resource (name="${otpOutOfBandSendStrategyBean:EmailOutOfBandSendStrategy}")
	OtpOutOfBandSendStrategy otpOutOfBandSendStrategy;
	
	@Override
	public boolean confirmOTP(String userIdentifier, String enteredOtp) {

		UserOTPDetails userOTPDetails = new UserOTPDetails();
		
		userOTPDetails.setOneTimePassword(enteredOtp);
		userOTPDetails.setEmailAddress(userIdentifier);
		
		LocalDateTime expirationTimestamp = null;
		
		if (StringUtils.endsWith(String.valueOf(otpValidityPeriod), "M"))
		{	
			expirationTimestamp = LocalDateTime.now().plusMinutes(Long.valueOf(StringUtils.removeEnd(otpValidityPeriod.toUpperCase(), "M")));
		} 
		else if (StringUtils.endsWith(String.valueOf(otpValidityPeriod), "S"))
		{
			expirationTimestamp = LocalDateTime.now().plusSeconds(Long.valueOf(StringUtils.removeEnd(otpValidityPeriod.toUpperCase(), "S")));
		}	
		else
		{
			expirationTimestamp = LocalDateTime.now();
		}	
		
		userOTPDetails.setExpirationTimestamp(expirationTimestamp);
		userOTPDetails.setUserAuthenticated(true);
		
		totpMap.put(userIdentifier, userOTPDetails);
		
		return true;
	}
	
	@Override
	public boolean isUserAuthenticated(String userIdentifier) {
		
		log.info("Checking isUserAuthenticated: " + userIdentifier);
		
		UserOTPDetails userOTPDetails = totpMap.get(userIdentifier);
		
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
		
		UserOTPDetails userOTPDetails = totpMap.get(userIdentifier);
		
		if (userOTPDetails != null)
		{
			totpMap.remove(userIdentifier);
			return true;
		}
		
		log.info("User is not authenticated.  Return false.");

		removeOldEntries();
		
		return false;
	}	
	
	private void removeOldEntries() {
		Iterator<Entry<String, UserOTPDetails>> it = totpMap.entrySet().iterator();
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

	@Override
	public String generateOTP(String userIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}



}
