<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ebts="http://cjis.fbi.gov/fbi_ebts/10.0" 
    xmlns:ansi-nist="http://niem.gov/niem/biometrics/1.0" 
    xmlns:itl="http://biometrics.nist.gov/standard/2011" 
    xmlns:nc20="http://niem.gov/niem/niem-core/2.0" 
    xmlns:j="http://niem.gov/niem/domains/jxdm/4.1"
    xmlns:submsg-doc="http://ojbc.org/IEPD/Exchange/SubscriptionMessage/1.0"
    xmlns:submsg-ext="http://ojbc.org/IEPD/Extensions/Subscription/1.0">
	
	<xsl:output indent="yes" method="xml"/>
	
	<xsl:template match="/submsg-doc:SubscriptionMessage">
	
		<itl:NISTBiometricInformationExchangePackage>
			<!-- Record Type 1 -->
			<itl:PackageInformationRecord>
				<ansi-nist:RecordCategoryCode>01</ansi-nist:RecordCategoryCode>
				 <ansi-nist:Transaction>
				 </ansi-nist:Transaction>
			</itl:PackageInformationRecord>
			<!-- Record Type 2 -->
			<itl:PackageInformationRecord>
				<ansi-nist:RecordCategoryCode>02</ansi-nist:RecordCategoryCode>
				<itl:UserDefinedDescriptiveDetail>
					<ebts:DomainDefinedDescriptiveFields>
						<xsl:apply-templates select="submsg-ext:Subject"/>
					</ebts:DomainDefinedDescriptiveFields>
				</itl:UserDefinedDescriptiveDetail>
			</itl:PackageInformationRecord>
		</itl:NISTBiometricInformationExchangePackage>
	</xsl:template>
	<xsl:template match="submsg-ext:Subject">
		<ebts:RecordSubject>
			<xsl:apply-templates select="nc20:PersonBirthDate"/>
			<xsl:apply-templates select="nc20:PersonName"/>
		</ebts:RecordSubject>
	</xsl:template>
	<xsl:template match="nc20:PersonBirthDate">
		<xsl:copy-of select="." copy-namespaces="no"/>
	</xsl:template>
	<xsl:template match="nc20:PersonName">
		<ebts:PersonName>
		</ebts:PersonName>
	</xsl:template>
</xsl:stylesheet>