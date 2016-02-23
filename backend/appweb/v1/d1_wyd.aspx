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

    .ls-page-module > div.row img {
        width:44px;
        height:44px;
        margin-top:7px;
    }

     .ls-page-module .row.nolast {
        border-bottom:0px !important;                
    }

    .ls-page-notify> .row> .left {
        text-align:left;
        font-size:18px;  
    }

    .ls-page-notify> .row> .detail {
        text-align:right;
        font-size:14px;  
    }

    .ls-page-notify> .row> .detail>img {
        height:21px;
        width:15px;
        margin-bottom:5px;
    }

    .ls-page-notify > .row.list {
        height:101px;   
        padding:2px;     
    }
    .ls-page-notify > .row.list .subtitle {
        text-align:left;
        font-size:16px;  
        height:20px;
    }
     .ls-page-notify > .row.list .content {
        text-align:left;
        font-size:12px;          
        margin-top:2px; 
        height:20px;       
    }
     .ls-page-notify > .row.list .publishdt {
        text-align:right;
        font-size:12px;  
        height:20px;
    }

</style>
<link href="./bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="./css/wsapp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./bootstrap/dist/js/bootstrap.min.js"></script>
<script>
	ngsjLoad = function(){		
	}
	ngsjAppear = function(){
		console.info("appear");
	}
	ngsjRightButtonDo = function(){
	}
	
</script>

</head>
<body>   
    <div class="container-fluid ls-page-base ls-page-module">
        <div class="row nolast">
            <div class="col-xs-3">
                <img src="imgs/1业主查询.png" /></br>
                <label>业主查询</label>                
            </div>
            <div class="col-xs-3">
                <img src="imgs/2车牌查询.png" /></br>
                <label>业主查询</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/3投诉表扬.png" /></br>
                <label>业主查询</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/4维修.png" /></br>
                <label>业主查询</label>   
            </div>
        </div>

        <div class="row nolast">
            <div class="col-xs-3">
                <img src="imgs/5社区通知.png" /></br>
                <label>社区通知</label>                
            </div>
            <div class="col-xs-3">
                <img src="imgs/6物业费查询.png" /></br>
                <label>物业费查询</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/7短信群发.png" /></br>
                <label>短信群发</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/8快递录入.png" /></br>
                <label>快递录入</label>   
            </div>
        </div>

        <div class="row">
            <div class="col-xs-3">
                <img src="imgs/9水费抄表.png" /></br>
                <label>水费抄表</label>                
            </div>
            <div class="col-xs-3">
                <img src="imgs/10访客信息.png" /></br>
                <label>访客信息</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/11保安巡逻.png" /></br>
                <label>保安巡逻</label>   
            </div>
            <div class="col-xs-3">
                <img src="imgs/12消息推送.png" /></br>
                <label>消息推送</label>   
            </div>
        </div>
    </div> 
    
    <div class="container-fluid ls-page-base ls-page-notify">
        <div class="row title">
            <div class="col-xs-7 left">内部通知</div>
            <div class="col-xs-5 detail">显示全部<img src="imgs/arr_right.png" /></div>
        </div>

         <div class="row list">
            <div class="col-xs-12">
                <p class="subtitle">关于开始收取2016年度物业管理费的通知</p>
                <p class="content">尊敬的业主：2016年度物业管理费收取工作已经...</p>
                <p class="publishdt">2016/2/22</p>
            </div>            
        </div>

         <div class="row list">
            <div class="col-xs-12">
                <p class="subtitle">关于开始收取2016年度物业管理费的通知</p>
                <p class="content">尊敬的业主：2016年度物业管理费收取工作已经...</p>
                <p class="publishdt">2016/2/22</p>
            </div>            
        </div>

         <div class="row list">
            <div class="col-xs-12">
                <p class="subtitle">关于开始收取2016年度物业管理费的通知</p>
                <p class="content">尊敬的业主：2016年度物业管理费收取工作已经...</p>
                <p class="publishdt">2016/2/22</p>
            </div>            
        </div>
      

    </div>  

</body>
</html>