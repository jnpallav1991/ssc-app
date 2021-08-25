package com.sales.ssc.response

data class ProductCheckoutRequest(
    var ProductList : List<Product> = listOf(),
    var SellingDate : String = "",
    var SellingTime : String = "",
    var TotalPrice  : Double = 0.0,
    var TotalProductQuantity : Int = 0,
    var CustomerName : String?=null,
    var SalesPerson  : String?=null,
    var MobileNumber:String?=null,
    var City:String? = null
) {
    data class Product(
        var ProductCode : String = "",
        var Quantity : Int = 0,
        var SellingPrice : Double = 0.0
    )
}