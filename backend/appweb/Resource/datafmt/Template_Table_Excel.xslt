<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:import href="Template_Excel_Style.xslt"/>
  
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
    <xsl:value-of select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol='checkbox')])"/>
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

  <xsl:template name="Excel_Content" xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"
     xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
     xmlns:html="http://www.w3.org/TR/REC-html40">
    
    <Table x:FullColumns="1" x:FullRows="1" ss:DefaultColumnWidth="54" ss:DefaultRowHeight="14.25">
      <!--<xsl:attribute name="ss:ExpandedColumnCount">
            <xsl:value-of select="$numberTd"/>
          </xsl:attribute>
          <xsl:attribute name="ss:ExpandedRowCount">
            <xsl:value-of select="$numberTr + $numberEditTr + 3"/>
          </xsl:attribute>-->

      <!--定义表格宽度-->
      <xsl:for-each select="fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol='checkbox')]">
        <xsl:sort order="ascending" data-type="number" select="@cIndex"/>

        <xsl:variable name="style" select="substring-before(substring-after(@set,'xls('),')')"/>
        <Column>
          <xsl:if test="position()=1">
            <xsl:attribute name="ss:Index">1</xsl:attribute>
          </xsl:if>
          <xsl:if test="not($style='')">
            <xsl:attribute name="ss:Width">
              <xsl:value-of select="substring-before($style,',')"/>
            </xsl:attribute>
          </xsl:if>
        </Column>
      </xsl:for-each>

      <!--标题-->
      <Row ss:Index="1" ss:Height="50">
        <Cell ss:Index="1" ss:StyleID="s1">
          <xsl:attribute name="ss:MergeAcross">
            <xsl:value-of select="$numberTd +- 1"/>
          </xsl:attribute>
          <Data ss:Type="String">
            <xsl:value-of select="property/*[name()=$xmltitle]/@title"/>
          </Data>
        </Cell>
      </Row>

      <Row>
        <Cell ss:StyleID="s5">
          <xsl:attribute name="ss:MergeAcross">
            <xsl:value-of select="$numberTd +- 1"/>
          </xsl:attribute>
        </Cell>
      </Row>

      <!--表格-->

      <!--表头-->
      <Row ss:Height="18.75">
        <xsl:for-each select="fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol='checkbox')]">
          <xsl:sort data-type="number" order="ascending" select="@cIndex"/>
          <Cell ss:StyleID="s2">
            <xsl:if test="position()=1">
              <xsl:attribute name="ss:Index">1</xsl:attribute>
            </xsl:if>
            <xsl:if test="not(@edtcontrol='seq')">
              <Data ss:Type="String">
                <xsl:value-of select="@title"/>
              </Data>
            </xsl:if>
          </Cell>
        </xsl:for-each>
      </Row>

      <!--表格内容-->
      <xsl:for-each select="/xmldata/data/*[name()=$xmltitle]/d">
        <xsl:sort data-type="number" select="@rid" order="ascending"/>
        <xsl:variable name="sort" select="position()"></xsl:variable>
        <Row>
          <xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0 and not(@edtcontrol='checkbox')]">
            <xsl:sort data-type="number" order="ascending" select="@cIndex"/>

            <xsl:variable name="style" select="substring-before(substring-after(@set,'xls('),')')"/>
            <xsl:variable name="Name" select="@name"/>
            <xsl:variable name="Edtcontrol" select="@edtcontrol"/>

            <Cell>
              <xsl:if test="position()=1">
                <xsl:attribute name="ss:Index">1</xsl:attribute>
              </xsl:if>
              <xsl:if test="not($style='')">
                <xsl:attribute name="ss:StyleID">
                  <xsl:value-of select="substring-after($style,',')"/>
                </xsl:attribute>
              </xsl:if>
              <Data>
                <xsl:attribute name="ss:Type">
                  <xsl:call-template name="StyleData">
                    <xsl:with-param name="sstyle" select="substring-after($style,',')"/>
                  </xsl:call-template>
                </xsl:attribute>
                <xsl:if test="$Edtcontrol = 'seq'">
                  <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/d[position()=$sort]/@rid"/>
                </xsl:if>
                <xsl:if test="not($Edtcontrol = 'seq')">
                  <xsl:if test="substring-after($style,',') ='s14' or substring-after($style,',') ='s15' or substring-after($style,',') ='s16' or substring-after($style,',') ='s17'">
                    <xsl:call-template name="DateTimeStyle">
                      <xsl:with-param name="datetime" select="/xmldata/data/*[name()=$xmltitle]/d[position()=$sort]/*[name()=$Name]"></xsl:with-param>
                    </xsl:call-template>
                  </xsl:if>
                  <xsl:if test="not(substring-after($style,',') ='s14') and not(substring-after($style,',') ='s15') and not(substring-after($style,',') ='s16') and not(substring-after($style,',') ='s17')">
                    <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/d[position()=$sort]/*[name()=$Name]"/>
                  </xsl:if>
                </xsl:if>
              </Data>
            </Cell>
          </xsl:for-each>
        </Row>
      </xsl:for-each>

      <!--合计-->
      <xsl:if test="not($numberSumTd = 'NaN')">
        <Row ss:Height="18">
          <Cell ss:StyleID="s2" ss:Index="1">
            <xsl:attribute name="ss:MergeAcross">
              <xsl:value-of select="$numberSumTd +- 1"/>
            </xsl:attribute>
            <Data ss:Type="String">合计</Data>
          </Cell>
          <xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= $numberSySumTd and not(@edtcontrol='checkbox')]">
            <xsl:sort data-type="number" order="ascending" select="@cIndex"/>

            <xsl:variable name="style" select="substring-before(substring-after(@set,'xls('),')')"/>
            <xsl:variable name="Name" select="@name"/>
            <xsl:variable name="Cando" select="@cando"/>

            <Cell>
              <xsl:if test="not($style='')">
                <xsl:attribute name="ss:StyleID">
                  <xsl:value-of select="substring-after($style,',')"/>
                </xsl:attribute>
              </xsl:if>

              <xsl:if test="contains($Cando,'sum')">
                <Data ss:Type="Number">
                  <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/s/@*[name()=$Name]"/>
                </Data>
              </xsl:if>
              <xsl:if test="not(contains($Cando,'sum'))">
                <Data ss:Type="String"></Data>
              </xsl:if>
            </Cell>
          </xsl:for-each>
        </Row>
      </xsl:if>
    </Table>
  </xsl:template>
</xsl:stylesheet>
