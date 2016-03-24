angular.module('ktwy.services')
.factory('service_sys', function() {
  // Might use a resource here that returns a JSON array

  // Some fake testing data
  var sys={
    pre_url:''//上一个网页
  };

  return {
    all:function()
    {
      return sys;
    },
    set_pre_url:function(url)
    {
      sys.pre_url=url;
    }
  };
});


/*
var IController={
  //原url
  preurl:'',
  //返回
  goback:function()
  {
    console.info("please override goback method!");
  },
  //跳转到
  goto:function()
  {
    console.info("please override goto method!");
  }
};
*/
