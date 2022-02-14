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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscriptionDetail;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.web.excel.ExpiringSubscriptionsExcelBuilder;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.LocalDatePropertyEditor;
import org.ojbc.web.portal.validators.subscriptions.ExpiringSubscriptionRequestValidator;
import org.ojbc.web.portal.validators.subscriptions.RapbackNotificationDateRangeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Element;

@Controller
@Profile({"subscriptions", "standalone"})
@RequestMapping("/subscriptions/admin")
@SessionAttributes({"subscription", "userLogonInfo", "rapsheetData", "subscriptionSearchRequest", 
	"expiringSubscriptionRequest", "agencyMap", "expiringSubscriptions", "expiredSubscriptions", 
	"expiredSubscriptionRequest", "rapbackNotificationDateRange", "subscriptionFilterCommand"})
public class SubscriptionsAdminController extends SubscriptionsController{
	private Log log = LogFactory.getLog(this.getClass());

	@Value("${rapbackNotificationDaysBack: 30}")
	Integer rapbackNotificationDaysBack;

	@Resource
	ExpiringSubscriptionRequestValidator expiringSubscriptionRequestValidator;

	@Resource
	RapbackNotificationDateRangeValidator rapbackNotificationDateRangeValidator;
	
	@Resource
	ExpiringSubscriptionsExcelBuilder expiringSubscriptionsExcelBuilder;
	
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Value("#{propertySplitter.map('${notificationSystemNameMap}', '^')}")
	Map<String, String> notificationSystemNameMap;
	
    @RequestMapping("landingPage")
    public String adminDefaultSearch(HttpServletRequest request,	        
	        Map<String, Object> model){
		Element samlElement = samlService.getSamlAssertion(request);
		
		SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(true);
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
        model.put("subscriptionFilterCommand", new SubscriptionFilterCommand());
		
	    return "subscriptions/admin/adminLandingPage::adminLandingPageContent";
	}
    
	@RequestMapping(value = "adminSearchForm", method = RequestMethod.GET)
	public String adminSearchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		log.info("Presenting the search Form");
		if (resetForm) {
			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(true);
			model.put("subscriptionSearchRequest", subscriptionSearchRequest);
		} 

		return "subscriptions/admin/searchForm::searchFormContent";
	}
    
	@RequestMapping(value = "adminAdvancedsearch", method = RequestMethod.POST)
	public String adminAdvancedSearch(HttpServletRequest request,	
			@ModelAttribute("subscriptionSearchRequest") @Valid SubscriptionSearchRequest subscriptionSearchRequest,
	        BindingResult errors,
	        Map<String, Object> model) {	
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/searchForm::searchFormContent";
		}
					
		Element samlElement = samlService.getSamlAssertion(request);
		
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
		
		return "subscriptions/admin/subscriptionResults::subscriptionResultsContent";
	}
	
	@RequestMapping(value = "expiringSubscriptions", method = RequestMethod.POST)
	public String getExpiringSubscriptions(HttpServletRequest request,	
			@ModelAttribute("expiringSubscriptionRequest") @Valid ExpiringSubscriptionRequest expiringSubscriptionRequest,
			BindingResult errors,
			Map<String, Object> model) {
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/reports/expiringSubscriptionsForm::expiringSubscriptionsFormContent";
		}
					
		finalize(expiringSubscriptionRequest, model);
		List<org.ojbc.util.model.rapback.Subscription> subscriptions = subscriptionsRestClient.getExpiringSubscriptions(expiringSubscriptionRequest);
		log.info("Expiring subscriptions: " + subscriptions );
		
		if (subscriptions.size() == 0){
			errors.reject(null, "No subscriptions found");
			model.put("errors", errors);
			return "subscriptions/admin/reports/expiringSubscriptionsForm::expiringSubscriptionsFormContent";
		}
		
		model.put("expiringSubscriptions", subscriptions);
		return "subscriptions/admin/reports/expiringSubscriptions::expiringSubscriptionsContent";
	}

	@RequestMapping(value = "expiredSubscriptions", method = RequestMethod.POST)
	public String getExpiredSubscriptions(HttpServletRequest request,	
			@ModelAttribute("expiredSubscriptionRequest") @Valid ExpiringSubscriptionRequest expiredSubscriptionRequest,
			BindingResult errors,
			Map<String, Object> model) {
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/reports/expiredSubscriptionsForm::expiredSubscriptionsFormContent";
		}
		
		finalize(expiredSubscriptionRequest, model);
		List<org.ojbc.util.model.rapback.Subscription> subscriptions = subscriptionsRestClient.getExpiredSubscriptions(expiredSubscriptionRequest);
		log.info("Expired subscriptions: " + subscriptions );
		
		if (subscriptions.size() == 0){
			errors.reject(null, "No subscriptions found");
			model.put("errors", errors);
			return "subscriptions/admin/reports/expiredSubscriptionsForm::expiredSubscriptionsFormContent";
		}
		
		model.put("expiredSubscriptions", subscriptions);
		return "subscriptions/admin/reports/expiredSubscriptions::expiredSubscriptionsContent";
	}
	
	@GetMapping(value = "exportExpiringSubscriptions")
	public void exportExpiringSubscriptions(HttpServletRequest request,	
			Map<String, Object> model, HttpServletResponse response) throws IOException {
		XSSFWorkbook workbook = expiringSubscriptionsExcelBuilder.createWorkbook(model);
		String fileName = "ExpiringSubscriptions.xlsx";
		downloadReport(response, workbook, fileName);;
	}
	
	@RequestMapping(value = "exportExpiredSubscriptions")
	public ModelAndView exportExpiredSubscriptions(HttpServletRequest request,	
			Map<String, Object> model) {
		
		return new ModelAndView("excelView", "expiredSubscriptionsExcelView", model); 
	}
	
	private void finalize(
			ExpiringSubscriptionRequest expiringSubscriptionRequest, Map<String, Object> model) {
//		expiringSubscriptionRequest.setSystemName("{http://demostate.gov/SystemNames/1.0}SystemC");
		expiringSubscriptionRequest.setSystemName("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB");
		model.put("expiringSubscriptionRequest", expiringSubscriptionRequest);
		log.info("expiringSubscriptionRequest:" + expiringSubscriptionRequest);
	}
	
	@RequestMapping(value = "expiringSubscriptionsForm", method = RequestMethod.GET)
	public String getExpiringSubscriptionsForm(
			@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		log.info("Presenting the expiringSubscriptionsForm");
		
		if (resetForm) {
			ExpiringSubscriptionRequest expiringSubscriptionRequest = new ExpiringSubscriptionRequest(validationThreshold);
			model.put("expiringSubscriptionRequest", expiringSubscriptionRequest);
		}
		
		return "subscriptions/admin/reports/expiringSubscriptionsForm::expiringSubscriptionsFormContent";
	}
    
	@RequestMapping(value = "expiredSubscriptionsForm", method = RequestMethod.GET)
	public String getExpiredSubscriptionsForm(
			@RequestParam(value = "resetForm", required = false) boolean resetForm,
			Map<String, Object> model) {
		log.info("Presenting the expiredSubscriptionsForm");
		
		if (resetForm) {
			ExpiringSubscriptionRequest expiredSubscriptionRequest = new ExpiringSubscriptionRequest(validationThreshold);
			model.put("expiredSubscriptionRequest", expiredSubscriptionRequest);
		}
		
		return "subscriptions/admin/reports/expiredSubscriptionsForm::expiredSubscriptionsFormContent";
	}
	
	@RequestMapping(value = "notificationsSearchForm", method = RequestMethod.GET)
	public String getNotificationsSearchForm(
			@RequestParam(value = "resetForm", required = false) boolean resetForm,
			Map<String, Object> model) {
		log.info("Presenting the notificationsSearchForm");
		
		if (resetForm) {
			QueryRequestByDateRange rapbackNotificationDateRange = 
					new QueryRequestByDateRange(LocalDate.now().minusDays(rapbackNotificationDaysBack), LocalDate.now());
			model.put("rapbackNotificationDateRange", rapbackNotificationDateRange);
		}
		
		return "subscriptions/admin/notificationsSearchForm::notificationsSearchFormContent";
	}
	
	@RequestMapping(value = "federalRapbackSubscriptionDetail/{subscriptionId}", method = RequestMethod.GET)
	public String getFederalRapbackSubscriptionDetail(
			@PathVariable("subscriptionId") String subscriptionId,
			Map<String, Object> model) {
		log.info("getting FederalRapbackSubscriptionDetail for " + subscriptionId);
		FederalRapbackSubscriptionDetail federalRapbackSubscriptionDetail = 
				subscriptionsRestClient.getFederalRapbackSubscriptionDetail(subscriptionId);
		model.put("federalRapbackSubscriptionDetail", federalRapbackSubscriptionDetail);
		return "subscriptions/admin/edit/federalRapbackSubscriptionDetail::federalRapbackSubscriptionDetailContent";
	}
	
	
    @RequestMapping("federalRapbackSubscriptionErrors")
    public String getFederalRapbackSubscriptionErrors(HttpServletRequest request,	        
	        Map<String, Object> model){
    	List<FederalRapbackSubscription> federalRapbackSubscriptionErrors = subscriptionsRestClient.getFederalRapbackSubscriptionErrors();
		
    	model.put("federalRapbackSubscriptionErrors", federalRapbackSubscriptionErrors);
	    return "subscriptions/admin/federalRapbackSubscriptionErrors::federalRapbackSubscriptionErrorsContent";
	}
    
    @RequestMapping("notifications")
    public String getNotifications(HttpServletRequest request,	        
    		@ModelAttribute("rapbackNotificationDateRange") @Valid QueryRequestByDateRange rapbackNotificationDateRange,
    		BindingResult errors,
    		Map<String, Object> model){
    	
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/admin/notificationsSearchForm::notificationsSearchFormContent";
		}
		
		List<NotificationSent> notifications = subscriptionsRestClient.getNotificationsSent(rapbackNotificationDateRange );
    	model.put("notifications", notifications);
    	return "subscriptions/admin/rapbackNotifications::rapbackNotificationsContent";
    }
    
	@InitBinder("rapbackNotificationDateRange")
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new LocalDatePropertyEditor());
		binder.addValidators(rapbackNotificationDateRangeValidator);
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
    	
    	if (! model.containsAttribute("rapbackNotificationDateRange")){
    		model.addAttribute("rapbackNotificationDateRange", 
    				new QueryRequestByDateRange(LocalDate.now().minusDays(rapbackNotificationDaysBack), LocalDate.now()));
    	}
    	
		List<AgencyProfile> agencies = subscriptionsRestClient.getAllAgencies();
		Map<String, String> agencyMap = new LinkedHashMap<>();
		agencies.forEach(entry -> agencyMap.put(entry.getAgencyOri(),entry.getAgencyName() ));
		model.addAttribute("agencyMap", agencyMap);
		model.addAttribute("notificationSystemNameMap", notificationSystemNameMap); 
		
		model.addAttribute("validationThreshold", validationThreshold);

	}
    
	private void downloadReport(HttpServletResponse response, XSSFWorkbook workbook, String fileName) throws IOException {
		String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		// set content attributes for the response
		response.setContentType(mimeType);
		
//		response.setContentLength();
		
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);
		
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(8192)) { 
            try { 
                workbook.write(baos); 
            } finally { 
                workbook.close(); 
            }
            
            baos.writeTo(outStream);
            baos.close(); 
        } finally{
        	outStream.close();
        }
		log.info("The report is writen to fileName: " + fileName);
	}

}

