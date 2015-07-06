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
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
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
import org.ojbc.adapters.analyticaldatastore.dao.model.Charge;
import org.ojbc.adapters.analyticaldatastore.dao.model.County;
import org.ojbc.adapters.analyticaldatastore.dao.model.Disposition;
import org.ojbc.adapters.analyticaldatastore.dao.model.DispositionType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonRace;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonSex;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialService;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialServiceParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
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
		agency.setAgencyOri("12345");
		
		int agencyPk = analyticalDatastoreDAOImpl.saveAgency(agency );
		assertEquals(3, agencyPk);
		
		log.debug("Agency primary key: " + agencyPk);

		Incident incident = new Incident();
		
		incident.setIncidentDescriptionText("Incident Description Text");
		incident.setReportingAgencyID(agencyPk);
		incident.setIncidentCaseNumber("999999");
		incident.setRecordType('N');
		incident.setIncidentLocationStreetAddress("Street address");
		incident.setIncidentLocationTown("Town");
		incident.setReportingSystem("RMS");
		
		incident.setIncidentDate(new Date());
		incident.setIncidentTime(new java.sql.Time(incident.getIncidentDate().getTime()));
		
		int incidentPk = analyticalDatastoreDAOImpl.saveIncident(incident);
		assertEquals(1, incidentPk);

		IncidentType incidentType = new IncidentType();
		incidentType.setIncidentID(1);
		incidentType.setIncidentDescriptionText("Incident DescriptionText");
		
		int incidentTypePk = analyticalDatastoreDAOImpl.saveIncidentType(incidentType);
		assertEquals(1, incidentTypePk);
		
		IncidentCircumstance incidentCircumstance = new IncidentCircumstance();
		incidentCircumstance.setIncidentID(1);
		incidentCircumstance.setIncidentCircumstanceText("Incident Circumstance Text");
		
		int incidentCircumstancePk = analyticalDatastoreDAOImpl.saveIncidentCircumstance(incidentCircumstance);
		assertEquals(1, incidentCircumstancePk);


		Person person = returnPerson();
		
		int personPk = analyticalDatastoreDAOImpl.savePerson(person);
		assertEquals(1, personPk);
				
		Arrest arrest = new Arrest();
		
		arrest.setPersonID(personPk);
		arrest.setIncidentID(incidentPk);
		arrest.setArrestingAgencyName("Arresting Agency Name");
		arrest.setArrestDate(new Date());
		arrest.setArrestTime(new java.sql.Time(arrest.getArrestDate().getTime()));
		arrest.setReportingSystem("RMS");
		
		int arrestPk = analyticalDatastoreDAOImpl.saveArrest(arrest);
		assertEquals(1, arrestPk);
		
		Charge charge = new Charge();
		
		charge.setArrestID(arrestPk);
		charge.setOffenseDescriptionText("Offense Description Text");
		
		int chargePk = analyticalDatastoreDAOImpl.saveCharge(charge);
		assertEquals(1, chargePk);
		
	}

	protected Person returnPerson() {
		PersonSex personSex = new PersonSex();
		personSex.setPersonSexDescription("male");
		
		int personSexPk = analyticalDatastoreDAOImpl.savePersonSex(personSex);

		PersonRace personRace = new PersonRace();
		personRace.setPersonRaceDescription("caucasion");
		
		int personRacePk = analyticalDatastoreDAOImpl.savePersonRace(personRace);

		Person person = new Person();
		
		person.setPersonRaceID(personRacePk);
		person.setPersonSexID(personSexPk);
		person.setPersonBirthDate(new Date());
		person.setPersonUniqueIdentifier("123332123123unique");
		return person;
	}

	@Test
	public void testSavePretrial() throws Exception
	{
		AssessedNeed assessedNeed = new AssessedNeed();
		assessedNeed.setAssessedNeedDescription("Assessed Need Description");
		
		int assessedNeedPk = analyticalDatastoreDAOImpl.saveAssessedNeed(assessedNeed);
		assertEquals(6, assessedNeedPk);
		
		PretrialService preTrialService = new PretrialService();
		
		preTrialService.setPretrialServiceDescription("Pretrial Description");
		
		int preTrialPk = analyticalDatastoreDAOImpl.savePreTrialService(preTrialService);
		assertEquals(4, preTrialPk);
		
		County county = new County();
		county.setCountyName("County Name");
		
		int countyTypePk = analyticalDatastoreDAOImpl.saveCounty(county);
		assertEquals(2, countyTypePk);
		
		log.debug("County Type primary key: " + countyTypePk);
		
		PretrialServiceParticipation pretrialServiceParticipation = new PretrialServiceParticipation();
		Person person = returnPerson();
		
		int personPk = analyticalDatastoreDAOImpl.savePerson(person);
		//assertEquals(2, personPk);
		
		pretrialServiceParticipation.setCountyID(countyTypePk);
		pretrialServiceParticipation.setIntakeDate(new Date());
		pretrialServiceParticipation.setPersonID(personPk);
		pretrialServiceParticipation.setRecordType('N');
		pretrialServiceParticipation.setRiskScore(1);
		pretrialServiceParticipation.setArrestIncidentCaseNumber("case12345");
		pretrialServiceParticipation.setArrestingAgencyORI("ORI12345");
		
		int pretrialServiceParticipationPk = analyticalDatastoreDAOImpl.savePretrialServiceParticipation(pretrialServiceParticipation);
		assertEquals(1, pretrialServiceParticipationPk);
		
	}
	
	@Test
	public void testDisposition() throws Exception
	{
		DispositionType dispositionType = new DispositionType();
		
		dispositionType.setDispositionDescription("Disposition Description");
		
		int dispositionTypePk = analyticalDatastoreDAOImpl.saveDispositionType(dispositionType);
		assertEquals(16, dispositionTypePk);
		
		Person person = returnPerson();
		
		int personPk = analyticalDatastoreDAOImpl.savePerson(person);
		//assertEquals(3, personPk);
				
		Disposition disposition = new Disposition();
		
		disposition.setDispositionDate(new Date());
		disposition.setDispositionTypeID(dispositionTypePk);
		disposition.setIncidentCaseNumber("case12345");
		disposition.setIsProbationViolation('N');
		disposition.setIsProbationViolationOnOldCharge('N');
		disposition.setPersonID(personPk);
		disposition.setRecidivismEligibilityDate(new Date());
		disposition.setRecordType('N');
		disposition.setSentenceFineAmount(Float.parseFloat("354.65"));
		disposition.setSentenceTermDays(new BigDecimal(345.25));
		disposition.setArrestingAgencyORI("PD12345678");
		disposition.setInitialChargeCode("Initial Charge Code");
		disposition.setFinalChargeCode("Initial Charge Code");
		
		int dispositionPk = analyticalDatastoreDAOImpl.saveDisposition(disposition);
		assertEquals(1, dispositionPk);

		
	}
	
	@Test
	public void testSearchForAgenyIDbyAgencyORI()
	{
		assertEquals(1,analyticalDatastoreDAOImpl.searchForAgenyIDbyAgencyORI("ST123").intValue());
		
		//The ORI below is undefined
		assertNull(analyticalDatastoreDAOImpl.searchForAgenyIDbyAgencyORI("PD023242342342"));
	}

	
}
