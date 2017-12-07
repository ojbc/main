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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wlq-res-doc="http://ojbc.org/IEPD/Exchange/WildlifeLicenseQueryResults/1.0"
	xmlns:wlq-res-ext="http://ojbc.org/IEPD/Extensions/WildlifeLicenseQueryResultsExtension/1.0" xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" xmlns:qrer="http://ojbc.org/IEPD/Extensions/QueryRequestErrorReporting/1.0"
	xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
	exclude-result-prefixes="#all">
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:variable name="primaryResidence"
		select="/wlq-res-doc:WildlifeLicenseQueryResults/wlq-res-ext:WildlifeLicenseReport/nc:Location[@structures:id=../nc:PersonResidenceAssociation/nc:Location/@structures:ref]/nc:Address" />
	<xsl:variable name="secondaryResidence"
		select="/wlq-res-doc:WildlifeLicenseQueryResults/wlq-res-ext:WildlifeLicenseReport/nc:Location[@structures:id=../wlq-res-ext:PersonSecondaryResidenceAssociation/nc:Location/@structures:ref]/nc:Address" />
	<xsl:template match="/">
		<div>
			<table class="detailsTable">
				<tr>
					<td colspan="8" class="detailsTitle">PRIMARY RESIDENCE</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">CITY</td>
					<td colspan="6">
						<xsl:value-of select="$primaryResidence/nc:LocationCityName" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">GEO Code</td>
					<td colspan="6">
						<xsl:value-of
							select="/wlq-res-doc:WildlifeLicenseQueryResults/wlq-res-ext:WildlifeLicenseReport/nc:Location[@structures:id=../nc:PersonResidenceAssociation/nc:Location/@structures:ref]/wlq-res-ext:GeoCode" />
					</td>
				</tr>
				<tr>
					<td colspan="8" class="detailsTitle">SECONDARY RESIDENCE (non-Legal)</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">STREET ADDRESS</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/nc:LocationStreet/nc:StreetFullText" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">CITY</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/nc:LocationCityName" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">STATE</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/j:LocationStateNCICLISCode" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">ZIP CODE</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/nc:LocationPostalCode" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">ZIP EXT</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/nc:LocationPostalExtensionCode" />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="detailsLabel">COUNTRY</td>
					<td colspan="6">
						<xsl:value-of select="$secondaryResidence/nc:LocationCountryName" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>