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
package org.ojbc.adapters.analyticsstaging.custody.processor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class BehavioralHealthReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( BehavioralHealthReportProcessor.class );
	
	@Transactional
	public void processReport(Document report, String personUniqueIdentifier) throws Exception
	{
		log.info("Processing Behavioral Health report." );
		XmlUtils.printNode(report);
		
		NodeList activityPersonAssociations = XmlUtils.xPathNodeListSearch(report, "/bhr-doc:BehavioralHealthEvaluationRecord/nc30:ActivityPersonAssociation");
		List<BehavioralHealthAssessment> behavioralHealthAssessments = new ArrayList<BehavioralHealthAssessment>();
		
		for (int i = 0; i < activityPersonAssociations.getLength(); i++) {
			Node activityPersonAssociation = activityPersonAssociations.item(i);
			Integer personId = getPersonId(report, activityPersonAssociation);
			
			BehavioralHealthAssessment behavioralHealthAssessment = new BehavioralHealthAssessment(); 
			behavioralHealthAssessment.setPersonId(personId);
			
			setEvaluationInfo(report, activityPersonAssociation,
					behavioralHealthAssessment);
			behavioralHealthAssessments.add(behavioralHealthAssessment);
		}
		
		analyticalDatastoreDAO.saveBehavioralHealthAssessments(behavioralHealthAssessments);
		
		log.info("Processed Behavioral Health report.");
		
	}

	private void setEvaluationInfo(Document report, Node activityPersonAssociation,
			BehavioralHealthAssessment behavioralHealthAssessment)
			throws Exception {
		String evaluationRefId = XmlUtils.xPathStringSearch(activityPersonAssociation, "nc30:Activity/@s30:ref");
		Node behavioralHealthEvaluationNode = XmlUtils.xPathNodeSearch(report, 
				"/bhr-doc:BehavioralHealthEvaluationRecord/bhr-ext:BehavioralHealthEvaluation[@s30:id='" + evaluationRefId + "']");
		
		String evaluationDate = XmlUtils.xPathStringSearch(behavioralHealthEvaluationNode, "nc30:ActivityDate/nc30:Date");
		if (StringUtils.isNotBlank(evaluationDate)){
			behavioralHealthAssessment.setEvaluationDate(LocalDate.parse(evaluationDate));
		}
		
		String seriousMentalHealthCategoryText = XmlUtils.xPathStringSearch(behavioralHealthEvaluationNode, "bhr-ext:SeriousMentalHealthCategoryText");
		Integer behavioralHealthTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BehavioralHealthType, seriousMentalHealthCategoryText);
		
		behavioralHealthAssessment.setBehavioralHealthType(new KeyValue(behavioralHealthTypeId, seriousMentalHealthCategoryText));
	}

	private Integer getPersonId(Document report, Node activityPersonAssociation)
			throws Exception {
		String personRefId = XmlUtils.xPathStringSearch(activityPersonAssociation, "nc30:Person/@s30:ref");
		
		String personUniqueId = XmlUtils.xPathStringSearch(report, 
				"/bhr-doc:BehavioralHealthEvaluationRecord/nc30:Person[@s30:id='" + personRefId + "']/bhr-ext:PersonIndexIdentification/nc30:IdentificationID");
		
		if (StringUtils.isBlank(personUniqueId)){
			String errorMessage = "The PersonIndexIdentification should not be empty on nc30:Person with id " + personRefId;
			log.error(errorMessage);
			throw new IllegalArgumentException( errorMessage ); 
		}
		
		Integer personId = analyticalDatastoreDAO.getPersonIdByUniqueId(personUniqueId);
		
		if (personId == null){
			String errorMessage = "Person uniqeu ID " + personUniqueId + " not found in the database";
			log.error(errorMessage);
			throw new IllegalArgumentException( errorMessage ); 
		}
		return personId;
	}

}
