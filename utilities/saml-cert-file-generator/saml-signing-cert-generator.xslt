<?xml version="1.0" encoding="UTF-8"?>
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