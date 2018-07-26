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
		<pd-doc:ProsecutionDecisionReport>
			<xsl:variable name="caseID">
				<xsl:value-of select="CriminalCaselist/id" />
			</xsl:variable>
			<xsl:apply-templates select="." mode="report1">
				<xsl:with-param name="caseID" select="$caseID" />
			</xsl:apply-templates>
		</pd-doc:ProsecutionDecisionReport>
	</xsl:template>
	<xsl:template match="ROOT" mode="report1">
		<xsl:param name="caseID" />
		<xsl:for-each
			select="CaseDefendantToChargelist[target-CriminalDefendant=../CriminalDefendantlist[target-CriminalCase=$caseID]/id]/source-Charge">
			<xsl:variable name="chargeID">
				<xsl:value-of select="." />
			</xsl:variable>
			<xsl:apply-templates select="/ROOT" mode="report2">
				<xsl:with-param name="caseID" select="$caseID" />
				<xsl:with-param name="chargeID" select="$chargeID" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="ROOT" mode="report2">
		<xsl:param name="caseID" />
		<xsl:param name="chargeID" />
		<nc:Case>
			<xsl:apply-templates select="." mode="case">
				<xsl:with-param name="caseID" select="$caseID" />
			</xsl:apply-templates>
			<xsl:apply-templates select="." mode="augment">
				<xsl:with-param name="caseID" select="$caseID" />
			</xsl:apply-templates>
		</nc:Case>
		<xsl:apply-templates select="." mode="subject">
			<xsl:with-param name="caseID" select="$caseID" />
			<xsl:with-param name="chargeID" select="$chargeID" />
		</xsl:apply-templates>
		<xsl:for-each select="CaseVictimlist">
			<xsl:variable name="victimID">
				<xsl:value-of select=".[target-CriminalCase=$caseID]/source-Person" />
			</xsl:variable>
			<xsl:apply-templates select="." mode="victim">
				<xsl:with-param name="caseID" select="$caseID" />
				<xsl:with-param name="chargeID" select="$chargeID" />
				<xsl:with-param name="victimID" select="$victimID" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="ROOT" mode="case">
		<xsl:param name="caseID" />
		<xsl:apply-templates select="CriminalCaselist[id=$caseID]/fileNumber[.!='']" />
		<xsl:apply-templates
			select="Organizationlist[id=../CaseToOrganizationlist[target-CriminalCase=$caseID]/source-Organization]/primaryReferenceId[.!='']" />
	</xsl:template>
	<xsl:template match="ROOT" mode="augment">
		<xsl:param name="caseID" />
		<j:CaseAugmentation>
			<xsl:apply-templates select="." mode="acharge">
				<xsl:with-param name="caseID" select="$caseID" />
			</xsl:apply-templates>
		</j:CaseAugmentation>
	</xsl:template>
	<xsl:template match="ROOT" mode="acharge">
		<xsl:param name="caseID" />
		<j:CaseAmendedCharge>
			<xsl:apply-templates select="CriminalCaselist[id=$caseID]" mode="disposition" />
			<xsl:apply-templates select="CriminalCaselist[id=$caseID]/filingType" mode="charge_severity" />
			<xsl:apply-templates
				select="CaseDefendantToChargelist[target-CriminalDefendant=../CriminalDefendantlist[target-CriminalCase=$caseID]/id]/source-Charge"
				mode="charge_statute" />
			<xsl:apply-templates select="." mode="role" />
		</j:CaseAmendedCharge>
	</xsl:template>
	<xsl:template match="ROOT" mode="role">
		<j:ChargeSubject>
			<xsl:for-each select="CaseDefendantToChargelist/target-CriminalDefendant">
				<nc:RoleOfPerson>
					<xsl:attribute name="structures:ref" select="generate-id(.)" />
				</nc:RoleOfPerson>
			</xsl:for-each>
		</j:ChargeSubject>
		<j:ChargeVictim>
			<xsl:for-each select="CaseVictimlist">
				<nc:RoleOfPerson>
					<xsl:attribute name="structures:ref" select="generate-id(.)" />
				</nc:RoleOfPerson>
			</xsl:for-each>
		</j:ChargeVictim>
	</xsl:template>
	<xsl:template match="ROOT" mode="subject">
		<xsl:param name="caseID" />
		<xsl:param name="chargeID" />
		<xsl:variable name="defendantID">
			<xsl:value-of select="CaseDefendantToChargelist[source-Charge=$chargeID]/target-CriminalDefendant" />
		</xsl:variable>
		<xsl:variable name="subjectID">
			<xsl:value-of select="CriminalDefendantlist[id=$defendantID]/source-Person" />
		</xsl:variable>
		<nc:Person>
			<xsl:attribute name="structures:id" select="generate-id(./CaseDefendantToChargelist/target-CriminalDefendant)" />
			<xsl:apply-templates select="Personlist[id=$subjectID]/birthDate[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$subjectID]/primaryName[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$subjectID]/gender[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$subjectID]/socialSecurityNumber[.!='']" />
			<xsl:apply-templates select="/ROOT/CriminalDefendantlist[source-Person=$subjectID]/otn[.!='']" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="CaseVictimlist" mode="victim">
		<xsl:param name="caseID" />
		<xsl:param name="chargeID" />
		<xsl:param name="victimID" />
		<nc:Person>
			<xsl:attribute name="structures:id" select="generate-id(.)" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$victimID]/birthDate[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$victimID]/description[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$victimID]/race[.!='']" />
			<xsl:apply-templates select="/ROOT/Personlist[id=$victimID]/gender[.!='']" />
		</nc:Person>
	</xsl:template>
	<xsl:template match="CriminalCaselist" mode="disposition">
		<j:ChargeDisposition>
			<xsl:apply-templates select="startDate[.!='']" mode="disposition" />
			<xsl:apply-templates select="description[.!='']" mode="disposition" />
		</j:ChargeDisposition>
	</xsl:template>
	<xsl:template match="description" mode="disposition">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="startDate" mode="disposition">
		<nc:DispositionDate>
			<nc:Date>
				<xsl:value-of select="substring-before(., ' ')" />
			</nc:Date>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="source-Charge" mode="charge_statute">
		<xsl:variable name="chargeID" select="." />
		<j:ChargeStatute>
			<j:StatuteCodeSectionIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="/ROOT/Chargelist[id=$chargeID]/comments[.!='']" />
				</nc:IdentificationID>
			</j:StatuteCodeSectionIdentification>
		</j:ChargeStatute>
	</xsl:template>
	<xsl:template match="filingType" mode="charge_severity">
		<j:ChargeSeverityText>
			<xsl:value-of select="normalize-space(.)" />
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="fileNumber">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="Organizationlist/primaryReferenceId">
		<j:ActivityAugmentation>
			<j:CaseLinkage>
				<j:OrganizationORIIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="normalize-space(.)" />
					</nc:IdentificationID>
				</j:OrganizationORIIdentification>
			</j:CaseLinkage>
		</j:ActivityAugmentation>
	</xsl:template>
	<xsl:template match="birthDate">
		<nc:PersonBirthDate>
			<nc:Date>
				<xsl:value-of select="substring-before(., ' ')" />
			</nc:Date>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="primaryName">
		<nc:PersonName>
			<xsl:apply-templates select="." mode="first" />
			<xsl:apply-templates select="." mode="middle" />
			<xsl:apply-templates select="." mode="last" />
		</nc:PersonName>
	</xsl:template>
	<xsl:template match="primaryName" mode="first">
		<nc:PersonGivenName>
			<xsl:value-of select="substring-before(., ';')" />
		</nc:PersonGivenName>
	</xsl:template>
	<xsl:template match="primaryName" mode="middle">
		<nc:PersonMiddleName>
			<xsl:value-of select="substring-before(substring-after(., ';'), ';')" />
		</nc:PersonMiddleName>
	</xsl:template>
	<xsl:template match="primaryName" mode="last">
		<nc:PersonSurName>
			<xsl:value-of select="substring-before(substring-after(substring-after(., ';'), ';'), ';')" />
		</nc:PersonSurName>
	</xsl:template>
	<xsl:template match="gender">
		<!-- Need to check with Legaledge on possible values for gender -->
		<xsl:choose>
			<xsl:when test=". = 'Male'">
				<j:PersonSexCode>M</j:PersonSexCode>
			</xsl:when>
			<xsl:when test=". = 'Female'">
				<j:PersonSexCode>F</j:PersonSexCode>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="socialSecurityNumber">
		<nc:PersonSSNIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="normalize-space(.)" />
			</nc:IdentificationID>
		</nc:PersonSSNIdentification>
	</xsl:template>
	<xsl:template match="otn">
		<j:PersonAugmentation>
			<j:PersonStateFingerprintIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="normalize-space(.)" />
				</nc:IdentificationID>
			</j:PersonStateFingerprintIdentification>
		</j:PersonAugmentation>
	</xsl:template>
	<xsl:template match="description">
		<nc:PersonDescriptionText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonDescriptionText>
	</xsl:template>
	<xsl:template match="race">
		<nc:PersonRaceText>
			<xsl:value-of select="normalize-space(.)" />
		</nc:PersonRaceText>
	</xsl:template>
</xsl:stylesheet>