angular.module('ktwy.controllers')

  .controller('visitor_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_visitor) {
    $scope.usercenter = service_usercenter;
    $scope.visitor = service_visitor;

    //ionNavBackButton.showBackButton


    $scope.qeryfilters={roomid:'0',selectidx:'0'};
    //修改过滤条件
    $scope.changequeryfilter=function(idx)
    {
      $scope.qeryfilters.selectidx=idx;
      $scope.refresh();
    };
    //获取当前查询条件
    $scope.getQry=function()
    {
      var qry={};
      if($scope.qeryfilters.selectidx=="0")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '未过期', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '0', 'andor': ''}];
      }

      if($scope.qeryfilters.selectidx=="1")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '未过期', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '1', 'andor': ''}];
      }

      if($scope.qeryfilters.selectidx=="2")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '已过期', 'andor': ''}];
      }
      return qry;
    };

    //按照房号查询
    $scope.querybyroomid=function()
    {
      var qry={};
      if($scope.qeryfilters.selectidx=="0")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '未过期', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '0', 'andor': 'and'},
          {'col': 'roomid', 'logic': '=', 'val': $scope.qeryfilters.roomid, 'andor': ''}];
      }

      if($scope.qeryfilters.selectidx=="1")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '未过期', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '1', 'andor': 'and'},
          {'col': 'roomid', 'logic': '=', 'val': $scope.qeryfilters.roomid, 'andor': ''}];
      }

      if($scope.qeryfilters.selectidx=="2")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'expird', 'logic': '=', 'val': '已过期', 'andor': 'and'},
          {'col': 'roomid', 'logic': '=', 'val': $scope.qeryfilters.roomid, 'andor': ''}];
      }
      $scope.refresh(qry,"0");
    };


    //通用功能-------------------------------
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //refresh
    $scope.refresh = function (qry,clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry=$scope.getQry();
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

      $scope.refresh();
    };

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

    //选择房号
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
      $scope.qeryfilters.roomid=roomid;
      $scope.qeryfilters.roompath=roompath;
      $scope.querybyroomid();
      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.SelectRoomWnd.remove();
    });

    //execute init function
    $scope.init();
  })

  .controller('visitor_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_visitor) {
    $scope.usercenter = service_usercenter;
    $scope.visitor = service_visitor;


    //保存
    $scope.receivevisitor=function()
    {

      if($scope.visitor.model.status!="0" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-blue',
          template: '状态不正确!'
        });
        return;
      }


      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定访客到达了吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });
      confirmPopup.then(function(res) {
        if(res) {

          $scope.visitor.model.receiver=$scope.usercenter.name;
          $scope.visitor.model.dtm_real=util.DateFormat(new Date(),'yyyy-MM-dd');
          $scope.visitor.model.status="1";

          $scope.visitor.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType:'button-blue',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }},function(rtn){

      });



    };

  })
;
