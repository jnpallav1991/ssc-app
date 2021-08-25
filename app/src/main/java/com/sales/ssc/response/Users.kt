package com.sales.ssc.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    var id: String? = "",
    var name: String? = "",
    var isAdmin: String? = "",
    var mobileNumber: String? = "",
    var email: String? = ""
): Parcelable