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
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.FingerPrintsType;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class IdentificationRequestReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( IdentificationRequestReportProcessor.class );
	
	@Autowired
	private RapbackDAO rapbackDAO;
	
	@Override
	@Transactional
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
			civilFingerPrints.setFingerPrintsType(FingerPrintsType.FBI);
		}
		else if (rootNode.getLocalName().equals("PersonStateIdentificationRequest")){
			civilFingerPrints.setFingerPrintsType(FingerPrintsType.State);
		}
		
		byte[] binaryData = getBinaryData(rootNode, 
				"ident-ext:FederalFingerprintBasedIdentificationRequestDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject|"
				+ "ident-ext:StateFingerprintBasedIdentificationRequestDocument/nc30:DocumentBinary/ident-ext:Base64BinaryObject"
				);
		
		civilFingerPrints.setFingerPrintsFile(binaryData);
		rapbackDAO.saveCivilFingerPrints(civilFingerPrints);
	}

}
