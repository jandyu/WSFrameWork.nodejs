angular.module('ktwy.services', [])

    .factory('service_usercenter', function () {

        var usercenter = $.extend(IService, {
            userid: '0',
            enumber: '',
            name: '',
            dpetid: '0',
            deptname: '',
            phone: '',
            //判断是否登录成功
            checkLogin: function () {
                var _this = this;
                if (_this.userid == "0") {
                    return false;
                }
                else {
                    return true;
                }
            }
        });
        return usercenter;
    })
    //login service
    .factory('service_usercenter_login', function () {
        var usercenter_login = $.extend(IService, {
            username: '',
            password: '',
            checkcode:'',
            checkcode_number:'',//短信编号
            btn_getcode_txt: '获取验证码',
            btn_getcode_disabled: '',//disabled 或者空
            btn_reg_disabled: 'disabled',
            willGoUrl:'',//登陆成功后即将跳转的页面
            //用户登录
            userLogin: function () {
                var _this = this;
                return jsondal.doPromise(jsondal.Exec, "sp_user_login", {
                    username: _this.username,
                    password: _this.password
                });
            },
          getCheckCode:function(succ,err)
          {
            var _this=this;
            var ls_phone=_this.username;
            jsondal.Exec("sp_sms_get_message", { phone: ls_phone }, function (rtn) {
              _this.checkcode_number = jsondal.AnaRtn(rtn);
              jsondal.DealMessage();
              succ()
            }, function (rtn) {
              console.info(rtn);
              err(rtn);
            });
          },
          //校验验证码是否正确
          userLogin:function(succ,err)
          {
            var _this=this;
            jsondal.Exec("sp_employee_login", { number:_this.checkcode_number, code: _this.checkcode, phone: _this.username }, function (rtn) {
              var rtn = jsondal.AnaRtn(rtn);
              succ(rtn);
            }, function (rtn) {
              console.info(rtn);
              err(rtn);
            });
          }
        });


        return usercenter_login;
    })

    .factory('service_roomselect', function ($q) {

        var roomselect = $.extend(IService, {
            units: [
                {
                    pid: '0',
                    p_title: '',
                    uid: '',
                    unit_title: '',
                    child: 0,//子个数
                    p_pid: '0'//父的父id
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
    });
