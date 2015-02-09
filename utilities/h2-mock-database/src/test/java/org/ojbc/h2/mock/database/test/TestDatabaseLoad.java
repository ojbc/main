package org.ojbc.h2.mock.database.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-auditlog.xml",
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
		"classpath:META-INF/spring/h2-mock-database-context-policy-acknowledgement.xml",
		})
@DirtiesContext
public class TestDatabaseLoad {
	
	private static final Log log = LogFactory.getLog(TestDatabaseLoad.class);
	
    @Resource  
    private DataSource auditLogTestDataSource; 
    
    @Resource  
    private DataSource subscriptionDataSource;  
	
    @Resource  
    private DataSource policyAcknowledgementDataSource;  
	
	@Test
	public void testAuditlog() throws Exception {
		
		Connection conn = auditLogTestDataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from AuditLog");
		assertFalse(rs.next());
		
	}

	@Test
	public void testSubscription() throws Exception {
		
		Connection conn = subscriptionDataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from subscription");
		assertTrue(rs.next());
		assertEquals(62720,rs.getInt("id"));
		
	}

	@Test
	public void testPolicyAcknowledgement() throws Exception {
		
		Connection conn = policyAcknowledgementDataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select * from policy where id=1");
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("id"));
		assertEquals("http://ojbc.org/policies/privacy/hawaii/ManualSubscriptionPolicy",rs.getString("policy_uri"));
		
	}

}
