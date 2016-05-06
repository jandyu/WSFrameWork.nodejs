angular.module('ktwy.controllers')

  .controller('express_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource,service_express) {
    $scope.usercenter=service_usercenter;
    $scope.express=service_express;

    //refresh
    $scope.refresh=function(qry)
    {
      if(qry==undefined || qry=="" || qry==null)
      {
        qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
      }

      //清空数据
      $scope.express.model_list=[];

      //查询数据
      $scope.express.getlist(qry).then(function(rtn){
        //刷新完成
        $scope.$broadcast('scroll.refreshComplete');
        $scope.$apply();
      },function(rtn){

      });

    };


    //loadmore
    $scope.loadMore=function(qry)
    {
      if(qry==undefined || qry=="" || qry==null)
      {
        qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
      }
      //查询数据
      $scope.express.getlist(qry).then(function(rtn){
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      },function(rtn){

      });

    };


    //init
    $scope.init=function()
    {
      $scope.refresh();
    };

    //execute init function
    $scope.init();
  })
;
