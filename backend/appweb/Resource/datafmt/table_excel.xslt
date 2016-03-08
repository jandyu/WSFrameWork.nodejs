<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
  
  <xsl:import href="Template_Table_Excel.xslt"/>  
  
  <xsl:output method="xml" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
    <xsl:apply-templates select="/xmldata/property/child::*" mode="getTitle">
    </xsl:apply-templates>
  </xsl:variable>
  <xsl:template match="/xmldata/property/child::*" mode="getTitle">
    <xsl:value-of select="name()"/>
  </xsl:template>
  
    <xsl:template match="/xmldata">
      <xsl:text disable-output-escaping="yes">&lt;?xml version="1.0" encoding="utf-8"?&gt;</xsl:text>
      <xsl:text disable-output-escaping="yes">&lt;?mso-application progid="Excel.Sheet"?&gt;</xsl:text>
      <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"
       xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
       xmlns:html="http://www.w3.org/TR/REC-html40">

        <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
          <Author>杭州西软电子技术开发有限公司</Author>
          <LastAuthor>杭州西软电子技术开发有限公司</LastAuthor>
          <Created>2011-06-09T01:08:13Z</Created>
          <LastSaved>2011-06-09T03:30:07Z</LastSaved>
          <Company>杭州西软电子技术开发有限公司</Company>
          <Version>11.5606</Version>
        </DocumentProperties>

        <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
          <WindowHeight>10695</WindowHeight>
          <WindowWidth>18195</WindowWidth>
          <WindowTopX>120</WindowTopX>
          <WindowTopY>75</WindowTopY>
          <ActiveSheet>1</ActiveSheet>
          <ProtectStructure>False</ProtectStructure>
          <ProtectWindows>False</ProtectWindows>
        </ExcelWorkbook>

        <!--样式-->
        <xsl:call-template name="excel_style"></xsl:call-template>

        <!--表格内容区-->
        <Worksheet>
          <xsl:attribute name="ss:Name">
            <xsl:value-of select="property/*[name()=$xmltitle]/@title"/>
          </xsl:attribute>
          
          <!--表格-->
          <xsl:call-template name="Excel_Content">
            
          </xsl:call-template>
          
          <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <Print>
              <ValidPrinterInfo/>
              <PaperSizeIndex>9</PaperSizeIndex>
              <HorizontalResolution>600</HorizontalResolution>
              <VerticalResolution>600</VerticalResolution>
            </Print>
            <Panes>
              <Pane>
                <Number>3</Number>
                <ActiveRow>1</ActiveRow>
                <ActiveCol>1</ActiveCol>
              </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
          </WorksheetOptions>
        </Worksheet>
        
      </Workbook>
    </xsl:template>
</xsl:stylesheet>
