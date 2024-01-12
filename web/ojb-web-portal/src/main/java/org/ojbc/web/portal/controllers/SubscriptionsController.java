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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.tools.generic.DateTool;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.ojbc.processor.subscription.subscribe.SubscriptionResponseProcessor;
import org.ojbc.processor.subscription.validation.SubscriptionValidationResponseProcessor;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.model.SubscriptionCategoryCode;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.person.search.PersonName;
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
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.ojbc.web.model.subscription.search.SubscriptionStatus;
import org.ojbc.web.model.subscription.validation.SubscriptionValidationResponse;
import org.ojbc.web.portal.controllers.config.PeopleControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.controllers.dto.CriminalHistoryRapsheetData;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.DateTimeJavaUtilPropertyEditor;
import org.ojbc.web.portal.controllers.helpers.DateTimePropertyEditor;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;
import org.ojbc.web.portal.controllers.helpers.SubscriptionQueryResultsProcessor;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.rest.client.SubscriptionsRestClient;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.portal.validators.subscriptions.SubscriptionSearchRequestValidator;
import org.ojbc.web.portal.validators.subscriptions.SubscriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Profile({"subscriptions", "standalone"})
@RequestMapping("/subscriptions")
@SessionAttributes({"subscription", "userLogonInfo", "rapsheetData", "subscriptionSearchRequest", "editSourcePage", "subscriptionFilterCommand"})
public class SubscriptionsController {
	private static final String FBI_SUBSCRIPTION_REQUEST_PROCESSING = "State subscription created. FBI subscription request processing";
	private static final String FBI_SUBSCRIPTION_UPDATE_REQUEST_PROCESSING = "State subscription updated.  FBI subscription update request pending.";
	private static final String FBI_UCN_DOES_NOT_EXIST_WARNING = "FBI UCN does not exist. Subscription with the FBI will not be created. If a FBI UCN is received in the future, an FBI subscription will automatically be created and you will be notified.";

	private Log log = LogFactory.getLog(this.getClass());

	public static final String ARREST_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/arrest";
	public static final String RAPBACK_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/rapback";
	public static final String RAPBACK_TOPIC_SUB_TYPE_CI = "{http://ojbc.org/wsn/topics}:person/rapback/ci";
	public static final String RAPBACK_TOPIC_SUB_TYPE_CS = "{http://ojbc.org/wsn/topics}:person/rapback/cs";
	
	public static final String INCIDENT_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/incident";	
	
	public static final String CHCYCLE_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment";
	
	public static final String PERSON_VEHICLE_CRASH_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/vehicleCrash";
	
	private static DocumentBuilder docBuilder;
	
	private SimpleDateFormat dateFormDOB = new SimpleDateFormat("yyyy-MM-dd");
	private DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private DateTimeFormatter localDateformatterMmDdYyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	@Value("${validationThreshold: 60}")
	Integer validationThreshold;

	@Value("${defaultPersonSearchSubscriptionTopic:}")
	String defaultPersonSearchSubscriptionTopic;
	
	@Value("${validateSubscriptionButton:false}")
	String validateSubscriptionButton;
	
	@Value("${showSubscriptionPurposeDropDown:false}")
	Boolean showSubscriptionPurposeDropDown;
	
	@Value("${showCaseIdInput:false}")
	Boolean showCaseIdInput;
	
	@Value("${fbiIdWarning:false}")
	Boolean fbiIdWarning;
	
	@Value("${sidRegexForAddSubscription:[a-zA-Z0-9]+}")
	String sidRegexForAddSubscription;
	
	@Value("${sidRegexValidationErrorMessage:SID should contain only Chars or Digits}")
	String sidRegexValidationErrorMessage;
	
	@Value("${subscriptionExpirationAlertPeriod:0}")
	String subscriptionExpirationAlertPeriod;
	
	@Resource
	Map<String, SubscriptionStartDateStrategy> subscriptionStartDateStrategyMap;
	
	@Resource
	Map<String, SubscriptionEndDateStrategy> subscriptionEndDateStrategyMap;
	
	@Resource
	Map<String, SubscriptionStartDateStrategy> editSubscriptionStartDateStrategyMap;
	
	@Resource
	Map<String, String> subscriptionDefaultsMap;
	
	@Value("#{getObject('triggeringEventCodeMap')}")
	Map<String, String> triggeringEventCodeMap;
	
    @Resource
    Map<String, String> subscriptionFilterProperties;
		
	@Resource
	Map<String, String> subscriptionFilterValueToLabelMap;
	
	@Resource
	UserSession userSession;
	
	@Resource
	SamlService samlService;
		
	@Resource
	SubscriptionValidator subscriptionValidator;
	
	@Resource
	SubscriptionSearchRequestValidator subscriptionSearchRequestValidator;
	
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
		
	@Autowired
	SubscriptionQueryResultsProcessor subQueryResultProcessor;
	
	@Resource
	SubscriptionsRestClient subscriptionsRestClient;
	
	
	private Map<String, String> subscriptionStatusMap = new HashMap<String, String>();
	private Map<String, String> subscriptionPurposeMap = new HashMap<String, String>();
	private ObjectMapper mapper = new ObjectMapper();;

	@ModelAttribute("subscriptionFilterValueToLabelMap")
	public Map<String, String> getSubscriptionFilterValueToLabelMap(){
		return subscriptionFilterValueToLabelMap;
	}

	@ModelAttribute
    public void setupFormModelAttributes(Model model) {
		
		log.info("subscriptionPurposeValueToLabelMap: "+ subscriptionPurposeValueToLabelMap);
		subscriptionPurposeValueToLabelMap.forEach((key, value) -> {
			if (StringUtils.isNotBlank(key)){
				subscriptionPurposeMap.put(key, value);
			}
		});
		model.addAttribute("subscriptionPurposeMap", subscriptionPurposeMap);
		log.info("subscriptionPurposeMap: "+ subscriptionPurposeMap);
		Arrays.stream(SubscriptionStatus.values()).map(SubscriptionStatus::name)
			.forEach(item -> subscriptionStatusMap.put(item, item));
		model.addAttribute("subscriptionStatusMap", subscriptionStatusMap);
        model.addAttribute("subscriptionFilterProperties", subscriptionFilterProperties);
        model.addAttribute("vmDateTool", new DateTool());
        model.addAttribute("dateTimeformatter", dateTimeformatter);
        model.addAttribute("localDateformatterMmDdYyyyy", localDateformatterMmDdYyyyy);
        model.addAttribute("booleanUtils", BooleanUtils.class);
        model.addAttribute("sidRegexForAddSubscription", sidRegexForAddSubscription);
        model.addAttribute("sidRegexValidationErrorMessage", sidRegexValidationErrorMessage);
        model.addAttribute("triggeringEventCodeMap", triggeringEventCodeMap);
    }
    
	@GetMapping("subscriptionResults")
	public String searchSubscriptions(HttpServletRequest request,	        
			Map<String, Object> model) {		
		
		Element samlElement = samlService.getSamlAssertion(request);
		
		SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(false);
		subscriptionSearchRequest.setOwnerFederatedId((String) request.getAttribute("principal"));
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
        model.put("subscriptionFilterCommand", new SubscriptionFilterCommand());
		
		return "subscriptions/subscriptionResults::subscriptionResultsContent";
	}
	
	@GetMapping("searchForm")
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {
		log.info("Presenting the search Form");
		if (resetForm) {
			SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest(false);
			model.put("subscriptionSearchRequest", subscriptionSearchRequest);
		} 

		return "subscriptions/searchForm::searchFormContent";
	}
    

	@PostMapping("search")
	public String advancedSearch(HttpServletRequest request,	
			@ModelAttribute("subscriptionSearchRequest") @Valid SubscriptionSearchRequest subscriptionSearchRequest,
	        BindingResult errors,
	        Map<String, Object> model, Authentication authentication) {	
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "subscriptions/searchForm::searchFormContent";
		}
					
		Element samlElement = samlService.getSamlAssertion(request);
		subscriptionSearchRequest.setOwnerFederatedId((String) request.getAttribute("principal"));
		performSubscriptionSearch(model, samlElement, subscriptionSearchRequest);
		
		return "subscriptions/subscriptionResults::subscriptionResultsContent";
	}
	
	void performSubscriptionSearch(Map<String, Object> model, Element samlElement,
			SubscriptionSearchRequest subscriptionSearchRequest) {
		String rawResults = null; 
		
		String informationMessage = "";
		
		try{
			rawResults = subConfig.getSubscriptionSearchBean()
					.invokeSubscriptionSearchRequest(subscriptionSearchRequest, samlElement);
			userSession.setMostRecentSubscriptionSearchResult(rawResults);			
			userSession.setSavedMostRecentSubscriptionSearchResult(null);
		}catch(Exception e){
			informationMessage = "Failed retrieving subscriptions";
			e.printStackTrace();
		}			
		log.debug("Subscription results raw xml:\n" + rawResults);
		model.put("subscriptionSearchRequest", subscriptionSearchRequest);
		log.info("subscription Search results: " + rawResults);
		convertSubscriptionSearchResults(model, informationMessage, rawResults, subscriptionSearchRequest);
	}


	@GetMapping(value="filter")
	public String filter(@RequestParam String subscriptionStatus, 
			@RequestParam Boolean showValidationButton, Map<String, Object> model) {
		
		SubscriptionFilterCommand subscriptionFilterCommand = new SubscriptionFilterCommand(); 
		subscriptionFilterCommand.setSubscriptionStatus(subscriptionStatus);
		log.info("In the filter method."); 
		String filterInput;
		//we do not wish to re-filter on filtered results - we always filter on results from a non-filtered search
		if(userSession.getSavedMostRecentSubscriptionSearchResult() == null){
			userSession.setSavedMostRecentSubscriptionSearchResult(userSession.getMostRecentSubscriptionSearchResult());
			filterInput = userSession.getMostRecentSubscriptionSearchResult();
		}else{
			filterInput = userSession.getSavedMostRecentSubscriptionSearchResult();
		}
		
		String filteredResults = filterResults(filterInput, subscriptionFilterCommand, model);
		//saving filtered results allows pagination to function:
		userSession.setMostRecentSearchResult(filteredResults);	

		log.debug("Filtered Result: \n" + filteredResults);
				
		//transform the filtered xml results into html		
		SubscriptionSearchRequest subscriptionSearchRequest = (SubscriptionSearchRequest) model.get("subscriptionSearchRequest"); 
		convertSubscriptionSearchResults(model, "", filteredResults, subscriptionSearchRequest);
		if (BooleanUtils.isTrue(showValidationButton)){
			return "subscriptions/subscriptionResults::subscriptionResultsContent";
		}
		else{
			return "subscriptions/admin/subscriptionResults::subscriptionResultsContent";
		}
	}

	private String filterResults(String filterInput, SubscriptionFilterCommand subscriptionFilterCommand, 
			Map<String, Object> model) {
		String subscriptionStatus = subscriptionFilterCommand.getSubscriptionStatus();

		log.info("inside filter() for status: " + subscriptionStatus);
						
		// filter xml with parameters passed in
		subscriptionFilterCommand.setCurrentDate(new Date());
		
		String sValidationDueWarningDays = subscriptionFilterProperties.get("validationDueWarningDays");
		int iValidationDueWarningDays = Integer.parseInt(sValidationDueWarningDays);
		
		subscriptionFilterCommand.setValidationDueWarningDays(iValidationDueWarningDays);
		model.put("subscriptionFilterCommand", subscriptionFilterCommand);
		
		log.info("Using subscriptionFilterCommand: " + subscriptionFilterCommand);
		
		log.info("\n * filterInput = \n" + filterInput);
		
		return searchResultConverter.filterXml(filterInput, subscriptionFilterCommand);
		
	}
	
	/**
	 * Intended to just be used for returning the modal contents to be 
	 * displayed for adding a subscription. Another method calls the service 
	 * to create the subscription
	 */
	@GetMapping(value="addSubscription")
	public String getAddSubscriptionModal(HttpServletRequest request,@RequestParam Map<String,String> allRequestParams,
			Map<String, Object> model) throws Exception{
		
		log.debug("All request params: " + allRequestParams.toString());
		
		Subscription subscription = new Subscription();

		String firstName = allRequestParams.get("firstName");
		
		if (StringUtils.isNotBlank(firstName))
		{
			subscription.setFirstName(firstName);
		}	
		
		String lastName = allRequestParams.get("lastName");

		if (StringUtils.isNotBlank(lastName))
		{
			subscription.setLastName(lastName);
		}	

		String sid = allRequestParams.get("sid");

		if (StringUtils.isNotBlank(sid))
		{
			subscription.setStateId(sid);
		}	

		String dob = allRequestParams.get("dob");

		if (StringUtils.isNotBlank(dob))
		{
			Date testDob = dateFormDOB.parse(dob);
			subscription.setDateOfBirth(testDob);
		}	

		
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
		
		String sourcePage = allRequestParams.get("sourcePage");
		if ("personSearch".equals(sourcePage) && StringUtils.isNotBlank(defaultPersonSearchSubscriptionTopic))
		{	
			subscription.setTopic(defaultPersonSearchSubscriptionTopic);
		}
		
		if (StringUtils.isNotBlank(sourcePage))
		{	
			model.put("sourcePage", sourcePage);
			log.info("Source page: " + sourcePage);
		}
		
		
		model.put("subscription", subscription);
			
		log.info("inside addSubscription()");
		
		return "subscriptions/addSubscriptionDialog/addSubscriptionModal::addSubscriptionModalContent";
	}

	/**
	 * Gets person original name and alternate(alias) names corresponding to 
	 * the state id(sid) in the provided request object
	 * 
	 *  Two-step process uses search service with sid to get system id
	 *  which is passed into the detail service
	 */
	@GetMapping(value="sidLookup")
	public @ResponseBody CriminalHistoryRapsheetData sidLookup(HttpServletRequest request, 
			@RequestParam("identificationID") String sid,
			@ModelAttribute("subscription") Subscription subscription,
			Model model) throws Exception {
		CriminalHistoryRapsheetData rapsheetData = getChRapsheetData(request, StringUtils.trimToEmpty(sid));
		model.addAttribute("rapsheetData", rapsheetData);
		return rapsheetData;
			
	}

	CriminalHistoryRapsheetData getChRapsheetData(HttpServletRequest request, String sid) throws Exception {
		
		CriminalHistoryRapsheetData sidLookupResult = new CriminalHistoryRapsheetData();
		
		Document rapSheetDoc = getRapsheetBySid(request, sid);

		if (rapSheetDoc != null){
		
			sidLookupResult.setFbiId(getFbiIdFromRapsheet(rapSheetDoc));
			sidLookupResult.setPersonNames(getAllPersonNamesFromRapsheet(rapSheetDoc));
			sidLookupResult.setDobs(getDobsFromRapsheet(rapSheetDoc));
			return sidLookupResult;
		}
		
		return sidLookupResult;
	}

	private Document getRapsheetBySid(HttpServletRequest request, String sid) {
		String systemId = getSystemIdFromPersonSID(request, sid);	
		
		Document rapSheetDoc = null; 
		if (StringUtils.isNotBlank(systemId) ){
			rapSheetDoc = processDetailQueryCriminalHistory(request, systemId);
		}
		return rapSheetDoc;
	}
	
	
	@GetMapping(value="arrestForm")
	public String getArrestForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		log.info("inside arrestForm()");		
				
		Subscription subscription = (Subscription) model.get("subscription");
		
		if (subscription == null)
		{
			subscription = new Subscription();
		}	
		
		log.info("Subscription: " + subscription.toString());
		initDatesForAddForm(subscription, model, ARREST_TOPIC_SUB_TYPE);
				
		// pre-populate an email field on the form w/email from saml token
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		String sEmail = userLogonInfo.getEmailAddress();
		
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
		
		setSubcriptionDefaultPurpose(subscription);		
							
		model.put("subscription", subscription);
		
		return "subscriptions/addSubscriptionDialog/arrestForm::arrestFormContent";
	}
	
	@GetMapping(value="rapbackForm")
	public String getRapbackForm(HttpServletRequest request,
			@ModelAttribute("subscription") Subscription subscription,
			Map<String, Object> model) throws Exception{
		
		log.info("inside getRapbackForm()");		
		initDatesForAddRapbackForm(subscription, model);
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		// pre-populate an email field on the form w/email from saml token
		String sEmail = userLogonInfo.getEmailAddress();
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
		
		if (userLogonInfo.getLawEnforcementEmployerIndicator()){
			setSubcriptionDefaultPurpose(subscription);		
		}
		else{
			subscription.setSubscriptionPurpose("CS");	
		}
		
		model.put("subscription", subscription);
		
		model.put("showSubscriptionPurposeDropDown", showSubscriptionPurposeDropDown);
		
		model.put("showCaseIdInput", showCaseIdInput);
		
		return "subscriptions/addSubscriptionDialog/rapbackForm::rapbackFormContent";
	}

	private void setSubcriptionDefaultPurpose(Subscription subscription) {
		String purposeSelection = subscriptionDefaultsMap.get("purpose");
		if(StringUtils.isNotEmpty(purposeSelection)){
			subscription.setSubscriptionPurpose(purposeSelection);	
		}
	}
	
	
	/**
	 * note: uses pass-by-reference to modify subscription parameter
	 * 
	 * pre-populate the subscription start date as a convenience to the user
	 * this will be displayed on the modal
	 */
	private void initDatesForAddRapbackForm(Subscription subscription, Map<String, Object> model){
				
		SubscriptionStartDateStrategy startDateStrategy = subscriptionStartDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE);		
				
		subscription.setSubscriptionStartDate(startDateStrategy.getDefaultValue());
		
		model.put("isStartDateEditable", startDateStrategy.isEditable());
		
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		
		SubscriptionEndDateStrategy csEndDateStrategy = subscriptionEndDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE_CS);
		if (userLogonInfo.getLawEnforcementEmployerIndicator()){
			SubscriptionEndDateStrategy ciEndDateStrategy = subscriptionEndDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE_CI);
			subscription.setSubscriptionEndDate(ciEndDateStrategy.getDefaultValue());
			model.put("isEndDateEditable", ciEndDateStrategy.isEditable());
			
			model.put("csDefaultEndDate", csEndDateStrategy.getDefaultValue());
			model.put("ciDefaultEndDate", ciEndDateStrategy.getDefaultValue());
		}
		else{
			subscription.setSubscriptionEndDate(csEndDateStrategy.getDefaultValue());
			model.put("isEndDateEditable", csEndDateStrategy.isEditable());
		}
	}
	
	
	void initDatesForEditForm(Map<String, Object> model, String topic){
		
		SubscriptionStartDateStrategy editSubStartDateStrategy = editSubscriptionStartDateStrategyMap.get(topic);
		
		model.put("isStartDateEditable", editSubStartDateStrategy.isEditable());
	}
	
	
	private void initDatesForAddForm(Subscription subscription, Map<String, Object> model, String topic){
		
		// START date
		SubscriptionStartDateStrategy startDateStrategy = subscriptionStartDateStrategyMap.get(topic);		
		subscription.setSubscriptionStartDate(startDateStrategy.getDefaultValue());
		model.put("isStartDateEditable", startDateStrategy.isEditable());		
		
		//END date		
		SubscriptionEndDateStrategy endDateStrategy = subscriptionEndDateStrategyMap.get(topic);
		subscription.setSubscriptionEndDate(endDateStrategy.getDefaultValue());
		model.put("isEndDateEditable", endDateStrategy.isEditable());				
	}
	
	void initDatesForEditVehicleCrashForm(Map<String, Object> model){
		
		SubscriptionStartDateStrategy editIncidentSubStartDateStrategy = editSubscriptionStartDateStrategyMap.get(PERSON_VEHICLE_CRASH_TOPIC_SUB_TYPE);
		
		boolean isStartDateEditable = editIncidentSubStartDateStrategy.isEditable();
		
		model.put("isStartDateEditable", isStartDateEditable);		
	}
	
	@GetMapping(value="incidentForm")
	public String getIncidentForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		log.info("inside incidentForm()");
		
		Subscription subscription = (Subscription) model.get("subscription");
		
		if (subscription == null)
		{
			subscription = new Subscription();
		}	
		
		setSubcriptionDefaultPurpose(subscription);		
		log.debug("Subscription: " + subscription.toString());
		
		initDatesForAddForm(subscription, model, INCIDENT_TOPIC_SUB_TYPE);
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		String sEmail = userLogonInfo.getEmailAddress();

		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
				
		model.put("subscription", subscription);
				
		return "subscriptions/addSubscriptionDialog/incidentForm::incidentFormContent";
	}

	@GetMapping(value="chCycleForm")
	public String getChCycleForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		log.info("inside getChCycleForm()");
		
		Subscription subscription = (Subscription) model.get("subscription");
		
		if (subscription == null)
		{
			subscription = new Subscription();
		}	
		setSubcriptionDefaultPurpose(subscription);		
		
		log.info("Subscription: " + subscription.toString());
				
		initDatesForAddForm(subscription, model, CHCYCLE_TOPIC_SUB_TYPE);
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		String sEmail = userLogonInfo.getEmailAddress();
		
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
				
		model.put("subscription", subscription);
				
		return "subscriptions/addSubscriptionDialog/chCycleForm::chCycleFormContent";
	}	
	
	@GetMapping(value="vehicleCrashForm")
	public String getVehicleCrashForm(HttpServletRequest request,
			Map<String, Object> model) throws Exception{
		
		log.info("inside getVehicleCrashForm()");
		
		Subscription subscription = (Subscription) model.get("subscription");
		
		if (subscription == null)
		{
			subscription = new Subscription();
		}
		
		setSubcriptionDefaultPurpose(subscription);		
		
		log.info("Subscription: " + subscription.toString());
				
		initDatesForAddForm(subscription, model, PERSON_VEHICLE_CRASH_TOPIC_SUB_TYPE);
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		String sEmail = userLogonInfo.getEmailAddress();
		
		if(StringUtils.isNotBlank(sEmail)){
			subscription.getEmailList().add(sEmail);
		}
				
		model.put("subscription", subscription);
				
		return "subscriptions/addSubscriptionDialog/vehicleCrashForm::vehicleCrashFormContent";
	}
	
	/**
	 * @return
	 * 		json array string of errors if any.  These can be used by the UI to display to the user
	 */
	@PostMapping(value="saveSubscription")
	public  @ResponseBody SaveSubscriptionResponse  saveSubscription(HttpServletRequest request, @RequestParam Map<String,String> allRequestParams,
			@ModelAttribute("subscription") @Valid Subscription subscription,
			BindingResult errors, 
			Map<String, Object> model) {
								
		log.info("\n\n\n * * * * inside saveSubscription() * * * * *\n\n: " + subscription + "\n\n\n");
		
		String sourcePage = allRequestParams.get("sourcePage");
		
		Element samlElement = samlService.getSamlAssertion(request);
										
		// retrieve any spring mvc validation errors from the controller
		List<String> errorsList = getValidationBindingErrorsList(errors);
						
		// if no spring mvc validation errors were found, call the subscribe operation and see if there are errors 
		// from the notification broker response
		if(errorsList == null || errorsList.isEmpty()){		
			
			try {
				
				processSubscriptionName(subscription, model);
				errorsList = processSubscribeOperation(subscription, samlElement);										
				
			} catch (Exception e) {
				errorsList = Arrays.asList("An error occurred while processing subscription");				
				log.error("Failed processing subscription: " + e);
			}									
		}	
		
		List<String> subWarningsList = new ArrayList<>(); 
		if (errorsList.isEmpty()){
			subWarningsList = getSubscriptionWarnings(subscription);
		}
		
		String errorMsgsWarnMsgsJson = getErrorsWarningsJson(errorsList, subWarningsList);
		
		log.info("\n\n Returning errors/warnings json:\n\n" + errorMsgsWarnMsgsJson);
		
		SaveSubscriptionResponse subscriptionResponse = new SaveSubscriptionResponse();
		
		subscriptionResponse.setErrorMsgsWarnMsgsJson(errorMsgsWarnMsgsJson);
		
		if (StringUtils.isNotBlank(sourcePage))
		{	
			subscriptionResponse.setSourcePage(sourcePage);
			log.info("Source page: " + sourcePage);
		}
		
		return subscriptionResponse;
	}

	private void processSubscriptionName(Subscription subscription, Map<String, Object> model) {
		if (RAPBACK_TOPIC_SUB_TYPE.equals(subscription.getTopic()) 
				|| ARREST_TOPIC_SUB_TYPE.equalsIgnoreCase(subscription.getTopic())) {
			CriminalHistoryRapsheetData rapsheetData = (CriminalHistoryRapsheetData) model.get("rapsheetData"); 
			
			PersonName personName = rapsheetData.getfullNameToPersonNameMap().get(subscription.getFullName());
			subscription.setFirstName(personName.getGivenName());
			subscription.setLastName(personName.getSurName());
		}
	}		 
	
	List<String> getSubscriptionWarnings(Subscription subscription){
		
		List<String> warningList = new ArrayList<String>();
			
		if(RAPBACK_TOPIC_SUB_TYPE.equals(subscription.getTopic())){			
			
			if (fbiIdWarning){
			
				if(StringUtils.isBlank(subscription.getFbiId())){
					warningList.add(FBI_UCN_DOES_NOT_EXIST_WARNING);
				}
				else{
					if (StringUtils.isBlank(subscription.getSystemId())){
						warningList.add(FBI_SUBSCRIPTION_REQUEST_PROCESSING);
					}
					else {
						warningList.add(FBI_SUBSCRIPTION_UPDATE_REQUEST_PROCESSING);
					}
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
	List<String> processSubscribeOperation(Subscription subscription, Element samlElement) throws Exception{
				
		if(subscription == null){
			throw new Exception("subscription was null");
		}
				
		log.info("Calling subscribe operation...");
		log.info("email lists: " + subscription.getEmailList()); 
		
		SubscriptionInterface subscribeBean = subConfig.getSubscriptionSubscribeBean();
		
		FaultableSoapResponse faultableSoapResponse = subscribeBean.subscribe(subscription, getFederatedQueryId(), samlElement);
				
		log.info("Subscribe operation returned faultableSoapResponse:  " + faultableSoapResponse);
		
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
				
				log.info("\n Parsed/received error: " + sError);				
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
				log.warn("Determined Invalid email, but received no email address values");
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
			
			if (invalidReqResp.getRequestErrorTxt().contains("InvalidEmailAddressesException")){
				rErrorMsg = "The subscription request contains an invalid e-mail address"; 
			}
			else{
				rErrorMsg = "Invalid Request: \n";
				rErrorMsg += invalidReqResp.getRequestErrorSystemName() + "\n";
				rErrorMsg += invalidReqResp.getRequestErrorTxt();
			}
			
		}else if(SubscriptionResponseType.UNSUBSCRIPTION_ACCESS_DENIAL == responseType){
			
			UnsubscriptionAccessDenialResponse unsubAcsDenialResp = (UnsubscriptionAccessDenialResponse)subResponse;
			
			rErrorMsg = "Access denied for unsubscription: \n";
			rErrorMsg += unsubAcsDenialResp.getAccessDenyingSystemName() + "\n";
			rErrorMsg += unsubAcsDenialResp.getAccessDenyingReason();
			
		}else if(SubscriptionResponseType.SUBSCRIPTION_SUCCESS == responseType){
		
			rErrorMsg="";
			log.warn("Attempt was made to get subscription response error type for a scenario where the "
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
	@GetMapping(value="editSubscription")
	public String editSubscription(HttpServletRequest request,			
			@RequestParam String identificationID,
			@RequestParam(required=false, defaultValue="false") Boolean admin,
			@RequestParam(required=false, defaultValue="adminLanding") String editSourcePage,
			Map<String, Object> model) {
		
		model.put("editSourcePage", editSourcePage);
		try{			
			//init success flag to true - allow processing below to set it to false if things go wrong
			model.put("initializationSucceeded", true);
			log.info("identificationID: " + identificationID);
			log.info("admin: " + BooleanUtils.toString(admin, "true", "false", "false"));
						
			Document subQueryResponseDoc = runSubscriptionQueryForEditModal(identificationID, request, admin);
			
			Subscription subscription = parseSubscriptionQueryResults(subQueryResponseDoc);				
			
			if(ARREST_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditForm(model, ARREST_TOPIC_SUB_TYPE);
				 
				SubscriptionEndDateStrategy endDateStrategy = subscriptionEndDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE_CI);
				Date defaultEndDate = OJBCDateUtils.getEndDate(subscription.getSubscriptionStartDate(),
						endDateStrategy.getPeriod());
				model.put("defaultEndDate", defaultEndDate);
				
			}else if(RAPBACK_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditForm(model, RAPBACK_TOPIC_SUB_TYPE);
				 
				UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
				if (userLogonInfo.getLawEnforcementEmployerIndicator()) {

					SubscriptionEndDateStrategy ciEndDateStrategy = subscriptionEndDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE_CI);
					Date ciDefaultEndDate = OJBCDateUtils.getEndDate(subscription.getSubscriptionStartDate(),
							ciEndDateStrategy.getPeriod());
					model.put("ciDefaultEndDate", ciDefaultEndDate);
					
					SubscriptionEndDateStrategy csEndDateStrategy = subscriptionEndDateStrategyMap.get(RAPBACK_TOPIC_SUB_TYPE_CS);
					Date csDefaultEndDate = OJBCDateUtils.getEndDate(subscription.getSubscriptionStartDate(),
							csEndDateStrategy.getPeriod());
					model.put("csDefaultEndDate", csDefaultEndDate);
				}
				 
				model.put("showSubscriptionPurposeDropDown", showSubscriptionPurposeDropDown);
				
				model.put("showCaseIdInput", showCaseIdInput);
				
			}else if(INCIDENT_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditForm(model, INCIDENT_TOPIC_SUB_TYPE);
			
			}else if(CHCYCLE_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditForm(model, CHCYCLE_TOPIC_SUB_TYPE);
			}else if(PERSON_VEHICLE_CRASH_TOPIC_SUB_TYPE.equals(subscription.getTopic())){
				
				initDatesForEditVehicleCrashForm(model);
			}
											
			log.info("Subscription Edit Request: " + subscription);
			model.put("subscription", subscription);	
											
		}catch(Exception e){
			
			model.put("initializationSucceeded", false);			
			e.printStackTrace();
			log.info("initialization FAILED for identificationID=" + identificationID + ":\n" + e.toString());
			model.put("subscription", new Subscription());	
		}
		
		if (!BooleanUtils.isTrue(admin)){
			return "subscriptions/editSubscriptionDialog/editSubscriptionModal::editSubscriptionModalContent";
		}
		else{
			return "subscriptions/admin/edit/editSubscriptionModal::editSubscriptionModalContent";
		}
	}
	
	
	private Subscription parseSubscriptionQueryResults(
			Document subQueryResponseDoc) throws Exception {

		Subscription subscription = subQueryResultProcessor
				.parseSubscriptionQueryResults(subQueryResponseDoc);

		log.info("Subscription Query Results: \n" + subscription.toString());

		return subscription;
	}
	
	
	Document runSubscriptionQueryForEditModal(String identificationID,
			HttpServletRequest request, Boolean admin) throws Exception {

		Element samlAssertion = samlService.getSamlAssertion(request);

		DetailsRequest subscriptionQueryRequest = new DetailsRequest(identificationID, admin);
		subscriptionQueryRequest.setIdentificationSourceText("Subscriptions");

		String subQueryResponse = subConfig.getSubscriptionQueryBean().invokeRequest(
				subscriptionQueryRequest, getFederatedQueryId(), samlAssertion);

		Document subQueryResponseDoc = getDocBuilder().parse(new InputSource(new StringReader(subQueryResponse)));

		log.info("subQueryResponseDoc: \n");
		XmlUtils.printNode(subQueryResponseDoc);

		return subQueryResponseDoc;
	}
	


	/**
	 * 
	 * @return
	 * 		the subscription results page(refreshed after validate)
	 * @throws JsonProcessingException 
	 * @throws com.fasterxml.jackson.databind.JsonMappingException 
	 */
	@PostMapping(value="validate")
	public String  validate(HttpServletRequest request, 
			@RequestParam String subscriptionsJsonString, 
			Map<String, Object> model) throws com.fasterxml.jackson.databind.JsonMappingException, JsonProcessingException {
		List<Subscription> subscriptions = parseSubscriptions(subscriptionsJsonString);
		log.info("Received subscriptions to validate: " + subscriptions);

		Element samlAssertion = samlService.getSamlAssertion(request);
						
		processValidateSubscription(request, subscriptions, model, samlAssertion);
			
		return "subscriptions/subscriptionResults::subscriptionResultsContent";
	}

	private List<Subscription> parseSubscriptions(String subscriptionsJsonString)
			throws JsonProcessingException, com.fasterxml.jackson.databind.JsonMappingException {
		List<Subscription> subscriptions = new ArrayList<>();
		subscriptions = Arrays.asList(mapper.readValue(subscriptionsJsonString, Subscription[].class));
		return subscriptions;
	}
	
	

	private void processValidateSubscription(HttpServletRequest request, List<Subscription> subscriptions, 
			Map<String, Object> model, Element samlAssertion) {		
		
		// used to generated status message
		List<String> validatedIdList = new ArrayList<String>();		
		List<String> failedIdList = new ArrayList<String>();
		List<String> failedDueToValidationDateList = new ArrayList<String>();
		
		// call the validate operation for each id/topic parameter
		for(Subscription subscription : subscriptions){
			
			
			String iTopic = subscription.getTopic();
			String reasonCode = subscription.getSubscriptionPurpose();
			
			if (RAPBACK_TOPIC_SUB_TYPE.equals(iTopic) && !SubscriptionCategoryCode.isCivilCategoryCode(reasonCode)){
				
				LocalDate validationDueDate = subscription.getValidationDueDate(); 
				
				if (validationDueDate != null && 
						LocalDate.now().isBefore(validationDueDate.minusDays(validationThreshold))){
					failedDueToValidationDateList.add(subscription.getSystemId());
					continue; 
				}
			}

						
			try{
				FaultableSoapResponse faultableSoapResponse = subConfig.getSubscriptionValidationBean().validate(
						subscription.getSystemId(), iTopic, reasonCode, getFederatedQueryId(), samlAssertion);
				
				//TODO see if we should check faultableSoapResponse exception attribute(if there is one)
				if(faultableSoapResponse == null){
					
					failedIdList.add(subscription.getSystemId());
					log.error("FAILED! to validate id: " + subscription.getSystemId());
					continue;
				}
				
				boolean isValidated = getValidIndicatorFromValidateResponse(faultableSoapResponse);
				
				log.info("isValidated: " + isValidated + " - for id: " + subscription.getSystemId());

				if(isValidated){
					validatedIdList.add(subscription.getSystemId());	
				}else{
					failedIdList.add(subscription.getSystemId());
				}														
				
			}catch(Exception e){				
				failedIdList.add(subscription.getSystemId());
				log.error("FAILED! to validate id: " + subscription.getSystemId() + ", " + e);
			}														
		}
				
		String operationResultMessage = getOperationResultStatusMessage(validatedIdList, failedIdList, failedDueToValidationDateList);				
		
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


	String getOperationResultStatusMessage(List<String> succeededIdList, List<String> failedIdList, List<String> failedDueToValidationDateList){
				
		String resultMessage = "";
		
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
				
		if(hasFailedIds || (failedDueToValidationDateList != null && !failedDueToValidationDateList.isEmpty())){	
			if (hasFailedIds){
				resultMessage = "Ids Failed: " + sFailedIds;
			}
			
			if (failedDueToValidationDateList != null && !failedDueToValidationDateList.isEmpty()){
				resultMessage += 
						"\n one or more selected subscriptions are not eligible for validation because the validation due date is too far in the future. "; 
			}
		}else{			
			resultMessage = "Operation Successful";			
		}
		
		return resultMessage;		
	}


	@PostMapping(value = "unsubscribe")
	public String unsubscribe(HttpServletRequest request, @RequestParam String subscriptionsJsonString, 
			Map<String, Object> model) throws JsonParseException, JsonMappingException, IOException {
		List<Subscription> subscriptions = parseSubscriptions(subscriptionsJsonString);

		Element samlAssertion = samlService.getSamlAssertion(request);	
		
		log.info("in unsubscribe");
		log.info("subscriptions to unsubscribe: " + subscriptions);
//					
//		ObjectMapper objectMapper = new ObjectMapper();
//		List<Subscription> subscriptions = objectMapper.readValue(subscriptionStrings, new TypeReference<List<Subscription>>(){});
		
		List<String> successfulUnsubIdlist = new ArrayList<String>();		
		List<String> failedUnsubIdList = new ArrayList<String>();
		
		for(Subscription subscription : subscriptions){
								
			Unsubscription unsubscription = new Unsubscription(subscription.getSystemId(), 
					subscription.getTopic(), subscription.getSubscriptionPurpose(), null, null, null, null);
			
			try{
				subConfig.getUnsubscriptionBean().unsubscribe(unsubscription, getFederatedQueryId(), samlAssertion);
				
				successfulUnsubIdlist.add(subscription.getSystemId());				
				
			}catch(Exception e){
				
				failedUnsubIdList.add(subscription.getSystemId());	
				e.printStackTrace();
			}														
		}
		
		String operationStatusResultMsg = getOperationResultStatusMessage(successfulUnsubIdlist, failedUnsubIdList, null);

		refreshSubscriptionsContent(request, model, operationStatusResultMsg);

		SubscriptionSearchRequest subscriptionSearchRequest = (SubscriptionSearchRequest) model.get("subscriptionSearchRequest"); 

		if (BooleanUtils.isNotTrue(subscriptionSearchRequest.getAdminSearch())){
			return "subscriptions/subscriptionResults::subscriptionResultsContent";
		}
		else {
			return "subscriptions/admin/subscriptionResults::subscriptionResultsContent";
		}
	}
	
	
	void refreshSubscriptionsContent(HttpServletRequest request, Map<String, Object> model, String informationMessage) {
		
		Element samlElement = samlService.getSamlAssertion(request);
		
		String rawResults = null;
		
		SubscriptionSearchRequest subscriptionSearchRequest = (SubscriptionSearchRequest) model.get("subscriptionSearchRequest"); 
		try{
						
			rawResults = subConfig.getSubscriptionSearchBean().invokeSubscriptionSearchRequest(subscriptionSearchRequest, samlElement);
			userSession.setMostRecentSubscriptionSearchResult(rawResults);
			userSession.setSavedMostRecentSubscriptionSearchResult(null);
						
		}catch(Exception e){
			e.printStackTrace();
			log.error("Failed retrieving subscriptions, ignoring informationMessage param: " + informationMessage );			
			informationMessage = "Failed retrieving subscriptions";
		}
		SubscriptionFilterCommand subscriptionFilterCommand = (SubscriptionFilterCommand) model.get("subscriptionFilterCommand");
		String filteredResults = filterResults(rawResults, subscriptionFilterCommand, model);
		log.info("Filtered Result: \n" + filteredResults);								
		convertSubscriptionSearchResults(model, informationMessage, filteredResults,
				subscriptionSearchRequest);		
		
	}

	private void convertSubscriptionSearchResults(Map<String, Object> model,
			String informationMessage, String rawResults,
			SubscriptionSearchRequest subscriptionSearchRequest) {
		Map<String,Object> converterParamsMap = getParams(0, null, null);
		converterParamsMap.put("validateSubscriptionButton", BooleanUtils.toStringTrueFalse(BooleanUtils.isNotTrue(subscriptionSearchRequest.getAdminSearch())));
		converterParamsMap.put("includeAgencyORIColumn", BooleanUtils.toStringTrueFalse(BooleanUtils.isTrue(subscriptionSearchRequest.getAdminSearch())));
		converterParamsMap.put("includeStatusColumn", BooleanUtils.toStringTrueFalse(BooleanUtils.isTrue(subscriptionSearchRequest.getAdminSearch())));

		//note must default to empty string instead of null for ui to display nothing when desired instead
		// of having ui display "$subscriptionsContent"
		String transformedResults = ""; 
				
		if(StringUtils.isNotBlank(rawResults)){
			transformedResults = searchResultConverter.convertSubscriptionSearchResult(rawResults, converterParamsMap);			
		}
			
		model.put("subscriptionsContent", transformedResults);
		model.put("informationMessages", informationMessage);
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
	
	@ModelAttribute("subscriptionTypeValueToLabelMap")
	public Map<String, String> getTopicValueToLabelMap() {
		if (subscriptionTypeValueToLabelMap != null && subscriptionTypeValueToLabelMap.size() == 2){
			subscriptionTypeValueToLabelMap.remove("");
		}
		return subscriptionTypeValueToLabelMap;
	}
	
	
	@ModelAttribute("subscriptionPurposeEditableMap")
	public Map<String, String> getSubscriptionPurposeValueToLabelMap(Map<String, ?> model) {
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		
		Map<String, String> subscriptionPurposeMap = new HashMap<>();
		
		if (userLogonInfo.getLawEnforcementEmployerIndicator()){
			subscriptionPurposeMap.putAll(subscriptionPurposeValueToLabelMap);
		}
		else if (userLogonInfo.getCriminalJusticeEmployerIndicator()){
			subscriptionPurposeMap.put("CS", subscriptionPurposeValueToLabelMap.get("CS"));
		}
		return subscriptionPurposeMap;
	}

	Map<String, Object> getParams(int start, String purpose, String onBehalfOf) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("purpose", purpose);
		params.put("onBehalfOf", onBehalfOf);
		params.put("validateSubscriptionButton", validateSubscriptionButton);
		params.put("subscriptionExpirationAlertPeriod", subscriptionExpirationAlertPeriod);
		params.put("validationThreshold", validationThreshold.toString());
		return params;
	}

	// note system id is used by the broker intermediary to recognize that this is 
	// an edit.  The system id is not set for the add operation
	@PostMapping(value="updateSubscription")
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String updateSubscription(HttpServletRequest request,
			@ModelAttribute("subscription") @Valid Subscription subscription,
			BindingResult errors,
			Map<String, Object> model) throws Exception{					
		
		log.info("\n* * * * inside updateSubscription() * * * *\n\n: " + subscription + "\n");
		Element samlElement = samlService.getSamlAssertion(request);		
						
		// get potential spring mvc controller validation errors from validating UI values
//		validateSubscriptionUpdate(subscription, errors);		
						
		List<String> errorsList = getValidationBindingErrorsList(errors);
		
		if(errorsList == null || errorsList.isEmpty()){											
			// get potential errors from processing subscribe operation
			
			errorsList = processSubscribeOperation(subscription, samlElement);			
		}
			
		List<String> warningsList  = new ArrayList<>();
		if (errorsList.isEmpty()){
			warningsList = getSubscriptionWarnings(subscription);
		}
		String errorsWarningsJson = getErrorsWarningsJson(errorsList, warningsList);
		
		log.info("\n\n updateSubscription(...) returning errors/warnings json: \n" + errorsWarningsJson);
		
		return errorsWarningsJson;
	}
	
	
	List<String> getValidationBindingErrorsList(Errors errors){
		
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
			String sid) {

		if (StringUtils.isBlank(sid)){
			return null;
		}
		
		log.info("person sid: " + sid);
		
		PersonSearchRequest personSearchRequest = new PersonSearchRequest();				
		personSearchRequest.setPersonSID(sid);	
		
		List<String> sourceSystemsList = Arrays.asList(OJBCWebServiceURIs.CRIMINAL_HISTORY_SEARCH);		
		personSearchRequest.setSourceSystems(sourceSystemsList);
		
		Element samlAssertion = samlService.getSamlAssertion(request);	
		
		String searchContent = null;
		
		try {			
			searchContent = config.getPersonSearchBean().invokePersonSearchRequest(personSearchRequest,		
					getFederatedQueryId(), samlAssertion);					
		} catch (Exception e) {		
			log.error("Exception thrown while invoking person search request:\n" + e);
		}
			
		Document personSearchResultDoc = null;
		
		if(StringUtils.isNotBlank(searchContent)){
			try{
				DocumentBuilder docBuilder = getDocBuilder();		
				personSearchResultDoc = docBuilder.parse(new InputSource(new StringReader(searchContent)));						
			}catch(Exception e){
				log.error("Exception thrown while parsing search content Document:\n" + e);
			}			
		}else{
			log.error("searchContent was blank");
		}

		NodeList psrNodeList = null;
		
		if(personSearchResultDoc != null){
			try {
				psrNodeList = XmlUtils.xPathNodeListSearch(personSearchResultDoc, 
						"/emrm-exc:EntityMergeResultMessage/emrm-exc:EntityContainer/emrm-ext:Entity/psres:PersonSearchResult");
			} catch (Exception e) {
				log.error("Exception thrown - getting nodes from PersonSearchResult:\n" + e);
			}			
		}else{
			log.error("personSearchResultDoc was null");
		}
								
		String systemId = null;
		
		if(psrNodeList != null && psrNodeList.getLength() == 1){				
			try{
				Node psrNode = psrNodeList.item(0);			
				systemId = XmlUtils.xPathStringSearch(psrNode, "intel:SystemIdentifier/nc:IdentificationID");						
			}catch(Exception e){
				log.error("Exception thrown referencing IdentificationID xpath: \n" + e);
			}						
		}else{
			log.error("Search Results (SystemIdentifier/nc:IdentificationID) count != 1");
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
			log.error("Exception invoking details request:\n" + e);			
		}
									
		if("noResponse".equals(detailsContent)){			
			log.error("No response from Criminial History");			
		}
		
		Document detailsDoc = null;
		
		if(detailsContent != null){
			try {
				detailsDoc = getDocBuilder().parse(new InputSource(new StringReader(detailsContent)));
			} catch (Exception e){
				log.error("Exception parsing detailsContent:\n" + detailsContent + "\n, exception:\n" + e);
			}		
		}
						
		return detailsDoc;
	}
	
	
	String getFbiIdFromRapsheet(Document rapSheetDoc){
	
		String fbiId = null;
		
		try{			
			fbiId = XmlUtils.xPathStringSearch(rapSheetDoc, 
					"/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
					
		}catch(Exception e){
			log.error("Exception while getting fbi id from rapsheet: \n" + e);
		}		
		return fbiId;
	}
	
	
	private List<LocalDate> getDobsFromRapsheet(Document rapSheetDoc){
		
		List<LocalDate> dobs = new ArrayList<>();
		
		try{			
			String primaryDobString = XmlUtils.xPathStringSearch(rapSheetDoc, 
					"/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate"
					+ "[@s:metadata =/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[nc:CommentText='Primary']/@s:id]/nc:Date");
			List<String> dobStrings = new ArrayList<>();
			dobStrings.add(primaryDobString);
			
			NodeList aliasDobNodes = XmlUtils.xPathNodeListSearch(rapSheetDoc, 
					"/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonBirthDate"
					+ "[@s:metadata =/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Metadata[nc:CommentText='Alias']/@s:id]/nc:Date");
			for(int i=0; i < aliasDobNodes.getLength(); i++){
				Node dobNode = aliasDobNodes.item(i);	
				String dobString = dobNode.getTextContent();	
				
				dobStrings.add(dobString);
			}
			
			dobs = dobStrings.stream()
					.map(OJBCDateUtils::parseLocalDate)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			
		}catch(Exception e){
			log.error("Exception while getting dob from rapsheet \n" + e);
		}
		
		return dobs;
	}	
	
	SubscribedPersonNames getAllPersonNamesFromRapsheet(Document rapSheetDoc) throws Exception{
						
		SubscribedPersonNames rSubscribedPersonNames = new SubscribedPersonNames();
						
		Node rapSheetNode = XmlUtils.xPathNodeSearch(rapSheetDoc, "/ch-doc:CriminalHistory/ch-ext:RapSheet");	
						
		Node pNameNode = XmlUtils.xPathNodeSearch(rapSheetNode, "rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName");
		
		PersonName personOrigFullName = getPersonName(pNameNode);			
		
		if(personOrigFullName!= null){			
			rSubscribedPersonNames.setOriginalName(personOrigFullName);			
		}
						
		NodeList altNameNodeList = XmlUtils.xPathNodeListSearch(rapSheetNode, "rap:RapSheetPerson/nc:PersonAlternateName");	
				
		//process the alternate names
		for(int i=0; i < altNameNodeList.getLength(); i++){
			
			Node iAltNameNode = altNameNodeList.item(i);	
			PersonName personName = getPersonName(iAltNameNode);	
			
			if(personName.isNotEmpty()){
				rSubscribedPersonNames.getAlternateNamesList().add(personName);
			}								
		}		
		return rSubscribedPersonNames;		
	}
	

	
	PersonName getPersonName(Node nameNode) throws Exception{
		
		
		String fName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonGivenName");
		String mName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonMiddleName");		
		String lName = XmlUtils.xPathStringSearch(nameNode, "nc:PersonSurName");	
								
		PersonName personName = new PersonName(fName, mName, lName);
		
		return personName;	
	}
	
	
	static DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		
		if(docBuilder == null){
			
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			docBuilder = fact.newDocumentBuilder();			
		}				
		return docBuilder;
	}	
	
	public class SaveSubscriptionResponse implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		public String errorMsgsWarnMsgsJson;
		public String sourcePage;
		
		public String getErrorMsgsWarnMsgsJson() {
			return errorMsgsWarnMsgsJson;
		}
		public void setErrorMsgsWarnMsgsJson(String errorMsgsWarnMsgsJson) {
			this.errorMsgsWarnMsgsJson = errorMsgsWarnMsgsJson;
		}
		public String getSourcePage() {
			return sourcePage;
		}
		public void setSourcePage(String sourcePage) {
			this.sourcePage = sourcePage;
		}

	}	
	
}

