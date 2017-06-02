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
package org.ojbc.util.model;

/**
 * This class wraps a property to allow it to be used in a Camel route in a way
 * that is easier to test.  It is difficult to change Camel properties in unit tests:
 * 
 * http://camel.465427.n5.nabble.com/Change-property-in-unit-test-td5758017.html
 * 
 * If not set, this property will return 'true' by default.
 * 
 * 
 * @author yogeshchawla
 *
 */
public class BooleanPropertyWrapper {
	
	public Boolean booleanProperty;
	
	public boolean sendEmailNotificationsToRecipients()
	{
		//We default to true if the property is not set
		if (booleanProperty == null)
		{
			booleanProperty = true;
		}	
		
		return booleanProperty;		
	}

	public Boolean getBooleanProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

}
