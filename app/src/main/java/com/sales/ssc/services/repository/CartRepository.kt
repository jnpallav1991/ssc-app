package com.sales.ssc.services.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sales.ssc.AppApplication
import com.sales.ssc.response.*
import com.sales.ssc.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CartRepository {

    fun cartCheckout(productCheckoutRequest: ProductCheckoutRequest): MutableLiveData<ResponseSuccessfulBody> {
        val response = MutableLiveData<ResponseSuccessfulBody>()
        AppApplication.getApiClient().getRestInterface().checkoutProducts(productCheckoutRequest)
            .enqueue(object : Callback<ResponseSuccessfulBody> {
                override fun onResponse(call: Call<ResponseSuccessfulBody>, responseSsc: Response<ResponseSuccessfulBody>) {

                    if (responseSsc.isSuccessful) {
                        response.value = responseSsc.body()
                    }
                }

                override fun onFailure(call: Call<ResponseSuccessfulBody>, t: Throwable) {
                    response.value = null
                }
            })

        return response
    }

    fun returnProductCheckout(productReturnRequest: ProductReturnRequest): MutableLiveData<ResponseSuccessfulBody> {
        val response = MutableLiveData<ResponseSuccessfulBody>()
        AppApplication.getApiClient().getRestInterface().returnProducts(productReturnRequest)
            .enqueue(object : Callback<ResponseSuccessfulBody> {
                override fun onResponse(call: Call<ResponseSuccessfulBody>, responseSsc: Response<ResponseSuccessfulBody>) {

                    if (responseSsc.isSuccessful) {
                        response.value = responseSsc.body()
                    }
                }

                override fun onFailure(call: Call<ResponseSuccessfulBody>, t: Throwable) {
                    response.value = null
                }
            })
        return response
    }
}