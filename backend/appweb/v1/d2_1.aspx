<!DOCTYPE html>
<%@ Page Language="C#" %>
<%@ Register Assembly="westsoft.data.xml" Namespace="westsoft.data.srv.UI" TagPrefix="xmldataui" %>
<html manifest="app1.manifest">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />

<title>维修</title>
<link href="./bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="./css/wsapp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./bootstrap/dist/js/bootstrap.min.js"></script>

<script type="text/javascript" src="./js/data.srv.js"></script>
<script type="text/javascript" src="./js/model.js"></script>
<script type="text/javascript">  
    var g_model = null;
    var g_m_data = null;
    function ngsjLoad() {
        /*
        wxpage = hybridPage.create({
            currpage: 0,
            getNextPage: function () {
                var me = this;
                me.currpage = me.currpage + 1;
                var where = [];
                var fmtid = "hy_app_repair_lst";
                var where = [{ "col": "app_user.device_id", "logic": "=", "val": me.DEVICE_UDID, "andor": "" }];
                datsrv.UIGetSimpleQuery("app_wy_repair_lst", fmtid, me.currpage, 15, where, $("#loadmsg"), function () {
                    $("#newslist").append($("#loadmsg").html());
                    setTimeout(function () { myScroll.options.bottom = -60; myScroll.refresh(); }, 0);
                });
            }
        });

        hybridPage.Native.SetRightButton("{ 'title': '保存', 'img': '', 'callback': 'ngsjRightButtonDo' }");
        */
        g_model = model.create({
            data: g_m_data,
            formitem: {
                "app_wy_repair": {
                    iid: { item: 'edit_iid', save_flag: false },
                    create_dt: { item: 'edit_create_dt', save_flag: true },
                    creater: { item: 'edit_creater', save_flag: true },
                    report_dt: { item: 'edit_report_dt', save_flag: true },
                    category: { item: 'edit_category', save_flag: true },
                    roomid: { item: 'edit_roomid', save_flag: true },
                    report_person: { item: 'edit_report_person', save_flag: true },
                    phone: { item: 'edit_phone', save_flag: true },
                    reason: { item: 'edit_reason', save_flag: true },
                    receiver: { item: 'edit_receiver', save_flag: true },
                    status: { item: 'edit_status', save_flag: true },
                    source: { item: 'edit_source', save_flag: true },
                    imagelist_img: { item: 'edit_imagelist_img', save_flag: true },
                    room_path: { item: 'edit_room_path', save_flag: true }
                }
            }            
        });
        g_model.refresh({ a: 0 }, function () { }, function () { });
    }
    

    ngsjAppear = function () {
        console.info("appear");
    }
    function ngsjRightButtonDo() {
        hybridPage.Native.NavToNewWebPage("v1/wy_wx_list.aspx");
    }
    function showdetail(rid) {
        //显示packinfo.aspx
        var u = '{"url":"repairinfo.aspx?rid=' + rid + '"}'
        execNativeFunc("ngsj://url:web1:" + Base64.encode(u));
    }
    
</script>


</head>
<body>              
<div class="container-fluid">
    <div  class="row title" style="display:none;">
        <div class="col-xs-12">
            <input class="form-control" id="edit_iid" value="" />
            <input class="form-control" id="edit_create_dt" value="" />
            <input class="form-control" id="edit_creater" value="" />
            <input class="form-control" id="edit_report_dt" value="" />
            <input class="form-control" id="edit_roomid" value="" />            
            <input class="form-control" id="edit_status" value="" />
            <input class="form-control" id="edit_source" value="" />
            <input class="form-control" id="edit_category" value="" />
            <input class="form-control" id="edit_imagelist_img" value="" />            
        </div>
    </div>
	<div class="row title">
		维修信息
	</div>
	<div class="row list">		
		<div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
		<div class="col-xs-2">联系人</div>		
		<div class="col-xs-4"><input class="form-control" id="edit_report_person" value="" /> </div>
		<div class="col-xs-4"><input class="form-control"  id="edit_phone" value=""/></div>			
	</div>
	<div class="row list havecontent">		
		<div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-map-marker"></span>  </div>		
		<div class="col-xs-10"> 
            <input class="form-control" id="edit_room_path" value="" />           
		</div>		
	</div>
	<div class="row title">
		维修类型
	</div>
	<div class="row list button">		
		<div class="col-xs-10 col-xs-offset-1">
			<div class="btn-group col-xs-12" data-toggle="buttons">                
				<label class="col-xs-6 btn btn-info active"><input type="radio" name="category" id="category_0" checked="true" />个人维修</label>
				<label class="col-xs-6 btn btn-primary"><input type="radio" name="category"  id="category_1" />公共维修</label>                                  
			</div>
		</div>		
				
	</div>
	
	<div class="row title">
		图片(拍照)说明
	</div>
	<div class="row pickimg">		
		<div class="col-xs-3"><img src="./css/imgadd.png" /></div>
		<div class="col-xs-3"><img src="./css/imgadd.png" /></div>
		<div class="col-xs-3"><img src="./css/imgadd.png" /></div>
		<div class="col-xs-3"><img src="./css/imgadd.png" /></div>		
	</div>
	<div class="row title">
		情况说明
	</div>
	<div class="row area">		
		<textarea rows="5" id="edit_reason"></textarea>
	</div>
	<div class="row list button lastrow">		
		<div class="col-xs-8 col-xs-offset-2">
			<button type="button" class="col-xs-12 btn btn-primary">报  修</button>
		</div>						
	</div>
</div>	
    
    <xmldataui:xslt runat="server" id="Xslt1" datadef="db_app.v_app_wy_repair"
                        currpage="1" strWhere="[{ 'col': 'iid', 'logic': '= ', 'val': '#_iid_#', 'andor': '' }]"
                        order="iid:desc" rowcnt="1">  
          
        <xsl:template match="/xmldata/data/v_app_wy_repair_table">	
		    <xsl:apply-templates select="/xmldata/data/v_app_wy_repair_table/d" mode="item"></xsl:apply-templates>		 	
	    </xsl:template>	
	    <xsl:template match="d" mode="item" >
            <script>
                g_m_data = {
                    app_wy_repair: {
                        iid: '<xsl:value-of select="iid" />',
                        create_dt: '<xsl:value-of select="create_dt" />',
                        creater: '<xsl:value-of select="creater" />',
                        report_dt: '<xsl:value-of select="report_dt" />',
                        category: '<xsl:value-of select="category" />',
                        roomid: '<xsl:value-of select="roomid" />',
                        report_person: '<xsl:value-of select="report_person" />',
                        phone: '<xsl:value-of select="phone" />',
                        reason: '<xsl:value-of select="reason" />',
                        receiver: '<xsl:value-of select="receiver" />',
                        status: '<xsl:value-of select="status" />',
                        source: '<xsl:value-of select="source" />',
                        imagelist_img: '<xsl:value-of select="imagelist_img" />',
                        room_path: '<xsl:value-of select="room_path" />'
                    }
                };
                //ngsjLoad();
            </script>   
        </xsl:template>	 
    </xmldataui:xslt>         
</body>
</html>