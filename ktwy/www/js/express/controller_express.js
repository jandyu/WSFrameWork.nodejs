angular.module('ktwy.controllers')

  .controller('express_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_express) {
    $scope.usercenter = service_usercenter;
    $scope.express = service_express;

    //ionNavBackButton.showBackButton


    $scope.qeryfilters={status:'0',roomid:'0'};
    //修改过滤条件
    $scope.changequeryfilter=function(status)
    {
      $scope.qeryfilters.status=status;
    };

    //通用功能-------------------------------
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
      $scope.express.getlist(qry,clearflag).then(function (rtn) {
        //刷新完成
        $scope.$broadcast('scroll.refreshComplete');
        $scope.$apply();
      }, function (rtn) {

      });

    };


    //loadmore
    $scope.loadMore = function (qry) {
      if (qry == undefined || qry == "" || qry == null) {
        //qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'roomid', 'logic': '=', 'val': $scope.usercenter.roomid, 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      //查询数据
      $scope.express.getlist(qry,"1").then(function (rtn) {
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




    //
    $scope.open_wnd_express_edit = function (iid) {
      if (iid == "0") {
        $scope.express.ini_model($scope.usercenter);
      }
      else
      {
        $scope.express.getmodel(iid).then(function(rtn){
          console.info("----------getmodel----------------");
          console.info(rtn);
          $scope.express.model.md_url = wwwurl + rtn.md_url.substr(1);
          $scope.express.model.bg_url = wwwurl + rtn.bg_url.substr(1);
        },function(rtn){

        });
      }
      $state.go("root.express_edit");
    };

    //向右滑动
    $scope.onSwipeRight=function()
    {
      console.info("-------onSwipeRight-------");
      $ionicHistory.goBack();
    };



    //execute init function
    $scope.init();
  })

  .controller('express_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_express) {
    $scope.usercenter = service_usercenter;
    $scope.express = service_express;

    //保存
    $scope.saveexpress=function()
    {
      if($scope.express.model.roomid=="" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '请选择房号!'
        });
        return;
      }


      if($scope.express.model.status!="0" )
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


          $scope.express.savemodel().then(function (rtn) {
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

    //签收
    $scope.receiveexpress=function()
    {

      if($scope.express.model.status!="0" )
      {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '已签收,不能再次签收!'
        });
        return;
      }

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要签收吗?',
        cancelText: '取消',
        cancelType: 'button-orange',
        okText: '确定',
        okType: 'button-orange'
      });
      confirmPopup.then(function(res) {
        if(res) {

          //savemodel
          $scope.express.model.receiver=$scope.usercenter.name;
          $scope.express.model.receive_dt=util.today('-');
          $scope.express.model.status='1';
          //savemodel
          $scope.express.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType:'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();
          }, function (rtn) {

          });



        }
      },function(rtn){});



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

      if($scope.express.model.status=='1')return;

      $scope.SelectRoomWnd.show();
    };

    $scope.closeSelectRoomWnd = function (roomid, roompath) {

      $scope.express.model.roomid = roomid;
      $scope.express.model.roompath = roompath;

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });


    //获取照片
    $scope.getPicture = function (ptype) {
      //获取图片
      NativePlugin.GetPicture(function (imageData) {

        //上传图片
        NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {
          //保存资源
          service_wy_resource.model.category = '快递照片';
          service_wy_resource.model.url = rtn;

          if(ptype=="md") {
            $scope.express.model.md_url = wwwurl + rtn.substr(1);
          }
          else
          {
            $scope.express.model.bg_url = wwwurl + rtn.substr(1);
          }
          service_wy_resource.SaveResource(function (rtn) {
            if(ptype=="md") {
              $scope.express.model.md = jsondal.AnaRtn(rtn);
            }
            else
            {
              $scope.express.model.bg = jsondal.AnaRtn(rtn);
            }
            $scope.$apply();
          }, function (rtn) {
          });


        }, function (rtn) {
          console.log(JSON.stringify(rtn));
        }, {});

      }, function (message) {

      }, {});


    };

    //照片详情
    $scope.photoview = function (urls, idx) {

      var arr_urls = _.clone(urls);
      arr_urls = _.reject(arr_urls, function (itm) {
        return itm.url == "img/photo_camar.png";
      });

      console.info(arr_urls);

      NativePlugin.PhotoViews(arr_urls, idx, ".express_edit.pswp");
    };

    //显示图片详情或者添加图片
    $scope.add_viewPhoto = function (ptype) {
      var url="";
      if(ptype=="md")
      {
        url=$scope.express.model.md_url;
      }
      else
      {
        url=$scope.express.model.bg_url;
      }

      if (url == 'img/photo_camar.png') {
        $scope.getPicture(ptype);
      }
      else {
        var urls=[];
        urls.push({'url':url});
        $scope.photoview(urls, 0);
      }

    };

    //长按替换图片
    $scope.replacePhoto = function (ptype) {
      $scope.getPicture(ptype);
    };

  })
;
