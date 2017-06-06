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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.ISODateTimeFormat;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A sample generator for incidents.  This will generate a specific number of incident instances, but will create multiple instances for some people (with the count
 * Poisson-distributed.)
 *
 */
public class IncidentSampleGenerator extends AbstractSampleGenerator {

    private static final Log LOG = LogFactory.getLog(IncidentSampleGenerator.class);

    private static IncidentSampleGenerator INSTANCE;

    private List<TangibleObject> objectData;

    /**
     * Get the singleton instance of this class
     * 
     * @return the instance
     */
    public static final IncidentSampleGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new IncidentSampleGenerator();
            } catch (ParserConfigurationException pce) {
                throw new RuntimeException(pce);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }

    IncidentSampleGenerator() throws ParserConfigurationException, IOException {
        super();
        loadObjectData();
    }

    private void loadObjectData() throws IOException {
        objectData = new ArrayList<TangibleObject>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static-files/Objects.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = br.readLine()) != null) {
            TangibleObject to = new TangibleObject();
            StringTokenizer st = new StringTokenizer(line, "\t");
            to.itemDescription = st.nextToken();
            to.category = st.nextToken();
            to.make = st.nextToken();
            to.model = st.nextToken();
            to.id = generateRandomID("", 8);
            objectData.add(to);
        }
    }

    private TangibleObject getRandomObject() {
        int index = randomGenerator.nextInt(0, objectData.size() - 1);
        return objectData.get(index);
    }

    /**
     * Generate a specified number of sample instances.  The number of instances per person is Poisson distributed with a mean of .8.
     * @param recordCount the number of instances to create
     * @param baseDate the base date to use for computing age, etc.
     * @param stateParam the state to restrict instances, or null for any state
     * @return the list of generated instances
     * @throws Exception
     */
    public List<Document> generateSample(int recordCount, DateTime baseDate, String stateParam) throws Exception {
        List<Document> ret = new ArrayList<Document>(recordCount);
        while (ret.size() < recordCount) {
            ret.addAll(buildIncidentInstanceDocuments(baseDate, stateParam));
        }
        int size = ret.size();
        if (size > recordCount) {
            List<Document> trimmed = new ArrayList<Document>(recordCount);
            for (int i = 0; i < recordCount; i++) {
                trimmed.add(ret.get(i));
            }
            ret = trimmed;
        }
        return ret;
    }

    List<Document> buildIncidentInstanceDocuments(DateTime baseDate, String stateParam) throws IOException {

        PersonElementWrapper subject = getRandomIdentity(stateParam);
        int incidents = generatePoissonInt(0.8, true);
        
        LOG.info("Generating " + incidents + " incidents for subject " + subject.lastName);

        List<Document> documents = new ArrayList<Document>();

        for (int i = 0; i < incidents; i++) {

            Incident incident = generateRandomIncident(baseDate, stateParam);
            incident.subject = subject;
            
            while (incident.subject.id.equals(incident.suspect.id) || incident.subject.id.equals(incident.officer.id) || incident.subject.id.equals(incident.victim.id))
            {
                incident = generateRandomIncident(baseDate, stateParam);
                incident.subject = subject;
            }

            Document d = documentBuilder.newDocument();

            Element e = null;

            e = d.createElementNS(OjbcNamespaceContext.NS_IR, "IncidentReport");
            d.appendChild(e);
            e.setPrefix(OjbcNamespaceContext.NS_PREFIX_IR);
            // this is only in here to support validation, if you copy the generated instance to a file and validate it
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "schemaLocation", "http://ojbc.org/IEPD/Exchange/IncidentReport/1.0 ../xsd/exchange_schema/Incident_Report.xsd");

            e = appendElement(e, OjbcNamespaceContext.NS_LEXSPD, "doPublish");
            e = appendElement(e, OjbcNamespaceContext.NS_LEXS, "PublishMessageContainer");
            e = appendElement(e, OjbcNamespaceContext.NS_LEXS, "PublishMessage");

            addMessageMetadataElement(e);
            addDataSubmitterMetadataElement(e, incident);
            addDataItemPackageElement(e, incident, baseDate);
            
            OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(d.getDocumentElement());

            documents.add(d);
        
        }
        
        return documents;

    }

    final class Incident {
        public String incidentID;
        public String ori;
        public String agencyName;
        public String systemID;
        public PersonElementWrapper subject;
        public PersonElementWrapper officer;
        public PersonElementWrapper victim;
        public PersonElementWrapper suspect;
        public Vehicle vehicle;
        public TangibleObject object;
        public String offense;
        public DateTime startDate;
        public DateTime endDate;
        public String reportingOrganizationName;
        public String reportingOrganizationID;

        public Incident(DateTime baseDate, String stateParam) throws IOException {
            incidentID = generateRandomID("I", 10);
            ori = generateRandomID("VT", 5);
            agencyName = "Random LEA";
            systemID = "RMS";
            subject = getRandomIdentity(stateParam);
            officer = getRandomIdentity(stateParam);
            while (officer.id.equals(subject.id)){
                officer = getRandomIdentity(stateParam);
            }
            victim = getRandomIdentity(stateParam);
            while (victim.id.equals(officer.id) || victim.id.equals(subject.id)){
                victim = getRandomIdentity(stateParam);
            }
            suspect = getRandomIdentity(stateParam);
            while (suspect.id.equals(victim.id) || suspect.id.equals(officer.id) || suspect.id.equals(subject.id))
            {
                suspect = getRandomIdentity(stateParam);
            }
            vehicle = new Vehicle(subject, baseDate);
            object = getRandomObject();
            offense = (String) generateRandomValueFromList("Domestic disturbance", "General mayhem", "Suspected assault", "Suspicious Activity");
            startDate = generateUniformRandomDateBetween(baseDate.minusYears(5), baseDate.minusMonths(1));
            endDate = generateUniformRandomTimeBetween(startDate, startDate.plusHours(6));
            reportingOrganizationName = victim.city + " PD";
            if (coinFlip(.1)) {
                reportingOrganizationName = victim.state + " State Police";
            }
            reportingOrganizationID = incidentID + "-Org1";
        }
    }

    final class TangibleObject {
        public String itemDescription;
        public String id;
        public String make;
        public String model;
        public String category;
    }

    final class Vehicle {
        public String make;
        public String model;
        public int year;
        public String vin;
        public String plate;
        public DateTime plateExpiration;
        public String plateState;
        public String color;
        public String colorCode;
        public int doors;
        public String plateTypeCode;
        public String plateTypeText;

        public Vehicle(PersonElementWrapper subject, DateTime baseDate) {
            make = getRandomVehicleMakeCode();
            model = getRandomVehicleModelCode();
            year = randomGenerator.nextInt(1970, 2012);
            vin = generateRandomID("V", 18);
            char[] plateChars = new char[6];
            int plateLetters = randomGenerator.nextInt(1, 3);
            for (int i = 0; i < plateLetters; i++) {
                int pos = randomGenerator.nextInt(0, plateChars.length - 1);
                while (plateChars[pos] != 0) {
                    pos = randomGenerator.nextInt(0, plateChars.length - 1);
                }
                plateChars[pos] = generateRandomLetter().charAt(0);
            }
            for (int i = 0; i < plateChars.length; i++) {
                if (plateChars[i] == 0) {
                    plateChars[i] = ("" + randomGenerator.nextInt(0, 9)).charAt(0);
                }
            }
            plate = new String(plateChars);
            plateExpiration = baseDate.plusMonths(randomGenerator.nextInt(1, 15));
            plateExpiration = plateExpiration.plusDays(randomGenerator.nextInt(-15, 15));
            plateState = subject.state;
            doors = 4;
            if (coinFlip(.2)) {
            	doors = 2;
            }
            if (coinFlip(.01)) {
            	doors = 5;
            }
            plateTypeCode = "PC";
            plateTypeText = "Regular Passenger Automobile Plates";
            color = generateRandomCodeFromList("Black", "White", "Yellow", "Red", "Green", "Grey");
            colorCode = null;
            if ("Black".equals(color)) {
            	colorCode = "BLK";
            } else if ("White".equals(color)) {
            	colorCode = "WHI";
            } else if ("Yellow".equals(color)) {
            	colorCode = "YEL";
            } else if ("Red".equals(color)) {
            	colorCode = "RED";
            } else if ("Green".equals(color)) {
            	colorCode = "GRN";
            } else if ("Grey".equals(color)) {
            	colorCode = "GRY";
            }
        }
    }

    private Incident generateRandomIncident(DateTime baseDate, String stateParam) throws IOException {
        return new Incident(baseDate, stateParam);
    }

    private void addMessageMetadataElement(Element publishMessageElement) {
        Element messageMetadataElement = appendElement(publishMessageElement, OjbcNamespaceContext.NS_LEXS, "PDMessageMetadata");
        Element e = appendElement(messageMetadataElement, OjbcNamespaceContext.NS_LEXS, "LEXSVersion");
        e.setTextContent("3.1.4");
        e = appendElement(messageMetadataElement, OjbcNamespaceContext.NS_LEXS, "MessageDateTime");
        e.setTextContent(ISODateTimeFormat.dateTime().print(new DateTime()));
        e = appendElement(messageMetadataElement, OjbcNamespaceContext.NS_LEXS, "MessageSequenceNumber");
        e.setTextContent("1");
        e = appendElement(messageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataSensitivity");
        e.setTextContent("SBU");
    }

    private void addDataSubmitterMetadataElement(Element publishMessageElement, Incident incident) throws IOException {

        Element dataSubmitterMetadataElement = appendElement(publishMessageElement, OjbcNamespaceContext.NS_LEXS, "DataSubmitterMetadata");
        Element systemIdentifierElement = appendElement(dataSubmitterMetadataElement, OjbcNamespaceContext.NS_LEXS, "SystemIdentifier");
        addSystemIdentifierDetail(incident, systemIdentifierElement);
        Element systemContactElement = appendElement(dataSubmitterMetadataElement, OjbcNamespaceContext.NS_LEXS, "SystemContact");
        addRandomContactInfoElement(systemContactElement);
        Element e = appendElement(systemContactElement, OjbcNamespaceContext.NS_NC, "OrganizationName");
        e.setTextContent("Random LEA");

    }

    private void addSystemIdentifierDetail(Incident incident, Element parentElement) {
        Element e = appendElement(parentElement, OjbcNamespaceContext.NS_LEXS, "ORI");
        e.setTextContent(incident.ori);
        e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "OrganizationName");
        e.setTextContent(incident.agencyName);
        e = appendElement(parentElement, OjbcNamespaceContext.NS_LEXS, "SystemID");
        e.setTextContent(incident.systemID);
    }

    private void addRandomContactInfoElement(Element parentElement) throws IOException {
        Element e;
        PersonElementWrapper contactPerson = getRandomIdentity("VT");
        e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(contactPerson.firstName);
        e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(contactPerson.lastName);
        e = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "PersonFullName");
        e.setTextContent(contactPerson.firstName + " " + contactPerson.middleName.substring(0, 1) + ". " + contactPerson.lastName);
        Element contactPhoneNumberElement = appendElement(parentElement, OjbcNamespaceContext.NS_NC, "ContactTelephoneNumber");
        Element phoneNumberElement = appendElement(contactPhoneNumberElement, OjbcNamespaceContext.NS_NC, "NANPTelephoneNumber");
        e = appendElement(phoneNumberElement, OjbcNamespaceContext.NS_NC, "TelephoneAreaCodeID");
        e.setTextContent(contactPerson.telephoneAreaCode);
        e = appendElement(phoneNumberElement, OjbcNamespaceContext.NS_NC, "TelephoneExchangeID");
        e.setTextContent(contactPerson.telephoneExchange);
        e = appendElement(phoneNumberElement, OjbcNamespaceContext.NS_NC, "TelephoneLineID");
        e.setTextContent(contactPerson.telephoneLine);
    }

    private void addDataItemPackageElement(Element publishMessageElement, Incident incident, DateTime baseDate) throws IOException {

        Element e = appendElement(publishMessageElement, OjbcNamespaceContext.NS_LEXS, "DataItemPackage");
        addPackageMetadataElement(e, incident);
        addDigestElement(e, incident, baseDate);
        addNDExStructuredPayloadElement(e, incident);
        addOJBCStructuredPayloadElement(e, incident);
    }

    private void addPackageMetadataElement(Element dataItemPackageElement, Incident incident) throws IOException {

        Element packageMetadataElement = appendElement(dataItemPackageElement, OjbcNamespaceContext.NS_LEXS, "PackageMetadata");
        Element e = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemID");
        e.setTextContent(incident.incidentID);
        Element dataItemContactElement = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemContact");
        addRandomContactInfoElement(dataItemContactElement);
        e = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemReferenceID");
        e.setTextContent(incident.incidentID);
        e = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemPublishInstruction");
        e.setTextContent("Insert");
        e = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemStatus");
        e.setTextContent("Closed");
        Element dataOwnerMetadataElement = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataOwnerMetadata");
        e = appendElement(dataOwnerMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataOwnerIdentifier");
        addSystemIdentifierDetail(incident, e);
        e = appendElement(dataOwnerMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataOwnerContact");
        addRandomContactInfoElement(e);
        Element disseminationCriteriaElement = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DisseminationCriteriaValue");
        e = appendElement(disseminationCriteriaElement, OjbcNamespaceContext.NS_LEXS, "DisseminationCriteriaDomainText");
        e.setTextContent("N-DEx");
        e = appendElement(disseminationCriteriaElement, OjbcNamespaceContext.NS_LEXS, "DisseminationCriteriaText");
        e.setTextContent("Green");
        e = appendElement(packageMetadataElement, OjbcNamespaceContext.NS_LEXS, "DataItemCategory");
        e = appendElement(e, OjbcNamespaceContext.NS_LEXS, "DataItemCategoryText");
        e.setTextContent("Incident Report");

    }

    private void addDigestElement(Element dataItemPackageElement, Incident incident, DateTime baseDate) {

        Element digestElement = appendElement(dataItemPackageElement, OjbcNamespaceContext.NS_LEXS, "Digest");
        addIncidentActivityElement(incident, digestElement);
        addOffenseActivityElement(incident, digestElement);
        addVictimPersonElement(incident, digestElement, baseDate);
        addSuspectPersonElement(incident, digestElement, baseDate);
        addOfficerPersonElement(incident, digestElement);
        addSubjectPersonElement(incident, digestElement, baseDate);
        addReportingAgencyElement(incident, digestElement);
        addSubjectPhoneElement(incident, digestElement);
        addLocationElement(incident, digestElement, incident.subject, "subject-residence");
        addLocationElement(incident, digestElement, incident.victim, "incident-location");
        addVehicleElement(incident, digestElement);
        addTangibleItemElement(incident, digestElement);
        addAssociationsElement(incident, digestElement);

    }

    private void addAssociationsElement(Incident incident, Element digestElement) {

        Element associationsElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Associations");
        Element a = appendElement(associationsElement, OjbcNamespaceContext.NS_JXDM_40, "ActivityEnforcementOfficialAssociation");
        Element e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.officer.personId);
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityTelephoneNumberAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.subject.personId);
        e = appendElement(a, OjbcNamespaceContext.NS_LEXSDIGEST, "TelephoneNumberReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "phone1");
        e = appendElement(a, OjbcNamespaceContext.NS_LEXSDIGEST, "TelephoneNumberPrimaryIndicator");
        e.setTextContent("true");
        e = appendElement(a, OjbcNamespaceContext.NS_LEXSDIGEST, "TelephoneNumberWorkIndicator");
        e.setTextContent("true");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "IncidentLocationAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "LocationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "incident-location");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "IncidentOffenseAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID + "-O1");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "IncidentSubjectPersonAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.subject.personId);
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "IncidentVictimPersonAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.victim.personId);
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "OffenseLocationAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID + "-O1");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "LocationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "incident-location");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "OffenseWeaponAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID + "-O1");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ItemReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "item1");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_LEXSDIGEST, "PersonContactAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.subject.personId);
        e = appendElement(a, OjbcNamespaceContext.NS_LEXSDIGEST, "ContactPersonTelephoneNumberReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "phone1");
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_NC, "ActivityReportingOrganizationAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.incidentID);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "OrganizationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.reportingOrganizationID);
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_NC, "PersonAssignedUnitAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.officer.personId);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "OrganizationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.reportingOrganizationID);
        a = appendElement(associationsElement, OjbcNamespaceContext.NS_NC, "ResidenceAssociation");
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.subject.personId);
        e = appendElement(a, OjbcNamespaceContext.NS_NC, "LocationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", "subject-residence");

    }

    private void addTangibleItemElement(Incident incident, Element digestElement) {
        Element entityTangibleItemELement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityTangibleItem");
        Element tangibleItemElement = appendElement(entityTangibleItemELement, OjbcNamespaceContext.NS_NC, "TangibleItem");
        XmlUtils.addAttribute(tangibleItemElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "item1");
        Element e = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NC, "ItemDescriptionText");
        e.setTextContent(incident.object.itemDescription);
        e = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NC, "ItemSerialIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(incident.object.id);
        e = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NC, "ItemCategoryText");
        e.setTextContent(incident.object.category);
        e = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NC, "ItemMakeName");
        e.setTextContent(incident.object.make);
        e = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NC, "ItemModelName");
        e.setTextContent(incident.object.model);
    }

    private void addVehicleElement(Incident incident, Element digestElement) {

        Vehicle vehicle = incident.vehicle;

        Element entityVehicleElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityVehicle");
        Element vehicleElement = appendElement(entityVehicleElement, OjbcNamespaceContext.NS_NC, "Vehicle");
        XmlUtils.addAttribute(vehicleElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "vehicle1");
        Element e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "ItemColorDescriptionText");
        e.setTextContent(vehicle.color);
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "VehicleColorPrimaryCode");
        e.setTextContent(vehicle.colorCode);
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "ItemModelYearDate");
        e.setTextContent(String.valueOf(vehicle.year));
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "ConveyanceRegistrationPlateIdentification");
        appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID").setTextContent(vehicle.plate);
        appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationExpirationDate").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(vehicle.plateExpiration));
        appendElement(e, OjbcNamespaceContext.NS_JXDM_40, "IdentificationJurisdictionUSPostalServiceCode").setTextContent(vehicle.plateState);
        appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "VehicleDoorQuantity").setTextContent(String.valueOf(vehicle.doors));
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "VehicleIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(vehicle.vin);
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "ConveyanceRegistration");
        appendElement(e, OjbcNamespaceContext.NS_NC, "ConveyanceRegistrationPlateCategoryCode").setTextContent(vehicle.plateTypeCode);
        appendElement(e, OjbcNamespaceContext.NS_NC, "ConveyanceRegistrationPlateCategoryText").setTextContent(vehicle.plateTypeText);
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "VehicleMakeCode");
        e.setTextContent(vehicle.make);
        e = appendElement(vehicleElement, OjbcNamespaceContext.NS_NC, "VehicleModelCode");
        e.setTextContent(vehicle.model);

    }

    private void addLocationElement(Incident incident, Element digestElement, PersonElementWrapper referencePerson, String locationId) {

        Element entityLocationElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityLocation");
        Element locationElement = appendElement(entityLocationElement, OjbcNamespaceContext.NS_NC, "Location");
        XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", locationId);
        Element e = appendElement(locationElement, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "StructuredAddress");
        Element sa = e;
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "LocationStreet");
        Element streetElement = e;
        e = appendElement(streetElement, OjbcNamespaceContext.NS_NC, "StreetNumberText");
        e.setTextContent(referencePerson.addressStreetNumber);
        e = appendElement(streetElement, OjbcNamespaceContext.NS_NC, "StreetName");
        e.setTextContent(referencePerson.addressStreetName);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationCityName");
        e.setTextContent(referencePerson.city);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationStateUSPostalServiceCode");
        e.setTextContent(referencePerson.state);
        e = appendElement(sa, OjbcNamespaceContext.NS_NC, "LocationPostalCode");
        e.setTextContent(referencePerson.zipCode);

    }

    private void addSubjectPhoneElement(Incident incident, Element digestElement) {
        Element entityPhoneNumberElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityTelephoneNumber");
        Element phoneNumberElement = appendElement(entityPhoneNumberElement, OjbcNamespaceContext.NS_LEXSDIGEST, "TelephoneNumber");
        XmlUtils.addAttribute(phoneNumberElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "phone1");
        Element e = appendElement(phoneNumberElement, OjbcNamespaceContext.NS_NC, "FullTelephoneNumber");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "TelephoneNumberFullID");
        e.setTextContent(incident.subject.telephoneNumber);
    }

    private void addReportingAgencyElement(Incident incident, Element digestElement) {

        Element entityOrganizationElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityOrganization");
        Element organizationElement = appendElement(entityOrganizationElement, OjbcNamespaceContext.NS_NC, "Organization");
        XmlUtils.addAttribute(organizationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", incident.reportingOrganizationID);
        Element e = appendElement(organizationElement, OjbcNamespaceContext.NS_NC, "OrganizationCategoryText");
        e.setTextContent("Criminal Justice");
        e = appendElement(organizationElement, OjbcNamespaceContext.NS_NC, "OrganizationName");
        e.setTextContent(incident.reportingOrganizationName);

    }

    private void addSubjectPersonElement(Incident incident, Element digestElement, DateTime baseDate) {

        PersonElementWrapper person = incident.subject;

        Element entityPersonElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityPerson");
        Element personElement = appendElement(entityPersonElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Person");
        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", person.personId);
        Element personAgeMeasureElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        Element e = appendElement(personAgeMeasureElement, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        int ageInYears = Years.yearsBetween(person.birthdate, baseDate).getYears();
        e.setTextContent(String.valueOf(ageInYears));
        e = appendElement(personAgeMeasureElement, OjbcNamespaceContext.NS_NC, "TimeUnitCode");
        e.setTextContent("ANN");
        Element personAlternateNameElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAlternateName");
        e = appendElement(personAlternateNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName + " A");
        e = appendElement(personAlternateNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(person.birthdate));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonEyeColorText");
        e.setTextContent(generateRandomCodeFromList("Blue", "Brown", "Hazel"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHairColorText");
        e.setTextContent(generateRandomCodeFromList("Black", "Brown", "Blonde"));
        Element personHeightMeasureElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHeightMeasure");
        e = appendElement(personHeightMeasureElement, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        e.setTextContent(String.valueOf(Math.round(Integer.parseInt(person.centimeters) * .39)));
        e = appendElement(personHeightMeasureElement, OjbcNamespaceContext.NS_NC, "LengthUnitCode");
        e.setTextContent("INH");
        Element personNameElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
        e = appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName);
        e = appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);
        // Note: this is the field used to identify multiple incidents belonging to the same person
        // It is the mock's equivalent of a "master name index" in an RMS
        Element otherId = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonOtherIdentification");
        e = appendElement(otherId, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(person.id);
        e = appendElement(otherId, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent(getClass().getName());
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceText");
        e.setTextContent(generateRandomCodeFromList("W", "B", "U"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());
        Element ssn = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
        e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(person.nationalID);
        e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent("SSA");
        Element pwm = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonWeightMeasure");
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        e.setTextContent(person.pounds);
        e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "WeightUnitCode");
        e.setTextContent("LBR");
        Element personAugmentationElement = appendElement(personElement, OjbcNamespaceContext.NS_JXDM_40, "PersonAugmentation");
        e = appendElement(personAugmentationElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        Element dl = e;
        e = appendElement(dl, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID(person.state, 7));
        e = appendElement(dl, OjbcNamespaceContext.NS_JXDM_40, "IdentificationJurisdictionNCICLISCode");
        e.setTextContent(person.state);
        e = appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_40, "PersonFBIIdentification");
        Element fbi = e;
        e = appendElement(fbi, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("", 6) + generateRandomLetter() + generateRandomLetter() + generateRandomID("", 1));
        e = appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_40, "PersonStateFingerprintIdentification");
        Element sid = e;
        e = appendElement(sid, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("A", 7));
        e = appendElement(sid, OjbcNamespaceContext.NS_JXDM_40, "IdentificationJurisdictionNCICLISCode");
        e.setTextContent(person.state);

        e = appendElement(entityPersonElement, OjbcNamespaceContext.NS_JXDM_40, "IncidentSubject");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);

    }

    private void addOfficerPersonElement(Incident incident, Element digestElement) {

        Element entityPersonElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityPerson");
        Element personElement = appendElement(entityPersonElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Person");
        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", incident.officer.personId);
        Element personNameElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
        Element e = appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonFullName");
        e.setTextContent(incident.officer.firstName + " " + incident.officer.middleName.substring(0, 1) + ". " + incident.officer.lastName);
        Element enforcementOfficialElement = appendElement(entityPersonElement, OjbcNamespaceContext.NS_JXDM_40, "EnforcementOfficial");
        e = appendElement(enforcementOfficialElement, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incident.officer.personId);
        e = appendElement(enforcementOfficialElement, OjbcNamespaceContext.NS_JXDM_40, "EnforcementOfficialBadgeIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("B", 5));
        e = appendElement(enforcementOfficialElement, OjbcNamespaceContext.NS_JXDM_40, "EnforcementOfficialUnit");
        e = appendElement(e, OjbcNamespaceContext.NS_JXDM_40, "EnforcementUnitBeatIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomCodeFromList("Beat", "Patrol Sector", "Troop") + " " + randomGenerator.nextInt(0, 10));

    }

    private void addSuspectPersonElement(Incident incident, Element digestElement, DateTime baseDate) {
        Element entityPersonElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityPerson");
        Element e = appendElement(entityPersonElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Metadata");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "SourceIDText");
        e.setTextContent(incident.incidentID);
        addPersonElement(baseDate, incident.suspect, entityPersonElement, "Suspect");
    }

    private void addVictimPersonElement(Incident incident, Element digestElement, DateTime baseDate) {
        Element entityPersonElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityPerson");
        Element e = appendElement(entityPersonElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Metadata");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "SourceIDText");
        e.setTextContent(incident.incidentID);
        addPersonElement(baseDate, incident.victim, entityPersonElement, "Victim");
    }

    private void addPersonElement(DateTime baseDate, PersonElementWrapper person, Element entityPersonElement, String roleName) {
        Element personElement = appendElement(entityPersonElement, OjbcNamespaceContext.NS_LEXSDIGEST, "Person");
        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", person.personId);
        Element e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
        int ageInYears = Years.yearsBetween(person.birthdate, baseDate).getYears();
        e.setTextContent(String.valueOf(ageInYears));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(person.birthdate));
        Element personNameElement = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
        e = appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName);
        e = appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceText");
        e.setTextContent(generateRandomCodeFromList("W", "B", "U"));
        e = appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());
        e = appendElement(entityPersonElement, OjbcNamespaceContext.NS_JXDM_40, roleName);
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "RoleOfPersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
    }

    private void addOffenseActivityElement(Incident incident, Element digestElement) {
        Element entityActivityElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityActivity");
        Element activityElement = appendElement(entityActivityElement, OjbcNamespaceContext.NS_NC, "Activity");
        XmlUtils.addAttribute(activityElement, OjbcNamespaceContext.NS_STRUCTURES, "id", incident.incidentID + "-O1");
        Element e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityCategoryText");
        e.setTextContent("Offense");
        e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityDescriptionText");
        e.setTextContent(incident.offense);
    }

    private void addIncidentActivityElement(Incident incident, Element digestElement) {
        Element entityActivityElement = appendElement(digestElement, OjbcNamespaceContext.NS_LEXSDIGEST, "EntityActivity");
        Element activityElement = appendElement(entityActivityElement, OjbcNamespaceContext.NS_NC, "Activity");
        XmlUtils.addAttribute(activityElement, OjbcNamespaceContext.NS_STRUCTURES, "id", incident.incidentID);
        Element e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(incident.incidentID);
        e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityCategoryText");
        e.setTextContent("Incident");
        Element dateRangeElement = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityDateRange");
        e = appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "DateTime");
        e.setTextContent(ISODateTimeFormat.dateTimeNoMillis().print(incident.startDate));
        e = appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "DateTime");
        e.setTextContent(ISODateTimeFormat.dateTimeNoMillis().print(incident.endDate));
        e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "ActivityDescriptionText");
        e.setTextContent(incident.offense);
    }
    
    private void addOJBCStructuredPayloadElement(Element dataItemPackageElement, Incident incident) {
        Element structuredPayloadElement = appendElement(dataItemPackageElement, OjbcNamespaceContext.NS_LEXS, "StructuredPayload");
        XmlUtils.addAttribute(structuredPayloadElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "ojbc-sp");
        addStructuredPayloadMetadataElement(structuredPayloadElement, "http://www.ojbc.org", "OJBC", "1.0");
        Element incidentReportElement = XmlUtils.appendElement(structuredPayloadElement, OjbcNamespaceContext.NS_INC_EXT, "IncidentReport");
        Element incidentElement = XmlUtils.appendElement(incidentReportElement, OjbcNamespaceContext.NS_INC_EXT, "Incident");
        Element e = XmlUtils.appendElement(incidentElement, OjbcNamespaceContext.NS_INC_EXT, "IncidentCategoryCode");
        e.setTextContent(generateRandomCodeFromList("Law", "Traffic", "Citation", "Field_Interview", "Warning"));
        e = appendElement(incidentElement, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", incident.incidentID);
    }

    private void addNDExStructuredPayloadElement(Element dataItemPackageElement, Incident incident) {
        Element structuredPayloadElement = appendElement(dataItemPackageElement, OjbcNamespaceContext.NS_LEXS, "StructuredPayload");
        XmlUtils.addAttribute(structuredPayloadElement, OjbcNamespaceContext.NS_STRUCTURES, "id", "ndex-sp");
        addStructuredPayloadMetadataElement(structuredPayloadElement, "http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1", "N-DEx IncidentArrest", "2.1");
        addIncidentReportElement(structuredPayloadElement, incident);
    }

    private void addIncidentReportElement(Element structuredPayloadElement, Incident incident) {

        Element incidentReportElement = appendElement(structuredPayloadElement, OjbcNamespaceContext.NS_NDEXIA, "IncidentReport");
        Element incidentElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "Incident");
        Element e = appendElement(incidentElement, OjbcNamespaceContext.NS_NDEXIA, "ActivityStatus");
        e = appendElement(e, OjbcNamespaceContext.NS_NDEXIA, "ActivityStatusAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_NDEXIA, "IncidentStatusCode");
        e.setTextContent("Closed");
        Element augElement = appendElement(incidentElement, OjbcNamespaceContext.NS_NDEXIA, "IncidentAugmentation");
        e = appendElement(augElement, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", incident.incidentID);
        e = appendElement(augElement, OjbcNamespaceContext.NS_NDEXIA, "IncidentCaseNumberText");
        e.setTextContent(generateRandomID("CASE", 8));
        Element offenseElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "Offense");
        e = appendElement(offenseElement, OjbcNamespaceContext.NS_NDEXIA, "ActivityAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", incident.incidentID + "-O1");
        e = appendElement(offenseElement, OjbcNamespaceContext.NS_JXDM_40, "IncidentStructuresEnteredQuantity");
        e.setTextContent("1.0");
        e = appendElement(offenseElement, OjbcNamespaceContext.NS_NDEXIA, "CriminalActivityCategoryText");
        e.setTextContent("crime type");
        e = appendElement(offenseElement, OjbcNamespaceContext.NS_NDEXIA, "OffenseEntryPoint");
        e = appendElement(e, OjbcNamespaceContext.NS_NDEXIA, "PassagePointAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_NDEXIA, "MethodOfAccessText");
        e.setTextContent(generateRandomCodeFromList("window", "door", "crawlspace"));
        e = appendElement(offenseElement, OjbcNamespaceContext.NS_NDEXIA, "OffenseText");
        e.setTextContent(incident.offense);
        Element locationElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "Location");
        e = appendElement(locationElement, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "StructuredAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "LocationCountyName");
        e.setTextContent(getRandomCounty(incident.subject.state));
        e = appendElement(locationElement, OjbcNamespaceContext.NS_NDEXIA, "LocationAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", "incident-location");
        Element personElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "Person");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NDEXIA, "PersonPhysicalFeature");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "PhysicalFeatureCategoryText");
        e.setTextContent("tattoo");
        e = appendElement(personElement, OjbcNamespaceContext.NS_NDEXIA, "PersonAugmentation");
        e = appendElement(e, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", incident.subject.personId);
        Element vehicleElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "Vehicle");
        augElement = appendElement(vehicleElement, OjbcNamespaceContext.NS_NDEXIA, "VehicleAugmentation");
        e = appendElement(augElement, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", "vehicle1");
        e = appendElement(augElement, OjbcNamespaceContext.NS_NDEXIA, "VehicleStyleCategoryCode");
        e.setTextContent("Automobile");
        Element tangibleItemElement = appendElement(incidentReportElement, OjbcNamespaceContext.NS_NDEXIA, "TangibleItem");
        augElement = appendElement(tangibleItemElement, OjbcNamespaceContext.NS_NDEXIA, "TangibleItemAugmentation");
        e = appendElement(augElement, OjbcNamespaceContext.NS_LEXSLIB, "SameAsDigestReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_LEXSLIB, "ref", "item1");
        Element quantityElement = appendElement(augElement, OjbcNamespaceContext.NS_NDEXIA, "ItemQuantityStatusValue");
        e = appendElement(quantityElement, OjbcNamespaceContext.NS_NC, "ItemQuantity");
        e.setTextContent(String.valueOf(generatePoissonInt(.5, false)));
        e = appendElement(quantityElement, OjbcNamespaceContext.NS_NDEXIA, "ItemValue");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "ItemValueAmount");
        e.setTextContent(String.valueOf(50));

    }

    private void addStructuredPayloadMetadataElement(Element structuredPayloadElement, String communityURI, String communityDescription, String communityVersion) {
        Element structuredPayloadMetadataElement = appendElement(structuredPayloadElement, OjbcNamespaceContext.NS_LEXS, "StructuredPayloadMetadata");
        Element e = appendElement(structuredPayloadMetadataElement, OjbcNamespaceContext.NS_LEXS, "CommunityURI");
        e.setTextContent(communityURI);
        e = appendElement(structuredPayloadMetadataElement, OjbcNamespaceContext.NS_LEXS, "CommunityDescription");
        e.setTextContent(communityDescription);
        e = appendElement(structuredPayloadMetadataElement, OjbcNamespaceContext.NS_LEXS, "CommunityVersion");
        e.setTextContent(communityVersion);
    }

}
