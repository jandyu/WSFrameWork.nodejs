angular.module('ktwy.controllers')

  .controller('user_repair', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_repair, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    $scope.dict = service_dict;

    //$scope.dict.getDcit('repair_status',function(rtn){ });
    $scope.getRepairList = function () {
      $scope.user_repair.getRepairList($scope.usercenter.userid, function (rtn) {
        console.info($scope.user_repair.model_list);
        //初始化字
        service_dict.getDcit('repair_status', function (rtn) {

          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        });
        //$scope.$apply();
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          template: '提交有误!' + rtn
        });
        return;
      });
    };

    $scope.loadMore = function () {
      $scope.$broadcast('scroll.infiniteScrollComplete');
    };

    $scope.getRepairList();


    //维修编辑或者申请
    //cate:
    $scope.eidt_repair = function (iid, cate) {
      if (iid == "" || iid == "0") {
        $scope.user_repair.iniModel();
        $scope.user_repair.model.creater = $scope.usercenter.userid;
        $scope.user_repair.model.report_person = $scope.usercenter.name;
        $scope.user_repair.model.phone = $scope.usercenter.phone;
        $scope.user_repair.model.roomid = $scope.usercenter.roomid;
        $scope.user_repair.model.roompath = $scope.usercenter.roompath;

        $scope.user_repair.model.imagelist_url = [{id: '0', url: 'img/photo_add.png'},
          {id: '1', url: 'img/photo_add.png'},
          {id: '2', url: 'img/photo_add.png'},
          {id: '3', url: 'img/photo_add.png'}];
      }
      else {
        $scope.user_repair.getRepair(iid, function (rtn) {
          if (cate == "1") {
            $scope.user_repair.model.imagelist_url =
              _.reject($scope.user_repair.model.imagelist_url, function (itm) {
                return itm.url == "img/photo_add.png";
              });
            console.log($scope.user_repair.model.imagelist_url);
          }

          $scope.$apply();
          console.info("get repari from server");
          console.info(JSON.stringify(rtn));
        }, function (rnt) {
        });
      }
    };


    //编辑窗口-----------------------------
    $ionicModal.fromTemplateUrl('templates/usercenter/user_repair_edit.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_repair_edit = modal;
    });

    $scope.open_wnd_user_repair_edit = function (iid) {
      //此处初始化
      if (iid == "0") {
        $scope.eidt_repair(iid);

        $scope.wnd_user_repair_edit.show();
      }
      else {
        if ($scope.user_repair.model.status == "" || $scope.user_repair.model.status == "0") {

          $scope.eidt_repair(iid);

          $scope.wnd_user_repair_edit.show();
        }
        else {
          $ionicPopup.alert({
            title: '提醒',
            template: '当前状态不能修改!'
          });
          return;
        }
      }
    };

    $scope.close_wnd_user_repair_edit = function () {

      $scope.user_repair.model.imagelist_url =
        _.reject($scope.user_repair.model.imagelist_url, function (itm) {
          return itm.url == "img/photo_add.png";
        });

      $scope.wnd_user_repair_edit.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_repair_edit.remove();
    });


    //详情窗口------------------------------
    $ionicModal.fromTemplateUrl('templates/usercenter/user_repair_detail.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_repair_detail = modal;
    });

    $scope.open_wnd_user_repair_detail = function (iid) {
      //此处初始化

      $scope.eidt_repair(iid, "1");

      $scope.wnd_user_repair_detail.show();
    };

    $scope.close_wnd_user_repair_detail = function () {

      $scope.wnd_user_repair_detail.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_repair_detail.remove();
    });


    //评价------------------------------
    $ionicModal.fromTemplateUrl('templates/usercenter/user_repair_visit.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {
      $scope.wnd_user_repair_visit = modal;
    });

    $scope.open_wnd_user_repair_visit = function (iid) {
      //此处初始化
      if ($scope.user_repair.model.status == "7" || $scope.user_repair.model.status == "6") {
        $scope.eidt_repair(iid, "1");
        $scope.wnd_user_repair_visit.show();
      }
      else {
        $ionicPopup.alert({
          title: '提醒',
          template: '当前状态不能评价!'
        });
      }

    };

    $scope.close_wnd_user_repair_visit = function () {
      $scope.$apply();
      $scope.wnd_user_repair_visit.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.wnd_user_repair_visit.remove();
    });


  })

  .controller('user_repair_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_repair, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    $scope.dict = service_dict;

    //选择房号--------------------------------------------------
    var roomselect = service_roomselect;
    $scope.roomselect = roomselect;

    $scope.return_text = "";

    $ionicModal.fromTemplateUrl('templates/usercenter/selectroom.html', {
      scope: $scope,
      animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.modal = modal;
    });

    $scope.getChildUnit = function (pid, p_title) {
      $scope.roomselect.getUnits(pid).then(function (rtn) {

          if (rtn.d != undefined) {
            $scope.roomselect.units = [];
            $.each(rtn.d, function (k, v) {
              var itm = {
                pid: v.pid,
                p_title: v.p_title,
                uid: v.uid,
                unit_title: v.unit_title,
                child: v.child,//子个数
                p_pid: v.parentid
              };
              $scope.roomselect.units.push(itm);
            });
          } else {
            $scope.user_repair.roomid = pid;
            $scope.user_repair.roompath = p_title;
            $scope.closeModal();
          }
          var s_txt = $scope.roomselect.getFirst().p_title;
          if (s_txt == "") {
            s_txt = "选择房号";
          }
          $scope.return_text = s_txt;
          $scope.$apply();
        },
        function (rtn) {
        });
    }

    //返回
    $scope.getParentUnit = function () {
      var first = $scope.roomselect.getFirst();
      if (first.p_title == "") {
        $scope.closeModal();
      }
      else {
        $scope.getChildUnit(first.p_pid);
      }
    };


    $scope.openModal = function () {
      $scope.getChildUnit(0);
      $scope.modal.show();
    };

    $scope.closeModal = function () {
      $scope.modal.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.modal.remove();
    });

    //数据保存
    $scope.saveRepair = function () {
      $scope.user_repair.saveRepair(function (rtn) {
        console.log($scope.user_repair.model.iid);
        console.log(rtn);
        $ionicPopup.alert({
          title: '提醒',
          template: '提交成功!'
        });
        $scope.closeWithReturn();
        return;
      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          template: '提交有误!' + rtn
        });
        return;
      });
    };

    //获取照片
    $scope.getPicture = function (id) {
      if ($scope.user_repair.model.status == '' || $scope.user_repair.model.status == '0') {

        //获取图片
        NativePlugin.GetPicture(function (imageData) {

          $scope.user_repair.model.imagelist_url[id].url = NativePlugin.PictureModel.image_url;

          console.log(NativePlugin.PictureModel.image_url);

          //上传图片
          NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {

            console.info('--------uploadfile callback------------');
            console.info(rtn);

            //保存资源
            service_wy_resource.model.category = '维修照片';
            service_wy_resource.model.url = rtn;

            service_wy_resource.SaveResource(function (rtn) {
              $scope.user_repair.model.imagelist_url[id].rid = jsondal.AnaRtn(rtn);

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


    $scope.photoview=function (url)
    {
      NativePlugin.PhotoView(url,"");
    };

    //显示图片详情或者添加图片
    $scope.add_viewPhoto=function(url,id)
    {
      console.info("---------------------");
      console.info("url:"+url+"id:"+id);
      if(url=='img/photo_add.png')
      {
        $scope.getPicture(id);
      }
      else
      {
        $scope.photoview(url);
      }

    };
    //维修编辑相关-----------------------------------------------

    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_repair_edit();
      $scope.getRepairList();
    };


  })

  .controller('user_repair_detail', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_repair, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    //维修编辑相关-----------------------------------------------
    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_repair_detail();
      $scope.getRepairList();
    };


    $scope.masterReturn = function (iid) {

      if ($scope.user_repair.model.status == "0") {
        $scope.user_repair.masterReturn(iid, function (rtn) {

          $ionicPopup.alert({
            title: '提醒',
            template: '撤回成功!'
          });
          $scope.$apply();
        }, function (rtn) {
          $ionicPopup.alert({
            title: '提醒',
            template: '撤回失败!' + rtn
          });
        });
      }
      else {
        $ionicPopup.alert({
          title: '提醒',
          template: '当前状态不能撤回!'
        });
      }
    };

    //显示图片详情
    $scope.photoview=function (url)
    {
      NativePlugin.PhotoView(url,"");
    };

  })

  .controller('user_repair_visit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, service_roomselect, service_usercenter, service_user_repair, service_dict, NativePlugin, service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    $scope.visit = {myd: "0", detail: ""};

    $scope.saveVisit = function (v) {
      $scope.user_repair.saveVisit(v, function (rtn) {

        $ionicPopup.alert({
          title: '提醒',
          template: '评价成功!'
        });
        $scope.visit_myd = "0";
        $scope.visit_detail = "";

        $scope.closeWithReturn();

      }, function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          template: '评价失败!' + rtn
        });
      });
    };


    //维修编辑相关-----------------------------------------------

    $scope.closeWithReturn = function () {
      $scope.close_wnd_user_repair_visit();
    };

  })


;
