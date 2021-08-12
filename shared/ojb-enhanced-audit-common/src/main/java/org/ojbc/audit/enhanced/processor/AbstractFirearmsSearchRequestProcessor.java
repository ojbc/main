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
package org.ojbc.audit.enhanced.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractFirearmsSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractFirearmsSearchRequestProcessor.class);
	
	public abstract void auditFirearmsSearchRequest(Exchange exchange, @Body Document inputDoc);
	
	FirearmsSearchRequest processFirearmsSearchRequest(Document inputDoc) throws Exception
	{
		FirearmsSearchRequest firearmsSearchRequest = new FirearmsSearchRequest();

		Node firearmNode = XmlUtils.xPathNodeSearch(inputDoc, 
				"/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm");
		
		if(firearmNode != null){
			
			// note since this is for the search request xml, there should be only one Firearm element in the xml
			String serialNumber = XmlUtils.xPathStringSearch(firearmNode, "nc:ItemSerialIdentification/nc:IdentificationID");			
			if(StringUtils.isNotBlank(serialNumber)){
				firearmsSearchRequest.setSerialNumber(serialNumber);
			}
			
			String firearmsType = XmlUtils.xPathStringSearch(firearmNode, "nc:FirearmCategoryCode");
			if(StringUtils.isNotBlank(firearmsType)){
				firearmsSearchRequest.setFirearmsType(firearmsType);
			}
			
			String firearmMake = XmlUtils.xPathStringSearch(firearmNode, "firearm-search-req-ext:FirearmMakeText");			
			if(StringUtils.isNotBlank(firearmMake)){
				firearmsSearchRequest.setMake(firearmMake);
			}
			
			String firearmModel = XmlUtils.xPathStringSearch(firearmNode, "nc:ItemModelName");
			if(StringUtils.isNotBlank(firearmModel)){
				firearmsSearchRequest.setModel(firearmModel);
			}
			
			
			// get the metadata attribute, used as a pointer to reference the search qualifier in an element 
			// futher down at the bottom of the xml doc
			Node serialNumNode = XmlUtils.xPathNodeSearch(firearmNode, "nc:ItemSerialIdentification/nc:IdentificationID");
			
			String serialNumMetaVal = null;
			
			if (serialNumNode != null)
			{	
				serialNumMetaVal = serialNumNode.getAttributes().getNamedItemNS(OjbcNamespaceContext.NS_STRUCTURES, "metadata").getNodeValue();
			}	
			
			if(StringUtils.isNotBlank(serialNumMetaVal)){
				
				String serialNumQualCode = XmlUtils.xPathStringSearch(inputDoc, 
						"/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:SearchMetadata[@s:id='"+ serialNumMetaVal + "']/firearm-search-req-ext:SearchQualifierCode");
				
				firearmsSearchRequest.setSerialNumberQualifierCode(serialNumQualCode);									
			}						
		}
		
		Node itemRegNode = XmlUtils.xPathNodeSearch(inputDoc, 
				"/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
		
		if(itemRegNode != null){
			
			String regId = XmlUtils.xPathStringSearch(itemRegNode, 
					"nc:RegistrationIdentification/nc:IdentificationID");
			
			if(StringUtils.isNotBlank(regId)){
				firearmsSearchRequest.setRegistrationNumber(regId);
			}
			
			
			String isRegCurrent = XmlUtils.xPathStringSearch(itemRegNode, "firearm-search-req-ext:CurrentRegistrationIndicator");
			if(StringUtils.isNotBlank(isRegCurrent)){
				
				boolean regCurrentFromXml = Boolean.valueOf(isRegCurrent);
				
				firearmsSearchRequest.setCurrentRegistrationsOnly(regCurrentFromXml);
			}		
		}						
		
		
        NodeList sourceSystems = XmlUtils.xPathNodeListSearch(inputDoc, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:SourceSystemNameText");

        List<String> sourceSystemsList = new ArrayList<String>();
        
        if (sourceSystems != null) {
            int length = sourceSystems.getLength();
            for (int i = 0; i < length; i++) {
                if (sourceSystems.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element sourceSystem = (Element) sourceSystems.item(i);
                    
                    if (StringUtils.isNotBlank(sourceSystem.getTextContent()))
                    {
                    	sourceSystemsList.add(sourceSystem.getTextContent());
                    }		
                }
            }
        }
        
        String onBehalfOfText = XmlUtils.xPathStringSearch(inputDoc, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:SearchMetadata/firearm-search-req-ext:SearchRequestOnBehalfOfText");
        
        if (StringUtils.isNotEmpty(onBehalfOfText)) {
        	firearmsSearchRequest.setOnBehalfOf(onBehalfOfText);
        }

        String purposeText = XmlUtils.xPathStringSearch(inputDoc, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:SearchMetadata/firearm-search-req-ext:SearchPurposeText");
        
        if (StringUtils.isNotEmpty(purposeText)) {
        	firearmsSearchRequest.setPurpose(purposeText);
        }
        
        firearmsSearchRequest.setSystemsToSearch(sourceSystemsList);

        log.debug("Firearms Search Request: " + firearmsSearchRequest.toString());

        return firearmsSearchRequest;
	}

}
