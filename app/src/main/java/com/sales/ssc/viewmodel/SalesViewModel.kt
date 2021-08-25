package com.sales.ssc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchDate
import com.sales.ssc.services.repository.SalesRepository
import java.text.SimpleDateFormat
import java.util.*

class SalesViewModel : ViewModel() {

    private val cal = MutableLiveData<Calendar>()
    private var responseSales = MutableLiveData<ResponseSscBody>()
    private var responseReturnSales = MutableLiveData<ResponseSscBody>()
    private lateinit var selectedDate: String

    init {
        cal.value = Calendar.getInstance()
        selectedDate =
            SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.value!!.timeInMillis)
        responseSales = SalesRepository.getSalesList(SearchDate(selectedDate))
        responseReturnSales = SalesRepository.getReturnSalesList(SearchDate(selectedDate))
    }

    fun getSelectedDate(): LiveData<Calendar?> {
        return cal
    }

    fun setCalenderDate(calendar: Calendar) {

        cal.value = calendar
        selectedDate =
            SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.value!!.timeInMillis)
        getSalesResponse(false)
        getReturnSalesResponse(false)
    }

    fun getSalesResponse(callService: Boolean): LiveData<ResponseSscBody> {
        val selectedDate =
            SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.value!!.timeInMillis)
        if (callService) {

            responseSales = SalesRepository.getSalesList(SearchDate(selectedDate))
        } else {
            if (this.selectedDate != selectedDate) {
                responseSales = SalesRepository.getSalesList(SearchDate(selectedDate))
            }

        }
        return responseSales
    }

    fun getReturnSalesResponse(callService: Boolean): LiveData<ResponseSscBody> {
        val selectedDate =
            SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.value!!.timeInMillis)
        if (callService) {
            responseReturnSales = SalesRepository.getReturnSalesList(SearchDate(selectedDate))
        } else {
            if (this.selectedDate != selectedDate) {
                responseReturnSales = SalesRepository.getReturnSalesList(SearchDate(selectedDate))
            }

        }
        return responseReturnSales
    }
}