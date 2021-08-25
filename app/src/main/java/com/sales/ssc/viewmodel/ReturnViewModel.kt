package com.sales.ssc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sales.ssc.response.*
import com.sales.ssc.services.repository.CartRepository


class ReturnViewModel : ViewModel() {

    private val selectedProduct = MutableLiveData<MutableList<GetProducts>>()

    private var responseCartCheckout = MutableLiveData<ResponseSuccessfulBody>()
    private var responseReturnCheckout = MutableLiveData<ResponseSuccessfulBody>()

    init {
        selectedProduct.value = ArrayList()
    }

    fun responseCheckout(productCheckoutRequest: ProductCheckoutRequest): LiveData<ResponseSuccessfulBody> {
        responseCartCheckout = CartRepository.cartCheckout(productCheckoutRequest)
        return responseCartCheckout
    }

    fun responseReturnCheckout(productReturnRequest: ProductReturnRequest): LiveData<ResponseSuccessfulBody> {
        responseReturnCheckout = CartRepository.returnProductCheckout(productReturnRequest)
        return responseReturnCheckout
    }

    fun removeProduct(getProducts: GetProducts)
    {
        selectedProduct.value?.remove(getProducts)
        selectedProduct.value = selectedProduct.value
    }

    fun setProduct(getProducts: GetProducts)
    {
        if (selectedProduct.value!!.size!=0) {
            for (p in selectedProduct.value!!) {
                if (p.ProductCode == getProducts.ProductCode) {
                    p.selectedQuantity = p.selectedQuantity!! + getProducts.selectedQuantity!!
                    p.selectedPrice = getProducts.SellingPrice!! * p.selectedQuantity!!
                } else {
                    getProducts.selectedPrice =
                        getProducts.SellingPrice!! * getProducts.selectedQuantity!!
                    selectedProduct.value?.add(getProducts)
                    selectedProduct.value = selectedProduct.value
                }
            }
        }
        else
        {
            getProducts.selectedPrice =
                getProducts.SellingPrice!! * getProducts.selectedQuantity!!
            selectedProduct.value?.add(getProducts)
            selectedProduct.value = selectedProduct.value
        }
    }

    fun getProduct() : LiveData<MutableList<GetProducts>>
    {
        return selectedProduct
    }

    fun removeAll()
    {
        selectedProduct.value?.clear()
    }
}