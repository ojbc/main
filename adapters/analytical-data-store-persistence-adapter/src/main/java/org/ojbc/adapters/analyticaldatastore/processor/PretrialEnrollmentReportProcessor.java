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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticaldatastore.processor;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialServiceParticipation;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class PretrialEnrollmentReportProcessor extends AbstractReportRepositoryProcessor{

	private static final Log log = LogFactory.getLog( PretrialEnrollmentReportProcessor.class );
	
	
	@Transactional
	public void processReport(Document report) throws Exception
	{
		int pretrialServiceParticipationPkId = processPretrialServiceParticipation(report);
		processAssessedNeeds(pretrialServiceParticipationPkId, report);
		
		//TODO need to know the pretrialServiceDescription mapping to work on PretrialServiceAssociation.
	}


	private void processAssessedNeeds(int pretrialServiceParticipationPkId,
			Document report) throws Exception {
		List<Integer> assessedNeedsIds = new ArrayList<Integer>();
		
		String housingNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:HousingNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, housingNeeds, "housing");
		
		String insuranceNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:InsuranceNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, insuranceNeeds, "insurance");
		
		String medicalNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MedicalNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, medicalNeeds, "medical");
		
		String mentalHealthNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MentalHealthNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, mentalHealthNeeds, "mental health");

		String substanceAbuseNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:SubstanceAbuseNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, substanceAbuseNeeds, "substance abuse");
		
		analyticalDatastoreDAO.savePretrialServiceNeedAssociations(assessedNeedsIds, pretrialServiceParticipationPkId);
	}


	private void addAssessedNeedsIdToList(int pretrialServiceParticipationPkId,
			List<Integer> assessedNeedsIds,
			String indicator, String indicatorType) {
		if (BooleanUtils.toBoolean(indicator)){
			assessedNeedsIds.add(descriptionCodeLookupService.retrieveCode(CodeTable.AssessedNeed, indicatorType));
		}
	}


	private int processPretrialServiceParticipation(Document report) throws Exception, ParseException {
		PretrialServiceParticipation pretrialServiceParticipation = new PretrialServiceParticipation(); 
		
		pretrialServiceParticipation.setRecordType('N');
		//TODO need to find out the mapping for PretrialServiceCaseNumber
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/jxdm50:Subject/nc30:RoleOfPerson");
        int personPk = savePerson(personNode, OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_PREFIX_JXDM_50);
        pretrialServiceParticipation.setPersonID(personPk);

        String countyName = XmlUtils.xPathStringSearch(report, 
        		"/pse-doc:PretrialServiceEnrollmentReport/pse-ext:PreTrialServicesEnrollment/pse-ext:ActivityLocation/nc30:Address/nc30:LocationCountyName");
        Integer countyId = descriptionCodeLookupService.retrieveCode(CodeTable.County, countyName); 
        pretrialServiceParticipation.setCountyID(countyId);
        
        String riskScore = XmlUtils.xPathStringSearch(report, 
        		"/pse-doc:PretrialServiceEnrollmentReport/pse-ext:ORASAssessment/pse-ext:AssessmentScoreNumeric");
        if (StringUtils.isNotBlank(riskScore)){
        	pretrialServiceParticipation.setRiskScore(Integer.valueOf(riskScore));
        }
        
        //TODO mapping to the arrest Date. double check with Andrew. 
		String intakeDateString=XmlUtils.xPathStringSearch(report,
				"/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/jxdm50:IncidentAugmentation/jxdm50:IncidentArrest/nc30:ActivityDate/nc30:DateTime");
		if (StringUtils.isNotBlank(intakeDateString)){
			pretrialServiceParticipation.setIntakeDate(DATE_TIME_FORMAT.parse(intakeDateString));
		}
		
		String arrestAgencyOri = XmlUtils.xPathStringSearch(report, 
				"/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/jxdm50:IncidentAugmentation/jxdm50:IncidentArrest/jxdm50:ArrestAgency/jxdm50:OrganizationAugmentation/jxdm50:OrganizationORIIdentification/nc30:IdentificationID");
		pretrialServiceParticipation.setArrestingAgencyORI(StringUtils.trimToNull(arrestAgencyOri));
		
		String arrestIncidentCaseNumber = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/nc30:ActivityIdentification/nc30:IdentificationID");
		pretrialServiceParticipation.setArrestIncidentCaseNumber(StringUtils.trimToNull(arrestIncidentCaseNumber));
		
		//TODO pretrialServiceCaseNumber can not be null. set it to incident number to pass test.
		pretrialServiceParticipation.setPretrialServiceCaseNumber(StringUtils.trimToNull(arrestIncidentCaseNumber));
		
		return analyticalDatastoreDAO.savePretrialServiceParticipation(pretrialServiceParticipation);
	}

}
