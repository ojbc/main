package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantRemarks {

    private Integer warrantRemarksID;
    
    private Integer warrantID;
    
    private String warrantRemarkText;

	public Integer getWarrantRemarksID() {
		return warrantRemarksID;
	}

	public void setWarrantRemarksID(Integer warrantRemarksID) {
		this.warrantRemarksID = warrantRemarksID;
	}

	public Integer getWarrantID() {
		return warrantID;
	}

	public void setWarrantID(Integer warrantID) {
		this.warrantID = warrantID;
	}

	public String getWarrantRemarkText() {
		return warrantRemarkText;
	}

	public void setWarrantRemarkText(String warrantRemarkText) {
		this.warrantRemarkText = warrantRemarkText;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
