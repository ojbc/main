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

import static org.ojbc.util.helper.UniqueIdUtils.getFederatedQueryId;
import static org.ojbc.web.OjbcWebConstants.CIVIL_SUBSCRIPTION_REASON_CODE;
import static org.ojbc.web.OjbcWebConstants.TOPIC_PERSON_ARREST;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.model.IdentificationResultsCategory;
import org.ojbc.web.model.IdentificationResultsQueryResponse;
import org.ojbc.web.model.SimpleServiceResponse;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.ojbc.web.portal.controllers.config.RapbackControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.security.DocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
    @Value("${rapbackSubscriptionPeriod:1}")
    Integer rapbackSubscriptionPeriod;
    
	@RequestMapping(value = "/rapbackResults", method = RequestMethod.POST)
	public String searchForm(HttpServletRequest request,	        
	        Map<String, Object> model) {		
								
		Element samlElement = samlService.getSamlAssertion(request);
		
		String informationMessage = "";
		
		String rawResults = "";
        try {
            rawResults = config.getRapbackSearchBean()
            		.invokeRapbackSearchRequest(IdentificationResultsCategory.Civil, samlElement);
        } catch (Exception e) {
            informationMessage="Failed to get the rapback search result.";
            e.printStackTrace();
        }
												
		logger.debug("Rapback search results raw xml:\n" + rawResults);
		model.put("rapbackSearchResults", rawResults);
		
    	String transformedResults = searchResultConverter.convertRapbackSearchResult(rawResults);
		logger.debug("Rapback Results HTML:\n" + transformedResults);
		
		model.put("rapbackSearchContent", transformedResults);
		
		model.put("informationMessages", informationMessage);
		
		return "rapbacks/_rapbackResults";
	}

	@RequestMapping(value = "initialResults", method = RequestMethod.GET)
	public String initialResults(HttpServletRequest request, @RequestParam String transactionNumber,
	        @ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
		try {
			processDetailRequest(request, transactionNumber, true, model);
			return "rapbacks/_initialResultsDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/_searchDetailsError";
		}
	}
	
	@RequestMapping(value = "subsequentResults", method = RequestMethod.GET)
	public String subsequentResults(HttpServletRequest request, @RequestParam String transactionNumber,
			@ModelAttribute("detailsRequest") DetailsRequest detailsRequest, Map<String, Object> model) {
		try {
			processDetailRequest(request, transactionNumber, false, model);
			return "rapbacks/_subsequentResultsDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/_searchDetailsError";
		}
	}
	
	@RequestMapping(value = "subscribe", method = RequestMethod.GET)
	public @ResponseBody String subscribe(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		try {
			Subscription subscription = buildSubscription(transactionNumber, model);
			FaultableSoapResponse faultableSoapResponse = callSubscribeService(subscription, samlService.getSamlAssertion(request));
			
			if (faultableSoapResponse.isSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", faultableSoapResponse.getException().getMessage());
				return faultableSoapResponse.getException().getMessage() + ", please report the error try again later.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@RequestMapping(value = "archive", method = RequestMethod.GET)
	public @ResponseBody String archive(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		try {
			SimpleServiceResponse simpleServiceResponse = 
					config.getIdentificationResultsModificationBean().handleIdentificationResultsModificationRequest(
							transactionNumber, samlService.getSamlAssertion(request));
			
			if (simpleServiceResponse.getSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", simpleServiceResponse.getErrorMessage());
				return simpleServiceResponse.getErrorMessage() + ", please report the error try again later.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@RequestMapping(value = "unsubscribe", method = RequestMethod.GET)
	public @ResponseBody String unsubscribe(HttpServletRequest request, @RequestParam String subscriptionId,
			Map<String, Object> model) {
		try {
			Unsubscription unsubscription = new Unsubscription(subscriptionId, TOPIC_PERSON_ARREST, CIVIL_SUBSCRIPTION_REASON_CODE, null, null, null, null);
			try{
				subConfig.getUnsubscriptionBean().unsubscribe(unsubscription, getFederatedQueryId(), samlService.getSamlAssertion(request));
				return "success";
			}catch(Exception e){
				e.printStackTrace();
				return (e.getMessage() + ", please report the error and try again later. ");
			}														
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public @ResponseBody String validate(HttpServletRequest request, @RequestParam String subscriptionId,
			Map<String, Object> model) {
		try{
			FaultableSoapResponse faultableSoapResponse = subConfig.getSubscriptionValidationBean().validate(
					subscriptionId, TOPIC_PERSON_ARREST, CIVIL_SUBSCRIPTION_REASON_CODE, 
					getFederatedQueryId(), samlService.getSamlAssertion(request));
			if (faultableSoapResponse.isSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", faultableSoapResponse.getException().getMessage());
				return faultableSoapResponse.getException().getMessage() + ", please report the error try again later.";
			}
		}catch(Exception e){
			e.printStackTrace();
			return (e.getMessage() + ", please report the error and try again later. ");
		}														
	}	
	/**
	 * @return
	 * 	 FaultableSoapResponse web service response
	 */
	private FaultableSoapResponse callSubscribeService(Subscription subscription, Element samlElement) throws Exception{
				
		logger.info("Calling subscribe operation...");
		
		SubscriptionInterface subscribeBean = subConfig.getSubscriptionSubscribeBean();
		
		FaultableSoapResponse faultableSoapResponse = subscribeBean.subscribe(subscription, 
					getFederatedQueryId(), samlElement);
				
		logger.info("Subscribe operation returned faultableSoapResponse:  " + faultableSoapResponse);
		
		return faultableSoapResponse;				
	}
	

	private Subscription buildSubscription(String transactionNumber, Map<String, Object> model) throws Exception {
		String rapbackSearchResults = (String) model.get("rapbackSearchResults");
		Document rapbackSearchResultsDoc = DocumentUtils.getDocumentFromXmlString(rapbackSearchResults); 
		Node organizationIdentificationResultsSearchResult = 
				XmlUtils.xPathNodeSearch(rapbackSearchResultsDoc, "/oirs-res-doc:OrganizationIdentificationResultsSearchResults"
						+ "/oirs-res-ext:OrganizationIdentificationResultsSearchResult[intel:SystemIdentification"
						+ "/nc30:IdentificationID='" + transactionNumber + "']");
		Node identifiedPerson = XmlUtils.xPathNodeSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:IdentifiedPerson");
		Subscription subscription = new Subscription(); 
		subscription.setCaseId(transactionNumber);
		
		DateTime dob = XmlUtils.parseXmlDate(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonBirthDate/nc30:Date"));
		subscription.setDateOfBirth(dob.toDate());
		
		subscription.setFirstName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonGivenName"));
		subscription.setLastName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonSurName"));
		subscription.setFullName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonFullName"));
		
		subscription.setFbiId(XmlUtils.xPathStringSearch(identifiedPerson, "jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID"));
		subscription.setStateId(XmlUtils.xPathStringSearch(identifiedPerson, "jxdm50:PersonAugmentation"
				+ "/jxdm50:PersonStateFingerprintIdentification[oirs-res-ext:FingerpringIdentificationIssuedForCivilPurposeIndicator='true']"
				+ "/nc30:IdentificationID"));
		
		setStartDateAndEndDate(subscription);
		
		setSubscripitonContactEmails(rapbackSearchResultsDoc, subscription);
		
		subscription.setTopic(TOPIC_PERSON_ARREST);
		subscription.setSubscriptionPurpose(CIVIL_SUBSCRIPTION_REASON_CODE);
		
		return subscription;
	}

	private void setSubscripitonContactEmails(Document rapbackSearchResultsDoc, Subscription subscription) throws Exception {
		NodeList emailNodeList = XmlUtils.xPathNodeListSearch(rapbackSearchResultsDoc, 
				"/oirs-res-doc:OrganizationIdentificationResultsSearchResults/nc30:ContactInformationAssociation"
				+ "/nc30:ContactInformation/nc30:ContactEmailID");
		
		if (emailNodeList != null && emailNodeList.getLength() > 0){
			List<String> emailList = new ArrayList<String>();
			for (int i = 0; i < emailNodeList.getLength(); i++) {
	            if (emailNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element emailElement = (Element) emailNodeList.item(i);
	                emailList.add(emailElement.getTextContent());
	            }
	        }
			
			subscription.setEmailList(emailList);
		}
	}

	private void setStartDateAndEndDate(Subscription subscription) {
		Calendar cal = Calendar.getInstance(); 
		subscription.setSubscriptionStartDate(cal.getTime());
		cal.add(Calendar.YEAR, rapbackSubscriptionPeriod);
		subscription.setSubscriptionEndDate(cal.getTime());
	}

	private void processDetailRequest(HttpServletRequest request, String transactionNumber,
			boolean initialResultsQuery, Map<String, Object> model)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		IdentificationResultsQueryResponse identificationResultsQueryResponse = 
				config.getIdentificationResultsQueryBean().invokeIdentificationResultsQueryRequest(
						transactionNumber, initialResultsQuery, samlAssertion);;
		model.put("identificationResultsQueryResponse", identificationResultsQueryResponse);
	}

	
	@RequestMapping(value = "/criminalIdentificationsResults", method = RequestMethod.POST)
	public String criminalIdentificationResults(HttpServletRequest request,	        
			Map<String, Object> model) {		
		
		Element samlElement = samlService.getSamlAssertion(request);
		
		String informationMessage = "";
		
		String rawResults = "";
		try {
			rawResults = config.getRapbackSearchBean()
					.invokeRapbackSearchRequest(IdentificationResultsCategory.Criminal, samlElement);
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
	
}

