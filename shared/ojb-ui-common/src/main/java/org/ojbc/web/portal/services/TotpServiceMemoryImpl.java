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
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("totpServiceMemoryImpl")
public class TotpServiceMemoryImpl{

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	ConcurrentHashMap<String, UserOTPDetails> otpMap;
	/**
	 * If string ends with 'M', minutes.  If string ends with 'S', seconds.
	 */
	@Value("${portal.otpValidityPeriodInMinutes:5M}")
	private String otpValidityPeriod;
	
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
		
		otpMap.put(userIdentifier, userOTPDetails);
		
		return true;
	}
		

}
