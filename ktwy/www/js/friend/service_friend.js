angular.module('ktwy.services')
  .factory('service_friend', function () {
    var friend=angular.extend({},IService,{
      tablename_list:"v_m_app_wy_friend",
      tablename:"app_wy_friend",
      order_list:[{col:'namec',sort:'asc'}],
      save_fileds:['iid','category','mid','fid','memo','status','lastdtm'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.category='0';
        me.model.mid="0";
        me.model.fid="0";
        me.model.memo="";
        me.model.name="";
        me.model.phone="";
        me.model.appnickname="";
        me.model.sex="男";
        me.model.birthday=util.DateFormat(new Date(),'yyyy-MM-dd');
        me.model.deptid="0";
        me.model.deptname="";
        me.model.photourl="";
        me.model.post="0";
        me.model.postname="";
        me.model.namec="";
        me.model.categoryname="业主";
        me.model.status="0";
        me.model.statusname="未通过";
        me.model.lastdtm=util.DateFormat(new Date(),'yyyy-MM-dd hh:mm');
      },
      model_list_deal:[],
      add_model_list_deal:function(itm)
      {
        var me=this;
        var result= _.find(me.model_list_deal,function(titm){return titm.key==itm.namec;});
        if(result==undefined)
        {
          me.model_list_deal.push({key:itm.namec,val:[itm]});
        }
        else
        {
          result.val.push(itm);
        }
      },
      getlist_after:function()
      {
        var me=this;
        me.model_list_deal=[];
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
          me.add_model_list_deal(v);
        });
        console.info("------------------me.model_list_deal------------------");
        console.info(me.model_list_deal);
      },
      getmodel_after:function() {
        var me = this;
        if (me.model.photourl != '') {
          me.model.photourl= wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      }
    });
    return friend;
  })


  .factory('service_newfriend', function () {
    var friend=angular.extend({},IService,{
      tablename_list:"v_m_app_wy_friend",
      tablename:"app_wy_friend",
      order_list:[{col:'namec',sort:'asc'}],
      save_fileds:['iid','category','mid','fid','memo','status','lastdtm'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.category='0';
        me.model.mid="0";
        me.model.fid="0";
        me.model.memo="";
        me.model.name="";
        me.model.phone="";
        me.model.appnickname="";
        me.model.sex="男";
        me.model.birthday=util.DateFormat(new Date(),'yyyy-MM-dd');
        me.model.deptid="0";
        me.model.deptname="";
        me.model.photourl="";
        me.model.post="0";
        me.model.postname="";
        me.model.namec="";
        me.model.categoryname="业主";
        me.model.status="0";
        me.model.statusname="未通过";
        me.model.lastdtm=util.DateFormat(new Date(),'yyyy-MM-dd hh:mm');
        me.model.ftype="0";
      },
      getlist_after:function()
      {
        var me=this;
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
        });
        console.info("------------------me.model_list_deal------------------");
        console.info(me.model_list_deal);
      },
      getmodel_after:function() {
        var me = this;
        if (me.model.photourl != '') {
          me.model.photourl= wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      }
    });
    return friend;
  })


  .factory('service_employee', function () {
    var friend=angular.extend({},IService,{
      tablename_list:"v_m_app_wy_friend",
      tablename:"app_wy_friend",
      order_list:[{col:'namec',sort:'asc'}],
      save_fileds:['iid','category','mid','fid','memo'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.category='1';
        me.model.mid="0";
        me.model.fid="0";
        me.model.memo="";
        me.model.name="";
        me.model.phone="";
        me.model.appnickname="";
        me.model.sex="男";
        me.model.birthday=util.DateFormat(new Date(),'yyyy-MM-dd');
        me.model.deptid="0";
        me.model.deptname="";
        me.model.photourl="";
        me.model.post="0";
        me.model.postname="";
        me.model.namec="";
        me.model.categoryname="物业";
        me.model.status="0";
        me.model.statusname="未通过";
      },
      model_list_deal:[],
      add_model_list_deal:function(itm)
      {
        var me=this;
        var result= _.find(me.model_list_deal,function(titm){return titm.key==itm.namec;});
        if(result==undefined)
        {
          me.model_list_deal.push({key:itm.namec,val:[itm]});
        }
        else
        {
          result.val.push(itm);
        }
      },
      getlist_after:function()
      {
        var me=this;
        me.model_list_deal=[];
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
          me.add_model_list_deal(v);
        });
        console.info("------------------me.model_list_deal------------------");
        console.info(me.model_list_deal);
      },
      getmodel_after:function() {
        var me = this;

        if (me.model.photourl != '') {
          me.model.photourl = wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      }
    });
    return friend;
  })


  .factory('service_master', function () {
    var friend=angular.extend({},IService,{
      tablename_list:"v_app_master",
      tablename:"v_app_master",
      order_list:[{col:'iid',sort:'asc'}],
      save_fileds:['iid'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.name="";
        me.model.phone="";
        me.model.appnickname="";
        me.model.sex="";
        me.model.birthday="";
        me.model.roomid="0";
        me.model.roompath="";
        me.model.photourl="";
        me.fstatus="0";//0:未通过,1:已通过,2:等待对方验证
      },
      getlist_after:function()
      {
        var me=this;
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
        });
        console.info("------------------me.model_list_deal------------------");
        console.info(me.model_list_deal);
      },
      getmodel_after:function() {
        var me = this;
        if (me.model.photourl != '') {
          me.model.photourl= wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      }
    });
    return friend;
  })


;
