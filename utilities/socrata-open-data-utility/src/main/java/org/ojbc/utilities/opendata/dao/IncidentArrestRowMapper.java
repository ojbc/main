package org.ojbc.utilities.opendata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.springframework.jdbc.core.RowMapper;

/**
 * This class can be used for smaller recordsets where it is appropriate
 * to put the entire result set in memory.  However, for very large record sets,
 * the IncidentArrestRowCallbackHandler should be used.
 * 
 */
public class IncidentArrestRowMapper implements RowMapper<IncidentArrest> {

	@Override
	public IncidentArrest mapRow(ResultSet rs, int rowNum) throws SQLException {
		IncidentArrest incidentArrest = DaoUtils.processResultSet(rs);
		
    	return incidentArrest;
	}

}
