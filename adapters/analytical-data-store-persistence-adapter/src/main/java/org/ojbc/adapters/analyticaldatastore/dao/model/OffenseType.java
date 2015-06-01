package org.ojbc.adapters.analyticaldatastore.dao.model;

public class OffenseType {

	//pk
    private int offenseTypeID;
    
    private String offenseDescription;
    
    private String isDrugOffense;
    
    private String offenseSeverity;

	public int getOffenseTypeID() {
		return offenseTypeID;
	}

	public void setOffenseTypeID(int offenseTypeID) {
		this.offenseTypeID = offenseTypeID;
	}

	public String getOffenseDescription() {
		return offenseDescription;
	}

	public void setOffenseDescription(String offenseDescription) {
		this.offenseDescription = offenseDescription;
	}

	public String getIsDrugOffense() {
		return isDrugOffense;
	}

	public void setIsDrugOffense(String isDrugOffense) {
		this.isDrugOffense = isDrugOffense;
	}

	public String getOffenseSeverity() {
		return offenseSeverity;
	}

	public void setOffenseSeverity(String offenseSeverity) {
		this.offenseSeverity = offenseSeverity;
	}
}
