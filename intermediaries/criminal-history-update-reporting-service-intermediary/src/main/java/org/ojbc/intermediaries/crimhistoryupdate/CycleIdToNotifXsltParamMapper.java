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
package org.ojbc.intermediaries.crimhistoryupdate;

import java.util.Map;

import org.apache.camel.Exchange;

/**
 * This class will read a map and set Camel headers accordingly.
 * The Camel Headers are used as parameters for XSLT calls.
 * 
 */
public class CycleIdToNotifXsltParamMapper {
	
	private Map<String, String> cycleIdToNotifXsltParamMap;
			
	public void populateHeaderWithXsltParams(Exchange exchange) throws Exception {
		
		for(String paramNameForXslt : cycleIdToNotifXsltParamMap.keySet()){
					
			String valueOfXsltParam = cycleIdToNotifXsltParamMap.get(paramNameForXslt);
			
			exchange.getIn().setHeader(paramNameForXslt, valueOfXsltParam);
		}
		
	}

	public Map<String, String> getCycleIdToNotifXsltParamMap() {
		return cycleIdToNotifXsltParamMap;
	}

	public void setCycleIdToNotifXsltParamMap(
			Map<String, String> cycleIdToNotifXsltParamMap) {
		this.cycleIdToNotifXsltParamMap = cycleIdToNotifXsltParamMap;
	}	

}
