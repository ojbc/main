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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
