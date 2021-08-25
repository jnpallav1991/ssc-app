package com.sales.ssc.response

data class ResponseSuccessfulBody(
    var StatusCode: Int? = null,
    val Message: String? = null,
    val Data: Any? = null
)