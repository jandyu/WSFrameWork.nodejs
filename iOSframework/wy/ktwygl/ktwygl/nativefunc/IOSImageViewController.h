//
//  IOSImageViewController.h
//  xxx
//
//  Created by Mac on 14-5-18.
//  Copyright (c) 2014年 cn.youjish. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface IOSImageViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *imgBrower;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *progress;

@property(weak,nonatomic) NSMutableArray *imgurls;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil imgurls:(NSMutableArray*) imgurls;

@end
