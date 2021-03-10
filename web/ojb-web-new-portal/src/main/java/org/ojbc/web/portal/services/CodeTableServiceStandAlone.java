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
package org.ojbc.web.portal.services;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.audit.AuditUser;
import org.ojbc.web.portal.security.UserAttributes;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("standalone")
public class CodeTableServiceStandAlone implements CodeTableService{
	@Resource
	AppProperties appProperties;

	public Map<String, String> getMuniDispositionCodeMap(){
		return appProperties.getDispoCodeMapping();
	}

	public Map<String, String> getMuniFiledChargeCodeMap(){
		return  appProperties.getMuniFiledChargeCodeMapping();
	}
	
	public Map<String, String> getMuniAmendedChargeCodeMap(){
		return appProperties.getMuniAmendedChargeCodeMapping(); 
	}
	
	public Map<String, String> getMuniAlternateSentenceMap(){
		return appProperties.getMuniAlternateSentenceMapping(); 
	}
	
	public Map<String, String> getMuniReasonsForDismissalMap(){
		return appProperties.getMuniReasonForDismissalMapping(); 
	}

	@Override
	public Map<String, String> getDaDispositionCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaFiledChargeCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaAmendedChargeCodeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaAlternateSentenceMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaReasonsForDismissalMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDaProvisions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAgencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAuditActivityTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AuditUser> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAttributes auditUserLoginReturnUserAttributes(AuditUser auditUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodeTableEntry> getCodeTableEntries(String uri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}