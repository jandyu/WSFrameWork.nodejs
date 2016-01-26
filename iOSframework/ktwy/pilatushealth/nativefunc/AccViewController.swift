
//
//  AccViewController.swift
//  navt
//
//  Created by jrain on 15/12/30.
//  Copyright © 2015年 jrain. All rights reserved.
//

import UIKit
import WebKit

class AccViewController: UIViewController ,WKNavigationDelegate,WKUIDelegate,WKScriptMessageHandler,SRRefreshDelegate,UIScrollViewDelegate {

    var webbrower:WKWebView!
    var progressView:UIProgressView!
    var refreshView:SRRefreshView!
    var nativeFunction:NativeFunctionProxy!
    var startDrag:Bool!
    var currUrl:String!
    
    init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle? ,initWebUrl url:String){
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        currUrl = url
        startDrag = false
        
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nativeFunction = NativeFunctionProxy(webViewController: self)

        // Do any additional setup after loading the view.
        
        //注入hybridpage.js
        let cfg = WKWebViewConfiguration()
        cfg.preferences.javaScriptEnabled = true
        cfg.preferences.javaScriptCanOpenWindowsAutomatically = false
        
        cfg.userContentController = WKUserContentController()
        
        do
        {
            let filePath = NSBundle.mainBundle().pathForResource("hybridPage", ofType: "js")
            let regjs = try NSString(contentsOfFile: filePath!, encoding: NSUTF8StringEncoding)
            let script = WKUserScript(source: regjs as String, injectionTime: .AtDocumentEnd, forMainFrameOnly: true)
            cfg.userContentController.addUserScript(script)
        }
        catch let error as NSError{
            NSLog("registe hybridpage error.\(error.localizedDescription)")
            
        }
        //注册消息通道  ngsj
        cfg.userContentController.addScriptMessageHandler(self, name: "ngsj")
        
        
        //web brower
        self.webbrower = WKWebView(frame: self.view.frame, configuration: cfg)
        self.webbrower.scrollView.delegate = self
        self.webbrower.navigationDelegate = self
        self.webbrower.UIDelegate = self
        self.webbrower.scrollView.backgroundColor = UIColor.grayColor()
        
        //下拉刷新动画
        NSLog("did load:\(self.view.frame)")
        self.refreshView = SRRefreshView(frame: CGRectMake(0,-44,self.view.frame.width,44))
        self.refreshView.upInset = 0
        self.refreshView.delegate = self
        self.refreshView.slimeMissWhenGoingBack = true
        
        self.webbrower.scrollView.addSubview(self.refreshView)
        
        self.webbrower.loadRequest(NSURLRequest(URL: NSURL(string: currUrl)!))
        self.view.addSubview(self.webbrower)
        self.webbrower.translatesAutoresizingMaskIntoConstraints = false
        //self.view.sendSubviewToBack(self.wk)
        
        self.view.addConstraint(NSLayoutConstraint(item: self.webbrower, attribute: .Left, relatedBy: .Equal, toItem: self.view, attribute: .Left, multiplier: 1, constant: 0))
        self.view.addConstraint(NSLayoutConstraint(item: self.webbrower, attribute: .Right, relatedBy: .Equal, toItem: self.view, attribute: .Right, multiplier: 1, constant: 0))
        self.view.addConstraint(NSLayoutConstraint(item: self.webbrower, attribute: .Top, relatedBy: .Equal, toItem: self.view, attribute: .Top, multiplier: 1, constant: 0))
        self.view.addConstraint(NSLayoutConstraint(item: self.webbrower, attribute: .Bottom, relatedBy: .Equal, toItem: self.bottomLayoutGuide, attribute: .Bottom, multiplier: 1, constant: 0))
        
        
        
        
        let f  = (self.navigationController?.navigationBar.frame.height)! + (self.navigationController?.navigationBar.frame.origin.y)!
        
        progressView = UIProgressView(frame: CGRectMake(0,f,self.view.frame.width,20))
        progressView.progressViewStyle = .Default
        progressView.progress = 0.05
        
        self.view.addSubview(progressView)
        
        
        // 监听支持KVO的属性
        self.webbrower.addObserver(self, forKeyPath: "loading", options: .New, context: nil)
        self.webbrower.addObserver(self, forKeyPath: "title", options: .New, context: nil)
        self.webbrower.addObserver(self, forKeyPath: "estimatedProgress", options: .New, context: nil)
        
        self.webbrower.scrollView.showsHorizontalScrollIndicator = false
        self.webbrower.scrollView.showsVerticalScrollIndicator = true
        
        
        //
        //self.navController.navigationBarHidden = true
        //navController.navigationBar.barStyle = .BlackTranslucent
        //navController.navigationBar.translucent = false
        //navController.navigationBar.alpha = 1
        //self.navigationController?.navigationBar.backgroundColor = UIColor.redColor()
        //reset frame when appear
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        NSLog("appear:\(self.view.frame)")
        
        //reset frame when appear
        
        self.webbrower.frame = self.view.frame
        let h = (self.navigationController?.navigationBar.frame.height)! + (self.navigationController?.navigationBar.frame.origin.y)!
        NSLog("navibar height:\(h)")
        self.refreshView.frame = CGRectMake(0, -h, self.view.frame.width, h)
        
        self.refreshView.upInset = h

        self.refreshView.loading = false

        if(!self.webbrower.loading){
            
            nativeFunction.functionOfNgsjAppear()
            
        }
    }
    
    func userContentController(userContentController: WKUserContentController, didReceiveScriptMessage message: WKScriptMessage) {
        //recieve message from javascript
        print(message.body)
        let p = message.body as! Dictionary<String,String>
        nativeFunction.ExecuteFunctionByJsMessage(jsMessage: p)
    }
    
    func scrollViewWillBeginDecelerating(scrollView: UIScrollView) {
        NSLog("scroll begin decleer:\(scrollView.contentOffset)")
    }
    
    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        NSLog("scrollBeginDragging:\(scrollView.contentOffset)")
        self.startDrag = true
    }
    
    func scrollViewDidScroll(scrollView: UIScrollView) {
        if (self.startDrag == true) {
            //NSLog("scroll:\(scrollView.contentOffset)")
         
            refreshView.scrollViewDidScroll()
        }
    }
    
    func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        NSLog("scroll did end")
        self.startDrag = false
        refreshView.scrollViewDidEndDraging()
    }
    
    override func observeValueForKeyPath(keyPath: String?, ofObject object: AnyObject?, change: [String : AnyObject]?, context: UnsafeMutablePointer<Void>) {
        if keyPath == "loading" {
            NSLog("loading")
        } else if keyPath == "title" {
            self.title = self.webbrower.title
        } else if keyPath == "estimatedProgress" {
            NSLog("progress:\(webbrower.estimatedProgress)")
            self.progressView.setProgress(Float(webbrower.estimatedProgress), animated: true)
        }
        
        // 已经完成加载
        if !webbrower.loading {
            // 手动调用JS代码
            let js = "ngsjLoad()";
            self.webbrower.evaluateJavaScript(js) {
                (result,error)->Void in
                if error != nil{
                    NSLog("evaluate ngsjload result:\(result)")
                }
                else{
                    NSLog("call ngsgload:\(result)")
                    if result != nil{
                    self.nativeFunction.functionArgument = result as! String
                    self.nativeFunction.functionOfRightButtonList(self.nativeFunction.functionArgument)
                    }
                }
            }
            
            UIView.animateWithDuration(0.55, animations: { () -> Void in
                self.progressView.alpha = 0.0;
            })
            refreshView.performSelector(Selector("endRefresh"), withObject: nil, afterDelay: 1)
        }
    }
}

private typealias wkRefreshDelegate = AccViewController
extension wkRefreshDelegate{
    func slimeRefreshStartRefresh(refreshView: SRRefreshView!) {
        NSLog("reload-------------")
        
        self.progressView.progress = 0.05
        self.progressView.alpha = 1.0

        nativeFunction.functionOfReloadPage()
        
        //self.webbrower.reload()

//        
//        self.webbrower.evaluateJavaScript("reloadpage()"){
//            (result,error)->Void in
//            if error == nil{
//                NSLog("evaluate javascript result:\(result)")
//            }
//            else{
//                NSLog("evalute javascript error:\(error)")
//            }
//        }
        
        //self.webbrower.reload()
    }
}

//javascript alert ,confirm , prompt
private typealias wkUIDelegate = AccViewController
extension wkUIDelegate{
    //alert
    func webView(webView: WKWebView, runJavaScriptAlertPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: () -> Void) {
        let ac = UIAlertController(title: self.webbrower.title, message: message, preferredStyle: UIAlertControllerStyle.Alert)
        ac.addAction(UIAlertAction(title: "确 定", style: UIAlertActionStyle.Cancel, handler: {(n)->Void in
            //关闭
            completionHandler()}))
        self.presentViewController(ac, animated: true, completion: nil)
    }
    //confirm
    func webView(webView: WKWebView, runJavaScriptConfirmPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: (Bool) -> Void) {
        let alert = UIAlertController(title: self.webbrower.title, message: message, preferredStyle: .Alert)
        alert.addAction(UIAlertAction(title: "确 定", style: .Default, handler: { (_) -> Void in
            // 返回 true
            completionHandler(true)
        }))
        alert.addAction(UIAlertAction(title: "取 消", style: .Cancel, handler: { (_) -> Void in
            // 返回 false
            completionHandler(false)
        }))
        
        self.presentViewController(alert, animated: true, completion: nil)
    }
    //prompt
    func webView(webView: WKWebView, runJavaScriptTextInputPanelWithPrompt prompt: String, defaultText: String?, initiatedByFrame frame: WKFrameInfo, completionHandler: (String?) -> Void) {
        let alert = UIAlertController(title: prompt, message: defaultText, preferredStyle: .Alert)
        
        alert.addTextFieldWithConfigurationHandler { (textField: UITextField) -> Void in
            textField.textColor = UIColor.blackColor()
        }
        alert.addAction(UIAlertAction(title: "确 定", style: .Default, handler: { (_) -> Void in
            // 返回输入值
            completionHandler(alert.textFields![0].text!)
        }))
        
        self.presentViewController(alert, animated: true, completion: nil)
    }
}

private typealias wkNavigationDelegate = AccViewController
extension wkNavigationDelegate {
    
    func webView(webView: WKWebView, didFailNavigation navigation: WKNavigation!, withError error: NSError) {
        NSLog(error.debugDescription)
    }
    func webView(webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: NSError) {
        NSLog(error.debugDescription)
    }
    
    func webView(webView: WKWebView, didFinishNavigation navigation: WKNavigation!) {
        NSLog("load finish")
        //refreshView.performSelector(Selector("endRefresh"), withObject: nil, afterDelay: 1)
        //refreshView.endRefresh()
    }
    

}