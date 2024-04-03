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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.ojbc.util.mail.MockMailSender;
import org.ojbc.web.security.UserOTPDetails;

public class OTPServiceMemoryImplTest {

	@Test
	public void testDefaultOtpGenerationAndValidation() throws Exception
	{
		OTPServiceMemoryImpl emailedOtpService = new OTPServiceMemoryImpl();
		OtpServiceImpl otpService = new OtpServiceImpl();
		
		emailedOtpService.otpMap = new ConcurrentHashMap<String, UserOTPDetails>();
		otpService.otpMap = emailedOtpService.otpMap;
		emailedOtpService.otpValidityPeriod = "2S";
		
		DefaultOtpGenerator defaultOtpGenerator = new DefaultOtpGenerator();
		defaultOtpGenerator.length = 6;
		 
		emailedOtpService.otpGenerator = defaultOtpGenerator;
		
		EmailOutOfBandSendStrategy emailOutOfBandSendStrategy = new EmailOutOfBandSendStrategy();
		
		List<String> emailRecipientsLogMessageOnly = new ArrayList<String>();
		emailRecipientsLogMessageOnly.add("someone@local.com");
		
		emailOutOfBandSendStrategy.emailRecipientsLogMessageOnly = emailRecipientsLogMessageOnly;
		
		emailOutOfBandSendStrategy.ojbcMailSender = new MockMailSender();
		
		emailedOtpService.otpOutOfBandSendStrategy = emailOutOfBandSendStrategy;
		
		String emailAddress = "me@email.com";
		
		String oneTimePassword = emailedOtpService.generateOTP(emailAddress);
		assertEquals(6, oneTimePassword.length());
		
		assertTrue(emailedOtpService.confirmOTP(emailAddress, oneTimePassword));
		assertTrue(otpService.isUserAuthenticated(emailAddress));

		oneTimePassword = emailedOtpService.generateOTP(emailAddress);
		
		//Sleep for two seconds so OTP expires
		Thread.sleep(2000);

		assertFalse(emailedOtpService.confirmOTP(emailAddress, oneTimePassword));
		assertFalse(otpService.isUserAuthenticated(emailAddress));
		
		oneTimePassword = emailedOtpService.generateOTP(emailAddress);
		assertTrue(emailedOtpService.confirmOTP(emailAddress, oneTimePassword));
		
		//Sleep for two seconds so OTP expires
		Thread.sleep(2000);

		assertFalse(otpService.isUserAuthenticated(emailAddress));
		
	}
	
	
}
