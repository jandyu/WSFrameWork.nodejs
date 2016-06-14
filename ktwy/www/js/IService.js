var IService = {
  model: {},//数据模型
  ini_model: function (gl) {
  },//初始化数据模型
  model_list: [],//列表
  page: {currpage: 0, pagesize: 10, totalpages: 0, totalrows: 0},//分页信息
  tablename_list: '',//查询列表用到的表名称
  order_list: {col: 'iid', sort: 'asc'},
  load_more: false,//是否还有更多数据
  //查询列表-------------------------------------
  //qry:查询条件;op:0表示刷新,1表示加载更多
  getlist: function (qry, op) {
    var me = this;
    if (op == undefined || op == "0") {
      me.page.currpage=0;
    }
    else
    {
      //me.page.currpage=me.page.currpage +1;
      me.page.currpage=parseInt(me.page.currpage) +1;
    }
    return jsondal.doPromise(jsondal.Query, me.tablename_list, qry, me.page.currpage, me.page.pagesize, me.order_list)
      .then(function (rtn) {
        //
        console.info('---------get [' + me.tablename_list + '] data succ---------');
        console.info(rtn);
        if (op == undefined || op == "0") {
          me.model_list = [];
        }

        if (rtn.d != undefined) {
          $.each(rtn.d, function (k, v) {
            $.each(v,function(k1,v1){v[k1]=jsondal.TransDBToStr(v1);});
            me.model_list.push(v);
          });
        }

        //数据加载完毕后,执行一次检查是否还有未加载的数据
        console.info('---------me.model_list.length:' + me.model_list.length + ';rtn.p.totalrows:' + rtn.p.totalrows + '---------');
        if (me.model_list.length >=  parseInt(rtn.p.totalrows)) {
          me.load_more = false;
        }
        else {
          me.load_more = true;
        }
        me.page = rtn.p;
        //数据解析完成后,做一些处理
        me.getlist_after();
        return me.model_list;
      }, function (rtn) {
        console.info('---------get [' + me.tablename_list + '] data error---------');
        console.info(rtn);
        return rtn;
      });
  },
  //数据解析完成后,做一些处理
  getlist_after:function()
  {
  },
  //获取单个数据模型------------------------------
  getmodel: function (vqry) {
    var me = this;
    var qry ={};
    if($.isNumeric(vqry)==true) {
      qry = {'col': 'iid', 'logic': '=', 'val': vqry, 'andor': ''};
    }
    else
    {
      qry=vqry;
    }

    var ord = {'col': 'iid', 'sort': 'desc'};

    return jsondal.doPromise(jsondal.Query, me.tablename_list, qry, 1, 1, ord)
      .then(function (rtn) {
        //
        console.info('---------get [' + me.tablename_list + '-] data succ---------');
        console.info(rtn);

        if (rtn.d != undefined) {
          me.model = rtn.d[0];
        }
        $.each(me.model,function(k,v){
          me.model[k]=jsondal.TransDBToStr(v);
        });
        //通过getmodel_after处理获取的数据
        me.getmodel_after();
        return me.model;
      }, function (rtn) {
        console.info('---------get [' + me.tablename_list + '-] data error---------');
        console.info(rtn);
        return rtn;
      });
  },
  //获取单个数据模型后,对数据模型进行预处理
  getmodel_after:function(){
  },
  //保存数据-------------------------------------
  tablename: '',//保存数据时用到的表名称
  save_fileds: [],//保存的字段
  //保存之前对保存的数据进行预处理
  savemodel_before:function(dat) {
    return dat;
  },
  savemodel: function () {
    var me = this;
    var dat = {};
    //处理保存的字段
    $.each(me.save_fileds, function (k, v) {
      dat[v] = me.model[v];
      dat[v]=jsondal.TransStrToDB(dat[v]);
    });

    console.info('---------before save data [' + me.tablename + ']---------');
    console.info(dat);

    //经过save_before处理
    dat=me.savemodel_before(dat);

    console.info('---------save_before deal data [' + me.tablename + ']---------');
    console.info(dat);


    return jsondal.doPromise(jsondal.Update, me.tablename, dat)
      .then(function (rtn) {
        console.info('---------save [' + me.tablename + '] data succ---------');
        console.info(rtn);
        return rtn;
      }, function (rtn) {
        console.info('---------save [' + me.tablename + '] data error---------');
        console.info(rtn);

        return rtn;
      });
  },
  //删除数据---------------------------------
  delmodel: function (deldat) {
    var me = this;
    return jsondal.doPromise(jsondal.Delete, me.tablename, deldat)
      .then(function (rtn) {
        console.info('---------del [' + me.tablename + '] data succ---------');
        console.info(rtn);

        return rtn;
      }, function (rtn) {
        console.info('---------del [' + me.tablename + '] data error---------');
        console.info(rtn);

        return rtn;
      });
  },
  //执行过程---------------------------------
  //过程名称\参数
  exec: function (procname,procparams) {
    var me = this;
    return jsondal.doPromise(jsondal.Exec,procname, procparams)
      .then(function (rtn) {
        console.info('---------exec [' + procname + '] data succ---------');
        console.info(rtn);
        return rtn;
      }, function (rtn) {
        console.info('---------exec [' + procname + '] data error---------');
        console.info(rtn);

        return rtn;
      });
  }
};

