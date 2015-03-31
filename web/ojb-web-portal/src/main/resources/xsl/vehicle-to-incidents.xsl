<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exchange="http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0"
    xmlns:ext="http://ojbc.org/IEPD/Extensions/IncidentSearchResults/1.0"
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" 
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0" 
    xmlns:s="http://niem.gov/niem/structures/2.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:usps="http://niem.gov/niem/usps_states/2.0" 
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    exclude-result-prefixes="#all"
>
    <xsl:output method="html" encoding="UTF-8" />


    <xsl:template match="/">
   		<xsl:choose>
    		<xsl:when test="//srm:SearchResultsMetadata/srer:SearchRequestError">
    			<table id="incidentsError" class="detailsTable">
		            <tr>
		                <td class="detailsTitle" >INCIDENTS ERROR</td>
		            </tr>
		            <tr>
			            <td>
			            	<xsl:apply-templates select="//srm:SearchResultsMetadata/srer:SearchRequestError" /> 
			            </td>
		            </tr>
		        </table>
    		</xsl:when>
    		<xsl:when test="not(//srm:SearchResultsMetadata/srer:SearchRequestError) and count(exchange:IncidentVehicleSearchResults/ext:IncidentVehicleSearchResult) = 0">
    			<table id="incidentsError" class="detailsTable">
		            <tr>
		                <td class="detailsTitle" >NO ASSOCIATED INCIDENTS</td>
		            </tr>
		            <tr>
			            <td>
			            	<span class="error">There are no incidents associated with this vehicle record.</span><br /> 
			            </td>
		            </tr>
		        </table>
    		</xsl:when>
    		<xsl:otherwise>
		        <script type="text/javascript">
		            $(function () {
		                $('#incidentsSummary tr.clickableIncident').click(function () {
		                
		                    var systemName =$(this).attr('systemName');
		                    var identificationSourceText = $(this).attr('identificationSourceText');
		                    var identificationID = $(this).attr('identificationID');
		                    
		                    
		                    $('#incidentsSummary tr').removeClass("incidentSelected");
		                    $(this).addClass("incidentSelected");
		                    
		                    var tempDiv = '<div id="incidentDetailTemp" style="height:50%;width:100%"/>';
		                    // tempDiv for css spinner - replaced upon receipt of get data
		                    $('#incidentDetailTabsHolder').html(tempDiv);                                         
		                    
		                    $.get("incidentDetails?identificationID="+identificationID+"&amp;systemName="+systemName+"&amp;identificationSourceText="+identificationSourceText,function(data) {
		                      $('#incidentDetailTabsHolder').html(data);
		                    }).fail(ojbc.displayIncidentDetailFailMessage);
		                    
		                }).hover(function () {
		                     $(this).addClass("incidentHover");
		                }, function () {
		                     $(this).removeClass("incidentHover");
		                });
		            });
		        </script>
		       
		        <table id="incidentsSummary" class="detailsTable">
		            <tr>
		                <td class="detailsTitle" >TYPE/NATURE</td>
		                <td class="detailsTitle">ROLE</td>
		                <td class="detailsTitle">INCIDENT #</td>
		                <td class="detailsTitle">AGENCY</td>
		                <td class="detailsTitle">DATE</td>
		                <td class="detailsTitle">LOCATION</td>
		            </tr>
		            <xsl:apply-templates /> 
		        </table>
		        <div id="incidentDetailTabsHolder"></div>   
	        </xsl:otherwise>
        </xsl:choose> 
    </xsl:template>
    
    <xsl:template match="exchange:IncidentVehicleSearchResults/ext:IncidentVehicleSearchResult" >
        <tr class="clickableIncident"
            systemName="{intel:SystemIdentifier/intel:SystemName}"
            identificationSourceText="{ext:SourceSystemNameText}"   
        >
        	<xsl:attribute name="identificationID"><xsl:text>{</xsl:text><xsl:value-of select="ext:Incident/ext:IncidentCategoryCode"/><xsl:text>}</xsl:text><xsl:value-of select="intel:SystemIdentifier/nc:IdentificationID"/></xsl:attribute>
        
            <td><xsl:value-of select="ext:Incident/ext:IncidentCategoryCode"/></td>
            <td><xsl:value-of select="normalize-space(ext:IncidentInvolvedVehicleInvolvementDescriptionText)"/></td>
            <td><xsl:value-of select="ext:Incident/nc:ActivityIdentification/nc:IdentificationID"/></td>
            <td><xsl:value-of select="nc:Organization/nc:OrganizationName"/></td>
            <td>
            
            <xsl:if test="normalize-space(ext:Incident/nc:ActivityDate/nc:DateTime) !=''">
            	<xsl:variable name="date" select="ext:Incident/nc:ActivityDate/nc:DateTime"/>
            	<xsl:value-of select="concat(substring($date, 6, 2),'-',substring($date, 9, 2),'-',substring($date, 1, 4))" />
            </xsl:if>
            <xsl:if test="normalize-space(ext:Incident/nc:ActivityDate/nc:Date) !=''">
            	<xsl:variable name="date" select="ext:Incident/nc:ActivityDate/nc:Date"/>
            	<xsl:value-of select="concat(substring($date, 6, 2),'-',substring($date, 9, 2),'-',substring($date, 1, 4))" />
            </xsl:if>
            
            </td>
            <td>
           	  <xsl:apply-templates select="nc:Location/nc:LocationAddress/ext:StructuredAddress"/>
            </td>
        </tr>    
    </xsl:template>
    
    <xsl:template match="ext:StructuredAddress">
    	<xsl:variable name="address" select="."/>
   		<xsl:variable name="city">
       		<xsl:choose>
	       		<xsl:when test="$address/*[local-name() = 'LocationCityTownCode'][. !='']">
	       			<xsl:value-of select="$address/*[local-name() = 'LocationCityTownCode']"/>
	       		</xsl:when>
	       		<xsl:when test="$address/nc:LocationCityName[. != '']">
	       			<xsl:value-of select="$address/nc:LocationCityName"/>
	       		</xsl:when>
       		</xsl:choose>
        </xsl:variable>
		<xsl:choose>
			<xsl:when test="nc:LocationStreet/nc:StreetFullText != ''">
				<xsl:value-of select="concat(nc:LocationStreet/nc:StreetFullText,' ',nc:AddressSecondaryUnitText,' ', $city,', ',nc:LocationStateUSPostalServiceCode, ' ', nc:LocationPostalCode)" />
			</xsl:when>
			<xsl:otherwise>
		    	<xsl:value-of select="concat(nc:LocationStreet/nc:StreetNumberText,' ',nc:LocationStreet/nc:StreetName,' ',nc:AddressSecondaryUnitText,' ', $city,', ',nc:LocationStateUSPostalServiceCode, ' ', nc:LocationPostalCode)" />
			</xsl:otherwise>
		</xsl:choose>
   </xsl:template>
    
    <xsl:template match="srer:SearchRequestError">
    	<span class="error">System Name: <xsl:value-of select="intel:SystemName" />, Error: <xsl:value-of select="srer:ErrorText"/></span><br />
    </xsl:template>
</xsl:stylesheet>
