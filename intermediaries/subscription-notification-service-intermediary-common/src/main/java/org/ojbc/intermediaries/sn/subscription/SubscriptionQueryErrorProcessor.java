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
