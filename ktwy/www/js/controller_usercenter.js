angular.module('ktwy.controllers', [])

  .controller('usercenter', function ($scope, $stateParams, $state, $log,$ionicModal,$ionicPopup,service_usercenter,service_usercenter_login) {
    $scope.usercenter=service_usercenter;
    $scope.usercenter_login=service_usercenter_login;
    //登录窗口
    $scope.login = function () {
      $scope.openLoginWnd();
        //$state.go("userlogin");
    };
    $scope.userExit=function()
    {

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要退出吗?',
        cancelText: '取消',
        cancelType: 'button-orange',
        okText: '确定',
        okType: 'button-orange'
      });
      confirmPopup.then(function(res) {
        if(res) {
          service_usercenter.userid="0";
          service_usercenter.roomid="";
          service_usercenter.roompath="";
          service_usercenter.name="";
          service_usercenter.phone="";
          service_usercenter.nickname="";

          $scope.usercenter_login.userLoginOut().then(
            function(rtn){
            },function(rtn){
            });
        } else {
          console.log('You are not sure');
        }
      });

    };

    //登陆窗口
    $ionicModal.fromTemplateUrl('templates/usercenter/userlogin.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function(modal) {

      $scope.LoginWnd = modal;
    });


    $scope.openLoginWnd = function() {
      //此处初始化
      $scope.LoginWnd.show();
    };

    $scope.closeLoginWnd = function() {
      $scope.LoginWnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.LoginWnd.remove();
    });

    //账号设置
    $ionicModal.fromTemplateUrl('templates/usercenter/user_set.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_set = modal;
    });

    $scope.open_wnd_user_set = function (iid) {
      //此处初始化
      $scope.wnd_user_set.show();
    };

    $scope.close_wnd_user_set = function () {

      $scope.wnd_user_set.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_set.remove();
    });

  })

  //登录
  .controller('userlogin', function ($scope, $stateParams, $state, $log,$rootScope,$ionicPopup,$ionicModal,service_usercenter,service_usercenter_login) {

    $scope.usercenter_login=service_usercenter_login;


    $scope.login = function () {

      $scope.usercenter_login.userLogin().then(
        function(rtn){
        rtn=jsondal.AnaRtn(rtn);
        var arr_rtn=rtn.split(',');
        if(arr_rtn[0]=="0")
        {
          service_usercenter.userid=arr_rtn[1];
          service_usercenter.roomid=arr_rtn[2];
          service_usercenter.roompath=arr_rtn[3];
          service_usercenter.name=arr_rtn[4];
          service_usercenter.phone=arr_rtn[5];
          service_usercenter.nickname=arr_rtn[6];

          //登录成功则返回
          //$rootScope.$ionicGoBack();
          if($scope.usercenter_login.willGoUrl!='')
          {

            $state.go($scope.usercenter_login.willGoUrl);
            $scope.usercenter_login.willGoUrl="";
            $scope.closeLoginWnd();
          }
          else
          {
            $scope.closeLoginWnd();
            //$rootScope.$ionicGoBack();
          }
        }
        else
        {
          $ionicPopup.alert({
            title: '提醒',
            okType:'button-orange',
            template: '登录失败!'+arr_rtn[1]
          });
        }
      },function(rtn){
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '登录失败!'+rtn
        });
      });
    };


    $scope.goreg=function()
    {
      //$state.go("userreg");
      $scope.openRegWnd();
    };


    //注册窗口
    $ionicModal.fromTemplateUrl('templates/usercenter/userreg.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function(modal) {

      $scope.RegWnd = modal;
    });


    $scope.openRegWnd = function() {
      //此处初始化
      $scope.RegWnd.show();
    };

    $scope.closeRegWnd = function() {
      $scope.RegWnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.RegWnd.remove();
    });

  })

  //注册-step1
  .controller('userreg', function ($scope, $stateParams, $state, $log,$ionicPopup,$ionicModal, service_usercenter_reg) {
    var usercenter_reg=service_usercenter_reg.usercenter_reg;
    $scope.usercenter_reg=usercenter_reg;

    $scope.InterValObj=null; //timer变量，控制时间
    //获取短信验证码
    $scope.getCheckCode=function() {

      //向后台发送处理数据
      var ls_phone = usercenter_reg.username;
      if (!util.IsPhone(ls_phone)) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone=="") {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '手机号不能为空!'
        });
        return;
      }

      //var InterValObj; //timer变量，控制时间
      var count = 120; //间隔函数，1秒执行
      var curCount;//当前剩余秒数
      var msg_number = "";

      service_usercenter_reg.getCheckCode(function(){
          curCount=count;
          usercenter_reg.btn_getcode_disabled="disabled";
          usercenter_reg.btn_reg_disabled="";
          usercenter_reg.btn_getcode_txt=curCount + "秒内输入验证码("+usercenter_reg.checkcode_number+")";
          $scope.InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        },
          function(rtn){

          });

      function SetRemainTime() {
        console.info(curCount);
        if (curCount == 0) {
          window.clearInterval($scope.InterValObj);//停止计时器
          usercenter_reg.btn_getcode_disabled="";
          usercenter_reg.btn_getcode_txt="重新发送验证码";
          usercenter_reg.btn_reg_disabled="disabled";
        }
        else {
          curCount--;
          usercenter_reg.btn_getcode_txt=curCount + "秒内输入验证码(" + usercenter_reg.checkcode_number + ")";
        }
        $scope.$apply();
      }
    };
    //下一步
    $scope.nextstep = function () {
      //数据校验
      var ls_phone = usercenter_reg.username;
      if (!util.IsPhone(ls_phone)) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone=="") {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '手机号不能为空!'
        });
        return;
      }

      //校验验证码是否正确
      service_usercenter_reg.checkCode(function(rtn){
        if (rtn.indexOf('0') == 0)
        {
          var o_arr = rtn.split(",");
          if (o_arr.length == 3) {
            var uid = o_arr[1];
            var uname = o_arr[2];
            usercenter_reg.unit=uid;
            usercenter_reg.unitname=uname;
            if(uid!="") {
              usercenter_reg.selectroom_disabled = true;
            }
            else
            {
              usercenter_reg.selectroom_disabled = false;
            }
            window.clearInterval($scope.InterValObj);//停止计时器
            usercenter_reg.btn_getcode_disabled="";
            usercenter_reg.btn_getcode_txt="重新发送验证码";
            //跳转
            //$state.go("userreg_step2");
            $scope.openReg2Wnd();

          }
          if (o_arr.length == 2) {
            var msg = o_arr[1];

            usercenter_reg.selectroom_disabled=false;

            window.clearInterval($scope.InterValObj);//停止计时器
            usercenter_reg.btn_getcode_disabled="";
            usercenter_reg.btn_getcode_txt="重新发送验证码";

            $ionicPopup.alert({
              title: '提醒',
              okType:'button-orange',
              template: msg
            });
          }


        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            okType:'button-orange',
            template: '验证码不正确!'
          });
        }
      },function(rtn){

      });
    };


    //注册窗口step2
    $ionicModal.fromTemplateUrl('templates/usercenter/userreg_step2.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function(modal) {

      $scope.Reg2Wnd = modal;
    });


    $scope.openReg2Wnd = function() {
      //此处初始化
      $scope.Reg2Wnd.show();
    };

    $scope.closeReg2Wnd = function() {
      $scope.Reg2Wnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.Reg2Wnd.remove();
    });



  })

  //注册-step2
  .controller('userreg_step2', function ($scope, $stateParams, $state, $log, service_usercenter_reg,$ionicModal,service_roomselect,$ionicPopup,service_usercenter) {
    var usercenter_reg=service_usercenter_reg.usercenter_reg;
    $scope.usercenter_reg=usercenter_reg;

    var roomselect=service_roomselect;
    $scope.roomselect=roomselect;

    $scope.return_text="";

    $ionicModal.fromTemplateUrl('templates/usercenter/selectroom.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function(modal) {

      $scope.modal = modal;
    });

    $scope.getChildUnit=function(pid,p_title)
    {
      $scope.roomselect.getUnits(pid).then(function(rtn){

          if(rtn.d!=undefined)
          {
            $scope.roomselect.units=[];
            $.each(rtn.d,function(k,v){
              var itm={
                pid: v.pid,
                p_title: v.p_title,
                uid: v.uid,
                unit_title: v.unit_title,
                child: v.child,//子个数
                p_pid: v.parentid
              };
              $scope.roomselect.units.push(itm);
            });
          }else
          {
            $scope.usercenter_reg.unit=pid;
            $scope.usercenter_reg.unitname=p_title;
            $scope.closeModal();
          }
        var s_txt=$scope.roomselect.getFirst().p_title;
        if(s_txt=="") {
        s_txt="选择房号";
        }
          $scope.return_text=s_txt;
          $scope.$apply();
        },
        function (rtn) {
        });
    }

    //返回
    $scope.getParentUnit=function()
    {
      var first=$scope.roomselect.getFirst();
      if(first.p_title=="") {
        $scope.closeModal();
      }
      else {
        $scope.getChildUnit(first.p_pid);
      }
    };

    $scope.regUser=function()
    {

      if(usercenter_reg.unit=="0")
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '请选择房号!'
        });
        return;
      }

      if(usercenter_reg.name==""){
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '姓名不能为空!'
        });
        return;
      }

      if(usercenter_reg.nickname==""){
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '昵称不能为空!'
        });
        return;
      }


      if(usercenter_reg.password==""){
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '密码不能为空!'
        });
        return;
      }

       /*
      if(usercenter_reg.password!=usercenter_reg.repassword){
        $ionicPopup.alert({
          title: '提醒',
          template: '密码不一致,请确认密码!'
        });
        return;
      }*/


      service_usercenter_reg.regUser().then(function(rtn){
        var rtn = jsondal.AnaRtn(rtn);
        if (rtn.indexOf('0') == 0) {
          //初始化全局用户信息

          var userid=rtn.split(',')[1];
          service_usercenter.userid=userid;
          service_usercenter.name=$scope.usercenter_reg.name;
          service_usercenter.phone=$scope.usercenter_reg.username;
          service_usercenter.nickname=$scope.usercenter_reg.nickname;
          service_usercenter.roomid=$scope.usercenter_reg.unit;
          service_usercenter.roompath=$scope.usercenter_reg.unitname;

          $ionicPopup.alert({
            title: '提醒',
            okType:'button-orange',
            template: '注册成功!'
          });
          //$state.go("tab.usercenter");
          $scope.closeReg2Wnd();
          $scope.closeRegWnd();
          $scope.closeLoginWnd();

        } else {
          $ionicPopup.alert({
            title: '提醒',
            okType:'button-orange',
            template: rtn
          });
        }
      },function(err){
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '注册失败'+err
        });
      });
    }
    $scope.openModal = function() {
      $scope.getChildUnit(0);
      $scope.modal.show();
    };

    $scope.closeModal = function() {
      $scope.modal.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.modal.remove();
    });
})

  .controller('user_set', function ($scope, $stateParams, $state, $log,$ionicModal,$ionicPopup,service_usercenter,service_usercenter_login) {
    $scope.usercenter = service_usercenter;
  })
;

