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

