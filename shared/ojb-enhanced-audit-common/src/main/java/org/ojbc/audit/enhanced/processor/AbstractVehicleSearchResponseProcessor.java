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
import org.ojbc.audit.enhanced.dao.model.VehicleSearchResult;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractVehicleSearchResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractVehicleSearchResponseProcessor.class);
	
	public abstract void auditVehicleSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	VehicleSearchResult processVehicleSearchResponse(Document document) throws Exception
	{
		VehicleSearchResult vehicleSearchResult = new VehicleSearchResult();

        NodeList vehicleSearchResults = XmlUtils.xPathNodeListSearch(document, "/vsres-exch:VehicleSearchResults/vsres:VehicleSearchResult");

        if (vehicleSearchResults != null)
        {
        	vehicleSearchResult.setSearchResultsCount(vehicleSearchResults.getLength());
        	
        	if (vehicleSearchResults.getLength() > 0)
        	{
        		String systemSearchResultURI = XmlUtils.xPathStringSearch(vehicleSearchResults.item(0),"vsres:SourceSystemNameText");
        		vehicleSearchResult.setSystemSearchResultURI(systemSearchResultURI);
        	}
        }	
        
        Node searchResultsMetaata = XmlUtils.xPathNodeSearch(document, "/vsres-exch:VehicleSearchResults/srm:SearchResultsMetadata");
        
        if (searchResultsMetaata != null)
        {
        	String errorText = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/srer:ErrorText");
        	
        	if (StringUtils.isNotBlank(errorText))
        	{
        		vehicleSearchResult.setSearchResultsErrorIndicator(true);
        		vehicleSearchResult.setSearchResultsErrorText(errorText);
        	}	
        	
        	String accessDenialReasonText = XmlUtils.xPathStringSearch(searchResultsMetaata, "iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
        	
        	if (StringUtils.isNotBlank(accessDenialReasonText))
        	{
        		vehicleSearchResult.setSearchResultsAccessDeniedIndicator(true);
        		vehicleSearchResult.setSearchResultsAccessDeniedText(accessDenialReasonText);
        	}	

        	String searchResultsRecordCount = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchErrors/srer:SearchResultsExceedThresholdError/srer:SearchResultsRecordCount");
        	
        	if (StringUtils.isNotBlank(searchResultsRecordCount))
        	{
        		if (StringUtils.isNotEmpty(searchResultsRecordCount))
        		{
        			try {
						vehicleSearchResult.setSearchResultsCount(Integer.valueOf(searchResultsRecordCount));
					} catch (Exception e) {
						log.error("Unable to set search results error count when too many records returned.");
					}
        			
        			vehicleSearchResult.setSearchResultsErrorText("Vehicle search returned too many results");
        		}	
        	}	        	
        	
        	String systemName = XmlUtils.xPathStringSearch(searchResultsMetaata, "srer:SearchRequestError/intel:SystemName");
        	vehicleSearchResult.setSystemName(systemName);

        }	
        
        log.debug("Vehicle Search Result: " + vehicleSearchResult.toString());

        return vehicleSearchResult;
	}

}
