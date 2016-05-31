angular.module('ktwy.controllers')

  .controller('bmap_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource,service_bmap) {
    $scope.usercenter = service_usercenter;
    $scope.bmap = {map:null,local:null};

    $scope.ini=function()
    {
      NativePlugin.GetCurrLocation(function(position){


        $scope.bmap.map = new BMap.Map("allmap");
        $scope.bmap.map.centerAndZoom(new BMap.Point(position.coords.longitude, position.coords.latitude), 15);
        $scope.bmap.map.enableScrollWheelZoom(true);

      },function(error){

      },{});
    };

    $scope.qeryfilters={search_txt:""};
    $scope.searchAddress=function()
    {
      $scope.bmap.local.search($scope.qeryfilters.search_txt);
    };
    $scope.ini();
  })
;
