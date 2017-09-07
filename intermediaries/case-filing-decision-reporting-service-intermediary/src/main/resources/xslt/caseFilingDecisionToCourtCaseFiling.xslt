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
				<xsl:apply-templates select="j51:Arrest" />
				<xsl:apply-templates
					select="nc30:Case/j51:CaseAugmentation/j51:CaseCharge[@structures:id=../../../j51:OffenseChargeAssociation/j51:Charge/@structures:ref]"
					mode="charge" />
				<xsl:apply-templates select="." mode="citation" />
			</ojb-crim-ext:CriminalCase>
			<xsl:apply-templates select="nc30:PrimaryDocument" />
			<xsl:apply-templates select="nc30:SecondaryDocument" />
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
			<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="defendant" />
			<xsl:apply-templates select="nc30:Person[@structures:id=../j51:Arrest/j51:ArrestOfficial/@structures:ref]" mode="officer" />
			<xsl:apply-templates
				select="nc30:Person[@structures:id=../nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty/nc30:EntityPerson/@structures:ref]" mode="initiating" />
			<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="party" />
			<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty" mode="party" />
		</ojb-crim-ext:CaseAugmentation>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty" mode="defendant">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(./nc30:EntityPerson)" /></xsl:attribute>
			<xsl:variable name="CPid" select="nc30:EntityPerson/@structures:ref" />
			<xsl:apply-templates select="../../../nc30:Person[@structures:id=$CPid]" mode="defendant" />
			<xsl:apply-templates
				select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]" />
			<xsl:apply-templates
				select="../../../nc30:Location[@structures:id=../nc30:PersonResidenceAssociation[nc30:Person/@structures:ref=$CPid]/nc30:Location/@structures:ref]/nc30:Address" />
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty" mode="party">
		<ojb-crim-ext:CaseDefendantParty>
			<nc:EntityPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(./nc30:EntityPerson)" /></xsl:attribute>
			</nc:EntityPersonReference>
			<xsl:apply-templates select="cfd-ext:PartyIdentification" />
			<xsl:apply-templates select="cfd-ext:PartyRoleText" />
			<xsl:apply-templates select="cfd-ext:PartyCategoryText" />
			<xsl:apply-templates select="cfd-ext:PartyConfidentialIndicator" />
		</ojb-crim-ext:CaseDefendantParty>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="defendant">
		<ojb-crim-ext:EntityPerson>
			<xsl:copy-of select="node()" copy-namespaces="no" />
		</ojb-crim-ext:EntityPerson>
		<ecf:CaseParticipantRoleCode>Defendant</ecf:CaseParticipantRoleCode>
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
	<xsl:template match="nc30:Person" mode="officer">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(../j51:Arrest/j51:ArrestOfficial)" /></xsl:attribute>
			<ecf:EntityPerson>
				<xsl:copy-of select="node()" copy-namespaces="no" />
			</ecf:EntityPerson>
			<ecf:CaseParticipantRoleCode>Arresting Officer</ecf:CaseParticipantRoleCode>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="initiating">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<ecf:EntityPerson>
				<xsl:copy-of select="node()" copy-namespaces="no" />
			</ecf:EntityPerson>
			<ecf:CaseParticipantRoleCode>Initiating Party</ecf:CaseParticipantRoleCode>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="j51:CaseInitiatingParty" mode="party">
		<ojb-crim-ext:CaseInitiatingParty>
			<nc:EntityPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(..)" /></xsl:attribute>
			</nc:EntityPersonReference>
			<xsl:apply-templates select="cfd-ext:PartyRoleText" />
			<xsl:apply-templates select="cfd-ext:PartyCategoryText" />
		</ojb-crim-ext:CaseInitiatingParty>
	</xsl:template>
	<xsl:template match="cfd-ext:PartyIdentification">
		<ojb-crim-ext:PartyIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(nc30:IdentificationID)" />
			</nc:IdentificationID>
		</ojb-crim-ext:PartyIdentification>
	</xsl:template>
	<xsl:template match="cfd-ext:PartyRoleText">
		<ojb-crim-ext:PartyRoleText>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:PartyRoleText>
	</xsl:template>
		<xsl:template match="cfd-ext:PartyCategoryText">
		<ojb-crim-ext:PartyCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:PartyCategoryText>
	</xsl:template>
	<xsl:template match="cfd-ext:PartyConfidentialIndicator">
		<ojb-crim-ext:PartyConfidentialIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:PartyConfidentialIndicator>
	</xsl:template>
	<xsl:template match="j51:Arrest">
		<ojb-crim-ext:CaseArrest>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:copy-of select="nc30:ActivityDate" copy-namespaces="no" />
			<xsl:copy-of select="j51:ArrestAgency" copy-namespaces="no" />
			<xsl:copy-of select="j51:ArrestAgencyRecordIdentification" copy-namespaces="no" />
			<xsl:apply-templates select="j51:ArrestOfficial" />
			<xsl:apply-templates select="j51:ArrestSubject" />
			<xsl:apply-templates select="../j51:Booking" />
		</ojb-crim-ext:CaseArrest>
	</xsl:template>
	<xsl:template match="j51:CaseCharge" mode="charge">
		<xsl:variable name="chargeid" select="./@structures:id" />
		<ojb-crim-ext:CaseCharge>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="j51:ChargeSeverityLevel/j51:SeverityLevelDescriptionText" />
			<xsl:apply-templates select="j51:ChargeStatute" />
			<xsl:apply-templates
				select="../../../j51:Offense[@structures:id=../j51:OffenseChargeAssociation[j51:Charge/@structures:ref=$chargeid]/j51:Offense/@structures:ref]" />
		</ojb-crim-ext:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j51:StatuteCodeIdentification" />
			<xsl:apply-templates select="j51:StatuteDescriptionText" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:Offense">
		<criminal:ChargeOffense>
			<xsl:if test="nc30:ActivityDate!=''">
				<xsl:copy-of select="." copy-namespaces="no" />
			</xsl:if>
			<xsl:if test="nc30:ActivityDateRange!=''">
				<xsl:copy-of select="." copy-namespaces="no" />
			</xsl:if>
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
	<xsl:template match="j51:ArrestOfficial">
	<xsl:variable name="AOid" select="./@structures:ref" />
	<JIMMY>
	<xsl:value-of select="$AOid" />
	
	</JIMMY>
	
		<j:ArrestOfficial>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestOfficial>
	</xsl:template>
	<xsl:template match="j51:ArrestSubject">
		<j:ArrestSubject>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestSubject>
	</xsl:template>
	<xsl:template match="j51:Booking">
		<j:Booking>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:copy-of select="node()" copy-namespaces="no" />
		</j:Booking>
	</xsl:template>
	<xsl:template match="cfd-doc:CaseFilingDecisionReport" mode="citation">
		<ojb-crim-ext:CitationCaseAugmentation>
			<xsl:apply-templates select="nc30:Vehicle" />
		</ojb-crim-ext:CitationCaseAugmentation>
	</xsl:template>
	<xsl:template match="nc30:Vehicle">
		<ojb-crim-ext:Vehicle>
			<xsl:copy-of select="node()" copy-namespaces="no" />
		</ojb-crim-ext:Vehicle>
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
	<xsl:template match="nc30:PrimaryDocument">
		<ojb-crim-ext:FilingLeadDocument>
			<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:copy-of select="nc30:DocumentCategoryName" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentCategoryText" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentDescriptionText" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentRelatedResourceText" copy-namespaces="no" />
			<nc:DocumentLanguageCode>eng</nc:DocumentLanguageCode>
			<ecf:DocumentMetadata>
				<j:RegisterActionDescriptionText />
				<ecf:FilingAttorneyID />
				<ecf:FilingPartyID />
			</ecf:DocumentMetadata>
			<ecf:DocumentRendition>
				<ecf:DocumentRenditionMetadata>
					<nc:DocumentBinary>
						<nc:BinaryBase64Object>
							<xsl:value-of select="nc30:DocumentBinary/cfd-ext:Base64BinaryObject" />
						</nc:BinaryBase64Object>
					</nc:DocumentBinary>
					<ecf:DocumentAttachment>
						<nc:BinaryDescriptionText />
						<nc:BinaryLocationURI />
					</ecf:DocumentAttachment>
				</ecf:DocumentRenditionMetadata>
			</ecf:DocumentRendition>
			<xsl:apply-templates select="cfd-ext:PreviouslyFiledDocketText" />
			<xsl:apply-templates select="cfd-ext:DocumentPublicPartyViewingRestrictedIndicator" />
			<xsl:apply-templates select="ojb-crim-ext:DocumentPublicViewingRestrictedIndicator" />
		</ojb-crim-ext:FilingLeadDocument>
	</xsl:template>
	<xsl:template match="nc30:SecondaryDocument">
		<ojb-crim-ext:FilingConnectedDocument>
			<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:copy-of select="nc30:DocumentCategoryName" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentCategoryText" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentDescriptionText" copy-namespaces="no" />
			<xsl:copy-of select="nc30:DocumentRelatedResourceText" copy-namespaces="no" />
			<nc:DocumentLanguageCode>eng</nc:DocumentLanguageCode>
			<ecf:DocumentMetadata>
				<j:RegisterActionDescriptionText />
				<ecf:FilingAttorneyID />
				<ecf:FilingPartyID />
			</ecf:DocumentMetadata>
			<ecf:DocumentRendition>
				<ecf:DocumentRenditionMetadata>
					<nc:DocumentBinary>
						<nc:BinaryBase64Object>
							<xsl:value-of select="nc30:DocumentBinary/cfd-ext:Base64BinaryObject" />
						</nc:BinaryBase64Object>
					</nc:DocumentBinary>
					<ecf:DocumentAttachment>
						<nc:BinaryDescriptionText />
						<nc:BinaryLocationURI />
					</ecf:DocumentAttachment>
				</ecf:DocumentRenditionMetadata>
			</ecf:DocumentRendition>
			<xsl:apply-templates select="cfd-ext:PreviouslyFiledDocketText" />
			<xsl:apply-templates select="cfd-ext:DocumentPublicPartyViewingRestrictedIndicator" />
			<xsl:apply-templates select="ojb-crim-ext:DocumentPublicViewingRestrictedIndicator" />
		</ojb-crim-ext:FilingConnectedDocument>
	</xsl:template>
	<xsl:template match="cfd-ext:PreviouslyFiledDocketText">
		<ojb-crim-ext:PreviouslyFiledDocketText>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:PreviouslyFiledDocketText>
	</xsl:template>
	<xsl:template match="cfd-ext:DocumentPublicPartyViewingRestrictedIndicator">
		<ojb-crim-ext:DocumentPublicPartyViewingRestrictedIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:DocumentPublicPartyViewingRestrictedIndicator>
	</xsl:template>
	<xsl:template match="cfd-ext:DocumentPublicViewingRestrictedIndicator">
		<ojb-crim-ext:DocumentPublicViewingRestrictedIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:DocumentPublicViewingRestrictedIndicator>
	</xsl:template>
</xsl:stylesheet>