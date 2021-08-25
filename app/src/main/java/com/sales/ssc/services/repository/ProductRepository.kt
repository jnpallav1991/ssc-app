package com.sales.ssc.services.repository

import androidx.lifecycle.MutableLiveData
import com.sales.ssc.AppApplication
import com.sales.ssc.response.InsertProduct
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchProduct
import com.sales.ssc.response.SendProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {

    fun searchProduct(searchProduct: SearchProduct): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()

        AppApplication.getApiClient().getRestInterface().searchProduct(searchProduct)
            .enqueue(object : Callback<ResponseSscBody> {

                override fun onResponse(
                    call: Call<ResponseSscBody>,
                    responseSsc: Response<ResponseSscBody>
                ) {

                    if (responseSsc.isSuccessful) {
                        response.value = responseSsc.body()
                    }

                }

                override fun onFailure(call: Call<ResponseSscBody>, t: Throwable) {

                    response.value = null
                }
            })
        return response

    }

    fun scanProduct(sendProduct: SendProduct): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()

        AppApplication.getApiClient().getRestInterface().scanProductByQrCode(sendProduct)
            .enqueue(object : Callback<ResponseSscBody> {

                override fun onResponse(
                    call: Call<ResponseSscBody>,
                    responseSsc: Response<ResponseSscBody>
                ) {

                    if (responseSsc.isSuccessful) {
                        response.value = responseSsc.body()
                    }

                }

                override fun onFailure(call: Call<ResponseSscBody>, t: Throwable) {
                    response.value = null
                }
            })
        return response

    }

    fun insertProductMaster(insertProduct: InsertProduct): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()

        AppApplication.getApiClient().getRestInterface().insertProductMaster(insertProduct)
            .enqueue(object : Callback<ResponseSscBody> {

                override fun onResponse(
                    call: Call<ResponseSscBody>,
                    responseSsc: Response<ResponseSscBody>
                ) {

                    if (responseSsc.isSuccessful) {
                        response.value = responseSsc.body()
                    }
                }

                override fun onFailure(call: Call<ResponseSscBody>, t: Throwable) {

                    response.value = null
                }
            })
        return response

    }
}