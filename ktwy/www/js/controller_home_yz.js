angular.module('ktwy.controllers')

  .controller('maintab', function ($scope, $ionicTabsDelegate,$ionicNavBarDelegate) {
    $scope.tab ={currtitle:'首页'};
    $scope.clicktab = function (u) {
      //$ionicNavBarDelegate.title($ionicTabsDelegate._instances[0].tabs[u].title);
      $scope.tab.currtitle =  $ionicTabsDelegate._instances[0].tabs[u].title;
      $ionicTabsDelegate.select(u);
      return true;
    }
  })


  .controller('home', function ($scope, $stateParams, $state, $log, $ionicModal,service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;


    //自动登录
    $scope.$on("kwsq-device-on-ready",function(event,msg){

      console.info("service_usercenter:"+service_usercenter.deviceid);
      service_usercenter_login.deviceid=service_usercenter.deviceid;
      service_usercenter_login.platform=service_usercenter.platform;
      $scope.usercenter_login.userLoginDevice().then(
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
            service_usercenter.sex=arr_rtn[7];
            service_usercenter.birthday=new Date(arr_rtn[8]);
            service_usercenter.photo=arr_rtn[9];
            var purl=arr_rtn[10];
            if(purl=="")
            {
              purl="img/person_photo_default.png";
            }
            else
            {
              purl=wwwurl+purl.substr(1);
            }
            service_usercenter.photo_url=purl;

            console.info("自动登录成功!");
            return;
          }
          console.info("自动登失败!");
        },function(rtn){
          console.info("自动登录失败!"+rtn);
        });

    });


    //登录窗口
    //登陆后跳转到指定页面,未登陆则跳转到登陆页面,登陆完成后再跳转到要跳转到页面
    $scope.goAfterLogin = function (url) {
      //$state.go(url);

      if ($scope.usercenter.checkLogin() == true) {
        $state.go(url);
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

    $scope.doRefresh=function()
    {
      $scope.$broadcast('scroll.refreshComplete');
    };
    $scope.reload=function()
    {
      window.location.reload();
    }
  })
;
