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

import javax.annotation.Resource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailOutOfBandSendStrategy")
public class EmailOutOfBandSendStrategy implements  OtpOutOfBandSendStrategy {

    /*
     * If ojbcMailSenderBean is defined in cfg file, use the property value, 
     * Otherwise, use the default bean "mockMailSender"
     */

	@Resource (name="${ojbcMailSenderBean:mockMailSender}")
    JavaMailSender ojbcMailSender;	
	
	@Override
	public void sendToken(String oneTimePassword, String recipient) {
		
		StringBuilder body = new StringBuilder();
		body.append("Here is your one time password: " + oneTimePassword );
		
        // takes input from e-mail form
        String recipientAddress = recipient;
        String subject = "Your One Time Password";
         
        // creates a simple e-mail object
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(body.toString());
         
        // sends the e-mail
        ojbcMailSender.send(email);
		
	}

}
