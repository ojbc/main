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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cq-res-doc="http://ojbc.org/IEPD/Exchange/CustodyQueryResults/1.0"
	xmlns:cq-res-ext="http://ojbc.org/IEPD/Extensions/CustodyQueryResultsExtension/1.0"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:ac-bkg-codes="http://ojbc.org/IEPD/Extensions/AdamsCounty/BookingCodes/1.0"
	xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/"
	xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:intel="http://release.niem.gov/niem/domains/intelligence/3.1/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:cyfs31="http://release.niem.gov/niem/domains/cyfs/3.1/"
	exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/cq-res-doc:CustodyQueryResults">
		<xsl:choose>
			<xsl:when
				test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates
					select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates
					select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript">
					$(function () {												
						$('#accordion').accordion({
							heightStyle: "content",
							active: false,
							collapsible: true,
		  					activate: function( event, ui ) { 
		  						var modalIframe = $("#modalIframe", parent.document);
		  						modalIframe.height(modalIframe.contents().find("body").height() + 16);
		  					}							
						});						
					});
				</script>			
				
				<div id="custodyDetailOutsideDiv" style="overflow:auto; width:100%; height:auto">															
					<xsl:apply-templates select="cq-res-ext:Custody/j:Booking"/>										
					<div id="accordion">		
						<xsl:apply-templates select="cq-res-ext:Custody/j:Arrest"/> 				  
					</div>				
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="j:Booking">
		<table class="detailTable">
			<tr>
				<th>
					<label>Subject ID: </label>
					<xsl:value-of
						select="j:BookingSubject/j:SubjectIdentification/nc:IdentificationID" />
				</th>
				<th>
					<label>Supervision Status: </label>
					<xsl:if test="normalize-space(../nc:Person/j:PersonAugmentation/j:PersonProbationerIndicator) = 'true'">probation</xsl:if>
					<xsl:if test="normalize-space(../nc:Person/cq-res-ext:PersonIncarceratedIndicator) = 'true'">incarcerated</xsl:if>
				</th>				
			</tr>
			<tr>
				<th>
					<label>Scheduled Release Date: </label>
					<xsl:value-of
						select="../j:Detention/j:SupervisionAugmentation/j:SupervisionReleaseEligibilityDate/nc:Date" />
				</th>
				<th>
					<label>Actual Release Date: </label>
					<xsl:value-of select="../nc:Release/nc:ActivityDate/nc:DateTime" />
				</th>
			</tr>
			<tr>
				<th>
					<label>Detention Facility ID: </label>
					<xsl:value-of
						select="j:BookingDetentionFacility/nc:FacilityIdentification/nc:IdentificationID" />
				</th>
				<th>
					<label>Allow Account Deposit: </label>
					<xsl:value-of
						select="../j:Detention/cq-res-ext:AllowAccountDepositIndicator" />
				</th>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="j:Arrest">
		<h4>
			Charges:
			<xsl:value-of select="j:ArrestAgency/nc:OrganizationName" />
		</h4>
		<div>
			<xsl:apply-templates select="j:ArrestCharge" />
		</div>
	</xsl:template>
	<xsl:template match="j:ArrestCharge">
		<xsl:variable name="chargeID" select="@structures:ref" />
		<xsl:apply-templates
			select="/cq-res-doc:CustodyQueryResults/cq-res-ext:Custody/j:Charge[@structures:id = $chargeID]" />
	</xsl:template>
	<xsl:template match="j:Charge">
		<h4>Charge</h4>
		<table class="detailTable">
			<tr>
				<th>
					<label>Sequence Number: </label>
					<xsl:value-of select="j:ChargeSequenceID" />
				</th>
			</tr>
			<tr>
				<th>
					<label>Description: </label>
					<xsl:value-of select="j:ChargeDescriptionText" />
				</th>
			</tr>
			<tr>
				<th>
					<label>Statute Code Section/Ordinance: </label>
					<xsl:value-of
						select="j:ChargeStatute/j:StatuteCodeSectionIdentification/nc:IdentificationID" />
				</th>
			</tr>
			<tr>
				<th>
					<label>Category: </label>
					<xsl:value-of select="j:ChargeCategoryDescriptionText" />
				</th>
			</tr>
			<tr>
				<th>
					<label>Sentence Date: </label>
					<xsl:value-of select="j:ChargeSentence/nc:ActivityDate/nc:Date" />
				</th>
			</tr>
			<xsl:variable name="chargeId" select="@structures:id" />
			<xsl:variable name="eventId"
				select="/cq-res-doc:CustodyQueryResults/cq-res-ext:Custody/j:ActivityChargeAssociation[j:Charge/@structures:ref = $chargeId]/nc:Activity/@structures:ref" />
		</table>
	</xsl:template>
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">
			System Name:
			<xsl:value-of select="intel:SystemIdentification/nc:SystemName" />
			, Error:
			<xsl:value-of select="qrer:ErrorText" />
		</span>
		<br />
	</xsl:template>
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">
			Access to System
			<xsl:value-of select="iad:InformationAccessDenyingSystemNameText" />
			Denied. Denied Reason:
			<xsl:value-of select="iad:InformationAccessDenialReasonText" />
		</span>
		<br />
	</xsl:template>
</xsl:stylesheet>