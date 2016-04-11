angular.module('ktwy.controllers')

  .controller('home', function ($scope, $stateParams, $state, $log, $ionicModal, service_usercenter, service_usercenter_login) {
    $scope.usercenter = service_usercenter;
    $scope.usercenter_login = service_usercenter_login;
    //登录窗口

    //登陆后跳转到指定页面,未登陆则跳转到登陆页面,登陆完成后再跳转到要跳转到页面
    $scope.goAfterLogin = function (url) {
      //$state.go(url);

      if ($scope.usercenter.checkLogin() == true) {
        $state.go(url);
      }
      else {
        $scope.usercenter_login.willGoUrl = url;
        $scope.openLoginWnd();
      }
    };


    //登陆窗口
    $ionicModal.fromTemplateUrl('templates/usercenter/userlogin.html', {
      scope: $scope
      //animation: 'slide-in-up'
    }).then(function (modal) {

      $scope.LoginWnd = modal;
    });


    $scope.openLoginWnd = function () {
      //此处初始化
      $scope.LoginWnd.show();
    };

    $scope.closeLoginWnd = function () {
      $scope.LoginWnd.hide();
    };
    $scope.$on('$destroy', function () {
      $scope.LoginWnd.remove();
    });
  })

  .controller('user_repair', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,service_roomselect, service_usercenter, service_user_repair,service_dict,NativePlugin,service_wy_resource) {
    $scope.usercenter = service_usercenter;
    $scope.user_repair = service_user_repair;


    $scope.dict=service_dict;

    //$scope.dict.getDcit('repair_status',function(rtn){ });
    $scope.getRepairList=function()
    {
      $scope.user_repair.getRepairList($scope.usercenter.userid,function(rtn){
        console.info($scope.user_repair.model_list);
        //初始化字
        service_dict.getDcit('repair_status',function(rtn){

          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        });
        //$scope.$apply();
      },function(rtn){
        $ionicPopup.alert({
          title: '提醒',
          template: '提交有误!' + rtn
        });
        return;
      });
    };

    $scope.loadMore=function()
    {
      $scope.$broadcast('scroll.infiniteScrollComplete');
    };


    //第一次打开之后执行
    $scope.getRepairList();

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

    //选择房号--------------------------------------------------

    //维修编辑相关-----------------------------------------------
    //维修申请
    $scope.repail_apply=function()
    {
      $scope.user_repair.show_list=!$scope.user_repair.show_list;
      if($scope.user_repair.show_list==true)
      {
        $scope.getRepairList();
      }
      else
      {
        $scope.eidt_repair('0');
      }
      //$scope.$apply();
    };

    //维修编辑或者申请
    $scope.eidt_repair=function(iid)
    {
      $scope.user_repair.show_list=false;
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
          $scope.$apply();
          console.info("get repari from server");
          console.info(JSON.stringify(rtn));
        },function(rnt){});
      }
    };

    //数据保存
    $scope.saveRepair = function () {
      $scope.user_repair.saveRepair(function (rtn) {
        $ionicPopup.alert({
          title: '提醒',
          template: '提交成功!'
        });
        $scope.repail_apply();
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
    $scope.getPicture=function(id)
    {
      //获取图片
      NativePlugin.GetPicture(function(imageData){

        $scope.user_repair.model.imagelist_url[id].url=NativePlugin.PictureModel.image_url;

        console.log(NativePlugin.PictureModel.image_url);

        //上传图片
        NativePlugin.FileTrans(NativePlugin.PictureModel.image_url,function(rtn){

          //保存资源
          service_wy_resource.model.category='维修照片';
          service_wy_resource.model.url=rtn;

          service_wy_resource.SaveResource(function(rtn){
            $scope.user_repair.model.imagelist_url[id].rid=jsondal.AnaRtn(rtn);


            $scope.$apply();
          },function(rtn){});


        },function(rtn){
          console.log(JSON.stringify(rtn));
        },{});

      },function(message){

      },{});
    };
    //维修编辑相关-----------------------------------------------

  })
;
