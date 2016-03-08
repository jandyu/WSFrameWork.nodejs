<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">

  <xsl:output method="html" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>

  <xsl:template name="Pitem">
    <xsl:param name="labelName"/>
    <xsl:param name="className"/>
    <xsl:param name="dataName"/>
    <xsl:param name="maxLength"/>
    <xsl:param name="editType">input</xsl:param>
    <xsl:param name="cate"/>

    <!--数据-->
    <xsl:variable name="Value">
      <xsl:call-template name="ValueCatch">
        <xsl:with-param name="dataName" select="$dataName"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:if test="$editType='password'">
      ws.def.input{"labeltitle":"<xsl:value-of select="$labelName"/>","title":"<xsl:value-of select="$labelName"/>","type":"password","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","value":"<xsl:value-of select="$Value"/>","class":"<xsl:value-of select="$className"/>","maxlength":"<xsl:value-of select="$maxLength"/>"}.ws
    </xsl:if>
    <xsl:if test="$editType='input'">
      ws.def.input{"labeltitle":"<xsl:value-of select="$labelName"/>","title":"<xsl:value-of select="$labelName"/>","type":"text","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","value":"<xsl:value-of select="$Value"/>","class":"<xsl:value-of select="$className"/>","maxlength":"<xsl:value-of select="$maxLength"/>"}.ws
    </xsl:if>

    <xsl:if test="$editType='ddlist'">
      ws.def.ddlist{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","name":"<xsl:value-of select="$dataName"/>","title":"<xsl:value-of select="$labelName"/>","cate":"<xsl:value-of select="$cate"/>",'select':'<xsl:value-of select="$Value"/>'}.ws
    </xsl:if>

    <xsl:if test="$editType='checkbox'">
      <xsl:if test="$Value='1'">
        ws.def.checkbox{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","name":"<xsl:value-of select="$dataName"/>","value":"<xsl:value-of select="$Value"/>","checked":"checked='checked'"}.ws
      </xsl:if>
      <xsl:if test="$Value='0'">
        ws.def.checkbox{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","name":"<xsl:value-of select="$dataName"/>","value":"<xsl:value-of select="$Value"/>","checked":""}.ws
      </xsl:if>
    </xsl:if>

    <xsl:if test="$editType='dateinput'">
      <xsl:if test="contains($Value,' ')">
        ws.def.dateinput{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","value":"<xsl:value-of select="substring-before($Value,' ')"/>"}.ws
      </xsl:if>
      <xsl:if test="not(contains($Value,' '))">
        ws.def.dateinput{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","value":"<xsl:value-of select="$Value"/>"}.ws
      </xsl:if>
      
    </xsl:if>
    <xsl:if test="$editType='textarea'">
      ws.def.textarea{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cols":"50","rows":"5","value":"<xsl:value-of select="$Value"/>"}.ws
    </xsl:if>
    
    <xsl:if test="$editType='morecheckbox'">
      <xsl:choose>
        <xsl:when test="$Value=''">
          ws.def.morecheckbox{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"{'col':'(s.id','logic':'=','val':' ','andor':')'}"}.ws
        </xsl:when>
        <xsl:when test="not($Value='') and not(contains(substring-after($Value,'@'),'@'))">
          ws.def.morecheckbox{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"{'col':'(s.id','logic':'=','val':'<xsl:value-of select="substring-after($Value,'@')"/>','andor':')'}"}.ws
        </xsl:when>
        <xsl:when test="contains($Value,'@')">
          <xsl:variable name="ListValue">
            <xsl:call-template name="ListValueCatch">
              <xsl:with-param name="NowValue" select="substring-before(substring-after($Value,'@'),'@')"/>
              <xsl:with-param name="SyValue" select="substring-after(substring-after($Value,'@'),'@')"/>
              <xsl:with-param name="ListValueString"></xsl:with-param>
              <xsl:with-param name="Rid">first</xsl:with-param>
            </xsl:call-template>
          </xsl:variable>
          ws.def.morecheckbox{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"<xsl:value-of select="$ListValue"/>"}.ws
        </xsl:when>
      </xsl:choose>
    </xsl:if>

    <xsl:if test="$editType='moreinput'">
      <xsl:call-template name="MoreInputContent">
        <xsl:with-param name="labelName" select="$labelName"/>
        <xsl:with-param name="nowValues" select="substring-before(substring-after($Value,'@'),'@')"/>
        <xsl:with-param name="syValues" select="substring-after(substring-after($Value,'@'),'@')"/>
        <xsl:with-param name="num">1</xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    
    <xsl:if test="$editType='moreselect'">
      <xsl:choose>
        <xsl:when test="$Value=''">
          ws.def.moreselect{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"{'col':'(s.id','logic':'=','val':' ','andor':')'}"}.ws
        </xsl:when>
        <xsl:when test="not($Value='') and not(contains(substring-after($Value,'@'),'@'))">
          ws.def.moreselect{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"{'col':'(s.id','logic':'=','val':'<xsl:value-of select="substring-after($Value,'@')"/>','andor':')'}"}.ws
        </xsl:when>
        <xsl:when test="contains($Value,'@')">
          <xsl:variable name="ListValue">
            <xsl:call-template name="ListValueCatch">
              <xsl:with-param name="NowValue" select="substring-before(substring-after($Value,'@'),'@')"/>
              <xsl:with-param name="SyValue" select="substring-after(substring-after($Value,'@'),'@')"/>
              <xsl:with-param name="ListValueString"></xsl:with-param>
              <xsl:with-param name="Rid">first</xsl:with-param>
            </xsl:call-template>
          </xsl:variable>
          ws.def.moreselect{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$dataName)"/>","cate":"<xsl:value-of select="$cate"/>","list":"<xsl:value-of select="$ListValue"/>"}.ws
        </xsl:when>
      </xsl:choose>
    </xsl:if>
    
    <xsl:if test="$editType='treeselect'">
      ws.def.treeselect{"labeltitle":"<xsl:value-of select="$labelName"/>","id":"<xsl:value-of select="concat('frm_',$cate)"/>","cate":"<xsl:value-of select="$cate"/>","list":"{'col':'(s.roleid','logic':'=','val':'<xsl:value-of select="$Value"/>','andor':')'}"}.ws
    </xsl:if>

  </xsl:template>

  <xsl:template name="MoreInputContent">
    <xsl:param name="labelName"/>
    <xsl:param name="nowValues"/>
    <xsl:param name="syValues"/>
    <xsl:param name="num"/>
    <xsl:if test="$num mod 2=1">
      <xsl:text disable-output-escaping="yes">&lt;p class="paccessory"&gt;</xsl:text>
    </xsl:if>
    
    <xsl:variable name="accessoryvalue">
      <xsl:if test="not($nowValues='00' or $nowValues='')">
        <xsl:value-of select="$nowValues"/>
      </xsl:if>
      <xsl:if test="not(not($nowValues='00' or $nowValues=''))">
      </xsl:if>
    </xsl:variable>

    ws.def.input{"labeltitle":"<xsl:value-of select="concat($labelName,$num)"/>","title":"<xsl:value-of select="concat($labelName,$num)"/>","type":"text","id":"<xsl:value-of select="concat('frm_accessory',$num)"/>","value":"<xsl:value-of select="$accessoryvalue"/>","class":"width_80","maxlength":"20"}.ws

    <xsl:if test="$num mod 2=0">
      <xsl:text disable-output-escaping="yes">&lt;/p&gt;</xsl:text>
    </xsl:if>

    <xsl:if test="$num = 7">
      <xsl:call-template name="MoreInputContent">
        <xsl:with-param name="labelName" select="$labelName"/>
        <xsl:with-param name="nowValues" select="$syValues"/>
        <xsl:with-param name="syValues"></xsl:with-param>
        <xsl:with-param name="num" select="$num + 1"></xsl:with-param>
      </xsl:call-template>
    </xsl:if>

    <xsl:if test="$num &lt; 7">
      <xsl:call-template name="MoreInputContent">
        <xsl:with-param name="labelName" select="$labelName"/>
        <xsl:with-param name="nowValues" select="substring-before($syValues,'@')"/>
        <xsl:with-param name="syValues" select="substring-after($syValues,'@')"></xsl:with-param>
        <xsl:with-param name="num" select="$num + 1"></xsl:with-param>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="ValueCatch">
    <xsl:param name="dataName"/>
    <xsl:if test="count(/xmldata/data/*[name()=$xmltitle]/d) &gt; 0">
      <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/d[1]/@*[name()=$dataName]"/>
    </xsl:if>
    <xsl:if test="count(/xmldata/data/*[name()=$xmltitle]/d) &lt;= 0">
      <xsl:value-of select="/xmldata/fielddef/*[name()=$xmltitle]/item[@name=$dataName]/@def"/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="ListValueCatch">
    <xsl:param name="NowValue"/>
    <xsl:param name="SyValue"/>
    <xsl:param name="ListValueString"/>
    <xsl:param name="Rid"/>

    <xsl:if test="$Rid='first' or $Rid='middle'">
      <xsl:variable name="Nowlist"><xsl:if test="$Rid='first'">{'col':'(s.id','logic':'=','val':'<xsl:value-of select="$NowValue"/>','andor':'or'},</xsl:if><xsl:if test="$Rid='middle'">{'col':'s.id','logic':'=','val':'<xsl:value-of select="$NowValue"/>','andor':'or'},</xsl:if></xsl:variable>
      <xsl:choose>
        <xsl:when test="contains($SyValue,'@')">
          <xsl:call-template name="ListValueCatch">
            <xsl:with-param name="NowValue" select="substring-before($SyValue,'@')"/>
            <xsl:with-param name="SyValue" select="substring-after($SyValue,'@')"/>
            <xsl:with-param name="ListValueString" select="concat($ListValueString,$Nowlist)"></xsl:with-param>
            <xsl:with-param name="Rid">middle</xsl:with-param>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="ListValueCatch">
            <xsl:with-param name="NowValue" select="$SyValue"/>
            <xsl:with-param name="SyValue" select="$SyValue"/>
            <xsl:with-param name="ListValueString" select="concat($ListValueString,$Nowlist)"></xsl:with-param>
            <xsl:with-param name="Rid">last</xsl:with-param>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>

    <xsl:if test="$Rid='last'">
      <xsl:variable name="Nowlist">{'col':'s.id','logic':'=','val':'<xsl:value-of select="$NowValue"/>','andor':')'}</xsl:variable>
      <xsl:value-of select="concat($ListValueString,$Nowlist)"/>
    </xsl:if>

  </xsl:template>
</xsl:stylesheet>
