<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="xml" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>

  <xsl:variable name="DataNum" select="count(/xmldata/data/*[name()=$xmltitle]/d)"/>


  <xsl:template name="ChartContent">

    <xsl:param name="Style"/>
    <xsl:param name="DataShowX"/>
    <xsl:param name="DataShowY"/>
    <xsl:param name="DataShowZ"/>
    
    <!--是否有动画-->
    <xsl:param name="animation">1</xsl:param>
    <!--颜色盘-->
    <xsl:param name="palette">2</xsl:param>
    <!--是否显示数字-->
    <xsl:param name="showValues">1</xsl:param>
    <!--数字单位-->
    <xsl:param name="numberPrefix">￥</xsl:param>
    <!--小数位数-->
    <xsl:param name="decimals">2</xsl:param>
    

    <!--标题-->
    <xsl:param name="caption" select="/xmldata/property/*[name()=$xmltitle]/@title"></xsl:param>
    <!--副标题-->
    <xsl:param name="subCaption" select="/xmldata/property/*[name()=$xmltitle]/@title"></xsl:param>
    <!--X轴名称-->
    <xsl:param name="xAxisName" select="/xmldata/fielddef/*[name()=$xmltitle]/item[@name=$DataShowX]/@title"></xsl:param>
    <!--Y轴名称-->
    <xsl:param name="yAxisName" select="/xmldata/fielddef/*[name()=$xmltitle]/item[@name=$DataShowY]/@title"></xsl:param>
    
    <!--set中的link属性-->
    <xsl:param name="Setlink"/>
    
    <chart formatNumberScale="0" >
      
      <xsl:attribute name="animation">
        <xsl:value-of select="$animation"/>
      </xsl:attribute>
      
      <xsl:attribute name="palette">
        <xsl:value-of select="$palette"/>
      </xsl:attribute>
      
      <xsl:attribute name="showValues">
        <xsl:value-of select="$showValues"/>
      </xsl:attribute>
      
      <xsl:attribute name="numberPrefix">
        <xsl:value-of select="$numberPrefix"/>
      </xsl:attribute>
      
      <xsl:attribute name="decimals">
        <xsl:value-of select="$decimals"/>
      </xsl:attribute>
      
      <xsl:attribute name="caption">
        <xsl:value-of select="$caption"/>
      </xsl:attribute>
      
      <xsl:attribute name="subCaption">
        <xsl:value-of select="$subCaption"/>
      </xsl:attribute>
      
      <xsl:attribute name="xAxisName">
        <xsl:value-of select="$xAxisName"/>
      </xsl:attribute>
      
      <xsl:attribute name="yAxisName">
        <xsl:value-of select="$yAxisName"/>
      </xsl:attribute>

      <xsl:if test="$Style='Single'">
        <xsl:call-template name="SingleSetContent">
          <xsl:with-param name="DataShowLabel" select="$DataShowX"/>
          <xsl:with-param name="DataShowValue" select="$DataShowY"/>
          <xsl:with-param name="Setlink" select="$Setlink"/>
        </xsl:call-template>
      </xsl:if>
      
      <xsl:if test="$Style='Multi'">
        <xsl:call-template name="MultiDataContent">
          <xsl:with-param name="DataShowX" select="$DataShowX"/>
          <xsl:with-param name="DataShowY" select="$DataShowY"/>
          <xsl:with-param name="DataShowZ" select="$DataShowZ"/>
          <xsl:with-param name="Setlink" select="$Setlink"/>
        </xsl:call-template>
      </xsl:if>
      
      <styles>
        <definition>
          <style name='MyFirstFontStyle' type='font' font='Arial' size='14' color='000000' bold='1'/>
          <style name='MySFontStyle' type='font' font='Arial' size='12' color='000000'/>
        </definition>
        <application>
          <apply toObject='Caption' styles='MyFirstFontStyle' />
          <apply toObject='SUBCAPTION' styles='MyFirstFontStyle' />
          <apply toObject='XAXISNAME' styles='MyFirstFontStyle' />
          <apply toObject='YAXISNAME' styles='MyFirstFontStyle' />
          <apply toObject='TOOLTIP' styles='MySFontStyle' />
		  <apply toObject='DATAVALUES' styles='MySFontStyle' />
		  <apply toObject='YAXISVALUES' styles='MySFontStyle' />
		  <apply toObject='Datalabels' styles='MySFontStyle' />
		  
        </application>
      </styles>
    
    </chart>
    
  </xsl:template>
  
  
  <!--获得单表 <set label="" value=""/> 数据-->
  <xsl:template name="SingleSetContent">
    
    <!--要显示的数据字段DataShowLabel，DataShowValue-->
    <xsl:param name="DataShowLabel"/>
    <xsl:param name="DataShowValue"/>

    <xsl:param name="Setlink"/>

    <!--要显示数字的列表的顺序-->
    <xsl:param name="ListShowData"/>
    <!--要分裂的列表的顺序-->
    <xsl:param name="ListIsSliced"/>

    <xsl:for-each select="/xmldata/data/*[name()=$xmltitle]/d">
      <set>
        <xsl:attribute name="label">
          <xsl:value-of select="./*[name()=$DataShowLabel]"/>
        </xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="./*[name()=$DataShowValue]"/>
        </xsl:attribute>
        <xsl:if test="not($Setlink='')">
          <xsl:variable name="linkZD">
            <xsl:value-of select="substring-before(substring-after($Setlink,'link('),')')"/>
          </xsl:variable>
          <xsl:if test="not(contains($linkZD,','))">
            <xsl:attribute name="link">JavaScript:GraphClick('<xsl:value-of select="$xmltitle"/>','<xsl:value-of select="./*[name()=$linkZD]"/>');</xsl:attribute>
          </xsl:if>
          <xsl:if test="contains($linkZD,',')">
            <xsl:attribute name="link">JavaScript:GraphClick('<xsl:value-of select="$xmltitle"/>','<xsl:value-of select="./*[name()=substring-before($linkZD,',')]"/>,<xsl:value-of select="./*[name()=substring-after($linkZD,',')]"/>');</xsl:attribute>
          </xsl:if>
        </xsl:if>
      </set>
    </xsl:for-each>
  </xsl:template>

  <!--获得多系列的图标数据-->
  <xsl:template name="MultiDataContent">
    
    <xsl:param name="DataShowX"/>
    <xsl:param name="DataShowY"/>
    <xsl:param name="DataShowZ"/>

    <xsl:param name="Setlink"/>
    
    <!--获得X轴的数据列表-->
    <xsl:variable name="ListXData">
      <xsl:call-template name="ListGet">
        <xsl:with-param name="NowString">,</xsl:with-param>
        <xsl:with-param name="NowZD" select="$DataShowX"></xsl:with-param>
        <xsl:with-param name="nowNum">1</xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <!--获得Z轴的数据列表-->
    <xsl:variable name="ListZData">
      <xsl:call-template name="ListGet">
        <xsl:with-param name="NowString">,</xsl:with-param>
        <xsl:with-param name="NowZD" select="$DataShowZ"></xsl:with-param>
        <xsl:with-param name="nowNum">1</xsl:with-param>
      </xsl:call-template>
    </xsl:variable>

    <!--X轴的数据内容-->
    <categories>
      <xsl:call-template name="categoryContent">
        <xsl:with-param name="NowXData" select="substring-before($ListXData,',')"/>
        <xsl:with-param name="SyXData" select="substring-after($ListXData,',')"/>
      </xsl:call-template>
    </categories>
    
    <!--循环Z轴数据-->
    <xsl:call-template name="datasetContent">
      <xsl:with-param name="NowZData" select="substring-before($ListZData,',')"/>
      <xsl:with-param name="SyZData" select="substring-after($ListZData,',')"/>

      <xsl:with-param name="DataShowX" select="$DataShowX"/>
      <xsl:with-param name="DataShowY" select="$DataShowY"/>
      <xsl:with-param name="DataShowZ" select="$DataShowZ"/>

      <xsl:with-param name="ListXData" select="$ListXData"/>

      <xsl:with-param name="Setlink" select="$Setlink"/>
    </xsl:call-template>
    
  </xsl:template>

  <xsl:template name="ListGet">
    <xsl:param name="NowString"/>
    <xsl:param name="NowZD"/>
    <xsl:param name="nowNum"/>

    <xsl:if test="$nowNum &lt;= $DataNum">
      
      <xsl:if test="contains($NowString,concat(',',/xmldata/data/*[name()=$xmltitle]/d[position()=$nowNum]/*[name()=$NowZD],','))">
        <xsl:if test="not($nowNum=$DataNum)">
          <xsl:call-template name="ListGet">
            <xsl:with-param name="NowString" select="$NowString"></xsl:with-param>
            <xsl:with-param name="NowZD" select="$NowZD"></xsl:with-param>
            <xsl:with-param name="nowNum" select="$nowNum + 1"></xsl:with-param>
          </xsl:call-template>
        </xsl:if>
        <xsl:if test="$nowNum=$DataNum">
          <xsl:value-of select="substring-after($NowString,',')"/>
        </xsl:if>
      </xsl:if>
      <xsl:if test="not(contains($NowString,concat(',',/xmldata/data/*[name()=$xmltitle]/d[position()=$nowNum]/*[name()=$NowZD],',')))">
        <xsl:if test="not($nowNum=$DataNum)">
          <xsl:call-template name="ListGet">
            <xsl:with-param name="NowString" select="concat($NowString,/xmldata/data/*[name()=$xmltitle]/d[position()=$nowNum]/*[name()=$NowZD],',')"></xsl:with-param>
            <xsl:with-param name="NowZD" select="$NowZD"></xsl:with-param>
            <xsl:with-param name="nowNum" select="$nowNum + 1"></xsl:with-param>
          </xsl:call-template>
        </xsl:if>
        <xsl:if test="$nowNum=$DataNum">
          <xsl:value-of select="substring-after(concat($NowString,/xmldata/data/*[name()=$xmltitle]/d[position()=$nowNum]/*[name()=$NowZD],','),',')"/>
        </xsl:if>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <xsl:template name="categoryContent">
    <xsl:param name="NowXData"/>
    <xsl:param name="SyXData"/>

    <xsl:if test="not($NowXData='')">
      <category>
        <xsl:attribute name="label">
          <xsl:value-of select="$NowXData"/>
        </xsl:attribute>
      </category>

      <xsl:if test="not($SyXData='')">
        <xsl:call-template name="categoryContent">
          <xsl:with-param name="NowXData" select="substring-before($SyXData,',')"/>
          <xsl:with-param name="SyXData" select="substring-after($SyXData,',')"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <xsl:template name="datasetContent">
    
    <xsl:param name="NowZData"/>
    <xsl:param name="SyZData"/>

    <xsl:param name="DataShowX"/>
    <xsl:param name="DataShowY"/>
    <xsl:param name="DataShowZ"/>

    <xsl:param name="ListXData"/>

    <xsl:param name="Setlink"/>
    
    <xsl:if test="not($NowZData='')">
      <dataset>
        <xsl:attribute name="seriesName">
          <xsl:value-of select="$NowZData"/>
        </xsl:attribute>
        
        <!--当前z轴下，循环X轴数据-->
        <xsl:call-template name="setContent">
          <xsl:with-param name="NowZData" select="$NowZData"/>
          
          <xsl:with-param name="NowXData" select="substring-before($ListXData,',')"/>
          <xsl:with-param name="SyXData" select="substring-after($ListXData,',')"/>

          <xsl:with-param name="DataShowX" select="$DataShowX"/>
          <xsl:with-param name="DataShowY" select="$DataShowY"/>
          <xsl:with-param name="DataShowZ" select="$DataShowZ"/>

          <xsl:with-param name="Setlink" select="$Setlink"/>
        </xsl:call-template>
      </dataset>

      <xsl:if test="not($SyZData='')">
        <xsl:call-template name="datasetContent">
          
          <xsl:with-param name="NowZData" select="substring-before($SyZData,',')"/>
          <xsl:with-param name="SyZData" select="substring-after($SyZData,',')"/>

          <xsl:with-param name="DataShowX" select="$DataShowX"/>
          <xsl:with-param name="DataShowY" select="$DataShowY"/>
          <xsl:with-param name="DataShowZ" select="$DataShowZ"/>

          <xsl:with-param name="ListXData" select="$ListXData"/>
          
          <xsl:with-param name="Setlink" select="$Setlink"/>
        
        </xsl:call-template>
      </xsl:if>

    </xsl:if>
  </xsl:template>

  <xsl:template name="setContent">
    
    <xsl:param name="NowZData"/>
    
    <xsl:param name="NowXData"/>
    <xsl:param name="SyXData"/>

    <xsl:param name="DataShowX"/>
    <xsl:param name="DataShowY"/>
    <xsl:param name="DataShowZ"/>

    <xsl:param name="Setlink"/>

    <xsl:if test="not($NowXData='')">
      <xsl:variable name="NowValue">
        <xsl:for-each select="/xmldata/data/*[name()=$xmltitle]/d">
          <xsl:if test="./*[name()=$DataShowX]=$NowXData and ./*[name()=$DataShowZ]=$NowZData">
            <xsl:value-of select="./*[name()=$DataShowY]"/>
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>
      <!--获得Y轴的值-->
      <set>
        <xsl:attribute name="value">
          <xsl:if test="not($NowValue='')">
            <xsl:value-of select="$NowValue"/>
          </xsl:if>
          <xsl:if test="$NowValue=''">0.00</xsl:if>
        </xsl:attribute>
        <xsl:if test="not($Setlink='')">
          <xsl:variable name="linkZD">
            <xsl:value-of select="substring-before(substring-after($Setlink,'link('),')')"/>
          </xsl:variable>
          <xsl:if test="not(contains($linkZD,','))">
            <xsl:attribute name="link">JavaScript:GraphClick('<xsl:value-of select="$xmltitle"/>','<xsl:value-of select="./*[name()=$linkZD]"/>');</xsl:attribute>
          </xsl:if>
          <xsl:if test="contains($linkZD,',')">
            <xsl:attribute name="link">JavaScript:GraphClick('<xsl:value-of select="$xmltitle"/>','<xsl:value-of select="./*[name()=substring-before($linkZD,',')]"/>,<xsl:value-of select="./*[name()=substring-after($linkZD,',')]"/>');</xsl:attribute>
          </xsl:if>
        </xsl:if>
      </set>
      
      <xsl:if test="not($SyXData='')">
        <xsl:call-template name="setContent">
          <xsl:with-param name="NowZData" select="$NowZData"/>
          
          <xsl:with-param name="NowXData" select="substring-before($SyXData,',')"/>
          <xsl:with-param name="SyXData" select="substring-after($SyXData,',')"/>

          <xsl:with-param name="DataShowX" select="$DataShowX"/>
          <xsl:with-param name="DataShowY" select="$DataShowY"/>
          <xsl:with-param name="DataShowZ" select="$DataShowZ"/>

          <xsl:with-param name="Setlink" select="$Setlink"/>
          
        </xsl:call-template>
      </xsl:if>
    </xsl:if>

  </xsl:template>

</xsl:stylesheet>
