//
//  ViewController.swift
//  QRReaderDemo
//
//  Created by Simon Ng on 23/11/14.
//  Copyright (c) 2014 AppCoda. All rights reserved.
//

import UIKit
import AVFoundation


@objc class ReadBarcodeViewController: UIViewController,AVCaptureMetadataOutputObjectsDelegate {
    
    @IBOutlet weak var closeBtn: UIButton!
    @IBAction func closeTouch(sender: UIButton) {
        qrCodeFrameView?.frame = CGRectZero
        //captureSession?.startRunning()
        self.dismissViewControllerAnimated(true, completion: {})
    }
    @IBOutlet weak var messageLabel:UILabel!

    var captureSession :AVCaptureSession?
    var videoPreviewLayer:AVCaptureVideoPreviewLayer?
    var qrCodeFrameView:UIView?
    
    var callbackDelegate:ReadBarcodeViewDelegate!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //开始识别
        startCapture()
        //显示识别框
        qrCodeFrameSet()
    }
    
    func captureOutput(captureOutput: AVCaptureOutput!, didOutputMetadataObjects metadataObjects: [AnyObject]!, fromConnection connection: AVCaptureConnection!) {
        if metadataObjects == nil || metadataObjects.count == 0 {
            qrCodeFrameView?.frame = CGRectZero
            messageLabel.text = "没有条码被识别"
            return
        }
        NSLog("\(metadataObjects)")
        let metadataObj = metadataObjects[0] as! AVMetadataMachineReadableCodeObject
        
        switch metadataObj.type{
        case AVMetadataObjectTypeEAN13Code:
            fallthrough
        case AVMetadataObjectTypeQRCode:
            fallthrough
        default:
            let barcodeObject = videoPreviewLayer?.transformedMetadataObjectForMetadataObject(metadataObj) as! AVMetadataMachineReadableCodeObject
            qrCodeFrameView?.frame = barcodeObject.bounds
            if let code = metadataObj.stringValue {
                messageLabel.text = code
                captureSession?.stopRunning()
                
                callbackDelegate.ReadBarcodeCallback(code);
                self.dismissViewControllerAnimated(true, completion: {})
            }
        }
    }
    
    
    func startCapture(){
        //
        let captureDevice = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        
        do{
            let input =  try AVCaptureDeviceInput.init(device: captureDevice)
            
            captureSession = AVCaptureSession()
            
            captureSession?.addInput(input)
            
            let captureMetadataOutput = AVCaptureMetadataOutput()
            captureSession?.addOutput(captureMetadataOutput)
            
            captureMetadataOutput.setMetadataObjectsDelegate(self, queue: dispatch_get_main_queue())
            
            captureMetadataOutput.metadataObjectTypes=[AVMetadataObjectTypeQRCode,AVMetadataObjectTypeEAN13Code,AVMetadataObjectTypePDF417Code]
            
            videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
            videoPreviewLayer?.videoGravity = AVLayerVideoGravityResizeAspectFill
            videoPreviewLayer?.frame = view.layer.bounds
            view.layer.addSublayer(videoPreviewLayer!)
            
            captureSession?.startRunning()
            
            view.bringSubviewToFront(messageLabel)
            view.bringSubviewToFront(closeBtn)
            
        }
        catch let error as NSError{
            NSLog("some thing error. \(error.localizedDescription)")
            //print(error.localizedDescription)
        }
    }
    func qrCodeFrameSet(){
        qrCodeFrameView = UIView()
        qrCodeFrameView?.layer.borderColor = UIColor.greenColor().CGColor
        qrCodeFrameView?.layer.borderWidth = 2
        view.addSubview(qrCodeFrameView!)
        view.bringSubviewToFront(qrCodeFrameView!)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

