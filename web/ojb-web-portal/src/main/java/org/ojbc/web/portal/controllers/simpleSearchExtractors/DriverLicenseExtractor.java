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
package org.ojbc.web.portal.controllers.simpleSearchExtractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseExtractor extends SearchTermExtractorBase {

	private final Log logger = LogFactory.getLog(this.getClass());
	
    private Pattern pattern;
    
    private String defaultStateOfIssue;       
    
    public DriverLicenseExtractor() {
    }
    
    public void setDriversLicenseRegex(String driversLicenseRegex)
    {
        pattern = Pattern.compile(driversLicenseRegex);
    }
    
    public void setDefaultStateOfIssue(String defaultStateOfIssue)
    {
        this.defaultStateOfIssue = defaultStateOfIssue;
    }

    @Override
    protected boolean extractTermLocal(String token, PersonSearchRequest personSearchRequest) {
    	
        Matcher matcher = pattern.matcher(token);
        
        if (matcher.matches()) {
        	
            if (matcher.groupCount() != 3) {
                throw new IllegalStateException("Drivers License Extractors must have three regex groups");
            }
            
            if (matcher.group(1) != null) {            	
                personSearchRequest.setPersonDriversLicenseIssuer(matcher.group(1).substring(0, 2));
                personSearchRequest.setPersonDriversLicenseNumber(matcher.group(2));
                
            } else {            	
                personSearchRequest.setPersonDriversLicenseIssuer(defaultStateOfIssue);
                personSearchRequest.setPersonDriversLicenseNumber(token);
            }
            return true;
        }
        
        logger.debug("DriverLicenseExtractor.extractTermLocal() returning false (no match)");
        
        return false;

    }

}
