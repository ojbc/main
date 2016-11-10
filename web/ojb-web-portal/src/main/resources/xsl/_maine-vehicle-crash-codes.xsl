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
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	xmlns:nc20="http://niem.gov/niem/niem-core/2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all">
	<xsl:template name="formatLawEnforcementPresentAtWorkZone">
		<xsl:param name="LawEnforcementPresentAtWorkZoneCode" />
		<xsl:variable name="map">
			<entry key="1">Yes</entry>
			<entry key="2">No</entry>
			<entry key="3">Unknown</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key"
				select="$LawEnforcementPresentAtWorkZoneCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatWorkZoneWorkersPresent">
		<xsl:param name="WorkZoneWorkersPresentCode" />
		<xsl:variable name="map">
			<entry key="1">Yes</entry>
			<entry key="2">No</entry>
			<entry key="3">Unknown</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$WorkZoneWorkersPresentCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatVehicleDamageExtent">
		<xsl:param name="VehicleDamageExtentCode" />
		<xsl:variable name="map">
			<entry key="1">No Damage Observed</entry>
			<entry key="2">Minor Damage</entry>
			<entry key="3">Functional Damage</entry>
			<entry key="4">Towed Due to Disabling Damage</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$VehicleDamageExtentCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatAlcoholTestCategory">
		<xsl:param name="AlcoholTestCategoryCode" />
		<xsl:variable name="map">
			<entry key="1">Test Not Given</entry>
			<entry key="2">Test Refused</entry>
			<entry key="4">Blood</entry>
			<entry key="5">Breath</entry>
			<entry key="7">Urine</entry>
			<entry key="8">Other Chemical Test (Not Field Sobriety or PBT)</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$AlcoholTestCategoryCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatDrugTestCategory">
		<xsl:param name="DrugTestCategoryCode" />
		<xsl:variable name="map">
			<entry key="1">Test Not Given</entry>
			<entry key="2">Test Refused</entry>
			<entry key="3">Blood</entry>
			<entry key="4">Urine</entry>
			<entry key="5">Other</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$DrugTestCategoryCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatDrugTestResult">
		<xsl:param name="DrugTestResultCode" />
		<xsl:variable name="map">
			<entry key="1">Positive</entry>
			<entry key="2">Negative</entry>
			<entry key="3">Pending</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$DrugTestResultCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatCrashSchoolBusRelated">
		<xsl:param name="CrashSchoolBusRelatedCode" />
		<xsl:variable name="map">
			<entry key="0">No</entry>
			<entry key="1">Yes, School Bus Directly Involved</entry>
			<entry key="2">Yes, School Bus Indirectly Involved</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$CrashSchoolBusRelatedCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="formatPostedSpeedLimit">
		<xsl:param name="PostedSpeedLimitCode" />
		<xsl:variable name="map">
			<entry key="1">Not Posted 25 Zone</entry>
			<entry key="2">Not Posted 45 Zone</entry>
			<entry key="3">Unknown</entry>
			<entry key="5">5 Mph</entry>
			<entry key="10">10 Mph</entry>
			<entry key="15">15 Mph</entry>
			<entry key="18">18 Mph</entry>
			<entry key="20">20 Mph</entry>
			<entry key="25">25 Mph</entry>
			<entry key="30">30 Mph</entry>
			<entry key="35">35 Mph</entry>
			<entry key="35">30 Mph</entry>
			<entry key="40">40 Mph</entry>
			<entry key="45">45 Mph</entry>
			<entry key="50">50 Mph</entry>
			<entry key="55">55 Mph</entry>
			<entry key="60">60 Mph</entry>
			<entry key="65">65 Mph</entry>
			<entry key="70">70 Mph</entry>
			<entry key="75">75 Mph</entry>
			<entry key="81">N/A</entry>
		</xsl:variable>
		<xsl:call-template name="getValueFromMapWithDefault">
			<xsl:with-param name="key" select="$PostedSpeedLimitCode" />
			<xsl:with-param name="map" select="$map" />
		</xsl:call-template>
	</xsl:template>
	<!-- Methods below are "private" not intended to be used outside this file -->
	<xsl:template name="getValueFromMapWithDefault">
		<xsl:param name="key" />
		<xsl:param name="map" />
		<xsl:variable name="value"
			select="$map/entry[@key=normalize-space($key)]" />
		<xsl:choose>
			<xsl:when test="$value">
				<xsl:value-of select="$value" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$key" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>