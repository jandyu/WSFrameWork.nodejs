angular.module('ktwy.services')
  .factory('NativePlugin', function($resource,$ionicActionSheet)
  {


    var Native_Plugin = {

      //1照片--------------------------------------------------------------------------------------------------------------
      PictureModel:{
        image_url:''
      },
      PictureDefaultOption:{
        quality: 50,
        allowEdit:true,
        //saveToPhotoAlbum: false,
        //targetWidth: 100,
        //targetHeight: 100,
        mediaType:0,
        destinationType:1,//DATA_URL : 0,FILE_URI : 1,NATIVE_URI : 2
        sourceType:0//PHOTOLIBRARY : 0,CAMERA : 1,SAVEDPHOTOALBUM : 2
      },

      GetPicture:function(succ,fail,option)
      {
        var me=this;

        var options = angular.extend({},me.PictureDefaultOption,option);

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
            if(index==0)
            {
              options.sourceType=1;
            }
            //照片库选择
            if(index==1)
            {
              options.sourceType=0;
            }

            me.getPictureReal(succ,fail,options);

            return true;
          },
          destructiveButtonClicked: function () {
            console.log('DESTRUCT');
            return true;
          }
        });
      },
      getPictureReal:function(succ,fail,option)
      {
        console.log(JSON.stringify(option));
        var me=this;
        navigator.camera.getPicture(function(imageData){


          if(imageData.indexOf("content://com.google.android.apps.photos.contentprovider")==0)
          {
              window.FilePath.resolveNativePath(imageData,function(rtn){
                imageData=rtn;
                me.getPictureRealCollback(option,imageData,succ);
              },function(rtn){
                console.log("convert uri error:"+rtn);
              })
          }
          else
          {
           me.getPictureRealCollback(option,imageData,succ);
          }

        }, function(message){
          fail(message);
        }, option);
      },
      getPictureRealCollback:function(option,imageData,succ)
      {
        var me=this;
        if(option.destinationType==0)
        {
          me.PictureModel.image_url="data:image/jpeg;base64," + imageData;
          console.log(me.PictureModel.image_url);
        }

        if(option.destinationType==1)
        {
          me.PictureModel.image_url=imageData
          console.log(me.PictureModel.image_url);
        }

        succ(imageData);
      },
      //2上传文件--------------------------------------------------------------------------------------------------------------
      FileTransDefaultOption:
      {
        mimeType:'image/jpeg',//image/jpeg,text/plain,multipart/form-data
        urlServer:'http://wy.zjy8.cn/resource.wsdat'
      },
      FileTrans:function(fileURL,succ,fail,option)
      {
        var me=this;
        var option_new = angular.extend({},me.FileTransDefaultOption,option);

        var options = new FileUploadOptions();
        options.fileKey="file";
        options.fileName=fileURL.substr(fileURL.lastIndexOf('/')+1);
        options.mimeType=option_new.mimeType;

        var headers={'headerParam':'headerValue'};

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
        var onsucc=function(rtn)
        {
          var ls_res=JSON.stringify(rtn.response);
          var ls_tmp="var tmp="+ls_res.substr(1,ls_res.length - 2)+";";
          eval(ls_tmp);
          /*
          var rg=RegExp(/^\//);//以/开头,则去掉
          if(rg.test(tmp.url))
          {
            tmp.url=tmp.url.substr(1);
          }*/
          succ(tmp.url);
        }

        ft.upload(fileURL, uri, onsucc, fail, options);
      },
      //photoview------------------------
      //PhotoViewer.show('http://my_site.com/my_image.jpg', 'Optional Title')
      PhotoView:function(url,title)
      {
        PhotoViewer.show(url, title)
      }
    };

    return Native_Plugin;
  });



