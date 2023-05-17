package org.ojbc.booking.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ojbc.booking.common.dao.model.Person;
import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person> {

	@Override
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(rs.getString("person_unique_identifier"));
		person.setId(rs.getInt("id"));
		
		return person;
		
	}
}
