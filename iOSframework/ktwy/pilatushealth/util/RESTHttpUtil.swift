//
//  RESTHttpUtil.swift
//  ktwy
//
//  Created by jrain on 15/12/22.
//  Copyright © 2015年 cn.youjish. All rights reserved.
//

import Foundation
import UIKit

@objc class RESTHttpUtil: NSObject {
    
    static func getJsonValueFromString(strjson:String,jsonkey  key :String)->String?
    {
        let str = strjson.stringByReplacingOccurrencesOfString("'", withString: "\"")
        let jsonData = str.dataUsingEncoding(NSUTF8StringEncoding)
        do
        {
            let jsdic = try NSJSONSerialization.JSONObjectWithData(jsonData!, options: .MutableContainers)
            return jsdic.objectForKey(key) as? String
        }
        catch let error as NSError{
            NSLog("json value error:\(error.description)")
            return nil
        }
    
    }
    
    //==post====================================================
    static func postSyncRequest(stringUrl url:String, postContent content:String)->String{
        let nsurl = NSURL(string:url)
        return postSyncRequest(nsUrl:nsurl!, postContent: content)
    }
    static func postSyncRequest(nsUrl url:NSURL, postContent content:String)->String{
        let received = postSyncRequestNSData(nsUrl: url, postContent: content);
        let str = NSString(data: received, encoding: NSUTF8StringEncoding) as! String
        return str
    }
    static func postSyncRequestNSData(nsUrl url:NSURL, postContent content:String)->NSData{

        var received = NSData()
        //创建请求
        let nsurl = url //NSURL(string: url)
        let request = NSMutableURLRequest(URL: nsurl, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: CONST_HTTP_ACCESS_TIMEOUT)
        request.HTTPMethod = "POST"
        let data = content.dataUsingEncoding(NSUTF8StringEncoding)
        request.HTTPBody = data
    
        //连接服务器
        do{
        
            received = try NSURLConnection.sendSynchronousRequest(request, returningResponse: nil)

        }catch let error as NSError{
            NSLog("postSyncRequest error. \n\nhttp post:\(url)\n\ncontent:\(content)\n\nErrro:\(error.localizedDescription)")
        }
        
        NSLog("postSyncRequest%@","\n\nhttp post:\(url)\n\ncontent:\(content)\n\nrecv:\(received)")
        return received
    }
    //==get===========================================================
    static func getSyncRequest(stringUrl url:String)->String{
        let nsurl = NSURL(string: url)
        return getSyncRequest(nsUrl: nsurl!)
    }
    static func getSyncRequest(nsUrl url:NSURL)->String{
        let received = getSyncRequestNSData(nsUrl: url)
        let str = NSString(data: received, encoding: NSUTF8StringEncoding) as! String
        return str
    }
    static func getSyncRequestNSData(nsUrl url:NSURL)->NSData{
        var received = NSData()
        //创建请求
        let nsurl = url //NSURL(string: url)
        let request = NSMutableURLRequest(URL: nsurl, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: CONST_HTTP_ACCESS_TIMEOUT)
        request.HTTPMethod = "GET"
        //let data = content.dataUsingEncoding(NSUTF8StringEncoding)
        //request.HTTPBody = data
        
        //连接服务器
        do{
            
            received = try NSURLConnection.sendSynchronousRequest(request, returningResponse: nil)
            
            
        }catch let error as NSError{
            NSLog("getSyncRequest error. \n\nhttp post:\(url)\n\nErrro:\(error.localizedDescription)")
        }
        NSLog("getSyncRequest%@","\n\nhttp post:\(url)\n\nrecv:\(received)")
        return received
    }
    //==img================================================================
    static func postImgSyncRequest(stringUrl url:String, postImage img:UIImage) ->String
    {
        let nsurl = NSURL(string: url)
        return postImgSyncRequest(nsUrl: nsurl!, postImage: img)
    }
    static func postImgSyncRequest(nsUrl url:NSURL, postImage img:UIImage) ->String{
        let received = postImgSyncRequestNSData(nsUrl: url, postImage: img)
        let str = NSString(data: received, encoding: NSUTF8StringEncoding) as! String
        return str
    }
    
    static func postImgSyncRequestNSData(nsUrl url:NSURL, postImage img:UIImage) ->NSData
    {
        var received = NSData()
        
        //声明myRequestData，http payload
        let myRequestData = NSMutableData()
        myRequestData.appendData("\(CONST_FORM_BOUNDARY_START)\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        myRequestData.appendData("Content-Disposition: form-data; name=\"pic\"; filename=\"iosjpeg.jpg\"\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        myRequestData.appendData("Content-Type: image/jpeg\r\n\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        //得到图片的data,JPEG COMPRESS
        myRequestData.appendData(UIImageJPEGRepresentation(img,CGFloat.init(CONST_COMPRESSION_QUALITY))!)
        myRequestData.appendData("\r\n\(CONST_FORM_BOUNDARY_END)".dataUsingEncoding(NSUTF8StringEncoding)!)

        //根据url初始化request
        let request = NSMutableURLRequest(URL: url, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: CONST_HTTP_ACCESS_TIMEOUT)

        //设置HTTPHeader中Content-Type的值
        request.setValue("multipart/form-data; boundary=\(CONST_FORM_BOUNDARY_TAG)", forHTTPHeaderField: "Content-Type")
        //设置Content-Length
        request.setValue("\(myRequestData.length)", forHTTPHeaderField: "Content-Length")
        //设置http body
        request.HTTPBody = myRequestData
        //http method
        request.HTTPMethod = "POST"

        do
        {
            received =  try NSURLConnection.sendSynchronousRequest(request, returningResponse: nil)
            
        }catch let error as NSError{
            NSLog("postImgSyncRequest error. \n\nhttp post:\(url)\n\nErrro:\(error.localizedDescription)")
        }

        NSLog("postImgSyncRequest post:\(url) \nrecv:\(received)")
        
        return received
    }
    
    static func compressImage(image:UIImage,maxWidthOrHeight maxlength:CGFloat)->UIImage{
        NSLog("img compress ");
        
        //let image = info["UIImagePickerControllerOriginalImage"] as! UIImage
        //let image = info["UIImagePickerControllerEditedImage"] as! UIImage
        
        let imageData = UIImageJPEGRepresentation(image, 0.5)
        // 获取沙盒目录
        //let fullpath = NSHomeDirectory() + "/Library/Caches/currentImage.png"
        // 将图片写入文件
        //imageData?.writeToFile(fullpath, atomically: false)

        var savedImage = UIImage(data: imageData!)
        
        //UIImage *savedImage = [[UIImage alloc] initWithContentsOfFile:fullPath];
        var rect = savedImage!.size
        
        let r = savedImage!.size.width / savedImage!.size.height;
        
        //返回图片尺寸
        
        if (r > 1 && savedImage!.size.width > maxlength) {
            rect = CGSizeMake(maxlength, savedImage!.size.height * maxlength / savedImage!.size.width  );
        }
        if (r < 1 && savedImage!.size.height > maxlength) {
            rect = CGSizeMake(savedImage!.size.width * maxlength / savedImage!.size.height,maxlength  );
        }
        savedImage = self.reSizeImage(savedImage!, destSize: rect)
        
        return savedImage!
    }
    
    static func reSizeImage(image:UIImage,destSize toSize:CGSize)->UIImage{
        //保持宽高比例
        let r = image.size.width / image.size.height;
        let h = toSize.width / r;
        
        UIGraphicsBeginImageContext(CGSizeMake(toSize.width, h));
        
        image.drawInRect(CGRectMake(0, 0, toSize.width, h))
        let reSizeImage = UIGraphicsGetImageFromCurrentImageContext();
    
        UIGraphicsEndImageContext()
        
        return reSizeImage
    }
    
}
