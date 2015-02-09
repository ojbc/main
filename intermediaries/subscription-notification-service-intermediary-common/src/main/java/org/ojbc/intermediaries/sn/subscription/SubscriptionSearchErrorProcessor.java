package org.ojbc.intermediaries.sn.subscription;

import org.apache.camel.ExchangeException;
import org.apache.camel.Header;

public class SubscriptionSearchErrorProcessor {

	public String returnSubscriptionSearchAccessDenied()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ssr:SubscriptionSearchResults"); 
		sb.append(" xmlns:iad=\"http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0\""); 
		sb.append(" xmlns:ssr=\"http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0\" ");
		sb.append(" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" ");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		sb.append(" xsi:schemaLocation=\"http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0 ../xsd/exchange_schema.xsd\"> ");
		sb.append("<srm:SearchResultsMetadata>");
		sb.append("    <iad:InformationAccessDenial>");
		sb.append("        <iad:InformationAccessDenialIndicator>true</iad:InformationAccessDenialIndicator>");
		sb.append("        <iad:InformationAccessDenyingSystemNameText>subscriptions</iad:InformationAccessDenyingSystemNameText>");
		sb.append("        <iad:InformationAccessDenialReasonText>improper privileges</iad:InformationAccessDenialReasonText>");
		sb.append("    </iad:InformationAccessDenial>");
		sb.append("</srm:SearchResultsMetadata>");
		sb.append("</ssr:SubscriptionSearchResults>");
		
		
		return sb.toString();
	}

	public String returnSubscriptionSearchError(@ExchangeException Exception ex)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ssr:SubscriptionSearchResults  ");
		sb.append("    xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" "); 
		sb.append("    xmlns:ssr=\"http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0\" "); 
		sb.append("    xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" "); 
		sb.append("    xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\"> ");
		sb.append("    <srm:SearchResultsMetadata> ");
		sb.append("        <srer:SearchRequestError> ");
		sb.append("            <srer:ErrorText>" + ex.getMessage() +"</srer:ErrorText> ");
		sb.append("            <intel:SystemName>Subscriptions</intel:SystemName> ");
		sb.append("        </srer:SearchRequestError> ");
		sb.append("    </srm:SearchResultsMetadata> ");
		sb.append("</ssr:SubscriptionSearchResults> ");
		
		return sb.toString();
	}
	
	public String returnTooManyRecordsError(@Header("subscriptionSearchResultCount") String subscriptionResultCount)
	{
		StringBuffer sb = new StringBuffer();
	
		sb.append("<ssr:SubscriptionSearchResults ");
		sb.append("    xmlns:ssr=\"http://ojbc.org/IEPD/Exchange/SubscriptionSearchResults/1.0\" "); 
		sb.append("    xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" "); 
		sb.append("    xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" > ");
		sb.append("    <srm:SearchResultsMetadata>");
		sb.append("        <srer:SearchErrors>");
		sb.append("            <srer:SearchResultsExceedThresholdError>");
		sb.append("                <srer:SearchResultsRecordCount>" + subscriptionResultCount + "</srer:SearchResultsRecordCount>");
		sb.append("            </srer:SearchResultsExceedThresholdError>");
		sb.append("        </srer:SearchErrors>");
		sb.append("    </srm:SearchResultsMetadata>");
		sb.append("</ssr:SubscriptionSearchResults>");

		return sb.toString();
	}	
	
}
