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
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:ac-doc="http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0"
    xmlns:s30="http://release.niem.gov/niem/structures/3.0/"
    xmlns:ac-ext="http://ojbc.org/IEPD/Extensions/AccessControlResponse/1.0"
    xmlns:ac-p="http://ojbc.org/IEPD/Extensions/AccessControlDecisionContexts/PolicyBasedAccessControlDecisionContext/1.0"
    xmlns:acr-srm="http://ojbc.org/IEPD/Extensions/AccessControlResultsMetadata/1.0"
    xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/"
    xmlns:acrer="http://ojbc.org/IEPD/Extensions/AccessControlRequestErrorReporting/1.0"
    xmlns:exc="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    exclude-result-prefixes="#all">
  
    <xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="policyUrlUserName" />
	<xsl:param name="policyUrlPassword" />
	<xsl:param name="helpDeskContactInfo" />
	
	<xsl:template match="/">
		<xsl:apply-templates select=
			"ac-doc:AccessControlResponse/ac-ext:AccessControlDecision/ac-ext:AccessControlDecisionContext/ac-p:PolicyBasedAccessControlDecisionContext[normalize-space()]"/>
		
		<xsl:apply-templates select="ac-doc:AccessControlResponse/acr-srm:AccessControlResultsMetadata/acrer:AccessControlRequestError[normalize-space()]"/>
		<xsl:apply-templates select="exc:EntityMergeResultMessage/exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/srer:SearchRequestError[normalize-space()]"/>
	</xsl:template>

	<xsl:template match="ac-p:PolicyBasedAccessControlDecisionContext">
		You need to acknowledge all of the following policies to be able to access the website. 
		<xsl:if test="$policyUrlUserName !='' and $policyUrlPassword != ''">
			<br/>Please use username "<xsl:value-of select="$policyUrlUserName"/>" 
			and password "<xsl:value-of select="$policyUrlPassword"/>" to view the policies.  	
		</xsl:if>
		<ul>
			<xsl:apply-templates select="ac-p:Policy"/>
		</ul>
		<form name="acknowledgePolicies"  method="POST" id="acknowledgePoliciesForm">
			<input name="acknowledgeAll" id="acknowledgeAll" type="submit" value="Acknowledge All" class="blueButton"/>
		</form>
	</xsl:template>
	<xsl:template match="ac-p:Policy">
		<li>
			<xsl:element name="a">
				<xsl:attribute name="href">
					<xsl:value-of select="ac-p:PolicyLocationURL"></xsl:value-of>
				</xsl:attribute>
				<xsl:attribute name="target"><xsl:text>_blank</xsl:text></xsl:attribute>
				<xsl:value-of select="ac-p:PolicyURI"/>
			</xsl:element>
		</li>
	</xsl:template>
	
	<xsl:template match="acrer:AccessControlRequestError">
		<xsl:value-of select="acrer:ErrorText"/>, please contact your Network Administrator or Help Desk 
		<xsl:if test="$helpDeskContactInfo != ''">
			at <xsl:value-of select="$helpDeskContactInfo"/>
		</xsl:if>
		 and provide this error message. 
	</xsl:template>

	<xsl:template match="srer:SearchRequestError">
		<xsl:choose>
			<xsl:when test="normalize-space(srer:ErrorText) = 'The source systems timed out or returned an error. Please try your search again later.'">
				The access control system timed out or returned an error. Please try again later. 
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="srer:ErrorText"></xsl:value-of>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>		
</xsl:stylesheet>
