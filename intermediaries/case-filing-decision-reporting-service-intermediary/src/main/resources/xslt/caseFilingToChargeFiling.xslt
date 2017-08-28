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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cfd-doc="http://ojbc.org/IEPD/Exchange/CaseFilingDecisionReport/1.0" xmlns:cfd-ext="http://ojbc.org/IEPD/Extensions/CaseFilingDecisionReportExtension/1.0"
	xmlns:j51="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xop="http://www.w3.org/2004/08/xop/include" xmlns:criminal="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CriminalCase-4.0"
	xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:ecf="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CommonTypes-4.0"
	xmlns:s="http://niem.gov/niem/structures/2.0" exclude-result-prefixes="cfd-doc cfd-ext j51 nc30 niem-xs structures">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="cfd-doc:CaseFilingDecisionReport">
		<CoreFilingMessage xmlns="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CoreFilingMessage-4.0">
			<!-- SendingMDELocationID and SendingMDEProfileCode elements required by ECF -->
			<ecf:SendingMDELocationID>
				<nc:IdentificationID />
			</ecf:SendingMDELocationID>
			<ecf:SendingMDEProfileCode />
			<criminal:CriminalCase>
				<xsl:apply-templates select="nc30:Case" />
				<xsl:apply-templates select="." mode="ecfAugmentation" />
				<xsl:apply-templates
					select="nc30:Case/j51:CaseAugmentation/j51:CaseCharge[@structures:id=../../../j51:OffenseChargeAssociation/j51:Charge/@structures:ref]"
					mode="charge" />
				<!-- ChargeAmendedIndicator required by ECF -->
				<criminal:ChargeAmendedIndicator>false</criminal:ChargeAmendedIndicator>
			</criminal:CriminalCase>
			<!-- FilingLeadDocument element required by ECF -->
			<FilingLeadDocument>
				<ecf:DocumentMetadata>
					<j:RegisterActionDescriptionText>String</j:RegisterActionDescriptionText>
					<ecf:FilingAttorneyID />
					<ecf:FilingPartyID />
				</ecf:DocumentMetadata>
				<ecf:DocumentRendition>
					<ecf:DocumentRenditionMetadata>
						<ecf:DocumentAttachment />
					</ecf:DocumentRenditionMetadata>
				</ecf:DocumentRendition>
			</FilingLeadDocument>
		</CoreFilingMessage>
	</xsl:template>
	<xsl:template match="nc30:Case">
		<xsl:apply-templates select="nc30:CaseTitleText" />
		<xsl:apply-templates select="nc30:CaseCategoryText" />
		<xsl:apply-templates select="j51:CaseAugmentation/j51:CaseCourt" />
	</xsl:template>
	<xsl:template match="j51:CaseCourt">
		<j:CaseAugmentation>
			<j:CaseCourt>
				<xsl:apply-templates select="nc30:OrganizationLocation/nc30:LocationName" mode="court" />
				<xsl:apply-templates select="j51:CourtName" />
			</j:CaseCourt>
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="cfd-doc:CaseFilingDecisionReport" mode="ecfAugmentation">
		<ecf:CaseAugmentation>
			<ecf:CaseParticipant>
				<xsl:apply-templates
					select="nc30:Person[@structures:id=../nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson/@structures:ref]"
					mode="defendant" />
				<xsl:apply-templates
					select="nc30:Location[@structures:id=../nc30:PersonResidenceAssociation/nc30:Location/@structures:ref]/nc30:Address" mode="residence" />
			</ecf:CaseParticipant>
		</ecf:CaseAugmentation>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="defendant">
		<ecf:EntityPerson>
			<xsl:apply-templates select="nc30:PersonBirthDate" />
			<xsl:apply-templates select="nc30:PersonName" />
			<xsl:apply-templates select="nc30:PersonSexCode" />
			<xsl:apply-templates select="j51:PersonAugmentation/j51:PersonStateFingerprintIdentification/nc30:IdentificationID"
				mode="SID" />
		</ecf:EntityPerson>
		<xsl:apply-templates select="../nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/cfd-ext:PartyRoleText" />
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc30:PersonGivenName" />
			<xsl:apply-templates select="nc30:PersonMiddleName" />
			<xsl:apply-templates select="nc30:PersonSurName" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc30:Address" mode="residence">
		<nc:ContactInformation>
			<nc:ContactMailingAddress>
				<nc:StructuredAddress>
					<xsl:apply-templates select="nc30:LocationStreet/nc30:StreetFullText" />
					<xsl:apply-templates select="nc30:LocationCityName" />
					<xsl:apply-templates select="nc30:LocationStateUSPostalServiceCode" />
					<xsl:apply-templates select="nc30:LocationPostalCode" />
				</nc:StructuredAddress>
			</nc:ContactMailingAddress>
			<xsl:apply-templates select="../nc30:LocationCategoryText" />
		</nc:ContactInformation>
	</xsl:template>
	<xsl:template match="j51:CaseCharge" mode="charge">
		<xsl:variable name="chargeid" select="./@structures:id" />
		<criminal:CaseCharge>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="j51:ChargeSeverityLevel/j51:SeverityLevelDescriptionText" />
			<xsl:apply-templates select="j51:ChargeStatute" />
			<xsl:apply-templates
				select="../../../j51:Offense[@structures:id=../j51:OffenseChargeAssociation[j51:Charge/@structures:ref=$chargeid]/j51:Offense/@structures:ref]" />
		</criminal:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j51:StatuteCodeIdentification" />
			<xsl:apply-templates select="j51:StatuteDescriptionText" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:Offense">
		<criminal:ChargeOffense>
			<xsl:apply-templates select="nc30:ActivityDate" />
		</criminal:ChargeOffense>
	</xsl:template>
	<xsl:template match="nc30:ActivityDate">
		<nc:ActivityDate>
			<xsl:apply-templates select="nc30:DateTime" />
		</nc:ActivityDate>
	</xsl:template>
	<!-- MATCH -->
	<xsl:template match="nc30:CaseTitleText">
		<nc:CaseTitleText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:CaseTitleText>
	</xsl:template>
	<xsl:template match="nc30:CaseCategoryText">
		<nc:CaseCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:CaseCategoryText>
	</xsl:template>
	<xsl:template match="nc30:LocationName" mode="court">
		<nc:OrganizationLocation>
			<nc:LocationName>
				<xsl:value-of select="normalize-space(.)" />
			</nc:LocationName>
		</nc:OrganizationLocation>
	</xsl:template>
	<xsl:template match="j51:CourtName">
		<j:CourtName>
			<xsl:value-of select="normalize-space(.)" />
		</j:CourtName>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<xsl:apply-templates select="nc30:Date" />
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonGivenName">
		<nc:PersonGivenName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="nc30:PersonMiddleName">
		<nc:PersonMiddleName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="nc30:PersonSurName">
		<nc:PersonSurName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="nc30:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="SID">
		<nc:PersonStateIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:PersonStateIdentification>
	</xsl:template>
	<xsl:template match="cfd-ext:PartyRoleText">
		<ecf:CaseParticipantRoleCode>
			<xsl:value-of select="normalize-space(.)" />
		</ecf:CaseParticipantRoleCode>
	</xsl:template>
	<xsl:template match="nc30:StreetFullText">
		<nc:LocationStreet>
			<nc:StreetFullText>
				<xsl:value-of select="normalize-space(.)" />
			</nc:StreetFullText>
		</nc:LocationStreet>
	</xsl:template>
	<xsl:template match="nc30:LocationCityName">
		<nc:LocationCityName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:LocationCityName>
	</xsl:template>
	<xsl:template match="nc30:LocationStateUSPostalServiceCode">
		<nc:LocationStateName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:LocationStateName>
	</xsl:template>
	<xsl:template match="nc30:LocationPostalCode">
		<nc:LocationPostalCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:LocationPostalCode>
	</xsl:template>
	<!-- NOTE: For equating LocationCategoryText with ContactInformationDescriptionText -->
	<xsl:template match="nc30:LocationCategoryText">
		<nc:ContactInformationDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:ContactInformationDescriptionText>
	</xsl:template>
	<xsl:template match="j51:SeverityLevelDescriptionText">
		<j:ChargeSeverityLevel>
			<j:SeverityLevelDescriptionText>
				<xsl:value-of select="normalize-space(.)" />
			</j:SeverityLevelDescriptionText>
		</j:ChargeSeverityLevel>
	</xsl:template>
	<xsl:template match="j51:StatuteCodeIdentification">
		<j:StatuteCodeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</j:StatuteCodeIdentification>
	</xsl:template>
	<xsl:template match="j51:StatuteDescriptionText">
		<j:StatuteDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:StatuteDescriptionText>
	</xsl:template>
	<xsl:template match="nc30:Date">
		<nc:Date>
			<xsl:value-of select="normalize-space(.)" />
		</nc:Date>
	</xsl:template>
	<xsl:template match="nc30:DateTime">
		<nc:DateTime>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DateTime>
	</xsl:template>
</xsl:stylesheet>