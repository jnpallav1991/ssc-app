package com.sales.ssc.response

data class ErrorBody(
    var error: Error = Error()
) {
    data class Error(
        var details: List<Detail> = listOf(),
        var message: String = "",
        var statusCode: Int = 0
    ) {
        data class Detail(
            var message: String = "",
            var statusCode: Int = 0
        )
    }
}