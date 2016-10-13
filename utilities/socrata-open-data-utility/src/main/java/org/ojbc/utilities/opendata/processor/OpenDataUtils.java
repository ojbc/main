package org.ojbc.utilities.opendata.processor;

import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;

public class OpenDataUtils {

	public static final String VALUE_DELIMITER  = "|";
	
	public static String returnChargesDelimited(IncidentArrest incidentArrest)
	{
		StringBuffer charges = new StringBuffer();
		
		if (incidentArrest.getCharges() != null  && incidentArrest.getCharges().size() > 0)
		{
			
			for (Charge charge : incidentArrest.getCharges() )
			{
				charges.append(charge.getArrestChargeStatute());
				charges.append(VALUE_DELIMITER);
			}
			
			charges.deleteCharAt(charges.lastIndexOf(VALUE_DELIMITER));
			
		}	
		
		return charges.toString();
	}
	
	public static String returnIncidentTypesDelimited(IncidentArrest incidentArrest)
	{
		StringBuffer incidentTypeDescription = new StringBuffer();
		
		if (incidentArrest.getIncidentTypes() != null  && incidentArrest.getIncidentTypes().size() > 0)
		{
			
			for (IncidentType incidentType : incidentArrest.getIncidentTypes() )
			{
				incidentTypeDescription.append(incidentType.getIncidentTypeDescription());
				incidentTypeDescription.append(VALUE_DELIMITER);
			}
			
			incidentTypeDescription.deleteCharAt(incidentTypeDescription.lastIndexOf(VALUE_DELIMITER));
			
		}
		
		return incidentTypeDescription.toString();
	}	
	
	public static String returnIncidentCategoryDelimited(IncidentArrest incidentArrest)
	{
		StringBuffer incidentCategoryDescription = new StringBuffer();
		
		if (incidentArrest.getIncidentTypes() != null  && incidentArrest.getIncidentTypes().size() > 0)
		{

			for (IncidentType incidentType : incidentArrest.getIncidentTypes() )
			{
				incidentCategoryDescription.append(incidentType.getIncidentCategoryDescription());
				incidentCategoryDescription.append(VALUE_DELIMITER);
			}
			
			incidentCategoryDescription.deleteCharAt(incidentCategoryDescription.lastIndexOf(VALUE_DELIMITER));
		}	
		
		return incidentCategoryDescription.toString();
	}

	public static String returnInvolvedDrugDelimited(
			IncidentArrest incidentArrest) {
		StringBuffer charges = new StringBuffer();
		
		if (incidentArrest.getCharges() != null  && incidentArrest.getCharges().size() > 0)
		{
			
			for (Charge charge : incidentArrest.getCharges() )
			{
				charges.append(charge.getInvolvedDrugDescription());
				charges.append(VALUE_DELIMITER);
			}
			
			charges.deleteCharAt(charges.lastIndexOf(VALUE_DELIMITER));
			
		}	
		
		return charges.toString();	}	
}

