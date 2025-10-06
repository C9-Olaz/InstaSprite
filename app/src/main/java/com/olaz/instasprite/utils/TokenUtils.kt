package com.olaz.instasprite.utils

import android.content.Context
import android.content.SharedPreferences

class TokenUtils(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "instasprite_auth"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_TYPE = "token_type"
    }
    
    fun saveTokens(accessToken: String, refreshToken: String, tokenType: String = "Bearer") {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_TOKEN_TYPE, tokenType)
            apply()
        }
    }
    
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }
    
    fun getTokenType(): String {
        return prefs.getString(KEY_TOKEN_TYPE, "Bearer") ?: "Bearer"
    }
    
    fun getAuthorizationHeader(): String? {
        val token = getAccessToken()
        val type = getTokenType()
        return if (token != null) "$type $token" else null
    }
    
    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
    
    fun clearTokens() {
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_TOKEN_TYPE)
            apply()
        }
    }
}