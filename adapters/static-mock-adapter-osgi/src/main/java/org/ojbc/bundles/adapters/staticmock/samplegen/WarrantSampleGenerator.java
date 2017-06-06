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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A sample generator that creates random warrant instances.  The class is a singleton (create by calling getInstance() ).
 *
 */
public final class WarrantSampleGenerator extends AbstractPersonSampleGenerator {

    private static final Log LOG = LogFactory.getLog(WarrantSampleGenerator.class);

    WarrantSampleGenerator() throws ParserConfigurationException, IOException {
        super();
    }

    private static WarrantSampleGenerator INSTANCE;

    /**
     * Get the singleton instance of this class
     * @return the instance
     */
    public static final WarrantSampleGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new WarrantSampleGenerator();
            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }

    @Override
    protected List<Document> generateSample(Collection<PersonElementWrapper> people, DateTime baseDate, String stateParam) throws Exception {

        List<Document> personDocuments = new ArrayList<Document>();
        
        LOG.info("Processing " + people.size() + " records");
        
        for (PersonElementWrapper person : people) {

            Document ret = documentBuilder.newDocument();
            personDocuments.add(ret);
        
            Element e = null;

            e = ret.createElementNS(OjbcNamespaceContext.NS_WARRANT, "Warrants");
            ret.appendChild(e);
            e.setPrefix(OjbcNamespaceContext.NS_PREFIX_WARRANT);

            e = appendElement(e, OjbcNamespaceContext.NS_WARRANT_EXT, "eBWResults");

            addPersonElement(e, person, baseDate);
            addLocationElement(e, person);
            
            e = appendElement(e, OjbcNamespaceContext.NS_WARRANT_EXT, "eBWResult");
            Element ebw = e;
            
            e = appendElement(e, OjbcNamespaceContext.NS_JXDM_41, "Case");
            Element caseElement = e;
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "ActivityStatus");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
            e.setTextContent("Open");
            
            e = appendElement(caseElement, OjbcNamespaceContext.NS_NC, "CaseDocketID");
            e.setTextContent(generateRandomID("C", 2) + "-" + generateRandomID("", 4));
            
            e = appendElement(ebw, OjbcNamespaceContext.NS_WARRANT_EXT, "eBWWarrantStatus");
            e.setTextContent("Active");
            
            e = appendElement(ebw, OjbcNamespaceContext.NS_JXDM_41, "Bail");
            Element bail = e;
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "ActivityStatus");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
            e.setTextContent("Posted");
            e = appendElement(bail, OjbcNamespaceContext.NS_JXDM_41, "BailSetAmount");
            e.setTextContent(NumberFormat.getCurrencyInstance().format(generateUniformDouble(1000.00)).replace("$", ""));
            
            e = appendElement(ebw, OjbcNamespaceContext.NS_JXDM_41, "Warrant");
            Element warrant = e;
            
            e = appendElement(warrant, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(generateRandomID("WRT", 10));
            e = appendElement(warrant, OjbcNamespaceContext.NS_NC, "ActivityStatus");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
            e.setTextContent("Active");
            e = appendElement(warrant, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderDesignatedSubject");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
            e = appendElement(warrant, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderIssuingDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            DateTime orderDate = generateNormalRandomDateBefore(baseDate, 30);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            e.setTextContent(fmt.print(orderDate));
            e = appendElement(warrant, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderJurisdiction");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "JurisdictionText");
            e.setTextContent("District Court");
            e = appendElement(warrant, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderStatus");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
            e.setTextContent("Open");
            e = appendElement(warrant, OjbcNamespaceContext.NS_JXDM_41, "WarrantLevelText");
            e.setTextContent(generateRandomCodeFromList("Traffic", "Felony"));

            OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(ret.getDocumentElement());

        }
        return personDocuments;

    }
    
    private void addPersonElement(Element parentElement, PersonElementWrapper person, DateTime baseDate) {
        
        Element personElement = appendElement(parentElement, OjbcNamespaceContext.NS_JXDM_41, "Person");
        
        int ageInYears = Years.yearsBetween(person.birthdate, baseDate).getYears();

        String personID = "P" + person.id;
        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", personID);
        person.personId = personID;
        Element e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        e.setTextContent(String.valueOf(ageInYears));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        e.setTextContent(fmt.print(person.birthdate));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHeightMeasure");
        Element phm = e;
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
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
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonOtherIdentification");
        Element oid = e;
        e = appendElement(oid, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("", 8));
        e = appendElement(oid, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent("CMS");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceCode");
        e.setTextContent(generateRandomCodeFromList("B", "A", "W", "U", "I"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
        Element ssn = e;
        e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(person.nationalID);
        e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent("SSA");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonWeightMeasure");
        Element pwm = e;
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        e.setTextContent(person.pounds);
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "WeightUnitCode");
        e.setTextContent("LBR");
        e = appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "DriverLicense");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        Element dl = e;
        e = appendElement(dl, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID(person.state, 7));
        e = appendElement(dl, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent(person.state);
        
    }
    
    private void addLocationElement(Element parentElement, PersonElementWrapper person)
    {
        Element locationElement = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "Location");
        Element e = appendElement(locationElement, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "StructuredAddress");
        Element sa = e;
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "LocationStreet");
        Element streetElement = e;
        e = appendElement(streetElement, OjbcNamespaceContext.NS_NC, "StreetNumberText");
        e.setTextContent(person.addressStreetNumber);
        e = appendElement(streetElement, OjbcNamespaceContext.NS_NC, "StreetName");
        e.setTextContent(person.addressStreetName);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationCityName");
        e.setTextContent(person.city);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationStateNCICLSTACode");
        e.setTextContent(person.state);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationPostalCode");
        e.setTextContent(person.zipCode);
    }

}
