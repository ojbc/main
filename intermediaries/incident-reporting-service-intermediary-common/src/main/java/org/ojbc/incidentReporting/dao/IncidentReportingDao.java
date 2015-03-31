package org.ojbc.incidentReporting.dao;

import org.ojbc.incidentReporting.dao.model.PersonInvolvement;

/**
 * DAO interface with appropriate methods to store and check for hashed data.
 * This assists with sending the proper person involvements and arrests to 
 * the Notification and Arrest service.
 * 
 */

public interface IncidentReportingDao {

	public PersonInvolvement isThereAPriorPersonInvolvment(String incidentId, String incidentOriginatingSystemUri, String personInvolvementHash, String personInvolvementActivity);
	public void addPersonInvolvement(String incidentId, String incidentOriginatingSystemUri, String personInvolvementHash, String personInvolvementActivity) throws Exception;
}
