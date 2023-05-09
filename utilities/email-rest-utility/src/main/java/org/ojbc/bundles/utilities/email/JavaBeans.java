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
package org.ojbc.bundles.utilities.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaBeans {
	private static final Log log = LogFactory.getLog( JavaBeans.class );
    @Autowired 
    AppProperties appProperties;
    
    @Bean
    JavaMailSender ojbcMailSender() {
    	JavaMailSenderImpl ojbcMailSender = new JavaMailSenderImpl(); 
    	ojbcMailSender.setHost(appProperties.getHost());
    	ojbcMailSender.setPort(appProperties.getPort());
    	
    	ojbcMailSender.getJavaMailProperties().put("mail.transport.protocol", appProperties.getMailSenderTransportProtocol()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.auth", appProperties.getMailSenderSmtpAuth()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", appProperties.getMailSenderSmtpStarttlesEnable()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.starttls.required", appProperties.getMailSenderSmtpStarttlesEnable()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.smtp.ssl.protocols", appProperties.getMailSenderSmtpSSlProtocol()); 
    	ojbcMailSender.getJavaMailProperties().put("mail.debug", appProperties.getMailSenderDebug());

    	log.info("Created ojbcMailSender bean with the properties: " + ojbcMailSender.getJavaMailProperties());
    	log.info("ojbcMailSender host: " + ojbcMailSender.getHost());
    	log.info("ojbcMailSender port: " + ojbcMailSender.getPort());
    	return ojbcMailSender;
    }    
}