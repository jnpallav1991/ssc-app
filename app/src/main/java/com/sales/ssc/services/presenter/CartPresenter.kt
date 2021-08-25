package com.sales.ssc.services.presenter

import com.sales.ssc.response.ProductCheckoutRequest
import com.sales.ssc.response.ProductReturnRequest
import com.sales.ssc.response.SearchProduct
import com.sales.ssc.response.SendProduct

interface CartPresenter {

    fun cartCheckout(productCheckoutRequest: ProductCheckoutRequest)

    fun returnProductCheckout(productReturnRequest: ProductReturnRequest)
}