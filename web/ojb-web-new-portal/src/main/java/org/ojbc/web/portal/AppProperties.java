/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ojbc.web.portal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("portal")
public class AppProperties {

	private Map<String, String> dispoCodeMapping = new HashMap<>();
	private Map<String, String> muniFiledChargeCodeMapping = new HashMap<>();
	private Map<String, String> muniAmendedChargeCodeMapping = new HashMap<>();
	private Map<String, String> muniAlternateSentenceMapping = new HashMap<>();
	private Map<String, String> muniReasonForDismissalMapping = new HashMap<>();
	private Map<String, String> provisionCodeMapping = new HashMap<>();
	private Map<String, String> chargeSeverityCodeMapping = new HashMap<>();
	private String restServiceBaseUrl = "http://localhost:9898";
	private List<String> dispoCodesRequiringSentence;
	private List<String> dispoCodesRequiringAmendedCharge;

	private Boolean allowQueriesWithoutSAMLToken = true; 
	
	public AppProperties() {
		super();
		dispoCodeMapping.put("301", "Acquitted");
		dispoCodeMapping.put("305", "Charge Dismissed");
		dispoCodeMapping.put("310", "Found Guilty");
		dispoCodeMapping.put("326", "Municipal Prosecutor Declines to File");
		dispoCodeMapping.put("356", "Pled Nolo Contendere");
		dispoCodeMapping.put("357", "Guilty Plea");
		dispoCodeMapping.put("376", "Pled Guilty of Other Charge");
		dispoCodeMapping.put("377", "Pled Not Guilty, Case Dismissed");
		dispoCodeMapping.put("520", "Pled Nolo Contendere to Different Charge");
		dispoCodeMapping.put("331", "Bond Forfeiture");
		dispoCodeMapping.put("538", "Failure to Appear and/or Pay");
		dispoCodeMapping.put("348", "Found Guilty of Other Charge");
		dispoCodeMapping.put("380", "Deferred Judgement accelerated");
		dispoCodeMapping.put("388", "Sentence Revoked");
		dispoCodeMapping.put("398", "Charges Filed");
		
		setDispoCodesRequiringSentence(Arrays.asList("310", "348", "356", "357", "376", "380", "384", "388", "503", "520", "525"));
		setDispoCodesRequiringAmendedCharge(Arrays.asList("376", "520", "348"));
		
		chargeSeverityCodeMapping.put("F", "Felony");
		chargeSeverityCodeMapping.put("M", "Misdemeanor");
		chargeSeverityCodeMapping.put("E", "Either");
	}

	public Map<String, String> getDispoCodeMapping() {
		return dispoCodeMapping;
	}

	public void setDispoCodeMapping(Map<String, String> dispoCodeMapping) {
		this.dispoCodeMapping = dispoCodeMapping;
	}

	public String getRestServiceBaseUrl() {
		return restServiceBaseUrl;
	}

	public void setRestServiceBaseUrl(String restServiceBaseUrl) {
		this.restServiceBaseUrl = restServiceBaseUrl;
	}

	public Map<String, String> getMuniFiledChargeCodeMapping() {
		return muniFiledChargeCodeMapping;
	}

	public void setMuniFiledChargeCodeMapping(Map<String, String> muniFiledChargeCodeMapping) {
		this.muniFiledChargeCodeMapping = muniFiledChargeCodeMapping;
	}

	public Map<String, String> getMuniAmendedChargeCodeMapping() {
		return muniAmendedChargeCodeMapping;
	}

	public void setMuniAmendedChargeCodeMapping(Map<String, String> muniAmendedChargeCodeMapping) {
		this.muniAmendedChargeCodeMapping = muniAmendedChargeCodeMapping;
	}

	public Map<String, String> getMuniAlternateSentenceMapping() {
		return muniAlternateSentenceMapping;
	}

	public void setMuniAlternateSentenceMapping(Map<String, String> muniAlternateSentenceMapping) {
		this.muniAlternateSentenceMapping = muniAlternateSentenceMapping;
	}

	public Map<String, String> getMuniReasonForDismissalMapping() {
		return muniReasonForDismissalMapping;
	}

	public void setMuniReasonForDismissalMapping(Map<String, String> muniReasonForDismissalMapping) {
		this.muniReasonForDismissalMapping = muniReasonForDismissalMapping;
	}

	public List<String> getDispoCodesRequiringSentence() {
		return dispoCodesRequiringSentence;
	}

	public void setDispoCodesRequiringSentence(List<String> dispoCodesRequiringSentence) {
		this.dispoCodesRequiringSentence = dispoCodesRequiringSentence;
	}

	public List<String> getDispoCodesRequiringAmendedCharge() {
		return dispoCodesRequiringAmendedCharge;
	}

	public void setDispoCodesRequiringAmendedCharge(List<String> dispoCodesRequiringAmendedCharge) {
		this.dispoCodesRequiringAmendedCharge = dispoCodesRequiringAmendedCharge;
	}

	public Map<String, String> getProvisionCodeMapping() {
		return provisionCodeMapping;
	}

	public void setProvisionCodeMapping(Map<String, String> provisionCodeMapping) {
		this.provisionCodeMapping = provisionCodeMapping;
	}

	public Boolean getAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}

	public void setAllowQueriesWithoutSAMLToken(Boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

	public Map<String, String> getChargeSeverityCodeMapping() {
		return chargeSeverityCodeMapping;
	}

	public void setChargeSeverityCodeMapping(Map<String, String> chargeSeverityCodeMapping) {
		this.chargeSeverityCodeMapping = chargeSeverityCodeMapping;
	}
}