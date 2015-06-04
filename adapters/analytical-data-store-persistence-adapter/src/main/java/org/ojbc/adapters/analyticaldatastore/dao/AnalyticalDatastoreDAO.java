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

import java.util.List;

import org.ojbc.adapters.analyticaldatastore.dao.model.Agency;
import org.ojbc.adapters.analyticaldatastore.dao.model.Arrest;
import org.ojbc.adapters.analyticaldatastore.dao.model.AssessedNeed;
import org.ojbc.adapters.analyticaldatastore.dao.model.Charge;
import org.ojbc.adapters.analyticaldatastore.dao.model.County;
import org.ojbc.adapters.analyticaldatastore.dao.model.Disposition;
import org.ojbc.adapters.analyticaldatastore.dao.model.DispositionType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.dao.model.IncidentType;
import org.ojbc.adapters.analyticaldatastore.dao.model.InvolvedDrug;
import org.ojbc.adapters.analyticaldatastore.dao.model.OffenseType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Person;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonRace;
import org.ojbc.adapters.analyticaldatastore.dao.model.PersonSex;
import org.ojbc.adapters.analyticaldatastore.dao.model.PreTrialService;
import org.ojbc.adapters.analyticaldatastore.dao.model.PretrialServiceParticipation;

public interface AnalyticalDatastoreDAO {

	public int saveAgency(Agency agency);
	
	public int saveIncidentType(IncidentType incidentType);
	
	public int saveIncident(Incident incident);
	
	public int saveCounty(County county);

	public int saveArrest(Arrest arrest);
	
	public int saveAssessedNeed(AssessedNeed assesedNeed);
	
	public int savePreTrialService(PreTrialService preTrialService);
	
	public int saveDispositionType(DispositionType dispositionType);
	
	public int savePersonSex(PersonSex personSex);
	
	public int savePersonRace(PersonRace personRace);
	
	public int savePerson(Person person);
	
	public int savePretrialServiceParticipation(PretrialServiceParticipation pretrialServiceParticipation);
	
	public int saveOffenseType(OffenseType offenseType);
	
	public int saveCharge(Charge charge);
	
	public int saveDisposition(Disposition disposition);
	
	public int saveInvolvedDrug(InvolvedDrug involvedDrug);
	
	public List<Incident> searchForIncidentsByIncidentNumber(String incidentNumber);
	
	public List<Arrest> searchForArrestsByIncidentPk(int incidentPk);
	
	public int returnPersonSexKeyfromSexDescription(String personSexDescription);
	
	public int returnPersonRaceKeyfromRaceDescription(String personRaceDescription);
	
	//TODO: Update this to use ORI when ORI added to data model
	public int returnAgencyKeyfromAgencyName(String agencyName);
	
	public int returnIncidentTypeKeyfromIncidentTypeDescription(String incidentTypeDescription);
}
