var express = require('express');
var router = express.Router();
var lifestar = require("../lib/lifestar");
var logger = require("../lib/log").sqllog;
var _ = require("underscore");


/* GET level 1 page. */
router.get('/:m?', function (req, res) {
    var pageName = req.params.m;
    logger.info(pageName);

    var viewName = (pageName==undefined)?"homepage":pageName;

    if (!_.has(lifestar.resource.data,viewName)){
        viewName = "homepage";
    }

    var viewData = lifestar.resource.data[viewName];
    viewData["layout"] = lifestar.resource.data.layout;

    logger.info(viewData);
    res.render(viewName, viewData);

});




module.exports = router;
