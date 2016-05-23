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
package org.ojbc.util.helper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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


}