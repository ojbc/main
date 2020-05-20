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
package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will use the provided URI to error text and system name maps to splice in an error
 * message for a federated search.  It is used when an endpoint times out and sends no response.
 * 
 * It will add the error message in the SearchResultsMetadata container using the the provided
 * parent element name and namespace as the parent wrapper.  The SearchResultsMetadata will 
 * have this XML format:
 * 
 *  <srm:SearchResultsMetadata>
 *		<srer:SearchRequestError>
 *			<srer:ErrorText>This is an error from System.</srer:ErrorText>
 *			<intel:SystemName>System Name</intel:SystemName>
 *		</srer:SearchRequestError>
 *	</srm:SearchResultsMetadata>
 *
 * It is used rather than the default since this service has three operations and the default only supports a single operation/response container
 * 
 */
public class IncidentSearchTimeoutProcessor implements FederatedQueryTimeoutProcessorStrategy{

	private static final Log LOGGER = LogFactory.getLog(IncidentSearchTimeoutProcessor.class);
	
	private Map<String, String> uriToErrorMessageMap = new HashMap<String, String>();
	
	private Map<String, String> uriToErrorSystemNameMap= new HashMap<String, String>();
	
	@Override
	public Document createErrorMessagesForEndpointsThatTimedOut(@Body Document response, @Header("endpointsThatDidNotRespond") List<String> endpointsThatDidNotRespond) throws Exception {
		
		if (endpointsThatDidNotRespond == null )
		{
			LOGGER.info("All endpoints reponsed.  EndpointsThatDidNotResponse variable is null");
			return response;
		}	
		
		for (String endpointThatDidNotRespond : endpointsThatDidNotRespond)
		{
			if (uriToErrorMessageMap.containsKey(endpointThatDidNotRespond))
			{
				Element wrapperElement = (Element)XmlUtils.xPathNodeSearch(response, "/OJBAggregateResponseWrapper");
				
				String parentElementName="";
				String parentElementNamespace=OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC;
				
				if (endpointThatDidNotRespond.contains("SubmitIncidentSearchRequest"))
				{
					parentElementName="IncidentSearchResults";
				}	

				if (endpointThatDidNotRespond.contains("SubmitIncidentPersonSearchRequest"))
				{
					parentElementName="IncidentPersonSearchResults";
				}	

				if (endpointThatDidNotRespond.contains("SubmitIncidentVehicleSearchRequest"))
				{
					parentElementName="IncidentVehicleSearchResults";
				}	
				
				LOGGER.info("Parent Element Name is: " + parentElementName);
				
				Element errorContainerElement = XmlUtils.appendElement(wrapperElement, parentElementNamespace, parentElementName);
				
				Element searchResultsMetadata = XmlUtils.appendElement(errorContainerElement, OjbcNamespaceContext.NS_SEARCH_RESULTS_METADATA_EXT, "SearchResultsMetadata");
				
				Element searchRequestError = XmlUtils.appendElement(searchResultsMetadata, OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchRequestError");
				
				Element errorText = XmlUtils.appendElement(searchRequestError, OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING, "ErrorText");
				errorText.setTextContent(uriToErrorMessageMap.get(endpointThatDidNotRespond));
				
				Element systemName = XmlUtils.appendElement(searchRequestError, OjbcNamespaceContext.NS_INTEL, "SystemName");
				
				if (uriToErrorSystemNameMap.containsKey(endpointThatDidNotRespond))
				{	
					systemName.setTextContent(uriToErrorSystemNameMap.get(endpointThatDidNotRespond));
				}
			}	
		}

		return response;
		
	}

	public Map<String, String> getUriToErrorMessageMap() {
		return uriToErrorMessageMap;
	}

	public void setUriToErrorMessageMap(Map<String, String> uriToErrorMessageMap) {
		this.uriToErrorMessageMap = uriToErrorMessageMap;
	}

	public Map<String, String> getUriToErrorSystemNameMap() {
		return uriToErrorSystemNameMap;
	}

	public void setUriToErrorSystemNameMap(
			Map<String, String> uriToErrorSystemNameMap) {
		this.uriToErrorSystemNameMap = uriToErrorSystemNameMap;
	}

	
	
}
