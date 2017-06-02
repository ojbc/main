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
package org.ojbc.util.camel.saml;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.signature.SignatureConstants;

public class TestSAMLTokenUtils {

	@Test
	public void testCreateAssertionWithCustomAttributesWithNull() throws Exception
	{
		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		
		assertNotNull(samlToken);
		
		assertEquals(16, samlToken.getAttributeStatements().get(0).getAttributes().size());
		
		for (Attribute attributes : samlToken.getAttributeStatements().get(0).getAttributes())
		{
			//System.out.println(attributes.getName());
			//System.out.println(attributes.getAttributeValues().get(0).getDOM().getTextContent());
			
			if (attributes.getName().equals("gfipm:2.0:user:EmployeePositionName"))
			{
				assertEquals("Sergeant", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:SurName"))
			{
				assertEquals("owen", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:FederatedQueryUserIndicator"))
			{
				assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerName"))
			{
				assertEquals("Department of Attorney General", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:GivenName"))
			{
				assertEquals("andrew", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:CommonName"))
			{
				assertEquals("Andrew Owen", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:CriminalJusticeEmployerIndicator"))
			{
				assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:LawEnforcementEmployerIndicator"))
			{
				assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals(SamlAttribute.SupervisoryRoleIndicator.getAttibuteName()))
			{
			    assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator.getAttibuteName()))
			{
			    assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals("gfipm:2.0:user:FederationId"))
			{
				assertEquals("HIJIS:IDP:HCJDC:USER:admin", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:TelephoneNumber"))
			{
				assertEquals("916-215-3933", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerSubUnitName"))
			{
				assertEquals("HCJDC ISDI", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmailAddressText"))
			{
				assertEquals("andrew@search.org", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerORI"))
			{
				assertEquals("002015Y", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:IdentityProviderId"))
			{
				assertEquals("HIJIS:IDP:HCJDC", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
		}

		//XMLUtils.printDOM(samlToken.getDOM());
	}

	@Test
	public void testCreateAssertionWithCustomAttributes() throws Exception
	{
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        
        customAttributes.put(SamlAttribute.EmployeePositionName, "detective");
        customAttributes.put(SamlAttribute.SurName, "smith");
        customAttributes.put(SamlAttribute.FederatedQueryUserIndicator, "false");
        customAttributes.put(SamlAttribute.EmployerName, "DOJ");
        customAttributes.put(SamlAttribute.GivenName,"Joe");
        customAttributes.put(SamlAttribute.CommonName,"Joe Smith");
        
        customAttributes.put(SamlAttribute.CriminalJusticeEmployerIndicator,"false");
        customAttributes.put(SamlAttribute.LawEnforcementEmployerIndicator,"false");
        customAttributes.put(SamlAttribute.SupervisoryRoleIndicator,"true");
        customAttributes.put(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator,"true");
        customAttributes.put(SamlAttribute.FederationId,"FedID");
        customAttributes.put(SamlAttribute.TelephoneNumber,"999 999-9999");
        customAttributes.put(SamlAttribute.EmployerSubUnitName,"sub");
        customAttributes.put(SamlAttribute.EmailAddressText,"1@1.com");
        customAttributes.put(SamlAttribute.EmployerORI,"ORI");
        customAttributes.put(SamlAttribute.IdentityProviderId,"HIJIS");

		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
		
		assertNotNull(samlToken);
		
		assertEquals(16, samlToken.getAttributeStatements().get(0).getAttributes().size());
		
		for (Attribute attributes : samlToken.getAttributeStatements().get(0).getAttributes())
		{
			//System.out.println(attributes.getName());
			//System.out.println(attributes.getAttributeValues().get(0).getDOM().getTextContent());
			
			if (attributes.getName().equals("gfipm:2.0:user:EmployeePositionName"))
			{
				assertEquals("detective", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:SurName"))
			{
				assertEquals("smith", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:FederatedQueryUserIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerName"))
			{
				assertEquals("DOJ", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:GivenName"))
			{
				assertEquals("Joe", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:CommonName"))
			{
				assertEquals("Joe Smith", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:CriminalJusticeEmployerIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:ext:user:LawEnforcementEmployerIndicator"))
			{
				assertEquals("false", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals(SamlAttribute.SupervisoryRoleIndicator.getAttibuteName()))
			{
			    assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals(SamlAttribute.FirearmsRegistrationRecordsPersonnelIndicator.getAttibuteName()))
			{
			    assertEquals("true", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
			
			if (attributes.getName().equals("gfipm:2.0:user:FederationId"))
			{
				assertEquals("FedID", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:TelephoneNumber"))
			{
				assertEquals("999 999-9999", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerSubUnitName"))
			{
				assertEquals("sub", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmailAddressText"))
			{
				assertEquals("1@1.com", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:EmployerORI"))
			{
				assertEquals("ORI", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	

			if (attributes.getName().equals("gfipm:2.0:user:IdentityProviderId"))
			{
				assertEquals("HIJIS", attributes.getAttributeValues().get(0).getDOM().getTextContent());
			}	
		}

		//XMLUtils.printDOM(samlToken.getDOM());
	}

}
