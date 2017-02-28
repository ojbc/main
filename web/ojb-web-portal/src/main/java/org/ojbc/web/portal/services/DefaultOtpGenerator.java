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

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Generates random OTP tokens using the letters A-Z and digits 0-9.
 * This code was obtained from this link:
 * https://github.com/upcrob/spring-security-otp
 */
@Service("DefaultOtpGenerator")
public class DefaultOtpGenerator implements OtpGenerator {
	private static final char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9'};
	
	private Random rand = new SecureRandom();
	
	@Value("${otpLength:6}")
	int length;
	
	@Override
	public String generateToken() {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(CHARS[rand.nextInt(CHARS.length)]);
		}
		return sb.toString();
	}
}