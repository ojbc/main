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
	xmlns:exc="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0"
	xmlns:s="http://niem.gov/niem/structures/2.0" 
    xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
	xmlns:ext1="http://ojbc.org/IEPD/Extensions/PersonSearchResults/1.0"
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:cyfs="http://niem.gov/niem/domains/cyfs/2.1/1"
	xmlns:micodes="http://ojbc.org/IEPD/Extensions/Michigan/PersonSearchResultsCodes/1.0"
	exclude-result-prefixes="#all">
    
    <xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="hrefBase" />
	<xsl:param name="purpose" />
	<xsl:param name="onBehalfOf" />

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
   			<span class="error">Unable to perform Entity Resolution. The search returned too many records.</span>
   		</xsl:if>
    	
    	<xsl:choose>
	    	<xsl:when test="($totalCount &gt; 0)">
	    		<table class="searchResultsTable display" id="personSearchResultsTable">
	    			<thead>
		    			<tr>
		    				<th>ENTITY</th>
		    				<th>NAME</th>
		    				<th>COURT</th>
		    				<th>ALIASES</th>
		    				<th>DOB</th>
		    				<th>GENDER</th>
		    				<th>SSN</th>
		    				<th>File Number</th>
		    				<th>PLACEMENT</th>
		    				<th>PARENT(S)</th>
		    				<th>RACE</th>
		    				<th>ETHNICITY</th>
		    				<th>TRIBE</th>
		    				<th>ADDRESS</th>
		    				<th>CITY</th>
		    				<th>STATE</th>
		    				<th>ZIP</th>
		    				<th class="hidden"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
		    			</tr>
	    			</thead>
	    			<tbody>
	    				<xsl:apply-templates select="exc:EntityContainer/ext:Entity">
	    				</xsl:apply-templates>
	    			</tbody>
	    		</table>
	    	</xsl:when>
	    	<xsl:otherwise>
	    		<xsl:if test="($entityContainer &gt; 0) and (count($tooManyResultsErrors) = 0)">
	    			No Matches Found
	    		</xsl:if>
	    	</xsl:otherwise>
    	</xsl:choose>
    </xsl:template>

	<!-- this will print a "merge" on the results screen -->
    <xsl:template match="ext:Entity">
    	<xsl:variable name="originalEntityID" select="@s:id" />
    	<xsl:variable name="position" select="position()"/>
		<!--If E’ is merged with any of the preceding-siblings of E’, then skip it.  Otherwise, write it out. -->
		<xsl:choose>
			<!-- Find the MergedRecord that pertains to E’.  Call this MergedRecord MR’. -->
			<xsl:when test="preceding-sibling::ext:Entity[@s:id = /exc:EntityMergeResultMessage/exc:MergedRecords/ext:MergedRecord[ext:OriginalRecordReference/@s:ref=$originalEntityID]/ext:OriginalRecordReference/@s:ref]">
				<!-- no nothing on purpose -->
			</xsl:when>
			<xsl:otherwise>
		    	<xsl:variable name="previouslyMergedEntityCount" select="count(//ext:MergedRecord[ext:OriginalRecordReference/@s:ref=//ext:Entity[position() &lt; $position]/@s:id])"/>
	   			
	            <xsl:call-template name="PersonRecord">
	                <xsl:with-param name="personId" select="$originalEntityID" />
	            	<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
	            </xsl:call-template>
	            
	            <xsl:for-each select="following-sibling::ext:Entity">
					<xsl:variable name="id" select="@s:id" />
					<xsl:if test="/exc:EntityMergeResultMessage/exc:MergedRecords/ext:MergedRecord[ext:OriginalRecordReference/@s:ref = $id and ext:OriginalRecordReference/@s:ref = $originalEntityID]">
						<xsl:call-template name="PersonRecord">
	                   		<xsl:with-param name="personId" select="$id" />
							<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
	              		 </xsl:call-template>
					</xsl:if>
				</xsl:for-each>
	     	 </xsl:otherwise>
     	</xsl:choose>
    </xsl:template>

    <xsl:template name="PersonRecord">
        <xsl:param name="personId" />
    	<xsl:param name="entityCount"/>

        <xsl:for-each select="/exc:EntityMergeResultMessage/exc:EntityContainer/ext:Entity[@s:id = $personId]/ext1:PersonSearchResult">
            <xsl:variable name="person" select="ext1:Person"/>    
            <xsl:variable name="personName" select="$person/nc:PersonName"/>
            <tr>
            	<td><xsl:value-of select="$entityCount"/></td>
                <td><xsl:apply-templates select="$personName" mode="primaryName"></xsl:apply-templates>
                </td>
            	<td ><xsl:value-of select="ext1:InformationOwningOrganization/nc:OrganizationName"></xsl:value-of></td>
            	<td ><xsl:apply-templates select="$person/nc:PersonAlternateName"/></td>
            	<td>
            		<xsl:if test="$person/nc:PersonBirthDate/nc:Date[. != '']">
            			<xsl:call-template name="formatDate">
            				<xsl:with-param name="date" select="$person/nc:PersonBirthDate/nc:Date" />
            			</xsl:call-template>
            		</xsl:if>
            	</td>
            	<td ><xsl:value-of select="$person/nc:PersonSexCode" /></td>
            	<td style="width:30%;">
            		<xsl:call-template name="formatSSN">
            			<xsl:with-param name="ssn" select="$person/nc:PersonSSNIdentification/nc:IdentificationID" />
            		</xsl:call-template>
            	</td>
            	<td><xsl:value-of select="j:Case/nc:ActivityIdentification/nc:IdentificationID" /></td>
            	<td><xsl:value-of select="cyfs:Placement/micodes:PlacementCategoryCode"/></td>
            	<td><xsl:apply-templates select="ext1:Parent/nc:PersonName"/></td>
                <td><xsl:value-of select="$person/nc:PersonRaceCode" /></td>
            	<td><xsl:value-of select="$person/nc:PersonEthnicityCode"/></td>
            	<td><xsl:value-of select="$person/cyfs:PersonAugmentation/cyfs:TribalAffiliationText"/></td>
            	<td>
            		<xsl:variable name="addr" select="nc:Location[1]/nc:LocationAddress/nc:StructuredAddress" />
            		
            		<xsl:if test="normalize-space($addr/nc:LocationStreet/nc:StreetNumberText) != '' and normalize-space($addr/nc:LocationStreet/nc:StreetName) != ''">
            			<xsl:value-of select="concat($addr/nc:LocationStreet/nc:StreetNumberText,' ',$addr/nc:LocationStreet/nc:StreetName)"/>
            		</xsl:if>
            		
            		<xsl:if test="normalize-space($addr/nc:LocationStreet/nc:StreetFullText) != ''">
            			<xsl:value-of select="$addr/nc:LocationStreet/nc:StreetFullText"/>
            		</xsl:if>
            	</td>
            	<td><xsl:value-of select="nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName"/></td>
            	<td><xsl:value-of select="nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationStateFIPS5-2AlphaCode"/></td>
            	<td><xsl:value-of select="nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode"/></td>
            	<td class="hidden">
            		<xsl:variable name="systemSource"><xsl:value-of select="normalize-space(ext1:SourceSystemNameText)"/></xsl:variable>
            		<xsl:variable name="queryType"><xsl:text>Person</xsl:text></xsl:variable>
                    <a href="{concat('../people/searchDetails?identificationID=',intel:SystemIdentifier/nc:IdentificationID , '&amp;systemName=' , intel:SystemIdentifier/intel:SystemName,'&amp;identificationSourceText=',$systemSource,'&amp;purpose=',$purpose,'&amp;onBehalfOf=',$onBehalfOf,'&amp;queryType=',$queryType)}" 
                        class="blueButton viewDetails" searchName='{intel:SystemIdentifier/intel:SystemName} Detail' 
                            appendPersonData="{concat('personalInformation-',$personId)}"
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
