angular.module('ktwy.controllers')

  .controller('maintab', function ($scope, $ionicTabsDelegate, $ionicNavBarDelegate, $ionicModal, service_usercenter, service_usercenter_login) {

    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;


    $scope.tab = {currtitle: '首页', idx: 0};
    $scope.clicktab = function (u) {
      if (u != 2) {
        //$ionicNavBarDelegate.title($ionicTabsDelegate._instances[0].tabs[u].title);
        $scope.tab.currtitle = $ionicTabsDelegate._instances[0].tabs[u].title;
        $ionicTabsDelegate.select(u);
        return true;
      }
      else {
        $scope.tab.idx = u;
        $scope.goAfterLogin(u);
      }
    };

    //登陆后跳转到指定页面,未登陆则跳转到登陆页面,登陆完成后再跳转到要跳转到页面
    $scope.goAfterLogin = function (u) {
      if ($scope.usercenter.checkLogin() == true) {
        $scope.tab.currtitle = $ionicTabsDelegate._instances[0].tabs[u].title;
        $ionicTabsDelegate.select(u);
        return true;
      }
      else {
        $scope.usercenter_login.willGoUrl = "";
        $scope.openLoginWnd();
      }

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

      if ($scope.usercenter.checkLogin() == true) {
        $scope.tab.currtitle = $ionicTabsDelegate._instances[0].tabs[$scope.tab.idx].title;
        $ionicTabsDelegate.select($scope.tab.idx);
        return true;
      }

    };
    $scope.$on('$destroy', function () {
      $scope.LoginWnd.remove();
    });
  })


  .controller('home', function ($scope, $stateParams, $state, $log, $ionicModal, service_usercenter, service_usercenter_login, NativePlugin) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;


    //自动登录
    $scope.$on("kwsq-device-on-ready", function (event, msg) {

      console.info("service_usercenter:" + service_usercenter.deviceid);
      service_usercenter_login.deviceid = service_usercenter.deviceid;
      service_usercenter_login.platform = service_usercenter.platform;

      //初始化jpush
      var jpush_option = {
        OpenNotificationCallBackOption:{
          link:function(params)
          {
            var notify=params;
            notify.checklogin=true;

            if(!notify.url)
            {
              console.error('params url undefined');
              return;
            }
            if(!notify.iid)
            {
              console.error('params iid undefined');
              return;
            }

            //业主通知时不需要验证是否登陆
            if(notify.url=="root.news_yz_detail"){
              notify.checklogin=false;
            }

            if(notify.url!="") {
              //登陆成功后才能看到
              if(notify.checklogin==true) {
                $scope.goAfterLogin(
                  function () {
                    $state.go(notify.url, {iid: notify.iid});
                  }
                );
              }
              else//不需要登陆
              {
                $state.go(notify.url, {iid: notify.iid});
              }
            }
          }
        }
      };
      NativePlugin.JPush_Init(jpush_option);

      $scope.usercenter_login.userLoginDevice().then(
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
            service_usercenter.tags = arr_rtn[11];//获取tags

            console.info("自动登录成功!");

            //注册推送信息
            NativePlugin.JPush_SetTagsWithAlias({
              Tags: jsondal.TransTagsYZ(service_usercenter.tags),
              Alias: jsondal.TransAliasYZ(service_usercenter.userid)
            });

            return;
          }
          console.info("自动登失败!");
        }, function (rtn) {
          console.info("自动登录失败!" + rtn);
        });

    });


    //登录窗口
    //登陆后跳转到指定页面,未登陆则跳转到登陆页面,登陆完成后再跳转到要跳转到页面
    //参数说明:url
    //字符串:表示url,如果登陆成功,则系统会调用$state.go(url)
    //function:表示回调函数,如果登陆成功,则系统执行该回调函数
    $scope.goAfterLogin = function (url) {
      if ($scope.usercenter.checkLogin() == true) {
        if (typeof(url) == "string") {
          $state.go(url);
        }
        if (typeof(url) == "function") {
          url();
        }
      }
      else {
        $scope.usercenter_login.willGoUrl = url;
        $scope.openLoginWnd();
      }

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

    $scope.doRefresh = function () {
      $scope.$broadcast('scroll.refreshComplete');
    };
    $scope.reload = function () {
      window.location.reload();
    };

    $scope.inijpush = function () {
      NativePlugin.JPush_Init();
    }
  })


  .directive('rjPositionMiddle', ['$window', function ($window) {
    return {
      replace: false,
      link: function (scope, iElm, iAttrs, controller) {
        var height = $window.innerHeight - 44 - 49 - iElm[0].offsetHeight;
        if (height >= 0) {
          iElm[0].style.top = (height / 2 + 44) + 'px';
        } else {
          iElm[0].style.top = 44 + 'px';
        }
      }
    }
  }])

;
