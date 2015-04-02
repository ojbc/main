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
    Document   : $Id: $
    Description: Generic stylesheet any XML data file. Stylesheet idea is based on the article by Craig Berry in XMLJournal 
-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
    method="html"
    encoding="UTF-8"
    indent="yes"
    omit-xml-declaration="yes"
    media-type="text/html"
    standalone="yes"/>

  <xsl:variable name="location" select="'./xsl'"/>
  <!-- Templates used to generate text content -->  

  <xsl:template match="/child::*[1]">
    <html>
      <head>
        <title>Data View</title>
        <style type="text/css">
          body { font-size: smaller }
          div, img { border: 0px; margin: 0px; padding: 0px }
          div.Node * { vertical-align: middle }
          i.AttributeVal  { color: rgb(50,127,127) }
          i.ElementVal  { color: rgb(40,0,255) }
          b.Attribute   { color: #77151F }
        </style>
      </head>
      <body>
        <b><xsl:value-of select="name()"/></b>
        <xsl:apply-templates mode="line"/>
      </body>
    </html>
  </xsl:template>

  <!-- Show each tree line -->
  <xsl:template match="*" mode="line">
    <div class="Node">
      <xsl:call-template name="graft"/>
      <xsl:apply-templates select="." mode="item"/>
    </div>    
    <xsl:apply-templates  select="child::*" mode="line"/>
  </xsl:template>
  
  <xsl:template match="*" mode="item">
      <b><xsl:value-of select="name()"/></b>
      <xsl:if test="@*">
          <xsl:text> [  </xsl:text>      
            <xsl:for-each select="@*">
                <b class="Attribute"><xsl:value-of select="name()"/></b>
                <xsl:text> = "</xsl:text>
                <i class="AttributeVal"><xsl:value-of select="."/></i>
                <xsl:text>" </xsl:text>
            </xsl:for-each>
          <xsl:text> ] </xsl:text>
      </xsl:if>
      <xsl:text>    </xsl:text>
      <i class="ElementVal"><xsl:value-of select="text()"/></i>
  </xsl:template>
   
  <!-- Templates used to generate the "stick stack" of
       tree connectors -->

  <xsl:template name="graft">
    <!-- Generate ancestor connectors -->
    <xsl:apply-templates select="ancestor::*" mode="tree"/>

    <!-- Generate current-node connector -->
    <xsl:choose>
      <xsl:when test="following-sibling::*">
        <img src="{$location}/tree_tee.gif"/>
      </xsl:when>
      <xsl:otherwise>
        <img src="{$location}/tree_corner.gif"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Suppress ancestor connector for the top node -->

  <xsl:template match="/child::*[1]" mode="tree"/>

  <!-- Show ancestor connectors for all other node types -->

  <xsl:template match="*" mode="tree">
    <xsl:choose>
      <xsl:when test="following-sibling::*">
        <img src="{$location}/tree_bar.gif"/>
      </xsl:when>
      <xsl:otherwise>
        <img src="{$location}/tree_spacer.gif"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>