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
		person.setAddressFullText(rs.getString("AddressFullText"));
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
		person.setPersonEntryNumber(rs.getString("PersonEntryNumber"));
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
		person.setPersonID(rs.getInt("PersonID"));
		
    	return person;
	}
}
