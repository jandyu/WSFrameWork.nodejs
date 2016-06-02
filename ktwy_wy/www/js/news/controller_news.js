angular.module('ktwy.controllers')

  .controller('news_wy_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_wy) {
    $scope.usercenter = service_usercenter;
    $scope.news_wy = service_news_wy;

    //ionNavBackButton.showBackButton


    $scope.qeryfilters = {status: '1'};
    //修改过滤条件
    $scope.changequeryfilter = function (status) {
      $scope.qeryfilters.status = status;
    };

    //通用功能-------------------------------
    $scope.loadMore_qry = {};
    //refresh
    $scope.refresh = function (qry, clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.news_wy.getlist(qry, clearflag).then(function (rtn) {
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
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      //查询数据
      $scope.news_wy.getlist(qry, "1").then(function (rtn) {
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

      $scope.refresh();
    };

    //
    $scope.open_wnd_news_wy_edit = function (iid, ev) {
      if (iid == "0") {
        $scope.news_wy.ini_model($scope.usercenter);
        $state.go("root.news_wy_edit");
      }
      else {
        $scope.news_wy.getmodel(iid).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
          $scope.$apply();
          $state.go("root.news_wy_detail");
        }, function (rtn) {

        });
      }

      ev.stopPropagation();
    };

    //execute init function
    $scope.init();


    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.refresh();
      });

  })

  .controller('news_wy_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_wy) {
    $scope.usercenter = service_usercenter;
    $scope.news_wy = service_news_wy;

    //保存
    $scope.savenews_wy = function () {
      if ($scope.news_wy.model.roomid == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '请选择房号!'
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
      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_wy.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });


    };

    //获取照片
    $scope.getPicture = function (id) {

      //获取图片
      NativePlugin.GetPicture(function (imageData) {


        console.log(NativePlugin.PictureModel.image_url);

        //上传图片
        NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {

          console.info('--------uploadfile callback------------');
          console.info(rtn);

          //保存资源
          service_wy_resource.model.category = '新闻图片';
          service_wy_resource.model.url = rtn;

          $scope.news_wy.model.imagelist_url[id].url = wwwurl + rtn.substr(1);

          service_wy_resource.SaveResource(function (rtn) {
            $scope.news_wy.model.imagelist_url[id].rid = jsondal.AnaRtn(rtn);

            $scope.$apply();
          }, function (rtn) {
          });


        }, function (rtn) {
          console.log(JSON.stringify(rtn));
        }, {});

      }, function (message) {

      }, {});


    };


    $scope.photoview = function (urls, idx) {

      var arr_urls = _.clone(urls);
      arr_urls = _.reject(arr_urls, function (itm) {
        return itm.url == "img/photo_add.png";
      });

      console.info(arr_urls);

      NativePlugin.PhotoViews(arr_urls, idx, ".news_wy_edit.pswp");
    };

    //显示图片详情或者添加图片
    $scope.add_viewPhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      if (url == 'img/photo_add.png') {
        $scope.getPicture(id);
      }
      else {
        $scope.photoview($scope.news_wy.model.imagelist_url, idx);
      }

    };

    //长按替换图片
    $scope.replacePhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      $scope.getPicture(id);

    };


  })

  .controller('news_wy_detail', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_wy, service_news_reply_wy) {
    $scope.usercenter = service_usercenter;
    $scope.news_wy = service_news_wy;
    $scope.news_reply_wy = service_news_reply_wy;

    $scope.open_wnd_news_wy_edit = function (iid) {
      $scope.news_wy.getmodel(iid).then(function (rtn) {
        $scope.$apply();
      }, function (rtn) {

      });
      $state.go("root.news_wy_edit");
    };

    $scope.reply = function (iid) {
      $scope.news_wy.getmodel(iid).then(function (rtn) {
        $state.go("root.news_wy_detail_reply");
        $scope.$apply();
      }, function (rtn) {

      });

    };

    //删除
    $scope.delnews = function () {
      $scope.news_wy.model.status = '2';

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要删除吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });

      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_wy.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });

    };

    $scope.settop = function (topnumber) {
      $scope.news_wy.model.topnumber = topnumber;
      var stip = "置顶";
      if (topnumber == "0") {
        stip = "取消置顶";
      }
      else {
        stip = "置顶";
      }
      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要' + stip + '吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });

      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_wy.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });

    };


    //显示图片详情
    $scope.photoview = function (urls, idx) {
      NativePlugin.PhotoViews(urls, idx, ".news_wy_detail.pswp");
    };


    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.news_wy.getmodel($scope.news_wy.model.iid).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
          $scope.$apply();
        }, function (rtn) {

        });
      });

  })

  .controller('news_wy_detail_reply', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_wy, service_news_reply_wy) {
    $scope.usercenter = service_usercenter;
    $scope.news_wy = service_news_wy;
    $scope.news_reply_wy = service_news_reply_wy;


    //通用功能-------------------------------
    $scope.loadMore_qry = {};
    //refresh
    $scope.refresh = function (qry, clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'niid', 'logic': '=', 'val': $scope.news_wy.model.iid, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.news_reply_wy.getlist(qry, clearflag).then(function (rtn) {
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
          {'col': 'niid', 'logic': '=', 'val': $scope.news_wy.model.iid, 'andor': ''}];
      }
      //查询数据
      $scope.news_reply_wy.getlist(qry, "1").then(function (rtn) {
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      }, function (rtn) {

      });

    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.refresh();
      });

    //发评论
    $scope.sendReply = {message: ''};
    //发送消息
    $scope.sendMessage = function () {
      if ($scope.sendReply.message == "") return;
      $scope.news_reply_wy.ini_model($scope.usercenter);
      $scope.news_reply_wy.model.niid = $scope.news_wy.model.iid;
      $scope.news_reply_wy.model.rtext = $scope.sendReply.message;

      $scope.news_reply_wy.savemodel().then(function (rtn) {
        $scope.refresh();
      }, function (rtn) {

      });

    };

    //删除评论
    $scope.delreply = function (iid) {
      $scope.news_reply_wy.delmodel(iid).then(function (rtn) {
        $scope.refresh();
      }, function (rtn) {

      });
    }

  })


  .controller('news_yz_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_yz) {
    $scope.usercenter = service_usercenter;
    $scope.news_yz = service_news_yz;

    //ionNavBackButton.showBackButton


    $scope.qeryfilters = {status: '1'};
    //修改过滤条件
    $scope.changequeryfilter = function (status) {
      $scope.qeryfilters.status = status;
    };

    //通用功能-------------------------------
    $scope.loadMore_qry = {};
    //refresh
    $scope.refresh = function (qry, clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.news_yz.getlist(qry, clearflag).then(function (rtn) {
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
          {'col': 'status', 'logic': '=', 'val': $scope.qeryfilters.status, 'andor': ''}];
      }
      //查询数据
      $scope.news_yz.getlist(qry, "1").then(function (rtn) {
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

      $scope.refresh();
    };

    //
    $scope.open_wnd_news_yz_edit = function (iid, ev) {
      if (iid == "0") {
        $scope.news_yz.ini_model($scope.usercenter);
        $state.go("root.news_yz_edit");
      }
      else {
        $scope.news_yz.getmodel(iid).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
          $scope.$apply();
          $state.go("root.news_yz_detail");
        }, function (rtn) {

        });
      }

      ev.stopPropagation();
    };

    //execute init function
    $scope.init();


    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.refresh();
      });

  })

  .controller('news_yz_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_yz) {
    $scope.usercenter = service_usercenter;
    $scope.news_yz = service_news_yz;

    //保存
    $scope.savenews_yz = function () {
      if ($scope.news_yz.model.roomid == "") {
        $ionicPopup.alert({
          title: '提醒',
          okType: 'button-orange',
          template: '请选择房号!'
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
      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_yz.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });


    };

    //获取照片
    $scope.getPicture = function (id) {

      //获取图片
      NativePlugin.GetPicture(function (imageData) {


        console.log(NativePlugin.PictureModel.image_url);

        //上传图片
        NativePlugin.FileTrans(NativePlugin.PictureModel.image_url, function (rtn) {

          console.info('--------uploadfile callback------------');
          console.info(rtn);

          //保存资源
          service_wy_resource.model.category = '新闻图片';
          service_wy_resource.model.url = rtn;

          $scope.news_yz.model.imagelist_url[id].url = wwwurl + rtn.substr(1);

          service_wy_resource.SaveResource(function (rtn) {
            $scope.news_yz.model.imagelist_url[id].rid = jsondal.AnaRtn(rtn);

            $scope.$apply();
          }, function (rtn) {
          });


        }, function (rtn) {
          console.log(JSON.stringify(rtn));
        }, {});

      }, function (message) {

      }, {});


    };


    $scope.photoview = function (urls, idx) {

      var arr_urls = _.clone(urls);
      arr_urls = _.reject(arr_urls, function (itm) {
        return itm.url == "img/photo_add.png";
      });

      console.info(arr_urls);

      NativePlugin.PhotoViews(arr_urls, idx, ".news_yz_edit.pswp");
    };

    //显示图片详情或者添加图片
    $scope.add_viewPhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      if (url == 'img/photo_add.png') {
        $scope.getPicture(id);
      }
      else {
        $scope.photoview($scope.news_yz.model.imagelist_url, idx);
      }

    };

    //长按替换图片
    $scope.replacePhoto = function (url, id, idx) {
      console.info("---------------------");
      console.info("url:" + url + "id:" + id);
      $scope.getPicture(id);

    };


  })

  .controller('news_yz_detail', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_yz, service_news_reply_yz) {
    $scope.usercenter = service_usercenter;
    $scope.news_yz = service_news_yz;
    $scope.news_reply_yz = service_news_reply_yz;

    $scope.open_wnd_news_yz_edit = function (iid) {
      $scope.news_yz.getmodel(iid).then(function (rtn) {
        $scope.$apply();
      }, function (rtn) {

      });
      $state.go("root.news_yz_edit");
    };

    $scope.reply = function (iid) {
      $scope.news_yz.getmodel(iid).then(function (rtn) {
        $state.go("root.news_yz_detail_reply");
        $scope.$apply();
      }, function (rtn) {

      });

    };

    //删除
    $scope.delnews = function () {
      $scope.news_yz.model.status = '2';

      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要删除吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });

      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_yz.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });

    };

    $scope.settop = function (topnumber) {
      $scope.news_yz.model.topnumber = topnumber;
      var stip = "置顶";
      if (topnumber == "0") {
        stip = "取消置顶";
      }
      else {
        stip = "置顶";
      }
      var confirmPopup = $ionicPopup.confirm({
        title: '确认',
        template: '确定要' + stip + '吗?',
        cancelText: '取消',
        cancelType: 'button-blue',
        okText: '确定',
        okType: 'button-blue'
      });

      confirmPopup.then(function (res) {
        if (res) {


          $scope.news_yz.savemodel().then(function (rtn) {
            //保存完成
            $ionicPopup.alert({
              title: '提醒',
              okType: 'button-orange',
              template: '成功!'
            });

            $ionicHistory.goBack();

          }, function (rtn) {

          });


        }
      }, function (rtn) {

      });

    };

    //显示图片详情
    $scope.photoview = function (urls, idx) {
      NativePlugin.PhotoViews(urls, idx, ".news_yz_detail.pswp");
    };

    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.news_yz.getmodel($scope.news_yz.model.iid).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
          $scope.$apply();
        }, function (rtn) {

        });
      });

  })

  .controller('news_yz_detail_reply', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet, $ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_news_yz, service_news_reply_yz) {
    $scope.usercenter = service_usercenter;
    $scope.news_yz = service_news_yz;
    $scope.news_reply_yz = service_news_reply_yz;


    //通用功能-------------------------------
    $scope.loadMore_qry = {};
    //refresh
    $scope.refresh = function (qry, clearflag) {
      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'iid', 'logic': '>', 'val': '0', 'andor': 'and'},
          {'col': 'niid', 'logic': '=', 'val': $scope.news_yz.model.iid, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.news_reply_yz.getlist(qry, clearflag).then(function (rtn) {
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
          {'col': 'niid', 'logic': '=', 'val': $scope.news_yz.model.iid, 'andor': ''}];
      }
      //查询数据
      $scope.news_reply_yz.getlist(qry, "1").then(function (rtn) {
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      }, function (rtn) {

      });

    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //if (fromState.name == "root.employee_edit") {}
        $scope.refresh();
      });

    //发评论
    $scope.sendReply = {message: ''};
    //发送消息
    $scope.sendMessage = function () {
      if ($scope.sendReply.message == "") return;
      $scope.news_reply_yz.ini_model($scope.usercenter);
      $scope.news_reply_yz.model.niid = $scope.news_yz.model.iid;
      $scope.news_reply_yz.model.rtext = $scope.sendReply.message;

      $scope.news_reply_yz.savemodel().then(function (rtn) {
        $scope.refresh();
      }, function (rtn) {

      });

    };

    //删除评论
    $scope.delreply = function (iid) {
      $scope.news_reply_yz.delmodel(iid).then(function (rtn) {
        $scope.refresh();
      }, function (rtn) {

      });
    }

  })

;
