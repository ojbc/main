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
package org.ojbc.adapters.analyticsstaging.custody.processor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.personid.IdentifierGenerationStrategy;
import org.ojbc.adapters.analyticsstaging.custody.service.DescriptionCodeLookupService;
import org.ojbc.adapters.analyticsstaging.custody.util.AnalyticalDataStoreUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractReportRepositoryProcessor {
	private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );

	protected IdentifierGenerationStrategy identifierGenerationStrategy;
	
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
	protected DescriptionCodeLookupService descriptionCodeLookupService; 
	
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
    @Transactional
	public abstract void processReport(@Body Document report, @Header("personUniqueId") String personUniqueId) throws Exception;

    protected int savePerson(Node personNode, String ncPrefix, String jxdmPrefix) throws Exception{
		Map<String, Object> personAttributes = AnalyticalDataStoreUtils.retrieveMapOfPersonAttributes(personNode, 
				ncPrefix, jxdmPrefix);
		log.debug("Person Attributes: " + personAttributes);
		
        String personIdentifierKey = identifierGenerationStrategy.generateIdentifier(personAttributes);
        log.debug("Arrestee person identifier keys: " + personIdentifierKey);

        String personBirthDateAsString = (String)personAttributes.get(IdentifierGenerationStrategy.BIRTHDATE_FIELD);
        String personSex = (String)personAttributes.get(IdentifierGenerationStrategy.SEX_FIELD);
		String personRace = getPersonRace(personNode, jxdmPrefix);	
				

        return savePerson(personBirthDateAsString, personSex, personRace, personIdentifierKey);	

    }

	private String getPersonRace(Node personNode, String jxdmPrefix) throws Exception {
		String personRace=XmlUtils.xPathStringSearch(personNode, jxdmPrefix + ":PersonRaceCode");
		return StringUtils.trimToNull(personRace);
	}
	
	protected int savePerson(String personBirthDateAsString, String personSex, String personRace,
			String personIdentifierKey) throws Exception {
		//Save person
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personIdentifierKey);
		
		if (StringUtils.isNotEmpty(personBirthDateAsString))
		{
			person.setPersonBirthDate(LocalDate.parse(personBirthDateAsString));
		}	
		
		Integer personRacePk = descriptionCodeLookupService.retrieveCode(CodeTable.PersonRace, personRace);
		person.setPersonRaceID(personRacePk);

		//Get Person Sex from code table
		Integer personSexPk = descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, personSex);
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

	public DescriptionCodeLookupService getDescriptionCodeLookupService() {
		return descriptionCodeLookupService;
	}

	public void setDescriptionCodeLookupService(
			DescriptionCodeLookupService descriptionCodeLookupService) {
		this.descriptionCodeLookupService = descriptionCodeLookupService;
	}

}
