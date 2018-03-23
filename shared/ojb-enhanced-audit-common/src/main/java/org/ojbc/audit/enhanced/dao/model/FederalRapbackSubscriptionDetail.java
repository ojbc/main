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
package org.ojbc.audit.enhanced.dao.model;

import java.util.List;

/**
 * This class will return these details on a state subscription: 
 * 
 * 1. UCN exists in CJIS HI (yes/no)
 * 2. FBI Subscription Request Sent (yes/no)
 * 3. FBI Subscription Created (yes/no – if no, list reason why, this is like message returned in the RBSR response)
 * 4. RBMNT sent to FBI (yes/no)
 * 5. RBMNT confirmed by FBI (yes/no)
 *
 */
public class FederalRapbackSubscriptionDetail {

	private boolean fbiSubscriptionSent;
	private boolean fbiSubscriptionCreated;
	private String fbiSubscriptionErrorText;
	
	private boolean fbiRapbackMaintenanceSent;
	private boolean fbiRapbackMaintenanceConfirmed;
	private String fbiRapbackMaintenanceErrorText;
	
	private List<FederalRapbackSubscription> federalRapbackSubscriptions;
	
	public boolean isFbiSubscriptionSent() {
		return fbiSubscriptionSent;
	}
	public void setFbiSubscriptionSent(boolean fbiSubscriptionSent) {
		this.fbiSubscriptionSent = fbiSubscriptionSent;
	}

	public String getFbiSubscriptionErrorText() {
		return fbiSubscriptionErrorText;
	}
	public void setFbiSubscriptionErrorText(String fbiSubscriptionErrorText) {
		this.fbiSubscriptionErrorText = fbiSubscriptionErrorText;
	}
	public boolean isFbiRapbackMaintenanceSent() {
		return fbiRapbackMaintenanceSent;
	}
	public void setFbiRapbackMaintenanceSent(boolean fbiRapbackMaintenanceSent) {
		this.fbiRapbackMaintenanceSent = fbiRapbackMaintenanceSent;
	}
	public boolean isFbiRapbackMaintenanceConfirmed() {
		return fbiRapbackMaintenanceConfirmed;
	}
	public void setFbiRapbackMaintenanceConfirmed(
			boolean fbiRapbackMaintenanceConfirmed) {
		this.fbiRapbackMaintenanceConfirmed = fbiRapbackMaintenanceConfirmed;
	}
	public String getFbiRapbackMaintenanceErrorText() {
		return fbiRapbackMaintenanceErrorText;
	}
	public void setFbiRapbackMaintenanceErrorText(
			String fbiRapbackMaintenanceErrorText) {
		this.fbiRapbackMaintenanceErrorText = fbiRapbackMaintenanceErrorText;
	}
	public boolean isFbiSubscriptionCreated() {
		return fbiSubscriptionCreated;
	}
	public void setFbiSubscriptionCreated(boolean fbiSubscriptionCreated) {
		this.fbiSubscriptionCreated = fbiSubscriptionCreated;
	}
	public List<FederalRapbackSubscription> getFederalRapbackSubscriptions() {
		return federalRapbackSubscriptions;
	}
	public void setFederalRapbackSubscriptions(
			List<FederalRapbackSubscription> federalRapbackSubscriptions) {
		this.federalRapbackSubscriptions = federalRapbackSubscriptions;
	}

	
}
