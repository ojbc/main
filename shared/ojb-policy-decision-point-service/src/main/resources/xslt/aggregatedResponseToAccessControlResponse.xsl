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

    Copyright 2012-2015 Open Justice Broker Consortium

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ac-doc="http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0"
    xmlns:s30="http://release.niem.gov/niem/structures/3.0/"
    xmlns:ac-p="http://ojbc.org/IEPD/Extensions/AccessControlDecisionContexts/PolicyBasedAccessControlDecisionContext/1.0"
    xmlns:ac-ext="http://ojbc.org/IEPD/Extensions/AccessControlResponse/1.0"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output omit-xml-declaration="yes"/>
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/OJBAggregateResponseWrapper">
    	<ac-doc:AccessControlResponse
	        xmlns:ac-doc="http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0"
	        xmlns:s30="http://release.niem.gov/niem/structures/3.0/"
	        xmlns:ac-p="http://ojbc.org/IEPD/Extensions/AccessControlDecisionContexts/PolicyBasedAccessControlDecisionContext/1.0"
	        xmlns:ac-ext="http://ojbc.org/IEPD/Extensions/AccessControlResponse/1.0"
	        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	        xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/AccessControlResponse/1.0 ../xsd/exchange_schema.xsd">
    	    
    	    <xsl:for-each select="ac-doc:AccessControlResponse">
    	        <xsl:apply-templates select="ac-ext:AccessControlDecision">
    	            <xsl:with-param name="childPosition" select="position()" tunnel="yes"/>
    	        </xsl:apply-templates>
    	    </xsl:for-each>
        </ac-doc:AccessControlResponse>
    </xsl:template>    
    
    <xsl:template match="ac-ext:AccessControlDecision">
        <xsl:param name="childPosition" tunnel="yes"/>
        <xsl:element name="ac-ext:AccessControlDecision">
            <xsl:attribute name="s30:id">
                <xsl:text>Decision_</xsl:text><xsl:value-of select="format-number($childPosition, '00')"></xsl:value-of>
            </xsl:attribute>
            <xsl:copy-of select="child::node()"></xsl:copy-of>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>