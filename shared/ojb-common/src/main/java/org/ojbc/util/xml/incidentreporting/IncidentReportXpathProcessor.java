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
package org.ojbc.util.xml.incidentreporting;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.incidentreporting.IncidentReport;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IncidentReportXpathProcessor {
	
	private final Log log = LogFactory.getLog( this.getClass() );
	
	public ArrayList<IncidentReport> reportList = new ArrayList<IncidentReport>();
	
	@Value("${incidentReportingService.attachementDir}")
	private String attachmentDir;
	
	public void processReport(Document report) {
		IncidentReport incidentReport = new IncidentReport();
		
		try {
			
			int responsePersonCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:ActivityResponsiblePersonAssociation").getLength();
			incidentReport.setResponsePersonCount(responsePersonCount);
			
			int involvedPersonCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ActivityInvolvedPersonAssociation").getLength();
			incidentReport.setInvolvedPersonCount(involvedPersonCount);
			
			int enforcedOfficialCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/jxdm40:EnforcementOfficial").getLength();
			incidentReport.setEnforcedOfficialCount(enforcedOfficialCount);
			
			if(responsePersonCount == 0 || involvedPersonCount == 0 || enforcedOfficialCount ==0 ) {
				incidentReport.setOfficerFlag(true);
			}
			
			String organization = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata/lexs:SystemIdentifier/nc:OrganizationName");
			if(StringUtils.isNotBlank(organization)) {
				incidentReport.setOrganizationName(organization);
			}
			
			String ori =  XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata/lexs:SystemIdentifier/lexs:ORI");
			if(StringUtils.isNotBlank(ori)) {
				incidentReport.setOri(ori);
			}
			
			String incidentNumber = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemID");
			if(StringUtils.isNotBlank(incidentNumber)) {
				incidentReport.setIncidentNumber(incidentNumber);
			}
			
			String county = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/inc-ext:LocationCountyCodeText");
			if(StringUtils.isNotBlank(county)) {
				incidentReport.setCounty(county);
			}
			
			String township = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/inc-ext:LocationTownshipCodeText");
			if(StringUtils.isNotBlank(township)) {
				incidentReport.setTownship(township);
			}
			
			int arrestOffenseAssociationCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:ArrestOffenseAssociation").getLength();
			incidentReport.setArrestOffenseAssociationCount(arrestOffenseAssociationCount);
			
			int incidentOffenseAssociationCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentOffenseAssociation").getLength();
			incidentReport.setIncidentOffenseAssociationCount(incidentOffenseAssociationCount);
			
			int incidentSubjectPersonAssociationCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentSubjectPersonAssociation").getLength();
			incidentReport.setIncidentSubjectPersonAssociationCount(incidentSubjectPersonAssociationCount);
			
			int arrestSubjectAssociationCount = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:ArrestSubjectAssociation").getLength();
			incidentReport.setArrestSubjectAssociationCount(arrestSubjectAssociationCount);
			
			
			log.info(incidentReport.toString());
			
			reportList.add(incidentReport);
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void printList() {
		for(IncidentReport report : reportList) {
			log.info(report.toString());
		}
	}
	
	public void saveAttachment(Document report) {

		try { 
			
			String incidentNumber = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemID");
			
			NodeList attachments = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:Attachment/nc:Binary/nc:BinaryObject");
			
			NodeList attachmentTypes = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:Attachment/nc:Binary/nc:BinaryFormatID");
			
			if(attachments.getLength() > 0) {
				for(int i = 0; i < attachments.getLength(); i++) {
					Node attachment = attachments.item(i);
					
					parseAttachment(incidentNumber, attachmentTypes, i, attachment);
					
				}
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

    private void parseAttachment(String incidentNumber, NodeList attachmentTypes, int i, Node attachment) {
        String attachmentType = attachmentTypes.item(i).getTextContent();
        
        File file;
        try {
        	if(attachmentType.equalsIgnoreCase("pdf")) {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".pdf");
        	}
        	else if(attachmentType.equalsIgnoreCase("png")) {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".png");
        	}
        	else if(attachmentType.equalsIgnoreCase("jpeg")) {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".jpeg");
        	}
        	else if(attachmentType.equalsIgnoreCase("zip")) {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".zip");
        	}
        	else if(attachmentType.equalsIgnoreCase("doc")) {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".doc");
        	}
        	else {
        		file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + "." + attachmentType);
        	}
        	
        	file.getParentFile().mkdirs();
        	
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } 
            else {
                System.out.println("File already exists.");
            }
              
            byte[] decodedImg = Base64.getDecoder()
                        .decode(attachment.getTextContent().replace("\n", "").getBytes(StandardCharsets.UTF_8));
            
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedImg);
            fos.close();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }
    
	public void saveReferralAttachment(Document report) {

		try { 
			
			NodeList attachments = null;
			NodeList attachmentTypes = null;
			
			
			String incidentNumber = XmlUtils.xPathStringSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemID");
			
			attachments = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:IncidentDocument/nc:DocumentBinary");
			
			attachmentTypes = XmlUtils.xPathNodeListSearch(report, "ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:IncidentDocument/nc:DocumentFormatText");
			
			if (attachments == null || attachments.getLength() == 0)
			{
				attachments = XmlUtils.xPathNodeListSearch(report, "cr-doc:ChargeReferral/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:IncidentDocument/nc:DocumentBinary");
				
				attachmentTypes = XmlUtils.xPathNodeListSearch(report, "cr-doc:ChargeReferral/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:IncidentDocument/nc:DocumentFormatText");

			}	
			
			
			if(attachments.getLength() > 0) {
				for(int i = 0; i < attachments.getLength(); i++) {
					Node attachment = attachments.item(i);
					
					String attachmentType = attachmentTypes.item(i).getTextContent();
					
					log.info("Attachment Type: " + attachmentType);
					
					File file;
					try {
						if(attachmentType.equalsIgnoreCase("pdf")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".pdf");
						}
						else if(attachmentType.equalsIgnoreCase("png")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".png");
						}
						else if(attachmentType.equalsIgnoreCase("jpeg")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".jpeg");
						}
						else if(attachmentType.equalsIgnoreCase("jpg")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".jpeg");
						}
						else if(attachmentType.equalsIgnoreCase("zip")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".zip");
						}
						else if(attachmentType.equalsIgnoreCase("doc")) {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + ".doc");
						}
						else {
							file = new File(attachmentDir + "/" + incidentNumber + "_attachment_" + i + "." + attachmentType);
						}
						 
					    if (file.createNewFile()) {
					        System.out.println("File created: " + file.getName());
					    } 
					    else {
					        System.out.println("File already exists.");
					    }
					      
					    byte[] decodedImg = Base64.getDecoder()
				                    .decode(attachment.getTextContent().replace("\n", "").getBytes(StandardCharsets.UTF_8));
					    
					    FileOutputStream fos = new FileOutputStream(file);
					    fos.write(decodedImg);
					    fos.close();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					
				}
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
