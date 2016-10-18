package org.ojbc.utilities.opendata.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;

/**
 * This DAO retrieves incident and arrest information from the datastore
 * 
 */

public interface OpenDataDAO {
	
	public List<String> searchForIncidentsArrestsByLoadTimestamp(LocalDateTime dateTime) throws Exception;

	public List<String> returnAllIncidentsNumbers() throws Exception;
	
	public List<IncidentArrest> searchForIncidentByIncidentId(String incidentId);
	
	public List<Charge> returnChargesFromArrest(String arrestId);
	
	public List<IncidentType> returnIncidentTypesFromIncident(String incidentId);
	
	public LocalDateTime returnLastUpdatedIncidentDateTimestamp();
	
}
