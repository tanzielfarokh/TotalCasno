package com.totalcasno.games.njskn.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "game_preferences"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_SAVED_LINK = "saved_link"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveToken(context: Context, token: String) {
        getPreferences(context).edit().putString(KEY_AUTH_TOKEN, token).apply()
    }
    
    fun getToken(context: Context): String? {
        return getPreferences(context).getString(KEY_AUTH_TOKEN, null)
    }
    
    fun saveLink(context: Context, link: String) {
        getPreferences(context).edit().putString(KEY_SAVED_LINK, link).apply()
    }
    
    fun getLink(context: Context): String? {
        return getPreferences(context).getString(KEY_SAVED_LINK, null)
    }
    
    fun hasToken(context: Context): Boolean {
        return getToken(context) != null
    }
    
    fun clearAll(context: Context) {
        getPreferences(context).edit().clear().apply()
    }
}

