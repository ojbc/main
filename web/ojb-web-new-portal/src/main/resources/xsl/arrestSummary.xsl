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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:chsres-doc="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0"
	xmlns:chsres-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchResults/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
	exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<!-- xsl:variable name="agency" select="//chsres-ext:SearchResultCategoryText" / -->
	<xsl:template match="/chsres-doc:CriminalHistorySearchResults">
		<table border="BASIC">
			<xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult" mode="personDetail" />
			<xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult" mode="charges" />
		</table>
	</xsl:template>
	<xsl:template match="chsres-ext:CriminalHistorySearchResult" mode="personDetail">
		<tr>
			<td colspan="2" class="detailsLabel">
				<b>NAME:</b>
			</td>
			<td colspan="6">
				<b>
					<xsl:apply-templates select="j:Subject/nc:RoleOfPerson/nc:PersonName" mode="constructName" />
				</b>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="detailsLabel">ARREST DATE:</td>
			<td colspan="6">
				<xsl:value-of select="j:Arrest/nc:ActivityDate/nc:Date" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="chsres-ext:CriminalHistorySearchResult" mode="charges">
		<xsl:apply-templates select="j:Arrest/j:ArrestCharge" />
	</xsl:template>
	<xsl:template match="j:ArrestCharge">
		<td colspan="8" height="15" class="blank">
		</td>
		<tr>
			<td colspan="2" class="chargeLabel">
				<b>CHARGE AT ARREST:</b>
			</td>
			<td colspan="6">
				<b>
					<xsl:value-of select="j:ChargeDescriptionText" />
				</b>
			</td>
		</tr>
		<xsl:apply-templates select="j:ChargeDisposition" />
	</xsl:template>
	<xsl:template match="j:ChargeDisposition">
		<xsl:variable name="CDid" select="@structures:id" />
		<tr>
			<td colspan="8" height="15" class="blank">
			</td>
		</tr>
		<xsl:if test="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID[. !='']">
			<tr>
				<td colspan="2" class="dispositionLabel">CASE NUMBER:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:CourtCase/nc:ActivityIdentification/nc:IdentificationID" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:DispositionCodeDescriptionText[. !='']">
			<tr>
				<td colspan="2" class="dispositionLabel">DISPOSITION:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:DispositionCodeDescriptionText" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:DispositionDismissalReasonText[. !='']">
			<tr>
				<td colspan="2" class="dispositionLabel">DISMISSAL REASON:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:DispositionDismissalReasonText" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="nc:DispositionDate/nc:Date[. !='']">
			<tr>
				<td colspan="2" class="dispositionLabel">DISPOSITION DATE:</td>
				<td colspan="6">
					<xsl:value-of select="nc:DispositionDate/nc:Date" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:ChargeModificationDescriptionCodeText[. !='']">
			<tr>
				<td colspan="2" class="dispositionLabel">GENERAL OFFENSE:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:ChargeModificationDescriptionCodeText" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:DispositionChargeCountQuantity[. !='' and . &gt; 1]">
			<tr>
				<td colspan="2" class="dispositionLabel">COUNTS:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:DispositionChargeCountQuantity" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
			<xsl:for-each select="chsres-ext:FiledCharge/chsres-ext:ChargeMunicipalCodeDescriptionText">
				<tr>
					<td colspan="2" class="chargeLabel">CHARGE FILED:</td>
					<td colspan="6">
						<xsl:value-of select="." />
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText[. !='']">
			<xsl:for-each select="chsres-ext:AmendedCharge/chsres-ext:ChargeMunicipalCodeDescriptionText">
				<tr>
					<td colspan="2" class="chargeLabel">AMENDED CHARGE:</td>
					<td colspan="6">
						<xsl:value-of select="." />
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="chsres-ext:FiledCharge/j:ChargeStatute/j:StatuteDescriptionText[. !='']">
			<xsl:for-each select="chsres-ext:FiledCharge">
				<tr>
					<td colspan="2" class="chargeLabel">CHARGE FILED:</td>
					<td colspan="6">
						<xsl:value-of select="j:ChargeStatute/j:StatuteDescriptionText" />
						<xsl:if test="j:ChargeSeverityText">
							<xsl:value-of select="' - '" />
							<xsl:value-of select="j:ChargeSeverityText" />
						</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="chsres-ext:AmendedCharge/j:ChargeStatute/j:StatuteDescriptionText[. !='']">
			<xsl:for-each select="chsres-ext:AmendedCharge">
				<tr>
					<td colspan="2" class="chargeLabel">AMENDED CHARGE:</td>
					<td colspan="6">
						<xsl:value-of select="j:ChargeStatute/j:StatuteDescriptionText" />
						<xsl:if test="j:ChargeSeverityText">
							<xsl:value-of select="' - '" />
							<xsl:value-of select="j:ChargeSeverityText" />
						</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:apply-templates
			select="../j:ChargeSentence[@structures:id=../../../chsres-ext:DispositionSentenceAssociation[nc:Disposition/@structures:ref=$CDid]/j:Sentence/@structures:ref]">
			<xsl:with-param name="CDid" select="$CDid" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="j:ChargeSentence">
		<xsl:param name="CDid" />
		<xsl:variable name="CSid" select="@structures:id" />
		<xsl:if test="j:SentenceTerm[nc:ActivityCategoryText='JAIL']/j:TermDuration[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">JAIL:</td>
				<td colspan="6">
					<xsl:apply-templates select="j:SentenceTerm/j:TermDuration" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="j:SentenceTerm[nc:ActivityCategoryText='PRISON']/j:TermDuration[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">PRISON:</td>
				<td colspan="6">
					<xsl:apply-templates select="j:SentenceTerm/j:TermDuration" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="j:SentenceSuspendedTerm/j:TermDuration[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">SUSPENDED:</td>
				<td colspan="6">
					<xsl:apply-templates select="j:SentenceSuspendedTerm/j:TermDuration" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="j:SentenceDeferredTerm/j:TermDuration[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">DEFERRED:</td>
				<td colspan="6">
					<xsl:apply-templates select="j:SentenceDeferredTerm/j:TermDuration" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="j:SupervisionFineAmount/nc:Amount[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">FINE</td>
				<td colspan="6">
					<xsl:value-of select="j:SupervisionFineAmount/nc:Amount" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:FineSuspendedAmount/nc:Amount[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">SUSPENDED FINE:</td>
				<td colspan="6">
					<xsl:value-of select="chsres-ext:FineSuspendedAmount/nc:Amount" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if
			test="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">RESTITUTION:</td>
				<td colspan="6">
					<xsl:value-of
						select="//j:Restitution[@structures:id=//j:ActivityObligationAssociation[nc:Activity/@structures:ref=$CSid]/nc:Obligation/@structures:ref]/nc:ObligationDueAmount/nc:Amount" />
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="chsres-ext:AlternateSentence/chsres-ext:AlternateSentenceCodeDescriptionText[. !='']">
			<xsl:for-each select="chsres-ext:AlternateSentence/chsres-ext:AlternateSentenceCodeDescriptionText">
				<tr>
					<td colspan="2" class="sentenceLabel">ADDITIONAL SENTENCE:</td>
					<td colspan="6">
						<xsl:value-of select="." />
					</td>
				</tr>
			</xsl:for-each>
		</xsl:if>
		<xsl:if test="../j:ChargeDisposition[@structures:id=$CDid]/chsres-ext:DispositionProvisionCodeDescriptionText[. !='']">
			<tr>
				<td colspan="2" class="sentenceLabel">PROVISION:</td>
				<td colspan="6">
					<xsl:value-of select="../j:ChargeDisposition[@structures:id=$CDid]/chsres-ext:DispositionProvisionCodeDescriptionText" />
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="nc:PersonName" mode="constructName">
		<xsl:value-of select="concat(nc:PersonSurName,', ',nc:PersonGivenName)" />
	</xsl:template>
	<xsl:template match="j:TermDuration">
		<xsl:if test="contains(.,'Y')">
			<xsl:variable name="years" select="translate(substring-before(.,'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
			<xsl:value-of select="$years,' Years; '" />
		</xsl:if>
		<xsl:if test="contains(.,'M')">
			<xsl:choose>
				<xsl:when test="contains(.,'Y') and contains(.,'M')">
					<xsl:variable name="months"
						select="translate(substring-after(substring-before(.,'M'),'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$months,' Months; '" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="months" select="translate(substring-before(.,'M'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$months,' Months; '" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="contains(.,'D')">
			<xsl:choose>
				<xsl:when test="contains(.,'Y') and contains(.,'M')">
					<xsl:variable name="days"
						select="translate(substring-after(substring-before(.,'D'),'M'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$days,' Days'" />
				</xsl:when>
				<xsl:when test="contains(.,'Y') and not(contains(.,'M'))">
					<xsl:variable name="days"
						select="translate(substring-after(substring-before(.,'D'),'Y'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$days,' Days'" />
				</xsl:when>
				<xsl:when test="contains(.,'M') and not(contains(.,'Y'))">
					<xsl:variable name="days"
						select="translate(substring-after(substring-before(.,'D'),'M'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$days,' Days'" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="days" select="translate(substring-before(.,'D'), translate(., '0123456789', ''), '')"></xsl:variable>
					<xsl:value-of select="$days,' Days; '" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>