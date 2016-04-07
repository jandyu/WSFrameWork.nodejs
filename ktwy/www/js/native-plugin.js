/**
 * Created by jrain on 16/4/6.
 */

//angular.module('Native-Plugin', ['ngResource'])
angular.module('ktwy.services')
  .factory('NativePlugin', function($resource)
  {


    var Native_Plugin = {

      /*
      PushMessageDefualtOption:{
        android: {
          senderID: "348237944030"
        },
        ios: {
          alert: "true",
          badge: true,
          sound: 'false'
        },
        windows: {}
      },
      PushMessageInactiveCallback:function(data)
      {
        console.log(data);
      },

      PushMessageInit:function(option,registerCallback,inactiveCallback,activeCallback){
        var me = this;
        var options = angular.extend({},me.PushMessageDefualtOption,option);
        //init push

        var push = PushNotification.init(options);

        push.on('registration', function(data) {
          console.log(JSON.stringify(data));
          registerCallback(data);
        });

        push.on('notification', function(data) {
          console.log(JSON.stringify(data));
          activeCallback(data);
        });

      },


      //{"card_number":"4111111111111111","card_type":"Visa","redacted_card_number":"4111 1111 1111 1111","expiry_month":11,"expiry_year":2018,"cvv":"123"}
      CardIORead:function(succ,error)
      {
        CardIO.scan({
            "expiry": true,
            "cvv": true,
            "zip": false,
            "suppressManual": false,
            "suppressConfirm": false,
            "hideLogo": true
          },
          succ,
          error
        );

        //CardIO.canScan(callback3);
      },


      BrainTreeDefaultOption:{
        tokenUrl : "http://192.168.1.55:3000/pay/client_token",
        transactionUrl:"http://192.168.1.55:3000/pay/checkout"
        //{payment_method_nonce:'',amount:1,srvid:'',type:'',userid:''}
      },
      BrainTreeToken:"",
      BraintreeInit:function(callback)
      {
        var me=this;
        $resource(me.BrainTreeDefaultOption.tokenUrl).get(function(data){
          console.info(data);
          if(data.ok=="1")
          {
            me.BrainTreeToken=data.token;

            if(callback!=undefined && callback!=null)
            {
              callback(data.token);
            }
          }
          else
          {
            me.BrainTreeToken="";
          }


        },function(rtn){});
      },

      //{"card_number":"4111111111111111","card_type":"Visa","redacted_card_number":"4111 1111 1111 1111","expiry_month":11,"expiry_year":2018,"cvv":"123"}
      BraintreePay:function(option,cardinfo,callback){
        var me = this;
        var options = angular.extend({},me.BrainTreeDefaultOption,option);
        //get token
        //get nonce

        if(me.BrainTreeToken=="")
        {
          me.BraintreeInit(function(token){
            me.braintreePostServer(token,cardinfo,callback,me.BrainTreeDefaultOption.transactionUrl);
          })
        }
        else {
          me.braintreePostServer(me.BrainTreeToken,cardinfo,callback,me.BrainTreeDefaultOption.transactionUrl);
        }
      },


      braintreePostServer:function(token,cardinfo,callback,transactionUrl)
      {
        var client = new braintree.api.Client({clientToken:token});
        var expiry=cardinfo.expiry_month+'/'+cardinfo.expiry_year.toString().substr(2,2);
        client.tokenizeCard({
          number: cardinfo.card_number,
          expirationDate: expiry
        }, function (err, nonce) {
          //取到nonce
          console.log(nonce);

          var payInfo={payment_method_nonce:nonce,amount:1,srvid:'',type:'',userid:''};
          //发送到服务器
          var pays=$resource(transactionUrl);
          pays.save(payInfo,function(data){
            //付款成功
            console.log(JSON.stringify(data));
            if(callback!=undefined && callback!=null) {
              callback(data);
            }
          });


        });
      },
      */
      PictureModel:{
        image_url:''
      },
      PictureDefaultOption:{
        quality: 50,
        destinationType: Camera.DestinationType.DATA_URL//DATA_URL,FILE_URI,
      },

      GetPicture:function(succ,fail,option)
      {
        var me=this;
        var options = angular.extend({},me.PictureDefaultOption,option);
        navigator.camera.getPicture(function(imageData){
          if(options.destinationType==Camera.DestinationType.DATA_URL)
          {
            me.PictureModel.image_url="data:image/jpeg;base64," + imageData;
          }

          if(options.destinationType==Camera.DestinationType.FILE_URI)
          {
            me.PictureModel.image_url=imageData
          }

          succ(imageData);
        }, function(message){

        }, options);
      }
      /*
      //1-----------------
      navigator.camera.getPicture(onSuccess, onFail, { quality: 50,
      destinationType: Camera.DestinationType.DATA_URL
    });

    function onSuccess(imageData) {
      var image = document.getElementById('myImage');
      image.src = "data:image/jpeg;base64," + imageData;
    }

    function onFail(message) {
      alert('Failed because: ' + message);
    }

       //2-----------------
       navigator.camera.getPicture(onSuccess, onFail, { quality: 50,
       destinationType: Camera.DestinationType.FILE_URI });

       function onSuccess(imageURI) {
       var image = document.getElementById('myImage');
       image.src = imageURI;
       }

       function onFail(message) {
       alert('Failed because: ' + message);
       }
    */
    }

    return Native_Plugin;
  });



