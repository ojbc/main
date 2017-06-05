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
package org.ojbc.intermediaries.sn.dao.rapback;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FbiSubModDocBuilder {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Logger logger = Logger.getLogger(FbiSubModDocBuilder.class);
	
	private static final OjbcNamespaceContext CTXT = new OjbcNamespaceContext();
	
	
	public Document buildFbiSubModDoc(FbiSubscriptionModification fbiSubMod) throws Exception{
				
		Document doc = OJBCXMLUtils.createDocument();
		
		Element modifyElement = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Modify");
		modifyElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);		
		doc.appendChild(modifyElement);
				
		Element subModMsgElement = XmlUtils.appendElement(modifyElement, OjbcNamespaceContext.NS_SUB_MODIFY_MESSAGE, "SubscriptionModificationMessage");
		
		buildSubjectElement(subModMsgElement, fbiSubMod);
				
		buildFBISubscriptionElement(subModMsgElement, fbiSubMod);				
					
		buildSubModEndDateElement(subModMsgElement, fbiSubMod);				
		
		CTXT.populateRootNamespaceDeclarations(doc.getDocumentElement());
		
		return doc;
	}


	private Element buildSubjectElement(Element parent, FbiSubscriptionModification fbiSubMod){
		
		Element subjectElement = null;
		
		String fbiUcnId = fbiSubMod.getPersonFbiUcnId();
		
		if(StringUtils.isNotEmpty(fbiUcnId)){
			
			subjectElement = XmlUtils.appendElement(parent, OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");
			
			Element personAugElement = XmlUtils.appendElement(subjectElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
			
			Element personFBIIdElement = XmlUtils.appendElement(personAugElement, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
			
			Element personFbiIdValElement = XmlUtils.appendElement(personFBIIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
			
			personFbiIdValElement.setTextContent(fbiUcnId);			
		}			
		return subjectElement;
	}
	
	
	private void buildFBISubscriptionElement(Element parentElement, FbiSubscriptionModification fbiSubMod){
	
		String subId = fbiSubMod.getSubscriptionFbiId();
		
		boolean hasSubId = StringUtils.isNotEmpty(subId);
						
		String reasonCode = fbiSubMod.getReasonCode();
		
		boolean hasReasonCode = StringUtils.isNotEmpty(reasonCode);
				
		if(hasSubId || hasReasonCode){
				
			Element fBISubscriptionElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FBISubscription");
			
			if(hasSubId){				
				
				Element subFBIIdElement = XmlUtils.appendElement(fBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, 
						"SubscriptionFBIIdentification");
				
				Element subFBIIdValElement = XmlUtils.appendElement(subFBIIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
				
				subFBIIdValElement.setTextContent(subId);				
			}
						
			if(hasReasonCode){
			
				Element reasonCodeElement = XmlUtils.appendElement(fBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
				
				reasonCodeElement.setTextContent(reasonCode);				
			}			
		}		
	}
	
	
	private void buildSubModEndDateElement(Element parentElement, FbiSubscriptionModification fbiSubMod){
		
		Date subModEndDate = fbiSubMod.getSubModEndDate();
						
		if(subModEndDate != null){
			
			Element subModElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionModification");
			
			Element dateRangeElement = XmlUtils.appendElement(subModElement, OjbcNamespaceContext.NS_NC, "DateRange");
			
			Element endDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
			
			Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC, "Date");
			
			try{
				String sSubModEndDate = SDF.format(subModEndDate);
				
				endDateValElement.setTextContent(sSubModEndDate);
				
			}catch(Exception e){
				logger.error("Cannot format date");
			}		
		}				
	}

}
