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
package org.ojbc.processor.error;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class MergeNotificationMaxRecordProcessor {
	
	private static final Log log = LogFactory.getLog(MergeNotificationMaxRecordProcessor.class);
	
	private Map<String, String> federatedQueryEndpointMap = new HashMap<String, String>();
	
	public String returnMergeNotificationErrorMessageTooManyRecords(Document resultsDocument, @Header("personSearchMaxRecords") String maxRecords) throws Exception
	{
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("The search results contained too many records, please refine your search.  The maximum combined number of records that will display is " + maxRecords  + ".<br />");

		for (String systemName : federatedQueryEndpointMap.keySet()) {
		    String count = XmlUtils.xPathStringSearch(resultsDocument, "count(/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/psres:PersonSearchResult[normalize-space(psres:SourceSystemNameText)='" + systemName + "'])");
		    
		    String systemDisplayName = "";
		    
		    if (Integer.valueOf(count) > 0 )
		    {
		    	systemDisplayName = XmlUtils.xPathStringSearch(resultsDocument, "normalize-space(/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/psres:PersonSearchResult[normalize-space(psres:SourceSystemNameText)='" + systemName + "'][1]/intel:SystemIdentifier/intel:SystemName)");
		    	errorMessage.append("The " + systemDisplayName + " search results contained " + count + " records.<br />");
		    }	
		    
		}
		
		log.debug("The error message is: " + errorMessage.toString());
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exc:EntityMergeResultMessage");
		sb.append(" xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\""); 
		sb.append(" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\"");
		sb.append(" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\">");
		sb.append(" <exc:SearchResultsMetadataCollection>");
		sb.append(" 	<srm:SearchResultsMetadata>");
		sb.append(" 		<srer:SearchRequestError>");
		sb.append(" 			<srer:ErrorText>" + errorMessage.toString() + "</srer:ErrorText>");
		sb.append(" 			<intel:SystemName>All Systems</intel:SystemName>");
		sb.append(" 		</srer:SearchRequestError>");
		sb.append(" 	</srm:SearchResultsMetadata>");
		sb.append(" </exc:SearchResultsMetadataCollection>");	
		sb.append("</exc:EntityMergeResultMessage>");
		
		return sb.toString();
	}

	public Map<String, String> getFederatedQueryEndpointMap() {
		return federatedQueryEndpointMap;
	}

	public void setFederatedQueryEndpointMap(
			Map<String, String> federatedQueryEndpointMap) {
		this.federatedQueryEndpointMap = federatedQueryEndpointMap;
	}
}
