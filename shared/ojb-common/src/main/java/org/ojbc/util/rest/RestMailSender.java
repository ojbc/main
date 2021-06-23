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
package org.ojbc.util.rest;

import java.io.InputStream;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.mail.Email;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.client.RestTemplate;

/**
 * This is a REST implementation which will post the mail message to a REST service who will send it.   
 *
 */

public class RestMailSender implements JavaMailSender {

	private final Log log = LogFactory.getLog(this.getClass());

    private RestTemplate restTemplate;
    
    private String emailRestPostURI;

	@Override
	public void send(final MimeMessagePreparator mimeMessagePreparator)
			throws MailException {
		final MimeMessage mimeMessage = createMimeMessage();
		try {
			mimeMessagePreparator.prepare(mimeMessage);
			final String content = (String) mimeMessage.getContent();

			log.info("Mail Message: " + content);
			
		} catch (final Exception e) {
			throw new MailPreparationException(e);
		}
	}
	
	@Override
	public void send(MimeMessage mimeMessage) throws MailException {
		try {
			
			String content = (String) mimeMessage.getContent();
			String subject = mimeMessage.getSubject();
			Address[] recipients = mimeMessage.getRecipients(RecipientType.TO);
			
			StringBuffer toSb = new StringBuffer();
			
			for (Address recipient : recipients)
			{
				toSb.append(recipient.toString());
				toSb.append(",");
			}	
			
			if (toSb.length() > 1)
			{
				toSb.setLength(toSb.length() - 1);
			}	
			
			postMailMessage(content, subject, toSb.toString());
			
		} catch (final Exception e) {
			throw new MailPreparationException(e);
		}
		
	}

	private void postMailMessage(String content, String subject,
			String recipients) {
		
		Email email = new Email();
		email.setTo(recipients);
		email.setBody(content);
		email.setSubject(subject);
		
		restTemplate.postForObject(emailRestPostURI, email, Email.class);
	}

	@Override
	public void send(MimeMessage... mimeMessages) throws MailException {
		log.info("Method not implementede (mimeMessages[]): ");
		
	}

	@Override
	public void send(MimeMessagePreparator... mimeMessagePreparators)
			throws MailException {
		log.info("Method not implemented (MimeMessagePreparator[]): ");
		
	}	

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		log.info("Entering send Simple Mail Message ");
		
		String content = simpleMessage.getText();
		String subject = simpleMessage.getSubject();
		String[] recipients = simpleMessage.getTo();
		
		StringBuffer toSb = new StringBuffer();
		
		for (String recipient : recipients)
		{
			toSb.append(recipient.toString());
			toSb.append(",");
		}	
		
		if (toSb.length() > 1)
		{
			toSb.setLength(toSb.length() - 1);
		}	
		
		
		postMailMessage(content, subject, toSb.toString());
	}

	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {
		log.info("Method not implemented (SimpleMailMessage[]): ");
		
	}

	@Override
	public MimeMessage createMimeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MimeMessage createMimeMessage(InputStream contentStream)
			throws MailException {
		// TODO Auto-generated method stub
		return null;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getEmailRestPostURI() {
		return emailRestPostURI;
	}

	public void setEmailRestPostURI(String emailRestPostURI) {
		this.emailRestPostURI = emailRestPostURI;
	}


}