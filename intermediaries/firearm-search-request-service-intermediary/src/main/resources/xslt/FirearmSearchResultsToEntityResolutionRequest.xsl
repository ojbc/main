<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:entityResolutionExtension="http://nij.gov/IEPD/Extensions/EntityResolutionExtensions/1.0"
    xmlns:entityMergeRequest="http://nij.gov/IEPD/Exchange/EntityMergeRequestMessage/1.0"
	xmlns:ext="http://ojbc.org/IEPD/Extensions/FirearmSearchResults/1.0"
    xmlns:er-ext="http://nij.gov/IEPD/Extensions/EntityResolutionExtensions/1.0"
    xmlns:s="http://niem.gov/niem/structures/2.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:hi="http://ojbc.org/IEPD/Extensions/Hawaii/FirearmCodes/1.0"
    xmlns:firearm-search-resp-ext="http://ojbc.org/IEPD/Extensions/FirearmSearchResults/1.0"
    xmlns:firearms-hi="http://ojbc.org/IEPD/Extensions/Hawaii/FirearmCodes/1.0"
    xmlns:firearm-search-resp-doc="http://ojbc.org/IEPD/Exchange/FirearmSearchResults/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <!-- Camel will inject these parameter since it is declared as a Camel header -->
    <!-- See: http://camel.apache.org/xslt.html -->
    <xsl:param name="erAttributeParametersFilePath"/>
    <xsl:param name="entityResolutionRecordThreshold"/>
    
    <xsl:variable name="attributeParameters" select="document($erAttributeParametersFilePath)"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
    <entityMergeRequest:EntityMergeRequestMessage >
        <entityMergeRequest:MergeParameters>
        
        	<xsl:copy-of select="$attributeParameters"/>
        
        	<er-ext:EntityResolutionConfiguration>
        		<er-ext:RecordLimit><xsl:value-of select="$entityResolutionRecordThreshold"/></er-ext:RecordLimit>
        	</er-ext:EntityResolutionConfiguration>
        
            <entityResolutionExtension:EntityContainer>
                
            <xsl:apply-templates select="//firearm-search-resp-doc:FirearmSearchResults/ext:FirearmSearchResult"/>
                
            </entityResolutionExtension:EntityContainer>
        </entityMergeRequest:MergeParameters>
    </entityMergeRequest:EntityMergeRequestMessage>   
    </xsl:template>    
    
    <xsl:template match="//firearm-search-resp-doc:FirearmSearchResults/ext:FirearmSearchResult">
        <xsl:call-template name="FirearmSearchResult"/>
    </xsl:template>
 
    <xsl:template name="FirearmSearchResult">
        <entityResolutionExtension:Entity>
        	<xsl:attribute name="id" namespace="http://niem.gov/niem/structures/2.0"><xsl:value-of select="generate-id()"/></xsl:attribute>
            <ext:FirearmSearchResult>
	            <xsl:attribute name="id" namespace="http://niem.gov/niem/structures/2.0"><xsl:value-of select="generate-id()"/></xsl:attribute>
                <xsl:copy-of select="node()" copy-namespaces="no"/>
            </ext:FirearmSearchResult>    
        </entityResolutionExtension:Entity>    
    </xsl:template>
    
</xsl:stylesheet>