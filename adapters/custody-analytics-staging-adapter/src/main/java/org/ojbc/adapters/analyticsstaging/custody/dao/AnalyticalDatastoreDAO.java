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
package org.ojbc.adapters.analyticsstaging.custody.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.ojbc.adapters.analyticsstaging.custody.dao.model.Address;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonRace;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonSex;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;

public interface AnalyticalDatastoreDAO {

	public Integer savePersonSex(PersonSex personSex);
	
	public Integer savePersonRace(PersonRace personRace);
	
	public Integer savePerson(Person person);
	
	public Integer getPersonIdByUniqueId(String uniqueId);
	
	public Integer saveBooking(Booking booking);
	
	public Integer saveCustodyStatusChange(CustodyStatusChange custodyStatusChange);
	
	public void saveBehavioralHealthAssessments(
			final List<BehavioralHealthAssessment> behavioralHealthAssessments);

	public void saveBookingCharges(
			final List<BookingCharge> bookingCharges);
	
	public void saveCustodyStatusChangeCharges(
			final List<CustodyStatusChangeCharge> custodyStatusChangeCharges);
	
	public void deleteBooking(Integer bookingPk);
	
	public Booking getBookingByBookingNumber(String bookingNumber);
	public Integer getBookingIdByBookingNumber(String bookingNumber);
	public Person getPerson(Integer personId);
	public List<BookingCharge> getBookingCharges(Integer bookingId);
	public List<BookingArrest> getBookingArrests(Integer bookingId);
	public List<CustodyStatusChangeArrest> getCustodyStatusChangeArrests(Integer custodyStatusChangeId);
	public List<BehavioralHealthAssessment> getBehavioralHealthAssessments(Integer personId);
	public List<Treatment> getTreatments(Integer behavioralHealthAssessmentId);
	public List<PrescribedMedication> getPrescribedMedication(Integer behavioralHealthAssessmentId);

	public void saveCustodyRelease(CustodyRelease custodyRelease);
	public void saveCustodyRelease(Integer bookingId, LocalDateTime releaseDate, String releaseCondition);
	public CustodyRelease getCustodyReleaseByBookingId(Integer bookingId);
	public CustodyStatusChange getCustodyStatusChangeByReportId(String reportId); 
	public List<CustodyStatusChangeCharge> getCustodyStatusChangeCharges(Integer custodyStatusChangeId);
	
	public Person getPersonByBookingNumber(String bookingNumber);

	public Integer saveBookingArrest(BookingArrest bookingArrest);
	public Integer saveAddress(Address address);
	public Integer saveCustodyStatusChangeArrest(CustodyStatusChangeArrest custodyStatusChangeArrest);

	public Integer saveBehavioralHealthAssessment(BehavioralHealthAssessment assessment);
	
	public Integer getMedicationTypeId(String genericProductIdentification, String medicationTypeDescription);
	
	public Integer saveMedicationType(String genericProductIdentification, String medicationTypeDescription);
	
	public void saveTreatments(final List<Treatment> treatments);
	public void saveBehavioralHealthEvaluations(Integer behavioralHealthAssessmentId, final List<KeyValue> behavioralHealthTypes);
	public void savePrescribedMedications(final List<PrescribedMedication> prescribedMedications);

	public Integer saveBehavioralHealthDiagnosisType(String evaluationDiagnosisDescriptionText);


}
