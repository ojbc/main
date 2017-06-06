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
    xmlns:warrant-ext="http://ojbc.org/IEPD/Extensions/Warrants/1.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:warrant="http://ojbc.org/IEPD/Exchange/Warrants/1.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    xmlns:qrm="http://ojbc.org/IEPD/Extensions/QueryResultsMetadata/1.0"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" 
    exclude-result-prefixes="#all">
	<xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />

	
    <xsl:template match="/">
    <xsl:variable name="accessDenied" select="warrant:Warrants/warrant-ext:eBWResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial[iad:InformationAccessDenialIndicator='true']"/>
    <xsl:choose>
    	<xsl:when test="$accessDenied">
    		<xsl:apply-templates select="$accessDenied"/>
    	</xsl:when>
    	<xsl:otherwise>
	    	<table style="width:100%">
	            <tr>
	                <td style="vertical-align: top;"><div class="bigPersonImage"></div></td>
	                <td> 
	                    <xsl:apply-templates /> 
	                </td>                
	            </tr>
	        </table>
	        <table class="detailsTable"> 
	             <tr>
	                <td colspan="8" class="detailsTitle">WARRANTS</td>
	            </tr>
	            <tr>
	            	<td id="warrantsHolderTD">
	            		<div id="warrants" style="overflow:auto; width:100%; height:auto">
	                       <xsl:call-template name="cycles" />
	                    </div>
	            	</td>
	            </tr>
	        </table>
    	</xsl:otherwise>
    </xsl:choose>
    </xsl:template>
    
    <xsl:template match="iad:InformationAccessDenial">
		<span class="error">User does not meet privilege requirements to access <xsl:value-of select="iad:InformationAccessDenyingSystemNameText"/>. To request access, contact your IT department.</span><br />
	</xsl:template>
    
    <xsl:template name ="cycles" >
		<script type="text/javascript" >
			$(function () {
				$("#warrantList").accordion({
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
		<div id="warrantList">
			<xsl:apply-templates select="/warrant:Warrants/warrant-ext:eBWResults/warrant-ext:eBWResult" />
		</div>
	</xsl:template>
	
	<xsl:template match="/warrant:Warrants/warrant-ext:eBWResults/warrant-ext:eBWResult">
		<h3><xsl:value-of select="concat('WARRANT ID: ', jxdm:Warrant/nc:ActivityIdentification/nc:IdentificationID)" /></h3>
		<div>
			<table style="width:100%">
	            <tr>
	                <td colspan="2" class="detailsLabel">ISSUE DATE</td>
	                <td colspan="2">
	                   <xsl:variable name="warrantDate" select="jxdm:Warrant/jxdm:CourtOrderIssuingDate/nc:Date"/>
	                    <xsl:value-of select="concat(substring($warrantDate,6,2),'/',substring($warrantDate,9,2),'/',substring($warrantDate,3,2))"/>
	                </td>
	                <td colspan="2" class="detailsLabel">BAIL AMOUNT</td>
	                <td colspan="2"><xsl:value-of select="format-number(jxdm:Bail/jxdm:BailSetAmount,'$#,###,###,###.00')" /></td>
	            </tr>
	            <tr>
	                <td colspan="2" class="detailsLabel">STATUS</td>
	                <td colspan="2"><xsl:value-of select="jxdm:Warrant/nc:ActivityStatus/nc:StatusDescriptionText" /></td>
	                <td colspan="2" class="detailsLabel">TYPE</td>
	                <td colspan="2"><xsl:value-of select="jxdm:Warrant/jxdm:WarrantLevelText" /></td>
	            </tr>
	            <tr>
	                <td colspan="2" class="detailsLabel">CASE NUMBER</td>
	                <td colspan="2"><xsl:value-of select="jxdm:Case/nc:ActivityIdentification/nc:IdentificationID" /></td>
	                <td colspan="2" class="detailsLabel">COURT</td>
	                <td colspan="2"><xsl:value-of select="jxdm:Warrant/jxdm:CourtOrderJurisdiction/nc:JurisdictionText" /></td>
	                
	            </tr>
	        </table>
      	</div>
    </xsl:template>
	
    <xsl:template match="warrant:Warrants/warrant-ext:eBWResults" >
        <table class="detailsTable">
            <tr>
                <td colspan="8" class="detailsFullName">
                <xsl:value-of select="concat(jxdm:Person/nc:PersonName/nc:PersonSurName,', ',jxdm:Person/nc:PersonName/nc:PersonGivenName,' ',jxdm:Person/nc:PersonName/nc:PersonMiddleName)" />
                </td>
            </tr>
            <tr>
                <td colspan="8" class="detailsTitle">SUBJECT DETAILS</td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">FBI#</td>
                <td colspan="2"><xsl:value-of select="jxdm:Person/jxdm:PersonAugmentation/jxdm:PersonFBIIdentification/nc:IdentificationID" /></td>
                <td colspan="2" class="detailsLabel">GENDER</td>
                
                
                <td colspan="2">
                <xsl:call-template name="formatSex" >
                	<xsl:with-param name="sexCode" select="jxdm:Person/nc:PersonSexCode" />
                </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SID/ISSUER</td>
                <td colspan="1"><xsl:value-of select="jxdm:Person/jxdm:PersonAugmentation/jxdm:PersonStateFingerprintIdentification/nc:IdentificationID" /></td>
                <td colspan="1"><xsl:value-of select="jxdm:Person/jxdm:PersonAugmentation/jxdm:PersonStateFingerprintIdentification/nc:IdentificationSourceText" /></td>
                <td colspan="2" class="detailsLabel">RACE</td>
                <td colspan="2">
                	<xsl:call-template name="formatRace" >
                		<xsl:with-param name="raceCode" select="jxdm:Person/nc:PersonRaceCode" />
                	</xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">DL#/ISSUER</td>
                <td colspan="1"><xsl:value-of select="jxdm:Person/jxdm:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationID" /></td>
                <td colspan="1"><xsl:value-of select="jxdm:Person/jxdm:PersonAugmentation/nc:DriverLicense/nc:DriverLicenseIdentification/nc:IdentificationSourceText" /></td>
                <td colspan="2" class="detailsLabel">HEIGHT</td>
                <td colspan="2">
                	<xsl:call-template name="formatHeight" >
                		<xsl:with-param name="heightInInches" select="jxdm:Person/nc:PersonHeightMeasure/nc:MeasurePointValue" />
                	</xsl:call-template>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">SSN</td>
                <td colspan="2"><xsl:value-of select="jxdm:Person/nc:PersonSSNIdentification/nc:IdentificationID"/></td>
                <td colspan="2" class="detailsLabel">WEIGHT</td>
                <td colspan="2"><xsl:value-of select="jxdm:Person/nc:PersonWeightMeasure/nc:MeasurePointValue" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">DOB</td>
                <td colspan="2">
                     <xsl:call-template name="formatDate">
						<xsl:with-param name="date" select="jxdm:Person/nc:PersonBirthDate/nc:Date" />
					</xsl:call-template>
                </td>
                <td colspan="2" class="detailsLabel">SCARS/MARKS/TATTOOS</td>
                <td colspan="2"><xsl:apply-templates select="jxdm:Person/nc:PersonPhysicalFeature" /></td>
            </tr>
            <tr>
                <td colspan="2" class="detailsLabel">RESIDENCE ADDRESS</td>
                <td colspan="6">   
                <xsl:choose>             
                    <xsl:when test="nc:Location/nc:LocationAddress/nc:AddressFullText">
                        <xsl:value-of select="nc:Location/nc:LocationAddress/nc:AddressFullText"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="sAddr" select="nc:Location/nc:LocationAddress/nc:StructuredAddress" />
                        <xsl:value-of select="concat($sAddr/nc:LocationStreet/nc:StreetNumberText, ' ', $sAddr/nc:LocationStreet/nc:StreetName, ', ',$sAddr/nc:LocationCityName,', ', $sAddr/nc:LocationStateNCICRESCode, ' ', $sAddr/nc:LocationPostalCode )"/>
                    </xsl:otherwise> 
                </xsl:choose>
                </td>
            </tr>
            
        </table>
    </xsl:template>
    <xsl:template match="nc:PersonPhysicalFeature">
		<p><xsl:value-of select="nc:PhysicalFeatureCategoryText"/><xsl:text> </xsl:text><xsl:value-of select="nc:PhysicalFeatureDescriptionText"/><xsl:text> </xsl:text><xsl:value-of select="nc:PhysicalFeatureLocationText"/></p>
	</xsl:template>
</xsl:stylesheet>
