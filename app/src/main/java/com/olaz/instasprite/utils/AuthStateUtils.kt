package com.olaz.instasprite.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStateUtils(context: Context) {
    private val tokenUtils = TokenUtils(context)
    
    private val _isLoggedIn = MutableStateFlow(tokenUtils.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    fun getTokenManager(): TokenUtils = tokenUtils
}