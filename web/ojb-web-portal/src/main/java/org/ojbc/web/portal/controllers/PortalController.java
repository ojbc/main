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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.SearchProfile;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.controllers.dto.PersonFilterCommand;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.ojbc.web.portal.services.OTPService;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.security.Authorities;
import org.ojbc.web.security.SecurityContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@SessionAttributes({"sensitiveInfoAlert", "userLogonInfo", "userSignOutUrl", "samlAssertion", 
	"requestHeaders", "incidentSystemsToQuery", "personFilterCommand"})
public class PortalController implements ApplicationContextAware {

	@Resource
	OTPService otpService;
	
	@Resource
    AppProperties appProperties;
	
	static final String DEFAULT_USER_TIME_ONLINE = "0:00";
	static final String DEFAULT_USER_LOGON_MESSAGE = "Not Logged In";
	
	private static final Log log = LogFactory.getLog(PortalController.class);


	private XPath xPath;
	
	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;
	
    @Value("${requireOtpAuthentication:false}")
    Boolean requireOtpAuthentication;
	
	@Value("${showReasonsForSearch:true}")
	Boolean showReasonsForSearch;

	@Value("${showDemographicsFilter:true}")
	Boolean showDemographicsFilter;
	
	@Value("${sensitiveInfoAlert:false}")
	Boolean sensitiveInfoAlert;
	
	@Value("${sensitiveInfoAlertMessage:You are about to view sensitive information. Please press OK to proceed.}")
	String sensitiveInfoAlertMessage;
	
	@Resource
	Map<String, String> races;

	@Resource
	Map<String, String> eyeColors;

	@Resource
	Map<String, String> hairColors;
	
	@Resource
	Map<String, String> searchProfilesEnabled;
	
	@Resource
	Map<String, String> searchPurposes;
	
	@Resource
	Map<String, String> leftMenuLinks;
	
	@Autowired(required=false)
	Map<String, String> leftMenuLinkTitles;

    @Resource
	Map<String, String> leftBarLinks;

	Map<String, String> leftBarTitles;
    
    @Resource
    Map<String, String> subscriptionFilterProperties;
    
	@Resource
	Map<String, String> subscriptionFilterValueToLabelMap;
	
	@Resource
	Map<String, String> systemsToQuery_incidents;
	
	@Value("#{getObject('rapbackFilterOptionsMap')}")
	Map<String, String> rapbackFilterOptionsMap;

	@Value("#{getObject('entityLogoutReturnUrlMap')}")
	Map<String, String> entityLogoutReturnUrlMap;
	
    @Value("${userSignOutUrl:/portal/defaultLogout}")
    String userSignOutUrl;
    
	@Resource
	UserSession userSession;
	
	@Resource
	SamlService samlService;
	
    @Value("${enableEnhancedAudit:false}")
    Boolean enableEnhancedAudit;
    
	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	private Map<String, Boolean> visibleProfileStateMap;
	

	public PortalController() {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		xPath = xPathFactory.newXPath();
		xPath.setNamespaceContext(new Saml2NamespaceContext());
		
		// This map is used to translate those possible values for "searchProfilesEnabled" map entries
		// that represent visible states ("enabled", "disabled") into booleans indicating whether they are actually enabled
		visibleProfileStateMap = new HashMap<String, Boolean>();
		visibleProfileStateMap.put("enabled", true);
		visibleProfileStateMap.put("disabled", false);
	}

    @GetMapping("/portal/landingPage")
    public String landingPage(){
	    return "portal/landingPage::landingPageContent";
	}

    @GetMapping("/portal/helpPage")
    public String helpPage(){
	    return "portal/helpPage";
	}

    @GetMapping("/portal/faq")
    public String faq(){
	    return "portal/faq::faqContent";
	}
    
    @GetMapping("/")
    public String index(HttpServletRequest request, Map<String, Object> model, Authentication authentication) throws Exception{

		// To pull something from the header you want something like this
		// String header = request.getHeader("currentUserName");

		org.ojbc.web.portal.controllers.UserLogonInfo userLogonInfo = new org.ojbc.web.portal.controllers.UserLogonInfo();

		try {
			Element assertionElement = samlService.getSamlAssertion(request);
						
			userLogonInfo = getUserLogonInfo(assertionElement);	
			model.put("userLogonInfo", userLogonInfo);
			
			userSession.setUserLogonInfo(userLogonInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.put("personFilterCommand", new PersonFilterCommand());
		model.put("currentUserName", userLogonInfo.getUserNameString());
		model.put("timeOnline", userLogonInfo.getTimeOnlineString());
		
		model.put("searchLinksHtml", getSearchLinksHtml(model, authentication));
		
		if (model.get("sensitiveInfoAlert") == null) {
			model.put("sensitiveInfoAlert", sensitiveInfoAlert);
		}
		
    	putSamlAssertionIntoModel(request, model);
    	
    	return "index";
	}

	@GetMapping("/samlTokenInfo")
	public String getSamlTokenInfo(HttpServletRequest request, Model model) throws Exception {
		
		Element samlToken = (Element) model.getAttribute("samlAssertion");
		model.addAttribute("samlToken", XmlUtils.getPrettyStringFromNode(samlToken)); 
		
		return "saml::samlTokenInfo";
	}

	private void putSamlAssertionIntoModel(HttpServletRequest request, Map<String, Object> model)
			throws Exception {
    	Element samlAssertion = (Element)request.getAttribute("samlAssertion");
    	model.put("samlAssertion", samlAssertion);
    	
		List<Pair<String,String>> requestHeaders = new ArrayList<>();
		
		for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			String value = request.getHeader(name);
			requestHeaders.add(Pair.of(name, value));
		}
		model.put("requestHeaders", requestHeaders);
    	
	}

    @GetMapping("/portal/defaultLogout")
    public String defaultLogout(HttpServletRequest request, Map<String, Object> model, Authentication authentication){
    	return "logoutSuccess";
    }
    

    @GetMapping("/portal/landingLeftBar")
    public String landingLeftBar(){
    	return "common/landingLeftBar::landingLeftBarContent";
    }

    @PostMapping(value="/portal/subscriptionsLeftBar")
    public String subscriptionsLeftBar(HttpServletRequest request, 
    		@ModelAttribute SubscriptionFilterCommand subscriptionFilterCommand, 
    		Map<String, Object> model){   
    	
    	return "common/_subscriptionsLeftBar";
    }
    
    @PostMapping(value="/portal/rapbackLeftBar")
    public String rapbackLeftBar(Map<String, Object> model){   
        return "common/_rapbackLeftBar";
    }
    
    @PostMapping(value="/portal/criminalIdentificationLeftBar")
    public String criminalIdentificationLeftBar(Map<String, Object> model){   
    	return "common/_criminalIdentificationLeftBar";
    }
    
    @GetMapping(value="/portal/leftBar")
    public String leftBar(HttpServletRequest request,
			Map<String, Object> model){
    	model.put("showReasonsForSearch", showReasonsForSearch);
    	model.put("showDemographicsFilter", showDemographicsFilter);
    	
	    return "common/leftBar::leftBarContent";
	}
    
    @GetMapping(value="/portal/negateSenstiveInfoAlert")
    public @ResponseBody String negateSenstiveInfoAlert( Map<String, Object> model ){
        model.put("sensitiveInfoAlert", false); 
        return "success";
    }

	private List<SearchProfile> getActiveSearchProfiles(Map<String, Object> model, Authentication authentication) {
		List<SearchProfile> visibleProfiles = new ArrayList<SearchProfile>();
		
		for (Map.Entry<String, String> entry : searchProfilesEnabled.entrySet()) {
			Boolean enabled = visibleProfileStateMap.get(entry.getValue()); 
			
			if ( enabled != null ) {
				String displayName = appProperties.getSearchProfileTitles().get(entry.getKey());
				
				boolean hasAccess = false; 
				switch (entry.getKey()) {
				case "incident":
					Map<String, String> incidentSystemsToQuery = getIncidentSystemsToQuery(authentication); 
					model.put("incidentSystemsToQuery", incidentSystemsToQuery);
					if (incidentSystemsToQuery.size() > 0 && 
							(authentication == null || SecurityContextUtils.hasAuthority(authentication, Authorities.AUTHZ_INCIDENT_DETAIL))){
						hasAccess = true; 
					}
					break; 
				case "vehicle":
					if (authentication == null || SecurityContextUtils.hasAuthority(authentication, Authorities.AUTHZ_INCIDENT_DETAIL)){
						hasAccess = true;
					}
					break; 
				default:
					hasAccess = true;
				}
				
				if (hasAccess) {
					visibleProfiles.add(new SearchProfile(entry.getKey(), displayName, enabled));
				}
			}
		}
		
		return visibleProfiles;
	}

	String getSearchLinksHtml(Map<String, Object> model, Authentication authentication) {
		StringBuilder links = new StringBuilder();

		for(SearchProfile profile : getActiveSearchProfiles(model, authentication)) {
			links.append("<a id='").append(getLinkId(profile)).append("' href='#' class='dropdown-item small ")
				.append(getLinkId(profile)).append("'>");
			links.append(profile.getDisplayName());
			links.append("</a>");
		}
		
		return links.toString();
	}
	
	private String getLinkId(SearchProfile profile) {
		return profile.getId() + "SearchLink" + (profile.isEnabled() ? "" : "Disabled");
	}

	public UserLogonInfo getUserLogonInfo(Element assertionElement) {
				
		UserLogonInfo userLogonInfo = new UserLogonInfo();
		
		if(assertionElement == null){
			log.warn("assertionElement was null, returning empty UserLogonInfo");
			return userLogonInfo;
		}

		try {
			
			debugPrintAssertion(assertionElement);

			String instantString = (String) xPath.evaluate("/saml2:Assertion/saml2:AuthnStatement/@AuthnInstant", assertionElement, XPathConstants.STRING);
			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
			DateTime authnInstant = fmt.parseDateTime(instantString);
			int minutesOnline = Minutes.minutesBetween(authnInstant, new DateTime()).getMinutes();
			int hoursOnline = (int) minutesOnline / 60;
			minutesOnline = minutesOnline % 60;
			userLogonInfo.setTimeOnlineString(String.valueOf(hoursOnline) + ":" + (minutesOnline < 10 ? "0" : "") + String.valueOf(minutesOnline));

			String userLastName = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.SurName);
			String userFirstName =  SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.GivenName);
			String userAgency = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerName);
			
	    	String criminalJusticeEmployerIndicatorString = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.CriminalJusticeEmployerIndicator);
	    	userLogonInfo.setCriminalJusticeEmployerIndicator(BooleanUtils.toBoolean(criminalJusticeEmployerIndicatorString));
	    	String lawEnforcementEmployerIndicatorString = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.LawEnforcementEmployerIndicator);
	    	userLogonInfo.setLawEnforcementEmployerIndicator(BooleanUtils.toBoolean(lawEnforcementEmployerIndicatorString)); 
	    	userLogonInfo.setEmployerOri(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerORI));  

			String sEmail = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmailAddressText);

			userLogonInfo.setUserName((userFirstName == null ? "" : userFirstName) + " " + (userLastName == null ? "" : userLastName));
			userLogonInfo.setEmployer(userAgency);
			userLogonInfo.setUserNameString(StringUtils.join(Arrays.asList(userLogonInfo.getUserName(), userLogonInfo.getEmployer()), " / "));
			userLogonInfo.setEmailAddress(sEmail);
			userLogonInfo.setUserFirstName(userFirstName);
			userLogonInfo.setUserLastName(userLastName);
			
			String employerSubunitName = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerSubUnitName);
			userLogonInfo.setEmployerSubunitName(employerSubunitName);
			String federationId = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.FederationId);
			userLogonInfo.setFederationId(federationId);
			String identityProviderId = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.IdentityProviderId);
			userLogonInfo.setIdentityProviderId(identityProviderId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userLogonInfo;
	}

	@ModelAttribute("sensitiveInfoAlert")
	public Boolean getSensitiveInfoAlert() {
	    return sensitiveInfoAlert;
	}
	
    @ModelAttribute("sensitiveInfoAlertMessage")
    public String getSensitiveInfoAlertMessage() {
        return sensitiveInfoAlertMessage;
    }
    
	@ModelAttribute("races")
	public Map<String, String> getRaces() {
		return races;
	}
	
	@ModelAttribute("eyeColors")
	public Map<String, String> getEyeColors() {
		return eyeColors;
	}	
	
	@ModelAttribute("hairColors")
	public Map<String, String> getHairColors() {
		return hairColors;
	}

	@ModelAttribute("searchPurposes")
	public Map<String, String> getSearchPurposes() {
		return searchPurposes;
	}
    
    @ModelAttribute("subscriptionFilterProperties")
    public Map<String, String> getSubscriptionFilterProperties(){
    	return subscriptionFilterProperties;
    }            

	@ModelAttribute("leftMenuLinks")
	public Map<String, String> getLeftMenuLinks() {
		return leftMenuLinks;
	}

    @ModelAttribute("leftBarLinks")
    public Map<String, String> getLeftBarLinks()  {
        return leftBarLinks;
    }

    @ModelAttribute("leftBarTitles")
    public Map<String, String> getLeftBarTitles()  {
        return leftBarTitles;
    }
	
	@ModelAttribute("leftMenuLinkTitles")
	public Map<String, String> getLeftMenuLinkTitles() {
		return appProperties.getLeftMenuLinkTitles();
	}
	
	@ModelAttribute("subscriptionFilterValueToLabelMap")
	public Map<String, String> getSubscriptionFilterValueToLabelMap(){
		return subscriptionFilterValueToLabelMap;
	}
	
	@ModelAttribute("rapbackFilterOptionsMap")
	public Map<String, String> getRapbackFilterOptionsMap(){
	    return rapbackFilterOptionsMap;
	}

	public Map<String, String> getIncidentSystemsToQuery(Authentication authentication) {
		
		Map<String, String> incidentSystemsToQuery = new LinkedHashMap<>();
		incidentSystemsToQuery.putAll(systemsToQuery_incidents);
		if (appProperties.getRequireIncidentAccessControl() 
				&& appProperties.getPeopleSearchSourcesRequireIncidentAccess().size()>0) {
			boolean containsIncidentAccess = SecurityContextUtils.hasAuthority(authentication, Authorities.AUTHZ_INCIDENT_SEARCH_SOURCES);
			log.info("containsIncidentAccess: " + containsIncidentAccess);
			if (!containsIncidentAccess) {
				appProperties.getPeopleSearchSourcesRequireIncidentAccess()
					.stream()
					.map(item->StringUtils.substringAfter(item, "RMS - "))
					.forEach(incidentSystemsToQuery::remove);
			}
		}
		return incidentSystemsToQuery;
	}

	private void debugPrintAssertion(Element assertionElement) throws Exception{
		
		if(assertionElement == null){
			log.debug("assertionElement was null, skipping debug output");
			return;
		}
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(assertionElement);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		log.debug(xmlString);
	}
	
	private static final class Saml2NamespaceContext implements NamespaceContext {

		private Map<String, String> prefixToURIMap = new HashMap<String, String>();
		private Map<String, String> uriToPrefixMap = new HashMap<String, String>();

		public Saml2NamespaceContext() {
			prefixToURIMap.put("saml2", "urn:oasis:names:tc:SAML:2.0:assertion");
			uriToPrefixMap.put("urn:oasis:names:tc:SAML:2.0:assertion", "saml2");
		}

		@Override
		public String getNamespaceURI(String prefix) {
			return prefixToURIMap.get(prefix);
		}

		@Override
		public String getPrefix(String namespaceURI) {
			return uriToPrefixMap.get(namespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			ArrayList<String> prefixes = new ArrayList<String>();
			String prefix = uriToPrefixMap.get(namespaceURI);
			prefixes.add(prefix);
			return prefixes.iterator();
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
}
