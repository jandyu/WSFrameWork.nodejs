<!DOCTYPE html>
<%@ Page Language="C#" %>
<%@ Register Assembly="westsoft.data.xml" Namespace="westsoft.data.srv.UI" TagPrefix="xmldataui" %>
<html manifest="app1.manifest">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />

<title>维修</title>
<link href="./css/wsapp.css" rel="stylesheet" type="text/css" />
<link href="./css/font/css/font-awesome.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/data.srv.js"></script>
<script type="text/javascript">

function ngsjLoad(){
	wxpage = hybridPage.create({
		currpage:0,
		getNextPage:function(){
			var me = this;								
			me.currpage = me.currpage + 1;
			var where =[];
			var fmtid = "hy_app_repair_lst";			
			var where = [{"col":"app_user.device_id","logic":"=","val":me.DEVICE_UDID,"andor":""}];
			datsrv.UIGetSimpleQuery("app_wy_repair_lst", fmtid, me.currpage, 15, where, $("#loadmsg"),function(){		
				$("#newslist").append($("#loadmsg").html());				
				setTimeout(function () {myScroll.options.bottom = -60;myScroll.refresh();}, 0);
			});
		}
	});
	
	
	var rtn = {rightbtn:JSON.stringify({title:"保存",badge:"1"})};
	
	return JSON.stringify(rtn);
}
function ngsjRightButtonDo(){
	//hybridPage.Native.NavToNewWebPage("v1/wy_wx_modify.aspx");
	hybridPage.Native.CloseWebBrower();
}
function showdetail(rid){
	//显示packinfo.aspx
	var u = '{"url":"repairinfo.aspx?rid='+rid+'"}'
	execNativeFunc("ngsj://url:web1:"+ Base64.encode(u) );
}
</script>
<style>
	
</style>

</head>
<body>
<div id="maincontent">
		<ul class="linelist">
			<li class="subtitle" ><span>处理中维修列表</span></li>
			<li class="item" ><i class="fa fa-thumb-tack red"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack red"></i>维修列表1</li>
			
			<li class="subtitle" ><span>已经结束的维修列表</span></li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
			<li class="item" ><i class="fa fa-thumb-tack blue"></i>维修列表1</li>
		</ul>
</div>
</body>
</html>