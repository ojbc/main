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
package org.ojbc.adapters.analyticsstaging.custody.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao-adams.xml",
		"classpath:META-INF/spring/properties-context-adams.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
@DirtiesContext
public class DescriptionCodeLookupServiceTest {
	//private final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DescriptionCodeLookupService descriptionCodeLookupService;
	
	@Before
	public void setUp() throws Exception {
		assertNotNull(descriptionCodeLookupService);
	}

	@Test
	public void test(){
		assertEquals(Integer.valueOf(1), descriptionCodeLookupService.retrieveCode(CodeTable.PersonRaceType, "Asian"));
		assertEquals(Integer.valueOf(2), descriptionCodeLookupService.retrieveCode(CodeTable.PersonSexType, "Female"));
		assertThat(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSexType, "W"), is(3));
		assertEquals(Integer.valueOf(3),descriptionCodeLookupService.retrieveCode(CodeTable.Agency, "Adams County SO"));
	}

}
