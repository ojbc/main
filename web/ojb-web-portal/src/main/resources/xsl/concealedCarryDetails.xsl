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
       <div class="row">
           	<div class="col-12 table-responsive"> 
               <xsl:call-template name="personInformation"/>
            </div>                
       </div>
	       
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					LICENSE INFORMATION
				</td>
			</tr>
            <tr>
                <td colspan="2" class="detailsLabel">LIC NO.</td>
                <td colspan="6">A123456</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">COUNTY</td>
                <td colspan="2">HONOLULU</td>
                <td colspan="2" class="detailsLabel">STATUS</td>
                <td colspan="2">APPROVED</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">LIC DATE</td>
                <td colspan="2">5/5/2023</td>
                <td colspan="2" class="detailsLabel">STATUS DATE</td>
                <td colspan="2">5/5/2023</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">LIC TYPE</td>
                <td colspan="2">CONCEALED</td>
                <td colspan="2" class="detailsLabel">AGE AT LIC</td>
                <td colspan="2">48</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">NOTES</td>
                <td colspan="6"></td>
            </tr>
       </table>
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					FIREARM INFORMATION
				</td>
			</tr>
            <tr>
                <td colspan="2" class="detailsLabel">FACTORY SERIAL NO.</td>
                <td colspan="2">G123456</td>
                <td colspan="2" class="detailsLabel">GAUGE/CALIBER</td>
                <td colspan="2">.270 WINCHESTER .270 WEATHERBY</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">MAKE</td>
                <td colspan="2">SAVAGE</td>
                <td colspan="2" class="detailsLabel">ACTION</td>
                <td colspan="2">I</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">MODEL</td>
                <td colspan="2">110</td>
                <td colspan="2" class="detailsLabel">REG NO.</td>
                <td colspan="2">G123456</td>
            </tr>
       </table>
   
       
       <table class="detailsTable">
			<tr>
				<td colspan="8" class="detailsTitle">
					FIRARMS PROFICIENCY
				</td>
			</tr>
            <tr>
                <td colspan="2" class="detailsLabel">DMEAKMEONSTRATED ON</td>
                <td colspan="2">5/5/2021</td>
                <td colspan="2" class="detailsLabel">EXAMINEDI BY</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">INSTRUCTOR NO.</td>
                <td colspan="2">123456</td>
                <td colspan="2" class="detailsLabel">LOCATION</td>
                <td colspan="2">LOCATION</td>
            </tr>
       </table>
        
    </xsl:template>
    
    
    <xsl:template name="personInformation" >
        <table class="detailsTable table">
            <tr>
            	<td colspan="8" class="detailsFullName">
           			Concealed Carray Number: A123456
           		</td>
            </tr>
            <tr>
                <td colspan="8" class="detailsTitle">PERSON INFORMATION</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">NAME</td>
                <td colspan="2">JOHN DOE</td>
                <td colspan="2" class="detailsLabel">DOB</td>
                <td colspan="2">
                    10/15/1972
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">OCCUPATION</td>
                <td colspan="2">Hunter</td>	
                
                <td colspan="2" class="detailsLabel">SEX</td>
                <td colspan="2">Mail</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">RES ADDRESS</td>
            	<td colspan="2">Residence Address</td>        	
                
                <td colspan="2" class="detailsLabel">Race</td>
                <td colspan="2">WHITE/CAUCASIAN</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">BUS ADDRESS</td>
                <td colspan="2">
                </td>
                <td colspan="2" class="detailsLabel">HEIGHT</td>
                <td colspan="2">6'8"</td>
            </tr>
            <tr>
            	<td colspan="2" class="detailsLabel">RES PHONE</td>
                <td colspan="2">
                </td>
                <td colspan="2" class="detailsLabel">WEIGHT</td>
                <td colspan="2">215</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SSN</td>
               <td colspan="2">
                </td>
                <td colspan="2" class="detailsLabel">HAIR</td>
                <td colspan="2">BROWN</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">PLACE OF BIRTH</td>
                <td colspan="2">WAIALUA, HI</td>
                <td colspan="2" class="detailsLabel">COMPLEXION</td>
                <td colspan="2">
                	FAIR
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">DL#/ISSUER</td>
                <td colspan="2"></td>
                <td colspan="2" class="detailsLabel">STATE ID</td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">CURRENT CITIZENSHIP</td>
                <td colspan="2">1</td>
                <td colspan="2" class="detailsLabel">LEOSA</td>
                <td colspan="2"></td>
            </tr>  
            <tr>
                <td colspan="2" class="detailsLabel">COMMENTS</td>
                <td colspan="6"></td>
            </tr>  
        </table>
        
    </xsl:template>
	
</xsl:stylesheet>
