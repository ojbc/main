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
    xmlns:jxdm="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:usps-states="http://niem.gov/niem/usps_states/2.0"
    xmlns:error="http://ojbc.org/IEPD/Extensions/PersonQueryErrorReporting/1.0"
    xmlns:firearm-ext="http://ojbc.org/IEPD/Extensions/FirearmRegistrationQueryResults/1.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:firearm="http://ojbc.org/IEPD/Exchange/FirearmRegistrationQueryResults/1.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    exclude-result-prefixes="#all">
    <xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />

    <xsl:template name="FirearmRegistration">
        <xsl:param name="regNumberLabel" >Firearm Registration Number: </xsl:param>
    	<xsl:variable name="regSid" select="/firearm:FirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation/nc:ItemRegistrationReference/@s:ref"/>
		<xsl:variable name="reg" select="//firearm-ext:ItemRegistration[@s:id=$regSid]" />
       <table style="width:100%">
       		<tr>
       			<td>
       				<h3><xsl:value-of select="concat($regNumberLabel, $reg/nc:RegistrationIdentification/nc:IdentificationID)" /></h3>
       			</td>
       		</tr>
            <tr>
<!--                 <td style="vertical-align: top;"><div class="bigPersonImage"></div></td> -->
                <td> 
                   <xsl:apply-templates select="/firearm:FirearmRegistrationQueryResults/nc:Person"/> 
                </td>                
            </tr>
        </table>
        <table style="width:100%">
            <tr>
            	<td id="firearmRegistrationsHolderTD">
                      <xsl:apply-templates select="/firearm:FirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation"/>
            	</td>
            </tr>
        </table>
        
    </xsl:template>
</xsl:stylesheet>
