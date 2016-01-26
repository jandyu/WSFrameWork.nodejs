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
  
  <xsl:template name="pageContainer">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>
    <xsl:param name="totalrows"></xsl:param>
    <div class="pageContainer">
      <ul>
        <li class="pagenum">
          共<xsl:value-of select="$totalrows"></xsl:value-of>行
        </li>
        <li class="pagenum">
          <xsl:attribute name="page">
            <xsl:value-of select="$totalpages"/>
          </xsl:attribute>          
          共<xsl:value-of select="$totalpages"></xsl:value-of>页
        </li>
        <li class="fristpage">
          <a href="#"><xsl:attribute name="onclick">querydat(1,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
            <xsl:text> </xsl:text>
          </a>
        </li>
        <xsl:if test="$currpage &lt;= 1">
          <li class="backpage">
            <a href="#">
              <xsl:attribute name="onclick">querydat(1,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
              <xsl:text> </xsl:text>
            </a>
          </li>
        </xsl:if>
        <xsl:if test="$currpage &gt; 1">
          <li class="backpage">
            <a id='PageDown' href="#">
              <xsl:attribute name="onclick">querydat(<xsl:value-of select="$currpage +- 1"/>,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
              <xsl:text> </xsl:text>
            </a>
          </li>
        </xsl:if>
        <li class="nowpage">
          <input type="text" style="width:30px;">
            <!--<xsl:attribute name="onkeydown">PageChange(this,'<xsl:value-of select="$totalpages"/>','<xsl:value-of select="$xmltitle"/>');</xsl:attribute>-->
            <xsl:attribute name="value">
              <xsl:value-of select="$currpage"/>
            </xsl:attribute>
          </input>
        </li>
        <xsl:if test="$currpage=$totalpages">
          <li class="nextpage">
            <a href="#">
              <xsl:attribute name="onclick">querydat(<xsl:value-of select="$totalpages"/>,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
              <xsl:text> </xsl:text>
            </a>
          </li>
        </xsl:if>
        <xsl:if test="not($currpage=$totalpages)">
          <li class="nextpage">
            <a href="#">
              <xsl:attribute name="onclick">querydat(<xsl:value-of select="$currpage+1"/>,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
              <xsl:text> </xsl:text>
            </a>
          </li>
        </xsl:if>
        <li class="lastpage">
          <a href="#">
            <xsl:attribute name="onclick">querydat(<xsl:value-of select="$totalpages"/>,'<xsl:value-of select="$xmltitle"/>');</xsl:attribute>
            <xsl:text> </xsl:text>
          </a>
        </li>
      </ul>
    </div>
  </xsl:template>
  
  <!--页码的基本结构-->
  <xsl:template name="pageContent">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="orderby"></xsl:param>
    <xsl:param name="totalrows"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>

    <div class="pagination">
      <ul class="zpager">
        <li>转到</li>
        <li>
          <input type="text" style="width:30px;" />
        </li>
        <li>
          <input type="button" value="GO" />
        </li>
      </ul>
      <!--<script>
        <xsl:value-of select="concat('queryobj.common.currpage=',$currpage,';')"/>
        <xsl:value-of select="concat('queryobj.common.orderby=',$orderby,';')"/>
      </script>-->
      <ul class="pager">
        <!--页码的主要内容-->
        <xsl:call-template name="TablePager">
          <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
          <xsl:with-param name="orderby" select="$orderby"></xsl:with-param>
          <xsl:with-param name="totalrows" select="$totalrows"></xsl:with-param>
          <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
        </xsl:call-template>
      </ul>
      <ul class="dpager">
        <li>
          共<xsl:value-of select="$totalrows"></xsl:value-of>行
        </li>
        <li>
          当前<xsl:value-of select="$currpage"></xsl:value-of>/<xsl:value-of select="$totalpages"></xsl:value-of>页
        </li>
      </ul>
    </div>
  </xsl:template>
  
  <xsl:template name="TablePager">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="orderby"></xsl:param>
    <xsl:param name="totalrows"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>

    <!--是第一页-->
    <xsl:if test="$currpage &lt;= 1">
      <li class='disabled'>首页</li>
      <li class='disabled'>&lt;&lt;上一页</li>
    </xsl:if>

    <!--不是第一页-->
    <xsl:if test="$currpage &gt; 1">
      <li>
        <a href="javascript:querydat({1});">首页</a>
      </li>
      <li>
        <a href='javascript:querydat({$currpage +- 1});' id='PageDown'>&lt;&lt;上一页</a>
      </li>
    </xsl:if>

    <xsl:choose >
      <!--页数小于5的情况，循环将其全部输出-->
      <xsl:when test='$totalpages &lt;=5 and $totalpages &gt; 0'>
        <xsl:call-template name="PageLtFive">
          <!--当前页码-->
          <xsl:with-param name="i">1</xsl:with-param>
          <!--已输出了的页码-->
          <xsl:with-param name="count">1</xsl:with-param>
          <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
          <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
        </xsl:call-template>
      </xsl:when>

      <!--页数大于5的情况,以当前页为中心-->
      <xsl:when test='$totalpages &gt;5'>
        <xsl:call-template name="PageGtFive">
          <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
          <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
    </xsl:choose>

    <!--是最后一页-->
    <xsl:if test="$currpage=$totalpages">
      <li class="disabled">&gt;&gt;下一页</li>
      <li class="disabled">末页</li>
    </xsl:if>

    <!--不是最后一页-->
    <xsl:if test="$currpage!=$totalpages">
      <li>
        <a href='javascript:querydat({$currpage+1});' id='PageDown'>&gt;&gt;下一页</a>
      </li>
      <li>
        <a href='javascript:querydat({$totalpages});' id='PageDown'>末页</a>
      </li>
    </xsl:if>
  </xsl:template>

  <!--大于5页-->
  <xsl:template name="PageGtFive">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>
    <!--以当前页为中心-->
    <!--当前页的左两页（存在）-->
    <xsl:if test="$currpage+-2&gt;0">
      <!--当前页的后两页（存在）-->
      <xsl:if test="$currpage+2 &lt;= $totalpages">
        <xsl:call-template name="PageLtFive">
          <xsl:with-param name="i" select="$currpage +- 2"></xsl:with-param>
          <xsl:with-param name="count">1</xsl:with-param>
          <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
          <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
        </xsl:call-template>
      </xsl:if>
      <!--当前页的后两页（不存在）-->
      <xsl:if test="$currpage+2&gt;$totalpages">
        <!--输出最后5页-->
        <xsl:call-template name="lastFivePages">
          <xsl:with-param name="i">4</xsl:with-param>
          <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
          <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>
    <!--当前页的左两页（不存在）-->
    <xsl:if test="$currpage+-2&lt;=0">
      <xsl:call-template name="PageLtFive">
        <xsl:with-param name="i">1</xsl:with-param>
        <xsl:with-param name="count">1</xsl:with-param>
        <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
        <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!--小于5页-->
  <xsl:template name="PageLtFive">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>
    <!--当前页码-->
    <xsl:param name="i"/>
    <!--已输出了的页码-->
    <xsl:param name="count"/>

    <li>
      <!--是否当前页-->
      <xsl:if test="$i=$currpage">
        <xsl:attribute name="class">current</xsl:attribute>
        <xsl:value-of select='$i'/>
      </xsl:if>
      <xsl:if test="$i!=$currpage">
        <a href='javascript:querydat({$i});' id='Page'>
          <xsl:value-of select='$i'/>
        </a>
      </xsl:if>
    </li>

    <!--循环-->
    <xsl:if test="(($i+1) &lt;= $totalpages) and ($count+1) &lt;=5">
      <xsl:call-template name="PageLtFive">

        <xsl:with-param name="i" select="$i + 1"></xsl:with-param>
        <xsl:with-param name="count" select="$count + 1"></xsl:with-param>
        <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
        <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!--递归输出最后5页-->
  <xsl:template name="lastFivePages">
    <xsl:param name="currpage"></xsl:param>
    <xsl:param name="totalpages"></xsl:param>

    <xsl:param name="i"></xsl:param>

    <li>
      <xsl:if test="$totalpages+-$i=$currpage">
        <xsl:attribute name="class">current</xsl:attribute>
        <xsl:value-of select='$totalpages +- $i'/>
      </xsl:if>
      <xsl:if test="$totalpages+-$i!=$currpage">
        <a href='javascript:querydat({$totalpages+-$i});' id='Page'>
          <xsl:value-of select='$totalpages +- $i'/>
        </a>
      </xsl:if>
    </li>

    <xsl:if test="($i+-1) &gt;= 0">
      <xsl:call-template name="lastFivePages">
        <xsl:with-param name="i">
          <xsl:value-of select="$i +- 1"/>
        </xsl:with-param>
        <xsl:with-param name="currpage" select="$currpage"></xsl:with-param>
        <xsl:with-param name="totalpages" select="$totalpages"></xsl:with-param>
      </xsl:call-template>
    </xsl:if>

  </xsl:template>
</xsl:stylesheet>
