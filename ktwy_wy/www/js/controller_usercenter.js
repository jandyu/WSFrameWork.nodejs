angular.module('ktwy.controllers', [])

  .controller('usercenter', function ($scope, $stateParams, $state, $ionicModal, $log, service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;
    //登录窗口
    $scope.login = function () {
      //$state.go("userlogin");
      $scope.openLoginWnd();

    };

    $scope.userExit = function () {

      service_usercenter.userid = "0";
      service_usercenter.enumber = "";
      service_usercenter.name = "";
      service_usercenter.phone = "";
      service_usercenter.dpetid = "";
      service_usercenter.deptname = "";

      $scope.usercenter_login.userLoginOut().then(
        function (rtn) {
        }, function (rtn) {
        });
    };

    //登陆窗口
    $ionicModal.fromTemplateUrl('templates/usercenter/userlogin.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.LoginWnd = modal;
    });


    $scope.openLoginWnd = function () {
      //此处初始化
      $scope.LoginWnd.show();
    };

    $scope.closeLoginWnd = function () {
      $scope.LoginWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.LoginWnd.remove();
    });

  })

  //登录
  .controller('userlogin', function ($scope, $stateParams, $state, $log, service_usercenter, service_usercenter_login, $rootScope, $ionicPopup) {

    $scope.usercenter_login = service_usercenter_login;


    $scope.login = function () {
      $scope.usercenter_login.userLogin(function (rtn) {
        rtn = jsondal.AnaRtn(rtn);
        var arr_rtn = rtn.split(',');
        if (arr_rtn[0] == "0") {
          service_usercenter.userid = arr_rtn[1];
          service_usercenter.enumber = arr_rtn[2];
          service_usercenter.name = arr_rtn[3];
          service_usercenter.dpetid = arr_rtn[4];
          service_usercenter.deptname = arr_rtn[5];
          service_usercenter.phone = arr_rtn[6];


          window.clearInterval($scope.InterValObj);//停止计时器
          $scope.usercenter_login.btn_getcode_disabled = "";
          $scope.usercenter_login.btn_getcode_txt = "重新发送验证码";
          $scope.usercenter_login.btn_reg_disabled = "disabled";

          //登录成功则返回
          //$rootScope.$ionicGoBack();

          if ($scope.usercenter_login.willGoUrl != '') {

            $state.go($scope.usercenter_login.willGoUrl);
            $scope.usercenter_login.willGoUrl = "";
            $scope.closeLoginWnd();
          }
          else {
            $scope.closeLoginWnd();
            //$rootScope.$ionicGoBack();
          }
        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            template: '登录失败!' + arr_rtn[1]
          });
        }
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          template: '登录失败!' + rtn
        });
      });
    };

    $scope.InterValObj = null; //timer变量，控制时间
    //获取短信验证码
    $scope.getCheckCode = function () {

      //向后台发送处理数据
      var ls_phone = $scope.usercenter_login.username;
      if (!util.IsPhone(ls_phone)) {
        $ionicPopup.alert({
          title: '提醒',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone == "") {
        $ionicPopup.alert({
          title: '提醒',
          template: '手机号不能为空!'
        });
        return;
      }

      //var InterValObj; //timer变量，控制时间
      var count = 120; //间隔函数，1秒执行
      var curCount;//当前剩余秒数
      var msg_number = "";

      $scope.usercenter_login.getCheckCode(function () {
          curCount = count;
          $scope.usercenter_login.btn_getcode_disabled = "disabled";
          $scope.usercenter_login.btn_reg_disabled = "";
          $scope.usercenter_login.btn_getcode_txt = curCount + "秒内输入验证码(" + $scope.usercenter_login.checkcode_number + ")";
          $scope.InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        },
        function (rtn) {

        });

      function SetRemainTime() {
        console.info(curCount);
        if (curCount == 0) {
          window.clearInterval($scope.InterValObj);//停止计时器
          $scope.usercenter_login.btn_getcode_disabled = "";
          $scope.usercenter_login.btn_getcode_txt = "重新发送验证码";
          $scope.usercenter_login.btn_reg_disabled = "disabled";
        }
        else {
          curCount--;
          $scope.usercenter_login.btn_getcode_txt = curCount + "秒内输入验证码(" + $scope.usercenter_login.checkcode_number + ")";
        }
        $scope.$apply();
      }
    };

  })


  .controller('ctr_selectroom', function ($scope, $stateParams, $state, $log, service_usercenter, service_roomselect) {
    $scope.usercenter = service_usercenter;

    var roomselect = service_roomselect;
    $scope.roomselect = roomselect;
    $scope.return_text = "";

    $scope.roomid = "0";
    $scope.roomname = "";

    $scope.getChildUnit = function (pid, p_title) {
      $scope.roomselect.getUnits(pid).then(function (rtn) {

          if (rtn.d != undefined) {
            $scope.roomselect.units = [];
            $.each(rtn.d, function (k, v) {
              var itm = {
                pid: v.pid,
                p_title: v.p_title,
                uid: v.uid,
                unit_title: v.unit_title,
                child: v.child,//子个数
                p_pid: v.parentid
              };
              $scope.roomselect.units.push(itm);
            });
          } else {
            //$scope.usercenter_reg.unit=pid;
            //$scope.usercenter_reg.unitname=p_title;
            $scope.closeSelectRoomWnd(pid, p_title);
          }
          var s_txt = $scope.roomselect.getFirst().p_title;
          if (s_txt == "") {
            s_txt = "选择房号";
          }
          $scope.return_text = s_txt;
          $scope.$apply();
        },
        function (rtn) {
        });
    }

    //返回
    $scope.getParentUnit = function () {
      var first = $scope.roomselect.getFirst();
      if (first.p_title == "") {
        $scope.closeSelectRoomWnd("", "");
      }
      else {
        $scope.getChildUnit(first.p_pid);
      }
    };

    //first open exec ini
    $scope.getChildUnit('0', '');
  })
;
