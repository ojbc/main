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
package org.ojbc.intermediaries.sn.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.w3c.dom.Document;

@SuppressWarnings("deprecation")
@DirtiesContext
public class RapbackSubscriptionManagerTest extends AbstractSubscriptionNotificationTest {
    private final Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected ModelCamelContext context;
	
    @Resource  
    private DataSource dataSource;  
    
    @Value("${publishSubscribe.subscriptionManagerEndpoint}")
    private String subscriptionManagerUrl;
    
    @Value("${publishSubscribe.notificationBrokerEndpoint}")
    private String notificationBrokerUrl;
    
    @EndpointInject(value="mock:cxf:bean:fbiEbtsSubscriptionRequestService")
    protected MockEndpoint fbiEbtsSubscriptionMockEndpoint; 
    
	@BeforeEach
	public void setUp() throws Exception {
    	AdviceWith.adviceWith(context, "sendToFbiEbtsAdapter", route -> {
	    	route.mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
	    	route.mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionManagerService**");
    	});

    	context.start();
    	
    	DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
        DatabaseOperation.INSERT.execute(getConnection(), getDataSet());
	}
	
	@AfterEach
	public void tearDown() throws Exception {
        //DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}
	
    private IDataSet getDataSet() throws Exception{  
        // get insert data  
    	return new FlatXmlDataSetBuilder().build(new FileInputStream(
				"src/test/resources/xmlInstances/dbUnit/subscriptionDataSet.xml"));
    }  

	private IDatabaseConnection getConnection() throws Exception {
        Connection con = dataSource.getConnection();  
        IDatabaseConnection connection = new DatabaseConnection(con);

        return connection;  
	}

	@Test
	public void unsubscribe() throws Exception {
		//Subscribe new record
		fbiEbtsSubscriptionMockEndpoint.expectedMessageCount(1);
		String response = invokeRequest("rapback/subscribeSoapRequestWithRapbackData.xml", notificationBrokerUrl);
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		fbiEbtsSubscriptionMockEndpoint.assertIsSatisfied();
		
		Exchange receivedExchange = fbiEbtsSubscriptionMockEndpoint.getExchanges().get(0);
		String body = OJBUtils.getStringFromDocument(receivedExchange.getIn().getBody(Document.class));
		log.info("Message to send to the EBTS adapter: " + body );
		XmlTestUtils.compareDocuments("src/test/resources/xmlInstances/rapback/output/subscribeRequestToEBTS.xml", body);

		//Unsubscribe record and confirm with expected dataset
		response = invokeRequest("rapback/unSubscribeSoapRequestRapback.xml", subscriptionManagerUrl);
		assertThat(response, containsString(UNSUBSCRIBE_RESPONSE_ELEMENT_STRING));
	    
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterUnSubscribeRapback.xml");
		
	}

	private ITable getFilteredTableFromDataset(IDataSet dataSet, String tableName) throws Exception {
        ITable table = dataSet.getTable(tableName);
        ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(table, new String[]{"id*", "subscriptionId", "*date", "timestamp"});
        
        return filteredTable;
	}
	
	private void compareDatabaseWithExpectedDataset(String expectedDatasetFileName) throws SQLException,
		Exception, MalformedURLException, DataSetException, DatabaseUnitException {

		// Fetch database data after executing your code
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable filteredActualSubscriptionTable = getFilteredTableFromDataset(databaseDataSet, "subscription");
		ITable filteredActualNotficationMechanismTable = getFilteredTableFromDataset(databaseDataSet, "notification_mechanism");
		ITable filteredActualSubjectIdentiferTable = getFilteredTableFromDataset(databaseDataSet, "subscription_subject_identifier");
		ITable filteredSubscriptionPropertiesTable = getFilteredTableFromDataset(databaseDataSet, "subscription_properties");
		
		
		// Load expected data from an XML dataset
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/xmlInstances/dbUnit/" + expectedDatasetFileName));
		ITable filteredExpectedSubscriptionTable = getFilteredTableFromDataset(expectedDataSet, "subscription");
		ITable filteredExpectedNotficationMechanismTable = getFilteredTableFromDataset(expectedDataSet, "notification_mechanism");
		ITable filteredExpectedSubjectIdentiferTable = getFilteredTableFromDataset(expectedDataSet, "subscription_subject_identifier");
		ITable filteredExpectedSubscriptionPropertiesTable = getFilteredTableFromDataset(expectedDataSet, "subscription_properties");
		
		// Assert actual database table match expected table
		Assertion.assertEquals(filteredExpectedSubscriptionTable, filteredActualSubscriptionTable);
		Assertion.assertEquals(filteredExpectedNotficationMechanismTable, filteredActualNotficationMechanismTable);
		Assertion.assertEquals(filteredExpectedSubjectIdentiferTable, filteredActualSubjectIdentiferTable);
		Assertion.assertEquals(filteredExpectedSubscriptionPropertiesTable, filteredSubscriptionPropertiesTable);
	}
	

    private String invokeRequest(String fileName, String url)
    	throws IOException, Exception {
		File subscriptionInputFile = new File("src/test/resources/xmlInstances/" + fileName);
		String subscriptionBody = FileUtils.readFileToString(subscriptionInputFile);
		String response = callServiceViaHttp(subscriptionBody, url);
		return response;
	}
		
	private String callServiceViaHttp(String msgBody, String url) throws Exception {
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(msgBody));
		HttpResponse response = client.execute(post);
		HttpEntity reply = response.getEntity();
		return IOUtils.readStringFromStream(reply.getContent());
	}
	
}
