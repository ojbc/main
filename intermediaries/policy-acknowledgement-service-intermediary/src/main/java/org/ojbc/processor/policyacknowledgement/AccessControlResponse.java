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
package org.ojbc.processor.policyacknowledgement;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ojbc.policyacknowledgement.dao.Policy;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
