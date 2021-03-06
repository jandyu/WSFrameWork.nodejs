﻿/*
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
    AnaRtn:function(rtn)
    {
        //成功
        if (rtn.indexOf("<succ ") == 0) {
            rtn = rtn.substring(11);
            rtn = rtn.substring(0, rtn.indexOf("'/>"));
        }
        return rtn;
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
    },
    //使用方法jsondal.Exec("存储过程名称",{iid:'1'})
    Exec: function (proc, parm, succ, fail) {
        var _this = this;
        datsrv.CallSrvOper(_this.database, proc, parm, succ, fail);
    },
    Query: function (tablename, param, succ, fail, currpage, pagesize, order) {
        var _this = this;
        var defid = _this.database + "." + tablename;
        _this.QueryBase(defid,"json",param,"json",currpage,pagesize,order,succ,fail);
    },

    QueryBase: function (defid, fmtid, param, dstyle, currpage, pagesize, order, succ, fail) {
        if (!currpage) currpage = 1;
        if (!pagesize) pagesize = 999999;        
        if (!param) param = [];
        if (!order) order = [];

        var _param = [];
        if ($.isArray(param)) {
            _param = param;
        }
        else {
            _param.push(param);
        }


        var _order = [];
        if ($.isArray(order)) {
            _order = order;
        }
        else {
            _order.push(order);
        }

        var order_key = "";
        if (defid.indexOf('.') > -1) {
            order_key = defid.split('.')[1] + "_table";
        }
        else {
            order_key = defid;
        }
        console.info(order_key);

        var qryobj = { common: { currpage: currpage, pagesize: pagesize, where: [] } };
        qryobj[order_key] = { order: _order };
        qryobj.common.where = _param;
        var pstdat = { defid: defid, fmtid: 'json', strparam: JSON.stringify(qryobj),  dStyle: "json" };        
        console.info(pstdat);

        if (defid.indexOf('.') > -1) {
            defid = defid.split('.')[1] + "_table";
        }       
        $.getJSON(datsrv.uurl + "?callback=?", pstdat, function (rtnobj) {            
            console.info(rtnobj);            
            if (!rtnobj.data) {
                console.info(rtnobj);
                if (fail) {
                    fail(rtnobj)
                }
                return;
            }
            
            var m = rtnobj.data[0][defid];
            var obj = { p: { currpage: m.currpage, pagesize: m.pagesize, totalpages: m.totalpages, totalrows: m.totalrows }, d: m.d };
            if (succ) {
                succ(obj);
            }
        });
       
    }
}