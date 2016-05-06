angular.module('ktwy.services')
  .factory('service_master_query', function () {

    var master_query = $.extend(IService, {
      roomid: '0',
      roompath: '',
      name: '',
      masters: [{name: '', phone: ''}],
      getMaster: function (qry) {
        var _this = this;
        //{'col':'roomid','logic':'=','val':'','andor':''}
        return jsondal.doPromise(jsondal.Query, "app_master", qry, 1, 99999, {
          'col': 'name',
          'sort': 'asc'
        }).then(function (rtn) {
          _this.masters = [];
          if (rtn.d != undefined) {
            $.each(rtn.d, function (k, v) {
              var itm = {
                name: v.name,
                phone: v.phone
              };
              _this.masters.push(itm);
            });
          }
          else {
            _this.masters[{name: '', phone: ''}];
          }
        }, function (rtn) {

        });
      }
    });
    return master_query;
  })
  .factory("service_dict", function () {
    var dict = {
      dict_list: [],
      getDcit: function (category, succ) {
        var me = this;
        var qry = {'col': 'category', 'logic': '=', 'val': category, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'asc'};
        jsondal.Query("v_dict", qry, 1, 9999, ord, function (rtn) {
            console.info(rtn);
            me.dict_list = [];
            if (rtn.d != undefined) {
              $.each(rtn.d, function (k, v) {
                var itm = {
                  category: v.category,
                  id: v.id,
                  txt: v.txt,
                  pid: v.pid,
                  order_id: v.order_id,
                  iid: v.iid
                };
                me.dict_list.push(itm);
              });
            }
            if (succ != undefined && succ != null) {
              succ(rtn);
            }
          },
          function (rtn) {
          });
      },
      getTxtById:function(id)
      {
        var me=this;
        var rtn=id;
        $.each(me.dict_list,function(k,v){
          if(v.id==id)
          {
            rtn= v.txt;
          }
        });
        return rtn;
      }
    };
    return dict;
  })

  .factory('service_wy_resource',function(){
    var wy_resource={
      model:{iid:'0',category:'',url:'',memo:'',objid:''},
      SaveResource:function(succ,fail)
      {
        var me = this;
        var dat = {
          category: me.model.category,
          url: me.model.url,
          memo: me.model.memo,
          objid:me.objid,
          iid:me.iid
        };
        jsondal.Insert("app_wy_resource", dat, succ, fail);
      }
    };
    return wy_resource;
  })
  /*
  .factory('templateInjector', function (Session) {
    var sessionInjector = {
      request: function (config) {
        //if (!SessionService.isAnonymus) {
        console.info(config);
        if(/^templates\//.test(config.url)) {
          config.url = config.url + "?" + new Date().getTime();
        }
        config.requestTimestamp = new Date().getTime();
       // config.headers['authorization'] = Session.token;
        //}
        return config;
      },
      response: function(response) {
        response.config.responseTimestamp = new Date().getTime();
        if(response.status==401){
          //todo unauthorized
          console.info("Unauthorized");
          console.info(response.data);
        }
        return response;
      }
    };
    return sessionInjector;
  })
  */
;
