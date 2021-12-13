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
package org.ojbc.adapters.rapbackdatastore.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * This class will handle archive processing.  It will interact with the DAO and then create either a valid response or error response 
 */

@Service
public class DatabaseConnectionMonitor{
    private final Log log = LogFactory.getLog(this.getClass());

	@Resource
    private DataSource dataSource;
	
	public DatabaseConnectionMonitor(){
    }


	public void checkDatabaseConnection(Exchange exchange) throws Exception{
		
		ServiceStatus status = exchange.getContext().getRouteStatus("resend_database_failed_endpoint_route");
		log.debug("route resend_database_failed_endpoint_route status: " + status.toString());
		
		
		if ( ServiceStatus.Started != status && isDatabaseConnected()){
			exchange.getContext().startRoute("resend_database_failed_endpoint_route");
			log.info("route resend_database_failed_endpoint_route is started");
		}
		else if ( ServiceStatus.Started == status && !isDatabaseConnected()){
			exchange.getContext().stopRoute("resend_database_failed_endpoint_route");
			log.info("route resend_database_failed_endpoint_route is stopped");
		}
	}

	private boolean isDatabaseConnected() {
		
		boolean isDatabaseConnected = false; 
		try {
			Connection conn;
			conn = dataSource.getConnection();
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT 1 FROM DUAL");
			while(rs.next()) {
				log.debug("verification query result: " + String.valueOf(rs.getObject(1)));
			}
			log.debug("connection is good");
			conn.close();
			isDatabaseConnected = true;
		} catch (SQLException e) {
			log.info("Connection is not valid");
		}
		
		return isDatabaseConnected; 
	}

}
