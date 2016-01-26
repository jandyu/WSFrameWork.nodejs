<!DOCTYPE html>
<html manifest="app1.manifest">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta charset="utf-8">

<title>首页</title>
<style>
		
	ul#newslist>li{
		
		width:100%;
		line-height:normal;
	}
	
	body{
		
		overflow-y: auto;
		-webkit-overflow-scrolling: touch;
		font-size:12px;
		
	}
	#wrapper{
		margin:0px;
		padding:0px;
		
	}
	
</style>
<script src="../js/jquery.js"></script>
<script>
	ngsjLoad = function(){
		var rtn = {rightbtn:JSON.stringify({title:"...",badge:"1"})};
	
		return JSON.stringify(rtn);
	}
	ngsjAppear = function(){
		console.info("appear");
	}
	ngsjRightButtonDo = function(){
	}
	
</script>

</head>
<body>
<script>
	
	setheadphoto = function(imgurl){
		$("#txtbox1").val(imgurl);
		$("#newslist li:eq(0)").html("<img src='"+imgurl+"' />");
	}
	reloadpage = function(){
		//document.location.href='d1.aspx?a=1';
		//for(var i=1;i<999;i++){console.info(i);}
		location.reload();
		return "s";
	}
	setinputstring = function(txt){
		$("#txtbox1").val(txt);
	}
	ngsjload = function(){
		$("#devideid").text(hybridPage.DEVICE_UDID);
		$("#userinfo").text(JSON.stringify(hybridPage.userinfo));
		
		$("#info").text("txt");
		
	}
	
	cfmmsg = function(){
		var msg = confirm("继续处理?")
		$("#info").text(msg);
	}
	promsg = function(){
		var msg = prompt('Please enter your name','hello');
		console.info(msg);
		$('#info').text(msg);
	}
	sendmsg = function(){
		window.webkit.messageHandlers.ngsj.postMessage({"a":"1","b":2});
	}
	scanb = function(){
		if(hybridPage){
			hybridPage.Native.ScanBarcode('setb');
		}
		console.info('scan')
	}
	ppview = function(s){
		if(hybridPage){
			hybridPage.Native.PictureView(s);
		}
	}
	pickimg = function(){
		if(hybridPage){
			hybridPage.Native.PickImage('setb');
		}
		
	}
	tonewpage = function(s){
		if(hybridPage){
			hybridPage.Native.NavToNewWebPage('v1/d2.aspx');
		}
	}
	setb = function(s){
		console.info(s)
		$("#info").text(s);
		$("#txtbox1").val(s);
		$("body").append("<img onclick=\"ppview('" +s+ "')\" style='width:100%' src='"+s+"' />")
	}
</script>

<div id="wrapper">
	
		<ul id="newslist">
				<li style="height:30px;">img</li>
				<li style="height:30px;" id="devideid"></li>
				<li style="height:30px;" id="userinfo"></li>
				
			<li style="height:30px;" onclick='reloadpage()'>2刷新刷新</li>						
			<li style="height:30px;" onclick="tonewpage()">第二页</li>				
					
			
			<li style="height:30px;" onclick="pickimg()">pickimg</li>
			<li style="height:30px;">
			<input id='txtbox1' value="" style="width:300px"></input></li>
			
			
			
			
						
			<li style="height:30px;" onclick="scanb()">扫描</li>
			
			<li style="height:30px;">第一页</li>
			
			<li style="height:30px;" onclick="sendmsg()">发送消息</li>				
			<li style="height:30px;" onclick="alert('警告消息')">alert</li>
			
			<li style="height:30px;" onclick="cfmmsg()">confirm</li>
			
			<li style="height:30px;" onclick="promsg()">prompt</li>
			
			
			
<li id="info">
	
</li>			
						
		</ul>
		
		
	</div>
</div>
</body>
<script src="./js/model.js?1"></script>
</html>