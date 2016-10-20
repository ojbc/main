<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:lexs="http://usdoj.gov/leisp/lexs/3.1" xmlns:j="http://niem.gov/niem/domains/jxdm/4.0"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:lexslib="http://usdoj.gov/leisp/lexs/library/3.1"
	xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:scr="http://niem.gov/niem/domains/screening/2.0"
	xmlns:inc-ext="http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0"
	xmlns:lexsdigest="http://usdoj.gov/leisp/lexs/digest/3.1" xmlns:ndexia="http://fbi.gov/cjis/N-DEx/IncidentArrest/2.1"
	xmlns:ir-doc="http://ojbc.org/IEPD/Exchange/IncidentReport/1.0"
	xmlns:lexspd="http://usdoj.gov/leisp/lexs/publishdiscover/3.1">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/ir-doc:IncidentReport">
		<xsl:copy-of select="lexspd:doPublish" />
	</xsl:template>
</xsl:stylesheet>