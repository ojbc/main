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
package org.ojbc.web.portal.validators;

import org.ojbc.web.portal.controllers.dto.PersonFilterCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class PersonFilterCommandValidator {
	
	public void validate(PersonFilterCommand PersonFilterCommand, BindingResult errors) {
		
		if(hasAgeRange(PersonFilterCommand) ){
			Integer startAge = PersonFilterCommand.getFilterAgeRangeStart();
			Integer endAge = PersonFilterCommand.getFilterAgeRangeEnd();
			
			if(startAge != null  && startAge < 0){
				errors.rejectValue("filterAgeRangeStart","startAgeInvalid", "from age in invalid");
			}			
			if(startAge == null  && endAge!=null){
				errors.rejectValue("filterAgeRangeStart","startAgeMissing", "from age missing");
			}	
			if(startAge != null  && endAge==null){
				errors.rejectValue("filterAgeRangeStart","endAgeMissing", "to age missing");
			}							
			if(startAge!= null  && endAge!=null && startAge > endAge ){
				errors.rejectValue("filterAgeRangeStart","endAgeBeforeStart", "from age must be after to age");				
			}			
		}
		
		if(hasHeightRange(PersonFilterCommand) ){
			Integer heightFeet = PersonFilterCommand.getFilterHeightInFeet();
			Integer heightInches = PersonFilterCommand.getFilterHeightInInches();
			Integer heightTolerance = PersonFilterCommand.getFilterHeightTolerance();

			if(heightFeet == null  || heightFeet < 1){
				errors.rejectValue("filterHeightInFeet","filterHeightInFeetInvalid", "height in feet is invalid");
			}	
			if(heightInches == null  || heightInches < 0 ||  heightInches > 11 ){
				errors.rejectValue("filterHeightInFeet","filterHeightInInchesInvalid", "height in inches is invalid");
			}
			if(heightTolerance == null  || heightTolerance < 0){
				errors.rejectValue("filterHeightInFeet","filterHeightToleranceInvalid", "height +/- is invalid");
			}									
			if(heightFeet!=null  && heightInches!=null && heightTolerance!=null ){
				if (  ((heightFeet * 12) + heightInches - heightTolerance) < 1 ) {
					errors.rejectValue("filterHeightInFeet","filterHeightToleranceExcessive", "height +/- is excessive");					
				}				
			}			
		}
		
		if(hasWeightRange(PersonFilterCommand) ){
			Integer weight = PersonFilterCommand.getFilterWeight();
			Integer weightTolerance = PersonFilterCommand.getFilterWeightTolerance();
									
			if(weight == null  || weight < 1){
				errors.rejectValue("filterWeight","filterWeightInvalid", "weight is invalid");
			}	
			if(weightTolerance == null  || weightTolerance < 0){
				errors.rejectValue("filterWeight","filterWeightToleranceInvalid", "weight +/- is invalid");
			}									
			if(weight!=null   && weightTolerance!=null ){
				if ( weight - weightTolerance  < 1 ) {
					errors.rejectValue("filterWeight","filterWeightToleranceExcessive", "weight +/- is excessive");					
				}				
			}			
		}	
		
	}
	
	

	private boolean hasAgeRange(PersonFilterCommand PersonFilterCommand) {
		return PersonFilterCommand.getFilterAgeRangeStart() != null || PersonFilterCommand.getFilterAgeRangeEnd() != null;
	}
	private boolean hasHeightRange(PersonFilterCommand PersonFilterCommand) {
		return PersonFilterCommand.getFilterHeightInFeet() != null || 
				PersonFilterCommand.getFilterHeightInInches() != null ||
				PersonFilterCommand.getFilterHeightTolerance() != null;
	}	
	private boolean hasWeightRange(PersonFilterCommand PersonFilterCommand) {
		return PersonFilterCommand.getFilterWeight() != null || 
				PersonFilterCommand.getFilterWeightTolerance() != null;
	}	
}
