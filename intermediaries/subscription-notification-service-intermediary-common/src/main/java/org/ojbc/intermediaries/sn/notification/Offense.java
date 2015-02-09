package org.ojbc.intermediaries.sn.notification;


public class Offense {

    private String fbiNdexCode;
    private String offenseDescriptionText;
    private String offenseCategoryText;

    public String getFbiNdexCode() {
        return fbiNdexCode;
    }

    public void setFbiNdexCode(String fbiNdexCode) {
        this.fbiNdexCode = fbiNdexCode;
    }

    public String getOffenseDescriptionText() {
        return offenseDescriptionText;
    }

    public void setOffenseDescriptionText(String offenseDescriptionText) {
        this.offenseDescriptionText = offenseDescriptionText;
    }

    public String getOffenseCategoryText() {
        return offenseCategoryText;
    }

    public void setOffenseCategoryText(String offenseCategoryText) {
        this.offenseCategoryText = offenseCategoryText;
    }

}
