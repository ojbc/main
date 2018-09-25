package org.ojbc.web.portal.arrest;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DispositionValidator implements Validator {

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
        
        if (disposition.getSuspendedDays() != null && disposition.getSuspendedDays() > 0 && 
        		(disposition.getJailDays() == null || disposition.getSuspendedDays() > disposition.getJailDays())) {
        	errors.rejectValue("suspendedDays", null, "may not be greater than Jail Days");
        }
        	
    }
}