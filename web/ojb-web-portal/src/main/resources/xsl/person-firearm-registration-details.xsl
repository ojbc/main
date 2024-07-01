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
    <xsl:output method="html" encoding="UTF-8" />
    <xsl:template name="PersonFirearmRegistration">
        <xsl:param name="detailsTitle">FIREARM REGISTRATIONS</xsl:param>
       <div class="row">
          <div class="col-12 col-sm-3 align-top"><div class="bigPersonImage"></div></div>
          <div class="col-12 col-sm-9 table-responsive">
              <xsl:apply-templates select="/firearm:PersonFirearmRegistrationQueryResults/nc:Person"/> 
          </div>                
        </div>
        <table class="detailsTable"> 
             <tr>
                <td colspan="8" class="detailsTitle"><xsl:value-of select="$detailsTitle"/></td>
            </tr>
            <tr>
            	<td id="firearmRegistrationsHolderTD">
            		<div id="firearmRegistrations" style="overflow:auto; width:100%; height:auto">
                       <xsl:call-template name="personRegistrations" />
                    </div>
            	</td>
            </tr>
        </table>
    </xsl:template>
    
    <xsl:template name ="personRegistrations" >
		<script type="text/javascript" >
			$(function () {
				$("#registrationList").accordion({
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
		<div id="registrationList">
			<xsl:apply-templates select="/firearm:PersonFirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation"/>
		</div>
	</xsl:template>  
</xsl:stylesheet>