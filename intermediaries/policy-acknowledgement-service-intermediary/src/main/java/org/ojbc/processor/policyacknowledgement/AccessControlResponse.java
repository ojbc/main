package org.ojbc.processor.policyacknowledgement;

import java.util.List;

import org.ojbc.policyacknowledgement.dao.Policy;
import org.ojbc.policyacknowledgement.util.JSONUtil;

public class AccessControlResponse {
    private Boolean accessDenied;
    private List<Policy> outstandingPolicies;
    private String accessDecisionResourceUri;

    public AccessControlResponse(List<Policy> outstandingPolicies,
            String accessDecisionResouceUri) {

        setOutstandingPolicies(outstandingPolicies);

        if (outstandingPolicies.isEmpty()) {
            setAccessDenied(false);
        } else {
            setAccessDenied(true);
        }
        
        this.accessDecisionResourceUri = accessDecisionResouceUri;
    }

    public Boolean getAccessDenied() {
        return accessDenied;
    }

    private void setAccessDenied(Boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public List<Policy> getOutstandingPolicies() {
        return outstandingPolicies;
    }

    private void setOutstandingPolicies(List<Policy> outstandingPolicies) {
        this.outstandingPolicies = outstandingPolicies;
    }

    public String getAccessDecisionResourceUri() {
        return accessDecisionResourceUri;
    }

    public void setAccessDecisionResourceUri(String accessDecisionResourceUri) {
        this.accessDecisionResourceUri = accessDecisionResourceUri;
    }

    public String toString() {
        String jsonString = JSONUtil.toJsonString(this); 
        return "\n" + this.getClass().getSimpleName() + ":\n" + jsonString;
    }
}
