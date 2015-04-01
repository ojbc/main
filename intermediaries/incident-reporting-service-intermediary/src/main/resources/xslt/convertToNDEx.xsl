<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:gov="urn://gov.usdoj.publish.webservice"
>
	<xsl:output omit-xml-declaration="yes" indent="yes" method="xml"/>

	<xsl:template match="/">
      <gov:submitDataItem>
         <gov:dataItem>
             <xsl:copy-of select="."/>
         </gov:dataItem>
      </gov:submitDataItem>
	</xsl:template>
</xsl:stylesheet>