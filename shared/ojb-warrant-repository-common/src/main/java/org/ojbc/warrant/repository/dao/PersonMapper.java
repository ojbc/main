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
package org.ojbc.warrant.repository.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.warrant.repository.model.Person;
import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {

	@Override
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Person person = new Person();
		
		person.setAddressCity(rs.getString("AddressCity"));
		person.setAddressStreetFullText(rs.getString("AddressStreetFullText"));
		person.setAddressStreetNumber(rs.getString("AddressStreetNumber"));
		person.setAddressStreetName(rs.getString("AddressStreetName"));
		person.setAddressCity(rs.getString("AddressCity"));
		person.setAddressCounty(rs.getString("AddressCounty"));
		person.setAddressState(rs.getString("AddressState"));
		person.setAddressZip(rs.getString("addressZip"));
		person.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
		person.setFbiIdentificationNumber(rs.getString("FbiIdentificationNumber"));
		person.setFirstName(rs.getString("FirstName"));
		person.setFullPersonName(rs.getString("FullPersonName"));
		person.setLastName(rs.getString("LastName"));
		person.setMiddleName(rs.getString("MiddleName"));
		person.setMiscellaneousIDBase(rs.getString("MiscellaneousIDBase"));
		person.setNameSuffix(rs.getString("NameSuffix"));
		person.setOperatorLicenseNumberBase(rs.getString("OperatorLicenseNumberBase"));
		person.setOperatorLicenseStateBase(rs.getString("OperatorLicenseStateBase"));
		person.setPersonAge(rs.getString("PersonAge"));
		person.setPersonCautionDescription(rs.getString("PersonCautionDescription"));
		person.setPersonCitizenshipCountry(rs.getString("PersonCitizenshipCountry"));
		person.setPersonEthnicityDescription(rs.getString("PersonEthnicityDescription"));
		person.setPersonEyeColorDescription(rs.getString("PersonEyeColorDescription"));
		person.setPersonHairColorDescription(rs.getString("PersonHairColorDescription"));
		person.setPersonHeight(rs.getString("PersonHeight"));
		person.setPersonImmigrationAlienQueryIndicator(rs.getBoolean("personImmigrationAlienQueryInd"));
		person.setPersonRaceDescription(rs.getString("personRaceDescription"));
		person.setPersonScarsMarksTattosBase(rs.getString("personScarsMarksTattosBase"));
		person.setPersonSexDescription(rs.getString("personSexDescription"));
		person.setPersonSkinToneDescription(rs.getString("personSkinToneDescription"));
		person.setPersonStateIdentification(rs.getString("personStateIdentification"));
		person.setPersonWeight(rs.getString("personWeight"));
		person.setSocialSecurityNumberBase(rs.getString("SocialSecurityNumberBase"));
		person.setUsCitizenshipIndicator(rs.getBoolean("USCitizenshipIndicator"));
		person.setPersonID(rs.getInt("PersonID"));
		
    	return person;
	}
}
