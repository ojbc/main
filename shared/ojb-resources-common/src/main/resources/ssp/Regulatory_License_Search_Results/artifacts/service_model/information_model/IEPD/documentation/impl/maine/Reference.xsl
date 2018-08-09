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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" xmlns:rls-res-doc="http://ojbc.org/IEPD/Exchange/RegulatoryLicenseSearchResults/1.0"
	xmlns:rls-res-ext="http://ojbc.org/IEPD/Extensions/RegulatoryLicenseSearchResults/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0"
	exclude-result-prefixes="">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="soap:Envelope/soap:Body/FindLicenseInformationResponse" xmlns="http://www.sauper.com/MBCSOnlineServices">
		<rls-res-doc:RegulatoryLicenseSearchResults>
			<xsl:apply-templates select="LicenseInfoResult" />
		</rls-res-doc:RegulatoryLicenseSearchResults>
	</xsl:template>
	<xsl:template match="LicenseInfoResult">
		<xsl:choose>
			<xsl:when test="./item" />
			<xsl:otherwise>
				<wlq-res-ext:WildlifeLicenseReport>
					<xsl:attribute name="structures:id" select="./@id" />
					<xsl:apply-templates select="." mode="system" />
					<xsl:apply-templates select="." mode="person" />
					<xsl:apply-templates select="." mode="ContactInf" />
					<xsl:apply-templates select="." mode="Residence" />
					<xsl:apply-templates select="." mode="secondaryResidence" />
					<xsl:apply-templates select="." mode="ContactAssoc" />
					<xsl:apply-templates select="." mode="ResAssoc" />
					<xsl:apply-templates select="." mode="secondaryResAssoc" />
				</wlq-res-ext:WildlifeLicenseReport>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="multiRef" mode="person">
		<nc:Person>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<xsl:apply-templates select="dob[.!='']" />
			<xsl:apply-templates select="eyeColor[.!='']" />
			<xsl:apply-templates select="hairColor[.!='']" />
			<xsl:apply-templates select="." mode="height" />
			<xsl:apply-templates select="." mode="personName" />
			<xsl:apply-templates select="gender[.!='']" />
			<xsl:apply-templates select="ssn[.!='']" />
			<xsl:apply-templates select="weight[.!='']" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="multiRef" mode="personName">
		<nc:PersonName>
			<xsl:apply-templates select="nameFirst[.!='']" />
			<xsl:apply-templates select="nameMiddle[.!='']" />
			<xsl:apply-templates select="nameLast[.!='']" />
			<xsl:apply-templates select="nameSuffix[.!='']" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="multiRef" mode="ContactInf">
		<nc:ContactInformation>
			<xsl:attribute name="structures:id" select="generate-id(./mailAddress1)" />
			<xsl:apply-templates select="." mode="mailing" />
		</nc:ContactInformation>
	</xsl:template>
	<xsl:template match="multiRef" mode="Residence">
		<nc:Location>
			<xsl:attribute name="structures:id" select="generate-id(./resAddress1)" />
			<nc:Address>
				<xsl:apply-templates select="residencyCity[.!='']" />
				<xsl:apply-templates select="residencyState[.!='']" />
				<xsl:apply-templates select="residencyCountry[.!='']" />
			</nc:Address>
			<xsl:apply-templates select="residencyGeoCode[.!='']" />
		</nc:Location>
	</xsl:template>
	<xsl:template match="multiRef" mode="secondaryResidence">
		<nc:Location>
			<xsl:attribute name="structures:id" select="generate-id(./residencyCity)" />
			<nc:Address>
				<xsl:apply-templates select="resAddress2[.!='']" />
				<xsl:apply-templates select="resAddress1[.!='']" />
				<xsl:apply-templates select="resCity[.!='']" />
				<xsl:apply-templates select="resState[.!='']" />
				<xsl:apply-templates select="resCountry[.!='']" />
				<xsl:apply-templates select="resZip[.!='']" />
				<xsl:apply-templates select="resZipPlus[.!='']" />
			</nc:Address>
		</nc:Location>
	</xsl:template>
	<xsl:template match="multiRef" mode="ContactAssoc">
		<nc:ContactInformationAssociation>
			<nc:ContactEntity>
				<nc:EntityPerson>
					<xsl:attribute name="structures:ref" select="generate-id(.)" />
				</nc:EntityPerson>
			</nc:ContactEntity>
			<nc:ContactInformation>
				<xsl:attribute name="structures:ref" select="generate-id(./mailAddress1)" />
			</nc:ContactInformation>
		</nc:ContactInformationAssociation>
	</xsl:template>
	<xsl:template match="multiRef" mode="ResAssoc">
		<nc:PersonResidenceAssociation>
			<nc:Person>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:Person>
			<nc:Location>
				<xsl:attribute name="structures:ref" select="generate-id(./resAddress1)" />
			</nc:Location>
		</nc:PersonResidenceAssociation>
	</xsl:template>
	<xsl:template match="multiRef" mode="secondaryResAssoc">
		<wlq-res-ext:PersonSecondaryResidenceAssociation>
			<nc:Person>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</nc:Person>
			<nc:Location>
				<xsl:attribute name="structures:ref" select="generate-id(./residencyCity)" />
			</nc:Location>
		</wlq-res-ext:PersonSecondaryResidenceAssociation>
	</xsl:template>
	<xsl:template match="multiRef" mode="system">
		<xsl:variable name="system" select="source" />
		<xsl:if test="$system='EXTERNAL'">
			<intel:SystemIdentification>
				<nc:IdentificationID>
					<xsl:text>EXTERNAL-</xsl:text>
					<xsl:value-of select="externalCustSysNo" />
				</nc:IdentificationID>
				<nc:SystemName>
					<xsl:value-of
						select="'{http://ojbc.org/Services/WSDL/Wildlife_License_Query_Request_Service/1.0}Person-Query-Service---Wildlife-License'" />
				</nc:SystemName>
			</intel:SystemIdentification>
		</xsl:if>
		<xsl:if test="$system='MOSES'">
			<intel:SystemIdentification>
				<nc:IdentificationID>
					<xsl:text>MOSES-</xsl:text>
					<xsl:value-of select="mosesId" />
				</nc:IdentificationID>
				<nc:SystemName>
					<xsl:value-of
						select="'{http://ojbc.org/Services/WSDL/Wildlife_License_Query_Request_Service/1.0}Person-Query-Service---Wildlife-License'" />
				</nc:SystemName>
			</intel:SystemIdentification>
		</xsl:if>
	</xsl:template>
	<xsl:template match="multiRef" mode="mailing">
		<nc:ContactMailingAddress>
			<xsl:apply-templates select="mailAddress2[.!='']" />
			<xsl:apply-templates select="mailAddress1[.!='']" />
			<xsl:apply-templates select="mailCity[.!='']" />
			<xsl:apply-templates select="mailState[.!='']" />
			<xsl:apply-templates select="mailCountry[.!='']" />
			<xsl:apply-templates select="mailZip[.!='']" />
			<xsl:apply-templates select="mailZipPlus[.!='']" />
		</nc:ContactMailingAddress>
	</xsl:template>
	<xsl:template match="multiRef" mode="height">
		<xsl:variable name="heightFT" select="heightInFeet" />
		<xsl:variable name="heightINCH" select="heightInInches" />
		<nc:PersonHeightMeasure>
			<nc:MeasureValueText>
				<xsl:value-of select="concat($heightFT,$heightINCH)" />
			</nc:MeasureValueText>
			<nc:LengthUnitCode>FOT</nc:LengthUnitCode>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="weight">
		<nc:PersonWeightMeasure>
			<nc:MeasureValueText>
				<xsl:value-of select="." />
			</nc:MeasureValueText>
			<nc:WeightUnitCode>LBR</nc:WeightUnitCode>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="gender">
		<j:PersonSexCode>
			<xsl:value-of select="." />
		</j:PersonSexCode>
	</xsl:template>
	<xsl:template match="hairColor">
		<nc:PersonHairColorText>
			<xsl:value-of select="." />
		</nc:PersonHairColorText>
	</xsl:template>
	<xsl:template match="dob">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="concat(substring(.,7,4),'-',substring(.,1,2),'-',substring(.,4,2))" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="eyeColor">
		<nc:PersonEyeColorText>
			<xsl:value-of select="." />
		</nc:PersonEyeColorText>
	</xsl:template>
	<xsl:template match="mailZipPlus | resZipPlus">
		<nc:LocationPostalExtensionCode>
			<xsl:value-of select="." />
		</nc:LocationPostalExtensionCode>
	</xsl:template>
	<xsl:template match="mailZip | resZip">
		<nc:LocationPostalCode>
			<xsl:value-of select="." />
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="mailCountry | resCountry | residencyCountry">
		<nc:LocationCountryName>
			<xsl:value-of select="." />
		</nc:LocationCountryName>
	</xsl:template>
	<xsl:template match="mailState | resState | residencyState">
		<j:LocationStateNCICLISCode>
			<xsl:value-of select="." />
		</j:LocationStateNCICLISCode>
	</xsl:template>
	<xsl:template match="mailCity | resCity | residencyCity">
		<nc:LocationCityName>
			<xsl:value-of select="." />
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="mailAddress2 | resAddress2">
		<nc:AddressSecondaryUnitText>
			<xsl:value-of select="." />
		</nc:AddressSecondaryUnitText>
	</xsl:template>
	<xsl:template match="mailAddress1 | resAddress1">
		<nc:LocationStreet>
			<nc:StreetFullText>
				<xsl:value-of select="." />
			</nc:StreetFullText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="residencyGeoCode">
		<wlq-res-ext:GeoCode>
			<xsl:value-of select="." />
		</wlq-res-ext:GeoCode>
	</xsl:template>
	<xsl:template match="ssn">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="." />
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="nameFirst">
		<nc:PersonGivenName>
			<xsl:value-of select="." />
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nameMiddle">
		<nc:PersonMiddleName>
			<xsl:value-of select="." />
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nameLast">
		<nc:PersonSurName>
			<xsl:value-of select="." />
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="nameSuffix">
		<nc:PersonNameSuffixText>
			<xsl:value-of select="." />
		</nc:PersonNameSuffixText>
	</xsl:template>
</xsl:stylesheet>