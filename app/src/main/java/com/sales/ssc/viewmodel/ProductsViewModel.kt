package com.sales.ssc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sales.ssc.response.InsertProduct
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchProduct
import com.sales.ssc.response.SendProduct
import com.sales.ssc.services.repository.ProductRepository

class ProductsViewModel : ViewModel() {

    private var responseSearchProduct = MutableLiveData<ResponseSscBody>()
    private var responseScanProduct = MutableLiveData<ResponseSscBody>()
    private var insertProductMaster = MutableLiveData<ResponseSscBody>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun searchProduct(searchProduct: SearchProduct): LiveData<ResponseSscBody> {
        responseSearchProduct = ProductRepository.searchProduct(searchProduct)
        return responseSearchProduct

    }

    fun scanProduct(sendProduct: SendProduct): LiveData<ResponseSscBody> {
        responseScanProduct = ProductRepository.scanProduct(sendProduct)
        return responseScanProduct

    }

    fun insertProductMaster(insertProduct: InsertProduct): LiveData<ResponseSscBody> {
        insertProductMaster = ProductRepository.insertProductMaster(insertProduct)
        return insertProductMaster

    }
}