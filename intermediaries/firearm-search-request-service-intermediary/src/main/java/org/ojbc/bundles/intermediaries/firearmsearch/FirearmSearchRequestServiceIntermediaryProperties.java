package org.ojbc.bundles.intermediaries.firearmsearch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "firearm-search-request")
public class FirearmSearchRequestServiceIntermediaryProperties {
	private String dbAuditLog = "false";

	public String getDbAuditLog() {
		return dbAuditLog;
	}

	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	} 
}
