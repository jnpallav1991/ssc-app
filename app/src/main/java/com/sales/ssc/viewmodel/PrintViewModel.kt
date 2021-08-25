package com.sales.ssc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sales.ssc.services.repository.SalesRepository
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchDate
import com.sales.ssc.response.SearchId
import com.sales.ssc.services.repository.PrintRepository
import java.text.SimpleDateFormat
import java.util.*

class PrintViewModel : ViewModel() {

    private var responsePrintSales = MutableLiveData<ResponseSscBody>()

    init {

       // responseReturnSales = SalesRepository.getReturnSalesList(SearchDate(selectedDate))
    }


    fun getPrintSalesResponse(searchId: SearchId): LiveData<ResponseSscBody> {

        responsePrintSales = PrintRepository.getSalesPrint(searchId)

        return responsePrintSales
    }

    fun getPrintReturnSalesResponse(searchId: SearchId): LiveData<ResponseSscBody> {

        responsePrintSales = PrintRepository.getReturnSalesPrint(searchId)

        return responsePrintSales
    }
}