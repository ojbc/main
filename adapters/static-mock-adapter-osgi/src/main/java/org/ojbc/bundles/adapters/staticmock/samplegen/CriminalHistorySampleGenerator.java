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
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Years;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class for generating random sample instances of criminal histories (rap sheets).  The class produces singleton instances (create them by calling
 * getInstance() ).
 *
 */
public class CriminalHistorySampleGenerator extends AbstractPersonSampleGenerator {

    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog( CriminalHistorySampleGenerator.class );

    private List<CriminalOffenseStatute> offenses;

    protected CriminalHistorySampleGenerator() throws ParserConfigurationException, IOException {
        super();
        offenses = new ArrayList<CriminalOffenseStatute>();
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("static-files/HI-offenses.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "\t");
            String statute = st.nextToken();
            @SuppressWarnings("unused")
            String statuteSource = st.nextToken();
            String shortDescription = st.nextToken();
            String description = st.nextToken();
            String severity = st.hasMoreTokens() ? st.nextToken() : "NA";
            CriminalOffenseStatute o = new CriminalOffenseStatute();
            o.statute = statute;
            o.shortDescription = shortDescription;
            o.description = description;
            o.severity = severity;
            offenses.add(o);
        }
    }

    private static CriminalHistorySampleGenerator INSTANCE;

    /**
     * Get the singleton instance of this class
     * @return the instance
     */
    public static final CriminalHistorySampleGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new CriminalHistorySampleGenerator();
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

        for (PersonElementWrapper person : people) {

            Document ret = documentBuilder.newDocument();
            personDocuments.add(ret);
            Element e = null;

            //LOG.info("person.state=" + person.state + ", stateParam=" + stateParam);

            e = ret.createElementNS(OjbcNamespaceContext.NS_CH_DOC, "CriminalHistory");
            ret.appendChild(e);
            e.setPrefix(OjbcNamespaceContext.NS_PREFIX_CH_DOC);

            e = appendElement(e, OjbcNamespaceContext.NS_CH_EXT, "RapSheet");
            Element rapSheet = e;

            e = appendElement(rapSheet, OjbcNamespaceContext.NS_RAPSHEET_41, "Metadata");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "ReportingOrganizationText");
            e.setTextContent("State CHRI");

            addIntroductionElement(person, rapSheet);
            addPersonElement(ret, person, rapSheet);
            addBiometricElement(ret, person, rapSheet);

            String locationId = addLocationElement(ret, person, rapSheet);

            int arrestCount = generatePoissonInt(1, true);

            List<Arrest> arrests = new ArrayList<Arrest>();

            for (int i = 0; i < arrestCount; i++) {
                Arrest arrest = new Arrest(baseDate, person);
                arrests.add(arrest);
            }

            Set<Agency> agencies = new HashSet<Agency>();
            for (Arrest arrest : arrests) {
                agencies.add(arrest.arrestingAgency);
                for (ArrestCharge arrestCharge : arrest.charges) {
                    if (arrestCharge.offense != null) {
                        agencies.add(arrestCharge.offense.supervisionAgency);
                    }
                }
            }

            addAgencyElements(ret, rapSheet, agencies);
            addResidenceLocationElement(ret, person, rapSheet, locationId);
            addSupervisionCycleElements(ret, rapSheet, arrests);
            addMainCycleElements(ret, rapSheet, arrests);
            addArrestAgencyAssociations(ret, rapSheet, arrests);
            addSupervisionAgencyAssociations(ret, rapSheet, arrests);
            
            addProtectionOrderElements(rapSheet, person, baseDate);
            
            OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(ret.getDocumentElement());

        }

        return personDocuments;

    }

    private void addProtectionOrderElements(Element rapSheetElement, PersonElementWrapper person, DateTime baseDate) {
        
        int orderCount = generatePoissonInt(.3, false);
        
        for (int i=0;i < orderCount;i++) {
            
            Element orderElement = appendElement(rapSheetElement, OjbcNamespaceContext.NS_CH_EXT, "Order");
            Element activityElement = appendElement(orderElement, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
            Element e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(generateRandomID("ORDER", 10));
            e = appendElement(activityElement, OjbcNamespaceContext.NS_NC, "IdentificationCategoryText");
            e.setTextContent("TRO DOCUMENT ID");
            e = appendElement(orderElement, OjbcNamespaceContext.NS_NC, "ActivityCategoryText");
            e.setTextContent("TRO");
            e = appendElement(orderElement, OjbcNamespaceContext.NS_NC, "ActivityDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            DateTime orderDate = generateUniformRandomDateBetween(baseDate, baseDate.minusYears(5));
            e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(orderDate));
            e = appendElement(orderElement, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderIssuingCourt");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "OrganizationName");
            e.setTextContent(getRandomCounty(person.state) + " District Court");
            e = appendElement(orderElement, OjbcNamespaceContext.NS_JXDM_41, "CourtOrderServiceDescriptionText");
            e.setTextContent(generateRandomCodeFromList("PENDING", "SERVED"));
            e = appendElement(orderElement, OjbcNamespaceContext.NS_CH_EXT, "CourtCase");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent("TRO-" + baseDate.getYear() + generateRandomID("-", 8));
            e = appendElement(orderElement, OjbcNamespaceContext.NS_CH_EXT, "ProtectionOrderExpirationDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date"); 
            e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(orderDate.plusDays(randomGenerator.nextInt(5, 365))));
        }
        
    }

    private void addSupervisionAgencyAssociations(Document parentDocument, Element rapSheetElement, List<Arrest> arrests) {

        for (Arrest arrest : arrests) {

            if (arrest.custodySupervisionId != null) {

                Element saa = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "SupervisionAgencyAssociation");
                Element e = appendElement(saa, OjbcNamespaceContext.NS_RAPSHEET_41, "SupervisionReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.custodySupervisionId);
                e = appendElement(saa, OjbcNamespaceContext.NS_NC, "OrganizationReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.custodySupervisionAgency.getXmlId());
            }

            if (arrest.probationSupervisionId != null) {

                Element saa = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "SupervisionAgencyAssociation");
                Element e = appendElement(saa, OjbcNamespaceContext.NS_RAPSHEET_41, "SupervisionReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.probationSupervisionId);
                e = appendElement(saa, OjbcNamespaceContext.NS_NC, "OrganizationReference");
                XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.probationSupervisionAgency.getXmlId());
            }

        }
    }

    private void addArrestAgencyAssociations(Document parentDocument, Element rapSheetElement, List<Arrest> arrests) {

        for (Arrest arrest : arrests) {

            Element e = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "ArrestAgencyAssociation");
            Element aaa = e;
            e = appendElement(aaa, OjbcNamespaceContext.NS_NC, "ActivityReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.id);
            e = appendElement(aaa, OjbcNamespaceContext.NS_NC, "OrganizationReference");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", arrest.arrestingAgency.getXmlId());
        }

    }

    private void addMainCycleElements(Document parentDocument, Element rapSheetElement, List<Arrest> arrests) {
        Element e;
        for (Arrest arrest : arrests) {
            boolean courtAction = false;
            Element rapSheetCycle = appendElement(rapSheetElement, OjbcNamespaceContext.NS_CH_EXT, "RapSheetCycle");
            e = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "CycleEarliestDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.date));
            Element arrestElement = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "Arrest");
            XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES, "id", arrest.id);
            e = appendElement(arrestElement, OjbcNamespaceContext.NS_NC, "ActivityDate");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
            e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.date));
            e = appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_41, "ArrestAgencyRecordIdentification");
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(arrest.recordId);
            for (ArrestCharge arrestCharge : arrest.charges) {
                Element arrestChargeElement = appendElement(arrestElement, OjbcNamespaceContext.NS_RAPSHEET_41, "ArrestCharge");
                e = appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeDescriptionText");
                e.setTextContent(arrestCharge.description);
                if (!arrestCharge.prosecuted) {
                    e = appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeDisposition");
                    e = appendElement(e, OjbcNamespaceContext.NS_NC, "DispositionDescriptionText");
                    e.setTextContent("LACK OF PROS");
                } else {
                    courtAction = true;
                }
                e = appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeIdentification");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                e.setTextContent(arrestCharge.id);
                e = appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeSeverityText");
                e.setTextContent(arrestCharge.severity);
                e = appendElement(arrestChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeTrackingIdentification");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                e.setTextContent(arrestCharge.trackingId);
            }

            boolean sentenced = false;

            if (courtAction && arrest.dispoDate != null) {

                Element courtActionElement = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "CourtAction");
                for (ArrestCharge arrestCharge : arrest.charges) {
                    if (arrestCharge.prosecuted) {
                        Element courtChargeElement = appendElement(courtActionElement, OjbcNamespaceContext.NS_RAPSHEET_41, "CourtCharge");
                        e = appendElement(courtChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeDescriptionText");
                        e.setTextContent(arrestCharge.description);
                        e = appendElement(courtChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeDisposition");
                        Element dispoElement = e;
                        e = appendElement(dispoElement, OjbcNamespaceContext.NS_NC, "DispositionDate");
                        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                        e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.dispoDate));
                        e = appendElement(dispoElement, OjbcNamespaceContext.NS_NC, "DispositionDescriptionText");
                        if (arrestCharge.guilty) {
                            e.setTextContent("GUILTY");
                            sentenced = true;
                        } else {
                            e.setTextContent("NOT GUILTY");
                        }
                        e = appendElement(courtChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeIdentification");
                        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                        e.setTextContent(arrestCharge.id);
                        e = appendElement(courtChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeSeverityText");
                        e.setTextContent(arrestCharge.severity);
                        e = appendElement(courtChargeElement, OjbcNamespaceContext.NS_JXDM_41, "ChargeTrackingIdentification");
                        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                        e.setTextContent(arrestCharge.trackingId);
                    }
                }
            }

            if (sentenced) {

                Element sentencingElement = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_CH_EXT, "Sentencing");

                for (ArrestCharge arrestCharge : arrest.charges) {
                    if (arrestCharge.guilty) {
                        Element sentenceElement = appendElement(sentencingElement, OjbcNamespaceContext.NS_CH_EXT, "Sentence");
                        e = appendElement(sentenceElement, OjbcNamespaceContext.NS_JXDM_41, "SentenceDescriptionText");
                        String termPeriod = " DAYS";
                        String sentenceType = " CONFINEMENT ";
                        int termLength = arrestCharge.offense.daysInJail;
                        if (termLength > 365 * 2) {
                            termLength = (int) Math.round(termLength / 365.0);
                            termPeriod = " YEARS";
                        } else if (termLength == 0) {
                            termLength = (int) Math.round(arrestCharge.offense.daysOfProbation / 30.0);
                            termPeriod = " MONTHS";
                            sentenceType = " PROBATION ";
                        }
                        e.setTextContent("ON " + DATE_FORMATTER_MM_DD_YYYY.print(arrest.dispoDate) + " SUBJECT SENTENCED TO " + termLength + termPeriod + sentenceType + "AND " + "A FINE OF "
                                + NumberFormat.getCurrencyInstance().format(arrestCharge.offense.fine));

                        e = appendElement(sentenceElement, OjbcNamespaceContext.NS_RAPSHEET_41, "SentenceCharge");
                        e = appendElement(e, OjbcNamespaceContext.NS_JXDM_41, "ChargeTrackingIdentification");
                        e = appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
                        e.setTextContent(arrestCharge.trackingId);
                    }
                }
            }

        }
    }

    private void addSupervisionCycleElements(Document parentDocument, Element rapSheetElement, List<Arrest> arrests) {
        Element e;
        for (Arrest arrest : arrests) {

            if (arrest.custodyEndDate != null) {
                Element rapSheetCycle = appendElement(rapSheetElement, OjbcNamespaceContext.NS_CH_EXT, "RapSheetCycle");
                e = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "CycleEarliestDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.dispoDate));
                Element supervisionElement = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "Supervision");
                XmlUtils.addAttribute(supervisionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", arrest.custodySupervisionId);
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "ActivityCategoryText");
                e.setTextContent("CUSTODY");
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "ActivityDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.dispoDate));
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "SupervisionCustodyStatus");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
                e.setTextContent(arrest.felonyConviction ? "SENT FELON" : "SENT MISD");
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "SupervisionRelease");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "ActivityDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.custodyEndDate));
            }

            if (arrest.probationEndDate != null) {
                Element rapSheetCycle = appendElement(rapSheetElement, OjbcNamespaceContext.NS_CH_EXT, "RapSheetCycle");
                e = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "CycleEarliestDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.dispoDate));
                Element supervisionElement = appendElement(rapSheetCycle, OjbcNamespaceContext.NS_RAPSHEET_41, "Supervision");
                XmlUtils.addAttribute(supervisionElement, OjbcNamespaceContext.NS_STRUCTURES, "id", arrest.probationSupervisionId);
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "ActivityCategoryText");
                e.setTextContent("PROBATION");
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "ActivityDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.dispoDate));
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "SupervisionCustodyStatus");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "StatusDescriptionText");
                e.setTextContent(arrest.felonyConviction ? "SENT FELON" : "SENT MISD");
                e = appendElement(supervisionElement, OjbcNamespaceContext.NS_NC, "SupervisionRelease");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "ActivityDate");
                e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
                e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(arrest.probationEndDate));
            }

        }
    }

    private void addResidenceLocationElement(Document parentDocument, PersonElementWrapper person, Element rapSheetElement, String locationId) {
        Element e;
        e = appendElement(rapSheetElement, OjbcNamespaceContext.NS_NC, "ResidenceAssociation");
        Element ra = e;
        e = appendElement(ra, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);
        e = appendElement(ra, OjbcNamespaceContext.NS_NC, "LocationReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", locationId);
    }

    private void addAgencyElements(Document parentDocument, Element rapSheetElement, Set<Agency> agencies) {
        Element e;
        for (Agency agency : agencies) {
            e = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "Agency");
            XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "id", agency.getXmlId());
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "OrganizationName");
            e.setTextContent(agency.name);
        }
    }

    private final class Agency {
        public String name;

        public int hashCode() {
            return ("AGY" + name).hashCode();
        }

        public String getXmlId() {
            return "AGY" + hashCode();
        }
        
        public boolean equals(Object o)
        {
            return o != null && o instanceof Agency && o.hashCode() == hashCode();
        }
    }

    private final class Arrest {
        public DateTime date;
        public DateTime dispoDate;
        public String id;
        public String recordId;
        public Agency arrestingAgency;
        public DateTime custodyEndDate;
        public DateTime probationEndDate;
        public String custodySupervisionId;
        public String probationSupervisionId;
        public Agency custodySupervisionAgency;
        public Agency probationSupervisionAgency;
        public boolean felonyConviction;
        public List<ArrestCharge> charges = new ArrayList<ArrestCharge>();

        public Arrest(DateTime baseDate, PersonElementWrapper person) {

            date = person.birthdate;

            while (Years.yearsBetween(person.birthdate, date).getYears() < 14) {
                // make sure we don't arrest anyone younger than 14
                date = generateUniformRandomDateBetween(baseDate.minusYears(8), baseDate);
            }

            id = generateRandomID("A", 10); // could be a problem in the unlikely event you generate two arrests with the same id (this is used as an xml id)
            recordId = generateRandomID("", 3) + generateRandomID("-", 7);
            int chargeCount = generatePoissonInt(1, true);
            int courtCaseLength = (int) randomGenerator.nextGaussian(180, 60);
            int daysSinceArrest = Days.daysBetween(date, baseDate).getDays();
            arrestingAgency = new Agency();
            arrestingAgency.name = getRandomCity(person.state).toUpperCase() + " PD";

            if (courtCaseLength < daysSinceArrest) {
                dispoDate = date.plusDays(courtCaseLength);
            }

            int maxDaysInJail = 0;
            int maxDaysOfProbation = 0;
            felonyConviction = false;

            for (int i = 0; i < chargeCount; i++) {
                ArrestCharge arrestCharge = new ArrestCharge(person);
                charges.add(arrestCharge);
                if (arrestCharge.offense != null) {
                    maxDaysInJail = Math.max(maxDaysInJail, arrestCharge.offense.daysInJail);
                    maxDaysOfProbation = Math.max(maxDaysOfProbation, arrestCharge.offense.daysOfProbation);
                    if ("F".equals(arrestCharge.severity.substring(0, 1))) {
                        felonyConviction = true;
                    }
                    if (arrestCharge.offense.daysOfProbation > 0) {
                        probationSupervisionAgency = arrestCharge.offense.supervisionAgency;
                    }
                    if (arrestCharge.offense.daysInJail > 0) {
                        custodySupervisionAgency = arrestCharge.offense.supervisionAgency;
                    }
                }
            }

            if (maxDaysInJail > 0 && dispoDate != null) {
                custodyEndDate = dispoDate.plusDays(maxDaysInJail);
                custodySupervisionId = id + "SC";
            }

            if (maxDaysOfProbation > 0 && dispoDate != null) {
                probationEndDate = dispoDate.plusDays(maxDaysOfProbation);
                probationSupervisionId = id + "SP";
            }

            if (coinFlip(.15)) {
                arrestingAgency.name = "State Police";
            }

        }
    }

    @SuppressWarnings("unused")
    private final class OffenseSentence {
        public String statute;
        public String shortDescription;
        public String description;
        public String severity;
        public int daysInJail;
        public double fine;
        public int daysOfProbation;
        public Agency supervisionAgency;

        public OffenseSentence(CriminalOffenseStatute statute, PersonElementWrapper person) {
            this.statute = statute.statute;
            shortDescription = statute.shortDescription;
            description = statute.description;
            severity = statute.severity;
            SentenceOptions sentence = statute.getSentence();
            daysInJail = sentence.getDaysInJail();
            fine = sentence.getFine();
            daysOfProbation = sentence.getProbationTerm();
            supervisionAgency = new Agency();
            if (daysOfProbation != 0) {
                supervisionAgency.name = getRandomCounty(person.state) + " County Probation";
            }
            if (daysInJail > 0 && daysInJail < 366) {
                supervisionAgency.name = getRandomCounty(person.state) + " County Jail";
            } else if (daysInJail > 0) {
                supervisionAgency.name = "Department of Corrections";
            }
        }
    }

    private final class CriminalOffenseStatute {
        public String statute;
        public String shortDescription;
        public String description;
        public String severity;

        public SentenceOptions getSentence() {
            if (severity.contains("MD")) {
                return new SentenceOptions(10, 90, 1.0, 1000.0, .8);
            }
            if (severity.contains("FC")) {
                return new SentenceOptions(365, 365 * 2, 1.0, 1000.0, .5);
            }
            if (severity.contains("FB")) {
                return new SentenceOptions(365, 365 * 5, 1.0, 10000.0, .1);
            }
            if (severity.contains("FA")) {
                return new SentenceOptions(365, 365 * 30, 1.0, 50000.0, .0);
            }
            return new SentenceOptions(1, 5, 1.0, 1000.0, .8);
        }
    }

    private final class SentenceOptions {
        private int minDaysInJail;
        private int maxDaysInJail;
        private double minFine;
        private double maxFine;
        private boolean probation;

        public SentenceOptions(int minDaysInJail, int maxDaysInJail, double minFine, double maxFine, double probationProbability) {
            probation = coinFlip(probationProbability);
            this.minDaysInJail = minDaysInJail;
            this.maxDaysInJail = maxDaysInJail;
            this.minFine = minFine;
            this.maxFine = maxFine;
        }

        public int getDaysInJail() {
            return probation ? 0 : randomGenerator.nextInt(minDaysInJail, maxDaysInJail);
        }

        public double getFine() {
            return randomGenerator.nextUniform(minFine, maxFine);
        }

        public int getProbationTerm() {
            return probation ? randomGenerator.nextInt(180, 365 * 5) : 0;
        }
    }

    private final class ArrestCharge {
        public String id;
        public String description;
        public String severity;
        public String trackingId;
        public boolean prosecuted;
        public boolean guilty;
        public OffenseSentence offense;

        public ArrestCharge(PersonElementWrapper person) {
            id = generateRandomID("AC", 8);
            trackingId = id;
            CriminalOffenseStatute offenseStatute = (CriminalOffenseStatute) generateRandomValueFromList(offenses.toArray());
            severity = offenseStatute.severity;
            prosecuted = coinFlip(.5);
            guilty = prosecuted && coinFlip(.3);
            if (guilty) {
                offense = new OffenseSentence(offenseStatute, person);
            }
        }
    }

    private String addLocationElement(Document parentDocument, PersonElementWrapper person, Element rapSheet) {
        StringBuffer fullAddress = new StringBuffer(1024);
        fullAddress.append(person.streetAddress).append(" ").append(person.city).append(", ").append(person.state).append(" ").append(person.zipCode);
        String id = "L" + String.valueOf(fullAddress.hashCode());
        Element location = appendElement(rapSheet, OjbcNamespaceContext.NS_NC, "Location");
        XmlUtils.addAttribute(location, OjbcNamespaceContext.NS_STRUCTURES, "id", id);

        Element e = appendElement(location, OjbcNamespaceContext.NS_NC, "LocationAddress");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "AddressFullText");
        e.setTextContent(fullAddress.toString());

        return id;
    }

    private void addBiometricElement(Document parentDocument, PersonElementWrapper person, Element rapSheetElement) {

        Element bior = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "PersonBiometricsAssociation");
        Element e = appendElement(bior, OjbcNamespaceContext.NS_NC, "PersonReference");
        XmlUtils.addAttribute(e, OjbcNamespaceContext.NS_STRUCTURES, "ref", person.personId);

        Element bio = appendElement(bior, OjbcNamespaceContext.NS_NC, "Biometric");
        e = appendElement(bio, OjbcNamespaceContext.NS_NC, "BiometricValueText");
        e.setTextContent("W W \\ \\ \\ ,W A / / /");
        e = appendElement(bio, OjbcNamespaceContext.NS_NC, "BiometricDescriptionText");
        e.setTextContent("Fingerprint Class");

        e = appendElement(bior, OjbcNamespaceContext.NS_RAPSHEET_41, "PersonBiometrics");

    }

    private void addPersonElement(Document parentDocument, PersonElementWrapper person, Element rapSheetElement) {

        Element rsp = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "RapSheetPerson");
        Element e;
        
        XmlUtils.addAttribute(rsp, OjbcNamespaceContext.NS_STRUCTURES, "id", person.personId);

        if (coinFlip(.7)) {
            e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonAlternateName");
            Element alternateName = e;
            e = appendElement(alternateName, OjbcNamespaceContext.NS_NC, "PersonGivenName");
            e.setTextContent(person.firstName + " " + generateRandomLetter());
            e = appendElement(alternateName, OjbcNamespaceContext.NS_NC, "PersonSurName");
            e.setTextContent(person.lastName);
        }

        if (coinFlip(.1)) {
            e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonAlternateName");
            Element alternateName = e;
            e = appendElement(alternateName, OjbcNamespaceContext.NS_NC, "PersonGivenName");
            e.setTextContent(person.firstName);
            e = appendElement(alternateName, OjbcNamespaceContext.NS_NC, "PersonSurName");
            e.setTextContent(person.lastName + "-" + generateRandomCodeFromList("Washington", "Adams", "Jefferson", "Madison", "Monroe"));
        }

        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(person.birthdate));
        e = appendElement(rsp, OjbcNamespaceContext.NS_RAPSHEET_41, "PersonEyeColorText");
        e.setTextContent(generateRandomCodeFromList("Brown", "Black", "Blue", "Hazel"));
        e = appendElement(rsp, OjbcNamespaceContext.NS_RAPSHEET_41, "PersonHairColorText");
        e.setTextContent(generateRandomCodeFromList("Brown", "Black", "Red Or Auburn", "Blonde Or Strawberry"));

        if (coinFlip(.4)) {
            e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonHeightMeasure");
            Element phm = e;
            e = appendElement(e, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
            e.setTextContent(String.valueOf(Math.round(Integer.parseInt(person.centimeters) * .39)));
            e = appendElement(phm, OjbcNamespaceContext.NS_NC, "LengthUnitCode");
            e.setTextContent("INH");
        }

        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonName");
        Element pn = e;
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
        e.setTextContent(person.middleName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);

        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonRaceCode");
        e.setTextContent(generateRandomCodeFromList("B", "A", "W", "U", "I"));
        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonSexCode");
        e.setTextContent(person.sex.substring(0, 1).toUpperCase());

        if (coinFlip(.8)) {
            e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonSSNIdentification");
            Element ssn = e;
            e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(person.nationalID);
            e = appendElement(ssn, OjbcNamespaceContext.NS_NC, "IdentificationJurisdictionText");
            e.setTextContent("SSA");
        }

        if (coinFlip(.4)) {
            e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonWeightMeasure");
            Element pwm = e;
            e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "MeasurePointValue");
            e.setTextContent(person.pounds);
            e = appendElement(pwm, OjbcNamespaceContext.NS_NC, "WeightUnitCode");
            e.setTextContent("LBR");
        }

        e = appendElement(rsp, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
        Element pa = e;
        if (coinFlip(.7)) {
            e = appendElement(pa, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
            Element fbi = e;
            e = appendElement(fbi, OjbcNamespaceContext.NS_NC, "IdentificationID");
            e.setTextContent(generateRandomID("", 6) + generateRandomLetter() + generateRandomLetter() + generateRandomID("", 1));
            e = appendElement(fbi, OjbcNamespaceContext.NS_NC, "IdentificationJurisdictionText");
        }
        e = appendElement(pa, OjbcNamespaceContext.NS_JXDM_41, "PersonStateFingerprintIdentification");
        Element sid = e;
        e = appendElement(sid, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(generateRandomID("A", 7));
        e = appendElement(sid, OjbcNamespaceContext.NS_NC, "IdentificationJurisdictionText");

    }

    private void addIntroductionElement(PersonElementWrapper person, Element rapSheetElement) {

        Element e;
        e = appendElement(rapSheetElement, OjbcNamespaceContext.NS_RAPSHEET_41, "Introduction");
        e = appendElement(e, OjbcNamespaceContext.NS_RAPSHEET_41, "RapSheetRequest");
        Element rsr = e;

        e = appendElement(rsr, OjbcNamespaceContext.NS_RAPSHEET_41, "PurposeCode");
        e.setTextContent("A");

        e = appendElement(rsr, OjbcNamespaceContext.NS_RAPSHEET_41, "Attention");
        e.setTextContent("**CONFIDENTIAL INFORMATION FOR CRIMINAL JUSTICE AGENCIES ONLY**");

        e = appendElement(rsr, OjbcNamespaceContext.NS_RAPSHEET_41, "RapSheetPerson");
        Element rsp = e;

        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonBirthDate");
        e = appendElement(e, OjbcNamespaceContext.NS_NC, "Date");
        e.setTextContent(DATE_FORMATTER_YYYY_MM_DD.print(person.birthdate));

        e = appendElement(rsp, OjbcNamespaceContext.NS_NC, "PersonName");
        Element pn = e;
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonGivenName");
        e.setTextContent(person.firstName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonMiddleName");
        e.setTextContent(person.middleName);
        e = appendElement(pn, OjbcNamespaceContext.NS_NC, "PersonSurName");
        e.setTextContent(person.lastName);

    }

}
