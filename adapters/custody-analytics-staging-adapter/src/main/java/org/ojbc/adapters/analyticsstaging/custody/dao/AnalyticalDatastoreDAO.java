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

import java.util.List;

import org.ojbc.adapters.analyticsstaging.custody.dao.model.Agency;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonRace;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PersonSex;

public interface AnalyticalDatastoreDAO {

	public Integer saveAgency(Agency agency);
	
	public Integer savePersonSex(PersonSex personSex);
	
	public Integer savePersonRace(PersonRace personRace);
	
	public Integer savePerson(Person person);
	
	public Integer saveBookingSubject(BookingSubject bookingSubject);
	
	public Integer saveBooking(Booking booking);
	
	public Integer searchForAgenyIDbyAgencyORI(String agencyORI);
	
	public Person getPerson(Integer personId);
	
	public void saveBehavioralHealthAssessments(
			final List<BehavioralHealthAssessment> behavioralHealthAssessments);

	public void saveBookingCharges(
			final List<BookingCharge> bookingCharges);
	
	public void deleteBooking(Integer bookingPk);
	
	public Booking getBookingByBookingReportId(String bookingReportId);
}
