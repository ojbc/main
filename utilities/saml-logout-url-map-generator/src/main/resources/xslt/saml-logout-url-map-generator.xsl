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
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:shibmd="urn:mace:shibboleth:metadata:1.0" xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata"
	xmlns:saml20="urn:oasis:names:tc:SAML:2.0:metadata" xmlns:util="urn:Util.map"
	exclude-result-prefixes="ds shibmd xsi saml20 util">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:template match="/saml20:EntitiesDescriptor">
		<util:map id="entityLogoutReturnUrlMap" map-class="java.util.LinkedHashMap">
			<xsl:apply-templates select="saml20:EntityDescriptor" />
		</util:map>
	</xsl:template>
	<xsl:template match="saml20:EntityDescriptor">
		<entry>
			<xsl:attribute name="key"><xsl:value-of select="@entityID" /></xsl:attribute>
			<xsl:choose>
				<xsl:when test="md:SPSSODescriptor/md:SingleLogoutService or md:IDPSSODescriptor/md:SingleLogoutService">
					<xsl:apply-templates select="md:SPSSODescriptor/md:SingleLogoutService | md:IDPSSODescriptor/md:SingleLogoutService" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="value"><xsl:value-of select="'NotAvailableInMetadata'" />
				</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</entry>
	</xsl:template>
	<xsl:template match="md:SingleLogoutService">
		<xsl:if test="contains(@Binding,'HTTP-Redirect')">
			<xsl:attribute name="value"><xsl:value-of select="./@Location" />
				</xsl:attribute>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>