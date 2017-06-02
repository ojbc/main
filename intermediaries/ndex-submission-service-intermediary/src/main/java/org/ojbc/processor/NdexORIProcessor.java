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
package org.ojbc.processor;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NdexORIProcessor {

	private static final Log log = LogFactory.getLog(NdexORIProcessor.class);

	private List<String> ndexTestORIList;

	//read in comma separated list of ORIs and write to an array
	public NdexORIProcessor(String ndexTestORIs) {
		
		if (StringUtils.isNotBlank(ndexTestORIs))
		{	
			ndexTestORIList = Arrays.asList(ndexTestORIs.split(","));
		}
		
	}
	
	
	//Determine if data owner agency is authorized to report to N-DEx
	public void confirmNdexTestOri(Exchange exchange, @Header("AgencyORI") String submittingORI){

		boolean result = isOriInList(submittingORI,ndexTestORIList);
		
		if (result)
		{	
			log.debug("ORI IS a test ORI, prepend 'TEST' to filename: "+submittingORI);
		}	
		else
		{
			log.debug("ORI is NOT a test ORI: "+submittingORI);
		}	
		
		exchange.getIn().setHeader("ndexTestORI", result);
			
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
