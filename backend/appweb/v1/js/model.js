/*
wsdta.wsdat 客户端js model
支持：jquery 1.9 +,datsrv.srv.js 0.1+
ver:0.1 
author:Rain.J

--配置举例----------
data例子
 g_m_data = {
                    app_wy_repair: {
                        iid: '<xsl:value-of select="iid" />',
                        create_dt: '<xsl:value-of select="create_dt" />',
                        creater: '<xsl:value-of select="creater" />',
                        report_dt: '<xsl:value-of select="report_dt" />',
                        category: '<xsl:value-of select="category" />',
                        roomid: '<xsl:value-of select="roomid" />',
                        report_person: '<xsl:value-of select="report_person" />',
                        phone: '<xsl:value-of select="phone" />',
                        reason: '<xsl:value-of select="reason" />',
                        receiver: '<xsl:value-of select="receiver" />',
                        status: '<xsl:value-of select="status" />',
                        source: '<xsl:value-of select="source" />',
                        imagelist_img: '<xsl:value-of select="imagelist_img" />',
                        room_path: '<xsl:value-of select="room_path" />'
                    },
                     app_wy_repair1: {
                        iid: '<xsl:value-of select="iid" />',
                        create_dt: '<xsl:value-of select="create_dt" />',
                        creater: '<xsl:value-of select="creater" />',
                        report_dt: '<xsl:value-of select="report_dt" />',
                        category: '<xsl:value-of select="category" />',
                        roomid: '<xsl:value-of select="roomid" />',
                        report_person: '<xsl:value-of select="report_person" />',
                        phone: '<xsl:value-of select="phone" />',
                        reason: '<xsl:value-of select="reason" />',
                        receiver: '<xsl:value-of select="receiver" />',
                        status: '<xsl:value-of select="status" />',
                        source: '<xsl:value-of select="source" />',
                        imagelist_img: '<xsl:value-of select="imagelist_img" />',
                        room_path: '<xsl:value-of select="room_path" />'
                    }
                }
formitem例子
formitem: {
                "app_wy_repair": {
                    iid: { item: 'edit_iid', save_flag: true },
                    create_dt: { item: 'edit_create_dt', save_flag: true },
                    creater: { item: 'edit_creater', save_flag: true },
                    report_dt: { item: 'edit_report_dt', save_flag: true },
                    category: { item: 'edit_category', save_flag: true },
                    roomid: { item: 'edit_roomid', save_flag: true },
                    report_person: { item: 'edit_report_person', save_flag: true },
                    phone: { item: 'edit_phone', save_flag: true },
                    reason: { item: 'edit_reason', save_flag: true },
                    receiver: { item: 'edit_receiver', save_flag: true },
                    status: { item: 'edit_status', save_flag: true },
                    source: { item: 'edit_source', save_flag: true },
                    imagelist_img: { item: 'edit_imagelist_img', save_flag: true },
                    room_path: { item: 'edit_room_path', save_flag: true }
                },
                 "app_wy_repair1": {
                    iid: { item: 'edit_iid', save_flag: true },
                    create_dt: { item: 'edit_create_dt', save_flag: true },
                    creater: { item: 'edit_creater', save_flag: true },
                    report_dt: { item: 'edit_report_dt', save_flag: true },
                    category: { item: 'edit_category', save_flag: true },
                    roomid: { item: 'edit_roomid', save_flag: true },
                    report_person: { item: 'edit_report_person', save_flag: true },
                    phone: { item: 'edit_phone', save_flag: true },
                    reason: { item: 'edit_reason', save_flag: true },
                    receiver: { item: 'edit_receiver', save_flag: true },
                    status: { item: 'edit_status', save_flag: true },
                    source: { item: 'edit_source', save_flag: true },
                    imagelist_img: { item: 'edit_imagelist_img', save_flag: true },
                    room_path: { item: 'edit_room_path', save_flag: true }
                }
            }
*/


var model = {    
    formitem: {},
	defID:"",
	dataFmt:"json",
	uiFmt:"html",
	data: {},    
	create: function(options) {
        var empty = {};
        var rtn = $.extend(empty, model, options);
		rtn.init();
		return rtn;
    },
	init:function(){
	    var me = this;
	    //me.data["app_wy_repair"] = {};
	},
    //刷新UI
	refresh: function (model,callback, er) {
	    var me = this;
	    if (model == null || model == undefined) {
	        model = {};
	    }
	    model.model = me;
	    $.when(me.refreshUIBefore(model))
		.then(me.refreshUI)
		.then(me.refreshUIAfter)
		.then(callback,er)
	},
	refreshUIBefore: function (model) {
	    var me = this; var dfd = $.Deferred();
	    dfd.resolve(model);
	    return dfd.promise();
	},
	refreshUI: function (model) {
	    var me = this; var dfd = $.Deferred();

	    $.each(model.model.data, function (tbname, col_vals) {
	        $.each(col_vals, function (col, val) {
	            var item_set = model.model.formitem[tbname][col];
	            if (item_set == undefined || item_set == null || item_set == "") {
	                return true;//continue;
	            }
	            $("#" + item_set.item).val(val);	            
	        });
	    });


	    dfd.resolve(model);
	    return dfd.promise();
	},
	refreshUIAfter: function (model) {
	    var me = this; var dfd = $.Deferred();
	    dfd.resolve(model);
	    return dfd.promise();
	},
    
	getUIData: function () {
	    var me = this; 
	    
	    $.each(me.formitem, function (tbname, sets) {
	        $.each(sets, function (colname, set) {
	            if (me.data[tbname][colname] == undefined || me.data[tbname][colname] == null) {
	                return true;
	            }
	            var lv = $("#" + set.item).val();
	            if (lv != null) {
	                me.data[tbname][colname] = $("#" + set.item).val();
	            }
	        });
	    });
	},
	
	getSplitPageData:function(page, pageSize,search){		
		var me = this; var dfd = $.Deferred();		
		datsrv.DatGetJsonData(me.defID, page, pageSize, search, function(obj){
			var m = obj.data[0][me.defID];
			dfd.resolve(m);
		});		
		return dfd.promise();
	},
	getSplitPageUI:function(page, pageSize,search){
		var me = this; var dfd = $.Deferred();
		datsrv.DatGetJsonData(me.defID, page, pageSize, search, function(obj){
			var m = obj.data[0][me.defID];
			dfd.resolve(m);
		});	
		return dfd.promise();
	},



	saveBefore:function(model){
	    var me = this; var dfd = $.Deferred();
	    me.getUIData();

	    if (model == null || model == undefined) {
	        model = {};
	    }
	    model.model = me;
	    dfd.resolve(model);
		return dfd.promise();
	},
	saveModel:function(model){
		var me = this; var dfd = $.Deferred();
		
		dfd.resolve(model);
		return dfd.promise();
	},
	saveAfter:function(model){
		var me = this; var dfd = $.Deferred();
		
		dfd.resolve(model);
		return dfd.promise();
	},
	save:function(model,callback,er){		
	    var me = this;	    
		$.when(me.saveBefore(model))
		.then(me.saveModel)
		.then(me.saveAfter)
		.then(callback,er)
	}		
};
