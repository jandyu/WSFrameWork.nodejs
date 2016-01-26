//
//  IOSImageViewController.m
//  xxx
//
//  Created by Mac on 14-5-18.
//  Copyright (c) 2014年 cn.youjish. All rights reserved.
//

#import "IOSImageViewController.h"
#import "const.h"

@interface IOSImageViewController ()

@end

@implementation IOSImageViewController

@synthesize imgurls = _imgurls;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil imgurls:(NSMutableArray *) urls
{
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    self.imgurls = urls;
    return self;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    NSLog( @"img load");
   
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    //load first img
    //[self.view setMultipleTouchEnabled:YES];
    //[self.view setUserInteractionEnabled:YES];
    
    
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(closeself)];
    [self.view addGestureRecognizer:singleTap];
    
    //[self.imgBrower setMultipleTouchEnabled:YES];
    //[self.imgBrower setUserInteractionEnabled:YES];
    [self.view setMultipleTouchEnabled:YES];
    [self.view setUserInteractionEnabled:YES];
    // 缩放手势
    UIPinchGestureRecognizer *pinchGestureRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(pinchView:)];
    [self.view addGestureRecognizer:pinchGestureRecognizer];
    
    // 移动手势
    UIPanGestureRecognizer *panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panView:)];
    [self.view addGestureRecognizer:panGestureRecognizer];
    

    
    [self.progress startAnimating];
    NSString *url =  [[NSString alloc] initWithFormat:@"%@%@", CONST_SRV_API_URL,[self.imgurls objectAtIndex:0] ];
    NSLog(@"imgrul:%@",url);
    
    UIImage *img = [[ UIImage alloc]initWithData:
                    [NSData dataWithContentsOfURL:
                     [NSURL URLWithString:url ]]];
    [self.imgBrower setImage:img];
    //if(img.size.width > self.view.frame.size.width){
        
    //}
    float r=1.0;
    CGRect  ssize = [[UIScreen mainScreen] applicationFrame];
    if (ssize.size.width>img.size.width) {
        r=1.0;
    }
    else{
        r= ssize.size.width / img.size.width;
    }
    self.imgBrower.transform = CGAffineTransformScale(self.imgBrower.transform,r,r);
    //[self.imgBrower setFrame:CGRectMake(1, 1,[[[UIScreen mainScreen] applicationFrame] width]);
    [self.progress stopAnimating];

    
}
//move
- (void) panView:(UIPanGestureRecognizer *)panGestureRecognizer
{
    //UIView *view = panGestureRecognizer.view;
    UIView *view = self.imgBrower;
    if (panGestureRecognizer.state == UIGestureRecognizerStateBegan || panGestureRecognizer.state == UIGestureRecognizerStateChanged) {
        CGPoint translation = [panGestureRecognizer translationInView:view.superview];
        [view setCenter:(CGPoint){view.center.x + translation.x, view.center.y + translation.y}];
        [panGestureRecognizer setTranslation:CGPointZero inView:view.superview];
    }
    
}
//scale
- (void) pinchView:(UIPinchGestureRecognizer *)pinchGestureRecognizer
{
    //UIView *view = pinchGestureRecognizer.view;
        UIView *view = self.imgBrower;
    if (pinchGestureRecognizer.state == UIGestureRecognizerStateBegan || pinchGestureRecognizer.state == UIGestureRecognizerStateChanged) {
        view.transform = CGAffineTransformScale(view.transform, pinchGestureRecognizer.scale, pinchGestureRecognizer.scale);
        pinchGestureRecognizer.scale = 1;
    }
}
-(void) closeself
{
    [self dismissViewControllerAnimated:YES completion:^{}];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
