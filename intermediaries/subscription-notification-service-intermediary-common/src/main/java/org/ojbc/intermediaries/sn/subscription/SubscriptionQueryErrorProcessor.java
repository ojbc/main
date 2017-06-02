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
package org.ojbc.intermediaries.sn.subscription;

import org.apache.camel.ExchangeException;

public class SubscriptionQueryErrorProcessor {

	public String returnSubscriptionQueryAccessDenied()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:SubscriptionQueryResults");
		sb.append("    xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/SubscriptionQueryResults/1.0\" ");
		sb.append("    xmlns:iad=\"http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0\" ");
		sb.append("    xmlns:qrm=\"http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0\" >");
		sb.append("    <qrm:QueryResultsMetadata>");
		sb.append("        <iad:InformationAccessDenial>");
		sb.append("            <iad:InformationAccessDenialIndicator>true</iad:InformationAccessDenialIndicator>");
		sb.append("            <iad:InformationAccessDenyingSystemNameText>Subscriptions</iad:InformationAccessDenyingSystemNameText>");
		sb.append("            <iad:InformationAccessDenialReasonText>improper privileges</iad:InformationAccessDenialReasonText>");
		sb.append("        </iad:InformationAccessDenial>");
		sb.append("    </qrm:QueryResultsMetadata>");
		sb.append("</exchange:SubscriptionQueryResults>");		
		
		return sb.toString();
	}

	public String returnSubscriptionQueryError(@ExchangeException Exception ex)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:SubscriptionQueryResults ");
		sb.append("    xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" ");
		sb.append("    xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/SubscriptionQueryResults/1.0\" ");
		sb.append("    xmlns:qre=\"http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0\" ");
		sb.append("    xmlns:qrm=\"http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0\">");
		sb.append("    <qrm:QueryResultsMetadata>");
		sb.append("        <qre:QueryRequestError>");
		sb.append("            <qre:ErrorText>" + ex.getMessage() + "</qre:ErrorText>");
		sb.append("            <intel:SystemName>Subscriptions</intel:SystemName>");
		sb.append("        </qre:QueryRequestError>");
		sb.append("    </qrm:QueryResultsMetadata>");
		sb.append("</exchange:SubscriptionQueryResults>");
		
		return sb.toString();
	}
	
}
