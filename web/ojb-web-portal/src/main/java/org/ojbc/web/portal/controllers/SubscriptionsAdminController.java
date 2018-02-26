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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimeJavaUtilPropertyEditor;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.ojbc.web.portal.rest.client.SubscriptionsRestClient;
import org.ojbc.web.portal.validators.subscriptions.ExpiringSubscriptionRequestValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@Profile({"subscriptions", "standalone"})
@RequestMapping("/subscriptions/admin/*")
@SessionAttributes({"subscription", "userLogonInfo", "rapsheetData", "subscriptionSearchRequest"
	, "expiringSubscriptionRequest", "agencyMap", "expiringSubscriptions", "expiredSubscriptions", "expiredSubscriptionRequest"})
public class SubscriptionsAdminController extends SubscriptionsController{
	private Log log = LogFactory.getLog(this.getClass());

	@Resource
	SubscriptionsRestClient subscriptionsRestClient;
	
	@Resource
	ExpiringSubscriptionRequestValidator expiringSubscriptionRequestValidator;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
    @RequestMapping("landingPage")
    public String admingDefaultSearch(HttpServletRequest request,	        
	        Map<String, Object> model){
		Element samlElement = samlService.getSamlAssertion(request);
		
		SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(true);
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
		
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
		
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
		
		return "subscriptions/admin/_subscriptionResults";
	}
	
	@RequestMapping(value = "expiringSubscriptions", method = RequestMethod.POST)
	public String getExpiringSubscriptions(HttpServletRequest request,	
			@ModelAttribute("expiringSubscriptionRequest") @Valid ExpiringSubscriptionRequest expiringSubscriptionRequest,
			BindingResult errors,
			Map<String, Object> model) {
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/reports/_expiringSubscriptionsForm";
		}
					
		finalize(expiringSubscriptionRequest, model);
		List<org.ojbc.util.model.rapback.Subscription> subscriptions = subscriptionsRestClient.getExpiringSubscriptions(expiringSubscriptionRequest);
		log.info("Expiring subscriptions: " + subscriptions );
		
		if (subscriptions.size() == 0){
			errors.reject(null, "No subscriptions found");
			model.put("errors", errors);
			return "subscriptions/admin/reports/_expiringSubscriptionsForm";
		}
		
		model.put("expiringSubscriptions", subscriptions);
		return "subscriptions/admin/reports/_expiringSubscriptions";
	}

	@RequestMapping(value = "expiredSubscriptions", method = RequestMethod.POST)
	public String getExpiredSubscriptions(HttpServletRequest request,	
			@ModelAttribute("expiredSubscriptionRequest") @Valid ExpiringSubscriptionRequest expiredSubscriptionRequest,
			BindingResult errors,
			Map<String, Object> model) {
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/reports/_expiredSubscriptionsForm";
		}
		
		finalize(expiredSubscriptionRequest, model);
		List<org.ojbc.util.model.rapback.Subscription> subscriptions = subscriptionsRestClient.getExpiredSubscriptions(expiredSubscriptionRequest);
		log.info("Expired subscriptions: " + subscriptions );
		
		if (subscriptions.size() == 0){
			errors.reject(null, "No subscriptions found");
			model.put("errors", errors);
			return "subscriptions/admin/reports/_expiredSubscriptionsForm";
		}
		
		model.put("expiredSubscriptions", subscriptions);
		return "subscriptions/admin/reports/_expiredSubscriptions";
	}
	
	@RequestMapping(value = "exportExpiringSubscriptions")
	public String exportExpiringSubscriptions(HttpServletRequest request,	
			Map<String, Object> model) {
		
		return "expiringSubscriptionsExcelView";
	}
	
	@RequestMapping(value = "exportExpiredSubscriptions")
	public String exportExpiredSubscriptions(HttpServletRequest request,	
			Map<String, Object> model) {
		
		return "expiredSubscriptionsExcelView";
	}
	
	private void finalize(
			ExpiringSubscriptionRequest expiringSubscriptionRequest, Map<String, Object> model) {
//		expiringSubscriptionRequest.setSystemName("{http://demostate.gov/SystemNames/1.0}SystemC");
		expiringSubscriptionRequest.setSystemName("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB");
		model.put("expiringSubscriptionRequest", expiringSubscriptionRequest);
		log.info("expiringSubscriptionRequest:" + expiringSubscriptionRequest);
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
	
	@RequestMapping(value = "expiringSubscriptionsForm", method = RequestMethod.GET)
	public String expiringSubscriptionsForm(
			@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		log.info("Presenting the expiringSubscriptionsForm");
		
		if (resetForm) {
			ExpiringSubscriptionRequest expiringSubscriptionRequest = new ExpiringSubscriptionRequest(validationThreshold);
			model.put("expiringSubscriptionRequest", expiringSubscriptionRequest);
		}
		
		return "subscriptions/admin/reports/_expiringSubscriptionsForm";
	}
    
	@RequestMapping(value = "expiredSubscriptionsForm", method = RequestMethod.GET)
	public String expiredSubscriptionsForm(
			@RequestParam(value = "resetForm", required = false) boolean resetForm,
			Map<String, Object> model) {
		log.info("Presenting the expiredSubscriptionsForm");
		
		if (resetForm) {
			ExpiringSubscriptionRequest expiredSubscriptionRequest = new ExpiringSubscriptionRequest(validationThreshold);
			model.put("expiredSubscriptionRequest", expiredSubscriptionRequest);
		}
		
		return "subscriptions/admin/reports/_expiredSubscriptionsForm";
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
	
	@InitBinder("expiringSubscriptionRequest")
	public void initExpiringSubscriptionRequestBinder(WebDataBinder binder) {
		binder.addValidators(expiringSubscriptionRequestValidator);
	}
	
	@InitBinder("expiredSubscriptionRequest")
	public void initExpiredSubscriptionRequestBinder(WebDataBinder binder) {
		binder.addValidators(expiringSubscriptionRequestValidator);
	}
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	if (! model.containsAttribute("expiringSubscriptionRequest")){
    		model.addAttribute("expiringSubscriptionRequest", new ExpiringSubscriptionRequest(validationThreshold));
    	}
		
    	if (! model.containsAttribute("expiredSubscriptionRequest")){
    		model.addAttribute("expiredSubscriptionRequest", new ExpiringSubscriptionRequest(validationThreshold));
    	}
    	
		List<AgencyProfile> agencies = subscriptionsRestClient.getAllAgencies();
		Map<String, String> agencyMap = new LinkedHashMap<>();
		agencies.forEach(entry -> agencyMap.put(entry.getAgencyOri(),entry.getAgencyName() ));
		model.addAttribute("agencyMap", agencyMap);
		
		model.addAttribute("validationThreshold", validationThreshold);
	}
    

}

