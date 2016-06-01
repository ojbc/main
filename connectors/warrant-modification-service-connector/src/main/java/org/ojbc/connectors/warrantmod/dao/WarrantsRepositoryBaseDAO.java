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
package org.ojbc.connectors.warrantmod.dao;

import java.util.List;

import org.ojbc.warrant.repository.model.ChargeReferralReport;
import org.ojbc.warrant.repository.model.Person;
import org.ojbc.warrant.repository.model.Warrant;


public interface WarrantsRepositoryBaseDAO {

	public Warrant retrieveWarrant(Integer warrantId);
	
	public List<String> getTransactionControlNumbers(Integer warrantId);
	
	/**
	 * Retrieve Person info along with the Person Vehicle info 
	 * @param warrantId
	 * @return List of Persons with PersonVehicle list populated.
	 */
	public List<Person> retrievePersons(Integer warrantId);
	
}
