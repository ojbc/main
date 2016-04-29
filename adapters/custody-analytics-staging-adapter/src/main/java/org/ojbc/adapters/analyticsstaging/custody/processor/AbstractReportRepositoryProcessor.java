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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.service.DescriptionCodeLookupService;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractReportRepositoryProcessor {
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );

	@Autowired
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
	@Autowired
	protected DescriptionCodeLookupService descriptionCodeLookupService; 
	
    @Transactional
	public abstract void processReport(@Body Document report, @Header("personUniqueId") String personUniqueId) throws Exception;

	protected Integer savePerson(Node personNode, String personUniqueIdentifier) throws Exception {
		
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personUniqueIdentifier);
		
		String personRace=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonRaceCode");
		person.setPersonRaceDescription(personRace);
		person.setPersonRaceID(descriptionCodeLookupService.retrieveCode(CodeTable.PersonRace, personRace));
		
		String personSex=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonSexCode");
		person.setPersonSexDescription(personSex);
		person.setPersonSexID(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, personSex));
		
		String personBirthDate = XmlUtils.xPathStringSearch(personNode, "nc30:PersonBirthDate/nc30:Date");
		person.setPersonBirthDate(LocalDate.parse(personBirthDate));
		
		String language = XmlUtils.xPathStringSearch(personNode, "nc30:PersonPrimaryLanguage/nc30:LanguageName");
		person.setLanguage(language);
		person.setLanguageId(descriptionCodeLookupService.retrieveCode(CodeTable.Language, language));
		
		Integer personId = analyticalDatastoreDAO.savePerson(person);
		
		return personId;
	}

	protected Integer saveBookingSubject(Node personNode, String personUniqueIdentifier) throws Exception {
		BookingSubject bookingSubject = new BookingSubject();

		Integer personId = analyticalDatastoreDAO.getPersonIdByUniqueId(personUniqueIdentifier);

		if (personId != null){
			bookingSubject.setRecidivistIndicator(1);
		}
		else{
			personId = savePerson(personNode, personUniqueIdentifier);
		}
		
		bookingSubject.setPersonId(personId);
		
		String birthDateString = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonBirthDate/nc30:Date");
	 	java.time.LocalDate birthDay = java.time.LocalDate.parse(birthDateString);
	 	long age = birthDay.until(java.time.LocalDate.now(), ChronoUnit.YEARS );
	 	bookingSubject.setPersonAge(Long.valueOf(age).intValue());
	 	
	 	String educationLevel = XmlUtils.xPathStringSearch(personNode, "nc30:PersonEducationLevelText");
	 	if(StringUtils.isNotBlank(educationLevel)){
		 	Integer educationLevelId = descriptionCodeLookupService.retrieveCode(CodeTable.EducationLevel, StringUtils.trim(educationLevel));
		 	bookingSubject.setEducationLevelId(educationLevelId);
	 	}
	 	
	 	String occupation = XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonAugmentation/nc30:EmployeeOccupationCategoryText");
	 	if (StringUtils.isNotBlank(occupation)){
	 		Integer occupationId = descriptionCodeLookupService.retrieveCode(CodeTable.Occupation, StringUtils.trim(occupation));
	 		bookingSubject.setOccupationId(occupationId);
	 	}
	 	
	 	String incomeLevel = XmlUtils.xPathStringSearch(personNode, "br-ext:PersonSocioEconomicStatusDescriptionText");
	 	if (StringUtils.isNotBlank(incomeLevel)){
	 		Integer incomeLevelId = descriptionCodeLookupService.retrieveCode(CodeTable.IncomeLevel, StringUtils.trim(incomeLevel));
	 		bookingSubject.setIncomeLevelId(incomeLevelId);
	 	}
	 	
	 	String housingStatus = XmlUtils.xPathStringSearch(personNode, "nc30:PersonResidentText");
	 	if(StringUtils.isNotBlank( housingStatus )){
	 		Integer housingStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.HousingStatus, StringUtils.trim(housingStatus));
	 		bookingSubject.setHousingStatusId(housingStatusId);
	 	}
	 	
		Integer bookingSubjectId = analyticalDatastoreDAO.saveBookingSubject(bookingSubject);
		return bookingSubjectId;
	}


}
