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
package org.ojbc.util.model.rapback;
public enum IdentificationTransactionState {
	/*
	 * The ordinal of the enum is used in the code. DO NOT change the order 
	 * of the instances. 
	 */
	Archived, Available_for_Subscription, Subscribed_State, Subscribed_State_FBI, Subscribed_State_FBI_Pending, Subscribed_State_FBI_Error;
	
	public String toString(){
		switch(this) {
		case Subscribed_State:
			return "Subscribed(State)"; 
		case Subscribed_State_FBI: 
			return "Subscribed(State/FBI)";
		case Subscribed_State_FBI_Pending: 
			return "Subscribed(State/FBI Pending)";
		case Subscribed_State_FBI_Error: 
			return "Subscribed(State/FBI Error)";
		default : 
			return name().replace('_', ' ');
		}
		 
	}
}
