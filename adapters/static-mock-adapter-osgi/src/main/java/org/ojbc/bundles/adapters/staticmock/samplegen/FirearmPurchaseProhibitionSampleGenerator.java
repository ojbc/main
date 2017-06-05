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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_51;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_MAINE_FPP_CODES;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FirearmPurchaseProhibitionSampleGenerator extends AbstractPersonSampleGenerator {

    private static final String ORDER_01 = "Order_01";
	private static final String CASE_01 = "Case_01";
	private static final String FPP_01 = "FPP_01";
	private static final String PERSON_01 = "Person_01";
	private static final DateTimeFormatter XML_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final Log LOG = LogFactory.getLog(FirearmPurchaseProhibitionSampleGenerator.class);
    private static FirearmPurchaseProhibitionSampleGenerator INSTANCE;

    public FirearmPurchaseProhibitionSampleGenerator() throws ParserConfigurationException, IOException {
        super();
    }

    /**
     * Get the singleton instance of this class
     * 
     * @return the instance
     */
    public static final FirearmPurchaseProhibitionSampleGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new FirearmPurchaseProhibitionSampleGenerator();
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

            Document document = documentBuilder.newDocument();
            personDocuments.add(document);

            Element rootElement = createRootElment(document);
            
			Element firearmPurchaseProhibitionReport = XmlUtils.appendElement(
					rootElement,
					NS_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS_EXT,
					"FirearmPurchaseProhibitionReport");
			
			appendFirearmPurchaohibitionElement(firearmPurchaseProhibitionReport);
			appendCaseElement(firearmPurchaseProhibitionReport);
			appendCourtOrderElement(firearmPurchaseProhibitionReport);
            addPersonElement(firearmPurchaseProhibitionReport, person, baseDate);
            appendActivityCourtOrderAssociation(firearmPurchaseProhibitionReport); 
            
            OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(document.getDocumentElement());

        }

        return personDocuments;

    }

	private void appendActivityCourtOrderAssociation(Element firearmPurchaseProhibitionReport) {
		Element activityCourtOrderAssociation = XmlUtils.appendElement(firearmPurchaseProhibitionReport, NS_JXDM_51, "ActivityCourtOrderAssociation");
		Element fppActivity = XmlUtils.appendElement(activityCourtOrderAssociation, NS_NC_30, "Activity"); 
		XmlUtils.addAttribute(fppActivity, NS_STRUCTURES_30, "ref", FPP_01);
		Element caseActivity = XmlUtils.appendElement(activityCourtOrderAssociation, NS_NC_30, "Activity"); 
		XmlUtils.addAttribute(caseActivity, NS_STRUCTURES_30, "ref", CASE_01); 
		Element courtOrder = XmlUtils.appendElement(activityCourtOrderAssociation, NS_JXDM_51, "CourtOrder");
		XmlUtils.addAttribute(courtOrder, NS_STRUCTURES_30, "ref", ORDER_01); 
		Element subject = XmlUtils.appendElement(activityCourtOrderAssociation, NS_JXDM_51, "Subject");
		XmlUtils.addAttribute(subject, NS_STRUCTURES_30, "ref", PERSON_01);
	}

	private void appendCourtOrderElement(Element firearmPurchaseProhibitionReport)
			throws IOException {
		Element courtOrder = XmlUtils.appendElement(firearmPurchaseProhibitionReport, NS_JXDM_51, "CourtOrder");
		XmlUtils.addAttribute(courtOrder, NS_STRUCTURES_30, "id", ORDER_01);
		Element courtOrderIssuingCourt = XmlUtils.appendElement(firearmPurchaseProhibitionReport, NS_JXDM_51, "CourtOrderIssuingCourt");
		Element organizationAugmentation = XmlUtils.appendElement(courtOrderIssuingCourt, NS_JXDM_51, "OrganizationAugmentation");
		Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_51, "OrganizationORIIdentification");
		Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC_30, "IdentificationID");
		identificationID.setTextContent("C"+ RandomStringUtils.randomNumeric(6));
		
		Element courtOrderIssuingDate = XmlUtils.appendElement(courtOrder, NS_JXDM_51, "CourtOrderIssuingDate");
		Element dateElement = XmlUtils.appendElement(courtOrderIssuingDate, NS_NC_30, "Date"); 
		dateElement.setTextContent(randomDate());
	}

	private void appendCaseElement(Element firearmPurchaseProhibitionReport) {
		Element caseElement = XmlUtils.appendElement(firearmPurchaseProhibitionReport, NS_NC_30, "Case");
		XmlUtils.addAttribute(caseElement, NS_STRUCTURES_30, "id", CASE_01);
		Element caseDocketId = XmlUtils.appendElement(caseElement, NS_NC_30, "CaseDocketID"); 
		caseDocketId.setTextContent("D" + RandomStringUtils.randomNumeric(6));
	}

	private void appendFirearmPurchaohibitionElement(Element firearmPurchaseProhibitionReport) {
		Element firearmPurchaseProhibition = XmlUtils.appendElement(
				firearmPurchaseProhibitionReport,
				NS_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS_EXT,
				"FirearmPurchaseProhibition");
		XmlUtils.addAttribute(firearmPurchaseProhibition, NS_STRUCTURES_30, "id", FPP_01); 
		Element firearmPurchaseProhibitionCode = XmlUtils.appendElement(
				firearmPurchaseProhibition, NS_MAINE_FPP_CODES,
				"FirearmPurchaseProhibitionCode");
		firearmPurchaseProhibitionCode.setTextContent("Mental Health");
	}

	private Element createRootElment(Document document) {
		Element rootElement = document.createElementNS(
				OjbcNamespaceContext.NS_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS,
		        OjbcNamespaceContext.NS_PREFIX_FIREARM_PURCHASE_PROHIBITION_QUERY_RESULTS +":FirearmPurchaseProhibitionQueryResults");
		document.appendChild(rootElement);
		return rootElement;
	}

    private void addPersonElement(Element parentElement, PersonElementWrapper person, DateTime baseDate) {

        Element e;
        Element personElement = appendElement(parentElement, OjbcNamespaceContext.NS_NC_30, "Person");

        XmlUtils.addAttribute(personElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", PERSON_01);
        person.personId = "Person"+ StringUtils.leftPad(person.id, 2);

        e = appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC_30, "Date");
        e.setTextContent(XML_DATE_FORMAT.print(person.birthdate));

        e = appendElement(personElement, OjbcNamespaceContext.NS_NC_30, "PersonName");
        Element pn = e;
        e = appendElement(pn, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
        e.setTextContent(getFirstNameSample(person));
        e = appendElement(pn, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
        e.setTextContent(person.middleName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC_30, "PersonSurName");
        e.setTextContent(getLastNameSample(person));
        e = appendElement(personElement, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());
    }

}
