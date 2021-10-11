package org.ojbc.bundles.intermediaires.personquery;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "person-query-service")
public class PersonQueryServiceCannabisIntermediaryProperties {
	private String dbAuditLog = "false";

	public String getDbAuditLog() {
		return dbAuditLog;
	}

	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	} 
}
