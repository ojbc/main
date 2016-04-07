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
package org.ojbc.util.lucene.personid;

import java.util.Map;

/**
 * The interface for objects that generate a unique and consistent identifier for an entity with a set of attributes.
 *
 */
public interface IdentifierGenerationStrategy {
	
	public static final String FIRST_NAME_FIELD = "first-name";
	public static final String LAST_NAME_FIELD = "last-name";
	public static final String MIDDLE_NAME_FIELD = "middle-name";
	public static final String BIRTHDATE_FIELD = "birthdate";
	public static final String SEX_FIELD = "sex";
	public static final String SSN_FIELD = "ssn";
	public static final String ID_FIELD = "identifier";
	
	/**
	 * Generate the identifier for the specified set of attributes
	 * @param attributes the entity attributes for which to generate an identifier
	 * @return the identifier
	 * @throws Exception 
	 */
	public String generateIdentifier(Map<String, Object> attributes) throws Exception;

}
