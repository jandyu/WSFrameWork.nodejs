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
	
	
	var rtn = {rightbtn:JSON.stringify({title:"...",badge:"1"})};
	
	return JSON.stringify(rtn);
}
ngsjAppear = function(){
		console.info("appear");
	}
function ngsjRightButtonDo(){
	hybridPage.Native.NavToNewWebPage("v1/wy_wx_list.aspx");
}
function showdetail(rid){
	//显示packinfo.aspx
	var u = '{"url":"repairinfo.aspx?rid='+rid+'"}'
	execNativeFunc("ngsj://url:web1:"+ Base64.encode(u) );
}
</script>


</head>
<body>
<div class="container-fluid">
	<div class="row title">
		维修信息
	</div>
	<div class="row list">		
		<div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
		<div class="col-xs-2">联系人</div>		
		<div class="col-xs-4"><input class="form-control" value="测试人员"> </div>
		<div class="col-xs-4"><input class="form-control" value="13912341234"></div>			
	</div>
	<div class="row list havecontent">		
		<div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-map-marker"></span>  </div>		
		<div class="col-xs-10"> 北区--紫园--01幢--1单元--201室 </div>		
	</div>
	<div class="row title">
		维修类型
	</div>
	<div class="row list button">		
		<div class="col-xs-10 col-xs-offset-1">
			<div class="btn-group col-xs-12" data-toggle="buttons">
				<label class="col-xs-6 btn btn-info active"><input type="radio" name="st" checked> 个人维修</label>
				<label class="col-xs-6 btn btn-primary "><input type="radio" name="st" > 公共维修</label>
			</div>
		</div>		
				
	</div>
	
	<div class="row title">
		图片(拍照)说明
	</div>
	<div class="row pickimg">		
		<div class="col-xs-3"><img src="./css/imgadd.png"></div>
		<div class="col-xs-3"><img src="./css/imgadd.png"></div>
		<div class="col-xs-3"><img src="./css/imgadd.png"></div>
		<div class="col-xs-3"><img src="./css/imgadd.png"></div>		
	</div>
	<div class="row title">
		情况说明
	</div>
	<div class="row area">		
		<textarea rows="5">如图所示</textarea>
	</div>
	<div class="row list button lastrow">		
		<div class="col-xs-8 col-xs-offset-2">
			<button type="button" class="col-xs-12 btn btn-primary">报  修</button>
		</div>		
				
	</div>
</div>
</body>
</html>