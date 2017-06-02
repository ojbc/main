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
package org.ojbc.util.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;

/**
 * This sample validator only assures that there is an assertion that is signed.  It does not test against the CTF.
 * 
 * @author yogesh chawla
 *
 */

public class OJBSimpleWSPSamlTokenValidator extends SamlAssertionValidator{

	private static final Log log = LogFactory.getLog(OJBSimpleWSPSamlTokenValidator.class);
	
	@Override
	public Credential validate(Credential credential, RequestData data)
			throws WSSecurityException {
		
		super.validate(credential, data);
		
		log.debug("Entering OJB saml assertion validator");
		
		SamlAssertionWrapper assertion = credential.getSamlAssertion();
		
		if (assertion == null)
		{
			log.error("Error: Unable to find assertion.");
			throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, "invalidSAMLsecurity");
		}	
		
		//Confirm that the assertion is signed, the framework confirms the validity of the signature
		if (!assertion.isSigned())
		{
			log.error("Error: Assertion is not signed.");
			throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, "invalidSAMLsecurity");
		}

			
		return credential;
	}
	
    /**
     * Check the Conditions of the Assertion.
     */
    protected void checkConditions(SamlAssertionWrapper assertion) throws WSSecurityException {
    	
    	log.info("Entering OJB custom check conditions method.");
    	
        DateTime validFrom = null;
        DateTime validTill = null;
        if (assertion.getSamlVersion().equals(SAMLVersion.VERSION_20)
            && assertion.getSaml2().getConditions() != null) {
            validFrom = assertion.getSaml2().getConditions().getNotBefore();
            validTill = assertion.getSaml2().getConditions().getNotOnOrAfter();
        } else if (assertion.getSamlVersion().equals(SAMLVersion.VERSION_11)
            && assertion.getSaml1().getConditions() != null) {
            validFrom = assertion.getSaml1().getConditions().getNotBefore();
            validTill = assertion.getSaml1().getConditions().getNotOnOrAfter();
        }
        
        if (validFrom != null) {
            DateTime currentTime = new DateTime();
            
            //We hardcode future time to live at 60 here, it is configurable in the super class
            currentTime = currentTime.plusSeconds(60);
            if (validFrom.isAfter(currentTime)) {
                log.debug("SAML Token condition (Not Before) not met");
                throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, "invalidSAMLsecurity");
            }
        }

        if (validTill != null && validTill.isBeforeNow()) {
            log.info("SAML Token condition (Not On Or After) not met.  We catch this error and don't throw an exception");
            //throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
        }
    }

}
