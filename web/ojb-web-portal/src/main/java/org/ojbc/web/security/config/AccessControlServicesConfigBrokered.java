package org.ojbc.web.security.config;

import javax.annotation.Resource;

import org.ojbc.web.IdentityBasedAccessControlService;
import org.ojbc.web.PolicyBasedAcknowledgementRecordingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "policy-acknowledgement", "access-control"})
public class AccessControlServicesConfigBrokered implements
		AccessControlServicesConfig {
	
	@Resource (name="identityBasedAccessControlRequestProcessor") 
	IdentityBasedAccessControlService identityBasedAccessControlService;

	@Resource (name="policyAcknowledgingRequestProcessor") 
	PolicyBasedAcknowledgementRecordingService policyBasedAcknowledgementRecordingService;

	@Override
    public IdentityBasedAccessControlService getIdentityBasedAccessControlServiceBean() {
	    return identityBasedAccessControlService; 
	}
	
	@Override
    public PolicyBasedAcknowledgementRecordingService getPolicyBasedAcknowledgementRecordingServiceBean() {
        return policyBasedAcknowledgementRecordingService; 
    }
}
