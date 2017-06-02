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
package org.ojbc.intermediaries.identificationreporting;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IdentificationReportProcessor {

	private static final Log log = LogFactory
			.getLog(IdentificationReportProcessor.class);

	private static final String IDENTIFICATION_RECORDING_OPERATION_NAMESPACE=
			"http://ojbc.org/Services/WSDL/IdentificationRecordingService/1.0";
	private static final List<String> operationNames = 
			Arrays.asList(new String[]{"ReportPersonStateIdentificationRequest","ReportPersonStateIdentificationResults", "ReportPersonFederalIdentificationRequest", "ReportPersonFederalIdentificationResults"});
	
	public void setOperationNameAndNamespace(Exchange exchange, @Header("operationName") String operationName){

		boolean result = operationNames.contains(operationName);
		
		if (result){	
			log.debug("Received request with correct operation Name: "+operationName);
			exchange.getIn().setHeader("operationName", operationName.replace("Report", "Record"));
			exchange.getIn().setHeader("operationNamespace", IDENTIFICATION_RECORDING_OPERATION_NAMESPACE);
		}	
		else{
			log.info("The operation Name '" +StringUtils.trimToEmpty(operationName) +  "' is not correct, the message will not be forwarded." );
		}	

		exchange.getIn().setHeader("callIdentificationRecordingService", result);
			
	}
	
}
