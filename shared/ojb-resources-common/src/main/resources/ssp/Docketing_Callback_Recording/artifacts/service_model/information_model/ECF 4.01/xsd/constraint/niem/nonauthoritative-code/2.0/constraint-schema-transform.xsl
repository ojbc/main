<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" encoding="UTF-8" indent="yes" />
<xsl:template match="xsd:enumeration[@value='adoption' and ../../@name='FamilyKinshipCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='biological' and ../../@name='FamilyKinshipCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='foster' and ../../@name='FamilyKinshipCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='marriage' and ../../@name='FamilyKinshipCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='civil union' and ../../@name='MarriageCategoryCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='common law' and ../../@name='MarriageCategoryCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='married' and ../../@name='MarriageCategoryCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:enumeration[@value='unknown' and ../../@name='MarriageCategoryCodeSimpleType']">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:schema">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:complexType">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:simpleType">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:complexContent">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:extension">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:restriction">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:sequence">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:minInclusive">
<xsl:copy>
<xsl:for-each select="@*">
<xsl:copy />
</xsl:for-each>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:simpleContent">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:schema/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<!--<xsl:template match="/xsd:schema/xsd:complexType">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:schema/xsd:simpleType">
<xsl:copy-of select="." />
</xsl:template>-->
<xsl:template match="/xsd:attribute/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="/xsd:attributeGroup/xsd:annotation">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:annotation" />
<xsl:template match="xsd:import">
<xsl:copy>
<xsl:attribute name="namespace">
<xsl:value-of select="@namespace" />
</xsl:attribute>
<xsl:attribute name="schemaLocation">
<xsl:value-of select="@schemaLocation" />
</xsl:attribute>
<xsl:apply-templates />
</xsl:copy>
</xsl:template>
<xsl:template match="xsd:attribute">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:attributeGroup">
<xsl:copy-of select="." />
</xsl:template>
<xsl:template match="xsd:element">
<xsl:copy-of select="." />
</xsl:template>
</xsl:stylesheet>
