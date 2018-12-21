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