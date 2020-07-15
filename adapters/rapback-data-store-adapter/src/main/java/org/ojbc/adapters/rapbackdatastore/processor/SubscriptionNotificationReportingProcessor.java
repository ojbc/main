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

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.utils.Base64;
import org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class SubscriptionNotificationReportingProcessor {

	private static final Log log = LogFactory.getLog( SubscriptionNotificationReportingProcessor.class );
	
	@Autowired
	protected RapbackDAO rapbackDAO;
	
	@Autowired
	private FbiRapbackDao fbiRapbackDao;

	@Transactional
	public void processFbiNotificationReport(@Body Document report, @Header("operationName") String operationName, @Header("currentUcn") String currentUcn, @Header("newUcn") String newUcn, @Header("transactionNumber") String transactionNumber) throws Exception
	{
		String ucn="";
		String xpathToRapsheet = "";
		
		if (RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_CONSOLIDATION.equals(operationName)){
			
			ucn=newUcn;
			xpathToRapsheet="/chc-report-doc:CriminalHistoryConsolidationReport/chc-report-ext:CriminalHistoryRecordDocument/nc30:DocumentBinary/chc-report-ext:Base64BinaryObject";
			
			retrieveAndSaveRapsheet(report, transactionNumber, ucn, xpathToRapsheet, false); 
		}

		if (RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_RESTORATION.equals(operationName)){
			
			ucn=currentUcn;
			xpathToRapsheet="/chr-report-doc:CriminalHistoryRestorationReport/chr-report-ext:CriminalHistoryRecordDocument/nc30:DocumentBinary/chr-report-ext:Base64BinaryObject";
			
			retrieveAndSaveRapsheet(report, transactionNumber, ucn, xpathToRapsheet, false); 
		}

		if (RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_IDENTIFIER_UPDATE.equals(operationName)){
			
			ucn=newUcn;
			xpathToRapsheet="/chiu-report-doc:CriminalHistoryIdentifierUpdateReport/chiu-report-ext:CriminalHistoryRecordDocument/nc30:DocumentBinary/chiu-report-ext:Base64BinaryObject";
			
			retrieveAndSaveRapsheet(report, transactionNumber, ucn, xpathToRapsheet, false);
		}

		if (RapbackDataStoreAdapterConstants.REPORT_NEW_CRIMINAL_HISTORY_EVENT.equals(operationName)){
			
			ucn=currentUcn;
			xpathToRapsheet="//chr-ext:FederalCriminalHistoryRecordDocument/nc30:DocumentBinary/chr-ext:Base64BinaryObject";
			
			log.info("Saving and retrieving rap sheet for criminal history record: " + ucn);
			
			retrieveAndSaveRapsheet(report, transactionNumber, ucn, xpathToRapsheet, true);
		}

	}
	
	@Transactional
	public void processFbiSubscriptionReport(@Body Document report, @Header("operationName") String operationName) throws Exception
	{
		log.info("Processing FBI Subscription Report.");
		
		if (RapbackDataStoreAdapterConstants.REPORT_FEDERAL_SUBSCRIPTION_CREATION.equals(operationName)){
			processFbiSubscriptionCreationReport(report);
		}
		else if (RapbackDataStoreAdapterConstants.REPORT_FEDERAL_SUBSCRIPTION_UPDATE.equals(operationName)){
			processFbiSubscriptionUpdateReport(report);
		}
		else if (RapbackDataStoreAdapterConstants.REPORT_FEDERAL_SUBSCRIPTION_ERROR.equals(operationName)) {
			processFbiSubscriptionErrorReport(report);
		}
		
	}

	private void processFbiSubscriptionErrorReport(Document report) throws Exception {
		
		String subscriptionIdString = XmlUtils.xPathStringSearch(report, "/fed_subcr-doc:FederalSubscriptionErrorReport/fed_subcr-ext:StateSubscriptionIdentification/nc30:IdentificationID");
		
		Integer subscriptionId = Integer.valueOf(subscriptionIdString);
		
		rapbackDAO.updateFbiSubscriptionStatus(subscriptionId, "ERROR");
		
	}

	@Transactional
	private void processFbiSubscriptionUpdateReport(Document report) throws Exception {
		FbiRapbackSubscription fbiRapbackSubscription = buildFbiSubscriptionFromUpdate(report);
		rapbackDAO.updateFbiRapbackSubscription(fbiRapbackSubscription);
		retrieveAndSaveRapsheet(report, fbiRapbackSubscription.getTransactionNumber(), fbiRapbackSubscription.getUcn(), "fed_subcr_upd-doc:FederalSubscriptionUpdateReport/fed_subcr_upd-ext:CriminalHistoryDocument/nc30:DocumentBinary/fed_subcr_upd-ext:Base64BinaryObject|"
				+ "fed_subcr-doc:FederalSubscriptionCreationReport/fed_subcr-ext:CriminalHistoryDocument/nc30:DocumentBinary/fed_subcr-ext:Base64BinaryObject", false);
	}

	private void retrieveAndSaveRapsheet(Document report, String transactionNumber, String ucn, String xpathToRapsheet, Boolean notificationIndicator) throws Exception {
		byte[] binaryData = getBinaryData(report, xpathToRapsheet);
		
		if (binaryData == null || binaryData.length == 0) 
			return; 
		
		SubsequentResults subsequentResults = new SubsequentResults(); 
		subsequentResults.setRapSheet(binaryData);
		subsequentResults.setUcn(ucn);
		subsequentResults.setTransactionNumber(transactionNumber);
		subsequentResults.setResultsSender(ResultSender.FBI);
		subsequentResults.setNotificationIndicator(notificationIndicator);
		fbiRapbackDao.saveSubsequentResults(subsequentResults);
	}

	@Transactional
	private void processFbiSubscriptionCreationReport(Document report) throws Exception {
		FbiRapbackSubscription fbiRapbackSubscription = buildNewFbiSubscription(report);
		rapbackDAO.saveFbiRapbackSubscription(fbiRapbackSubscription);
		
		String subscriptionIdString = XmlUtils.xPathStringSearch(report, "/fed_subcr-doc:FederalSubscriptionCreationReport/fed_subcr-ext:RapBackSubscriptionData/fed_subcr-ext:StateSubscriptionIdentification/nc30:IdentificationID");
		
		Integer subscriptionId = Integer.valueOf(subscriptionIdString);
		
		rapbackDAO.updateFbiSubscriptionStatus(subscriptionId, "SUBSCRIBED");
		
		if (!fbiRapbackSubscription.getRapbackCategory().equals("CS") &&  !fbiRapbackSubscription.getRapbackCategory().equals("CI"))
		{	
			retrieveAndSaveRapsheet(report, fbiRapbackSubscription.getTransactionNumber(), fbiRapbackSubscription.getUcn(), "fed_subcr_upd-doc:FederalSubscriptionUpdateReport/fed_subcr_upd-ext:CriminalHistoryDocument/nc30:DocumentBinary/fed_subcr_upd-ext:Base64BinaryObject|"
					+ "fed_subcr-doc:FederalSubscriptionCreationReport/fed_subcr-ext:CriminalHistoryDocument/nc30:DocumentBinary/fed_subcr-ext:Base64BinaryObject", false);
		}
		
	}

	private FbiRapbackSubscription buildFbiSubscriptionFromUpdate(
			Document report) throws Exception {
		Node rootNode = XmlUtils.xPathNodeSearch(report, 
				"/fed_subcr_upd-doc:FederalSubscriptionUpdateReport");
		Node rapbackSubscriptionData = XmlUtils.xPathNodeSearch(rootNode, "fed_subcr_upd-ext:RapBackSubscriptionData"); 
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription();
		
		String rapBackActivityNotificationFormatCode = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackActivityNotificationFormatCode");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat(rapBackActivityNotificationFormatCode);
		
		String transactionNumber = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:FingerprintIdentificationTransactionIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setTransactionNumber(transactionNumber);
		
		String rapBackExpirationDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackExpirationDate/nc30:Date");
		fbiRapbackSubscription.setRapbackExpirationDate(OJBCDateUtils.parseLocalDate(rapBackExpirationDate));
		
		String rapBackInStateOptOutIndicator = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackInStateOptOutIndicator");
		fbiRapbackSubscription.setRapbackOptOutInState(BooleanUtils.toBooleanObject(rapBackInStateOptOutIndicator));
		
		String rapBackSubscriptionDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackSubscriptionDate/nc30:Date");
		fbiRapbackSubscription.setRapbackStartDate(OJBCDateUtils.parseLocalDate(rapBackSubscriptionDate));
		
		String rapBackSubscriptionIdentification = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackSubscriptionIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setFbiSubscriptionId(rapBackSubscriptionIdentification);
		
		String rapBackSubscriptionTermCode = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackSubscriptionTermCode");
		fbiRapbackSubscription.setSubscriptionTerm(rapBackSubscriptionTermCode);
		
		String rapBackTermDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr_upd-ext:RapBackTermDate/nc30:Date");
		fbiRapbackSubscription.setRapbackTermDate(OJBCDateUtils.parseLocalDate(rapBackTermDate));
		
		String ucn = XmlUtils.xPathStringSearch(rootNode, "nc30:Person[@s30:id=../jxdm50:Subject/nc30:RoleOfPerson/@s30:ref]/jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setUcn(ucn);
		return fbiRapbackSubscription;
	}

	private FbiRapbackSubscription buildNewFbiSubscription(Document report) throws Exception {
		Node rootNode = XmlUtils.xPathNodeSearch(report, 
				"/fed_subcr-doc:FederalSubscriptionCreationReport");
		Node rapbackSubscriptionData = XmlUtils.xPathNodeSearch(rootNode, "fed_subcr-ext:RapBackSubscriptionData"); 
		FbiRapbackSubscription fbiRapbackSubscription = new FbiRapbackSubscription();
		
		String eventIdentifier = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackEnrollmentEventID/nc30:IdentificationID");
		fbiRapbackSubscription.setEventIdentifier(eventIdentifier);
		
		String rapBackActivityNotificationFormatCode = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackActivityNotificationFormatCode");
		fbiRapbackSubscription.setRapbackActivityNotificationFormat(rapBackActivityNotificationFormatCode);
		
		String transactionNumber = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:FingerprintIdentificationTransactionIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setTransactionNumber(transactionNumber);

		String rapBackCategoryCode = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackCategoryCode");
		fbiRapbackSubscription.setRapbackCategory(rapBackCategoryCode);
		
		String rapBackExpirationDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackExpirationDate/nc30:Date");
		fbiRapbackSubscription.setRapbackExpirationDate(OJBCDateUtils.parseLocalDate(rapBackExpirationDate));
		
		String rapBackInStateOptOutIndicator = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackInStateOptOutIndicator");
		fbiRapbackSubscription.setRapbackOptOutInState(BooleanUtils.toBooleanObject(rapBackInStateOptOutIndicator));
		
		String rapBackSubscriptionDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackSubscriptionDate/nc30:Date");
		fbiRapbackSubscription.setRapbackStartDate(OJBCDateUtils.parseLocalDate(rapBackSubscriptionDate));
		
		String rapBackSubscriptionIdentification = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackSubscriptionIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setFbiSubscriptionId(rapBackSubscriptionIdentification);
		
		String stateSubscriptionIdentification = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:StateSubscriptionIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setStateSubscriptionId(stateSubscriptionIdentification);
		
		String rapBackSubscriptionTermCode = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackSubscriptionTermCode");
		fbiRapbackSubscription.setSubscriptionTerm(rapBackSubscriptionTermCode);
		
		String rapBackTermDate = XmlUtils.xPathStringSearch(rapbackSubscriptionData, "fed_subcr-ext:RapBackTermDate/nc30:Date");
		fbiRapbackSubscription.setRapbackTermDate(OJBCDateUtils.parseLocalDate(rapBackTermDate));
		
		String ucn = XmlUtils.xPathStringSearch(rootNode, 
				"nc30:Person[@s30:id=../jxdm50:Subject/nc30:RoleOfPerson/@s30:ref]/jxdm50:PersonAugmentation"
				+ "/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		fbiRapbackSubscription.setUcn(ucn);
		return fbiRapbackSubscription;
	}

	protected byte[] getBinaryData(Node rootNode, String pathToReport) {
		String base64BinaryData;
		try {
			base64BinaryData = XmlUtils.xPathStringSearch(rootNode, pathToReport);
			return Base64.decode(base64BinaryData);
			
		} catch (Exception e) {			
			log.error("Failed to retrieve binary data from the subscription reporting message: ", e);			
			return null;
		}
	}


}
