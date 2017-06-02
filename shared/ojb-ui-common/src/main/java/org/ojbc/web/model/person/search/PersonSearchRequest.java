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
import java.lang.reflect.Field;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.web.OjbcWebConstants;
import org.ojbc.web.SearchFieldMetadata;


public class PersonSearchRequest implements Serializable{

    private static final long serialVersionUID = 4875534327596344809L;
    
    @Valid
    private PersonName personName = new PersonName();
    private PersonName alias = new PersonName();
    private PersonName parentName = new PersonName(); 
    private Address address = new Address();
    
    private String placement; 
    //Demographics
	private DateTime personDateOfBirth;
	private DateTime personDateOfBirthRangeStart;
	private DateTime personDateOfBirthRangeEnd;
	private String personRaceCode;
	private String personSexCode;
	private String personEyeColor;
	private String personHairColor;

	private Integer personWeight;
	private Integer personWeightRangeStart;
	private Integer personWeightRangeEnd;

	private Integer personHeightTotalInches;
	private Integer personHeightTotalInchesRangeStart;
	private Integer personHeightTotalInchesRangeEnd;
	
	//Identifiers
	@Pattern(regexp=OjbcWebConstants.SSN_REGEX)
	private String personSocialSecurityNumber;
	@Pattern(regexp="[A-Za-z0-9 -]*")
	private String personDriversLicenseNumber;
	@Pattern(regexp="[A-Za-z0-9 -]*")
	private String personDriversLicenseIssuer;
	private String personSID;
	@Pattern(regexp=OjbcWebConstants.FBI_ID_REGEX)
	private String personFBINumber;
	
	//Logging
	private String purpose;
	private String onBehalfOf;
	
	private List<String> sourceSystems;
	
	public String getPersonHairColor() {
		return personHairColor;
	}
	public void setPersonHairColor(String personHairColor) {
		this.personHairColor = personHairColor;
	}
	public String getPersonEyeColor() {
		return personEyeColor;
	}
	public void setPersonEyeColor(String personEyeColor) {
		this.personEyeColor = personEyeColor;
	}
	
    @Pattern(regexp="^([a-zA-Z]+[- '])*['a-zA-Z]*+[*]?$")
	public String getPersonGivenName() {
		return personName.getGivenName();
	}
	public void setPersonGivenName(String personGivenName) {
		this.personName.setGivenName(StringEscapeUtils.escapeHtml(personGivenName)) ;
	}
    @Pattern(regexp="^([a-zA-Z]+[- '])*['a-zA-Z]*+[*]?$")
	public String getPersonMiddleName() {
		return personName.getMiddleName();
	}
	public void setPersonMiddleName(String personMiddleName) {
		this.personName.setMiddleName(StringEscapeUtils.escapeHtml(personMiddleName));
	}
	
    @Pattern(regexp="^([a-zA-Z]+[- '])*['a-zA-Z]*+[*]?$")
	public String getPersonSurName() {
		return personName.getSurName();
	}
	public void setPersonSurName(String personSurName) {
		this.personName.setSurName(StringEscapeUtils.escapeHtml(personSurName));
	}
	public String getPersonSexCode() {
		return personSexCode;
	}
	public void setPersonSexCode(String personSexCode) {
		this.personSexCode = personSexCode;
	}
	public String getPersonRaceCode() {
		return personRaceCode;
	}
	public void setPersonRaceCode(String personRaceCode) {
		this.personRaceCode = personRaceCode;
	}
	
	public String getPersonSocialSecurityNumber() {
		return personSocialSecurityNumber;
	}
	public void setPersonSocialSecurityNumber(String personSocialSecurityNumber) {
		this.personSocialSecurityNumber = StringEscapeUtils.escapeHtml(personSocialSecurityNumber);
	}
	public String getPersonDriversLicenseNumber() {
		return personDriversLicenseNumber;
	}
	public void setPersonDriversLicenseNumber(String personDriversLicenseNumber) {
		this.personDriversLicenseNumber = StringEscapeUtils.escapeHtml(personDriversLicenseNumber);
	}
	public String getPersonFBINumber() {
		return personFBINumber;
	}
	public void setPersonFBINumber(String personFBINumber) {
		this.personFBINumber = StringEscapeUtils.escapeHtml(personFBINumber);
	}
	public String getPersonSID() {
		return personSID;
	}
	public void setPersonSID(String personSID) {
		this.personSID = StringUtils.upperCase(StringEscapeUtils.escapeHtml(personSID));
	}
	public SearchFieldMetadata getPersonGivenNameMetaData() {
		return personName.getGivenNameMetaData();
	}
	public void setPersonGivenNameMetaData(SearchFieldMetadata personGivenNameMetaData) {
		this.personName.setGivenNameMetaData( personGivenNameMetaData );
	}
	public SearchFieldMetadata getPersonSurNameMetaData() {
		return personName.getSurNameMetaData();
	}
	public void setPersonSurNameMetaData(SearchFieldMetadata personSurNameMetaData) {
		this.personName.setSurNameMetaData( personSurNameMetaData );
	}
	public String getPersonDriversLicenseIssuer() {
		return personDriversLicenseIssuer;
	}
	public void setPersonDriversLicenseIssuer(String personDriversLicenseIssuer) {
		this.personDriversLicenseIssuer = StringEscapeUtils.escapeHtml(personDriversLicenseIssuer);
	}
	public DateTime getPersonDateOfBirth() {
		return personDateOfBirth;
	}
	public void setPersonDateOfBirth(DateTime personDateOfBirth) {
		this.personDateOfBirth = personDateOfBirth;
	}
	public DateTime getPersonDateOfBirthRangeStart() {
		return personDateOfBirthRangeStart;
	}
	public void setPersonDateOfBirthRangeStart(DateTime personDateOfBirthRangeStart) {
		this.personDateOfBirthRangeStart = personDateOfBirthRangeStart;
	}
	public DateTime getPersonDateOfBirthRangeEnd() {
		return personDateOfBirthRangeEnd;
	}
	public void setPersonDateOfBirthRangeEnd(DateTime personDateOfBirthRangeEnd) {
		this.personDateOfBirthRangeEnd = personDateOfBirthRangeEnd;
	}
	public List<String> getSourceSystems() {
		return sourceSystems;
	}
	public void setSourceSystems(List<String> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}
	public Integer getPersonWeight() {
		return personWeight;
	}
	public void setPersonWeight(Integer personWeight) {
		this.personWeight = personWeight;
	}
	public Integer getPersonWeightRangeStart() {
		return personWeightRangeStart;
	}
	public void setPersonWeightRangeStart(Integer personWeightRangeStart) {
		this.personWeightRangeStart = personWeightRangeStart;
	}
	public Integer getPersonWeightRangeEnd() {
		return personWeightRangeEnd;
	}
	public void setPersonWeightRangeEnd(Integer personWeightRangeEnd) {
		this.personWeightRangeEnd = personWeightRangeEnd;
	}
	public Integer getPersonHeightTotalInches() {
		return personHeightTotalInches;
	}
	public void setPersonHeightTotalInches(Integer personHeightTotalInches) {
		this.personHeightTotalInches = personHeightTotalInches;
	}
	public Integer getPersonHeightTotalInchesRangeStart() {
		return personHeightTotalInchesRangeStart;
	}
	public void setPersonHeightTotalInchesRangeStart(Integer personHeightTotalInchesRangeStart) {
		this.personHeightTotalInchesRangeStart = personHeightTotalInchesRangeStart;
	}
	public Integer getPersonHeightTotalInchesRangeEnd() {
		return personHeightTotalInchesRangeEnd;
	}
	public void setPersonHeightTotalInchesRangeEnd(Integer personHeightTotalInchesRangeEnd) {
		this.personHeightTotalInchesRangeEnd = personHeightTotalInchesRangeEnd;
	}
	
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
		}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOnBehalfOf() {
		return onBehalfOf;
	}
	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}
    public PersonName getAlias() {
        return alias;
    }
    public void setAlias(PersonName alias) {
        this.alias = alias;
    }
    public PersonName getParentName() {
        return parentName;
    }
    public void setParentName(PersonName parentName) {
        this.parentName = parentName;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public String getPlacement() {
        return placement;
    }
    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
