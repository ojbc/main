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
