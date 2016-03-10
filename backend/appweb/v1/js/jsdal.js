/*
data.srv数据服务器客户端js库
支持：jquery 1.4 以上
ver:0.1
author:Rain.J
*/

var wwwurl_message = "http://localhost:50045";

var jsondal = {
    database:"db_app",
    DealMessage: function () {
        $.getJSON(wwwurl_message + "/DealMsg.asmx/DealMessage?jsoncallback=?", {}, function (rtn) { console.info(rtn); })
    },
    //使用方法:
    //1、jsondal.Insert("app_resource_message",{priority:'9',target_type:'4',target:'15606526620',info:'注册,验证码54321,短信编号12345。【科腾社区】',status:'0'})
    //2、jsondal.Insert("app_resource_message",[{priority:'9',target_type:'4',target:'15606526620',info:'注册,验证码54321,短信编号12345。【科腾社区】',status:'0'}])
    Insert: function (tablename, data, succ, fail) {
        var _this = this;
        _this.Save(tablename, data,[], succ, fail);
    },
    Update: function (tablename, data, succ, fail) {
        var _this = this;
        _this.Save(tablename, data,[], succ, fail);
    },
    //jsondal.Delete("app_resource_message",562)
    //jsondal.Delete("app_resource_message",[562,563])
    Delete: function (tablename, deldata, succ, fail) {
        var _this = this;
        _this.Save(tablename, [], deldata, succ, fail);
    },

    Save: function (tablename, data,deldata, succ, fail) {
        var _this = this;
        var _data = [];
        var _deldata = [];
        var _tablename = _this.database + "." + tablename;
        if ($.isArray(data)) {
            _data = data;
        }
        else {
            _data.push(data);
        }

        if ($.isArray(deldata)) {
            _deldata = deldata;
        }
        else {
            _deldata.push(deldata);
        }


        //增加c0
        $.each(_data, function (k, v) {
            var iid = v.iid || 0;
            if (iid == 0) {
                v = $.extend({ c0: '8' }, v);
                delete v.iid;//不需要iid
                _data[k] = v;
            }
            else {
                v = $.extend({ c0: '9', key_iid: iid }, v);
                delete v.iid;//不需要iid
                _data[k] = v;
            }
        });
        //处理key_iid
        datsrv.DatSaveSingleRow(_tablename, _data, _deldata, succ, fail);
    }
}