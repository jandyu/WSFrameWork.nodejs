/**
 * Created by jrain on 16/1/28.
 */

var mongodb = require("mongodb");
ObjectID = require('mongodb').ObjectID;
var connection = require('./connection');

var fs = require('fs');
var Promise = require('promise');



var BSON = mongodb.BSONPure;

var _ = require('underscore');

var logger = require("./log").sqllog;

var dbConfig = JSON.parse(fs.readFileSync("mongodb.json", "utf8"));

var dal = {
    collectionName:"",
    modelName:"",

    errorCallBack:function(err){
        //TODO:global error callback in class
        //if define errback ,do errback , else do class.errorCallback
    },
    newModelData:function()
    {
        var me = this;
        if (me.modelName!="") {
            return JSON.parse(fs.readFileSync('./lib/model/'+me.modelName + ".json", "utf8"));
        }
        return undefined;
    },

    max:function(query,field,callback,errback){
        var me = this;
        var options={limit:1, fields:{},sort:{}};
        options.fields[field] = 1;
        options.sort[field] = -1;
        this.queryDataJson(query,options,function(rtns) {

            logger.info("max :" + me.collectionName + "  query max:" + JSON.stringify(query) + ",return:" + JSON.stringify(rtns));
            if (rtns.length > 0) {
                callback(rtns[0][field]);
            }
            else{
                callback("");
            }
        },errback);
    },

    count:function(query,callback,errback){
        var me = this;
        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {

            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }
                collection.count(query,function(err, count){
                    logger.info("count :"+me.collectionName +"  count:"+ JSON.stringify(query) +",return:"+count );
                    callback({ ok: 1 ,count:count});
                });
            });
        });

    },


    /**
     * distinct
     * @param keyname
     * @param callback
     * @param errback
     */
    distinct:function(keyname,callback,errback){
        var me = this;
        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {

            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }

                collection.distinct(keyname, function(err, docs) {
                    if (err) {
                        logger.error('Error distinct from ' + me.collectionName + ': ' + err.message);
                        errback(err.message);
                        return;
                    }
                    logger.info("distinct :"+me.collectionName +" "+me.modelName + " "+keyname );
                    callback({ ok: 1 ,data:docs});
                });
            });
        });
    },
    /*
    delete
     */
    deleteData:function(query,callback,errback){
        var me = this;
        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {

            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }

                collection.remove(query, function(err, docs) {
                    if (err) {
                        logger.error('Error removing from ' + me.collectionName + ': ' + err.message);
                        errback(err.message);
                        return;
                    }
                    logger.info("delete :"+me.collectionName +" "+me.modelName+ JSON.stringify(query));
                    callback({ ok: 1 });
                });
            });
        });
    },
    deleteDataByID:function(id,callback,errback){
        //var spec = {'_id': new BSON.ObjectID(id)};
        var spec = {'_id': id};
        this.deleteData(spec,callback,errback);
    },
    updateData:function(id,data,callback,errback) {
        var spec = {'_id': id};
        this.updateDataWhere(spec,data,callback,errback);
    },

    updateDataWhere:function(query,data,callback,errback){
        //var spec = {'_id': new BSON.ObjectID(id)};

        var me = this;
        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {

            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }

                var mdata = {$set:data};
                var opt = {multi:true,upsert:true };
                collection.update(query, mdata, opt, function(err, docs) {
                    logger.info("udpate "+me.collectionName +" " + me.modelName + " where:"+ JSON.stringify(query) + 'rtn:'+ JSON.stringify(docs));
                    callback({ ok: 1 ,data:docs});
                });
            });
        });
    },
    transformCollection:function(outputType,data){
        return data;
    },
    /*
    split page query
     */
    queryPageData:function(query,page,pagesize,callback,errback){
        var p = page>0?1:page;
        var ps = pagesize>0?1:pagesize;
        var options = {limit:pagesize,skip:ps * (p - 1)};

        this.queryDataJson(query,options,callback,errback);
    },
    queryData:function(query,callback,errback) {
        var options = {limit:999};  //defualt max return 999
        this.queryDataJson(query,options,callback,errback);
    },
    queryDataByID:function(id,callback,errback){
        //var spec = {'_id': new BSON.ObjectID(id)};
        var spec = {'_id': id};
        var options = {limit:1,fields:{password:0}};
        this.queryDataJson(spec,options,callback,errback);
    },
    queryDataJson:function(query,options,callback,errback) {
        this.queryDataWithOptions(query,options,'json',callback,errback);
    },

    queryDataWithOptions:function(query,options,outputType,callback,errback){
        //TODO: need helper func for options{sort , limit, skip ,fields}
        var me = this;

        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {

            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }

                collection.find(query, options, function(err, cursor) {
                    if (err) {
                        logger.error('Error finding document(s): ' + err.message);
                        errback(err.message);
                        return;
                    }

                    cursor.toArray(function(err, docs){
                        if (err) {
                            logger.error('Error getting database cursor as array: ' + err.message);
                            errback(err.message);
                            return;
                        }
                        var result = [];
                        docs.forEach(function(doc) {
                            result.push(doc);
                        });
                        logger.info("query :"+ me.collectionName + ":"+ JSON.stringify(query) +",options:"+JSON.stringify(options)+ " ,count:" + result.length);
                        //logger.info(result);
                        callback(me.transformCollection(outputType, result));
                    });
                });
            });

        });
    },
    insertNewData:function(callback,errback){
        var me = this;
        var newdata = me.newModelData();
        me.insertData(newdata,callback,errback);
    },
    /*
    insert
     */
    insertData:function(data,callback,errback){

        if(hasOwnProp(data,"_id")){
            data._id = uuid.v4().replace(/-/g,"");
        }else {
            data["_id"] = uuid.v4().replace(/-/g,"");
        }
        var me = this;
        connection.connect(dbConfig.db + '/' + dbConfig.database, function (err, db) {
            if (err) {
                logger.error('Db open error: ' + err.message);
                errback(err.message);
                return;
            }

            db.collection(me.collectionName, function(err, collection) {
                if (err) {
                    logger.error('Error getting collection ' + me.collectionName + ': ' + err.message);
                    errback(err.message);
                    return;
                }


                collection.insert(
                    data,
                    function(err, docs) {
                        if (err) {
                            logger.error('Error inserting into collection ' + me.collectionName + ': ' + err.message);
                            errback(err.message);
                            return;
                        }
                        logger.info("insert:"+me.collectionName +" " + me.modelName+":" +docs);
                        //db.close();
                        callback({ok:1,data:docs});
                    }
                );
            });
        });
    }
}

/*
pilatus models
 */

var lifestar = {
    dal:dal,

    resource: _.extend(dal, {
        "collectionName":"Resource","modelName":"resource",
        data:{

                homepage:{
                    logo:{img:"/images/logo.png",link:''},
                    menu:[
                        {title:"首页",link:"/index"},
                        {title:"为什么星生命",link:"/index"},
                        {title:"我的医疗条件如何选择",link:"/index"},
                        {title:"向别人学习",link:"/index"},
                        {title:"支持服务",link:"/index"},
                        {title:"星生命故事",link:"/index"},
                        {title:"登录",link:"/index"},
                        {title:"用户信息",link:"/index"}
                    ],
                    slide:[
                        {img:"/images/home/slide1.jpg",link:""},
                        {img:"/images/home/slide2.jpg",link:""},
                        {img:"/images/home/slide3.jpg",link:""},
                        {img:"/images/home/slide4.jpg",link:""},
                        {img:"/images/home/slide5.jpg",link:""},
                        {img:"/images/home/slide6.jpg",link:""}
                    ],
                    footer:[
                        {title:"关于星生命",
                            link:[
                                {title:'我们是谁',link:''},
                                {title:'新闻',link:''},
                                {title:'广告',link:''},
                                {title:'职业',link:''},
                                {title:'赞助商',link:''},
                                {title:'加盟计划',link:''},
                            ]
                        },
                        {title:"帮助",
                            link:[
                                {title:'网站导航',link:''},
                                {title:'系统要求',link:''}
                            ]
                        },
                        {title:"联系方式",
                            link:[
                                {title:'找到一个星生命的会议',link:''},
                                {title:'社区',link:''},
                                {title:'联系我们',link:''},
                                {title:'微信二维码',link:''}
                            ]
                        }
                    ],
                    policy:{
                        link:[
                            {title:"International Sites",link:""},
                            {title:"Privacy Policy",link:""},
                            {title:"Notice of Privacy Practices",link:""},
                            {title:"Terms and Conditions",link:""}
                        ],
                        desc:"© 2016 星生命 International, Inc. All rights reserved."
                    },
                    succ:[
                        {head:"/images/home/head1.jpg",desc:"琳达是一位癌症幸存者…. 也许你忧伤的坐着, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                        {head:"/images/home/head2.jpg",desc:"珍妮是一个癌症幸存者 …. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                        {head:"/images/home/head3.jpg",desc:"安妮是一个癌症幸存者 …. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                    ]
                },
                whylf:{
                    logo:{img:"/images/logo.png",link:''},
                    menu:[
                        {title:"首页",link:"/index"},
                        {title:"为什么星生命",link:"/index"},
                        {title:"我的医疗条件如何选择",link:"/index"},
                        {title:"向别人学习",link:"/index"},
                        {title:"支持服务",link:"/index"},
                        {title:"星生命故事",link:"/index"},
                        {title:"登录",link:"/index"},
                        {title:"用户信息",link:"/index"}
                    ],
                    slide:[
                        {img:"/images/home/slide1.jpg",link:""},
                        {img:"/images/home/slide2.jpg",link:""},
                        {img:"/images/home/slide3.jpg",link:""},
                        {img:"/images/home/slide4.jpg",link:""},
                        {img:"/images/home/slide5.jpg",link:""},
                        {img:"/images/home/slide6.jpg",link:""}
                    ],
                    footer:[
                        {title:"关于星生命",
                            link:[
                                {title:'我们是谁',link:''},
                                {title:'新闻',link:''},
                                {title:'广告',link:''},
                                {title:'职业',link:''},
                                {title:'赞助商',link:''},
                                {title:'加盟计划',link:''},
                            ]
                        },
                        {title:"帮助",
                            link:[
                                {title:'网站导航',link:''},
                                {title:'系统要求',link:''}
                            ]
                        },
                        {title:"联系方式",
                            link:[
                                {title:'找到一个星生命的会议',link:''},
                                {title:'社区',link:''},
                                {title:'联系我们',link:''},
                                {title:'微信二维码',link:''}
                            ]
                        }
                    ],
                    policy:{
                        link:[
                            {title:"International Sites",link:""},
                            {title:"Privacy Policy",link:""},
                            {title:"Notice of Privacy Practices",link:""},
                            {title:"Terms and Conditions",link:""}
                        ],
                        desc:"© 2016 星生命 International, Inc. All rights reserved."
                    },
                    succ:[
                        {head:"/images/home/head1.jpg",desc:"琳达是一位癌症幸存者…. 也许你忧伤的坐着, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                        {head:"/images/home/head2.jpg",desc:"珍妮是一个癌症幸存者 …. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                        {head:"/images/home/head3.jpg",desc:"安妮是一个癌症幸存者 …. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."},
                    ]
                }

            }
        })
};

module.exports = lifestar;