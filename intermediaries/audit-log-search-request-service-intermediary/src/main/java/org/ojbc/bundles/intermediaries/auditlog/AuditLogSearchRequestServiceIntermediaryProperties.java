package org.ojbc.bundles.intermediaries.auditlog;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "audit-log-search")
public class AuditLogSearchRequestServiceIntermediaryProperties {
	private String dbAuditLog = "false";

	public String getDbAuditLog() {
		return dbAuditLog;
	}

	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	} 
}
