var IService = {
  model: {},//数据模型
  ini_model: function (gl) {
  },//初始化数据模型
  model_list: [],//列表
  page: {currpage: 0, pagesize: 10, totalpages: 0, totalrows: 0},//分页信息
  tablename_list: '',//查询列表用到的表名称
  order_list: {col: 'iid', sort: 'asc'},
  load_more:false,//是否还有更多数据
  //查询列表-------------------------------------
  //qry:查询条件;op:0表示刷新,1表示加载更多
  getlist: function (qry,op) {
    var me = this;
    return jsondal.doPromise(jsondal.Query, me.tablename_list, qry, me.page.currpage, me.page.pagesize, me.order_list)
      .then(function (rtn) {
        //
        console.info('---------get ['+me.tablename_list+'] data succ---------');
        console.info(rtn);
        if(op==undefined || op=="0")
        {
          me.model_list=[];
        }

        if (rtn.d != undefined) {
          $.each(rtn.d, function (k, v) {
            me.model_list.push(v);
          });
          //数据加载完毕后,执行一次检查是否还有未加载的数据
          console.info('---------me.model_list.length:'+me.model_list.length+';rtn.p.totalrows:'+rtn.p.totalrows+'---------');
          if(me.model_list.length>=rtn.p.totalrows)
          {
            me.load_more=false;
          }
          else
          {
            me.load_more=true;
          }

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
        return me.model;
      }, function (rtn) {
        console.info('---------get ['+me.tablename_list+'-'+iid+'] data error---------');
        console.info(rtn);
        return rtn;
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

        return rtn;
      }, function (rtn) {
        console.info('---------save ['+me.tablename+'] data error---------');
        console.info(rtn);

        return rtn;
      });
  }
};

