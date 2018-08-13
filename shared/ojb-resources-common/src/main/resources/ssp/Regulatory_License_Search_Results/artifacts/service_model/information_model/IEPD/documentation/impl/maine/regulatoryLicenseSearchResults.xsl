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
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:rls-res-doc="http://ojbc.org/IEPD/Exchange/RegulatoryLicenseSearchResults/1.0"
	xmlns:rls-res-ext="http://ojbc.org/IEPD/Extensions/RegulatoryLicenseSearchResults/1.0"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0" xmlns:mbsc="http://www.sauper.com/MBCSOnlineServices"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/4.0/" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" exclude-result-prefixes="soap mbsc">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="/soap:Envelope/soap:Body/mbsc:FindLicenseInformationResponse">
		<rls-res-doc:RegulatoryLicenseSearchResults>
			<xsl:apply-templates select="mbsc:LicenseInfoResult" />
		</rls-res-doc:RegulatoryLicenseSearchResults>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfoResult">
		<xsl:choose>
			<xsl:when test="./mbsc:Messages/mbsc:string !=''">
				<xsl:apply-templates select="mbsc:Messages" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="mbsc:LicenseInfos" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfos">
		<xsl:apply-templates select="mbsc:LicenseInfo" mode="license" />
		<xsl:apply-templates select="mbsc:LicenseInfo" mode="person" />
		<xsl:apply-templates select="./mbsc:LicenseInfo/mbsc:PhysicalAddress[.!='']" />
		<xsl:apply-templates select="./mbsc:LicenseInfo/mbsc:CaseInfos[.!='']" />
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfo" mode="license">
		<rls-res-ext:RegulatoryLicense>
			<xsl:apply-templates select="mbsc:LicenseNumber[.!='']" />
			<xsl:apply-templates select="mbsc:LicenseTypeDescription[.!='']" />
			<xsl:apply-templates select="mbsc:Agency[.!='']" />
			<xsl:apply-templates select="mbsc:Department[.!='']" />
			<xsl:apply-templates select="mbsc:Regulator[.!='']" />
			<xsl:apply-templates select="mbsc:IssueDate[.!='']" />
			<xsl:apply-templates select="mbsc:ExpirationDate[.!='']" />
			<xsl:apply-templates select=".[.!='']" mode="licensePerson" />
			<xsl:apply-templates select="mbsc:LicenseeID[.!='']" />
			<xsl:apply-templates select=".[.!='']" mode="licenseStatus" />
			<xsl:apply-templates select="mbsc:CurrentlyAuthorizedToPractice[.!='']" />
		</rls-res-ext:RegulatoryLicense>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfo" mode="person">
		<nc:Person>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<xsl:apply-templates select="mbsc:BirthDate[.!='']" />
			<xsl:apply-templates select=".[.!='']" mode="personName" />
			<xsl:apply-templates select="mbsc:MailingAddress[.!='']" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="mbsc:CaseInfos">
		<xsl:apply-templates select="mbsc:CaseInfo[.!='']" />
	</xsl:template>
	<xsl:template match="mbsc:CaseInfo">
		<nc:Case>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<xsl:apply-templates select="mbsc:CaseID[.!='']" />
			<xsl:apply-templates select="." mode="dates" />
			<xsl:apply-templates select="mbsc:ResolutionInfos" />
		</nc:Case>
	</xsl:template>
	<xsl:template match="mbsc:ResolutionInfos">
		<rls-res-ext:CaseComplaint>
			<xsl:apply-templates select="mbsc:ResolutionInfo[.!='']" />
		</rls-res-ext:CaseComplaint>
	</xsl:template>
	<xsl:template match="mbsc:ResolutionInfo">
		<rls-res-ext:ComplaintResolution>
			<xsl:apply-templates select="mbsc:ResolutionDate[.!='']" />
			<xsl:apply-templates select="mbsc:ResolutionDescription[.!='']" />
		</rls-res-ext:ComplaintResolution>
		<xsl:apply-templates select="mbsc:AllegationInfos[.!='']" />
		<xsl:apply-templates select="mbsc:ActionInfos[.!='']" />
	</xsl:template>
	<xsl:template match="mbsc:AllegationInfos">

			<xsl:apply-templates select="mbsc:AllegationInfo[.!='']" />

	</xsl:template>
		<xsl:template match="mbsc:ActionInfos">

			<xsl:apply-templates select="mbsc:ActionInfo[.!='']" />

	</xsl:template>
	
	
	<xsl:template match="mbsc:AllegationInfo">
		<rls-res-ext:ComplaintAllegation>
			<xsl:apply-templates select="mbsc:AllegationDescription[.!='']" />
		</rls-res-ext:ComplaintAllegation>
	</xsl:template>
	
		<xsl:template match="mbsc:ActionInfo">
		<rls-res-ext:ComplaintAction>
			<xsl:apply-templates select="mbsc:ActionDescription[.!='']" />
		</rls-res-ext:ComplaintAction>
	</xsl:template>
	
	
	
	<!-- ELEMENTS -->
	<xsl:template match="mbsc:LicenseNumber">
		<rls-res-ext:RegulatoryLicenseIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</rls-res-ext:RegulatoryLicenseIdentification>
	</xsl:template>
	<xsl:template match="mbsc:LicenseTypeDescription">
		<rls-res-ext:RegulatoryLicenseCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:RegulatoryLicenseCategoryText>
	</xsl:template>
	<xsl:template match="mbsc:Agency">
		<rls-res-ext:RegulatoryLicenseAgency>
			<nc:OrganizationName>
				<xsl:value-of select="normalize-space(.)" />
			</nc:OrganizationName>
		</rls-res-ext:RegulatoryLicenseAgency>
	</xsl:template>
	<xsl:template match="mbsc:Department">
		<rls-res-ext:RegulatoryLicenseDepartmentName>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:RegulatoryLicenseDepartmentName>
	</xsl:template>
	<xsl:template match="mbsc:Regulator">
		<rls-res-ext:RegulatoryLicenseRegulatorName>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:RegulatoryLicenseRegulatorName>
	</xsl:template>
	<xsl:template match="mbsc:IssueDate">
		<rls-res-ext:RegulatoryLicenseIssueDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</rls-res-ext:RegulatoryLicenseIssueDate>
	</xsl:template>
	<xsl:template match="mbsc:ExpirationDate">
		<rls-res-ext:RegulatoryLicenseExpirationDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</rls-res-ext:RegulatoryLicenseExpirationDate>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfo" mode="licensePerson">
		<rls-res-ext:RegulatoryLicensePerson>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
		</rls-res-ext:RegulatoryLicensePerson>
	</xsl:template>
	<xsl:template match="mbsc:LicenseeID">
		<rls-res-ext:PersonLicenseeID>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</rls-res-ext:PersonLicenseeID>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfo" mode="licenseStatus">
		<rls-res-ext:RegulatoryLicenseStatus>
			<xsl:apply-templates select="mbsc:LicenseStatusDescription[.!='']" />
			<xsl:apply-templates select="mbsc:SecondaryStatusDescription[.!='']" />
		</rls-res-ext:RegulatoryLicenseStatus>
	</xsl:template>
	<xsl:template match="mbsc:LicenseStatusDescription">
		<nc:StatusDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:StatusDescriptionText>
	</xsl:template>
	<xsl:template match="mbsc:SecondaryStatusDescription">
		<rls-res-ext:SecondaryStatusDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:SecondaryStatusDescriptionText>
	</xsl:template>
	<xsl:template match="mbsc:CurrentlyAuthorizedToPractice">
		<rls-res-ext:CurrentlyAuthorizedToPracticeIndicator>
			<xsl:choose>
				<xsl:when test=". = 'True'">
					<xsl:value-of select="'true'" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'false'" />
				</xsl:otherwise>
			</xsl:choose>
		</rls-res-ext:CurrentlyAuthorizedToPracticeIndicator>
	</xsl:template>
	<xsl:template match="mbsc:BirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="mbsc:LicenseInfo" mode="personName">
		<nc:PersonName>
			<xsl:apply-templates select="mbsc:FirstName[.!='']" />
			<xsl:apply-templates select="mbsc:MiddleName[.!='']" />
			<xsl:apply-templates select="mbsc:LastName[.!='']" />
			<xsl:apply-templates select="mbsc:NameSuffix[.!='']" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="mbsc:FirstName">
		<nc:PersonGivenName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="mbsc:MiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="mbsc:LastName">
		<nc:PersonSurName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="mbsc:NameSuffix">
		<nc:PersonNameSuffixText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonNameSuffixText>
	</xsl:template>
	<xsl:template match="mbsc:MailingAddress">
		<nc:PersonHomeContactInformation>
			<nc:ContactMailingAddress>
				<xsl:apply-templates select="." mode="address" />
			</nc:ContactMailingAddress>
		</nc:PersonHomeContactInformation>
	</xsl:template>
	<xsl:template match="mbsc:MailingAddress" mode="address">
		<xsl:apply-templates select="." mode="street" />
		<xsl:apply-templates select="mbsc:City[.!='']" />
		<xsl:apply-templates select="mbsc:StateCode[.!='']" />
		<xsl:apply-templates select="mbsc:Country[.!='']" />
		<xsl:apply-templates select="mbsc:PostalCode[.!='']" />
	</xsl:template>
	<xsl:template match="mbsc:PhysicalAddress">
		<nc:Location>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<nc:Address>
				<xsl:apply-templates select="." mode="street" />
				<xsl:apply-templates select="mbsc:City[.!='']" />
				<xsl:apply-templates select="mbsc:StateCode[.!='']" />
				<xsl:apply-templates select="mbsc:Country[.!='']" />
				<xsl:apply-templates select="mbsc:PostalCode[.!='']" />
			</nc:Address>
		</nc:Location>
	</xsl:template>
	<xsl:template match="mbsc:MailingAddress | mbsc:PhysicalAddress" mode="street">
		<nc:LocationStreet>
			<xsl:for-each select="node()[starts-with(name(), 'AddressLine')]">
				<nc:StreetFullText>
					<xsl:value-of select="normalize-space(.)" />
				</nc:StreetFullText>
			</xsl:for-each>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="mbsc:City">
		<nc:LocationCityName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="mbsc:StateCode">
		<nc:LocationState>
			<nc:LocationStateUSPostalServiceCode>
				<xsl:value-of select="normalize-space(.)" />
			</nc:LocationStateUSPostalServiceCode>
		</nc:LocationState>
	</xsl:template>
	<xsl:template match="mbsc:Country">
		<nc:LocationCountry>
			<nc:LocationCountryName>
				<xsl:value-of select="normalize-space(.)" />
			</nc:LocationCountryName>
		</nc:LocationCountry>
	</xsl:template>
	<xsl:template match="mbsc:PostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="mbsc:CaseID">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="mbsc:CaseInfo" mode="dates">
		<nc:ActivityDate>
			<nc:DateRange>
				<xsl:apply-templates select="mbsc:DateOpened[.!='']" />
				<xsl:apply-templates select="mbsc:DateClosed[.!='']" />
			</nc:DateRange>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="mbsc:DateOpened">
		<nc:StartDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</nc:StartDate>
	</xsl:template>
	<xsl:template match="mbsc:DateClosed">
		<nc:EndDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</nc:EndDate>
	</xsl:template>
	<xsl:template match="mbsc:ResolutionDate">
		<rls-res-ext:ResolutionDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</rls-res-ext:ResolutionDate>
	</xsl:template>
	<xsl:template match="mbsc:ResolutionDescription">
		<rls-res-ext:ResolutionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:ResolutionDescriptionText>
	</xsl:template>
	<xsl:template match="mbsc:AllegationDescription">
		<rls-res-ext:AllegationDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</rls-res-ext:AllegationDescriptionText>
	</xsl:template>
	
		<xsl:template match="mbsc:ActionDescription">
	<rls-res-ext:ActionDescriptionText>
	<xsl:value-of select="normalize-space(.)" />
	t</rls-res-ext:ActionDescriptionText>
	</xsl:template>
	
	
	<!-- ERROR -->
	<xsl:template match="mbsc:Messages">
		<qrm:SearchResultsMetadata>
			<qrer:SearchRequestError>
				<xsl:for-each select="./mbsc:string">
					<qrer:ErrorText>
						<xsl:value-of select="normalize-space(.)" />
					</qrer:ErrorText>
				</xsl:for-each>
			</qrer:SearchRequestError>
		</qrm:SearchResultsMetadata>
	</xsl:template>
</xsl:stylesheet>