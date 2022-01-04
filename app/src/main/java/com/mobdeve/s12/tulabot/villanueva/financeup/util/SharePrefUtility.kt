package com.mobdeve.s12.tulabot.villanueva.financeup.util

import android.content.Context
import android.content.SharedPreferences

class SharePrefUtility {

    private var appPreferences: SharedPreferences? = null
    private val PREFS = "appPreferences"

    constructor(context: Context) {
        appPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun saveStringPreferences(key: String?, value: String?) {
        val prefsEditor = appPreferences!!.edit()
        prefsEditor.putString(key, value).commit()
    }

    fun saveBoolPreferences(key: String?, value: Boolean) {
        val prefsEditor = appPreferences!!.edit()
        prefsEditor.putBoolean(key, value).commit()
    }

    fun saveIntegerPreferences(key: String?, value: Int){
        val prefsEditor = appPreferences!!.edit()
        prefsEditor.putInt(key, value).commit()
    }

    fun getStringPreferences(key: String?): String?
    = appPreferences!!.getString(key, "Nothing Saved")

    fun getBoolPreferences(key: String?): Boolean
    = appPreferences!!.getBoolean(key, false)

    fun getIntegerPreferences(key: String?): Int
    = appPreferences!!.getInt(key, 0)

    fun removeAllPreferences() {
        appPreferences!!.edit().clear().commit()
    }
}