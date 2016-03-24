var express = require('express');
var router = express.Router();
var lifestar = require("../lib/lifestar");
var logger = require("../lib/log").sqllog;
var _ = require("underscore");
var sha1 = require("sha1");

/* GET users listing. */
router.get('/login', function (req, res) {
    var url = req.query.u;
    var viewData = {errinfo: ""};
    viewData["layout"] = lifestar.resource.data.layout;
    viewData["url"] = (url == undefined) ? "" : url;
    res.render("login", viewData);
});


router.post('/login', function (req, res) {

    var rtnurl = req.body.url;
    var log = {
        account: req.body.account,
        passwd: sha1(req.body.pwd)
    }


    errback = function (err) {
        var viewData = {errinfo: err};
        viewData["layout"] = lifestar.resource.data.layout;
        viewData["url"] = rtnurl;
        res.render("login", viewData);
    }

    lifestar.Users.doPromise(lifestar.Users.queryData, log).then(
        function (rtn) {
            if (rtn.length == 1) {
                res.redirect((rtnurl == "") ? "/users/center" : rtnurl);
            }
            else {
                errback("登录信息失败,用户或密码错误.")
            }
        },
        function (err) {
            errback("系统验证登录信息失败,请稍后重试.")
        }
    );
});


router.get('/register', function (req, res) {
    var viewData = {errinfo: ""};
    viewData["layout"] = lifestar.resource.data.layout;
    res.render("register", viewData);
});

router.post('/register', function (req, res) {

    var user = lifestar.Users.newModelData();
    _.extend(user, {
        account: req.body.account,
        passwd: sha1(req.body.pwd),
        fullname: req.body.account.split("@")[0],
        role: 'member'
    });

    logger.info(user);

    var viewData = {errinfo: ""};
    viewData["layout"] = lifestar.resource.data.layout;


    errback = function (err) {
        logger.error(err);
        viewData.errinfo = err;
        res.render("register", viewData);
    }

    lifestar.Users.doPromise(lifestar.Users.queryData, {account: user.account}).then(
        function (qryrtn) {
            if (qryrtn.length > 0) {
                errback("注册失败:用户名已经存在!");
            }
            else {
                lifestar.Users.doPromise(lifestar.Users.insertData, user).then(
                    function () {
                        //succ
                        res.redirect("/");
                    },
                    function (err) {
                        errback("注册失败:" + err);
                    }
                );
            }
        },
        function (err) {
            errback("注册失败:" + err);
        }
    );


});

router.get("/center",function(req,res){

});

module.exports = router;
