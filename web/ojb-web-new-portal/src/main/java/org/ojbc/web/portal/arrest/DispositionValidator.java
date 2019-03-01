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


import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.util.helper.ArrayUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.portal.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
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
        
        if ( Objects.nonNull(disposition.getDispositionDate()) && disposition.getDispositionDate().isBefore(disposition.getArrestDate())) {
        	errors.rejectValue("dispositionDate", null, "may not be before arrest date " + disposition.getArrestDate().format(formatter));
        }
        
        if (disposition.getFineSuspended() != null && disposition.getFineSuspended() > 0 && 
        		(disposition.getFineAmount() == null || disposition.getFineSuspended() > disposition.getFineAmount() )) {
        	errors.rejectValue("fineSuspended", null, "may not be greater than Fine Amount");
        }
        
        validateJailSuspendedDeferredDays(disposition, errors);
        
        if (appProperties.getDispoCodesRequiringSentence().contains(disposition.getDispositionCode()) && !disposition.containsSentenceInfo()) {
        	errors.rejectValue("dispositionCode", null, "the dispo code requires sentence info");
        }
        
        if (appProperties.getDispoCodesRequiringAmendedCharge().contains(disposition.getDispositionCode()) && StringUtils.isBlank(disposition.getAmendedCharge())) {
        	errors.rejectValue("amendedCharge", null, "the dispo code requires an amended charge");
        }
    }

	private void validateJailSuspendedDeferredDays(Disposition disposition, Errors errors) {
        if (ArrayUtils.hasPositiveValue(disposition.getJailDays(), disposition.getJailYears()) 
        		&& ArrayUtils.hasPositiveValue(disposition.getPrisonDays(), disposition.getPrisonYears()) ) {
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
        if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays(), disposition.getSuspendedYears()) ) {
        	
        	int jailDays = Optional.ofNullable(disposition.getJailYears()).map(i->i*360).orElse(0)
        			+ Optional.ofNullable(disposition.getJailDays()).map(i->i.intValue()).orElse(0); 
        	int suspendedDays = Optional.ofNullable(disposition.getSuspendedYears()).map(i->i*360).orElse(0)
        			+ Optional.ofNullable(disposition.getSuspendedDays()).map(i->i.intValue()).orElse(0);
        	if (suspendedDays > jailDays) {
        		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedDays())) {
        			errors.rejectValue("suspendedDays", null, "may not be greater than Jail time");
        		}
        		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
        			errors.rejectValue("suspendedYears", null, "may not be greater than Jail time");
        		}
        	}
        }
        
        if (disposition.getDispositionType() == ArrestType.DA 
        		&& ArrayUtils.hasPositiveValue(disposition.getDeferredDays(), disposition.getDeferredYears()) 
        		&& ArrayUtils.hasPositiveValue(disposition.getJailYears(),disposition.getPrisonDays(), disposition.getPrisonYears()) ) {
    		if (ArrayUtils.hasPositiveValue(disposition.getDeferredDays())) {
    			errors.rejectValue("deferredDays", null, "must be empty when jail years or prison time are not empty");
    		}
    		if (ArrayUtils.hasPositiveValue(disposition.getSuspendedYears())) {
    			errors.rejectValue("deferredYears", null, "must be empty when jail years or prison time are not empty");
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