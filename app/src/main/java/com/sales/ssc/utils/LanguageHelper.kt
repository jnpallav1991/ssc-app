package com.sales.ssc.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.*

object LanguageHelper {

     // returns Context having application default locale for all activities
    fun onAttach(context: Context): Context {
        val lang =
            getPersistedData(context, Locale.getDefault().language)
        return setLanguage(context, lang)
    }

    // sets application locale with default locale persisted in preference manager on each new launch of application and
    // returns Context having application default locale
    fun onAttach(
        context: Context,
        defaultLanguage: String?
    ): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLanguage(context, lang)
    }

    // returns language code persisted in preference manager
    fun getLanguage(context: Context?): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    // persists new language code change in preference manager and updates application default locale
    // returns Context having application default locale
    fun setLanguage(
        context: Context,
        language: String?
    ): Context {
        SharedPreferenceUtil(context).setString("SET_LANGUAGE",language!!)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    // returns language code persisted in preference manager
    fun getPersistedData(
        context: Context?,
        defaultLanguage: String?
    ): String? {
        val language  =SharedPreferenceUtil(context!!).getString("SET_LANGUAGE","")
        if (!language.isNullOrEmpty())
        {
            return language
        }
        else {
            return defaultLanguage
        }
    }


    // For android device versions above Nougat (7.0)
    // updates application default locale configurations and
    // returns new Context object for the current Context but whose resources are adjusted to match the given Configuration
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(
        context: Context,
        language: String?
    ): Context {
        val locale = Locale(language)
        val configuration =
            context.resources.configuration
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    // For android device versions below Nougat (7.0)
    // updates application default locale configurations and
    // returns new Context object for the current Context but whose resources are adjusted to match the given Configuration
    private fun updateResourcesLegacy(
        context: Context,
        language: String?
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}