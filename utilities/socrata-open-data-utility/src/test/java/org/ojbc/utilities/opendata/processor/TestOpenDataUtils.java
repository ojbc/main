package org.ojbc.utilities.opendata.processor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.utilities.opendata.IncidentArrestUtils;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;

public class TestOpenDataUtils {

	IncidentArrest incidentArrest = new IncidentArrest();
	
	@Before
	public void setUp()
	{
		incidentArrest = IncidentArrestUtils.returnExampleIncidentArrest();
	}


	
	@Test
	public void testReturnChargesDelimited() throws Exception
	{
		String chargesDelimString = OpenDataUtils.returnChargesDelimited(incidentArrest);
		assertEquals("charge1|charge2", chargesDelimString);
	}

	@Test
	public void testReturnIncidentCategoryDelimited() throws Exception
	{
		String incidentCategoryDelimited = OpenDataUtils.returnIncidentCategoryDelimited(incidentArrest);
		assertEquals("Category1|Category2", incidentCategoryDelimited);
	}
	
	@Test
	public void testReturnIncidentTypesDelimited() throws Exception
	{
		String incidentTypesDelimited = OpenDataUtils.returnIncidentTypesDelimited(incidentArrest);
		assertEquals("Desc1|Desc2", incidentTypesDelimited);
	}
	
}
