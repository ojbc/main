package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodySampleGenerator extends AbstractSampleGenerator{

	public CustodySampleGenerator() throws ParserConfigurationException,
			IOException {

		super();
	}
	
	
	public List<Document> generateCustodySamples(int recordCount) throws Exception {
		
		List<Document> rCustodyDocList = new ArrayList<Document>(recordCount);
		
		for(int i=0; i < recordCount; i++){
		
			PersonElementWrapper iGeneratedPerson = getRandomIdentity(null);
			
			Document custodyDoc = buildCustodyDetailDoc(iGeneratedPerson);
			
			rCustodyDocList.add(custodyDoc);
		}
		
		return rCustodyDocList;
	}
	
	
	Document buildCustodyDetailDoc(PersonElementWrapper person) throws ParserConfigurationException{
		
		Document rCustodyDetailDoc = getNewDocument();
		
		Element rootCustodyResultsElement = rCustodyDetailDoc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXCH_DOC, "CustodyQueryResults"); 
		
		rootCustodyResultsElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CUSTODY_QUERY_RESULTS_EXCH_DOC);
		
		rCustodyDetailDoc.appendChild(rootCustodyResultsElement);
		
		Element docCreationDateElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");
		
		Element dateTimeElement = XmlUtils.appendElement(docCreationDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		dateTimeElement.setTextContent("TODO");
		
		Element documentIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
		
		Element docIdValElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		docIdValElement.setTextContent("TODO");
		
		Element idCatDescTxtElement = XmlUtils.appendElement(documentIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		
		idCatDescTxtElement.setTextContent("TODO");
		
		Element sysIdElement = XmlUtils.appendElement(rootCustodyResultsElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");
		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_INTEL_31, "IdentificationID");
		
		sysIdValElement.setTextContent("TODO");
		
		Element sysIdSystemNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
		
		sysIdSystemNameElement.setTextContent("TODO");
		
		
		Element inmateCustodyElement = XmlUtils.appendElement(rootCustodyResultsElement, OJBC_NAMESPACE_CONTEXT.NS_CUSTODY_QUERY_RESULTS_EXT, "InmateCustody");
		
		Element personBirthDateElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		
		Element personDobValElement = XmlUtils.appendElement(personBirthDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		
		personDobValElement.setTextContent("TODO");
		
		
		Element ethnicityElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonEthnicityText");
		
		ethnicityElement.setTextContent("TODO");
		
		
		Element personNameElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
		
		Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");		
		personGivenNameElement.setTextContent("TODO");
		
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");		
		personMiddleNameElement.setTextContent("TODO");
		
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
		personSurNameElement.setTextContent("TODO");
		
		Element personRaceElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonRaceText");
		personRaceElement.setTextContent("TODO");
		
		Element personSexElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSexText");
		personSexElement.setTextContent("TODO");
		
		Element personSsnElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");		
		Element personSsnValElement = XmlUtils.appendElement(personSsnElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSsnValElement.setTextContent("TODO");
		
		Element personSidElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_NC_30, "PersonStateIdentification");		
		Element personSidValElement = XmlUtils.appendElement(personSidElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		personSidValElement.setTextContent("TODO");
		
		Element bailBondElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_JXDM_51, "BailBond");
		
		Element activityCatElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_NC_30, "ActivityCategoryText");
		activityCatElement.setTextContent("TODO");
		
		Element bailBondAmountElement = XmlUtils.appendElement(bailBondElement, OjbcNamespaceContext.NS_JXDM_51, "BailBondAmount");
		
		Element bailBondAmountValElement = XmlUtils.appendElement(bailBondAmountElement, OjbcNamespaceContext.NS_NC_30, "Amount");
		
		bailBondAmountValElement.setTextContent("TODO");
		
		
		Element bookingElement = XmlUtils.appendElement(inmateCustodyElement, OjbcNamespaceContext.NS_CUSTODY_QUERY_RESULTS_EXT, "Booking");
		
		Element fingerprintDateElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "FingerprintDate");
		
		Element fingerprintDateValElement = XmlUtils.appendElement(fingerprintDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");		
		fingerprintDateValElement.setTextContent("TODO");
		
		
		Element bookingSubjectElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_JXDM_51, "BookingSubject");
		
		Element bookingSubjectIdElement = XmlUtils.appendElement(bookingSubjectElement, OjbcNamespaceContext.NS_JXDM_51, "SubjectIdentification");
		
		Element bookingSubjectIdValElement = XmlUtils.appendElement(bookingSubjectIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		bookingSubjectIdValElement.setTextContent("TODO");
		
		
		Element imageElement = XmlUtils.appendElement(bookingElement, OjbcNamespaceContext.NS_NC_30, "Image");
		
		Element imgLocElement = XmlUtils.appendElement(imageElement, OjbcNamespaceContext.NS_NC_30, "ImageLocation");
		
		Element imgLocDescElement = XmlUtils.appendElement(imgLocElement, OjbcNamespaceContext.NS_NC_30, "LocationDescriptionText");
		
		imgLocDescElement.setTextContent("TODO");
		
		
		
		
		
		
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootCustodyResultsElement);
		
		return rCustodyDetailDoc;
	}
	
	
	private Document getNewDocument() throws ParserConfigurationException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();		

		Document doc = docBuilder.newDocument();
		
		return doc;
	}

}



