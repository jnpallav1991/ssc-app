package com.sales.ssc.ui.scan

import com.google.firebase.ml.vision.common.FirebaseVisionImage

interface LumaListener {
    fun scanBarcode(imageVision : FirebaseVisionImage)
}