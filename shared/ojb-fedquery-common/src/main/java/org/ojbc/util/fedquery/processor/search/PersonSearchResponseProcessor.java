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
package org.ojbc.util.fedquery.processor.search;

import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.ojbc.util.fedquery.model.PersonSearchResponse;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A Camel processor bean that builds a person search response XML document from an equivalent POJO.
 * 
 */
public class PersonSearchResponseProcessor {

    private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();

    private String systemName;
    private String sourceSystemNameText;
    private String searchResultCategoryText;
    
    /**
     * Convert the list of POJOs to the equivalent XML document
     * @throws ParserConfigurationException 
     */
    public Document buildPersonSearchResponseDoc(List<PersonSearchResponse> personSearchResponseList) 
    		throws ParserConfigurationException {

        Document doc = createNewDocument();

        Element root = doc.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC, "PersonSearchResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_DOC);
        
        for (PersonSearchResponse searchResponse : personSearchResponseList) {

            Element personSearchResultElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "PersonSearchResult");

            Element personElement = XmlUtils.appendElement(personSearchResultElement, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "Person");
            
            Date dob = searchResponse.getDob();
                            
            if (dob != null) {
            	
            	// set age
                Element personAgeMeasure = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
                Element ageValueElement = XmlUtils.appendElement(personAgeMeasure, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
                int age = getAge(searchResponse.getDob());
                setElementContent(ageValueElement, String.valueOf(age));
                                    
                // set birth date
                Element personBirthDateElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
            	DateTime jtDobDate = new DateTime(dob);
            	Element dateElement = XmlUtils.appendElement(personBirthDateElement, OjbcNamespaceContext.NS_NC, "Date");
            	setElementContent(dateElement, jtDobDate.toString("yyyy-MM-dd"));	                   
            }

            
            String firstName = searchResponse.getFirstName();
            String middleName = searchResponse.getMiddleName();
            String lastName = searchResponse.getLastName();
            
            if(StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(middleName) || StringUtils.isNotBlank(lastName)){
            	
            	Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
            	
                if(StringUtils.isNotBlank(firstName)){                	                	
                	Element personFirstNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
                	setElementContent(personFirstNameElement, firstName);
                }
                                                  
                if(StringUtils.isNotBlank(middleName)){                	
                    Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
                    setElementContent(personMiddleNameElement, middleName);
                }
                                
                if(StringUtils.isNotBlank(lastName)){
                    Element personLastNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
                    setElementContent(personLastNameElement, lastName);
                }             	                	
            }
            
            
            String race = searchResponse.getRace();
            if(StringUtils.isNotBlank(race)){
                Element raceCodeElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceCode");
                setElementContent(raceCodeElement, race);                	
            }
            
            
            String sexCode = searchResponse.getSex();
            if(StringUtils.isNotBlank(sexCode)){                	
                Element personSexCodeElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
                setElementContent(personSexCodeElement, sexCode);                	                	
            }
            

            String ssn = searchResponse.getSsn();
            if(StringUtils.isNotBlank(ssn)){
                Element personSsnIdElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
                Element identificatinoIdElement = XmlUtils.appendElement(personSsnIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
                setElementContent(identificatinoIdElement, ssn);                	                	
            }
            
            
            String driverLicenseId = searchResponse.getDriverLicenseId();
            String fbiNumber = searchResponse.getFbiNumber();
            String stateId = searchResponse.getStateId();
            
            if(StringUtils.isNotBlank(driverLicenseId) || StringUtils.isNotBlank(fbiNumber) || StringUtils.isNotBlank(stateId)){
            	
            	Element personAugmentationElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
            	
                if(StringUtils.isNotBlank(driverLicenseId)){
                    Element driverLicenseElement = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
                    Element driverIdElement = XmlUtils.appendElement(driverLicenseElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
                    Element driverIdValueElement = XmlUtils.appendElement(driverIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    setElementContent(driverIdValueElement, driverLicenseId);                	
                }                                
                
                if (StringUtils.isNotBlank(searchResponse.getFbiNumber())) {
                    Element personFbiIdElement = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
                    Element fbiIdValueElement = XmlUtils.appendElement(personFbiIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    setElementContent(fbiIdValueElement, fbiNumber);
                }         
                                    
                if (StringUtils.isNotBlank(stateId)) {
                    Element personStateFingerprintIdElement = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");
                    Element fingerprintIdValueElement = XmlUtils.appendElement(personStateFingerprintIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    setElementContent(fingerprintIdValueElement, stateId);
                }
            }
            

            Element sourceSystemNameTextElement = XmlUtils.appendElement(personSearchResultElement, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "SourceSystemNameText");
            setElementContent(sourceSystemNameTextElement, sourceSystemNameText);

            Element systemIdentifierElement = XmlUtils.appendElement(personSearchResultElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");

            Element systemIdentifierIdElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
            setElementContent(systemIdentifierIdElement, searchResponse.getSystemId());

            Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
            setElementContent(systemNameElement, systemName);

            if (StringUtils.isBlank(searchResultCategoryText))
            {
            	searchResultCategoryText = "Firearms Reg.";
            }	
            
            Element searchResultCategoryTextElement = XmlUtils.appendElement(personSearchResultElement, 
            		OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "SearchResultCategoryText");
            setElementContent(searchResultCategoryTextElement, searchResultCategoryText);

        }

        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
    }

    private void setElementContent(Element e, String value) {
        e.setTextContent(value == null ? null : value.trim());
    }

    public static int getAge(Date birthDate) {
        DateTime todayDT = new DateTime();
        DateTime birthDateDT = new DateTime(birthDate);
        return Years.yearsBetween(birthDateDT, todayDT).getYears();
    }
    
    
    private Document createNewDocument() throws ParserConfigurationException{
	    
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);

        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        
        return doc; 	
    }

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSourceSystemNameText() {
		return sourceSystemNameText;
	}

	public void setSourceSystemNameText(String sourceSystemNameText) {
		this.sourceSystemNameText = sourceSystemNameText;
	}

	public String getSearchResultCategoryText() {
		return searchResultCategoryText;
	}

	public void setSearchResultCategoryText(String searchResultCategoryText) {
		this.searchResultCategoryText = searchResultCategoryText;
	}

}




