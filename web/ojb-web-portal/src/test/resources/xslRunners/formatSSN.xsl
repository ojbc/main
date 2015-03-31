<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="#all"
>
	<!-- This file exists only to provide a way to test _pagination.xsl in isolation -->
   	<xsl:import href="../../../src/main/resources/xsl/_formatters.xsl" />
    <xsl:output method="html" encoding="UTF-8" />
	
	<xsl:param name="input" />

    <xsl:template match="/">
    	<xsl:call-template name="formatSSN" >
    		<xsl:with-param name="ssn" select="$input" />
    	</xsl:call-template>
    </xsl:template>
</xsl:stylesheet>