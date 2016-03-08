<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
     xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"
     xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:html="http://www.w3.org/TR/REC-html40">
    <xsl:output method="xml" indent="yes"/>

    <xsl:template name="excel_style">
      <Styles>
        <!--默认样式-->
        <Style ss:ID="Default" ss:Name="Normal">
          <Alignment ss:Vertical="Center"/>
          <Borders/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <Interior/>
          <NumberFormat/>
          <Protection/>
        </Style>
        <!--标题样式-->
        <Style ss:ID="s1">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="20" ss:Bold="1"/>
        </Style>
        <!--文本：居中，加粗-->
        <Style ss:ID="s2">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="14" ss:Bold="1"/>
        </Style>
        <!--文本：居中-->
        <Style ss:ID="s3">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
        </Style>
        <!--文本：居右，加粗-->
        <Style ss:ID="s4">
          <Alignment ss:Horizontal="Right" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="13" ss:Bold="1"/>
        </Style>
        <!--文本：居右-->
        <Style ss:ID="s5">
          <Alignment ss:Horizontal="Right" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
        </Style>
        <!--文本：居左，加粗-->
        <Style ss:ID="s6">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12" ss:Bold="1"/>
        </Style>
        <!--文本：居左-->
        <Style ss:ID="s7">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
        </Style>
        <!--整数：居中-->
        <Style ss:ID="s8">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
        </Style>
        <!--整数：居右-->
        <Style ss:ID="s9">
          <Alignment ss:Horizontal="Right" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
        </Style>
        <!--数字：保留两位小数，居右-->
        <Style ss:ID="s10">
          <Alignment ss:Horizontal="Right" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="0.00_ "/>
        </Style>
        <!--数字：保留两位小数，居左-->
        <Style ss:ID="s11">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="0.00_ "/>
        </Style>
        <!--数字：保留四位小数，居右-->
        <Style ss:ID="s12">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="0.0000_ "/>
        </Style>
        <!--数字：保留四位小数，居左-->
        <Style ss:ID="s13">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="0.0000_ "/>
        </Style>
        <!--日期格式：yyyy/mm/dd，居中-->
        <Style ss:ID="s14">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="yyyy/mm/dd"/>
        </Style>
        <!--日期格式：yyyy/mm/dd，居左-->
        <Style ss:ID="s15">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="yyyy/mm/dd"/>
        </Style>
        <!--日期时间格式：yyyy/mm/dd\ hh:mm:ss，居中-->
        <Style ss:ID="s16">
          <Alignment ss:Horizontal="Right" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="yyyy/mm/dd\ hh:mm:ss"/>
        </Style>
        <!--日期时间格式：yyyy/mm/dd\ hh:mm:ss，居左-->
        <Style ss:ID="s17">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="yyyy/mm/dd\ hh:mm:ss"/>
        </Style>
        <!--数字文本：居中-->
        <Style ss:ID="s18">
          <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="@"/>
        </Style>
        <!--数字文本：居左-->
        <Style ss:ID="s19">
          <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
          <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
          <NumberFormat ss:Format="@"/>
        </Style>
        <!--表格线-->
        <Style ss:ID="s20">
          <Borders>
            <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
          </Borders>
        </Style>
      </Styles>
    </xsl:template>
  
  <!--根据style判断数据格式-->
  <xsl:template name="StyleData">
    <xsl:param name="sstyle"></xsl:param>

    <xsl:choose>
      <xsl:when test="$sstyle='s1' or $sstyle ='s2' or $sstyle ='s3' or $sstyle ='s4' or $sstyle ='s5' or $sstyle ='s6' or $sstyle ='s7' or $sstyle ='s18' or $sstyle ='s19'">String</xsl:when>
      <xsl:when test="$sstyle='s8' or $sstyle ='s9' or $sstyle ='s10' or $sstyle ='s11' or $sstyle ='s12' or $sstyle='s13'">Number</xsl:when>
      <xsl:when test="$sstyle ='s14' or $sstyle ='s15' or $sstyle ='s16' or $sstyle ='s17'">DateTime</xsl:when>
      <xsl:otherwise>String</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!--格式化日期的格式-->
  <xsl:template name="DateTimeStyle">
    <xsl:param name="datetime"></xsl:param>

    <xsl:if test="string-length($datetime) &gt; 10">
      <xsl:variable name="years" select="substring-before($datetime,'-')"></xsl:variable>
      <xsl:variable name="Ydatetime" select="substring-after($datetime,'-')"></xsl:variable>
      
      <xsl:variable name="month">
        <xsl:call-template name="DateLength">
          <xsl:with-param name="date" select="substring-before($Ydatetime,'-')"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="Ymouth" select="substring-after($Ydatetime,'-')"></xsl:variable>

      <xsl:variable name="day">
        <xsl:call-template name="DateLength">
          <xsl:with-param name="date" select="substring-before($Ymouth,' ')"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="Yday" select="substring-after($Ymouth,' ')"></xsl:variable>

      <xsl:variable name="hour">
        <xsl:call-template name="DateLength">
          <xsl:with-param name="date" select="substring-before($Yday,':')"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="Yhour" select="substring-after($Yday,':')"></xsl:variable>

      <xsl:variable name="minute" select="substring-before($Yhour,':')"></xsl:variable>
      <xsl:variable name="Yminute" select="substring-after($Yhour,':')"></xsl:variable>

      <xsl:variable name="second" select="$Yminute"></xsl:variable>
      
      <xsl:value-of select="concat($years,'-',$month,'-',$day,'T',$hour,':',$minute,':',$second,'.000')"/>
    </xsl:if>

    <xsl:if test="not(string-length($datetime) &gt; 10) and contains($datetime,'-')">
      <xsl:variable name="years" select="substring-before($datetime,'-')"></xsl:variable>
      <xsl:variable name="Ydatetime" select="substring-after($datetime,'-')"></xsl:variable>

      <xsl:variable name="month">
        <xsl:call-template name="DateLength">
          <xsl:with-param name="date" select="substring-before($Ydatetime,'-')"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="Ymouth" select="substring-after($Ydatetime,'-')"></xsl:variable>

      <xsl:variable name="day">
        <xsl:call-template name="DateLength">
          <xsl:with-param name="date" select="$Ymouth"></xsl:with-param>
        </xsl:call-template>
      </xsl:variable>

      <xsl:value-of select="concat($years,'-',$month,'-',$day,'T00:00:00.000')"/>
    </xsl:if>

    <xsl:if test="not(string-length($datetime) &gt; 8) and not(contains($datetime,'-'))">
      <xsl:variable name="years" select="substring($datetime,1,4)"></xsl:variable>
      <xsl:variable name="month" select="substring($datetime,5,2)"></xsl:variable>
      <xsl:variable name="day" select="substring($datetime,7,2)"></xsl:variable>
      
      <xsl:value-of select="concat($years,'-',$month,'-',$day,'T00:00:00.000')"/>
    </xsl:if>

  </xsl:template>
  
  <!--将1位数转为两位数-->
  <xsl:template name="DateLength">
    <xsl:param name="date"></xsl:param>

    <xsl:if test="string-length($date)=1">
      <xsl:value-of select="concat('0',$date)"/>
    </xsl:if>

    <xsl:if test="string-length($date)=2">
      <xsl:value-of select="$date"/>
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>
