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
package org.ojbc.util.fedquery.processor.search;

import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.ojbc.util.helper.PersonSearchResponseErrorBuilderUtils;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.w3c.dom.Document;

public class ErrorProcessor {
	
	private String systemName;

	public Document returnPersonSearchAccessDeniedErrorMessage(Exchange ex) throws Exception
	{
		//This header is set in the access control processor
		AccessControlResponse accessControlResponse = (AccessControlResponse) ex.getIn().getHeader("accessControlResponse");
		
		return PersonSearchResponseErrorBuilderUtils.createPersonSearchAccessDenial(accessControlResponse, systemName);
		
	}

	public Document returnFirearmsSearchAccessDeniedErrorMessage(Exchange ex) throws Exception
	{
		//This header is set in the access control processor
		AccessControlResponse accessControlResponse = (AccessControlResponse) ex.getIn().getHeader("accessControlResponse");
		
		return FirearmsSearchResponseErrorBuilderUtils.createFirearmsSearchAccessDenial(accessControlResponse, systemName);
	}

	public Document returnFirearmsQueryAccessDeniedErrorMessage(Exchange ex) throws Exception
	{
		//This header is set in the access control processor
		AccessControlResponse accessControlResponse = (AccessControlResponse) ex.getIn().getHeader("accessControlResponse");
		
		return FirearmsQueryResponseErrorBuilderUtils.createFirearmsQueryAccessDenial(accessControlResponse, systemName);
	}

	
	public String createFirearmsSearchErrorMessage(@ExchangeException Exception ex)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:FirearmSearchResults ");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/FirearmSearchResults/1.0\" ");
		sb.append("	xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" ");
		sb.append("	xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" ");
		sb.append("	xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" ");
		sb.append("	> ");
		sb.append("	<srm:SearchResultsMetadata>");
		sb.append("		<srer:SearchRequestError>");
		sb.append("			<srer:ErrorText>" +  ex.getMessage() +  "</srer:ErrorText>");
		sb.append("			<intel:SystemName>" + systemName + "</intel:SystemName>");
		sb.append("		</srer:SearchRequestError>");
		sb.append("	</srm:SearchResultsMetadata>");
		sb.append("</exchange:FirearmSearchResults>");

		return sb.toString();
	}

	public String createPersonSearchErrorMessage(@ExchangeException Exception ex)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:PersonSearchResults ");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/PersonSearchResults/1.0\" ");
		sb.append("	xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" ");
		sb.append("	xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" ");
		sb.append("	xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" ");
		sb.append("	>");
		sb.append("	<srm:SearchResultsMetadata>");
		sb.append("		<srer:SearchRequestError>");
		sb.append("			<srer:ErrorText>" +  ex.getMessage() +  "</srer:ErrorText>");
		sb.append("			<intel:SystemName>" + systemName + "</intel:SystemName>");
		sb.append("		</srer:SearchRequestError>");
		sb.append("	</srm:SearchResultsMetadata>");
		sb.append("</exchange:PersonSearchResults>");
		
		return sb.toString();
	}	
	
	public String createFirearmsQueryErrorMessage(@ExchangeException Exception ex, @Header("operationName") String operationName)
	{
    	boolean isPersonOp = "SubmitPersonFirearmRegistrationQueryRequest".equals(operationName);
    	
    	boolean isFirearmOp = "SubmitFirearmRegistrationQueryRequest".equals(operationName);
    	
		StringBuffer sb = new StringBuffer();
		
		if (isFirearmOp)
		{	
			sb.append("<exchange:FirearmRegistrationQueryResults");
		}
		
		if (isPersonOp)
		{	
			sb.append("<exchange:PersonFirearmRegistrationQueryResults");	
		}
			
		sb.append("	xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" "); 
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryResults/1.0\" ");
		sb.append("	xmlns:qre=\"http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0\" ");
		sb.append("	xmlns:qrm=\"http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0\" ");
		sb.append("	>");
		sb.append("	<qrm:QueryResultsMetadata>");
		sb.append("		<qre:QueryRequestError>");
		sb.append("			<qre:ErrorText>" + ex.getMessage() + "</qre:ErrorText>");
		sb.append("			<intel:SystemName>" + systemName + "</intel:SystemName>");
		sb.append("		</qre:QueryRequestError>");
		sb.append("	</qrm:QueryResultsMetadata>");
		
		if (isFirearmOp)
		{	
			sb.append("</exchange:FirearmRegistrationQueryResults>");
		}
		
		if (isPersonOp)
		{	
			sb.append("</exchange:PersonFirearmRegistrationQueryResults>");	
		}

		return sb.toString();
	}
		
	public String createPersonSearchTooManyRecordsErrorMessage(@Header("resultSize") int resultSize)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:PersonSearchResults");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/PersonSearchResults/1.0\" ");
		sb.append("	xmlns:domains=\"http://niem.gov/niem/domains/intelligence/2.1\" ");
		sb.append("	xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" ");
		sb.append("	xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" ");
		sb.append("	> ");
		sb.append("	<srm:SearchResultsMetadata> ");
		sb.append("		<srer:SearchErrors> ");
		sb.append("			<domains:SystemName>" + systemName + "</domains:SystemName> ");
		sb.append("			<srer:SearchResultsExceedThresholdError> ");
		sb.append("				<srer:SearchResultsRecordCount>"+ resultSize  + "</srer:SearchResultsRecordCount> ");
		sb.append("			</srer:SearchResultsExceedThresholdError> ");
		sb.append("		</srer:SearchErrors> ");
		sb.append("	</srm:SearchResultsMetadata> ");
		sb.append("</exchange:PersonSearchResults> ");
		
		return sb.toString();
	}	

	public String createFirearmsSearchTooManyRecordsErrorMessage(@Header("resultSize") int resultSize)
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:FirearmSearchResults");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/FirearmSearchResults/1.0\" ");
		sb.append("	xmlns:domains=\"http://niem.gov/niem/domains/intelligence/2.1\" ");
		sb.append("	xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\" ");
		sb.append("	xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" ");
		sb.append("	> ");
		sb.append("	<srm:SearchResultsMetadata> ");
		sb.append("		<srer:SearchErrors> ");
		sb.append("			<domains:SystemName>" + systemName + "</domains:SystemName> ");
		sb.append("			<srer:SearchResultsExceedThresholdError> ");
		sb.append("				<srer:SearchResultsRecordCount>" + resultSize  + "</srer:SearchResultsRecordCount> ");
		sb.append("			</srer:SearchResultsExceedThresholdError> ");
		sb.append("		</srer:SearchErrors> ");
		sb.append("	</srm:SearchResultsMetadata> ");
		sb.append("</exchange:FirearmSearchResults> ");
		
		return sb.toString();
	}	

	
	@SuppressWarnings("rawtypes")
	public boolean didTheSearchReturnTooManyRecords(Exchange in, @Header("maxRecords") int maxRecords, @Body List records)
	{
		boolean tooManyRecords = false;
		
		//We check for null here to suppor testing without an exchange
		if (in != null)
		{	
			in.getIn().setHeader("resultSize", records.size());
		}
			
		if (records.size() > maxRecords)
		{
			tooManyRecords = true;
		}
			
		return tooManyRecords;
	}
	
	public boolean didTheSearchReturnTooManyRecordsUsingXpathCount(Exchange in, @Header("maxRecords") int maxRecords, @Header("resultSize") int resultSize)
	{
		boolean tooManyRecords = false;
			
		if (resultSize > maxRecords)
		{
			tooManyRecords = true;
		}
			
		return tooManyRecords;
	}	

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
}
