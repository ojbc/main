package org.ojbc.warrant.repository.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.warrant.repository.model.ChargeReferral;
import org.ojbc.warrant.repository.model.ChargeReferralReport;
import org.ojbc.warrant.repository.model.Person;
import org.springframework.jdbc.core.RowMapper;

public class ChargeReferralMapper implements RowMapper<ChargeReferralReport> {

	private static final Log log = LogFactory.getLog(ChargeReferralMapper.class);
	
	@Override
	public ChargeReferralReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChargeReferralReport chargeReferralReport = new ChargeReferralReport();

		//Only mapping the minimum number of fields required for the implementation
		//A more extensive mapping could be performed if additional fields are needed
		ChargeReferral chargeReferral = new ChargeReferral();
		chargeReferral.setChargeReferralID(rs.getInt("ChargeReferralID"));
		chargeReferralReport.setChargeReferral(chargeReferral);

		Person person = new Person();
		person.setPersonID(rs.getInt("PersonID"));
		chargeReferralReport.setPerson(person);
		
		try {
			chargeReferralReport.setWarrantId(rs.getInt("WarrantID"));
		} catch (Exception e) {
			log.error("Unable to map warrant ID.");
		}
		
    	return chargeReferralReport;
	}
}
