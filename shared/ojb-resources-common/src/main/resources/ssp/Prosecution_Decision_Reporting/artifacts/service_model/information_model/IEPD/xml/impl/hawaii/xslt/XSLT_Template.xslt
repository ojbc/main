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
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" exclude-result-prefixes="">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="/">
		<xsl:apply-templates select="ROOT" />
	</xsl:template>
	<xsl:template match="ROOT">
		<doc:CJISProsDocument>
			<!--  xsl:apply-templates select="nc:Case" /-->
		</doc:CJISProsDocument>
	</xsl:template>
	<xsl:template match="nc:Case">
		<j:Case>
			<xsl:apply-templates select="nc:ActivityIdentification/nc:IdentificationID" mode="otn" />
			<xsl:apply-templates select="nc:ActivityStatus/pd-hi-codes:CaseStatusCode" />
			<xsl:apply-templates select="j51:CaseAugmentation/j51:CaseCourt/pd-ext:CourtCaseNumber" />
			<xsl:apply-templates select="j51:CaseAugmentation/j51:CaseCharge" />
			<xsl:apply-templates select="pd-ext:CaseAdditionalCharge" />
		</j:Case>
	</xsl:template>
	<xsl:template match="j51:CaseCharge | pd-ext:CaseAdditionalCharge">
		<xsl:param name="cID" select="./@structures:id" />
		<j:CaseCharge>
			<xsl:apply-templates select="j51:ChargeIdentification/nc:IdentificationID" mode="charge">
				<xsl:with-param name="cID" select="$cID" />
			</xsl:apply-templates>
			<xsl:apply-templates select="." mode="chargeClassification" />
			<xsl:apply-templates select="j51:ChargeDisposition" />
			<xsl:apply-templates select="j51:ChargeStatute" />
			<xsl:apply-templates select="." mode="subject" />
		</j:CaseCharge>
	</xsl:template>
	<xsl:template match="j51:CaseCharge | pd-ext:CaseAdditionalCharge" mode="subject">
		<xsl:variable name="subjectID"
			select="/pd-doc:ProsecutionDecisionReport/nc:Case/j51:CaseAugmentation/j51:CaseDefendantParty/nc:EntityPerson/@structures:ref" />
		<j:ChargeSubject>
			<xsl:apply-templates
				select="/pd-doc:ProsecutionDecisionReport/nc:Person[@structures:id=$subjectID]/nc:PersonName" />
			<xsl:apply-templates
				select="/pd-doc:ProsecutionDecisionReport/nc:Person[@structures:id=$subjectID]/nc:PersonBirthDate" />
			<xsl:apply-templates select="/pd-doc:ProsecutionDecisionReport/nc:Person[@structures:id=$subjectID]"
				mode="assignedID" />
		</j:ChargeSubject>
	</xsl:template>
	<xsl:template match="nc:Person" mode="assignedID">
		<j:PersonAssignedIDDetails>
			<xsl:apply-templates select="j51:PersonAugmentation/j51:PersonStateFingerprintIdentification" />
		</j:PersonAssignedIDDetails>
	</xsl:template>
	<xsl:template match="nc:PersonName">
		<j:PersonName>
			<xsl:apply-templates select="nc:PersonFullName" />
		</j:PersonName>
	</xsl:template>
	<xsl:template match="j51:ChargeStatute">
		<j:ChargeStatute>
			<xsl:apply-templates select="j51:StatuteCodeIdentification/nc:IdentificationID" mode="statute" />
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="j51:ChargeDisposition">
		<j:ChargeDisposition>
			<xsl:apply-templates select="nc:DispositionDate" />
			<xsl:apply-templates select="nc:DispositionText" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="j51:CaseCharge | pd-ext:CaseAdditionalCharge" mode="chargeClassification">
		<j:ChargeClassification>
			<!-- xsl:apply-templates select="j51:ChargeApplicabilityText" / -->
			<xsl:apply-templates select="." mode="AddOn" />
			<xsl:apply-templates select="pd-hi-codes:ChargeSeverityCode" />
		</j:ChargeClassification>
	</xsl:template>
	<xsl:template match="j51:PersonSexCode">
		<j:PersonSexCode>
			<xsl:value-of select="normalize-space(.)" />
		</j:PersonSexCode>
	</xsl:template>
	<xsl:template match="j51:PersonAugmentation/j51:PersonStateFingerprintIdentification">
		<j:PersonStateID>
			<j:ID>
				<xsl:value-of select="normalize-space(nc:IdentificationID)" />
			</j:ID>
		</j:PersonStateID>
	</xsl:template>
	<xsl:template match="nc:PersonBirthDate">
		<j:PersonBirthDate>
			<xsl:value-of select="normalize-space(nc:Date)" />
		</j:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc:PersonFullName">
		<xsl:param name="last" select="substring-before(.,',')" />
		<xsl:param name="remain" select="substring-after(.,', ')" />
		<xsl:if test="contains($remain,' ')">
			<j:PersonGivenName>
				<xsl:value-of select="substring-before($remain,' ')" />
			</j:PersonGivenName>
			<j:PersonMiddleName>
				<xsl:value-of select="substring-after($remain,' ')" />
			</j:PersonMiddleName>
		</xsl:if>
		
			<xsl:if test="not(contains($remain,' '))">
			<j:PersonGivenName>
				<xsl:value-of select="$remain" />
			</j:PersonGivenName>
		
		</xsl:if>
		
		
		<j:PersonSurName>
			<xsl:value-of select="$last" />
		</j:PersonSurName>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="statute">
		<j:StatuteCodeSectionID>
			<j:ID>
				<xsl:value-of select="normalize-space(.)" />
			</j:ID>
		</j:StatuteCodeSectionID>
	</xsl:template>
	<xsl:template match="nc:DispositionText">
		<j:ChargeDispositionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeDispositionDescriptionText>
	</xsl:template>
	<xsl:template match="nc:DispositionDate">
		<j:ChargeDispositionDate>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeDispositionDate>
	</xsl:template>
	<xsl:template match="pd-hi-codes:ChargeSeverityCode">
		<j:ChargeSeverityText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="j51:CaseCharge | pd-ext:CaseAdditionalCharge" mode="AddOn">
		<xsl:if test="name() = 'j:CaseCharge'">
			<j:ChargeQualifierText>N</j:ChargeQualifierText>
		</xsl:if>
		<xsl:if test="name() = 'pd-ext:CaseAdditionalCharge'">
			<j:ChargeQualifierText>Y</j:ChargeQualifierText>
		</xsl:if>
	</xsl:template>
	<xsl:template match="j51:ChargeApplicabilityText">
		<j:ChargeApplicabilityText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeApplicabilityText>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="charge">
		<xsl:param name="cID" />
		<j:ChargeID>
			<xsl:if test="$cID !=''">
				<xsl:attribute name="j:id"><xsl:value-of select="$cID" /></xsl:attribute>
			</xsl:if>
			<j:ID>
				<xsl:value-of select="normalize-space(.)" />
			</j:ID>
		</j:ChargeID>
	</xsl:template>
	<xsl:template match="pd-ext:CourtCaseNumber">
		<j:CaseTrackingID>
			<j:ID>
				<xsl:value-of select="normalize-space(.)" />
			</j:ID>
		</j:CaseTrackingID>
	</xsl:template>
	<xsl:template match="pd-hi-codes:CaseStatusCode">
		<j:ActivityTypeText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ActivityTypeText>
	</xsl:template>
	<xsl:template match="nc:IdentificationID" mode="otn">
		<j:ActivityID>
			<j:ID>
				<xsl:value-of select="normalize-space(.)" />
			</j:ID>
		</j:ActivityID>
	</xsl:template>
</xsl:stylesheet>