<!DOCTYPE html>
<html manifest="app1.manifest">
<head>
<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />    -->    
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

<meta charset="utf-8">

<title>开元国际物业</title>
<style>    

    .ls-page-base {
        text-align:center;
        font-family: 微软雅黑;
        letter-spacing: 1px;   
        font-size:12px;   
             
    }  

</style>
<link href="../v1/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="../v1/css/wsapp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../v1/js/jquery.min.js"></script>
<script type="text/javascript" src="../v1/bootstrap/dist/js/bootstrap.min.js"></script>
<script>
    ngsjLoad = function () {

	}
	ngsjAppear = function(){
		console.info("appear");
	}
	ngsjRightButtonDo = function(){
	}

	function funLogin() {
	    changeUI("success");
	}

	function changeUI(reg_login) {
	    if (reg_login == "reg") {
	        $(".ls-page-login").hide();
	        $(".ls-page-success").hide();
	        $(".ls-page-reg").show();
	    }
	    if (reg_login == "login") {	    
	        $(".ls-page-reg").hide();
	        $(".ls-page-success").hide();
	        $(".ls-page-login").show();
	    }
	    if (reg_login == "success") {	        
	        $(".ls-page-reg").hide();
	        $(".ls-page-login").hide();
	        $(".ls-page-success").show();
	    }
	}


	var InterValObj; //timer变量，控制时间
	var count = 5; //间隔函数，1秒执行
	var curCount;//当前剩余秒数

	function sendMessage() {
	    curCount = count;
	    //设置button效果，开始计时
	    
	    //向后台发送处理数据
	    var ls_phone=$("#edit_phone").val();
	    $.post("../handler/Login.ashx", { op: "send_checkcode", phone: ls_phone }, function (rtn) {
	        $("#btn_getcode").attr("disabled", "true");
	        $("#btn_reg_reg").removeAttr("disabled");//启用按钮

	        $("#btn_getcode").text("请在" + curCount + "秒内输入验证码");
	        InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
	    });
        
	}
	function SetRemainTime() {
	    if (curCount == 0) {
	        window.clearInterval(InterValObj);//停止计时器
	        $("#btn_getcode").removeAttr("disabled");//启用按钮
	        $("#btn_getcode").text("重新发送验证码");
	        $("#btn_reg_reg").attr("disabled", "true");
	    }
	    else {
	        curCount--;
	        $("#btn_getcode").text("请在" + curCount + "秒内输入验证码");
	    }
	}
	
</script>

</head>
<body>   
    <div class="container-fluid ls-page-base ls-page-reg" style="display:none;">    
	    <div class="row title">
		    用户注册
	    </div>
	    <div class="row list">				
		    <div class="col-xs-3">手机号</div>		
		    <div class="col-xs-6"><input class="form-control" id="edit_phone" value="" placeholder="请输入手机号" /> </div>					
	    </div>	
        <div class="row list">				
		    <div class="col-xs-3">验证码</div>		
		    <div class="col-xs-4"><input class="form-control" id="edit_checkcode" value="" placeholder="请输验证码" /> </div>					
            <div class="col-xs-5"><button type="button" class="btn btn-default" id="btn_getcode" onclick="sendMessage()">获取验证码</button></div>		
	    </div>	

        <div class="row list">				
		    <div class="col-xs-3">昵 称</div>		
		    <div class="col-xs-6"><input class="form-control" id="edit_nick_name" value="" placeholder="请输入昵称" /> </div>					
	    </div>

        <div class="row list">				
	        <div class="col-xs-3">密 码</div>		
	        <div class="col-xs-6"><input type="password" class="form-control" id="edit_password" value="" placeholder="请输入密码" /> </div>					
	    </div>
       
        <div class="row list lastrow">					    	
	        <div class="col-xs-12" >
                <button type="button" class="btn btn-danger" id="btn_reg_reg"  onclick="funReg()" disabled="disabled" style="width:200px;">立即注册</button>

                <button type="button" class="btn btn-success" id="btn_reg_login"  onclick="changeUI('login')" style="width:100px;">快速登录>></button>
            </div>
	    </div>
    </div>
    
     <div class="container-fluid ls-page-base ls-page-login" style="display:none;">    
	    <div class="row title">
		    用户登录
	    </div>
	    <div class="row list">				
		    <div class="col-xs-3">手机号</div>		
		    <div class="col-xs-6"><input class="form-control" id="edit_login_logname" value="" placeholder="请输入手机号" /> </div>					
	    </div>	       

        <div class="row list">				
	        <div class="col-xs-3">密 码</div>		
	        <div class="col-xs-6"><input type="password" class="form-control" id="edit_login_password" value="" placeholder="请输入密码" /> </div>					
	    </div>
       
        <div class="row list lastrow">					    	
	        <div class="col-xs-12" >
                <button type="button" class="btn btn-danger" id="btn_login_login"  onclick="funLogin()" style="width:200px;">登录</button>

                <button type="button" class="btn btn-success" id="btn_login_reg"  onclick="changeUI('reg')" style="width:100px;">快速注册>></button>
            </div>
	    </div>
    </div>

    <div class="container-fluid ls-page-success">            
          <div class="row list lastrow" onclick="changeUI('login')" style="height:90px;">				
	        <div class="col-xs-4 ">
                <img src="../v1/imgs/头像_未登录.png" style="width:80px;height:80px;margin:5px;"/>
	        </div>
	        <div class="col-xs-7">
                <label style="margin-top:20px;">未登录</label>
	        </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" style="margin-top:25px;" /></div>					
	    </div>
        	    
	    <div class="row title">				
		    			
	    </div>	       

        <div class="row list">				
	        <div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
	        <div class="col-xs-9">我发起的资讯 </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" /></div>					
	    </div>

        
        <div class="row list">				
	        <div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
	        <div class="col-xs-9">我的投诉 </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" /></div>					
	    </div>


        <div class="row title">				
		    			
	    </div>
        <div class="row list">				
	        <div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
	        <div class="col-xs-9">我发起的维修 </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" /></div>					
	    </div>

        <div class="row list">				
	        <div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
	        <div class="col-xs-9">我发起的资讯 </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" /></div>					
	    </div>

        <div class="row title">				
		    			
	    </div>

        <div class="row list">				
	        <div class="col-xs-2 inputlabel"><span class="glyphicon glyphicon-link"></span>  </div>
	        <div class="col-xs-9">我发起的资讯 </div>
            <div class="col-xs-1"><img src="../v1/imgs/arr_right.png" /></div>					
	    </div>



    </div>
</body>
</html>