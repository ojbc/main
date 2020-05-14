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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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

import org.apache.commons.io.IOUtils;
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
import org.ojbc.web.portal.controllers.dto.PersonFilterCommand;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.ojbc.web.portal.controllers.helpers.SamlTokenProcessor;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.rest.client.RestEnhancedAuditClient;
import org.ojbc.web.portal.services.OTPService;
import org.ojbc.web.portal.services.SamlService;
import org.ojbc.web.security.Authorities;
import org.ojbc.web.security.SecurityContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Element;

@Controller
@RequestMapping("/portal/*")
@SessionAttributes({"sensitiveInfoAlert", "userLogonInfo", "userSignOutUrl", "samlAssertion"})
public class PortalController implements ApplicationContextAware {

	@Resource (name="${otpServiceBean:OTPServiceMemoryImpl}")
	OTPService otpService;
	
	static final String DEFAULT_USER_TIME_ONLINE = "0:00";
	static final String DEFAULT_USER_LOGON_MESSAGE = "Not Logged In";
	
	public static final String STATE_LINK_ID = "stateGovLink";
    public static final String QUERY_LINK_ID = "queryLink";
	public static final String SUBSCRIPTIONS_LINK_ID = "subscriptionsLink";
	public static final String RAPBACK_LINK_ID = "rapbackLink";
	public static final String CRIMINAL_ID_LINK_ID = "criminalIdLink";
	private static final String ADMIN_LINK_ID = "adminLink";
	private static final String AUDIT_LINK_ID = "auditLink";
	public static final String HELP_LINK_ID = "helpLink";
	public static final String HELP_LINK_EXTERNAL_ID = "helpLinkExternal";
    public static final String PRIVACY_LINK_ID = "privacyPolicyLink";
    public static final String FAQ_LINK_ID = "faqLink";
    public static final String SUGGESTIONFORM_LINK_ID = "suggestionFormLink";
		
	public static final String STATE_LINK_TITLE = "State.gov";
    public static final String QUERY_LINK_TITLE = "Query";
	public static final String SUBSCRIPTION_LINK_TITLE = "Subscriptions";
	public static final String RAPBACK_LINK_TITLE = "Applicant Rap Back";
	public static final String CRIMINAL_ID_LINK_TITLE = "Criminal Identification";
	public static final String ADMIN_LINK_TITLE = "Admin";
	public static final String AUDIT_LINK_TITLE = "Audit";
	public static final String HELP_LINK_TITLE = "Help";
	public static final String PRIVACY_LINK_TITLE = "Privacy Policies";
	public static final String FAQ_LINK_TITLE = "FAQ";
	public static final String SUGGESTIONFORM_LINK_TITLE = "Suggestions/Report a Problem";
	
	private static final Log log = LogFactory.getLog(PortalController.class);


	private XPath xPath;
	
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
	Map<String, String> stateSpecificIncludes;
	
	@Resource
	Map<String, String> leftMenuLinks;
	
	Map<String, String> leftMenuLinkTitles;

    @Resource
	Map<String, String> leftBarLinks;

	Map<String, String> leftBarTitles;

    @Resource
    Map<String, String> stateSpecificHomePage;
    
    @Resource
    Map<String, String> subscriptionFilterProperties;
    
	@Resource
	Map<String, String> subscriptionFilterValueToLabelMap;
	
	@Value("#{getObject('rapbackFilterOptionsMap')}")
	Map<String, String> rapbackFilterOptionsMap;

	@Value("#{getObject('entityLogoutReturnUrlMap')}")
	Map<String, String> entityLogoutReturnUrlMap;
	
    @Value("${userSignOutUrl:defaultLogout}")
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

    @RequestMapping("landingPage")
    public String landingPage(){
	    return "portal/landingPage";
	}

    @RequestMapping("helpPage")
    public String helpPage(){
	    return "portal/helpPage";
	}

    @RequestMapping("faq")
    public String faq(){
	    return "portal/faq";
	}

    @RequestMapping("index")
    public void index(HttpServletRequest request, Map<String, Object> model, Authentication authentication) throws Exception{

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
		model.put("searchLinksHtml", getSearchLinksHtml(authentication));
		model.put("stateSpecificInclude_preBodyClose", getStateSpecificInclude("preBodyClose"));
		
		if (model.get("sensitiveInfoAlert") == null) {
			model.put("sensitiveInfoAlert", sensitiveInfoAlert);
		}
		
    	putUserSignoutUrlAndSamlAssertionIntoModel(request, model);
	}

	private void putUserSignoutUrlAndSamlAssertionIntoModel(HttpServletRequest request, Map<String, Object> model)
			throws Exception {
		StringBuilder sb = new StringBuilder();
    	sb.append(userSignOutUrl);
    	
    	Element samlAssertion = (Element)request.getAttribute("samlAssertion");
    	
    	if (entityLogoutReturnUrlMap != null){
    		
    		String samlTokenIssuer = XmlUtils.xPathStringSearch( samlAssertion, "/saml2:Assertion/saml2:Issuer");
    		
    		log.info("Test login with the new jquery click");
    		log.info("Saml Token Issuer: " + samlTokenIssuer);
    		
    		String logoutReturnUrl = entityLogoutReturnUrlMap.get(samlTokenIssuer);
    		
    		log.info("Logout return URL: " + logoutReturnUrl);
    		
    		if (StringUtils.isNotBlank(logoutReturnUrl)){
    			sb.append("?return=");
    			sb.append(logoutReturnUrl);
    		}
    	}
    	model.put("userSignOutUrl", sb.toString());
    	model.put("samlAssertion", samlAssertion);
	}

    @RequestMapping("performLogout")
    public String performLogout(HttpServletRequest request, Map<String, Object> model) throws Exception{
    	String userSignoutUrl = (String) model.get("userSignOutUrl");
    	
    	log.info("User Signout URL: " + userSignoutUrl);
    	
		Element samlAssertion =  (Element) model.get("samlAssertion");
		
		//Enhanced audit logout here
		if (enableEnhancedAudit)
		{
        	try {
				String employerName = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerName);
				String federationId = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.FederationId);
				String employerSubunitName = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmployerSubUnitName);
				String firstName = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.GivenName);
				String lastName = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.SurName);
				String emailAddress = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);
				String identityProviderId = SamlTokenProcessor.getAttributeValue(samlAssertion, SamlAttribute.IdentityProviderId);
				
				restEnhancedAuditClient.auditUserLogout(federationId, employerName, employerSubunitName, firstName, lastName, emailAddress, identityProviderId);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Unable to audit user logout");
			}
		}	
		
    	if (request.getSession()!= null){
    		
    		if (otpService != null)
    		{
    			//Send OTP as soon as the input form is shown
    			
    			if (samlAssertion != null)
    			{	
	    			String userEmail = XmlUtils.xPathStringSearch(samlAssertion, "/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue/text()");
	
	    			log.info("User email address to remove OTP authentication: " + userEmail);
	    			
	    			if (StringUtils.isNotBlank(userEmail) && requireOtpAuthentication)
	    			{	
	    				log.info("Unauthenticate user.");
	    				otpService.unauthenticateUser(userEmail);
	    			}	
    			}	
    		}	
    		
    		model.remove("userSignOutUrl");
    		model.remove("sensitiveInfoAlert");
    		request.getSession().invalidate();
    	}
		new SecurityContextLogoutHandler().logout(request, null, null);
    	return "redirect:" + userSignoutUrl;
	}

    @RequestMapping("defaultLogout")
    public String defaultLogout(HttpServletRequest request, Map<String, Object> model, Authentication authentication){
    	
    	return "portal/logout";
    }
    

    @RequestMapping("landingLeftBar")
    public String landingLeftBar(){
    	return "common/_landingLeftBar";
    }

    @RequestMapping(value="subscriptionsLeftBar", method=RequestMethod.POST)
    public String subscriptionsLeftBar(HttpServletRequest request, 
    		@ModelAttribute("subscriptionFilterCommand") SubscriptionFilterCommand subscriptionFilterCommand, 
    		Map<String, Object> model){   
    	
    	return "common/_subscriptionsLeftBar";
    }
    
    @RequestMapping(value="rapbackLeftBar", method=RequestMethod.POST)
    public String rapbackLeftBar(Map<String, Object> model){   
        return "common/_rapbackLeftBar";
    }
    
    @RequestMapping(value="criminalIdentificationLeftBar", method=RequestMethod.POST)
    public String criminalIdentificationLeftBar(Map<String, Object> model){   
    	return "common/_criminalIdentificationLeftBar";
    }
    
	@RequestMapping(value="leftBar", method=RequestMethod.POST)
       public String leftBar(HttpServletRequest request, @ModelAttribute("personFilterCommand") PersonFilterCommand personFilterCommand, 
			Map<String, Object> model){
    	model.put("showReasonsForSearch", showReasonsForSearch);
    	model.put("showDemographicsFilter", showDemographicsFilter);
    	
	    return "common/_leftBar";
	}
    
    @RequestMapping(value="negateSenstiveInfoAlert", method=RequestMethod.POST)
    public @ResponseBody String negateSenstiveInfoAlert( Map<String, Object> model ){
        model.put("sensitiveInfoAlert", false); 
        return "success";
    }

	private String getStateSpecificInclude(String includeKey) {
		String includeFileName = stateSpecificIncludes.get(includeKey);
		if (StringUtils.isNotBlank(includeFileName)) {
			org.springframework.core.io.Resource preBodyClose = applicationContext.getResource("classpath:" + includeFileName);
			try {
				InputStream inputStream = preBodyClose.getInputStream();
				return IOUtils.toString(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private List<SearchProfile> getActiveSearchProfiles(Authentication authentication) {
		List<SearchProfile> visibleProfiles = new ArrayList<SearchProfile>();
		
		addProfileToReturnListIfVisible(visibleProfiles, "people", "Person Search");
		
		if (authentication == null || SecurityContextUtils.hasAuthority(authentication, Authorities.AUTHZ_INCIDENT_DETAIL)){
			addProfileToReturnListIfVisible(visibleProfiles, "incident", "Incident Search");
			addProfileToReturnListIfVisible(visibleProfiles, "vehicle", "Vehicle Search");
		}
		addProfileToReturnListIfVisible(visibleProfiles, "firearm", "Firearm Search");
		
		return visibleProfiles;
	}

	private void addProfileToReturnListIfVisible(List<SearchProfile> enabledProfiles, String profileId, String displayName) {
		Boolean enabled = visibleProfileStateMap.get(searchProfilesEnabled.get(profileId));
		if (enabled != null) {
			enabledProfiles.add(new SearchProfile(profileId, displayName, enabled));
		}
	}
	
	String getSearchLinksHtml(Authentication authentication) {
		StringBuilder links = new StringBuilder();

		for(SearchProfile profile : getActiveSearchProfiles(authentication)) {
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

			String userLastName = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:SurName']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
			String userFirstName = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:GivenName']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
			String userAgency = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmployerName']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
			
	    	String criminalJusticeEmployerIndicatorString = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.CriminalJusticeEmployerIndicator);
	    	userLogonInfo.setCriminalJusticeEmployerIndicator(BooleanUtils.toBoolean(criminalJusticeEmployerIndicatorString));
	    	String lawEnforcementEmployerIndicatorString = SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.LawEnforcementEmployerIndicator);
	    	userLogonInfo.setLawEnforcementEmployerIndicator(BooleanUtils.toBoolean(lawEnforcementEmployerIndicatorString)); 
	    	userLogonInfo.setEmployerOri(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerORI));  

			String sEmail = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);

			userLogonInfo.setUserName((userFirstName == null ? "" : userFirstName) + " " + (userLastName == null ? "" : userLastName));
			userLogonInfo.setEmployer(userAgency);
			userLogonInfo.setUserNameString(StringUtils.join(Arrays.asList(userLogonInfo.getUserName(), userLogonInfo.getEmployer()), " / "));
			userLogonInfo.setEmailAddress(sEmail);
			userLogonInfo.setUserFirstName(userFirstName);
			userLogonInfo.setUserLastName(userLastName);
			
			String employerSubunitName = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmployerSubUnitName']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
			userLogonInfo.setEmployerSubunitName(employerSubunitName);
			String federationId = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
			userLogonInfo.setFederationId(federationId);
			String identityProviderId = (String) xPath.evaluate("/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:IdentityProviderId']/saml2:AttributeValue/text()", assertionElement,
					XPathConstants.STRING);
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

    @ModelAttribute("stateSpecificHomePage")
    public Map<String, String> getStateSpecificHomePage() {
    	return stateSpecificHomePage;
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
		
		if(leftMenuLinkTitles == null){			
			leftMenuLinkTitles = new HashMap<String, String>();
			leftMenuLinkTitles.put(STATE_LINK_ID, STATE_LINK_TITLE);
			leftMenuLinkTitles.put(QUERY_LINK_ID, QUERY_LINK_TITLE);
			leftMenuLinkTitles.put(SUBSCRIPTIONS_LINK_ID, SUBSCRIPTION_LINK_TITLE);
			leftMenuLinkTitles.put(RAPBACK_LINK_ID, RAPBACK_LINK_TITLE);
			leftMenuLinkTitles.put(CRIMINAL_ID_LINK_ID, CRIMINAL_ID_LINK_TITLE);
			leftMenuLinkTitles.put(ADMIN_LINK_ID, ADMIN_LINK_TITLE);
			leftMenuLinkTitles.put(AUDIT_LINK_ID, AUDIT_LINK_TITLE);
			leftMenuLinkTitles.put(HELP_LINK_ID, HELP_LINK_TITLE);
			leftMenuLinkTitles.put(HELP_LINK_EXTERNAL_ID, HELP_LINK_TITLE);
			leftMenuLinkTitles.put(PRIVACY_LINK_ID, PRIVACY_LINK_TITLE);
			leftMenuLinkTitles.put(FAQ_LINK_ID, FAQ_LINK_TITLE);
			leftMenuLinkTitles.put(SUGGESTIONFORM_LINK_ID, SUGGESTIONFORM_LINK_TITLE);
		}		
		return leftMenuLinkTitles;
	}
	
	@ModelAttribute("subscriptionFilterValueToLabelMap")
	public Map<String, String> getSubscriptionFilterValueToLabelMap(){
		return subscriptionFilterValueToLabelMap;
	}
	
	@ModelAttribute("rapbackFilterOptionsMap")
	public Map<String, String> getRapbackFilterOptionsMap(){
	    return rapbackFilterOptionsMap;
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
