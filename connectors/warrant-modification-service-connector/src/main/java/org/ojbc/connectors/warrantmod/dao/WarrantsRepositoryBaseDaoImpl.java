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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.connectors.warrantmod.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.DaoUtils;
import org.ojbc.warrant.repository.model.Person;
import org.ojbc.warrant.repository.model.PersonVehicle;
import org.ojbc.warrant.repository.model.Warrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class WarrantsRepositoryBaseDaoImpl implements WarrantsRepositoryBaseDAO {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(WarrantsRepositoryBaseDaoImpl.class);
	
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public Warrant retrieveWarrant(Integer warrantId) {
		String sql = "SELECT w.*, wr.WarrantRemarkText from Warrant w "
				+ "LEFT JOIN WarrantRemarks wr ON wr.warrantID = w.warrantID WHERE w.warrantID = ? ";
		
		List<Warrant> warrants = jdbcTemplate.query(sql, new WarrantResultSetExtractor(), warrantId);
		return DataAccessUtils.singleResult(warrants);
	}

	private class WarrantResultSetExtractor implements ResultSetExtractor<List<Warrant>> {

		@Override
		public List<Warrant> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
            Map<Integer, Warrant> map = new HashMap<Integer, Warrant>();
            Warrant warrant = null;
            while (rs.next()) {
                Integer warrantId = rs.getInt("warrantID" ); 
                warrant  = map.get( warrantId );
                if ( warrant  == null){
                	warrant = new Warrant(); 
                	
                	warrant.setWarrantID(warrantId);
                	warrant.setStateWarrantRepositoryID(rs.getString("StateWarrantRepositoryID"));
                	warrant.setDateOfWarrantRequest( DaoUtils.getLocalDate(rs, "DateOfWarrant"));
                	warrant.setDateOfExpiration( DaoUtils.getLocalDate(rs, "DateOfExpiration"));
                	warrant.setBroadcastArea(rs.getString("BroadcastArea"));
                	warrant.setWarrantEntryType(rs.getString("WarrantEntryType"));
                	warrant.setCourtAgencyORI(rs.getString("CourtAgencyORI"));
                	warrant.setLawEnforcementORI(rs.getString("LawEnforcementORI"));
                	warrant.setCourtDocketNumber(rs.getString("CourtDocketNumber"));
                	warrant.setOcaComplaintNumber(rs.getString("OCAComplaintNumber"));
                	warrant.setOperator(rs.getString("Operator"));
                	warrant.setPaccCode(rs.getString("PACCCode"));
                	warrant.setOriginalOffenseCode(rs.getString("OriginalOffenseCode"));
                	warrant.setOffenseCode(rs.getString("OffenseCode"));
                	warrant.setGeneralOffenseCharacter(rs.getString("GeneralOffenseCharacter"));
                	warrant.setCriminalTrackingNumber(rs.getString("CriminalTrackingNumber"));
                	warrant.setExtradite(rs.getBoolean("Extradite"));
                	warrant.setExtraditionLimits(rs.getString("ExtraditionLimits"));
                	warrant.setPickupLimits(rs.getString("PickupLimits"));
                	warrant.setBondAmount(rs.getString("BondAmount"));
                	warrant.setWarrantStatus(rs.getString("WarrantStatus"));
                	warrant.setWarrantStatusTimestamp(DaoUtils.getLocalDateTime( rs, "WarrantStatusTimestamp"));
                	warrant.setWarrantModificationRequestSent( rs.getBoolean("WarrantModRequestSent") );
                	warrant.setWarrantModificationResponseReceived( rs.getBoolean("WarrantModResponseReceived") );
                	
                	List<String> warrantRemarks = new ArrayList<String>();
                	String warrantRemark = rs.getString("WarrantRemarkText");
                	
                	if (StringUtils.isNotBlank(warrantRemark)){
                		warrantRemarks.add( warrantRemark );
                	}
                	warrant.setWarrantRemarkStrings(warrantRemarks);
                	
                	map.put(warrantId, warrant); 
                }
                else{
                	String warrantRemark = rs.getString("WarrantRemarkText");
                	
                	if (StringUtils.isNotBlank(warrantRemark)){
                		warrant.getWarrantRemarkStrings().add( warrantRemark );
                	}
                }
	              
            }
            
            return (List<Warrant>) new ArrayList<Warrant>(map.values());
		}

	}

	@Override
	public List<String> getTransactionControlNumbers(Integer warrantId) {
		String sql = "SELECT cr.TransactionControlNumber FROM WarrantChargeRef wcr "
				+ "LEFT JOIN ChargeRef cr ON cr.ChargeRefID = wcr.ChargeRefID "
				+ "WHERE wcr.WarrantID = ? "; 

		List<String> transactionControlNumbers = 
				jdbcTemplate.queryForList(sql, String.class, warrantId);
		return transactionControlNumbers;
	}


	@Override
	public List<Person> retrievePersons(Integer warrantId) {
		String sql = "SELECT DISTINCT p.*, v.* from WarrantChargeRef wcr "
				+ "LEFT JOIN ChargeRef cr ON cr.ChargeRefID = wcr.chargeRefID "
				+ "LEFT JOIN Person p ON p.PersonID = cr.PersonID "
				+ "LEFT JOIN Vehicle v ON v.PersonID = p.PersonID "
				+ "WHERE wcr.warrantId = ? ";
		List<Person> persons = 
				jdbcTemplate.query(sql, new PersonReulstSetExtractor(), warrantId);
		return persons;
	}

	private class PersonReulstSetExtractor implements ResultSetExtractor<List<Person>> {

		@Override
		public List<Person> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
            Map<Integer, Person> map = new HashMap<Integer, Person>();
            Person person = null;
            while (rs.next()) {
                Integer personId = rs.getInt("personId" ); 
                person  = map.get( personId );
                if ( person  == null){
                	person = new Person(); 
                	
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
                	
                	List<PersonVehicle> personVehicles = new ArrayList<PersonVehicle>();

                	Integer vehicleId = rs.getInt("VehicleID");
                	if (vehicleId != null && vehicleId > 0){
	                	
	                	PersonVehicle personVehicle = buildVehicle(rs, personId);
	                	personVehicles.add(personVehicle);
                	}
                	person.setPersonVehicles(personVehicles);
                	
                	map.put(personId, person); 
                }
                else{
                	Integer vehicleId = rs.getInt("VehicleID");
                	if (vehicleId != null && vehicleId > 0){
	                	PersonVehicle personVehicle = buildVehicle(rs, personId);
	                	person.getPersonVehicles().add(personVehicle);
                	}
                }
	              
            }
            
            return (List<Person>) new ArrayList<Person>(map.values());
		}

		private PersonVehicle buildVehicle(ResultSet rs, Integer personId) throws SQLException {
        	PersonVehicle personVehicle = new PersonVehicle();
			personVehicle.setPersonID(personId);
			personVehicle.setLicensePlateType(rs.getString("LicensePlateType"));
			personVehicle.setPersonVehicleID(rs.getInt("VehicleID"));
			personVehicle.setVehicleIdentificationNumber(rs.getString("VehicleIdentificationNumber"));
			personVehicle.setVehicleLicensePlateExpirationDate(rs.getString("VehicleLicensePlateExpirationD"));
			personVehicle.setVehicleLicensePlateNumber(rs.getString("VehicleLicensePlateNumber"));
			personVehicle.setVehicleLicenseStateCode(rs.getString("VehicleLicenseStateCode"));
			personVehicle.setVehicleMake(rs.getString("VehicleMake"));
			personVehicle.setVehicleModel(rs.getString("VehicleModel"));
			personVehicle.setVehicleNonExpiringIndicator(rs.getBoolean("VehicleNonExpiringIndicator"));
			personVehicle.setVehiclePrimaryColor(rs.getString("VehiclePrimaryColor"));
			personVehicle.setVehicleSecondaryColor(rs.getString("VehicleSecondaryColor"));
			personVehicle.setVehicleStyle(rs.getString("VehicleStyle"));
			return personVehicle;
		}

	}

}
