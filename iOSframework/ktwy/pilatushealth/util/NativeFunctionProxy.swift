//
//  NativeFunctionProxy.swift
//  navt
//
//  Created by jrain on 16/1/4.
//  Copyright © 2016年 jrain. All rights reserved.
//
//  调用原生系统功能 ，从hybridPage.js调用

import Foundation
import UIKit
class NativeFunctionProxy:NSObject,ReadBarcodeViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate{
    
    var webViewController:AccViewController!
    var functionArgument:String
    
    init(webViewController webcontroller:AccViewController) {
        webViewController = webcontroller
        functionArgument="";
    }
    
    
    /*
    执行反射的class.func()
    className = dic["className"]?.description,
    functionName = dic["functionName"]?.description {
    if let cls = NSClassFromString(NSBundle.mainBundle().objectForInfoDictionaryKey("CFBundleName")!.description + "." + className) as? NSObject.Type{
    let obj = cls.init()
    let functionSelector = Selector(functionName)
    if obj.respondsToSelector(functionSelector) {
    obj.performSelector(functionSelector)
    } else {
    print("方法未找到！")
    }
    } else {
    print("类未找到！")
    }
    */
    
    func ExecuteFunctionByJsMessage(jsMessage msg:Dictionary<String,String>){
        //execute
        let cmd   = msg["func"]! as String
        let param = msg["arg"]! as String
        
        NSLog("cmd:\(cmd)")
        
        //参数保留到functioinArgument中，在accviewController中使用
        functionArgument = param
        //self.performSelector(Selector(cmd), withObject: nil)
        self.performSelector(Selector(cmd + ":"), withObject: param)
        
    }
    
    func functionOfConsoleLog(info:String){
        print("console.log:\(info)")
        
    }
    
    func functionOfRightButtonList(btnConfig:String){
        //javascript function rightButtonDo()
        //argument:'rightbtn:{"title":"+",img:"btn.png"}'
        
        if let rightbtn = RESTHttpUtil.getJsonValueFromString(btnConfig, jsonkey: "rightbtn"){
            let btnTitle = RESTHttpUtil.getJsonValueFromString(rightbtn, jsonkey: "title")
            let funcName = "functionOfRightButtonDo"
            //var btn:UIBarButtonItem
            if let btnImg = RESTHttpUtil.getJsonValueFromString(rightbtn, jsonkey: "img"){
                //img button
                let img = UIImage(data: NSData(contentsOfURL: NSURL(string: btnImg)!)!,scale:1.0)
                let cbtn = UIButton(frame: CGRectMake(0,0,img!.size.width,img!.size.height))
                cbtn.setImage(img, forState: .Normal)
                
                cbtn.addTarget(self, action: Selector(funcName), forControlEvents: .TouchUpInside)
                
                let btn = BBBadgeBarButtonItem(customUIButton: cbtn)
                btn.badgeOriginX = 20
                btn.badgeOriginY = -5
                btn.badgeValue = "1"
                
                webViewController.navigationItem.setRightBarButtonItem(btn, animated: true)
            }
            else
            {
                
                //let btn = UIBarButtonItem(title: btnTitle, style: .Plain, target: self, action: Selector(fName))
                let btn = BBBadgeBarButtonItem(title: btnTitle, style: .Plain, target: self, action: Selector(funcName))
                //btn.badgeOriginX = 20
                //btn.badgeOriginY = -5
                //btn.badgeValue = "1"
                webViewController.navigationItem.setRightBarButtonItem(btn, animated: true)
            }
            
            //
            
        }
        
    }
    
    
    //    刷新页面
    func functionOfReloadPage(){
        self.webViewController.webbrower.reload()
        
//        if let result = functionOfExecJavaScript("ngsjReloadpage()"){
//            self.functionArgument = result
//            //self.functionOfRightButtonList(self.functionArgument)
//            
//        }
    }
    
    //    右上按钮
    func functionOfRightButtonDo(){
        if let result = functionOfExecJavaScript("ngsjRightButtonDo()"){
            self.functionArgument = result
            //self.functionOfRightButtonList(self.functionArgument)
            
        }
    }
    
    //webview加载完成
    func functionOfNgsjLoad(){

        if let result = functionOfExecJavaScript("ngsjLoad()"){
            self.functionArgument = result
            self.functionOfRightButtonList(self.functionArgument)
            
        }
    }
    
    //webview 显示（包括pop后再次显示)
    func functionOfNgsjAppear(){
        
        if let result = functionOfExecJavaScript("ngsjAppear()"){
            self.functionArgument = result
            //self.functionOfRightButtonList(self.functionArgument)
            
        }
    }
    
    
    func functionOfExecJavaScript(js:String)->String?{
        //
        var rtn:String?
        
        self.webViewController.webbrower.evaluateJavaScript(js){
            (result,error)->Void in
            if error == nil{
                NSLog("webbrower evaluate javascript [\(js)] result:\(result)")
                rtn  = nil
            }
            else{

                NSLog("webbrower evalute javascript [\(js)] error:\(error)")
                rtn = result as? String
            }
        }
        return rtn
        
    }
    
    //全屏图片浏览
    func functionOfPictureView(imgurl:String){
        //
        let imgurls = NSMutableArray()
        imgurls.addObject(imgurl)
        
        
        let imgview = IOSImageViewController(nibName: "IOSImageViewController", bundle: nil,imgurls: imgurls)
        
        self.webViewController.presentViewController(imgview, animated: true, completion: { () -> Void in})
        
    }
    //设置小红点
    func functionOfSetBadge(badegParam:String){
        
    }
    //关闭当前view
    func functionOfCloseWebBrower(close:String){
        //pop 关闭
        self.webViewController.navigationController!.popViewControllerAnimated(true)
    }
    //拍照或选照片上传
    func functionOfChooseOrTakePhoto(callback:String){
        self.functionArgument = callback;
        let actionsheet = UIAlertController(title: "照片选择", message: nil, preferredStyle: .ActionSheet)
        actionsheet.addAction(UIAlertAction(title: "从相册选取照片", style: .Default, handler: { (_) -> Void in
            //
            let imagePicket = UIImagePickerController()
            imagePicket.delegate = self
            imagePicket.sourceType = .PhotoLibrary
            imagePicket.modalTransitionStyle = .CoverVertical
            imagePicket.allowsEditing = false
            self.webViewController.presentViewController(imagePicket, animated: true, completion: { () -> Void in
            })
        }))
        actionsheet.addAction(UIAlertAction(title: "相机拍摄照片", style: .Default, handler: { (_) -> Void in
            //
            let imagePicket = UIImagePickerController()
            imagePicket.delegate = self
            imagePicket.sourceType = .Camera
            imagePicket.modalTransitionStyle = .CoverVertical
            imagePicket.allowsEditing = true
            self.webViewController.presentViewController(imagePicket, animated: true, completion: { () -> Void in
            })
        }))
        
        
        actionsheet.addAction(UIAlertAction(title: "取消", style: .Cancel, handler: { (UIAlertAction) -> Void in}))
        
        self.webViewController.presentViewController(actionsheet, animated: true, completion: nil)
        
    }
    
    //打开新页面
    func functionOfNavToNewWebPage(newurl:String){
        
        let pageurl = "\(CONST_SRV_API_URL)/\(newurl)"
        NSLog("pageto:\(pageurl)")
        let navview = AccViewController(nibName:"AccViewController",bundle: nil,initWebUrl:pageurl)
        
        //push 新页面
        self.webViewController.navigationController!.pushViewController(navview, animated: true)
    }
    //扫描条码
    //func functionOfScanBarcode(callbackname:String){
    func functionOfScanBarcode(callback:String){
        NSLog("scanbarcode")
        self.functionArgument = callback
        let ui = UIStoryboard(name: "readbarcode", bundle: nil)
        let scan = ui.instantiateViewControllerWithIdentifier("ReadBarcodeViewController") as! ReadBarcodeViewController
        
        scan.callbackDelegate = self
        
        self.webViewController.presentViewController(scan, animated: true, completion: {()->Void in})
        
    }
    
    //打开消息view
    func functionOfOpenMessageView(){
        
    }
    
    
    //------------------------------------------------
    //functionOfScanBarcode ReadBarcodeViewDelegate
    
    func ReadBarcodeCallback(barcodeCodeValue: String) -> String {
        NSLog("barcode:\(barcodeCodeValue),\(self.functionArgument)")
        let js = "\(self.functionArgument)( \"\(barcodeCodeValue)\" )"
        
        NSLog("barcode:\(js)")
        self.webViewController.webbrower.evaluateJavaScript(js){(_,_)->Void in}
        
        return barcodeCodeValue
    }
    
    
    
    func imagePickerController(picker: UIImagePickerController, didFinishPickingImage image: UIImage, editingInfo: [String : AnyObject]?) {
        
        NSLog("picker \(image)")
        //
        let img = RESTHttpUtil.compressImage(image, maxWidthOrHeight: max(image.size.height, image.size.width))
        //post to srv
        let strJson = RESTHttpUtil.postImgSyncRequest(stringUrl: CONST_SRV_API_RESURL, postImage: img)
        
        NSLog("pick image \(strJson)")
        let fileurl = RESTHttpUtil.getJsonValueFromString(strJson, jsonkey: "url")!
        //callback
        let js = "\(self.functionArgument)(\"\(fileurl)\")"
        NSLog("pick image \(js)")
        self.webViewController.webbrower.evaluateJavaScript(js){(_,_)->Void in}
        
        //关闭view
        picker.dismissViewControllerAnimated(true) { () -> Void in}
        
        
        
        
    }
    func imagePickerControllerDidCancel(picker: UIImagePickerController) {
        NSLog("picker cancel")
        picker.dismissViewControllerAnimated(true) { () -> Void in}
    }
}
