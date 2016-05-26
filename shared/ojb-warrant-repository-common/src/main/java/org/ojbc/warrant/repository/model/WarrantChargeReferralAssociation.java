package org.ojbc.warrant.repository.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WarrantChargeReferralAssociation {

	private Integer WarrantChargeReferralAssociationID;
	
	private Integer ChargeReferralID;
	
	private Integer WarrantID;

	public Integer getWarrantChargeReferralAssociationID() {
		return WarrantChargeReferralAssociationID;
	}

	public void setWarrantChargeReferralAssociationID(
			Integer warrantChargeReferralAssociationID) {
		WarrantChargeReferralAssociationID = warrantChargeReferralAssociationID;
	}

	public Integer getChargeReferralID() {
		return ChargeReferralID;
	}

	public void setChargeReferralID(Integer chargeReferralID) {
		ChargeReferralID = chargeReferralID;
	}

	public Integer getWarrantID() {
		return WarrantID;
	}

	public void setWarrantID(Integer warrantID) {
		WarrantID = warrantID;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
