package org.ojbc.util.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.saml.ext.AssertionWrapper;
import org.apache.ws.security.validate.Credential;
import org.apache.ws.security.validate.SamlAssertionValidator;
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
		
		AssertionWrapper assertion = credential.getAssertion();
		
		if (assertion == null)
		{
			log.error("Error: Unable to find assertion.");
			throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
		}	
		
		//Confirm that the assertion is signed, the framework confirms the validity of the signature
		if (!assertion.isSigned())
		{
			log.error("Error: Assertion is not signed.");
			throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
		}

			
		return credential;
	}
	
    /**
     * Check the Conditions of the Assertion.
     */
    protected void checkConditions(AssertionWrapper assertion) throws WSSecurityException {
    	
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
                throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
            }
        }

        if (validTill != null && validTill.isBeforeNow()) {
            log.info("SAML Token condition (Not On Or After) not met.  We catch this error and don't throw an exception");
            //throw new WSSecurityException(WSSecurityException.FAILURE, "invalidSAMLsecurity");
        }
    }

}
