package org.ojbc.policyacknowledgement.dao;

import java.util.List;

public interface PolicyDAO {
	/**
	 * @param federationId
	 * @return list of outstanding policies for user with the federationId. 
	 */
	public List<Policy> getOutstandingPoliciesForUser(String federationId, String ori);
	/**
	 * <pre>
	 * 1. Update acknowledge date for the newly updated policies which were acknowledged before. 
	 * 2. Insert new rows into to the joiner table for the new policies which were never acknowledged by the user. </pre> 
	 * @param federationId
	 */
	public void acknowledgeOutstandingPolicies(String federationId, String ori); 
	
	public boolean isExistingUser(String federationId); 
}
