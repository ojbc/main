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
package org.ojbc.web.portal.controllers;

import static org.ojbc.util.helper.UniqueIdUtils.getFederatedQueryId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimeJavaUtilPropertyEditor;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@Profile({"subscriptions", "standalone"})
@RequestMapping("/subscriptions/admin/*")
@SessionAttributes({"subscription", "userLogonInfo", "rapsheetData", "subscriptionSearchRequest"})
public class SubscriptionsAdminController extends SubscriptionsController{
	private Log log = LogFactory.getLog(this.getClass());

	@Value("${defaultPersonSearchSubscriptionTopic:}")
	String defaultPersonSearchSubscriptionTopic;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
    @RequestMapping("landingPage")
    public String admingDefaultSearch(HttpServletRequest request,	        
	        Map<String, Object> model){
		Element samlElement = samlService.getSamlAssertion(request);
		
		SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(true);
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest, false);
		
	    return "subscriptions/admin/_adminLandingPage";
	}
    
	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		log.info("Presenting the search Form");
		if (resetForm) {
			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(true);
			model.put("subscriptionSearchRequest", subscriptionSearchRequest);
		} 

		return "subscriptions/admin/_searchForm";
	}
    
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String advancedSearch(HttpServletRequest request,	
			@ModelAttribute("subscriptionSearchRequest") @Valid SubscriptionSearchRequest subscriptionSearchRequest,
	        BindingResult errors,
	        Map<String, Object> model) {	
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/_searchForm";
		}
					
		Element samlElement = samlService.getSamlAssertion(request);
		
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest, false);
		
		return "subscriptions/admin/_subscriptionResults";
	}
	
	@RequestMapping(value="filter", method = RequestMethod.POST)
	public String filter(@ModelAttribute("subscriptionFilterCommand") SubscriptionFilterCommand subscriptionFilterCommand, 
			BindingResult errors, Map<String, Object> model) {
		
		String subscriptionStatus = subscriptionFilterCommand.getSubscriptionStatus();
		
		logger.info("inside filter() for status: " + subscriptionStatus);
		
		String filterInput;

		//we do not wish to re-filter on filtered results - we always filter on results from a non-filtered search
		if(userSession.getSavedMostRecentSubscriptionSearchResult() == null){
			userSession.setSavedMostRecentSubscriptionSearchResult(userSession.getMostRecentSubscriptionSearchResult());
			filterInput = userSession.getMostRecentSubscriptionSearchResult();
		}else{
			filterInput = userSession.getSavedMostRecentSubscriptionSearchResult();
		}
						
		// filter xml with parameters passed in
		subscriptionFilterCommand.setCurrentDate(new Date());
		
		String sValidationDueWarningDays = subscriptionFilterProperties.get("validationDueWarningDays");
		int iValidationDueWarningDays = Integer.parseInt(sValidationDueWarningDays);
		
		subscriptionFilterCommand.setValidationDueWarningDays(iValidationDueWarningDays);
		
		logger.info("Using subscriptionFilterCommand: " + subscriptionFilterCommand);
		
		logger.info("\n * filterInput = \n" + filterInput);
		
		String sFilteredSubResults = searchResultConverter.filterXml(filterInput, subscriptionFilterCommand);
		
		//saving filtered results allows pagination to function:
		userSession.setMostRecentSearchResult(sFilteredSubResults);	

		logger.info("Filtered Result: \n" + sFilteredSubResults);
				
		//transform the filtered xml results into html		
		Map<String,Object> subResultsHtmlXsltParamMap = getParams(0, null, null);		
		subResultsHtmlXsltParamMap.put("messageIfNoResults", "No " + subscriptionStatus +" subscriptions");
		subResultsHtmlXsltParamMap.put("validateSubscriptionButton", "false");
		
		String htmlResult = "";
		
		if(StringUtils.isNotBlank(sFilteredSubResults)){
			htmlResult = searchResultConverter.convertSubscriptionSearchResult(sFilteredSubResults, subResultsHtmlXsltParamMap);	
		}				
		
		logger.info("Subscriptions Transformed Html:\n" + htmlResult);
				 	
		//put it in the model
		model.put("subscriptionsContent", htmlResult);	
		//empty string(not null) prevents variable being displayed in ui html
		model.put("informationMessages", "");
		
		return "subscriptions/_subscriptionResults";
	}
	
    @RequestMapping(value="clearFilter", method = RequestMethod.POST)
    public String clearFilter( Map<String, Object> model ) {
        
        //reset the mostRecentSearchResult. 
        if (userSession.getSavedMostRecentSubscriptionSearchResult() != null) {
            userSession.setMostRecentSubscriptionSearchResult(userSession.getSavedMostRecentSubscriptionSearchResult()); 
        } 
                
        Map<String,Object> subResultsHtmlXsltParamMap = getParams(0, null, null);       
        
        String htmlResult = searchResultConverter.convertSubscriptionSearchResult(
                userSession.getMostRecentSubscriptionSearchResult(), 
                subResultsHtmlXsltParamMap);
        
        //put it in the model
        model.put("subscriptionsContent", htmlResult);  
        return "subscriptions/_subscriptionResults";
    }
	
				
	@RequestMapping(value = "unsubscribe", method = RequestMethod.GET)
	public String unsubscribe(HttpServletRequest request, @RequestParam String subIdToSubDataJson, 
			Map<String, Object> model) {
					
		Element samlAssertion = samlService.getSamlAssertion(request);	
					
		logger.info("* Unsubscribe using json param: " + subIdToSubDataJson);
		
		JSONObject subIdToSubDataJsonObj = new JSONObject(subIdToSubDataJson);
		
		String[] subIdJsonNames = JSONObject.getNames(subIdToSubDataJsonObj);
		
		// collections for status message
		List<String> successfulUnsubIdlist = new ArrayList<String>();		
		List<String> failedUnsubIdList = new ArrayList<String>();
		
		for(String iId : subIdJsonNames){
								
			JSONObject iSubDataJson = subIdToSubDataJsonObj.getJSONObject(iId);
			
			String iTopic = iSubDataJson.getString("topic");			
			String reasonCode = iSubDataJson.getString("reasonCode");
			
			Unsubscription unsubscription = new Unsubscription(iId, iTopic, reasonCode, null, null, null, null);
			
			try{
				subConfig.getUnsubscriptionBean().unsubscribe(unsubscription, getFederatedQueryId(), samlAssertion);
				
				successfulUnsubIdlist.add(iId);				
				
			}catch(Exception e){
				
				failedUnsubIdList.add(iId);	
				e.printStackTrace();
			}														
		}
		
		String operationStatusResultMsg = getOperationResultStatusMessage(successfulUnsubIdlist, failedUnsubIdList);
								
		refreshSubscriptionsContent(request, model, operationStatusResultMsg);
		
		return "subscriptions/admin/_subscriptionResults";
	}
	
	
	@InitBinder("subscription")
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateTime.class, new DateTimePropertyEditor());
		binder.registerCustomEditor(Date.class, new DateTimeJavaUtilPropertyEditor());
		binder.addValidators(subscriptionValidator);
	}
	
	@InitBinder("subscriptionSearchRequest")
	public void initSubscriptionSearchRequestBinder(WebDataBinder binder) {
		binder.addValidators(subscriptionSearchRequestValidator);
	}
	
	// note system id is used by the broker intermediary to recognize that this is 
	// an edit.  The system id is not set for the add operation
	@RequestMapping(value="updateSubscription", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String updateSubscription(HttpServletRequest request,
			@ModelAttribute("subscription") @Valid Subscription subscription,
			BindingResult errors,
			Map<String, Object> model) throws Exception{					
		
		logger.info("\n* * * * inside updateSubscription() * * * *\n\n: " + subscription + "\n");
		Element samlElement = samlService.getSamlAssertion(request);		
						
		// get potential spring mvc controller validation errors from validating UI values
//		validateSubscriptionUpdate(subscription, errors);		
						
		List<String> errorsList = getValidationBindingErrorsList(errors);
		
		if(errorsList == null || errorsList.isEmpty()){											
			// get potential errors from processing subscribe operation
			
			errorsList = processSubscribeOperation(subscription, samlElement);			
		}
						
		List<String> warningsList = getSubscriptionWarnings(subscription);
		
		String errorsWarningsJson = getErrorsWarningsJson(errorsList, warningsList);
		
		logger.info("\n\n updateSubscription(...) returning errors/warnings json: \n" + errorsWarningsJson);
		
		return errorsWarningsJson;
	}
	
	
}

