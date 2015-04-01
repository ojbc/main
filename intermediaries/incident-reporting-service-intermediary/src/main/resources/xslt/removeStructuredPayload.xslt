<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:maine="http://www.maine.gov/dps/msp/MaineIncidentReportNDEx"
 xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1"
 >
 <xsl:output omit-xml-declaration="yes" indent="yes"/>
 <xsl:strip-space elements="*"/>

 <xsl:template match="node()|@*">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>

 <xsl:template match="//lexs:StructuredPayload[lexs:StructuredPayloadMetadata/lexs:CommunityURI = 'http://www.maine.gov/dps/msp/MaineIncidentReportNDEx']"/>
 
 <xsl:template match="//lexs:StructuredPayload[lexs:StructuredPayloadMetadata/lexs:CommunityURI = 'http://www.ojbc.org']"/>

</xsl:stylesheet>