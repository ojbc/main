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
package org.ojbc.bundles.utilities.email;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.mail.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailRestImpl implements EmailInterface {

	@Resource (name="${emailRestUtility.mailSenderBean:mockMailSender}")
	JavaMailSender ojbcMailSender;
	
	@Value("${emailRestUtility.fromAddress:test@localhost.local}")
	String fromAddress;
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public Response sendEmail(Email email) {

        SimpleMailMessage emailToSend = new SimpleMailMessage();
        
        emailToSend.setFrom(fromAddress);
        
        if (StringUtils.isBlank(email.getTo()) || StringUtils.isBlank(email.getSubject()) ||  StringUtils.isBlank(email.getBody()))
        {
        	return Response.status(Status.BAD_REQUEST).entity(email).build();
        }	
        
        if (StringUtils.isNotBlank(email.getTo()))
        {	
        	emailToSend.setTo(email.getTo());
        }
        
        if (StringUtils.isNotBlank(email.getSubject()))
        {	
        	emailToSend.setSubject(email.getSubject());
        }

        if (StringUtils.isNotBlank(email.getBody()))
        {	
        	emailToSend.setText(email.getBody());
        }

        // sends the e-mail
        ojbcMailSender.send(emailToSend);

        return Response.status(Status.OK).entity(email).build();
	}
}