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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:pd-doc="http://ojbc.org/IEPD/Exchange/ProsecutionDecisionReport/1.0"
	xmlns:pd-ext="http://ojbc.org/IEPD/Extensions/ProsecutionDecisionReportExtension/1.0"
	
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exc="http://jjis.hawaii.gov/JJISExchange"
	xmlns:ext="http://jjis.hawaii.gov/JJISCommonExtension" xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:pd-hi-codes="http://ojbc.org/IEPD/Extensions/ProsecutionDecisionReportCodes/Hawaii/1.0"
	exclude-result-prefixes="pd-hi-codes pd-doc pd-ext niem-xs structures j51 nc30">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="Report/Tablix1/Details_Collection" xmlns="LegalInquiry_x0020_Custom">
		<exc:JJISProsCaseExchange>
			<nc:Document>
				<nc:DocumentCategoryName>Prosecutor Decision</nc:DocumentCategoryName>
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseCourtEvent/nc30:ActivityDescriptionText"
					mode="event" />
				<nc:DocumentSubmitter>
					<nc:EntityOrganization>
						<nc:OrganizationAbbreviationText>HPA</nc:OrganizationAbbreviationText>
						<nc:OrganizationName>Honolulu Prosecuting Attorney</nc:OrganizationName>
					</nc:EntityOrganization>
				</nc:DocumentSubmitter>
			</nc:Document>
			<xsl:apply-templates select="nc30:Case" />
			<xsl:apply-templates select="nc30:Person" />
			<xsl:apply-templates
				select="nc30:Case/j51:CaseAugmentation/j51:CaseProsecutionAttorney/nc30:RoleOfPerson/nc30:PersonName/nc30:PersonFullName"
				mode="pros" />
			<xsl:apply-templates select="j51:Offense" />
			<xsl:apply-templates select="j51:OffenseChargeAssociation" />
			<xsl:apply-templates select="." mode="osa" />
		</exc:JJISProsCaseExchange>
	</xsl:template>
	<xsl:template match="nc30:Case">
		<j:Case>
			<xsl:apply-templates select="nc30:ActivityIdentification" />
			<xsl:apply-templates select="nc30:ActivityStatus/pd-hi-codes:CaseStatusCode" />
			<xsl:apply-templates select="nc30:CaseDisposition/nc30:DispositionText" />
			<xsl:apply-templates select="../j51:Arrest" />
			<xsl:apply-templates select="j51:CaseAugmentation" />
		</j:Case>
	</xsl:template>
	<xsl:template match="nc30:Person">
		<j:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="./@structures:id" /></xsl:attribute>
			<xsl:variable name="personID" select="./@structures:id" />
			<xsl:apply-templates select="nc30:PersonBirthDate" />
			<xsl:apply-templates select="nc30:PersonName" />
			<xsl:apply-templates select="j51:PersonAugmentation" />
			<xsl:apply-templates select="j51:PersonSexCode" />
		</j:Person>
	</xsl:template>
	<xsl:template match="j51:Offense">
		<j:Offense>
			<xsl:attribute name="s:id"><xsl:value-of select="./@structures:id" /></xsl:attribute>
			<nc:ActivityDate>
				<nc:Date>
					<xsl:value-of select="normalize-space(nc30:ActivityDate/nc30:Date)" />
				</nc:Date>
			</nc:ActivityDate>
		</j:Offense>
	</xsl:template>
	<xsl:template match="j51:OffenseChargeAssociation">
		<j:OffenseChargeAssociation>
			<j:OffenseReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="j51:Offense/@structures:ref" /></xsl:attribute>
			</j:OffenseReference>
			<j:ChargeReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="j51:Charge/@structures:ref" /></xsl:attribute>
			</j:ChargeReference>
		</j:OffenseChargeAssociation>
	</xsl:template>
	<xsl:template match="j51:OffenseSubjectAssociation">
		<j:OffenseSubjectAssociation>
			<j:OffenseReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="j51:Offense/@structures:ref" /></xsl:attribute>
			</j:OffenseReference>
			<j:SubjectReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="j51:Subject/@structures:ref" /></xsl:attribute>
			</j:SubjectReference>
		</j:OffenseSubjectAssociation>
	</xsl:template>
	<xsl:template match="pd-hi-codes:CaseStatusCode">
		<nc:CaseCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:CaseCategoryText>
	</xsl:template>
	<xsl:template match="nc30:ActivityDescriptionText" mode="event">
		<nc:DocumentSummaryText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DocumentSummaryText>
	</xsl:template>
	<xsl:template match="nc30:DispositionText">
		<nc:CaseGeneralCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:CaseGeneralCategoryText>
	</xsl:template>
	<xsl:template match="nc30:ActivityIdentification">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="j51:Arrest">
		<nc:CaseFiling>
			<xsl:apply-templates select="../nc30:Case/pd-ext:CaseReferralDate/nc30:DateTime" mode="referral" />
			<xsl:apply-templates select="j51:ArrestAgency/nc30:OrganizationName" mode="arrest" />
		</nc:CaseFiling>
	</xsl:template>
	<xsl:template match="nc30:DateTime" mode="referral">
		<nc:DocumentReceivedDate>
			<nc:DateTime>
				<xsl:value-of select="normalize-space(.)" />
			</nc:DateTime>
		</nc:DocumentReceivedDate>
	</xsl:template>
	<xsl:template match="nc30:OrganizationName" mode="arrest">
		<nc:DocumentSubmitter>
			<nc:EntityOrganization>
				<nc:OrganizationName>
					<xsl:value-of select="normalize-space(.)" />
				</nc:OrganizationName>
			</nc:EntityOrganization>
		</nc:DocumentSubmitter>
	</xsl:template>
	<xsl:template match="j51:CaseAugmentation">
		<j:CaseAugmentation>
			<xsl:apply-templates select="j51:CaseCharge" />
			<xsl:apply-templates select="../pd-ext:CaseAdditionalCharge" />
			<xsl:apply-templates select="j51:CaseCourt/pd-ext:CourtCaseNumber" />
			<xsl:apply-templates select="j51:CaseProsecutionAttorney" />
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="j51:CaseCharge | pd-ext:CaseAdditionalCharge">
		<j:CaseCharge>
			<xsl:attribute name="s:id"><xsl:value-of select="./@structures:id" /></xsl:attribute>
			<xsl:apply-templates select="j51:ChargeDisposition" />
			<xsl:if test="name() = 'j:CaseCharge'">
				<j:ChargeQualifierText>Lead</j:ChargeQualifierText>
			</xsl:if>
			<xsl:if test="name() = 'pd-ext:CaseAdditionalCharge'">
				<j:ChargeQualifierText>Additional</j:ChargeQualifierText>
			</xsl:if>
			<xsl:apply-templates select="pd-hi-codes:ChargeSeverityCode" />
			<xsl:apply-templates select="j51:ChargeStatute" />
			<xsl:apply-templates
				select="/pd-doc:ProsecutionDecisionReport/j51:Arrest/j51:ArrestAgency/pd-ext:AgencyCaseIdentification/nc30:IdentificationID"
				mode="arrestAgency" />
		</j:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:CaseProsecutionAttorney">
		<j:CaseProsecutionAttorney>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="generate-id(./nc30:RoleOfPerson/nc30:PersonName/nc30:PersonFullName)" /></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:CaseProsecutionAttorney>
	</xsl:template>
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<nc:PersonFullName>
				<xsl:value-of select="normalize-space(.)" />
			</nc:PersonFullName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="j51:CaseOfficialStartDate">
		<j:CaseOfficialStartDate>
			<xsl:apply-templates select="nc30:DateTime" />
		</j:CaseOfficialStartDate>
	</xsl:template>
	<xsl:template match="j51:ChargeDisposition">
		<j:ChargeDisposition>
			<xsl:apply-templates select="nc30:DispositionDate" />
			<xsl:apply-templates select="nc30:DispositionText" mode="charge" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="nc30:DispositionDate">
		<nc:DispositionDate>
			<xsl:apply-templates select="nc30:DateTime" />
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="nc30:DateTime">
		<nc:DateTime>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DateTime>
	</xsl:template>
	<xsl:template match="nc30:DispositionDescriptionText">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j51:StatuteCodeIdentification" />
			<xsl:apply-templates select="j51:StatuteDescriptionText" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:StatuteCodeIdentification">
		<j:StatuteCodeSectionIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(nc30:IdentificationID)" />
			</nc:IdentificationID>
		</j:StatuteCodeSectionIdentification>
	</xsl:template>
	<xsl:template match="j51:StatuteDescriptionText">
		<j:StatuteDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:StatuteDescriptionText>
	</xsl:template>
	<xsl:template match="j51:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc30:DispositionText" mode="charge">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="pd-hi-codes:ChargeSeverityCode">
		<j:ChargeSeverityText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="j51:ChargeCategoryDescriptionText">
		<j:StatuteDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:StatuteDescriptionText>
	</xsl:template>
	<xsl:template match="j51:PersonAugmentation">
		<xsl:apply-templates select="j51:PersonStateFingerprintIdentification/nc30:IdentificationID" mode="sid" />
	</xsl:template>
	<xsl:template match="pd-ext:CourtCaseNumber">
		<j:CaseOtherIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
			<nc:IdentificationCategoryDescriptionText>Court Case/Cause Number</nc:IdentificationCategoryDescriptionText>
		</j:CaseOtherIdentification>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="sid">
		<nc:PersonOtherIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
			<nc:IdentificationCategoryDescriptionText>SID</nc:IdentificationCategoryDescriptionText>
		</nc:PersonOtherIdentification>
	</xsl:template>
	<xsl:template match="nc30:IdentificationID" mode="arrestAgency">
		<j:ChargeTrackingIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
			<nc:IdentificationCategoryDescriptionText>Police Report Number</nc:IdentificationCategoryDescriptionText>
		</j:ChargeTrackingIdentification>
	</xsl:template>
	<xsl:template match="nc30:PersonFullName" mode="pros">
		<nc:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<nc:RoleOfPerson>
				<nc:PersonName>
					<nc:PersonFullName>
						<xsl:value-of select="normalize-space(.)" />
					</nc:PersonFullName>
				</nc:PersonName>
			</nc:RoleOfPerson>
		</nc:Person>
	</xsl:template>
	<xsl:template match="pd-doc:ProsecutionDecisionReport" mode="osa">
		<j:OffenseSubjectAssociation>
			<xsl:for-each select="j51:Offense">
				<j:OffenseReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="@structures:id" /></xsl:attribute>
				</j:OffenseReference>
			</xsl:for-each>
			<j:SubjectReference>
				<xsl:attribute name="s:ref"><xsl:value-of
					select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson/@structures:ref" /></xsl:attribute>
			</j:SubjectReference>
			<j:OffenseJuvenileIndicator>1</j:OffenseJuvenileIndicator>
		</j:OffenseSubjectAssociation>
	</xsl:template>
</xsl:stylesheet>