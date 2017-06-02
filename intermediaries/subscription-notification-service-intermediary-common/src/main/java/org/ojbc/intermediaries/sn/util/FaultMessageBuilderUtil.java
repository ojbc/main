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
package org.ojbc.intermediaries.sn.util;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class FaultMessageBuilderUtil {

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd'T'HH:mm:ss";
	
	public static String createFault(String faultName, String namespace)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<" + faultName + " xmlns=\"" + namespace + "\"/>");
		
		return sb.toString();
	}

	public static String createUnableToDestorySubscriptionFault() {
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();
		
		sb.append("<wsnt:UnableToDestroySubscriptionFault xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" ");
		sb.append(" xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\"> ");
		sb.append("    <wsrf-bf:Timestamp>" + faultTime.toString(DATE_FORMAT_NOW)  + "</wsrf-bf:Timestamp>");
		sb.append("    <wsrf-bf:Description>No existing subscription found.</wsrf-bf:Description>");
		sb.append("</wsnt:UnableToDestroySubscriptionFault>");

		return sb.toString();		
	}
	
	public static String createSubscribeCreationFailedFaultErrorResponse(String errorText) {

		
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();

		sb.append("<wsnt:SubscribeCreationFailedFault ");
		sb.append("	xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" ");
		sb.append("	xmlns:fault_exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:fault_ext=\"http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" "); 
		sb.append("	xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" >");
		sb.append("		<fault_exchange:SubscriptionFaultResponseMessage>");
		sb.append("			<fault_ext:SubscriptionRequestError>");
		sb.append("				<fault_ext:ErrorText>" +  errorText + "</fault_ext:ErrorText>");
		sb.append("				<intel:SystemName>Subscription Notification</intel:SystemName>");
		sb.append("			</fault_ext:SubscriptionRequestError>");
		sb.append("		</fault_exchange:SubscriptionFaultResponseMessage>");		
		sb.append("		<wsrf-bf:Timestamp>" + faultTime.toString("yyyy-MM-dd'T'HH:mm:ss")  + "</wsrf-bf:Timestamp> ");
		sb.append("		<wsrf-bf:Description>Unable to create subscription</wsrf-bf:Description> ");
		sb.append("</wsnt:SubscribeCreationFailedFault>");
		
		return sb.toString();
	}

	public static String createSubscribeCreationFailedFaultInvalidEmailResponse(List<String> emailAddresses) {

		
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();

		sb.append("<wsnt:SubscribeCreationFailedFault ");
		sb.append("	xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" ");
		sb.append("	xmlns:fault_exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:fault_ext=\"http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:nc20=\"http://niem.gov/niem/niem-core/2.0\" ");
		sb.append("	xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" >");
		sb.append("		<fault_exchange:SubscriptionFaultResponseMessage>");
		sb.append("			<fault_ext:InvalidEmailAddressList>");
		
		for (String emailAddress : emailAddresses)
		{	
			sb.append("			<nc20:EmailRecipientAddressID>" + emailAddress + "</nc20:EmailRecipientAddressID>");
		}	
		
		sb.append("			</fault_ext:InvalidEmailAddressList>");
		sb.append("		</fault_exchange:SubscriptionFaultResponseMessage>");		
		sb.append("		<wsrf-bf:Timestamp>" + faultTime.toString("yyyy-MM-dd'T'HH:mm:ss")  + "</wsrf-bf:Timestamp> ");
		sb.append("		<wsrf-bf:Description>Unable to create subscription</wsrf-bf:Description> ");
		sb.append("</wsnt:SubscribeCreationFailedFault>");
		
		return sb.toString();
	}

	public static String createSubscribeCreationFailedFaultInvalidTokenResponse(String message) {

		
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();

		sb.append("<wsnt:SubscribeCreationFailedFault ");
		sb.append("	xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" ");
		sb.append("	xmlns:fault_exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:fault_ext=\"http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" >");
		sb.append("		<fault_exchange:SubscriptionFaultResponseMessage>");
		sb.append("			<fault_ext:InvalidSecurityTokenIndicator>true</fault_ext:InvalidSecurityTokenIndicator>");
		sb.append("			<fault_ext:InvalidSecurityTokenDescriptionText>" + message +"</fault_ext:InvalidSecurityTokenDescriptionText>");
		sb.append("		</fault_exchange:SubscriptionFaultResponseMessage>");		
		sb.append("		<wsrf-bf:Timestamp>" + faultTime.toString("yyyy-MM-dd'T'HH:mm:ss")  + "</wsrf-bf:Timestamp> ");
		sb.append("		<wsrf-bf:Description>" +  message + "</wsrf-bf:Description> ");
		sb.append("</wsnt:SubscribeCreationFailedFault>");
		
		return sb.toString();
	}

	public static String createSubscribeCreationFailedFaultAccessDenialResponse(String accessDeniedText) {

		
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();

		sb.append("<wsnt:SubscribeCreationFailedFault ");
		sb.append("	xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" ");
		sb.append("	xmlns:fault_exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:fault_ext=\"http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" >");
		sb.append("		<fault_exchange:SubscriptionFaultResponseMessage>");
		sb.append("			<fault_ext:AccessDenial>");
		sb.append("				<fault_ext:AccessDenialIndicator>true</fault_ext:AccessDenialIndicator>");
		sb.append("				<fault_ext:AccessDenyingSystemNameText>Subscription Notification</fault_ext:AccessDenyingSystemNameText>");
		sb.append("				<fault_ext:AccessDenialReasonText>" +  accessDeniedText +"</fault_ext:AccessDenialReasonText>");
		sb.append("			</fault_ext:AccessDenial>");
		sb.append("		</fault_exchange:SubscriptionFaultResponseMessage>");		
		sb.append("		<wsrf-bf:Timestamp>" + faultTime.toString("yyyy-MM-dd'T'HH:mm:ss")  + "</wsrf-bf:Timestamp> ");
		sb.append("		<wsrf-bf:Description>Unable to create subscription</wsrf-bf:Description> ");
		sb.append("</wsnt:SubscribeCreationFailedFault>");
		
		return sb.toString();
	}

	public static String createUnableToDestorySubscriptionFaultAccessDenialResponse(String accessDeniedText) {
		
		StringBuffer sb = new StringBuffer();
		
		DateTime faultTime = new DateTime();

		sb.append("<b:UnableToDestroySubscriptionFault xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\" ");
		sb.append("	xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" ");
		sb.append("	xmlns:fault_exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Fault_Response/1.0\" ");
		sb.append("	xmlns:fault_ext=\"http://ojbc.org/IEPD/Extension/Subscription_Fault_Response/1.0\" >");
		sb.append("		<fault_exchange:SubscriptionFaultResponseMessage>");
		sb.append("			<fault_ext:AccessDenial>");
		sb.append("				<fault_ext:AccessDenialIndicator>true</fault_ext:AccessDenialIndicator>");
		sb.append("				<fault_ext:AccessDenyingSystemNameText>Subscription Notification</fault_ext:AccessDenyingSystemNameText>");
		sb.append("				<fault_ext:AccessDenialReasonText>" +  accessDeniedText +"</fault_ext:AccessDenialReasonText>");
		sb.append("			</fault_ext:AccessDenial>");
		sb.append("		</fault_exchange:SubscriptionFaultResponseMessage>");		
		sb.append("		<wsrf-bf:Timestamp>" + faultTime.toString("yyyy-MM-dd'T'HH:mm:ss")  + "</wsrf-bf:Timestamp> ");
		sb.append("		<wsrf-bf:Description>Unable to create subscription</wsrf-bf:Description> ");
		sb.append("</b:UnableToDestroySubscriptionFault>");
		
		return sb.toString();
	}

	public static Document createUnableToValidateSubscriptionFault(@Header(value = "subscriptionID") String subscriptionID) throws Exception
	{
			DateTime faultTime = new DateTime();
		
	        Document doc = null;

	        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
	        docBuilderFact.setNamespaceAware(true);
	        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

	        doc = docBuilder.newDocument();
	        Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "UnableToValidateSubscriptionFault");
	        doc.appendChild(root);
	        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);

	        Element subscriptionValidationFaultResponseMessageElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_FAULT_RESPONSE_EXCH, "SubscriptionValidationFaultResponseMessage");
	        
	        if (StringUtils.isNotBlank(subscriptionID))
	        {	
		        Element subscriptionIdentificationElement = XmlUtils.appendElement(subscriptionValidationFaultResponseMessageElement, OjbcNamespaceContext.NS_SUBSCRIPTION_FAULT_RESPONSE_EXT, "SubscriptionIdentification");
		        
		        Element identificationIDElement = XmlUtils.appendElement(subscriptionIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
		        identificationIDElement.setTextContent(subscriptionID);
		        
		        Element identificationSourceTextElement = XmlUtils.appendElement(subscriptionIdentificationElement, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
		        identificationSourceTextElement.setTextContent("Subscriptions");
	        }     

	        Element invalidSubscriptionIdentifierIndicatorElement = XmlUtils.appendElement(subscriptionValidationFaultResponseMessageElement, OjbcNamespaceContext.NS_SUBSCRIPTION_FAULT_RESPONSE_EXT, "IdentificationSourceText");
	        invalidSubscriptionIdentifierIndicatorElement.setTextContent("true");
	        
	        Element timestampElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_BF2, "Timestamp");
	        timestampElement.setTextContent(faultTime.toString("yyyy-MM-dd'T'HH:mm:ss"));
	        
			return doc;
	}
}
