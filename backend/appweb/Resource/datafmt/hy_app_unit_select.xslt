<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="html" indent="yes"/>
  <xsl:template match="/xmldata">  
	<ul id="unitlstdiv">
		<xsl:if test="/xmldata/data//d[1]/pid=0">
				<li class="button" onclick="cancelunit()">返 回<xsl:text>    </xsl:text>小区房号选择</li>		
		</xsl:if>
		<xsl:if test="/xmldata/data//d[1]/pid!=0">
				<li class="button" onclick="clickunit({/xmldata/data//d[1]/parentid})">上一步<xsl:text>    </xsl:text><xsl:value-of select="/xmldata/data//d[1]/p_title"/></li>		
		</xsl:if>			
    <xsl:apply-templates select="/xmldata/data//d" mode="item"> 
    </xsl:apply-templates>	  
	</ul>
  </xsl:template>
     
<xsl:template match="d" mode="item" >
	<xsl:if test="child &gt;0">
		<li class="havecontent" onclick="clickunit('{iid}',{child})"><xsl:value-of select="title"/></li> 
	</xsl:if>
	<xsl:if test="child = 0">
		<li class="form" onclick="clickunit('{iid}',{child},'{unit_title}')"><xsl:value-of select="title"/></li> 
	</xsl:if>
  </xsl:template>
</xsl:stylesheet>



