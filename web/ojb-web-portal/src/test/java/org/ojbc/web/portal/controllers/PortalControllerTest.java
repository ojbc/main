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
package org.ojbc.web.portal.controllers;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class PortalControllerTest {

	PortalController unit;
	PortalController unitWithSAMLToken;
	private Map<String, Object> model = new HashMap<String, Object>();
	private MockHttpServletRequest request;
	private UserSession userSession;
	private SamlService samlService;

	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		
		samlService = new SamlService() {
			@Override
			public Element getSamlAssertion(HttpServletRequest request) {
				return buildTestSamlAssertion("Mickey", "Mouse", "Olympia PD", "2013-03-16T18:04:19.526Z");
			}
		};
		
		unit = new PortalController();
		unitWithSAMLToken = new PortalController();
		unitWithSAMLToken.samlService = samlService;
		
		userSession = mock(UserSession.class);
		
		unit.userSession = userSession;
		unitWithSAMLToken.userSession = userSession;
		unitWithSAMLToken.samlService = samlService;
		unit.searchProfilesEnabled = getSearchProfilesEnabledMap();
		unitWithSAMLToken.searchProfilesEnabled = getSearchProfilesEnabledMap();
		unit.stateSpecificIncludes = getStateSpecificIncludes();
		unitWithSAMLToken.stateSpecificIncludes = getStateSpecificIncludes();
	}

	private Map<String, String> getSearchProfilesEnabledMap() {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		mapToReturn.put("people", "enabled");
		mapToReturn.put("vehicle", "enabled");
		mapToReturn.put("incident", "disabled");
		mapToReturn.put("firearm", "hidden");
		
		return mapToReturn;
	}
	
	private Map<String, String> getStateSpecificIncludes() {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		mapToReturn.put("preBodyClose", "");
		
		return mapToReturn;
	}

	@Test
	public void testIndex()
	{
		String expectedSearchLinksHtml = "<a id=\"peopleSearchLink\" href=\"#\" class=\"blueButton\" style=\"border-bottom-right-radius: 0px; border-top-right-radius: 0px;\"><div  class=\"activeSearchLink\"></div>PERSON SEARCH</a><a id=\"incidentSearchLinkDisabled\" href=\"#\" class=\"grayButton\" style=\"border-radius: 0px 0px 0px 0px;\"><div ></div>INCIDENT SEARCH</a><a id=\"vehicleSearchLink\" href=\"#\" class=\"grayButton\" style=\"border-bottom-left-radius: 0px; border-top-left-radius: 0px;\"><div ></div>VEHICLE SEARCH</a>";
		
		unit.index(request, model, null);
		assertThat((String) model.get("currentUserName"), is(PortalController.DEFAULT_USER_LOGON_MESSAGE));
		assertThat((String) model.get("timeOnline"), is(PortalController.DEFAULT_USER_TIME_ONLINE));
		assertThat((String) model.get("searchLinksHtml"), is(expectedSearchLinksHtml));
	}

	@Test
	public void testIndexWithSAMLAssertion() throws Exception
	{
		unitWithSAMLToken.index(request, model, null);
		assertThat((String) model.get("currentUserName"), is("Mickey Mouse / Olympia PD"));
	}
	
	@Test
	public void testBasicUserString() throws Exception
	{
		PortalController.UserLogonInfo userLogonInfo = unit.getUserLogonInfo(buildTestSamlAssertion("Mickey", "Mouse", "Olympia PD", "2013-03-16T18:04:19.526Z"));
		assertEquals("Mickey Mouse / Olympia PD", userLogonInfo.getUserNameString());
		
	}
	
	@Test
	public void testUserStringWithMissingSurname() throws Exception
	{
		PortalController.UserLogonInfo userLogonInfo = unit.getUserLogonInfo(buildTestSamlAssertion("Mickey", "", "Olympia PD", "2013-03-16T18:04:19.526Z"));
		assertEquals("Mickey  / Olympia PD", userLogonInfo.getUserNameString());
		
	}
	
	@Test
	public void testUserStringWithMissingFirstName() throws Exception
	{
		PortalController.UserLogonInfo userLogonInfo = unit.getUserLogonInfo(buildTestSamlAssertion("", "Duck", "Olympia PD", "2013-03-16T18:04:19.526Z"));
		assertEquals(" Duck / Olympia PD", userLogonInfo.getUserNameString());
		
	}
	
	@Test
	public void testUserTimeOnline() throws Exception
	{
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		DateTime authnInstantDate = fmt.parseDateTime("2013-03-16T18:04:19.526Z");
		PortalController.UserLogonInfo userLogonInfo = unit.getUserLogonInfo(buildTestSamlAssertion("Mickey", "Mouse", "Olympia PD", fmt.print(authnInstantDate)));
		// this might fail on a really slow machine (if the test takes more than a minute!)
		int minutes = Minutes.minutesBetween(authnInstantDate, new DateTime()).getMinutes();
		int minute =minutes % 60;
		assertEquals(String.valueOf((int) minutes/60 + ":" + (minute<10? "0":"") + minute), userLogonInfo.getTimeOnlineString());
	}
	
	private String buildTestSamlAssertionString(String userFirstName, String userLastName, String userAgency, String authnInstant) throws Exception {
		String xmlAssertionString = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"_9ec004c141a0b62d7c8954a0ab3ef4bd\" IssueInstant=\"2013-03-16T18:04:19.580Z\" Version=\"2.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
			+ "    <saml2:Issuer Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:entity\">"
			+ "https://idp.ojbc-local.org:9443/idp/shibboleth</saml2:Issuer>"
			+ "    <ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"
			+ "        <ds:SignedInfo>"
			+ "            <ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>"
			+ "            <ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>"
			+ "            <ds:Reference URI=\"#_9ec004c141a0b62d7c8954a0ab3ef4bd\">"
			+ "                <ds:Transforms>"
			+ "                    <ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>"
			+ "                    <ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\">"
			+ "                        <ec:InclusiveNamespaces xmlns:ec=\"http://www.w3.org/2001/10/xml-exc-c14n#\" PrefixList=\"xs\"/>"
			+ "                    </ds:Transform>"
			+ "                </ds:Transforms>"
			+ "                <ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
			+ "                <ds:DigestValue>GCzdLZxn/AVGfneYigEkHQ5FOUg=</ds:DigestValue>"
			+ "            </ds:Reference>"
			+ "        </ds:SignedInfo>"
			+ "        <ds:SignatureValue>"
			+ "Evsxy6Yty/dQTUevXtqOFTimgiV7N7AOitTf0FZ/nJocb10zfHFRolHjUzDqX4GpWx9TDgAQTUO5PHQoShHJBv9VRL6OUFIYnO9eD6py0njuYBt6CdYibQOw2524ONf22IVrPYNwRNxNCmMcRaHzAx71VKZVotMGVQTpxmCarukRGBj/WbHFGL+MPVELrzSH1Gb7SeVzx5VQiI4dl4VvaKQgEuROIx2KSLFzCV9X4h6yLI2jwN9UBgPo95YlgKzaF2Lt/X2oMxp5Tf9PsC2TNJddJcgtSPhGgNrnChJWFv45C+8jkTpibD/dgJ79iForZedgUx5ToqKM1SLd+++IXQ==</ds:SignatureValue>"
			+ "        <ds:KeyInfo>"
			+ "            <ds:X509Data>"
			+ "                <ds:X509Certificate>"
			+ "MIIDNDCCAhygAwIBAgIVAKBUMfAX96OORfdKbRCNTopP1TcqMA0GCSqGSIb3DQEBBQUAMB0xGzAZBgNVBAMTEmlkcC5vamJjLWxvY2FsLm9yZzAeFw0xMjA1MDIxNjI2MzNaFw0zMjA1MDIxNjI2MzNaMB0xGzAZBgNVBAMTEmlkcC5vamJjLWxvY2FsLm9yZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJrG9G7vPfBYYv1I6s1xtTVSneLhCErrL4yNdcLdELFz2faXZxgE9h9VSyL4bKjmBZbHLd63bcvJT7FvGTBampzxF6AGr5JTkuJS9ynwtLFHvx1dBIky7CT8o9/xccmGD2xGF+9ExU+xZwsnu8jxZeUCkWw8rxv1rLX7NAhwOTRFQiUIJe5iOoVMwa3LRxbkYhUIb/1WJlrezgnVVQiRU4NYK/PJKX8qLWKzpMkiTEx7NM7/OHQNh+1abD0cBGAkhnOOibE7n4qSwl71j/buN5KX8lD8wkvjUM9+IulEobyiR0ygBt5n1NdKiTEt7LRJfdAdDQyiUKaqsiZeKB3u+Z8CAwEAAaNrMGkwSAYDVR0RBEEwP4ISaWRwLm9qYmMtbG9jYWwub3JnhilodHRwczovL2lkcC5vamJjLWxvY2FsLm9yZy9pZHAvc2hpYmJvbGV0aDAdBgNVHQ4EFgQUgvGpJxb0bK+9KPtTmg2b9BMPaT8wDQYJKoZIhvcNAQEFBQADggEBAJTd5rc9BC7m4YDa9E/Y4yR65uUMS0Mx62EFHwjJ6hSrLFvd/0dcpEjELDR1esz9xHCFul4OI8z9A7fzYkiA07eSyzRQWB+jpMQUfdj0793cLQbSabbrKKhE8p9WHQDbAjV2SHeYeywRIcK+OkrXGjmS5GWxvpcU2edWUUm0S5ghu8pDdKiQSYzZJpSPSeahxdqtRGgD21Arwr4xWOSDd1obq2WbyayfTM1WHCHMKRrpRsPW3KypFk7WpK2FtGnwxiQbp07+LqREWQXDCrb1px6iKvzaqG3TiU20M6teeHCZ5rCc8hGaAHcN9S0qlG9OZ3V3e5YAga6ske8ZFlL9MNY=</ds:X509Certificate>"
			+ "            </ds:X509Data>"
			+ "        </ds:KeyInfo>"
			+ "    </ds:Signature>"
			+ "    <saml2:Subject>"
			+ "        <saml2:NameID Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\" NameQualifier=\"https://idp.ojbc-local.org:9443/idp/shibboleth\" SPNameQualifier=\"https://sp-suse.ojbc-local.org/shibboleth\">"
			+ "_459a52c101b51cc4c298db79fb8c8d3c</saml2:NameID>"
			+ "        <saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">"
			+ "            <saml2:SubjectConfirmationData Address=\"127.0.0.1\" InResponseTo=\"_44010b2da667e0ba0adad72d6ce51611\" NotOnOrAfter=\"2013-03-16T18:09:19.580Z\" Recipient=\"https://www.ojbc-local.org/Shibboleth.sso/SAML2/POST\"/>"
			+ "        </saml2:SubjectConfirmation>"
			+ "    </saml2:Subject>"
			+ "    <saml2:Conditions NotBefore=\"2013-03-16T18:04:19.580Z\" NotOnOrAfter=\"2013-03-16T18:09:19.580Z\">"
			+ "        <saml2:AudienceRestriction>"
			+ "            <saml2:Audience>"
			+ "https://sp-suse.ojbc-local.org/shibboleth</saml2:Audience>"
			+ "        </saml2:AudienceRestriction>"
			+ "    </saml2:Conditions>"
			+ "    <saml2:AuthnStatement AuthnInstant=\"" + authnInstant + "\" SessionIndex=\"5c9ec626169beddd904ea53ce3e423c78bd8bd0a11f6397413272d2e45176fe7\">"
			+ "        <saml2:SubjectLocality Address=\"127.0.0.1\"/>"
			+ "        <saml2:AuthnContext>"
			+ "            <saml2:AuthnContextClassRef>"
			+ "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport</saml2:AuthnContextClassRef>"
			+ "        </saml2:AuthnContext>"
			+ "    </saml2:AuthnStatement>"
			+ "    <saml2:AttributeStatement>"
			+ "        <saml2:Attribute Name=\"gfipm:2.0:user:LocalId\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\">"
			+ "            <saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
			+ "scottcame</saml2:AttributeValue>"
			+ "        </saml2:Attribute>"
			+ "        <saml2:Attribute Name=\"gfipm:2.0:user:SurName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\">"
			+ "            <saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
			+ userLastName + "</saml2:AttributeValue>"
			+ "        </saml2:Attribute>"
			+ "        <saml2:Attribute Name=\"gfipm:2.0:user:EmployerSubUnitName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\">"
			+ "            <saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
			+ "Narcotics</saml2:AttributeValue>"
			+ "        </saml2:Attribute>"
			+ "        <saml2:Attribute Name=\"gfipm:2.0:user:EmployerName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\">"
			+ "            <saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
			+ userAgency + "</saml2:AttributeValue>"
			+ "        </saml2:Attribute>"
			+ "        <saml2:Attribute Name=\"gfipm:2.0:user:GivenName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\">"
			+ "            <saml2:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
			+ userFirstName + "</saml2:AttributeValue>"
			+ "        </saml2:Attribute>"
			+ "    </saml2:AttributeStatement>" + "</saml2:Assertion>";
		
			return xmlAssertionString;
	}
	
	private Element buildTestSamlAssertion(String userFirstName, String userLastName, String userAgency, String authnInstant) {
		
		String xmlAssertionString;
		try {
			xmlAssertionString = buildTestSamlAssertionString(userFirstName, userLastName, userAgency, authnInstant);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xmlAssertionString))).getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
