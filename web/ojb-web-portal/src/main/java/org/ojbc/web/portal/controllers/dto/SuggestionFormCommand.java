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
package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;

import javax.validation.Valid;

import org.ojbc.web.model.suggestionForm.SuggestionFormRequest;

public class SuggestionFormCommand implements Serializable{
	
	private static final long serialVersionUID = 4217442613107719301L;
	
	@Valid
	private SuggestionFormRequest suggestionFormRequest = new SuggestionFormRequest();

	public SuggestionFormRequest getSuggestionFormRequest() {
		return suggestionFormRequest;
	}

	public void setSuggestionFormRequest(SuggestionFormRequest suggestionFormRequest) {
		this.suggestionFormRequest = suggestionFormRequest;
	}

	@Override
	public String toString() {
		return "SuggestionFormCommand [suggestionFormRequest="
				+ suggestionFormRequest + "]";
	}

}
