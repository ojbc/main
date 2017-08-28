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
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("OTPServiceMemoryImpl")
public class OTPServiceMemoryImpl implements OTPService{

	private final Log log = LogFactory.getLog(this.getClass());
	
	private ConcurrentHashMap<String, UserOTPDetails> otpMap = new ConcurrentHashMap<String, UserOTPDetails>();
	
	@Resource (name="${otpGeneratorBean:DefaultOtpGenerator}")
	OtpGenerator otpGenerator;
	
	/**
	 * If string ends with 'M', minutes.  If string ends with 'S', seconds.
	 */
	@Value("${otpValidityPeriodInMinutes:5M}")
	String otpValidityPeriod;
	
	@Resource (name="${otpOutOfBandSendStrategyBean:EmailOutOfBandSendStrategy}")
	OtpOutOfBandSendStrategy otpOutOfBandSendStrategy;
	
	@Override
	public String generateOTP(String userIdentifier) {

		String oneTimePassword = otpGenerator.generateToken();
		
		UserOTPDetails userOTPDetails = new UserOTPDetails();
		
		userOTPDetails.setOneTimePassword(oneTimePassword);
		userOTPDetails.setEmailAddress(userIdentifier);
		
		LocalDateTime expirationTimestamp = null;
		
		if (StringUtils.endsWith(String.valueOf(otpValidityPeriod), "M"))
		{	
			expirationTimestamp = LocalDateTime.now().plusMinutes(Long.valueOf(StringUtils.chomp(otpValidityPeriod.toUpperCase(), "M")));
		} 
		else if (StringUtils.endsWith(String.valueOf(otpValidityPeriod), "S"))
		{
			expirationTimestamp = LocalDateTime.now().plusSeconds(Long.valueOf(StringUtils.chomp(otpValidityPeriod.toUpperCase(), "S")));
		}	
		else
		{
			expirationTimestamp = LocalDateTime.now();
		}	
		
		userOTPDetails.setExpirationTimestamp(expirationTimestamp);
		
		otpMap.put(userIdentifier, userOTPDetails);
		
		otpOutOfBandSendStrategy.sendToken(oneTimePassword, userIdentifier);
		
		return oneTimePassword;
	}

	@Override
	public boolean confirmOTP(String userIdentifier, String enteredOtp) {

		UserOTPDetails userOTPDetails = otpMap.get(userIdentifier);
		
		if (userOTPDetails != null)
		{
			String expectedOneTimePassword = userOTPDetails.getOneTimePassword();
			
			LocalDateTime expirationTimestamp = userOTPDetails.getExpirationTimestamp();
			LocalDateTime now = LocalDateTime.now();
			
			if (now.isBefore(expirationTimestamp) && expectedOneTimePassword.equals(enteredOtp))
			{
				otpMap.get(userIdentifier).setUserAuthenticated(true);
				return true;
			}	
		}	
		
		return false;
	}
	
	@Override
	public boolean isUserAuthenticated(String userIdentifier) {
		
		log.info("Entering Is user authenticated: " + userIdentifier);
		
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
		
		UserOTPDetails userOTPDetails = otpMap.get(userIdentifier);
		
		if (userOTPDetails != null)
		{
			otpMap.remove(userIdentifier);
			return true;
		}
		
		log.info("User is not authenticated.  Return false.");

		removeOldEntries();
		
		return false;
	}	
	
	private void removeOldEntries() {
		Iterator it = otpMap.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry pair = (Map.Entry)it.next();

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

	public ConcurrentHashMap<String, UserOTPDetails> getOtpMap() {
		return otpMap;
	}

	public void setOtpMap(ConcurrentHashMap<String, UserOTPDetails> otpMap) {
		this.otpMap = otpMap;
	}



}
