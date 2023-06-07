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
    xmlns:pcq-res-doc="http://ojbc.org/IEPD/Exchange/ProsecutionCaseQueryResults/1.0"
    xmlns:pcq-res-ext="http://ojbc.org/IEPD/Extension/ProsecutionCaseQueryResults/1.0"
    xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" 
    xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
    xmlns:niem-xs="http://release.niem.gov/niem/proxy/xsd/3.0/" 
    xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all">
	
	<xsl:import href="_formatters.xsl" />
	
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:template match="/pcq-res-doc:ProsecutionCaseQueryResults">
		<script type="text/javascript">
			$(function () {
					var modalIframe = $("#modalIframe", parent.document);
					modalIframe.height(modalIframe.contents().find("body").height() + 16);
					
					$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
						var modalIframe = $("#modalIframe", parent.document);
						modalIframe.height(modalIframe.contents().find("body").height() + 16);
					})
					
					$('#partyTabs').tabs();
					$('.detailDataTable').DataTable({
 						"dom": 'rt',
 						responsive: true, 
 						colReorder: true 
					});
			});
		</script>

		<ul class="nav nav-tabs" id="myTab" role="tablist">
		  <li class="nav-item">
		    <a class="nav-link" id="detailsTab" data-toggle="tab" href="#details" role="tab" aria-controls="details" aria-selected="false">DETAILS</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="judgmentTab" data-toggle="tab" href="#judgment" role="tab" aria-controls="judgment" aria-selected="false">JUDGEMENT</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" id="sentencingTab" data-toggle="tab" href="#sentencing" role="tab" aria-controls="sentencing" aria-selected="false">SENTENCING</a>
		  </li>
		</ul>
		<div class="tab-content" id="myTabContent">
		  <div class="tab-pane fade show active" id="details" role="tabpanel" aria-labelledby="detailsTab">
		  	<p><xsl:apply-templates select="pcq-res-ext:ProsecutionCase" mode="details"/></p>
		  </div>
		  <div class="tab-pane fade" id="judgment" role="tabpanel" aria-labelledby="judgmentTab">
		  	<p><xsl:apply-templates select="pcq-res-ext:ProsecutionCase" mode="judgment"/></p>
		  </div>
		  <div class="tab-pane fade" id="sentencing" role="tabpanel" aria-labelledby="sentencingTab">
		  	<p><xsl:apply-templates select="pcq-res-ext:ProsecutionCase" mode="sentencing"/></p>
		  </div>
		</div>
	</xsl:template>
	
	<xsl:template match="pcq-res-ext:ProsecutionCase" mode="details">
        <table class="detailDataTable table table-striped table-bordered mt-2 nowrap" style="width:100%">
            <thead>
                <tr>
                    <th>Case Number</th>
                    <th>Status</th>
                    <th>Disposition</th>
                    <th>Disposition Final Date/Time</th>
                    <th>Prosecutor</th>
                    <th>Ref Agency</th>
                    <th>Referral Date</th>
                    <th>Stage Code</th>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="nc:Case" mode="details"/>
            </tbody>
        </table>
	</xsl:template>
	
   <xsl:template match="nc:Case" mode="details">
        <xsl:variable name="personAttorneyId"><xsl:value-of select="j:CaseAugmentation/j:CaseProsecutionAttorney/nc:RoleOfPerson/@structures:ref"/></xsl:variable>
        <tr>
            <td><xsl:value-of select="nc:CaseDocketID"/></td>
            <td><xsl:value-of select="nc:ActivityStatus/nc:StatusDescriptionText"/></td>
            <td><xsl:value-of select="nc:ActivityDisposition/nc:DispositionText"/></td>
            <td><xsl:value-of select="nc:ActivityDisposition/nc:DispositionDate/nc:DateTime"/></td>
            <td>
                <xsl:apply-templates select="ancestor::pcq-res-ext:ProsecutionCase/nc:Person[@structures:id= $personAttorneyId]/nc:PersonName" mode="primaryName"/>
            </td>
            <td><xsl:value-of select="pcq-res-ext:CaseReferral/nc:ReferralEntity/nc:EntityOrganization/nc:OrganizationName"/></td>
            <td><xsl:value-of select="pcq-res-ext:CaseReferral/nc:ActivityDate/nc:Date"/></td>
            <td><xsl:value-of select="pcq-res-ext:CaseStageCodeText"/></td>
        </tr>   
    </xsl:template>
	
	<xsl:template match="pcq-res-ext:ProsecutionCase" mode="judgment">
	   <xsl:text>judgment tab content </xsl:text>
	</xsl:template>
	<xsl:template match="pcq-res-ext:ProsecutionCase" mode="sentencing">
	   <xsl:text>sentencing tab content </xsl:text>
	</xsl:template>
</xsl:stylesheet>
