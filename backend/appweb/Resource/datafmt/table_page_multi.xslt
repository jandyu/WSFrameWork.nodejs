<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:import href="Template_Table_multi.xslt"/>

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/xmldata">

    <xsl:call-template name="TableContent">
      <xsl:with-param name="pagination">1</xsl:with-param>
      <xsl:with-param name="pagetype">two</xsl:with-param>
      <xsl:with-param name="yesData">1</xsl:with-param>	  
	  <xsl:with-param name="titlename"><xsl:value-of select ="name(/xmldata/property/child::*)"/>  </xsl:with-param>	  
    </xsl:call-template>
  </xsl:template>

</xsl:stylesheet>