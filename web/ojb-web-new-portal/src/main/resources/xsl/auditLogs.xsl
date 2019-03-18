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
  xmlns:alsres-doc="http://ojbc.org/IEPD/Exchange/AuditLogSearchResults/1.0"
  xmlns:alsres-ext="http://ojbc.org/IEPD/Extensions/AuditLogSearchResults/1.0" xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/"
  xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
  xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.1/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.1/"
  xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
  xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
  xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />

	<xsl:template match="/alsres-doc:AuditLogSearchResults">
	
	  <xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
    <xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

    <xsl:apply-templates select="$accessDenialReasons" />
    <xsl:apply-templates select="$tooManyResultsErrors" />
    
    <xsl:if test="(not($tooManyResultsErrors) and not($accessDenialReasons))">
		  <xsl:call-template name="auditLogs"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="auditLogs">
			<table class="table table-striped table-bordered" style="width:100%;" id="searchResultsTable">
				<thead>
					<tr>
						<th>NAME</th>
						<th>AGENCY NAME</th>
						<th>ORI</th>
						<th>ACTION</th>
						<th>REQUEST ID</th>
						<th>REQUEST TYPE</th>
            <th>DATE</th>
						<th>REQUEST PAYLOAD</th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="alsres-ext:AuditLogSearchResult"/>
				</tbody>
			</table>
	</xsl:template>
	
	<xsl:template match="alsres-ext:AuditLogSearchResult">
		<tr>
		  <xsl:attribute name="id">
         <xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
      </xsl:attribute>
		  
			<td><xsl:apply-templates select="nc:UserPersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:value-of select="nc:Organization/nc:OrganizationName"/>
			</td>
			<td>
				<xsl:value-of select="nc:Organization/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:value-of select="alsres-ext:UserActionPerformedText"/>
			</td>
			<td>
				<xsl:value-of select="nc:Request/nc:RequestIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:value-of select="nc:Request/nc:RequestCategoryText"/>
			</td>
      <td>
        <xsl:apply-templates select="alsres-ext:UserActionPerformedDate/nc:DateTime" mode="formatDateTime"/>
      </td>
			<td>
			   <a href="#" class="requestPayload" style="margin-right:3px" title="request payload" data-toggle="tooltip">
			     <i class="fas fa-file-alt fa-lg" ></i>
		     </a>
			   <div class="auditRequestPayload">
			     <pre><xsl:text>      </xsl:text><xsl:apply-templates select="nc:Request/alsres-ext:AuditedRequestMessage" mode="serialize"/></pre>
			   </div>
			</td>
		</tr>
	</xsl:template>
	
	 <xsl:template match="iad:InformationAccessDenial">
    <span class="error">
      User does not meet privilege requirements to access
      <xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />. To request access, contact your IT department.
    </span>
    <br />
  </xsl:template>

  <xsl:template match="srer:SearchResultsExceedThresholdError">
    <span class="error">
      System <xsl:value-of select="../nc:SystemName" /> returned too many records, please refine your criteria.
    </span>
    <br />
  </xsl:template>
	<xsl:template match="*" mode="serialize">
    <xsl:text>&lt;</xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:apply-templates select="@*" mode="serialize" />
    <xsl:choose>
        <xsl:when test="node()">
            <xsl:text>&gt;</xsl:text>
            <xsl:apply-templates mode="serialize" />
            <xsl:text>&lt;/</xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text>&gt;</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text> /&gt;</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match="@*" mode="serialize">
    <xsl:text> </xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:text>="</xsl:text>
    <xsl:value-of select="."/>
    <xsl:text>"</xsl:text>
</xsl:template>

<xsl:template match="text()" mode="serialize">
    <xsl:value-of select="."/>
</xsl:template>
 </xsl:stylesheet>
