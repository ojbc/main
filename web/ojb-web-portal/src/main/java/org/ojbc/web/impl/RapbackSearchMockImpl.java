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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.impl;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.ojbc.web.RapbackSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.IdentificationResultsCategory;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
@Profile({"rapbacks","standalone"})
public class RapbackSearchMockImpl implements RapbackSearchInterface{
		
	private Logger logger = Logger.getLogger(RapbackSearchMockImpl.class.getName());
	
	@Resource
	SearchResultConverter searchResultConverter;		
	
    @Override
    public String invokeRapbackSearchRequest(IdentificationResultsCategory category, Element samlToken)
            throws Exception {
        logger.info("Getting mock rapback search results.");
        
        String searchResult = WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                "/ssp/Organization_Identification_Results_Search_Results_Service"
                + "/artifacts/service_model/information_model/Organization_Identification_Results_Search_Results_IEPD/xml/OrganizationIdentificationResultsSearchResults.xml"));	
        
        return searchResult;
    }

	
}
