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
package org.ojbc.adapters.rapbackdatastore.processor;

import java.io.IOException;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class IdentificationResultsReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( IdentificationResultsReportProcessor.class );
	
	@Transactional
	public void processReport(@Body Document report, Exchange exchange) throws Exception
	{
		log.info("Processing Identification Results Report.");
		
		Node rootNode = XmlUtils.xPathNodeSearch(report, 
				"/pidresults:PersonFederalIdentificationResults[ident-ext:CivilIdentificationReasonCode]|"
				+ "/pidresults:PersonStateIdentificationResults[ident-ext:CivilIdentificationReasonCode]");
		
		if (rootNode != null){
			processCivilInitialResultsReport(rootNode, exchange);
		}
		else{
			rootNode = XmlUtils.xPathNodeSearch(report, 
					"/pidresults:PersonFederalIdentificationResults[ident-ext:CriminalIdentificationReasonCode]|"
							+ "/pidresults:PersonStateIdentificationResults[ident-ext:CriminalIdentificationReasonCode]");
			processCriminalInitialResultsReport(rootNode, exchange); 		
		}
	}

	private void processCriminalInitialResultsReport(Node rootNode, Exchange exchange) throws Exception {
		String transactionNumber = getTransactionNumber(rootNode);
		processIdentificationTransaction(rootNode, transactionNumber);
		processCriminalInitialResults(rootNode, exchange, transactionNumber); 
		
	}

	private void processCriminalInitialResults(Node rootNode, Exchange exchange, String transactionNumber) 
			throws Exception {
		CriminalInitialResults criminalInitialResults = new CriminalInitialResults(); 
		criminalInitialResults.setTransactionNumber(transactionNumber);
		
		criminalInitialResults.setSearchResultFile(	getBinaryData(rootNode, 
				"ident-ext:StateIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject|"
				+ "ident-ext:FBIIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
		
		if (rootNode.getLocalName().equals("PersonFederalIdentificationResults")){
			criminalInitialResults.setResultsSender(ResultSender.FBI);
		}
		else{
			criminalInitialResults.setResultsSender(ResultSender.State);
		}
		
		rapbackDAO.saveCriminalInitialResults(criminalInitialResults);

	}

	private void processCivilInitialResultsReport(Node rootNode, Exchange exchange) throws Exception {
		String transactionNumber = getTransactionNumber(rootNode);
		
		updateSubject(rootNode, transactionNumber);
		processCivilInitialResults(rootNode, exchange, transactionNumber);  
	}

	private void processCivilInitialResults(Node rootNode, Exchange exchange,
			String transactionNumber) throws Exception, IOException {
		CivilInitialResults civilInitialResults = new CivilInitialResults(); 
		civilInitialResults.setTransactionNumber(transactionNumber);
		
		if (rootNode.getLocalName().equals("PersonFederalIdentificationResults")){
			civilInitialResults.setResultsSender(ResultSender.FBI);
		}
		else{
			civilInitialResults.setResultsSender(ResultSender.State);
		}
		
		Integer initialResultsPkId = rapbackDAO.getCivilIntialResultsId(transactionNumber, civilInitialResults.getResultsSender());
		
		if (initialResultsPkId == null){
			civilInitialResults.setSearchResultFile(
					getBinaryData(rootNode, "ident-ext:StateIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject|ident-ext:FBIIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
			rapbackDAO.saveCivilInitialResults(civilInitialResults);
		}
		else{
		
			saveCivilRapSheet(exchange, transactionNumber, rootNode,
					initialResultsPkId);
		}
	}

	private void saveCivilRapSheet(Exchange exchange, String transactionNumber,
			Node rootNode, Integer initialResultsPkId) throws IOException {
		CivilInitialRapSheet civilInitialRapSheet = new CivilInitialRapSheet();
		civilInitialRapSheet.setCivilIntitialResultId(initialResultsPkId);
		
		civilInitialRapSheet.setRapSheet(getBinaryData(rootNode, "StateCriminalHistoryRecordDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject|"
				+ "ident-ext:FBIIdentityHistorySummaryDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
		
		rapbackDAO.saveCivilInitialRapSheet(civilInitialRapSheet);
	}

	private String getTransactionNumber(Node rootNode) throws Exception {
		String transactionNumber = XmlUtils.xPathStringSearch(rootNode, "ident-ext:TransactionIdentification/nc30:IdentificationID");
		return transactionNumber;
	}

}
