package com.sales.ssc.services.view

import com.sales.ssc.response.ResponseSuccessfulBody

interface CartView {

    fun showProgress()

    fun hideProgress()

    fun onFailure(message: String)

    fun onResponseCheckoutSuccess(responseSuccessfulBody: ResponseSuccessfulBody?)

    fun onResponseReturnSuccess(responseSuccessfulBody: ResponseSuccessfulBody?)
}