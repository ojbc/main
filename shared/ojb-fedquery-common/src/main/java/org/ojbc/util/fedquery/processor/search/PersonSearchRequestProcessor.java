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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.fedquery.model.PersonSearchRequest;
import org.ojbc.util.helper.HeightUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Node;

/**
 * A Camel processor bean that builds a person search request POJO from an equivalent XML document.
 */
public class PersonSearchRequestProcessor {

    private static final Log log = LogFactory.getLog(PersonSearchRequestProcessor.class);

    /**
     * Receives an xml document with search request parameters coming from the portal. 
     * Pulls these parameters out and populates a model pojo with their values
     * 
     * @throws Exception
     */
    public PersonSearchRequest buildPersonSearchRequest(org.w3c.dom.Document document) throws Exception {
    	    	    	
        PersonSearchRequest personSearchRequest = new PersonSearchRequest();

        Node personNode = XmlUtils.xPathNodeSearch(document, "/psr-doc:PersonSearchRequest/psr:Person");

        if (personNode != null) {

            String ssn = XmlUtils.xPathStringSearch(personNode, "nc:PersonSSNIdentification/nc:IdentificationID");
            if (StringUtils.isNotEmpty(ssn)) {
            	//Maui stored procedure inputs require ssn without hyphens
            	String ssnNoDashes = ssn.replace("-", "");            	
                personSearchRequest.setSsn(ssnNoDashes);
            }

            Node personName = XmlUtils.xPathNodeSearch(personNode, "nc:PersonName");

            String personGivenNameID = XmlUtils.xPathStringSearch(personName, "nc:PersonGivenName/@s:metadata");

            if (StringUtils.isNotEmpty(personGivenNameID)) {

                String personGivenNameQualiferCode = XmlUtils.xPathStringSearch(document, 
                		"/psr-doc:PersonSearchRequest/psr:SearchMetadata[@s:id = '" + personGivenNameID + "']/psr:SearchQualifierCode");

                if (StringUtils.isNotEmpty(personGivenNameQualiferCode)) {

                    personSearchRequest.setFirstNameQualifier(personGivenNameQualiferCode);
                }
            }

            String personSurNameID = XmlUtils.xPathStringSearch(personName, "nc:PersonSurName/@s:metadata");

            if (StringUtils.isNotEmpty(personSurNameID)) {

                String personSurNameQualiferCode = XmlUtils.xPathStringSearch(document, 
                		"/psr-doc:PersonSearchRequest/psr:SearchMetadata[@s:id = '" + personSurNameID + "']/psr:SearchQualifierCode");

                if (StringUtils.isNotEmpty(personSurNameQualiferCode)) {

                    personSearchRequest.setLastNameQualifier(personSurNameQualiferCode);
                }
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

            String weight = XmlUtils.xPathStringSearch(personNode, "nc:PersonWeightMeasure/nc:MeasureText");

            if (StringUtils.isNotEmpty(weight)) {

                personSearchRequest.setWeightMin(weight);
                personSearchRequest.setWeightMax(weight);

            } else {

                String weightRangeStart = XmlUtils.xPathStringSearch(personNode, "nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");

                String weightRangeEnd = XmlUtils.xPathStringSearch(personNode, "nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");

                if (StringUtils.isNotEmpty(weightRangeStart) && StringUtils.isNotEmpty(weightRangeEnd)) {

                    personSearchRequest.setWeightMin(weightRangeStart);

                    personSearchRequest.setWeightMax(weightRangeEnd);
                }
            }

            // Note Maui height units are 3 digit 'ft-inches'
            // ex: 24inches height from xml gets converted to 200 for 2 ft zero inches
            String heightInchesFromXml = XmlUtils.xPathStringSearch(personNode, "nc:PersonHeightMeasure/nc:MeasureText");

            if (StringUtils.isNotEmpty(heightInchesFromXml)) {

                String ftInches = convertHeightToFtIn(heightInchesFromXml);

                personSearchRequest.setHeightMin(ftInches);

                personSearchRequest.setHeightMax(ftInches);

            } else {

                String heightRangeInchesStart = XmlUtils.xPathStringSearch(personNode, "nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");

                String heightRangeInchesEnd = XmlUtils.xPathStringSearch(personNode, "nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");

                if (StringUtils.isNotEmpty(heightRangeInchesStart) && StringUtils.isNotEmpty(heightRangeInchesEnd)) {

                    String ftInchesMin = convertHeightToFtIn(heightRangeInchesStart);

                    String ftInchesMax = convertHeightToFtIn(heightRangeInchesEnd);

                    personSearchRequest.setHeightMin(ftInchesMin);

                    personSearchRequest.setHeightMax(ftInchesMax);
                }
            }

            String birthDateStart = XmlUtils.xPathStringSearch(personNode, "psr:PersonBirthDateRange/nc:StartDate/nc:Date");

            String birthDateEnd = XmlUtils.xPathStringSearch(personNode, "psr:PersonBirthDateRange/nc:EndDate/nc:Date");

            if (StringUtils.isNotEmpty(birthDateStart) && StringUtils.isNotEmpty(birthDateEnd)) {
                personSearchRequest.setDobFrom(birthDateStart);
                personSearchRequest.setDobTo(birthDateEnd);
            }
            
            String birthDate = XmlUtils.xPathStringSearch(personNode, "nc:PersonBirthDate/nc:Date");
            
            if (StringUtils.isNotBlank(birthDate))
            {
            	personSearchRequest.setDob(birthDate);
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

        log.debug("Person Search Request: " + personSearchRequest.toString());

        return personSearchRequest;
    }

    public static String convertHeightToFtIn(String pInches) {

        String feet = HeightUtils.returnFeetFromInches(Integer.parseInt(pInches));

        String remainingInches = HeightUtils.returnRemainingInchesFromInches(Integer.valueOf(pInches));

        String feetInches = feet + remainingInches;

        return feetInches;
    }

}
