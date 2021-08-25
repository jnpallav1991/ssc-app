package com.sales.ssc.services.repository

import androidx.lifecycle.MutableLiveData
import com.sales.ssc.AppApplication
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchDate
import com.sales.ssc.response.SearchId
import com.sales.ssc.response.UserLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginRepository {

    fun getLogin(userLogin: UserLogin): MutableLiveData<ResponseSscBody> {
        val response = MutableLiveData<ResponseSscBody>()
        AppApplication.getApiClient().getRestInterface().userLogin(userLogin)
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