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
package org.ojbc.web.portal.arrest;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.util.helper.ArrayUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.portal.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DispositionValidator implements Validator {
	@Resource
	AppProperties appProperties;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    /**
     * This Validator validates *only* Person instances
     */
    public boolean supports(Class<?> clazz) {
        return Disposition.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        Disposition disposition = (Disposition) obj;
        
        if (disposition.getFineSuspended() != null && disposition.getFineSuspended() > 0 && 
        		(disposition.getFineAmount() == null || disposition.getFineSuspended() > disposition.getFineAmount() )) {
        	errors.rejectValue("fineSuspended", null, "may not be greater than Fine Amount");
        }
        
        if (disposition.getDispositionType() == ArrestType.DA && !"390".equals(disposition.getDispositionCode())) {
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "caseType", null, "may not be empty");
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "year", null, "may not be empty");
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "caseNumber", null, "may not be empty");
        	
        	if (StringUtils.isNotBlank(disposition.getYear())) {
        		if ( disposition.getYear().length() < 4) {
        			errors.rejectValue("year", null, "must be 4-digit");
        		}
        		else if (Integer.valueOf(disposition.getYear()) > LocalDate.now().getYear()) {
        			errors.rejectValue("year", null, "may not be greater than current year");
        		}
        	}
        	
        	if (StringUtils.isNotBlank(disposition.getCaseNumber()) && Integer.valueOf(disposition.getCaseNumber()) <=1) {
        		errors.rejectValue("caseNumber", null, "must be greater than 1");
        	}
        	
    		boolean isCourtCaseNumberPartsReady = StringUtils.isNoneBlank(disposition.getCaseType(), 
    				disposition.getYear(), disposition.getCaseNumber());
    		if (isCourtCaseNumberPartsReady && "M".equals(disposition.getCaseType()) ) {
    			if ("F".equals(disposition.getChargeSeverityCode())) {
    				errors.rejectValue("chargeSeverityCode", null, "may not be Felony when case type is Misdemeanor");
    			}
    			if ("F".equals(disposition.getAmendedChargeSeverityCode())) {
    				errors.rejectValue("amendedChargeSeverityCode", null, "may not be Felony when case type is Misdemeanor");
    			}
    		}
        }
        
        validateJailSuspendedDeferredDays(disposition, errors);
        
        if (appProperties.getDispoCodesRequiringSentence().contains(disposition.getDispositionCode()) && !disposition.containsSentenceInfo()) {
        	errors.rejectValue("dispositionCode", null, "the dispo code requires sentence info");
        }
        
        if (appProperties.getDispoCodesRequiringAmendedCharge().contains(disposition.getDispositionCode()) && StringUtils.isBlank(disposition.getAmendedCharge())) {
        	errors.rejectValue("amendedCharge", null, "required by the dispo code");
        }
        
        if (disposition.getDispositionType() == ArrestType.DA) {
        	if (!(appProperties.getDispoCodesNotRequiringChargeSeverity().contains(disposition.getDispositionCode()) || 
        			Arrays.asList("T", "W").contains(disposition.getCaseType()))) {
        		if (StringUtils.isBlank(disposition.getChargeSeverityCode())) {
        			errors.rejectValue("chargeSeverityCode", null, "required by the dispo code");
        		}
        		
        		if (StringUtils.isNotBlank(disposition.getAmendedCharge()) && StringUtils.isBlank(disposition.getAmendedChargeSeverityCode())) {
        			errors.rejectValue("amendedChargeSeverityCode", null, "required by the dispo code");
        		}
        	}
        	
        	if ( ("F".equals(disposition.getChargeSeverityCode()) || "F".equals(disposition.getAmendedChargeSeverityCode())) && 
        			Arrays.asList("T", "W").contains(disposition.getCaseType())) {
        		if ("F".equals(disposition.getChargeSeverityCode())) {
        			errors.rejectValue("chargeSeverityCode", null, "invalid for the case type");
        		}
        		
        		if ("F".equals(disposition.getAmendedChargeSeverityCode())) {
        			errors.rejectValue("amendedChargeSeverityCode", null, "invalid for the case type");
        		}
        	}
        }
    }

	private void validateJailSuspendedDeferredDays(Disposition disposition, Errors errors) {
		int jailDays = Optional.ofNullable(disposition.getJailYears()).map(i->i*360).orElse(0)
				+ Optional.ofNullable(disposition.getJailDays()).map(i->i.intValue()).orElse(0); 
		int prisonDays = Optional.ofNullable(disposition.getPrisonYears()).map(i->i*360).orElse(0)
				+ Optional.ofNullable(disposition.getPrisonDays()).map(i->i.intValue()).orElse(0); 
        if (jailDays > 0 && prisonDays > 0 ) {
    		if (ArrayUtils.hasPositiveValue(disposition.getJailDays())) {
    			errors.rejectValue("jailDays", null, "may not coexist with prison time");
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getJailYears())) {
    			errors.rejectValue("jailYears", null, "may not coexist with prison time");
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getPrisonYears())) {
    			errors.rejectValue("prisonYears", null, "may not coexist with jail time");
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getPrisonDays())) {
    			errors.rejectValue("prisonDays", null, "may not coexist with jail time");
    		}
        	
        }
        
    	int suspendedDays = Optional.ofNullable(disposition.getSuspendedYears()).map(i->i*360).orElse(0)
    			+ Optional.ofNullable(disposition.getSuspendedDays()).map(i->i.intValue()).orElse(0);
    	int deferredDays = Optional.ofNullable(disposition.getDeferredYears()).map(i->i*360).orElse(0)
    			+ Optional.ofNullable(disposition.getDeferredDays()).map(i->i.intValue()).orElse(0);
    	if (disposition.getDispositionType() == ArrestType.MUNI) {
    		if (jailDays > 360) {
    			errors.rejectValue("jailYears", null, "may not be greater than 1");
    		}
    		
    		if (suspendedDays > 360) {
    			errors.rejectValue("suspendedYears", null, "may not be greater than 1");
    		}
    		
    		if (deferredDays > 360) {
    			errors.rejectValue("deferredYears", null, "may not be greater than 1");
    		}
    	}
    	
    	if (disposition.getDispositionType() == ArrestType.MUNI) {
	    	if (suspendedDays > jailDays) {
	    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays())) {
	    			errors.rejectValue("suspendedDays", null, "may not be greater than Jail time");
	    		}
	    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
	    			errors.rejectValue("suspendedYears", null, "may not be greater than Jail time");
	    		}
	    	}
    	}
    	else if (disposition.getDispositionType() == ArrestType.DA && suspendedDays > 0) {
    		
    		if (!(jailDays > 0 && prisonDays > 0)) {
		    	if (prisonDays > 0 && suspendedDays > prisonDays ) {
		    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays())) {
		    			errors.rejectValue("suspendedDays", null, "may not be greater than prison time");
		    		}
		    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
		    			errors.rejectValue("suspendedYears", null, "may not be greater than prison time");
		    		}
		    	}
		    	else if (jailDays > 0  && suspendedDays > jailDays){
		    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays())) {
		    			errors.rejectValue("suspendedDays", null, "may not be greater than Jail time");
		    		}
		    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
		    			errors.rejectValue("suspendedYears", null, "may not be greater than Jail time");
		    		}
		    	}
    		}
    	}
    	
        if (disposition.getDispositionType() == ArrestType.DA 
        		&& ArrayUtils.hasPositiveValue(disposition.getDeferredDays(), disposition.getDeferredYears())) {
        	if (ArrayUtils.hasPositiveValue(disposition.getJailYears(),disposition.getPrisonDays(), disposition.getPrisonYears()) ) {
	    		if (ArrayUtils.hasPositiveValue(disposition.getDeferredDays())) {
	    			errors.rejectValue("deferredDays", null, "must be empty when jail years or prison time are not empty");
	    		}
	    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
	    			errors.rejectValue("deferredYears", null, "must be empty when jail years or prison time are not empty");
	    		}
        	}
        	else if (jailDays > 90  && deferredDays <= 3600) {
        		errors.rejectValue("jailDays", null, "may not be greater than 90 when deferred time is not empty");
        	}
        	else if (deferredDays > 3600) {
    			errors.rejectValue("deferredDays", null, "Deferred time may not be greater than 10 years");
        	}
        }
        
        if (disposition.getDispositionType() == ArrestType.MUNI 
        		&& ArrayUtils.hasPositiveValue(disposition.getDeferredDays(), disposition.getDeferredYears()) 
        		&& ArrayUtils.hasPositiveValue(disposition.getJailYears(),disposition.getJailDays()) ) {
        	if (ArrayUtils.hasPositiveValue(disposition.getDeferredDays())) {
        		errors.rejectValue("deferredDays", null, "must be empty when jail time is not empty");
        	}
        	if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
        		errors.rejectValue("deferredYears", null, "must be empty when jail time is not empty");
        	}
        }
        
	}
}