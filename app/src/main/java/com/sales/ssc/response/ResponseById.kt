package com.sales.ssc.response

data class ResponseById(
    var Price: Double = 0.0,
    var ProductCode: String = "",
    var ProductName: String = "",
    var Quantity: Int = 0
)