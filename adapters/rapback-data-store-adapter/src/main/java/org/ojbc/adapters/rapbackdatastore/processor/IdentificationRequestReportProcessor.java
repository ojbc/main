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
package org.ojbc.adapters.rapbackdatastore.processor;

import java.io.IOException;

import javax.activation.DataHandler;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class IdentificationRequestReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( IdentificationRequestReportProcessor.class );
	
	@Autowired
	private RapbackDAO rapbackDAO;
	
	@Override
	public void processReport(@Body Document report, Exchange exchange) throws Exception {
		
		log.info("Processing Identification Request report");
		
		Node rootNode = XmlUtils.xPathNodeSearch(report, "/pidreq:PersonFederalIdentificationRequest|/pidreq:PersonStateIdentificationRequest");
		String transactionNumber = XmlUtils.xPathStringSearch(rootNode, "ident-ext:TransactionIdentification/nc30:IdentificationID"); 

		processIdentificationTransaction(rootNode, transactionNumber);
		
		processCivilFingerPrints(exchange, rootNode, transactionNumber);
	}

	private void processCivilFingerPrints(Exchange exchange, Node rootNode,
			String transactionNumber) throws Exception, IOException {
		CivilFingerPrints civilFingerPrints = new CivilFingerPrints();
		
		civilFingerPrints.setTransactionNumber(transactionNumber);
		
		if (rootNode.getLocalName().equals("PersonFederalIdentificationRequest")){
			civilFingerPrints.setFingerPrintsType("FBI");
		}
		else if (rootNode.getLocalName().equals("PersonStateIdentificationRequest")){
			civilFingerPrints.setFingerPrintsType("STATE");
		}
		
//		String contentType = XmlUtils.xPathStringSearch(rootNode, 
//				"ident-ext:FederalFingerprintBasedIdentificationRequestDocument/ident-ext:DocumentBinary/@xmime:contentType");
		String attachmentId = XmlUtils.xPathStringSearch(rootNode, 
				"ident-ext:FederalFingerprintBasedIdentificationRequestDocument/xop:Include/@href");
		
		DataHandler dataHandler = exchange.getIn().getAttachment(StringUtils.substringAfter(attachmentId, "cid:"));
		//assertEquals("image/jpeg", dataHandler.getContentType());
		
		if (dataHandler != null){
			byte[] receivedAttachment = IOUtils.readBytesFromStream(dataHandler.getInputStream());
			civilFingerPrints.setFingerPrintsFile(receivedAttachment);
		}
		else{
			log.error("No finger prints file found in the attachement for transaction " + transactionNumber);
			throw new IllegalArgumentException("No finger prints file found in the attachement"); 
		}

		civilFingerPrints.setTransactionType("Transaction");
		rapbackDAO.saveCivilFingerPrints(civilFingerPrints);
	}

	private void processIdentificationTransaction(Node rootNode, String transactionNumber)
			throws Exception {
		IdentificationTransaction identificationTransaction = new IdentificationTransaction(); 
		
		identificationTransaction.setTransactionNumber(transactionNumber);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(rootNode, "jxdm50:Subject/nc30:RoleOfPerson"); 
		Assert.notNull(subjectNode);
		Subject subject = buildSubject(subjectNode) ;
		identificationTransaction.setSubject(subject);
		
		String otn = XmlUtils.xPathStringSearch(subjectNode, "ident-ext:PersonTrackingIdentidication/nc30:IdentificationID");
		identificationTransaction.setOtn(otn);
		
		String ownerOri = XmlUtils.xPathStringSearch(rootNode, "ident-ext:IdentificationApplicantOrganization/"
				+ "jxdm50:OrganizationAugmentation/jxdm50:OrganizationORIIdentification/nc30:IdentificationID");
		identificationTransaction.setOwnerOri(ownerOri);
		
		String ownerProgramOca = XmlUtils.xPathStringSearch(rootNode, "//ident-ext:IdentificationApplicantOrganization/"
				+ "nc30:OrganizationIdentification/nc30:IdentificationID");
		identificationTransaction.setOwnerProgramOca(ownerProgramOca);
		
		rapbackDAO.saveIdentificationTransaction(identificationTransaction);
	}

	private Subject buildSubject(Node subjectNode) throws Exception {
		Subject subject = new Subject();
		String firstName = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonGivenName");
		subject.setFirstName(firstName);
		
		String lastName = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonSurName");
		subject.setLastName(lastName);
		
		String middleInitial = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonMiddleName");
		subject.setMiddleInitial(middleInitial);
		
		String dobString = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonBirthDate/nc30:date");
		if (StringUtils.isNotBlank(dobString)){
			subject.setDob(XmlUtils.parseXmlDate(dobString));
		}
		
		String sexCode = XmlUtils.xPathStringSearch(subjectNode, "jxdm50:PersonSexCode"); 
		subject.setSexCode(sexCode);
		 
		return subject;
	}

}
