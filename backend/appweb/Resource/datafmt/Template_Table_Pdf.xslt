<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl" xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <xsl:output method="xml" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>

  <!--要显示的表格行数-->
  <xsl:variable name="numberTr">
    <xsl:value-of select="count(/xmldata/data/*[name()=$xmltitle]/d)"/>
  </xsl:variable>

  <!--要显示的表格列数-->
  <xsl:variable name="numberTd">
    <xsl:value-of select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol ='checkbox')])"/>
  </xsl:variable>

  <!--取得小计或合计要跨行的数量-->
  <xsl:variable name="numberSumTd">
    <xsl:call-template name="sumNum">
    </xsl:call-template>
  </xsl:variable>
  <xsl:template name="sumNum">
    <xsl:variable name="num1" select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and @edtcontrol='checkbox'])"></xsl:variable>
    <xsl:variable name="num2">
      <xsl:call-template name="sumNum2"></xsl:call-template>
    </xsl:variable>
    <xsl:if test="$num1 &gt;= 1">
      <xsl:value-of select="$num2 +- 1"/>
    </xsl:if>
    <xsl:if test="$num1 &lt; 1">
      <xsl:value-of select="$num2"/>
    </xsl:if>
  </xsl:template>
  <xsl:template name="sumNum2">
    <xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[contains(@cando,'sum') and @cIndex &gt;= 0]/@cIndex">
      <xsl:sort order="ascending" select="." data-type="number"/>
      <xsl:if test="position() = 1">
        <xsl:value-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <!--取得小计或合计要跨行的数量-->
  <xsl:variable name="numberSySumTd">
    <xsl:call-template name="sumNum1">
    </xsl:call-template>
  </xsl:variable>
  <xsl:template name="sumNum1">
    <xsl:variable name="num1" select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and @edtcontrol='checkbox'])"></xsl:variable>
    
    <xsl:if test="$num1 &gt;= 1">
      <xsl:value-of select="$numberSumTd + 1"/>
    </xsl:if>
    <xsl:if test="$num1 &lt; 1">
      <xsl:value-of select="$numberSumTd"/>
    </xsl:if>
  </xsl:template>
  

  <!--基本结构-->
  <xsl:template name="printTableContent">

    <!--定义表的标题-->
    <fo:block font-size="0.5cm" font-weight="bold" text-align="center" font-family="宋体" space-after="0.2cm">
      <xsl:value-of select="property/*[name()=$xmltitle]/@title"/>
    </fo:block>

    <fo:table table-layout="fixed" font-family="宋体" text-align="start">

      <!--定义列的宽度-->
      <xsl:call-template name="printTableHead">
        <xsl:with-param name="style">0</xsl:with-param>
      </xsl:call-template>

      <!--定义表头-->
      <fo:table-header>
        <fo:table-row>
          <xsl:call-template name="printTableHead">
            <xsl:with-param name="style">1</xsl:with-param>
          </xsl:call-template>
        </fo:table-row>
      </fo:table-header>

      <!--定义表格内容-->
      <fo:table-body >

        <!--定义表内容-->
        <xsl:call-template name="printTbody"></xsl:call-template>

        <!--判断是否需要合计，即判断是否具有合计字段-->

        <!--判断是否需要合计，即判断是否具有合计字段-->
        <xsl:if test="not($numberSumTd='') and not($numberSumTd='NaN')">
          <fo:table-row>
            <fo:table-cell border-style="solid" border-color="black" border-width="0.01cm" padding-before="0.07cm" padding-after="0.07cm" padding-start="0.05cm" padding-end="0.05cm">
              <xsl:attribute name="number-columns-spanned">
                <xsl:value-of select="$numberSumTd"/>
              </xsl:attribute>
              <fo:block text-align="center" font-size="0.35cm" font-weight="bold">
                合计
              </fo:block>
            </fo:table-cell>
            <xsl:call-template name="printTfoot">
            </xsl:call-template>
          </fo:table-row>
        </xsl:if>
      </fo:table-body>
      
    </fo:table>
  </xsl:template>

  <!--定义列表的宽度和表头-->
  <xsl:template name="printTableHead">
    <!--类型 0：定义列宽度 1：定义表头-->
    <xsl:param name="style"></xsl:param>

    <xsl:for-each select="fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol ='checkbox')]">
      <xsl:sort select="@cIndex" data-type="number" order="ascending"/>

      <xsl:variable name="Style" select="substring-before(substring-after(@set,'pdf('),')')"></xsl:variable>

      <!--定义列宽度-->
      <xsl:if test="$style = 0">
        <fo:table-column>
          <!--设置宽度属性column-width-->
          <xsl:if test="not($Style = '')">
            <xsl:attribute name="column-width">
              <xsl:value-of select="concat(substring-before($Style,','),'cm')"/>
            </xsl:attribute>
          </xsl:if>
        </fo:table-column>
      </xsl:if>
      <!--定义表头-->
      <xsl:if test="$style = 1">
        <fo:table-cell border-style="solid" border-color="black" border-width="0.01cm" padding-before="0.07cm" padding-after="0.07cm" padding-start="0.05cm" padding-end="0.05cm">
          <fo:block text-align="center" font-size="0.35cm" font-weight="bold">
            <!--判断是否序号列-->
            <xsl:if test="not(@edtcontrol = 'seq')">
              <xsl:value-of select="@title"/>
            </xsl:if>
          </fo:block>
        </fo:table-cell>
      </xsl:if>

    </xsl:for-each>
  </xsl:template>

  <!--定义表内容-->
  <xsl:template name="printTbody">
    <xsl:for-each select="/xmldata/data/*[name()=$xmltitle]/d">
      <xsl:sort data-type="number" order="ascending" select="@rid"/>

      <xsl:variable name="Sort" select="position()"></xsl:variable>

      <fo:table-row>
        <xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol='checkbox')]">
          <xsl:sort order="ascending" select="@cIndex" data-type="number"/>

          <xsl:variable name="Edtcontrol" select="@edtcontrol"/>
          <xsl:variable name="Style" select="substring-before(substring-after(@set,'pdf('),')')"></xsl:variable>
          <xsl:variable name="Name" select="@name"></xsl:variable>

          <fo:table-cell border-style="solid" border-color="black" border-width="0.01cm" padding-before="0.07cm" padding-after="0.07cm" padding-start="0.05cm" padding-end="0.05cm">
            <xsl:if test="$Edtcontrol = 'seq'">
              <fo:block text-align="center" font-size="0.3cm">
                <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/d[$Sort]/@rid"/>
              </fo:block>
            </xsl:if>
            <xsl:if test="not ($Edtcontrol = 'seq')">
              <fo:block font-size="0.3cm">
                <xsl:if test="not($Style = '')">
                  <xsl:attribute name="text-align">
                    <xsl:value-of select="substring-after($Style,',')"/>
                  </xsl:attribute>
                </xsl:if>
                <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/d[$Sort]/*[name()=$Name]"/>
              </fo:block>
            </xsl:if>
          </fo:table-cell>
        </xsl:for-each>
      </fo:table-row>
    </xsl:for-each>
  </xsl:template>

  <!--合计的内容-->
  <xsl:template name="printTfoot">
    <!--遍历所有符合要求的字段，匹配当前列-->
    
    <xsl:for-each select="fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= ($numberSySumTd) and not(@edtcontrol = 'checkbox')]">
      <xsl:sort select="@cIndex" data-type="number" order="ascending"/>

      <xsl:variable name="Name" select="@name"/>

      <fo:table-cell border-style="solid" border-color="black" border-width="0.01cm" padding-before="0.07cm" padding-after="0.07cm" padding-start="0.05cm" padding-end="0.05cm">
        <!--是合计字段-->
        <xsl:if test="contains(@cando,'sum')">
          <!--设置对齐方式-->
          <xsl:attribute name="text-align">end</xsl:attribute>
          <fo:block font-size="0.3cm">
            <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/s/@*[name()=$Name]"/>
          </fo:block>
        </xsl:if>
        <!--不是合计字段-->
        <xsl:if test="not(contains(@cando,'sum'))">
          <fo:block></fo:block>
        </xsl:if>
      </fo:table-cell>

    </xsl:for-each>
    
  </xsl:template>

</xsl:stylesheet>
