<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:exchange="http://ojbc.org/IEPD/Exchange/PersonSearchResults/1.0"
    xmlns:ext="http://ojbc.org/IEPD/Extensions/PersonSearchResults/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
        <EntityMergeResultMessage xmlns="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0">
            <SearchResultsMetadataCollection>
                <xsl:copy-of select="exchange:PersonSearchResults/srm:SearchResultsMetadata"/>
            </SearchResultsMetadataCollection>
        </EntityMergeResultMessage>
    </xsl:template>    
    
</xsl:stylesheet>