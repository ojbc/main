package org.ojbc.audit.enhanced.dao.model;

public class PrintResults {

	private Integer printResultsId;
	private String messageId;
	private String systemName;
	private String description;
	
	public Integer getPrintResultsId() {
		return printResultsId;
	}
	public void setPrintResultsId(Integer printResultsId) {
		this.printResultsId = printResultsId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
