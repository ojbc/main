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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
		
		//XmlUtils.printNode(report);
		
		Disposition disposition = new Disposition();

		//Incident Number
		String incidentNumber = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Incident/nc30:ActivityIdentification/nc30:IdentificationID");
				
		if (StringUtils.isNotBlank(incidentNumber))
		{
			log.debug("Disposition incident number: " + incidentNumber);
			disposition.setIncidentCaseNumber(incidentNumber);
		}	
		
		String docketId = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseHearing/nc30:ActivityIdentification/nc30:IdentificationID");
		String count = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:InitialCharge/jxdm50:ChargeCountQuantity");
		
		String docketChargeNumber = "";
		
		if (StringUtils.isNotBlank(docketId) && StringUtils.isNotBlank(count))
		{
			docketChargeNumber = docketId + "|" + count;
			log.debug("Setting docket charge number to: " + docketChargeNumber);
			disposition.setDocketChargeNumber(docketChargeNumber);
		}	
		
		if (StringUtils.isNotEmpty(docketChargeNumber))
		{	
			List<Disposition> dispositions = analyticalDatastoreDAO.searchForDispositionsByDocketChargeNumber(docketChargeNumber);
			
			if (dispositions.size() > 1)
			{
				throw new Exception("Database has more than one disposition with the same docket charge number: " + docketChargeNumber);
			}	
			
			if (dispositions.size() == 1)
			{
				analyticalDatastoreDAO.deleteDisposition(dispositions.get(0).getDispositionID());
				disposition.setDispositionID(dispositions.get(0).getDispositionID());
			}	
			
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
		String termTotalDaysAsString = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeSentence/jxdm50:SentenceTerm/disp_ext:SentenceTermAugmentation/disp_ext:TermTotalDays");
		
		if (StringUtils.isNotBlank(termTotalDaysAsString))
		{
			log.debug("Disposition sentence term total days: " + termTotalDaysAsString);
			
			//Check if value is integer.  If it is, append .00 to match precision in database
			if (isStringInt(termTotalDaysAsString))
			{
				termTotalDaysAsString = termTotalDaysAsString + ".00";
			}	
			
			BigDecimal termTotalDays = new BigDecimal(termTotalDaysAsString);
			disposition.setSentenceTermDays(termTotalDays);
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
        
        String isProbationViolationOnOldCharge = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:InitialCharge/disp_ext:ChargeAugmentation/disp_ext:IsProbationViolationOnOldCharge");
        log.debug("Is probation violation on old charge: " + isProbationViolationOnOldCharge);
        
        disposition.setIsProbationViolationOnOldCharge(DaoUtils.getIndicatorValueForDatabase(isProbationViolationOnOldCharge));
        
		String arrestingAgencyORI = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/jxdm50:Arrest/jxdm50:ArrestAgency/nc30:OrganizationIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(arrestingAgencyORI))
		{
			log.debug("Arresting Agency ORI: " + arrestingAgencyORI);
			disposition.setArrestingAgencyORI(arrestingAgencyORI);
		}	

        
        //RecidivismEligibilityDate
		String recidivismEligibilityDateAsString = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeSentence/jxdm50:SentenceTerm/disp_ext:SentenceTermAugmentation/disp_ext:RecidivismEligibilityDate/nc30:Date");
		
		if (StringUtils.isNotEmpty(recidivismEligibilityDateAsString))
		{
			log.debug("Recidivism Eligibility date as string: " + recidivismEligibilityDateAsString);
			Date recidivismEligibilityDate = DATE_FORMAT.parse(recidivismEligibilityDateAsString);
			disposition.setRecidivismEligibilityDate(recidivismEligibilityDate);
		}	
        
        String courtDispositionCode = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeDisposition/disp_ext:ChargeDispositionAugmentation/ojbc_disp_codes:CourtDispositionCode");
        
        Integer dispositionTypePk = descriptionCodeLookupService.retrieveCode(CodeTable.DispositionType, courtDispositionCode);
        log.debug("Disposition type PK: " + dispositionTypePk);
        disposition.setDispositionTypeID(dispositionTypePk);
        
        //Add initial charge code and final charge code
        String initialChargeCode = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:InitialCharge/jxdm50:ChargeStatute/jxdm50:StatuteCodeIdentification/nc30:IdentificationID");
        disposition.setInitialChargeCode(initialChargeCode);
        log.debug("Initial Charge Code: " + initialChargeCode);
        
        String finalChargeCode = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/jxdm50:ChargeStatute/jxdm50:StatuteCodeIdentification/nc30:IdentificationID");
        disposition.setFinalChargeCode(finalChargeCode);
        log.debug("Final Charge Code: " + finalChargeCode);
        
        //Add initial charge rank and final charge rank
        String initialChargeRank = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:InitialCharge/disp_ext:ChargeAugmentation/disp_ext:ChargeNumericalRank");
        disposition.setInitialChargeRank(initialChargeRank);
        log.debug("Initial Charge Rank: " + initialChargeRank);
        
        String finalChargeRank = XmlUtils.xPathStringSearch(report, "/disp_exc:DispositionReport/nc30:Case/jxdm50:CaseAugmentation/jxdm50:CaseCharge/disp_ext:ChargeAugmentation/disp_ext:FinalCharge/disp_ext:ChargeAugmentation/disp_ext:ChargeNumericalRank");
        disposition.setFinalChargeRank(finalChargeRank);
        log.debug("Final Charge Rank: " + finalChargeRank);
        
        analyticalDatastoreDAO.saveDisposition(disposition);
        
	}

	private boolean isStringInt(String s)
	{
	    try
	    {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException ex)
	    {
	        return false;
	    }
	}
	
}
