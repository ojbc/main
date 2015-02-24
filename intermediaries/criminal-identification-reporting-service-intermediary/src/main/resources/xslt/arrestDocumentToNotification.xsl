<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ar="http://hijis.hawaii.gov/ArrestReport/1.0"
	xmlns:add="http://www.w3.org/2005/08/addressing"
	xmlns:ojbc-notification-message="http://ojbc.org/IEPD/Exchange/NotificationMessage/1.0"
	xmlns:ojbc-notification="http://ojbc.org/IEPD/Extensions/Notification/1.0" xmlns:jxdm41="http://niem.gov/niem/domains/jxdm/4.1"
	xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:niem-xsd="http://niem.gov/niem/proxy/xsd/2.0"
	xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:hi="http://ojbc.org/IEPD/Extensions/Hawaii/1.0"
	xmlns:jxdm40="http://niem.gov/niem/domains/jxdm/4.0" xmlns:ext="http://hijis.hawaii.gov/BookingReportExtension/1.0"
	xmlns:topics="http://ojbc.org/wsn/topics"
	>
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:template match="/ar:CJISDocument">
		<b:Notify xmlns:b="http://docs.oasis-open.org/wsn/b-2">
			<!--1 or more repetitions: -->
			<b:NotificationMessage>
				<!--Optional: -->
				<b:SubscriptionReference>
					<add:Address>http://www.ojbc.org/SubscriptionManager</add:Address>
					<!--Optional: -->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point -->
					</add:ReferenceParameters>
					<!--Optional: -->
					<add:Metadata>
						<!--You may enter ANY elements at this point -->
					</add:Metadata>
					<!--You may enter ANY elements at this point -->
				</b:SubscriptionReference>
				<!--Optional: -->
				<b:Topic Dialect="http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete">topics:person/arrest</b:Topic>

				<!--Optional: -->
				<b:ProducerReference>
					<add:Address>http://www.hawaii.gov/arrestNotificationProducer
					</add:Address>
					<!--Optional: -->
					<add:ReferenceParameters>
						<!--You may enter ANY elements at this point -->
					</add:ReferenceParameters>
					<!--Optional: -->
					<add:Metadata>
						<!--You may enter ANY elements at this point -->
					</add:Metadata>
					<!--You may enter ANY elements at this point -->
				</b:ProducerReference>
				<b:Message>
					<ojbc-notification-message:NotificationMessage>
						<ojbc-notification:NotifyingArrest s:id="A001">
							<xsl:if test="ext:AttorneyGeneralCaseIndicator != ''">
								<ojbc-notification:NotifyingActivityExtensions>
									<hi:AttorneyGeneralCaseDetails>
										<hi:AttorneyGeneralCaseIndicator><xsl:value-of select="ext:AttorneyGeneralCaseIndicator"/></hi:AttorneyGeneralCaseIndicator>
									</hi:AttorneyGeneralCaseDetails>
								</ojbc-notification:NotifyingActivityExtensions>
							</xsl:if>
							<jxdm41:Arrest>
								<nc:ActivityDate>
									<xsl:choose>
										<xsl:when test="jxdm40:Arrest/nc:ActivityDate/nc:DateTime != ''">
											<nc:DateTime><xsl:value-of select="jxdm40:Arrest/nc:ActivityDate/nc:DateTime"/></nc:DateTime>
										</xsl:when>
										<xsl:otherwise>
											<nc:Date><xsl:value-of select="jxdm40:Arrest/nc:ActivityDate/nc:Date"/></nc:Date>
										</xsl:otherwise>
									</xsl:choose>
								</nc:ActivityDate>
									
								<jxdm41:ArrestAgency>
									<nc:OrganizationName><xsl:value-of select="jxdm40:Arrest/ext:ArrestAgency/nc:OrganizationName"/></nc:OrganizationName>
								</jxdm41:ArrestAgency>
								
								<xsl:apply-templates select="//jxdm40:ArrestCharge"/>
								
								<jxdm41:Booking>
									<nc:ActivityDate>
										<xsl:choose>
											<xsl:when test="jxdm40:Arrest/jxdm40:Booking/nc:ActivityDate/nc:DateTime != ''">
												<nc:DateTime><xsl:value-of select="jxdm40:Arrest/jxdm40:Booking/nc:ActivityDate/nc:DateTime"/></nc:DateTime>
											</xsl:when>
											<xsl:otherwise>
												<nc:Date><xsl:value-of select="jxdm40:Arrest/jxdm40:Booking/nc:ActivityDate/nc:Date"/></nc:Date>
											</xsl:otherwise>
										</xsl:choose>
									</nc:ActivityDate>
									<jxdm41:BookingSubject>
										<nc:RoleOfPersonReference s:ref="P001" />
									</jxdm41:BookingSubject>
								</jxdm41:Booking>
							</jxdm41:Arrest>
						</ojbc-notification:NotifyingArrest>
						<nc:ActivityInvolvedPersonAssociation>
							<nc:ActivityReference s:ref="A001" />
							<nc:PersonReference s:ref="P001" />
						</nc:ActivityInvolvedPersonAssociation>
						<jxdm41:Person s:id="P001">
							<nc:PersonBirthDate>
								<nc:Date><xsl:value-of select="ext:Person/nc:PersonBirthDate/nc:Date"/></nc:Date>
							</nc:PersonBirthDate>
							<nc:PersonName>
								<nc:PersonGivenName><xsl:value-of select="ext:Person/nc:PersonName/nc:PersonGivenName"/></nc:PersonGivenName>
								<nc:PersonMiddleName><xsl:value-of select="ext:Person/nc:PersonName/nc:PersonMiddleName"/></nc:PersonMiddleName>
								<nc:PersonSurName><xsl:value-of select="ext:Person/nc:PersonName/nc:PersonSurName"/></nc:PersonSurName>
							</nc:PersonName>
							<jxdm41:PersonAugmentation>
								<jxdm41:PersonStateFingerprintIdentification>
									<nc:IdentificationID><xsl:value-of select="ext:Person/nc:PersonStateIdentification/nc:IdentificationID"/></nc:IdentificationID>
								</jxdm41:PersonStateFingerprintIdentification>
							</jxdm41:PersonAugmentation>
						</jxdm41:Person>
					</ojbc-notification-message:NotificationMessage>
				</b:Message>
			</b:NotificationMessage>
		</b:Notify>
	</xsl:template>
	
	<xsl:template match="jxdm40:ArrestCharge">
		<jxdm41:ArrestCharge>
			<jxdm41:ChargeDescriptionText><xsl:value-of select="jxdm40:ChargeStatute/jxdm40:StatuteDescriptionText"/></jxdm41:ChargeDescriptionText>
			<jxdm41:ChargeIdentification>
				<nc:IdentificationID><xsl:value-of select="jxdm40:ChargeIdentification/nc:IdentificationID"/></nc:IdentificationID>
			</jxdm41:ChargeIdentification>									
			<jxdm41:ChargeSeverityText><xsl:value-of select="jxdm40:ChargeSeverityText"/></jxdm41:ChargeSeverityText>
		</jxdm41:ArrestCharge>
	</xsl:template>
	
</xsl:stylesheet>