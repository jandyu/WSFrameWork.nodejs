/*
grid edit 客户端js库
支持：jquery 1.4 +,datsrv.srv.js 0.1+
ver:0.1 
author:Rain.J
*/
var g_dict ={};
var sysDict={
	create: function(options) {
        var empty = {};
        var rtn = $.extend(empty, sysDict, options);		
		return rtn;
    }
}
var gridEdit = {
	keyname:'iid',
    editlist:[],
	gridlist:[{idx:1,dictname:'',link:''}],
    rowid:   -1,		// 默认新 增模式-1
    currpage: 1,
    defid: "db_yjsh",
	fmtid:"table_page",
    tablename: "OMList_table",
	formname:"",
	addwhere:[],
	griddiv:"#table_content",
	order:{},
	where:{},
	billbi:'',
	wherediv:'#',
	btnlist:{save:"#save",last:"#last",next:"#next",add:"#add",del:"#del",print:"#print",export:"#export"},
    create: function(options) {
        var empty = {};
        var rtn = $.extend(empty, gridEdit, options);
		rtn.init();
		return rtn;
    },
	initcallback:function(){
	},
	
	init:function(){
		var me = this;
		this.setqueryresult();
		//绑定按钮
		$(me.btnlist.save).click(function(){me.saveRowData();});
		$(me.btnlist.last).click( function(){me.editLastRow();});
		$(me.btnlist.next).click(function(){me.editNextRow();});		
		$(me.btnlist.add).click(function(){me.addRowData();});
		$(me.btnlist.del).click(function(){me.delRowData();});	
		$(me.btnlist.print).click(function(){me.printPdf();});
		$(me.btnlist.export).click(function(){me.exporttxt();});
		
		$(me.btnlist.audite).click(function(){me.auditebill(g_user.name,'001');});
		//初始化回调
		this.initcallback();
	},
	auditebillCallback:function(){
	},
	auditebill:function(oper,oid){ 
		var me = this;
		datsrv.CallSrvOperSync(me.defid, "USP_STK_BILL_AUDITE_STOCK", {bill_bi:me.billbi,OPERATOR:oper,outlet_id:oid},function(rtn){
			datsrv.msgbox("审核数据成功！");	
			var iid = rtn.split("'")[1];
			me.editRowData(iid);
			me.auditebillCallback();
		},function(rtn){
			datsrv.msgbox("审核数据失败！"+rtn.split("'")[1]);
		});
	},
	dblclkrow:function(){		
	},
    /*
    *查询数据
    */
	setorder:function(colname){
		var me = this;
		if(typeof(me.order[me.defid])=='undefined'){
			me.order[me.defid] = {order:[{col:'',sort:''}]};
		}
		//默认多列排序时，表头点击改为单列重新排序
		if(me.order[me.defid].order.length>1){
			me.order[me.defid] = {order:[{col:'',sort:''}]};
		}
		if(me.order[me.defid].order[0].col==colname){
			if(me.order[me.defid].order[0].sort=='asc'){
				me.order[me.defid].order[0].sort='desc';					
			}
			else{
				me.order[me.defid].order[0].sort='asc';					
			}
		}
		else{
			me.order[me.defid].order[0].col=colname;
			me.order[me.defid].order[0].sort='asc';					
		}
		me.queryData(me.currpage,me.where);
	},
	setqueryresult:function(){
		var me = this;
		//循环行
		$(me.griddiv).find("tbody tr").each(function() {
            var line = $(this);
			//console.info($(line).children("td:eq(0)").text());
			if($.trim($(line).children("td:eq(0)").text()) == "合计") return;
			//循环列
           	var iid = $(line).children("td:eq(1)").children("input").attr("iid");
            me.gridlist.forEach(function(item) {

				var val = $(line).children("td:eq(" + item.idx + ")").text();
				if(val=="") return;
				var rtnitem=val;
				//设置列属性 sysDict
				if(item.hasOwnProperty("dictname")){
					if(sysDict.hasOwnProperty(item.dictname)){
						//标题
						if(sysDict[item.dictname].hasOwnProperty("val_"+val))
						{
							rtnitem= sysDict[item.dictname]["val_"+val].title+"["+val+"]";
							//样式,设置到td上						
							$(line).children("td:eq(" + item.idx + ")").addClass(sysDict[item.dictname]["val_"+val].class);
						}
					}
				}
				//设置列属性
				if(item.hasOwnProperty("dictname")){
					if(g_dict.hasOwnProperty(item.dictname)){
						//标题
						if(g_dict[item.dictname].hasOwnProperty("val_"+val))
						{
							rtnitem= g_dict[item.dictname]["val_"+val].title+"["+val+"]";
							//样式,设置到td上						
							$(line).children("td:eq(" + item.idx + ")").addClass(g_dict[item.dictname]["val_"+val].class);
						}
					}
				}
				if(item.hasOwnProperty("class")){
					var val = $(line).children("td:eq(" + item.idx + ")").text();
					if(item.class.mode=='0'){	//模式 0 ，大于小于，lt，gt
						if(parseFloat(val) < 0){
							$(line).children("td:eq(" + item.idx + ")").addClass(item.class.lt);
						}
						else{
							$(line).children("td:eq(" + item.idx + ")").addClass(item.class.gt);
						}
					}
					
					
					
				}
				//
				if(item.hasOwnProperty("isimg")){
					var str =item.isimg;
					rtnitem = $("<img/>").attr("src",rtnitem).css("width",str);
				}
				//
				if(item.hasOwnProperty("islink")){
					var str =item.islink;
					rtnitem = $("<a target='_blank'/>").attr("href",rtnitem).text(str);
				}
				//checkbox
				if(item.hasOwnProperty("check")){
					var str =item.check;
					str = str.replace(/_iid/g,iid);
					str = str.replace(/_val/g,val);
					if(val=="1" ){					
						rtnitem = $("<input type='checkbox'>").attr("id",str).attr("checked", true).click(item.chkclick);
					}					
					else{						
						rtnitem = $("<input type='checkbox'>").attr("id",str).attr("checked",false).click(item.chkclick);
					}
					
				}
				//链接
				if(item.hasOwnProperty("link")){
					var str =item.link;
					str = str.replace(/_iid/g,iid);
					str = str.replace(/_val/g,val);
					rtnitem = $("<a/>").text(rtnitem).attr("href",str).text(rtnitem);
				}	
				//click事件
				if(item.hasOwnProperty("click")){
					$(line).children("td:eq(" + item.idx + ")").addClass('click').click(function(){item.click(iid,val);return false;});
					//rtnitem = $("<span/>").html(rtnitem).click(function(){item.click(iid,val);return false;});
				}
							
				if(item.hasOwnProperty("subtext")){
					var len = item.subtext;
					rtnitem = rtnitem.substr(0,len)+"......";
				}
				$(line).children("td:eq(" + item.idx + ")").html(rtnitem);
			});
		});
		////表格选中变色	
		//$(me.griddiv).find("tbody tr").click(function() { 
        //    $(me.griddiv).find("tbody tr").removeClass("selected");
        //    $(this).addClass("selected");
		//});
	},
    querycallback: function(){
    },	
	
    queryData: function(page) {
        var me = this;
		var where = datsrv.GetWhereFromDiv($(me.wherediv));
		//组合条件 ，页面条件+系统条件		
		var where = where.concat(me.addwhere);
		//去除最后一个andor
		if(where.length>0) where[where.length -1 ].andor='';
        datsrv.UIGetSimpleQuery(me.defid, me.fmtid, page, util.cookie("pagesize"), where, $(me.griddiv), 
			function() {
			if($( "#"+me.tablename +" .currpagenum").length>0){
				me.currpage = parseInt( $( "#"+me.tablename +" .currpagenum").text());
			}
			else{
				me.currpage = 1;
			}
			
			me.where = where;
			me.setqueryresult();
            me.querycallback();
        },me.order);
    },
	//查询FusionCharts
	queryGraph: function(id,pagesize,chartStyle,cWidth,cHeight) {
        var me = this;
		var where = datsrv.GetWhereFromDiv($(me.wherediv));
		//组合条件 ，页面条件+系统条件		
		var where = where.concat(me.addwhere);
		//去除最后一个andor
		if(where.length>0) where[where.length -1 ].andor='';
        datsrv.DatGetFChartsData(me.defid, me.fmtid, pagesize, where, 
			function(rtn) {
				var myChart = new FusionCharts("../../fc/Charts/"+chartStyle+".swf" , id, cWidth, cHeight, "0", "0"); 
				myChart.setTransparent(true);
				myChart.setDataXML(rtn);
				myChart.render(id);
			},
			function(rtn){
				//失败
			});
    },
	//打印
    printPdf: function(where) {
        var me = this;
		var where = datsrv.GetWhereFromDiv($(me.wherediv));
		//组合条件 ，页面条件+系统条件		
		var where = where.concat(me.addwhere);
		//去除最后一个andor
		if(where.length>0) where[where.length -1 ].andor='';
        datsrv.GetPrintPdf(me.defid + "_prt", "table_print", where);
    },
	
	exporttxt:function(where){
		this.gettxtfile(where);
	},
    //导出
    gettxtfile: function(where) {
        var me = this;
		var where = datsrv.GetWhereFromDiv($(me.wherediv));
		//组合条件 ，页面条件+系统条件		
		var where = where.concat(me.addwhere);
		//去除最后一个andor
		if(where.length>0) where[where.length -1 ].andor='';
        datsrv.GetTxtDownload(me.defid + "_prt", "Table_Text", where);
    },
    /*
    *删除数据
    */
	delRowDataCallback:function(iid){		
	},
    delRowData: function() {
        var me = this;
        if (me.rowid == -1) return;
        dataobj = [me.rowid];
        if (!confirm("确认删除此信息?")) return;
        datsrv.DatSaveSingleRow(me.defid, [], dataobj, function() {
            //刷新
			me.editNextRow();
			me.queryData(me.currpage, me.tablename);
			//回调
			me.delRowDataCallback(me.rowid);
        });
    },
    /*
    *增加数据
    */
	addRowDataCallback: function() {
    },
    addRowData: function() {
        var me = this;
        $(me.editlist).each(function(idx, item) {

            //if ($("#frm_" +me.formname+ item).attr("type") != "hidden") {
            //    $("#frm_" +me.formname+ item).val("");
            //}
        });
        me.rowid = -1;        
        me.addRowDataCallback();
    },
    
    /*
    *编辑数据
    */
	editLastRow:function(){
		var me = this;
		var maxidx = $("#"+me.tablename+" td input[iid]").length;
		var idx = $("#"+me.tablename+" td input[iid]").index($("#"+me.tablename+" td input[iid="+me.rowid+"]"));
			idx --;
			if(idx == -1 ) idx = maxidx -1;
			me.editRowData($($("#"+me.tablename+" td input[iid]")[idx]).attr("iid"));
	},
	editNextRow:function(){
		var me = this;
		var maxidx = $("#"+me.tablename+" td input[iid]").length;
		var idx = $("#"+me.tablename+" td input[iid]").index($("#"+me.tablename+" td input[iid="+me.rowid+"]"));
			idx ++;
			if(idx == maxidx ) idx =0 ;
			me.editRowData($($("#"+me.tablename+" td input[iid]")[idx]).attr("iid"));
	},
    editRowCallback: function(iid,obj) {
		console.info(obj);
    },	
    editRowData: function(iid) {
        var me = this;
        datsrv.DatGetJsonData(me.defid, 1, 1, [{ col: me.keyname, logic: '= ', val: iid, andor: ''}], function(rtn) {
            var obj = rtn.data[0][me.tablename].d[0];
            //设置编辑模式，默认为-1，新增模式
            me.rowid = obj.iid;
            $(me.editlist).each(function(idx, item) {
                var jsitem = item.toLowerCase();   
				//按照控件，填充值
				var control =  $("#frm_" +me.formname+ item)[0].tagName;
				if(control=="INPUT" ){
					var t = $("#frm_" +me.formname+ item).attr("type");
					if(t=="date" && obj[jsitem].indexOf("-")<0){
						$("#frm_" +me.formname+ item).val(obj[jsitem].substr(0,4)+'-'+obj[jsitem].substr(4,2)+'-'+obj[jsitem].substr(6,2));
					}
					else{
						$("#frm_" +me.formname+ item).val(obj[jsitem]);
					}
					//触发onchange
					$("#frm_" +me.formname+ item).change();
				}
				if(control=="TEXTAREA" ){
					var itemstr = obj[jsitem].replace(/&lt;br\/&gt;/g,"\n")
					$("#frm_" +me.formname+ item).val(itemstr);
				}
				if(control =="SPAN"){
					$($("#frm_" +me.formname+ item).children("select")[0]).val(obj[jsitem]);
					//触发onchange
					$($("#frm_" +me.formname+ item).children("select")[0]).change();	
				}
				if(control =="SELECT"){
					$("#frm_" +me.formname+ item).val(obj[jsitem]);
					//触发onchange
					$("#frm_" +me.formname+ item).change();				
				}
            });
            //回调
			me.editRowCallback(iid,obj); 
        });
    },
    /*
    *保存数据
    */
    beforesave: function() {
		return true;
    },
    saveRowDataCallback: function(iid) {

    },
    saveRowData: function(succ,fail) {
        var me = this;
        if(me.beforesave()==false) return;
        var dataobj = [];
        if (me.rowid == -1) {dataobj = [{ c0: '8'}];}	//新增模式
        else {
			//dataobj =[{}];
			
            //dataobj[0]['c0']='9';
            //dataobj[0]['key_'+me.keyname]=me.rowid;
			dataobj= [{ c0: '9', key_iid: me.rowid}];	//修改模式
        }

        $(me.editlist).each(function(idx, item) {
		
			var control =  $("#frm_" +me.formname+ item)[0].tagName;
			if(control =="SPAN"){
				dataobj[0][item] = $($("#frm_" +me.formname+ item).children("select")[0]).val();
			}
			else{
				if(control =="TEXTAREA"){
					var itemstr = $("#frm_" +me.formname+ item).val();					
					
					dataobj[0][item] = itemstr.replace(/\n/g,"<br/>");
				}
				else{
				dataobj[0][item] = $("#frm_" +me.formname+ item).val();
				}
			}
        });

        datsrv.DatSaveSingleRow(me.defid, dataobj, [], function(rtn) {            
			datsrv.msgbox("数据保存成功！");	
            if (me.rowid == -1) {
				//新增模式，保存成功，修改为修改模式			
                me.rowid = rtn.split("'")[1];
            }                       
            me.saveRowDataCallback(me.rowid);            
        });
    }
}
