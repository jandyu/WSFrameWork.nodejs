/*
wsdta.wsdat 客户端js model
支持：jquery 1.9 +,datsrv.srv.js 0.1+
ver:0.1 
author:Rain.J
*/
var model ={
	defID:"",
	dataFmt:"json",
	uiFmt:"html",
	data:{},
	create: function(options) {
        var empty = {};
        var rtn = $.extend(empty, model, options);
		rtn.init();
		return rtn;
    },
	init:function(){
		
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
		setTimeout(function () {
		    model.a++;
		    console.info(model);
			dfd.resolve(model);
		},1);
		return dfd.promise();
	},
	saveModel:function(model){
		var me = this; var dfd = $.Deferred();
		setTimeout(function () {
		    model.a++;
		    console.info(model);
		    dfd.reject(model);
		},1);
		return dfd.promise();
	},
	saveAfter:function(model){
		var me = this; var dfd = $.Deferred();
		setTimeout(function () {
		    model.a++;
		    console.info(model);
			dfd.resolve(model);
		},1);
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


var wy_weixiu = model.create({});
wy_weixiu.save(
	{a:1},
	function(m){
		console.info(m);
		},
	function(e){
		console.info("错误");
		}
);

