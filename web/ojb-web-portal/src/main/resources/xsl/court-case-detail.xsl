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
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:rap="http://nlets.org/niem2/rapsheet/1.0"
    xmlns:ch-doc="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0"
    xmlns:ch-ext="http://ojbc.org/IEPD/Extensions/CriminalHistory/1.0"
    xmlns:ansi-nist="http://niem.gov/niem/ansi-nist/2.0"
    xmlns:screening="http://niem.gov/niem/domains/screening/2.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
	
    <xsl:output method="html" encoding="UTF-8" />

    <xsl:template match="/">
       <table>
            <tr>
                <td style="vertical-align: top;"><div class="bigPersonImage"></div></td>
                <td> 
                    <xsl:apply-templates /> 
                </td>                
            </tr>
       </table>
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					Court Case
				</td>
			</tr>
<!--        		<xsl:choose>
				<xsl:when test="//ch-ext:RapSheetCycle/rap:Supervision/nc:ActivityCategoryText[.='CUSTODY']">
					<xsl:call-template name="custodyTableRows"/>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td colspan="8">
							No Custody information available
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
 -->       </table>
 	</xsl:template>
</xsl:stylesheet>
