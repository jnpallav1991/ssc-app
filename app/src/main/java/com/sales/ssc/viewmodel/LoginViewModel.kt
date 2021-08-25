package com.sales.ssc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sales.ssc.services.repository.SalesRepository
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchDate
import com.sales.ssc.response.SearchId
import com.sales.ssc.response.UserLogin
import com.sales.ssc.services.repository.LoginRepository
import com.sales.ssc.services.repository.PrintRepository
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel : ViewModel() {

    private var responseLogin = MutableLiveData<ResponseSscBody>()

    fun getUserLogin(userLogin: UserLogin): LiveData<ResponseSscBody> {

        responseLogin = LoginRepository.getLogin(userLogin)

        return responseLogin
    }

}