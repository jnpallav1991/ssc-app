package com.sales.ssc.response


data class InsertProduct(
    val ID: Long? = null,
    var ProductCode: String? = null,
    var ProductName: String? = null,
    var PurchaseDate: String? = null,
    var PurchasePrice: Double? = null,
    var SellingPrice: Double? = null,
    var Quantity: Int? = null
)