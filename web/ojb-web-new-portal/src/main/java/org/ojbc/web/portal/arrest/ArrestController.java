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
package org.ojbc.web.portal.arrest;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

@Controller
@RequestMapping("/arrests")
public class ArrestController {
	private static final Log log = LogFactory.getLog(ArrestController.class);

	@Autowired
	ArrestService arrestService;
	
	@Resource
	SamlService samlService;
	
	@GetMapping("/lookupForm")
	public String getArrestLookupForm(HttpServletRequest request, Map<String, Object> model) throws Throwable {
		log.info("presenting Arrest Lookup Form ");
		return "arrest/arrestLookupForm::arrestLookupForm";
	}

	@PostMapping("/lookup")
	public @ResponseBody String lookup(HttpServletRequest request, String otn, Map<String, Object> model) throws Throwable {
		log.info("look up OTN: " + otn);
		String response = arrestService.lookupOtn(otn, samlService.getSamlAssertion(request)); 
		String responseMessage = getResponseMessage(otn, response); 
		return responseMessage;
	}

	private String getResponseMessage(String otn, String response) throws Exception {
		Document responseDocument = XmlUtils.toDocument(response); 
		Boolean recordFound =  BooleanUtils.toBoolean(XmlUtils.xPathStringSearch(responseDocument, "/rr-resp-doc:/RecordReplicationResponse/rr-resp-ext:RecordFoundIndicator"));
		
		StringBuilder sb = new StringBuilder(); 
		if (recordFound) {
			sb.append("Retrieval of the arrest "); 
			sb.append(otn); 
			sb.append("has been requested, please check back shortly to view the arrest");
		}
		else {
			sb.append("Unable to locate OTN "); 
			sb.append(otn); 
			sb.append(", please try another OTN");
		}
		
		String responseMessage = sb.toString();
		return responseMessage;
	}
	


}