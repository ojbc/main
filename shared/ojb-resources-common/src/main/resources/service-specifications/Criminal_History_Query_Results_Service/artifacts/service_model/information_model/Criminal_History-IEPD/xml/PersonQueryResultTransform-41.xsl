<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:j="http://niem.gov/niem/domains/jxdm/4.1" xmlns:rap="http://nlets.org/niem2/rapsheet/1.0" xmlns:ch-doc="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0" xmlns:ch-ext="http://ojbc.org/IEPD/Extensions/CriminalHistory/1.0" xmlns:nc="http://niem.gov/niem/niem-core/2.0" xmlns:s="http://niem.gov/niem/structures/2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" exclude-result-prefixes="fn xsl"/>
	<xsl:template match="/DATA">
		<xsl:variable name="pubAccessIndicator" select="CONTROL/SEARCH_MESSAGE"/>
		<ch-doc:CriminalHistory>
			<ch-ext:RapSheet>
				<rap:Metadata>
					<nc:ReportingOrganizationText>CJIS-Hawaii
					</nc:ReportingOrganizationText>
				</rap:Metadata>
				<rap:Introduction>
					<rap:RapSheetRequest>
						<rap:PurposeCode>
							<xsl:text>A</xsl:text>
							<!--just for validation purposes -->
						</rap:PurposeCode>
						<rap:Attention>**CONFIDENTIAL INFORMATION FOR CRIMINAL JUSTICE
							AGENCIES ONLY**</rap:Attention>
						<xsl:apply-templates select="PERSON_WITH/ROW" mode="introduction"/>
					</rap:RapSheetRequest>
				</rap:Introduction>
				<xsl:apply-templates select="PERSON_WITH/ROW" mode="person">
					<xsl:with-param name="pubAccessIndicator" select="$pubAccessIndicator"/>
				</xsl:apply-templates>
				<xsl:if test="//PER_FP_CLASS_MSG !='' or //PER_DNA_STATUS !=''">
					<rap:PersonBiometricsAssociation>
						<nc:PersonReference>
							<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(PERSON_WITH/ROW)"/></xsl:attribute>
						</nc:PersonReference>
						<xsl:apply-templates select="//PER_DNA_STATUS[. != '']"/>
						<xsl:apply-templates select="//PER_FP_CLASS_MSG[. != '']"/>
						<rap:PersonBiometrics/>
					</rap:PersonBiometricsAssociation>
				</xsl:if>
				<xsl:apply-templates select="PERSON_WITH/ROW/ADDRESS/ROW" mode="location"/>
				<xsl:apply-templates select="CUSTODY_VIEW/ROW/CUS_FACILITY_X"/>
				<xsl:apply-templates select="SUPERVISION_VIEW/ROW/SUP_ORI_X"/>
				<xsl:apply-templates select="ARREST_LIST/ROW/ARR_ARR_ORI_X"/>
				<xsl:apply-templates select="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW/CHG_ARR_PROS_ORI_X"/>
				<xsl:apply-templates select="PERSON_WITH/ROW/ADDRESS/ROW" mode="residence"/>
				<xsl:apply-templates select="CUSTODY_VIEW/ROW" mode="supervision"/>
				<xsl:apply-templates select="SUPERVISION_VIEW/ROW" mode="supervision"/>
				<!-- for full rap sheets -->
				<xsl:apply-templates select="ARREST_LIST/ROW" mode="arrest"/>
				<xsl:apply-templates select="ARREST_LIST/ROW" mode="association"/>
				<!-- For conviction only rap sheets -->
				<xsl:apply-templates select="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="convictiononly"/>
				<xsl:apply-templates select="CUSTODY_VIEW/ROW" mode="association"/>
				<xsl:apply-templates select="SUPERVISION_VIEW/ROW" mode="association"/>
				<xsl:apply-templates select="TRO_DOCUMENT_SUMMARY/ROW"/>
				<xsl:apply-templates select="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="arrestAgencyAssociation"/>
			</ch-ext:RapSheet>
		</ch-doc:CriminalHistory>
	</xsl:template>
	<xsl:template match="PER_DNA_STATUS">
		<nc:Biometric>
			<nc:BiometricValueText>
				<xsl:value-of select="."/>
			</nc:BiometricValueText>
			<nc:BiometricDescriptionText>DNA Sample
			</nc:BiometricDescriptionText>
		</nc:Biometric>
	</xsl:template>
	<xsl:template match="PER_FP_CLASS_MSG">
		<nc:Biometric>
			<nc:BiometricValueText>
				<xsl:value-of select="."/>
			</nc:BiometricValueText>
			<nc:BiometricDescriptionText>Fingerprint Class
				</nc:BiometricDescriptionText>
		</nc:Biometric>
	</xsl:template>
	<xsl:template match="TRO_DOCUMENT_SUMMARY/ROW">
		<ch-ext:Order>
			<xsl:apply-templates select="TRO_DOCUMENT_ID[. != '']"/>
			<xsl:apply-templates select="TRO_CREATED_AS_X[. != '']"/>
			<xsl:apply-templates select="TRO_FILE_DATE_USA[. != '']"/>
			<xsl:apply-templates select="TRO_FILE_ORI_X[. != '']"/>
			<xsl:apply-templates select="TSB_SERV_DATE_USA[. != '']"/>
			<xsl:apply-templates select="TSB_STATUS_X[. != '']"/>
			<xsl:apply-templates select="TRO_CASE_NO[. != '']"/>
			<xsl:apply-templates select="TRO_EXP_DATE_USA[. != '']"/>
		</ch-ext:Order>
	</xsl:template>
	<xsl:template match="TRO_CASE_NO">
		<ch-ext:CourtCase>
			<nc:ActivityIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</nc:ActivityIdentification>
		</ch-ext:CourtCase>
	</xsl:template>
	<xsl:template match="TRO_FILE_DATE_USA">
		<nc:ActivityDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="TSB_STATUS_X">
		<j:CourtOrderServiceDescriptionText>
			<xsl:value-of select="."/>
		</j:CourtOrderServiceDescriptionText>
	</xsl:template>
	<xsl:template match="TRO_DOCUMENT_ID">
		<nc:ActivityIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
			<nc:IdentificationCategoryText>TRO Document ID
			</nc:IdentificationCategoryText>
		</nc:ActivityIdentification>
	</xsl:template>
	<xsl:template match="TRO_CREATED_AS_X">
		<nc:ActivityCategoryText>
			<xsl:value-of select="."/>
		</nc:ActivityCategoryText>
	</xsl:template>
	<xsl:template match="TRO_EXP_DATE_USA">
		<ch-ext:ProtectionOrderExpirationDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</ch-ext:ProtectionOrderExpirationDate>
	</xsl:template>
	<xsl:template match="TRO_FILE_ORI_X">
		<j:CourtOrderIssuingCourt>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</j:CourtOrderIssuingCourt>
	</xsl:template>
	<xsl:template match="TSB_SERV_DATE_USA">
		<j:CourtOrderServiceDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</j:CourtOrderServiceDate>
	</xsl:template>
	<xsl:template match="SUPERVISION_VIEW/ROW" mode="association">
		<rap:SupervisionAgencyAssociation>
			<rap:SupervisionReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id()"/></xsl:attribute>
			</rap:SupervisionReference>
			<nc:OrganizationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(SUP_ORI_X)"/></xsl:attribute>
			</nc:OrganizationReference>
		</rap:SupervisionAgencyAssociation>
	</xsl:template>
	<xsl:template match="CUSTODY_VIEW/ROW" mode="association">
		<rap:SupervisionAgencyAssociation>
			<rap:SupervisionReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id()"/></xsl:attribute>
			</rap:SupervisionReference>
			<nc:OrganizationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(CUS_FACILITY_X)"/></xsl:attribute>
			</nc:OrganizationReference>
		</rap:SupervisionAgencyAssociation>
	</xsl:template>
	<xsl:template match="CUSTODY_VIEW/ROW" mode="supervision">
		<ch-ext:RapSheetCycle>
			<xsl:apply-templates select="CUS_ADM_DATE" mode="earliestDate"/>
			<rap:Supervision>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<nc:ActivityCategoryText>CUSTODY</nc:ActivityCategoryText>
				<xsl:apply-templates select="CUS_ADM_DATE" mode="activityDate"/>
				<xsl:apply-templates select="CUS_STATUS_X"/>
				<xsl:choose>
					<xsl:when test="CUS_REL_DATE!=''">
						<xsl:apply-templates select="CUS_REL_DATE"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="CUS_SCHED_REL_DATE[. != '']"/>
					</xsl:otherwise>
				</xsl:choose>
			</rap:Supervision>
		</ch-ext:RapSheetCycle>
	</xsl:template>
	<xsl:template match="CUS_ADM_DATE" mode="earliestDate">
		<rap:CycleEarliestDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</rap:CycleEarliestDate>
	</xsl:template>
	<xsl:template match="SUPERVISION_VIEW/ROW" mode="supervision">
		<ch-ext:RapSheetCycle>
			<xsl:apply-templates select="SUP_START_DATE" mode="earliestDate"/>
			<rap:Supervision>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<nc:ActivityCategoryText>SUPERVISION</nc:ActivityCategoryText>
				<xsl:apply-templates select="SUP_START_DATE" mode="activityDate"/>
				<xsl:apply-templates select="SUP_STATUS_X"/>
				<xsl:apply-templates select="SUP_END_DATE[. != ''] "/>
			</rap:Supervision>
		</ch-ext:RapSheetCycle>
	</xsl:template>
	<xsl:template match="SUP_START_DATE" mode="earliestDate">
		<rap:CycleEarliestDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</rap:CycleEarliestDate>
	</xsl:template>
	<xsl:template match="CUS_STATUS_X">
		<nc:SupervisionCustodyStatus>
			<nc:StatusDescriptionText>
				<xsl:value-of select="."/>
			</nc:StatusDescriptionText>
		</nc:SupervisionCustodyStatus>
	</xsl:template>
	<xsl:template match="SUP_STATUS_X">
		<nc:SupervisionCustodyStatus>
			<nc:StatusDescriptionText>
				<xsl:value-of select="."/>
			</nc:StatusDescriptionText>
		</nc:SupervisionCustodyStatus>
	</xsl:template>
	<xsl:template match="SUP_END_DATE">
		<nc:SupervisionRelease>
			<nc:ActivityDate>
				<nc:Date>
					<xsl:value-of select="."/>
				</nc:Date>
			</nc:ActivityDate>
		</nc:SupervisionRelease>
	</xsl:template>
	<xsl:template match="CUS_REL_DATE">
		<nc:SupervisionRelease>
			<nc:ActivityDate>
				<nc:Date>
					<xsl:value-of select="."/>
				</nc:Date>
			</nc:ActivityDate>
		</nc:SupervisionRelease>
	</xsl:template>
	<xsl:template match="CUS_SCHED_REL_DATE">
		<nc:SupervisionRelease>
			<nc:ActivityDate>
				<nc:Date>
					<xsl:value-of select="."/>
				</nc:Date>
			</nc:ActivityDate>
		</nc:SupervisionRelease>
	</xsl:template>
	<xsl:template match="CUS_ADM_DATE" mode="activityDate">
		<nc:ActivityDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="SUP_START_DATE" mode="activityDate">
		<nc:ActivityDate>
			<nc:Date>
				<xsl:value-of select="."/>
			</nc:Date>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="PERSON_WITH/ROW/ADDRESS/ROW" mode="residence">
		<nc:ResidenceAssociation>
			<nc:PersonReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(../../../ROW)"/></xsl:attribute>
			</nc:PersonReference>
			<nc:LocationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id()"/></xsl:attribute>
			</nc:LocationReference>
		</nc:ResidenceAssociation>
	</xsl:template>
	<xsl:template match="PERSON_WITH/ROW/ADDRESS/ROW" mode="location">
		<nc:Location>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:LocationAddress>
				<nc:AddressFullText>
					<xsl:value-of select="ADR_STREET_ADDR1"/>
					<xsl:text>, </xsl:text>
					<xsl:value-of select="ADR_STREET_ADDR2"/>
				</nc:AddressFullText>
			</nc:LocationAddress>
		</nc:Location>
	</xsl:template>
	<xsl:template match="ARREST_LIST/ROW" mode="association">
		<rap:ArrestAgencyAssociation>
			<nc:ActivityReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id()"/></xsl:attribute>
			</nc:ActivityReference>
			<nc:OrganizationReference>
				<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(ARR_ARR_ORI_X)"/></xsl:attribute>
			</nc:OrganizationReference>
		</rap:ArrestAgencyAssociation>
	</xsl:template>
	<!-- Arrest agency association for a conviction only rap sheet -->
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="arrestAgencyAssociation">
		<xsl:if test="CHG_ARR_PROS_ORI_X[. = 'HAWAII PD' or . = 'HAWAII SHERIFF' or . = 'HONOLULU PD' or . ='HON SHERIFF' or . ='HON SHER (OLD)' or . ='KAUAI PD' or . ='KAUAI SHERIFF' or . ='MAUI PD' or . ='MAUI SHERIFF' or . ='AG INVESTIGATOR']">
			<rap:ArrestAgencyAssociation>
				<nc:ActivityReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="generate-id()"/></xsl:attribute>
				</nc:ActivityReference>
				<nc:OrganizationReference>
					<xsl:attribute name="s:ref"><xsl:value-of select="generate-id(CHG_ARR_PROS_ORI_X)"/></xsl:attribute>
				</nc:OrganizationReference>
			</rap:ArrestAgencyAssociation>
		</xsl:if>
	</xsl:template>
	<xsl:template match="CUSTODY_VIEW/ROW/CUS_FACILITY_X">
		<rap:Agency>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</rap:Agency>
	</xsl:template>
	<xsl:template match="SUPERVISION_VIEW/ROW/SUP_ORI_X">
		<rap:Agency>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</rap:Agency>
	</xsl:template>
	<xsl:template match="ARREST_LIST/ROW/ARR_ARR_ORI_X">
		<rap:Agency>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</rap:Agency>
	</xsl:template>
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW/CHG_ARR_PROS_ORI_X">
		<rap:Agency>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<nc:OrganizationName>
				<xsl:value-of select="."/>
			</nc:OrganizationName>
		</rap:Agency>
	</xsl:template>
	<!-- Conviction only rap sheet. There is no arrest in this kind of rap sheet 
		so no way to tie charges or other segments to an arrest -->
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="convictiononly">
		<ch-ext:RapSheetCycle>
			<xsl:apply-templates select="ARR_ARR_DATE_USA" mode="earliest"/>
			<xsl:apply-templates select="CHG_OFF_DATE_USA[. != '']" mode="incidentDate"/>
			<xsl:apply-templates select="." mode="arrestConviction"/>
			<xsl:apply-templates select="." mode="prosecutionConviction"/>
			<xsl:apply-templates select="." mode="courtActionConviction"/>
			<xsl:apply-templates select="SENTENCE/ROW"/>
		</ch-ext:RapSheetCycle>
	</xsl:template>
	<!-- Full rap sheet, initiated by an arrest -->
	<xsl:template match="ARREST_LIST/ROW" mode="arrest">
		<xsl:variable name="arrestOTN" select="ARR_OTN"/>
		<xsl:variable name="arrestDate" select="ARR_ARR_DATE_USA"/>
		<ch-ext:RapSheetCycle>
			<!-- Rapsheet earliest date -->
			<xsl:apply-templates select="ARR_ARR_DATE_USA" mode="earliest"/>
			<!-- if an arrest has more than 1 charge, each charge lists the offense 
				date, the offense dates will always be the same, this is grabbing the first 
				offense date. -->
			<xsl:apply-templates select="//CHARGE_STD_VIEW/ROW[ARR_OTN=$arrestOTN][1]" mode="incidentDate"/>
			<rap:Arrest>
				<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
				<xsl:apply-templates select="ARR_ARR_DATE_USA" mode="arrestdate"/>
				<xsl:apply-templates select="ARR_OTN"/>
				<!-- This will print out arrest charges for a given arrest, ties arrest 
					to charges using OTN -->
				<xsl:apply-templates select="//CHARGE_STD_VIEW/ROW[ARR_OTN=$arrestOTN]" mode="arrest"/>
			</rap:Arrest>
			<!-- Build out the prosecution segment -->
			<xsl:apply-templates select="//CHARGE_STD_VIEW/ROW[ARR_OTN=$arrestOTN]" mode="prosecution"/>
			<!-- Build out the prosecution segment -->
			<xsl:apply-templates select="//CHARGE_STD_VIEW/ROW[ARR_OTN=$arrestOTN] " mode="courtAction"/>
			<!-- Build out the sentence segment -->
			<xsl:apply-templates select="//SENTENCE/ROW[../../ARR_OTN=$arrestOTN]"/>
		</ch-ext:RapSheetCycle>
	</xsl:template>
	<xsl:template match="CHARGE_STD_VIEW/ROW" mode="incidentDate">
		<xsl:apply-templates select="CHG_OFF_DATE_USA[. != '']" mode="incidentDate"/>
	</xsl:template>
	<xsl:template match="CHG_OFF_DATE_USA" mode="incidentDate">
		<rap:Incident>
			<nc:ActivityDate>
				<xsl:call-template name="ConvertDate">
					<xsl:with-param name="addDate">yes</xsl:with-param>
				</xsl:call-template>
			</nc:ActivityDate>
		</rap:Incident>
	</xsl:template>
	<!-- Arrest segment for a conviction only rap sheet -->
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="arrestConviction">
		<rap:Arrest>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="ARR_ARR_DATE_USA" mode="arrestdate"/>
			<xsl:apply-templates select="ARR_OTN"/>
			<xsl:if test="CHG_ARR_PROS_ORI_X[. = 'HAWAII PD' or . = 'HAWAII SHERIFF' or . = 'HONOLULU PD' or . ='HON SHERIFF' or . ='HON SHER (OLD)' or . ='KAUAI PD' or . ='KAUAI SHERIFF' or . ='MAUI PD' or . ='MAUI SHERIFF' or . ='AG INVESTIGATOR']">
				<rap:ArrestCharge>
					<xsl:apply-templates select="CHG_ARR_PROS_CHG_X[. != '']"/>
					<xsl:apply-templates select="CHG_CHARGE_ID[. != '']"/>
					<xsl:apply-templates select="CHG_ARR_PROS_SEV_X[. != '']"/>
					<xsl:apply-templates select="CHG_ARN[. != '']"/>
				</rap:ArrestCharge>
			</xsl:if>
			<rap:ArrestCharge/>
		</rap:Arrest>
	</xsl:template>
	<!-- Court Action segment for full rap sheet -->
	<xsl:template match="CHARGE_STD_VIEW/ROW" mode="courtAction">
		<!-- This if statement ensures that court action information won't show 
			up if the charge was disposed at prosecution or arrest -->
		<xsl:if test="CHG_CRT_CHG_X !='' or DISP_ORI_X[
				.='HAWAII CCT'
				or .='HI DCT (OLD1)'
				or .='KONA DCT'
				or .='HILO DCT'
				or .='HAWAII FAM CT'
				or .='KONA CCT'
				or .='KNFCT PROB'
				or .='JUD HUMAN RES'
				or .='HONOLULU CCT'
				or .='HON DCT (OLD)'
				or .='KAUAI DCT (OLD)'
				or .='HONOLULU DCT'
				or .='HONOLULU FAM CT'
				or .='KAUAI CCT (OLD)'
				or .='KAUAI CCT'
				or .='KAUAI DCT'
				or .='HI DCT (OLD2)'
				or .='KAUAI FAM CT'
				or .='MAUI CCT'
				or .='MAUI DCT'
				or .='MAUI FAM CT'
				or .='LAHAINA DCT'
				or .='MOLOKAI DCT'
				]">
			<rap:CourtAction>
				<rap:CourtCharge>
					<xsl:apply-templates select="CHG_CRT_CHG_X[. != '']"/>
					<xsl:if test="DISP_ORI_X[
						.='HAWAII CCT' 
						or .='HI DCT (OLD1)'
						or .='KONA DCT'
						or .='HILO DCT'
						or .='HAWAII FAM CT'
						or .='KONA CCT'
						or .='KNFCT PROB'
						or .='JUD HUMAN RES'
						or .='HONOLULU CCT'
						or .='HON DCT (OLD)'
						or .='KAUAI DCT (OLD)'
						or .='HONOLULU DCT'
						or .='HONOLULU FAM CT'
						or .='KAUAI CCT (OLD)'
						or .='KAUAI CCT'
						or .='KAUAI DCT'
						or .='HI DCT (OLD2)'
						or .='KAUAI FAM CT'
						or .='MAUI CCT'
						or .='MAUI DCT'
						or .='MAUI FAM CT'
						or .='LAHAINA DCT'
						or .='MOLOKAI DCT'
				]">
						<j:ChargeDisposition>
							<xsl:apply-templates select="DISP_DISP_DATE_USA[. != '']"/>
							<xsl:apply-templates select="DISP_DISP_X"/>
						</j:ChargeDisposition>
					</xsl:if>
					<xsl:apply-templates select="CHG_CHARGE_ID[. != '']"/>
					<xsl:apply-templates select="CHG_CRT_SEV[. != '']"/>
					<xsl:apply-templates select="CHG_ARN[. != '']"/>
					<xsl:apply-templates select="CHG_CRT_CHG[. != '']"/>
				</rap:CourtCharge>
				<xsl:apply-templates select="DISP_CASE_NO[. != '']"/>
			</rap:CourtAction>
		</xsl:if>
	</xsl:template>
	<xsl:template match="DISP_CASE_NO">
		<rap:CourtRecordIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</rap:CourtRecordIdentification>
	</xsl:template>
	<!-- Court Action segment for conviction only rap sheet -->
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="courtActionConviction">
		<!-- This if statement ensures that court action information won't show 
			up if the charge was disposed at prosecution or arrest -->
		<xsl:if test="CHG_CRT_CHG_X !='' or DISP_ORI_X[
				.='HAWAII CCT'
				or .='HI DCT (OLD1)'
				or .='KONA DCT'
				or .='HILO DCT'
				or .='HAWAII FAM CT'
				or .='KONA CCT'
				or .='KNFCT PROB'
				or .='JUD HUMAN RES'
				or .='HONOLULU CCT'
				or .='HON DCT (OLD)'
				or .='KAUAI DCT (OLD)'
				or .='HONOLULU DCT'
				or .='HONOLULU FAM CT'
				or .='KAUAI CCT (OLD)'
				or .='KAUAI CCT'
				or .='KAUAI DCT'
				or .='HI DCT (OLD2)'
				or .='KAUAI FAM CT'
				or .='MAUI CCT'
				or .='MAUI DCT'
				or .='MAUI FAM CT'
				or .='LAHAINA DCT'
				or .='MOLOKAI DCT'
				]">
			<rap:CourtAction>
				<rap:CourtCharge>
					<xsl:apply-templates select="CHG_LAST_CHG_X[. != '']"/>
					<xsl:if test="DISP_ORI_X[
						.='HAWAII CCT' 
						or .='HI DCT (OLD1)'
						or .='KONA DCT'
						or .='HILO DCT'
						or .='HAWAII FAM CT'
						or .='KONA CCT'
						or .='KNFCT PROB'
						or .='JUD HUMAN RES'
						or .='HONOLULU CCT'
						or .='HON DCT (OLD)'
						or .='KAUAI DCT (OLD)'
						or .='HONOLULU DCT'
						or .='HONOLULU FAM CT'
						or .='KAUAI CCT (OLD)'
						or .='KAUAI CCT'
						or .='KAUAI DCT'
						or .='HI DCT (OLD2)'
						or .='KAUAI FAM CT'
						or .='MAUI CCT'
						or .='MAUI DCT'
						or .='MAUI FAM CT'
						or .='LAHAINA DCT'
						or .='MOLOKAI DCT']">
						<j:ChargeDisposition>
							<xsl:apply-templates select="DISP_ABBR_DISP_DATE_USA[. != '']"/>
							<xsl:apply-templates select="DISP_ABBR_DISP_X[. !='']"/>
						</j:ChargeDisposition>
					</xsl:if>
					<xsl:apply-templates select="CHG_CHARGE_ID[. != '']"/>
					<xsl:apply-templates select="CHG_ARR_PROS_SEV_X[. != '']"/>
					<xsl:apply-templates select="CHG_ARN[. != '']"/>
					<xsl:apply-templates select="CHG_CRT_CHG[. != '']"/>
				</rap:CourtCharge>
				<xsl:apply-templates select="DISP_CASE_NO[. != '']"/>
			</rap:CourtAction>
		</xsl:if>
	</xsl:template>
	<!-- Prosecution Segment for a full rap sheet -->
	<xsl:template match="CHARGE_STD_VIEW/ROW" mode="prosecution">
		<xsl:if test="CHG_PROS_CHG_X[. != ''] or DISP_ORI_X[.='HAWAII PROS' or .='ATTY GENERAL' or .='HONOLULU PROD' or .='KAUAI PROS' or .='MAUI PROS' or .='AG TEU HAWAII' or .='AG TEU KAUAI' or .='AG TEU MAUI' or .='AG CJD HAWAII' or .='AG CJD MAUI' or .='AG CJD KAUAI']">
			<rap:Prosecution>
				<rap:ProsecutionCharge>
					<!-- write the prosecution charge description -->
					<xsl:apply-templates select="CHG_PROS_CHG_X[. != '']"/>
					<!-- Determine if charge was disposed at prosecution -->
					<xsl:if test="DISP_ORI_X[.='HAWAII PROS' or .='ATTY GENERAL' or .='HONOLULU PROD' or .='KAUAI PROS' or .='MAUI PROS' or .='AG TEU HAWAII' or .='AG TEU KAUAI' or .='AG TEU MAUI' or .='AG CJD HAWAII' or .='AG CJD MAUI' or .='AG CJD KAUAI']">
						<j:ChargeDisposition>
							<xsl:apply-templates select="DISP_DISP_X"/>
						</j:ChargeDisposition>
					</xsl:if>
					<xsl:apply-templates select="CHG_ARN[. != '']"/>
					<xsl:apply-templates select="CHG_PROS_CHG[. != '']"/>
				</rap:ProsecutionCharge>
			</rap:Prosecution>
		</xsl:if>
	</xsl:template>
	<!-- Prosecution Segment for a conviction only rap sheet -->
	<xsl:template match="CHARGE_DETAIL/CHARGE_ABBR_VIEW/ROW" mode="prosecutionConviction">
		<xsl:if test="CHG_ARR_PROS_ORI_X[.='HAWAII PROS' or .='ATTY GENERAL' or .='HONOLULU PROD' or .='KAUAI PROS' or .='MAUI PROS' or .='AG TEU HAWAII' or .='AG TEU KAUAI' or .='AG TEU MAUI' or .='AG CJD HAWAII' or .='AG CJD MAUI' or .='AG CJD KAUAI']">
			<rap:Prosecution>
				<rap:ProsecutionCharge>
					<!-- write the prosecution charge description -->
					<xsl:apply-templates select="CHG_ARR_PROS_CHG_X[. != '']"/>
					<!-- Determine if charge was disposed at prosecution -->
					<xsl:apply-templates select="CHG_ARN[. != '']"/>
				</rap:ProsecutionCharge>
			</rap:Prosecution>
		</xsl:if>
	</xsl:template>
	<!-- Arrest charge for a full rap sheet -->
	<xsl:template match="CHARGE_STD_VIEW/ROW" mode="arrest">
		<rap:ArrestCharge>
			<xsl:apply-templates select="CHG_ARR_CHG_X[. != '']"/>
			<!-- Determine if charge was disposed at arrest -->
			<xsl:if test="DISP_ORI_X[. = 'HAWAII PD' or . = 'HAWAII SHERIFF' or . = 'HONOLULU PD' or . ='HON SHERIFF' or . ='HON SHER (OLD)' or . ='KAUAI PD' or . ='KAUAI SHERIFF' or . ='MAUI PD' or . ='MAUI SHERIFF' or . ='AG INVESTIGATOR']">
				<j:ChargeDisposition>
					<xsl:apply-templates select="DISP_DISP_X"/>
				</j:ChargeDisposition>
			</xsl:if>
			<xsl:apply-templates select="CHG_CHARGE_ID[. != '']"/>
			<xsl:apply-templates select="CHG_ARR_SEV[. != '']"/>
			<xsl:apply-templates select="CHG_ARN[. != '']"/>
			<xsl:apply-templates select="CHG_ARR_CHG[. != '']"/>
		</rap:ArrestCharge>
	</xsl:template>
	<xsl:template match="CHG_ARN">
		<j:ChargeTrackingIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</j:ChargeTrackingIdentification>
	</xsl:template>
	<xsl:template match="ARR_ARR_DATE_USA" mode="earliest">
		<rap:CycleEarliestDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</rap:CycleEarliestDate>
	</xsl:template>
	<xsl:template match="SENTENCE/ROW">
		<ch-ext:Sentencing>
			<ch-ext:Sentence>
				<xsl:apply-templates select="DISP_SENT_X" mode="disposition"/>
				<xsl:apply-templates select="DISP_SENT_X" mode="description"/>
				<rap:SentenceCharge>
					<!-- add charge description - this will be the same as arrest charge -->
					<xsl:apply-templates select="../../CHG_ARN"/>
				</rap:SentenceCharge>
			</ch-ext:Sentence>
		</ch-ext:Sentencing>
	</xsl:template>
	<xsl:template match="CHG_PROS_CHG_X">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="CHG_ARR_PROS_CHG_X">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<!-- delete this, this is being replaced by ARN -->
	<xsl:template match="ARR_OTN">
		<j:ArrestAgencyRecordIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</j:ArrestAgencyRecordIdentification>
	</xsl:template>
	<xsl:template match="DISP_SENT_X" mode="disposition">
		<nc:ActivityDisposition>
			<nc:DispositionDescriptionText>
				<xsl:value-of select="."/>
			</nc:DispositionDescriptionText>
		</nc:ActivityDisposition>
	</xsl:template>
	<xsl:template match="DISP_SENT_X" mode="description">
		<j:SentenceDescriptionText>
			<xsl:value-of select="."/>
		</j:SentenceDescriptionText>
	</xsl:template>
	<xsl:template match="DISP_DISP_X">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="."/>
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="DISP_ABBR_DISP_X">
		<nc:DispositionDescriptionText>
			<xsl:value-of select="."/>
		</nc:DispositionDescriptionText>
	</xsl:template>
	<xsl:template match="DISP_DISP_DATE_USA">
		<nc:DispositionDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="DISP_ABBR_DISP_DATE_USA">
		<nc:DispositionDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</nc:DispositionDate>
	</xsl:template>
	<xsl:template match="CHG_CRT_SEV">
		<j:ChargeSeverityText>
			<xsl:value-of select="."/>
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="CHG_ARR_PROS_SEV_X">
		<j:ChargeSeverityText>
			<xsl:value-of select="."/>
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="CHG_CRT_CHG_X">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="CHG_LAST_CHG_X">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="CHG_ARR_SEV">
		<j:ChargeSeverityText>
			<xsl:value-of select="."/>
		</j:ChargeSeverityText>
	</xsl:template>
	<xsl:template match="CHG_ARR_CHG_X">
		<j:ChargeDescriptionText>
			<xsl:value-of select="."/>
		</j:ChargeDescriptionText>
	</xsl:template>
	<xsl:template match="CHG_CHARGE_ID">
		<j:ChargeIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</j:ChargeIdentification>
	</xsl:template>
	<xsl:template match="CHG_ARR_CHG">
		<rap:ChargeStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</rap:ChargeStatute>
	</xsl:template>
	<xsl:template match="CHG_PROS_CHG">
		<rap:ChargeStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</rap:ChargeStatute>
	</xsl:template>
	<xsl:template match="CHG_CRT_CHG">
		<rap:ChargeStatute>
			<j:StatuteCodeIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</j:StatuteCodeIdentification>
		</rap:ChargeStatute>
	</xsl:template>
	<xsl:template match="ARR_ARR_DATE_USA" mode="arrestdate">
		<nc:ActivityDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</nc:ActivityDate>
	</xsl:template>
	<xsl:template match="PERSON_WITH/ROW" mode="person">
		<xsl:param name="pubAccessIndicator"/>
		<ch-ext:Person>
			<xsl:attribute name="s:id"><xsl:value-of select="generate-id()"/></xsl:attribute>
			<xsl:apply-templates select="//ADDL_IDENT/ALIAS/ROW"/>
			<xsl:apply-templates select="PER_DOB_USA[. != '']"/>
			<xsl:apply-templates select="PER_YOB[. != '']"/>
			<xsl:apply-templates select="PER_US_CITIZEN_IND[. != '']"/>
			<xsl:apply-templates select="PER_EYE_COLOR_X[. != '']"/>
			<xsl:apply-templates select="PER_HAIR_COLOR_X[. != '']"/>
			<xsl:apply-templates select="PER_HEIGHT[. != '']"/>
			<xsl:apply-templates select="PER_FULLNAME[. != '']"/>
			<xsl:apply-templates select="//ADDL_IDENT/SMT/ROW"/>
			<xsl:apply-templates select="PER_RACE_X[. != '']"/>
			<xsl:apply-templates select="PER_SEX_X[. != '']"/>
			<xsl:apply-templates select="PER_SSN[. != '']">
				<xsl:with-param name="pubAccessIndicator" select="$pubAccessIndicator"/>
			</xsl:apply-templates>
			<xsl:apply-templates select="PER_WEIGHT[. != '']"/>
			<xsl:if test="PER_FBI_NO !='' or PER_SID !='' or //ADDL_IDENT/MISC_NO/ROW/MSC_TYPE_X ='HAWAII DRIVERS LICENSE'">
				<j:PersonAugmentation>
					<xsl:apply-templates select="//ADDL_IDENT/MISC_NO/ROW/MSC_MISC_NO[../MSC_TYPE_X='HAWAII DRIVERS LICENSE']" mode="dl"/>
					<xsl:apply-templates select="PER_FBI_NO[. != '']"/>
					<xsl:apply-templates select="PER_SID[. != '']">
						<xsl:with-param name="pubAccessIndicator" select="$pubAccessIndicator"/>
					</xsl:apply-templates>
				</j:PersonAugmentation>
			</xsl:if>
			<xsl:apply-templates select="OFFENDER_INDICATOR/ROW/*[starts-with(name(), 'OFF_IND_LABEL')]"/>
			<xsl:apply-templates select="PER_STATUS_X[. != '']"/>
		</ch-ext:Person>
	</xsl:template>
	<xsl:template match="MSC_MISC_NO" mode="dl">
		<nc:DriverLicense>
			<nc:DriverLicenseIdentification>
				<nc:IdentificationID>
					<xsl:value-of select="."/>
				</nc:IdentificationID>
			</nc:DriverLicenseIdentification>
		</nc:DriverLicense>
	</xsl:template>
	<xsl:template match="OFFENDER_INDICATOR/ROW/*">
		<rap:SubjectCautionInformationText>
			<xsl:value-of select="."/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="following-sibling::*[starts-with(name(), 'OFF_IND_DESCRIPT')]"/>
		</rap:SubjectCautionInformationText>
	</xsl:template>
	<xsl:template match="PER_STATUS_X">
		<j:SubjectOffenderNoticeText>
			<xsl:value-of select="."/>
		</j:SubjectOffenderNoticeText>
	</xsl:template>
	<xsl:template match="PER_DOB_USA">
		<nc:PersonBirthDate>
			<xsl:call-template name="ConvertDate">
				<xsl:with-param name="addDate">yes</xsl:with-param>
			</xsl:call-template>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="PER_YOB">
		<nc:PersonBirthDate>
			<nc:Year>
				<xsl:value-of select="."/>
			</nc:Year>
		</nc:PersonBirthDate>
	</xsl:template>
	<xsl:template match="ADDL_IDENT/ALIAS/ROW">
		<nc:PersonAlternateName>
			<xsl:call-template name="ConvertPersonName"/>
		</nc:PersonAlternateName>
	</xsl:template>
	<xsl:template match="ADDL_IDENT/SMT/ROW">
		<nc:PersonPhysicalFeature>
			<xsl:apply-templates select="SMT_SMT[. != '']" mode="category"/>
			<xsl:apply-templates select="SMT_DESCRIPT[. != '' and . != 'NULL']"/>
			<xsl:apply-templates select="SMT_SMT[. != '']" mode="location"/>
		</nc:PersonPhysicalFeature>
	</xsl:template>
	<xsl:template match="SMT_SMT" mode="category">
		<xsl:variable name="category" select="substring-before(. , ' ')"/>
		<nc:PhysicalFeatureCategoryText>
			<xsl:value-of select="$category"/>
		</nc:PhysicalFeatureCategoryText>
	</xsl:template>
	<xsl:template match="SMT_SMT" mode="location">
		<xsl:variable name="location" select="substring-after(. , ' ')"/>
		<nc:PhysicalFeatureLocationText>
			<xsl:value-of select="$location"/>
		</nc:PhysicalFeatureLocationText>
	</xsl:template>
	<xsl:template match="SMT_DESCRIPT">
		<nc:PhysicalFeatureDescriptionText>
			<xsl:value-of select="."/>
		</nc:PhysicalFeatureDescriptionText>
	</xsl:template>
	<xsl:template match="PER_SID">
		<xsl:param name="pubAccessIndicator"/>
		<xsl:choose>
			<xsl:when test="$pubAccessIndicator = 'PUBLIC ACCESS INFORMATION'">
				</xsl:when>
			<xsl:otherwise>
				<j:PersonStateFingerprintIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="."/>
					</nc:IdentificationID>
					<nc:IdentificationJurisdictionText>HI</nc:IdentificationJurisdictionText>
				</j:PersonStateFingerprintIdentification>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="PER_FBI_NO">
		<j:PersonFBIIdentification>
			<nc:IdentificationID>
				<xsl:value-of select="."/>
			</nc:IdentificationID>
		</j:PersonFBIIdentification>
	</xsl:template>
	<xsl:template match="PER_SSN">
		<xsl:param name="pubAccessIndicator"/>
		<xsl:choose>
			<xsl:when test="$pubAccessIndicator = 'PUBLIC ACCESS INFORMATION'">
				</xsl:when>
			<xsl:otherwise>
				<nc:PersonSSNIdentification>
					<nc:IdentificationID>
						<xsl:value-of select="."/>
					</nc:IdentificationID>
				</nc:PersonSSNIdentification>
			</xsl:otherwise>
		</xsl:choose>
		<!-- 		</xsl:if> -->
	</xsl:template>
	<xsl:template match="PER_SEX_X">
		<xsl:choose>
			<xsl:when test=".='MALE'">
				<xsl:call-template name="sex">
					<xsl:with-param name="sex">
						Male
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='FEMALE'">
				<xsl:call-template name="sex">
					<xsl:with-param name="sex">
						Female
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="sex">
					<xsl:with-param name="sex">
						Unknown
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="sex">
		<xsl:param name="sex"/>
		<rap:PersonSexText>
			<xsl:value-of select="normalize-space($sex)"/>
		</rap:PersonSexText>
	</xsl:template>
	<xsl:template match="PER_RACE_X">
		<rap:PersonRaceText>
			<xsl:value-of select="."/>
		</rap:PersonRaceText>
	</xsl:template>
	<xsl:template match="PER_WEIGHT">
		<xsl:variable name="weight" select="substring-before(., ' ')"/>
		<nc:PersonWeightMeasure>
			<nc:MeasurePointValue>
				<xsl:value-of select="$weight"/>
			</nc:MeasurePointValue>
			<nc:WeightUnitCode>LBR</nc:WeightUnitCode>
		</nc:PersonWeightMeasure>
	</xsl:template>
	<xsl:template match="PER_HEIGHT">
		<xsl:variable name="feet" select='number(substring-before(., "&apos;"))'/>
		<xsl:variable name="inchesRaw" select='substring-after(., "&apos;")'/>
		<xsl:variable name="inchesClean" select="number(substring-before($inchesRaw, '&quot;'))"/>
		<xsl:variable name="feetToInches" select="$feet * 12"/>
		<xsl:variable name="heightInInches" select="$feetToInches + $inchesClean"/>
		<nc:PersonHeightMeasure>
			<nc:MeasurePointValue>
				<xsl:value-of select="$heightInInches"/>
			</nc:MeasurePointValue>
			<nc:LengthUnitCode>INH</nc:LengthUnitCode>
		</nc:PersonHeightMeasure>
	</xsl:template>
	<xsl:template match="PER_HAIR_COLOR_X">
		<xsl:choose>
			<xsl:when test=".='BLACK'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Black
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BLOND'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Blonde or Strawberry
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BROWN'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Brown
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='GRAY'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Gray Or Partially Gray
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='RED'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Red or Auburn
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='SANDY'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Sandy
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='WHITE'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						White
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BALD'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Bald
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BLUE'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Blue
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='GREEN'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Green
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='ORANGE'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Orange
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='PINK'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Pink
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='PURPLE'">
				<xsl:call-template name="HairColor">
					<xsl:with-param name="hairColor">
						Purple
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="HairColor">
		<xsl:param name="hairColor"/>
		<rap:PersonHairColorText>
			<xsl:value-of select="normalize-space($hairColor)"/>
		</rap:PersonHairColorText>
	</xsl:template>
	<xsl:template match="PER_EYE_COLOR_X">
		<xsl:choose>
			<xsl:when test=".='BLACK'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Black
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BLUE'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Blue
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='BROWN'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Brown
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='GRAY'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Gray
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='GREEN'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Green
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='HAZEL'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Hazel
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='MAROON'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Maroon
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='MULTICOLORED'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Multicolored
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test=".='PINK'">
				<xsl:call-template name="EyeColor">
					<xsl:with-param name="eyeColor">
						Pink
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="EyeColor">
		<xsl:param name="eyeColor"/>
		<rap:PersonEyeColorText>
			<xsl:value-of select="normalize-space($eyeColor)"/>
		</rap:PersonEyeColorText>
	</xsl:template>
	<xsl:template match="PER_US_CITIZEN_IND">
		<xsl:choose>
			<xsl:when test=".='Y'">
				<nc:PersonCitizenshipText>USA</nc:PersonCitizenshipText>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="PERSON_WITH/ROW" mode="introduction">
		<rap:RapSheetPerson>
			<xsl:apply-templates select="PER_DOB_USA"/>
			<xsl:apply-templates select="PER_FULLNAME"/>
		</rap:RapSheetPerson>
	</xsl:template>
	<xsl:template match="PER_FULLNAME">
		<nc:PersonName>
			<xsl:call-template name="ConvertPersonName"/>
		</nc:PersonName>
	</xsl:template>
	<xsl:template name="ConvertPersonName">
		<xsl:choose>
			<xsl:when test="contains(.,',')">
				<nc:PersonGivenName>
					<xsl:value-of select="substring-after(.,',')"/>
				</nc:PersonGivenName>
				<nc:PersonSurName>
					<xsl:value-of select="substring-before(.,',')"/>
				</nc:PersonSurName>
			</xsl:when>
			<xsl:otherwise>
				<nc:PersonFullName>
					<xsl:value-of select="."/>
				</nc:PersonFullName>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="ConvertDate">
		<xsl:param name="addDate"/>
		<xsl:choose>
			<xsl:when test="$addDate='no'">
				<xsl:value-of select="substring(.,7)"/>
				<xsl:text>-</xsl:text>
				<xsl:value-of select="substring(.,1,2)"/>
				<xsl:text>-</xsl:text>
				<xsl:value-of select="substring(.,4,2)"/>
			</xsl:when>
			<xsl:otherwise>
				<nc:Date>
					<xsl:value-of select="substring(.,7)"/>
					<xsl:text>-</xsl:text>
					<xsl:value-of select="substring(.,1,2)"/>
					<xsl:text>-</xsl:text>
					<xsl:value-of select="substring(.,4,2)"/>
				</nc:Date>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
