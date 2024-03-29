/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.web.model.person.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.SearchFieldMetadata;

public class PersonName implements Serializable {
    private static final long serialVersionUID = 8438903157636288887L;
    private String givenName;
    private SearchFieldMetadata givenNameMetaData;
    private String middleName;
    private String surName;
    private SearchFieldMetadata surNameMetaData;

    public PersonName() {
    	super();
    }

    public PersonName(String givenName, String middleName, String surName) {
    	this();
    	this.givenName = givenName; 
    	this.middleName = middleName; 
    	this.surName = surName; 
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
    
    /**
     * @return givenName middleName surName separated by a space. 
     */
    public String getFullName(){
    	return Arrays.asList(givenName, middleName, surName).stream()
    			.filter(name -> StringUtils.isNotBlank(name))
    			.collect(Collectors.joining(" "));
    }
    
    public boolean isNotEmpty(){
    	return StringUtils.isNotBlank(givenName) && StringUtils.isNotBlank(surName);
    }
}
