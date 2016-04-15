// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('ktwy', ['ionic', 'ktwy.controllers', 'ktwy.services','ngResource'])

  .run(function ($ionicPlatform) {
    $ionicPlatform.ready(function () {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.disableScroll(true);

      }
      if (window.StatusBar) {
        // org.apache.cordova.statusbar required
        StatusBar.styleDefault();
      }
    });
  })
  .filter('Imgurl', function (srvRESTfulAPI) {
    return function (imgurl) {
      var rtnurl = imgurl;
      //var r = RegExp(/^([0-9,a-f,A-F]*-[0-9,a-f,A-F]*){4}$/);
      var r = RegExp(/^\/images\//i);
      if (r.test(imgurl)) {
        // id
        rtnurl = srvRESTfulAPI.config.urlBase + imgurl;
      }
      return rtnurl;
    }
  })
  .filter('to_trusted', ['$sce', function ($sce) {
    return function (text) {
      return $sce.trustAsHtml(text);
    }
  }])
  .filter('to_dateformat',function(){
    return function (sdt,ff){
      if(sdt=="") return "";
      var dt=new Date(sdt);
      return util.DateFormat(dt,ff);
    };
  })

  .config(function ($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

    $ionicConfigProvider.platform.ios.tabs.style('standard');
    $ionicConfigProvider.platform.ios.tabs.position('bottom');
    $ionicConfigProvider.platform.android.tabs.style('standard');
    $ionicConfigProvider.platform.android.tabs.position('standard');

    $ionicConfigProvider.platform.ios.navBar.alignTitle('center');
    $ionicConfigProvider.platform.android.navBar.alignTitle('left');

    $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
    $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');

    $ionicConfigProvider.platform.ios.views.transition('ios');
    $ionicConfigProvider.platform.android.views.transition('android');


    // Ionic uses AngularUI Router which uses the concept of states
    // Learn more here: https://github.com/angular-ui/ui-router
    // Set up the various states which the app can be in.
    // Each state's controller can be found in controllers.js
    $stateProvider

    // setup an abstract state for the tabs directive
      .state('tab', {
        url: '/tab',
        templateUrl: 'templates/tabs.html'
      })

      // Each tab has its own nav history stack:

      .state('tab.home', {
        url: '/home',
        views: {
          'tab-home': {
            templateUrl: 'templates/tab-home-wy.html',
            controller:'home_wy'
          }
        }
      })
      //用户中心
      .state('tab.usercenter', {
        url: '/usercenter',
        views: {
          'tab-usercenter': {
            templateUrl: 'templates/tab-usercenter.html',
            controller: 'usercenter'
          }
        }
      })
      .state('tab.master_query', {
        url: '/master_query',
        views: {
          'tab-home': {
            templateUrl: 'templates/usercenter/master_query.html',
            controller:'master_query'
          }
        }
      })
      .state('tab.vehicle_query', {
        url: '/vehicle_query',
        views: {
          'tab-home': {
            templateUrl: 'templates/usercenter/vehicle_query.html',
            controller:'vehicle_query'
          }
        }
      })
      .state('tab.user_repair', {
        url: '/user_repair',
        views: {
          'tab-home': {
            templateUrl: 'templates/usercenter/user_repair.html',
            controller:'user_repair'
          }
        }
      })
    ;

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise('/tab/home');

  });
