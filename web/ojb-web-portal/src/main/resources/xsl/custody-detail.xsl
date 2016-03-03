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

    Copyright 2012-2015 Open Justice Broker Consortium

-->
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:cq-res-doc="http://ojbc.org/IEPD/Exchange/CustodyQueryResults/1.0"
    xmlns:cq-res-ext="http://ojbc.org/IEPD/Extensions/CustodyQueryResultsExtension/1.0"
    xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
    xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
    xmlns:ac-bkg-codes="http://ojbc.org/IEPD/Extensions/AdamsCounty/BookingCodes/1.0"
    xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/"
    xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
    xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/"
    xmlns:cyfs="http://release.niem.gov/niem/domains/cyfs/3.1/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
    xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	
	<xsl:output method="html" encoding="UTF-8" />
		
	<xsl:template match="/cq-res-doc:CustodyQueryResults">
		<xsl:choose>
			<xsl:when test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript">
					$(function () {
						$('#custodyDetailTabs').tabs({
							activate: function( event, ui ) {
								var modalIframe = $("#modalIframe", parent.document);
								modalIframe.height(modalIframe.contents().find("body").height() + 16);
							}
						}); 
					
					});
				</script>
				<div id="custodyDetailTabs">
					<ul>
						<li>
							<a href="#bond">BOND</a>
						</li>
						<li>
							<a href="#booking">BOOKING</a>
						</li>
						<li>
							<a href="#nextCourtEvent">NEXT COURT EVENT</a>
						</li>
						<li>
							<a href="#charge">CHARGE</a>
						</li>
						<li>
							<a href="#detention">DETENTION</a>
						</li>
					</ul>
					
					<div id="bond">
						<p><xsl:apply-templates select="cq-res-ext:Custody/j:BailBond[@structures:id = parent::cq-res-ext:Custody/j:BailBondChargeAssociation/j:BailBond/@structures:ref]"/></p>	
					</div>
					<div id="booking">
						<p><xsl:apply-templates select="cq-res-ext:Custody/j:Booking[@structures:id= parent::cq-res-ext:Custody/j:ActivityChargeAssociation/nc:Activity/@structures:ref]"/></p>	
					</div>
					<div id="nextCourtEvent">
						<p>nextCourtEvent</p>
					</div>
					<div id="charge">
						<p>charge</p>
					</div>
					<div id="detention">
						<p>detention</p>
					</div>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xsl:template match="j:BailBond">
		<table class="detailTable">
			<tr>
				<th>
					<label>Bond Amount: </label>
					<xsl:value-of select="j:BailBondAmount/nc:Amount"></xsl:value-of>
				</th>
			</tr>
			<tr>
				<th>
					<label>Bond Type: </label>
					<xsl:value-of select="nc:ActivityCategoryText"></xsl:value-of>
				</th>
			</tr>
			<tr>
				<th>
					<label>Bond Status: </label>
					<xsl:value-of select="nc:ActivityStatus/nc:StatusDescriptionText"></xsl:value-of>
				</th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="j:Booking">
		<xsl:variable name="base64Image">
			<xsl:text>data:image/jpg;base64,</xsl:text>
			<xsl:value-of select="parent::cq-res-ext:Custody/nc:Person[@structures:id=parent::cq-res-ext:Custody/j:Booking/j:BookingSubject/nc:RoleOfPerson/@structures:ref]/nc:PersonDigitalImage/nc:Base64BinaryObject"></xsl:value-of>
		</xsl:variable>
		<table class="detailTable">
			<tr>
				<th>
					<label>Booking Number: </label>
					<xsl:value-of select="j:BookingSubject/j:SubjectIdentification/nc:IdentificationID"/>
				</th>
			</tr>
			<tr>
				<th>
					<label>Booking Date/Time: </label>
					<xsl:apply-templates select="nc:ActivityDate/nc:DateTime" mode="formatDateTime"/>
				</th>
			</tr>
			<tr>
				<th>
					<label>Booking Image: </label>
					<fo:external-graphic src="{$base64Image}"/>
				</th>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">System Name: <xsl:value-of select="intel:SystemIdentification/nc:SystemName" />, Error: <xsl:value-of select="qrer:ErrorText"/></span><br />
	</xsl:template>
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">Access to System <xsl:value-of select="iad:InformationAccessDenyingSystemNameText" /> Denied. Denied Reason: <xsl:value-of select="iad:InformationAccessDenialReasonText"/></span><br />
	</xsl:template>
</xsl:stylesheet>
