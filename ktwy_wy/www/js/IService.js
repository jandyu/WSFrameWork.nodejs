var IService = {
  model: {},//数据模型
  ini_model: function () {
  },//初始化数据模型
  model_list: [],//列表
  page: {currpage: 0, pagesize: 10, totalpages: 0, totalrows: 0},//分页信息
  tablename_list: '',//查询列表用到的表名称
  order_list: {col: 'iid', sort: 'asc'},
  //查询列表-------------------------------------
  getlist: function (qry) {
    var me = this;
    return jsondal.doPromise(jsondal.Query, me.tablename_list, qry, me.page.currpage, me.page.pagesize, me.order_list)
      .then(function (rtn) {
        //
        console.info('---------get ['+me.tablename_list+'] data succ---------');
        console.info(rtn);

        if (rtn.d != undefined) {
          $.each(rtn.d, function (k, v) {
            me.model_list.push(v);
          });
          me.page=rtn.p;
        }

      }, function (rtn) {
        console.info('---------get ['+me.tablename_list+'] data error---------');
        console.info(rtn);
      });
  },
  //获取单个数据模型------------------------------
  getmodel: function (iid) {
    var me = this;
    var qry = {'col': 'iid', 'logic': '=', 'val': iid, 'andor': ''};
    var ord = {'col': 'iid', 'sort': 'desc'};

    return jsondal.doPromise(jsondal.Query, me.tablename_list, qry,1, 1,ord)
      .then(function (rtn) {
        //
        console.info('---------get ['+me.tablename_list+'-'+iid+'] data succ---------');
        console.info(rtn);

        if (rtn.d != undefined) {
          me.model=rtn.d[0];
        }
      }, function (rtn) {
        console.info('---------get ['+me.tablename_list+'-'+iid+'] data error---------');
        console.info(rtn);
      });
  },

  //保存数据-------------------------------------
  tablename: '',//保存数据时用到的表名称
  save_fileds:[],//保存的字段
  savemodel: function () {
    var me=this;
    var dat={};
    //处理保存的字段
    $.each(me.save_fileds,function(k,v){
      dat[v]=me.model[v];
    });
    console.info('---------before save data ['+me.tablename+']---------');
    console.info(dat);


    return jsondal.doPromise(jsondal.Update, me.tablename, dat)
      .then(function (rtn) {
        console.info('---------save ['+me.tablename+'] data succ---------');
        console.info(rtn);
      }, function (rtn) {
        console.info('---------save ['+me.tablename+'] data error---------');
        console.info(rtn);
      });
  }
};

