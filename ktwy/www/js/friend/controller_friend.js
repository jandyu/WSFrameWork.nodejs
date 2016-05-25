angular.module('ktwy.controllers')

  .controller('friend_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource,
                                       service_friend,service_employee) {
    $scope.usercenter = service_usercenter;
    $scope.friend = service_friend;
    $scope.employee = service_employee;

    //ionNavBackButton.showBackButton
    $scope.qeryfilters={selectidx:'0'};
    //修改过滤条件
    $scope.changeselectidx=function(idx)
    {
      $scope.qeryfilters.selectidx=idx;
    };

    //通用功能-------------------------------
    //加载更多的查询条件
    $scope.loadMore_qry={};
    $scope.loadMore_qry_emp={};
    //refresh
    $scope.refresh = function (idx)
    {
      var qry="",clearflag="0";
      if (idx == undefined || idx == "" || idx == null)
      {
        idx=$scope.qeryfilters.selectidx;
      }

      if(idx=="0") {

        if (qry == undefined || qry == "" || qry == null) {
          qry = [{'col': 'category', 'logic': '=', 'val': '0', 'andor': 'and'},
            {'col': 'mid', 'logic': '=', 'val': $scope.usercenter.userid, 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': '1', 'andor': ''}];
        }
        $scope.loadMore_qry = qry;
        //查询数据
        if (clearflag == undefined) {
          clearflag = '0';
        }
        $scope.friend.getlist(qry, clearflag).then(function (rtn) {
          //刷新完成
          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        }, function (rtn) {

        });

      }
      else
      {
        if (qry == undefined || qry == "" || qry == null) {
          qry = [{'col': 'category', 'logic': '=', 'val': '1', 'andor': 'and'},
            {'col': 'status', 'logic': '=', 'val': '1', 'andor': ''}];
        }
        $scope.loadMore_qry_emp = qry;
        //查询数据
        if (clearflag == undefined) {
          clearflag = '0';
        }
        $scope.employee.getlist(qry, clearflag).then(function (rtn) {
          //刷新完成
          $scope.$broadcast('scroll.refreshComplete');
          $scope.$apply();
        }, function (rtn) {

        });
      }


    };


    //loadmore
    $scope.loadMore = function () {

      if($scope.qeryfilters.selectidx=="0") {

        var qry = $scope.loadMore_qry;
        //查询数据
        $scope.friend.getlist(qry, "1").then(function (rtn) {
          //加载更多完成
          $scope.$broadcast('scroll.infiniteScrollComplete');
          $scope.$apply();
        }, function (rtn) {

        });
      }
      else
      {
        var qry = $scope.loadMore_qry_emp;
        //查询数据
        $scope.employee.getlist(qry, "1").then(function (rtn) {
          //加载更多完成
          $scope.$broadcast('scroll.infiniteScrollComplete');
          $scope.$apply();
        }, function (rtn) {

        });
      }
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



      $scope.refresh('0');
      $scope.refresh('1');
    };

    //添加新朋友
    $scope.newfriend=function()
    {
      $state.go("root.newfriend_list");
    };

    //修改
    $scope.open_wnd_friend_edit = function (iid) {
      if($scope.qeryfilters.selectidx=="0") {
        if (iid == "0") {
          $scope.friend.ini_model($scope.usercenter);
        }
        else {
          $scope.friend.getmodel(iid).then(function (rtn) {
            console.info("----------getmodel----------------");
            console.info(rtn);
          }, function (rtn) {

          });
        }
        $state.go("root.friend_edit");
      }
      else
      {
        if (iid == "0") {
          $scope.employee.ini_model($scope.usercenter);
        }
        else {
          $scope.employee.getmodel(iid).then(function (rtn) {
            console.info("----------getmodel----------------");
            console.info(rtn);
          }, function (rtn) {

          });
        }
        $state.go("root.employee_edit");
      }
    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //event.preventDefault();
        console.info(fromState);
        if (fromState.name == "root.friend_edit") {
          $scope.refresh('0');
        }
        if (fromState.name == "root.employee_edit") {
          $scope.refresh('1');
        }
      });

    //execute init function
    $scope.init();
  })

  .controller('friend_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_friend) {
    $scope.usercenter = service_usercenter;
    $scope.friend = service_friend;

  })

  .controller('employee_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_employee) {
    $scope.usercenter = service_usercenter;
    $scope.employee = service_employee;

  })


  .controller('newfriend_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource,
                                       service_newfriend) {
    $scope.usercenter = service_usercenter;
    $scope.newfriend = service_newfriend;
    $scope.newfriend_list={meaddlist:[],otherlist:[]};

    //通用功能-------------------------------
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //refresh
    $scope.refresh = function (qry,clearflag) {

      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'category', 'logic': '=', 'val': '0', 'andor': 'and'},
          {'col': 'mid', 'logic': '=', 'val': $scope.usercenter.userid, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.newfriend.getlist(qry, clearflag).then(function (rtn) {

        $scope.newfriend_list.meaddlist=[];
        $scope.newfriend_list.otherlist=[];
        $.each($scope.newfriend.model_list,function(k,v){
          if(v.ftype=='0')
          {
            $scope.newfriend_list.meaddlist.push(v);
          }
          else
          {
            $scope.newfriend_list.otherlist.push(v);
          }
        });

        //刷新完成
        $scope.$broadcast('scroll.refreshComplete');
        $scope.$apply();
      }, function (rtn) {
      });

    };


    //loadmore
    $scope.loadMore = function () {
        var qry = $scope.loadMore_qry;
        //查询数据
        $scope.newfriend.getlist(qry, "1").then(function (rtn) {

          $scope.newfriend_list.meaddlist=[];
          $scope.newfriend_list.otherlist=[];
          $.each($scope.newfriend.model_list,function(k,v){
            if(v.ftype=='0')
            {
              $scope.newfriend_list.meaddlist.push(v);
            }
            else
            {
              $scope.newfriend_list.otherlist.push(v);
            }
          });

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

    $scope.addFriend=function()
    {
      $state.go("root.master_list");
    };

    //修改
    $scope.open_wnd_newfriend_edit = function (iid,ftype) {
      if (iid == "0") {
        $scope.newfriend.ini_model($scope.usercenter);
      }
      else {
        var qry=[{'col': 'iid', 'logic': '=', 'val': iid, 'andor': 'and'},
          {'col': 'ftype', 'logic': '=', 'val': ftype, 'andor': ''}]
        $scope.newfriend.getmodel(qry).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
        }, function (rtn) {

        });
      }
      $state.go("root.newfriend_edit");
    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //event.preventDefault();
        console.info(fromState);
        if (fromState.name == "root.newfriend_edit") {
          $scope.refresh();
        }
      });

    //execute init function
    $scope.init();
  })



  .controller('newfriend_edit', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource, service_newfriend) {
    $scope.usercenter = service_usercenter;
    $scope.newfriend = service_newfriend;

  })




  .controller('master_list', function ($scope, $stateParams, $state, $log, $ionicPopup, $ionicModal, $ionicActionSheet,$ionicHistory, service_roomselect, service_usercenter, service_dict, NativePlugin, service_wy_resource,
                                       service_master) {
    $scope.usercenter = service_usercenter;
    $scope.master = service_master;

    $scope.qeryfilters={search_txt:'15606526620'};
    //通用功能-------------------------------
    //加载更多的查询条件
    $scope.loadMore_qry={};
    //refresh
    $scope.refresh = function (qry,clearflag) {

      if (qry == undefined || qry == "" || qry == null) {
        qry = [{'col': 'phone', 'logic': '=', 'val': $scope.qeryfilters.search_txt, 'andor': ''}];
      }
      $scope.loadMore_qry = qry;
      //查询数据
      if (clearflag == undefined) {
        clearflag = '0';
      }
      $scope.master.getlist(qry, clearflag).then(function (rtn) {
        //刷新完成
        $scope.$broadcast('scroll.refreshComplete');
        $scope.$apply();
      }, function (rtn) {
      });

    };


    //loadmore
    $scope.loadMore = function () {
      var qry = $scope.loadMore_qry;
      //查询数据
      $scope.master.getlist(qry, "1").then(function (rtn) {
        //加载更多完成
        $scope.$broadcast('scroll.infiniteScrollComplete');
        $scope.$apply();
      }, function (rtn) {
      });
    };

    $scope.dealmodel=function(md) {
      var qry = [{'col': 'category', 'logic': '=', 'val': '0', 'andor': 'and '},
        {'col': 'fid', 'logic': '=', 'val': md.fid, 'andor': 'and'},
        {'col': 'mid', 'logic': '=', 'val': $scope.usercenter.userid, 'andor': ''}];

      var ord = {col: 'iid', sort: 'asc'};
      jsondal.doPromise(jsondal.Query, 'v_m_app_wy_friend', qry, 1, 1, ord)
        .then(function (rtn) {

          if (rtn.d != undefined) {
            var rn=rtn.d[0];



          }
          //通过getmodel_after处理获取的数据

        }, function (rtn) {
          return rtn;
        });
    };

    //修改
    $scope.open_wnd_master_edit = function (iid) {
      if (iid == "0") {
        $scope.master.ini_model($scope.usercenter);
      }
      else {
        var qry=[{'col': 'iid', 'logic': '=', 'val': iid, 'andor': ''}]
        $scope.master.getmodel(qry).then(function (rtn) {
          console.info("----------getmodel----------------");
          console.info(rtn);
        }, function (rtn) {

        });
      }
      $state.go("root.master_edit");
    };

    //导航
    $scope.$on('$stateChangeSuccess',
      function (event, toState, toParams, fromState, fromParams) {
        //event.preventDefault();
        console.info(fromState);
        if (fromState.name == "root.master_edit") {
          $scope.refresh();
        }
      });
  })

;
