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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.dao.model.Disposition;
import org.ojbc.adapters.analyticaldatastore.util.DaoUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DispositionReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( DispositionReportProcessor.class );
	
	@Override
	public void processReport(Document report) throws Exception {
		
		XmlUtils.printNode(report);
		
		Disposition disposition = new Disposition();

		//Incident Number
		String incidentNumber = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Incident/nc30:ActivityIdentification/nc30:IdentificationID");
				
		if (StringUtils.isNotBlank(incidentNumber))
		{
			log.debug("Disposition incident number: " + incidentNumber);
			disposition.setIncidentCaseNumber(incidentNumber);
		}	
		
		//Sentence Fine Amount
		String sentenceFineAmount = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeSentence/jxdm50:SentenceCondition/nc30:ConditionDisciplinaryAction/nc30:DisciplinaryActionFee/nc30:ObligationDueAmount/nc30:Amount");
		
		if (StringUtils.isNotBlank(sentenceFineAmount))
		{
			log.debug("Disposition sentence fine amount: " + sentenceFineAmount);
			disposition.setSentenceFineAmount(Float.valueOf(sentenceFineAmount));
		}	
		
		//Disposition Date
		String dispositionDateAsString = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeDisposition/nc30:DispositionDate/nc30:Date");
	
		if (StringUtils.isNotEmpty(dispositionDateAsString))
		{
			log.debug("Disposition date as string: " + dispositionDateAsString);
			Date dispositionDate = DATE_FORMAT.parse(dispositionDateAsString);
			disposition.setDispositionDate(dispositionDate);
		}	

		//Sentence term total days
		String termTotalDays = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeSentence/jxdm50:SentenceTerm/disp_ext:SentenceTermAugmentation/disp_ext:TermTotalDays");
		
		if (StringUtils.isNotBlank(termTotalDays))
		{
			log.debug("Disposition sentence term total days: " + termTotalDays);
			disposition.setSentenceTermDays(Integer.valueOf(termTotalDays));
		}	
		
		//Person info
		Node personNode = XmlUtils.xPathNodeSearch(report, "/disp_exc:DispositionReport/jxdm50:Subject/nc30:RoleOfPerson");
        int personPk = savePerson(personNode, OjbcNamespaceContext.NS_PREFIX_NC_30, OjbcNamespaceContext.NS_PREFIX_JXDM_50);
        disposition.setPersonID(personPk);
        
        //For now, disposition records are all consider new
        disposition.setRecordType('N');
        
        String isProbationIndicator = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:InitialCharge/disp_ext:ChargeAugmentation/disp_ext:ProbationViolationIndicator");
        log.debug("Is probation indicator: " + isProbationIndicator);
        
        disposition.setIsProbationViolation(DaoUtils.getIndicatorValueForDatabase(isProbationIndicator));
        
        //TODO: isProbationViolationOnOldCharge, 
        
        //TODO: updated when mapping is available from IEPD RecidivismEligibilityDate
        //Date recidivismEligibilityDate = new Date();
        //disposition.setRecidivismEligibilityDate(recidivismEligibilityDate);
        
        
        //TODO: Update when mapping is available from IEPD
        Integer dispositionTypePk = descriptionCodeLookupService.retrieveCode(CodeTable.DispositionType, "Disposition Type Placeholder");
        log.debug("Disposition type PK: " + dispositionTypePk);
        disposition.setDispositionTypeID(dispositionTypePk);
        
        //TODO: Update when mapping is available from IEPD
        Integer offenseTypePk = descriptionCodeLookupService.retrieveCode(CodeTable.OffenseType, "Violation of a Court Order");
        log.debug("Offense type PK: " + offenseTypePk);
        disposition.setOffenseTypeID(offenseTypePk);
        
        analyticalDatastoreDAO.saveDisposition(disposition);
        
        
	}

}
