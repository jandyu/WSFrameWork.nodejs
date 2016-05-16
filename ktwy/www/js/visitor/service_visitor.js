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
        me.model.dtm_yj=util.DateFormat(new Date(),'yyyy-MM-dd');
        me.model.vehicle_flag='0';
        me.model.vehicle_num='';
        me.model.dtm_real='';
        me.model.status='0';
        me.model.memo='';
        me.model.roompath=gl.roompath;
        me.model.statusname='未到访';
      }
    });
    return visitor;
  })
;
