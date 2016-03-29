angular.module('ktwy.controllers')

    .controller('home_wy', function ($scope, $stateParams, $state, $log,$ionicModal, service_usercenter,service_usercenter_login) {
        $scope.usercenter = service_usercenter;
        $scope.usercenter_login=service_usercenter_login;
        //登录窗口

      //登陆后跳转到指定页面,未登陆则跳转到登陆页面,登陆完成后再跳转到要跳转到页面
      $scope.goAfterLogin=function(url)
      {
        //$state.go(url);

        if($scope.usercenter.checkLogin()==true)
        {
          $state.go(url);
        }
        else
        {
          $scope.usercenter_login.willGoUrl=url;
          $scope.openLoginWnd();
        }
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
    })


  .controller('master_query', function ($scope, $stateParams, $state, $log,$ionicModal, service_usercenter,service_master_query) {
    $scope.usercenter = service_usercenter;
    $scope.master_query=service_master_query;


    $ionicModal.fromTemplateUrl('templates/usercenter/selectroom.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function(modal) {

      $scope.SelectRoomWnd = modal;
    });

    $scope.openSelectRoomWnd = function() {
      //execute inistall information
      $scope.SelectRoomWnd .show();
    };

    $scope.closeSelectRoomWnd = function(roomid,roompath) {
      $scope.master_query.roomid=roomid;
      $scope.master_query.roompath=roompath;

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.SelectRoomWnd.remove();
    });

    $scope.querybyroomid=function()
    {
      $scope.master_query.getMaster({'col':'roomid','logic':'=','val': $scope.master_query.roomid,'andor':''})
        .then(function(){
          $scope.$apply();
        },function(){

        });
    };
    $scope.querybyname=function()
    {
      $scope.master_query.getMaster({'col':'name','logic':'=','val': $scope.master_query.name,'andor':''})
        .then(function(){
          $scope.$apply();
        },function(){

        });
    };


  })
  .controller('vehicle_query', function ($scope, $stateParams, $state, $log, service_usercenter,service_vehicle_query) {
    $scope.usercenter = service_usercenter;
    $scope.vehicle_query=service_vehicle_query;


    $scope.queryplate=function()
    {
      $scope.vehicle_query.getVehicle()
        .then(function(){
          $scope.$apply();
        },function(){

        });
    };
  })
;
