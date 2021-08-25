package com.sales.ssc.response

data class ResponseSscBody(
    var StatusCode: Int? = null,
    val Message: String? = null,
    val Data: ArrayList<Any?>? = null
)