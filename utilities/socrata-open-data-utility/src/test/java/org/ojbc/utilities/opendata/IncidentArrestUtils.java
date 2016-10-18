package org.ojbc.utilities.opendata;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;

public class IncidentArrestUtils {

	public static IncidentArrest returnExampleIncidentArrest() {
		
		IncidentArrest incidentArrest = new IncidentArrest();
		
		List<Charge> charges = new ArrayList<Charge>();
		
		Charge charge1 = new Charge();
		charge1.setArrestChargeStatute("charge1");
		charge1.setInvolvedDrugDescription("marijuana");
		
		Charge charge2 = new Charge();
		charge2.setArrestChargeStatute("charge2");
		charge2.setInvolvedDrugDescription("heroin");
		
		charges.add(charge1);
		charges.add(charge2);
		
		incidentArrest.setCharges(charges);
		
		List<IncidentType> incidentTypes = new ArrayList<IncidentType>();
		
		IncidentType incidentType1 = new IncidentType();
		
		incidentType1.setIncidentCategoryDescription("Category1");
		incidentType1.setIncidentTypeDescription("Desc1");
		
		IncidentType incidentType2 = new IncidentType();

		incidentType2.setIncidentCategoryDescription("Category2");
		incidentType2.setIncidentTypeDescription("Desc2");

		incidentTypes.add(incidentType1);
		incidentTypes.add(incidentType2);
		
		incidentArrest.setIncidentTypes(incidentTypes);
		
		incidentArrest.setAgeInYears("29");
		incidentArrest.setArrestCountyName("arrest county");
		incidentArrest.setArrestDrugInvolved("True");
		incidentArrest.setArresteeSexDescription("Male");
		incidentArrest.setArrestingAgency("Local PD");
		incidentArrest.setChargesDelimited("charge1|charge2");
		incidentArrest.setIncidentCaseNumber("IC Number");
		incidentArrest.setIncidentCategoryDescriptionDelimited("Category1|Category2");
		incidentArrest.setIncidentCountyName("incident county");
		incidentArrest.setIncidentDateTime("2003-12-05 08:57:12");
		incidentArrest.setIncidentID(1);
		incidentArrest.setIncidentLoadTimeStamp("2016-02-19 03:18:03");
		incidentArrest.setIncidentLocationAddress("123 address");
		incidentArrest.setIncidentTown("inc town");
		incidentArrest.setIncidentTypeDelimited("Desc1|Desc2");
		incidentArrest.setPersonRaceDescription("race");
		incidentArrest.setReportingAgency("reporting agency");
		incidentArrest.setArrestDrugInvolvedDescription("marijuana|heroin");
		
		incidentArrest.setRowIdentifier("1");
		
		return incidentArrest;
	}
}
