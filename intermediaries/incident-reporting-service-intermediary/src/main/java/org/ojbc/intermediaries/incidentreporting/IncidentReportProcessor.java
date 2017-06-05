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
package org.ojbc.intermediaries.incidentreporting;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IncidentReportProcessor {

	private static final Log log = LogFactory
			.getLog(IncidentReportProcessor.class);

	private List<String> ndexSubmittingORIList;
	private List<String> chargeReferralORIList;
	
	//read in comma separated list of ORIs and write to an array
	public IncidentReportProcessor(String ndexAuthorizedORIs, String chargeReferralORIs) {
		
		if (StringUtils.isNotBlank(ndexAuthorizedORIs))
		{	
			ndexSubmittingORIList = Arrays.asList(ndexAuthorizedORIs.split(","));
		}
		
		if (StringUtils.isNotBlank(chargeReferralORIs))
		{	
			chargeReferralORIList = Arrays.asList(chargeReferralORIs.split(","));
		}	

	}

	//Determine if submitting agency is authorized to report to N-DEx
	public void confirmNdexAuthorizedOri(Exchange exchange, @Header("submittingORI") String submittingORI){

		boolean result = isOriInList(submittingORI, ndexSubmittingORIList);
		
		if (result)
		{	
			log.debug("ORI IS approved to submit to N-DEx: "+submittingORI);
		}	
		else
		{
			log.debug("ORI is NOT approved to submit to N-DEx: "+submittingORI);
		}	

		
		exchange.getIn().setHeader("callNDExSubmissionService", result);
			
	}

	//Determine if submitting agency is authorized to report to N-DEx
	public void confirmChargeReferralAuthorizedOri(Exchange exchange, @Header("submittingORI") String submittingORI){

		boolean result = isOriInList(submittingORI,chargeReferralORIList);
		
		if (result)
		{	
			log.debug("ORI IS approved to submit to Charge Referral: "+submittingORI);
		}	
		else
		{
			log.debug("ORI is NOT approved to submit to Charge Referral: "+submittingORI);
		}	
		
		exchange.getIn().setHeader("callChargeReferralService", result);
			
	}

	boolean isOriInList(String submittingORI, List<String> authorizedOris) {
		if (authorizedOris == null)
		{
			return false;
		}	
			
		//determine if submitting ORI exists in the list of authorized ORIs
		for(String s:authorizedOris){
			if(s.replaceAll("\\s","").equalsIgnoreCase(submittingORI)){
				return true;
			}
		}
		
		return false;
	}

	
}
