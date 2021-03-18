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
package org.ojbc.adapters.rapbackdatastore.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorDemographics;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorSearchResult;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml"
		})
@DirtiesContext
public class RapbackDAOImplDeleteMethodsTest {

	private final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	RapbackDAOImpl rapbackDAO;
	
    @Resource  
    private DataSource dataSource;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDAO);
	}
	
	@Test
	public void testDeleteCivilInitialResults() throws Exception {
		List<CivilInitialResults> civilInitialResults = rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339990");
		assertThat(civilInitialResults.size(), is(2)); 
		assertThat(civilInitialResults.get(0).getRapsheets().size(), is(2)); 
		assertThat(civilInitialResults.get(1).getRapsheets().size(), is(2)); 
		log.info("civilInitialResults.get(0)'s result sender: " + civilInitialResults.get(0).getResultsSender());
		log.info("civilInitialResults.get(1)'s result sender: " + civilInitialResults.get(1).getResultsSender());
		
		rapbackDAO.deleteCivilInitialResults("000001820140729014008339990", ResultSender.FBI); 
		List<CivilInitialResults> stateCivilInitialResults = rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339990");
		assertThat(stateCivilInitialResults.size(), is(1)); 
		assertThat(stateCivilInitialResults.get(0).getRapsheets().size(), is(2)); 
		assertThat(stateCivilInitialResults.get(0).getResultsSender(), is(ResultSender.State)); 
		
		rapbackDAO.deleteCivilInitialResults("000001820140729014008339990", ResultSender.State); 
		List<CivilInitialResults> emptyCivilInitialResults = rapbackDAO.getIdentificationCivilInitialResults("000001820140729014008339990");
		assertThat(emptyCivilInitialResults.size(), is(0)); 
	}
	
	@Test
	public void testDeleteCriminalInitialResults() throws Exception {
		List<CriminalInitialResults> criminalInitialResults = rapbackDAO.getIdentificationCriminalInitialResults("000001820140729014008339991");
		assertThat(criminalInitialResults.size(), is(2)); 
		log.info("civilInitialResults.get(0)'s result sender: " + criminalInitialResults.get(0).getResultsSender());
		log.info("civilInitialResults.get(1)'s result sender: " + criminalInitialResults.get(1).getResultsSender());
		
		rapbackDAO.deleteCriminalInitialResults("000001820140729014008339991", ResultSender.FBI); 
		List<CriminalInitialResults> stateCriminalInitialResults = rapbackDAO.getIdentificationCriminalInitialResults("000001820140729014008339991");
		assertThat(stateCriminalInitialResults.size(), is(1)); 
		assertThat(stateCriminalInitialResults.get(0).getResultsSender(), is(ResultSender.State)); 
		
		rapbackDAO.deleteCriminalInitialResults("000001820140729014008339991", ResultSender.State); 
		List<CriminalInitialResults> emptyCriminalInitialResults = rapbackDAO.getIdentificationCriminalInitialResults("000001820140729014008339991");
		assertThat(emptyCriminalInitialResults.size(), is(0)); 
	}
	
	@Test
	public void testDeleteNSORResults() throws Exception {
		List<NsorSearchResult>  nsorSearchResults = rapbackDAO.getNsorSearchResults("000001820140729014008339990");
		assertThat(nsorSearchResults.size(), is(2)); 
		assertThat(nsorSearchResults.get(0).getResultsSender(), is(ResultSender.FBI)); 
		assertThat(nsorSearchResults.get(1).getResultsSender(), is(ResultSender.FBI)); 
		
		rapbackDAO.deleteNsorSearchResult("000001820140729014008339990", ResultSender.State); 
		List<NsorSearchResult>  fbiNsorSearchResults = rapbackDAO.getNsorSearchResults("000001820140729014008339990");
		assertThat(fbiNsorSearchResults.size(), is(2)); 
		
		rapbackDAO.deleteNsorSearchResult("000001820140729014008339990", ResultSender.FBI); 
		List<NsorSearchResult>  emptyNsorSearchResults = rapbackDAO.getNsorSearchResults("000001820140729014008339990");
		assertThat(emptyNsorSearchResults.size(), is(0)); 
		
	}
	
	@Test
	public void testDeleteNSORDemographics() throws Exception {
		List<NsorDemographics>  nsorDemographics = rapbackDAO.getNsorDemographics("000001820140729014008339990");
		assertThat(nsorDemographics.size(), is(2)); 
		assertThat(nsorDemographics.get(0).getResultsSender(), is(ResultSender.FBI)); 
		assertThat(nsorDemographics.get(1).getResultsSender(), is(ResultSender.FBI)); 
		
		rapbackDAO.deleteNsorDemographics("000001820140729014008339990", ResultSender.State); 
		List<NsorDemographics>  fbiNsorDemographics = rapbackDAO.getNsorDemographics("000001820140729014008339990");
		assertThat(fbiNsorDemographics.size(), is(2)); 
		assertThat(fbiNsorDemographics.get(0).getResultsSender(), is(ResultSender.FBI)); 
		assertThat(fbiNsorDemographics.get(1).getResultsSender(), is(ResultSender.FBI)); 
		
		rapbackDAO.deleteNsorDemographics("000001820140729014008339990", ResultSender.FBI); 
		List<NsorDemographics>  emptyNsorDemographics = rapbackDAO.getNsorDemographics("000001820140729014008339990");
		assertThat(emptyNsorDemographics.size(), is(0)); 
		
	}
	
}
