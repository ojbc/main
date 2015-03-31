<?xml version="1.0" encoding="UTF-8"?>
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
    <xsl:template name="PersonFirearmRegistration">
       <table style="width:100%">
            <tr>
                <td style="vertical-align: top;"><div class="bigPersonImage"></div></td>
                <td> 
                    <xsl:apply-templates select="/firearm:PersonFirearmRegistrationQueryResults/nc:Person"/> 
                </td>                
            </tr>
        </table>
        <table class="detailsTable"> 
             <tr>
                <td colspan="8" class="detailsTitle">FIREARM REGISTRATIONS</td>
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