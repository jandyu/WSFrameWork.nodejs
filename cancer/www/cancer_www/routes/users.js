var express = require('express');
var router = express.Router();
var lifestar = require("../lib/lifestar");
var logger = require("../lib/log").sqllog;
var _ = require("underscore");

/* GET users listing. */
router.get('/login', function(req, res) {
  var viewData = {errinfo:""};
  viewData["layout"] = lifestar.resource.data.layout;
  res.render("login",viewData);
});


router.post('/login', function(req, res) {
  var acc = req.body.account;
  var pwd = req.body.pwd;
  var viewData = {errinfo:acc + pwd};
  viewData["layout"] = lifestar.resource.data.layout;
  res.render("login",viewData);
});



router.get('/register', function(req, res) {
  var viewData = {errinfo:""};
  viewData["layout"] = lifestar.resource.data.layout;
  res.render("register",viewData);
});

module.exports = router;
