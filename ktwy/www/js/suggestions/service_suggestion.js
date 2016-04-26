angular.module('ktwy.services')
  .factory('service_user_suggestion', function () {
    var suggestion = {
      model: {
        iid:'0',
        create_dt: util.today('-'),
        creater: '',//用户id
        report_dt: util.today('-'),
        category: '1',//维修类型
        roomid: '',//房号id
        report_person: '',//姓名
        phone: '',//联系电话
        reason: '',//原因
        receiver:'',
        status: '',
        source: '1',//移动端
        roompath: '',//房号路径
        statusname:'',
        sourcename:'',
        categoryname:'',
        deal_list:[],//处理情况
        visit_list:[]//评价
      },
      iniModel:function() {
        var me=this;
        me.model={
          iid:'0',
          create_dt: util.today('-'),
          creater: '',//用户id
          report_dt: util.today('-'),
          category: '1',//维修类型
          roomid: '',//房号id
          report_person: '',//姓名
          phone: '',//联系电话
          reason: '',//原因
          receiver:'',
          status: '',
          source: '1',//移动端
          roompath: '',//房号路径
          statusname:'',
          sourcename:'',
          categoryname:'',
          deal_list:[],//处理情况
          visit_list:[]//评价
        };
      },
      model_list: [],
      show_list: true,
      currpage: 0,
      getsuggestionList: function (userid, succ, fail) {
        var me = this;
        var qry = {'col': 'creater', 'logic': '=', 'val': userid, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'desc'};
        jsondal.Query("v_m_app_wy_activity", qry, me.currpage, 50, ord, function (rtn) {
          console.info(rtn);

          if (rtn.d != undefined) {
            me.model_list=[];
            $.each(rtn.d, function (k, v) {
              var itm = {
                creater: v.creater,
                report_person: v.report_person,
                phone: v.phone,
                roomid: v.roomid,
                roompath: v.roompath,
                category: v.category,
                reason: v.reason,
                source: v.source,
                status: v.status,
                statusname: v.statusname,
                sourcename: v.sourcename,
                categoryname: v.categoryname,
                report_dt: v.report_dt,
                create_dt: v.create_dt,
                receiver: v.receiver,
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
      getsuggestion:function(iid,succ,fail)
      {
        var me = this;
        var qry = {'col': 'iid', 'logic': '=', 'val': iid, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'desc'};
        jsondal.Query("v_m_app_wy_suggestion", qry, me.currpage, 1, ord, function (rtn) {
          console.info('---------------');
          console.info(rtn);
          if (rtn.d != undefined) {
            if (succ != undefined && succ != null) {
              me.model.creater=rtn.d[0].creater;
              me.model.report_person=rtn.d[0].report_person;
              me.model.phone=rtn.d[0].phone;
              me.model.roomid=rtn.d[0].roomid;
              me.model.roompath=rtn.d[0].roompath;
              me.model.category=rtn.d[0].category;
              me.model.categoryname=rtn.d[0].categoryname;
              me.model.reason=rtn.d[0].reason;
              me.model.source=rtn.d[0].source;
              me.model.sourcename=rtn.d[0].sourcename;
              me.model.status=rtn.d[0].status;
              me.model.statusname=rtn.d[0].statusname;
              me.model.receiver=rtn.d[0].receiver;
              me.model.report_dt=rtn.d[0].report_dt;
              me.model.create_dt=rtn.d[0].create_dt;
              me.model.iid=rtn.d[0].iid;

              //处理情况
              var qry_deal = {'col': 'pid', 'logic': '=', 'val': iid, 'andor': ''};
              var ord_deal = {'col': 'iid', 'sort': 'asc'};
              jsondal.Query("v_m_app_wy_activity_detail", qry_deal, me.currpage, 10, ord_deal,function(rtn_deal){

                  console.log("---------rtn_deal----------");
                  console.log(rtn_deal);

                  me.model.deal_list=[];
                  if (rtn_deal.d != undefined) {
                    me.model.deal_list=rtn_deal.d;
                  }

                  var qry_visit = {'col': 'pid', 'logic': '=', 'val': iid, 'andor': ''};
                  var ord_visit = {'col': 'iid', 'sort': 'asc'};
                  jsondal.Query("v_m_app_wy_activity_visit", qry_visit, me.currpage, 10, ord_visit,function(rtn_visit){

                    console.log("---------rtn_visit----------");
                    console.log(rtn_visit);


                    me.model.visit_list=[];
                    if (rtn_visit.d != undefined) {
                      me.model.visit_list=rtn_visit.d;
                    }
                    else
                    {

                    }
                    succ(rtn.d[0]);
                  },function(rtn_visit){

                  });


                },
                function(rtn_deal){

                });
            }
          }
        }, fail);
      }
      ,
      savesuggestion: function (succ, fail) {
        var me = this;
        //me.model.status==""?"0":me.model.status
        if(me.model.status=="")
        {
          me.model.status="0";
        }

        var dat = {
          creater: me.model.creater,
          report_person: me.model.report_person,
          phone: me.model.phone,
          roomid: me.model.roomid,
          category: me.model.category,
          reason: me.model.reason,
          source: me.model.source,
          status: me.model.status,
          report_dt: me.model.report_dt,
          create_dt: me.model.create_dt,
          receiver:me.model.receiver,
          iid:me.model.iid
        };
        jsondal.Insert("app_wy_activity", dat, function(rtn){
          rtn=jsondal.AnaRtn(rtn);
          if(me.model.iid=="0")
          {
            me.model.iid=rtn;
          }
          succ(rtn);
        }, fail);
      }
      ,
      //评价信息
      saveVisit:function(tmp,succ,fail){

        var me=this;
        var dat= {
          pid: me.model.iid,
          iid:"0",
          visiter:me.model.report_person,
          myd:tmp.myd,
          detail:tmp.detail,
          dt:util.today('-')
        };


        jsondal.Exec("p_m_suggestion_pj", {iid:dat.iid,pid:dat.pid,myd:dat.myd,detail:dat.detail,visiter:dat.visiter},
          function (rtn) {
            var rtn = jsondal.AnaRtn(rtn);
            if(dat.iid=="0")
            {
              dat.iid=rtn;
            }
            if(dat.myd=="0") {
              dat.mydname = '满意';
            }
            if(dat.myd=="1") {
              dat.mydname = '一般';
            }
            if(dat.myd=="2") {
              dat.mydname = '不满意';
            }
            dat.type='0';
            dat.typename="评价";

            me.model.visit_list.push(dat);

            me.model.status='6';

            succ(rtn);
          }, function (rtn) {
            console.info(rtn);
            err(rtn);
          });

      },

      //业主撤回
      masterReturn:function(iid,succ,fail){
        var me=this;

        jsondal.Exec("p_m_suggestion_yzth", {iid:iid},
          function (rtn) {
            var rtn = jsondal.AnaRtn(rtn);

            me.model.status='3';

            succ(rtn);
          }, function (rtn) {
            console.info(rtn);
            err(rtn);
          });

      }
    };
    return suggestion;
  })
;
