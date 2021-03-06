angular.module('ktwy.controllers')

  .controller('user_repair', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,service_roomselect, service_usercenter, service_user_repair,service_dict,NativePlugin,service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    $scope.dict=service_dict;
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //$scope.dict.getDcit('repair_status',function(rtn){ });
    $scope.getRepairList=function(qry)
    {
      //var qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
      if(qry==undefined || qry=="" || qry==null)
      {
        qry = $scope.getRefreshFilter();
      }
      $scope.loadMore_qry=qry;
      //清空数据
      $scope.user_repair.model_list=[];
      $scope.user_repair.page.currpage=0;

      $scope.user_repair.getRepairList(qry,function(rtn){
        //初始化字典
        service_dict.getDcit('repair_status',function(rtn){

          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        });
        //$scope.$apply();
      },function(rtn){
        $ionicPopup.alert({
          title: '提醒',
          template: '有误!' + rtn
        });
        return;
      });
    };


    $scope.qeryfilters={roomid:'0',roompath:'',status:'',selectidx:'0'};

    //按房号查询
    $scope.querybyroomid=function()
    {
      //查询数据
      var qry = {};
      var idx=$scope.qeryfilters.selectidx;
      if(idx=="0")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '0', 'andor': 'and'}];
      }
      if(idx=="1")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': "in ('3','5') and 1=", 'val': '1', 'andor': 'and'}];
      }

      if(idx=="2")
      {
        if($scope.qeryfilters.status=="")
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'}];
        }
        else
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': 'and'}];
        }
      }

      qry.push({'col': 'roomid', 'logic': '=', 'val':$scope.qeryfilters.roomid, 'andor': ''});

      $scope.getRepairList(qry);
    };


    $scope.getRefreshFilter=function()
    {
      var qry = {};
      var idx=$scope.qeryfilters.selectidx;
      if(idx=="0")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': '0', 'andor': ''}];
      }
      if(idx=="1")
      {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': "in ('3','5') and 1=", 'val': '1', 'andor': ''}];
      }

      if(idx=="2")
      {
        if($scope.qeryfilters.status=="")
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''}];
        }
        else
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
        }
      }
      return qry;
    };

    $scope.init=function() {

      $scope.changeselectidx=function(idx)
      {
        $scope.qeryfilters.selectidx=idx;
        var qry=$scope.getRefreshFilter();
        $scope.getRepairList(qry);
      };


      $scope.$watch('qeryfilters.status', function (newval, oldval) {
        //console.log("newval:"+newval+";oldval:"+oldval);
        var qry = {'col': 'status', 'logic': '=', 'val': newval, 'andor': ''};
        if (newval == "") {
          qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
        }
        //查询数据
        if($scope.qeryfilters.selectidx=="2") {
          $scope.getRepairList(qry);
        }
      });


      $scope.loadMore = function () {
        console.log("--1111111111111111");

        var qry=$scope.loadMore_qry;
        $scope.user_repair.page.currpage=$scope.user_repair.page.currpage+1;
        $scope.user_repair.getRepairList(qry, function (rtn) {
          console.info($scope.user_repair.model_list);
          //初始化字
          service_dict.getDcit('repair_status', function (rtn) {

            $scope.$broadcast('scroll.infiniteScrollComplete');
            $scope.$apply();

          });
          //$scope.$apply();
        }, function (rtn) {
          $ionicPopup.alert({
            title: '提醒',
            template: '有误!' + rtn
          });
          return;
        });
      };

      $scope.changeselectidx("0");
    };

    //维修编辑或者申请
    //cate:
    $scope.eidt_repair=function(iid,cate)
    {
      if(iid=="" || iid=="0") {
        $scope.user_repair.iniModel();
        $scope.user_repair.model.creater = $scope.usercenter.userid;
        $scope.user_repair.model.report_person = $scope.usercenter.name;
        $scope.user_repair.model.phone = $scope.usercenter.phone;
        $scope.user_repair.model.roomid = $scope.usercenter.roomid;
        $scope.user_repair.model.roompath = $scope.usercenter.roompath;

        $scope.user_repair.model.imagelist_url=[{id: '0', url: 'img/photo_add.png'},
          {id: '1', url: 'img/photo_add.png'},
          {id: '2', url: 'img/photo_add.png'},
          {id: '3', url: 'img/photo_add.png'}];
      }
      else
      {
        $scope.user_repair.getRepair(iid,function(rtn){

          if(cate=="1")
          {
            $scope.user_repair.model.imagelist_url=
              _.reject($scope.user_repair.model.imagelist_url,function(itm){
              return itm.url=="img/photo_add.png";
            });
            console.log($scope.user_repair.model.imagelist_url);
          }

          $scope.$apply();
          console.info("get repari from server");
          console.info(JSON.stringify(rtn));
        },function(rnt){});
      }
    };



    //详情窗口------------------------------
    $scope.open_wnd_user_repair_detail = function (iid) {
      //此处初始化
      $scope.eidt_repair(iid,"1");
      $state.go("root.user_repair_detail");
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
      //$scope.master_query.roomid=roomid;
      //$scope.master_query.roompath=roompath;
      $scope.qeryfilters.roomid=roomid;
      $scope.qeryfilters.roompath=roompath;
      $scope.querybyroomid();

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function() {
      $scope.SelectRoomWnd.remove();
    });

    $scope.init();

  })


  .controller('user_repair_detail', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,service_roomselect, service_usercenter, service_user_repair,service_dict,NativePlugin,service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    //维修编辑相关-----------------------------------------------

    $scope.closeWithReturn=function()
    {
      $scope.close_wnd_user_repair_detail();
      $scope.getRepairList();
    };


    $scope.masterReturn=function(iid)
    {
      $scope.user_repair.masterReturn(iid,function(rtn){

        $ionicPopup.alert({
          title: '提醒',
          template: '撤回成功!'
        });

        $scope.$apply();
      },function(rtn){
        $ionicPopup.alert({
          title: '提醒',
          template: '撤回失败!'+rtn
        });
      });
    };

    //显示图片详情
    $scope.photoview = function (urls, idx) {
      NativePlugin.PhotoViews(urls, idx, ".repair_detail.pswp");
    };
  })

  ;
