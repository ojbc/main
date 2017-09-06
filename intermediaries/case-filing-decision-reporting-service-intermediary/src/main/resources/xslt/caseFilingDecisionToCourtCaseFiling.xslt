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
	xmlns:core="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CoreFilingMessage-4.0" xmlns:cfd-doc="http://ojbc.org/IEPD/Exchange/CaseFilingDecisionReport/1.0"
	xmlns:cfd-ext="http://ojbc.org/IEPD/Extensions/CaseFilingDecisionReportExtension/1.0" xmlns:j51="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/" xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xop="http://www.w3.org/2004/08/xop/include" xmlns:criminal="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CriminalCase-4.0"
	xmlns:citation="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CitationCase-4.0" xmlns:ojb-crim-doc="http://ojbc.org/IEPD/Exchange/CriminalCaseDocument/1.0"
	xmlns:ojb-crim-ext="http://ojbc.org/IEPD/Extensions/CriminalCaseExtension/1.0" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0"
	xmlns:ecf="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CommonTypes-4.0" xmlns:s="http://niem.gov/niem/structures/2.0"
	exclude-result-prefixes="cfd-doc cfd-ext j51 nc30 niem-xs structures">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="cfd-doc:CaseFilingDecisionReport">
		<ojb-crim-doc:CoreFilingMessage xmlns="urn:oasis:names:tc:legalxml-courtfiling:schema:xsd:CoreFilingMessage-4.0">
			<!-- SendingMDELocationID and SendingMDEProfileCode elements required by ECF -->
			<ecf:SendingMDELocationID>
				<nc:IdentificationID />
			</ecf:SendingMDELocationID>
			<ecf:SendingMDEProfileCode />
			<ojb-crim-ext:CriminalCase>
				<xsl:apply-templates select="nc30:Case" />
				<xsl:apply-templates select="." mode="ecfAugmentation" />
				<xsl:apply-templates
					select="nc30:Case/j51:CaseAugmentation/j51:CaseCharge[@structures:id=../../../j51:OffenseChargeAssociation/j51:Charge/@structures:ref]"
					mode="charge" />
			</ojb-crim-ext:CriminalCase>
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
		</ojb-crim-doc:CoreFilingMessage>
	</xsl:template>
	<xsl:template match="nc30:Case">
		<xsl:apply-templates select="nc30:ActivityDescriptionText" />
		<xsl:apply-templates select="nc30:CaseTitleText" />
		<xsl:apply-templates select="nc30:CaseCategoryText" />
		<xsl:apply-templates select="nc30:CaseFiling" />
		<xsl:apply-templates select="j51:CaseAugmentation/j51:CaseCourt" />
		<xsl:apply-templates select="j51:CaseAugmentation/j51:CaseDomesticViolenceIndicator" />
	</xsl:template>
	<xsl:template match="j51:CaseCourt">
		<j:CaseAugmentation>
			<j:CaseCourt>
				<xsl:apply-templates select="nc30:OrganizationLocation/nc30:LocationName" mode="court" />
				<xsl:apply-templates select="j51:CourtName" />
				<xsl:apply-templates select="j51:CourtCategoryCode" />
			</j:CaseCourt>
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="cfd-doc:CaseFilingDecisionReport" mode="ecfAugmentation">
		<ojb-crim-ext:CaseAugmentation>
			<!-- xsl:apply-templates
				select="/cfd-doc:CaseFilingDecisionReport/nc30:Identity[@structures:id=../nc30:PersonIdentityAssociation[nc30:Person/@structures:ref=/cfd-doc:CaseFilingDecisionReport/nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson/@structures:ref]/nc30:Identity/@structures:ref]"
				mode="alias" / -->
			<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" />
		</ojb-crim-ext:CaseAugmentation>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:variable name="CPid" select="nc30:EntityPerson/@structures:ref" />
			<JIMMY>
				<xsl:value-of select="$CPid" />
			</JIMMY>
			<xsl:apply-templates select="../../../nc30:Person[@structures:id=$CPid]" mode="defendant" />
			<xsl:apply-templates
				select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]" />
			<xsl:apply-templates
				select="../../../nc30:Location[@structures:id=../nc30:PersonResidenceAssociation[nc30:Person/@structures:ref=$CPid]/nc30:Location/@structures:ref]/nc30:Address" />
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="defendant">
		<ecf:EntityPerson>
			<xsl:copy-of select="node()" copy-namespaces="no" />
		</ecf:EntityPerson>
		<xsl:apply-templates select="../nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/cfd-ext:PartyRoleText" />
	</xsl:template>
	<xsl:template match="nc30:ContactInformation">
		<xsl:copy-of select="." copy-namespaces="no" />
	</xsl:template>
	<xsl:template match="nc30:Address">
		<nc:ContactMailingAddress>
			<nc:StructuredAddress>
				<xsl:copy-of select="node()" copy-namespaces="no" />
			</nc:StructuredAddress>
		</nc:ContactMailingAddress>
		<xsl:apply-templates select="../nc30:LocationCategoryText" />
		<xsl:copy-of select="../cfd-ext:DefaultLocationIndicator" copy-namespaces="no" />
		<xsl:copy-of select="../cfd-ext:PreferredLocationIndicator" copy-namespaces="no" />
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
	<xsl:template match="nc30:ActivityDescriptionText">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:ActivityDescriptionText>
	</xsl:template>
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
	<xsl:template match="nc30:CaseFiling">
		<nc:CaseFiling>
			<nc:DocumentFiledDate>
				<nc:DateTime>
					<xsl:value-of select="normalize-space(.)" />
				</nc:DateTime>
			</nc:DocumentFiledDate>
		</nc:CaseFiling>
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
	<xsl:template match="j51:CourtCategoryCode">
		<j:CourtCategoryCode>
			<xsl:value-of select="normalize-space(.)" />
		</j:CourtCategoryCode>
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
	<xsl:template match="j51:CaseDomesticViolenceIndicator">
		<j:CaseDomesticViolenceIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</j:CaseDomesticViolenceIndicator>
	</xsl:template>
	<xsl:template match="nc30:Identity" mode="alias">
		<ecf:Alias>
			<ecf:AliasAlternateName>Alias</ecf:AliasAlternateName>
			<ecf:AliasAlternateNameTypeCode>ecf:Alias</ecf:AliasAlternateNameTypeCode>
			<nc:EntityReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			</nc:EntityReference>
		</ecf:Alias>
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