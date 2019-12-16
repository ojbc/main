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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.FirearmSearchResult;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractFirearmSearchResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractFirearmSearchResponseProcessor.class);
	
	public abstract void auditFirearmSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	FirearmSearchResult processFirearmSearchResponse(Document document) throws Exception
	{
		FirearmSearchResult firearmSearchResult = new FirearmSearchResult();

        NodeList firearmSearchResults = XmlUtils.xPathNodeListSearch(document, "/firearm-search-resp-doc:FirearmSearchResults/firearm-search-resp-ext:FirearmSearchResult");

        if (firearmSearchResults != null)
        {
        	firearmSearchResult.setSearchResultsCount(firearmSearchResults.getLength());
        	
        	if (firearmSearchResults.getLength() > 0)
        	{
        		String systemSearchResultURI = XmlUtils.xPathStringSearch(firearmSearchResults.item(0),"firearm-search-resp-ext:SourceSystemNameText");
        		firearmSearchResult.setSystemSearchResultURI(systemSearchResultURI);
        	}
        }	
        
        Node searchResultsMetaata = XmlUtils.xPathNodeSearch(document, "/firearm-search-resp-doc:FirearmSearchResults/srm:SearchResultsMetadata");
        
        if (searchResultsMetaata != null)
        {
        	String errorText = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/srer:ErrorText");
        	
        	if (StringUtils.isNotBlank(errorText))
        	{
        		firearmSearchResult.setSearchResultsErrorIndicator(true);
        		firearmSearchResult.setSearchResultsErrorText(errorText);
        	}	
        	
        	String accessDenialReasonText = XmlUtils.xPathStringSearch(searchResultsMetaata, "iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
        	
        	if (StringUtils.isNotBlank(accessDenialReasonText))
        	{
        		firearmSearchResult.setSearchResultsAccessDeniedIndicator(true);
        		firearmSearchResult.setSearchResultsAccessDeniedText(accessDenialReasonText);
        		
        		String accessDenialSystemName = XmlUtils.xPathStringSearch(searchResultsMetaata, "iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
        		
        		if (StringUtils.isNotBlank(accessDenialSystemName))
        		{
        			firearmSearchResult.setSystemName(accessDenialSystemName);
        		}	
        		
        	}	

        	String searchResultsRecordCount = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchErrors/srer:SearchResultsExceedThresholdError/srer:SearchResultsRecordCount");
        	
        	if (StringUtils.isNotBlank(searchResultsRecordCount))
        	{
        		if (StringUtils.isNotEmpty(searchResultsRecordCount))
        		{
        			try {
        				firearmSearchResult.setSearchResultsCount(Integer.valueOf(searchResultsRecordCount));
					} catch (Exception e) {
						log.error("Unable to set search results error count when too many records returned.");
					}
        			
        			firearmSearchResult.setSearchResultsErrorText("Firearm search returned too many results");
        		}	
        	}	        	
        	
        	String systemName = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/intel:SystemName");
        	
        	if (StringUtils.isNotBlank(systemName))
        	{	
        		firearmSearchResult.setSystemName(systemName);
        	}

        }	
        
        log.debug("Firearm Search Result: " + firearmSearchResult.toString());

        return firearmSearchResult;
	}

}
