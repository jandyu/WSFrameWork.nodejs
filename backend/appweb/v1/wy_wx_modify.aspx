<!DOCTYPE html>
<%@ Page Language="C#" %>
<%@ Register Assembly="westsoft.data.xml" Namespace="westsoft.data.srv.UI" TagPrefix="xmldataui" %>
<html manifest="app1.manifest">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />

<title>维修编辑</title>
<link href="./css/wsapp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/data.srv.js"></script>
<script type="text/javascript">

function ngsjload(){
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
	
	
	var rtn = {rightbtn:JSON.stringify({title:"完成"})};
	
	return JSON.stringify(rtn);
}

function rightButtonDo(){
	//hybridPage.Native.NavToNewWebPage("v1/wy_wx_modify.aspx");
	alert("保存完成");
	hybridPage.Native.CloseWebBrower();
}
function showdetail(rid){
	//显示packinfo.aspx
	var u = '{"url":"repairinfo.aspx?rid='+rid+'"}'
	execNativeFunc("ngsj://url:web1:"+ Base64.encode(u) );
}
</script>


</head>
<body>
<div id="maincontent">
		<ul class="linelist">
			<li class="subtitle" ><span>维修列表标题</span></li>
			<li class="form" >房号<input type="text"></li>
			<li class="form" >维修<input type="text"></li>
			<li class="item" >联系</li>
			
		</ul>
</div>
</body>
</html>