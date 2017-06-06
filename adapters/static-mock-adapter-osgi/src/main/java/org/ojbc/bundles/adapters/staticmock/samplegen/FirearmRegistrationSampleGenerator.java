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
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FirearmRegistrationSampleGenerator extends AbstractPersonSampleGenerator {

    private static final DateTimeFormatter XML_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final Log LOG = LogFactory.getLog(FirearmRegistrationSampleGenerator.class);
    private static FirearmRegistrationSampleGenerator INSTANCE;

    FirearmRegistrationSampleGenerator() throws ParserConfigurationException, IOException {
        super();
    }

    /**
     * Get the singleton instance of this class
     * 
     * @return the instance
     */
    public static final FirearmRegistrationSampleGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new FirearmRegistrationSampleGenerator();
            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }

    @Override
    protected List<Document> generateSample(Collection<PersonElementWrapper> people, DateTime baseDate, String state) throws Exception {

        List<Document> personDocuments = new ArrayList<Document>();

        LOG.info("Processing " + people.size() + " records");

        for (PersonElementWrapper person : people) {

            Document ret = documentBuilder.newDocument();
            personDocuments.add(ret);

            Element rootElement = ret.createElementNS(OjbcNamespaceContext.NS_FIREARM_DOC, "PersonFirearmRegistrationQueryResults");
            ret.appendChild(rootElement);
            rootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_FIREARM_DOC);

            addPersonElement(rootElement, person, baseDate);
            int firearmCount = generatePoissonInt(.7, true);
            for (int i = 0; i < firearmCount; i++) {
                addFirearmElement(rootElement, baseDate, i + 1);
            }
            for (int i = 0; i < firearmCount; i++) {
                addRegistrationElement(rootElement, person, baseDate, i + 1);
            }

            addDriverLicenseElement(rootElement, person);
                        
            addLocationElement(rootElement, person);

            Element e = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "ContactInformation");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "id", "CI1");
            addTelephoneNumberElement(e, "360-555-1212");

            e = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "ContactInformation");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "id", "CI2");
            addTelephoneNumberElement(e, "212-333-8888");

            e = appendElement(e, OjbcNamespaceContext.NS_NC, "ContactEntity");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "EntityOrganization");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "OrganizationLocation");
            addLocationElement(e, "123 Commerce Way", person);

            Element assn = null;

            for (int i = 0; i < firearmCount; i++) {
                assn = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "PropertyRegistrationAssociation");
                e = appendElement(assn, OjbcNamespaceContext.NS_NC, "ItemRegistrationReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "R" + (i + 1));
                e = appendElement(assn, OjbcNamespaceContext.NS_NC, "ItemReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "F" + (i + 1));
                e = appendElement(assn, OjbcNamespaceContext.NS_NC, "ItemRegistrationHolderReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            }

            assn = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "ResidenceAssociation");
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "PersonReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "LocationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "L1");

            assn = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "PersonContactInformationAssociation");
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "PersonReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "CI1");

            assn = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "PersonEmploymentAssociation");
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "EmployeeReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "EmployeeOccupationText");
            e.setTextContent(person.occupation);

            assn = appendElement(rootElement, OjbcNamespaceContext.NS_NC, "EmployeeContactInformationAssociation");
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "PersonReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            e = appendElement(assn, OjbcNamespaceContext.NS_NC, "ContactInformationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "CI2");

            OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(ret.getDocumentElement());

        }

        return personDocuments;

    }

    private void addDriverLicenseElement(Element rootElement, PersonElementWrapper person) {

    	Element dlElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
		
    	Element drivLicIdElement = XmlUtils.appendElement(dlElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
    	
    	Element dlIdValEl = XmlUtils.appendElement(drivLicIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
    	    	
    	String sampleDl = RandomStringUtils.randomNumeric(8);
    	
    	dlIdValEl.setTextContent(sampleDl);    	
    	    	
    	Element dlIdSrcTxtElement = XmlUtils.appendElement(drivLicIdElement, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
    	
    	dlIdSrcTxtElement.setTextContent(person.state);    	    	
	}

	private void addTelephoneNumberElement(Element parentElement, String phoneNumber) {
        Element e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "ContactTelephoneNumber");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "FullTelephoneNumber");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "TelephoneNumberFullID");
        e.setTextContent(phoneNumber);
    }

    private void addRegistrationElement(Element rootElement, PersonElementWrapper person, DateTime baseDate, int firearmIndex) {

        Element registrationElement = appendElement(rootElement, OjbcNamespaceContext.NS_FIREARM_EXT, "ItemRegistration");
        XmlUtils.addAttribute(registrationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "R" + firearmIndex);
        Element e = appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "RegistrationIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("", 12));
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "LocationCountyName");
        e.setTextContent(getRandomCounty(person.state));
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "RegistrationEffectiveDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        DateTime registrationDate = generateNormalRandomDateBefore(baseDate, 5 * 365);
        e.setTextContent(XML_DATE_FORMAT.print(registrationDate));
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_EXT, "AgeAtRegistration");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent("" + Years.yearsBetween(person.birthdate, registrationDate).getYears());
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_EXT, "PermitNumber");
        e.setTextContent(generateRandomID("", 12));
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_EXT, "PermitDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(XML_DATE_FORMAT.print(registrationDate));
        Element statusElement = appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_EXT, "RegistrationStatus");
        e = appendElement(statusElement, OjbcNamespaceContext.NS_NC, "StatusDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(XML_DATE_FORMAT.print(registrationDate));
        e = appendElement(statusElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmRegistrationStatusText");
        e.setTextContent(coinFlip(.7) ? "Active" : "Inactive");
        e = appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_EXT, "RegistrationNotesText");
        e.setTextContent("Notes");

    }

    private void addFirearmElement(Element rootElement, DateTime baseDate, int firearmIndex) throws IOException {

        Element firearmElement = appendElement(rootElement, OjbcNamespaceContext.NS_FIREARM_EXT, "Firearm");
        XmlUtils.addAttribute(firearmElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "F" + firearmIndex);
        Element e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "ItemSerialIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("", 10));
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "ItemModelName");
        e.setTextContent("Firearm");
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCategoryCode");
        e.setTextContent(generateRandomCodeFromList("P", "R", "J", "I"));
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCategoryDescriptionCode");
        e.setTextContent(generateRandomCodeFromList("A", "B", "C", "D", "E"));
        Element barrelLengthElement = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmBarrelLengthMeasure");
        e = appendElement(barrelLengthElement, OjbcNamespaceContext.NS_NC, "MeasureText");
        int length = (int) (5 + generateUniformDouble(40.0));
        e.setTextContent("" + length);
        e = appendElement(barrelLengthElement, OjbcNamespaceContext.NS_NC, "LengthUnitCode");
        e.setTextContent("INH");
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCaliberCode");
        e.setTextContent(generateRandomCodeFromList("22", "357", "9", "44"));
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmGaugeText");
        e.setTextContent("Gauge");
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARMS_CODES_DEMOSTATE, "FirearmMakeCode");
        String makeCode = generateRandomCodeFromList("REM", "SMI", "BER", "GLC");
        e.setTextContent(makeCode);
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmMakeText");
        if ("REM".equals(makeCode)) {
            e.setTextContent("FieldmasterMfd. by Remington Arms");
        } else if ("SMI".equals(makeCode)) {
            e.setTextContent("Smith (not Smith & Wesson)");
        } else if ("BER".equals(makeCode)) {
            e.setTextContent("BantamModel mfd. by Beretta");
        } else {
            e.setTextContent("Glock, Inc.");
        }
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmReceivedDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(XML_DATE_FORMAT.print(generateNormalRandomDateBefore(baseDate, 3 * 365)));
        e = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmImporter");
        e.setTextContent("Unknown");
        Element acquisitionElement = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmAcquisition");
        e = appendElement(acquisitionElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmAcquisitionSourceDescriptionText");
        PersonElementWrapper pew = getRandomIdentity(null);
        e.setTextContent(pew.firstName + "'s " + generateRandomCodeFromList("Gun Shop", "Firearms", "Sporting Goods"));
        e = appendElement(acquisitionElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmAcquisitionLocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "AddressFullText");
        e.setTextContent(pew.addressStreetNumber + " " + pew.addressStreetName);
        if (coinFlip(.2)) {
            Element statusElement = appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmStatus");
            e = appendElement(statusElement, OjbcNamespaceContext.NS_NC, "StatusDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            e.setTextContent(XML_DATE_FORMAT.print(generateNormalRandomDateBefore(baseDate, 1 * 365)));
            e = appendElement(statusElement, OjbcNamespaceContext.NS_FIREARM_EXT, "FirearmStatusText");
            e.setTextContent(generateRandomCodeFromList("Destroyed", "Lost", "Stolen"));
        }
    }

    private void addPersonElement(Element parentElement, PersonElementWrapper person, DateTime baseDate) {

        Element e;
        Element personElement = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "Person");

        String personID = "P" + person.id;
        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", personID);
        person.personId = personID;

        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        DateTimeFormatter fmt = XML_DATE_FORMAT;
        e.setTextContent(fmt.print(person.birthdate));

        Element birthLoc = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthLocation");
        e = appendElement(birthLoc, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "StructuredAddress");
        Element addr = e;
        e = appendElement(addr, OjbcNamespaceContext.NS_NC, "LocationCityName");
        e.setTextContent(person.city);
        e = appendElement(addr, OjbcNamespaceContext.NS_NC, "LocationStateName");
        e.setTextContent(person.state);

        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonCitizenshipText");
        e.setTextContent("USA");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonComplexionText");
        e.setTextContent("Fair");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonEyeColorCode");
        e.setTextContent(generateRandomCodeFromList("BRO", "BLK", "BLU", "HAZ"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHairColorCode");
        e.setTextContent(generateRandomCodeFromList("BRO", "BLK", "RED", "BLN"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHeightMeasure");
        Element phm = e;
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent(String.valueOf(Math.round(Integer.parseInt(person.centimeters) * .39)));
        e = appendElement(phm, OjbcNamespaceContext.NS_NC, "LengthUnitCode");
        e.setTextContent("INH");

        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
        Element pn = e;
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
        e.setTextContent(person.middleName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonFullName");
        e.setTextContent(person.firstName + " " + person.middleName + " " + person.lastName);
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceCode");
        e.setTextContent(generateRandomCodeFromList("B", "A", "W", "U", "I"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
        Element ssn = e;
        e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(person.nationalID);
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonStateIdentification");
        Element stateId = e;
        e = appendElement(stateId, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID(person.state, 6));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonWeightMeasure");
        Element pwm = e;
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent(person.pounds);
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "WeightUnitCode");
        e.setTextContent("LBR");

    }

    private void addLocationElement(Element parentElement, String streetFullText, PersonElementWrapper person) {
        Element e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "StructuredAddress");
        Element sa = e;
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "AddressDeliveryPointText");
        e.setTextContent(streetFullText);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationCityName");
        e.setTextContent(person.city);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationStateName");
        e.setTextContent(person.state);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationPostalCode");
        e.setTextContent(person.zipCode);
    }

    private void addLocationElement(Element parentElement, PersonElementWrapper person) {
        Element locationElement = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "Location");
        XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "L1");
        addLocationElement(locationElement, person.addressStreetName + " " + person.addressStreetName, person);
    }

}
