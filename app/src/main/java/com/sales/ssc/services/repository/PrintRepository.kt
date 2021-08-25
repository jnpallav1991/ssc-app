package com.sales.ssc.services.repository

import androidx.lifecycle.MutableLiveData
import com.sales.ssc.AppApplication
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchDate
import com.sales.ssc.response.SearchId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PrintRepository {

    fun getSalesList(searchDate: SearchDate): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()
        AppApplication.getApiClient().getRestInterface().getSalesProductByDate(searchDate)
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

    fun getReturnSalesList(searchDate: SearchDate): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()
        AppApplication.getApiClient().getRestInterface().getSalesReturnByDate(searchDate)
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

    fun getSalesPrint(searchId: SearchId): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()
        AppApplication.getApiClient().getRestInterface().getSalesProductByID(searchId)
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

    fun getReturnSalesPrint(searchId: SearchId): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()
        AppApplication.getApiClient().getRestInterface().getSalesReturnByID(searchId)
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