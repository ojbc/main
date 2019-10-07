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
	xmlns:chsres-doc="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0"
	xmlns:chsres-ext="http://ojbc.org/IEPD/Extensions/CriminalHistorySearchResults/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/6.0/" xmlns:intel="http://release.niem.gov/niem/domains/intelligence/4.0/"
	xmlns:nc="http://release.niem.gov/niem/niem-core/4.0/" xmlns:ncic="http://release.niem.gov/niem/codes/fbi_ncic/4.0/"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/4.0/" xmlns:structures="http://release.niem.gov/niem/structures/4.0/"
  xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4"
	xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/CriminalHistorySearchResults/1.0 ../xsd/criminal_history_search_results.xsd"
	exclude-result-prefixes="#all">

	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="resultType">MUNI</xsl:param>
	<xsl:param name="authorities"></xsl:param>
	<xsl:template match="/chsres-doc:CriminalHistorySearchResults">
	
	  <xsl:variable name="accessDenialReasons" select="srm:SearchResultsMetadata/iad:InformationAccessDenial" />
    <xsl:variable name="requestErrors" select="srm:SearchResultsMetadata/srer:SearchRequestError" />
    <xsl:variable name="tooManyResultsErrors" select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />

    <xsl:apply-templates select="$accessDenialReasons" />
    <xsl:apply-templates select="$requestErrors" />
    <xsl:apply-templates select="$tooManyResultsErrors" />
    
    <xsl:if test="(not($tooManyResultsErrors) and not($accessDenialReasons) and not($requestErrors))">
		  <xsl:call-template name="arrests"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="arrests">
			<table class="table table-striped table-bordered" style="width:100%" id="searchResultsTable">
				<thead>
					<tr>
						<th>OTN
					    <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="Offender Tracking Number on the OSBI fingerprint card supplied by the arresting agency.  
                  OTN is used to link the filing/disposition record to the OSBI Criminal History File.  OTN consists of nine numbers and a letter. (E.G. 001234567R)">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th>NAME
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The name on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th>DOB
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The date of birth on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th>SSN
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The social security number on the fingerprint card at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th style="white-space:nowrap;">DATE OF ARREST
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The date on which the arrest occurred, as provided on the fingerprint card.">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th>CHARGE
              <a href="#" data-toggle="popover" data-trigger="hover" 
                data-content="The charge on the fingerprint card entered by the arresting agency at the time of arrest.">
                <i class="fas fa-info-circle"></i></a>
						</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<xsl:apply-templates select="chsres-ext:CriminalHistorySearchResult"/>
				</tbody>
			</table>
	</xsl:template>
	
	<xsl:template match="chsres-ext:CriminalHistorySearchResult">
		<tr>
		  <xsl:attribute name="id">
         <xsl:value-of select="normalize-space(intel:SystemIdentification/nc:IdentificationID)"/>
      </xsl:attribute>
      <xsl:if test="exists(j:Arrest/j:ArrestCharge/chsres-ext:ChargeReferralCountQuantity[number() > 1])">
	      <xsl:attribute name="class">
	         <xsl:text>bg-warning</xsl:text>
	      </xsl:attribute>
      </xsl:if>
		  
			<td>
				<xsl:value-of select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/j:SubjectIdentification/nc:IdentificationID"></xsl:value-of>
			</td>					
			<td><xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonName" mode="primaryName"></xsl:apply-templates></td>
			<td>
				<xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonBirthDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>	
			<td>
				<xsl:apply-templates select="j:Subject[@structures:id=../j:Arrest/j:ArrestSubject/nc:RoleOfPerson/@structures:ref]/nc:RoleOfPerson/nc:PersonSSNIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:apply-templates select="j:Arrest/nc:ActivityDate/nc:Date" mode="formatDateAsMMDDYYYY"/>
			</td>
			<td>
				<xsl:for-each select="j:Arrest/j:ArrestCharge/j:ChargeDescriptionText">
					<xsl:if test="position() != 1">
						<br/>
					</xsl:if>
					<xsl:value-of select="."/>
				</xsl:for-each>
				<span class="chargeIds d-none">
				  <xsl:value-of select="string-join(j:Arrest/j:ArrestCharge/j:ChargeIdentification/nc:IdentificationID, ',')"/>
				</span>
				<span class="unhideableChargeIds d-none">
				  <xsl:value-of select="string-join(j:Arrest/j:ArrestCharge[chsres-ext:ChargeHiddenIndicator = 'true']/j:ChargeIdentification/nc:IdentificationID, ',')"/>
				</span>
				<span class="hideableChargeIds d-none">
				  <xsl:value-of select="string-join(j:Arrest/j:ArrestCharge[chsres-ext:ChargeHiddenIndicator = 'false']/j:ChargeIdentification/nc:IdentificationID, ',')"/>
				</span>
			</td>
			<td align="right" width="120px">
			  <xsl:if test="contains($authorities, 'CAN_EDIT')">
				  <a href="#" class="editArrest" style="margin-right:3px" title="Edit" data-toggle="tooltip">
	  				<i class="fas fa-edit fa-2x"></i>
	 				</a>
 				</xsl:if>
 				<xsl:if test="j:Arrest/j:ArrestCharge/chsres-ext:ChargeHiddenIndicator = 'true'">
           <a href="#" class="unhideArrest" style="margin-right:3px" title="Unhide" data-toggle="tooltip">
             <i class="fas fa-eye fa-2x"></i>
           </a>
				</xsl:if>
        <xsl:if test="j:Arrest/j:ArrestCharge/chsres-ext:ChargeHiddenIndicator = 'false' and contains($authorities, 'CAN_HIDE')">
          <a href="#" class="hideArrest" style="margin-right:3px" title="Hide" data-toggle="tooltip">
            <i class="fas fa-eye-slash fa-2x"></i>
          </a>
        </xsl:if>
        <xsl:if test="contains($authorities, 'CAN_REFER')">
					<xsl:element name="a">
					  <xsl:attribute name="href">#</xsl:attribute>
					  <xsl:attribute name="class">referArrest</xsl:attribute>
					  <xsl:attribute name="title">Refer the arrest</xsl:attribute>
	          <xsl:attribute name="data-toggle">tooltip</xsl:attribute>
					  <i class="fas fa-share-square fa-2x"></i>
					</xsl:element>
				</xsl:if>
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

  <xsl:template match="srer:SearchRequestError">
    <span class="error">
      System Name: <xsl:value-of select="nc:SystemName" />, 
      Error: <xsl:value-of select="srer:ErrorText" />
    </span>
    <br />
  </xsl:template>

  <xsl:template match="srer:SearchResultsExceedThresholdError">
    <span class="error">
      System <xsl:value-of select="../nc:SystemName" /> returned too many records, please refine your criteria.
    </span>
    <br />
  </xsl:template>
	
 </xsl:stylesheet>
