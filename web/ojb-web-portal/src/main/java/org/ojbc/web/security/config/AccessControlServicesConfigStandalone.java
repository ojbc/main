package org.ojbc.web.security.config;

import javax.annotation.Resource;

import org.ojbc.web.IdentityBasedAccessControlService;
import org.ojbc.web.PolicyBasedAcknowledgementRecordingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"standalone"})
public class AccessControlServicesConfigStandalone implements
		AccessControlServicesConfig {
	
	@Resource
	IdentityBasedAccessControlService identityBasedAccessControlService;

	@Resource 
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
