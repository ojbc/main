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
package org.ojbc.utilities.opendata.processor;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.utilities.opendata.dao.CsvExtractManager;
import org.ojbc.utilities.opendata.dao.OpenDataDAO;
import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;

public class OpenDataUtilityProcessor {

	private OpenDataDAO openDataDAO;
	
	private CsvExtractManager csvExtractManager;
	
	private static final Log log = LogFactory.getLog(OpenDataUtilityProcessor.class);
	
	/**
	 * This method will retrieve all incident and arrests from the database and produce a CSV file.
	 * To scale the query and CSV generation, the query and CSV file are produced in the same method.
	 */
	public void retrieveAllIncidentsAndWriteToCSVFile()
			throws Exception {
		
		//Set up the CSV file root directory and prepare the CSV file for writing
		csvExtractManager.setUpCSVFileForWriting();
		
		List<String> incidentIds = openDataDAO.returnAllIncidentsNumbers();
		
		HashMap<String, Integer> rowIdentifiers = new HashMap<String, Integer>();
		
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
				}	
				
				
				List<IncidentType> incidentTypes = openDataDAO.returnIncidentTypesFromIncident(incidentId);
				incidentArrest.setIncidentTypes(incidentTypes);

				if (rowIdentifiers.containsKey(incidentArrest.getRowIdentifier()))
				{
					log.error("DUPLICATE IDENTIFIER: " + incidentArrest.getRowIdentifier());
					throw new Exception("Row identifiers must be unique.  DUPLICATE IDENTIFIER: " + incidentArrest.getRowIdentifier());
				}
				
				rowIdentifiers.put(incidentArrest.getRowIdentifier(), incidentArrest.getIncidentID());
				
				csvExtractManager.incidentArrestToCSV(incidentArrest);
			}	

		}	
				
		//Flush CSV file and close properly
		csvExtractManager.flushDataAndCloseWriter();
	}


	public CsvExtractManager getCsvExtractManager() {
		return csvExtractManager;
	}

	public void setCsvExtractManager(CsvExtractManager csvExtractManager) {
		this.csvExtractManager = csvExtractManager;
	}


	public OpenDataDAO getOpenDataDAO() {
		return openDataDAO;
	}


	public void setOpenDataDAO(OpenDataDAO openDataDAO) {
		this.openDataDAO = openDataDAO;
	}
}
