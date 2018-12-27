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


import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.portal.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DispositionValidator implements Validator {
	@Resource
	AppProperties appProperties;

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
        
        if (disposition.getSuspendedDays() != null && disposition.getSuspendedDays() > 0 ) {
        	
        	int jailDays = Optional.ofNullable(disposition.getJailYears()).map(i->i*360).orElse(0)
        			+ Optional.ofNullable(disposition.getJailDays()).map(i->i.intValue()).orElse(0)
        			- Optional.ofNullable(disposition.getSuspendedYears()).map(i->i*360).orElse(0); 
        	if (disposition.getSuspendedDays() > jailDays) {
        		errors.rejectValue("suspendedDays", null, "may not be greater than Jail Days");
        	}
        }
        
        if (appProperties.getDispoCodesRequiringSentence().contains(disposition.getDispositionCode()) && !disposition.containsSentenceInfo()) {
        	errors.rejectValue("dispositionCode", null, "the dispo code requires sentence info");
        }
        
        if (appProperties.getDispoCodesRequiringAmendedCharge().contains(disposition.getDispositionCode()) && StringUtils.isBlank(disposition.getAmendedCharge())) {
        	errors.rejectValue("amendedCharge", null, "the dispo code requires an amended charge");
        }
    }
}