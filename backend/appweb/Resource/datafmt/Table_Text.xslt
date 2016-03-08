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
    <xsl:apply-templates select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt; 0]" mode="title">
	</xsl:apply-templates><xsl:text>
</xsl:text>
    <xsl:apply-templates select="/xmldata/data/*[name()=$xmltitle]/d" mode="row"></xsl:apply-templates>    
  </xsl:template>
  <xsl:template match="item" mode="title" ><xsl:value-of select="@title"/><xsl:if test="not(position()=last())">,</xsl:if>
  </xsl:template>
	
  <xsl:template match="d" mode="row" ><xsl:variable name="rid" select="@rid"/><xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt; 0]"><xsl:variable name="colname" select="@name"/><xsl:value-of select="//d[@rid=$rid]/*[name()=$colname]"/>,</xsl:for-each><xsl:text>
</xsl:text>  
  </xsl:template>
  
  
</xsl:stylesheet>
