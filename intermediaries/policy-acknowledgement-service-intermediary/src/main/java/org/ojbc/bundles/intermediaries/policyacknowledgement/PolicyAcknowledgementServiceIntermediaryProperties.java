package org.ojbc.bundles.intermediaries.policyacknowledgement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "policy-acknowledgement-service")
public class PolicyAcknowledgementServiceIntermediaryProperties {
	private String dbAuditLog = "false";

	public String getDbAuditLog() {
		return dbAuditLog;
	}

	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	} 
}
