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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:rls-req-doc="http://ojbc.org/IEPD/Exchange/RegulatoryLicenseSearchRequest/1.0"
	xmlns:rls-req-ext="http://ojbc.org/IEPD/Extensions/RegulatoryLicenseSearchRequestExtension/1.0"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" exclude-result-prefixes="rls-req-doc rls-req-ext nc structures" version="2.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="rls-req-doc:RegulatoryLicenseSearchRequest">
		<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
			xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
			<soap:Header>
				<AuthenticationHeader xmlns="https://www.sauper.com/MBCSOnlineServices">
					<AuthenticationKey>string</AuthenticationKey>
				</AuthenticationHeader>
			</soap:Header>
			<soap:Body>
				<xsl:apply-templates select="." mode="findLicense" />
			</soap:Body>
		</soap:Envelope>
	</xsl:template>
	<xsl:template match="rls-req-doc:RegulatoryLicenseSearchRequest" mode="findLicense">
		<FindLicenseInformation xmlns="https://www.sauper.com/MBCSOnlineServices">
			<xsl:apply-templates select="nc:Person/nc:PersonName[.!='']" />
			<xsl:apply-templates select="nc:Person/nc:PersonLicenseIdentification/nc:IdentificationID[.!='']"
				mode="license" />
			<xsl:apply-templates select="nc:Person/nc:PersonBirthDate/nc:Date[.!='']" mode="birth" />
		</FindLicenseInformation>
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<xsl:apply-templates select="nc:PersonSurName" />
		<xsl:apply-templates select="nc:PersonGivenName" />
	</xsl:template>
	<xsl:template match="nc:PersonSurName">
		<LastNameBeginsWith>
			<xsl:value-of select="normalize-space(.)" />
		</LastNameBeginsWith>
	</xsl:template>
	<xsl:template match="nc:PersonGivenName">
		<FirstNameBeginsWith>
			<xsl:value-of select="normalize-space(.)" />
		</FirstNameBeginsWith>
	</xsl:template>
	<xsl:template match="nc:Date" mode="birth">
		<BirthDate>
			<xsl:value-of select="normalize-space(.)" />
		</BirthDate>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="license">
		<LicenseNumber>
			<xsl:value-of select="normalize-space(.)" />
		</LicenseNumber>
	</xsl:template>
</xsl:stylesheet>