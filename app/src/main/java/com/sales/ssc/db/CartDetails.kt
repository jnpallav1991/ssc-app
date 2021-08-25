package com.sales.ssc.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity
class CartDetails : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "pCode")
    var pCode: String? = null

    @ColumnInfo(name = "pName")
    var pName: String? = null

    @ColumnInfo(name = "pPrice")
    var pPrice: Double? = null

    @ColumnInfo(name = "pTPrice")
    var pTotalPrice: Double? = null

    @ColumnInfo(name = "pQuantity")
    var pQuantity: Int? = null

    @ColumnInfo(name = "pTQuantity")
    var pTQuantity: Int? = null

}