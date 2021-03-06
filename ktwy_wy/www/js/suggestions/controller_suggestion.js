angular.module('ktwy.controllers')

  .controller('user_suggestion', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_suggestion, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_suggestion = service_user_suggestion;


    $scope.dict = service_dict;
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //$scope.dict.getDcit('suggestion_status',function(rtn){ });
    $scope.getsuggestionList = function (qry) {


      if(qry==undefined || qry=="" || qry==null) {
        //qry = {'col': 'iid', 'logic': '>', 'val': '0', 'andor': ''};
        qry=$scope.getRefreshFilter();
      }
      $scope.loadMore_qry=qry;
      //清空数据
      $scope.user_suggestion.model_list=[];
      $scope.user_suggestion.page.currpage=0;

      $scope.user_suggestion.getsuggestionList(qry, function (rtn) {
        console.info($scope.user_suggestion.model_list);
        //初始化字
        service_dict.getDcit('activity_status', function (rtn) {

          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        });
        //$scope.$apply();
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '有误!' + rtn
        });
        return;
      });
    };

    $scope.loadMore = function () {

      //var qry=$scope.getRefreshFilter();
      var qry=$scope.loadMore_qry;
      $scope.user_suggestion.page.currpage=$scope.user_suggestion.page.currpage+1;
      $scope.user_suggestion.getsuggestionList(qry, function (rtn) {
        console.info($scope.user_suggestion.model_list);
        //初始化字
        service_dict.getDcit('activity_status', function (rtn) {

          $scope.$broadcast('scroll.infiniteScrollComplete');
          $scope.$apply();
        });
        //$scope.$apply();
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '有误!' + rtn
        });
        return;
      });


    };


    $scope.qeryfilters={roomid:'0',roompath:'',category:'0',status:''};

    $scope.ini=function() {

      $scope.querybyroomid=function() {
        var qry = {};
        if($scope.qeryfilters.status=="")
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'category', 'logic': '=', 'val': $scope.qeryfilters.category, 'andor': 'and'}
          ];
        }
        else
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'category', 'logic': '=', 'val': $scope.qeryfilters.category, 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': 'and'}
          ];
        }

        qry.push({'col': 'roomid', 'logic': '=', 'val':$scope.qeryfilters.roomid, 'andor': ''});

        $scope.getsuggestionList(qry);
      };

      $scope.changeselectidx = function (idx) {
        $scope.qeryfilters.category = idx;
        $scope.qeryfilters.status="";
        var qry = $scope.getRefreshFilter();
        $scope.getsuggestionList(qry);
      };

      $scope.getRefreshFilter = function () {
        var qry = {};
        if($scope.qeryfilters.status=="")
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'category', 'logic': '=', 'val': $scope.qeryfilters.category, 'andor': ''}
          ];
        }
        else
        {
          qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
            {'col': 'category', 'logic': '=', 'val': $scope.qeryfilters.category, 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}
          ];
        }

        return qry;
      };

      $scope.$watch('qeryfilters.status', function (newval, oldval) {
        //查询数据
        var qry=$scope.getRefreshFilter();
        $scope.getsuggestionList(qry);
      });
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

    $scope.ini();




    //维修编辑或者申请
    //cate:
    $scope.eidt_suggestion = function (iid, cate) {
      if (iid == "" || iid == "0") {
        $scope.user_suggestion.iniModel();
        $scope.user_suggestion.model.creater = $scope.usercenter.userid;
        $scope.user_suggestion.model.report_person = $scope.usercenter.name;
        $scope.user_suggestion.model.phone = $scope.usercenter.phone;
        $scope.user_suggestion.model.roomid = $scope.usercenter.roomid;
        $scope.user_suggestion.model.roompath = $scope.usercenter.roompath;

        $scope.user_suggestion.model.imagelist_url = [{id: '0', url: 'img/photo_add.png'},
          {id: '1', url: 'img/photo_add.png'},
          {id: '2', url: 'img/photo_add.png'},
          {id: '3', url: 'img/photo_add.png'}];
      }
      else {
        $scope.user_suggestion.getsuggestion(iid, function (rtn) {
          if (cate == "1") {
            $scope.user_suggestion.model.imagelist_url =
              _.reject($scope.user_suggestion.model.imagelist_url, function (itm) {
                return itm.url == "img/photo_add.png";
              });
            console.log($scope.user_suggestion.model.imagelist_url);
          }

          $scope.$apply();
          console.info("get repari from server");
          console.info(JSON.stringify(rtn));
        }, function (rnt) {
        });
      }
    };


    //详情窗口------------------------------


    $scope.open_wnd_user_suggestion_detail = function (iid) {
      //此处初始化

      $scope.eidt_suggestion(iid, "1");

      $state.go("root.user_suggestion_detail");
    };



    //评价------------------------------
    $ionicModal.fromTemplateUrl('templates/suggestions/user_suggestion_visit.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_suggestion_visit = modal;
    });

    $scope.open_wnd_user_suggestion_visit = function (iid) {
      //此处初始化
      if ($scope.user_suggestion.model.status == "1") {
        $scope.eidt_suggestion(iid, "1");
        $scope.wnd_user_suggestion_visit.show();
      }
      else {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '当前状态不能评价!'
        });
      }

    };

    $scope.close_wnd_user_suggestion_visit = function () {
      $scope.$apply();
      $scope.wnd_user_suggestion_visit.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_suggestion_visit.remove();
    });
  })

  .controller('user_suggestion_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_suggestion, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_suggestion = service_user_suggestion;


    $scope.dict = service_dict;

    //选择房号--------------------------------------------------
    var roomselect = service_roomselect;
    $scope.roomselect = roomselect;

    $scope.return_text = "";

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

      console.info("----closeSelectRoomWnd---roomid:"+roomid+";roompath:"+roompath+"--------");

      if(roomid!='') {
        $scope.user_suggestion.model.roomid = roomid;
        $scope.user_suggestion.model.roompath = roompath;
      }

      $scope.SelectRoomWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.SelectRoomWnd.remove();
    });



    //数据保存
    $scope.savesuggestion = function () {
      $scope.user_suggestion.savesuggestion(function (rtn) {
        console.log($scope.user_suggestion.model.iid);
        console.log(rtn);
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '提交成功!'
        });
        $scope.closeWithReturn();
        return;
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '提交有误!' + rtn
        });
        return;
      });
    };

    //获取照片
    $scope.getPicture = function (id) {
      if ($scope.user_suggestion.model.status == '' || $scope.user_suggestion.model.status == '0') {

        //获取图片
        NativePlugin.GetPicture(function (imageData) {
          /*
           $scope.user_suggestion.model.imagelist_url[id].url = NativePlugin.PictureModel.image_url;
           $scope.$apply();
           */
          console.log(NativePlugin.PictureModel.image_url);

          //上传图片
          NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {

            console.info('--------uploadfile callback------------');
            console.info(rtn);

            //保存资源
            service_wy_resource.model.category = '维修照片';
            service_wy_resource.model.url = rtn;

            $scope.user_suggestion.model.imagelist_url[id].url = wwwurl + rtn.substr(1);

            service_wy_resource.SaveResource(function (rtn) {
              $scope.user_suggestion.model.imagelist_url[id].rid = jsondal.AnaRtn(rtn);

              $scope.$apply();
            }, function (rtn) {
            });


          }, function (rtn) {
            console.log(JSON.stringify(rtn));
          }, {});

        }, function (message) {

        }, {});

      }
    };


    $scope.photoview = function (urls, idx) {

      var arr_urls = _.clone(urls);
      arr_urls = _.reject(arr_urls, function (itm) {
        return itm.url == "img/photo_add.png";
      });

      console.info(arr_urls);

      NativePlugin.PhotoViews(arr_urls, idx, ".suggestion_edit.pswp");
    };

    //显示图片详情或者添加图片
    $scope.add_viewPhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      if (url == 'img/photo_add.png') {
        $scope.getPicture(id);
      }
      else {
        $scope.photoview($scope.user_suggestion.model.imagelist_url, idx);
      }

    };

    //长按替换图片
    $scope.replacePhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      /*
       if (url == 'img/photo_add.png') {
       $scope.getPicture(id);
       }
       else {
       $scope.photoview($scope.user_suggestion.model.imagelist_url,idx);
       }*/
      $scope.getPicture(id);

    };


    //维修编辑相关-----------------------------------------------

    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_suggestion_edit();
      $scope.getsuggestionList();
    };

    $scope.changeCategory = function (category) {
      $scope.user_suggestion.model.category = category;
    };


  })

  .controller('user_suggestion_detail', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_suggestion, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_suggestion = service_user_suggestion;


    //维修编辑相关-----------------------------------------------
    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_suggestion_detail();
      $scope.getsuggestionList();
    };


    $scope.masterReturn = function (iid) {

      if ($scope.user_suggestion.model.status == "0") {

        var confirmPopup = $ionicPopup.confirm({
          title: '确认',
          template: '确定要撤回吗?',
          cancelText: '取消',
          cancelType: 'button-orange',
          okText: '确定',
          okType: 'button-orange'
        });
        confirmPopup.then(function(res) {
          if(res) {

            $scope.user_suggestion.masterReturn(iid, function (rtn) {

              $ionicPopup.alert({
                title: '提醒',
                okType:'button-orange',
                template: '撤回成功!'
              });
              $scope.$apply();
            }, function (rtn) {
              $ionicPopup.alert({
                title: '提醒',
                okType:'button-orange',
                template: '撤回失败!' + rtn
              });
            });


          } else {
            console.log('You are not sure');
          }
        });


      }
      else {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '当前状态不能撤回!'
        });
      }
    };

    //显示图片详情
    $scope.photoview = function (urls, idx) {
      NativePlugin.PhotoViews(urls, idx, ".suggestion_detail.pswp");
    };

  })

  .controller('user_suggestion_visit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_suggestion, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_suggestion = service_user_suggestion;


    $scope.visit = {myd: "0", detail: ""};

    $scope.saveVisit = function (v) {
      $scope.user_suggestion.saveVisit(v, function (rtn) {

        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '评价成功!'
        });
        $scope.visit_myd = "0";
        $scope.visit_detail = "";

        $scope.closeWithReturn();

      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          okType:'button-orange',
          template: '评价失败!' + rtn
        });
      });
    };


    $scope.changeMyd=function(v){
      $scope.visit.myd=v;
    }

    //维修编辑相关-----------------------------------------------

    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_suggestion_visit();
    };

  })

;
