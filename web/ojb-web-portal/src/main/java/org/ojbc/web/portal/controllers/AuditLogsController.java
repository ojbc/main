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
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.OJBCDateUtils;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

import com.google.common.base.Objects;

@Controller
@Profile({"audit-search","standalone"})
@SessionAttributes({ "userAuthenticationSearchRequest", "userAuthenticationSearchResponses"})
@RequestMapping("/auditLogs")
public class AuditLogsController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Resource
	PeopleControllerConfigInterface peopleQueryConfig;

	@Resource
	RestEnhancedAuditClient restEnhancedAuditClient;
	
	@Resource
	SubscriptionsControllerConfigInterface subConfig;
	
    @Value("${auditSearchDateRange:30}")
    Integer auditSearchDateRange;

    @ModelAttribute
    public void addModelAttributes(Model model) {
    	UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
    	model.addAttribute("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
	}

	private UserAuthenticationSearchRequest initUserAuthenticationSearchRequest() {
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
    	userAuthenticationSearchRequest.setEndTime(LocalDateTime.now());
    	userAuthenticationSearchRequest.setStartTime(LocalDateTime.now().minusDays(auditSearchDateRange));
		return userAuthenticationSearchRequest;
	}
    
	@RequestMapping(value = "searchForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(value = "resetForm", required = false) boolean resetForm,
	        Map<String, Object> model) {

		if (resetForm) {
			UserAuthenticationSearchRequest userAuthenticationSearchRequest = initUserAuthenticationSearchRequest();
			model.put("userAuthenticationSearchRequest", userAuthenticationSearchRequest);
		} 

		return "auditLogs/_searchForm";
	}

}

