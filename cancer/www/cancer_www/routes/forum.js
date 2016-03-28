var express = require('express');
var router = express.Router();
var lifestar = require("../lib/lifestar");
var logger = require("../lib/log").sqllog;
var _ = require("underscore");
var sha1 = require("sha1");


/* GET users listing. */
router.get('/index', function (req, res) {
    var url = req.query.u;

    var viewData = {forum:lifestar.ForumTopics.getTopics()};

    viewData["layout"] = lifestar.resource.data.session(req.session.user);
    res.render("forum/index", viewData);
});

router.get("/topic/:id",function(req,res){
    var url = req.query.u;
    var tid = req.params.id;
    var viewData = {forum:lifestar.ForumTopics.getTopics()};
    viewData["layout"] = lifestar.resource.data.session(req.session.user);
    res.render("forum/index", viewData);
});

module.exports = router;
