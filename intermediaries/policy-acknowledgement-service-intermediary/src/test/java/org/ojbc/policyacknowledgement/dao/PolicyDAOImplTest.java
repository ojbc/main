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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.policyacknowledgement.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",
        "classpath:META-INF/spring/extensible-beans.xml",
        "classpath:META-INF/spring/local-osgi-context.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-policy-acknowledgement.xml",
		})
@DirtiesContext
public class PolicyDAOImplTest {
    @Autowired
    private PolicyDAOImpl policyDAO;
    @Test
    public void test() {
        assertNotNull(policyDAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOutstandingPoliciesForUserWithEmptyFedId() {
        policyDAO.getOutstandingPoliciesForUser("", "H00000001");  
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOutstandingPoliciesForUserWithNullFedId() {
        policyDAO.getOutstandingPoliciesForUser(null, "H00000001");  
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOutstandingPoliciesForUserWithEmptyOris() {
        policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris", "");  
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAcknowledgeOutstandingPoliciesWithEmptyFedId() {
        policyDAO.acknowledgeOutstandingPolicies("", "H00000001");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAcknowledgeOutstandingPoliciesWithNonExistingFedId() {
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:non-existent", "H00000001");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAcknowledgeOutstandingPoliciesWithEmptyOris() {
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hpotter", "");
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
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:cchris", "H00000002");
        List<Policy> outStandingPoliciesForChris = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris","H00000002"); 
        assertTrue(outStandingPoliciesForChris.isEmpty()); 
        
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hpotter", "H00000001");
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hpotter","H00000001"); 
        assertTrue(outStandingPoliciesForOwen.isEmpty()); 
        
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:hsimpson", "H00000001");
        List<Policy> outStandingPoliciesForChawla = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:hsimpson","H00000001"); 
        assertTrue(outStandingPoliciesForChawla.isEmpty()); 
    }
}
