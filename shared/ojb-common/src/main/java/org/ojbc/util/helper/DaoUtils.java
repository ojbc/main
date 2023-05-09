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
package org.ojbc.util.helper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DaoUtils {
	
	private DaoUtils() {
	}
	/**
	 * This method will return a null rather than zero if the database has a null
	 * value for the column.
	 * 
	 * See here:
	 * http://stackoverflow.com/questions/2920364/checking-for-a-null-int-value-from-a-java-resultset
	 * 
	 * @param rs
	 * @param strColName
	 * @return
	 * @throws SQLException
	 */
	static public Integer getInteger(ResultSet rs, String strColName) throws SQLException {
        int nValue = rs.getInt(strColName);
        return rs.wasNull() ? null : nValue;
    }
	
	/**
	 * Get LocalDate instance from the ResultSet rs with columnName
	 * @param rs
	 * @param columnName
	 * @return null if the sqlDate retrieved from the result set is null.
	 * @throws SQLException
	 */
	static public LocalDate getLocalDate(ResultSet rs, String columnName) throws SQLException {
		Date sqlDate = rs.getDate(columnName);
		return sqlDate == null ? null : sqlDate.toLocalDate();
	}
	
	/**
	 * Get LocalTime instance from the ResultSet rs with columnName
	 * @param rs
	 * @param columnName
	 * @return null if the sqlTime retrieved from the result set is null.
	 * @throws SQLException
	 */
	static public LocalTime getLocalTime(ResultSet rs, String columnName) throws SQLException {
		Time sqlTime = rs.getTime(columnName);
		return sqlTime == null ? null : sqlTime.toLocalTime();
	}
	
	/**
	 * Get LocalDateTime instance from the ResultSet rs with columnName 
	 * @param rs
	 * @param columnName
	 * @return null if the timestamp from the rs is null. 
	 * @throws SQLException
	 */
	static public LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
		Timestamp timestamp = rs.getTimestamp(columnName);
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}
	
	/**
	 * Convert "true" to 'Y' and "false" to 'N'  
	 * 
	 * @param indicatorValue
	 * @return
	 */
	public static Character getIndicatorValueForDatabase(String indicatorValue)
	{
		Character returnValue = null;
		
		if (indicatorValue.equalsIgnoreCase("true"))
		{
			returnValue = 'Y';
		}
		
		if (indicatorValue.equalsIgnoreCase("false"))
		{
			returnValue = 'N';
		}
		
		return returnValue;
	}
	
	/**
	 * If object is not null, set the correct object type value to the ps at index. 
	 * Otherwise, set java.sql.Types.NULL at index. 
	 *  
	 * @param object
	 * @param ps
	 * @param index
	 * @throws SQLException
	 */
	public static void setPreparedStatementVariable(Object object, PreparedStatement ps, int index)
			throws SQLException {
		
		if (object != null){
			if (object instanceof Integer){
				ps.setInt(index, (Integer) object);
			}
			else if (object instanceof String){
				ps.setString(index, (String) object);
			}
			else if (object instanceof LocalDate){
				ps.setDate(index, java.sql.Date.valueOf((LocalDate) object));
			}
			else if (object instanceof LocalTime){
				ps.setTime(index, java.sql.Time.valueOf((LocalTime) object));
			}
			else if (object instanceof LocalDateTime){
				ps.setTimestamp(index, java.sql.Timestamp.valueOf((LocalDateTime) object));
			}
			else if (object instanceof BigDecimal){
				ps.setBigDecimal(index, (BigDecimal) object);
			}
			else if (object instanceof Boolean){
				ps.setBoolean(index, (Boolean) object);
			}
        }
        else{
        	ps.setNull(index, java.sql.Types.NULL);
        }
	}
	
	public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.toLowerCase().equals(rsmd.getColumnName(x).toLowerCase())) {
	            return true;
	        }
	    }
	    return false;
	}
	



}