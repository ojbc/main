package org.search.ojb.staticmock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchTest extends AbstractStaticMockTest {

    @Test
    public void testDocumentCounts() throws Exception {
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/CriminalHistory", staticMockQuery.getCriminalHistoryDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/Warrant", staticMockQuery.getWarrantDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/Incident", staticMockQuery.getIncidentDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/FirearmRegistration", staticMockQuery.getFirearmRegistrationDocumentCount());
        
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenileCasePlan", staticMockQuery.getJuvenileCasePlanDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenileHistory", staticMockQuery.getJuvenileHistoryDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenileIntake", staticMockQuery.getJuvenileIntakeDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenileOffense", staticMockQuery.getJuvenileOffenseDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenilePlacement", staticMockQuery.getJuvenilePlacementDocumentCount());
        assertCorrectDocumentCount("src/test/resources/XpathTestSamples/JuvenileReferral", staticMockQuery.getJuvenileReferralDocumentCount());

    }

    @Test
    public void testSearchDocuments() throws Exception {
        Document request = buildBasePersonSearchRequestMessage(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Document response = staticMockQuery.searchDocuments(request);
        assertNotNull(XmlUtils.xPathNodeListSearch(response, "/psres-doc:PersonSearchResults"));
        request = buildBaseFirearmSearchRequestMessage();
        response = staticMockQuery.searchDocuments(request);
        assertNotNull(XmlUtils.xPathNodeListSearch(response, "/firearm-search-resp-doc:FirearmSearchResults"));
    }

    @Test
    public void testInvalidRequestMessage() throws Exception {

        Document invalidRequestMessage = documentBuilder.newDocument();
        Element rootElement = invalidRequestMessage.createElementNS("http://nothing", "DoesntExist");
        invalidRequestMessage.appendChild(rootElement);
        Throwable exception = null;
        try {
            staticMockQuery.personSearchDocumentsAsList(invalidRequestMessage, new DateTime());
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception instanceof IllegalArgumentException);

    }

    @Test
    public void testRequestParsing() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessage(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        StaticMockQuery.PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
        assertEquals("Williams", psp.getLastName());
        assertEquals("Theodore", psp.getFirstName());
        assertEquals("123456789", psp.getSSN());
        assertEquals("123458", psp.getSID());
        assertEquals("123456", psp.getDL());
        assertEquals("123457", psp.getFBI());
        assertEquals("XXX", psp.getEyeColor());
        assertEquals("BLK", psp.getHairColor());
        assertEquals("W", psp.getRace());
        assertEquals("M", psp.getSex());
        assertEquals(StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("1970-01-01"), psp.getDateOfBirthMin());
        assertEquals(StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("1970-01-05"), psp.getDateOfBirthMax());
        assertEquals(new Integer(38), psp.getAgeMin());
        assertEquals(new Integer(42), psp.getAgeMax());
        assertEquals(new Integer(200), psp.getWeightMin());
        assertEquals(new Integer(215), psp.getWeightMax());
        assertEquals(new Integer(72), psp.getHeightMin());
        assertEquals(new Integer(75), psp.getHeightMax());
        assertFalse(psp.isFirstNameSearchStartsWith());
        assertTrue(psp.isLastNameSearchStartsWith());

    }

    @Test
    public void testRequestParsingWithNull() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessage(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        removeElement(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/nc:PersonEyeColorCode");
        StaticMockQuery.PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
        assertNull(psp.getEyeColor());
    }

    @Test
    public void testRequestParsingWithRanges() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessage(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element pwm = removeElement(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/nc:PersonWeightMeasure/nc:MeasureRangeValue");
        Element e = XmlUtils.appendElement(pwm, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent("150");
        Element phm = removeElement(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue");
        e = XmlUtils.appendElement(phm, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent("54");
        Element pam = removeElement(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/nc:PersonAgeMeasure/nc:MeasureRangeValue");
        e = XmlUtils.appendElement(pam, OjbcNamespaceContext.NS_NC, "MeasureText");
        e.setTextContent("25");
        removeElement(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/psr:PersonBirthDateRange");
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person");
        Element eyeColorElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "/psr-doc:PersonSearchRequest/psr:Person/nc:PersonEyeColorCode");
        Element pbd = personSearchRequestMessage.createElementNS(OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        personElement.insertBefore(pbd, eyeColorElement);
        e = XmlUtils.appendElement(pbd, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent("1971-01-01");

        // XmlUtils.printNode(personSearchRequestMessage);
        StaticMockQuery.PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
        assertEquals(StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("1971-01-01"), psp.getDateOfBirth());
        assertEquals(new Integer(25), psp.getAge());
        assertEquals(new Integer(150), psp.getWeight());
        assertEquals(new Integer(54), psp.getHeight());
    }

    @Test
    public void testIncidentPersonSearch() throws Exception {
        Document incidentPersonSearchRequestMessage = buildIncidentPersonSearchRequestMessage(8);
        XmlUtils.validateInstance("service-specifications/Incident_Search_Request_Service/artifacts/service_model/information_model/Incident_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                incidentPersonSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentPersonSearchDocumentsAsList(incidentPersonSearchRequestMessage,
                StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        Document d = staticMockQuery.incidentPersonSearchDocuments(incidentPersonSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        // XmlUtils.printNode(d);
        XmlUtils.validateInstance("service-specifications/Incident_Search_Results_Service/artifacts/service_model/information_model/Incident_Search_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", d);
        incidentPersonSearchRequestMessage = buildIncidentPersonSearchRequestMessage(80);
        matches = staticMockQuery.incidentPersonSearchDocumentsAsList(incidentPersonSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastNameOnly() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        //XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
//        for (IdentifiableDocumentWrapper dw : matches) {
//            Document d = dw.getDocument();
//            XmlUtils.printNode(d);
//        }
    }

    @Test
    public void testPersonSearchAccessDeniedReturn() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("AccessDenied");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        //XmlUtils.printNode(personSearchRequestMessage);
        Document response = staticMockQuery.searchDocuments(personSearchRequestMessage);
        //XmlUtils.printNode(response);
        assertTrue(XmlUtils.nodeExists(response, "/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/iad:InformationAccessDenial"));
    }

    @Test
    public void testPersonSearchErrorReturn() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("ErrorReported");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        //XmlUtils.printNode(personSearchRequestMessage);
        Document response = staticMockQuery.searchDocuments(personSearchRequestMessage);
        //XmlUtils.printNode(response);
        assertTrue(XmlUtils.nodeExists(response, "/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchErrors"));
    }

    @Test
    public void testIncidentSearchErrorReturn() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("AccessDenied");
        Document searchResults = staticMockQuery.incidentSearchDocuments(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        XmlUtils.printNode(searchResults);
        assertTrue(XmlUtils.nodeExists(searchResults, "/isres-doc:IncidentSearchResults/srm:SearchResultsMetadata/iad:InformationAccessDenial"));
    }

    @Test
    public void testFirearmSearchErrorReturn() throws Exception {
        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        Node serialNumberNode = XmlUtils.xPathNodeSearch(firearmSearchRequestMessage, "firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        serialNumberNode.setTextContent("AccessDenied");
        Document searchResults = staticMockQuery.firearmSearchDocuments(firearmSearchRequestMessage);
        //XmlUtils.printNode(searchResults);
        assertTrue(XmlUtils.nodeExists(searchResults, "/firearm-search-resp-doc:FirearmSearchResults/srm:SearchResultsMetadata/iad:InformationAccessDenial"));
    }

    @Test
    public void testTwoSystemSearchLastNameOnly() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Element sourceSystemTextElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:SourceSystemNameText");
        Element root = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest");
        Element secondSourceSystemTextElement = XmlUtils.insertElementBefore(root, sourceSystemTextElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");
        secondSourceSystemTextElement.setTextContent(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(3, matches.size());
        int criminalHistoryCount = 0;
        int warrantCount = 0;
        for (IdentifiableDocumentWrapper dw : matches) {
            Document d = dw.getDocument();
            String rootElementName = d.getDocumentElement().getLocalName();
            if ("CriminalHistory".equals(rootElementName)) {
                criminalHistoryCount++;
            } else if ("Warrants".equals(rootElementName)) {
                warrantCount++;
            } else {
                throw new Exception("Invalid root element: " + rootElementName);
            }
        }
        assertEquals(criminalHistoryCount, 2);
        assertEquals(warrantCount, 1);
    }

    @Test
    public void testIncidentSearchLastNameOnly() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testFirearmSearchBySerialNumber() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        //XmlUtils.printNode(firearmSearchRequestMessage);

        List<IdentifiableDocumentWrapper> matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(1, matches.size());
        Document responseDocument = matches.get(0).getDocument();
        // XmlUtils.printNode(responseDocument);

        XmlUtils.validateInstance("service-specifications/Firearm_Registration_Query_Results_Service/artifacts/service_model/information_model/Firearm_Registration_Query_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", responseDocument);
        assertEquals(1, matches.size());
        assertEquals(1, XmlUtils.xPathNodeListSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults").getLength());
        String serialNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        assertEquals(serialNumber, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID"));

        Node serialNumberNode = XmlUtils.xPathNodeSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        serialNumberNode.setTextContent("doesnt exist");
        // XmlUtils.printNode(firearmSearchRequestMessage);
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(0, matches.size());

    }

    @Test
    public void testFirearmSearchByRegistrationNumber() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:LocationCountyName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        // XmlUtils.printNode(firearmSearchRequestMessage);

        List<IdentifiableDocumentWrapper> matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        Document responseDocument = matches.get(0).getDocument();
        XmlUtils.validateInstance("service-specifications/Firearm_Registration_Query_Results_Service/artifacts/service_model/information_model/Firearm_Registration_Query_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", responseDocument);
        assertEquals(1, matches.size());
        // XmlUtils.printNode(responseDocument);
        assertEquals(1, XmlUtils.xPathNodeListSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults").getLength());
        String registrationNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
        assertEquals(registrationNumber,
                XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID"));

        Node registrationNumberNode = XmlUtils.xPathNodeSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
        registrationNumberNode.setTextContent("doesnt exist");
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(0, matches.size());

    }
    
    @Test
    public void testIncidentSearchXPathBuild() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        String xPath = StaticMockQuery.buildIncidentSearchXPathFromMessage(incidentSearchRequestMessage);
        assertEquals("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[" +
                "lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityIdentification/nc:IdentificationID='12345' " + 
                "and lexs:Digest/lexsdigest:EntityLocation/nc:Location[nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName='Burlington' " + 
                "and @s:id=/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref] " +
                "and lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Incident[inc-ext:IncidentCategoryCode='Law']]", xPath);
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        xPath = StaticMockQuery.buildIncidentSearchXPathFromMessage(incidentSearchRequestMessage);
        assertEquals("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[" +
                "lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityIdentification/nc:IdentificationID='12345']", xPath);
        incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        xPath = StaticMockQuery.buildIncidentSearchXPathFromMessage(incidentSearchRequestMessage);
        assertEquals("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[" +
                "lexs:Digest/lexsdigest:EntityLocation/nc:Location[nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName='Burlington' " + 
                "and @s:id=/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref]]", xPath);
    }

    @Test
    public void testFirearmSearchXPathBuild() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");

        // XmlUtils.printNode(firearmSearchRequestMessage);
        String xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        // LOG.debug(xPath);
        assertEquals("/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[nc:ItemSerialIdentification/nc:IdentificationID='S11']", xPath);

        firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");

        xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        // log.debug(xPath);
        assertEquals(
                "/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[nc:ItemSerialIdentification/nc:IdentificationID='S11' and (firearms-codes-demostate:FirearmMakeCode='SMI')]",
                xPath);

        firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:LocationCountyName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");

        xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        // log.debug(xPath);
        assertEquals(
                "/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[@s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[nc:RegistrationIdentification/nc:IdentificationID='R11']/@s:id]/nc:ItemReference/@s:ref]",
                xPath);

        firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        String expectedXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[nc:ItemSerialIdentification/nc:IdentificationID='S11' and nc:ItemModelName='FirearmTest' and nc:FirearmCategoryCode='R' and (firearms-codes-demostate:FirearmMakeCode='SMI') and @s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[nc:RegistrationIdentification/nc:IdentificationID='R11' and nc:LocationCountyName='Laurel']/@s:id]/nc:ItemReference/@s:ref]";
        // log.debug(xPath);
        // log.debug(expectedXPath);
        assertEquals(expectedXPath, xPath);

        firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        expectedXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[nc:ItemSerialIdentification/nc:IdentificationID='S11' and nc:ItemModelName='FirearmTest' and nc:FirearmCategoryCode='R' and (firearm-search-req-ext:FirearmMakeText='SMI') and @s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[nc:RegistrationIdentification/nc:IdentificationID='R11' and nc:LocationCountyName='Laurel']/@s:id]/nc:ItemReference/@s:ref]";
        // log.debug(xPath);
        // log.debug(expectedXPath);
        assertEquals(expectedXPath, xPath);
        
        firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification");
        
        xPath = StaticMockQuery.buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        expectedXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[nc:ItemModelName='FirearmTest' and (firearms-codes-demostate:FirearmMakeCode='SMI' or firearm-search-req-ext:FirearmMakeText='SMI') and @s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[nc:LocationCountyName='Laurel']/@s:id]/nc:ItemReference/@s:ref]";
//         log.debug(xPath);
//         log.debug(expectedXPath);
        assertEquals(expectedXPath, xPath);

    }

    @Test
    public void testFirearmDocumentSearchForNothing() throws Exception {
        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        Node serialNumberNode = XmlUtils.xPathNodeSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        serialNumberNode.setTextContent("doesnt exist");
        Document responseDocument = staticMockQuery.firearmSearchDocuments(firearmSearchRequestMessage);
        XmlUtils.validateInstance("service-specifications/Firearm_Search_Results_Service/artifacts/service_model/information_model/Firearm_Search_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", responseDocument);
        Node root = XmlUtils.xPathNodeSearch(responseDocument, "firearm-search-resp-doc:FirearmSearchResults");
        assertNotNull(root);
        NodeList descendants = ((Element) root).getChildNodes();
        assertEquals(0, descendants.getLength());
    }

    @Test
    public void testFirearmDocumentSearchByModel() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        // XmlUtils.printNode(firearmSearchRequestMessage);

        Document responseDocument = staticMockQuery.firearmSearchDocuments(firearmSearchRequestMessage);
        // XmlUtils.printNode(responseDocument);
        XmlUtils.validateInstance("service-specifications/Firearm_Search_Results_Service/artifacts/service_model/information_model/Firearm_Search_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", responseDocument);
        NodeList results = XmlUtils.xPathNodeListSearch(responseDocument, "firearm-search-resp-doc:FirearmSearchResults/firearm-search-resp-ext:FirearmSearchResult");
        assertEquals(4, results.getLength());

        Node result = XmlUtils.xPathNodeSearch(responseDocument,
                "firearm-search-resp-doc:FirearmSearchResults/firearm-search-resp-ext:FirearmSearchResult[firearm-search-resp-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID='S11']");
        assertEquals("Rafael", XmlUtils.xPathStringSearch(result, "nc:Person/nc:PersonName/nc:PersonGivenName"));
        assertEquals("M", XmlUtils.xPathStringSearch(result, "nc:Person/nc:PersonName/nc:PersonMiddleName"));
        assertEquals("McElroy-McNamara", XmlUtils.xPathStringSearch(result, "nc:Person/nc:PersonName/nc:PersonSurName"));
        assertEquals("FirearmTest", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/nc:ItemModelName"));
        assertEquals("SMI", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/firearms-codes-demostate:FirearmMakeCode"));
        assertEquals("9", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/nc:FirearmCaliberCode"));
        assertEquals("Gauge", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/nc:FirearmGaugeText"));
        assertEquals("R11", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID"));
        assertEquals("Lafayette Parish", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/nc:LocationCountyName"));
        assertEquals("2012-02-19", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/nc:RegistrationEffectiveDate/nc:Date"));
        assertEquals("Inactive",
                XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/firearm-search-resp-ext:RegistrationStatus/firearm-search-resp-ext:FirearmRegistrationStatusText"));
        assertEquals("P", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/nc:FirearmCategoryCode"));
        assertEquals("E", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/nc:FirearmCategoryDescriptionCode"));
        assertEquals("Notes", XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/firearm-search-resp-ext:RegistrationNotesText"));
        assertEquals(XmlUtils.xPathStringSearch(result, "nc:Person/@s:id"), XmlUtils.xPathStringSearch(result, "nc:PropertyRegistrationAssociation/nc:ItemRegistrationHolderReference/@s:ref"));
        assertEquals(XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:Firearm/@s:id"), XmlUtils.xPathStringSearch(result, "nc:PropertyRegistrationAssociation/nc:ItemReference/@s:ref"));
        assertEquals(XmlUtils.xPathStringSearch(result, "firearm-search-resp-ext:ItemRegistration/@s:id"),
                XmlUtils.xPathStringSearch(result, "nc:PropertyRegistrationAssociation/nc:ItemRegistrationReference/@s:ref"));
    }

    @Test
    public void testFirearmSearchByModel() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        // XmlUtils.printNode(firearmSearchRequestMessage);

        List<IdentifiableDocumentWrapper> matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(4, matches.size());

        for (IdentifiableDocumentWrapper dw : matches) {
            Document responseDocument = dw.getDocument();
            // XmlUtils.printNode(responseDocument);
            assertEquals(1, XmlUtils.xPathNodeListSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm").getLength());
            String modelName = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
            assertEquals(modelName, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemModelName"));
        }

        Element modelNode = (Element) XmlUtils.xPathNodeSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        modelNode.setTextContent("doesnt exist");
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(0, matches.size());

    }

    @Test
    public void testFirearmSearchByModelAndMake() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        // XmlUtils.printNode(firearmSearchRequestMessage);

        List<IdentifiableDocumentWrapper> matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(3, matches.size());

        for (IdentifiableDocumentWrapper dw : matches) {
            Document responseDocument = dw.getDocument();
            // XmlUtils.printNode(responseDocument);
            assertEquals(1, XmlUtils.xPathNodeListSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm").getLength());
            String modelName = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
            String makeName = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                    "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
            assertEquals(modelName, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemModelName"));
            assertEquals(makeName, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearms-codes-demostate:FirearmMakeCode"));
        }

        Element modelNode = (Element) XmlUtils.xPathNodeSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        modelNode.setTextContent("doesnt exist");
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(0, matches.size());

    }

    @Test
    public void testFirearmSearchByModelAndMakeAndRegistrationNumber() throws Exception {

        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:LocationCountyName");
        // XmlUtils.printNode(firearmSearchRequestMessage);

        List<IdentifiableDocumentWrapper> matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(1, matches.size());

        for (IdentifiableDocumentWrapper dw : matches) {
            Document responseDocument = dw.getDocument();
            // XmlUtils.printNode(responseDocument);
            assertEquals(1, XmlUtils.xPathNodeListSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm").getLength());
            String modelName = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
            String makeName = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                    "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
            String registrationNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                    "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
            assertEquals(modelName, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemModelName"));
            assertEquals(makeName, XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearms-codes-demostate:FirearmMakeCode"));
            assertEquals(registrationNumber,
                    XmlUtils.xPathStringSearch(responseDocument, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID"));
        }

        // now take registration number out, we should get just a make/model search (see above unit test)
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(3, matches.size());

        Element modelNode = (Element) XmlUtils.xPathNodeSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        modelNode.setTextContent("doesnt exist");
        matches = submitDocumentFirearmSearch(firearmSearchRequestMessage);
        assertEquals(0, matches.size());

    }

    @Test
    public void testFirearmPersonSearchLastNameOnly() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("McElroy-McNamara");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testFirearmPersonSearchLastFirstName() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("McElroy-McNamara");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        firstNameElement.setTextContent("Rafael");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testWarrantSearchLastNameOnly() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastFirstName() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        firstNameElement.setTextContent("Larry");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testWarrantSearchLastFirstName() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        firstNameElement.setTextContent("Ross");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testIncidentSearchLastFirstName() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        firstNameElement.setTextContent("Larry");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }
    
    @Test
    public void testIncidentDocumentSearchIncidentNumber() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I0379588714");
        Document searchResults = staticMockQuery.incidentSearchDocuments(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        NodeList resultsNodes = XmlUtils.xPathNodeListSearch(searchResults, "/isres-doc:IncidentSearchResults/isres:IncidentSearchResult");
        assertEquals(1, resultsNodes.getLength());
    }
    
    @Test
    public void testIncidentSearchDatesOnly() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification");
        Node startDateElement = XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
        startDateElement.setTextContent("2012-05-13T21:23:00-07:00");
        Node endDateElement = XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:EndDate/nc:DateTime");
        endDateElement.setTextContent("2012-05-14T01:59:59-07:00");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        
        // test short date version of this
        
        startDateElement.setTextContent("2012-05-13T21:23:00");
        endDateElement.setTextContent("2012-05-14T01:59:59");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());

        // test date range with no matches
        
        startDateElement.setTextContent("1910-05-13T21:23:00-07:00");
        endDateElement.setTextContent("1911-05-13T21:23:00-07:00");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
        
        // TODO: we could do with more date scenario checking.  However, since this is just a demo tool, I don't know that we need to go crazy here.  As demo scenarios identify issues, document them with tests here.
        
    }

    @Test
    public void testIncidentSearchIncidentNumberAndLowerDate() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:EndDate");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Node incidentNumberElement = XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I0379588714");
        Node startDateElement = XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
        startDateElement.setTextContent("2012-05-01T21:23:05-07:00");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        startDateElement.setTextContent("2012-05-20T21:23:05-07:00");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testIncidentSearchIncidentNumber() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I0379588714");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        incidentNumberElement.setTextContent("doesnt exist");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testIncidentSearchCity() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification");
        Element cityElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location/nc:LocationAddress/isr:StructuredAddress/incident-location-codes-demostate:LocationCityTownCode");
        cityElement.setTextContent("Fulton");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        cityElement.setTextContent("doesnt exist");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testIncidentSearchIncidentNumberAndCity() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        Element cityElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location/nc:LocationAddress/isr:StructuredAddress/incident-location-codes-demostate:LocationCityTownCode");
        cityElement.setTextContent("Fulton");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I0379588714");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        cityElement.setTextContent("doesnt exist");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testIncidentSearchIncidentNumberAndCategory() throws Exception {
        Document incidentSearchRequestMessage = buildBaseIncidentSearchRequestMessage();
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Element categoryElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        categoryElement.setTextContent("Law");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I0379588714");
        List<IdentifiableDocumentWrapper> matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(1, matches.size());
        categoryElement.setTextContent("doesnt exist");
        matches = staticMockQuery.incidentSearchDocumentsAsList(incidentSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        assertEquals(0, matches.size());
    }

    @Test
    public void testIncidentSearchFBI() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element fbiElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "nc:PersonFBIIdentification");
        Element idElement = XmlUtils.appendElement(fbiElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("850182LE0");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchFBI() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element fbiElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "nc:PersonFBIIdentification");
        Element idElement = XmlUtils.appendElement(fbiElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("327656OB5");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchSID() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element sidElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "j:PersonStateFingerprintIdentification");
        Element idElement = XmlUtils.appendElement(sidElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("A5572940");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testIncidentSearchSIDAndFBI() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element fbiElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "nc:PersonFBIIdentification");
        Element idElement = XmlUtils.appendElement(fbiElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("850182LE0");
        Element sidElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "j:PersonStateFingerprintIdentification");
        idElement = XmlUtils.appendElement(sidElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("A8116437");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchSIDAndFBI() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element fbiElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "nc:PersonFBIIdentification");
        Element idElement = XmlUtils.appendElement(fbiElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("327656OB5");
        Element sidElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_JXDM_41, "j:PersonStateFingerprintIdentification");
        idElement = XmlUtils.appendElement(sidElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("A1019440");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
    }

    @Test
    public void testWarrantSearchSSNandDL() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSSNIdentification");
        Element idElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("267-63-3228");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element dlElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
        Element dlIdElement = XmlUtils.appendElement(dlElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        idElement = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("FL6767046");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testIncidentSearchSSNandDL() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSSNIdentification");
        Element idElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("428-67-9543");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element dlElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
        Element dlIdElement = XmlUtils.appendElement(dlElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        idElement = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("MS8602341");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
        // IdentifiableDocumentWrapper dw = matches.get(0);
        // Document d = dw.getDocument();
        // XmlUtils.printNode(d);
    }

    @Test
    public void testCriminalHistorySearchWithUnsupportedParameter() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element dlElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
        Element dlIdElement = XmlUtils.appendElement(dlElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        Element idElement = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("FL6767046");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(0, matches.size());
    }

    @Test
    public void testCriminalHistorySearchWithOneUnsupportedParameterAndOneSupportedParameter() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSSNIdentification");
        Element idElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("434-14-4059");
        Element augElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element dlElement = XmlUtils.appendElement(augElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
        Element dlIdElement = XmlUtils.appendElement(dlElement, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
        idElement = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("FL6767046");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        personElement.removeChild(personNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastNameAndSSN() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSSNIdentification");
        Element idElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("434-14-4059");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testWarrantSearchLastNameAndSSN() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element ssnElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSSNIdentification");
        Element idElement = XmlUtils.appendElement(ssnElement, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
        idElement.setTextContent("267-63-3228");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        // XmlUtils.printNode(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastNameAndSex() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Element sexElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSexCode");
        sexElement.setTextContent("F");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testWarrantSearchLastNameAndSex() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Element sexElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "nc:PersonSexCode");
        sexElement.setTextContent("F");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastNameAndDateOfBirth() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element dateOfBirthElement = XmlUtils.insertElementBefore(personElement, personNameElement, OjbcNamespaceContext.NS_NC, "nc:PersonBirthDate");
        Element d = XmlUtils.appendElement(dateOfBirthElement, OjbcNamespaceContext.NS_NC, "Date");
        d.setTextContent("1929-05-10");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
        personElement.removeChild(dateOfBirthElement);
        Element dobRangeElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "PersonBirthDateRange");
        Element dateElement = XmlUtils.appendElement(dobRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");
        dateElement = XmlUtils.appendElement(dateElement, OjbcNamespaceContext.NS_NC, "Date");
        dateElement.setTextContent("1928-01-01");
        dateElement = XmlUtils.appendElement(dobRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
        dateElement = XmlUtils.appendElement(dateElement, OjbcNamespaceContext.NS_NC, "Date");
        dateElement.setTextContent("1960-01-01");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
    }

    @Test
    public void testCriminalHistorySearchLastNameAndAge() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element ageElement = XmlUtils.insertElementBefore(personElement, personNameElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        Element ageRangeElement = XmlUtils.appendElement(ageElement, OjbcNamespaceContext.NS_NC, "MeasureRangeValue");
        Element rangeMinElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMinimumValue");
        rangeMinElement.setTextContent("52");
        Element rangeMaxElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMaximumValue");
        rangeMaxElement.setTextContent("58");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
        rangeMaxElement.setTextContent("88");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
        rangeMinElement.setTextContent("87");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(0, matches.size());
    }

    @Test
    public void testWarrantSearchLastNameAndAge() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element ageElement = XmlUtils.insertElementBefore(personElement, personNameElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        Element ageRangeElement = XmlUtils.appendElement(ageElement, OjbcNamespaceContext.NS_NC, "MeasureRangeValue");
        Element rangeMinElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMinimumValue");
        rangeMinElement.setTextContent("52");
        Element rangeMaxElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMaximumValue");
        rangeMaxElement.setTextContent("54");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
        rangeMaxElement.setTextContent("65");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(2, matches.size());
        rangeMinElement.setTextContent("64");
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(0, matches.size());
    }

    @Test
    public void testWarrantSearchAgeEqualRange() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element ageElement = XmlUtils.insertElementBefore(personElement, personNameElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
        Element ageRangeElement = XmlUtils.appendElement(ageElement, OjbcNamespaceContext.NS_NC, "MeasureRangeValue");
        Element rangeMinElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMinimumValue");
        rangeMinElement.setTextContent("53");
        Element rangeMaxElement = XmlUtils.appendElement(ageRangeElement, OjbcNamespaceContext.NS_NC, "RangeMaximumValue");
        rangeMaxElement.setTextContent("53");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Smith");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = submitDocumentPersonSearch(personSearchRequestMessage);
        assertEquals(1, matches.size());
    }

    @Test
    public void testSearchResultsWithFirearms() throws Exception {
        Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("McElroy-McNamara");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Document searchResults = staticMockQuery.personSearchDocuments(personSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        XmlUtils.validateInstance("service-specifications/Person_Search_Results_Service/artifacts/service_model/information_model/Person_Search_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", searchResults);
        NodeList nodes = XmlUtils.xPathNodeListSearch(searchResults, "psres-doc:PersonSearchResults/psres:PersonSearchResult");
        int nodeCount = nodes.getLength();
        assertEquals(1, nodeCount);
    }

    @Test
    public void testSearchResults() throws Exception {
        Document personSearchRequestMessage = buildFullResultsPersonSearchRequest();
        Document searchResults = staticMockQuery.personSearchDocuments(personSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
        // XmlUtils.printNode(searchResults);
        XmlUtils.validateInstance("service-specifications/Person_Search_Results_Service/artifacts/service_model/information_model/Person_Search_Results_IEPD/xsd", "Subset/niem", "exchange_schema.xsd", searchResults);
        NodeList nodes = XmlUtils.xPathNodeListSearch(searchResults, "psres-doc:PersonSearchResults/psres:PersonSearchResult");
        int nodeCount = nodes.getLength();
        assertEquals(4, nodeCount);
        Element criminalHistoryResult = null;
        Element warrantResult = null;
        Element incidentResult = null;
        Element firearmResult = null;
        for (int i = 0; i < nodeCount && (criminalHistoryResult == null || warrantResult == null); i++) {
            Element e = (Element) nodes.item(i);
            Element ssnt = (Element) XmlUtils.xPathNodeSearch(e, "psres:SourceSystemNameText");
            if (StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(ssnt.getTextContent())) {
                criminalHistoryResult = e;
            }
            if (StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(ssnt.getTextContent())) {
                warrantResult = e;
            }
            if (StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(ssnt.getTextContent())) {
                incidentResult = e;
            }
            if (StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(ssnt.getTextContent())) {
                firearmResult = e;
            }
        }
        assertNotNull(criminalHistoryResult);
        assertNotNull(warrantResult);
        assertNotNull(incidentResult);
        assertNotNull(firearmResult);

        assertEquals("8", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "intel:SystemIdentifier/nc:IdentificationID")).getTextContent());
        assertEquals("Ivey", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonName/nc:PersonSurName")).getTextContent());
        assertEquals("Larry", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonName/nc:PersonGivenName")).getTextContent());
        assertNull(((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonName/nc:PersonMiddleName")));
        assertEquals("56", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonAgeMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("1957-04-04", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonBirthDate/nc:Date")).getTextContent());
        assertEquals("69", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonHeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("A", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonRaceCode")).getTextContent());
        assertEquals("M", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonSexCode")).getTextContent());
        assertEquals("434-14-4059", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonSSNIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("204.2", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/nc:PersonWeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("MS8602341",
                ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("MS",
                ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText"))
                        .getTextContent());
        assertEquals("850182LE0", ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("A8116437",
                ((Element) XmlUtils.xPathNodeSearch(incidentResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID")).getTextContent());

        assertEquals("Ivey", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonName/nc:PersonSurName")).getTextContent());
        assertEquals("Larry", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonName/nc:PersonGivenName")).getTextContent());
        assertEquals("C", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonName/nc:PersonMiddleName")).getTextContent());
        assertEquals("56", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonAgeMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("1957-04-04", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonBirthDate/nc:Date")).getTextContent());
        assertNull(((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonHeightMeasure/nc:MeasurePointValue"))); // CH doesn't have height
        assertEquals("A", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonRaceCode")).getTextContent());
        assertEquals("M", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonSexCode")).getTextContent());
        assertEquals("434-14-4059", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonSSNIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("204.2", ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/nc:PersonWeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("850182LE0",
                ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("A8116437",
                ((Element) XmlUtils.xPathNodeSearch(criminalHistoryResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID")).getTextContent());

        assertEquals("Ivey", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonName/nc:PersonSurName")).getTextContent());
        assertEquals("Larry", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonName/nc:PersonGivenName")).getTextContent());
        assertEquals("C", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonName/nc:PersonMiddleName")).getTextContent());
        assertEquals("56", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonAgeMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("1957-04-04", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonBirthDate/nc:Date")).getTextContent());
        assertEquals("69", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonHeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("A", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonRaceCode")).getTextContent());
        assertEquals("M", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonSexCode")).getTextContent());
        assertEquals("434-14-4059", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonSSNIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("204.2", ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/nc:PersonWeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("LA9835340",
                ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("LA",
                ((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText"))
                        .getTextContent());
        assertNull(((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID")));
        assertNull(((Element) XmlUtils.xPathNodeSearch(warrantResult, "psres:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID")));

        assertEquals("Ivey", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonName/nc:PersonSurName")).getTextContent());
        assertEquals("Larry", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonName/nc:PersonGivenName")).getTextContent());
        assertEquals("C", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonName/nc:PersonMiddleName")).getTextContent());
        assertEquals("1957-04-04", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonBirthDate/nc:Date")).getTextContent());
        assertEquals("69", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonHeightMeasure/nc:MeasurePointValue")).getTextContent());
        assertEquals("A", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonRaceCode")).getTextContent());
        assertEquals("M", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonSexCode")).getTextContent());
        assertEquals("434-14-4059", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonSSNIdentification/nc:IdentificationID")).getTextContent());
        assertEquals("204.2", ((Element) XmlUtils.xPathNodeSearch(firearmResult, "psres:Person/nc:PersonWeightMeasure/nc:MeasurePointValue")).getTextContent());

    }

    private List<IdentifiableDocumentWrapper> submitDocumentPersonSearch(Document personSearchRequestMessage) throws Exception {
        XmlUtils.validateInstance("service-specifications/Person_Search_Request_Service/artifacts/service_model/information_model/Person_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                personSearchRequestMessage);
        return staticMockQuery.personSearchDocumentsAsList(personSearchRequestMessage, StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03"));
    }

    private List<IdentifiableDocumentWrapper> submitDocumentFirearmSearch(Document firearmSearchRequestMessage) throws Exception {
        XmlUtils.validateInstance("service-specifications/Firearm_Search_Request_Service/artifacts/service_model/information_model/Firearm_Search_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",
                firearmSearchRequestMessage);
        return staticMockQuery.firearmSearchDocumentsAsList(firearmSearchRequestMessage);
    }

    private void assertCorrectDocumentCount(String directory, int docCount) {
        File directoryFile = new File(directory);
        int fileCount = directoryFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.startsWith("sample");
            }
        }).length;
        assertEquals(fileCount, docCount);
    }

}
