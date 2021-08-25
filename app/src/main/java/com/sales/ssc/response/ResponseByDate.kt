package com.sales.ssc.response

import android.os.Parcelable
import com.sales.ssc.utils.Constant
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ResponseByDate(
    var ID: Int? = null,
    var SellingDate: String? = null,
    var SellingTime: String? = null,
    var ReturningDate: String? = null,
    var ReturningTime: String? = null,
    var TotalPrice: Double? = null,
    var TotalProductQuantity: Int? = null,
    var CustomerName: String? = null,
    var MobileNumber: String? = null,
    var CollectionPerson: String? = null,
    var SalesPerson: String? = null,
    var City: String? = null,
    var isSalesResponse: Boolean? = null
) : Parcelable
{
    fun printSellingDate() :String{
        val format = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)
        val timeFormat = SimpleDateFormat(Constant.TIME_FORMAT_SERVER, Locale.ENGLISH)
        val sDate = format.parse(SellingDate)
        val sTime = timeFormat.parse(SellingTime)

        val printDateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)
        val printTimeFormat = SimpleDateFormat(Constant.TIME_FORMAT_12, Locale.ENGLISH)
       val pDate =  printDateFormat.format(sDate)
       val pTime =  printTimeFormat.format(sTime)
        return "$pDate - $pTime"
    }

    fun printReturningDate() :String{
        val format = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)
        val timeFormat = SimpleDateFormat(Constant.TIME_FORMAT_SERVER, Locale.ENGLISH)
        val sDate = format.parse(ReturningDate)
        val sTime = timeFormat.parse(ReturningTime)

        val printDateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)
        val printTimeFormat = SimpleDateFormat(Constant.TIME_FORMAT_12, Locale.ENGLISH)
        val pDate =  printDateFormat.format(sDate)
        val pTime =  printTimeFormat.format(sTime)
        return "$pDate - $pTime"
    }
}