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

public enum SamlAttribute{
    SurName("gfipm:2.0:user:SurName"),
    FederatedQueryUserIndicator("gfipm:ext:user:FederatedQueryUserIndicator"),
    EmployerName("gfipm:2.0:user:EmployerName"),
    EmployeePositionName("gfipm:2.0:user:EmployeePositionName"),
    GivenName("gfipm:2.0:user:GivenName"),
    CommonName("gfipm:2.0:user:CommonName"),
    CriminalJusticeEmployerIndicator("gfipm:ext:user:CriminalJusticeEmployerIndicator"),
    LawEnforcementEmployerIndicator("gfipm:ext:user:LawEnforcementEmployerIndicator"),
    FederationId("gfipm:2.0:user:FederationId"), 
    TelephoneNumber("gfipm:2.0:user:TelephoneNumber"), 
    EmployerSubUnitName("gfipm:2.0:user:EmployerSubUnitName"), 
    EmailAddressText("gfipm:2.0:user:EmailAddressText"), 
    EmployerORI("gfipm:2.0:user:EmployerORI"),
    IdentityProviderId("gfipm:2.0:user:IdentityProviderId"),
    FirearmsRegistrationRecordsPersonnelIndicator("gfipm:ext:user:FirearmsRegistrationRecordsPersonnelIndicator"),
    SupervisoryRoleIndicator("gfipm:ext:user:SupervisoryRoleIndicator"),
    ; 
    
    private String attibuteName; 
    
    private SamlAttribute(String attributeName) {
        this.setAttibuteName(attributeName);
    }

    public String getAttibuteName() {
        return attibuteName;
    }

    public void setAttibuteName(String attibuteName) {
        this.attibuteName = attibuteName;
    }
    
}
