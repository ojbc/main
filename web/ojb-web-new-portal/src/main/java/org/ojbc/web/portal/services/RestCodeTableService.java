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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.web.portal.AppProperties;
import org.ojbc.web.portal.audit.AuditUser;
import org.ojbc.web.portal.security.UserAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"arrest-search"})
public class RestCodeTableService implements CodeTableService{
	private final WebClient webClient;

	@Autowired
	public RestCodeTableService(WebClient.Builder webClientBuilder, AppProperties appProperties) {
		this.webClient = webClientBuilder.baseUrl(appProperties.getRestServiceBaseUrl()).build();
	}
	
	public Map<String, String> getMuniDispositionCodeMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-disposition-codes");
	}

	public Map<String, String> getMuniFiledChargeCodeMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-filed-charge-codes");
	}
	
	public Map<String, String> getMuniAmendedChargeCodeMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-amended-charge-codes");
	}
	
	public Map<String, String> getMuniAlternateSentenceMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-alternate-sentences");
	}
	
	public Map<String, String> getMuniReasonsForDismissalMap(){
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-reasons-for-dismissal");
	}
	
	
	public Map<String, String> getIdDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
		return function.apply(uri)
				.stream()
				.filter(i->!"no description".equalsIgnoreCase(StringUtils.lowerCase(i.getDescription().trim())))
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
					(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getCodeDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
		return function.apply(uri)
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getCode, CodeTableEntry::getDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public Map<String, String> getIdCitationDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
		return function.apply(uri)
				.stream()
				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getCitationDescription, 
						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
	}
	
	public List<CodeTableEntry> getCodeTableEntries(String uri) {
		return this.webClient.get().uri(uri)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
				.block();
	}
	
	public List<AuditUser> getUsers(){
		return this.webClient.get().uri("/criminalhistory/userInformation")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<AuditUser>>() {})
				.defaultIfEmpty(new ArrayList<AuditUser>())
				.block();
	}
	@Override
	public Map<String, String> getDaDispositionCodeMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-disposition-codes");
	}

	@Override
	public Map<String, String> getDaFiledChargeCodeMap() {
		return getIdCitationDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-filed-charge-codes");
	}

	@Override
	public Map<String, String> getDaAmendedChargeCodeMap() {
		return getIdCitationDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-amended-charge-codes");
	}

	@Override
	public Map<String, String> getDaAlternateSentenceMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-alternative-sentences");
	}

	@Override
	public Map<String, String> getDaReasonsForDismissalMap() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-reasons-for-dismissal");
	}

	@Override
	public Map<String, String> getDaProvisions() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/da-provisions");
	}

	@Override
	public Map<String, String> getAuditActivityTypes() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/audit-activity-types");
	}
	
	@Override
	public Map<String, String> getAgencies() {
		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/agencies");
	}

	@Override
	public UserAttributes auditUserLoginReturnUserAttributes(AuditUser auditUser) {
		return this.webClient.post().uri("/criminalhistory/audit-login/return-user-attributes")
				.body(BodyInserters.fromObject(auditUser))
				.retrieve()
				.bodyToMono(UserAttributes.class)
				.block();
	}
	
}