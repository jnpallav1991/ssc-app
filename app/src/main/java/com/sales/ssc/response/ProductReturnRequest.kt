package com.sales.ssc.response

data class ProductReturnRequest(
    var ProductList: List<Product>? = null,
    var ReturningDate: String? = null,
    var ReturningTime: String? = null,
    var TotalPrice: Double? = null,
    var TotalProductQuantity: Int? = null,
    var CustomerName: String? = null,
    var SalesPerson: String? = null,
    var MobileNumber: String? = null,
    var City: String? = null
) {
    data class Product(
        var ProductCode: String? = null,
        var Quantity: Int? = null,
        var SellingPrice: Double? = null
    )
}