package org.ojbc.utilities.opendata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.utilities.opendata.dao.model.Charge;
import org.springframework.jdbc.core.RowMapper;

/**
 * This class can be used for smaller recordsets where it is appropriate
 * to put the entire result set in memory.  However, for very large record sets,
 * a RowCallbackHandler should be used.
 * 
 */
public class ChargeRowMapper implements RowMapper<Charge> {

	@Override
	public Charge mapRow(ResultSet rs, int rowNum) throws SQLException {
		Charge charge = new Charge();
		
		charge.setArrestChargeStatute(rs.getString("ArrestChargeStatute"));
		charge.setInvolvedDrugDescription(rs.getString("InvolvedDrugDescription"));
		
    	return charge;
	}

}
