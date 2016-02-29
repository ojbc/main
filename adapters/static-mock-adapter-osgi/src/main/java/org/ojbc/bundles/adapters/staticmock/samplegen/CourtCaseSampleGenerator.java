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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.dom.DocumentBuilderFactoryImpl;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CourtCaseSampleGenerator extends AbstractSampleGenerator {
	
	private static final String CURRENT_DATE = DateTime.now().toString("yyyy-MM-dd");
	
	public CourtCaseSampleGenerator() throws ParserConfigurationException,
			IOException {
		
		super();
	}

	public List<Document> generateCourtCaseSamples(int recordCount) throws IOException, ParserConfigurationException{
		
		List<Document> rCourtCaseDocList = new ArrayList<Document>(recordCount);
		
		for(int i=0; i<recordCount; i++){
			
			PersonElementWrapper iPerson = getRandomIdentity(null);
			
			Document courtCaseDoc = buildCourtCaseDetailDoc(iPerson);
			
			rCourtCaseDocList.add(courtCaseDoc);
		}
		
		return null;
	}
	
	
	
	
	Document buildCourtCaseDetailDoc(PersonElementWrapper person) throws ParserConfigurationException{
			
		Document rCourtCaseDetailDoc = getNewDocument();
		
		Element rootCourtCaseElement = rCourtCaseDetailDoc.createElementNS(OjbcNamespaceContext.NS_COURT_CASE_QUERY_RESULTS_EXCH_DOC, "CourtCaseQueryResults");
		rootCourtCaseElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_COURT_CASE_QUERY_RESULTS_EXCH_DOC);

		rCourtCaseDetailDoc.appendChild(rootCourtCaseElement);
				
		Element docCreateDateElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");		
		
		Element docCreateDateValElement = XmlUtils.appendElement(docCreateDateElement, OjbcNamespaceContext.NS_NC_30, "DateTime");
		docCreateDateValElement.setTextContent(CURRENT_DATE);

		Element docIdElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "DocumentIdentification");
		Element docIdValElement = XmlUtils.appendElement(docIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		docIdValElement.setTextContent("TODO");
		
		Element sysIdElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_INTEL_31, "SystemIdentification");		
		
		Element sysIdValElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		sysIdValElement.setTextContent("TODO");
		
		Element sysNameElement = XmlUtils.appendElement(sysIdElement, OjbcNamespaceContext.NS_NC_30, "SystemName");
		sysNameElement.setTextContent("TODO");
		
		
		
		Element caseElement = XmlUtils.appendElement(rootCourtCaseElement, OjbcNamespaceContext.NS_NC_30, "Case"); 		
		XmlUtils.addAttribute(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Case_01");
		
		Element activIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ActivityIdentification");
		Element activIdValElement = XmlUtils.appendElement(activIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleCaseId = RandomStringUtils.randomNumeric(8);		
		activIdValElement.setTextContent(sampleCaseId);
		
		Element activStatusElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "ActivityStatus");		
		Element actStatDescTxtElement = XmlUtils.appendElement(activStatusElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");		
		actStatDescTxtElement.setTextContent("TODO");
		
		Element caseDispElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseDisposition");
		Element dispDateElement = XmlUtils.appendElement(caseDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDate");
		Element caseDispDatValElement = XmlUtils.appendElement(dispDateElement, OjbcNamespaceContext.NS_NC_30, "Date");
		caseDispDatValElement.setTextContent(CURRENT_DATE);
		
		Element caseGenCatTxtElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseGeneralCategoryText");
		caseGenCatTxtElement.setTextContent("TODO");
		
		Element caseTrackIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseTrackingID");
		String sampleCaseTrackId = RandomStringUtils.randomNumeric(8);
		caseTrackIdElement.setTextContent(sampleCaseTrackId);
		
		Element caseDocketIdElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseDocketID");
		caseDocketIdElement.setTextContent("TODO");
		
		Element caseFilingElement = XmlUtils.appendElement(caseElement, OjbcNamespaceContext.NS_NC_30, "CaseFiling");		
		Element caseFileDocCreateDate = XmlUtils.appendElement(caseFilingElement, OjbcNamespaceContext.NS_NC_30, "DocumentCreationDate");		
		Element caseFileDocCreateDateValElement = XmlUtils.appendElement(caseFileDocCreateDate, OjbcNamespaceContext.NS_NC_30, "DateTime"); 
		caseFileDocCreateDateValElement.setTextContent(CURRENT_DATE);
		
		
		Element caseAugmentElement = XmlUtils.appendElement(caseFilingElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAugmentation");		
		Element caseAmendChargeElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseAmendedCharge");
		
		Element chargeCountElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity"); 		
		String sampleChargeCount = RandomStringUtils.randomNumeric(2);		
		chargeCountElement.setTextContent(sampleChargeCount);
		
		
		Element chargeDescTxtElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");		
		String sampleChargeDesc = getRandomString("TODO", "Speeding");		
		chargeDescTxtElement.setTextContent(sampleChargeDesc);
				
		Element chargeFilingDateElement = XmlUtils.appendElement(caseAmendChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeFilingDate");		
		Element chargeFilingDateValElement = XmlUtils.appendElement(chargeFilingDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeFilingDateValElement.setTextContent("TODO");
		
		Element chargeStatuteElement = XmlUtils.appendElement(chargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element chargeStatuteCodeIdElement = XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification");
		
		Element statCodeIdValElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleChargeStatuteId = RandomStringUtils.randomNumeric(6);	
		statCodeIdValElement.setTextContent(sampleChargeStatuteId);
		
		Element chargeStatIdCatDescTxtElement = XmlUtils.appendElement(chargeStatuteCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationCategoryDescriptionText");
		chargeStatIdCatDescTxtElement.setTextContent("TODO");
		
		Element caseChargeElement = XmlUtils.appendElement(caseAugmentElement, OjbcNamespaceContext.NS_JXDM_51, "CaseCharge");
		
		Element caseChargeCountElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeCountQuantity");	
		String sampleCaseChargeCount = RandomStringUtils.randomNumeric(1);		
		caseChargeCountElement.setTextContent(sampleCaseChargeCount);
		
		Element caseChargeDescTxtElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDescriptionText");
		caseChargeDescTxtElement.setTextContent("TODO");
		
		Element chargeDispElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDisposition");
		
		Element chargeDispDateElement = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDate");		
		Element chargeDispDateValElement = XmlUtils.appendElement(chargeDispDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeDispDateValElement.setTextContent(CURRENT_DATE);
		
		
		Element chargeDispDescTxt = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_NC_30, "DispositionDescriptionText");		
		chargeDispDescTxt.setTextContent("TODO");
		
		Element chargeDispOtherTxtElement = XmlUtils.appendElement(chargeDispElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeDispositionOtherText");
		chargeDispOtherTxtElement.setTextContent("TODO");
		
		Element caseChargeFilingDateElement = XmlUtils.appendElement(caseChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeFilingDate");		
		Element caseChargeFilingDateValElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		caseChargeFilingDateValElement.setTextContent(CURRENT_DATE);
		
		Element chargePleaElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargePlea");
		
		Element chargePleaActivityDateElement = XmlUtils.appendElement(chargePleaElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");		
		Element chargePleaActivityDateValElement = XmlUtils.appendElement(chargePleaActivityDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargePleaActivityDateValElement.setTextContent(CURRENT_DATE);
		
		Element pleaCatCodeElement = XmlUtils.appendElement(chargePleaElement, OjbcNamespaceContext.NS_JXDM_51, "PleaCategoryCode");		
		String samplePleaCatCode = RandomStringUtils.randomAlphabetic(1);		
		pleaCatCodeElement.setTextContent(samplePleaCatCode);
		
		Element chargeSentenceElement = XmlUtils.appendElement(caseChargeFilingDateElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeSentence");
		
		Element chargeSentActivityIdElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityIdentification");
		
		Element chargeSentActivIdValElement = XmlUtils.appendElement(chargeSentActivityIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		
		String sampleChargeActivId = RandomStringUtils.randomNumeric(8);		
		chargeSentActivIdValElement.setTextContent(sampleChargeActivId);
		
		Element chargeSentenceActivDateElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");		
		Element chargeSentActivDateValElement = XmlUtils.appendElement(chargeSentenceActivDateElement, OjbcNamespaceContext.NS_NC_30, "Date");		
		chargeSentActivDateValElement.setTextContent(CURRENT_DATE);
				
		Element chargeSentActivStatElement= XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_NC_30, "ActivityStatus");		
		Element chargeSentActivStatDescTxtElement = XmlUtils.appendElement(chargeSentActivStatElement, OjbcNamespaceContext.NS_NC_30, "StatusDescriptionText");		
		chargeSentActivStatDescTxtElement.setTextContent("TODO");
		
		Element chargeSentenceChargeElement = XmlUtils.appendElement(chargeSentenceElement, OjbcNamespaceContext.NS_JXDM_51, "SentenceCharge");		
		Element sentChargeStatElement = XmlUtils.appendElement(chargeSentenceChargeElement, OjbcNamespaceContext.NS_JXDM_51, "ChargeStatute");
		
		Element sentStatCodeIdElement = XmlUtils.appendElement(sentChargeStatElement, OjbcNamespaceContext.NS_JXDM_51, "StatuteCodeIdentification"); 
		
		Element sentStatCodeIdValElement = XmlUtils.appendElement(sentStatCodeIdElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID");		
		String sampleSentStatId = RandomStringUtils.randomNumeric(8);		
		sentStatCodeIdValElement.setTextContent(sampleSentStatId);
		
		
		
		
		
		// TODO build document
		
		OjbcNamespaceContext ojbcNamespaceContext = new OjbcNamespaceContext();
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(rootCourtCaseElement);
		
		return rCourtCaseDetailDoc;
	}
	
	
	private Document getNewDocument() throws ParserConfigurationException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactoryImpl.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();		

		Document doc = docBuilder.newDocument();
		
		return doc;
	}	

}
