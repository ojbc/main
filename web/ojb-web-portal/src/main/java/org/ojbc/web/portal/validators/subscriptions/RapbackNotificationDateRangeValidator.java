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
package org.ojbc.web.portal.validators.subscriptions;

import java.time.LocalDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class RapbackNotificationDateRangeValidator implements Validator{
	
	private final Log log = LogFactory.getLog(this.getClass());	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return QueryRequestByDateRange.class.equals(clazz);
	}


	@Override
	public void validate(Object target, Errors errors) {
		QueryRequestByDateRange dateRange = (QueryRequestByDateRange) target; 
		log.info("Validating rapback notification date range " + dateRange);

		ValidationUtils.rejectIfEmpty(errors, "startDate", null,  "Must be specified");
		ValidationUtils.rejectIfEmpty(errors, "endDate", null, "Must be specified");
		
		if(dateRange.getEndDate() != null && dateRange.getStartDate() != null){		
			if (dateRange.getEndDate().isAfter(LocalDate.now())){
				errors.rejectValue("endDate", null,"End Date may not occur after today's date");
			}
			if(dateRange.getEndDate().isBefore(dateRange.getStartDate())){
				errors.rejectValue("startDate", null, "Start Date may not occur after End Date");
			}									
		}
	}
}


