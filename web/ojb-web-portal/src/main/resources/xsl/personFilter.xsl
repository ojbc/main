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
    >
    

<xsl:output method='xml'></xsl:output>   
	<xsl:param name="filterPersonRaceCode"/>
	<xsl:param name="filterPersonHairColor"/>	
	<xsl:param name="filterPersonEyeColor"/>		
	<xsl:param name="filterDOBStart"/> 
	<xsl:param name="filterDOBEnd"/> 
	<xsl:param name="filterHeightTolerance"/> 
	<xsl:param name="filterHeightInFeet"/> 
	<xsl:param name="filterHeightInInches"/> 
	<xsl:param name="filterWeight"/> 
	<xsl:param name="filterWeightTolerance"/>			
	
	
	<xsl:variable name='heightInchesFrom' select="($filterHeightInFeet * 12) + $filterHeightInInches - $filterHeightTolerance"></xsl:variable>
	<xsl:variable name='heightInchesThru' select="($filterHeightInFeet * 12) + $filterHeightInInches + $filterHeightTolerance"></xsl:variable>				
	<xsl:variable name='weightFrom' select="($filterWeight - $filterWeightTolerance)"></xsl:variable>
	<xsl:variable name='weightThru' select="($filterWeight + $filterWeightTolerance)"></xsl:variable>
	
	<xsl:variable name="skipRace">
	   <xsl:choose>
	     <xsl:when test="$filterPersonRaceCode = ''">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="skipEye">
	   <xsl:choose>
	     <xsl:when test="$filterPersonEyeColor = ''">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="skipHair">
	   <xsl:choose>
	     <xsl:when test="$filterPersonHairColor = ''">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>	
	
	<xsl:variable name="skipDOB">
	   <xsl:choose>
	     <xsl:when test="$filterDOBStart = ''">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>		 
	
	<xsl:variable name="skipHeight">
	   <xsl:choose>
	     <xsl:when test="$filterHeightInFeet = 0">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>	
		 	
	<xsl:variable name="skipWeight">
	   <xsl:choose>
	     <xsl:when test="$filterWeight = 0">true</xsl:when>
	     <xsl:otherwise>false</xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>	
	 	 

	<!-- Copy all the nodes and attributes  -->
	<xsl:template match="@* | node()">
	      <xsl:copy >
	      	<xsl:apply-templates select="@* | node()"></xsl:apply-templates>
	      </xsl:copy>
	</xsl:template>

	<!-- Copy the Entity when it passes filter criteria -->
    <xsl:template match="//ext:Entity">
    	<xsl:if test="($skipRace = 'true') or ext1:PersonSearchResult/ext1:Person/nc:PersonRaceCode = $filterPersonRaceCode" >    	
    		<xsl:if test="($skipEye = 'true') or ext1:PersonSearchResult/ext1:Person/nc:PersonEyeColorCode = $filterPersonEyeColor" >
    			<xsl:if test="($skipHair = 'true') or ext1:PersonSearchResult/ext1:Person/nc:PersonHairColorCode = $filterPersonHairColor" >  
    				<xsl:if test="($skipDOB = 'true') or (ext1:PersonSearchResult/ext1:Person/nc:PersonBirthDate/nc:Date &gt;= $filterDOBStart  and
    						      ext1:PersonSearchResult/ext1:Person/nc:PersonBirthDate/nc:Date &lt;= $filterDOBEnd)"> 
    					<xsl:if test="($skipHeight = 'true') or (ext1:PersonSearchResult/ext1:Person/nc:PersonHeightMeasure/nc:MeasurePointValue &gt;= $heightInchesFrom  and
    						     	  ext1:PersonSearchResult/ext1:Person/nc:PersonHeightMeasure/nc:MeasurePointValue &lt;= $heightInchesThru)">  
	    					<xsl:if test="($skipWeight = 'true') or (ext1:PersonSearchResult/ext1:Person/nc:PersonWeightMeasure/nc:MeasurePointValue &gt;= $weightFrom  and
	    						     	  ext1:PersonSearchResult/ext1:Person/nc:PersonWeightMeasure/nc:MeasurePointValue &lt;= $weightThru)">  
	    						<xsl:copy-of select="." />
	    			    	</xsl:if>  
    			    	</xsl:if>    					  
    			    </xsl:if>
    			</xsl:if>
    		</xsl:if>
    	</xsl:if>
    </xsl:template>

	<!-- Copy the OriginalRecordReference only when it is used (when it refers to an Entity that passes criteria)-->
    <xsl:template match="//ext:OriginalRecordReference">
    	<xsl:variable name="sRef" select="@s:ref" />
    		<xsl:if test="($skipRace = 'true') or //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonRaceCode = $filterPersonRaceCode" >
    			<xsl:if test="($skipEye = 'true') or //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonEyeColorCode = $filterPersonEyeColor" >
    				<xsl:if test="($skipHair = 'true') or //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonHairColorCode = $filterPersonHairColor" >        	
    					<xsl:if test="($skipDOB = 'true') or (//ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonBirthDate/nc:Date &gt; $filterDOBStart  and
	    						   	  //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonBirthDate/nc:Date &lt; $filterDOBEnd)">      			      	
	    					<xsl:if test="($skipHeight = 'true') or (//ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonHeightMeasure/nc:MeasurePointValue &gt;= $heightInchesFrom  and
	    						     	  //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonHeightMeasure/nc:MeasurePointValue &lt;= $heightInchesThru)">  
		    					<xsl:if test="($skipWeight = 'true') or (//ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonWeightMeasure/nc:MeasurePointValue &gt;= $weightFrom  and
		    						     	  //ext:Entity[ @s:id = $sRef ]/ext1:PersonSearchResult/ext1:Person/nc:PersonWeightMeasure/nc:MeasurePointValue &lt;= $weightThru)">  
		    						<xsl:copy-of select="." />
		    			    	</xsl:if> 
	    			    	</xsl:if>     						
	    			    </xsl:if>
	    			</xsl:if>
	    		</xsl:if>
	    	</xsl:if>    		
    </xsl:template>

</xsl:stylesheet>
