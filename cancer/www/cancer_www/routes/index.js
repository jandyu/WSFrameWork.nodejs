var express = require('express');
var router = express.Router();
var lifestar = require("../lib/lifestar");
var logger = require("../lib/log").sqllog;

/* GET home page. */
router.get('/', function (req, res) {
    logger.info(lifestar.resource.data.homepage);
    res.render('index', lifestar.resource.data.homepage);
});

module.exports = router;
