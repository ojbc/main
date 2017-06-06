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
package org.ojbc.util.fedquery.error;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IncidentSearchErrorProcessor {
		
	private static enum ERROR_TYPE{
		PERSON_TO_INCIDENT,
		VEHICLE_TO_INCIDENT
	}
			
	public static String returnPersonToIncidentErrorMessage(){

		String errorMsg = null;
		try {
			errorMsg = getErrorMessage(ERROR_TYPE.PERSON_TO_INCIDENT); 	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorMsg;
	}
	
	public static String returnVehicleToIncidentErrorMessage(){
		
		String errorMsg = null;
		
		try {
			errorMsg = getErrorMessage(ERROR_TYPE.VEHICLE_TO_INCIDENT); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorMsg;		
	}
	
	private static String getErrorMessage(ERROR_TYPE errorType) throws Exception{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db =  dbf.newDocumentBuilder();
		Document rDoc = db.newDocument();
						
		Element rootElement = null;
		
		if(ERROR_TYPE.PERSON_TO_INCIDENT == errorType){			
			
	        rootElement = rDoc.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC, 
	        		"IncidentPersonSearchResults");
			
		}else if(ERROR_TYPE.VEHICLE_TO_INCIDENT == errorType){
			
	        rootElement = rDoc.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC, 
	        		"IncidentVehicleSearchResults");
		}else{
			throw new Exception("Unknown root element");
		}
			
		rootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_INCIDENT_SEARCH_RESULTS_DOC);
        
        rDoc.appendChild(rootElement);
        
        Element searchResultsMetaDataElement = XmlUtils.appendElement(rootElement, 
        		OjbcNamespaceContext.NS_SEARCH_RESULTS_METADATA_EXT, "SearchResultsMetadata");
        
        Element searchReqErrorElement = XmlUtils.appendElement(searchResultsMetaDataElement, 
        		OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchRequestError");
        
        Element errorTxtElement = XmlUtils.appendElement(searchReqErrorElement, 
        		OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING, "ErrorText");
        
        errorTxtElement.setTextContent("The source systems timed out or had an error condition.  Please try your search again later.");

        Element intelElement = XmlUtils.appendElement(searchReqErrorElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
        intelElement.setTextContent("All Systems");
				
        String sErrorMessageDoc = OJBUtils.getStringFromDocument(rDoc);
        
		return sErrorMessageDoc;				
	}
	
}
