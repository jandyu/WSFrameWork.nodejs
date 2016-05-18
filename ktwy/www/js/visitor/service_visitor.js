angular.module('ktwy.services')
  .factory('service_visitor', function () {
    var visitor=angular.extend({},IService,{
      tablename_list:"v_m_app_wy_visitor",
      tablename:"app_wy_visitor",
      order_list:[{col:'iid',sort:'desc'}],
      save_fileds:['iid','create_dt','creater','phone','roomid','fkname','fksex','dtm_yj','vehicle_flag','vehicle_num',
        'dtm_real','status','memo'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.create_dt=util.DateFormat(new Date(),'yyyy-MM-dd');
        me.model.creater=gl.name;
        me.model.phone=gl.phone;
        me.model.roomid=gl.roomid;
        me.model.fkname='';
        me.model.fksex='男';
        me.model.dtm_yj=new Date();
        me.model.vehicle_flag=false;
        me.model.vehicle_num='浙A';
        me.model.dtm_real='';
        me.model.status='0';
        me.model.memo='';
        me.model.roompath=gl.roompath;
        me.model.statusname='未到访';
        me.model.statusname_='未到访';
        me.model.expird="未过期";
      },
      //获取单个数据模型后,对数据模型进行预处理
      getmodel_after:function(){
        var me=this;
        if(me.model.vehicle_flag=="0")
        {
          me.model.vehicle_flag=false;
        }
        else
        {
          me.model.vehicle_flag=true;
        }
        me.model.dtm_yj=new Date(me.model.dtm_yj);

        if(me.model.vehicle_num=="")
        {
          me.model.vehicle_num='浙A';
        }
        return me.model;
      },
      //保存之前对保存的数据进行预处理
      savemodel_before:function(dat) {
        if(dat.vehicle_flag==false)
        {
          dat.vehicle_flag="0";
        }
        else
        {
          dat.vehicle_flag="1";
        }

        dat.dtm_yj=util.DateFormat(new Date(dat.dtm_yj),'yyyy-MM-dd');


        if(dat.vehicle_num=="浙A")
        {
          dat.vehicle_num='';
        }


        return dat;
      }
    });
    return visitor;
  })
;
