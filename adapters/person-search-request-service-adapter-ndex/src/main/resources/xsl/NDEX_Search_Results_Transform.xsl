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
<xsl:stylesheet version="2.0" xmlns:psres-doc="http://ojbc.org/IEPD/Exchange/PersonSearchResults/1.0" xmlns:psres-ext="http://ojbc.org/IEPD/Extensions/PersonSearchResults/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ulex="http://ulex.gov/ulex/2.0" xmlns:ulexsr="http://ulex.gov/searchretrieve/2.0" xmlns:ulexcodes="http://ulex.gov/codes/2.0" xmlns:ulexlib="http://ulex.gov/library/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:em="http://niem.gov/niem/domains/emergencyManagement/2.0" xmlns:im="http://niem.gov/niem/domains/immigration/2.0" xmlns:scr="http://niem.gov/niem/domains/screening/2.0" xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:lexs="http://lexs.gov/lexs/4.0" xmlns:wsa="http://www.w3.org/2005/08/addressing" xmlns:lexsdigest="http://lexs.gov/digest/4.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:template match="/">
		<psres-doc:PersonSearchResults>
			<xsl:apply-templates select="ulexsr:doSearchResponse/ulex:SearchResponseMessage" mode="Result"/>
		</psres-doc:PersonSearchResults>
	</xsl:template>
	<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-->
	<xsl:template match="/ulexsr:doSearchResponse/ulex:SearchResponseMessage" mode="Result">
		<psres-ext:PersonSearchResult>
			<xsl:apply-templates select="ulex:SearchResultPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person" mode="Person"/>
			<xsl:apply-templates select="ulex:SearchResultPackage/lexs:Digest/lexsdigest:EntityLocation/nc:Location" mode="Location"/>
			<xsl:apply-templates select="ulex:SearchResultPackage/lexs:Digest/lexsdigest:Associations" mode="Association"/>
			<xsl:apply-templates select="ulex:SRMessageMetadata/ulex:MessageOriginMetadata" mode="Origin"/>
			<!--Include the following to identify thr response as an N-DEx s/r? -->
			<psres-ext:SearchResultCategoryText>N-DEx Search/Retrieve</psres-ext:SearchResultCategoryText>
			<xsl:apply-templates select="ulex:SearchResultPackage/ulex:PackageMetadata/ulex:DataOwnerMetadata/lexs:DataOwnerIdentifier/nc:OrganizationName" mode="Owner"/>
		</psres-ext:PersonSearchResult>
	</xsl:template>
	<xsl:template match="ulex:SearchResultPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person" mode="Person">
		<psres-ext:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="nc:PersonAgeMeasure"/>
			<xsl:apply-templates select="nc:PersonAlternateName"/>
			<xsl:apply-templates select="nc:PersonBirthDate/nc:Date"/>
			<xsl:apply-templates select="nc:PersonHeightMeasure"/>
			<xsl:apply-templates select="nc:PersonName"/>
			<xsl:apply-templates select="nc:PersonRaceCode"/>
			<xsl:apply-templates select="nc:PersonSexCode"/>
			<xsl:apply-templates select="nc:PersonSSNIdentification"/>
			<xsl:apply-templates select="nc:PersonWeightMeasure"/>
			<xsl:apply-templates select="j:PersonAugmentation" mode="Aug"/>
		</psres-ext:Person>
	</xsl:template>
	<xsl:template match="j:PersonAugmentation" mode="Aug">
		<j:PersonAugmentation>
			<xsl:apply-templates select="nc:DriverLicense/nc:DriverLicenseIdentification"/>
			<xsl:apply-templates select="j:PersonFBIIdentification"/>
			<xsl:apply-templates select="j:PersonStateFingerprintIdentification"/>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="ulex:SearchResultPackage/lexs:Digest/lexsdigest:EntityLocation/nc:Location" mode="Location">
		<nc:Location>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
			<xsl:apply-templates select="nc:LocationAddress/nc:StructuredAddress"/>
		</nc:Location>
	</xsl:template>
	<xsl:template match="ulex:SearchResultPackage/lexs:Digest/lexsdigest:Associations" mode="Association">
		<xsl:apply-templates select="nc:ResidenceAssociation"/>
	</xsl:template>
	<xsl:template match="ulex:SRMessageMetadata/ulex:MessageOriginMetadata" mode="Origin">
		<intel:SystemIdentifier>
			<xsl:apply-templates select="lexs:SystemIdentifier/lexs:SystemID"/>
			<xsl:apply-templates select="lexs:SystemIdentifier/nc:OrganizationName"/>
		</intel:SystemIdentifier>
		<xsl:apply-templates select="nc:ResidenceAssociation"/>
	</xsl:template>
	<xsl:template match="ulex:SearchResultPackage/ulex:PackageMetadata/ulex:DataOwnerMetadata/lexs:DataOwnerIdentifier/nc:OrganizationName" mode="Owner">
		<psres-ext:InformationOwningOrganization>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</psres-ext:InformationOwningOrganization>
	</xsl:template>
	<xsl:template match="nc:LocationAddress/nc:StructuredAddress">
		<nc:LocationAddress>
			<nc:StructuredAddress>
				<xsl:apply-templates select="nc:LocationStreet"/>
				<xsl:apply-templates select="nc:LocationCityName"/>
				<xsl:apply-templates select="nc:LocationPostalCode"/>
			</nc:StructuredAddress>
		</nc:LocationAddress>
	</xsl:template>
	<xsl:template match="nc:LocationStreet">
		<nc:LocationStreet>
			<xsl:apply-templates select="nc:StreetFullText"/>
			<xsl:apply-templates select="nc:StreetNumberText"/>
			<xsl:apply-templates select="nc:StreetName"/>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc:StreetFullText">
		<nc:StreetFullText>
			<xsl:value-of select="."/>
		</nc:StreetFullText>
	</xsl:template>
	<xsl:template match="nc:StreetNumberText">
		<nc:StreetNumberText>
			<xsl:value-of select="."/>
		</nc:StreetNumberText>
	</xsl:template>
	<xsl:template match="nc:StreetName">
		<nc:StreetName>
			<xsl:value-of select="."/>
		</nc:StreetName>
	</xsl:template>
	<xsl:template match="nc:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc:LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="nc:PersonAgeMeasure">
		<nc:PersonAgeMeasure>
			<nc:MeasurePointValue>
				<xsl:value-of select="nc:MeasurePointValue"/>
			</nc:MeasurePointValue>
		</nc:PersonAgeMeasure>
	</xsl:template>
	<xsl:template match="nc:PersonAlternateName">
		<nc:PersonAlternateName>
			<xsl:apply-templates select="nc:PersonGivenName"/>
			<xsl:apply-templates select="nc:PersonMiddleName"/>
			<xsl:apply-templates select="nc:PersonSurName"/>
			<xsl:apply-templates select="nc:PersonFullName"/>
		</nc:PersonAlternateName>
	</xsl:template>
	<xsl:template match="nc:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="nc:PersonFullName">
		<nc:PersonFullName>
			<xsl:value-of select="."/>
		</nc:PersonFullName>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate/nc:Date">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc:PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<nc:MeasurePointValue>
				<xsl:value-of select="nc:MeasurePointValue"/>
			</nc:MeasurePointValue>
			<nc:LengthUnitCode>
				<xsl:value-of select="nc:LengthUnitCode"/>
			</nc:LengthUnitCode>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc:PersonGivenName"/>
			<xsl:apply-templates select="nc:PersonMiddleName"/>
			<xsl:apply-templates select="nc:PersonSurName"/>
			<xsl:apply-templates select="nc:PersonFullName"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc:PersonRaceCode">
		<nc:PersonRaceCode>
			<xsl:value-of select="."/>
		</nc:PersonRaceCode>
	</xsl:template>
	<xsl:template match="nc:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="."/>
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc:PersonSSNIdentification">
		<nc:PersonSSNIdentification>
			<xsl:apply-templates select="nc:IdentificationID"/>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="nc:PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<nc:MeasurePointValue>
				<xsl:value-of select="nc:MeasurePointValue"/>
			</nc:MeasurePointValue>
			<nc:WeightUnitCode>
				<xsl:value-of select="nc:WeightUnitCode"/>
			</nc:WeightUnitCode>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="nc:DriverLicense/nc:DriverLicenseIdentification">
		<nc:DriverLicense>
			<nc:DriverLicenseIdentification>
				<xsl:apply-templates select="nc:IdentificationID"/>
				<xsl:apply-templates select="nc:IdentificationSourceText"/>
			</nc:DriverLicenseIdentification>
		</nc:DriverLicense>
	</xsl:template>
	<xsl:template match="j:PersonFBIIdentification">
		<j:PersonFBIIdentification>
			<xsl:apply-templates select="nc:IdentificationID"/>
			<xsl:apply-templates select="nc:IdentificationSourceText"/>
		</j:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="j:PersonStateFingerprintIdentification">
		<j:PersonStateFingerprintIdentification>
			<xsl:apply-templates select="nc:IdentificationID"/>
			<xsl:apply-templates select="nc:IdentificationSourceText"/>
		</j:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="nc:ResidenceAssociation">
		<nc:ResidenceAssociation>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../lexsdigest:EntityPerson/lexsdigest:Person)"/></xsl:attribute>
			</nc:PersonReference>
			<nc:LocationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../lexsdigest:EntityLocation/nc:Location)"/></xsl:attribute>
			</nc:LocationReference>
		</nc:ResidenceAssociation>
	</xsl:template>
	<xsl:template match="lexs:SystemIdentifier/lexs:SystemID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="lexs:SystemIdentifier/nc:OrganizationName">
		<intel:SystemName>
			<xsl:value-of select="."/>
		</intel:SystemName>
	</xsl:template>
	<!-- BOTTOM -->
	<xsl:template match="nc:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc:IdentificationSourceText">
		<nc:IdentificationSourceText>
			<xsl:value-of select="."/>
		</nc:IdentificationSourceText>
	</xsl:template>
</xsl:stylesheet>
