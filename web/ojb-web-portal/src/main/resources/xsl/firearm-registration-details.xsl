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

    <xsl:template name="FirearmRegistration">
    	<xsl:variable name="regSid" select="/firearm:FirearmRegistrationQueryResults/nc:PropertyRegistrationAssociation/nc:ItemRegistrationReference/@s:ref"/>
		<xsl:variable name="reg" select="//firearm-ext:ItemRegistration[@s:id=$regSid]" />
       <table style="width:100%">
       		<tr>
       			<td>
       				<h3><xsl:value-of select="concat('Firearm Registration Number: ', $reg/nc:RegistrationIdentification/nc:IdentificationID)" /></h3>
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
