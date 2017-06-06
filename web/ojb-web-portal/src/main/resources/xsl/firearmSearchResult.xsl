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
	xmlns:fa-ext="http://ojbc.org/IEPD/Extensions/FirearmSearchResults/1.0"
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
	xmlns:hi="http://ojbc.org/IEPD/Extensions/Hawaii/FirearmCodes/1.0"
	xmlns:demostate-codes="http://ojbc.org/IEPD/Extensions/demostate/FirearmCodes/1.0"
    exclude-result-prefixes="#all">
  
    <xsl:import href="_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="hrefBase" />

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
		    	<table class="searchResultsTable display" id="searchResultsTable">
		    		<thead>	
				        <tr>
		    				<th>E</th>
			                <th>REG #</th>
			                <th>NAME</th>
			                <th>SER #</th>
			                <th>MAKE</th>
			                <th>MODEL</th>
			                <th>G/C</th>
			                <th>REG DATE</th>
			                <th>TYPE</th>
			                <th>STATUS</th>
			                <th>ACTION</th>
			                <th>COUNTY</th>
			                <th class="hidden"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></th>
			            </tr>
		            </thead>
		            <tbody>
				       	<xsl:apply-templates select="exc:EntityContainer/ext:Entity"/>
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
			<xsl:when test="preceding-sibling::ext:Entity[@s:id = //ext:MergedRecord[ext:OriginalRecordReference/@s:ref=$originalEntityID]/ext:OriginalRecordReference/@s:ref]">
				<!-- do nothing on purpose -->
			</xsl:when>
    		<xsl:otherwise>
    	
		    	<!-- show table if table starts or ends within pagination range -->
		    	<xsl:variable name="previouslyMergedEntityCount" select="count(/exc:EntityMergeResultMessage/exc:MergedRecords/ext:MergedRecord[ext:OriginalRecordReference/@s:ref=//ext:Entity[position() &lt; $position]/@s:id])"/>
	            <xsl:call-template name="FirearmRecord">
	            	<xsl:with-param name="firearmId" select="@s:id" />
		     		<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
		        </xsl:call-template>
		        <xsl:for-each select="following-sibling::ext:Entity">
						<xsl:variable name="id" select="@s:id" />
						<xsl:if test="/exc:EntityMergeResultMessage/exc:MergedRecords/ext:MergedRecord[ext:OriginalRecordReference/@s:ref = $id and ext:OriginalRecordReference/@s:ref = $originalEntityID]">
							<xsl:call-template name="FirearmRecord">
	                    		<xsl:with-param name="firearmId" select="$id" />
	                    		<xsl:with-param name="entityCount" select="$previouslyMergedEntityCount + 1"/>
	               		 </xsl:call-template>
						</xsl:if>
				</xsl:for-each>
	     	 </xsl:otherwise>
     	 </xsl:choose> 
    </xsl:template>

    <xsl:template name="FirearmRecord">
        <xsl:param name="firearmId" />
       	<xsl:param name="entityCount"/>

        <xsl:for-each select="/exc:EntityMergeResultMessage/exc:EntityContainer/ext:Entity[@s:id = $firearmId]/fa-ext:FirearmSearchResult">
            <tr>
                <td><xsl:value-of select="$entityCount"/></td>
                <xsl:variable name="registrationId">
                	<xsl:value-of select="fa-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID" />
                </xsl:variable>
                <td><xsl:value-of select="$registrationId" /></td>
                <td>
                	<xsl:choose>
                		<xsl:when test="normalize-space(nc:Person/nc:PersonName/nc:PersonFullName) != ''">
                			<xsl:value-of select="nc:Person/nc:PersonName/nc:PersonFullName" />
                		</xsl:when>
                		<xsl:otherwise>
                			<xsl:value-of select="nc:Person/nc:PersonName/nc:PersonGivenName" /><xsl:text> </xsl:text> <xsl:value-of select="nc:Person/nc:PersonName/nc:PersonMiddleName" /><xsl:text> </xsl:text><xsl:value-of select="nc:Person/nc:PersonName/nc:PersonSurName" />
                		</xsl:otherwise>
                	</xsl:choose>
                </td>
                <td><xsl:value-of select="fa-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID" /></td>
                <!-- TODO: add new hi code and text -->
                <td>
                	<xsl:choose>
                		<xsl:when test="normalize-space(fa-ext:Firearm/hi:FirearmMakeCode) != ''">
                			<xsl:value-of select="fa-ext:Firearm/hi:FirearmMakeCode"/>
                		</xsl:when>
                		<xsl:when test="normalize-space(fa-ext:Firearm/demostate-codes:FirearmMakeCode) != ''">
	                			<xsl:value-of select="fa-ext:Firearm/demostate-codes:FirearmMakeCode"/>
	                	</xsl:when>
                		<xsl:when test="normalize-space(fa-ext:Firearm/fa-ext:FirearmMakeText) != ''">
                			<xsl:value-of select="fa-ext:Firearm/fa-ext:FirearmMakeText"/>
                		</xsl:when>
                	
                	</xsl:choose>
				</td>
                <td><xsl:value-of select="fa-ext:Firearm/nc:ItemModelName" /></td>
                <td>
                	<xsl:apply-templates select ="fa-ext:Firearm/nc:FirearmGaugeText"/>
                	<xsl:choose>
                		<xsl:when test="normalize-space(fa-ext:Firearm/hi:FirearmCaliberCode) != ''">
                			<xsl:value-of select="fa-ext:Firearm/hi:FirearmCaliberCode"/>
                		</xsl:when>
                		<xsl:when test="normalize-space(fa-ext:Firearm/nc:FirearmCaliberText) != ''">
                			<xsl:value-of select="fa-ext:Firearm/nc:FirearmCaliberText"/>
                		</xsl:when>
	               </xsl:choose>
                </td>
                <td><xsl:value-of select="fa-ext:ItemRegistration/nc:RegistrationEffectiveDate/nc:Date" /></td>
                <td><xsl:value-of select="fa-ext:Firearm/nc:FirearmCategoryCode" /></td>
                <td>
                	<xsl:choose>
                		<xsl:when test="normalize-space(fa-ext:ItemRegistration/fa-ext:RegistrationStatus/hi:FirearmRegistrationStatusCode) != ''">
                			<xsl:value-of select="fa-ext:ItemRegistration/fa-ext:RegistrationStatus/hi:FirearmRegistrationStatusCode"/>
                		</xsl:when>
                		<xsl:when test="normalize-space(fa-ext:ItemRegistration/fa-ext:RegistrationStatus/fa-ext:FirearmRegistrationStatusText) != ''">
                			<xsl:value-of select="fa-ext:ItemRegistration/fa-ext:RegistrationStatus/fa-ext:FirearmRegistrationStatusText"/>
                		</xsl:when>
                	</xsl:choose>
                </td>
                <td><xsl:value-of select="fa-ext:Firearm/nc:FirearmCategoryDescriptionCode" /></td>
                <td><xsl:value-of select="fa-ext:ItemRegistration/nc:LocationCountyName" /></td>
                
                <td class="hidden">
                
                	<xsl:variable name="systemSource"><xsl:value-of select="normalize-space(fa-ext:SourceSystemNameText)"/></xsl:variable>
                	                    
					<!-- TODO use Identification ID for detail query rather than registration ID -->                    
                    <a href="{concat('../firearms/searchDetails?identificationID=', intel:SystemIdentifier/nc:IdentificationID, '&amp;systemName=Firearm Reg.&amp;identificationSourceText=',$systemSource)}" 
                        class="blueButton viewDetails" searchName='Firearm Registration Detail' 
                        
                            appendPersonData="{concat('firearmInformation-',$firearmId)}"
                        >DETAILS</a>
                    
                    
                </td>
            </tr>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="nc:FirearmGaugeText">
    	<xsl:value-of select="."/>
	   	<xsl:if test="preceding-sibling::hi:FirearmCaliberCode or preceding-sibling::nc:FirearmCaliberText">
	   		<xsl:text>/</xsl:text>
	   	</xsl:if>
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
