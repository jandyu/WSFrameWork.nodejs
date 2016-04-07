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
  .factory('service_user_repair', function () {
    var repair = {
      model: {
        creater: '',//用户id
        report_person: '',//姓名
        phone: '',//联系电话
        roomid: '',//房号id
        roompath: '',//房号路径
        category: '1',//维修类型
        imagelist_img: [{id: '', url: ''}],//图片 ,分割
        reason: '',//原因
        source: '1',//移动端
        status: '0',
        report_dt: util.today('-'),
        create_dt: util.today('-'),
        iid:'0'
      },
      //iid:'',creater:'',report_person:'',phone:'',roomid:'',category:'',imagelist_img:'',reason:'',source:'',status:'',report_dt:'',create_dt:''
      model_list: [],
      show_list: true,
      currpage: 0,
      getRepairList: function (userid, succ, fail) {
        var me = this;
        var qry = {'col': 'creater', 'logic': '=', 'val': userid, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'desc'};
        jsondal.Query("app_wy_repair", qry, me.currpage, 10, ord, function (rtn) {
          console.info(rtn);

          if (rtn.d != undefined) {
            me.model_list=[];
            $.each(rtn.d, function (k, v) {
              var itm = {
                creater: v.creater,
                report_person: v.report_person,
                phone: v.phone,
                roomid: v.roomid,
                category: v.category,
                imagelist_img: v.imagelist_img,
                reason: v.reason,
                source: v.source,
                status: v.status,
                report_dt: v.report_dt,
                create_dt: v.create_dt,
                iid: v.iid
              };
              me.model_list.push(itm);
            });

            me.currpage = rtn.p.currpage;
          }

          if (me.model_list.length == 0) {
            me.show_list = false;
          }
          else {
            me.show_list = true;
          }

          if (succ != undefined && succ != null) {

             if(me.model_list.length==0)
             {
             me.show_list=false;
             }
             else
             {
             me.show_list=true;
             }
            succ(rtn);
          }
        }, fail);
      },
      getRepair:function(iid,succ,fail)
      {
        var me = this;
        var qry = {'col': 'iid', 'logic': '=', 'val': iid, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'desc'};
        jsondal.Query("app_wy_repair", qry, me.currpage, 1, ord, function (rtn) {
          console.info(rtn);
          if (rtn.d != undefined) {
            if (succ != undefined && succ != null) {
              me.model.creater=rtn.d[0].creater;
              me.model.report_person=rtn.d[0].report_person;
              me.model.phone=rtn.d[0].phone;
              me.model.roomid=rtn.d[0].roomid;
              //me.model.roompath=rtn.d[0].roompath;
              me.model.category=rtn.d[0].category;
              me.model.imagelist_img=rtn.d[0].imagelist_img;
              me.model.reason=rtn.d[0].reason;
              me.model.source=rtn.d[0].source;
              me.model.status=rtn.d[0].status;
              me.model.report_dt=rtn.d[0].report_dt;
              me.model.create_dt=rtn.d[0].create_dt;
              me.model.iid=rtn.d[0].iid;

              //取出房号路径信息
              jsondal.Query("v_dict",
                [
                  {'col': 'category', 'logic': '=', 'val': 'room', 'andor': 'and'},
                  {'col': 'id', 'logic': '=', 'val': me.model.roomid, 'andor': ''}
                ],1,1,{'col': 'iid', 'sort': 'desc'},
                function(rtn1){
                  if (rtn1.d != undefined) {
                    me.model.roompath=rtn1.d[0].order_id;
                  }
                  succ(rtn.d[0]);
                },
                function(rtn1){}
              );
            }
          }
        }, fail);
      }
      ,
      saveRepair: function (succ, fail) {
        var me = this;
        var dat = {
          creater: me.model.creater,
          report_person: me.model.report_person,
          phone: me.model.phone,
          roomid: me.model.roomid,
          category: me.model.category,
          imagelist_img: '',
          reason: me.model.reason,
          source: me.model.source,
          status: me.model.status,
          report_dt: me.model.report_dt,
          create_dt: me.model.create_dt,
          iid:me.iid
        };
        jsondal.Insert("app_wy_repair", dat, succ, fail);
      }
    };
    return repair;
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
;
