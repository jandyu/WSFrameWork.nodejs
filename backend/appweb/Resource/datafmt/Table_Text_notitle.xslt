<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
  <xsl:output method="text" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>

  <xsl:template match="/xmldata">    
    <xsl:apply-templates select="/xmldata/data/*[name()=$xmltitle]/d"></xsl:apply-templates>    
  </xsl:template>

  <xsl:template match="d">
    <xsl:for-each select="./*">
      <xsl:if test="not(position()=last())"><xsl:value-of select="."/></xsl:if><xsl:if test="not(position()=last()) and not(position()=last()-1)">,</xsl:if>
    </xsl:for-each>
    <xsl:text>
</xsl:text>
  </xsl:template>
  
  
</xsl:stylesheet>
