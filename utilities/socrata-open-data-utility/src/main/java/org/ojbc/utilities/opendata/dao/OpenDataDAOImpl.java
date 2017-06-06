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
package org.ojbc.utilities.opendata.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;
import org.springframework.jdbc.core.JdbcTemplate;

public class OpenDataDAOImpl implements OpenDataDAO{

	private static final Log log = LogFactory.getLog(OpenDataDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//TODO: This query get the age directly from the arrest table.  Joining with the person age lookup
	//table was causing duplicate rows to be returned.  This works since it is a one to one relationship
	//However, it should be joined with the age table
	private static final String BASE_QUERY = "SELECT i.IncidentCaseNumber, agencyi.AgencyName as reportingIncidentAgency, i.IncidentDateTime, i.IncidentID, ti.TownDescription as incidentTown," +
			 "ci.CountyName as incidentCountyName, ca.CountyName as arrestCountyName, arrest.arresteeageid as ageInYears, " +
			 "personrace.PersonRaceDescription, yna.YesNoDescription as arrestDrugInolved, " +
			 "psa.PersonSexDescription as arresteeSexDescription, agencya.AgencyName as arrestingAgency, " +
			 " i.IncidentLocationStreetAddress, arrest.ArrestID, " +
			 "i.loadtimestamp as incidentLoadtimestamp " +
			 "FROM incident i " +
			 "left join arrest on i.IncidentID = arrest.IncidentID " + 
			 "INNER join agency agencyi on agencyi.AgencyID = i.ReportingAgencyID " + 
			 "INNER join town ti on ti.TownID = i.TownID " +
			 "INNER join county ci on ci.CountyID = i.CountyID " + 
			 "left join personrace on arrest.ArresteeRaceID = personrace.PersonRaceID " +
			 "left join county ca on arrest.CountyID = ca.CountyID " +
			 "left join YesNo yna on arrest.ArrestDrugRelated = yna.YesNoID " +
			 "left join personsex psa on arrest.ArresteeSexID = psa.PersonSexID " +
			 "left join agency agencya on agencya.AgencyID = arrest.ArrestingAgencyID ";
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(10);
    }

    /**
     * This method is suitable for smaller record sets and returns results based on the load timestamp
     * 
     */
	@Override
	public List<String> searchForIncidentsArrestsByLoadTimestamp(
			LocalDateTime dateTime) throws Exception {
		
		log.info("Query for incident arrests by Date/Time: " + dateTime.toString());
		
		String sql = "Select IncidentId from Incident where " +
				 "loadtimestamp > '" + dateTimeFormatter.format(dateTime) + 
				 "' order by loadtimestamp desc";
		
		List<String> incidentIDs = (List<String>) jdbcTemplate.queryForList(sql, String.class);
		
		return incidentIDs;
	}

	@Override
	public List<String> returnAllIncidentsNumbers() throws Exception {
		
		String sql = "Select IncidentId from Incident order by loadtimestamp desc";
		
		List<String> incidentIDs = (List<String>) jdbcTemplate.queryForList(sql, String.class);
		
		return incidentIDs;
	}

	@Override
	public List<IncidentArrest> searchForIncidentByIncidentId(String incidentId) {
		
		log.info("Search for incident by Incident ID: " + incidentId);
		
		String sql = BASE_QUERY +
				 "where " +
				 "i.incidentId = ?";
		
		List<IncidentArrest> incidentArrests = 
				jdbcTemplate.query(sql,new IncidentArrestRowMapper(), incidentId);

		return incidentArrests;
	}

	@Override
	public List<Charge> returnChargesFromArrest(String arrestId) {
		log.info("Search for charges by Arrest ID: " + arrestId);
		
		String sql = "SELECT ch.ArrestChargeStatute, id.InvolvedDrugDescription FROM ojbc_analytics.charge ch, ojbc_analytics.involveddrug id where ch.InvolvedDrugID = id.InvolvedDrugID and ch.arrestId = ?";
		
		List<Charge> charges = 
				jdbcTemplate.query(sql,new ChargeRowMapper(), arrestId);
		
		return charges;
	}

	@Override
	public List<IncidentType> returnIncidentTypesFromIncident(String incidentId) {
		log.info("Search for incident types by Incident ID: " + incidentId);
		
		String sql = "SELECT it.IncidentTypeDescription, ic.IncidentCategoryDescription " +
					 " FROM incident i, incidentcategory ic, incidentTypeAssociation ita, incidentType it " +
					 " where ita.incidentID = i.incidentID " +
					 " and  it.incidentTypeID = ita.incidentTypeID " +
					 " and ic.IncidentCategoryID = it.IncidentCategoryID " +
					 " and i.incidentID = ?";
		
		List<IncidentType> incidentTypes = 
				jdbcTemplate.query(sql,new IncidentTypeRowMapper(), incidentId);
		
		return incidentTypes;
	}

	@Override
	public LocalDateTime returnLastUpdatedIncidentDateTimestamp() {
		log.info("Query for last update incident arrests");
		
		String sql = "SELECT max(loadtimestamp) from Incident;";
		
		String lastUpdatedDatetimeString = (String) jdbcTemplate.queryForObject(sql, String.class);
		
		log.info("Last Updated Date Time String from Database: " + lastUpdatedDatetimeString);
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		LocalDateTime lastUpdatedDatetime = null;
		try {
			lastUpdatedDatetime = LocalDateTime.parse(lastUpdatedDatetimeString, dateTimeFormatter);
		} catch (Exception e) {
			log.error("Unable to retrieve last updated datetime stamp.");
			e.printStackTrace();
		}
		
		return lastUpdatedDatetime;	
	}	

}
