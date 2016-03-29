angular.module('ktwy.services')


  .factory('service_master_query', function () {

    var master_query = $.extend(IService, {
      roomid:'0',
      roompath:'',
      name:'',
      masters:[{name:'',phone:''}],
      getMaster:function(qry)
      {
        var _this=this;
        //{'col':'roomid','logic':'=','val':'','andor':''}
        return jsondal.doPromise(jsondal.Query,"app_master",qry, 1,99999,{'col':'name','sort':'asc'}).then(function(rtn){
          _this.masters=[];
          if(rtn.d!=undefined)
          {
            $.each(rtn.d,function(k,v){
              var itm={
                name: v.name,
                phone: v.phone
              };
              _this.masters.push(itm);
            });
          }
          else
          {
            _this.masters[{name:'',phone:''}];
          }
        },function(rtn){

        });
      }
    });
    return master_query;
  })
  .factory('service_vehicle_query', function () {

    var vehicle_query = $.extend(IService, {
      plate:'浙A',
      models:'',
      roomid:'',
      roompath:'',
      masterid:'',
      mastername:'',
      phone:'',
      parkid:'0',
      parkpath:'',//车位号
      getVehicle: function () {
        var _this=this;
        //{'col':'roomid','logic':'=','val':'','andor':''}
        return jsondal.doPromise(jsondal.Query,"v_m_vehicle",{'col':'plate','logic':'=','val':_this.plate,'andor':''}, 1,99999,{'col':'plate','sort':'asc'}).then(function(rtn){

          if(rtn.d!=undefined)
          {
            _this.models=rtn.d[0].models;
            _this.roomid=rtn.d[0].roomid;
            _this.roompath=rtn.d[0].roompath;
            _this.masterid=rtn.d[0].masterid;
            _this.mastername=rtn.d[0].mastername;
            _this.phone=rtn.d[0].phone;
            _this.parkid=rtn.d[0].parkid;
            _this.parkpath=rtn.d[0].parkpath;
          }
          else
          {
            _this.models='';
            _this.roomid='';
            _this.roompath='';
            _this.masterid='';
            _this.mastername='';
            _this.phone='';
            _this.parkid='';
            _this.parkpath='';
          }
        },function(rtn){

        });
      }
    });
    return vehicle_query;
  });
