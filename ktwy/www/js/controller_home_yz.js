angular.module('ktwy.controllers')

  .controller('home', function ($scope, $stateParams, $state, $log, $ionicModal, service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;
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
  })
;
