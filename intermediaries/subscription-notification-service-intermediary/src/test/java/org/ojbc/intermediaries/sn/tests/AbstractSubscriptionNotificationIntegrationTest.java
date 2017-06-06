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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.io.FileUtils;
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
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.HttpUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class AbstractSubscriptionNotificationIntegrationTest extends AbstractSubscriptionNotificationTest {

	private static final int MAIL_READ_DELAY = 3000;
	public static final String CXF_OPERATION_NAME_SUBSCRIBE = "Subscribe";
	public static final String CXF_OPERATION_NAMESPACE = "http://docs.oasis-open.org/wsn/brw-2";
	@Resource
	protected DataSource dataSource;
	@Resource
	protected SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	@Resource
	protected ModelCamelContext context;
	@Value("${publishSubscribe.notificationBrokerEndpoint}")
	protected String notificationBrokerUrl;
	@Value("${publishSubscribe.subscriptionManagerEndpoint}")
	protected String subscriptionManagerUrl;
	@Value("${publishSubscribe.smtpServerName}")
	private String mailServerName;
	@Value("${publishSubscribe.smtpServerPort}")
	private Integer mailPort;

	public void setUp() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet.xml"));
	}

	protected IDatabaseConnection getConnection() throws Exception {
		Connection con = dataSource.getConnection();
		IDatabaseConnection connection = new DatabaseConnection(con);
		return connection;
	}

	public void tearDown() throws Exception {
		// DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
	}

	protected IDataSet getDataSet(String fileName) throws Exception {
		// get insert data
		return new FlatXmlDataSetBuilder().build(new FileInputStream(fileName));
	}

	protected List<WiserMessage> notifyAndAssertBasics(String notificationFileName, String activityDateTimeXpath, String expectedEmailContainsString,
			int expectedMessageCount) throws Exception {

		List<WiserMessage> messages = notify(notificationFileName, activityDateTimeXpath);

		assertThat(messages.size(), is(expectedMessageCount));
		for (WiserMessage message : messages) {
			String emailBodyString = message.getMimeMessage().getContent().toString();
			emailBodyString = emailBodyString.replaceAll("\r\n", "\n");
			assertThat(emailBodyString, containsString(expectedEmailContainsString));
		}

		return messages; // in case you want to do further asserting...

	}

	protected List<WiserMessage> notify(String notificationFileName, String activityDateTimeXpath) throws ParserConfigurationException, SAXException,
			IOException, Exception {
		File inputFile = new File("src/test/resources/xmlInstances/" + notificationFileName);

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document notificationDom = docBuilder.parse(inputFile);

		Node dateTimeNode = XmlUtils.xPathNodeSearch(notificationDom, activityDateTimeXpath + "/nc:Date");
		dateTimeNode.setTextContent(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		String notificationBody = OJBUtils.getStringFromDocument(notificationDom);

		Wiser wiser = new Wiser();
		wiser.setPort(mailPort);
		wiser.setHostname(mailServerName);
		wiser.start();

		List<WiserMessage> messages = new ArrayList<WiserMessage>();

		try {

			HttpUtils.post(notificationBody, notificationBrokerUrl);
			Thread.sleep(MAIL_READ_DELAY);

			messages = wiser.getMessages();

		} finally {
			wiser.stop();
		}

		return messages;

	}

	protected void compareDatabaseWithExpectedDataset(String expectedDatasetFileName) throws SQLException, Exception, MalformedURLException,
			DataSetException, DatabaseUnitException {

		// Fetch database data after executing your code
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable filteredActualSubscriptionTable = getFilteredTableFromDataset(databaseDataSet, "subscription");
		ITable filteredActualNotficationMechanismTable = getFilteredTableFromDataset(databaseDataSet, "notification_mechanism");
		ITable filteredActualSubjectIdentiferTable = getFilteredTableFromDataset(databaseDataSet, "subscription_subject_identifier");

		// Load expected data from an XML dataset
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/xmlInstances/dbUnit/" + expectedDatasetFileName));
		ITable filteredExpectedSubscriptionTable = getFilteredTableFromDataset(expectedDataSet, "subscription");
		ITable filteredExpectedNotficationMechanismTable = getFilteredTableFromDataset(expectedDataSet, "notification_mechanism");
		ITable filteredExpectedSubjectIdentiferTable = getFilteredTableFromDataset(expectedDataSet, "subscription_subject_identifier");

		// Assert actual database table match expected table
		Assertion.assertEquals(filteredExpectedSubscriptionTable, filteredActualSubscriptionTable);
		Assertion.assertEquals(filteredExpectedNotficationMechanismTable, filteredActualNotficationMechanismTable);
		Assertion.assertEquals(filteredExpectedSubjectIdentiferTable, filteredActualSubjectIdentiferTable);
	}

	protected void verifyNotificationForSubscribeSoapRequest(List<WiserMessage> emails) throws MessagingException {
		// there should be three messages:  one to the "to", one to the "cc", and one to the "bcc"

		boolean toFound = false;
		boolean ccFound = false;
		boolean bccReceived = false;

		for (WiserMessage email : emails) {

			//dumpEmail(email);

			// all the emails should be addressed like this
			assertEquals("po6@localhost", email.getMimeMessage().getHeader("To", ","));
			assertEquals("sup@localhost", email.getMimeMessage().getHeader("Cc", ","));

			// now test what the address was that actually received them
			if ("po6@localhost".equals(email.getEnvelopeReceiver())) {
				toFound = true;
			} else if ("sup@localhost".equals(email.getEnvelopeReceiver())) {
				ccFound = true;
			} else if ("testbcc@localhost".equals(email.getEnvelopeReceiver())) {
				bccReceived = true;
			}

		}

		assertTrue(toFound);
		assertTrue(ccFound);
		assertTrue(bccReceived);
	}

	private ITable getFilteredTableFromDataset(IDataSet dataSet, String tableName) throws Exception {
		ITable table = dataSet.getTable(tableName);
		ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(table, new String[] { "id*", "subscriptionId", "*date", "timestamp" });
		return filteredTable;
	}

	protected static String invokeRequest(String fileName, String url) throws IOException, Exception {
		File subscriptionInputFile = new File("src/test/resources/xmlInstances/" + fileName);
		String subscriptionBody = FileUtils.readFileToString(subscriptionInputFile);
		String response = HttpUtils.post(subscriptionBody, url);
		return response;
	}

}
