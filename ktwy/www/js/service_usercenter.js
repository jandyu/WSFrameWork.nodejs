angular.module('ktwy.services', [])

.factory('service_usercenter', function() {

  var usercenter= $.extend( IService, {
    userid:'0',
    roomid:'0',
    roompath:'',
    name:'',
    phone:'',
    nickname:'',
    deviceid:'',
    platform:'',
    sex:'男',
    birthday:new Date('1900-01-01'),
    photo:'',
    photo_url:'',

    //设置时的标题
    title:'个人头像',
    type:'photo',

    //判断是否登录成功
    checkLogin:function() {
      var _this=this;
      if (_this.userid == "0") {
        return false;
      }
      else
      {
        return true;
      }
    },
    //保持用户信息
    saveUser:function(succ,fail)
    {
      var me=this;
      var dat = {
        iid:me.userid,
        name:me.name,
        appnickname:me.nickname,
        sex:me.sex,
        birthday:me.birthday,
        photo:me.photo,
        roomid:me.roomid
      };
      jsondal.Insert("app_master", dat, function(rtn){
        rtn=jsondal.AnaRtn(rtn);
        succ(rtn);
      }, fail);
    },
    changepassword:function(m,succ,err)
    {
      var _this=this;
      m.iid=_this.userid;

      jsondal.Exec("sp_user_upd_pwd", {iid: m.iid,oldpwd: m.oldpwd,newpwd: m.newpwd }, function (rtn) {
        var rtn = jsondal.AnaRtn(rtn);
        succ(rtn);
      }, function (rtn) {
        console.info(rtn);
        err(rtn);
      });
    },

    getCheckCode_updphone:function(m,succ,err)
    {
      var _this=this;
      var ls_phone= m.newphone;
      jsondal.Exec("sp_sms_get_message", { phone: ls_phone }, function (rtn) {
        var checkcode_number = jsondal.AnaRtn(rtn);
        jsondal.DealMessage();
        succ(checkcode_number);
      }, function (rtn) {
        console.info(rtn);
        err(rtn);
      });
    },

    changephone:function(m,succ,err)
    {
      var _this=this;
      m.iid=_this.userid;

      jsondal.Exec("sp_user_upd_phone", {number: m.checkcode_number,code: m.code,phone: m.newphone,iid: m.iid}, function (rtn) {
        var rtn = jsondal.AnaRtn(rtn);
        succ(rtn);
      }, function (rtn) {
        console.info(rtn);
        err(rtn);
      });
    },

  });
  return usercenter;
})
  //login service
  .factory('service_usercenter_login', function() {

    var usercenter_login= $.extend( IService,{
      username:'15606526620',
      password:'123456',
      deviceid:'',
      platform:'',
      willGoUrl:'',
      //用户登录
      userLogin:function()
      {
        var _this=this;
        return jsondal.doPromise(jsondal.Exec,"sp_user_login", { username:_this.username,password:_this.password,deviceid:_this.deviceid ,platform:_this.platform });
      },
      userLoginDevice:function()
      {
        var _this=this;
        return jsondal.doPromise(jsondal.Exec,"sp_user_login_device", { deviceid:_this.deviceid});
      },
      userLoginOut:function()
      {
        var _this=this;
        return jsondal.doPromise(jsondal.Exec,"sp_user_login_out", { deviceid:_this.deviceid});
      }
      });
    return usercenter_login;
  })
  //reg service
  .factory('service_usercenter_reg', function($ionicPopup) {

    var obj=$.extend( IService,{
      usercenter_reg:{
        username:'',//手机号
        checkcode:'',
        checkcode_number:'',//短信编号
        name:'',//姓名
        nickname:'',//昵称
        password:'',
        repassword:'',
        unit:'',//房间id
        unitname:'',//房号全路径
        btn_getcode_txt:'获取验证码',
        btn_getcode_disabled:'',//disabled 或者空
        btn_reg_disabled:'disabled',
        userid:'0',
        selectroom_disabled:false
      },
      //获取验证码
      getCheckCode:function(succ,err)
      {
        var _this=this;
        var ls_phone=_this.usercenter_reg.username;
        jsondal.Exec("sp_sms_get_message", { phone: ls_phone }, function (rtn) {
          _this.usercenter_reg.checkcode_number = jsondal.AnaRtn(rtn);
          jsondal.DealMessage();
          succ()
        }, function (rtn) {
          console.info(rtn);
          err(rtn);
        });
      },
      //校验验证码是否正确
      checkCode:function(succ,err)
      {
        var _this=this;
        jsondal.Exec("sp_check_code", { number:_this.usercenter_reg.checkcode_number, code: _this.usercenter_reg.checkcode, phone: _this.usercenter_reg.username }, function (rtn) {
          var rtn = jsondal.AnaRtn(rtn);
         succ(rtn);
        }, function (rtn) {
          console.info(rtn);
          err(rtn);
        });
      },

      //用户注册
      regUser:function()
      {
        var _this=this;
        return jsondal.doPromise(jsondal.Exec,"sp_user_reg", { number: _this.usercenter_reg.checkcode_number,
          code: _this.usercenter_reg.checkcode, phone: _this.usercenter_reg.username, name: _this.usercenter_reg.name,
          nick_name: _this.usercenter_reg.nickname, password: _this.usercenter_reg.password, unit: _this.usercenter_reg.unit });
      }
    });

    return obj;
  })


.factory('service_roomselect', function($q) {

  var roomselect = $.extend(IService, {
    navlist: [{id:'0',title:'社区'}],
    //添加一个导航
    addnavlist:function(id,title)
    {
      var me=this;
      if(_.isUndefined(_.find(me.navlist,function(itm){return itm.id==id;})))
      {
        me.navlist.push({id:id,title:title});
      }
    },
    //截断
    interceptnavlist: function (id) {
      var me=this;
      var idx=-1;
      idx= _.findIndex(me.navlist,function(itm){return itm.id==id;});
      if(idx>=0)
      {
        me.navlist.splice(idx+1,me.navlist.length - idx -1);
      }
    },
    units: [
      {
        pid: '0',
        p_title: '',
        uid: '',
        unit_title: '',
        child: 0,//子个数
        p_pid: '0',//父的父id
        title:''
      }
    ],
    getUnits: function (pid) {

      return jsondal.doPromise(jsondal.Query, "v_room_select", {
        'col': 'pid',
        'logic': '=',
        'val': pid,
        'andor': ''
      }, 1, 99999, {'col': 'unit_title', 'sort': 'asc'});
    },
    getFirst: function () {
      var _this = this;
      if (this.units.length > 0) {
        return _this.units[0];
      }
      else {
        return {
          pid: '',
          p_title: '',
          uid: '',
          unit_title: '',
          child: 0,//子个数
          p_pid: ''
        }
      }
    }
  });


  return roomselect;
})

.factory('service_imageview',function(){
  var rtn={urls:[],
    idx:0
  };
  return rtn;
})
;
