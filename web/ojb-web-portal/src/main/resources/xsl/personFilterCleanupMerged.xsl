<?xml version="1.0" encoding="UTF-8"?>
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

	<!-- Copy all the nodes and attributes  -->
	<xsl:template match="@* | node()">
	      <xsl:copy >
	      	<xsl:apply-templates select="@* | node()"></xsl:apply-templates>
	      </xsl:copy>
	</xsl:template>

	<!-- Copy the MergedRecord when there is an OriginalRecordReference -->
    <xsl:template match="//ext:MergedRecord">
    	<xsl:if test="ext:OriginalRecordReference" >    	
			<xsl:copy-of select="." />
    	</xsl:if>
    </xsl:template>

</xsl:stylesheet>

