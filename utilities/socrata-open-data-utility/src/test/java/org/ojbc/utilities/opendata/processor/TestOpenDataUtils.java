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
