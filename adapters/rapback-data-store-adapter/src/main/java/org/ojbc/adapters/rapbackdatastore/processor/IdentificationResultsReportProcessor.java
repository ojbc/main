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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.RapbackIllegalStateException;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorDemographics;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorSearchResult;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class IdentificationResultsReportProcessor extends AbstractReportRepositoryProcessor {

	private static final String CRIMINAL_SID_TO_HIJIS = "CRIMINAL-SID-TO-HIJIS";
	private static final String CIVIL_SID_TO_HIJIS = "CIVIL-SID-TO-HIJIS";
	private static final String NSOR_RESULT_TO_HIJIS = "NSOR-TO-HIJIS";
	private static final String INCOMING_QXS_RAP = "INCOMING-QXS-RAP";
	
	private static final Log log = LogFactory.getLog( IdentificationResultsReportProcessor.class );
    
    private String databaseResendFolder; 
    private String inputFolder; 
    
    public IdentificationResultsReportProcessor() {
    	super();
    }
    
    @Autowired
	public IdentificationResultsReportProcessor(
			@Value("${rapbackDatastoreAdapter.IdentificationRecordingInputDirectory}") String identificationRecordingInputDirectory) {
		this();
		databaseResendFolder = identificationRecordingInputDirectory + File.separator + "databaseResends";
		inputFolder = identificationRecordingInputDirectory + File.separator + "input";
//		log.info("databaseResendFolder: " + StringUtils.trimToEmpty(databaseResendFolder)); 
//		log.info("inputFolder: " + StringUtils.trimToEmpty(inputFolder)); 

	}	
	
	@Transactional
	public void processReport(@Body Document report, @Header("identificationID") String transactionNumber) throws Exception
	{
		log.info("Processing Identification Results Report.");
		
		String transactionCategoryText = XmlUtils.xPathStringSearch(report, "/pidresults:PersonFederalIdentificationResults/ident-ext:TransactionCategoryText|"
				+ "/pidresults:PersonStateIdentificationResults/ident-ext:TransactionCategoryText");
		if (CRIMINAL_SID_TO_HIJIS.equals(transactionCategoryText) || CIVIL_SID_TO_HIJIS.equals(transactionCategoryText)){
			return; 
		}
		
		Node civilRootNode = XmlUtils.xPathNodeSearch(report, 
				"/pidresults:PersonFederalIdentificationResults[ident-ext:CivilIdentificationReasonCode]|"
				+ "/pidresults:PersonStateIdentificationResults[ident-ext:CivilIdentificationReasonCode]");

		Node criminalRootNode = XmlUtils.xPathNodeSearch(report, 
				"/pidresults:PersonFederalIdentificationResults[ident-ext:CriminalIdentificationReasonCode]|"
						+ "/pidresults:PersonStateIdentificationResults[ident-ext:CriminalIdentificationReasonCode]");

		if (civilRootNode != null)
		{
			processCivilInitialResultsReport(civilRootNode, transactionNumber, transactionCategoryText);
		}
		
		if (criminalRootNode != null)
		{
			processCriminalInitialResultsReport(criminalRootNode, transactionNumber); 		
		}
	}

	private void processCriminalInitialResultsReport(Node rootNode, String transactionNumber) throws Exception {
		processIdentificationTransaction(rootNode, transactionNumber);
		processCriminalInitialResults(rootNode, transactionNumber); 
		
	}

	private void processCriminalInitialResults(Node rootNode, String transactionNumber) 
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

	private void processCivilInitialResultsReport(Node rootNode, String transactionNumber, String transactionCategoryText) throws Exception {
		
		//Check for transaction category code
		//Check for NSOR previous result
		//Save and exit
		
		if (INCOMING_QXS_RAP.equals(transactionCategoryText))
		{
			if (rapbackDAO.isExistingNsorTransaction(transactionNumber))
			{	
				saveNsorDocument(rootNode, transactionNumber);
			}
			else
			{
				throw new Exception("Unable to save QXS Rap with additional details. No existing transaction exists.");
			}
			
			return;
		}

		
		if (!rapbackDAO.isExistingTransactionNumber(transactionNumber)){
			if (isOrigNistQueued( inputFolder, transactionNumber ) || isOrigNistQueued(databaseResendFolder, transactionNumber)){
				throw new RapbackIllegalStateException("Inappropriate time to process the result"); 
			}
			else{
				throw new Exception("Invalid Result report: no transction number found");
			}
		}
		
		updateSubject(rootNode, transactionNumber);
		  
		
		if (NSOR_RESULT_TO_HIJIS.equals(transactionCategoryText))
		{
			log.info("Processing NSOR Result to HIJIS message.");
			saveNsorDocument(rootNode, transactionNumber);
		}
		else
		{
			processCivilInitialResults(rootNode, transactionNumber);	
		}

	}

	private void saveNsorDocument(Node rootNode, String transactionNumber) throws Exception {
		//Save ext:NationalSexOffenderRegistryDemographicsDocument or ext:NationalSexOffenderRegistrySearchResultDocument
		
		Node  nsorSearchResultElement = XmlUtils.xPathNodeSearch(rootNode, "/pidresults:PersonFederalIdentificationResults/ident-ext:NationalSexOffenderRegistrySearchResultDocument");
		Node nsorDemographicsElement = XmlUtils.xPathNodeSearch(rootNode, "/pidresults:PersonFederalIdentificationResults/ident-ext:NationalSexOffenderRegistryDemographicsDocument");
		
		if (nsorDemographicsElement != null)
		{
			NsorDemographics nsorDemographics = new NsorDemographics();
			
			//Make Model
			if (rootNode.getLocalName().equals("PersonFederalIdentificationResults")){
				nsorDemographics.setResultsSender(ResultSender.FBI);
			}
			else{
				nsorDemographics.setResultsSender(ResultSender.State);
			}			
			
			nsorDemographics.setTransactionNumber(transactionNumber);
			
			nsorDemographics.setDemographicsFile(
					getBinaryData(rootNode, "/pidresults:PersonFederalIdentificationResults/ident-ext:NationalSexOffenderRegistryDemographicsDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
			
			rapbackDAO.saveNsorDemographics(nsorDemographics);

		}	

		if (nsorSearchResultElement != null)
		{
			NsorSearchResult nsorSearchResult = new NsorSearchResult();
			
			//Make Model
			if (rootNode.getLocalName().equals("PersonFederalIdentificationResults")){
				nsorSearchResult.setResultsSender(ResultSender.FBI);
			}
			else{
				nsorSearchResult.setResultsSender(ResultSender.State);
			}			
			
			nsorSearchResult.setTransactionNumber(transactionNumber);
			
			nsorSearchResult.setSearchResultFile(
					getBinaryData(rootNode, "/pidresults:PersonFederalIdentificationResults/ident-ext:NationalSexOffenderRegistrySearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
			
			rapbackDAO.saveNsorSearchResult(nsorSearchResult);
			
		}	
		
		
	}

	private void processCivilInitialResults(Node rootNode, String transactionNumber) throws Exception, IOException {
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
					getBinaryData(rootNode, "ident-ext:StateIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"
							+ "|ident-ext:FBIIdentificationSearchResultDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
			rapbackDAO.saveCivilInitialResults(civilInitialResults);
		}
		else{
		
			saveCivilRapSheet(transactionNumber, rootNode,
					initialResultsPkId);
		}
	}

	private void saveCivilRapSheet(String transactionNumber,
			Node rootNode, Integer initialResultsPkId) throws IOException {
		CivilInitialRapSheet civilInitialRapSheet = new CivilInitialRapSheet();
		civilInitialRapSheet.setCivilIntitialResultId(initialResultsPkId);
		
		civilInitialRapSheet.setRapSheet(getBinaryData(rootNode, "ident-ext:StateCriminalHistoryRecordDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject|"
				+ "ident-ext:FBIIdentityHistorySummaryDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"));
		
		rapbackDAO.saveCivilInitialRapSheet(civilInitialRapSheet);
	}

	private boolean isOrigNistQueued(String filePath, String transactionNumber) {
		boolean isOrigNistQueued = false;
		
		log.info("check if ORIG_NIST of the transaction number, " + transactionNumber +" is in the folder " + StringUtils.trimToEmpty(filePath)); 
		
		try {
			isOrigNistQueued = Files.list(Paths.get(filePath))
					.anyMatch(path -> path.getFileName().toString().startsWith("ORIG-NIST-SEND_" + transactionNumber));
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return isOrigNistQueued;
	}
	
//	public static void main(String[] args) {
//		boolean isQuequed = isOrigNistQueued("/tmp/ojb/adapter/rapback/identificationRecording/databaseResends", "1A0412420180411133030808868");
//		System.out.println("Is the ORIG-NIST-SEND queued? " + BooleanUtils.toStringYesNo(isQuequed));
//	}
}
