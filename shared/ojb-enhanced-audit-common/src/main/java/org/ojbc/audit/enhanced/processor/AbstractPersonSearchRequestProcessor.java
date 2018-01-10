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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractPersonSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractPersonSearchRequestProcessor.class);
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public abstract void auditPersonSearchRequest(Exchange exchange, @Body Document document);
	
	PersonSearchRequest processPersonSearchRequest(Document document) throws Exception
	{
		PersonSearchRequest personSearchRequest = new PersonSearchRequest();

        Node personNode = XmlUtils.xPathNodeSearch(document, "/psr-doc:PersonSearchRequest/psr:Person");

        if (personNode != null) {

            String ssn = XmlUtils.xPathStringSearch(personNode, "nc:PersonSSNIdentification/nc:IdentificationID");
            if (StringUtils.isNotEmpty(ssn)) {
                personSearchRequest.setSsn(ssn);
            }

            Node personName = XmlUtils.xPathNodeSearch(personNode, "nc:PersonName");

            String personGivenNameID = XmlUtils.xPathStringSearch(personName, "nc:PersonGivenName/@s:metadata");

            if (StringUtils.isNotEmpty(personGivenNameID)) {

                String personGivenNameQualiferCode = XmlUtils.xPathStringSearch(document, 
                		"/psr-doc:PersonSearchRequest/psr:SearchMetadata[@s:id = '" + personGivenNameID + "']/psr:SearchQualifierCode");

                personSearchRequest.setFirstNameQualifierCode(personGivenNameQualiferCode);
                
            }

            String personSurNameID = XmlUtils.xPathStringSearch(personName, "nc:PersonSurName/@s:metadata");

            if (StringUtils.isNotEmpty(personSurNameID)) {

                String personSurNameQualiferCode = XmlUtils.xPathStringSearch(document, 
                		"/psr-doc:PersonSearchRequest/psr:SearchMetadata[@s:id = '" + personSurNameID + "']/psr:SearchQualifierCode");

                personSearchRequest.setLastNameQualifierCode(personSurNameQualiferCode);
            }

            String s = XmlUtils.xPathStringSearch(personName, "nc:PersonSurName");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setLastName(s);
            }

            s = XmlUtils.xPathStringSearch(personName, "nc:PersonGivenName");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setFirstName(s);
            }

            s = XmlUtils.xPathStringSearch(personName, "nc:PersonMiddleName");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setMiddleName(s);
            }

            s = XmlUtils.xPathStringSearch(personNode, "nc:PersonRaceCode");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setRaceCode(s);
            }

            s = XmlUtils.xPathStringSearch(personNode, "nc:PersonSexCode");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setGenderCode(s);
            }

            s = XmlUtils.xPathStringSearch(personNode, "nc:PersonHairColorCode");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setHairCode(s);
            }

            s = XmlUtils.xPathStringSearch(personNode, "nc:PersonEyeColorCode");
            if (StringUtils.isNotEmpty(s)) {
                personSearchRequest.setEyeCode(s);
            }

            String birthDateStartString = XmlUtils.xPathStringSearch(personNode, "psr:PersonBirthDateRange/nc:StartDate/nc:Date");

            String birthDateEndString = XmlUtils.xPathStringSearch(personNode, "psr:PersonBirthDateRange/nc:EndDate/nc:Date");

            if (StringUtils.isNotEmpty(birthDateStartString))
            {
            	personSearchRequest.setDobFrom(LocalDate.parse(birthDateStartString, formatter));
            }		

            if (StringUtils.isNotEmpty(birthDateEndString))
            {
            	personSearchRequest.setDobTo(LocalDate.parse(birthDateEndString, formatter));
            }		
            
            String dlId = XmlUtils.xPathStringSearch(personNode, 
            		"jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID");
   	 	 
            if(StringUtils.isNotBlank(dlId)){
            	personSearchRequest.setDriverLicenseId(dlId);
            }
                        
            String dlIssuer = XmlUtils.xPathStringSearch(personNode, 
            		"jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText");
            
            if(StringUtils.isNotBlank(dlIssuer)){
            	personSearchRequest.setDriverLiscenseIssuer(dlIssuer);
            }
            
            
            String fbiNum = XmlUtils.xPathStringSearch(personNode, 
            		"jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
            
            if(StringUtils.isNotBlank(fbiNum)){
            	personSearchRequest.setFbiNumber(fbiNum);
            }
            
            String stateId = XmlUtils.xPathStringSearch(personNode, 
            		"jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
            
            if(StringUtils.isNotBlank(stateId)){
            	personSearchRequest.setStateId(stateId);
            }
            
        }
        
        NodeList sourceSystems = XmlUtils.xPathNodeListSearch(document, "/psr-doc:PersonSearchRequest/psr:SourceSystemNameText");

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
        
//        <SearchMetadata xmlns="http://ojbc.org/IEPD/Extensions/PersonSearchRequest/1.0" NS1:id="OBO3">
//	        <SearchRequestOnBehalfOfText>test</SearchRequestOnBehalfOfText>
//	        <SearchPurposeText>Criminal Justice (C)</SearchPurposeText>
//	    </SearchMetadata>
        
        String onBehalfOfText = XmlUtils.xPathStringSearch(document, "/psr-doc:PersonSearchRequest/psr:SearchMetadata/psr:SearchRequestOnBehalfOfText");
        
        if (StringUtils.isNotEmpty(onBehalfOfText)) {
            personSearchRequest.setOnBehalfOf(onBehalfOfText);
        }

        String purposeText = XmlUtils.xPathStringSearch(document, "/psr-doc:PersonSearchRequest/psr:SearchMetadata/psr:SearchPurposeText");
        
        if (StringUtils.isNotEmpty(purposeText)) {
            personSearchRequest.setPurpose(purposeText);
        }
        
        personSearchRequest.setSystemsToSearch(sourceSystemsList);

        log.debug("Person Search Request: " + personSearchRequest.toString());

        return personSearchRequest;
	}

}
