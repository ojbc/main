package org.search.ojb.staticmock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An OSGi component that performs searches and queries against a set of static instance documents. The envisioned client of this class would be specific search and query services.
 * 
 */
public class StaticMockQuery {

    private static final Log LOG = LogFactory.getLog(StaticMockQuery.class);

    private static final String CRIMINAL_HISTORY_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/CriminalHistory";
    private static final String WARRANT_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/Warrant";
    private static final String INCIDENT_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/Incident";
    private static final String FIREARM_PRODUCTION_SAMPLES_DIRECTORY = "static-instances/FirearmRegistration";
    
    private static final String JUVENILE_HISTORY_SAMPLES_DIRECTORY = "static-instances/JuvenileHistory";
    
    static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");

    public static final String CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History";
    public static final String WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants";
    public static final String CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History";
    public static final String WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants";
    public static final String INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS";
    public static final String INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentPersonSearchRequest-RMS";
    public static final String INCIDENT_MOCK_ADAPTER_INCIDENT_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentSearchRequest-RMS";
    public static final String INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-RMS";
    public static final String FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Firearms/1.0}Submit-Person-Search---Firearms";
    public static final String FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0}SubmitFirearmSearchRequest";

    public static final String FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson";
    public static final String FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID = "{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm";;

    public static final String JUVENILE_CASE_PLAN_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}CasePlanRequest";
    public static final String JUVENILE_HEARING_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}HearingRequest";
    public static final String JUVENILE_INTAKE_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}IntakeRequest";
    public static final String JUVENILE_OFFENSE_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}OffenseRequest";
    public static final String JUVENILE_PLACEMENT_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}PlacementRequest";
    public static final String JUVENILE_REFERRAL_SYSTEM_ID="{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}ReferralRequest";
    	
    
    private ClasspathXmlDataSource criminalHistoryDataSource;
    private ClasspathXmlDataSource warrantDataSource;
    private ClasspathXmlDataSource incidentDataSource;
    private ClasspathXmlDataSource firearmRegistrationDataSource;
    
    private ClasspathXmlDataSource juvenileHistoryDataSource;

    public StaticMockQuery() {
        this(CRIMINAL_HISTORY_PRODUCTION_SAMPLES_DIRECTORY, WARRANT_PRODUCTION_SAMPLES_DIRECTORY, INCIDENT_PRODUCTION_SAMPLES_DIRECTORY, FIREARM_PRODUCTION_SAMPLES_DIRECTORY,
        	JUVENILE_HISTORY_SAMPLES_DIRECTORY);
    }

    StaticMockQuery(String criminalHistorySampleInstanceDirectoryRelativePath, String warrantSampleInstanceDirectoryRelativePath, String incidentSampleInstanceDirectoryRelativePath,
            String firearmSampleInstanceDirectoryRelativePath, String juvenileHistorySampleInstanceDirectoryRelativePath) {
        criminalHistoryDataSource = new ClasspathXmlDataSource(criminalHistorySampleInstanceDirectoryRelativePath);
        warrantDataSource = new ClasspathXmlDataSource(warrantSampleInstanceDirectoryRelativePath);
        incidentDataSource = new ClasspathXmlDataSource(incidentSampleInstanceDirectoryRelativePath);
        firearmRegistrationDataSource = new ClasspathXmlDataSource(firearmSampleInstanceDirectoryRelativePath);
        juvenileHistoryDataSource= new ClasspathXmlDataSource(juvenileHistorySampleInstanceDirectoryRelativePath);
    }

    /**
     * Get the total number of available juvenile case plan documents.
     * 
     * @return the document count
     * @throws Exception
     */
    public int getJuvenileHistoryDocumentCount() throws Exception {
        return juvenileHistoryDataSource.getDocuments().size();
    }
    
    /**
     * Get the total number of available criminal history documents.
     * 
     * @return the document count
     * @throws Exception
     */
    public int getCriminalHistoryDocumentCount() throws Exception {
        return criminalHistoryDataSource.getDocuments().size();
    }

    /**
     * Get the total number of available warrant documents.
     * 
     * @return the document count
     * @throws Exception
     */
    public int getWarrantDocumentCount() throws Exception {
        return warrantDataSource.getDocuments().size();
    }

    /**
     * Get the total number of available incident documents.
     * 
     * @return the document count
     * @throws Exception
     */
    public int getIncidentDocumentCount() throws Exception {
        return incidentDataSource.getDocuments().size();
    }

    /**
     * Get the total number of available firearm registration documents.
     * 
     * @return the document count
     * @throws Exception
     */
    public int getFirearmRegistrationDocumentCount() throws Exception {
        return firearmRegistrationDataSource.getDocuments().size();
    }

    /**
     * Perform a query (retrieving instances that match a (usually unique) identifier.
     * 
     * @param queryRequestMessage
     *            the request message, assumed to be valid against some Query Service IEPD
     * @return the matching document(s)
     * @throws Exception
     */
    public List<IdentifiableDocumentWrapper> queryDocuments(Document queryRequestMessage) throws Exception {
    	return queryDocuments(queryRequestMessage, null);
    }
    
    /**
     * Perform a query (retrieving instances that match a (usually unique) identifier.
     * 
     * @param queryRequestMessage
     *            the request message, assumed to be valid against some Query Service IEPD
     * @param context an arbitrary object that callers can pass in to provide some context for the query; this will be specific to each type of query
     * @return the matching document(s)
     * @throws Exception
     */
    public List<IdentifiableDocumentWrapper> queryDocuments(Document queryRequestMessage, Object context) throws Exception {

        Element rootElement = queryRequestMessage.getDocumentElement();

        String documentId = null;
        String systemId = null;

        String rootElementNamespace = rootElement.getNamespaceURI();
        String rootElementLocalName = rootElement.getLocalName();

        if (OjbcNamespaceContext.NS_INCIDENT_QUERY_REQUEST_DOC.equals(rootElementNamespace) && "IncidentIdentifierIncidentReportRequest".equals(rootElementLocalName)) {
            Element systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, "iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:SourceSystemNameText");
            if (systemElement == null) {
                throw new IllegalArgumentException("Invalid query request message:  must specify the system to query.");
            }
            Element systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage,
                    "iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:Incident/nc:ActivityIdentification/nc:IdentificationID");
            documentId = systemIdElement.getTextContent();
            systemId = systemElement.getTextContent();
        } else if (OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC.equals(rootElementNamespace) && "PersonFirearmRegistrationRequest".equals(rootElementLocalName)) {
            String xPath = OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":PersonFirearmRegistrationRequest/"
                    + OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":PersonFirearmRegistrationIdentification/" + OjbcNamespaceContext.NS_PREFIX_NC
                    + ":IdentificationSourceText";
            Element systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
            if (systemElement == null) {
                throw new IllegalArgumentException("Invalid query request message:  must specify the system to query.");
            }
            Element systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC
                    + ":PersonFirearmRegistrationRequest/" + OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":PersonFirearmRegistrationIdentification/"
                    + OjbcNamespaceContext.NS_PREFIX_NC + ":IdentificationID");
            documentId = systemIdElement.getTextContent();
            systemId = systemElement.getTextContent();
        } else if (OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC.equals(rootElementNamespace) && "FirearmRegistrationRequest".equals(rootElementLocalName)) {
            String xPath = OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":FirearmRegistrationRequest/"
                    + OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":FirearmRegistrationIdentification/" + OjbcNamespaceContext.NS_PREFIX_NC + ":IdentificationSourceText";
            Element systemElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, xPath);
            if (systemElement == null) {
                throw new IllegalArgumentException("Invalid query request message:  must specify the system to query.");
            }
            Element systemIdElement = (Element) XmlUtils.xPathNodeSearch(queryRequestMessage, OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC + ":FirearmRegistrationRequest/"
                    + OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_EXT + ":FirearmRegistrationIdentification/" + OjbcNamespaceContext.NS_PREFIX_NC + ":IdentificationID");
            documentId = systemIdElement.getTextContent();
            systemId = systemElement.getTextContent();
        } else {
            Element systemElement = (Element) XmlUtils.xPathNodeSearch(rootElement, "pqr:PersonRecordRequestIdentification/nc:IdentificationSourceText");
            if (systemElement == null) {
                throw new IllegalArgumentException("Invalid query request message:  must specify the system to query.");
            }
            Element systemIdElement = (Element) XmlUtils.xPathNodeSearch(rootElement, "pqr:PersonRecordRequestIdentification/nc:IdentificationID");
            documentId = systemIdElement.getTextContent();
            systemId = systemElement.getTextContent();
        }

        if (CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID.equals(systemId)) {
            return queryCriminalHistoryDocuments(documentId);
        } else if (WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID.equals(systemId)) {
            return queryWarrantDocuments(documentId);
        } else if (INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID.equals(systemId)) {
            return queryIncidentDocuments(documentId);
        } else if (FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID.equals(systemId)) {
            return queryPersonFirearmRegistrationDocuments(documentId);
        } else if (FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID.equals(systemId)) {
            return queryPersonFirearmRegistrationDocuments(documentId);
        } else {
            throw new IllegalArgumentException("Unknown system name: " + systemId);
        }

    }

    /**
     * Perform a search (searching for instances that match a set of criteria) and returning the data in those matching instances as a Search Results message. The type of result message will depend on
     * the type of request message passed in.
     * 
     * @param searchRequestMessage
     *            the request message, assumed to be valid against some Search Service's IEPD
     * @return the search results
     * @throws Exception
     */
    public Document searchDocuments(Document searchRequestMessage) throws Exception {
        DateTime baseDate = new DateTime();
        return searchDocuments(searchRequestMessage, baseDate);
    }

    /**
     * Perform a search (searching for instances that match a set of criteria) and returning the data in those matching instances as a Search Results message. The type of result message will depend on
     * the type of request message passed in.
     * 
     * @param searchRequestMessage
     *            the request message, assumed to be valid against some Search Service's IEPD
     * @param context an arbitrary object that a caller can pass in to provide context for the request; specific to each type of search
     * @return the search results
     * @throws Exception
     */
    public Document searchDocuments(Document searchRequestMessage, Object context) throws Exception {
        DateTime baseDate = new DateTime();
        return searchDocuments(searchRequestMessage, baseDate, context);
    }

    Document searchDocuments(Document searchRequestMessage, DateTime baseDate) throws Exception {
        return searchDocuments(searchRequestMessage, baseDate, null);
	}

	Document searchDocuments(Document searchRequestMessage, DateTime baseDate, Object context) throws Exception {
        Element rootElement = searchRequestMessage.getDocumentElement();
        String rootNamespaceURI = rootElement.getNamespaceURI();
        String rootLocalName = rootElement.getLocalName();
        if (OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "PersonSearchRequest".equals(rootLocalName)) {
            return personSearchDocuments(searchRequestMessage, baseDate);
        } else if (OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentPersonSearchRequest".equals(rootLocalName)) {
            return incidentPersonSearchDocuments(searchRequestMessage, baseDate);
        } else if (OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "FirearmSearchRequest".equals(rootLocalName)) {
            return firearmSearchDocuments(searchRequestMessage);
        } else if (OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentSearchRequest".equals(rootLocalName)) {
            return incidentSearchDocuments(searchRequestMessage, baseDate);
        }
        throw new IllegalArgumentException("Invalid message: {" + rootNamespaceURI + "}" + rootLocalName);
    }

    Document incidentSearchDocuments(Document incidentSearchRequestMessage, DateTime baseDate) throws Exception {
        
        Document errorReturn = getIncidentSearchStaticErrorResponse(incidentSearchRequestMessage);
        
        if (errorReturn != null) {
            return errorReturn;
        }
        
        List<IdentifiableDocumentWrapper> instanceWrappers = incidentSearchDocumentsAsList(incidentSearchRequestMessage, baseDate);
        Document ret = buildIncidentSearchResultsMessage(instanceWrappers, "IncidentSearchResults", "IncidentSearchResult", INCIDENT_MOCK_ADAPTER_INCIDENT_SEARCH_SYSTEM_ID);
        return ret;
        
    }

    private static final boolean isMissing(String s) {
        return s == null || s.trim().length() == 0;
    }

    List<IdentifiableDocumentWrapper> incidentSearchDocumentsAsList(Document incidentSearchRequestMessage, DateTime baseDate) throws Exception {

        Element rootElement = incidentSearchRequestMessage.getDocumentElement();
        String rootNamespaceURI = rootElement.getNamespaceURI();
        String rootLocalName = rootElement.getLocalName();
        if (!(OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentSearchRequest".equals(rootLocalName))) {
            throw new IllegalArgumentException("Invalid message, must have {" + OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC + "}IncidentSearchRequest as the root " + "instead of {"
                    + rootNamespaceURI + "}" + rootLocalName);
        }

        NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "isr:SourceSystemNameText");
        if (systemElements == null || systemElements.getLength() == 0) {
            throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
        }

        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

        String searchXPath = buildIncidentSearchXPathFromMessage(incidentSearchRequestMessage);

        String dateLowerString = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
        String dateUpperString = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange/nc:EndDate/nc:DateTime");

        LOG.debug("dateLowerString=" + dateLowerString);
        LOG.debug("dateUpperString=" + dateUpperString);
        
        if (searchXPath == null && isMissing(dateLowerString) && isMissing(dateUpperString)) {
            return ret;
        }

        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();

        for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

            Document d = dw.getDocument();
            LOG.debug("Searching document " + dw.getId());

            boolean match = (searchXPath == null || XmlUtils.xPathNodeSearch(d, searchXPath) != null);
            LOG.debug("Match=" + match + " based on xpath eval");

            if (match && (!(isMissing(dateLowerString) && isMissing(dateUpperString)))) {

                LOG.debug("In date evaluation");
                
                String incidentStartDateString = XmlUtils
                        .xPathStringSearch(
                                d,
                                "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
                String incidentEndDateString = XmlUtils
                        .xPathStringSearch(
                                d,
                                "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityDateRange/nc:EndDate/nc:DateTime");

                if (isMissing(incidentStartDateString) && isMissing(incidentEndDateString)) {
                    match = false;
                } else {
                    DateTime incidentStartDateTime = null;
                    DateTime incidentEndDateTime = null;
                    try {
                        incidentStartDateTime = dateTimeFormatter.parseDateTime(incidentStartDateString);
                        incidentEndDateTime = dateTimeFormatter.parseDateTime(incidentEndDateString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.warn("Unable to parse date (either " + incidentStartDateString + " or " + incidentEndDateString
                                + "). Note that null dates are ok...just means it's not part of the incident");
                    }
                    LOG.debug("incidentStartDateTime=" + incidentStartDateTime);
                    LOG.debug("incidentEndDateTime=" + incidentEndDateTime);
                    if (incidentStartDateTime == null && incidentEndDateTime == null) {
                        match = false;
                    } else {
                        if (!isMissing(dateLowerString)) {
                            DateTime lowerDateTime = dateTimeFormatter.parseDateTime(dateLowerString);
                            if (incidentEndDateTime != null  && incidentEndDateTime.isBefore(lowerDateTime)) {
                                match = false;
                            } else if (incidentEndDateTime == null && incidentStartDateTime.isBefore(lowerDateTime)) {
                                match = false;
                            }
                        }
                        if (!isMissing(dateUpperString)) {
                            DateTime upperDateTime = dateTimeFormatter.parseDateTime(dateUpperString);
                            if (incidentStartDateTime != null  && incidentStartDateTime.isAfter(upperDateTime)) {
                                match = false;
                            } else if (incidentStartDateTime == null && incidentEndDateTime.isBefore(upperDateTime)) {
                                match = false;
                            }
                        }
                    }
                }

            }

            if (match) {
                ret.add(dw);
            }

        }

        return ret;

    }

    Document incidentPersonSearchDocuments(Document incidentPersonSearchRequestMessage, DateTime baseDate) throws Exception {
        List<IdentifiableDocumentWrapper> instanceWrappers = incidentPersonSearchDocumentsAsList(incidentPersonSearchRequestMessage, baseDate);
        Document ret = buildIncidentSearchResultsMessage(instanceWrappers, "IncidentPersonSearchResults", "IncidentPersonSearchResult", INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID);
        return ret;
    }

    private Document buildIncidentSearchResultsMessage(List<IdentifiableDocumentWrapper> instanceWrappers, String rootElementName, String resultElementName, String sourceSystemName)
            throws ParserConfigurationException, Exception {

        Document ret = createNewDocument();

        Element root = ret.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC, rootElementName);
        ret.appendChild(root);
        String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_DOC);
        root.setPrefix(prefix);

        int incidentSequence = 1;

        for (IdentifiableDocumentWrapper instanceWrapper : instanceWrappers) {

            Document instance = instanceWrapper.getDocument();

            Element isrElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_EXT, resultElementName);
            Element incidentElement = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_EXT, "Incident");
            String incidentId = "I" + incidentSequence;
            XmlUtils.addAttribute(incidentElement, OjbcNamespaceContext.NS_STRUCTURES, "id", incidentId);
            Element e = XmlUtils.appendElement(incidentElement, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
            String pathToInstanceIncident = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']";
            Element instanceIncidentIdElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToInstanceIncident + "/nc:ActivityIdentification/nc:IdentificationID");
            e.setTextContent(instanceIncidentIdElement.getTextContent());
            Element instanceIncidentDateElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToInstanceIncident + "/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
            e = XmlUtils.appendElement(incidentElement, OjbcNamespaceContext.NS_NC, "ActivityDate");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "DateTime");
            e.setTextContent(instanceIncidentDateElement.getTextContent());
            e = XmlUtils.appendElement(incidentElement, OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_EXT, "IncidentCategoryCode");
            e.setTextContent("Law");

            String pathToLocation = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityLocation/nc:Location[@s:id='incident-location']";

            e = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_NC, "Location");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "id", incidentId + "-L1");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "LocationAddress");
            Element structuredAddressElement = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_EXT, "StructuredAddress");
            Element locationStreetElement = XmlUtils.appendElement(structuredAddressElement, OjbcNamespaceContext.NS_NC, "LocationStreet");
            e = XmlUtils.appendElement(locationStreetElement, OjbcNamespaceContext.NS_NC, "StreetNumberText");
            Element instanceStreetNumberElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText");
            e.setTextContent(instanceStreetNumberElement.getTextContent());
            e = XmlUtils.appendElement(locationStreetElement, OjbcNamespaceContext.NS_NC, "StreetName");
            Element instanceStreetElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName");
            e.setTextContent(instanceStreetElement.getTextContent());
            e = XmlUtils.appendElement(structuredAddressElement, OjbcNamespaceContext.NS_NC, "LocationCityName");
            Element instanceCityElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName");
            e.setTextContent(instanceCityElement.getTextContent());
            Element instanceStateElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateUSPostalServiceCode");
            e = XmlUtils.appendElement(structuredAddressElement, OjbcNamespaceContext.NS_NC, "LocationStateUSPostalServiceCode");
            e.setTextContent(instanceStateElement.getTextContent());
            Element instanceZipElement = (Element) XmlUtils.xPathNodeSearch(instance, pathToLocation + "/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode");
            e = XmlUtils.appendElement(structuredAddressElement, OjbcNamespaceContext.NS_NC, "LocationPostalCode");
            e.setTextContent(instanceZipElement.getTextContent());

            e = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_NC, "Organization");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "id", incidentId + "-O1");
            Element instanceOrganizationElement = (Element) XmlUtils
                    .xPathNodeSearch(instance,
                            "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityOrganization/nc:Organization/nc:OrganizationName");
            e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "OrganizationName");
            e.setTextContent(instanceOrganizationElement.getTextContent());

            Element a = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_NC, "ActivityReportingOrganizationAssociation");
            e = XmlUtils.appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incidentId);
            e = XmlUtils.appendElement(a, OjbcNamespaceContext.NS_NC, "OrganizationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incidentId + "-O1");

            a = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_JXDM_41, "ActivityLocationAssociation");
            e = XmlUtils.appendElement(a, OjbcNamespaceContext.NS_NC, "ActivityReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incidentId);
            e = XmlUtils.appendElement(a, OjbcNamespaceContext.NS_NC, "LocationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", incidentId + "-L1");

            e = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_INCIDENT_SEARCH_RESULTS_EXT, "SourceSystemNameText");
            e.setTextContent(sourceSystemName);

            Element systemIdentifierElement = XmlUtils.appendElement(isrElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");
            e = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(instanceWrapper.getId());
            e = XmlUtils.appendElement(systemIdentifierElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
            e.setTextContent("Incident System");

            incidentSequence++;

        }
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;
    }

    private Document createNewDocument() throws ParserConfigurationException {
        DocumentBuilder db = getNewDocumentBuilder();
        Document ret = db.newDocument();
        return ret;
    }

    private DocumentBuilder getNewDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db;
    }

    Document personSearchDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        
        Document errorReturn = getPersonSearchStaticErrorResponse(personSearchRequestMessage);
        
        if (errorReturn != null) {
            return errorReturn;
        }

        List<IdentifiableDocumentWrapper> instanceWrappers = personSearchDocumentsAsList(personSearchRequestMessage, baseDate);

        Document ret = createNewDocument();

        Element root = ret.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC, "PersonSearchResults");
        ret.appendChild(root);
        String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC);
        root.setPrefix(prefix);

        for (IdentifiableDocumentWrapper instanceWrapper : instanceWrappers) {

            Document instance = instanceWrapper.getDocument();

            Element psrElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "PersonSearchResult");
            Element personElement = XmlUtils.appendElement(psrElement, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "Person");
            Element documentRootElement = instance.getDocumentElement();

            SearchValueXPaths xPaths = null;
            if (OjbcNamespaceContext.NS_CH_DOC.equals(documentRootElement.getNamespaceURI()) && "CriminalHistory".equals(documentRootElement.getLocalName())) {
                xPaths = getCriminalHistoryXPaths();
            } else if (OjbcNamespaceContext.NS_WARRANT.equals(documentRootElement.getNamespaceURI()) && "Warrants".equals(documentRootElement.getLocalName())) {
                xPaths = getWarrantXPaths();
            } else if (OjbcNamespaceContext.NS_FIREARM_DOC.equals(documentRootElement.getNamespaceURI()) && "PersonFirearmRegistrationQueryResults".equals(documentRootElement.getLocalName())) {
                xPaths = getFirearmRegistrationXPaths();
            } else if (OjbcNamespaceContext.NS_IR.equals(documentRootElement.getNamespaceURI()) && "IncidentReport".equals(documentRootElement.getLocalName())) {
                xPaths = getIncidentXPaths();
            } else {
                throw new IllegalStateException("Unsupported document root element: " + documentRootElement.getLocalName());
            }

            Element dobElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.birthdateXPath);
            Element ageElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.ageXPath);
            if (dobElement != null) {
                Element e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
                String dob = dobElement.getTextContent();
                e.setTextContent(String.valueOf(Years.yearsBetween(DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dob), baseDate).getYears()));
                e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(dob);
            } else if (ageElement != null) {
                Element e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonAgeMeasure");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
                e.setTextContent(ageElement.getTextContent());
            }

            Element heightElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.heightXPath);
            if (heightElement != null) {
                Element phm = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonHeightMeasure");
                Element e = XmlUtils.appendElement(phm, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
                e.setTextContent(heightElement.getTextContent());
                e = XmlUtils.appendElement(phm, OjbcNamespaceContext.NS_NC, "LengthUnitCode");
                e.setTextContent("INH");
            }
            Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.lastNameXPath);
            Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.firstNameXPath);
            Element middleNameElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.middleNameXPath);
            if (lastNameElement != null || firstNameElement != null || middleNameElement != null) {
                Element nameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
                Element e = null;
                if (firstNameElement != null) {
                    e = XmlUtils.appendElement(nameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
                    e.setTextContent(firstNameElement.getTextContent());
                }
                if (middleNameElement != null) {
                    e = XmlUtils.appendElement(nameElement, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
                    e.setTextContent(middleNameElement.getTextContent());
                }
                if (lastNameElement != null) {
                    e = XmlUtils.appendElement(nameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
                    e.setTextContent(lastNameElement.getTextContent());
                }
            }
            Element raceElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.raceXPath);
            if (raceElement != null) {
                Element e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonRaceCode");
                e.setTextContent(raceElement.getTextContent());
            }
            Element sexElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.sexXPath);
            if (sexElement != null) {
                Element e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSexCode");
                e.setTextContent(sexElement.getTextContent());
            }
            Element ssnElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.ssnXPath);
            if (ssnElement != null) {
                Element e = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                e.setTextContent(ssnElement.getTextContent());
            }
            Element weightElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.weightXPath);
            if (weightElement != null) {
                Element phm = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonWeightMeasure");
                Element e = XmlUtils.appendElement(phm, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
                e.setTextContent(weightElement.getTextContent());
                e = XmlUtils.appendElement(phm, OjbcNamespaceContext.NS_NC, "WeightUnitCode");
                e.setTextContent("LBR");
            }
            Element dlElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.dlXPath);
            Element fbiElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.fbiXPath);
            Element sidElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.sidXPath);
            if (dlElement != null || fbiElement != null || sidElement != null) {
                Element personAugElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
                if (dlElement != null) {
                    Element dlJurisdictionElement = (Element) XmlUtils.xPathNodeSearch(instance, xPaths.dlJurisdictionXPath);
                    Element e = XmlUtils.appendElement(personAugElement, OjbcNamespaceContext.NS_NC, "DriverLicense");
                    Element dlIdElement = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "DriverLicenseIdentification");
                    e = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    e.setTextContent(dlElement.getTextContent());
                    if (dlJurisdictionElement != null) {
                        e = XmlUtils.appendElement(dlIdElement, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
                        e.setTextContent(dlJurisdictionElement.getTextContent());
                    }
                }
                if (fbiElement != null) {
                    Element e = XmlUtils.appendElement(personAugElement, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
                    e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    e.setTextContent(fbiElement.getTextContent());
                }
                if (sidElement != null) {
                    Element e = XmlUtils.appendElement(personAugElement, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");
                    e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                    e.setTextContent(sidElement.getTextContent());
                }
            }

            Element sourceSystem = XmlUtils.appendElement(psrElement, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "SourceSystemNameText");
            sourceSystem.setTextContent(xPaths.searchSystemId);
            Element sourceSystemIdentifierParentElement = XmlUtils.appendElement(psrElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");
            Element e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(xPaths.getSystemIdentifier(instanceWrapper));
            e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
            e.setTextContent(xPaths.systemName);
            e = XmlUtils.appendElement(psrElement, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "SearchResultCategoryText");
            e.setTextContent(xPaths.recordType);

        }

        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;

    }

    private Document getPersonSearchStaticErrorResponse(Document personSearchRequestMessage) throws Exception {
        PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
        Document ret = null;
        ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
        if ("AccessDenied".equals(psp.lastName))
        {
            ret = errorResourceRetriever.getPersonSearchAccessDeniedDocument();
        } else if ("ErrorReported".equals(psp.lastName))
        {
            ret = errorResourceRetriever.getPersonSearchErrorReportedDocument();
        }
        return ret;
    }

    private Document getIncidentSearchStaticErrorResponse(Document incidentSearchRequestMessage) throws Exception {
        String incidentNumber = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        Document ret = null;
        ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
        if ("AccessDenied".equals(incidentNumber))
        {
            ret = errorResourceRetriever.getIncidentSearchAccessDeniedDocument();
        }
        return ret;
    }

    private Document getFirearmSearchStaticErrorResponse(Document firearmSearchRequestMessage) throws Exception {
        String firearmSerialNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
            "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        Document ret = null;
        ErrorResourceRetriever errorResourceRetriever = new ErrorResourceRetriever();
        if ("AccessDenied".equals(firearmSerialNumber))
        {
            ret = errorResourceRetriever.getFirearmSearchAccessDeniedDocument();
        }
        return ret;
    }

    List<IdentifiableDocumentWrapper> incidentPersonSearchDocumentsAsList(Document incidentPersonSearchRequestMessage, DateTime baseDate) throws Exception {

        Element rootElement = incidentPersonSearchRequestMessage.getDocumentElement();
        String rootNamespaceURI = rootElement.getNamespaceURI();
        String rootLocalName = rootElement.getLocalName();
        if (!(OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "IncidentPersonSearchRequest".equals(rootLocalName))) {
            throw new IllegalArgumentException("Invalid message, must have {" + OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC + "}IncidentPersonSearchRequest as the root " + "instead of {"
                    + rootNamespaceURI + "}" + rootLocalName);
        }

        NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "isr:SourceSystemNameText");
        int systemElementCount;
        if (systemElements == null || (systemElementCount = systemElements.getLength()) == 0) {
            throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
        }

        Element personIdElement = (Element) XmlUtils.xPathNodeSearch(incidentPersonSearchRequestMessage,
                "/isr-doc:IncidentPersonSearchRequest/nc:Person/nc:PersonOtherIdentification/nc:IdentificationID");

        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

        if (personIdElement != null) {

            String id = personIdElement.getTextContent();

            for (int i = 0; i < systemElementCount; i++) {
                for (IdentifiableDocumentWrapper dw : incidentDataSource.getDocuments()) {

                    Document d = dw.getDocument();
                    Element documentPersonIdElement = (Element) XmlUtils
                            .xPathNodeSearch(
                                    d,
                                    "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person/nc:PersonOtherIdentification/nc:IdentificationID");
                    String documentPersonId = documentPersonIdElement.getTextContent();
                    if (id.equals(documentPersonId)) {
                        ret.add(dw);
                    }
                }
            }

        }

        return ret;

    }

    static String buildIncidentSearchXPathFromMessage(Document incidentSearchRequestMessage) throws Exception {

        String incidentNumber = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        String incidentCategory = XmlUtils.xPathStringSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        String city = XmlUtils.xPathStringSearch(incidentSearchRequestMessage,
                "/isr-doc:IncidentSearchRequest/nc:Location/nc:LocationAddress/isr:StructuredAddress/incident-location-codes-demostate:LocationCityTownCode");

        List<String> conditions = new ArrayList<String>();

        if (incidentNumber != null && incidentNumber.trim().length() > 0) {
            conditions.add("lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/nc:ActivityIdentification/nc:IdentificationID='" + incidentNumber + "'");
        }

        if (city != null && city.trim().length() > 0) {
            conditions
                    .add("lexs:Digest/lexsdigest:EntityLocation/nc:Location[nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName='"
                            + city
                            + "' and @s:id=/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref]");
        }

        if (incidentCategory != null && incidentCategory.trim().length() > 0) {
            conditions.add("lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Incident[inc-ext:IncidentCategoryCode='" + incidentCategory + "']");
        }

        if (conditions.isEmpty()) {
            return null;
        }

        StringBuffer xPath = new StringBuffer();
        xPath.append("/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage[");

        for (String condition : conditions) {
            xPath.append(condition).append(" and ");
        }
        xPath.setLength(xPath.length() - 5);

        xPath.append("]");

        return xPath.toString();
    }

    static String buildFirearmSearchXPathFromMessage(Document firearmSearchRequestMessage) throws Exception {

        String firearmSerialNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        String firearmMakeCode = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        String firearmMakeText = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        String firearmModel = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        String firearmCategoryCode = XmlUtils.xPathStringSearch(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        String registrationNumber = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
        String registrationCounty = XmlUtils.xPathStringSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration/nc:LocationCountyName");

        List<String> firearmConditions = new ArrayList<String>();
        List<String> firearmMakeConditions = new ArrayList<String>();
        List<String> registrationConditions = new ArrayList<String>();

        if (firearmSerialNumber != null && firearmSerialNumber.trim().length() > 0) {
            firearmConditions.add("nc:ItemSerialIdentification/nc:IdentificationID='" + firearmSerialNumber + "'");
        }
        if (firearmMakeCode != null && firearmMakeCode.trim().length() > 0) {
            firearmMakeConditions.add("firearms-codes-demostate:FirearmMakeCode='" + firearmMakeCode + "'");
        }
        if (firearmMakeText != null && firearmMakeText.trim().length() > 0) {
            firearmMakeConditions.add("firearm-search-req-ext:FirearmMakeText='" + firearmMakeText + "'");
        }
        if (firearmModel != null && firearmModel.trim().length() > 0) {
            firearmConditions.add("nc:ItemModelName='" + firearmModel + "'");
        }
        if (firearmCategoryCode != null && firearmCategoryCode.trim().length() > 0) {
            firearmConditions.add("nc:FirearmCategoryCode='" + firearmCategoryCode + "'");
        }

        if (registrationNumber != null && registrationNumber.trim().length() > 0) {
            registrationConditions.add("nc:RegistrationIdentification/nc:IdentificationID='" + registrationNumber + "'");
        }
        if (registrationCounty != null && registrationCounty.trim().length() > 0) {
            registrationConditions.add("nc:LocationCountyName='" + registrationCounty + "'");
        }

        if (firearmConditions.isEmpty() && registrationConditions.isEmpty()) {
            // no search parameters specified
            return null;
        }

        StringBuffer searchXPath = new StringBuffer("/firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:Firearm[");

        if (!firearmConditions.isEmpty()) {
            for (String condition : firearmConditions) {
                searchXPath.append(condition).append(" and ");
            }
            searchXPath.setLength(searchXPath.length() - 5);
        }

        if (!firearmMakeConditions.isEmpty()) {
            if (!firearmConditions.isEmpty()) {
                searchXPath.append(" and ");
            }
            searchXPath.append("(");
            for (String condition : firearmMakeConditions) {
                searchXPath.append(condition).append(" or ");
            }
            searchXPath.setLength(searchXPath.length() - 4);
            searchXPath.append(")");
        }

        if (!registrationConditions.isEmpty()) {
            if (!(firearmMakeConditions.isEmpty() || firearmConditions.isEmpty())) {
                searchXPath.append(" and ");
            }
            searchXPath
                    .append("@s:id = /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemRegistrationReference/@s:ref = /firearm-doc:PersonFirearmRegistrationQueryResults/firearm-ext:ItemRegistration[");
            for (String condition : registrationConditions) {
                searchXPath.append(condition).append(" and ");
            }
            searchXPath.setLength(searchXPath.length() - 5);
            searchXPath.append("]/@s:id]/nc:ItemReference/@s:ref");
        }

        searchXPath.append("]");
        return searchXPath.toString();

    }

    Document firearmSearchDocuments(Document firearmSearchRequestMessage) throws Exception {
        
        Document errorReturn = getFirearmSearchStaticErrorResponse(firearmSearchRequestMessage);
        
        if (errorReturn != null) {
            return errorReturn;
        }

        Document ret = createNewDocument();

        List<IdentifiableDocumentWrapper> searchResultsList = firearmSearchDocumentsAsList(firearmSearchRequestMessage);

        Element root = ret.createElementNS(OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_DOC, "FirearmSearchResults");
        ret.appendChild(root);
        String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_DOC);
        root.setPrefix(prefix);

        int index = 1;

        for (IdentifiableDocumentWrapper instanceWrapper : searchResultsList) {

            Document instance = instanceWrapper.getDocument();

            Element fsrElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "FirearmSearchResult");

            Element personElement = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_NC, "Person");
            XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES, "id", createFirearmPersonElementID(index));
            Element personNameElement = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_NC, "PersonName");
            String s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonGivenName");
            Element e = null;
            if (s != null) {
                e = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonGivenName");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonMiddleName");
            if (s != null) {
                e = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonSurName");
            if (s != null) {
                e = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC, "PersonSurName");
                e.setTextContent(s);
            }

            Element firearmElement = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "Firearm");
            XmlUtils.addAttribute(firearmElement, OjbcNamespaceContext.NS_STRUCTURES, "id", createFirearmElementID(index));

            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "ItemSerialIdentification");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:ItemModelName");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "ItemModelName");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCategoryCode");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCategoryCode");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCategoryDescriptionCode");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCategoryDescriptionCode");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmCaliberCode");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCaliberCode");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/nc:FirearmGaugeText");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmGaugeText");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARMS_CODES_DEMOSTATE, "FirearmMakeCode");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm/firearm-ext:FirearmStatus/firearm-ext:FirearmStatusText");
            if (s != null) {
                e = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "FirearmStatus");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "FirearmStatusText");
                e.setTextContent(s);
            }

            Element registrationElement = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "ItemRegistration");
            XmlUtils.addAttribute(registrationElement, OjbcNamespaceContext.NS_STRUCTURES, "id", createFirearmRegistrationElementID(index));
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
            if (s != null) {
                e = XmlUtils.appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "RegistrationIdentification");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:LocationCountyName");
            if (s != null) {
                e = XmlUtils.appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "LocationCountyName");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/nc:RegistrationEffectiveDate/nc:Date");
            if (s != null) {
                e = XmlUtils.appendElement(registrationElement, OjbcNamespaceContext.NS_NC, "RegistrationEffectiveDate");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance,
                    "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/firearm-ext:RegistrationStatus/firearm-ext:FirearmRegistrationStatusText");
            if (s != null) {
                e = XmlUtils.appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "RegistrationStatus");
                e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "FirearmRegistrationStatusText");
                e.setTextContent(s);
            }
            s = XmlUtils.xPathStringSearch(instance, "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:ItemRegistration/firearm-ext:RegistrationNotesText");
            if (s != null) {
                e = XmlUtils.appendElement(registrationElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "RegistrationNotesText");
                e.setTextContent(s);
            }

            Element propertyRegistrationAssociationElement = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_NC, "PropertyRegistrationAssociation");
            e = XmlUtils.appendElement(propertyRegistrationAssociationElement, OjbcNamespaceContext.NS_NC, "ItemRegistrationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", createFirearmRegistrationElementID(index));
            e = XmlUtils.appendElement(propertyRegistrationAssociationElement, OjbcNamespaceContext.NS_NC, "ItemReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", createFirearmElementID(index));
            e = XmlUtils.appendElement(propertyRegistrationAssociationElement, OjbcNamespaceContext.NS_NC, "ItemRegistrationHolderReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", createFirearmPersonElementID(index));

            Element sourceSystem = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_RESULT_EXT, "SourceSystemNameText");
            sourceSystem.setTextContent(FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID);
            Element sourceSystemIdentifierParentElement = XmlUtils.appendElement(fsrElement, OjbcNamespaceContext.NS_INTEL, "SystemIdentifier");
            e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(instanceWrapper.getId());
            e = XmlUtils.appendElement(sourceSystemIdentifierParentElement, OjbcNamespaceContext.NS_INTEL, "SystemName");
            e.setTextContent("Statewide Firearms Registry");

            index++;

        }

        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;

    }

    static String createFirearmElementID(int positionInDocument) {
        return "F" + positionInDocument;
    }

    static String createFirearmPersonElementID(int positionInDocument) {
        return "P" + positionInDocument;
    }

    static String createFirearmRegistrationElementID(int positionInDocument) {
        return "R" + positionInDocument;
    }

    List<IdentifiableDocumentWrapper> firearmSearchDocumentsAsList(Document firearmSearchRequestMessage) throws Exception {

        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();

        Element rootElement = firearmSearchRequestMessage.getDocumentElement();
        String rootNamespaceURI = rootElement.getNamespaceURI();
        String rootLocalName = rootElement.getLocalName();
        if (!(OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "FirearmSearchRequest".equals(rootLocalName))) {
            throw new IllegalArgumentException("Invalid message, must have {" + OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_DOC + "}FirearmSearchRequest as the root " + "instead of {"
                    + rootNamespaceURI + "}" + rootLocalName);
        }
        NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "firearm-search-req-ext:SourceSystemNameText");
        if (systemElements == null || (systemElements.getLength()) == 0) {
            throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
        }

        String searchXPath = buildFirearmSearchXPathFromMessage(firearmSearchRequestMessage);
        // LOG.info(searchXPath);

        if (searchXPath == null) {
            return ret;
        }

        for (IdentifiableDocumentWrapper dw : firearmRegistrationDataSource.getDocuments()) {

            Document d = dw.getDocument();
            LOG.debug("Searching document " + dw.getId());

            NodeList matches = XmlUtils.xPathNodeListSearch(d, searchXPath);
            for (int i = 0; i < matches.getLength(); i++) {
                Node match = matches.item(i);
                String firearmId = XmlUtils.xPathStringSearch(match, "@s:id");
                ret.add(new IdentifiableDocumentWrapper(createFirearmRegistrationDocument(dw.getDocument(), firearmId), dw.getId() + ":" + firearmId));
            }

        }
        return ret;
    }

    List<IdentifiableDocumentWrapper> personSearchDocumentsAsList(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        Element rootElement = personSearchRequestMessage.getDocumentElement();
        String rootNamespaceURI = rootElement.getNamespaceURI();
        String rootLocalName = rootElement.getLocalName();
        if (!(OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_DOC.equals(rootNamespaceURI) && "PersonSearchRequest".equals(rootLocalName))) {
            throw new IllegalArgumentException("Invalid message, must have {" + OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_DOC + "}PersonSearchRequest as the root " + "instead of {"
                    + rootNamespaceURI + "}" + rootLocalName);
        }
        // XmlUtils.printNode(personSearchRequestMessage);
        NodeList systemElements = XmlUtils.xPathNodeListSearch(rootElement, "psr:SourceSystemNameText");
        int systemElementCount;
        if (systemElements == null || (systemElementCount = systemElements.getLength()) == 0) {
            throw new IllegalArgumentException("Invalid query request message:  must specify at least one system to query.");
        }
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        for (int i = 0; i < systemElementCount; i++) {
            Element systemElement = (Element) systemElements.item(i);
            String systemId = systemElement.getTextContent();
            if (CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
                ret.addAll(personSearchCriminalHistoryDocuments(personSearchRequestMessage, baseDate));
            } else if (WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
                ret.addAll(personSearchWarrantDocuments(personSearchRequestMessage, baseDate));
            } else if (FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
                ret.addAll(personSearchFirearmRegistrationDocuments(personSearchRequestMessage, baseDate));
            } else if (INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(systemId)) {
                ret.addAll(personSearchIncidentDocuments(personSearchRequestMessage, baseDate));
            } else {
                throw new IllegalArgumentException("Unsupported system name: " + systemId);
            }
        }
        return ret;
    }

    private List<IdentifiableDocumentWrapper> personSearchIncidentDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getIncidentXPaths(), incidentDataSource);
    }

    private List<IdentifiableDocumentWrapper> personSearchWarrantDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getWarrantXPaths(), warrantDataSource);
    }

    private List<IdentifiableDocumentWrapper> personSearchFirearmRegistrationDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getFirearmRegistrationXPaths(), firearmRegistrationDataSource);
    }

    private List<IdentifiableDocumentWrapper> personSearchCriminalHistoryDocuments(Document personSearchRequestMessage, DateTime baseDate) throws Exception {
        return personSearchDocumentsAsList(personSearchRequestMessage, baseDate, getCriminalHistoryXPaths(), criminalHistoryDataSource);
    }

    private SearchValueXPaths getIncidentXPaths() {
        SearchValueXPaths xPaths = new SearchValueXPaths() {
            public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
                Document d = documentWrapper.getDocument();
                try {
                    Element e = (Element) XmlUtils
                            .xPathNodeSearch(
                                    d,
                                    "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person/nc:PersonOtherIdentification/nc:IdentificationID");
                    return e.getTextContent();
                } catch (Exception e) {
                    throw new RuntimeException(e); // TODO: fix
                }
            }
        };
        String rootXPath = "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson[jxdm40:IncidentSubject]/lexsdigest:Person";
        xPaths.ageXPath = rootXPath + "/nc:PersonAgeMeasure/nc:MeasurePointValue";
        xPaths.birthdateXPath = rootXPath + "/nc:PersonBirthDate/nc:Date";
        xPaths.ssnXPath = rootXPath + "/nc:PersonSSNIdentification/nc:IdentificationID";
        xPaths.sidXPath = rootXPath + "/jxdm40:PersonAugmentation/jxdm40:PersonStateFingerprintIdentification/nc:IdentificationID";
        xPaths.fbiXPath = rootXPath + "/jxdm40:PersonAugmentation/jxdm40:PersonFBIIdentification/nc:IdentificationID";
        xPaths.dlXPath = rootXPath + "/jxdm40:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID";
        xPaths.dlJurisdictionXPath = rootXPath + "/jxdm40:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/jxdm40:IdentificationJurisdictionNCICLISCode";
        xPaths.lastNameXPath = rootXPath + "/nc:PersonAlternateName/nc:PersonSurName";
        xPaths.middleNameXPath = null; // we could support this, but the minimal instance andrew sent didn't have it. TODO: support
        xPaths.firstNameXPath = rootXPath + "/nc:PersonName/nc:PersonGivenName";
        xPaths.eyeColorXPath = rootXPath + "/nc:PersonEyeColorText";
        xPaths.hairColorXPath = rootXPath + "/nc:PersonHairColorText";
        xPaths.raceXPath = rootXPath + "/nc:PersonRaceText";
        xPaths.sexXPath = rootXPath + "/nc:PersonSexCode";
        xPaths.heightXPath = rootXPath + "/nc:PersonHeightMeasure/nc:MeasurePointValue";
        xPaths.weightXPath = rootXPath + "/nc:PersonWeightMeasure/nc:MeasurePointValue";
        xPaths.searchSystemId = INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
        xPaths.systemName = "Demo RMS";
        xPaths.recordType = "Incident";
        return xPaths;
    }

    private SearchValueXPaths getWarrantXPaths() {
        SearchValueXPaths xPaths = new SearchValueXPaths();
        xPaths.ageXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonAgeMeasure/nc:MeasurePointValue";
        xPaths.birthdateXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonBirthDate/nc:Date";
        xPaths.ssnXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonSSNIdentification/nc:IdentificationID";
        xPaths.sidXPath = null;
        xPaths.fbiXPath = null;
        xPaths.dlXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID";
        xPaths.dlJurisdictionXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText";
        xPaths.lastNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonSurName";
        xPaths.middleNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonMiddleName";
        xPaths.firstNameXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonName/nc:PersonGivenName";
        xPaths.eyeColorXPath = null;
        xPaths.hairColorXPath = null;
        xPaths.raceXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonRaceCode";
        xPaths.sexXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonSexCode";
        xPaths.heightXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonHeightMeasure/nc:MeasurePointValue";
        xPaths.weightXPath = "/warrant:Warrants/warrant-ext:eBWResults/jxdm41:Person/nc:PersonWeightMeasure/nc:MeasurePointValue";
        xPaths.searchSystemId = WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
        xPaths.systemName = "Statewide Warrant System";
        xPaths.recordType = "Bench Warrant";
        return xPaths;
    }

    private SearchValueXPaths getFirearmRegistrationXPaths() {
        SearchValueXPaths xPaths = new SearchValueXPaths();
        xPaths.ageXPath = null;
        xPaths.birthdateXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonBirthDate/nc:Date";
        xPaths.ssnXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonSSNIdentification/nc:IdentificationID";
        xPaths.sidXPath = null;
        xPaths.fbiXPath = null;
        xPaths.dlXPath = null;
        xPaths.dlJurisdictionXPath = null;
        xPaths.lastNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonSurName";
        xPaths.middleNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonMiddleName";
        xPaths.firstNameXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonName/nc:PersonGivenName";
        xPaths.eyeColorXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonEyeColorCode";
        xPaths.hairColorXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonHairColorCode";
        xPaths.raceXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonRaceCode";
        xPaths.sexXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonSexCode";
        xPaths.heightXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonHeightMeasure/nc:MeasureText";
        xPaths.weightXPath = "/firearm-doc:PersonFirearmRegistrationQueryResults/nc:Person/nc:PersonWeightMeasure/nc:MeasureText";
        xPaths.searchSystemId = FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
        xPaths.systemName = "Statewide Firearm Registry";
        xPaths.recordType = "Firearm Registration";
        return xPaths;
    }

    private SearchValueXPaths getCriminalHistoryXPaths() {
        SearchValueXPaths xPaths = new SearchValueXPaths();
        xPaths.ageXPath = null; // CH does not have age, only birthdate
        xPaths.birthdateXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonBirthDate/nc:Date";
        xPaths.ssnXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonSSNIdentification/nc:IdentificationID";
        xPaths.sidXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/jxdm40:PersonAugmentation/jxdm40:PersonStateFingerprintIdentification/nc:IdentificationID";
        xPaths.fbiXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/jxdm40:PersonAugmentation/jxdm40:PersonFBIIdentification/nc:IdentificationID";
        xPaths.dlXPath = null; // ch does not have DL
        xPaths.dlJurisdictionXPath = null; // ch does not have DL
        xPaths.lastNameXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonName/nc:PersonSurName";
        xPaths.middleNameXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonName/nc:PersonMiddleName";
        xPaths.firstNameXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonName/nc:PersonGivenName";
        xPaths.eyeColorXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonEyeColorText";
        xPaths.hairColorXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonHairColorText";
        xPaths.raceXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonRaceCode";
        xPaths.sexXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonSexCode";
        xPaths.heightXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonHeightMeasure/nc:MeasurePointValue";
        xPaths.weightXPath = "/ch-doc:CriminalHistory/ch:RapSheet/ch:RapSheetPerson/nc:PersonWeightMeasure/nc:MeasurePointValue";
        xPaths.searchSystemId = CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID;
        xPaths.systemName = "Criminal History";
        xPaths.recordType = "Criminal History";
        return xPaths;
    }

    private List<IdentifiableDocumentWrapper> personSearchDocumentsAsList(Document personSearchRequestMessage, DateTime baseDate, SearchValueXPaths xPaths, ClasspathXmlDataSource dataSource)
            throws Exception {

        PersonSearchParameters psp = new StaticMockQuery.PersonSearchParameters(personSearchRequestMessage);
        List<IdentifiableDocumentWrapper> matches = new ArrayList<IdentifiableDocumentWrapper>();

        for (IdentifiableDocumentWrapper dw : dataSource.getDocuments()) {

            Document d = dw.getDocument();

            // to compute age, we use birthdate if it's there, otherwise we use age
            Element birthdateElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.birthdateXPath);
            Integer age = null;
            DateTime birthdate = null;
            if (birthdateElement != null) {
                birthdate = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(birthdateElement.getTextContent());
                age = Years.yearsBetween(birthdate, baseDate).getYears();
            } else {
                Element ageElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.ageXPath);
                if (ageElement != null) {
                    age = Integer.parseInt(ageElement.getTextContent());
                }
            }

            // there has to be a better way to do this, but I couldn't think of one... hey...it works! -sc
            List<Boolean> orStack = new ArrayList<Boolean>();
            boolean checkSSN = xPaths.ssnXPath != null && checkFieldEqual(d, psp.SSN, xPaths.ssnXPath);
            if (psp.SSN != null) {
                orStack.add(new Boolean(checkSSN));
            }
            boolean checkSID = xPaths.sidXPath != null && checkFieldEqual(d, psp.SID, xPaths.sidXPath);
            if (psp.SID != null) {
                orStack.add(new Boolean(checkSID));
            }
            boolean checkFBI = xPaths.fbiXPath != null && checkFieldEqual(d, psp.FBI, xPaths.fbiXPath);
            if (psp.FBI != null) {
                orStack.add(new Boolean(checkFBI));
            }
            boolean checkDL = xPaths.dlXPath != null && checkFieldEqual(d, psp.DL, xPaths.dlXPath);
            if (psp.DL != null) {
                orStack.add(new Boolean(checkDL));
            }
            boolean include = orStack.isEmpty() ? true : orStack.get(0);
            if (orStack.size() > 1) {
                for (int i = 1; i < orStack.size(); i++) {
                    include |= orStack.get(i);
                }
            }

            // Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.lastNameXPath);
            // Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(d, xPaths.firstNameXPath);
            // LOG.info("Search of doc for lastName=" + lastNameElement.getTextContent() + ", firstName=" + firstNameElement.getTextContent() + ": include=" + include);

            boolean checkLastName = checkStringFieldMatches(d, psp.lastName, xPaths.lastNameXPath, psp.lastNameSearchStartsWith);
            boolean checkFirstName = checkStringFieldMatches(d, psp.firstName, xPaths.firstNameXPath, psp.firstNameSearchStartsWith);
            boolean checkEyeColor = checkFieldEqual(d, psp.eyeColor, xPaths.eyeColorXPath);
            boolean checkHairColor = checkFieldEqual(d, psp.hairColor, xPaths.hairColorXPath);
            boolean checkRace = checkFieldEqual(d, psp.race, xPaths.raceXPath);
            boolean checkSex = checkFieldEqual(d, psp.sex, xPaths.sexXPath);
            boolean checkDateOfBirth = checkRange(d, psp.dateOfBirth, psp.dateOfBirthMin, psp.dateOfBirthMax, birthdate);
            boolean checkAge = checkRange(d, psp.age, psp.ageMin, psp.ageMax, age);
            boolean checkHeight = checkIntegerRange(d, psp.height, psp.heightMin, psp.heightMax, xPaths.heightXPath);
            boolean checkWeight = checkIntegerRange(d, psp.weight, psp.weightMin, psp.weightMax, xPaths.weightXPath);

            include = include && checkLastName && checkFirstName && checkEyeColor && checkHairColor && checkRace && checkSex && checkDateOfBirth && checkAge && checkHeight && checkWeight;

            if (include) {
                matches.add(dw);
            }
        }
        return matches;
    }

    private boolean checkIntegerRange(Document personSearchRequestMessage, Integer valueParam, Integer minValueParam, Integer maxValueParam, String xPathToDocumentValue) throws Exception {
        if (valueParam == null && minValueParam == null && maxValueParam == null) {
            // skipping this query parameter
            return true;
        }
        Element documentValueElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, xPathToDocumentValue);
        if (documentValueElement == null) {
            return false;
        }
        int documentValue = (int) Double.parseDouble(documentValueElement.getTextContent());
        return checkRange(personSearchRequestMessage, valueParam, minValueParam, maxValueParam, documentValue);
    }

    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    private boolean checkRange(Document personSearchRequestMessage, Comparable valueParam, Comparable minValueParam, Comparable maxValueParam, Comparable documentValue) throws Exception {
        if (valueParam == null && minValueParam == null && maxValueParam == null) {
            // skipping this query parameter
            return true;
        }
        if (valueParam != null) {
            // single-value test
            return valueParam.equals(documentValue);
        } else {
            // range test
            return minValueParam.compareTo(documentValue) != 1 && documentValue.compareTo(maxValueParam) != 1;
        }

    }

    private boolean checkStringFieldMatches(Document d, String value, String xPath, boolean startsWithCheck) throws Exception {
        boolean ret = true;
        if (value != null) {
            if (!startsWithCheck) {
                ret = checkFieldEqual(d, value, xPath);
            } else {
                Element documentElement = (Element) XmlUtils.xPathNodeSearch(d, xPath);
                // LOG.info("xPath=" + xPath + ", documentElement=" + documentElement);
                if (documentElement != null) {
                    String documentValue = documentElement.getTextContent();
                    if (documentValue != null) {
                        // LOG.info("documentvalue=" + documentValue);
                        ret = documentValue.toUpperCase().startsWith(value.toUpperCase());
                    } else {
                        ret = false;
                    }
                } else {
                    ret = false;
                }
            }
        }
        // LOG.info("Comparing string field match: value=" + value + ", xPathToParent=" + xPathToParent + ", elementToCheck=" + elementToCheck + ", returning " + ret);
        return ret;
    }

    private boolean checkFieldEqual(Document d, Object value, String xPath) throws Exception {
        boolean ret = true;
        if (value != null) {
            Element documentElement = (Element) XmlUtils.xPathNodeSearch(d, xPath);
            if (documentElement != null) {
                String documentValue = documentElement.getTextContent();
                if (documentValue != null) {
                    String valueS = value.toString().toUpperCase();
                    ret = documentValue.toUpperCase().startsWith(valueS);
                } else {
                    ret = false;
                }
            } else {
                ret = false;
            }
        }
        // LOG.info("Comparing object field match: value=" + value + ", xPathToParent=" + xPathToParent + ", elementToCheck=" + elementToCheck + ", returning " + ret);
        return ret;
    }

    private List<IdentifiableDocumentWrapper> queryWarrantDocuments(String documentId) throws Exception {
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        IdentifiableDocumentWrapper document = warrantDataSource.getDocument(documentId);
        if (document != null) {
            ret.add(document);
        }
        return ret;
    }

    private List<IdentifiableDocumentWrapper> queryIncidentDocuments(String documentId) throws Exception {
        // LOG.info("query incident documents, documentId=" + documentId);
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        IdentifiableDocumentWrapper document = incidentDataSource.getDocument(documentId);
        if (document != null) {
            ret.add(document);
        }
        return ret;
    }

    private List<IdentifiableDocumentWrapper> queryPersonFirearmRegistrationDocuments(String identifier) throws Exception {
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        String documentIdentifier = null;
        String firearmId = null;
        if (identifier.contains(":")) {
            String[] ss = identifier.split(":");
            documentIdentifier = ss[0];
            firearmId = ss[1];
        } else {
            documentIdentifier = identifier;
        }
        IdentifiableDocumentWrapper document = firearmRegistrationDataSource.getDocument(documentIdentifier);
        if (document != null) {
            if (firearmId != null) {
                document = new IdentifiableDocumentWrapper(createFirearmRegistrationDocument(document.getDocument(), firearmId), identifier);
            }
            ret.add(document);
        }
        return ret;
    }

    private Document createFirearmRegistrationDocument(Document document, String firearmId) throws Exception {
        Document copy = createNewDocument();
        copy.appendChild(copy.importNode(document.getDocumentElement(), true));
        Node rootElement = XmlUtils.xPathNodeSearch(copy, "/*");
        LOG.debug("Keeper: " + firearmId);
        NodeList otherFirearmNodes = XmlUtils.xPathNodeListSearch(rootElement, "firearm-ext:Firearm[@s:id != '" + firearmId + "']");
        for (int i = 0; i < otherFirearmNodes.getLength(); i++) {
            Node goner = otherFirearmNodes.item(i);
            LOG.debug("Goner: " + XmlUtils.xPathStringSearch(goner, "@s:id"));
            rootElement.removeChild(goner);
        }
        NodeList otherItemRegNodes = XmlUtils.xPathNodeListSearch(rootElement,
                "firearm-ext:ItemRegistration[@s:id != /firearm-doc:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation[nc:ItemReference/@s:ref='" + firearmId
                        + "']/nc:ItemRegistrationReference/@s:ref]");
        for (int i = 0; i < otherItemRegNodes.getLength(); i++) {
            rootElement.removeChild(otherItemRegNodes.item(i));
        }
        NodeList otherRegAssociationNodes = XmlUtils.xPathNodeListSearch(rootElement, "nc:PropertyRegistrationAssociation[nc:ItemReference/@s:ref != '" + firearmId + "']");
        for (int i = 0; i < otherRegAssociationNodes.getLength(); i++) {
            rootElement.removeChild(otherRegAssociationNodes.item(i));
        }
        copy.renameNode(rootElement, rootElement.getNamespaceURI(), "FirearmRegistrationQueryResults");
        Node documentRootElement = XmlUtils.xPathNodeSearch(document, "/*");
        rootElement.setPrefix(documentRootElement.getPrefix());
        return copy;
    }

    private List<IdentifiableDocumentWrapper> queryCriminalHistoryDocuments(String documentId) throws Exception {
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        IdentifiableDocumentWrapper document = criminalHistoryDataSource.getDocument(documentId);
        if (document != null) {
            ret.add(document);
        }
        return ret;
    }

    static final class PersonSearchParameters {

        private Integer ageMin;
        private Integer ageMax;
        private Integer age;
        private DateTime dateOfBirth;
        private String eyeColor;
        private String hairColor;
        private Integer heightMin;
        private Integer heightMax;
        private Integer height;
        private String lastName;
        private String firstName;
        private String race;
        private String sex;
        private String SSN;
        private Integer weightMin;
        private Integer weightMax;
        private Integer weight;
        private DateTime dateOfBirthMin;
        private DateTime dateOfBirthMax;
        private String DL;
        private String FBI;
        private String SID;
        private boolean firstNameSearchStartsWith;
        private boolean lastNameSearchStartsWith;

        public PersonSearchParameters(Document personSearchRequestMessage) throws Exception {
            String ageS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureText");
            if (ageS != null) {
                age = Integer.parseInt(ageS);
            }
            String ageMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
            if (ageMinS != null) {
                ageMin = Integer.parseInt(ageMinS);
            }
            String ageMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonAgeMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
            if (ageMaxS != null) {
                ageMax = Integer.parseInt(ageMaxS);
            }
            String dateOfBirthS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonBirthDate/nc:Date");
            if (dateOfBirthS != null) {
                dateOfBirth = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthS);
            }
            eyeColor = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonEyeColorCode");
            hairColor = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHairColorCode");
            String heightMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
            if (heightMinS != null) {
                heightMin = Integer.parseInt(heightMinS);
            }
            String heightMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
            if (heightMaxS != null) {
                heightMax = Integer.parseInt(heightMaxS);
            }
            String heightS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonHeightMeasure/nc:MeasureText");
            if (heightS != null) {
                height = Integer.parseInt(heightS);
            }
            lastName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonName/nc:PersonSurName");
            Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonName/nc:PersonSurName");
            if (lastNameElement != null) {
                String metadataAttribute = lastNameElement.getAttributeNS(OjbcNamespaceContext.NS_STRUCTURES, "metadata");
                Element lastNameSearchMetadataElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:SearchMetadata[@s:id='" + metadataAttribute + "']");
                if (lastNameSearchMetadataElement != null) {
                    Element qualifierCodeElement = (Element) XmlUtils.xPathNodeSearch(lastNameSearchMetadataElement, "psr:SearchQualifierCode");
                    lastNameSearchStartsWith = "startsWith".equals(qualifierCodeElement.getTextContent());
                }
            }
            firstName = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonName/nc:PersonGivenName");
            Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:Person/nc:PersonName/nc:PersonGivenName");
            if (firstNameElement != null) {
                String metadataAttribute = firstNameElement.getAttributeNS(OjbcNamespaceContext.NS_STRUCTURES, "metadata");
                Element firstNameSearchMetadataElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), "psr:SearchMetadata[@s:id='" + metadataAttribute + "']");
                if (firstNameSearchMetadataElement != null) {
                    Element qualifierCodeElement = (Element) XmlUtils.xPathNodeSearch(firstNameSearchMetadataElement, "psr:SearchQualifierCode");
                    firstNameSearchStartsWith = "startsWith".equals(qualifierCodeElement.getTextContent());
                }
            }
            race = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonRaceCode");
            sex = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonSexCode");
            SSN = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonSSNIdentification/nc:IdentificationID");
            String weightMinS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
            if (weightMinS != null) {
                weightMin = Integer.parseInt(weightMinS);
            }
            String weightMaxS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
            if (weightMaxS != null) {
                weightMax = Integer.parseInt(weightMaxS);
            }
            String weightS = getElementContent(personSearchRequestMessage, "psr:Person/nc:PersonWeightMeasure/nc:MeasureText");
            if (weightS != null) {
                weight = Integer.parseInt(weightS);
            }
            String dateOfBirthMinS = getElementContent(personSearchRequestMessage, "psr:Person/psr:PersonBirthDateRange/nc:StartDate/nc:Date");
            if (dateOfBirthMinS != null) {
                dateOfBirthMin = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthMinS);
            }
            String dateOfBirthMaxS = getElementContent(personSearchRequestMessage, "psr:Person/psr:PersonBirthDateRange/nc:EndDate/nc:Date");
            if (dateOfBirthMaxS != null) {
                dateOfBirthMax = DATE_FORMATTER_YYYY_MM_DD.parseDateTime(dateOfBirthMaxS);
            }
            DL = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID");
            FBI = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
            SID = getElementContent(personSearchRequestMessage, "psr:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");

            if (lastName == null && SSN == null && FBI == null && SID == null && DL == null) {
                throw new IllegalArgumentException("Searches must have a last name or an identifier");
            }

            if (ageMin != null && ageMax == null) {
                ageMax = Integer.MAX_VALUE;
            }
            if (ageMax != null && ageMin == null) {
                ageMin = 0;
            }
            if (weightMin != null && weightMax == null) {
                weightMax = Integer.MAX_VALUE;
            }
            if (weightMax != null && weightMin == null) {
                weightMin = 0;
            }
            if (heightMin != null && heightMax == null) {
                heightMax = Integer.MAX_VALUE;
            }
            if (heightMax != null && heightMin == null) {
                heightMin = 0;
            }
            if (dateOfBirthMin == null && dateOfBirthMax != null) {
                dateOfBirthMin = DATE_FORMATTER_YYYY_MM_DD.parseDateTime("1800-01-01");
            }
            if (dateOfBirthMin != null && dateOfBirthMax == null) {
                dateOfBirthMax = DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2200-12-31");
            }

        }

        public DateTime getDateOfBirth() {
            return dateOfBirth;
        }

        public Integer getAge() {
            return age;
        }

        public Integer getWeight() {
            return weight;
        }

        public Integer getHeight() {
            return height;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSSN() {
            return SSN;
        }

        public String getSID() {
            return SID;
        }

        public String getDL() {
            return DL;
        }

        public String getFBI() {
            return FBI;
        }

        public String getEyeColor() {
            return eyeColor;
        }

        public String getHairColor() {
            return hairColor;
        }

        public String getRace() {
            return race;
        }

        public String getSex() {
            return sex;
        }

        public DateTime getDateOfBirthMin() {
            return dateOfBirthMin;
        }

        public DateTime getDateOfBirthMax() {
            return dateOfBirthMax;
        }

        public Integer getAgeMin() {
            return ageMin;
        }

        public Integer getAgeMax() {
            return ageMax;
        }

        public Integer getWeightMin() {
            return weightMin;
        }

        public Integer getWeightMax() {
            return weightMax;
        }

        public Integer getHeightMin() {
            return heightMin;
        }

        public Integer getHeightMax() {
            return heightMax;
        }

        public boolean isFirstNameSearchStartsWith() {
            return firstNameSearchStartsWith;
        }

        public boolean isLastNameSearchStartsWith() {
            return lastNameSearchStartsWith;
        }

        private String getElementContent(Document personSearchRequestMessage, String xpath) throws Exception {
            Element e = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage.getDocumentElement(), xpath);
            return e == null ? null : e.getTextContent().trim();
        }

    }

    private static class SearchValueXPaths {
        private String birthdateXPath;
        private String ssnXPath;
        private String sidXPath;
        private String fbiXPath;
        private String dlXPath;
        private String dlJurisdictionXPath;
        private String lastNameXPath;
        private String middleNameXPath;
        private String firstNameXPath;
        private String eyeColorXPath;
        private String hairColorXPath;
        private String raceXPath;
        private String sexXPath;
        private String ageXPath;
        private String heightXPath;
        private String weightXPath;
        private String searchSystemId;
        private String systemName;
        private String recordType;

        public String getSystemIdentifier(IdentifiableDocumentWrapper documentWrapper) {
            // TODO: refactor this to be an abstract class...this is only default for person-based searches because it was the first thing we did...
            return documentWrapper.getId();
        }
    }

}
