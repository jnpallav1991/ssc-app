package com.sales.ssc.utils


interface OnFragmentInteractionListener {

    fun signInSuccess()

    fun badgeCount(count:Int)

    fun scanProductFromSearch(productCode:String)

}