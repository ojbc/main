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
package org.ojbc.web.portal.arrest;

import org.w3c.dom.Element;

public interface ArrestService {

	String findArrests(ArrestSearchRequest arrestSearchRequest, Element samlToken) throws Throwable;
	String getArrest(ArrestDetailSearchRequest arrestDetailSearchRequest, Element samlToken) throws Throwable;
	String hideArrest(String id, Element samlToken) throws Throwable;
	String unhideArrest(String id, Element samlToken) throws Throwable;
	String saveDisposition(Disposition disposition, Element samlToken) throws Throwable;
	String expungeDisposition(Disposition disposition, Element samlToken) throws Throwable;
	String deleteDisposition(Disposition disposition, Element samlAssertion) throws Throwable;
	String referArrestToDa(String id, Element samlAssertion) throws Throwable;
	String referArrestToMuni(String id, Element samlAssertion) throws Throwable;
	String lookupOtn(String otn, Element samlAssertion) throws Throwable;
	String finalizeArrest(String id, String[] chargeIds, Element samlAssertion) throws Throwable;
	String declineCharge(ArrestCharge arrestCharge, Element samlAssertion) throws Throwable;
}