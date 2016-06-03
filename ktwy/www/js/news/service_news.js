angular.module('ktwy.services')
  .factory('service_news_wy', function () {
    var news_wy = angular.extend({}, IService, {
      tablename_list: "v_m_app_wy_news_wy",
      tablename: "app_wy_news",
      order_list: [{col: 'topnumber', sort: 'desc'}, {col: 'iid', sort: 'desc'}],
      save_fileds: ['iid', 'create_dt', 'creater', 'publish_dt', 'publisher', 'ntype', 'category', 'title', 'ctext', 'imagelist_img', 'memo', 'status', 'topnumber'],
      ini_model: function (gl) {
        var me = this;
        me.model.iid = '0';
        me.model.create_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.creater = gl.userid;
        me.model.publish_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.publisher = gl.userid;
        me.model.ntype = '1';
        me.model.category = '0';
        me.model.title = '';
        me.model.ctext = '';
        me.model.imagelist_img = '';
        me.model.memo = '';
        me.model.topnumber = '0';
        me.model.status = '1';
        me.model.creatername = gl.name;
        me.model.publishername = gl.name;
        me.model.statusname = '已发布';
        me.model.imagelist_url = [{id: '0', url: 'img/photo_add.png', rid: '0'},
          {id: '1', url: 'img/photo_add.png', rid: '0'},
          {id: '2', url: 'img/photo_add.png', rid: '0'},
          {id: '3', url: 'img/photo_add.png', rid: '0'}];
        me.model.imagelist_url_real = [];
      },
      getlist_after: function () {
        var me = this;
        $.each(me.model_list, function (k, val) {

          var ls_url = val.imagelist_url.split(',');
          val.imagelist_url = [];
          for (var i = 0; i < 4; i++) {
            var v = "img/11gstg.png";
            var vrid = '';
            if (ls_url[i]) {
              //v=wwwurl+ls_url[i].split(';')[0];
              v = ls_url[i].split(';')[0];
              v = wwwurl + v.substr(1);
              vrid = ls_url[i].split(';')[1];
            }
            val.imagelist_url.push({id: i, url: v, rid: vrid});
          }
          val.imagelist_url_real = _.filter(val.imagelist_url, function (itm) {
            return itm.rid != "";
          });
        });


      },
      getmodel_after: function () {

        var me = this;
        var ls_url = me.model.imagelist_url.split(',');
        me.model.imagelist_url = [];
        for (var i = 0; i < 4; i++) {
          var v = "img/photo_add.png";
          var vrid = '';
          if (ls_url[i]) {
            v = ls_url[i].split(';')[0];
            v = wwwurl + v.substr(1);
            vrid = ls_url[i].split(';')[1];
          }

          me.model.imagelist_url.push({id: i, url: v, rid: vrid});
        }

        me.model.imagelist_url_real = _.filter(me.model.imagelist_url, function (itm) {
          return itm.rid != "";
        });


        if(me.model.topnumber=="" ||me.model.topnumber=="0")
        {
          me.model.topnumber=false;
        }
        else {
          me.model.topnumber=true;
        }

      },
      savemodel_before: function (dat) {
        var me = this;
        var ls_img = '';
        $.each(me.model.imagelist_url, function (k, v) {
          if (v.rid != "" && v.rid != undefined) {
            ls_img = ls_img + v.rid + ',';
          }
        });
        if (ls_img.length > 0) {
          ls_img = ls_img.substr(0, ls_img.length - 1);
        }
        dat.imagelist_img = ls_img;

        if(dat.topnumber==true)
        {
          dat.topnumber='1';
        }
        else {
          dat.topnumber = "0";
        }

        dat.ctext = dat.ctext.replace(/\n/g, '');

        return dat;
      },
    });
    return news_wy;
  })


  .factory('service_news_reply_wy', function () {
    var news_reply_wy = angular.extend({}, IService, {
      tablename_list: "v_m_app_wy_news_reply_wy",
      tablename: "app_wy_news_reply",
      order_list: [{col: 'iid', sort: 'desc'}],
      save_fileds: ['iid', 'niid', 'rep_dt', 'uid', 'rtext'],
      ini_model: function (gl) {
        var me = this;
        me.model.iid = '0';
        me.model.niid = "0";
        me.model.uid = gl.userid;
        me.model.rtext = "";
        me.model.rep_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.uname = gl.name;
        me.model.photo_url = gl.photo_url;
      },
      getlist_after: function () {
        var me = this;
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
        });
      },
      getmodel_after: function () {
        var me = this;
        if (me.model.photourl != '') {
          me.model.photourl= wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      },
      savemodel_before: function (dat) {


        return dat;
      },
    });
    return news_reply_wy;
  })


  .factory('service_news_yz', function () {
    var news_yz = angular.extend({}, IService, {
      tablename_list: "v_m_app_wy_news_yz",
      tablename: "app_wy_news",
      order_list: [{col: 'topnumber', sort: 'desc'}, {col: 'iid', sort: 'desc'}],
      save_fileds: ['iid', 'create_dt', 'creater', 'publish_dt', 'publisher', 'ntype', 'category', 'title', 'ctext', 'imagelist_img', 'memo', 'status', 'topnumber'],
      ini_model: function (gl) {
        var me = this;
        me.model.iid = '0';
        me.model.create_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.creater = gl.userid;
        me.model.publish_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.publisher = gl.userid;
        me.model.ntype = '0';
        me.model.category = '0';
        me.model.title = '';
        me.model.ctext = '';
        me.model.imagelist_img = '';
        me.model.memo = '';
        me.model.topnumber = '0';
        me.model.status = '1';
        me.model.creatername = gl.name;
        me.model.publishername = gl.name;
        me.model.statusname = '已发布';
        me.model.imagelist_url = [{id: '0', url: 'img/photo_add.png', rid: '0'},
          {id: '1', url: 'img/photo_add.png', rid: '0'},
          {id: '2', url: 'img/photo_add.png', rid: '0'},
          {id: '3', url: 'img/photo_add.png', rid: '0'}];
        me.model.imagelist_url_real = [];
      },
      getlist_after: function () {
        var me = this;
        $.each(me.model_list, function (k, val) {

          var ls_url = val.imagelist_url.split(',');
          val.imagelist_url = [];
          for (var i = 0; i < 4; i++) {
            var v = "img/11gstg.png";
            var vrid = '';
            if (ls_url[i]) {
              //v=wwwurl+ls_url[i].split(';')[0];
              v = ls_url[i].split(';')[0];
              v = wwwurl + v.substr(1);
              vrid = ls_url[i].split(';')[1];
            }
            val.imagelist_url.push({id: i, url: v, rid: vrid});
          }
          val.imagelist_url_real = _.filter(val.imagelist_url, function (itm) {
            return itm.rid != "";
          });
        });


      },
      getmodel_after: function () {

        var me = this;
        var ls_url = me.model.imagelist_url.split(',');
        me.model.imagelist_url = [];
        for (var i = 0; i < 4; i++) {
          var v = "img/photo_add.png";
          var vrid = '';
          if (ls_url[i]) {
            v = ls_url[i].split(';')[0];
            v = wwwurl + v.substr(1);
            vrid = ls_url[i].split(';')[1];
          }

          me.model.imagelist_url.push({id: i, url: v, rid: vrid});
        }

        me.model.imagelist_url_real = _.filter(me.model.imagelist_url, function (itm) {
          return itm.rid != "";
        });



        if(me.model.topnumber=="" ||me.model.topnumber=="0")
        {
          me.model.topnumber=false;
        }
        else {
          me.model.topnumber=true;
        }

      },
      savemodel_before: function (dat) {
        var me = this;
        var ls_img = '';
        $.each(me.model.imagelist_url, function (k, v) {
          if (v.rid != "" && v.rid != undefined) {
            ls_img = ls_img + v.rid + ',';
          }
        });
        if (ls_img.length > 0) {
          ls_img = ls_img.substr(0, ls_img.length - 1);
        }
        dat.imagelist_img = ls_img;

        if(dat.topnumber==true)
        {
          dat.topnumber='1';
        }
        else {
          dat.topnumber = "0";
        }

        dat.ctext = dat.ctext.replace(/\n/g, '');

        return dat;
      },
    });
    return news_yz;
  })


  .factory('service_news_reply_yz', function () {
    var news_reply_yz = angular.extend({}, IService, {
      tablename_list: "v_m_app_wy_news_reply_yz",
      tablename: "app_wy_news_reply",
      order_list: [{col: 'iid', sort: 'desc'}],
      save_fileds: ['iid', 'niid', 'rep_dt', 'uid', 'rtext'],
      ini_model: function (gl) {
        var me = this;
        me.model.iid = '0';
        me.model.niid = "0";
        me.model.uid = gl.userid;
        me.model.rtext = "";
        me.model.rep_dt = util.DateFormat(new Date(), 'yyyy-MM-dd hh:mm');
        me.model.uname = gl.name;
        me.model.photo_url = gl.photo_url;
      },
      getlist_after: function () {
        var me = this;
        $.each(me.model_list,function(k,v){
          if(v.photourl!='') {
            v.photourl=wwwurl + v.photourl.substr(1);
          }
          else
          {
            v.photourl="img/txwdl.png";
          }
        });
      },
      getmodel_after: function () {
        var me = this;
        if (me.model.photourl != '') {
          me.model.photourl= wwwurl + me.model.photourl.substr(1);
        }
        else {
          me.model.photourl = "img/txwdl.png";
        }
      },
      savemodel_before: function (dat) {
        return dat;
      },
    });
    return news_reply_yz;
  })

;
