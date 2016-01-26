//
//  MainTabBarController.swift
//  ktwy
//
//  Created by jrain on 15/12/23.
//  Copyright © 2015年 cn.youjish. All rights reserved.
//

import UIKit
import Foundation


@objc class AppMainFunc: NSObject {
    
    
    
    var tabBarController:UITabBarController!
    
    
    //install push message
    func PushMessageInstall()->Int{
        /*
        
        //JPUSh
        NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
        
        [defaultCenter addObserver:self selector:@selector(networkDidSetup:) name:kJPFNetworkDidSetupNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidClose:) name:kJPFNetworkDidCloseNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidRegister:) name:kJPFNetworkDidRegisterNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidLogin:) name:kJPFNetworkDidLoginNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kJPFNetworkDidReceiveMessageNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networeDidError) name:kJPFServiceErrorNotification object:nil];
        
        
        // ios 7 not here
        NSDictionary *notifymsg = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
        if (notifymsg) {
        NSLog(@"apn clicked");
        
        [APService handleRemoteNotification:notifymsg];
        }
        
        
        
        
        [APService  registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
        UIRemoteNotificationTypeSound |
        UIRemoteNotificationTypeAlert) categories:nil];
        //[APService registerForRemoteNotificationTypes:<#(NSUInteger)#> categories:<#(NSSet *)#>]
        [APService setupWithOption:launchOptions];
        
        
        if (self.USEINFO && [self.USEINFO compare:@""]!=NSOrderedSame) {
        //注入jpush
        NSLog(@"注入jpush");
        NSData *jsondata = [self.USEINFO dataUsingEncoding:NSUTF8StringEncoding];
        NSDictionary *js= [NSJSONSerialization JSONObjectWithData:jsondata options:NSJSONReadingMutableContainers error:nil];
        NSArray *ary = [[js objectForKey:@"unittitle"] componentsSeparatedByString:@","];
        //[APService set]
        NSLog(@"aps settags: %@,%@",ary,[OpenUDID value]);
        [APService setTags:[NSSet setWithArray:ary] alias:[OpenUDID value] callbackSelector:nil object:nil];
        
        }
        */
        return 0
    }
    
    //main nav
    func CreateMainTabBar()->UITabBarController{
        //初始化导航nav
        
        let views = [
            CONST_PAGETITLE_1:[CONST_PAGEURL_1,"mm1.png"],
            CONST_PAGETITLE_2:[CONST_PAGEURL_2,"mm2.png"],
            CONST_PAGETITLE_3:[CONST_PAGEURL_3,"mm3.png"],
            CONST_PAGETITLE_4:[CONST_PAGEURL_4,"mm4.png"],
            CONST_PAGETITLE_5:[CONST_PAGEURL_5,"mm5.png"]
        ]

        let viewArray = views.map({(title,setting)->UINavigationController in
            let navController = UINavigationController()

            let view = AccViewController(nibName:"AccViewController",bundle: nil,initWebUrl: setting[0])
            view.title = title

            navController.pushViewController(view, animated: true)
            
            //navController.navigationBarHidden = true
            //navController.navigationBar.barStyle = .BlackTranslucent
            
            
            let frame = navController.navigationBar.frame;
            let alphaView = UIView(frame: CGRectMake(0, 0, frame.size.width, frame.size.height+20))
            alphaView.backgroundColor = UIColor(colorLiteralRed: CONST_TITLE_COLOR_R  / 255.0, green: CONST_TITLE_COLOR_G / 255.0, blue: CONST_TITLE_COLOR_B / 255.0, alpha: CONST_TITLE_COLOR_A)
            
            navController.view.insertSubview(alphaView, belowSubview: navController.navigationBar)
            let bkimg = UIImage(named: "bigShadow.png")
            navController.navigationBar.setBackgroundImage(bkimg, forBarMetrics: .Compact)
            
            navController.navigationBar.layer.masksToBounds = true
            
            
            //navController.navigationBar.translucent = true
            navController.navigationBar.alpha = 0
            //navController.navigationBar.backgroundColor = UIColor.redColor()
            
            //navController.navigationBar.barStyle = .Black
            navController.navigationBar.backgroundColor = UIColor(colorLiteralRed: CONST_TITLE_COLOR_R / 255.0, green: CONST_TITLE_COLOR_G / 255.0, blue: CONST_TITLE_COLOR_B / 255.0, alpha: CONST_TITLE_COLOR_A)
//            navController.navigationBar.ti
            return navController
        })
        

        
        self.tabBarController = UITabBarController()
        //self.tabBarController.delegate = app
        self.tabBarController.viewControllers = viewArray
        tabBarController.tabBar.translucent = false
        tabBarController.tabBar.barTintColor = UIColor(colorLiteralRed: 246.0/255, green: 246.0/255, blue: 246.0/255, alpha: 1.0)
        tabBarController.tabBar.tintColor = UIColor(colorLiteralRed: 29.0/255, green: 155.0/255, blue: 219.0/255, alpha: 1.0)
        
        
        //启动
        return tabBarController
    }
}
