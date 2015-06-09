package org.ojbc.adapters.analyticaldatastore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.ojbc.adapters.analyticaldatastore.dao.model.CodeTable;
import org.ojbc.adapters.analyticaldatastore.dao.model.KeyValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Data access implementations of all Firearms database code tables.
 * @since November 29, 2012
 */
@Repository
public class CodeTableDAOImpl implements CodeTableDAO
{

	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public List<KeyValue> retrieveCodeDescriptions(CodeTable codeTable) {
		String sql = null;

		switch (codeTable) {
		
			case County:
			case PersonRace:
			case PersonSex:
			case InvolvedDrug: 
			case IncidentType: 
			case AssessedNeed: 
			case PretrialService: 
				sql = "SELECT * FROM " + codeTable.name();
				return jdbcTemplate.query(sql, new KeyValueRowMapper());
			default:
				return null;
		}
	}

	public class KeyValueRowMapper implements RowMapper<KeyValue> {
		public KeyValue mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new KeyValue(rs.getLong(1), rs.getString(2));
		}
	}


}
