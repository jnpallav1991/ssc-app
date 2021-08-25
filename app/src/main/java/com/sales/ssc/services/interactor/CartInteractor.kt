package com.sales.ssc.services.interactor

import com.google.gson.Gson
import com.sales.ssc.AppApplication
import com.sales.ssc.response.*
import com.sales.ssc.services.presenter.CartPresenter
import com.sales.ssc.services.view.CartView
import com.sales.ssc.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartInteractor(private val cartView: CartView) : CartPresenter {

    override fun cartCheckout(productCheckoutRequest: ProductCheckoutRequest) {

        if (Constant.IS_NETWORK_AVAILABLE) {
            cartView.showProgress()

            AppApplication.getApiClient().getRestInterface().checkoutProducts(productCheckoutRequest)
                .enqueue(object : Callback<ResponseSuccessfulBody> {
                    override fun onResponse(call: Call<ResponseSuccessfulBody>, responseSsc: Response<ResponseSuccessfulBody>) {
                        cartView.hideProgress()
                        if (responseSsc.isSuccessful) {
                            cartView.onResponseCheckoutSuccess(responseSsc.body()!!)
                        } else {

                            var message: String = responseSsc.code().toString()

                            try {
                                val errorBody = responseSsc.errorBody()!!.string()
                                if (responseSsc.errorBody() != null) {
                                    val convertedObject =
                                        Gson().fromJson(errorBody, ErrorBody::class.java)
                                    message = convertedObject.error.message
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            cartView.onFailure(message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseSuccessfulBody>, t: Throwable) {
                        cartView.hideProgress()
                        if (t.cause.toString().contains(Constant.JAVA_NET_EXCEPTION)) {
                            cartView.onFailure(Constant.SERVER_NOT_RESPONDING)
                        } else {
                            cartView.onFailure(t.message!!)
                        }
                    }
                })
        } else {
            cartView.onFailure(Constant.NO_INTERNET)
        }
    }

    override fun returnProductCheckout(productReturnRequest: ProductReturnRequest) {
        if (Constant.IS_NETWORK_AVAILABLE) {
            cartView.showProgress()

            AppApplication.getApiClient().getRestInterface().returnProducts(productReturnRequest)
                .enqueue(object : Callback<ResponseSuccessfulBody> {
                    override fun onResponse(call: Call<ResponseSuccessfulBody>, responseSsc: Response<ResponseSuccessfulBody>) {
                        cartView.hideProgress()
                        if (responseSsc.isSuccessful) {
                            cartView.onResponseReturnSuccess(responseSsc.body()!!)
                        } else {

                            var message: String = responseSsc.code().toString()

                            try {
                                val errorBody = responseSsc.errorBody()!!.string()
                                if (responseSsc.errorBody() != null) {
                                    val convertedObject =
                                        Gson().fromJson(errorBody, ErrorBody::class.java)
                                    message = convertedObject.error.message
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            cartView.onFailure(message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseSuccessfulBody>, t: Throwable) {
                        cartView.hideProgress()
                        if (t.cause.toString().contains(Constant.JAVA_NET_EXCEPTION)) {
                            cartView.onFailure(Constant.SERVER_NOT_RESPONDING)
                        } else {
                            cartView.onFailure(t.message!!)
                        }
                    }
                })
        } else {
            cartView.onFailure(Constant.NO_INTERNET)
        }
    }
}