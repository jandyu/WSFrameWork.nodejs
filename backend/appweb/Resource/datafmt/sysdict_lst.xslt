<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="html" indent="yes"/>


  <xsl:template match="/xmldata">
    var sysDict = sysDict.create({				
		<xsl:value-of select="name(/xmldata/property/child::*)"/>:{			
		<xsl:apply-templates select="data//d" mode="name">
		</xsl:apply-templates>
    }
	});
  </xsl:template>

  <xsl:template match="d" mode="name">"val_<xsl:value-of select="val"/>":{title:"<xsl:value-of select="title"/>",class:"blue"},
  </xsl:template>
</xsl:stylesheet>


