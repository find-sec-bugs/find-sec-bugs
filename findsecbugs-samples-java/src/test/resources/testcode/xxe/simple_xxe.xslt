<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <xsl:value-of select="document('http://csrf.me/findsecbugs')"></xsl:value-of>
    </xsl:template>
</xsl:stylesheet>