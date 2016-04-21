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
	xmlns:vcq-res-doc="http://ojbc.org/IEPD/Exchange/VehicleCrashQueryResults/1.0"
	xmlns:vcq-res-ext="http://ojbc.org/IEPD/Extensions/VehicleCrashQueryResultsExtension/1.0"
    xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
    xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
    xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
    xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/"
    xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	
    <xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="/">
		<table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle"></td>
			</tr>
			<tr>
				<td class="padding0">
					<div id="warrants" style="overflow:auto; width:100%; height:auto">
						<xsl:apply-templates select="vcq-res-doc:VehicleCrashQueryResults" />
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="vcq-res-doc:VehicleCrashQueryResults">
		<xsl:choose>
			<xsl:when test="qrm:QueryResultsMetadata/qrer:QueryRequestError | qrm:QueryResultsMetadata/iad:InformationAccessDenial">
				<xsl:apply-templates select="qrm:QueryResultsMetadata/qrer:QueryRequestError" />
				<xsl:apply-templates select="qrm:QueryResultsMetadata/iad:InformationAccessDenial" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript" >
					$(function () {
						$("#vihicleCrashDetail").accordion({
							heightStyle: "content",			
							active: false,
		  					collapsible: true,
		  					activate: function( event, ui ) { 
		  						var modalIframe = $("#modalIframe", parent.document);
		  						modalIframe.height(modalIframe.contents().find("body").height() + 16);
		  					}
						} );
					});
				</script>
				<div id="vihicleCrashDetail">
					<xsl:apply-templates select="vcq-res-ext:VehicleCrashReport"/>
				</div>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="vcq-res-ext:VehicleCrashReport">
		<xsl:apply-templates select="j:Crash" mode="crashDetail"></xsl:apply-templates>
		<xsl:apply-templates select="j:Crash/j:IncidentAugmentation" mode="damagedItem"/>
      	<h3>Crash Vehicle</h3>
      	<div>crash vehicle</div>
      	<h3>Work Zone</h3>
      	<div>work zone</div>
      	<h3>Witness</h3>
      	<div>witness</div>
	</xsl:template>
	
	<xsl:template match="j:IncidentAugmentation" mode="damagedItem">
		<h3>Damaged Item(s)</h3>
		<div>
			<table style="width:100%">
				<xsl:apply-templates select="j:IncidentDamagedItem"/>
			</table>	
		</div>
		
	</xsl:template>
		 
	<xsl:template match="j:IncidentDamagedItem">
		<tr>
			<td class="detailsLabel">Item</td>
			<td><xsl:value-of select="nc:ItemDescriptionText"/></td>
			<td class="detailsLabel">Owner</td>
			<td><xsl:apply-templates select="nc:ItemOwner"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="nc:ItemOwner">
		<xsl:for-each select="nc:EntityOrganization|nc:EntityPerson">
			<xsl:if test="position() > 1"><xsl:text>, </xsl:text></xsl:if>
			<xsl:variable name="ownerId">
				<xsl:value-of select="./@structures:ref"></xsl:value-of>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="local-name()='EntityOrganization'">
					<xsl:value-of select="ancestor::vcq-res-ext:VehicleCrashReport/nc:EntityOrganization[@structures:id=$ownerId]/nc:OrganizationName"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="ancestor::vcq-res-ext:VehicleCrashReport/nc:EntityPerson[@structures:id=$ownerId]/nc:PersonName/nc:PersonFullName"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="j:Crash" mode="crashDetail">
		<h3>Crash Detail</h3>
		<div>
			<table style="width:100%">
				<tr>
					<td class="detailsLabel">DATE</td>
					<td><xsl:apply-templates select="nc:ActivityDate/nc:DateTime" mode="formatDateTime"/>
					</td>
					<td class="detailsLabel">Crash Report Number</td>
					<td><xsl:value-of select="parent::vcq-res-ext:VehicleCrashReport/nc:DocumentIdentification/nc:IdentificationID"/></td>
				</tr>
				<tr>
					<td class="detailsLabel">Reporting Officer/Badge ID</td>
					<td>
						<xsl:value-of select="parent::vcq-res-ext:VehicleCrashReport/nc:Person[@structures:id=ancestor::vcq-res-ext:VehicleCrashReport/j:Crash/j:IncidentAugmentation/j:IncidentReportingOfficial/nc:RoleOfPerson/@structures:ref]/nc:PersonName/nc:PersonFullName"/>/
						<xsl:value-of select="j:IncidentAugmentation/j:IncidentReportingOfficial/j:EnforcementOfficialBadgeIdentification/nc:IdentificationID"/>
					</td>
					<td class="detailsLabel">Reporting ORI</td>
					<td>
						<xsl:value-of select="j:CrashInformationSource/j:OrganizationORIIdentification/nc:IdentificationID"></xsl:value-of>
					</td>
				</tr>
				<tr>
					<td class="detailsLabel">Location</td>
					<td>
						<xsl:value-of select="nc:Location/nc:Address/nc:LocationCityName"/>
						<xsl:text> </xsl:text>
						<xsl:value-of select="nc:Location/nc:AddressHighway/nc:HighwayFullText"></xsl:value-of>
					</td>
					<td class="detailsLabel">Coordinates</td>
					<td>
						<xsl:value-of select="nc:Location/nc:Location2DGeospatialCoordinate/nc:GeographicCoordinateLatitude/nc:LatitudeDegreeValue"/>
						<xsl:text>, </xsl:text>
						<xsl:value-of select="nc:Location/nc:Location2DGeospatialCoordinate/nc:GeographicCoordinateLongitude/nc:LongitudeDegreeValue"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	
	<xsl:template match="qrer:QueryRequestError">
		<span class="error">Vehicle Crash Query Result Error: <xsl:value-of select="qrer:ErrorText"/></span><br />
	</xsl:template>
	
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">Access to System <xsl:value-of select="iad:InformationAccessDenyingSystemNameText" /> Denied. Denied Reason: <xsl:value-of select="iad:InformationAccessDenialReasonText"/></span><br />
	</xsl:template>
</xsl:stylesheet>
