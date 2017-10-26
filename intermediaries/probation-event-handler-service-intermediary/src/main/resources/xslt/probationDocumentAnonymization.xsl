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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:pcext="http://ojbc.org/IEPD/Extensions/ProbationCase/1.0"
   xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
   xmlns:jxdm="http://niem.gov/niem/domains/jxdm/4.1"
>
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="//pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc20:PersonName"/>
  <xsl:template match="//pcext:ProbationCase/pcext:Supervision/pcext:Probationer/nc20:PersonSSNIdentification"/>
  <xsl:template match="//pcext:ProbationCase/pcext:Supervision/pcext:Probationer/jxdm:PersonAugmentation/jxdm:PersonStateFingerprintIdentification"/>
  
</xsl:stylesheet>