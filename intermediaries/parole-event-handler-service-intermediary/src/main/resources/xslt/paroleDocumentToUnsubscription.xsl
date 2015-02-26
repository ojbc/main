<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:b="http://docs.oasis-open.org/wsn/b-2" xmlns:add="http://www.w3.org/2005/08/addressing"
	xmlns:topics="http://ojbc.org/wsn/topics"
	xmlns:s="http://niem.gov/niem/structures/2.0"
	xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:nc20="http://niem.gov/niem/niem-core/2.0"
	xmlns:parole="http://ojbc.org/IEPD/Extensions/ParoleCase/1.0"
	xmlns:um="http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0"
	xmlns:smext="http://ojbc.org/IEPD/Extensions/Subscription/1.0"
	xmlns:pct="http://ojbc.org/IEPD/Exchange/ParoleCaseTermination/1.0" 
	exclude-result-prefixes="s parole pct"
	>
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:param name="topicExpression"/>
	
	<xsl:template match="pct:ParoleCaseTermination">
		<b:Unsubscribe>
			<!--You may enter ANY elements at this point-->
			<um:UnsubscriptionMessage>
				<smext:Subject>
					<xsl:apply-templates select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonBirthDate"/>
					<xsl:apply-templates select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonName" />
					<xsl:apply-templates select="parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonStateIdentification" />
				</smext:Subject>
				<nc20:ContactEmailID><xsl:value-of select="normalize-space(parole:ParoleCase/nc20:ContactInformation[@s:id='paroleOfficerEmail1']/nc20:ContactEmailID)"/></nc20:ContactEmailID>
				<smext:SystemName><xsl:value-of select="parole:SystemName"/></smext:SystemName>
				<smext:SubscriptionQualifierIdentification>
					<nc20:IdentificationID><xsl:value-of select="normalize-space(parole:ParoleCase/jxdm41:Supervision/nc20:SupervisionPerson/nc20:PersonStateIdentification/nc20:IdentificationID)"/></nc20:IdentificationID>
				</smext:SubscriptionQualifierIdentification>				
			</um:UnsubscriptionMessage>
			<b:TopicExpression
				Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete"
				><xsl:value-of select="$topicExpression"/></b:TopicExpression>
		</b:Unsubscribe>
	</xsl:template>
	<xsl:template match="nc20:PersonBirthDate">
		<nc20:PersonBirthDate>
			<nc20:Date><xsl:value-of select="nc20:Date"/></nc20:Date>
		</nc20:PersonBirthDate>
	</xsl:template>
	<xsl:template match="nc20:PersonName">
		<nc20:PersonName>
			<nc20:PersonFullName><xsl:value-of select="nc20:PersonGivenName"/><xsl:text> </xsl:text><xsl:value-of select="nc20:PersonSurName"/></nc20:PersonFullName>
			<nc20:PersonGivenName><xsl:value-of select="nc20:PersonGivenName"/></nc20:PersonGivenName>
			<nc20:PersonSurName><xsl:value-of select="nc20:PersonSurName"/></nc20:PersonSurName>
		</nc20:PersonName>
	</xsl:template>
	<xsl:template match="nc20:PersonStateIdentification">
		<jxdm41:PersonAugmentation>
			<jxdm41:PersonStateFingerprintIdentification><nc20:IdentificationID><xsl:value-of select="normalize-space(nc20:IdentificationID)"/></nc20:IdentificationID></jxdm41:PersonStateFingerprintIdentification>
		</jxdm41:PersonAugmentation>
	</xsl:template>
	
</xsl:stylesheet>
