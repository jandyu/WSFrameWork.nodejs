//
//  ReadBarcodeViewDelegate.swift
//  ktwy
//
//  Created by jrain on 15/12/22.
//  Copyright © 2015年 cn.youjish. All rights reserved.
//

import Foundation


@objc protocol ReadBarcodeViewDelegate {
    func ReadBarcodeCallback(barcodeCodeValue:String)->String
}
