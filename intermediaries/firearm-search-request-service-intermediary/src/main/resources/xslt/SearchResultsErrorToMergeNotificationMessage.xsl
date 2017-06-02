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
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:srm="http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0"
    xmlns:exchange="http://ojbc.org/IEPD/Exchange/FirearmSearchResults/1.0"
	xmlns:intel="http://niem.gov/niem/domains/intelligence/2.1"
	xmlns:iad="http://ojbc.org/IEPD/Extensions/InformationAccessDenial/1.0" 
	xmlns:srer="http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
        <EntityMergeResultMessage xmlns="http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0">
            <SearchResultsMetadataCollection>
                <xsl:copy-of select="exchange:FirearmSearchResults/srm:SearchResultsMetadata"/>
            </SearchResultsMetadataCollection>
        </EntityMergeResultMessage>
    </xsl:template>    
    
</xsl:stylesheet>