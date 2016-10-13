package org.ojbc.utilities.opendata.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.processor.OpenDataUtils;

/**
 * This class will manage all interactions with creating and writing a CSV file
 */
public class CsvExtractManager {

	private static final Log log = LogFactory.getLog(CsvExtractManager.class);
	
	private String csvFileRootDirectory;
	
	private FileWriter writer;

	/**
	 * The constructor will set up the csv file, file writer and first row of CSV headings
	 *  
	 * @throws Exception
	 */
	public void setUpCSVFileForWriting() throws Exception {

		LocalDateTime date = LocalDateTime.now();
		String timestamp = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		timestamp = StringUtils.replace(timestamp, ":", "_");
		
		if (StringUtils.isEmpty(csvFileRootDirectory))
		{
			throw new IllegalStateException("CSV Root Directory must be specified");
		}	
		
		if (!csvFileRootDirectory.endsWith("/"))
		{
			csvFileRootDirectory = csvFileRootDirectory.concat("/");
		}	
		
		String sFileName = csvFileRootDirectory + "Incident_Extract_" + timestamp + ".csv";
		
		writer = new FileWriter(sFileName);

		writer.append("row_identifier");
	    writer.append(',');

		writer.append("incident_case_number");
	    writer.append(',');
	    
		writer.append("reporting_agency");
	    writer.append(',');

		writer.append("incident_date_time");
	    writer.append(',');

		writer.append("incident_type_description");
	    writer.append(',');

		writer.append("incident_category_description");
	    writer.append(',');
	    
		writer.append("incident_location_town");
	    writer.append(',');

		writer.append("incident_location_county");
	    writer.append(',');

		writer.append("arrest_county");
	    writer.append(',');

		writer.append("age_in_years");
	    writer.append(',');

		writer.append("person_race_description");
	    writer.append(',');

		writer.append("arrest_drug_inolved");
	    writer.append(',');

	    writer.append("arrest_drug_involved_description");
		writer.append(',');

		writer.append("arrestee_sex_description");
	    writer.append(',');

		writer.append("arresting_agency");
		writer.append(',');
		
		writer.append("arrest_charges");
		writer.append(',');
		
		writer.append("incident_load_timestamp");
		
	    writer.append('\n');

	}
	
	
	/**
	 * This method will write a single row to a CSV file
	 * 
	 * @param incidentArrest
	 */
	public void incidentArrestToCSV(IncidentArrest incidentArrest) {
		
		try {
			writer.append(incidentArrest.getRowIdentifier());
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getIncidentCaseNumber()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getReportingAgency()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getIncidentDateTime()));
			writer.append(',');
			
			String incidentTypeDescription = OpenDataUtils.returnIncidentTypesDelimited(incidentArrest);	
			writer.append(incidentTypeDescription);
			writer.append(',');
				
			String incidentCategoryDescription = OpenDataUtils.returnIncidentCategoryDelimited(incidentArrest);
			writer.append(incidentCategoryDescription);
			writer.append(',');				
			
			writer.append(processCSVEntry(incidentArrest.getIncidentTown()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getIncidentCountyName()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getArrestCountyName()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getAgeInYears()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getPersonRaceDescription()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getArrestDrugInvolved()));
			writer.append(',');
			
			String involvedDrugDescriptionDelimited = OpenDataUtils.returnInvolvedDrugDelimited(incidentArrest);
			writer.append(involvedDrugDescriptionDelimited);
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getArresteeSexDescription()));
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getArrestingAgency()));
			writer.append(',');
			
			String chargesDelimted = OpenDataUtils.returnChargesDelimited(incidentArrest);
			writer.append(chargesDelimted);
			writer.append(',');
			
			writer.append(processCSVEntry(incidentArrest.getIncidentLoadTimeStamp()));
			
			writer.append('\n');
			
		} catch (IOException e) {
			e.printStackTrace();
			
			log.error("Unable to write CSV row.");
		}
		
	}	
	
	/**
	 * This prevents the CSV file from writing 'null' for a column value 
	 * 
	 * @param entry
	 * @return
	 */
	private String processCSVEntry(String entry)
	{
		if (StringUtils.isBlank(entry))
		{
			return "";
		}
		
		return StringEscapeUtils.escapeCsv(entry);
		
	}
	
	/**
	 * Cleans up writer after processing is complete.  Called from route.
	 * 
	 * @throws Exception
	 */
	public void flushDataAndCloseWriter() throws Exception
	{
		writer.flush();
		writer.close();
		
	}

	public String getCsvFileRootDirectory() {
		return csvFileRootDirectory;
	}

	public void setCsvFileRootDirectory(String csvFileRootDirectory) {
		this.csvFileRootDirectory = csvFileRootDirectory;
	}
}
