package org.ojbc.bundles.intermediaires.criminalhistory.searchrequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "criminal-history-search-request")
public class CriminalHistorySearchRequestServiceProperties {
	private String dbAuditLog = "false";

	public String getDbAuditLog() {
		return dbAuditLog;
	}

	public void setDbAuditLog(String dbAuditLog) {
		this.dbAuditLog = dbAuditLog;
	} 
}
