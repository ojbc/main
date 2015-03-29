<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:exchange="http://ojbc.org/IEPD/Exchange/VehicleSearchResults/1.0"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
        <EntityMergeResultMessage xmlns="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0">
            <SearchResultsMetadataCollection>
                <xsl:copy-of select="exchange:VehicleSearchResults/srm:SearchResultsMetadata" copy-namespaces="no"/>
            </SearchResultsMetadataCollection>
        </EntityMergeResultMessage>
    </xsl:template>    
    
</xsl:stylesheet>