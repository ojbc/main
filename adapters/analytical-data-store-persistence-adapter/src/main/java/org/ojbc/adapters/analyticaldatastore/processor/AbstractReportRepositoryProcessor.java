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
package org.ojbc.adapters.analyticaldatastore.processor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ojbc.adapters.analyticaldatastore.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.personid.IdentifierGenerationStrategy;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

public abstract class AbstractReportRepositoryProcessor {

	protected IdentifierGenerationStrategy identifierGenerationStrategy;
	
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
    @Transactional
	public abstract void processReport(Document report) throws Exception;

	protected int savePerson(String personBirthDateAsString, String personRace, String personSex,
			String personIdentifierKey) throws Exception {
		//Save person
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personIdentifierKey);
		
		if (StringUtils.isNotEmpty(personBirthDateAsString))
		{
			Date personBirthDate = DATE_FORMAT.parse(personBirthDateAsString);
			person.setPersonBirthDate(personBirthDate);
		}	
		
		//Get Person Race from code table
		int personRacePk = analyticalDatastoreDAO.returnPersonRaceKeyfromRaceDescription(personRace);
		person.setPersonRaceID(personRacePk);
		//Get Person Sex from code table
		int personSexPk = analyticalDatastoreDAO.returnPersonSexKeyfromSexDescription(personSex);
		person.setPersonSexID(personSexPk);
		
		int personPk = analyticalDatastoreDAO.savePerson(person);
		return personPk;
	}

	public IdentifierGenerationStrategy getIdentifierGenerationStrategy() {
		return identifierGenerationStrategy;
	}

	public void setIdentifierGenerationStrategy(
			IdentifierGenerationStrategy identifierGenerationStrategy) {
		this.identifierGenerationStrategy = identifierGenerationStrategy;
	}

	public AnalyticalDatastoreDAO getAnalyticalDatastoreDAO() {
		return analyticalDatastoreDAO;
	}

	public void setAnalyticalDatastoreDAO(
			AnalyticalDatastoreDAO analyticalDatastoreDAO) {
		this.analyticalDatastoreDAO = analyticalDatastoreDAO;
	}

}
