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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractReportRepositoryProcessor {

    private static final String R_ORIG_NIST_SEND = "R-ORIG-NIST-SEND";

	@Value("#{'${rapbackDatastoreAdapter.actingFbiOriForCivilPrivateAgencies:}'.split(',')}")
    private List<String> actingFbiOriForCivilPrivateAgencies;
    
    @Value("${rapbackDatastoreAdapter.civilPrivateAgencyOriRegex:}")
    private String civilPrivateAgencyOriRegex;
    
    private Pattern civilPrivateAgencyOriPattern; 
    
	@Autowired
	protected RapbackDAO rapbackDAO;
	
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );
    	
    @Transactional
	public abstract void processReport(@Body Document report, 
			@Header("transactionCategoryReplyText") String transactionCategoryReplyText, 
			@Header("identificationID") String transactionNumber) throws Exception;

	protected void processIdentificationTransaction(Node rootNode, 
			String transactionCategoryReplyText, 
			String transactionNumber)
			throws Exception {
		
		if (rapbackDAO.isExistingTransactionNumber(transactionNumber)){
			String identificationCategory = XmlUtils.xPathStringSearch(rootNode, 
					"ident-ext:CivilIdentificationReasonCode");
			if (StringUtils.isNotBlank(identificationCategory)){
				rapbackDAO.updateIdentificationCategory(transactionNumber, identificationCategory);
			}
			else{
				identificationCategory = XmlUtils.xPathStringSearch(rootNode, 
						"ident-ext:CriminalIdentificationReasonCode");
				if (StringUtils.isNotBlank(identificationCategory)){
					updateSubject(rootNode, transactionNumber);
				}
			}
			return;
		}
		
		IdentificationTransaction identificationTransaction = new IdentificationTransaction(); 
		
		identificationTransaction.setTransactionNumber(transactionNumber);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(rootNode, "jxdm50:Subject/nc30:RoleOfPerson"); 
		Assert.notNull(subjectNode, "the jxdm50:Subject/nc30:RoleOfPerson node should not be null");
		Subject subject = buildSubject(subjectNode) ;
		identificationTransaction.setSubject(subject);
		
		populateIdentificationTransaction(rootNode, identificationTransaction, subjectNode);
		
		rapbackDAO.saveIdentificationTransaction(identificationTransaction);
	}
	
	protected void updateIdentificationTransaction(Node rootNode, 
			String transactionCategoryText, 
			String transactionNumber)
					throws Exception {
		log.info("transactionCategoryReplyText: " + transactionCategoryText);
		if (!StringUtils.equalsIgnoreCase(R_ORIG_NIST_SEND, transactionCategoryText) && rapbackDAO.isExistingTransactionNumber(transactionNumber)){
			String identificationCategory = XmlUtils.xPathStringSearch(rootNode, 
					"ident-ext:CivilIdentificationReasonCode");
			if (StringUtils.isNotBlank(identificationCategory)){
				rapbackDAO.updateIdentificationCategory(transactionNumber, identificationCategory);
			}
			else{
				identificationCategory = XmlUtils.xPathStringSearch(rootNode, 
						"ident-ext:CriminalIdentificationReasonCode");
				if (StringUtils.isNotBlank(identificationCategory)){
					updateSubject(rootNode, transactionNumber);
				}
			}
			return;
		}
		
		IdentificationTransaction identificationTransaction = rapbackDAO.getIdentificationTransaction(transactionNumber);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(rootNode, "jxdm50:Subject/nc30:RoleOfPerson"); 
		Assert.notNull(subjectNode, "the jxdm50:Subject/nc30:RoleOfPerson node should not be null");
		Subject subject = identificationTransaction.getSubject() ;
		populateSubject(subjectNode, subject); 
		
		populateIdentificationTransaction(rootNode, identificationTransaction, subjectNode);
		
		rapbackDAO.updateIdentificationTransaction(identificationTransaction);
	}

	private void populateIdentificationTransaction(Node rootNode, IdentificationTransaction identificationTransaction, Node subjectNode)
			throws Exception {
		String otn = XmlUtils.xPathStringSearch(subjectNode, "ident-ext:PersonTrackingIdentification/nc30:IdentificationID");
		identificationTransaction.setOtn(otn);
		
		String ownerProgramOca = XmlUtils.xPathStringSearch(rootNode, "//ident-ext:IdentificationApplicantOrganization/"
				+ "nc30:OrganizationIdentification/nc30:IdentificationID");
		
		if ( civilPrivateAgencyOriPattern == null){
			civilPrivateAgencyOriPattern = Pattern.compile(civilPrivateAgencyOriRegex);	
		}
		
		Matcher matcher = civilPrivateAgencyOriPattern.matcher(StringUtils.trimToEmpty(ownerProgramOca));
		
		String ownerOri = XmlUtils.xPathStringSearch(rootNode, "ident-ext:IdentificationApplicantOrganization/"
				+ "jxdm50:OrganizationAugmentation/jxdm50:OrganizationORIIdentification/nc30:IdentificationID");
		
		if (actingFbiOriForCivilPrivateAgencies.contains(ownerOri) 
				&& matcher.find()){
			identificationTransaction.setOwnerOri(matcher.group(0));
			identificationTransaction.setOwnerProgramOca(ownerProgramOca);
		}else{
			identificationTransaction.setOwnerOri(ownerOri);
			identificationTransaction.setOwnerProgramOca(ownerProgramOca);
		}
		
		String identificationCategory = XmlUtils.xPathStringSearch(rootNode, "ident-ext:CivilIdentificationReasonCode|ident-ext:CriminalIdentificationReasonCode");
		identificationTransaction.setIdentificationCategory(identificationCategory);
	}

	protected Subject buildSubject(Node subjectNode) throws Exception {
		Subject subject = new Subject();
		populateSubject(subjectNode, subject);
		 
		return subject;
	}

	private void populateSubject(Node subjectNode, Subject subject) throws Exception {
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
				+ "[ident-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator = 'true']/nc30:IdentificationID");
		subject .setCivilSid(StringUtils.trimToNull(civilSid));
		
		String criminalSid = XmlUtils.xPathStringSearch(subjectNode, 
				"jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification"
						+ "[ident-ext:FingerprintIdentificationIssuedForCriminalPurposeIndicator = 'true']/nc30:IdentificationID");
		subject .setCriminalSid(criminalSid);
	}

	protected byte[] getBinaryData(Node rootNode, String xPath) {
		String base64BinaryData;
		try {
			base64BinaryData = XmlUtils.xPathStringSearch(rootNode, xPath);
			
			if (base64BinaryData == null){
				throw new IllegalArgumentException("Failed to retrieve binary data from the message xPath: " + xPath);
			}
			
			return Base64.decodeBase64(base64BinaryData);
			
		} catch (Exception e) {			
			log.error("Failed to retrieve binary data from the message: " + e.getMessage());			
			throw new IllegalArgumentException("Failed to retrieve binary data from the message xPath: " + xPath);
		}
	}
	
	void updateSubject(Node rootNode, String transactionNumber)
			throws Exception {
		IdentificationTransaction identificationTransaction = 
				rapbackDAO.getIdentificationTransaction(transactionNumber); 
		
		Subject subject = identificationTransaction.getSubject(); 
		
		String fbiId = XmlUtils.xPathStringSearch(rootNode, 
				"jxdm50:Subject/nc30:RoleOfPerson/jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		if (StringUtils.isNotBlank(fbiId)){
			subject.setUcn(fbiId);
		}
		
		String civilSid = XmlUtils.xPathStringSearch(rootNode, 
				"jxdm50:Subject/nc30:RoleOfPerson/jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification[ident-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator='true']/nc30:IdentificationID");
		if (StringUtils.isNotBlank(civilSid)){
			subject.setCivilSid(civilSid);
		}
		
		String criminalSid = XmlUtils.xPathStringSearch(rootNode, 
				"jxdm50:Subject/nc30:RoleOfPerson/jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification[ident-ext:FingerprintIdentificationIssuedForCriminalPurposeIndicator='true']/nc30:IdentificationID");
		if (StringUtils.isNotBlank(criminalSid)){
			subject.setCriminalSid(criminalSid);
		}
		
		rapbackDAO.updateSubject(subject);
	}


}
