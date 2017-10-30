package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueryRequest {

	private String identificationId;
	private String identificationSourceText;
	private Integer userInfofk;
	private String messageId;
	
	public String getIdentificationId() {
		return identificationId;
	}
	public void setIdentificationId(String identificationId) {
		this.identificationId = identificationId;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getIdentificationSourceText() {
		return identificationSourceText;
	}
	public void setIdentificationSourceText(String identificationSourceText) {
		this.identificationSourceText = identificationSourceText;
	}
	public Integer getUserInfofk() {
		return userInfofk;
	}
	public void setUserInfofk(Integer userInfofk) {
		this.userInfofk = userInfofk;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
}
