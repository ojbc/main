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
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:pd-hi-codes="http://ojbc.org/IEPD/Extensions/ProsecutionDecisionReportCodes/Hawaii/1.0" xmlns:hpa="LegalInquiry_x0020_Custom"
	exclude-result-prefixes="hpa">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="/hpa:Report/hpa:Tablix1">
		<xsl:apply-templates select="hpa:Details_Collection" />
	</xsl:template>
	<xsl:template match="hpa:Details_Collection">
		<pd-doc:HPA_SAMPLES>
			<xsl:apply-templates select="hpa:Details" mode="report" />
		</pd-doc:HPA_SAMPLES>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="report">
		<pd-doc:ProsecutionDecisionReport>
			<xsl:apply-templates select="." mode="case" />
			<xsl:apply-templates select="." mode="arrest" />
			<xsl:apply-templates select="@LeadChargeOffenseDate[.!='']" />
			<xsl:apply-templates select="@ChargeOffenseDate[.!='']" />
			<xsl:apply-templates select="." mode="subject" />
			<xsl:apply-templates select="@LeadCharge[.!='']" mode="offenseAssociationLead" />
			<xsl:if test="@ChargeCode">
				<xsl:apply-templates select="@ChargeCode" mode="offenseAssociationAdditional" />
			</xsl:if>
		</pd-doc:ProsecutionDecisionReport>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="case">
		<nc:Case>
			<xsl:apply-templates select="@FileNumber[.!='']" />
			<xsl:apply-templates select="@CaseStatus[.!='']" />
			<xsl:apply-templates select="." mode="caseDisposition" />
			<xsl:apply-templates select="." mode="augment" />
			<xsl:apply-templates select="." mode="caseChargeAdditional" />
			<xsl:apply-templates select="@CaseReferralDate[.!='']" />
			<xsl:apply-templates select="@Stage[.!='']" />
		</nc:Case>
	</xsl:template>
	<xsl:template match="@LeadCharge" mode="offenseAssociationLead">
		<j:OffenseChargeAssociation>
			<j:Offense>
				<xsl:attribute name="structures:ref" select="generate-id(../@LeadChargeOffenseDate)" />
			</j:Offense>
			<j:Charge>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</j:Charge>
		</j:OffenseChargeAssociation>
	</xsl:template>
	<xsl:template match="@ChargeCode" mode="offenseAssociationAdditional">
		<j:OffenseChargeAssociation>
			<j:Offense>
				<xsl:attribute name="structures:ref" select="generate-id(../@ChargeOffenseDate)" />
			</j:Offense>
			<j:Charge>
				<xsl:attribute name="structures:ref" select="generate-id(.)" />
			</j:Charge>
		</j:OffenseChargeAssociation>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="subject">
		<nc:Person>
			<xsl:attribute name="structures:id" select="generate-id(./@NAME)" />
			<xsl:apply-templates select="@DateOfBirth[.!='']" />
			<xsl:apply-templates select="@NAME[.!='']" />
			<xsl:apply-templates select="@Sex[.!='']" />
			<xsl:apply-templates select="@Sid[.!='']" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="arrest">
		<j:Arrest>
			<xsl:apply-templates select="@CaseArrestDate[.!='']" />
			<xsl:apply-templates select="." mode="arrestAgency" />
		</j:Arrest>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="arrestAgency">
		<j:ArrestAgency>
			<xsl:apply-templates select="@LEAgency[.!='']" />
			<xsl:apply-templates select="@CaseLEReportNumber[.!='']" />
			<xsl:apply-templates select="@ChargeLEReportNumber[.!='']" />
		</j:ArrestAgency>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseDisposition">
		<nc:CaseDisposition>
			<xsl:apply-templates select="@CaseDispositionDate[.!='']" />
			<!-- @CaseDisposition is always NULL -->
			<xsl:apply-templates select="@DispositionCode[.!='']" />
		</nc:CaseDisposition>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="augment">
		<j:CaseAugmentation>
			<xsl:apply-templates select="." mode="caseChargeLead" />
			<xsl:apply-templates select="." mode="caseCourt" />
			<xsl:apply-templates select="." mode="caseCourtEvent" />
			<xsl:apply-templates select="." mode="caseDefendant" />
			<xsl:apply-templates select="@DefAttorneyName[.!='']" />
			<xsl:apply-templates select="@JudgeName[.!='']" />
			<xsl:apply-templates select="@ProsAttorneyName[.!='']" />
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseChargeLead">
		<j:CaseCharge>
			<xsl:attribute name="structures:id" select="generate-id(./@LeadCharge)" />
			<xsl:apply-templates select="." mode="chargeDispositionLead" />
			<j:ChargeQualifierText>Lead</j:ChargeQualifierText>
			<xsl:apply-templates select="." mode="chargeStatuteLead" />
			<xsl:apply-templates select="@Severity[.!='']" />
		</j:CaseCharge>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseChargeAdditional">
		<pd-ext:CaseAdditionalCharge>
			<xsl:attribute name="structures:id" select="generate-id(./@ChargeCode)" />
			<xsl:apply-templates select="." mode="chargeDispositionAdditional" />
			<xsl:apply-templates select="." mode="chargeStatute" />
		</pd-ext:CaseAdditionalCharge>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="chargeStatuteLead">
		<j:ChargeStatute>
			<xsl:apply-templates select="@LeadChargeStatute[.!='']" />
			<xsl:apply-templates select="@LeadCharge[.!='']" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="chargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="@ChargeCode[.!='']" />
			<xsl:apply-templates select="@ChargeDescription[.!='']" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseCourt">
		<j:CaseCourt>
			<xsl:apply-templates select="@CauseNumber[.!='']" />
		</j:CaseCourt>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseCourtEvent">
		<j:CaseCourtEvent>
			<xsl:apply-templates select="@EventDescription[.!='']" />
			<xsl:apply-templates select="@DocketDate[.!='']" />
		</j:CaseCourtEvent>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="caseDefendant">
		<j:CaseDefendantParty>
			<nc:EntityPerson>
				<xsl:attribute name="structures:ref" select="generate-id(./@NAME)" />
			</nc:EntityPerson>
		</j:CaseDefendantParty>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="chargeDispositionLead">
		<j:ChargeDisposition>
			<xsl:apply-templates select="@LeadChargeDispositionDate[.!='']" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="hpa:Details" mode="chargeDispositionAdditional">
		<j:ChargeDisposition>
			<xsl:apply-templates select="@ChargeDispositionDate[.!='']" />
			<xsl:apply-templates select="@ChargeDispositionCode[.!='']" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="@Stage">
		<pd-hi-codes:CaseStageCode>
			<xsl:value-of select="normalize-space(.)" />
		</pd-hi-codes:CaseStageCode>
	</xsl:template>
	<xsl:template match="@Sex">
		<j:PersonSexCode>
			<xsl:value-of select="normalize-space(.)" />
		</j:PersonSexCode>
	</xsl:template>
	<xsl:template match="@Sid">
		<j:PersonAugmentation>
			<j:PersonStateFingerprintIdentification>
				<!-- Sid -->
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(.)" />
				</nc:IdentificationID>
			</j:PersonStateFingerprintIdentification>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="@DateOfBirth">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="substring-before(.,'T')" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="@NAME">
		<nc:PersonName>
			<nc:PersonFullName>
				<xsl:value-of select="normalize-space(.)" />
			</nc:PersonFullName>
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="@LeadChargeOffenseDate | @ChargeOffenseDate">
		<j:Offense>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<nc:ActivityDate>
				<nc:DateTime>
					<xsl:value-of select="normalize-space(.)" />
				</nc:DateTime>
			</nc:ActivityDate>
		</j:Offense>
	</xsl:template>
	<xsl:template match="@CaseLEReportNumber">
		<pd-ext:AgencyCaseIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</pd-ext:AgencyCaseIdentification>
	</xsl:template>
	<xsl:template match="@ChargeLEReportNumber">
		<pd-ext:AgencyChargeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</pd-ext:AgencyChargeIdentification>
	</xsl:template>
	<xsl:template match="@LEAgency">
		<nc:OrganizationName>
			<xsl:value-of select="normalize-space(.)" />
		</nc:OrganizationName>
	</xsl:template>
	<xsl:template match="@CaseArrestDate">
		<nc:ActivityDate>
			<nc:DateTime>
				<xsl:value-of select="normalize-space(.)" />
			</nc:DateTime>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="@FileNumber">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="@CaseStatus">
		<nc:ActivityStatus>
			<pd-hi-codes:CaseStatusCode>
				<xsl:value-of select="normalize-space(.)" />
			</pd-hi-codes:CaseStatusCode>
		</nc:ActivityStatus>
	</xsl:template>
	<xsl:template match="@CaseDispositionDate | @ChargeDispositionDate | @LeadChargeDispositionDate">
		<nc:DispositionDate>
			<nc:DateTime>
				<xsl:value-of select="normalize-space(.)" />
			</nc:DateTime>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="@DispositionCode | @ChargeDispositionCode">
		<nc:DispositionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DispositionText>
	</xsl:template>
	<xsl:template match="@LeadChargeStatute | @ChargeCode">
		<j:StatuteCodeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</j:StatuteCodeIdentification>
	</xsl:template>
	<xsl:template match="@LeadCharge | @ChargeDescription">
		<j:StatuteDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:StatuteDescriptionText>
	</xsl:template>
	<xsl:template match="@Severity">
		<pd-hi-codes:ChargeSeverityCode>
			<xsl:value-of select="normalize-space(.)" />
		</pd-hi-codes:ChargeSeverityCode>
	</xsl:template>
	<xsl:template match="@CauseNumber">
		<pd-ext:CourtCaseNumber>
			<xsl:value-of select="normalize-space(.)" />
		</pd-ext:CourtCaseNumber>
	</xsl:template>
	<xsl:template match="@EventDescription">
		<nc:ActivityDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:ActivityDescriptionText>
	</xsl:template>
	<xsl:template match="@CaseReferralDate">
		<pd-ext:CaseReferralDate>
			<nc:DateTime>
				<xsl:value-of select="normalize-space(.)" />
			</nc:DateTime>
		</pd-ext:CaseReferralDate>
	</xsl:template>
	<xsl:template match="@DocketDate">
		<j:CourtEventAppearance>
			<j:CourtAppearanceDate>
				<nc:DateTime>
					<xsl:value-of select="normalize-space(.)" />
				</nc:DateTime>
			</j:CourtAppearanceDate>
		</j:CourtEventAppearance>
	</xsl:template>
	<xsl:template match="@DefAttorneyName">
		<j:CaseDefenseAttorney>
			<nc:RoleOfPerson>
				<nc:PersonName>
					<nc:PersonFullName>
						<xsl:value-of select="normalize-space(.)" />
					</nc:PersonFullName>
				</nc:PersonName>
			</nc:RoleOfPerson>
		</j:CaseDefenseAttorney>
	</xsl:template>
	<xsl:template match="@JudgeName">
		<j:CaseJudge>
			<nc:RoleOfPerson>
				<nc:PersonName>
					<nc:PersonFullName>
						<xsl:value-of select="normalize-space(.)" />
					</nc:PersonFullName>
				</nc:PersonName>
			</nc:RoleOfPerson>
		</j:CaseJudge>
	</xsl:template>
	<xsl:template match="@ProsAttorneyName">
		<j:CaseProsecutionAttorney>
			<nc:RoleOfPerson>
				<nc:PersonName>
					<nc:PersonFullName>
						<xsl:value-of select="normalize-space(.)" />
					</nc:PersonFullName>
				</nc:PersonName>
			</nc:RoleOfPerson>
		</j:CaseProsecutionAttorney>
	</xsl:template>
</xsl:stylesheet>