angular.module('ktwy.services')
  .factory('NativePlugin', function ($resource, $ionicActionSheet) {


    var Native_Plugin = {

      //1照片--------------------------------------------------------------------------------------------------------------
      PictureModel: {
        image_url: ''
      },
      PictureDefaultOption: {
        quality: 60,
        allowEdit: false,
        //saveToPhotoAlbum: false,
        targetWidth: 1920,
        targetHeight: 1920,
        // mediaType:0,
        destinationType: 1//DATA_URL : 0,FILE_URI : 1,NATIVE_URI : 2
        //sourceType:0//PHOTOLIBRARY : 0,CAMERA : 1,SAVEDPHOTOALBUM : 2
      },

      GetPicture: function (succ, fail, option) {
        var me = this;

        var options = angular.extend({}, me.PictureDefaultOption, option);
        console.info("------------picture option----------")
        console.info(JSON.stringify(options));

        $ionicActionSheet.show({
          titleText: '请选择',
          buttons: [
            {
              text: '拍照'
            },
            {
              text: '照片库'
            }
          ],
          //destructiveText: 'Delete',
          cancelText: '取消',
          cancel: function () {
            console.log('CANCELLED');
          },
          buttonClicked: function (index) {
            console.log('BUTTON CLICKED', index);
            //拍照
            if (index == 0) {
              options.sourceType = 1;
            }
            //照片库选择
            if (index == 1) {
              options.sourceType = 0;
            }

            me.getPictureReal(succ, fail, options);

            return true;
          },
          destructiveButtonClicked: function () {
            console.log('DESTRUCT');
            return true;
          }
        });
      },
      getPictureReal: function (succ, fail, option) {
        console.log(JSON.stringify(option));
        var me = this;
        navigator.camera.getPicture(function (imageData) {


          //if(imageData.indexOf("content://com.google.android.apps.photos.contentprovider")==0)
          if (imageData.indexOf("content://") == 0) {
            window.FilePath.resolveNativePath(imageData, function (rtn) {
              imageData = rtn;
              me.getPictureRealCollback(option, imageData, succ);
            }, function (rtn) {
              console.log("convert uri error:" + rtn);
            })
          }
          else {

            me.getPictureRealCollback(option, imageData, succ);
          }

        }, function (message) {
          fail(message);
        }, option);
      },
      getPictureRealCollback: function (option, imageData, succ) {
        var me = this;
        if (option.destinationType == 0) {
          me.PictureModel.image_url = "data:image/jpeg;base64," + imageData;
          console.log(me.PictureModel.image_url);
        }

        if (option.destinationType == 1) {
          me.PictureModel.image_url = imageData
          console.log(me.PictureModel.image_url);
        }

        succ(imageData);
      },
      //2上传文件--------------------------------------------------------------------------------------------------------------
      FileTransDefaultOption: {
        mimeType: 'image/jpeg',//image/jpeg,text/plain,multipart/form-data
        urlServer: 'http://wy.zjy8.cn/resource.wsdat'
      },
      FileTrans: function (fileURL, succ, fail, option) {
        var me = this;
        var option_new = angular.extend({}, me.FileTransDefaultOption, option);

        var options = new FileUploadOptions();
        options.fileKey = "file";

        //options.fileName=fileURL.substr(fileURL.lastIndexOf('/')+1);
        //options.fileName=fileURL.substr(fileURL.lastIndexOf('/')+1);
        var fname = fileURL.substr(fileURL.lastIndexOf('/') + 1);
        if (fname.indexOf('?') != -1) {
          fname = fname.split('?')[0];
        }
        options.fileName = fname;
        options.mimeType = option_new.mimeType;

        var headers = {'headerParam': 'headerValue'};

        options.headers = headers;

        var ft = new FileTransfer();
        /*
         ft.onprogress = function(progressEvent) {
         if (progressEvent.lengthComputable) {
         loadingStatus.setPercentage(progressEvent.loaded / progressEvent.total);
         } else {
         loadingStatus.increment();
         }
         };
         */
        var uri = encodeURI(option_new.urlServer);
        var onsucc = function (rtn) {

          console.info("-------trans file callback--------");
          console.info(JSON.stringify(rtn));
          var ls_res = JSON.stringify(rtn.response);
          var ls_tmp = "var tmp=" + ls_res.substr(1, ls_res.length - 2) + ";";
          eval(ls_tmp);
          /*
           var rg=RegExp(/^\//);//以/开头,则去掉
           if(rg.test(tmp.url))
           {
           tmp.url=tmp.url.substr(1);
           }*/
          succ(tmp.url);
        }

        console.info("-----trans file options-------");
        console.info(JSON.stringify(options));


        ft.upload(fileURL, uri, onsucc, fail, options);
      },
      //3图片浏览photoview-----------------------------------------------------------------------------------------------------
      //PhotoViewer.show('http://my_site.com/my_image.jpg', 'Optional Title')
      PhotoView: function (url, title) {
        PhotoViewer.show(url, title)
      },
      //多图片浏览
      //urls:图片地址数组
      //idx:打开之后默认显示数组中的第几个图片;
      //cls:浏览图片窗口的类,例如:.repair_detail.pswp
      PhotoViews: function (urls, idx, cls) {
        var pswpElement = document.querySelectorAll(cls)[0];

        // build items array
        var items = [];

        $.each(urls, function (k, v) {

          var imginfo = jsondal.GetImageInfo(v.url);

          var url = {
            src: imginfo.orgurl,
            w: imginfo.w,
            h: imginfo.h
          };
          console.info(url);
          items.push(url);
        });

        // define options (if needed)
        var options = {
          index: idx, // start at first slide
          history: false,
          shareEl: false,
          tapToClose: true,
          fullscreenEl: false
        };

        // Initializes and opens PhotoSwipe
        var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
        gallery.init();

      },
      //4定位信息--------------------------------------------------------------------------------------------------------------
      LocationDefaultOption: {
        timeout: 30000,//30秒内读取不到位置信息,则报超时错误
        enableHighAccuracy: true,//高精度(启用卫星定位,默认是网络定位)
        maximumAge: 3000,//如果是watch,则3秒钟触发一次
        coorType:'bd09ll'//GCJ02\BD09LL
      },
      //获取当前位置信息,超时或者出错都调用err函数,通过error.code判断
      GetCurrLocation: function (succ, err, option) {
        var me = this;
        var options = angular.extend({}, me.LocationDefaultOption, option);

        navigator.geolocation.getCurrentPosition(
          function (position) {
          console.info('-----------GetCurrLocation succ info------------------');
          console.info(position);
          succ(position);
        }, function (error) {
            console.info('-----------GetCurrLocation error info------------------');
            console.info(error);
            err(error);
          }, options);

      },
      //位置信息变化触发的事件
      //
      WatchLocation: function (succ, err, option) {
        var me = this;
        var options = angular.extend({}, me.LocationDefaultOption, option);

        var watchID = navigator.geolocation.watchPosition(
          function (position) {
          console.info('-----------watchlocation succ info------------------');
          console.info(position);
          succ(position);
        }, function (error) {
            console.info('-----------watchlocation error info------------------');
            console.info(error);
            err(error);
          }, options);
        me.WatchID = watchID;
      },
      //如果使用WatchLocation,不用时,要调用此函数关闭监听
      WatchLocationClear: function () {
        var me = this;
        navigator.geolocation.clearWatch(me.WatchID);
      },
      //5扫码--------------------------------------------------------------------------------------------------------------
      BacodeSacnDefaultOption: {
        "preferFrontCamera": true, // iOS and Android
        "showFlipCameraButton": true, // iOS and Android
        "prompt": "请将二维码调整到方框内", // supported on Android only
        "formats": "QR_CODE,PDF_417", // default: all but PDF_417 and RSS_EXPANDED
        "orientation": "landscape" // Android only (portrait|landscape), default unset so it rotates with the device
      },
      BacodeSacn: function (succ, err, option) {
        var me = this;

        var options = angular.extend({}, me.BacodeSacnDefaultOption, option);

        cordova.plugins.barcodeScanner.scan(
          function (result) {
            console.info("-------scan succ result----------");
            console.info(result);
            succ(result);
          },
          function (error) {
            console.info("-------scan succ result----------");
            console.info(error)
            err(error);
          }, options
        );

      }
    };

    return Native_Plugin;
  });



