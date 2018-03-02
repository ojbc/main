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
			<xsl:apply-templates select="nc30:Case/cfd-ext:CaseConfidentialIndicator" />
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
	</xsl:template>
	<xsl:template match="j51:CaseCourt">
		<j:CaseAugmentation>
			<j:CaseCourt>
				<xsl:apply-templates select="nc30:OrganizationLocation/nc30:LocationName" mode="court" />
				<xsl:apply-templates select="j51:CourtName" />
				<xsl:apply-templates select="j51:CourtCategoryCode" />
			</j:CaseCourt>
			<xsl:apply-templates select="../j51:CaseDomesticViolenceIndicator" />
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="cfd-doc:CaseFilingDecisionReport" mode="ecfAugmentation">
		<ojb-crim-ext:CaseAugmentation>
			<xsl:apply-templates
				select="/cfd-doc:CaseFilingDecisionReport/nc30:Identity[@structures:id=../nc30:PersonIdentityAssociation[nc30:Person/@structures:ref=/cfd-doc:CaseFilingDecisionReport/nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson/@structures:ref]/nc30:Identity/@structures:ref]"
				mode="alias" />
			<xsl:if test="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson">
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="defendantPerson" />
			</xsl:if>
			<xsl:if test="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityOrganization">
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="defendantOrganization" />
			</xsl:if>
			<xsl:apply-templates select="nc30:Person[@structures:id=../j51:Arrest/j51:ArrestOfficial/@structures:ref]" mode="officer" />
			<xsl:apply-templates
				select="nc30:Person[@structures:id=../nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty/nc30:EntityPerson/@structures:ref]" mode="initiating" />
			<xsl:if test="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson">
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="partyPerson" />
			</xsl:if>
			<xsl:if test="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityOrganization">
				<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty" mode="partyOrganization" />
			</xsl:if>
			<xsl:apply-templates select="nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty" mode="party" />
			<xsl:apply-templates select="nc30:Case/cfd-ext:CaseInitiationMethodText" />
		</ojb-crim-ext:CaseAugmentation>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty" mode="defendantPerson">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(./nc30:EntityPerson)" /></xsl:attribute>
			<xsl:variable name="CPid" select="nc30:EntityPerson/@structures:ref" />
			<xsl:apply-templates select="../../../nc30:Person[@structures:id=$CPid]" mode="defendant" />
			<xsl:apply-templates
				select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]" />
			<ojb-crim-ext:ContactInformation>
				<xsl:apply-templates
					select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]/nc30:ContactEmailID" />
				<xsl:apply-templates
					select="../../../nc30:Location[@structures:id=../nc30:PersonResidenceAssociation[nc30:Person/@structures:ref=$CPid]/nc30:Location/@structures:ref]/nc30:Address" />
			</ojb-crim-ext:ContactInformation>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty" mode="defendantOrganization">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(./nc30:EntityOrganization)" /></xsl:attribute>
			<xsl:variable name="CPid" select="nc30:EntityOrganization/@structures:ref" />
			<xsl:apply-templates select="../../../nc30:Organization[@structures:id=$CPid]" mode="defendantOrganization" />
			<xsl:apply-templates
				select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]" />
			<ojb-crim-ext:ContactInformation>
				<xsl:apply-templates
					select="../../../nc30:ContactInformation[@structures:id=../nc30:ContactInformationAssociation[nc30:ContactEntity/@structures:ref=$CPid]/nc30:ContactInformation/@structures:ref]/nc30:ContactMailingAddress" />
			</ojb-crim-ext:ContactInformation>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="j51:CaseDefendantParty" mode="partyPerson">
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
	<xsl:template match="j51:CaseDefendantParty" mode="partyOrganization">
		<ojb-crim-ext:CaseDefendantParty>
			<nc:EntityOrganizationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(./nc30:EntityOrganization)" /></xsl:attribute>
			</nc:EntityOrganizationReference>
			<xsl:apply-templates select="cfd-ext:PartyIdentification" />
			<xsl:apply-templates select="cfd-ext:PartyRoleText" />
			<xsl:apply-templates select="cfd-ext:PartyCategoryText" />
			<xsl:apply-templates select="cfd-ext:PartyConfidentialIndicator" />
		</ojb-crim-ext:CaseDefendantParty>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="defendant">
		<ojb-crim-ext:EntityPerson>
			<xsl:apply-templates select="nc30:PersonBirthDate" />
			<xsl:apply-templates select="nc30:PersonEyeColorText" />
			<xsl:apply-templates select="nc30:PersonHairColorText" />
			<xsl:apply-templates select="nc30:PersonHeightMeasure" />
			<xsl:apply-templates select="nc30:PersonName" />
			<xsl:apply-templates select="j51:PersonSexCode" />
			<xsl:apply-templates select="nc30:PersonSSNIdentification" />
			<xsl:apply-templates select="nc30:PersonWeightMeasure" />
			<xsl:apply-templates select="j51:PersonAugmentation" />
		</ojb-crim-ext:EntityPerson>
		<ecf:CaseParticipantRoleCode>Defendant</ecf:CaseParticipantRoleCode>
	</xsl:template>
	<xsl:template match="nc30:Organization" mode="defendantOrganization">
		<ojb-crim-ext:EntityOrganization>
			<nc:OrganizationName>
				<xsl:value-of select="normalize-space(nc30:OrganizationName)" />
			</nc:OrganizationName>
		</ojb-crim-ext:EntityOrganization>
		<ecf:CaseParticipantRoleCode>Defendant</ecf:CaseParticipantRoleCode>
	</xsl:template>
	<xsl:template match="j51:PersonAugmentation">
		<ojb-crim-ext:PersonAugmentation>
			<xsl:apply-templates select="j51:PersonStateFingerprintIdentification" />
			<xsl:apply-templates select="j51:DriverLicense" />
			<ojb-crim-ext:PersonConfidentialIndicator>
				<xsl:value-of select="normalize-space(../cfd-ext:PersonConfidentialIndicator)" />
			</ojb-crim-ext:PersonConfidentialIndicator>
			<ojb-crim-ext:PersonJuvenileIndicator>
				<xsl:value-of select="normalize-space(../cfd-ext:PersonJuvenileIndicator)" />
			</ojb-crim-ext:PersonJuvenileIndicator>
			<ojb-crim-ext:IdentifiedPersonTrackingIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(../cfd-ext:IdentifiedPersonTrackingIdentification)" />
				</nc:IdentificationID>
			</ojb-crim-ext:IdentifiedPersonTrackingIdentification>
		</ojb-crim-ext:PersonAugmentation>
	</xsl:template>
	<xsl:template match="j51:DriverLicense">
		<ojb-crim-ext:PersonDriverLicense>
			<nc:DriverLicenseIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(j51:DriverLicenseIdentification/nc30:IdentificationID)" />
				</nc:IdentificationID>
				<j:DrivingJurisdictionAuthorityNCICLSTACode>
					<xsl:value-of select="normalize-space(j51:DriverLicenseIdentification/nc30:IdentificationJurisdiction/nc30:JurisdictionText)" />
				</j:DrivingJurisdictionAuthorityNCICLSTACode>
			</nc:DriverLicenseIdentification>
			<ojb-crim-ext:DriverLicenseCDLIndicator>
				<xsl:value-of select="normalize-space(cfd-ext:DriverLicenseCDLIndicator)" />
			</ojb-crim-ext:DriverLicenseCDLIndicator>
		</ojb-crim-ext:PersonDriverLicense>
	</xsl:template>
	<xsl:template match="j51:PersonStateFingerprintIdentification">
		<j:PersonStateFingerprintIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(nc30:IdentificationID)" />
			</nc:IdentificationID>
		</j:PersonStateFingerprintIdentification>
	</xsl:template>
	<xsl:template match="nc30:ContactInformation">
		<xsl:apply-templates select="nc30:ContactTelephoneNumber" />
	</xsl:template>
	<xsl:template match="nc30:ContactTelephoneNumber">
		<nc:ContactInformation>
			<nc:ContactTelephoneNumber>
				<nc:FullTelephoneNumber>
					<nc:TelephoneNumberFullID>
						<xsl:value-of select="normalize-space(nc30:FullTelephoneNumber/nc30:TelephoneNumberFullID)" />
					</nc:TelephoneNumberFullID>
				</nc:FullTelephoneNumber>
			</nc:ContactTelephoneNumber>
			<xsl:apply-templates select="nc30:TelephoneNumberCategoryText" />
		</nc:ContactInformation>
	</xsl:template>
	<xsl:template match="nc30:TelephoneNumberCategoryText">
		<nc:ContactInformationDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:ContactInformationDescriptionText>
	</xsl:template>
	<xsl:template match="nc30:Address | nc30:ContactMailingAddress">
		<ojb-crim-ext:ContactMailingAddress>
			<nc:StructuredAddress>
				<nc:AddressSecondaryUnitText>
					<xsl:value-of select="normalize-space(nc30:AddressSecondaryUnitText)" />
				</nc:AddressSecondaryUnitText>
				<nc:LocationStreet>
					<nc:StreetFullText>
						<xsl:value-of select="normalize-space(nc30:LocationStreet/nc30:StreetFullText)" />
					</nc:StreetFullText>
				</nc:LocationStreet>
				<nc:LocationCityName>
					<xsl:value-of select="normalize-space(nc30:LocationCityName)" />
				</nc:LocationCityName>
				<nc:LocationStateUSPostalServiceCode>
					<xsl:value-of select="normalize-space(nc30:LocationStateUSPostalServiceCode)" />
				</nc:LocationStateUSPostalServiceCode>
				<nc:LocationCountryName>
					<xsl:value-of select="normalize-space(nc30:LocationCountryName)" />
				</nc:LocationCountryName>
				<nc:LocationPostalCode>
					<xsl:value-of select="normalize-space(nc30:LocationPostalCode)" />
				</nc:LocationPostalCode>
			</nc:StructuredAddress>
			<xsl:apply-templates select="../nc30:LocationCategoryText" />
			<xsl:apply-templates select="../cfd-ext:DefaultLocationIndicator" />
			<xsl:apply-templates select="../cfd-ext:PreferredLocationIndicator" />
		</ojb-crim-ext:ContactMailingAddress>
	</xsl:template>
	<xsl:template match="nc30:ContactEmailID">
		<nc:ContactEmailID>
			<xsl:value-of select="normalize-space(.)" />
		</nc:ContactEmailID>
	</xsl:template>
	<xsl:template match="nc30:LocationCategoryText">
		<ojb-crim-ext:ContactMailingAddressCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:ContactMailingAddressCategoryText>
	</xsl:template>
	<xsl:template match="cfd-ext:DefaultLocationIndicator">
		<ojb-crim-ext:DefaultLocationIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:DefaultLocationIndicator>
	</xsl:template>
	<xsl:template match="cfd-ext:PreferredLocationIndicator">
		<ojb-crim-ext:PreferredLocationIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:PreferredLocationIndicator>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="officer">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(../j51:Arrest/j51:ArrestOfficial)" /></xsl:attribute>
			<ecf:EntityPerson>
				<xsl:apply-templates select="nc30:PersonName" />
			</ecf:EntityPerson>
			<ecf:CaseParticipantRoleCode>Arresting Officer</ecf:CaseParticipantRoleCode>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="nc30:Person" mode="initiating">
		<ojb-crim-ext:CaseParticipant>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(../nc30:Case/j51:CaseAugmentation/j51:CaseInitiatingParty/nc30:EntityPerson)" /></xsl:attribute>
			<ecf:EntityPerson>
				<xsl:apply-templates select="nc30:PersonName" />
			</ecf:EntityPerson>
			<ecf:CaseParticipantRoleCode>Initiating Party</ecf:CaseParticipantRoleCode>
		</ojb-crim-ext:CaseParticipant>
	</xsl:template>
	<xsl:template match="j51:CaseInitiatingParty" mode="party">
		<ojb-crim-ext:CaseInitiatingParty>
			<nc:EntityPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(nc30:EntityPerson)" /></xsl:attribute>
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
	<xsl:template match="cfd-ext:CaseInitiationMethodText">
		<ojb-crim-ext:CaseInitiationMethodText>
			<xsl:value-of select="normalize-space(.)" />
		</ojb-crim-ext:CaseInitiationMethodText>
	</xsl:template>
	<xsl:template match="j51:Arrest">
		<ojb-crim-ext:CaseArrest>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="nc30:ActivityDate" />
			<xsl:apply-templates select="j51:ArrestAgency" />
			<xsl:apply-templates select="j51:ArrestAgencyRecordIdentification" />
			<xsl:apply-templates select="j51:ArrestOfficial" />
			<xsl:apply-templates select="j51:ArrestSubject" />
			<xsl:apply-templates select="../j51:Booking" />
		</ojb-crim-ext:CaseArrest>
	</xsl:template>
	<xsl:template match="j51:ArrestAgency">
		<j:ArrestAgency>
			<nc:OrganizationName>
				<xsl:value-of select="normalize-space(nc30:OrganizationName)" />
			</nc:OrganizationName>
		</j:ArrestAgency>
	</xsl:template>
	<xsl:template match="j51:ArrestAgencyRecordIdentification">
		<j:ArrestAgencyRecordIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(nc30:IdentificationID)" />
			</nc:IdentificationID>
		</j:ArrestAgencyRecordIdentification>
	</xsl:template>
	<xsl:template match="j51:CaseCharge" mode="charge">
		<xsl:variable name="chargeid" select="./@structures:id" />
		<ojb-crim-ext:CaseCharge>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<xsl:apply-templates select="j51:ChargeCountQuantity" />
			<xsl:apply-templates select="j51:ChargeDescriptionText" />
			<xsl:apply-templates select="j51:ChargeFilingDate" />
			<xsl:apply-templates select="j51:ChargeQualifierText" />
			<xsl:apply-templates select="j51:ChargeSeverityLevel" />
			<xsl:apply-templates select="j51:ChargeStatute" />
			<xsl:apply-templates
				select="../../../j51:Offense[@structures:id=../j51:OffenseChargeAssociation[j51:Charge/@structures:ref=$chargeid]/j51:Offense/@structures:ref]" />
			<!-- ChargeAmendedIndicator Required by ECF but not in the Hawaii Prosecution Case Filing Decision -->
			<criminal:ChargeAmendedIndicator>false</criminal:ChargeAmendedIndicator>
		</ojb-crim-ext:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:ChargeCountQuantity">
		<j:ChargeCountQuantity>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeCountQuantity>
	</xsl:template>
	<xsl:template match="j51:ChargeDescriptionText">
		<j:ChargeDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="j51:ChargeFilingDate">
		<j:ChargeFilingDate>
			<xsl:apply-templates select="nc30:Date" />
		</j:ChargeFilingDate>
	</xsl:template>
	<xsl:template match="j51:ChargeQualifierText">
		<j:ChargeQualifierText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeQualifierText>
	</xsl:template>
	<xsl:template match="j51:ChargeSeverityLevel">
		<j:ChargeSeverityLevel>
			<j:SeverityLevelDescriptionText>
				<xsl:value-of select="normalize-space(j51:SeverityLevelDescriptionText)" />
			</j:SeverityLevelDescriptionText>
		</j:ChargeSeverityLevel>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(j51:StatuteCodeIdentification/nc30:IdentificationID)" />
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:Offense">
		<criminal:ChargeOffense>
			<xsl:if test="nc30:ActivityDate!=''">
				<xsl:apply-templates select="nc30:ActivityDate" />
			</xsl:if>
			<xsl:if test="nc30:ActivityDateRange!=''">
				<xsl:apply-templates select="nc30:ActivityDateRange" />
			</xsl:if>
		</criminal:ChargeOffense>
	</xsl:template>
	<!-- MATCH -->
	<xsl:template match="nc30:PersonBirthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="normalize-space(.)" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc30:PersonEyeColorText">
		<nc:PersonEyeColorCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonEyeColorCode>
	</xsl:template>
	<xsl:template match="nc30:PersonHairColorText">
		<nc:PersonHairColorCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonHairColorCode>
	</xsl:template>
	<xsl:template match="nc30:PersonHeightMeasure">
		<nc:PersonHeightMeasure>
			<nc:MeasureText>
				<xsl:value-of select="normalize-space(nc30:MeasureValueText)" />
			</nc:MeasureText>
			<nc:MeasureUnitText>
				<xsl:value-of select="normalize-space(nc30:LengthUnitCode)" />
			</nc:MeasureUnitText>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="nc30:PersonName">
		<nc:PersonName>
			<xsl:apply-templates select="nc30:PersonNamePrefix" />
			<xsl:apply-templates select="nc30:PersonGivenName" />
			<xsl:apply-templates select="nc30:PersonMiddleName" />
			<xsl:apply-templates select="nc30:PersonSurName" />
			<xsl:apply-templates select="nc30:PersonNameSuffixText" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="nc30:PersonNamePrefix">
		<nc:PersonNamePrefixText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonNamePrefixText>
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
	<xsl:template match="nc30:PersonNameSuffixText">
		<nc:PersonNameSuffixText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonNameSuffixText>
	</xsl:template>
	<xsl:template match="j51:PersonSexCode">
		<nc:PersonSexCode>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonSexCode>
	</xsl:template>
	<xsl:template match="nc30:PersonSSNIdentification">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(nc30:IdentificationID)" />
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="nc30:PersonWeightMeasure">
		<nc:PersonWeightMeasure>
			<nc:MeasureText>
				<xsl:value-of select="normalize-space(nc30:MeasureValueText)" />
			</nc:MeasureText>
			<nc:MeasureUnitText>
				<xsl:value-of select="normalize-space(nc30:WeightUnitCode)" />
			</nc:MeasureUnitText>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="nc30:ActivityDate">
		<nc:ActivityDate>
			<xsl:apply-templates select="nc30:DateTime" />
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="nc30:ActivityDateRange">
		<nc:ActivityDateRange>
			<nc:StartDate>
				<xsl:apply-templates select="nc30:StartDate/nc30:DateTime" />
			</nc:StartDate>
			<nc:EndDate>
				<xsl:apply-templates select="nc30:EndDate/nc30:DateTime" />
			</nc:EndDate>
		</nc:ActivityDateRange>
	</xsl:template>
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
			<xsl:apply-templates select="nc30:IdentityPersonRepresentation/nc30:PersonName" mode="alias" />
			<ecf:AliasAlternateNameTypeCode>ecf:Alias</ecf:AliasAlternateNameTypeCode>
			<nc:EntityReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson)" /></xsl:attribute>
			</nc:EntityReference>
		</ecf:Alias>
	</xsl:template>
	<xsl:template match="nc30:PersonName" mode="alias">
		<ecf:AliasAlternateName>
			<xsl:value-of select="nc30:PersonGivenName,nc30:PersonMiddleName,nc30:PersonSurName" />
		</ecf:AliasAlternateName>
	</xsl:template>
	<xsl:template match="j51:ArrestOfficial">
		<xsl:variable name="AOid" select="./@structures:ref" />
		<j:ArrestOfficial>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestOfficial>
	</xsl:template>
	<xsl:template match="j51:ArrestSubject">
		<j:ArrestSubject>
			<nc:RoleOfPersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../nc30:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc30:EntityPerson)" /></xsl:attribute>
			</nc:RoleOfPersonReference>
		</j:ArrestSubject>
	</xsl:template>
	<xsl:template match="j51:Booking">
		<j:Booking>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id(.)" /></xsl:attribute>
			<nc:ActivityIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(nc30:ActivityIdentification/nc30:IdentificationID)" />
				</nc:IdentificationID>
			</nc:ActivityIdentification>
			<j:BookingAgency>
				<nc:OrganizationName>
					<xsl:value-of select="normalize-space(j51:BookingAgency/nc30:OrganizationName)" />
				</nc:OrganizationName>
			</j:BookingAgency>
		</j:Booking>
	</xsl:template>
	<xsl:template match="cfd-doc:CaseFilingDecisionReport" mode="citation">
		<xsl:if test="../nc30:Vehicle">
			<ojb-crim-ext:CitationCaseAugmentation>
				<xsl:apply-templates select="nc30:Vehicle" />
			</ojb-crim-ext:CitationCaseAugmentation>
		</xsl:if>
	</xsl:template>
	<xsl:template match="nc30:Vehicle">
		<ojb-crim-ext:Vehicle>
			<nc:VehicleCMVIndicator>
				<xsl:value-of select="normalize-space(nc30:VehicleCMVIndicator)" />
			</nc:VehicleCMVIndicator>
			<ojb-crim-ext:VehicleHazmatIndicator>
				<xsl:value-of select="normalize-space(cfd-ext:VehicleHazmatIndicator)" />
			</ojb-crim-ext:VehicleHazmatIndicator>
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
	<xsl:template match="cfd-ext:CaseConfidentialIndicator">
		<core:FilingConfidentialityIndicator>
			<xsl:value-of select="normalize-space(.)" />
		</core:FilingConfidentialityIndicator>
	</xsl:template>
	<xsl:template match="nc30:PrimaryDocument">
		<ojb-crim-ext:FilingLeadDocument>
			<xsl:apply-templates select="nc30:DocumentCategoryName" />
			<xsl:apply-templates select="nc30:DocumentCategoryText" />
			<xsl:apply-templates select="nc30:DocumentDescriptionText" />
			<xsl:apply-templates select="nc30:DocumentRelatedResourceText" />
			<!-- ECF Required Element -->
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
			<xsl:apply-templates select="cfd-ext:DocumentPublicViewingRestrictedIndicator" />
		</ojb-crim-ext:FilingLeadDocument>
	</xsl:template>
	<xsl:template match="nc30:SecondaryDocument">
		<ojb-crim-ext:FilingConnectedDocument>
			<xsl:apply-templates select="nc30:DocumentCategoryName" />
			<xsl:apply-templates select="nc30:DocumentCategoryText" />
			<xsl:apply-templates select="nc30:DocumentDescriptionText" />
			<xsl:apply-templates select="nc30:DocumentRelatedResourceText" />
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
			<xsl:apply-templates select="cfd-ext:DocumentPublicViewingRestrictedIndicator" />
		</ojb-crim-ext:FilingConnectedDocument>
	</xsl:template>
	<xsl:template match="nc30:DocumentCategoryName">
		<nc:DocumentCategoryName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DocumentCategoryName>
	</xsl:template>
	<xsl:template match="nc30:DocumentCategoryText">
		<nc:DocumentCategoryText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DocumentCategoryText>
	</xsl:template>
	<xsl:template match="nc30:DocumentDescriptionText">
		<nc:DocumentDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DocumentDescriptionText>
	</xsl:template>
	<xsl:template match="nc30:DocumentRelatedResourceText">
		<nc:DocumentRelatedResourceText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DocumentRelatedResourceText>
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