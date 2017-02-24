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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.services;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.ojbc.web.security.UserOTPDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("OTPServiceMemoryImpl")
public class OTPServiceMemoryImpl implements OTPService{

	private ConcurrentHashMap<String, UserOTPDetails> otpMap = new ConcurrentHashMap<String, UserOTPDetails>();
	
	@Resource (name="${otpGeneratorBean:DefaultOtpGenerator}")
	OtpGenerator otpGenerator;
	
	@Value("${otpValidityPeriodInMinutes:5}")
	long otpValidityPeriodInMinutes;
	
	@Resource (name="${otpOutOfBandSendStrategyBean:EmailOutOfBandSendStrategy}")
	OtpOutOfBandSendStrategy otpOutOfBandSendStrategy;
	
	@Override
	public String generateOTP(String userIdentifier) {

		String oneTimePassword = otpGenerator.generateToken();
		
		UserOTPDetails userOTPDetails = new UserOTPDetails();
		
		userOTPDetails.setOneTimePassword(oneTimePassword);
		userOTPDetails.setEmailAddress(userIdentifier);
		
		LocalTime expirationTimestamp = LocalTime.now().plusMinutes(otpValidityPeriodInMinutes);
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
			
			LocalTime expirationTimestamp = userOTPDetails.getExpirationTimestamp();
			LocalTime now = LocalTime.now();
			
			if (now.isBefore(expirationTimestamp) && expectedOneTimePassword.equals(enteredOtp))
			{
				return true;
			}	
		}	
		
		return false;
	}

	public ConcurrentHashMap<String, UserOTPDetails> getOtpMap() {
		return otpMap;
	}

	public void setOtpMap(ConcurrentHashMap<String, UserOTPDetails> otpMap) {
		this.otpMap = otpMap;
	}

}
