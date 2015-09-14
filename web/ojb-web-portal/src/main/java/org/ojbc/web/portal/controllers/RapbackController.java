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
package org.ojbc.web.portal.controllers;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.model.IdentificationResultsCategory;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.portal.controllers.config.RapbackControllerConfigInterface;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@Profile({"rapback-search","initial-results-query","standalone"})
@SessionAttributes({"rapbackSearchResults", "criminalIdentificationSearchResults"})
@RequestMapping("/rapbacks")
public class RapbackController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Resource
	SamlService samlService;
		
	@Resource
	SearchResultConverter searchResultConverter;
	
	@Resource
	RapbackControllerConfigInterface config;
		
		
	@RequestMapping(value = "/rapbackResults", method = RequestMethod.POST)
	public String searchForm(HttpServletRequest request,	        
	        Map<String, Object> model) {		
								
		Element samlElement = samlService.getSamlAssertion(request);
		String searchId = getFederatedQueryId();
		
		String informationMessage = "";
		
		String rawResults = "";
        try {
            rawResults = config.getRapbackSearchBean()
            		.invokeRapbackSearchRequest(IdentificationResultsCategory.Civil, searchId, samlElement);
        } catch (Exception e) {
            informationMessage="Failed to process the request.";
            e.printStackTrace();
        }
												
		logger.debug("Rapback search results raw xml:\n" + rawResults);
		model.put("rapbackSearchResults", rawResults);
		
    	String transformedResults = searchResultConverter.convertRapbackSearchResult(rawResults);
		logger.debug("Rapback Results HTML:\n" + transformedResults);
		
		model.put("searchContent", transformedResults);
		
		model.put("informationMessages", informationMessage);
		
		return "rapbacks/_rapbackResults";
	}

	@RequestMapping(value = "initialResults", method = RequestMethod.GET)
	public String searchDetails(HttpServletRequest request, @RequestParam String transactionNumber,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
		try {
			processDetailRequest(request, transactionNumber, model);
			return "people/_searchDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/_searchDetailsError";
		}
	}
	
	private void processDetailRequest(HttpServletRequest request, String transactionNumber, Map<String, Object> model)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		String searchContent = config.getInitialResultsQueryBean().invokeIdentificationResultsQueryRequest(transactionNumber, samlAssertion);;
		String convertedContent = searchResultConverter.convertIdentificationResultsQueryResult(searchContent,null);
		model.put("searchContent", convertedContent);
	}

	
	@RequestMapping(value = "/criminalIdentificationsResults", method = RequestMethod.POST)
	public String criminalIdentificationResults(HttpServletRequest request,	        
			Map<String, Object> model) {		
		
		Element samlElement = samlService.getSamlAssertion(request);
		String searchId = getFederatedQueryId();
		
		String informationMessage = "";
		
		String rawResults = "";
		try {
			rawResults = config.getRapbackSearchBean()
					.invokeRapbackSearchRequest(IdentificationResultsCategory.Criminal, searchId, samlElement);
		} catch (Exception e) {
			informationMessage="Failed to process the request.";
			e.printStackTrace();
		}
		
		logger.debug("Criminal Identification search results raw xml:\n" + rawResults);
		model.put("criminalIdentificationSearchResults", rawResults);
		
		String transformedResults = searchResultConverter.convertCriminalIdentificationSearchResult(rawResults);
		logger.debug("Criminal Identification Results HTML:\n" + transformedResults);
		
		model.put("searchContent", transformedResults);
		
		model.put("informationMessages", informationMessage);
		
		return "rapbacks/_criminalIdentificationResults";
	}
	
	public static String getFederatedQueryId() {
		return UUID.randomUUID().toString();
	}

}

