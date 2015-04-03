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
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:cchris", "H00000001");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAcknowledgeOutstandingPoliciesWithEmptyOris() {
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:aowen", "");
    }
    
    @Test
    public void testGetOutstandingPoliciesForUser() {
        
        List<Policy> allPolicies = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris", "H00000001"); 
        assertEquals(allPolicies.size(), 3); 
        
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:aowen", "H00000001"); 
        assertEquals(outStandingPoliciesForOwen.size(), 2); 
        assertTrue(outStandingPoliciesForOwen.get(0).getId() == 1); 
        assertTrue(outStandingPoliciesForOwen.get(1).getId() == 3); 
        
        List<Policy> outStandingPoliciesForChawla = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:ychawla", "H00000001"); 
        assertTrue(outStandingPoliciesForChawla.isEmpty()); 
    }
    
    @Test
    @Transactional
    public void testAcknowledgeOutstandingPolicies() {
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:cchris", "H00000002");
        List<Policy> outStandingPoliciesForChris = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:cchris","H00000002"); 
        assertTrue(outStandingPoliciesForChris.isEmpty()); 
        
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:aowen", "H00000001");
        List<Policy> outStandingPoliciesForOwen = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:aowen","H00000001"); 
        assertTrue(outStandingPoliciesForOwen.isEmpty()); 
        
        policyDAO.acknowledgeOutstandingPolicies("HIJIS:IDP:HCJDC:USER:ychawla", "H00000001");
        List<Policy> outStandingPoliciesForChawla = policyDAO.getOutstandingPoliciesForUser("HIJIS:IDP:HCJDC:USER:ychawla","H00000001"); 
        assertTrue(outStandingPoliciesForChawla.isEmpty()); 
    }
}
