package org.ojbc.utilities.opendata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.utilities.opendata.dao.model.IncidentType;
import org.springframework.jdbc.core.RowMapper;

public class IncidentTypeRowMapper implements RowMapper<IncidentType> {


	@Override
	public IncidentType mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		IncidentType incidentType = new IncidentType();
		
		incidentType.setIncidentCategoryDescription(rs.getString("incidentCategoryDescription"));
		incidentType.setIncidentTypeDescription(rs.getString("incidentTypeDescription"));
		
    	return incidentType;
	}

}
