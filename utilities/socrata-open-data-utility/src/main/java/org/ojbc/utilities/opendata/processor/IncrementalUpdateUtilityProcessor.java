package org.ojbc.utilities.opendata.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.statemanager.LastUpdateFileManager;
import org.ojbc.utilities.opendata.dao.OpenDataDAO;
import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class IncrementalUpdateUtilityProcessor {

	private OpenDataDAO openDataDAO;
	
	private static final Log log = LogFactory.getLog(IncrementalUpdateUtilityProcessor.class);
	
	ObjectMapper mapper = new ObjectMapper();
	
	private LastUpdateFileManager lastUpdateFileManager;
	
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * This method will retrieve all incident and arrests from the database and produce a list of Incident Arrest lists.
	 * These lists will later be converted into JSON requests.  These are chunked into lists of 1,000 records.  This is to
	 * keep the JSON request size manageable.
	 */
	public List<List<IncidentArrest>> retrieveIncidentArrestsLists(LocalDateTime dateTime)
			throws Exception {
		
		if (dateTime == null)
		{
			String lastUpdateDateTime = lastUpdateFileManager.getLastUpdateTime();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			dateTime = LocalDateTime.parse(lastUpdateDateTime, formatter);
		}	
		
		List<String> incidentIds = openDataDAO.searchForIncidentsArrestsByLoadTimestamp(dateTime);
		
		List<IncidentArrest> allIncidentArrests = new ArrayList<IncidentArrest>();
		
		for (String incidentId : incidentIds)
		{
			log.info("Incident Number: " + incidentId);
			
			List<IncidentArrest> incidentArrests = openDataDAO.searchForIncidentByIncidentId(incidentId);

			for (IncidentArrest incidentArrest : incidentArrests)
			{	
				if (StringUtils.isNotBlank(incidentArrest.getArrestId()))
				{
					log.info("Incident Arrest contains arrest, retrieve charges");
					
					List<Charge> charges = openDataDAO.returnChargesFromArrest(incidentArrest.getArrestId());
					
					incidentArrest.setCharges(charges);
					
					String chargesDelimited = OpenDataUtils.returnChargesDelimited(incidentArrest);
					incidentArrest.setChargesDelimited(chargesDelimited);
				}	
				
				List<IncidentType> incidentTypes = openDataDAO.returnIncidentTypesFromIncident(incidentId);
				incidentArrest.setIncidentTypes(incidentTypes);
				
				String incidentCategoryDelimited = OpenDataUtils.returnIncidentCategoryDelimited(incidentArrest);
				incidentArrest.setIncidentCategoryDescriptionDelimited(incidentCategoryDelimited);

				String incidentTypeDelimited = OpenDataUtils.returnIncidentTypesDelimited(incidentArrest);
				incidentArrest.setIncidentTypeDelimited(incidentTypeDelimited);
				
				String arrestDrugsInvolvedDescription = OpenDataUtils.returnInvolvedDrugDelimited(incidentArrest);
				incidentArrest.setArrestDrugInvolvedDescription(arrestDrugsInvolvedDescription);

				//Here is the record to add
				allIncidentArrests.add(incidentArrest);
				
			}	

		}	
		
		LocalDateTime lastUpdatedDateTime = openDataDAO.returnLastUpdatedIncidentDateTimestamp();
		
		if (lastUpdatedDateTime != null)
		{	
			String lastUpdatedDateTimeAsString = lastUpdatedDateTime.format(dateTimeFormatter);
			lastUpdateFileManager.saveLastUpdateTime(lastUpdatedDateTimeAsString);
		}	
		
		//Partition the list into a list of lists that contains 1,000 records max in it.
		//This allows for larger record sets to processed using JSON.
		return Lists.partition(allIncidentArrests, 1000);
				
	}

	public String addIncidentArrestToJSONRequest(List<IncidentArrest> incidentArrests) throws Exception {

		String jsonObject = mapper.writeValueAsString(incidentArrests);
		
		log.info(jsonObject);
		
		return jsonObject;
	}

	public LastUpdateFileManager getLastUpdateFileManager() {
		return lastUpdateFileManager;
	}

	public void setLastUpdateFileManager(LastUpdateFileManager lastUpdateFileManager) {
		this.lastUpdateFileManager = lastUpdateFileManager;
	}

	public OpenDataDAO getOpenDataDAO() {
		return openDataDAO;
	}

	public void setOpenDataDAO(OpenDataDAO openDataDAO) {
		this.openDataDAO = openDataDAO;
	}
}
