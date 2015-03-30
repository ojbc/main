<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:exchange="http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0"
    xmlns:ext="http://ojbc.org/IEPD/Extensions/IncidentSearchResults/1.0"
    xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
    xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0"
    xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
        <EntityMergeResultMessage xmlns="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0">
            <EntityContainer/>
            <MergedRecords/>
            <SearchResultsMetadataCollection>
                <xsl:copy-of select="exchange:IncidentSearchResults/srm:SearchResultsMetadata"/>
            </SearchResultsMetadataCollection>
        </EntityMergeResultMessage>
    </xsl:template>    
    
</xsl:stylesheet>