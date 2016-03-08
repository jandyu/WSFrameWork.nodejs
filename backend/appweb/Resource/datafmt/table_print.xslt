<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:import href="Template_Table_Pdf.xslt"/>

  <xsl:output method="xml" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>

  <!--要显示的表格列数-->
  <xsl:variable name="numberTd">
    <xsl:value-of select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0])"/>
  </xsl:variable>

  <!--打印页面宽度-->
  <xsl:variable name="page_width">21</xsl:variable>
  <!--打印页面高度-->
  <xsl:variable name="page_height">29.7</xsl:variable>
  <!--打印页面上部高度-->
  <xsl:variable name="margin_top">1</xsl:variable>
  <!--打印页面下部高度-->
  <xsl:variable name="margin_bottom">1</xsl:variable>
  <!--打印页面主体上部高度-->
  <xsl:variable name="body_margin_top">1</xsl:variable>
  <!--打印页面主体下部高度-->
  <xsl:variable name="body_margin_bottom">1</xsl:variable>

  <!--<xsl:variable name="page_width_pt" select="$page_width * 28.346"></xsl:variable>-->

  <!--获得宽度-->
  <xsl:variable name="margin_width">
    <xsl:call-template name="width">
      <xsl:with-param name="nowtd">0</xsl:with-param>
      <xsl:with-param name="widths">0</xsl:with-param>
    </xsl:call-template>
  </xsl:variable>
  <xsl:template name="width">
    <xsl:param name="nowtd"></xsl:param>
    <xsl:param name="widths"></xsl:param>

    <xsl:variable name="set" select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex = $nowtd and not(@edtcontrol='checkbox')]/@set"></xsl:variable>
    <xsl:variable name="margin" select="substring-before(substring-before(substring-after($set,'pdf('),')'),',')"/>

    <xsl:if test="(($nowtd + 1) = $numberTd) and not($margin='')">
      <xsl:value-of select="($page_width - ($widths + $margin)) div 2"/>
    </xsl:if>

    <xsl:if test="(($nowtd + 1) = $numberTd) and $margin=''">
      <xsl:value-of select="($page_width - $widths) div 2"/>
    </xsl:if>

    <xsl:if test="($nowtd + 1) &lt; $numberTd">
      <xsl:if test="not($margin='')">
        <xsl:call-template name="width">
          <xsl:with-param name="nowtd" select="$nowtd + 1"></xsl:with-param>
          <xsl:with-param name="widths" select="$widths + $margin"></xsl:with-param>
        </xsl:call-template>
      </xsl:if>
      <xsl:if test="$margin=''">
        <xsl:call-template name="width">
          <xsl:with-param name="nowtd" select="$nowtd + 1"></xsl:with-param>
          <xsl:with-param name="widths" select="$widths"></xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <xsl:template match="/xmldata">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

      <fo:layout-master-set>

        <fo:simple-page-master master-name="main">
          <xsl:attribute name="page-height">
            <xsl:value-of select="concat($page_height,'cm')"/>
          </xsl:attribute>
          <xsl:attribute name="page-width">
            <xsl:value-of select="concat($page_width,'cm')"/>
          </xsl:attribute>
          <xsl:attribute name="margin-top">
            <xsl:value-of select="concat($margin_top,'cm')"/>
          </xsl:attribute>
          <xsl:attribute name="margin-bottom">
            <xsl:value-of select="concat($margin_bottom,'cm')"/>
          </xsl:attribute>
          <xsl:attribute name="margin-left">
            <xsl:value-of select="concat($margin_width,'cm')"/>
          </xsl:attribute>
          <xsl:attribute name="margin-right">
            <xsl:value-of select="concat($margin_width,'cm')"/>
          </xsl:attribute>
          <fo:region-body>
            <xsl:attribute name="margin-top">
              <xsl:value-of select="concat($body_margin_top,'cm')"/>
            </xsl:attribute>
            <xsl:attribute name="margin-bottom">
              <xsl:value-of select="concat($body_margin_bottom,'cm')"/>
            </xsl:attribute>
          </fo:region-body>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <!--实际内容-->
      <fo:page-sequence master-reference="main">

        <fo:flow flow-name="xsl-region-body">
          <fo:block font-family="宋体" text-align="center">
            <!--定义表格-->
            <xsl:call-template name="printTableContent">

            </xsl:call-template>
          </fo:block>

        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>
