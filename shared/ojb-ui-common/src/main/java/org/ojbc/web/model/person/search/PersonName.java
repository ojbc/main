package org.ojbc.web.model.person.search;

import java.io.Serializable;

import org.ojbc.person.search.SearchFieldMetadata;

public class PersonName implements Serializable {
    private static final long serialVersionUID = 8438903157636288887L;
    private String givenName;
    private SearchFieldMetadata givenNameMetaData;
    private String middleName;
    private String surName;
    private SearchFieldMetadata surNameMetaData;

    public PersonName() {
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public SearchFieldMetadata getGivenNameMetaData() {
        return givenNameMetaData;
    }

    public void setGivenNameMetaData(SearchFieldMetadata givenNameMetaData) {
        this.givenNameMetaData = givenNameMetaData;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public SearchFieldMetadata getSurNameMetaData() {
        return surNameMetaData;
    }

    public void setSurNameMetaData(SearchFieldMetadata surNameMetaData) {
        this.surNameMetaData = surNameMetaData;
    }
}