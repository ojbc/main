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
package org.ojbc.web.portal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	private Map<String, String> daCaseTypeCodeMapping = new HashMap<>();
	private Map<String, String> daGeneralOffenseCodeMapping;
	private Map<String, String> daGeneralOffenseDescMapping; 
	private String restServiceBaseUrl = "http://localhost:9898";
	private String externalTemplatesFolder ;
	private List<String> dispoCodesRequiringSentence;
	private List<String> dispoCodesRequiringAmendedCharge;
	private List<String> dispoCodesNotRequiringChargeSeverity;

	private Boolean allowQueriesWithoutSAMLToken = true; 
	private Boolean inactivityTimeout=true;
	private Integer inactivityTimeoutInSeconds = 1800; 
	private Integer arrestSearchDateRange = 30; 
	
	private Integer ajpPort = 9090; 
	private Boolean ajpEnabled = true; 
	private String submitArrestConfirmationMessage="You are about to submit this arrest to OSBI.  "
			+ "If you would like to edit this arrest in the future, you will need to use the \"Arrest Lookup\" functionality in this tool";
	private String signOutUrl = "/logoutSuccess"; 
	
	/*
	 * demo user Saml attributes
	 */
	private String employerOrganizationCategoryText;
	private String demoSamlAttributeEmployerOri;
	private String demoSamlAttributeEmailAddressText;
	
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
		setDispoCodesNotRequiringChargeSeverity(Arrays.asList("390"));
		
		chargeSeverityCodeMapping.put("F", "Felony");
		chargeSeverityCodeMapping.put("M", "Misdemeanor");
		
		daCaseTypeCodeMapping.put("F", "Felony");
		daCaseTypeCodeMapping.put("M", "Misdemeanor");
		daCaseTypeCodeMapping.put("T", "Traffic");
		daCaseTypeCodeMapping.put("W", "Wildlife");
		daCaseTypeCodeMapping.put("Y", "Youth");
		
		Map<String, String> generalOffenseCodeMapping = new HashMap<String, String>();
		generalOffenseCodeMapping.put("A", "Attempted");
		generalOffenseCodeMapping.put("V", "After Felony Conviction");
		setDaGeneralOffenseCodeMapping(generalOffenseCodeMapping);
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

	public Boolean getAjpEnabled() {
		return ajpEnabled;
	}

	public void setAjpEnabled(Boolean ajpEnabled) {
		this.ajpEnabled = ajpEnabled;
	}

	public Integer getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(Integer ajpPort) {
		this.ajpPort = ajpPort;
	}

	public Map<String, String> getDaCaseTypeCodeMapping() {
		return daCaseTypeCodeMapping;
	}

	public void setDaCaseTypeCodeMapping(Map<String, String> daCaseTypeCodeMapping) {
		this.daCaseTypeCodeMapping = daCaseTypeCodeMapping;
	}

	public String getSubmitArrestConfirmationMessage() {
		return submitArrestConfirmationMessage;
	}

	public void setSubmitArrestConfirmationMessage(String submitArrestConfirmationMessage) {
		this.submitArrestConfirmationMessage = submitArrestConfirmationMessage;
	}

	public String getExternalTemplatesFolder() {
		return externalTemplatesFolder;
	}

	public void setExternalTemplatesFolder(String externalTemplatesFolder) {
		this.externalTemplatesFolder = externalTemplatesFolder;
	}

	public List<String> getDispoCodesNotRequiringChargeSeverity() {
		return dispoCodesNotRequiringChargeSeverity;
	}

	public void setDispoCodesNotRequiringChargeSeverity(List<String> dispoCodesNotRequiringChargeSeverity) {
		this.dispoCodesNotRequiringChargeSeverity = dispoCodesNotRequiringChargeSeverity;
	}

	public Map<String, String> getDaGeneralOffenseCodeMapping() {
		return daGeneralOffenseCodeMapping;
	}

	public void setDaGeneralOffenseCodeMapping(Map<String, String> daGeneralOffenseCodeMapping) {
		this.daGeneralOffenseCodeMapping = daGeneralOffenseCodeMapping;
		
		daGeneralOffenseDescMapping = new HashMap<>();
		for (Entry<String, String> entry: daGeneralOffenseCodeMapping.entrySet()) {
			daGeneralOffenseDescMapping.put(entry.getValue(), entry.getKey());
		}
	}

	public Map<String, String> getDaGeneralOffenseDescMapping() {
		return daGeneralOffenseDescMapping;
	}

	public void setDaGeneralOffenseDescMapping(Map<String, String> daGeneralOffenseDescMapping) {
		this.daGeneralOffenseDescMapping = daGeneralOffenseDescMapping;
	}

	public String getSignOutUrl() {
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}

	public Boolean getInactivityTimeout() {
		return inactivityTimeout;
	}

	public void setInactivityTimeout(Boolean inactivityTimeout) {
		this.inactivityTimeout = inactivityTimeout;
	}

	public Integer getInactivityTimeoutInSeconds() {
		return inactivityTimeoutInSeconds;
	}

	public void setInactivityTimeoutInSeconds(Integer inactivityTimeoutInSeconds) {
		this.inactivityTimeoutInSeconds = inactivityTimeoutInSeconds;
	}

	public String getEmployerOrganizationCategoryText() {
		return employerOrganizationCategoryText;
	}

	public void setEmployerOrganizationCategoryText(String employerOrganizationCategoryText) {
		this.employerOrganizationCategoryText = employerOrganizationCategoryText;
	}

	public String getDemoSamlAttributeEmployerOri() {
		return demoSamlAttributeEmployerOri;
	}

	public void setDemoSamlAttributeEmployerOri(String demoSamlAttributeEmployerOri) {
		this.demoSamlAttributeEmployerOri = demoSamlAttributeEmployerOri;
	}

	public String getDemoSamlAttributeEmailAddressText() {
		return demoSamlAttributeEmailAddressText;
	}

	public void setDemoSamlAttributeEmailAddressText(String demoSamlAttributeEmailAddressText) {
		this.demoSamlAttributeEmailAddressText = demoSamlAttributeEmailAddressText;
	}

	public Integer getArrestSearchDateRange() {
		return arrestSearchDateRange;
	}

	public void setArrestSearchDateRange(Integer arrestSearchDateRange) {
		this.arrestSearchDateRange = arrestSearchDateRange;
	}

}