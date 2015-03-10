package org.search.ojb.samplegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.search.ojb.samplegen.JuvenileHistorySampleGenerator.IdentifiableHistoryComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JuvenileHistorySampleGenerator extends AbstractSampleGenerator {

	private static final String ID_SOURCE_TEXT = "CMS";

	private static final Log LOG = LogFactory.getLog(JuvenileHistorySampleGenerator.class);
	
    private static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final String[] OFFENSE_CODES = new String[] { "13.1017", "324.74203", "324.801261", "324.8015", "324.801771C-A", "29.29", "324.811296", "324.81133U", "324.811462", "324.82126C1", "324.821521", "328.2281E", "333.28359",
			"333.7340A", "333.74012F", "55.3014", "333.74071A", "333.74101-E", "333.7417", "333.12531A", "125.14471B2", "333.17766B", "333.201992", "338.835", "338.10693", "125.2321", "380.18093B", "399.1585", "400.6073", "408.66", "168.4992",
			"421.54D-A", "431.3304", "432.1102", "432.2183", "168.759", "436.19042B", "436.20253", "445.2A", "445.72B3C", "168.931A", "445.574A3C", "445.20812", "451.2501", "462.391", "480.17C3-A", "487.21421", "500.2062", "500.7034", "565.371",
			"600.87279", "722.115F8A", "722.675", "750.164-A", "750.49-B", "750.50C2", "750.731", "168.933", "750.81C3", "750.9", "750.94", "750.110A3", "169.23312", "750.131A2", "750.14", "750.145D2F", "750.153", "169.243", "750.160C2B-",
			"750.1671K", "750.1744C", "750.174A7B", "169.255", "750.1952", "750.200J1A-A", "750.2072D", "750.211A2A", "750.217B", "750.2187A", "750.2232-A", "750.227B-B", "750.232A4", "205.106-A", "750.26", "750.282A1A", "750.300A1C", "750.316-A",
			"750.329", "750.349", "750.356A2A", "750.360A2A", "750.3632B", "750.3682", "750.377C", "750.3821D2", "750.393", "750.397", "750.411A4A", "750.411R1", "750.416", "750.428", "750.439", "750.462B2", "750.462H1B3", "750.469", "750.479A3",
			"750.49", "750.493E", "750.511", "750.520C1C", "750.520D1F", "750.529", "750.536A1", "750.5405B", "750.543K1B", "752.62", "752.3653", "752.7971C2", "752.862-B", "764.25B", "780.79", "801.2622", "257.2551", "257.312A", "257.3291",
			"257.613D ", "28.2951C", "257.6251", "257.6264", "257.722", "257.9047-A", "257.13542", "259.181", "280.602", "286.214", "287.118", "287.335A", "287.9675", "290.631C", "290.673", "324.21571C1", "324.411", "4.83", "324.111512-A",
			"324.11714", "324.172041", "324.315255-A", "28.729", "324.413093B", "324.42505", "324.435241", "324.435581E", "28.7352A", "324.459082", "324.47323", "324.48707", "324.4873", "29.5G", "324.61511" };

	public JuvenileHistorySampleGenerator() throws ParserConfigurationException, IOException {
		super();
	}

	/**
	 * Generate a specified number of sample histories.
	 * 
	 * @param recordCount
	 *            the number of histories to create
	 * @param baseDate
	 *            the base date to use for computing age, etc.
	 * @param stateParam
	 *            the state to restrict instances, or null for any state
	 * @return the list of generated instances
	 * @throws Exception
	 */
	public List<Document> generateSample(int recordCount, DateTime baseDate, String stateParam) throws Exception {

		List<Document> ret = new ArrayList<Document>(recordCount);

		List<PersonElementWrapper> kids = loadIdentities(recordCount, 2000, baseDate, stateParam);

		while (ret.size() < recordCount) {
			ret.addAll(buildJuvenileHistoryInstanceDocuments(kids, baseDate, stateParam));
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

	private List<Document> buildJuvenileHistoryInstanceDocuments(List<PersonElementWrapper> kids, DateTime baseDate, String stateParam) throws Exception {
		List<Document> ret = new ArrayList<Document>();
		for (PersonElementWrapper kid : kids) {
			ret.add(createJuvenileHistoryInstanceDocument(kid, baseDate, stateParam));
		}
		return ret;
	}

	Document createJuvenileHistoryInstanceDocument(PersonElementWrapper kid, DateTime baseDate, String stateParam) throws Exception {
		JuvenileHistory history = createJuvenileHistory(kid, baseDate, stateParam);
		Document ret = writeHistoryToDocument(history, baseDate);
		return ret;
	}

	private Document writeHistoryToDocument(JuvenileHistory history, DateTime baseDate) throws Exception {
		// call a method to build a sub-element structure for each component of the history.  write unit tests that validate each of those sub-elements.
		// then assemble them all into a container document, with the search params in a made-up structure at the top.  we can't validate that container
		// document because we won't have a schema for it.
		Document ret = documentBuilder.newDocument();
		
		Element root = ret.createElementNS(OjbcNamespaceContext.NS_JUVENILE_HISTORY_CONTAINER, "JuvenileHistoryContainer");
		root.setPrefix(OjbcNamespaceContext.NS_PREFIX_JUVENILE_HISTORY_CONTAINER);
		ret.appendChild(root);
		
		appendAvailabilityMetadata(ret, baseDate, history);
		appendPeople(ret, baseDate, history);
		appendLocations(ret, baseDate, history);
		appendParentChildAssociations(ret, baseDate, history);
		
		appendReferrals(ret, baseDate, history);
		
		new OjbcNamespaceContext().populateRootNamespaceDeclarations(root);
		
		return ret;
		
	}
	
	private void appendReferrals(Document d, DateTime baseDate, JuvenileHistory history) throws Exception {
		
		Element root = d.getDocumentElement();
		
		int i=0;
		
		for(Referral referral : history.referrals) {
			
			i++;
			
			Element referralElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Referral");
			XmlUtils.addAttribute(referralElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", referral.id);
			XmlUtils.addAttribute(referralElement, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element e = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ActivityDate");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(referral.date));
			
			Element issuerElement = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ReferralIssuer");
			Element orgElement = XmlUtils.appendElement(issuerElement, OjbcNamespaceContext.NS_NC_30, "EntityOrganization");
			XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_CODES, "ReferralIssuerCategoryCode").setTextContent(referral.issuerCategory);
			Element orgLocation = XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationLocation");
			e = XmlUtils.appendElement(orgLocation, OjbcNamespaceContext.NS_NC_30, "Address");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStreet").setTextContent(referral.issuerStreet);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(referral.issuerCity);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "LocationStateNCICRESCode").setTextContent(referral.issuerState);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(referral.issuerZip);
			XmlUtils.appendElement(orgElement, OjbcNamespaceContext.NS_NC_30, "OrganizationName").setTextContent(referral.issuerName);
			
			e = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_NC_30, "ReferralPerson");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_XSI, "nil", "true");
			
			Element aug = XmlUtils.appendElement(referralElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_EXT, "ReferralAugmentation");
			XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_CODES, "ReferralCategoryCode").setTextContent(referral.category);
			Element idElement = XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
			XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(referral.id);
			XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(referral.getSource());
			if (referral.relatedComponents.size() > 0 || referral.relatedUnsupportedClass != null) {
				Element relatedRecordsElement = XmlUtils.appendElement(aug, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecords");
				for (IdentifiableHistoryComponent related : referral.relatedComponents) {
					Element relatedRecordElement = XmlUtils.appendElement(relatedRecordsElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecord");
					XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryCategoryCode").setTextContent(related.getCategory());
					idElement = XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
					XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(related.id);
					XmlUtils.appendElement(idElement, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(related.getSource());
				}
				if (referral.relatedUnsupportedClass != null) {
					Element relatedRecordElement = XmlUtils.appendElement(relatedRecordsElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecord");
					IdentifiableHistoryComponent fakeRelated = referral.relatedUnsupportedClass.newInstance();
					XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryCategoryCode").setTextContent(fakeRelated.getCategory());
					XmlUtils.appendElement(relatedRecordElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "RelatedJuvenileHistoryRecordNotSupportedIndicator").setTextContent("true");
				}
			}
			
		}
		
	}

	private void appendParentChildAssociations(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();

		if (history.mother != null) {
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "ParentChildAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Parent");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "mother");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

		if (history.father != null) {
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CYFS, "ParentChildAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_CYFS, "Parent");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "father");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}

	}

	private void appendLocations(Document d, DateTime baseDate, JuvenileHistory history) {
		
		Element root = d.getDocumentElement();
		
		appendLocation(root, history.kidResidence, "Location-K");
		String motherResidenceRef = history.mother == null ? null : "Location-K";
		String fatherResidenceRef = history.father == null ? null : "Location-K";
		if (history.kidResidence != history.motherResidence && history.mother != null) {
			motherResidenceRef = appendLocation(root, history.motherResidence, "Location-M");
		}
		if (history.kidResidence != history.fatherResidence && history.father != null) {
			fatherResidenceRef = appendLocation(root, history.fatherResidence, "Location-F");
		}
		
		Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
		XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
		Element ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "child");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Location-K");
		XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");

		if (motherResidenceRef != null) {
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "mother");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", motherResidenceRef);
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}
		
		if (fatherResidenceRef != null) {
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "PersonResidenceAssociation");
			XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "father");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
			ee = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Location");
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", fatherResidenceRef);
			XmlUtils.addAttribute(ee, OjbcNamespaceContext.NS_XSI, "nil", "true");
		}
		
	}

	protected String appendLocation(Element root, Residence residence, String locationName) {
		Element location = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Location");
		XmlUtils.addAttribute(location, OjbcNamespaceContext.NS_STRUCTURES_30, "id", locationName);
		XmlUtils.addAttribute(location, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
		Element address = XmlUtils.appendElement(location, OjbcNamespaceContext.NS_NC_30, "Address");
		Element e = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "StreetFullText").setTextContent(residence.street);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(residence.city);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_JXDM_50, "LocationStateNCICRESCode").setTextContent(residence.state);
		XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(residence.zip);
		return locationName;
	}
	
	private void appendPeople(Document d, DateTime baseDate, JuvenileHistory history) throws IOException {
		
		Element root = d.getDocumentElement();

		Element p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
		XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "child");
		XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");

		Element e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.kid.firstName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.kid.middleName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.kid.lastName);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");
		
		if (coinFlip(.2)) {
			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			if (coinFlip(.5)) {
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(getRandomIdentity(history.kid.state).firstName);
			} else {
				XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(getRandomIdentity(history.kid.state).middleName);
			}
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.kid.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("alias");
		}
		
		Element recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.personId);
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);
		
		if (history.mother != null) {
			
			p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "mother");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			
			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.mother.firstName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.mother.middleName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.mother.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");
			
			recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.mother.personId);
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);
		
		}
		
		if (history.father != null) {
			
			p = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "Person");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "father");
			XmlUtils.addAttribute(p, OjbcNamespaceContext.NS_STRUCTURES_30, "metadata", "metadata");
			
			e = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_NC_30, "PersonName");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonGivenName").setTextContent(history.father.firstName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName").setTextContent(history.father.middleName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSurName").setTextContent(history.father.lastName);
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "PersonNameCategoryCode").setTextContent("provided");
			
			recordId = XmlUtils.appendElement(p, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.father.personId);
			XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);
		
		}
		
		e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CONTAINER, "AdditionalChildInformation");
		Element birthdate = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
		XmlUtils.appendElement(birthdate, OjbcNamespaceContext.NS_NC_30, "Date").setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(history.kid.birthdate));
		Element ssn = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "PersonSSNIdentification");
		XmlUtils.appendElement(ssn, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.nationalID);
		
	}

	private void appendAvailabilityMetadata(Document d, DateTime baseDate, JuvenileHistory history) {

		Element root = d.getDocumentElement();
		
		Element md = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationAvailabilityMetadata");
		XmlUtils.addAttribute(md, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "metadata");
		
		Element e = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_NC_30, "LastUpdatedDate");
		e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date");
		e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(baseDate));
		
		Element owner = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationOwnerOrganization");
		e = XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationBranchName");
		e.setTextContent(history.court.branchName);
		
		e = XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationLocation");
		e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "Address");
		Element street = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
		XmlUtils.appendElement(street, OjbcNamespaceContext.NS_NC_30, "StreetFullText").setTextContent(history.court.addressStreetFullText);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationCityName").setTextContent(history.court.city);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JXDM_50, "LocationStateNCICRESCode").setTextContent(history.court.state);
		XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode").setTextContent(history.court.zip);
		
		XmlUtils.appendElement(owner, OjbcNamespaceContext.NS_NC_30, "OrganizationName").setTextContent(history.court.name);
		
		Element recordId = XmlUtils.appendElement(md, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(history.kid.personId);
		XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText").setTextContent(ID_SOURCE_TEXT);
		
	}

	JuvenileHistory createJuvenileHistory(PersonElementWrapper kid, DateTime baseDate, String stateParam) throws IOException {
		int birthYearSubtraction = randomGenerator.nextInt(12, 17);
		kid.birthdate = kid.birthdate.withYear(baseDate.getYear() - birthYearSubtraction);
		String state = stateParam == null ? kid.state : stateParam;
		JuvenileHistory ret = new JuvenileHistory();
		ret.kid = kid;
		ret.kid.personId = "P" + ret.kid.id;
		String county = getRandomCounty(state);
		List<Court> courts = new ArrayList<Court>();
		int baseZip = 43100;
		for (int i = 0; i < 9; i++) {
			courts.add(new Court(county + " County Circuit Court", "Juvenile Division", "123 Any Street", getRandomCity(state), state, "" + (baseZip + i)));
		}
		ret.court = (Court) generateRandomValueFromList(courts.toArray());
		ret.court.city = getRandomCity(state);
		if (coinFlip(.7)) {
			PersonElementWrapper mother = getRandomIdentity(state);
			while (!("female".equals(mother.sex))) {
				mother = getRandomIdentity(state);
			}
			mother.personId = "P" + mother.id;
			ret.mother = mother;
			Residence r = new Residence();
			r.street = buildRandomStreet();
			r.city = getRandomCity(state);
			r.state = state;
			r.zip = "12345";
			ret.motherResidence = r;
			if (coinFlip(.5)) {
				kid.lastName = mother.lastName;
			}
		}
		if (ret.motherResidence == null || coinFlip(.7)) {
			PersonElementWrapper father = getRandomIdentity(state);
			while (!("male".equals(father.sex))) {
				father = getRandomIdentity(state);
			}
			father.personId = "P" + father.id;
			ret.father = father;
			Residence r = ret.motherResidence;
			if (r == null || coinFlip(.5)) {
				r = new Residence();
				r.street = buildRandomStreet();
				r.city = getRandomCity(state);
				r.state = state;
				r.zip = "12345";
			}
			ret.fatherResidence = r;
			if (coinFlip(.7)) {
				kid.lastName = father.lastName;
			}
		}
		ret.kidResidence = ret.motherResidence;
		if (ret.kidResidence == null || (ret.fatherResidence != null && coinFlip(.6))) {
			ret.kidResidence = ret.fatherResidence;
		}
		if (ret.mother != null && ret.father != null && ret.motherResidence == ret.fatherResidence) {
			if (coinFlip(.75)) {
				ret.mother.lastName = ret.father.lastName;
				ret.kid.lastName = ret.father.lastName;
			}
		}
		int referralCount = generatePoissonInt(.8, true);
		for (int i = 0; i < referralCount; i++) {
			Referral referral = new Referral();
			referral.id = "Referral-" + i;
			referral.date = generateNormalRandomDateBefore(baseDate, 150);
			referral.issuerCategory = generateRandomCodeFromList("Police", "Prosecutor", "School", "Court/Probation", "Social Services", "Other");
			referral.category = generateRandomCodeFromList("Complaint", "Citation or Appearance Ticket", "Petition", "Community Referral");
			if ("Police".equals(referral.issuerCategory)) {
				referral.issuerName = ret.kidResidence.city + " Police Department";
			} else if ("Prosecutor".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Prosecutor";
			} else if ("School".equals(referral.issuerCategory)) {
				referral.issuerName = generateRandomCodeFromList(ret.kidResidence.city, "Washington", "Lincoln", "Jefferson") + " High School";
			} else if ("Court/Probation".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Courts";
			} else if ("Social Services".equals(referral.issuerCategory)) {
				referral.issuerName = county + " County Social Services";
			} else {
				referral.issuerName = "Other Referral Source";
			}
			referral.issuerStreet = buildRandomStreet();
			referral.issuerCity = getRandomCity(state);
			referral.issuerState = state;
			referral.issuerZip = "12345";
			ret.referrals.add(referral);
		}
		ret.casePlanIndicator = coinFlip(.6);
		ret.assessmentIndicator = coinFlip(.8);
		int hearingCount = generatePoissonInt(1.2, true);
		for (int i = 0; i < hearingCount; i++) {
			Hearing h = new Hearing();
			h.id = "Hearing-" + i;
			h.date = generateNormalRandomDateBefore(baseDate, 200);
			h.contemptOfCourtIndicator = coinFlip(.2);
			h.probationViolationIndicator = coinFlip(.4);
			h.hearingCategory = generateRandomCodeFromList("Preliminary hearing", "Preliminary inquiry", "Pretrial Conference", "Adjudication", "Bench Trial", "Jury Trial", "Competency Hearing", "Disposition Hearing", "Dispositional Review",
					"Detention Hearing", "Probation Violation Hearing", "Phase I Waiver Hearing", "Phase II Waiver Hearing", "Designation Arraignment", "Designation Preliminary Examination", "Designtion Hearing", "Desgination adjudication or trial",
					"Designation Sentencing", "Contempt of Court (on a Motion to Show Cause)", "Dismissal");
			h.disposition = generateRandomCodeFromList("Petition authorized", "Petition not authorized", "Refer to alternative services", "Placed on the Consent Calendar", "Placed on diversion", "Attorney appointed", "Placement determination",
					"Title IV-E language", "Bond may be set if out of home placement is ordered", "Next hearing scheduled", "Determine notice issues", "American Indian tribe or band notified", "Adjournment", "Adjudication hearing set",
					"Bench trial set", "Jury trial set", "Case dismissed", "Other stipulations between the parties noted", "Order for competency evaluation requested", "Plea is accepted", "Plea is rejected", "Plea is taken under advisement",
					"Order fingerprinting", "Order an abstract to Sec. of State", "Order licensing sanctions", "Placement determination", "Title IV-E language (if necessary)", "Bond may be set if out of home placement is ordered",
					"Guilty or not guilty determination by judge or referee", "The juvenile is competent", "The juvenile is not competent to proceed and will remain incompetent",
					"The juvenile is not competent to proceed but may be restored to competency", "The proceedings on the charges shall proceed immediately", "The proceedings on the charges are suspended pending restoration efforts",
					"Warn and dismiss", "Probation", "Refer to Michigan DHS for placement and care", "Order HIV, Sex offender registration and/DNA testing", "Order costs, fees, etc.", "Continue Probation", "Modify previous dispositional orders",
					"Release of jurisdiction", "Plea accepted to probation violation", "Modify previous dispositional orders", "Set a probation violation hearing", "Probation violation dismissed", "Finding the juvenile violated probation",
					"Finding the juvenile did not violate probation", "Determine probable cause", "Phase I waived by parties", "Deny the motion to waive", "Schedule Phase II hearing",
					"The motion to waive jurisdiction is denied and the case shall proceed under the juvenile code", "The motion to waive jurisdiction is granted and the case is transferred to the court having general criminal jurisdiction",
					"Preliminary examination waived", "Probable cause does exist", "Probable cause does not exist", "The request for designation is granted", "The request for designation is not granted", "Schedule sentencing hearing",
					"Sentence as an adult", "Blended sentence", "Juvenile disposition", "The person is guilty of contempt of court", "The person is not guilty of contempt of court", "The motion is denied", "Sentencing factors listed");
			ret.hearings.add(h);
		}
		int intakeCount = generatePoissonInt(1.2, true);
		for (int i = 0; i < intakeCount; i++) {
			Intake intake = new Intake();
			intake.id = "Intake-" + i;
			intake.date = generateNormalRandomDateBefore(baseDate, 150);
			intake.assessmentCategory = generateRandomCodeFromList("Assessment", "Screening", "Interview", "Other");
			intake.recommendedCourseOfAction = generateRandomCodeFromList("Court diversion", "Consent calendar", "Formal calendar", "Transfer");
			ret.intakes.add(intake);
		}
		int offenseCount = generatePoissonInt(1.6, true);
		for (int i = 0; i < offenseCount; i++) {
			OffenseCharge offenseCharge = new OffenseCharge();
			offenseCharge.id = "Offense-" + i;
			offenseCharge.pacCode = generateRandomCodeFromList(OFFENSE_CODES);
			DateTime offenseDate = generateNormalRandomDateBefore(baseDate, 400);
			offenseCharge.offenseDate = offenseDate;
			offenseCharge.filingDate = offenseDate.plusDays(randomGenerator.nextInt(2, 14));
			offenseCharge.dispositionDate = offenseCharge.filingDate.plusDays(randomGenerator.nextInt(30, 180));
			offenseCharge.verdictDate = offenseCharge.dispositionDate.plusDays(randomGenerator.nextInt(0, 30));
			offenseCharge.sanctionDate = offenseCharge.verdictDate.plusDays(randomGenerator.nextInt(0, 30));
			offenseCharge.verdict = generateRandomCodeFromList("Not responsible", "Responsible", "Dismissed");
			if ("Responsible".equals(offenseCharge.verdict)) {
				int sanctionCount = generatePoissonInt(.5, true);
				for (int j = 0; j < sanctionCount; j++) {
					offenseCharge.sanctions.add(generateRandomCodeFromList("Warning", "Probation", "Community service", "Payment of fines, fees, restitution", "Electronic tether", "Drug or other testing or screening",
							"Participation in Drug or other Specialty Court", "Detention", "Boot Camp", "Courtesy supervision", "Residential Placements", "Court/county operated treatment facility", "In-state private operated treatment facility",
							"Out-of-state operated treatment facility", "State or public treatment/residential facility", "A referral or commitment to human services", "County jail", "Sentence as an Adult"));
				}
			}
			ret.offenseCharges.add(offenseCharge);
		}
		int placementCount = generatePoissonInt(.8, true);
		for (int i = 0; i < placementCount; i++) {
			Placement placement = new Placement();
			placement.id = "Placement-" + i;
			placement.startDate = generateNormalRandomDateBefore(baseDate, 365);
			placement.endDate = placement.startDate.plusDays(randomGenerator.nextInt(10, 180));
			placement.placementCategory = generateRandomCodeFromList("Juvenile Detention Facility", "Foster Home", "Group Home", "Residential", "Mother", "Father", "Stepfather", "Stepmother", "Relative/Fictive Kin", "Jail");
			if ("Juvenile Detention Facility".equals(placement.placementCategory) || "Jail".equals(placement.placementCategory)) {
				placement.securityCode = "Secure";
			} else {
				placement.securityCode = "Non-secure";
			}
			placement.facilityName = "Facility Name: " + placement.placementCategory;
			placement.facilityStreet = buildRandomStreet();
			placement.facilityCity = getRandomCity(state);
			placement.facilityState = state;
			placement.facilityZip = "12345";
			ret.placements.add(placement);
		}
		for (IdentifiableHistoryComponent component : ret.getIdentifiableComponents()) {
			Class<IdentifiableHistoryComponent> unsupportedRelatedComponentClass = null;
			if (coinFlip(.5)) {
				while (unsupportedRelatedComponentClass == null || unsupportedRelatedComponentClass.isAssignableFrom(component.getClass())) {
					unsupportedRelatedComponentClass = (Class<IdentifiableHistoryComponent>) generateRandomValueFromList(Referral.class, Placement.class, Intake.class, OffenseCharge.class, Hearing.class);
				}
			}
			component.relatedUnsupportedClass = unsupportedRelatedComponentClass;
			List<IdentifiableHistoryComponent> others = new ArrayList<IdentifiableHistoryComponent>();
			for (IdentifiableHistoryComponent innerComponent : ret.getIdentifiableComponents()) {
				if (!(innerComponent.getClass() == component.getClass() || innerComponent.getClass() == unsupportedRelatedComponentClass)) {
					others.add(innerComponent);
				}
			}
			if (!others.isEmpty()) {
				Collections.shuffle(others);
				int relatedCount = randomGenerator.nextInt(0, others.size() - 1);
				if (relatedCount > 0) {
					component.relatedComponents = others.subList(0, relatedCount);
				}
			}
		}
		return ret;
	}

	protected String buildRandomStreet() {
		return randomGenerator.nextInt(100, 5000) + " " + generateRandomCodeFromList("First", "Elm", "Center", "32nd", "Martin", "Main") + " " + generateRandomCodeFromList("St.", "Ave.", "Rd.");
	}

	static final class JuvenileHistory {
		public PersonElementWrapper kid;
		public PersonElementWrapper mother;
		public PersonElementWrapper father;
		public Residence motherResidence;
		public Residence fatherResidence;
		public Residence kidResidence;
		public Court court;
		public List<Referral> referrals = new ArrayList<Referral>();
		boolean casePlanIndicator;
		boolean assessmentIndicator;
		public List<Hearing> hearings = new ArrayList<Hearing>();
		public List<Intake> intakes = new ArrayList<Intake>();
		public List<OffenseCharge> offenseCharges = new ArrayList<OffenseCharge>();
		public List<Placement> placements = new ArrayList<Placement>();
		public List<IdentifiableHistoryComponent> getIdentifiableComponents() {
			List<IdentifiableHistoryComponent> ret = new ArrayList<IdentifiableHistoryComponent>();
			ret.addAll(referrals);
			ret.addAll(hearings);
			ret.addAll(intakes);
			ret.addAll(offenseCharges);
			ret.addAll(placements);
			return Collections.unmodifiableList(ret);
		}
	}
	
	static abstract class IdentifiableHistoryComponent {
		public String id;
		public List<IdentifiableHistoryComponent> relatedComponents = new ArrayList<IdentifiableHistoryComponent>();
		public abstract String getSource();
		public abstract String getCategory();
		public Class<IdentifiableHistoryComponent> relatedUnsupportedClass;
	}

	static final class Placement extends IdentifiableHistoryComponent {
		public DateTime startDate;
		public DateTime endDate;
		public String placementCategory;
		public String securityCode;
		public String facilityName;
		public String facilityStreet;
		public String facilityCity;
		public String facilityState;
		public String facilityZip;
		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}PlacementHistory";
		}
		@Override
		public String getCategory() {
			return "JuvenilePlacementHistory";
		}
	}

	static final class OffenseCharge extends IdentifiableHistoryComponent {
		public String pacCode;
		public DateTime offenseDate;
		public DateTime filingDate;
		public DateTime sanctionDate;
		public DateTime verdictDate;
		public DateTime dispositionDate;
		public String verdict;
		public List<String> sanctions = new ArrayList<String>();
		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}OffenseHistory";
		}
		@Override
		public String getCategory() {
			return "JuvenileOffenseHistory";
		}
	}

	static final class Intake extends IdentifiableHistoryComponent {
		public DateTime date;
		public String assessmentCategory;
		public String recommendedCourseOfAction;
		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}IntakeHistory";
		}
		@Override
		public String getCategory() {
			return "JuvenileIntakeHistory";
		}
	}

	static final class Hearing extends IdentifiableHistoryComponent {
		public DateTime date;
		public boolean contemptOfCourtIndicator;
		public boolean probationViolationIndicator;
		public String hearingCategory;
		public String disposition;
		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}HearingHistory";
		}
		@Override
		public String getCategory() {
			return "JuvenileHearingHistory";
		}
	}

	static final class Referral extends IdentifiableHistoryComponent {
		public DateTime date;
		public String category;
		public String issuerCategory;
		public String issuerName;
		public String issuerStreet;
		public String issuerCity;
		public String issuerState;
		public String issuerZip;
		@Override
		public String getSource() {
			return "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}ReferralHistory";
		}
		@Override
		public String getCategory() {
			return "JuvenileReferralHistory";
		}
	}

	static final class Residence {
		public String street;
		public String city;
		public String state;
		public String zip;
	}

	static final class Court {

		public Court(String name, String branchName, String addressStreetFullText, String city, String state, String zip) {
			this.name = name;
			this.branchName = branchName;
			this.addressStreetFullText = addressStreetFullText;
			this.city = city;
			this.state = state;
			this.zip = zip;
		}

		public String name;
		public String branchName;
		public String addressStreetFullText;
		public String city;
		public String state;
		public String zip;
	}

}
