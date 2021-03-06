angular.module('ktwy.services')
  .factory('service_user_suggestion', function () {
    var suggestion = {
      model: {
        creater: '',//用户id
        report_person: '',//姓名
        phone: '',//联系电话
        roomid: '',//房号id
        roompath: '',//房号路径
        category: '0',//维修类型
        imagelist_img: '',//图片 ,分割
        imagelist_url:[{id: '', url: 'img/photo_add.png',rid:''},
          {id: '', url: 'img/photo_add.png',rid:''},
          {id: '', url: 'img/photo_add.png',rid:''},
          {id: '', url: 'img/photo_add.png',rid:''}],//imageurl
        reason: '',//原因
        source: '1',//移动端
        status: '',
        report_dt: util.today('-'),
        create_dt: util.today('-'),
        iid:'0',
        deal_list:[],//处理情况
        visit_list:[]//评价
      },
      iniModel:function() {
        var me=this;
        me.model={
          creater: '',//用户id
          report_person: '',//姓名
          phone: '',//联系电话
          roomid: '',//房号id
          roompath: '',//房号路径
          category: '0',//维修类型
          imagelist_img: '',//图片 ,分割
          imagelist_url:[{id: '', url: 'img/photo_add.png',rid:''},
            {id: '', url: 'img/photo_add.png',rid:''},
            {id: '', url: 'img/photo_add.png',rid:''},
            {id: '', url: 'img/photo_add.png',rid:''}],//imageurl
          reason: '',//原因
          source: '1',//移动端
          status: '',
          statusname:'',
          sourcename:'',
          categoryname:'',
          receiver:'',
          report_dt: util.today('-'),
          create_dt: util.today('-'),
          iid:'0',
          deal_list:[],
          visit_list:[]//评价
        };
      },
      model_list: [],
      show_list: true,
      page:{currpage:0,pagesize:10,totalpages:0,totalrows:0},//分页信息
      load_more:false,//是否还有更多数据
      getsuggestionList: function (userid, succ, fail) {
        var me = this;
        var qry = {'col': 'creater', 'logic': '=', 'val': userid, 'andor': ''};
        var ord = {'col': 'iid', 'sort': 'desc'};
        jsondal.Query("v_m_app_wy_activity", qry, me.page.currpage, me.page.pagesize, ord, function (rtn) {
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
                imagelist_img: v.imagelist_img,
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

          }

          if(me.model_list.length>= parseInt(rtn.p.totalrows))
          {
            me.load_more=false;
          }
          else
          {
            me.load_more=true;
          }

          me.page=rtn.p;

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
        jsondal.Query("v_m_app_wy_activity", qry, me.currpage, 1, ord, function (rtn) {
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

              me.model.imagelist_img=rtn.d[0].imagelist_img;

              me.model.imagelist_url=[];
              var ls_url=rtn.d[0].imagelist_url.split(',');
              /*
               $.each(ls_url,function(k,v){
               me.model.imagelist_url.push({id:k,url:v});
               });
               */
              for(var i=0;i<4;i++)
              {
                var v="img/photo_add.png";
                var vrid='';
                if(ls_url[i])
                {
                  //v=wwwurl+ls_url[i].split(';')[0];
                  v=ls_url[i].split(';')[0];
                  v= wwwurl+v.substr(1);
                  vrid=ls_url[i].split(';')[1];
                }

                me.model.imagelist_url.push({id:i,url:v,rid:vrid});
              }

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
              jsondal.Query("app_wy_activity_detail", qry_deal, me.currpage, 10, ord_deal,function(rtn_deal){

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

        var ls_img='';
        $.each(me.model.imagelist_url,function(k,v){
          if(v.rid!="" && v.rid !=undefined) {
            ls_img = ls_img + v.rid+',';
          }
        });
        if(ls_img.length>0)
        {
          ls_img=ls_img.substr(0,ls_img.length -1);
        }

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
          iid:me.model.iid,
          imagelist_img: ls_img
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


        jsondal.Exec("p_m_activity_pj", {iid:dat.iid,pid:dat.pid,myd:dat.myd,detail:dat.detail,visiter:dat.visiter},
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

        jsondal.Exec("p_m_activity_yzth", {iid:iid},
          function (rtn) {
            var rtn = jsondal.AnaRtn(rtn);

            me.model.status='3';
            me.model.statusname='已撤回';
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
