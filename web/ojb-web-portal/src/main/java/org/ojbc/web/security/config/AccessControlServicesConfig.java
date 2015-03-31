package org.ojbc.web.security.config;

import org.ojbc.web.IdentityBasedAccessControlService;
import org.ojbc.web.PolicyBasedAcknowledgementRecordingService;

public interface AccessControlServicesConfig {
    IdentityBasedAccessControlService getIdentityBasedAccessControlServiceBean();
    PolicyBasedAcknowledgementRecordingService getPolicyBasedAcknowledgementRecordingServiceBean();
}
