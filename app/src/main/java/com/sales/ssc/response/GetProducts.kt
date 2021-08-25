package com.sales.ssc.response

import android.os.Parcelable
import androidx.lifecycle.Observer
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetProducts(
    var ID: Long? = null,
    var ProductCode: String? = null,
    var ProductName: String? = null,
    var PurchaseDate: String? = null,
    var PurchasePrice: Double? = null,
    var SellingPrice: Double? = null,
    var Quantity: Int? = null,
    var selectedQuantity:Int?=null,
    var selectedPrice:Double?=null
): Parcelable