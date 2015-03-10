<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
	<xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="param1" />	
	<xsl:param name="param2" />	
	
	<xsl:template match="/">
		<div>Parameter 1: <xsl:value-of select="$param1"/></div>
		<div>Parameter 2: <xsl:value-of select="$param2"/></div>
	</xsl:template>

</xsl:stylesheet>