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
		<xsl:apply-templates select="CaseDefendantToChargelist" />
	</xsl:template>
	<xsl:template match="CaseDefendantToChargelist">
		<xsl:variable name="chargeID" select="source-Charge" />
		<xsl:variable name="PersonID" select="target-CriminalDefendant" />
		<pd-doc:ProsecutionDecisionReport>
			<xsl:apply-templates select="/ROOT" mode="report" />
		</pd-doc:ProsecutionDecisionReport>
	</xsl:template>
	<xsl:template match="ROOT" mode="report">
		<pd-doc:ProsecutionDecisionReport>
			<xsl:apply-templates select="." mode="case" />
			<xsl:apply-templates select="." mode="person" />
		</pd-doc:ProsecutionDecisionReport>
	</xsl:template>
	<xsl:template match="ROOT" mode="case">
		<nc:Case>
			<xsl:apply-templates select="CriminalCaselist/fileNumber" />
			<xsl:apply-templates select="." mode="charge" />
		</nc:Case>
	</xsl:template>
	<xsl:template match="ROOT" mode="charge">
		<j:CaseAugmentation>
			<j:CaseAmendedCharge>
				<xsl:attribute name="structures:id"><xsl:value-of select="(.)" /></xsl:attribute>
				<xsl:apply-templates select="CriminalCaselist" mode="disposition" />
				<xsl:apply-templates select="CaseDefendantToChargelist" mode="charge_statute" />
				<xsl:apply-templates select="CriminalCaselist/filingType" mode="charge_severity" />
			</j:CaseAmendedCharge>
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="CriminalCaselist" mode="disposition">
		<j:ChargeDisposition>
			<xsl:apply-templates select="description" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="CaseDefendantToChargelist" mode="charge_statute">
		<xsl:variable name="chargeID" select="source-Charge" />
		<j:ChargeStatute>
			<j:StatuteCodeSectionIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="/ROOT/Chargelist[id=$chargeID]/section" />
				</nc:IdentificationID>
			</j:StatuteCodeSectionIdentification>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="filingType" mode="charge_severity">
		<j:ChargeSeverityText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="ROOT" mode="person">
		<nc:Person>
			<xsl:attribute name="structures:id"><xsl:value-of select="(.)" /></xsl:attribute>
			<xsl:apply-templates select="birthDate" />
		</nc:Person>
	</xsl:template>
	<!-- ELEMENTS -->
	<xsl:template match="fileNumber">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="description">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DispositionDescriptionText>
	</xsl:template>
</xsl:stylesheet>