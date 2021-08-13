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
package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractPersonSearchResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractPersonSearchResponseProcessor.class);
	
	public abstract void auditPersonSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	PersonSearchResult processPersonSearchResponse(Document document) throws Exception
	{
		PersonSearchResult personSearchResult = new PersonSearchResult();

        NodeList personSearchResults = XmlUtils.xPathNodeListSearch(document, "/psres-doc:PersonSearchResults/psres:PersonSearchResult");

        if (personSearchResults != null)
        {
        	personSearchResult.setSearchResultsCount(personSearchResults.getLength());
        	
        	if (personSearchResults.getLength() > 0)
        	{
        		String systemSearchResultURI = XmlUtils.xPathStringSearch(personSearchResults.item(0),"psres:SourceSystemNameText");
        		personSearchResult.setSystemSearchResultURI(systemSearchResultURI);
        	}
        }	
        
        Node searchResultsMetaata = XmlUtils.xPathNodeSearch(document, "/psres-doc:PersonSearchResults/srm:SearchResultsMetadata");
        
        if (searchResultsMetaata != null)
        {
        	String errorText = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/srer:ErrorText");
        	
        	if (StringUtils.isNotBlank(errorText))
        	{
        		personSearchResult.setSearchResultsErrorIndicator(true);
        		personSearchResult.setSearchResultsErrorText(errorText);
        	}	
        	
        	String accessDenialReasonText = XmlUtils.xPathStringSearch(searchResultsMetaata, "iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
        	
        	if (StringUtils.isNotBlank(accessDenialReasonText))
        	{
        		personSearchResult.setSearchResultsAccessDeniedIndicator(true);
        		personSearchResult.setSearchResultsAccessDeniedText(accessDenialReasonText);
        	}	

        	String searchResultsRecordCount = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchErrors/srer:SearchResultsExceedThresholdError/srer:SearchResultsRecordCount");
        	
        	if (StringUtils.isNotBlank(searchResultsRecordCount))
        	{
        		if (StringUtils.isNotEmpty(searchResultsRecordCount))
        		{
        			try {
						personSearchResult.setSearchResultsCount(Integer.valueOf(searchResultsRecordCount));
					} catch (Exception e) {
						log.error("Unable to set search results error count when too many records returned.");
					}
        			
        			personSearchResult.setSearchResultsErrorText("Person search returned too many results");
        		}	
        	}	        	
        	
        	String systemName = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/intel:SystemName");
        	personSearchResult.setSystemName(systemName);

        }	
        
        log.debug("Person Search Result: " + personSearchResult.toString());

        return personSearchResult;
	}

}
