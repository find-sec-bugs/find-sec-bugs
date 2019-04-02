<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rt="http://xml.apache.org/xalan/java/java.lang.Runtime"
                exclude-result-prefixes="date">
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:text>Quote requested for: </xsl:text><blink><xsl:value-of select="stock/symbol"/></blink>
    </xsl:template>
</xsl:stylesheet>