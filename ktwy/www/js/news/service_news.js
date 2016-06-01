angular.module('ktwy.services')
  .factory('service_news_wy', function () {
    var expres=angular.extend({},IService,{
      tablename_list:"v_m_app_wy_expres",
      tablename:"app_wy_expres",
      order_list:[{col:'iid',sort:'desc'}],
      save_fileds:['iid','create_dt','creater','roomid','md','bg','receive_dt','receiver','status','memo'],
      ini_model: function (gl) {
        var me=this;
        me.model.iid='0';
        me.model.create_dt=util.DateFormat(new Date(),'yyyy-MM-dd hh:mm');
        me.model.creater=gl.name;
        me.model.roomid='';
        me.model.md='';
        me.model.bg='';
        me.model.receive_dt='';
        me.model.receiver='';
        me.model.status='0';
        me.model.memo='';
        me.model.roompath='';
        me.model.md_url='img/photo_camar.png';
        me.model.bg_url='img/photo_camar.png';
        me.model.statusname='未签收';
      }
    });
    return expres;
  })
;
