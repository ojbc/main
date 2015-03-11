package org.search.ojb.staticmock;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JuvenileHistoryContainerTest {
	
	private JuvenileHistoryContainer juvenileHistoryContainer;
	
	@Before
	public void setUp() throws Exception {
		Document containerDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/JuvenileHistoryTestInstances/TestInstance.xml"));
		juvenileHistoryContainer = new JuvenileHistoryContainer(containerDocument);
	}
	
	@Test
	public void testConstruction() throws Exception {
		assertNotNull(juvenileHistoryContainer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstruction() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document d = dbf.newDocumentBuilder().newDocument();
		Element e = d.createElementNS("foo", "bar");
		d.appendChild(e);
		juvenileHistoryContainer = new JuvenileHistoryContainer(d);
	}
	
	@Test
	public void testBuildReferralHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildReferralHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateReferralHistoryDocument(d);
	}

	@Test
	public void testBuildOffenseHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildOffenseHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateOffenseHistoryDocument(d);
	}

	@Test
	public void testBuildCasePlanHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildCasePlanHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateInstance(d, "CasePlan", "CasePlan/JuvenileCasePlanHistoryExtension.xsd");
	}

	@Test
	public void testBuildPlacementHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildPlacementHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateInstance(d, "Placement", "Placement/JuvenilePlacementHistoryExtension.xsd");
	}

	@Test
	public void testBuildIntakeHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildIntakeHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateInstance(d, "Intake", "Intake/JuvenileIntakeHistoryExtension.xsd");
	}

	@Test
	public void testBuildHearingHistoryDocument() throws Exception {
		Document d = juvenileHistoryContainer.buildHearingHistoryDocument();
		//XmlUtils.printNode(d);
		JuvenileHistoryContainerTestUtils.validateInstance(d, "Hearing", "Hearing/JuvenileHearingHistoryExtension.xsd");
	}

}
