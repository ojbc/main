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

import java.text.SimpleDateFormat;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractReportRepositoryProcessor {

	@Autowired
	protected RapbackDAO rapbackDAO;
	
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	
    @Transactional
	public abstract void processReport(@Body Document report, Exchange exchange) throws Exception;

	protected void processIdentificationTransaction(Node rootNode, String transactionNumber)
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
		
		String identificationCategory = XmlUtils.xPathStringSearch(rootNode, "ident-ext:CivilIdentificationReasonCode|ident-ext:CriminalIdentificationReasonCode");
		identificationTransaction.setIdentificationCategory(identificationCategory);
		
		rapbackDAO.saveIdentificationTransaction(identificationTransaction);
	}

	protected Subject buildSubject(Node subjectNode) throws Exception {
		Subject subject = new Subject();
		String firstName = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonGivenName");
		subject.setFirstName(firstName);
		
		String lastName = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonSurName");
		subject.setLastName(lastName);
		
		String middleInitial = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonName/nc30:PersonMiddleName");
		subject.setMiddleInitial(middleInitial);
		
		String dobString = XmlUtils.xPathStringSearch(subjectNode, "nc30:PersonBirthDate/nc30:Date");
		subject.setDob(XmlUtils.parseXmlDate(dobString));
		
		String sexCode = XmlUtils.xPathStringSearch(subjectNode, "jxdm50:PersonSexCode"); 
		subject.setSexCode(sexCode);
		
		String fbiId = XmlUtils.xPathStringSearch(subjectNode, 
				"jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		subject.setUcn(StringUtils.trimToNull(fbiId));
		
		String civilSid = XmlUtils.xPathStringSearch(subjectNode, 
				"jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification"
				+ "[ident-ext:FingerpringIdentificationIssuedForCivilPurposeIndicator = 'true']/nc30:IdentificationID");
		subject .setCivilSid(StringUtils.trimToNull(civilSid));
		
		String criminalSid = XmlUtils.xPathStringSearch(subjectNode, 
				"jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification"
						+ "[ident-ext:FingerpringIdentificationIssuedForCriminalPurposeIndicator = 'true']/nc30:IdentificationID");
		subject .setCriminalSid(criminalSid);
		 
		return subject;
	}

	protected String getAttachmentId(Node rootNode) throws Exception {
		return XmlUtils.xPathStringSearch(rootNode, 
				"ident-ext:FederalFingerprintBasedIdentificationRequestDocument/xop:Include/@href|"
				+ "ident-ext:FBIIdentityHistorySummaryDocument/xop:Include/@href");
	}

}
