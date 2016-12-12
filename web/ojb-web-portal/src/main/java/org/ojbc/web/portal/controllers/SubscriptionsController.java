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

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ojbc.processor.subscription.subscribe.SubscriptionResponseProcessor;
import org.ojbc.processor.subscription.validation.SubscriptionValidationResponseProcessor;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.model.subscription.add.SubscriptionEndDateStrategy;
import org.ojbc.web.model.subscription.add.SubscriptionStartDateStrategy;
import org.ojbc.web.model.subscription.response.SubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidEmailResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidSecurityTokenResponse;
import org.ojbc.web.model.subscription.response.SubscriptionRequestErrorResponse;
import org.ojbc.web.model.subscription.response.UnsubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponseType;
import org.ojbc.web.model.subscription.validation.SubscriptionValidationResponse;
import org.ojbc.web.portal.controllers.config.PeopleControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimeJavaUtilPropertyEditor;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;
import org.ojbc.web.portal.controllers.helpers.SubscriptionQueryResultsProcessor;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.ChCycleSubscriptionValidator;
import org.ojbc.web.portal.validators.IncidentSubscriptionAddValidator;
import org.ojbc.web.portal.validators.IncidentSubscriptionEditValidator;
import org.ojbc.web.portal.validators.subscriptions.ArrestSubscriptionValidatorInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Controller
@Profile({"subscriptions", "standalone"})
@RequestMapping("/subscriptions/*")
@SessionAttributes("subscription")
public class SubscriptionsController {
		
	public static final String ARREST_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/arrest";
	
	public static final String INCIDENT_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/incident";	
	
	public static final String CHCYCLE_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment";
	
	private static DocumentBuilder docBuilder;
	
	private Logger logger = Logger.getLogger(SubscriptionsController.class.getName());
	
	@Value("${validateSubscriptionButton:false}")
	String validateSubscriptionButton;
	
	@Value("${showSubscriptionPurposeDropDown:false}")
	Boolean showSubscriptionPurposeDropDown;
	
	@Value("${showCaseIdInput:false}")
	Boolean showCaseIdInput;
	
	@Value("${fbiIdWarning:false}")
	Boolean fbiIdWarning;
	
	@Resource
	Map<String, SubscriptionStartDateStrategy> subscriptionStartDateStrategyMap;
	
	@Resource
	Map<String, SubscriptionEndDateStrategy> subscriptionEndDateStrategyMap;
	
	@Resource
	Map<String, SubscriptionStartDateStrategy> editSubscriptionStartDateStrategyMap;
	
	@Resource
	Map<String, String> subscriptionDefaultsMap;
	
    @Resource
    Map<String, String> subscriptionFilterProperties;
		
	@Resource
	UserSession userSession;
	
	@Resource
	SamlService samlService;
		
	//TODO see if edit validator needs injection also
	@Value("#{getObject('arrestSubscriptionAddValidator')}")
	ArrestSubscriptionValidatorInterface arrestSubscriptionAddValidator;
	
	@Resource
	ArrestSubscriptionValidatorInterface arrestSubscriptionEditValidator;
	
	@Resource
	IncidentSubscriptionAddValidator incidentSubscriptionAddValidator;
	
	@Resource
	ChCycleSubscriptionValidator chCycleSubscriptionValidator;
	
	@Resource
	IncidentSubscriptionEditValidator incidentSubscriptionEditValidator;
	
	@Resource
	SearchResultConverter searchResultConverter;
	
	@Resource
	Map<String, String> subscriptionTypeValueToLabelMap;
	
	@Resource
	Map<String, String> subscriptionPurposeValueToLabelMap;
		
	@Resource
	PeopleControllerConfigInterface config;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
		
		
    @ModelAttribute("subscriptionFilterProperties")
    public Map<String, String> getSubscriptionFilterProperties(){
    	return subscriptionFilterProperties;
    } 	

	@RequestMapping(value = "subscriptionResults", method = RequestMethod.POST)
	public String searchSubscriptions(HttpServletRequest request,	        
	        Map<String, Object> model) {		
								
		Element samlElement = samlService.getSamlAssertion(request);
		String searchId = getFederatedQueryId();
		
		String rawResults = null; 
		
		String informationMessage = "";
		
		try{
									
			rawResults = subConfig.getSubscriptionSearchBean()
					.invokeSubscriptionSearchRequest(searchId, samlElement);
												
			userSession.setMostRecentSubscriptionSearchResult(rawResults);			
			userSession.setSavedMostRecentSubscriptionSearchResult(null);
		
		}catch(Exception e){
			
			informationMessage = "Failed retrieving subscriptions";
			e.printStackTrace();
		}			
		
		logger.info("Subscription results raw xml:\n" + rawResults);
		
		Map<String,Object> subResultsHtmlXsltParamMap = getParams(0, null, null);
		subResultsHtmlXsltParamMap.put("messageIfNoResults", "You do not have any subscriptions.");
		
		//note empty string required for ui - so "$subscriptionsContent" not displayed
		String transformedResults = ""; 
		
		if(StringUtils.isNotBlank(rawResults)){
		
			transformedResults = searchResultConverter.convertSubscriptionSearchResult(rawResults, subResultsHtmlXsltParamMap);
			
			logger.info("Subscription Results HTML:\n" + transformedResults);
		}
													
		model.put("subscriptionsContent", transformedResults);	
		model.put("informationMessages", informationMessage);
		
		return "subscriptions/_subscriptionResults";
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
	
	/**
	 * Intended to just be used for returning the modal contents to be 
	 * displayed for adding a subscription. Another method calls the service 
	 * to create the subscription
	 */
	@RequestMapping(value="addSubscription", method = RequestMethod.POST)
	public String getAddSubscriptionModal(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
								
		Subscription subscription = new Subscription();
		
		// if there is only one real subscription type option, make it selected
		Map<String, String> subTypeMap =  getTopicValueToLabelMap();
		// size 2 means only drop down label and 1 real value
		if(subTypeMap.size() == 2){			
												
			for(String subType : subTypeMap.keySet()){
				// ignoring empty requires dropdown label "sub type" have a value of empty string
				// so it will be ignored
				if(StringUtils.isNotBlank(subType)){
					subscription.setTopic(subType);
				}
			}						
		}
					
		model.put("subscription", subscription);
			
		logger.info("inside addSubscription()");
		
		return "subscriptions/addSubscriptionDialog/_addSubscriptionModal";
	}

	/**
	 * Gets person original name and alternate(alias) names corresponding to 
	 * the state id(sid) in the provided request object
	 * 
	 *  Two-step process uses search service with sid to get system id
	 *  which is passed into the detail service
	 */
	@RequestMapping(value="personNames", method = RequestMethod.GET)
	public @ResponseBody String getPersonNames(HttpServletRequest request, 
			@ModelAttribute("detailsRequest")DetailsRequest detailsRequest, 
			Map<String, Object> model) {
				
		String rNamesJsonArray = null;
		
		String personSid = detailsRequest.getIdentificationID();
						
		String systemId = null;
		
		if(StringUtils.isNotBlank(personSid)){			
			
			systemId = getSystemIdFromPersonSID(request, detailsRequest);			
		}
									
		if(StringUtils.isNotBlank(systemId)){
			
			logger.info("using systemId: " + systemId);				
			
			Document rapSheetDoc = processDetailQueryCriminalHistory(request, systemId);	
									
			loadChDataFromRapsheet(rapSheetDoc, model);

			// consider making UI retrieve names from subscription pojo, since ui already 
			// retrieves dob and fbi from sub. pojo
			rNamesJsonArray = prepareNamesFromRapSheetParsing(rapSheetDoc, model);
		}									
						
		return rNamesJsonArray;
	}
	
	
	private Date parseRapsheetDate(String rapSheetDate){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dobDate = null;
		try {
			dobDate = sdf.parse(rapSheetDate);
			
		} catch (ParseException e) {
			logger.severe("Couldn't parse date: " + rapSheetDate);
			e.printStackTrace();
		}		
		return dobDate;
	}
	
	
	private void loadChDataFromRapsheet(Document rapSheetDoc, Map<String, Object> model){
		
		Subscription subscription = (Subscription)model.get("subscription");
		
		logger.info("\n\n\n * * * * \n Subscription before loading CH data * * * * \n " + subscription + "\n\n\n");
		
		String dobString = getDOBFromRapsheet(rapSheetDoc);
				
		Date dobDate = parseRapsheetDate(dobString);
		subscription.setDateOfBirth(dobDate);
				
		String fbiId = getFbiIdFromRapsheet(rapSheetDoc);		
		subscription.setFbiId(fbiId);
		
		logger.info("\n\n\n * * * * * \n\n Populated Subscription from Rapsheet \n " + subscription + "* * * * * \n");
				
		// see if this is needed, because we already modified the object which is pass-by-reference
		model.put("subscription", subscription);		
	}
	
	
	private String prepareNamesFromRapSheetParsing(Document rapSheetDoc, Map<String, Object> model){
				
		String rNamesJsonArray = null; 
		
		SubscribedPersonNames subscribedPersonNames = null;
		
		if(rapSheetDoc != null){
			try {
				subscribedPersonNames = getAllPersonNamesFromRapsheet(rapSheetDoc);
				
			} catch (Exception e) {
				logger.severe("Exception getting names from rapsheet \n" + e);
			}				
		}				
		
		if(subscribedPersonNames != null){
			
			List<String> allNamesList = new ArrayList<String>();
			
			allNamesList.add(subscribedPersonNames.getOriginalName());			
			
			allNamesList.addAll(subscribedPersonNames.getAlternateNamesList());
			
			JSONArray namesJsonArray = new JSONArray(allNamesList);
			
			rNamesJsonArray = namesJsonArray == null ? null : namesJsonArray.toString();
			
			model.put("originalName", subscribedPersonNames.getOriginalName());
			
			logger.info("returning all names: \n " + rNamesJsonArray + ", with original name: " + subscribedPersonNames.getOriginalName());									
		}
				
		return rNamesJsonArray;		
	}
	
	

	@RequestMapping(value="arrestForm", method=RequestMethod.POST)
	public String getArrestForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		logger.info("inside arrestForm()");		
				
		Subscription subscription = new Subscription();
		
		initDatesForAddArrestForm(subscription, model);
				
		// pre-populate an email field on the form w/email from saml token
		String sEmail = userSession.getUserLogonInfo().emailAddress;
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
		
		String purposeSelection = subscriptionDefaultsMap.get("purpose");
		if(StringUtils.isNotEmpty(purposeSelection)){
			subscription.setSubscriptionPurpose(purposeSelection);	
		}		
							
		model.put("subscription", subscription);
		
		model.put("showSubscriptionPurposeDropDown", showSubscriptionPurposeDropDown);
		
		model.put("showCaseIdInput", showCaseIdInput);
		 				
		return "subscriptions/addSubscriptionDialog/_arrestForm";
	}
	
	
	/**
	 * note: uses pass-by-reference to modify subscription parameter
	 * 
	 * pre-populate the subscription start date as a convenience to the user
	 * this will be displayed on the modal
	 */
	private void initDatesForAddArrestForm(Subscription subscription, Map<String, Object> model){
				
		SubscriptionStartDateStrategy startDateStrategy = subscriptionStartDateStrategyMap.get(ARREST_TOPIC_SUB_TYPE);		
		Date defaultSubStartDate = startDateStrategy.getDefaultValue();
		
		boolean isStartDateEditable = startDateStrategy.isEditable();
				
		subscription.setSubscriptionStartDate(defaultSubStartDate);
		
		model.put("isStartDateEditable", isStartDateEditable);
		
		
		
		SubscriptionEndDateStrategy endDateStrategy = subscriptionEndDateStrategyMap.get(ARREST_TOPIC_SUB_TYPE);
		
		Date defaultSubEndDate = endDateStrategy.getDefaultValue();
		
		boolean isEndDateEditable = endDateStrategy.isEditable();
		
		subscription.setSubscriptionEndDate(defaultSubEndDate);
		
		model.put("isEndDateEditable", isEndDateEditable);		
	}
	
	
	private void initDatesForEditArrestForm(Map<String, Object> model){
		
		SubscriptionStartDateStrategy arrestEditSubStartDateStrategy = editSubscriptionStartDateStrategyMap.get(ARREST_TOPIC_SUB_TYPE);
		
		boolean isStartDateEditable = arrestEditSubStartDateStrategy.isEditable();
		
		model.put("isStartDateEditable", isStartDateEditable);
	}
	
	
	private void initDatesForAddIncidentForm(Subscription subscription, Map<String, Object> model){
		
		// START date
		SubscriptionStartDateStrategy startDateStrategy = subscriptionStartDateStrategyMap.get(INCIDENT_TOPIC_SUB_TYPE);		
		Date defaultStartDate = startDateStrategy.getDefaultValue();
		
		boolean isStartDateEditable = startDateStrategy.isEditable();
		
		subscription.setSubscriptionStartDate(defaultStartDate);
				
		model.put("isStartDateEditable", isStartDateEditable);		
		
		
		//END date		
		SubscriptionEndDateStrategy endDateStrategy = subscriptionEndDateStrategyMap.get(INCIDENT_TOPIC_SUB_TYPE);
		Date defaultEndDate = endDateStrategy.getDefaultValue();
		
		boolean isEndDateEditable = endDateStrategy.isEditable();
		
		subscription.setSubscriptionEndDate(defaultEndDate);
	
		model.put("isEndDateEditable", isEndDateEditable);				
	}

	private void initDatesForEditIncidentForm(Map<String, Object> model){
		
		SubscriptionStartDateStrategy editIncidentSubStartDateStrategy = editSubscriptionStartDateStrategyMap.get(INCIDENT_TOPIC_SUB_TYPE);
		
		boolean isStartDateEditable = editIncidentSubStartDateStrategy.isEditable();
		
		model.put("isStartDateEditable", isStartDateEditable);		
	}
	
	

	private void initDatesForAddChCycleForm(Subscription subscription, Map<String, Object> model){
		
		// START date
		SubscriptionStartDateStrategy startDateStrategy = subscriptionStartDateStrategyMap.get(CHCYCLE_TOPIC_SUB_TYPE);		
		Date defaultStartDate = startDateStrategy.getDefaultValue();
		
		boolean isStartDateEditable = startDateStrategy.isEditable();
		
		subscription.setSubscriptionStartDate(defaultStartDate);
				
		model.put("isStartDateEditable", isStartDateEditable);		
		
		
		//END date		
		SubscriptionEndDateStrategy endDateStrategy = subscriptionEndDateStrategyMap.get(CHCYCLE_TOPIC_SUB_TYPE);
		Date defaultEndDate = endDateStrategy.getDefaultValue();
		
		boolean isEndDateEditable = endDateStrategy.isEditable();
		
		subscription.setSubscriptionEndDate(defaultEndDate);
	
		model.put("isEndDateEditable", isEndDateEditable);				
	}
	
	private void initDatesForEditChCycleForm(Map<String, Object> model){
		
		SubscriptionStartDateStrategy editIncidentSubStartDateStrategy = editSubscriptionStartDateStrategyMap.get(CHCYCLE_TOPIC_SUB_TYPE);
		
		boolean isStartDateEditable = editIncidentSubStartDateStrategy.isEditable();
		
		model.put("isStartDateEditable", isStartDateEditable);		
	}

	@RequestMapping(value="incidentForm", method=RequestMethod.POST)
	public String getIncidentForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		logger.info("inside incidentForm()");
		
		Subscription subscription = new Subscription();
				
		initDatesForAddIncidentForm(subscription, model);
		
		String sEmail = userSession.getUserLogonInfo().emailAddress;
		
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
				
		model.put("subscription", subscription);
				
		return "subscriptions/addSubscriptionDialog/_incidentForm";
	}

	@RequestMapping(value="chCycleForm", method=RequestMethod.POST)
	public String getChCycleForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		logger.info("inside getChCycleForm()");
		
		Subscription subscription = new Subscription();
				
		initDatesForAddChCycleForm(subscription, model);
		
		String sEmail = userSession.getUserLogonInfo().emailAddress;
		
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
				
		model.put("subscription", subscription);
				
		return "subscriptions/addSubscriptionDialog/_chCycleForm";
	}		
	
	private void validateSubscription(Subscription subscription, BindingResult errors){
				
		logger.info("subscription: \n" + subscription);
		
		if(ARREST_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			arrestSubscriptionAddValidator.validate(subscription, errors);
			
		}else if(INCIDENT_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			incidentSubscriptionAddValidator.validate(subscription, errors);
			
		}else if(CHCYCLE_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			chCycleSubscriptionValidator.validate(subscription, errors);
		}
	}
	
	
	/**
	 * @return
	 * 		json array string of errors if any.  These can be used by the UI to display to the user
	 */
	@RequestMapping(value="saveSubscription", method=RequestMethod.GET)
	public  @ResponseBody String  saveSubscription(HttpServletRequest request,
			@ModelAttribute("subscription") Subscription subscription,
			BindingResult errors) {
								
		logger.info("\n\n\n * * * * inside saveSubscription() * * * * *\n\n: " + subscription + "\n\n\n");
		
		Element samlElement = samlService.getSamlAssertion(request);
										
		validateSubscription(subscription, errors);										
		
		// retrieve any spring mvc validation errors from the controller
		List<String> errorsList = getValidationBindingErrorsList(errors);
						
		// if no spring mvc validation errors were found, call the subscribe operation and see if there are errors 
		// from the notification broker response
		if(errorsList == null || errorsList.isEmpty()){		
			
			try {
				errorsList = processSubscribeOperation(subscription, samlElement);										
				
			} catch (Exception e) {

				errorsList = Arrays.asList("An error occurred while processing subscription");				
								
				logger.severe("Failed processing subscription: " + e);
			}									
		}					
		
		List<String> subWarningsList = getSubscriptionWarnings(subscription);
		
		String errorMsgsWarnMsgsJson = getErrorsWarningsJson(errorsList, subWarningsList);
		
		logger.info("\n\n Returning errors/warnings json:\n\n" + errorMsgsWarnMsgsJson);
		
		return errorMsgsWarnMsgsJson;
	}		 
	
	List<String> getSubscriptionWarnings(Subscription subscription){
		
		List<String> warningList = new ArrayList<String>();
			
		if(ARREST_TOPIC_SUB_TYPE.equals(subscription.getTopic())){			
			
			if (fbiIdWarning){
			
				if(StringUtils.isEmpty(subscription.getFbiId())){
					warningList.add("FBI ID missing. Subscription with the FBI is pending.");
				}				
			}
		}			
		
		return warningList;
	}
	
	String getErrorsWarningsJson(List<String> errorsList, List<String> warningsList){
		
		JSONObject errorsWarningsArraysJson = new JSONObject();
		
		for(String error : errorsList){
			errorsWarningsArraysJson.append("errors", error);
		}
		
		for(String warning : warningsList){
			errorsWarningsArraysJson.append("warnings", warning);
		}
		
		String sErrWarnJson = errorsWarningsArraysJson.toString();
		
		return sErrWarnJson;
	}
	
	/**
	 * @return
	 * 	 json errors array (if any)
	 * 
	 * @throws Exception 
	 * 		if no response received from subscribe operation
	 */
	private List<String> processSubscribeOperation(Subscription subscription, Element samlElement) throws Exception{
				
		if(subscription == null){
			throw new Exception("subscription was null");
		}
				
		logger.info("Calling subscribe operation...");
		
		SubscriptionInterface subscribeBean = subConfig.getSubscriptionSubscribeBean();
		
			FaultableSoapResponse faultableSoapResponse = subscribeBean.subscribe(subscription, 
					getFederatedQueryId(), samlElement);
				
		logger.info("Subscribe operation returned faultableSoapResponse:  " + faultableSoapResponse);
		
		List<String> subRespErrorsList = null;
		
		if(faultableSoapResponse != null){
			
			subRespErrorsList = getErrorListFromSubscriptionResponse(faultableSoapResponse);		
			
		}else{
			throw new Exception("FaultableSoapResponse was null(got no response from subscribe operation), which is required");
		}
				
		return subRespErrorsList;				
	}
	
	
	/**
	 * note default visibility so it can be unit-tested
	 */
	List<String> getErrorListFromSubscriptionResponse(FaultableSoapResponse faultableSoapResponse) throws Exception{
				
		if(faultableSoapResponse == null){
			throw new Exception("FaultableSoapResponse was null");
		}		
				
		List<String> errorsList = null;
		
		Document subResponseDoc = getSubscriptionResponseDoc(faultableSoapResponse);
						
		if(subResponseDoc != null){
			
			errorsList = getErrorsFromSubscriptionResponse(subResponseDoc);	
			
		}else{
			
			errorsList = Arrays.asList("Did not receive subscription confirmation");			
		}	
		
		return errorsList;
	}
	
	
	private Document getSubscriptionResponseDoc(FaultableSoapResponse faultableSoapResponse) throws Exception{
		
	  if(faultableSoapResponse == null){
		  throw new Exception("Cannot get Document from null " + FaultableSoapResponse.class.getName());
	  }
		
	  String sSoapEnvelope = faultableSoapResponse.getSoapResponse();
	  
	  if(StringUtils.isBlank(sSoapEnvelope)){
		  throw new Exception("soap envelope was blank in the FaultableSoapResponse");
	  }
	  				
	  Document soapEnvDoc = getDocBuilder().parse(new InputSource(new StringReader(sSoapEnvelope)));
      
	  if(soapEnvDoc == null){
		  throw new Exception("soapEnvDoc Document could not be parsed");
	  }
	  	  
      Document subResponseDoc = getSubscriptionResponseDocFromSoapEnvDoc(soapEnvDoc);   
      
      if(subResponseDoc == null){
    	  throw new Exception("Could not get subscription response document from soap envelope document");
      }
            		
      return subResponseDoc;
	}
	
	
	private Document getSubscriptionResponseDocFromSoapEnvDoc(Document soapEnvDoc) throws Exception{
		
		if(soapEnvDoc == null){
			throw new Exception("Cannot get subscription response Document from null soap envelope document");			
		}
		
		Document rDocument = null;
											
		Node rootSubRespNode = null;
		
		if(SubscriptionResponseProcessor.isSubscriptionSuccessResponse(soapEnvDoc)){
			
			rootSubRespNode = XmlUtils.xPathNodeSearch(soapEnvDoc, "//b-2:SubscribeResponse");
			
		}else if(SubscriptionResponseProcessor.isUnsubscriptionAccessDenialResponse(soapEnvDoc)){
			
			rootSubRespNode = XmlUtils.xPathNodeSearch(soapEnvDoc, "//b-2:UnableToDestroySubscriptionFault");
						
		}else if(SubscriptionResponseProcessor.isSubscriptionFaultResponse(soapEnvDoc)){
			
			rootSubRespNode = XmlUtils.xPathNodeSearch(soapEnvDoc, "//b-2:SubscribeCreationFailedFault");
			
		}else{
			throw new Exception("Unknown Subscription Type");
		}
		
		if(rootSubRespNode != null){
			
			rDocument = getDocBuilder().newDocument();
						
			Node importedNode = rDocument.importNode(rootSubRespNode, true);
			
			rDocument.appendChild(importedNode);
												
		}else{
			throw new Exception("Could not recognize subscription response");
		}
		
		return rDocument;
	}
	
		 
	private List<String> getErrorsFromSubscriptionResponse(Document subResponseDoc) throws Exception{
												
		if(subResponseDoc == null){
			throw new Exception("subResponseDoc was null");
		}
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(subResponseDoc);
		
		if(subResponseList == null){
			throw new Exception("Could not parse responses from response xml document");
		}
		
		List<String> responseErrorList = new ArrayList<String>();
		
		for(SubscriptionResponse iSubResponse : subResponseList){
			
			SubscriptionResponseType iResponseType = iSubResponse.getResponseType();
			
			if(SubscriptionResponseType.SUBSCRIPTION_SUCCESS != iResponseType){
				
				String sError = getErrorFromResponse(iSubResponse);
				
				if(StringUtils.isEmpty(sError)){
					throw new Exception("Couldn't get error from invalid response");
				}
				
				responseErrorList.add(sError);
				
				logger.info("\n Parsed/received error: " + sError);				
			}
		}
						
		return responseErrorList;
	}
	
	private String getErrorFromResponse(SubscriptionResponse subResponse) throws Exception{
		
		if(subResponse == null){
			throw new Exception("Can't get error from null subscription response");
		}		
		
		String rErrorMsg = null;
		
		SubscriptionResponseType responseType = subResponse.getResponseType();
		
		if(SubscriptionResponseType.SUBSCRIPTION_ACCESS_DENIAL == responseType){
			
			SubscriptionAccessDenialResponse subAccessDenyResp = (SubscriptionAccessDenialResponse)subResponse;

			rErrorMsg = "Access Denied: \n";
			rErrorMsg += subAccessDenyResp.getAccessDenyingReason() +"\n";
			rErrorMsg += subAccessDenyResp.getAccessDenyingSystemName();	
			
			
		}else if(SubscriptionResponseType.SUBSCRIPTION_INVALID_EMAIL == responseType){
			
			SubscriptionInvalidEmailResponse subInvalidEmailResp = (SubscriptionInvalidEmailResponse)subResponse;
			
			List<String> invalidEmailList = subInvalidEmailResp.getInvalidEmailList();
			
			if(invalidEmailList == null || invalidEmailList.isEmpty()){
				logger.warning("Determined Invalid email, but received no email address values");
			}
			
			rErrorMsg = "Invalid Email(s): ";
			
			for(String iInvalidEmail : invalidEmailList){
				rErrorMsg += iInvalidEmail + " ";
			}
			
			rErrorMsg = rErrorMsg.trim();
			
		}else if(SubscriptionResponseType.SUBSCRIPTION_INVALID_TOKEN == responseType){
			
			SubscriptionInvalidSecurityTokenResponse invalidTknResp = (SubscriptionInvalidSecurityTokenResponse)subResponse;
			
			rErrorMsg = "Invalid Security Token: \n";
			rErrorMsg += invalidTknResp.getInvalidSecurityTokenDescription();
			
		}else if(SubscriptionResponseType.SUBSCRIPTION_REQUEST_ERROR == responseType){
			
			SubscriptionRequestErrorResponse invalidReqResp = (SubscriptionRequestErrorResponse)subResponse;
			
			rErrorMsg = "Invalid Request: \n";
			rErrorMsg += invalidReqResp.getRequestErrorSystemName() + "\n";
			rErrorMsg += invalidReqResp.getRequestErrorTxt();
			
		}else if(SubscriptionResponseType.UNSUBSCRIPTION_ACCESS_DENIAL == responseType){
			
			UnsubscriptionAccessDenialResponse unsubAcsDenialResp = (UnsubscriptionAccessDenialResponse)subResponse;
			
			rErrorMsg = "Access denied for unsubscription: \n";
			rErrorMsg += unsubAcsDenialResp.getAccessDenyingSystemName() + "\n";
			rErrorMsg += unsubAcsDenialResp.getAccessDenyingReason();
			
		}else if(SubscriptionResponseType.SUBSCRIPTION_SUCCESS == responseType){
		
			rErrorMsg="";
			logger.warning("Attempt was made to get subscription response error type for a scenario where the "
					+ "subscription response was actually 'success'");
		
		}else{
			
			throw new Exception("Subscription response type unrecognized");
		}
		
		return rErrorMsg;
	}
	

	/**
	 * Note: catches exceptions and sets model boolean to false to allow UI layer to display
	 * an error.  
	 * 
	 * TODO maybe use spring binding to store the error instead of putting it in the regular model map
	 * 
	 * @param identificationID
	 * 		subscription id
	 * @param topic
	 * 		used to display appropriate form on the edit modal view
	 */
	@RequestMapping(value="editSubscription", method = RequestMethod.GET)
	public String getSubscriptionEditModal(HttpServletRequest request,			
			@RequestParam String identificationID,
			@RequestParam String topic,
			Map<String, Object> model) {
		
		try{			
			//init success flag to true - allow processing below to set it to false if things go wrong
			model.put("initializationSucceeded", true);
						
			Document subQueryResponseDoc = runSubscriptionQueryForEditModal(identificationID, request);
			
			Subscription subscription = parseSubscriptionQueryResults(subQueryResponseDoc);				
						
			List<String> allNamesList = null;	
			
			if(ARREST_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				 ChRapsheetData chRapsheetData = lookupChRapbackDataForArrestEdit(request, subscription, model);
				
				 allNamesList = chRapsheetData.formattedAlternateNamesList;				 
				 subscription.setFbiId(chRapsheetData.fbiNumber);

				 Date rapSheetDob = parseRapsheetDate(chRapsheetData.personDob);				 
				 subscription.setDateOfBirth(rapSheetDob);
				
				 initDatesForEditArrestForm(model);
				 
				 model.put("showSubscriptionPurposeDropDown", showSubscriptionPurposeDropDown);
				
				 model.put("showCaseIdInput", showCaseIdInput);
				
			}else if(INCIDENT_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditIncidentForm(model);
			
			}else if(CHCYCLE_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditChCycleForm(model);
			}
											
			if(allNamesList != null && !allNamesList.isEmpty()){
				JSONArray namesJsonArray = new JSONArray(allNamesList);		
				subscription.setPersonNamesJsonArray(namesJsonArray.toString());			
			}						
			
			logger.info("Subscription Edit Request: " + subscription);
									
			model.put("subscription", subscription);	
											
		}catch(Exception e){
			
			model.put("initializationSucceeded", false);			
			logger.info("initialization FAILED for identificationID=" + identificationID + ":\n" + e.toString());
		}
		
		return "subscriptions/editSubscriptionDialog/_editSubscriptionModal";
	}
	
	
	private class ChRapsheetData{
		
		List<String> formattedAlternateNamesList;
		
		SubscribedPersonNames subscribedPersonNames;
		
		String fbiNumber;
				
		String personDob;
	}
	
	private ChRapsheetData lookupChRapbackDataForArrestEdit(HttpServletRequest request, Subscription subscription, 
			Map<String, Object> model) throws Exception{
		
		List<String> allNamesList = new ArrayList<String>();
		
		ChRapsheetData chRapsheetData = getChRapbackData(request, subscription);
		
		SubscribedPersonNames subscribedNames = chRapsheetData.subscribedPersonNames;
		
		String originalName = subscribedNames == null ? null : subscribedNames.getOriginalName();
										
		if(StringUtils.isNotBlank(originalName)){
			allNamesList.add(originalName);
		}
										
		List<String> alternateNameList = subscribedNames == null ? null : subscribedNames.getAlternateNamesList();
		
		if(alternateNameList != null && !alternateNameList.isEmpty()){
			allNamesList.addAll(subscribedNames.getAlternateNamesList());
		}								
										
		if(allNamesList == null || allNamesList.isEmpty()){
			model.put("initializationSucceeded", false);
			logger.severe("Failed to lookup names for arrest subscription");
		}else{
			model.put("originalName", subscribedNames.getOriginalName());
		}
				
		chRapsheetData.formattedAlternateNamesList = allNamesList;
		
		
		return chRapsheetData;
	}
	
	
	
	private Subscription parseSubscriptionQueryResults(
			Document subQueryResponseDoc) throws Exception {

		SubscriptionQueryResultsProcessor subQueryResultProcessor = new SubscriptionQueryResultsProcessor();

		Subscription subscription = subQueryResultProcessor
				.parseSubscriptionQueryResults(subQueryResponseDoc);

		logger.info("Subscription Query Results: \n" + subscription.toString());

		return subscription;
	}
	
	
	private Document runSubscriptionQueryForEditModal(String identificationID,
			HttpServletRequest request) throws Exception {

		Document subQueryResponseDoc = null;

		Element samlAssertion = samlService.getSamlAssertion(request);

		DetailsRequest subscriptionQueryRequest = new DetailsRequest();
		subscriptionQueryRequest.setIdentificationID(identificationID);

		String subQueryResponse = null;

		subQueryResponse = subConfig.getSubscriptionQueryBean().invokeRequest(
				subscriptionQueryRequest, getFederatedQueryId(), samlAssertion);

		subQueryResponseDoc = getDocBuilder().parse(new InputSource(new StringReader(subQueryResponse)));

		logger.info("subQueryResponseDoc: \n");
		XmlUtils.printNode(subQueryResponseDoc);

		return subQueryResponseDoc;
	}
	


	/**
	 * @param idToTopicJsonProps
	 * 		a json formatted string that's an object of name-value pairs where the 
	 * 		id is the name(key) and the topic is the value.  ex:
	 * 		 {"62723":"{http://ojbc.org/wsn/topics}:person/arrest","62724":"{http://ojbc.org/wsn/topics}:person/arrest"}
	 * 
	 * @return
	 * 		the subscription results page(refreshed after validate)
	 */
	@RequestMapping(value="validate", method = RequestMethod.POST)
	public String  validate(HttpServletRequest request, 
			@RequestParam String subIdToSubDataJson, 
			Map<String, Object> model) {
		
		logger.info("Received subIdToSubDataJson: " + subIdToSubDataJson);
		
		Element samlAssertion = samlService.getSamlAssertion(request);
						
		processValidateSubscription(request, subIdToSubDataJson, model, samlAssertion);
			
		return "subscriptions/_subscriptionResults";
	}
	
	

	private void processValidateSubscription(HttpServletRequest request, String subIdToSubDataJson, 
			Map<String, Object> model, Element samlAssertion) {		
		
		JSONObject subIdToSubDataJsonObjMap = new JSONObject(subIdToSubDataJson);
		
		String[] idJsonNames = JSONObject.getNames(subIdToSubDataJsonObjMap);
		
		// used to generated status message
		List<String> validatedIdList = new ArrayList<String>();		
		List<String> failedIdList = new ArrayList<String>();
		
		// call the validate operation for each id/topic parameter
		for(String iSubId : idJsonNames){
			
			JSONObject subIdToSubDataJsonObj = subIdToSubDataJsonObjMap.getJSONObject(iSubId);
			
			String iTopic = subIdToSubDataJsonObj.getString("topic");
			String reasonCode = subIdToSubDataJsonObj.getString("");
						
			try{
				FaultableSoapResponse faultableSoapResponse = subConfig.getSubscriptionValidationBean().validate(
						iSubId, iTopic, reasonCode, getFederatedQueryId(), samlAssertion);
				
				//TODO see if we should check faultableSoapResponse exception attribute(if there is one)
				if(faultableSoapResponse == null){
					
					failedIdList.add(iSubId);
					logger.severe("FAILED! to validate id: " + iSubId);
					continue;
				}
				
				boolean isValidated = getValidIndicatorFromValidateResponse(faultableSoapResponse);
				
				logger.info("isValidated: " + isValidated + " - for id: " + iSubId);

				if(isValidated){
					validatedIdList.add(iSubId);	
				}else{
					failedIdList.add(iSubId);
				}														
				
			}catch(Exception e){				
				failedIdList.add(iSubId);
				logger.severe("FAILED! to validate id: " + iSubId + ", " + e);
			}														
		}
				
		String operationResultMessage = getOperationResultStatusMessage(validatedIdList, failedIdList);				
		
		refreshSubscriptionsContent(request, model, operationResultMessage);						
	}
	
				
	boolean getValidIndicatorFromValidateResponse(FaultableSoapResponse faultableSoapResponse) throws Exception{

		boolean isValidated = false;
		
		if(faultableSoapResponse == null){
			throw new Exception("FaultableSoapResponse param was null");
		}		
				
		Document soapBodyDoc = faultableSoapResponse.getSoapBodyDoc();
		
		Document validateResponseDoc = null;  		
		
		Node rootValidRespNodeFromSoapEnvDoc = XmlUtils.xPathNodeSearch(soapBodyDoc, "//b-2:ValidateResponse");
		
		if(rootValidRespNodeFromSoapEnvDoc != null){
			
			validateResponseDoc = getDocBuilder().newDocument();
						
			Node importedValidRespNodeFromSoapEnvDoc = validateResponseDoc.importNode(rootValidRespNodeFromSoapEnvDoc, true);
			
			validateResponseDoc.appendChild(importedValidRespNodeFromSoapEnvDoc);
												
		}else{
			throw new Exception("Could not recognize validate response message root node");
		}
						
		SubscriptionValidationResponseProcessor validateResponseProcessor = new SubscriptionValidationResponseProcessor();
		
		SubscriptionValidationResponse subValidResponse = validateResponseProcessor.processResponse(validateResponseDoc);
		
		isValidated = subValidResponse.isSubscriptionValidated();			
				
		return isValidated;		
	}


	private String getOperationResultStatusMessage(List<String> succeededIdList, List<String> failedIdList){
				
		String resultMessage = null;
		
		boolean hasSuccessfulIds = !succeededIdList.isEmpty();
		boolean hasFailedIds = !failedIdList.isEmpty();
		
		@SuppressWarnings("unused")
        String sSuccessIds = null;
		String sFailedIds = null;		
		
		if(hasSuccessfulIds){
			
			String[] aSuccessIds = succeededIdList.toArray(new String[]{});			
			
			sSuccessIds = Arrays.toString(aSuccessIds);						
		}
		
		if(hasFailedIds){
			
			String[] aFailedIds = failedIdList.toArray(new String[]{});
			
			sFailedIds = Arrays.toString(aFailedIds);									
		}
				
		if(hasFailedIds){			
			resultMessage = "Ids Failed: " + sFailedIds;			
		}else{			
			resultMessage = "Operation Successful";			
		}
		
		return resultMessage;		
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
		
		return "subscriptions/_subscriptionResults";
	}
	
	
	private void refreshSubscriptionsContent(HttpServletRequest request, Map<String, Object> model, String informationMessage) {
		
		Element samlElement = samlService.getSamlAssertion(request);
		
		String searchId = getFederatedQueryId();
		
		String rawResults = null;
		
		try{
						
			rawResults = subConfig.getSubscriptionSearchBean().invokeSubscriptionSearchRequest(searchId, samlElement);
						
		}catch(Exception e){
			
			e.printStackTrace();
			
			logger.severe("Failed retrieving subscriptions, ignoring informationMessage param: " + informationMessage );			
			
			informationMessage = "Failed retrieving subscriptions";
		}
								
		Map<String,Object> converterParamsMap = getParams(0, null, null);

		//note must default to empty string instead of null for ui to display nothing when desired instead
		// of having ui display "$subscriptionsContent"
		String transformedResults = ""; 
				
		if(StringUtils.isNotBlank(rawResults)){
			
			transformedResults = searchResultConverter.convertSubscriptionSearchResult(rawResults, converterParamsMap);			
		}
			
		model.put("subscriptionsContent", transformedResults);
		model.put("informationMessages", informationMessage);		
	}
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DateTime.class, new DateTimePropertyEditor());
		binder.registerCustomEditor(Date.class, new DateTimeJavaUtilPropertyEditor());
	}
	
	@ModelAttribute("subscriptionTypeValueToLabelMap")
	public Map<String, String> getTopicValueToLabelMap() {
		return subscriptionTypeValueToLabelMap;
	}
	
	
	@ModelAttribute("subscriptionPurposeValueToLabelMap")
	public Map<String, String> getSubscriptionPurposeValueToLabelMap() {
		return subscriptionPurposeValueToLabelMap;
	}

	private ChRapsheetData getChRapbackData(HttpServletRequest request, Subscription subscription) throws Exception{
						
		DetailsRequest detailsRequestWithStateId = new DetailsRequest();
		detailsRequestWithStateId.setIdentificationID(subscription.getStateId());
		
		String crimHistSysIdFromPersonSid = getSystemIdFromPersonSID(request, detailsRequestWithStateId);
		
		if(StringUtils.isBlank(crimHistSysIdFromPersonSid)){
			return null;
		}
		
		Document rapSheetDoc = processDetailQueryCriminalHistory(request, crimHistSysIdFromPersonSid);	
				
		logger.info("Rapsheet doc for alt names: \n");		
		XmlUtils.printNode(rapSheetDoc);
		
		SubscribedPersonNames subscribedPersonNames = getAllPersonNamesFromRapsheet(rapSheetDoc);
				
		logger.info("Subscription person names: \n"+ subscribedPersonNames.getOriginalName() + " + " 
				+ Arrays.toString(subscribedPersonNames.getAlternateNamesList().toArray()));	
				
		ChRapsheetData chRapsheetData = new ChRapsheetData();		
		chRapsheetData.subscribedPersonNames = subscribedPersonNames;		
		chRapsheetData.fbiNumber = getFbiIdFromRapsheet(rapSheetDoc);
		chRapsheetData.personDob = getDOBFromRapsheet(rapSheetDoc);
		
		return chRapsheetData;
	}
		
	
	private Map<String, Object> getParams(int start, String purpose, String onBehalfOf) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("purpose", purpose);
		params.put("onBehalfOf", onBehalfOf);
		params.put("validateSubscriptionButton", validateSubscriptionButton);
		return params;
	}
	

	// note system id is used by the broker intermediary to recognize that this is 
	// an edit.  The system id is not set for the add operation
	@RequestMapping(value="updateSubscription", method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String updateSubscription(HttpServletRequest request,
			@ModelAttribute("subscription") Subscription subscription,
			BindingResult errors,
			Map<String, Object> model) throws Exception{					
		
		Element samlElement = samlService.getSamlAssertion(request);		
						
		// get potential spring mvc controller validation errors from validating UI values
		validateSubscriptionUpdate(subscription, errors);		
						
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
	
	
	private void validateSubscriptionUpdate(Subscription subscription, BindingResult errorsBindingResult){
								
		logger.info("sub Edit Request = \n" + subscription);
		
		if(ARREST_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			arrestSubscriptionEditValidator.validate(subscription, errorsBindingResult);
			
		}else if(INCIDENT_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			incidentSubscriptionEditValidator.validate(subscription, errorsBindingResult);
		
		}else if(CHCYCLE_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
			
			chCycleSubscriptionValidator.validate(subscription, errorsBindingResult);
		}
	}
	
	
	private List<String> getValidationBindingErrorsList(BindingResult errors){
		
		List<String> errorMsgList = null;
		
		if(errors.hasErrors()){		
			
			List<ObjectError> bindingErrorsList = errors.getAllErrors();
			
			errorMsgList = new ArrayList<String>();
			
			for(ObjectError iObjError : bindingErrorsList){		
				
				String errorMsgCode = iObjError.getCode();		
				
				errorMsgList.add(errorMsgCode);
			}								
		}		
		return errorMsgList;
	}
		
	
	/**
	 * @return systemId
	 */
	private String getSystemIdFromPersonSID(HttpServletRequest request,
			DetailsRequest detailsRequestWithSid) {
						
		logger.info("person sid: " + detailsRequestWithSid.getIdentificationID());
		
		PersonSearchRequest personSearchRequest = new PersonSearchRequest();				
		personSearchRequest.setPersonSID(detailsRequestWithSid.getIdentificationID());	
		
		List<String> sourceSystemsList = Arrays.asList(OJBCWebServiceURIs.CRIMINAL_HISTORY_SEARCH);		
		personSearchRequest.setSourceSystems(sourceSystemsList);
		
		Element samlAssertion = samlService.getSamlAssertion(request);	
		
		String searchContent = null;
		
		try {			
			searchContent = config.getPersonSearchBean().invokePersonSearchRequest(personSearchRequest,		
					getFederatedQueryId(), samlAssertion);					
		} catch (Exception e) {		
			logger.severe("Exception thrown while invoking person search request:\n" + e);
		}
			
		Document personSearchResultDoc = null;
		
		if(StringUtils.isNotBlank(searchContent)){
			try{
				DocumentBuilder docBuilder = getDocBuilder();		
				personSearchResultDoc = docBuilder.parse(new InputSource(new StringReader(searchContent)));						
			}catch(Exception e){
				logger.severe("Exception thrown while parsing search content Document:\n" + e);
			}			
		}else{
			logger.severe("searchContent was blank");
		}

		NodeList psrNodeList = null;
		
		if(personSearchResultDoc != null){
			try {
				psrNodeList = XmlUtils.xPathNodeListSearch(personSearchResultDoc, 
						"/emrm-exc:EntityMergeResultMessage/emrm-exc:EntityContainer/emrm-ext:Entity/psres:PersonSearchResult");
			} catch (Exception e) {
				logger.severe("Exception thrown - getting nodes from PersonSearchResult:\n" + e);
			}			
		}else{
			logger.severe("personSearchResultDoc was null");
		}
								
		String systemId = null;
		
		if(psrNodeList != null && psrNodeList.getLength() == 1){				
			try{
				Node psrNode = psrNodeList.item(0);			
				systemId = XmlUtils.xPathStringSearch(psrNode, "intel:SystemIdentifier/nc:IdentificationID");						
			}catch(Exception e){
				logger.severe("Exception thrown referencing IdentificationID xpath: \n" + e);
			}						
		}else{
			logger.severe("Search Results (SystemIdentifier/nc:IdentificationID) count != 1");
		}
				
		return systemId;
	}
	
	
	
	/**
	 * @return rap sheet
	 */
	private Document processDetailQueryCriminalHistory(HttpServletRequest request,
			String systemId) {
				
		DetailsRequest detailsRequest = new DetailsRequest();
		detailsRequest.setIdentificationID(systemId);
		detailsRequest.setIdentificationSourceText(OJBCWebServiceURIs.CRIMINAL_HISTORY);
				
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		String detailsContent = null;
		
		try {
			detailsContent = config.getDetailsQueryBean().invokeRequest(detailsRequest, 
					getFederatedQueryId(), samlAssertion);
			
		} catch (Exception e) {
			logger.severe("Exception invoking details request:\n" + e);			
		}
									
		if("noResponse".equals(detailsContent)){			
			logger.severe("No response from Criminial History");			
		}
		
		Document detailsDoc = null;
		
		if(detailsContent != null){
			try {
				detailsDoc = getDocBuilder().parse(new InputSource(new StringReader(detailsContent)));
			} catch (Exception e){
				logger.severe("Exception parsing detailsContent:\n" + detailsContent + "\n, exception:\n" + e);
			}		
		}
						
		return detailsDoc;
	}
	
	
	private String getFbiIdFromRapsheet(Document rapSheetDoc){
	
		String fbiId = null;
		
		try{			
			fbiId = XmlUtils.xPathStringSearch(rapSheetDoc, 
					"/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
					
		}catch(Exception e){
			logger.severe("Exception while getting fbi id from rapsheet: \n" + e);
		}		
		return fbiId;
	}
	
	
	private String getDOBFromRapsheet(Document rapSheetDoc){
		
		String dob = null;
		
		try{			
			dob = XmlUtils.xPathStringSearch(rapSheetDoc, 
					"/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate/nc:Date");
			
		}catch(Exception e){
			logger.severe("Exception while getting dob from rapsheet \n" + e);
		}
		
		return dob;
	}	
	
	SubscribedPersonNames getAllPersonNamesFromRapsheet(Document rapSheetDoc) throws Exception{
						
		SubscribedPersonNames rSubscribedPersonNames = new SubscribedPersonNames();
						
		Node rapSheetNode = XmlUtils.xPathNodeSearch(rapSheetDoc, "/ch-doc:CriminalHistory/ch-ext:RapSheet");	
						
		Node pNameNode = XmlUtils.xPathNodeSearch(rapSheetNode, "rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName");
		
		String personOrigFullName = getNameConcatinated(pNameNode);			
		personOrigFullName = StringUtils.strip(personOrigFullName);
		
		if(StringUtils.isNotBlank(personOrigFullName)){			
			rSubscribedPersonNames.setOriginalName(personOrigFullName);			
		}
						
		NodeList altNameNodeList = XmlUtils.xPathNodeListSearch(rapSheetNode, "rap:RapSheetPerson/nc:PersonAlternateName");	
				
		//process the alternate names
		for(int i=0; i < altNameNodeList.getLength(); i++){
			
			Node iAltNameNode = altNameNodeList.item(i);	
			String fullNameContinated = getNameConcatinated(iAltNameNode);	
			
			if(StringUtils.isNotBlank(fullNameContinated)){
				rSubscribedPersonNames.getAlternateNamesList().add(fullNameContinated);
			}								
		}		
		return rSubscribedPersonNames;		
	}
	

	
	String getNameConcatinated(Node nameNode) throws Exception{
		
		String fullName = "";
		
		String fName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonGivenName");
		String mName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonMiddleName");		
		String lName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonSurName");	
								
		if(StringUtils.isNotBlank(fName)){
			fullName += fName.trim();
		}
						
		if(StringUtils.isNotBlank(mName)){
			fullName += " " + mName.trim();
		}
		
		if(StringUtils.isNotBlank(lName)){
			fullName += " " + lName.trim();
		}
		
		fullName = StringUtils.trim(fullName);
		
		return fullName;	
	}
	
	
	static DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		
		if(docBuilder == null){
			
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			docBuilder = fact.newDocumentBuilder();			
		}				
		return docBuilder;
	}	
	
}

