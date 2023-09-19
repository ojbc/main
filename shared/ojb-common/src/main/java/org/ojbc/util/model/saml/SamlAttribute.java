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
package org.ojbc.util.model.saml;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum SamlAttribute{
    SurName("gfipm:2.0:user:SurName", StringUtils.EMPTY),
    FederatedQueryUserIndicator("gfipm:ext:user:FederatedQueryUserIndicator", 
    		"ATG_HCJDC_HIJIS_FederatedQueryUserIndicator"),
    EmployerName("gfipm:2.0:user:EmployerName", StringUtils.EMPTY),
    EmployeePositionName("gfipm:2.0:user:EmployeePositionName", StringUtils.EMPTY),
    GivenName("gfipm:2.0:user:GivenName", StringUtils.EMPTY),
    CommonName("gfipm:2.0:user:CommonName", StringUtils.EMPTY),
    CriminalJusticeEmployerIndicator("gfipm:ext:user:CriminalJusticeEmployerIndicator", 
    		"ATG_HCJDC_HIJIS_CriminalJusticeEmployerIndicator"),
    LawEnforcementEmployerIndicator("gfipm:ext:user:LawEnforcementEmployerIndicator", 
    		"ATG_HCJDC_HIJIS_LawEnforcementEmployerIndicator"),
    FederationId("gfipm:2.0:user:FederationId", StringUtils.EMPTY), 
    TelephoneNumber("gfipm:2.0:user:TelephoneNumber", StringUtils.EMPTY), 
    EmployerSubUnitName("gfipm:2.0:user:EmployerSubUnitName", StringUtils.EMPTY), 
    EmailAddressText("gfipm:2.0:user:EmailAddressText", StringUtils.EMPTY), 
    EmployerORI("gfipm:2.0:user:EmployerORI", "ATG_HCJDC_HIJIS_Agency_ORI_"),
    IdentityProviderId("gfipm:2.0:user:IdentityProviderId", StringUtils.EMPTY),
    FirearmsRegistrationRecordsPersonnelIndicator("gfipm:ext:user:FirearmsRegistrationRecordsPersonnelIndicator", 
    		"ATG_HCJDC_HIJIS_FirearmsRegistrationRecordsPersonnelIndicator"),
    SupervisoryRoleIndicator("gfipm:ext:user:SupervisoryRoleIndicator", "ATG_HCJDC_HIJIS_SupervisoryRoleIndicator"),
    EmployerOrganizationCategoryText("gfipm:2.0:user:ext:EmployerOrganizationCategoryText", StringUtils.EMPTY),
    Groups("gfipm:ext:user:Groups",""),
    IncidentAccessIndicator("gfipm:ext:user:IncidentReportingPersonnelIndicator","ATG_HCJDC_HIJIS_IncidentReportingPersonnelIndicator")
    ; 
    
    private String attibuteName; 
    private String groupsAttributeValue; 
    
    private SamlAttribute(String attributeName, String groupsAttributeValue) {
        this.setAttibuteName(attributeName);
        this.setGroupsAttributeValue(groupsAttributeValue);
    }

    public String getAttibuteName() {
        return attibuteName;
    }

    private void setAttibuteName(String attibuteName) {
        this.attibuteName = attibuteName;
    }

	public String getGroupsAttributeValue() {
		return groupsAttributeValue;
	}

	private void setGroupsAttributeValue(String groupsAttributeValue) {
		this.groupsAttributeValue = groupsAttributeValue;
	}
    
	public static List<SamlAttribute> getGroupAttributes(){
		return Arrays.asList(LawEnforcementEmployerIndicator, CriminalJusticeEmployerIndicator, 
				EmployerORI, SupervisoryRoleIndicator, FederatedQueryUserIndicator, IncidentAccessIndicator, 
				FirearmsRegistrationRecordsPersonnelIndicator); 
	}
	
	public static boolean isGroupAttribute(SamlAttribute samlAttribute) {
		return getGroupAttributes().contains(samlAttribute); 
	}
}
