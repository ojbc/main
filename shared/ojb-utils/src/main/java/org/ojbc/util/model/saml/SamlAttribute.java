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
    IdentityProviderId("gfipm:2.0:user:IdentityProviderId"); 
    
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
