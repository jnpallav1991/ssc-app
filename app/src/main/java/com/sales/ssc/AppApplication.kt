package com.sales.ssc

import android.app.Application
import android.content.Context
import com.sales.ssc.services.ApiClient
import com.sales.ssc.utils.LanguageHelper.getLanguage
import com.sales.ssc.utils.LanguageHelper.onAttach


class AppApplication : Application() {
    companion object {

        lateinit var appContext: Context
        fun getApiClient(): ApiClient {
            return ApiClient()
        }

    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        getApiClient()
    }

    // override the base context of application to update default locale for the application
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(onAttach(base!!, getLanguage(base)))
    }
}