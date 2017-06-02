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

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailOutOfBandSendStrategyTest {

	private JavaMailSender ojbcMailSender;	
	private EmailOutOfBandSendStrategy emailOutOfBandSendStrategy;

	@Before
	public void setup()
	{
		ojbcMailSender = mock(JavaMailSender.class);
		emailOutOfBandSendStrategy = new EmailOutOfBandSendStrategy(); 
		
		emailOutOfBandSendStrategy.ojbcMailSender = ojbcMailSender;
		
		List<String> emailRecipientsLogMessageOnly = new ArrayList<String>();
		emailRecipientsLogMessageOnly.add("someone@local.com");
		
		emailOutOfBandSendStrategy.emailRecipientsLogMessageOnly = emailRecipientsLogMessageOnly;
		
	}

	@Test
	public void testSendToken()
	{
        emailOutOfBandSendStrategy.sendToken("123456", "someone@local.com");
		verify(ojbcMailSender, times(0)).send(Mockito.any(SimpleMailMessage.class));

        emailOutOfBandSendStrategy.sendToken("123456", "someone1@local.com");
		verify(ojbcMailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));

	}
}
