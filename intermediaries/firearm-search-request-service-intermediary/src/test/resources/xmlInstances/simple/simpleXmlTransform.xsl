<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
	<xsl:output method="html" encoding="UTF-8" />
	<xsl:template match="cars/car">
		<div>Maker:<xsl:value-of select="maker" /></div>
	</xsl:template>
</xsl:stylesheet>