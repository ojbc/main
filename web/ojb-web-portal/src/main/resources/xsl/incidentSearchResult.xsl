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
	xmlns:ext="http://nij.gov/IEPD/Extensions/EntityMergeResultMessageExtensions/1.0"
	xmlns:ext1="http://ojbc.org/IEPD/Extensions/IncidentSearchResults/1.0"
	xmlns:exc="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0"
	xmlns:s="http://niem.gov/niem/structures/2.0" 
    xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    exclude-result-prefixes="#all">
    
    <xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="hrefBase" />
	<xsl:param name="purpose" />
	<xsl:param name="onBehalfOf" />
	<xsl:param name="incidentTypesToDrillDown" />

    <xsl:template match="/exc:EntityMergeResultMessage">
    	<xsl:variable name="totalCount" select="count(exc:MergedRecords/ext:MergedRecord)" />
    	<xsl:variable name="entityContainer" select="count(exc:EntityContainer)" />
    	<xsl:variable name="accessDenialReasons" select="exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/iad:InformationAccessDenial" />
    	<xsl:variable name="requestErrors" select="exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/srer:SearchRequestError" />
    	<xsl:variable name="tooManyResultsErrors" select="exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/srer:SearchErrors/srer:SearchResultsExceedThresholdError" />
    	    	
		<xsl:apply-templates select="$accessDenialReasons" />
		<xsl:apply-templates select="$requestErrors" />
		<xsl:apply-templates select="$tooManyResultsErrors" />

   		<xsl:if test="exc:RecordLimitExceededIndicator='true'">
   			<div class="alert alert-warning" role="alert">Unable to perform Entity Resolution. The search returned too many records.</div>
   		</xsl:if>

        <table class="searchResultsTable table table-striped table-bordered nowrap" style="width:100%" id="searchResultsTable">	
        	<thead>
		        <tr>
    				<th>ENTITY</th>
	                <th>TYPE/NATURE</th>
	                <th>ROLE</th>
	                <th>INCIDENT #</th>
	                <th>AGENCY</th>
	                <th>DATE</th>
	                <th>LOCATION</th>
	                <th>SYSTEM</th>
	                <th class="d-none"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
	            </tr>
            </thead>
            <tbody>
		    	<xsl:if test="($totalCount &gt; 0)">
			        <xsl:apply-templates select="exc:EntityContainer/ext:Entity"/>
		    	</xsl:if>
	        </tbody>
        </table>
    </xsl:template>

    <!-- this will print a "merge" on the results screen -->
    <xsl:template match="ext:Entity">
    	<xsl:variable name="originalEntityID" select="@s:id" />
    	<xsl:variable name="position" select="position()"/>
    	<!--If E’ is merged with any of the preceding-siblings of E’, then skip it.  Otherwise, write it out. -->
		<xsl:choose>
			<!-- Find the MergedRecord that pertains to E’.  Call this MergedRecord MR’. -->
			<xsl:when test="preceding-sibling::ext:Entity[@s:id = //ext:MergedRecord[ext:OriginalRecordReference/@s:ref=$originalEntityID]/ext:OriginalRecordReference/@s:ref]">
				<!-- do nothing on purpose -->
			</xsl:when>
    		<xsl:otherwise>
		    	<xsl:variable name="previouslyMergedEntityCount" select="count(//ext:MergedRecord[ext:OriginalRecordReference/@s:ref=//ext:Entity[position() &lt; $position]/@s:id])"/>
  				<xsl:call-template name="IncidentRecord">
		            <xsl:with-param name="incidentId" select="@s:id" />
		     		<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
		        </xsl:call-template>
	            <xsl:for-each select="following-sibling::ext:Entity">
						<xsl:variable name="id" select="@s:id" />
						<xsl:if test="//ext:MergedRecord[ext:OriginalRecordReference/@s:ref = $id and ext:OriginalRecordReference/@s:ref = $originalEntityID]">
							<xsl:call-template name="IncidentRecord">
	                    		<xsl:with-param name="incidentId" select="$id" />
					     		<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
	               		 </xsl:call-template>
						</xsl:if>
				</xsl:for-each>
	     	 </xsl:otherwise>
     	 </xsl:choose>
    </xsl:template>

    <xsl:template name="IncidentRecord">
        <xsl:param name="incidentId" />
       	<xsl:param name="entityCount"/>
        
        <xsl:for-each select="/exc:EntityMergeResultMessage/exc:EntityContainer/ext:Entity[@s:id = $incidentId]/ext1:IncidentSearchResult">
            <xsl:variable name="incident" select="ext1:Incident"/>    
            <tr>
            	<xsl:if test="contains($incidentTypesToDrillDown, $incident/ext1:IncidentCategoryCode)">
            		<xsl:attribute name="class">clickableIncident</xsl:attribute>
            	</xsl:if>		    			    
            	
                <td><xsl:value-of select="$entityCount"/></td>
                <td><xsl:value-of select="$incident/ext1:IncidentCategoryCode" /></td>
                <td><!-- Role goes here --></td>
                <td><xsl:value-of select="$incident/nc:ActivityIdentification/nc:IdentificationID" /></td>
                <td><xsl:value-of select="nc:Organization/nc:OrganizationName" /></td>
                <td>
                    <xsl:call-template name="formatDate">
						<xsl:with-param name="date" select="$incident/nc:ActivityDate/nc:DateTime" />
					</xsl:call-template>
                </td>
                <td>
                	<xsl:variable name="address" select="nc:Location/nc:LocationAddress/ext1:StructuredAddress"/>
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
                	
                	<xsl:value-of select="concat($address/nc:LocationStreet/nc:StreetNumberText, ' ', $address/nc:LocationStreet/nc:StreetName, ' ', $city, ', ', $address/nc:LocationStateUSPostalServiceCode)" />
                </td>
                <td><xsl:value-of select="intel:SystemIdentifier/intel:SystemName" /></td>
                
                <td class="d-none">
                    <xsl:variable name="systemSource"><xsl:value-of select="normalize-space(ext1:SourceSystemNameText)"/></xsl:variable>
                    <xsl:variable name="queryType"><xsl:text>Incident</xsl:text></xsl:variable>
                    <a href="{concat('../incidents/incidentDetails?identificationID=','{',$incident/ext1:IncidentCategoryCode,'}',intel:SystemIdentifier/nc:IdentificationID , '&amp;systemName=Incident System&amp;identificationSourceText=',$systemSource,'&amp;purpose=',$purpose,'&amp;onBehalfOf=',$onBehalfOf,'&amp;queryType=',$queryType)}" 
                        class="btn btn-primary btn-sm viewDetails" searchName='{intel:SystemIdentifier/intel:SystemName} Detail' 
                        
                            appendPersonData="{concat('personalInformation-',$incidentId)}"
                        >DETAILS</a>
                    
                </td>
            </tr>
        </xsl:for-each>
    </xsl:template>
    
	<xsl:template match="iad:InformationAccessDenial">
		<span class="error">User does not meet privilege requirements to access <xsl:value-of select="iad:InformationAccessDenyingSystemNameText"/>. To request access, contact your IT department.</span><br />
	</xsl:template>
	
	<xsl:template match="srer:SearchRequestError">
		<span class="error">System Name: <xsl:value-of select="intel:SystemName" />, Error: <xsl:value-of select="srer:ErrorText"/></span><br />
	</xsl:template>
	
	<xsl:template match="srer:SearchResultsExceedThresholdError">
		<span class="error">System <xsl:value-of select="../intel:SystemName" /> returned too many records, please refine your criteria.</span><br />
	</xsl:template>
</xsl:stylesheet>
