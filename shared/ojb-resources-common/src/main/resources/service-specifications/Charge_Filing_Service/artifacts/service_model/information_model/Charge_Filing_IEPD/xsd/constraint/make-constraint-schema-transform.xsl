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
<!--

    This stylesheet creates a stylesheet that can be used to transform a proper NIEM subset schema into a constraint schema.  The generated
    stylesheet is valuable, because it is a place to represent cardinality rules and other rules that can be used again and again as subsets are
    regenerated with the subset schema generator tool.
    
    This stylesheet accepts as input a proper NIEM subset schema.  It outputs a stylesheet that can be edited to apply cardinality and other
    rules.  The output stylesheet should be saved (e.g., under a project's source control system) and used as changes are made to the subset, so that the
    rules don't have to be remembered and reapplied manually each time.
    
    Jim Cabral
    MTG Management Consultants LLC
    jcabral@mtgmc.com

    Based on constraint schema generation scripts for the GJXDM 3.x created by:
    Scott Came
    Justice Integration Solutions, Inc.
    scott@justiceintegration.com
    
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xslt="temp" version="1.0">
	<xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" indent="yes"/>
	<xsl:namespace-alias result-prefix="xsl" stylesheet-prefix="xslt"/>
	<xsl:template match="/xsd:schema">
		<xslt:stylesheet version="1.0">
			<xslt:output method="xml" encoding="UTF-8" indent="yes"/>
			<xsl:for-each select="/xsd:schema/xsd:complexType/xsd:complexContent/xsd:extension/xsd:sequence/xsd:element">
				<xslt:template>
					<xsl:attribute name="match"><xsl:text>xsd:element[@ref='</xsl:text><xsl:value-of select="@ref"/><xsl:text>' and ../../../../@name='</xsl:text><xsl:value-of select="../../../../@name"/><xsl:text>']</xsl:text></xsl:attribute>
					<xsd:element>
						<xsl:attribute name="ref"><xsl:value-of select="@ref"/></xsl:attribute>
						<xslt:attribute name="minOccurs">
							<xsl:value-of select="@minOccurs"/>
						</xslt:attribute>
						<xslt:attribute name="maxOccurs">
							<xsl:value-of select="@maxOccurs"/>
						</xslt:attribute>
					</xsd:element>
				</xslt:template>
			</xsl:for-each>
			<xsl:for-each select="/xsd:schema/xsd:simpleType/xsd:restriction/xsd:enumeration">
				<xslt:template>
					<xsl:attribute name="match"><xsl:text>xsd:enumeration[@value='</xsl:text><xsl:value-of select="@value"/><xsl:text>' and ../../@name='</xsl:text><xsl:value-of select="../../@name"/><xsl:text>']</xsl:text></xsl:attribute>
					<xslt:copy-of select="."/>
				</xslt:template>
			</xsl:for-each>
			<xslt:template match="/xsd:schema">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:complexType">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:simpleType">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:complexContent">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:extension">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:restriction">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:sequence">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:minInclusive">
				<xslt:copy>
					<xslt:for-each select="@*">
						<xslt:copy/>
					</xslt:for-each>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:simpleContent">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="/xsd:schema/xsd:annotation">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="/xsd:schema/xsd:complexType">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="/xsd:schema/xsd:simpleType">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="/xsd:attribute/xsd:annotation">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="/xsd:attributeGroup/xsd:annotation">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="xsd:annotation"/>
			<xslt:template match="xsd:import">
				<xslt:copy>
					<xslt:attribute name="namespace">
						<xslt:value-of select="@namespace"/>
					</xslt:attribute>
					<xslt:attribute name="schemaLocation">
						<xslt:value-of select="@schemaLocation"/>
					</xslt:attribute>
					<xslt:apply-templates/>
				</xslt:copy>
			</xslt:template>
			<xslt:template match="xsd:attribute">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="xsd:attributeGroup">
				<xslt:copy-of select="."/>
			</xslt:template>
			<xslt:template match="xsd:element">
				<xslt:copy-of select="."/>
			</xslt:template>
		</xslt:stylesheet>
	</xsl:template>
	<xsl:template match="xsd:element">
		<xslt:template>
			<xsl:attribute name="name"><xsl:value-of select="@ref"/></xsl:attribute>
			<xsl:attribute name="enclosingTypeName"><xsl:value-of select="../../../../@name"/></xsl:attribute>
		</xslt:template>
	</xsl:template>
</xsl:stylesheet>
<!--

Copyright (C) 2004-2005 Justice Integration Solutions, Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without modifica-
tion, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
   this list of conditions, and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
(INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->
