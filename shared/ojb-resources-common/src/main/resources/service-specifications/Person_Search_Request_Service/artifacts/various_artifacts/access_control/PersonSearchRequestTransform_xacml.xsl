<?xml version="1.0" encoding="UTF-8"?>
<!--

    Unless explicitly acquired and licensed from Licensor under another license, the contents of
    this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
    versions as allowed by the RPL, and You may not copy or use this file in either source code
    or executable form, except in compliance with the terms and conditions of the RPL

    All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
    WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
    WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
    PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
    governing rights and limitations under the RPL.

    http://opensource.org/licenses/RPL-1.5

    Copyright 2012-2015 Open Justice Broker Consortium

-->
<xsl:stylesheet version="2.0" xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:saml2="urn:oasis:names:tc:SAML:2.0:assertion" xmlns:wsp="http://schemas.xmlsoap.org/ws/2004/09/policy" xmlns:wsa="http://www.w3.org/2005/08/addressing" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" exclude-result-prefixes="fn soap wsa"/>
	<xsl:template match="/">
		<Request xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17" ReturnPolicyIdList="false" CombinedDecision="false">
			<Attributes Category="urn:oasis:names:tc:xacml:3.0:attribute-category:resource">
				<xsl:apply-templates select="soap:Envelope/soap:Header/wsa:To [normalize-space(.) != '']"/>
			</Attributes>
			<Attributes Category="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject">
				<xsl:apply-templates select="soap:Envelope/soap:Header/wsse:Security/saml2:Assertion/saml2:AttributeStatement [normalize-space(.) != '']"/>
			</Attributes>
		</Request>
	</xsl:template>
	<xsl:template match="soap:Envelope/soap:Header/wsa:To">
		<Attribute IncludeInResult="true" AttributeId="urn:oasis:names:tc:xacml:1.0:resource:resource-id">
			<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
				<xsl:value-of select="."/>
			</AttributeValue>
		</Attribute>
	</xsl:template>
	<xsl:template match="soap:Envelope/soap:Header/wsse:Security/saml2:Assertion/saml2:AttributeStatement">
		<xsl:apply-templates select="saml2:Attribute[@Name= 'gfipm:ext:user:FederatedQueryUserIndicator']" mode="federatedQueryUser"/>
		<xsl:apply-templates select="saml2:Attribute[@Name= 'gfipm:ext:user:LawEnforcementEmployerIndicator']" mode="employerIsLawEnforcement"/>
		<xsl:apply-templates select="saml2:Attribute[@Name= 'gfipm:2.0:user:EmployerSubUnitName']" mode="employerSubUnitName"/>
	</xsl:template>
	<xsl:template match="saml2:Attribute" mode="federatedQueryUser">
		<Attribute IncludeInResult="true" AttributeId="http://ojbc.org/policy/attributes/subjectIsFederatedQueryUser">
			<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#boolean">
				<xsl:value-of select="."/>
			</AttributeValue>
		</Attribute>
	</xsl:template>
	<xsl:template match="saml2:Attribute" mode="employerIsLawEnforcement">
		<Attribute IncludeInResult="true" AttributeId="http://ojbc.org/policy/attributes/subjectEmployerIsLawEnforcement">
				<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#boolean">
					<xsl:value-of select="."/>
				</AttributeValue>
			</Attribute>
	</xsl:template>
	<xsl:template match="saml2:Attribute" mode="employerSubUnitName">
			<Attribute IncludeInResult="true" AttributeId="http://ojbc.org/policy/demostate/attributes/subjectEmployerSubUnitName">
				<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
					<xsl:value-of select="."/>
				</AttributeValue>
			</Attribute>
	</xsl:template>
</xsl:stylesheet>
