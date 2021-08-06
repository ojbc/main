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
package org.ojbc.policyacknowledgement.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ojbc.bundles.intermediaries.policyacknowledgement.PolicyAcknowledgementServiceIntermediaryApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@CamelSpringBootTest
@SpringBootTest(classes=PolicyAcknowledgementServiceIntermediaryApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@UseAdviceWith
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-policy-acknowledgement.xml",
		})
public class PolicyDAOImplTest {
    @Autowired
    private PolicyDAOImpl policyDAO;
    @Test
    public void test() {
        assertNotNull(policyDAO);
    }
    
    @Test
    public void testGetOutstandingPoliciesForUserWithEmptyFedId() {
        //policyDAO.getOutstandingPoliciesForUser("", "H00000001");  
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.getOutstandingPoliciesForUser("", "H00000001"));
    }
    
    @Test
    public void testGetOutstandingPoliciesForUserWithNullFedId() {
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.getOutstandingPoliciesForUser(null, "H00000001"));
    }
    
    @Test
    public void testGetOutstandingPoliciesForUserWithEmptyOris() {
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris", ""));

    }
    
    @Test
    public void testAcknowledgeOutstandingPoliciesWithEmptyFedId() {
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.acknowledgeOutstandingPolicies("", "H00000001"));

    }
    
    @Test
    public void testAcknowledgeOutstandingPoliciesWithNonExistingFedId() {
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:non-existent", "H00000001"));

    }
    
    @Test
    public void testAcknowledgeOutstandingPoliciesWithEmptyOris() {
        Assertions.assertThrows((IllegalArgumentException.class), () -> policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hpotter", ""));

    }
    
    @Test
    public void testGetOutstandingPoliciesForUser() {
        
        List<Policy> allPolicies = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris", "H00000001"); 
        assertEquals(allPolicies.size(), 3); 
        
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001"); 
        assertEquals(outStandingPoliciesForOwen.size(), 2); 
        assertTrue(outStandingPoliciesForOwen.get(0).getId() == 1); 
        assertTrue(outStandingPoliciesForOwen.get(1).getId() == 3); 
        
        List<Policy> outStandingPoliciesForChawla = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hsimpson", "H00000001"); 
        assertTrue(outStandingPoliciesForChawla.isEmpty()); 
    }
    
    @Test
    @Transactional
    public void testAcknowledgeOutstandingPolicies() {
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001");
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter","H00000001"); 
        assertTrue(outStandingPoliciesForOwen.isEmpty()); 
        
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hsimpson", "H00000001");
        List<Policy> outStandingPoliciesForChawla = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hsimpson","H00000001"); 
        assertTrue(outStandingPoliciesForChawla.isEmpty()); 
    }
}
