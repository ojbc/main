<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:br-doc="http://ojbc.org/IEPD/Exchange/BookingReport/1.0"
xmlns:br-ext="http://ojbc.org/IEPD/Extensions/BookingReportExtension/1.0"
xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
xmlns:j="http://release.niem.gov/niem/domains/jxdm/5.1/" 
xmlns:structures="http://release.niem.gov/niem/structures/3.0/"
xmlns:phisr-doc="http://ojbc.org/IEPD/Exchange/PersonHealthInformationSearchRequest/1.0"
xmlns:phisr-ext="http://ojbc.org/IEPD/Extensions/PersonHealthInformationSearchRequest/1.0"
xmlns:cscr-doc="http://ojbc.org/IEPD/Exchange/CustodyStatusChangeReport/1.0"
xmlns:cscr-ext="http://ojbc.org/IEPD/Extensions/CustodyStatusChangeReportExtension/1.0"
xmlns:crr-doc="http://ojbc.org/IEPD/Exchange/CustodyReleaseReport/1.0"
xmlns:crr-ext="http://ojbc.org/IEPD/Extensions/CustodyReleaseReportExtension/1.0"
xmlns:pc-bkg-codes="http://ojbc.org/IEPD/Extensions/PimaCounty/BookingCodes/1.0"
xmlns:ac-bkg-codes="http://ojbc.org/IEPD/Extensions/AdamsCounty/BookingCodes/1.0"
xmlns:ac-phi-codes="http://ojbc.org/IEPD/Extensions/AdamsCounty/PersonHealthInformationCodes/1.0"
xmlns:pc-phi-codes="http://ojbc.org/IEPD/Extensions/PimaCounty/PersonHealthInformationCodes/1.0"
exclude-result-prefixes="crr-ext crr-doc cscr-doc cscr-ext br-doc br-ext">

<xsl:output indent="yes"/>
	<xsl:param name="temporaryID"/>
	<xsl:param name="systemName"/>
	<xsl:param name="addBookingID">true</xsl:param>
	
	<xsl:template match="/br-doc:BookingReport">
		<xsl:apply-templates select="j:Booking/j:BookingSubject"/>
	</xsl:template>
	
	<xsl:template match="/cscr-doc:CustodyStatusChangeReport">
		<xsl:apply-templates select="cscr-ext:Custody/j:Booking/j:BookingSubject"/>
	</xsl:template>
	
	<xsl:template match="/crr-doc:CustodyReleaseReport">
		<xsl:apply-templates select="crr-ext:Custody/j:Booking/j:BookingSubject"/>
	</xsl:template>
	
	
	<xsl:template match="j:BookingSubject">
		<xsl:variable name="subjectID" >
			<xsl:value-of select="j:SubjectIdentification/nc:IdentificationID"/>
		</xsl:variable>
		<phisr-doc:PersonHealthInformationSearchRequest>
			<xsl:apply-templates select="nc:RoleOfPerson" mode="booking">
				<xsl:with-param name="subjectID" select="$subjectID"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="nc:RoleOfPerson" mode="custodyStatusChange">
				<xsl:with-param name="subjectID" select="$subjectID"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="nc:RoleOfPerson" mode="release">
				<xsl:with-param name="subjectID" select="$subjectID"/>
			</xsl:apply-templates>
			<phisr-ext:SourceSystemNameText><xsl:value-of select="$systemName"/></phisr-ext:SourceSystemNameText>
		</phisr-doc:PersonHealthInformationSearchRequest>
	</xsl:template>
	
	<xsl:template match="nc:RoleOfPerson" mode="booking">
		<xsl:param name="subjectID"/>
		<xsl:variable name="personID" select="@structures:ref"/>
		<xsl:apply-templates select="/br-doc:BookingReport/nc:Person[@structures:id = $personID]">
			<xsl:with-param name="subjectID" select="$subjectID"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="/br-doc:BookingReport/nc:Identity"/>
		<xsl:copy-of select="/br-doc:BookingReport/nc:PersonAliasIdentityAssociation" copy-namespaces="no" />
	</xsl:template>
	
	<xsl:template match="nc:RoleOfPerson" mode="custodyStatusChange">
		<xsl:param name="subjectID"/>
		<xsl:variable name="personID" select="@structures:ref"/>
		<xsl:apply-templates select="/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc:Person[@structures:id = $personID]">
			<xsl:with-param name="subjectID" select="$subjectID"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc:Identity"/>
		<xsl:copy-of select="/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc:PersonAliasIdentityAssociation" copy-namespaces="no" />
	</xsl:template>
	
	<xsl:template match="nc:RoleOfPerson" mode="release">
		<xsl:param name="subjectID"/>
		<xsl:variable name="personID" select="@structures:ref"/>
		<xsl:apply-templates select="/crr-doc:CustodyReleaseReport/crr-ext:Custody/nc:Person[@structures:id = $personID]">
			<xsl:with-param name="subjectID" select="$subjectID"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="/crr-doc:CustodyReleaseReport/crr-ext:Custody/nc:Identity"/>
		<xsl:copy-of select="/crr-doc:CustodyReleaseReport/crr-ext:Custody/nc:PersonAliasIdentityAssociation" copy-namespaces="no" />
	</xsl:template>
	

	<xsl:template match="nc:Person">
		<xsl:param name="subjectID"/>
		<nc:Person>
			<xsl:copy-of select="@structures:id" copy-namespaces="no"/>
			<xsl:copy-of select="nc:PersonBirthDate" copy-namespaces="no" />
			<xsl:copy-of select="j:PersonEthnicityCode" copy-namespaces="no"/>
			<xsl:copy-of select="nc:PersonName" copy-namespaces="no"/>
			<xsl:apply-templates select="pc-bkg-codes:PersonRaceCode"/>
			<xsl:apply-templates select="ac-bkg-codes:PersonRaceCode"/>
			<xsl:copy-of select="j:PersonSexCode" copy-namespaces="no"/>
			<xsl:copy-of select="nc:PersonSexText" copy-namespaces="no"/>
			<xsl:if test="$temporaryID != ''">
				<phisr-ext:PersonTemporaryIdentification>
					 <nc:IdentificationID>
					 	<xsl:value-of select="$temporaryID"/>
				 	</nc:IdentificationID>
				</phisr-ext:PersonTemporaryIdentification>
			</xsl:if>
			<xsl:if test="$addBookingID ='true'">
			<phisr-ext:PersonSystemAssignedIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="$subjectID"/>
				</nc:IdentificationID>
			</phisr-ext:PersonSystemAssignedIdentification>
			</xsl:if>
		</nc:Person>
	</xsl:template>
	
	<xsl:template match="nc:Identity">
		<xsl:copy-of select="." copy-namespaces="no"/>
	</xsl:template>
	
	<xsl:template match="pc-bkg-codes:PersonRaceCode">
		<pc-phi-codes:PersonRaceCode>
			<xsl:value-of select="."/>
		</pc-phi-codes:PersonRaceCode>
	</xsl:template>
	
	<xsl:template match="ac-bkg-codes:PersonRaceCode">
		<ac-phi-codes:PersonRaceCode>
			<xsl:value-of select="."/>
		</ac-phi-codes:PersonRaceCode>
	</xsl:template>
	
</xsl:stylesheet>