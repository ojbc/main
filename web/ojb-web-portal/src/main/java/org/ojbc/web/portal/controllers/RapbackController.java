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
import static org.ojbc.web.OjbcWebConstants.RAPBACK_TOPIC_SUB_TYPE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.model.rapback.IdentificationDetailQueryType;
import org.ojbc.util.model.rapback.IdentificationResultCategory;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.OjbcWebConstants;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.model.SimpleServiceResponse;
import org.ojbc.web.model.identificationresult.search.CivilIdentificationReasonCode;
import org.ojbc.web.model.identificationresult.search.CriminalIdentificationReasonCode;
import org.ojbc.web.model.identificationresult.search.IdentificationResultsQueryResponse;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.ojbc.web.portal.controllers.config.PeopleControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.RapbackControllerConfigInterface;
import org.ojbc.web.portal.controllers.config.SubscriptionsControllerConfigInterface;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.ojbc.web.security.DocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

import com.google.common.base.Objects;

@Controller
@Profile({"rapback-search","initial-results-query","standalone"})
@SessionAttributes({"rapbackSearchResults", "criminalIdentificationSearchResults", "rapbackSearchRequest", 
	"criminalIdentificationSearchRequest", "identificationResultStatusCodeMap", 
	"criminalIdentificationReasonCodeMap", "civilIdentificationReasonCodeMap", "userLogonInfo", 
	"rapsheetQueryRequest", "detailsRequest", "identificationResultsQueryResponse", "fbiRapsheets"})
@RequestMapping("/rapbacks")
public class RapbackController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	
	private Map<String, String> identificationResultStatusCodeMap = 
			new HashMap<String, String>();
	private Map<String, String> criminalIdentificationStatusCodeMap = 
			new HashMap<String, String>();
	private Map<String, String> criminalIdentificationReasonCodeMap = 
			new HashMap<String, String>();
	private Map<String, String> civilIdentificationReasonCodeMap = 
			new HashMap<String, String>();
	@Resource
	SamlService samlService;
		
	@Resource
	SearchResultConverter searchResultConverter;
	
	@Resource
	RapbackControllerConfigInterface config;
	
	@Resource
	PeopleControllerConfigInterface peopleQueryConfig;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
    @Value("${civilRapbackSubscriptionPeriod:5}")
    Integer civilRapbackSubscriptionPeriod;
    
    @Value("${rapbackSearchDateRange:1095}")
    Integer rapbackSearchDateRange;

    @Value("${identificationResultsSystemName:Identification Results System}")
    String identificationResultsSystemName;
    
    @Value("${criminalHistorySystemName:Criminal History Adapter}")
    String criminalHistorySystemName;

    @Value("${enableEnhancedAudit:false}")
    Boolean enableEnhancedAudit;
    
    @Value("${civilSubscriptionPlaceholderEmail:consult@agency.profile}")
    String civilSubscriptionPlaceholderEmail;
    
    @Value("${allowFirearmSubscription:true}")
    String allowFirearmSubscription;
    
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
		for (IdentificationTransactionState state : IdentificationTransactionState.values()){
			identificationResultStatusCodeMap.put(state.toString(), state.toString());
		}
		
		for (CriminalIdentificationReasonCode criminalReasonCode : CriminalIdentificationReasonCode.values()){
			criminalIdentificationReasonCodeMap.put(criminalReasonCode.name(), criminalReasonCode.getDescription());
		}
		
		for (CivilIdentificationReasonCode civillReasonCode : CivilIdentificationReasonCode.values()){
			civilIdentificationReasonCodeMap.put(civillReasonCode.name(), civillReasonCode.getDescription());
		}
		
    	if (! model.containsAttribute("fbiRapsheets")){
    		model.addAttribute("fbiRapsheets", new HashMap<String, String>());
    	}

		criminalIdentificationStatusCodeMap.put(IdentificationTransactionState.Available_for_Subscription.toString(), "Not archived");
		criminalIdentificationStatusCodeMap.put(IdentificationTransactionState.Archived.toString(), "Archived");
		
        model.addAttribute("identificationResultStatusCodeMap", identificationResultStatusCodeMap);
        model.addAttribute("criminalIdentificationStatusCodeMap", criminalIdentificationStatusCodeMap);
        model.addAttribute("criminalIdentificationReasonCodeMap", criminalIdentificationReasonCodeMap);
        model.addAttribute("civilIdentificationReasonCodeMap", civilIdentificationReasonCodeMap);
	}
    
	@GetMapping(value = "/rapbackResults")
	public String rapbackResults(HttpServletRequest request, 
	        Map<String, Object> model) {
		
		
		IdentificationResultSearchRequest rapbackSearchRequest = (IdentificationResultSearchRequest) model.get("rapbackSearchRequest");
		
		if (rapbackSearchRequest == null){
			rapbackSearchRequest = getDefaultCivilIdentificationSearchRequest();
			model.put("rapbackSearchRequest", rapbackSearchRequest);
		}
		
		return performRapbackSearchAndReturnResult(request, model, rapbackSearchRequest);
	}

	@GetMapping(value = "/rapbackDefaultSearch")
	public String rapbackDefaultSearch(HttpServletRequest request, 
	        Map<String, Object> model) {
		
		IdentificationResultSearchRequest rapbackSearchRequest = getDefaultCivilIdentificationSearchRequest();
		model.put("rapbackSearchRequest", rapbackSearchRequest);
		
		return performRapbackSearchAndReturnResult(request, model, rapbackSearchRequest);
	}

	private String performRapbackSearchAndReturnResult(HttpServletRequest request,
			Map<String, Object> model,
			IdentificationResultSearchRequest searchRequest) {
		Element samlElement = samlService.getSamlAssertion(request);
		
		String informationMessage = "";
		
		
		String rawResults = "";
        try {
            rawResults = config.getRapbackSearchBean()
            		.invokeRapbackSearchRequest(searchRequest, samlElement);
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
		
		return "rapbacks/rapbackResults::rapbackResultsContent";
	}
	
	@PostMapping(value = "rapbackAdvancedSearch")
	public String advanceSearch(HttpServletRequest request,
			@ModelAttribute("rapbackSearchRequest") IdentificationResultSearchRequest rapbackSearchRequest,
	        BindingResult errors, Map<String, Object> model) throws Exception {

		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "rapbacks/searchForm::searchFormContent";
		}

		model.put("rapbackSearchRequest", rapbackSearchRequest);
		return performRapbackSearchAndReturnResult(request, model, rapbackSearchRequest);
	}
	
	@PostMapping(value = "auditPrintResults")
	@ResponseStatus(value = HttpStatus.OK)
	public void auditInitialResultsPrint(HttpServletRequest request,
			@RequestParam String messageId,
			@RequestParam String activeTab,
			@ModelAttribute("identificationResultsQueryResponse") IdentificationResultsQueryResponse identificationResultsQueryResponse,
			@ModelAttribute("userLogonInfo") UserLogonInfo userLogonInfo,
	        Map<String, Object> model) throws Exception {

		logger.info("Message ID: " + messageId);
		logger.info("activeTab ID: " + activeTab);
		logger.info("Identification results system name: " + identificationResultsSystemName);
		logger.info("enableEnhancedAudit: " + BooleanUtils.isTrue(enableEnhancedAudit));

		if (!enableEnhancedAudit) return; 
		
		PrintResults printResults = new PrintResults();
		printResults.setSid(identificationResultsQueryResponse.getSid());
		printResults.setSystemName(identificationResultsSystemName);
		
		auditPrintResults(messageId, activeTab, userLogonInfo, printResults);
	}

	private void auditPrintResults(String messageId, String activeTab,
			UserLogonInfo userLogonInfo, PrintResults printResults) {
		printResults.setMessageId(messageId);
		printResults.setDescription(activeTab);
		
		UserInfo userInfo = new UserInfo(); 
		userInfo.setEmployerName(userLogonInfo.getEmployer());
		userInfo.setEmployerOri(userLogonInfo.getEmployerOri());
		userInfo.setEmployerSubunitName(userLogonInfo.getEmployerSubunitName());
		userInfo.setFederationId(userLogonInfo.getFederationId());
		userInfo.setIdentityProviderId(userLogonInfo.getIdentityProviderId());
		userInfo.setUserEmailAddress(userLogonInfo.getEmailAddress());
		userInfo.setUserFirstName(userLogonInfo.getUserFirstName());
		userInfo.setUserLastName(userLogonInfo.getUserLastName());
		printResults.setUserInfo(userInfo);
		restEnhancedAuditClient.auditPrintResults(printResults);
	}

	@PostMapping(value = "auditRapsheetPrint")
	@ResponseStatus(value = HttpStatus.OK)
	public void auditRapsheetPrint(HttpServletRequest request,
			@RequestParam String messageId,
			@RequestParam String activeTab,
			@ModelAttribute("detailsRequest") DetailsRequest detailsRequest, 
			@ModelAttribute("userLogonInfo") UserLogonInfo userLogonInfo,
			Map<String, Object> model) throws Exception {
		if (!enableEnhancedAudit) return;
			
		logger.info("Message ID: " + messageId);
		logger.info("activeTab ID: " + activeTab);
		logger.info("Criminal History system name: " + criminalHistorySystemName);
		
		PrintResults printResults = new PrintResults();
		printResults.setSid(detailsRequest.getIdentificationID());
		printResults.setSystemName(criminalHistorySystemName);

		auditPrintResults(messageId, activeTab, userLogonInfo, printResults);
	}
	
	@PostMapping("userAcknowledgement")
	@ResponseStatus(value = HttpStatus.OK)
	public void auditUserAcknowledgement(HttpServletRequest request,
			@RequestParam Boolean decision,
			@RequestParam String subscriptionId,
			Map<String, Object> model) throws Exception {
		
		logger.info("decision: " + BooleanUtils.toStringYesNo(decision));
		logger.info("subscriptionId: " + subscriptionId);
		
		UserAcknowledgement userAcknowledgement = new UserAcknowledgement();
		userAcknowledgement.setDecision(decision);
		
		String rapbackSearchResults = (String) model.get("rapbackSearchResults");
		Document rapbackSearchResultsDoc = DocumentUtils.getDocumentFromXmlString(rapbackSearchResults); 
		Node organizationIdentificationResultsSearchResult = 
				XmlUtils.xPathNodeSearch(rapbackSearchResultsDoc, "/oirs-res-doc:OrganizationIdentificationResultsSearchResults"
						+ "/oirs-res-ext:OrganizationIdentificationResultsSearchResult[oirs-res-ext:Subscription"
						+ "/oirs-res-ext:SubscriptionIdentification/nc30:IdentificationID='" + subscriptionId + "']");
		Node identifiedPerson = XmlUtils.xPathNodeSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:IdentifiedPerson");
		String stateId = XmlUtils.xPathStringSearch(identifiedPerson, "jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification"
				+ "[oirs-res-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator='true']/nc30:IdentificationID");
		userAcknowledgement.setSid(stateId);
		
		UserLogonInfo userLogonInfo = (UserLogonInfo) model.get("userLogonInfo");
		UserInfo userInfo = new UserInfo(); 
		userInfo.setUserFirstName(userLogonInfo.getUserFirstName());
		userInfo.setUserLastName(userLogonInfo.getUserLastName());
		userInfo.setIdentityProviderId(userLogonInfo.getIdentityProviderId());
		userInfo.setEmployerName(userLogonInfo.getEmployer());
		userInfo.setUserEmailAddress(userLogonInfo.getEmailAddress());
		userInfo.setEmployerSubunitName(userLogonInfo.getEmployerSubunitName());
		userInfo.setFederationId(userLogonInfo.getFederationId());
		
		userAcknowledgement.setUserInfo(userInfo);
		userAcknowledgement.setDecisionDateTime(LocalDateTime.now());
		userAcknowledgement.setLastUpdatedTime(userAcknowledgement.getDecisionDateTime());

		if (enableEnhancedAudit)
		{	
			restEnhancedAuditClient.auditUserAcknowledgement(userAcknowledgement);
		}	
		
	}
	
	@GetMapping(value = "criminalIdentificationAdvancedSearch")
	public String criminalIdentificationAdvancedSearch(HttpServletRequest request,
			@ModelAttribute("criminalIdentificationSearchRequest") IdentificationResultSearchRequest searchRequest,
			BindingResult errors, Map<String, Object> model) throws Exception {
		
		if (errors.hasErrors()) {
			model.put("errors", errors);
			return "rapbacks/criminalIdentificationSearchForm::criminalIdentificationSearchFormContent";
		}
		
		return performCriminalIdentificationSearchAndReturnResult(request, model, searchRequest);
	}
	

	private String performCriminalIdentificationSearchAndReturnResult(
			HttpServletRequest request, Map<String, Object> model,
			IdentificationResultSearchRequest searchRequest) {
		Element samlElement = samlService.getSamlAssertion(request);
		
		String informationMessage = "";
		
		String rawResults = "";
		try {
			rawResults = config.getRapbackSearchBean()
					.invokeRapbackSearchRequest(searchRequest, samlElement);
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
		
		return "rapbacks/criminalIdentificationResults::criminalIdentificationResultsContent";
	}

	@GetMapping(value = "searchForm")
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm) {
			IdentificationResultSearchRequest rapbackSearchRequest = new IdentificationResultSearchRequest();
			rapbackSearchRequest.setIdentificationResultCategory(IdentificationResultCategory.Civil.name());
			model.put("rapbackSearchRequest", rapbackSearchRequest);
		} 

		return "rapbacks/searchForm::searchFormContent";
	}

	@GetMapping(value = "criminalIdentificationSearchForm")
	public String criminalIdentificationSearchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
			Map<String, Object> model) {
		
		if (resetForm) {
			IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
			searchRequest.setIdentificationResultCategory(IdentificationResultCategory.Criminal.name());
			model.put("criminalIdentificationSearchRequest", searchRequest);
		} 
		
		return "rapbacks/criminalIdentificationSearchForm::criminalIdentificationSearchFormContent";
	}
	
	private IdentificationResultSearchRequest getDefaultCivilIdentificationSearchRequest() {
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		searchRequest.setIdentificationResultCategory(IdentificationResultCategory.Civil.name());
		
		List<String> identificationTransactionStatus = setDateRangeAndStatus(searchRequest);
		
		searchRequest.setIdentificationTransactionStatus(identificationTransactionStatus );;
		return searchRequest;
	}

	private IdentificationResultSearchRequest getDefaultCriminallIdentificationSearchRequest() {
		IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
		searchRequest.setIdentificationResultCategory(IdentificationResultCategory.Criminal.name());
		
		List<String> identificationTransactionStatus = setDateRangeAndStatus(searchRequest);
		
		searchRequest.setIdentificationTransactionStatus(identificationTransactionStatus );;
		return searchRequest;
	}
	
	private List<String> setDateRangeAndStatus(
			IdentificationResultSearchRequest searchRequest) {
		List<String> identificationTransactionStatus = new ArrayList<String>();
		identificationTransactionStatus.add(IdentificationTransactionState.Available_for_Subscription.toString());
		identificationTransactionStatus.add(IdentificationTransactionState.Subscribed_State.toString());
		identificationTransactionStatus.add(IdentificationTransactionState.Subscribed_State_FBI_Pending.toString());
		identificationTransactionStatus.add(IdentificationTransactionState.Subscribed_State_FBI_Error.toString());
		identificationTransactionStatus.add(IdentificationTransactionState.Subscribed_State_FBI.toString());
		
		searchRequest.setReportedDateEndLocalDate(LocalDate.now());
		searchRequest.setReportedDateStartLocalDate(LocalDate.now().minusDays(rapbackSearchDateRange -1));
		return identificationTransactionStatus;
	}

	@GetMapping(value = "initialResults/{sid}/{transactionNumber}")
	public String initialResults(HttpServletRequest request, 
			@PathVariable("sid") String sid,
			@PathVariable("transactionNumber") String transactionNumber,
			Map<String, Object> model) {
		try {
			processDetailRequest(request, sid, transactionNumber, IdentificationDetailQueryType.InitialResults, model);
			return "rapbacks/initialResultsDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}
	
	@GetMapping(value = "nsorCheckResults/{transactionNumber}")
	public String nsorCheckResults(HttpServletRequest request, 
			@PathVariable("transactionNumber") String transactionNumber,
			Map<String, Object> model) {
		try {
			processDetailRequest(request, null, transactionNumber, IdentificationDetailQueryType.NSORCheckResults, model);
			return "rapbacks/nsorFiveYearCheckResults";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}
	
	@GetMapping(value = "initialResults/{transactionNumber}")
	public String initialCriminalResults(HttpServletRequest request, 
			@PathVariable("transactionNumber") String transactionNumber,
			Map<String, Object> model) {
		try {
			processDetailRequest(request, null, transactionNumber, IdentificationDetailQueryType.InitialResults, model);
			return "rapbacks/initialResultsDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}
	
	@GetMapping(value = "stateRapsheet/{sid}/{transactionNumber}/{hasFbiRapsheet}")
	public String getStateRapsheet(HttpServletRequest request, 
			@PathVariable("sid") String sid,
			@PathVariable("transactionNumber") String transactionNumber,
			@PathVariable("hasFbiRapsheet") Boolean hasFbiRapsheet,
			Map<String, Object> model) {
		RapSheetQueryRequest rapsheetQueryRequest = new RapSheetQueryRequest(sid, transactionNumber, hasFbiRapsheet); 
		model.put("rapsheetQueryRequest", rapsheetQueryRequest);
		
		try {
			processStateRapsheetRequest(request, sid, model);
			return "rapbacks/rapsheets";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}
	
	@GetMapping(value = "fbiRapsheet/{transactionNumber}")
	@ResponseBody
	public String getFbiRapsheet(HttpServletRequest request, 
			@PathVariable("transactionNumber") String transactionNumber,
			Map<String, Object> model) {
		
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, String> fbiRapsheets = (Map<String, String>) model.get("fbiRapsheets"); 
			
			String rapsheet = fbiRapsheets.get(transactionNumber); 
			
			if (StringUtils.isBlank(rapsheet)){
				rapsheet = processFbiRapsheetRequest(request, transactionNumber, model);
				fbiRapsheets.put(transactionNumber, rapsheet);
				model.put("fbiRapsheets", fbiRapsheets);
			}
			
			return rapsheet;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Got exception when retrieving the FBI rapsheet", ex);
			return "<p>Error retrieving FBI rap sheet, please report to the IT department or try again later</p>";
		}
	}
	
	private String processFbiRapsheetRequest(HttpServletRequest request,
			String transactionNumber, Map<String, Object> model) throws Exception {
		String rapbackSearchResults = (String) model.get("rapbackSearchResults");
		DetailsRequest detailsRequest = new DetailsRequest();
		detailsRequest.setQueryType("FBIRapsheet");
		detailsRequest.setIdentificationSourceText(OJBCWebServiceURIs.CRIMINAL_HISTORY_FBI);
		Document document = OJBUtils.loadXMLFromString(rapbackSearchResults);
		Node node = XmlUtils.xPathNodeSearch(document, "/oirs-res-doc:OrganizationIdentificationResultsSearchResults/"
				+ "oirs-res-ext:OrganizationIdentificationResultsSearchResult[intel30:SystemIdentification/nc30:IdentificationID='" + transactionNumber + "']"); 
		
		String sid = XmlUtils.xPathStringSearch(node, "oirs-res-ext:IdentifiedPerson/jxdm50:PersonAugmentation/"
				+ "jxdm50:PersonStateFingerprintIdentification[oirs-res-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator = 'true']/nc30:IdentificationID");
		detailsRequest.setIdentificationID(sid);
		
		String fbiId = XmlUtils.xPathStringSearch(node, "oirs-res-ext:IdentifiedPerson/jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		detailsRequest.setFbiId(fbiId);
		
		String rapbackSubscriptionId = XmlUtils.xPathStringSearch(node, "oirs-res-ext:Subscription/oirs-res-ext:RapBackSubscriptionIdentification/nc30:IdentificationID");
		detailsRequest.setRapbackSubscriptionId(rapbackSubscriptionId);
		
		String rapbackActivityNotificationId = XmlUtils.xPathStringSearch(node, "oirs-res-ext:Subscription/oirs-res-ext:RapBackActivityNotificationIdentification/nc30:IdentificationID");
		detailsRequest.setRapbackActivityNotificationId(rapbackActivityNotificationId);
		
		detailsRequest.setFederatedQueryId(getFederatedQueryId());
		String fbiRapsheetDocumentString = peopleQueryConfig.getDetailsQueryBean().invokeRequest(detailsRequest, 
				detailsRequest.getFederatedQueryId(), samlService.getSamlAssertion(request));

		String fbiRapsheet = XmlUtils.getStringFromBinaryDataElement(OJBUtils.loadXMLFromString(fbiRapsheetDocumentString), 
				"/cht-doc:CriminalHistoryTextDocument/cht-doc:FederalCriminalHistoryRecordDocument/cht-doc:Base64BinaryObject");
		
		model.put("detailsRequest", detailsRequest);
		return "<div style='height:480; overflow:scroll;'><pre>" + fbiRapsheet + "</pre></div>";
	}

	private void processStateRapsheetRequest(HttpServletRequest request, 
			String sid, Map<String, Object> model) {
				
		DetailsRequest detailsRequest = new DetailsRequest();
		detailsRequest.setQueryType("StateRapsheet");
		detailsRequest.setIdentificationID(sid);
		detailsRequest.setIdentificationSourceText(OJBCWebServiceURIs.CRIMINAL_HISTORY);
		detailsRequest.setTextRapsheetRequest(true);
		detailsRequest.setCivilPurposeRequest(true);
		detailsRequest.setFederatedQueryId(getFederatedQueryId());
				
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		String stateRapsheetDoc = null;
		
		try {
			stateRapsheetDoc = peopleQueryConfig.getDetailsQueryBean().invokeRequest(detailsRequest, 
					detailsRequest.getFederatedQueryId(), samlAssertion);
			model.put("detailsRequest", detailsRequest);
		} catch (Exception e) {
			logger.error("Exception invoking details request:\n" + e);
			throw new IllegalStateException("Exception invoking details request:\n" + e);
		}
									
		if("noResponse".equals(stateRapsheetDoc) || StringUtils.isBlank(stateRapsheetDoc)){			
			logger.error("No response from Criminial History");	
			throw new IllegalStateException("No response from Criminial History");
		}
		
		try {
			Document responseDocument =  OJBUtils.loadXMLFromString(stateRapsheetDoc); 
 			String errorText = XmlUtils.xPathStringSearch(responseDocument, "/cht-doc:CriminalHistoryTextDocument/error:PersonQueryResultError/error:ErrorText");

 			if (StringUtils.isNotBlank(errorText)) {
 				model.put("errorText", errorText);
 			}
 			else {
 				String stateRapsheet = XmlUtils.getStringFromBinaryDataElement(OJBUtils.loadXMLFromString(stateRapsheetDoc), "/cht-doc:CriminalHistoryTextDocument/cht-doc:StateCriminalHistoryRecordDocument/cht-doc:Base64BinaryObject");
 				model.put("stateRapsheet", stateRapsheet);
 			}
		} catch (Exception e){
			throw new IllegalStateException(e.getMessage());
		}		
						
	}
	
	@GetMapping(value = "subsequentResults")
	public String subsequentResults(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		try {
			processDetailRequest(request, null, transactionNumber, IdentificationDetailQueryType.SubsequentResults, model);
			return "rapbacks/subsequentResultsDetails";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "common/searchDetailsError::searchDetailsErrorContent";
		}
	}
	
	@PostMapping(value = "subscribe")
	public @ResponseBody String subscribe(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		
		logger.info("In civil subscribe function");
		try {
			Subscription subscription = buildSubscription(transactionNumber, model);
			FaultableSoapResponse faultableSoapResponse = callSubscribeService(subscription, samlService.getSamlAssertion(request));
			
			if (faultableSoapResponse.isSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", faultableSoapResponse.getException().getMessage());
				return faultableSoapResponse.getException().getMessage() + ", please report the error and try again later.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@GetMapping(value = "archive")
	public @ResponseBody String archive(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		try {
			SimpleServiceResponse simpleServiceResponse = 
					config.getIdentificationResultsModificationBean().handleIdentificationResultsModificationRequest(
							transactionNumber, true, samlService.getSamlAssertion(request));
			
			if (simpleServiceResponse.getSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", simpleServiceResponse.getErrorMessage());
				return simpleServiceResponse.getErrorMessage() + ", please report the error and try again later.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Got error:  " + ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@GetMapping(value = "unarchive")
	public @ResponseBody String unarchive(HttpServletRequest request, @RequestParam String transactionNumber,
			Map<String, Object> model) {
		try {
			SimpleServiceResponse simpleServiceResponse = 
					config.getIdentificationResultsModificationBean().handleIdentificationResultsModificationRequest(
							transactionNumber, false, samlService.getSamlAssertion(request));
			
			if (simpleServiceResponse.getSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", simpleServiceResponse.getErrorMessage());
				return simpleServiceResponse.getErrorMessage() + ", please report the error and try again later.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Got error:  " + ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@GetMapping(value = "unsubscribe")
	public @ResponseBody String unsubscribe(HttpServletRequest request, @RequestParam String subscriptionId,
			Map<String, Object> model) {
		try {
			Unsubscription unsubscription = new Unsubscription(subscriptionId, RAPBACK_TOPIC_SUB_TYPE, OjbcWebConstants.NON_CRIMINAL_JUSTICE_EMPLOYMENT, null, null, null, null);
			try{
				subConfig.getUnsubscriptionBean().unsubscribe(unsubscription, getFederatedQueryId(), samlService.getSamlAssertion(request));
				return "success";
			}catch(Exception e){
				e.printStackTrace();
				return ("Got error:  " + e.getMessage() + ", please report the error and try again later. ");
			}														
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Got error:  " + ex.getMessage() + ", please report the error and try again later.";
		}
	}
	
	@GetMapping(value = "validate")
	public @ResponseBody String validate(HttpServletRequest request, @RequestParam String subscriptionId,
			@RequestParam String reasonCode,
			Map<String, Object> model) {
		try{
			
			FaultableSoapResponse faultableSoapResponse = subConfig.getSubscriptionValidationBean().validate(
					subscriptionId, RAPBACK_TOPIC_SUB_TYPE, reasonCode, 
					getFederatedQueryId(), samlService.getSamlAssertion(request));
			if (faultableSoapResponse.isSuccess()){
				return "success";
			}
			else{
				model.put("informationMessages", faultableSoapResponse.getException().getMessage());
				return faultableSoapResponse.getException().getMessage() + ", please report the error and try again later.";
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
						+ "/oirs-res-ext:OrganizationIdentificationResultsSearchResult[intel30:SystemIdentification"
						+ "/nc30:IdentificationID='" + transactionNumber + "']");
		Node identifiedPerson = XmlUtils.xPathNodeSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:IdentifiedPerson");
		Subscription subscription = new Subscription(); 
		subscription.setTransactionNumber(transactionNumber);
		
		String ownerProgramOca = XmlUtils.xPathStringSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:AgencyCaseNumber");
		subscription.setCaseId(ownerProgramOca);
		subscription.setOwnerProgramOca(ownerProgramOca);
		
		DateTime dob = XmlUtils.parseXmlDate(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonBirthDate/nc30:Date"));
		subscription.setDateOfBirth(dob.toDate());
		
		subscription.setFirstName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonGivenName"));
		subscription.setLastName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonSurName"));
		subscription.setFullName(XmlUtils.xPathStringSearch(identifiedPerson, "nc30:PersonName/nc30:PersonFullName"));
		
		subscription.setFbiId(XmlUtils.xPathStringSearch(identifiedPerson, "jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID"));
		subscription.setStateId(XmlUtils.xPathStringSearch(identifiedPerson, "jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification"
				+ "[oirs-res-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator='true']/nc30:IdentificationID"));
		
		
		String orgnizationRefId = XmlUtils.xPathStringSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:IdentificationRequestingOrganization/@s30:ref");
		
		subscription.setEmailList(Arrays.asList(civilSubscriptionPlaceholderEmail));
		setSubscripitonTriggeringEvents(rapbackSearchResultsDoc, subscription, orgnizationRefId);
		
		String reasonCode = XmlUtils.xPathStringSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:CivilIdentificationReasonCode");
		subscription.setSubscriptionPurpose(reasonCode);

		String status = XmlUtils.xPathStringSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:IdentificationResultStatusCode");
		if (Objects.equal(status, "Subscribed(State)")){
			Node subscriptionNode = XmlUtils.xPathNodeSearch(organizationIdentificationResultsSearchResult, "oirs-res-ext:Subscription");
			String subscriptionId = XmlUtils.xPathStringSearch(subscriptionNode, "oirs-res-ext:SubscriptionIdentification/nc30:IdentificationID");
			subscription.setSystemId(subscriptionId);
			String topic = XmlUtils.xPathStringSearch(subscriptionNode, "wsn-br:Topic");
			subscription.setTopic(topic);
			
			String startDate = XmlUtils.xPathStringSearch(subscriptionNode, "nc30:ActivityDateRange/nc30:StartDate/nc30:Date");
			subscription.setSubscriptionStartDate(OJBCDateUtils.toDate(LocalDate.parse(startDate)));
			String endDate = XmlUtils.xPathStringSearch(subscriptionNode, "nc30:ActivityDateRange/nc30:EndDate/nc30:Date");
			subscription.setSubscriptionEndDate(OJBCDateUtils.toDate(LocalDate.parse(endDate)));
		}
		else{
			setStartDateAndEndDate(subscription);
			subscription.setTopic(RAPBACK_TOPIC_SUB_TYPE);
		}
		return subscription;
	}

	private void setSubscripitonTriggeringEvents(Document rapbackSearchResultsDoc, 
			Subscription subscription, String organizationId) throws Exception {
		NodeList triggeringEventCodeNodeList = XmlUtils.xPathNodeListSearch(rapbackSearchResultsDoc, 
				"/oirs-res-doc:OrganizationIdentificationResultsSearchResults/nc30:EntityOrganization[@s30:id = '" + organizationId + "']"
						+ "/oirs-res-ext:OrganizationAuthorizedTriggeringEvents/oirs-res-ext:FederalTriggeringEventCode");
		
		if (triggeringEventCodeNodeList != null && triggeringEventCodeNodeList.getLength() > 0){
			List<String> triggeringEventCodeList = new ArrayList<String>();
			for (int i = 0; i < triggeringEventCodeNodeList.getLength(); i++) {
				if (triggeringEventCodeNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element triggeringEventCode = (Element) triggeringEventCodeNodeList.item(i);
					triggeringEventCodeList.add(triggeringEventCode.getTextContent());
				}
			}
			
			subscription.setFederalTriggeringEventCode(triggeringEventCodeList);;;
		}
	}
	
	private void setStartDateAndEndDate(Subscription subscription) {
		Calendar cal = Calendar.getInstance(); 
		subscription.setSubscriptionStartDate(cal.getTime());
		cal.add(Calendar.YEAR, civilRapbackSubscriptionPeriod);
		subscription.setSubscriptionEndDate(cal.getTime());
	}

	private void processDetailRequest(HttpServletRequest request, String sid, String transactionNumber,
			IdentificationDetailQueryType identificationDetailQueryType, Map<String, Object> model)
			throws Exception {
		Element samlAssertion = samlService.getSamlAssertion(request);		
		
		IdentificationResultsQueryResponse identificationResultsQueryResponse = 
				config.getIdentificationResultsQueryBean().invokeIdentificationResultsQueryRequest(
						transactionNumber, identificationDetailQueryType, samlAssertion);
		identificationResultsQueryResponse.setSid(sid);
		
		model.put("identificationResultsQueryResponse", identificationResultsQueryResponse);
	}

	
	@GetMapping(value = "/criminalIdentificationsResults")
	public String criminalIdentificationResults(HttpServletRequest request, 
			Map<String, Object> model) {
		
		IdentificationResultSearchRequest criminalIdentificationSearchRequest = 
				(IdentificationResultSearchRequest) model.get("criminalIdentificationSearchRequest");
		
		if (criminalIdentificationSearchRequest == null){
			criminalIdentificationSearchRequest = getDefaultCriminallIdentificationSearchRequest();
			model.put("criminalIdentificationSearchRequest", getDefaultCriminallIdentificationSearchRequest());
		}

		return performCriminalIdentificationSearchAndReturnResult(request, model, criminalIdentificationSearchRequest);
	}
	
	@GetMapping(value = "/criminalIdentificationDefaultSearch")
	public String criminalIdentificationDefaultSearch(HttpServletRequest request, 
			Map<String, Object> model) {
		
		IdentificationResultSearchRequest criminalIdentificationSearchRequest = getDefaultCriminallIdentificationSearchRequest();
		model.put("criminalIdentificationSearchRequest", getDefaultCriminallIdentificationSearchRequest());
		
		return performCriminalIdentificationSearchAndReturnResult(request, model, criminalIdentificationSearchRequest);
	}
	
}

