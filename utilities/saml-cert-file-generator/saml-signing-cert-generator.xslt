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

    Copyright 2012-2017 Open Justice Broker Consortium

-->
<!-- This stylesheet generates an x509 certificate (.crt, .pem, etc.) file that includes the signing certificate(s) for a given identity provider -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata"
xmlns:ds="http://www.w3.org/2000/09/xmldsig#">

<xsl:output method="text" omit-xml-declaration="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="/md:EntityDescriptor"/>
	</xsl:template>
	
	<xsl:template match="md:EntityDescriptor">
		<xsl:apply-templates select="md:IDPSSODescriptor"/>
	</xsl:template>
	
	<xsl:template match="md:IDPSSODescriptor">
		<xsl:apply-templates select="md:KeyDescriptor[@use='signing']"/>
	</xsl:template>
	
	<xsl:template match="md:KeyDescriptor">
		<xsl:text>-----BEGIN CERTIFICATE-----</xsl:text>
		<xsl:value-of select="normalize-space(ds:KeyInfo/ds:X509Data/ds:X509Certificate)"/>
		<xsl:text>-----END CERTIFICATE-----</xsl:text>
	</xsl:template>
	
</xsl:stylesheet>