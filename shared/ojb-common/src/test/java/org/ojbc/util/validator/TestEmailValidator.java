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
package org.ojbc.util.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestEmailValidator {

	@Test
	public void testAreEmailAddressesValidNoPatternSpecified()
	{
		
		assertTrue(EmailValidator.validateEmailAddress("1@1.com"));
		assertTrue(EmailValidator.validateEmailAddress("test@localhost.local"));
		assertTrue(EmailValidator.validateEmailAddress("test@localhost"));
		assertFalse(EmailValidator.validateEmailAddress("1@1@.com"));
		assertFalse(EmailValidator.validateEmailAddress("1.com"));
		assertFalse(EmailValidator.validateEmailAddress(".com"));
		assertFalse(EmailValidator.validateEmailAddress("().com"));
		assertFalse(EmailValidator.validateEmailAddress("@.com"));
		assertFalse(EmailValidator.validateEmailAddress("1..@.com"));
		
		
	}

	@Test
	public void testAreEmailAddressesValidSuccess()
	{		
		assertTrue(EmailValidator.testEmailAddressAgainstRegularExpression("1@1.com", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)"));
		assertFalse(EmailValidator.testEmailAddressAgainstRegularExpression("1@3.com", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)"));
	}

	
}
