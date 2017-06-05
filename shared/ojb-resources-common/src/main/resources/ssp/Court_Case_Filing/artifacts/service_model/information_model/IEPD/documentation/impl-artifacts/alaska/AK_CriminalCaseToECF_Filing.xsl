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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:core="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CoreFilingMessage-4.0" xmlns:criminalcase="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CriminalCase-4.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:ecf="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CommonTypes-4.0" xmlns:cc-filing-doc="http://ojbc.org/IEPD/Exchange/CourtCaseFiling/1.0" xmlns:cc-filing-ext="http://ojbc.org/IEPD/Extension/CourtCaseFiling/1.0" xmlns:j51="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:template match="/cc-filing-doc:CourtCaseFilingDocument">
		<core:CoreFilingMessage>
			<xsl:apply-templates select="." mode="ecf"/>
			<criminalcase:CriminalCase>
				<xsl:apply-templates select="nc30:DocumentFiledDate"/>
				<xsl:apply-templates select="nc30:Case"/>
				<xsl:apply-templates select="j51:Arrest"/>
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseCharge"/>
			</criminalcase:CriminalCase>
			<core:FilingConfidentialityIndicator>false</core:FilingConfidentialityIndicator>
			<core:FilingLeadDocument>
				<nc:DocumentBinary>
					<nc:BinaryCaptureDate>
						<nc:Date>2012-09-23</nc:Date>
					</nc:BinaryCaptureDate>
					<nc:BinaryCategoryText/>
				</nc:DocumentBinary>
				<ecf:DocumentMetadata>
					<j:RegisterActionDescriptionText/>
					<ecf:FilingAttorneyID/>
					<ecf:FilingPartyID/>
				</ecf:DocumentMetadata>
				<ecf:DocumentRendition>
					<ecf:DocumentRenditionMetadata>
						<ecf:DocumentAttachment/>
					</ecf:DocumentRenditionMetadata>
				</ecf:DocumentRendition>
			</core:FilingLeadDocument>
		</core:CoreFilingMessage>
	</xsl:template>
	<xsl:template match="j51:CaseCharge">
		<criminalcase:CaseCharge>
			<xsl:apply-templates select="j51:ChargeSequenceID"/>
			<xsl:apply-templates select="j51:ChargeSeverityLevel/j51:SeverityLevelDescriptionText"/>
			<!--xsl:apply-templates select="j51:ChargeSeverityText"/-->
			<xsl:apply-templates select="j51:ChargeStatute"/>
			<criminalcase:ChargeOffense/>
			<criminalcase:ChargeAmendedIndicator>false</criminalcase:ChargeAmendedIndicator>
		</criminalcase:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j51:StatuteCodeIdentification"/>
			<xsl:apply-templates select="j51:StatuteCodeSectionIdentification"/>
			<xsl:apply-templates select="j51:StatuteDescriptionText"/>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:StatuteCodeSectionIdentification">
		<j:StatuteCodeSectionIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:StatuteCodeSectionIdentification>
	</xsl:template>
	<xsl:template match="j51:StatuteCodeIdentification">
		<j:StatuteCodeIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:StatuteCodeIdentification>
	</xsl:template>
	<xsl:template match="j51:Arrest">
		<criminalcase:CaseArrest>
			<xsl:apply-templates select="j51:ArrestAgency"/>
			<xsl:apply-templates select="j51:ArrestAgencyRecordIdentification"/>
			<xsl:apply-templates select="j51:ArrestOfficial/j51:EnforcementOfficialBadgeIdentification"/>
		</criminalcase:CaseArrest>
	</xsl:template>
	<xsl:template match="j51:ArrestAgencyRecordIdentification">
		<j:ArrestAgencyRecordIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:ArrestAgencyRecordIdentification>
	</xsl:template>
	<xsl:template match="j51:EnforcementOfficialBadgeIdentification">
		<j:ArrestOfficial>
			<j:EnforcementOfficialBadgeIdentification>
				<xsl:apply-templates select="nc30:IdentificationID"/>
			</j:EnforcementOfficialBadgeIdentification>
		</j:ArrestOfficial>
	</xsl:template>
	<xsl:template match="nc30:Case">
		<xsl:apply-templates select="nc30:ActivityCategoryText"/>
		<xsl:apply-templates select="j51:CaseAugmentation"/>
	</xsl:template>
	<xsl:template match="j51:CaseAugmentation">
		<j:CaseAugmentation>
			<xsl:apply-templates select="j51:CaseCourt"/>
			<xsl:apply-templates select="j51:CaseCourtEvent"/>
			<xsl:apply-templates select="/cc-filing-doc:CourtCaseFilingDocument/nc30:Person[@structures:id=/cc-filing-doc:CourtCaseFilingDocument/nc30:PersonResidenceAssociation/nc30:Person/@structures:ref]" mode="subject"/>
			<xsl:apply-templates select="j51:CaseInitiatingParty"/>
			<xsl:apply-templates select="j51:CaseProsecutionAttorney/j51:JudicialOfficialBarMembership/j51:JudicialOfficialBarIdentification/nc30:IdentificationID" mode="prosecutor"/>
		</j:CaseAugmentation>
		<ecf:CaseAugmentation/>
	</xsl:template>
	<xsl:template match="j51:CaseCourt">
		<j:CaseCourt>
			<xsl:apply-templates select="nc30:OrganizationName"/>
			<xsl:apply-templates select="j51:CourtName"/>
		</j:CaseCourt>
	</xsl:template>
	<xsl:template match="j51:CaseCourtEvent">
		<j:CaseCourtEvent>
			<j:CourtEventSchedule>
				<nc:ScheduleDate>
					<nc:Date>
						<xsl:value-of select="j51:CourtEventAppearance/j51:CourtAppearanceDate/nc30:Date"/>
					</nc:Date>
				</nc:ScheduleDate>
			</j:CourtEventSchedule>
		</j:CaseCourtEvent>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="subject">
		<j:CaseDefendantParty>
			<ecf:EntityPerson>
				<xsl:apply-templates select="nc30:PersonBirthDate"/>
				<xsl:apply-templates select="nc30:PersonName"/>
				<xsl:apply-templates select="cc-filing-ext:OperatorLicenseIdentification"/>
				<xsl:apply-templates select="cc-filing-ext:AlaskaPublicSafetyInformationNetworkIdentification"/>
				<xsl:apply-templates select="j51:PersonAugmentation/j51:DriverLicense/j51:DriverLicenseIdentification"/>
				<xsl:apply-templates select="/cc-filing-doc:CourtCaseFilingDocument/nc30:Location[@structures:id=/cc-filing-doc:CourtCaseFilingDocument/nc30:PersonResidenceAssociation/nc30:Location/@structures:ref]/nc30:Address" mode="subject"/>
			</ecf:EntityPerson>
		</j:CaseDefendantParty>
	</xsl:template>
	<xsl:template match="j51:CaseInitiatingParty">
		<j:CaseInitiatingParty>
			<xsl:apply-templates select="/cc-filing-doc:CourtCaseFilingDocument/nc30:Organization[@structures:id=/cc-filing-doc:CourtCaseFilingDocument/nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty/nc30:EntityOrganization/@structures:ref]/nc30:OrganizationName" mode="initiating_party"/>
			<xsl:apply-templates select="/cc-filing-doc:CourtCaseFilingDocument/nc30:Person[@structures:id=/cc-filing-doc:CourtCaseFilingDocument/j51:JudicialOfficial[@structures:id=/cc-filing-doc:CourtCaseFilingDocument/nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty/nc30:EntityPerson/@structures:ref]/nc30:RoleOfPerson/@structures:ref]/nc30:PersonName" mode="initiating_party"/>
		</j:CaseInitiatingParty>
	</xsl:template>
	<xsl:template match="nc30:OrganizationName" mode="initiating_party">
		<ecf:EntityOrganization>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</ecf:EntityOrganization>
	</xsl:template>
	<xsl:template match="nc30:PersonName" mode="initiating_party">
		<ecf:EntityPerson>
			<nc:PersonName>
				<xsl:apply-templates select="nc30:PersonFullName"/>
			</nc:PersonName>
		</ecf:EntityPerson>
	</xsl:template>
	<xsl:template match="nc30:Address" mode="subject">
		<ecf:PersonAugmentation>
			<nc:ContactInformation>
				<nc:ContactMailingAddress>
					<nc:StructuredAddress>
						<xsl:apply-templates select="nc30:LocationStreet/nc30:StreetFullText"/>
						<xsl:apply-templates select="nc30:LocationCityName"/>
						<xsl:apply-templates select="nc30:LocationStateName"/>
						<xsl:apply-templates select="nc30:LocationPostalCode"/>
					</nc:StructuredAddress>
				</nc:ContactMailingAddress>
			</nc:ContactInformation>
		</ecf:PersonAugmentation>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc30:PersonGivenName"/>
			<xsl:apply-templates select="nc30:PersonMiddleName"/>
			<xsl:apply-templates select="nc30:PersonSurName"/>
			<xsl:apply-templates select="nc30:PersonFullName"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="cc-filing-ext:OperatorLicenseIdentification">
		<nc:PersonOtherIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
			<xsl:apply-templates select="nc30:IdentificationCategoryText"/>
		</nc:PersonOtherIdentification>
	</xsl:template>
	<xsl:template match="cc-filing-ext:AlaskaPublicSafetyInformationNetworkIdentification">
		<nc:PersonOtherIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
			<nc:IdentificationCategoryText>APSIN</nc:IdentificationCategoryText>
		</nc:PersonOtherIdentification>
	</xsl:template>
	<xsl:template match="j51:DriverLicenseIdentification">
		<nc:PersonOtherIdentification>
			<xsl:apply-templates select="nc30:IdentificationID"/>
			<nc:IdentificationCategoryText>DDL</nc:IdentificationCategoryText>
			<xsl:apply-templates select="nc30:IdentificationJurisdiction/nc30:JurisdictionText"/>
		</nc:PersonOtherIdentification>
	</xsl:template>
	<!-- MATCH -->
	<xsl:template match="cc-filing-doc:CourtCaseFilingDocument" mode="ecf">
		<ecf:SendingMDELocationID/>
		<ecf:SendingMDEProfileCode/>
	</xsl:template>
	<xsl:template match="nc30:DocumentFiledDate">
		<nc:ActivityDate>
			<nc:Date>
				<xsl:value-of select="nc30:Date"/>
			</nc:Date>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc30:ActivityCategoryText">
		<nc:CaseCategoryText>
			<xsl:value-of select="."/>
		</nc:CaseCategoryText>
	</xsl:template>
	<xsl:template match="nc30:OrganizationName">
		<nc:OrganizationName>
			<xsl:value-of select="."/>
		</nc:OrganizationName>
	</xsl:template>
	<xsl:template match="j51:CourtName">
		<j:CourtName>
			<xsl:value-of select="."/>
		</j:CourtName>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="nc30:Date"/>
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="."/>
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc30:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="."/>
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc30:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="."/>
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="nc30:PersonFullName">
		<nc:PersonFullName>
			<xsl:value-of select="."/>
		</nc:PersonFullName>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID">
		<nc:IdentificationID>
			<xsl:value-of select="."/>
		</nc:IdentificationID>
	</xsl:template>
	<xsl:template match="nc30:IdentificationCategoryText">
		<nc:IdentificationCategoryText>
			<xsl:value-of select="."/>
		</nc:IdentificationCategoryText>
	</xsl:template>
	<xsl:template match="nc30:JurisdictionText">
		<nc:IdentificationSourceText>
			<xsl:value-of select="."/>
		</nc:IdentificationSourceText>
	</xsl:template>
	<xsl:template match="nc30:StreetFullText">
		<nc:LocationStreet>
			<nc:StreetFullText>
				<xsl:value-of select="."/>
			</nc:StreetFullText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc30:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="."/>
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc30:LocationStateName">
		<nc:LocationStateName>
			<xsl:value-of select="."/>
		</nc:LocationStateName>
	</xsl:template>
	<xsl:template match="nc30:LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="."/>
		</nc:LocationPostalCode>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="prosecutor">
		<j:CaseProsecutionAttorney>
			<nc:RoleOfPersonReference/>
			<j:JudicialOfficialBarMembership>
				<j:JudicialOfficialBarIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="."/>
					</nc:IdentificationID>
				</j:JudicialOfficialBarIdentification>
			</j:JudicialOfficialBarMembership>
		</j:CaseProsecutionAttorney>
	</xsl:template>
	<xsl:template match="j51:ArrestAgency">
		<j:ArrestAgency>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</j:ArrestAgency>
	</xsl:template>
	<xsl:template match="j51:ChargeSequenceID">
		<j:ChargeSequenceID>
			<xsl:apply-templates select="nc30:IdentificationID"/>
		</j:ChargeSequenceID>
	</xsl:template>
	<xsl:template match="j51:SeverityLevelDescriptionText">
		<j:ChargeSeverityLevel>
			<j:SeverityLevelDescriptionText>
				<xsl:value-of select="."/>
			</j:SeverityLevelDescriptionText>
		</j:ChargeSeverityLevel>
	</xsl:template>
	<xsl:template match="j51:SeverityLevelText">
		<j:ChargeSeverityLevel>
			<j:SeverityLevelDescriptionText>
				<xsl:value-of select="."/>
			</j:SeverityLevelDescriptionText>
		</j:ChargeSeverityLevel>
	</xsl:template>
	<xsl:template match="j51:StatuteDescriptionText">
		<j:StatuteDescriptionText>
			<xsl:value-of select="."/>
		</j:StatuteDescriptionText>
	</xsl:template>
</xsl:stylesheet>
