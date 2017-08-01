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
    xmlns:cs-res-doc="http://ojbc.org/IEPD/Exchange/CustodySearchResults/1.0"
    xmlns:cs-res-ext="http://ojbc.org/IEPD/Extensions/CustodySearchResultsExtension/1.0"
    xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
    xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/"
    xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
    xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/"
    xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
    xmlns:nc30="http://release.niem.gov/niem/niem-core/3.0/"    
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/cs-res-doc:CustodySearchResults">
		<xsl:choose>
			<xsl:when test="srm:SearchResultsMetadata/srer:SearchRequestError 
				| srm:SearchResultsMetadata/iad:InformationAccessDenial
				| srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError">
				<table id="searchResultsError" class="detailsTable">
					<tr>
						<td class="detailsTitle" >SEARCH RESULTS ERROR</td>
					</tr>
					<tr>
						<td>
							<xsl:apply-templates select="srm:SearchResultsMetadata/srer:SearchRequestError" /> 
							<xsl:apply-templates select="srm:SearchResultsMetadata/iad:InformationAccessDenial" /> 
							<xsl:apply-templates select="srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" /> 
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(cs-res-ext:CustodySearchResult) = 0">
						<table id="custodyDetailError" class="detailsTable">
							<tr>
								<td class="detailsTitle" >NO ASSOCIATED JAIL CUSTODY</td>
							</tr>
							<tr>
								<td>
									<span class="error">There is no custody data associated with this person record.</span><br /> 
								</td>
							</tr>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<script type="text/javascript">
							$(function () {
								$('#custodyTable tr').click(function () {
								
									var systemName =$(this).attr('systemName');
									var identificationSourceText = encode($(this).attr('identificationSourceText'));
									var identificationID = $(this).attr('identificationID');
																		
									$('#custodyTable tr').removeClass("selected");
									$(this).addClass("selected");
									
									var tempDiv = '<div id="modalIframeSpinner" style="height:50%;width:100%"/>';
									// tempDiv for css spinner - replaced upon receipt of get data
									$('#custodyDetailDataHolder').html(tempDiv);                                         
								
									xhr = $.get("instanceDetails?identificationID="+identificationID+"&amp;systemName="+systemName+"&amp;identificationSourceText="+identificationSourceText,function(data) {
										$('#custodyDetailDataHolder').html(data);
										
				  						var modalIframe = $("#modalIframe", parent.document);
				  						modalIframe.height(modalIframe.contents().find("body").height() + 16);
										
									}).fail(ojbc.displayCustodyDetailFailMessage);
								
								}).hover(function () {
										$(this).addClass("custodyDetailHover");
								}, function () {
										$(this).removeClass("custodyDetailHover");
								});
							});
						</script>
						
						<table id="custodyTable" class="detailsTable">
							<tr>
								<td class="detailsTitle">BOOKING NUMBER</td>
								<td class="detailsTitle">INFO OWNER</td>
								<td class="detailsTitle">BOOKING DATE</td>
							</tr>
							<xsl:apply-templates /> 
						</table>
						<div id="custodyDetailDataHolder"/>   
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="nc30:Person"/>
	
	<xsl:template match="cs-res-ext:CustodySearchResult">
		
		<xsl:variable name="systemSource"><xsl:text>{http://ojbc.org/Services/WSDL/Custody_Query_Request_Service/1.0}SubmitCustodyQueryRequest</xsl:text></xsl:variable>
		<tr systemName="Custody Detail" 
			identificationSourceText="{$systemSource}">
			<xsl:attribute name="identificationID"><xsl:value-of select="intel:SystemIdentification/nc:IdentificationID"/></xsl:attribute>
			
			<td>
				<xsl:value-of select="cs-res-ext:Booking/j:BookingAgencyRecordIdentification/nc:IdentificationID"/>
			</td>
			<td>
				<xsl:value-of select="cs-res-ext:InformationOwningOrganization/nc:OrganizationName"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="format-dateTime(cs-res-ext:Booking/nc:ActivityDate/nc:DateTime, '[M01]/[D01]/[Y0001]')"/>
			</td>
		</tr>		
	</xsl:template>
	<xsl:template match="srer:SearchRequestError">
		<span class="error">System Name: <xsl:value-of select="nc:SystemName" />, Error: <xsl:value-of select="srer:ErrorText"/></span><br />
	</xsl:template>
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">Access to System <xsl:value-of select="iad:InformationAccessDenyingSystemNameText" /> Denied. Denied Reason: <xsl:value-of select="iad:InformationAccessDenialReasonText"/></span><br />
	</xsl:template>
	<xsl:template match="srer:SearchResultsExceedThresholdError">
		<span class="error">System <xsl:value-of select="preceding-sibling::nc:SystemName" /> returned too many records. Record count <xsl:value-of select="srer:SearchResultsRecordCount"/></span><br />
	</xsl:template>
</xsl:stylesheet>
