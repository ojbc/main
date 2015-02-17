package org.ojbc.util.fedquery.error;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IncidentSearchErrorProcessor {
		
	private enum ERROR_TYPE{
		PERSON_TO_INCIDENT,
		VEHICLE_TO_INCIDENT
	}
		
	public static Document getErrorMessage(ERROR_TYPE errorType) throws ParserConfigurationException{
				
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db =  dbf.newDocumentBuilder();
		Document rDoc = db.newDocument();
						
		String sRootElement = null;
		
		if(ERROR_TYPE.PERSON_TO_INCIDENT == errorType){			
			sRootElement = "IncidentPersonSearchResults";
			
		}else if(ERROR_TYPE.VEHICLE_TO_INCIDENT == errorType){
			sRootElement = "IncidentVehicleSearchResults";
		}
				
        Element rootElement = rDoc.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC, 
        		sRootElement);
        
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
				
		return rDoc;				
	}
	
	public static String returnPersonToIncidentErrorMessage()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:IncidentPersonSearchResults");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0\"");
		sb.append("		xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\"");
		sb.append("		xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\"");
		sb.append("		xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\">");
		sb.append("		<srm:SearchResultsMetadata>");
		sb.append("			<srer:SearchRequestError>");
		sb.append("				<srer:ErrorText>The source systems timed out or had an error condition.  Please try your search again later.</srer:ErrorText>");
		sb.append("				<intel:SystemName>All Systems</intel:SystemName>");
		sb.append("			</srer:SearchRequestError>");
		sb.append("		</srm:SearchResultsMetadata>");
		sb.append("	</exchange:IncidentPersonSearchResults>");

		return sb.toString();
	}
	
	public static String returnVehicleToIncidentErrorMessage()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exchange:IncidentVehicleSearchResults");
		sb.append("	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0\"");
		sb.append("		xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\"");
		sb.append("		xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\"");
		sb.append("		xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\">");
		sb.append("		<srm:SearchResultsMetadata>");
		sb.append("			<srer:SearchRequestError>");
		sb.append("				<srer:ErrorText>The source systems timed out or had an error condition.  Please try your search again later.</srer:ErrorText>");
		sb.append("				<intel:SystemName>All Systems</intel:SystemName>");
		sb.append("			</srer:SearchRequestError>");
		sb.append("		</srm:SearchResultsMetadata>");
		sb.append("	</exchange:IncidentVehicleSearchResults>");

		return sb.toString();
	}
	
}
