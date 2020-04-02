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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.ojbc.bundles.adapters.fbi.ebts.util.FBIEbtsUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FbiEmailErrorProcessor {

	private static final Logger logger = Logger.getLogger(FbiEmailErrorProcessor.class.getName());
	
	public static final String EMAIL_SUBJECT_LINE="Rap Back: Subscription Failed";
	
	public static final String SUBSCRIPTION_ERROR_EMAIL_TEMPLATE="Error Code:<Error Code>\nError Text:<Error Text>\n\nThis subscription request that was sent to the FBI has failed.";
	
	private String toEmailAddress;
	
	private String emailSubjectPrefix;
	
	public void createErrorEmail(Exchange in) throws Exception
	{
		Document doc = in.getIn().getBody(Document.class);
		
		String errorCode = (String)in.getIn().getHeader("trxCatCode");
		
		String transactionStatusText = FBIEbtsUtils.processTransactionStatusText(doc);
		
		logger.info("Error Text:" + transactionStatusText);
		
		in.getIn().setHeader("subject", emailSubjectPrefix + " " + EMAIL_SUBJECT_LINE);
		in.getIn().setHeader("to", toEmailAddress);
		
		String messageBody = SUBSCRIPTION_ERROR_EMAIL_TEMPLATE;
		
		messageBody = messageBody.replace("<Error Code>", errorCode);
		messageBody = messageBody.replace("<Error Text>", transactionStatusText);
		
		in.getIn().setBody(messageBody);
		
	}

	public String getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}

	public String getEmailSubjectPrefix() {
		return emailSubjectPrefix;
	}

	public void setEmailSubjectPrefix(String emailSubjectPrefix) {
		this.emailSubjectPrefix = emailSubjectPrefix;
	}
	
}
