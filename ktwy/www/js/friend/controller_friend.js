angular.module('ktwy.controllers')

  .controller('friend_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_friend) {
    $scope.usercenter = service_usercenter;
    $scope.friend = service_friend;

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
          {'col': 'roomid', 'logic': '=', 'val': $scope.usercenter.roomid, 'andor': ''}];
      }
      $scope.loadMore_qry=qry;
      //查询数据
      if(clearflag==undefined)
      {
        clearflag='0';
      }
      $scope.friend.getlist(qry,clearflag).then(function (rtn) {
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
      $scope.friend.getlist(qry,"1").then(function (rtn) {
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      }, function (rtn) {

      });
    };

    //init
    $scope.init = function () {

      $scope.right_bar_ui={bg:'',navtitle:'A',navshowhide:false,
        items:[{id:'1',txt:'☆'},
          {id:'A',txt:'A'},{id:'B',txt:'B'},{id:'C',txt:'C'},{id:'D',txt:'D'},{id:'E',txt:'E'},{id:'F',txt:'F'},
          {id:'G',txt:'G'},{id:'H',txt:'H'},{id:'I',txt:'I'},{id:'J',txt:'J'},{id:'K',txt:'K'},{id:'L',txt:'L'},
          {id:'M',txt:'M'},{id:'N',txt:'N'},{id:'O',txt:'O'},{id:'P',txt:'P'},{id:'Q',txt:'Q'},{id:'R',txt:'R'},
          {id:'S',txt:'S'},{id:'T',txt:'T'},{id:'U',txt:'U'},{id:'V',txt:'V'},{id:'W',txt:'W'},{id:'X',txt:'X'},
          {id:'Y',txt:'Y'},{id:'Z',txt:'Z'},
          {id:'2',txt:'#'}]
      };



      $scope.right_bar_item_enter=function(cidx)
      {
        console.info(cidx);
        $scope.right_bar_ui.navtitle=cidx;
      };

      $scope.right_bar_touch = function(e){
        //console.log(e);
        $scope.right_bar_ui.bg="stable-bg";
        //$scope.right_bar_ui.navtitle="A";
        $scope.right_bar_ui.navshowhide=true;
      };
      $scope.right_bar_release = function(e){
        //console.log(e);
        $scope.right_bar_ui.bg="";
        //$scope.right_bar_ui.navtitle="";
        $scope.right_bar_ui.navshowhide=false;
      };



      $scope.$watch('qeryfilters.status', function (newval, oldval) {

        if (newval != oldval) {
          //查询数据
          $scope.refresh();
        }

      });

      $scope.refresh();
    };

    //修改
    $scope.open_wnd_friend_edit = function (iid) {
      if (iid == "0") {
        $scope.friend.ini_model($scope.usercenter);
      }
      else
      {
        $scope.friend.getmodel(iid).then(function(rtn){
          console.info("----------getmodel----------------");
          console.info(rtn);
        },function(rtn){

        });
      }
      $state.go("root.friend_edit");
    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //event.preventDefault();
        console.info(fromState);
        if (fromState.name == "root.friend_edit") {
          $scope.refresh();
        }
      });

    //execute init function
    $scope.init();
  })

  .controller('friend_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_friend) {
    $scope.usercenter = service_usercenter;
    $scope.friend = service_friend;


    //保存
    $scope.savefriend=function()
    {
      if($scope.friend.model.roomid=="" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '请选择房号!'
        });
        return;
      }


      if($scope.friend.model.status!="0" )
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
        template: '确定要保存吗?',
        cancelText: '取消',
        cancelType: 'button-orange',
        okText: '确定',
        okType: 'button-orange'
      });
      confirmPopup.then(function(res) {
        if(res) {


          $scope.friend.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType:'button-orange',
              template: '成功!'
            });

            //$ionicHistory.goBack();
            $state.go("root.friend_list", {}, {notify: true});

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

      if($scope.friend.model.status=='1')return;

      $scope.SelectRoomWnd.show();
    };

    $scope.closeSelectRoomWnd = function (roomid, roompath) {

      $scope.friend.model.roomid = roomid;
      $scope.friend.model.roompath = roompath;

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });

  })
;
