package org.ojbc.intermediaries.sn.dao.audit;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NotificationPropertiesMapper implements RowMapper<NotificationProperties> {

	@Override
	public NotificationProperties mapRow(ResultSet rs, int rowNum) throws SQLException {

		NotificationProperties notificationProperties = new NotificationProperties();
		
		notificationProperties.setPropertyName(rs.getString("PROPERTY_NAME"));
		notificationProperties.setPropertyValue(rs.getString("PROPERTY_VALUE"));
		
		return notificationProperties;
		
	}


}
