angular.module('ktwy.controllers')

  .controller('visitor_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_visitor) {
    $scope.usercenter = service_usercenter;
    $scope.visitor = service_visitor;

    //ionNavBackButton.showBackButton


    $scope.qeryfilters={status:'0',roomid:'0'};
    //修改过滤条件
    $scope.changequeryfilter=function(status)
    {
      $scope.qeryfilters.status=status;
    };

    //通用功能-------------------------------
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //refresh
    $scope.refresh = function (qry,clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'roomid', 'logic': '=', 'val': $scope.usercenter.roomid, 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      $scope.loadMore_qry=qry;
      //查询数据
      if(clearflag==undefined)
      {
        clearflag='0';
      }
      $scope.visitor.getlist(qry,clearflag).then(function (rtn) {
        //刷新完成
        $scope.$broadcast('scroll.refreshComplete');
        $scope.$apply();
      }, function (rtn) {

      });

    };


    //loadmore
    $scope.loadMore = function () {
      var qry=$scope.loadMore_qry;
      //查询数据
      $scope.visitor.getlist(qry,"1").then(function (rtn) {
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      }, function (rtn) {

      });

    };


    //init
    $scope.init = function () {

      $scope.$watch('qeryfilters.status', function (newval, oldval) {

        if (newval != oldval) {
          //查询数据
          $scope.refresh();
        }

      });


      $scope.$watch('qeryfilters.roomid', function (newval, oldval) {

        console.info('--------qeryfilters.roomid---------')
        if (newval !='0') {
          //查询数据
          var qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'roomid', 'logic': '=', 'val': newval, 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
          $scope.refresh(qry, "0");

        }
      });

      $scope.refresh();
    };

    //按照房号查询
    //选择房号
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

      if(roomid!='') {
        $scope.qeryfilters.roomid = roomid;
      }

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });


    //修改
    $scope.open_wnd_visitor_edit = function (iid) {
      if (iid == "0") {
        $scope.visitor.ini_model($scope.usercenter);
      }
      else
      {
        $scope.visitor.getmodel(iid).then(function(rtn){
          console.info("----------getmodel----------------");
          console.info(rtn);
        },function(rtn){

        });
      }
      $state.go("root.visitor_edit");
    };

    //execute init function
    $scope.init();
  })

  .controller('visitor_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_visitor) {
    $scope.usercenter = service_usercenter;
    $scope.visitor = service_visitor;


    //保存
    $scope.savevisitor=function()
    {
      if($scope.visitor.model.roomid=="" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '请选择房号!'
        });
        return;
      }


      if($scope.visitor.model.status!="0" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '状态不正确,不能修改!'
        });
        return;
      }


      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要修改吗?',
        cancelText: '取消',
        cancelType: 'button-orange',
        okText: '确定',
        okType: 'button-orange'
      });
      confirmPopup.then(function(res) {
        if(res) {


          $scope.visitor.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType:'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }},function(rtn){

      });



    };


    //选择房号
    $ionicModal.fromTemplateUrl('templates/usercenter/selectroom.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.SelectRoomWnd = modal;
    });

    $scope.openSelectRoomWnd = function () {
      //execute inistall information

      if($scope.visitor.model.status=='1')return;

      $scope.SelectRoomWnd.show();
    };

    $scope.closeSelectRoomWnd = function (roomid, roompath) {

      $scope.visitor.model.roomid = roomid;
      $scope.visitor.model.roompath = roompath;

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });

  })
;
