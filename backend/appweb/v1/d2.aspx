<!DOCTYPE html>
<html manifest="app1.manifest">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta charset="utf-8">

<title>首页</title>
<style>
		
	ul#newslist>li{
		height:155px;
		width:100%;
		line-height:normal;
	}
	
	#wrapper{
		height:800px;
		overflow-y: scroll;
		-webkit-overflow-scrolling: touch;
	}
</style>
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
	
	closethis = function(s){
		if(hybridPage){
			hybridPage.Native.CloseWebBrower('v1/d2.aspx');
		}
	}
	setb = function(s){
		console.info(s)
		$("#txtbox1").val(s);
		$("body").append("<img onclick=\"ppview('" +s+ "')\" style='width:100%' src='"+s+"' />")
	}
	
	function setbadge()
	{
		if(hybridPage){
			hybridPage.Native.SetBadge("{btnname:'rightbtn',number:'1'}");
		}
	}
</script>
</head>
<body>

<div id="wrapper">
	
		<ul id="newslist">
					
			<li style="height:30px;">第二页</li>
			<li style="height:30px;" onclick="closethis()">返回第一页</li>
			
			<li style="height:30px;">第二页</li>
			<li style="height:30px;" onclick="setbadge()">SetBadge</li>
						
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
		
		<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
			<li style="height:30px;">第二页</li>				
		</ul>
		
		
	</div>
</div>
</body>
</html>