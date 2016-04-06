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
  .factory('service_user_repair', function () {
    var repair={
      userid:'',//用户id
      name:'',//姓名
      phone:'',//联系电话
      roomid:'',//房号id
      roompath:'',//房号路径
      category:'0',//维修类型
      imagelist_img:[{id:'',url:''}],//图片 ,分割
      reason:''//原因
    };
    return repair;
  })
;
