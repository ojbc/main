<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:entityResolutionExtension="http://nij.gov/IEPD/Extensions/EntityResolutionExtensions/1.0"
    xmlns:entityMergeRequest="http://nij.gov/IEPD/Exchange/EntityMergeRequestMessage/1.0"
	xmlns:ext="http://ojbc.org/IEPD/Extensions/VehicleSearchResults/1.0"
	xmlns:exchange="http://ojbc.org/IEPD/Exchange/VehicleSearchResults/1.0"
    xmlns:er-ext="http://nij.gov/IEPD/Extensions/EntityResolutionExtensions/1.0"
    xmlns:ns0="http://niem.gov/niem/structures/2.0"
    xmlns:nc="http://niem.gov/niem/niem-core/2.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"	
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <!-- Camel will inject in this parameter since it is declared as a Camel header -->
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
                
            <xsl:apply-templates select="exchange:VehicleSearchResults/ext:VehicleSearchResult"/>
                
            </entityResolutionExtension:EntityContainer>
        </entityMergeRequest:MergeParameters>
    </entityMergeRequest:EntityMergeRequestMessage>   
    </xsl:template>    
    
    <xsl:template match="ext:VehicleSearchResult">
        <xsl:call-template name="VehicleSearchResult"/>
    </xsl:template>
 
    <xsl:template name="VehicleSearchResult">
        <entityResolutionExtension:Entity>
        	<xsl:attribute name="id" namespace="http://niem.gov/niem/structures/2.0"><xsl:value-of select="generate-id()"/></xsl:attribute>
            <ext:VehicleSearchResult>
                <xsl:copy-of select="node()" copy-namespaces="no"/>
            </ext:VehicleSearchResult>    
        </entityResolutionExtension:Entity>    
    </xsl:template>
    
</xsl:stylesheet>