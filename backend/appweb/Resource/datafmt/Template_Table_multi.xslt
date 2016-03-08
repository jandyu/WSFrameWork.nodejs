<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">


  <xsl:import href="Template_Page.xslt"/>

  <xsl:output method="html" indent="yes"/>

  <!--获得标题名-->
  <xsl:variable name="xmltitle">
  <xsl:value-of select ="name(/xmldata/property/child::*)"/>    
  </xsl:variable>

  <!--获得子查询名-->
  <xsl:variable name="xmlchildtitle">
  <xsl:value-of select ="name(/xmldata/property/*[name()=$xmltitle]/child::*)"/>    
  </xsl:variable>
  
  <!--总页数-->
  <xsl:variable name="totalpages">
    <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@totalpages"/>
  </xsl:variable>

  <!--当前页-->
  <xsl:variable name="currpage">
    <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@currpage"/>
  </xsl:variable>

  <!--总行数-->
  <xsl:variable name="totalrows">
    <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@totalrows"/>
  </xsl:variable>

  <!--默认排列字段和顺序-->
  <xsl:variable name="orderby">
    <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@orderby"/>
  </xsl:variable>

  <!--要显示的表格行数-->
  <xsl:variable name="numberTr">
    <xsl:value-of select="count(/xmldata/data/*[name()=$xmltitle]/d)"/>
  </xsl:variable>

  <!--要显示的表格列数-->
  <xsl:variable name="numberTd">
    <xsl:value-of select="count(/xmldata/fielddef/*[name()=$xmltitle]/item[@cIndex &gt;= 0])"/>
  </xsl:variable>

  <!--取得小计或合计要跨行的数量-->
  <xsl:variable name="numberSumTd">
    <xsl:call-template name="sumNum">
    </xsl:call-template>
  </xsl:variable>
  <xsl:template name="sumNum">
    <xsl:for-each select="/xmldata/fielddef/*[name()=$xmltitle]/item[contains(@cando,'sum') and @cIndex &gt;= 0]/@cIndex">
      <xsl:sort order="ascending" select="." data-type="number"/>
      <xsl:if test="position() = 1">
        <xsl:value-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>


  <!--整个表格的基本结构-->
  <xsl:template name="TableContent">
    <xsl:param name="pagination"></xsl:param>
    <xsl:param name="pagetype"></xsl:param>
    <xsl:param name="yesData"></xsl:param>	
    <xsl:param name="titlename"></xsl:param>
        <table  class="uk-table uk-table-condensed uk-table-hover">
          <!--设置table的id属性-->
          <xsl:attribute name="id">
            <xsl:value-of select="$titlename"/>
          </xsl:attribute>
          <thead>
            <tr>
              <!--表格头-->
              <xsl:apply-templates select="//*[name()=$titlename]/item" mode="ThList">					
				</xsl:apply-templates>
				<th><xsl:text> </xsl:text>				
				</th>
            </tr>
          </thead>
          <tbody>
            <!--表格内容-->
            <xsl:if test="$yesData = 1">				
				<xsl:apply-templates select="//data/*[name()=$titlename]/d" mode="Tbody">					
				</xsl:apply-templates>              										
            </xsl:if>                            
            <!--小计和合计-->
			
				<xsl:if test="$currpage = $totalpages">
				<xsl:if test="$yesData = 1 and not($numberSumTd = '')">                          
				  <tr>
					<td class="center">
					  <xsl:attribute name="colspan">
						<xsl:value-of select="$numberSumTd"/>
					  </xsl:attribute>
					  合计
					</td>
					<xsl:call-template name="Tfoot">
					  <xsl:with-param name="style">1</xsl:with-param>
					</xsl:call-template>
				  </tr>
				</xsl:if>
				 </xsl:if>
				<xsl:text> </xsl:text>			
			</tbody>
			
			<xsl:if test="$totalpages &gt; 1">
			<tfoot style="font-style:normal">
			<tr>
				<td>
					<xsl:attribute name="colspan">
                    <xsl:value-of select="$numberTd"/>
                  </xsl:attribute>
				  <ul class="uk-pagination uk-pagination-left">
					<li>共<xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@totalrows"/>行，
						<xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@totalpages"/>页，
					</li>
					<li><a href="#" onclick="showdata(-{/xmldata/data/*[name()=$xmltitle]/@totalpages},'{$xmltitle}');return false;"><i class="uk-icon-fast-backward"><xsl:text> </xsl:text></i>首 页</a></li>
					<li><a href="#" onclick="showdata(-1,'{$xmltitle}');return false;"><i class="uk-icon-backward"><xsl:text> </xsl:text></i>上一页</a></li>
					<li>当前第<span class="currpagenum"><xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/@currpage"/></span>页</li>
					<li><a href="#" onclick="showdata(1,'{$xmltitle}');return false;"><i class="uk-icon-forward"><xsl:text> </xsl:text></i>下一页</a></li>
					<li><a href="#" onclick="showdata({/xmldata/data/*[name()=$xmltitle]/@totalpages},'{$xmltitle}');return false;"><i class="uk-icon-fast-forward"><xsl:text> </xsl:text></i>末 页</a></li>
					<li><span class="progressinfo"><xsl:text> </xsl:text></span></li>
				  </ul>
				</td>
			</tr>
          </tfoot>
		  </xsl:if>
		  
          
        </table>

    
  </xsl:template>

  <!--输出表格头部-->
  <xsl:template mode="ThList" match="item" >		   
	<xsl:if test="@cIndex &gt;= 0">
        <th>
          <xsl:if test="contains(@cando,'order') and not(@edtcontrol='seq') and not(@edtcontrol='checkbox')">
            <xsl:attribute name="onclick">orderby('<xsl:value-of select="@name"/>','<xsl:value-of select="name(parent::*)"/>')</xsl:attribute>
			<xsl:attribute name="class">orderbypointer</xsl:attribute>
          </xsl:if>
          <xsl:if test="@edtcontrol='seq'">
            <xsl:attribute name="class">th_first</xsl:attribute>
          </xsl:if>
          <xsl:if test="@edtcontrol='checkbox'">
            <xsl:attribute name="class">check</xsl:attribute>
          </xsl:if>
          <!--根据当前的编辑类型，输出不同的内容（针对seq 和 checkbox）-->
          <span>
            <xsl:if test="(@edtcontrol='seq') "> </xsl:if>
            <xsl:if test="(@edtcontrol='checkbox')">
              <input type='checkbox' onclick="SelectAll('{$xmltitle}')">
                <xsl:attribute name="id">
                  <xsl:value-of select="concat(name(parent::*),'_allSelect')"/>
                </xsl:attribute>
              </input>
            </xsl:if>
            <xsl:if test="not(@edtcontrol='seq') and not(@edtcontrol='checkbox')">
              <xsl:value-of select="@title"/>              
            </xsl:if>
          </span>
        </th> 		 
    </xsl:if>
  </xsl:template>

  <!--表格数据-->
  <xsl:template mode="Tbody" match="d"> 
		
		<xsl:variable name="titlename">
			<xsl:value-of select="name(parent::*)"/>   
		</xsl:variable>		
		<!--获得父节点查询名-->
		<xsl:variable name="xmlparenttitle">
		<xsl:value-of select ="name(/xmldata/property//*[name()=$titlename]/parent::*)"/>    
	  </xsl:variable>	
		<!--获得子查询名-->
	  <xsl:variable name="xmlchildtitle">
		<xsl:value-of select ="name(/xmldata/property//*[name()=$titlename]/child::*)"/>    
	  </xsl:variable>	
      <tr pnode="{$titlename}">	 
			<xsl:if test="not($xmlparenttitle='property')">
				<xsl:attribute name="style">display:none</xsl:attribute>
			</xsl:if>
			<td>
				<xsl:if test="$xmlparenttitle='property'">
				<xsl:value-of select= "@rid"/>
				</xsl:if>
			</td>
          	<xsl:for-each select ="node()">
				<xsl:variable name="Name" select="name(.)"/>
				<xsl:variable name='ifshow'>
					<xsl:value-of select="/xmldata/fielddef//*[name()=$titlename]/item[@name= $Name]/@cIndex"/>   
				</xsl:variable>
				<xsl:variable name="Class">
					<xsl:value-of select="/xmldata/fielddef//*[name()=$titlename]/item[@name= $Name]/@fmt"/>   
				</xsl:variable>
				<xsl:variable name="Edtcontrol">
					<xsl:value-of select="/xmldata/fielddef//*[name()=$titlename]/item[@name= $Name]/@edtcontrol"/>   
				</xsl:variable>
			<xsl:if test="not($ifshow ='')">
          <td>
				<xsl:if test="not($Class = '')">
              <xsl:attribute name="class">
                <xsl:value-of select="$Class"/>
              </xsl:attribute>
            </xsl:if>
				<xsl:if test="$ifshow &gt;=0">
					<xsl:value-of select="." disable-output-escaping="no"/>
				</xsl:if>            			              
				<xsl:if test="not($ifshow &gt;=0)">
					<xsl:text> </xsl:text>
				</xsl:if>            			              
          </td>
		  </xsl:if>
			</xsl:for-each>
			<td>
			<xsl:if test="not($xmlchildtitle='')">
						<xsl:text> </xsl:text><a href="#" onclick="expandtbl($(this))" child="{$xmlchildtitle}"><i class="uk-icon-plus-sign"> </i></a>
				</xsl:if>
			</td>
      </tr>	  	
	  <!--子查询-->	
	    	 
	  <xsl:if test ="not($xmlchildtitle='')">	 
		<xsl:apply-templates select="./*[name()=$xmlchildtitle]/d" mode="Tbody">
		</xsl:apply-templates>         
	  </xsl:if>
  </xsl:template>

  <xsl:template name="Tfoot">
    <xsl:param name="style"></xsl:param>
    
    <xsl:for-each select="//*[name()=$xmltitle]/item[@cIndex &gt;= $numberSumTd]">
      <xsl:sort select="@cIndex" data-type="number" order="ascending"/>
	  <!--取得字段名-->
        <xsl:variable name="name" select="@name"/>
      <td>        
        <!--当当前字段是要合计或小计的-->
        <xsl:if test="contains(@cando,'sum')">
          <!--增加class-->
          <xsl:attribute name="class"><xsl:value-of select="/xmldata/fielddef//*[name()=$xmltitle]/item[@name= $name]/@fmt"/> sum_<xsl:value-of select="$name"/></xsl:attribute>
          <!--小计-->
          <xsl:if test="$style = 0">
            <xsl:value-of select="format-number(sum(/xmldata/data/*[name()=$xmltitle]/d/@*[name()=$name]),'####.0000')"/>
          </xsl:if>
          <!--合计-->
          <xsl:if test="$style = 1">
            <xsl:value-of select="/xmldata/data/*[name()=$xmltitle]/s/@*[name()=$name]"/>
          </xsl:if>
        </xsl:if>
        <!--当当前字段不需要合计或小计-->
        <xsl:if test="not(contains(@cando,'sum'))">
          &#160;
        </xsl:if>
      </td>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>

