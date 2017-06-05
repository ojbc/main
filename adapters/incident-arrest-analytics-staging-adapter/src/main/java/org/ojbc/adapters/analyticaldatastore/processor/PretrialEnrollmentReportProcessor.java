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
package org.ojbc.adapters.analyticaldatastore.processor;

import java.text.ParseException;
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
		processPretrialServiceAssociations(pretrialServiceParticipationPkId, report);
	}


	private void processPretrialServiceAssociations(
			Integer pretrialServiceParticipationPkId, Document report) throws Exception {
		List<Integer> pretrialServiceIds = new ArrayList<Integer>();
		
		String housingNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:HousingNeedsIndicator");
		addPretrialServiceIdToList(pretrialServiceParticipationPkId, pretrialServiceIds, housingNeeds, AssessedNeeds.Housing);
		
		String insuranceNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:InsuranceNeedsIndicator");
		addPretrialServiceIdToList(pretrialServiceParticipationPkId, pretrialServiceIds, insuranceNeeds, AssessedNeeds.Insurance);
		
		String medicalNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MedicalNeedsIndicator");
		addPretrialServiceIdToList(pretrialServiceParticipationPkId, pretrialServiceIds, medicalNeeds, AssessedNeeds.Medical);
		
		String mentalHealthNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MentalHealthNeedsIndicator");
		addPretrialServiceIdToList(pretrialServiceParticipationPkId, pretrialServiceIds, mentalHealthNeeds, AssessedNeeds.MentalHealth);

		String substanceAbuseNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:SubstanceAbuseNeedsIndicator");
		addPretrialServiceIdToList(pretrialServiceParticipationPkId, pretrialServiceIds, substanceAbuseNeeds, AssessedNeeds.SubstanceAbuse);
		
		analyticalDatastoreDAO.savePretrialServiceAssociations(pretrialServiceIds, pretrialServiceParticipationPkId);

	}


	private void addPretrialServiceIdToList(
			Integer pretrialServiceParticipationPkId,
			List<Integer> pretrialServiceIds, String indicator, AssessedNeeds assessedNeeds) {
		if (BooleanUtils.toBoolean(indicator)){
			pretrialServiceIds.add(descriptionCodeLookupService.retrieveCode(CodeTable.PretrialService, assessedNeeds.toString()));
		}
	}


	private void processAssessedNeeds(Integer pretrialServiceParticipationPkId,
			Document report) throws Exception {
		List<Integer> assessedNeedsIds = new ArrayList<Integer>();
		
		String housingNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:HousingNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, housingNeeds, AssessedNeeds.Housing);
		
		String insuranceNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:InsuranceNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, insuranceNeeds, AssessedNeeds.Insurance);
		
		String medicalNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MedicalNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, medicalNeeds, AssessedNeeds.Medical);
		
		String mentalHealthNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:MentalHealthNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, mentalHealthNeeds, AssessedNeeds.MentalHealth);

		String substanceAbuseNeeds = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/pse-ext:NeedsAssessment/pse-ext:SubstanceAbuseNeedsIndicator");
		addAssessedNeedsIdToList(pretrialServiceParticipationPkId, assessedNeedsIds, substanceAbuseNeeds, AssessedNeeds.SubstanceAbuse);
		
		analyticalDatastoreDAO.savePretrialServiceNeedAssociations(assessedNeedsIds, pretrialServiceParticipationPkId);
	}


	public static enum AssessedNeeds{
		Housing, Insurance, Medical, MentalHealth, SubstanceAbuse; 
		
		public String toString(){
			return StringUtils.join(name().split("(?<=[a-z])(?=[A-Z])"), " ");
		}
	}
	private void addAssessedNeedsIdToList(Integer pretrialServiceParticipationPkId,
			List<Integer> assessedNeedsIds,
			String indicator, AssessedNeeds assessedNeeds) {
		if (BooleanUtils.toBoolean(indicator)){
			assessedNeedsIds.add(descriptionCodeLookupService.retrieveCode(CodeTable.AssessedNeed, assessedNeeds.toString()));
		}
	}


	private Integer processPretrialServiceParticipation(Document report) throws Exception, ParseException {
		PretrialServiceParticipation pretrialServiceParticipation = new PretrialServiceParticipation(); 
		
		pretrialServiceParticipation.setRecordType('N');
		//TODO need to find out the mapping for PretrialServiceCaseNumber
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/jxdm50:Subject/nc30:RoleOfPerson");
        int personPk = savePerson(personNode, OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_PREFIX_JXDM_50);
        pretrialServiceParticipation.setPersonID(personPk);
        
		String arrestIncidentCaseNumber = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/nc30:ActivityIdentification/nc30:IdentificationID");
		pretrialServiceParticipation.setArrestIncidentCaseNumber(StringUtils.trimToNull(arrestIncidentCaseNumber));
        
		String subjectID = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/jxdm50:Subject/jxdm50:SubjectIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotEmpty(arrestIncidentCaseNumber) && StringUtils.isNotEmpty(subjectID))
		{
			String uniqueID = subjectID + "|" + arrestIncidentCaseNumber;
			
			pretrialServiceParticipation.setPretrialServiceUniqueID(uniqueID);
			
			PretrialServiceParticipation participation = analyticalDatastoreDAO.searchForPretrialServiceParticipationByUniqueID(uniqueID);
			
			if (participation != null)
			{
				pretrialServiceParticipation.setPretrialServiceParticipationID(participation.getPretrialServiceParticipationID());
				analyticalDatastoreDAO.deletePretrialServiceParticipation(participation.getPretrialServiceParticipationID());
			}	
		}	
		else
		{
			throw new Exception("Subject ID and Arrest Incident Case Number required to process pre-trial record");
		}	

        String countyName = XmlUtils.xPathStringSearch(report, 
        		"/pse-doc:PretrialServiceEnrollmentReport/pse-ext:PreTrialServicesEnrollment/pse-ext:ActivityLocation/nc30:Address/nc30:LocationCountyName");
        
        if (StringUtils.isBlank(countyName)){
        	countyName = XmlUtils.xPathStringSearch(report, "/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/nc30:IncidentLocation/nc30:Address/nc30:LocationCountyName");
        }
        
        Integer countyId = descriptionCodeLookupService.retrieveCode(CodeTable.County, countyName);
        
        if (countyId == null){
        	log.warn("County name " + StringUtils.trimToEmpty(countyName) + " not found in the county table") ;
        }
        pretrialServiceParticipation.setCountyID(countyId);
        
        String riskScore = XmlUtils.xPathStringSearch(report, 
        		"/pse-doc:PretrialServiceEnrollmentReport/pse-ext:ORASAssessment/pse-ext:AssessmentScoreNumeric");
        if (StringUtils.isNotBlank(riskScore)){
        	pretrialServiceParticipation.setRiskScore(Integer.valueOf(riskScore));
        }
        
        String arrestAgencyOri = XmlUtils.xPathStringSearch(report, 
        		"/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/jxdm50:IncidentAugmentation/jxdm50:IncidentArrest/jxdm50:ArrestAgency/jxdm50:OrganizationAugmentation/jxdm50:OrganizationORIIdentification/nc30:IdentificationID");
        pretrialServiceParticipation.setArrestingAgencyORI(StringUtils.trimToNull(arrestAgencyOri));
        
		setIntakeDate(report, pretrialServiceParticipation);
		
		
		return analyticalDatastoreDAO.savePretrialServiceParticipation(pretrialServiceParticipation);
	}


	/**
	 * Set intakeDate in the order of ORAS AsseseementDate, contactDate1, incidentDate. 
	 * @param report
	 * @param pretrialServiceParticipation
	 * @throws Exception
	 * @throws ParseException
	 */
	private void setIntakeDate(Document report,
			PretrialServiceParticipation pretrialServiceParticipation)
			throws Exception, ParseException {
		String intakeDateString=XmlUtils.xPathStringSearch(report,
				"/pse-doc:PretrialServiceEnrollmentReport/pse-ext:ORASAssessment/nc30:ActivityDate/nc30:DateTime");
		
		if (StringUtils.isBlank(intakeDateString)){
			intakeDateString=XmlUtils.xPathStringSearch(report,
				"/pse-doc:PretrialServiceEnrollmentReport/cyfs:ContactActivity/pse-ext:FirstContact/nc30:ActivityDate/nc30:DateTime");
		}
		
		if (StringUtils.isBlank(intakeDateString)){
			intakeDateString=XmlUtils.xPathStringSearch(report,
				"/pse-doc:PretrialServiceEnrollmentReport/nc30:Incident/nc30:ActivityDate/nc30:DateTime");
		}
		
		if (StringUtils.isNotBlank(intakeDateString)){
			pretrialServiceParticipation.setIntakeDate(DATE_TIME_FORMAT.parse(intakeDateString));
		}
		else{
			throw new Exception("The record is rejected because the intake date is null : " + pretrialServiceParticipation.toString()); 
		}
	}

}
