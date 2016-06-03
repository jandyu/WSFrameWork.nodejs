angular.module('ktwy.controllers', [])

  .controller('usercenter', function ($scope, $stateParams, $state, $log, $ionicModal, $ionicPopup,NativePlugin, service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;

    //登录窗口
    $scope.login = function () {
      $scope.openLoginWnd();
      //$state.go("userlogin");
    };
    $scope.userExit = function () {

      service_usercenter_login.deviceid=service_usercenter.deviceid;
      service_usercenter_login.platform=service_usercenter.platform;

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要退出吗?',
        cancelText: '取消',
        cancelType: 'button-orange',
        okText: '确定',
        okType: 'button-orange'
      });
      confirmPopup.then(function (res) {
        if (res) {
          service_usercenter.userid = "0";
          service_usercenter.roomid = "";
          service_usercenter.roompath = "";
          service_usercenter.name = "";
          service_usercenter.phone = "";
          service_usercenter.nickname = "";

          $scope.usercenter_login.userLoginOut().then(
            function (rtn) {
            }, function (rtn) {
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

    //位置信息
    $scope.locationinfo={test:[]};
    $scope.getcurrlocation=function()
    {
      NativePlugin.GetCurrLocation(function(position){
        //$scope.locationinfo.info=JSON.stringify(position);
        $scope.locationinfo.test.push({info:JSON.stringify(position)});
      },function(error){
        $scope.locationinfo.test.push({info:JSON.stringify(error)});
      },{});
    };

    $scope.watchlocation=function()
    {
      $scope.locationinfo.test=[];
      NativePlugin.WatchLocation(function(position){
        $scope.locationinfo.test.push({info:JSON.stringify(position)});
        $scope.$apply();
      },function(error){
        $scope.locationinfo.test.push({info:JSON.stringify(error)});
      },{});
    };
    $scope.stopwatchlocation=function()
    {
      NativePlugin.WatchLocationClear();
    };

    $scope.vibratet=function()
    {
      NativePlugin.Vibrate([0,1000,1000]);
    };

    $scope.beep=function()
    {
      navigator.notification.beep();
    };


  })

  //登录
  .controller('userlogin', function ($scope, $stateParams, $state, $log, $rootScope, $ionicPopup, $ionicModal, service_usercenter, service_usercenter_login) {

    $scope.usercenter_login = service_usercenter_login;


    $scope.login = function () {

      service_usercenter_login.deviceid=service_usercenter.deviceid;
      service_usercenter_login.platform=service_usercenter.platform;

      $scope.usercenter_login.userLogin().then(
        function (rtn) {
          rtn = jsondal.AnaRtn(rtn);
          var arr_rtn = rtn.split(',');
          if (arr_rtn[0] == "0") {
            service_usercenter.userid = arr_rtn[1];
            service_usercenter.roomid = arr_rtn[2];
            service_usercenter.roompath = arr_rtn[3];
            service_usercenter.name = arr_rtn[4];
            service_usercenter.phone = arr_rtn[5];
            service_usercenter.nickname = arr_rtn[6];
            service_usercenter.sex = arr_rtn[7];
            service_usercenter.birthday = new Date(arr_rtn[8]);
            service_usercenter.photo = arr_rtn[9];
            var purl = arr_rtn[10];
            if (purl == "") {
              purl = "img/person_photo_default.png";
            }
            else {
              purl = wwwurl + purl.substr(1);
            }
            service_usercenter.photo_url = purl;

            //登录成功则返回
            if ($scope.usercenter_login.willGoUrl != '')
            {
              console.info($scope.usercenter_login.willGoUrl);
              $state.go($scope.usercenter_login.willGoUrl);
              $scope.usercenter_login.willGoUrl = "";
              $scope.closeLoginWnd();
            }
            else {
              $scope.closeLoginWnd();
            }
          }
          else {
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '登录失败!' + arr_rtn[1]
            });
          }
        }, function (rtn) {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '登录失败!' + rtn
          });
        });
    };


    $scope.goreg = function () {
      //$state.go("userreg");
      $scope.openRegWnd();
    };


    //注册窗口
    $ionicModal.fromTemplateUrl('templates/usercenter/userreg.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.RegWnd = modal;
    });


    $scope.openRegWnd = function () {
      //此处初始化
      $scope.RegWnd.show();
    };

    $scope.closeRegWnd = function () {
      $scope.RegWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.RegWnd.remove();
    });

  })

  //注册-step1
  .controller('userreg', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, service_usercenter_reg) {
    var usercenter_reg = service_usercenter_reg.usercenter_reg;
    $scope.usercenter_reg = usercenter_reg;

    $scope.InterValObj = null; //timer变量，控制时间
    //获取短信验证码
    $scope.getCheckCode = function () {

      //向后台发送处理数据
      var ls_phone = usercenter_reg.username;
      if (!util.IsPhone(ls_phone)) {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '手机号不能为空!'
        });
        return;
      }

      //var InterValObj; //timer变量，控制时间
      var count = 120; //间隔函数，1秒执行
      var curCount;//当前剩余秒数
      var msg_number = "";

      service_usercenter_reg.getCheckCode(function () {
          curCount = count;
          usercenter_reg.btn_getcode_disabled = "disabled";
          usercenter_reg.btn_reg_disabled = "";
          usercenter_reg.btn_getcode_txt = curCount + "秒内输入验证码(" + usercenter_reg.checkcode_number + ")";
          $scope.InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        },
        function (rtn) {

        });

      function SetRemainTime() {
        console.info(curCount);
        if (curCount == 0) {
          window.clearInterval($scope.InterValObj);//停止计时器
          usercenter_reg.btn_getcode_disabled = "";
          usercenter_reg.btn_getcode_txt = "重新发送验证码";
          usercenter_reg.btn_reg_disabled = "disabled";
        }
        else {
          curCount--;
          usercenter_reg.btn_getcode_txt = curCount + "秒内输入验证码(" + usercenter_reg.checkcode_number + ")";
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
          okType: 'button-orange',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '手机号不能为空!'
        });
        return;
      }

      //校验验证码是否正确
      service_usercenter_reg.checkCode(function (rtn) {
        if (rtn.indexOf('0') == 0) {
          var o_arr = rtn.split(",");
          if (o_arr.length == 3) {
            var uid = o_arr[1];
            var uname = o_arr[2];
            usercenter_reg.unit = uid;
            usercenter_reg.unitname = uname;
            if (uid != "") {
              usercenter_reg.selectroom_disabled = true;
            }
            else {
              usercenter_reg.selectroom_disabled = false;
            }
            window.clearInterval($scope.InterValObj);//停止计时器
            usercenter_reg.btn_getcode_disabled = "";
            usercenter_reg.btn_getcode_txt = "重新发送验证码";
            //跳转
            //$state.go("userreg_step2");
            $scope.openReg2Wnd();

          }
          if (o_arr.length == 2) {
            var msg = o_arr[1];

            usercenter_reg.selectroom_disabled = false;

            window.clearInterval($scope.InterValObj);//停止计时器
            usercenter_reg.btn_getcode_disabled = "";
            usercenter_reg.btn_getcode_txt = "重新发送验证码";

            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: msg
            });
          }


        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '验证码不正确!'
          });
        }
      }, function (rtn) {

      });
    };


    //注册窗口step2
    $ionicModal.fromTemplateUrl('templates/usercenter/userreg_step2.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.Reg2Wnd = modal;
    });


    $scope.openReg2Wnd = function () {
      //此处初始化
      $scope.Reg2Wnd.show();
    };

    $scope.closeReg2Wnd = function () {
      $scope.Reg2Wnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.Reg2Wnd.remove();
    });


  })

  //注册-step2
  .controller('userreg_step2', function ($scope, $stateParams, $state, $log, service_usercenter_reg, $ionicModal, service_roomselect, $ionicPopup, service_usercenter) {
    var usercenter_reg = service_usercenter_reg.usercenter_reg;
    $scope.usercenter_reg = usercenter_reg;

    var roomselect = service_roomselect;
    $scope.roomselect = roomselect;

    $scope.return_text = "";

    $ionicModal.fromTemplateUrl('templates/usercenter/selectroom.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.SelectRoomWnd = modal;
    });

    $scope.openSelectRoomWnd = function () {
      //execute inistall information
      $scope.SelectRoomWnd.show();
    };

    $scope.closeSelectRoomWnd = function (roomid, roompath) {

      if (roomid != '') {
        $scope.usercenter_reg.unit = roomid;
        $scope.usercenter_reg.unitname = roompath;
      }

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });


    $scope.regUser = function () {

      if (usercenter_reg.unit == "0") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '请选择房号!'
        });
        return;
      }

      if (usercenter_reg.name == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '姓名不能为空!'
        });
        return;
      }

      if (usercenter_reg.nickname == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '昵称不能为空!'
        });
        return;
      }


      if (usercenter_reg.password == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
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


      service_usercenter_reg.regUser().then(function (rtn) {
        var rtn = jsondal.AnaRtn(rtn);
        if (rtn.indexOf('0') == 0) {
          //初始化全局用户信息

          var userid = rtn.split(',')[1];
          service_usercenter.userid = userid;
          service_usercenter.name = $scope.usercenter_reg.name;
          service_usercenter.phone = $scope.usercenter_reg.username;
          service_usercenter.nickname = $scope.usercenter_reg.nickname;
          service_usercenter.roomid = $scope.usercenter_reg.unit;
          service_usercenter.roompath = $scope.usercenter_reg.unitname;

          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '注册成功!'
          });
          //$state.go("tab.usercenter");
          $scope.closeReg2Wnd();
          $scope.closeRegWnd();
          $scope.closeLoginWnd();

        } else {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: rtn
          });
        }
      }, function (err) {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '注册失败' + err
        });
      });
    }
    $scope.openModal = function () {
      $scope.getChildUnit(0);
      $scope.modal.show();
    };

    $scope.closeModal = function () {
      $scope.modal.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.modal.remove();
    });
  })

  .controller('user_set', function ($scope, $stateParams, $state, $log, $ionicModal, $ionicPopup, NativePlugin, service_usercenter, service_usercenter_login, service_wy_resource) {
    $scope.usercenter = service_usercenter;

    //修改图像
    $scope.change_photo = function () {
      //获取图片
      NativePlugin.GetPicture(function (imageData) {

        console.log(NativePlugin.PictureModel.image_url);

        //上传图片
        NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {

          console.info('--------uploadfile callback------------');
          console.info(rtn);

          //保存资源
          service_wy_resource.model.category = '业主个人头像';
          service_wy_resource.model.url = rtn;
          service_wy_resource.objid = $scope.usercenter.userid;

          $scope.usercenter.photo_url = wwwurl + rtn.substr(1);

          service_wy_resource.SaveResource(function (rtn) {
            console.info('--------save resource callback------------');
            console.info(rtn);

            $scope.usercenter.photo = jsondal.AnaRtn(rtn);
            //保持照片信息
            $scope.usercenter.saveUser(function (rtn) {
              console.info('--------save user callback------------');
              console.info(rtn);

              $scope.$apply();
            }, function (rtn) {
              console.info('--------save user fail callback------------');
              console.info(rtn);
            });

          }, function (rtn) {
          });


        }, function (rtn) {
          console.log(JSON.stringify(rtn));
        }, {});

      }, function (message) {

      }, {allowEdit: true});
    };

    $scope.saveUser = function () {
      $scope.usercenter.saveUser(function (rtn) {
      }, function (rtn) {
        console.info(rtn);
      });
    };

    //账号设置详情页
    $ionicModal.fromTemplateUrl('templates/usercenter/user_set_detail.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_set_detail = modal;
    });

    $scope.open_wnd_user_set_detail = function (type, title) {
      //此处初始化
      $scope.usercenter.type = type;
      $scope.usercenter.title = title;
      $scope.wnd_user_set_detail.show();
    };

    $scope.close_wnd_user_set_detail = function () {

      $scope.wnd_user_set_detail.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_set_detail.remove();
    });
  })

  .controller('user_set_detail', function ($scope, $stateParams, $state, $log, $ionicModal, $ionicPopup, service_usercenter) {
    $scope.usercenter = service_usercenter;
    $scope.saveUser = function () {
      $scope.usercenter.saveUser(function (rtn) {
        $scope.close_wnd_user_set_detail();
      }, function (rtn) {
        console.info(rtn);
      });
    };

    $scope.m_model = {oldpwd: '', newpwd: '', reppwd: ''};

    $scope.changepassword = function () {
      if ($scope.m_model.oldpwd == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '原密码不能为空!'
        });

        return;
      }

      if ($scope.m_model.newpwd == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '新密码不能为空!'
        });

        return;
      }

      if ($scope.m_model.newpwd != $scope.m_model.reppwd) {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '两次输入的密码不一致!'
        });

        return;
      }

      $scope.usercenter.changepassword($scope.m_model, function (rtn) {

        rtn = jsondal.AnaRtn(rtn);
        var arr_rtn = rtn.split(',');
        if (arr_rtn[0] == "0") {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '修改成功!'
          });
          $scope.close_wnd_user_set_detail();

          $scope.m_model = {oldpwd: '', newpwd: '', reppwd: ''};
        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '原密码不正确!'
          });
        }

      }, function (rtn) {
        console.info(rtn);
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: rtn
        });
      });

    };


    $scope.InterValObj = null; //timer变量，控制时间
    $scope.m_phone_model = {
      oldphone: '',
      newphone: '',
      code: '',
      btn_getcode_disabled: '',//获取验证码
      btn_reg_disabled: 'disabled',//确认提交
      btn_getcode_txt: '获取验证码',//获取验证码文本
      checkcode_number: ''//短信编号
    };

    //获取短信验证码
    $scope.getCheckCode_changephone = function () {

      //向后台发送处理数据
      var ls_phone = $scope.m_phone_model.newphone;//接受短信的手机
      if (!util.IsPhone(ls_phone)) {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '手机号输入有误!'
        });
        return;
      }
      if (ls_phone == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '手机号不能为空!'
        });
        return;
      }

      //var InterValObj; //timer变量，控制时间
      var count = 120; //间隔函数，1秒执行
      var curCount;//当前剩余秒数
      var msg_number = "";

      $scope.usercenter.getCheckCode_updphone($scope.m_phone_model, function (rtn) {
          curCount = count;
          $scope.m_phone_model.checkcode_number = rtn;
          $scope.m_phone_model.btn_getcode_disabled = "disabled";
          $scope.m_phone_model.btn_reg_disabled = "";
          $scope.m_phone_model.btn_getcode_txt = curCount + "秒内输入验证码(" + $scope.m_phone_model.checkcode_number + ")";
          $scope.InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        },
        function (rtn) {

        });

      function SetRemainTime() {
        console.info(curCount);
        if (curCount == 0) {
          window.clearInterval($scope.InterValObj);//停止计时器
          $scope.m_phone_model.btn_getcode_disabled = "";
          $scope.m_phone_model.btn_getcode_txt = "重新发送验证码";
          $scope.m_phone_model.btn_reg_disabled = "disabled";
        }
        else {
          curCount--;
          $scope.m_phone_model.btn_getcode_txt = curCount + "秒内输入验证码(" + $scope.m_phone_model.checkcode_number + ")";
        }
        $scope.$apply();
      }
    };


    $scope.changephone = function () {
      if ($scope.usercenter.phone == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '原手机号不能为空!'
        });

        return;
      }

      if ($scope.m_phone_model.newphone == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '新手机号不能为空!'
        });

        return;
      }

      if ($scope.m_phone_model.code == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '验证码不能为空!'
        });

        return;
      }

      $scope.usercenter.changephone($scope.m_phone_model, function (rtn) {

        rtn = jsondal.AnaRtn(rtn);
        var arr_rtn = rtn.split(',');
        if (arr_rtn[0] == "0") {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: '修改成功!'
          });
          $scope.close_wnd_user_set_detail();

          $scope.m_phone_model = {
            oldphone: '',
            newphone: '',
            code: '',
            btn_getcode_disabled: '',//获取验证码
            btn_reg_disabled: 'disabled',//确认提交
            btn_getcode_txt: '获取验证码',//获取验证码文本
            checkcode_number: ''//短信编号
          };
        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            okType: 'button-orange',
            template: arr_rtn[1]
          });
        }

      }, function (rtn) {
        console.info(rtn);
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: rtn
        });
      });

    };


  })

  .controller('ctr_selectroom', function ($scope, $stateParams, $state, $log, $ionicScrollDelegate, service_usercenter, service_roomselect) {
    $scope.usercenter = service_usercenter;

    var roomselect = service_roomselect;
    $scope.roomselect = roomselect;
    $scope.return_text = "";

    $scope.roomid = "0";
    $scope.roomname = "";

    $scope.getChildUnit = function (pid, p_title) {
      if (pid == "-1") {
        pid = "0";
      }
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
                p_pid: v.parentid,
                title: v.title
              };
              $scope.roomselect.units.push(itm);
              $ionicScrollDelegate.resize();
            });
          } else {

            //$scope.roomselect.navlist=[];
            //$scope.roomselect.units=[];
            //$scope.closeSelectRoomWnd(pid, p_title);

            $scope.closewnd(pid, p_title);
          }

          $scope.$apply();
        },
        function (rtn) {
        });
    };

    //点击导航--顶部
    $scope.nav = function (id, title, pid) {
      $scope.roomselect.interceptnavlist(id);
      $scope.getChildUnit(pid, '');
    };

    //点击表格--表格
    $scope.navtable = function (id, title, pid, roompath) {
      $scope.roomselect.addnavlist(id, title, pid);
      $scope.getChildUnit(id, roompath);
    };

    //返回
    $scope.closewnd = function (roomid, roompath) {

      $scope.roomselect.navlist = [];
      $scope.roomselect.units = [];

      $scope.getChildUnit('0', '');
      $scope.roomselect.addnavlist('0', '房号:', '-1');

      $scope.closeSelectRoomWnd(roomid, roompath);
    };

    //first open exec ini
    $scope.getChildUnit('0', '');
    $scope.roomselect.addnavlist('0', '房号:', '-1');

  })

;

