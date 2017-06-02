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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0" 
	xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
	xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" 
	xmlns:arrest-exch="http://ojbc.org/IEPD/Exchange/ArrestReport/1.0" 
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" 
	xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1" 
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" 
	xmlns:s="http://niem.gov/niem/structures/2.0" 
	xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1" 
	xmlns:ojbc="http://ojbc.org/IEPD/Extensions/ArrestReportStructuredPayload/1.0" 
	xmlns:j40="http://niem.gov/niem/domains/jxdm/4.0" 
	xmlns:j41="http://niem.gov/niem/domains/jxdm/4.1" 
	xmlns:j50="http://release.niem.gov/niem/domains/jxdm/5.0/" 
	xmlns:pidres-doc="http://ojbc.org/IEPD/Exchange/PersonIdentificationResults/1.0" 
	xmlns:ident-ext="http://ojbc.org/IEPD/Extensions/IdentificationExtension/1.0" 
	xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>	
	<xsl:variable name="currentDateTime" select="current-dateTime()"/>
	<xsl:variable name="currentDateTimeString" select="format-dateTime($currentDateTime, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')" />	 		
	<xsl:template match="/">
		<arrest-exch:ArrestReport>
			<xsl:apply-templates select="pidres-doc:PersonStateIdentificationResults"/>
		</arrest-exch:ArrestReport>
	</xsl:template>
	<xsl:template match="pidres-doc:PersonStateIdentificationResults">
		<lexspd:doPublish>
			<lexs:PublishMessageContainer>
				<lexs:PublishMessage>
					<lexs:PDMessageMetadata>
						<lexs:LEXSVersion>3.1</lexs:LEXSVersion>
  						<lexs:MessageDateTime> 
  							<xsl:value-of select="$currentDateTimeString"/> 
  						</lexs:MessageDateTime> 
						<lexs:MessageSequenceNumber>1</lexs:MessageSequenceNumber>
					</lexs:PDMessageMetadata>
					<lexs:DataSubmitterMetadata>
						<lexs:SystemIdentifier>
							<xsl:apply-templates select="ident-ext:IdentificationApplicantOrganization/nc30:OrganizationName"/>
						</lexs:SystemIdentifier>
					</lexs:DataSubmitterMetadata>
					<lexs:DataItemPackage>
						<lexs:PackageMetadata>
							<lexs:DataOwnerMetadata>
								<lexs:DataOwnerIdentifier>
									<xsl:apply-templates select="ident-ext:IdentificationApplicantOrganization/j50:OrganizationAugmentation/j50:OrganizationORIIdentification/nc30:IdentificationID" mode="ORI"/>
								</lexs:DataOwnerIdentifier>
							</lexs:DataOwnerMetadata>
						</lexs:PackageMetadata>
						<xsl:apply-templates select="j50:Subject" mode="digestPerson"/>
						<lexs:StructuredPayload>
							<lexs:StructuredPayloadMetadata>
								<lexs:CommunityURI>http://www.ojbc.org</lexs:CommunityURI>
								<lexs:CommunityDescription>OJBC</lexs:CommunityDescription>
								<lexs:CommunityVersion>1.0</lexs:CommunityVersion>
							</lexs:StructuredPayloadMetadata>
							<ojbc:ArrestReport>
								<xsl:apply-templates select="j50:Subject/nc30:RoleOfPerson/j50:PersonAugmentation/j50:PersonStateFingerprintIdentification" mode="structuredPayload"/>
							</ojbc:ArrestReport>
						</lexs:StructuredPayload>
					</lexs:DataItemPackage>
				</lexs:PublishMessage>
			</lexs:PublishMessageContainer>
		</lexspd:doPublish>
	</xsl:template>
	<xsl:template match="nc30:OrganizationName">
		<nc20:OrganizationName>
			<xsl:value-of select="."/>
		</nc20:OrganizationName>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="ORI">
		<lexs:ORI>
			<xsl:value-of select="."/>
		</lexs:ORI>
	</xsl:template>
	<xsl:template match="j50:Subject">
		<xsl:apply-templates select="nc30:RoleOfPerson"/>
	</xsl:template>
	<xsl:template match="nc30:RoleOfPerson" mode="digestPerson">
		<lexs:Digest>
			<lexsdigest:EntityPerson>
				<lexsdigest:Person>
					<xsl:attribute name="s:id">
						<xsl:value-of select="generate-id(.)"/>
					</xsl:attribute>
					<xsl:apply-templates select="nc30:PersonBirthDate"/>
					<xsl:apply-templates select="nc30:PersonName"/>
					<xsl:apply-templates select="j50:PersonAugmentation"/>
				</lexsdigest:Person>
				<xsl:apply-templates select="." mode="arrestSubject"/>
			</lexsdigest:EntityPerson>
		</lexs:Digest>
	</xsl:template>
	<xsl:template match="j50:PersonAugmentation">
		<j40:PersonAugmentation>
			<xsl:apply-templates select="j50:PersonStateFingerprintIdentification" mode="sid"/>
		</j40:PersonAugmentation>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc20:PersonBirthDate>
			<nc20:Date>
				<xsl:value-of select="nc30:Date"/>
			</nc20:Date>
		</nc20:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc20:PersonName>
			<xsl:apply-templates select="nc30:PersonGivenName"/>
			<xsl:apply-templates select="nc30:PersonMiddleName"/>
			<xsl:apply-templates select="nc30:PersonSurName"/>
		</nc20:PersonName>
	</xsl:template>
	<xsl:template match="nc30:PersonGivenName">
		<nc20:PersonGivenName>
			<xsl:value-of select="."/>
		</nc20:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc30:PersonMiddleName">
		<nc20:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc20:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc30:PersonSurName">
		<nc20:PersonSurName>
			<xsl:value-of select="."/>
		</nc20:PersonSurName>
	</xsl:template>
	<xsl:template match="nc30:RoleOfPerson" mode="arrestSubject">
		<j40:ArrestSubject>
			<nc20:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			</nc20:RoleOfPersonReference>
		</j40:ArrestSubject>
	</xsl:template>
	<xsl:template match="j50:PersonStateFingerprintIdentification" mode="sid">
		<j40:PersonStateFingerprintIdentification>
			<xsl:attribute name="s:id">
				<xsl:value-of select="generate-id()"/>
			</xsl:attribute>
			<xsl:apply-templates select="nc30:IdentificationID" mode="sid"/>
		</j40:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="j50:PersonStateFingerprintIdentification" mode="structuredPayload">
		<ojbc:PersonStateFingerprintIdentification>
			<xsl:choose>
				<xsl:when test="ident-ext:FingerprintIdentificationIssuedForCivilPurposeIndicator='true'">
					<ojbc:FingerprintIdentificationIssuedForCivilPurposeIndicator>true</ojbc:FingerprintIdentificationIssuedForCivilPurposeIndicator>
				</xsl:when>
				<xsl:when test="ident-ext:FingerprintIdentificationIssuedForCriminalPurposeIndicator='true'">
					<ojbc:FingerprintIdentificationIssuedForCriminalPurposeIndicator>true</ojbc:FingerprintIdentificationIssuedForCriminalPurposeIndicator>
				</xsl:when>
			</xsl:choose>
			<lexslib:SameAsDigestReference>
				<xsl:attribute name="lexslib:ref">
					<xsl:value-of select="generate-id()"/>
				</xsl:attribute>
			</lexslib:SameAsDigestReference>
		</ojbc:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="sid">
		<nc20:IdentificationID>
			<xsl:value-of select="."/>
		</nc20:IdentificationID>
	</xsl:template>
</xsl:stylesheet>
