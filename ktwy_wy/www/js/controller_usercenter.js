angular.module('ktwy.controllers', [])

  .controller('usercenter', function ($scope, $stateParams, $state, $ionicModal, $log, $ionicPopup, service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;
    //登录窗口
    $scope.login = function () {
      //$state.go("userlogin");
      $scope.openLoginWnd();

    };

    $scope.userExit = function () {
      service_usercenter_login.deviceid = service_usercenter.deviceid;
      service_usercenter_login.platform = service_usercenter.platform;

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要退出吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });
      confirmPopup.then(function (res) {
        if (res) {

          service_usercenter.userid = "0";
          service_usercenter.enumber = "";
          service_usercenter.name = "";
          service_usercenter.phone = "";
          service_usercenter.dpetid = "";
          service_usercenter.deptname = "";
          service_usercenter.sex = "";
          service_usercenter.birthday = "";
          service_usercenter.photo = "";
          service_usercenter.photo_url = "";

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

  })

  //登录
  .controller('userlogin', function ($scope, $stateParams, $state, $log, service_usercenter, service_usercenter_login, $rootScope, $ionicPopup, NativePlugin) {

    $scope.usercenter_login = service_usercenter_login;


    $scope.login = function () {
      service_usercenter_login.deviceid = service_usercenter.deviceid;
      service_usercenter_login.platform = service_usercenter.platform;

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
          service_usercenter.tags = arr_rtn[11];//获取tags

          //注册推送信息
          var pushinfo = {
            Tags: jsondal.TransTagsWY(service_usercenter.tags),
            Alias: jsondal.TransAliasWY(service_usercenter.userid)
          };
          console.info(pushinfo);
          NativePlugin.JPush_SetTagsWithAlias(pushinfo);


          window.clearInterval($scope.InterValObj);//停止计时器
          $scope.usercenter_login.btn_getcode_disabled = "";
          $scope.usercenter_login.btn_getcode_txt = "重新发送验证码";
          $scope.usercenter_login.btn_reg_disabled = "disabled";

          //登录成功则返回
          //$rootScope.$ionicGoBack();

          if ($scope.usercenter_login.willGoUrl != '') {
            if (typeof($scope.usercenter_login.willGoUrl) == "string") {
              $state.go($scope.usercenter_login.willGoUrl);
            }
            if (typeof($scope.usercenter_login.willGoUrl ) == "function") {
              $scope.usercenter_login.willGoUrl();
            }
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

    /*
     $scope.$watch('roomselect.navlist.length', function (newval, oldval) {
     console.info("-------------$scope.roomselect.navlist.length----------------"+newval);
     if (newval=="0") {
     //查询数据
     $scope.getChildUnit('0', '');
     $scope.roomselect.addnavlist('0','房号:','-1');
     }

     });
     */

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

;
