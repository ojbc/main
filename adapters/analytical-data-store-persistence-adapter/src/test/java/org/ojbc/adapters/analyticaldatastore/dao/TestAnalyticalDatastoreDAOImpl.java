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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticaldatastore.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.analyticaldatastore.dao.model.Agency;
import org.ojbc.adapters.analyticaldatastore.dao.model.Arrest;
import org.ojbc.adapters.analyticaldatastore.dao.model.AssessedNeed;
import org.ojbc.adapters.analyticaldatastore.dao.model.County;
import org.ojbc.adapters.analyticaldatastore.dao.model.DispositionType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.dao.model.IncidentType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonRace;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonSex;
import org.ojbc.adapters.analyticaldatastore.dao.model.PreTrialService;
import org.ojbc.adapters.analyticaldatastore.dao.model.RiskScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/properties-context.xml"
		})
@DirtiesContext
public class TestAnalyticalDatastoreDAOImpl {

	private static final Log log = LogFactory.getLog(TestAnalyticalDatastoreDAOImpl.class);
	
    @Resource  
    private DataSource dataSource;  
	
	@Autowired
	private AnalyticalDatastoreDAOImpl analyticalDatastoreDAOImpl;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(analyticalDatastoreDAOImpl);
		Assert.assertNotNull(dataSource);
	}
		
	@Test
	public void testSaveIncidentArrest() throws Exception
	{
		Agency agency = new Agency();
		agency.setAgencyName("Some PD");
		
		int agencyPk = analyticalDatastoreDAOImpl.saveAgency(agency );
		assertEquals(1, agencyPk);
		
		log.debug("Agency primary key: " + agencyPk);

		IncidentType incidentType = new IncidentType();
		incidentType.setIncidentTypeDescription("Incident Type Description");
		
		int incidentTypePk = analyticalDatastoreDAOImpl.saveIncidentType(incidentType);
		assertEquals(1, incidentTypePk);
		
		log.debug("Incident Type primary key: " + incidentTypePk);

		County county = new County();
		county.setCountyName("County Name");
		
		int countyTypePk = analyticalDatastoreDAOImpl.saveCounty(county);
		assertEquals(1, countyTypePk);
		
		log.debug("County Type primary key: " + countyTypePk);
		
		Incident incident = new Incident();
		
		incident.setIncidentTypeID(incidentTypePk);
		incident.setReportingAgencyID(agencyPk);
		incident.setCountyID(countyTypePk);
		incident.setIncidentCaseNumber("999999");
		incident.setRecordType('N');
		incident.setIncidentLocationStreetAddress("Street address");
		incident.setIncidentLocationTown("Town");
		
		incident.setIncidentDate(new Date());
		incident.setIncidentTime(new java.sql.Time(incident.getIncidentDate().getTime()));
		
		int incidentPk = analyticalDatastoreDAOImpl.saveIncident(incident);
		assertEquals(1, incidentPk);

		PersonSex personSex = new PersonSex();
		personSex.setPersonSexDescription("male");
		
		int personSexPk = analyticalDatastoreDAOImpl.savePersonSex(personSex);
		assertEquals(1, personSexPk);

		PersonRace personRace = new PersonRace();
		personRace.setPersonRaceDescription("caucasion");
		
		int personRacePk = analyticalDatastoreDAOImpl.savePersonRace(personRace);
		assertEquals(1, personRacePk);

		Person person = new Person();
		
		person.setPersonRaceID(personRacePk);
		person.setPersonSexID(personSexPk);
		person.setPersonBirthDate(new Date());
		person.setPersonUniqueIdentifier("123332123123unique");
		
		int personPk = analyticalDatastoreDAOImpl.savePerson(person);
		assertEquals(1, personPk);

		
		Arrest arrest = new Arrest();
		
		arrest.setPersonID(personPk);
		arrest.setIncidentID(incidentPk);
		arrest.setArrestingAgencyID(agencyPk);
		arrest.setArrestDate(new Date());
		arrest.setArrestDrugRelated('Y');
		arrest.setArrestTime(new java.sql.Time(arrest.getArrestDate().getTime()));
		
		int arrestPk = analyticalDatastoreDAOImpl.saveArrest(arrest);
		assertEquals(1, arrestPk);

		
	}

	@Test
	public void testSavePretrial() throws Exception
	{
		AssessedNeed assessedNeed = new AssessedNeed();
		assessedNeed.setAssessedNeedDescription("Assessed Need Description");
		
		int assessedNeedPk = analyticalDatastoreDAOImpl.saveAssessedNeed(assessedNeed);
		assertEquals(1, assessedNeedPk);
		
		RiskScore riskScore = new RiskScore();
		riskScore.setRiskScoreDescription("Risk Score Description");
		
		int riskScorePk = analyticalDatastoreDAOImpl.saveRiskScore(riskScore);
		assertEquals(1, riskScorePk);
		
		PreTrialService preTrialService = new PreTrialService();
		
		preTrialService.setIsParticipant("YES");
		preTrialService.setPretrialServiceDescription("Pretrial Description");
		
		int preTrialPk = analyticalDatastoreDAOImpl.savePreTrialService(preTrialService);
		assertEquals(1, preTrialPk);
		
	}
	
	@Test
	public void testDisposition() throws Exception
	{
		DispositionType dispositionType = new DispositionType();
		
		dispositionType.setDispositionDescription("Disposition Description");
		dispositionType.setIsConviction('Y');
		
		int dispositionTypePk = analyticalDatastoreDAOImpl.saveDispositionType(dispositionType);
		assertEquals(1, dispositionTypePk);

	}

}
